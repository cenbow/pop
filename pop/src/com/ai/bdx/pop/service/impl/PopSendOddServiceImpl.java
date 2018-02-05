package com.ai.bdx.pop.service.impl;

import it.unimi.dsi.fastutil.bytes.Byte2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.shorts.Short2ObjectOpenHashMap;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import net.sf.json.JSONObject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.tools.ant.util.DateUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.util.CollectionUtils;

import com.ai.bdx.pop.bean.AvoidCustBean;
import com.ai.bdx.pop.bean.PopPolicyBaseinfoBean;
import com.ai.bdx.pop.bean.PopPolicyRuleSendBean;
import com.ai.bdx.pop.bean.PopTaskBean;
import com.ai.bdx.pop.enums.CustGroupType;
import com.ai.bdx.pop.enums.PolicyStatus;
import com.ai.bdx.pop.exception.PopException;
import com.ai.bdx.pop.kafka.CepMessageReceiverThread;
import com.ai.bdx.pop.kafka.CepReceiveThreadCache;
import com.ai.bdx.pop.model.PopPolicyBaseinfo;
import com.ai.bdx.pop.model.PopPolicyBaseinfoFiles;
import com.ai.bdx.pop.phonefilter.ICheckPhoneExistFilter;
import com.ai.bdx.pop.phonefilter.IPopAvoidBotherFilter;
import com.ai.bdx.pop.service.IPopSendOddService;
import com.ai.bdx.pop.util.CepUtil;
import com.ai.bdx.pop.util.DataSourceFactory;
import com.ai.bdx.pop.util.ErrorCodeMap;
import com.ai.bdx.pop.util.PopConfigure;
import com.ai.bdx.pop.util.PopConstant;
import com.ai.bdx.pop.util.PopTaskCache;
import com.ai.bdx.pop.util.PopUtil;
import com.ai.bdx.pop.util.SimpleCache;
import com.ai.bdx.pop.util.SpringContext;
import com.ai.bdx.pop.util.ftp.ApacheFtpUtil;
import com.ai.bdx.pop.util.ftp.FtpConfig;
import com.ai.bdx.pop.util.ftp.FtpConfigure;
import com.ai.bdx.pop.wsclient.ICmCustomersWsClient;
import com.ai.bdx.pop.wsclient.ISendPccInfoService;
import com.ai.bdx.pop.wsclient.PopWsclientFactory;
import com.ai.bdx.pop.wsclient.ResultModel;
import com.asiainfo.biframe.utils.config.Configure;
import com.asiainfo.biframe.utils.date.DateUtil;
import com.asiainfo.biframe.utils.string.DES;
import com.asiainfo.biframe.utils.string.StringUtil;
import com.google.common.base.Strings;
import com.jfinal.plugin.activerecord.Db;
/***
 * @author liyz
 * 派单相关实现
 * 
 * */
public class PopSendOddServiceImpl implements IPopSendOddService {
	private static Logger log = LogManager.getLogger(PopSendOddServiceImpl.class);
	
	
	private IPopAvoidBotherFilter popAvoidBotherFilterImpl;
	
	private JdbcTemplate jdbcTemplate;
	
	private ICheckPhoneExistFilter checkPhoneExistFilter;
	
	private static String productFileTime="";

	/**
	 * 从CM获取客户群
	 * @return 返回客户群bitSetMap
	 * @param custId
	 * **/
	private Byte2ObjectOpenHashMap<Short2ObjectOpenHashMap<BitSet>> getCustGroup(String custId,String custType,String execDate) {
		String msgCode = "";
		JSONObject ciCustGroup = null;
		long t1 = System.currentTimeMillis();
		String custListViewName;
		Byte2ObjectOpenHashMap<Short2ObjectOpenHashMap<BitSet>> baseBitMap;
		// step1:获取客户群信息
		try {
			msgCode = "1001";
			ciCustGroup = getCmCustGroupArray(custId,custType,execDate);
			custListViewName = (String) ciCustGroup.get("target_customers_tab_name");
			if (StringUtil.isEmpty(custListViewName)) {
				throw new PopException("从CM获取用户table表为null,custId:" + custId+ "..!");
			}
		} catch (Exception e) {
			throw new PopException(e+",出错步骤:"+msgCode);
		}
		// step2:从CM加载客户群清单到MAP(bitSet)
		try {
			msgCode = "1002";
			baseBitMap = loadCustDataFromCm(custListViewName);
		} catch (Exception e) {
			throw new PopException(e+",出错步骤:"+msgCode);
		}
		long t2 = System.currentTimeMillis();
		long totalTime = (t2 - t1) / 1000;
		log.debug("从CM获取客户群,并生成基础BitMAP-custID{},共计:{}秒",custId,totalTime);
		return baseBitMap;
	}


	/**
	 * 获取免打扰之后的客户群 和 剔除的客户群
	 * @param avoidTypeId 免打扰类型id
	 * @param map 原始客户群
	 * @return 	AvoidCustBean
	 * **/
	private AvoidCustBean getAvoidAfterCustAndDelCust(Byte2ObjectOpenHashMap<Short2ObjectOpenHashMap<BitSet>> map,String avoidTypeIds) {
		AvoidCustBean acb = null;
		try {
			acb  = popAvoidBotherFilterImpl.allowPassFilterBatch(avoidTypeIds, map);
		}catch (Exception e) {
			throw new PopException("获取免打扰之后的客户群 和 剔除的客户群出错!"+e);
		}
		return acb;
	}

	/**
	 * 通过表名从CM取表数据到bitMap
	 * 
	 * @param tableName
	 * **/
	public Byte2ObjectOpenHashMap<Short2ObjectOpenHashMap<BitSet>> loadCustDataFromCm(String tableName) {
		JdbcTemplate jt = null;
		StringBuffer sql = new StringBuffer();
		int count=0;
		final Byte2ObjectOpenHashMap<Short2ObjectOpenHashMap<BitSet>> data = new Byte2ObjectOpenHashMap<Short2ObjectOpenHashMap<BitSet>>(1);
		//add by jinl 20150821 武汉现场COC数据源为 phone_no ，增加参数CM_PRI_KEY_COLUMN判断,pop.properties从配置文件获取
		String CM_PRI_KEY_COLUMN = PopConfigure.getInstance().getProperty("CM_PRI_KEY_COLUMN");
		System.out.println("==============CM_PRI_KEY_COLUMN==========="+CM_PRI_KEY_COLUMN);//客户群字段改为phone_no
//		sql.append("select SUBSTR(product_no,2,2) v1,SUBSTR(product_no,4,4) v2,SUBSTR(product_no,8,4) v3 " )
//			.append(" from ")
//			.append(tableName).append(" tmp where product_no is not null ");
		sql.append("select SUBSTR(phone_no,2,2) v1,SUBSTR(phone_no,4,4) v2,SUBSTR(phone_no,8,4) v3 " )
		.append(" from ")
		.append(tableName).append(" tmp where phone_no is not null ");
		try {
			String jndiCiBack = Configure.getInstance().getProperty("JDBC_CM");
			DataSource ds = DataSourceFactory.getDataSource(jndiCiBack);
			jt = new JdbcTemplate(ds);
			count = jt.query(sql.toString(), new RowMapper() {
					public Object mapRow(ResultSet resultset, int i) throws SQLException {
						try {
							//组装成MAP
							String v11=resultset.getString("v1");
							String v22=resultset.getString("v2");
							String v33=resultset.getString("v3");
							byte v1 =Byte.valueOf(v11);
							short v2 = Short.parseShort(v22);
							short v3 =Short.parseShort(v33);
							Short2ObjectOpenHashMap<BitSet> h2 = data.get(v1);
							if (h2 == null) {
								h2 = new Short2ObjectOpenHashMap<BitSet>();
								data.put(v1, h2);
							}
							BitSet h3 = h2.get(v2);
							if (h3 == null) {
								h3 = new BitSet(10000);
							}
							h3.set(v3);
							h2.put(v2, h3);
 
						} catch (Exception e) {
							log.warn("忽略非法号码：" + resultset.getString("PRODUCT_NO"), e);
						}
						return null;
					}
				  }).size();

			log.info("从CM获取客户群:"+tableName+",总计:"+count);
		} catch (Exception e) {
			log.error("从CM通过表名取表出错!", e);
			throw new PopException("从CM通过表名取表出错!" + e.getMessage());
		}

		return data;
	}

	
	/**
	 * 从CM获取客户群对象（表名）
	 * 
	 * @param custId
	 * @param custType
	 * **/
	public JSONObject getCmCustGroupArray(String custId,String custType,String execDate) {
		JSONObject ciCustGroup = null;
		try {
			log.debug("获取客户群信息开始,custId:{}...", custId);
			ICmCustomersWsClient cmClient = PopWsclientFactory.getCmCustomersWsClient();
			String ciCustGroupArray="";
			Calendar cal = Calendar.getInstance();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Date exDate = sdf.parse(execDate);
			cal.setTime(exDate);
			if (CustGroupType.MONTHLY.equals(custType)) {
				cal.add(Calendar.MONTH, -1);
				SimpleDateFormat df = new SimpleDateFormat("yyyyMM");
				String date = df.format(cal.getTime());
				ciCustGroupArray = cmClient.getTargetCustomersCycleObj(custId, date);
			} else if (CustGroupType.DAILY.equals(custType)) {
				cal.add(Calendar.DAY_OF_MONTH, -1);
				SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
				String date = df.format(cal.getTime());
				ciCustGroupArray = cmClient.getTargetCustomersCycleObj(custId, date);
			} else {
				ciCustGroupArray = cmClient.getTargetCustomersObj(custId);
			}
			ciCustGroup = JSONObject.fromObject(ciCustGroupArray);
			log.debug("获取客户群信息成功,strResult:{}", ciCustGroupArray);
		} catch (Exception e) {
			log.error("调用WS获取CM客户群信息出错!", e);
			throw new PopException("调用WS获取CM客户群信息出错!" + e.getMessage());
		}
		return ciCustGroup;
	}
	

	public IPopAvoidBotherFilter getPopAvoidBotherFilterImpl() {
		return popAvoidBotherFilterImpl;
	}


	public void setPopAvoidBotherFilterImpl(IPopAvoidBotherFilter popAvoidBotherFilterImpl) {
		this.popAvoidBotherFilterImpl = popAvoidBotherFilterImpl;
	}

	/**
	 * 获取免打扰之后的客户群 和 剔除的客户群
	 * @param avoidTypeId 免打扰类型id
	 * @param custId 客户群id
	 * @return 	avoidAfterCust --免打扰后客户群
				delCust--删除客户群
	 * */
	@Override
	public AvoidCustBean getAvoidAfterCustAndDelCust(String custId, String avoidTypeId,String custType,String execDate) {
		Byte2ObjectOpenHashMap<Short2ObjectOpenHashMap<BitSet>> custData = getCustGroup(custId,custType,execDate);
		AvoidCustBean acb= getAvoidAfterCustAndDelCust(custData, avoidTypeId);
		return acb;
	}


	@Override
	public boolean createPolicyRuleTask(String policyId) {
		boolean flag = true;
		String msgCode="";
		String policyTaskTab ="";
		//回滚用
		PopPolicyBaseinfoBean popPolicyBaseinfoBean = new PopPolicyBaseinfoBean();
		try{
		//step.1 通过policyId 查询 -策略基本信息bean
		msgCode="POP1001";
		PopPolicyBaseinfo popPolicyBaseinfo = (PopPolicyBaseinfo) PopPolicyBaseinfo.dao().findById(policyId);
		popPolicyBaseinfoBean.setPolicyId(policyId);
		//step.2判断任务表名是否是空,空则建表
		policyTaskTab = popPolicyBaseinfo.getStr(popPolicyBaseinfo.COL_POLICY_TASK_TAB);
		msgCode="POP1002";
		if(!Strings.isNullOrEmpty(policyTaskTab)){
			//已经建过此表
			//拿到所有规则的任务,从缓存中移除
			String sql = "select id from "+policyTaskTab ;
			List<Map> policyTaskList  = jdbcTemplate.queryForList(sql);
			for(Map m:policyTaskList){
				String id = (String) m.get("id");
				PopTaskBean popTaskBean = new PopTaskBean();
				popTaskBean.setTaskId(id);
				PopTaskCache.getInstance().removeTask(popTaskBean);
			}
		} 
		//建表
		//drop table
		this.dropTable(PopConstant.POP_TASK_PREFIX+popPolicyBaseinfo.getStr(popPolicyBaseinfo.COL_ID));
		policyTaskTab = this.createCustTab(PopConstant.POP_TASK_PREFIX,popPolicyBaseinfo.getStr(popPolicyBaseinfo.COL_ID),PopConstant.POP_TASK_TABLE);
		popPolicyBaseinfoBean.setPolicyTaskTab(policyTaskTab);
		//step.3通过policyId查询所有的规则
		msgCode="POP1003";
		StringBuffer sql =new StringBuffer("SELECT ");
	    sql.append(" t1.pccid,t1.id, t1.pre_send_data_tab,t1.send_data_tab,");
		sql.append(" t0.start_time,t0.end_time,t2.date_ranges ,");
		sql.append(" t3.custgroup_type,t3.custgroup_id, ");
		sql.append(" t4.cep_rule_id, t4.simple_condtions_data ,");
		sql.append(" t5.avoid_custgroup_ids ");
		sql.append(" FROM  POP_POLICY_BASEINFO t0 join ");
		sql.append(" POP_POLICY_RULE t1  on t0.id = t1.policy_id").append(" and t1.policy_id = '"+policyId+"'");
		sql.append(" LEFT JOIN ");
		sql.append(" POP_POLICY_RULE_EXEC_TIME t2 ");
		sql.append(" ON ");
		sql.append(" t1.id = t2.policy_rule_id ");
		sql.append(" LEFT JOIN ");
		sql.append(" POP_POLICY_RULE_CUSTGROUP t3 ");
		sql.append(" ON ");
		sql.append(" t1.id = t3.policy_rule_id ");
		sql.append(" LEFT JOIN ");
		sql.append(" POP_POLICY_RULE_EVENT_CON t4 ");
		sql.append(" ON ");
		sql.append(" t1.id = t4.policy_rule_id  ");	 
		sql.append(" LEFT JOIN ");
		sql.append(" POP_POLICY_RULE_ACT t5 ");
		sql.append(" ON ");
		sql.append(" t1.id = t5.policy_rule_id  ");	 
		
		log.debug("public boolean createPolicyRuleTask:sql"+sql.toString());
		List<Map> policyTaskList  = jdbcTemplate.queryForList(sql.toString());
		//step.4任务拆分
		msgCode="POP1004";
		List<PopTaskBean> todayTaskList = new ArrayList<PopTaskBean>();
		
		if(CollectionUtils.isEmpty(policyTaskList)){
			throw  new PopException("没有查询到相关策略规则,请检查!策略id:"+policyId);
		}
		for(Map m :policyTaskList){
			Date beginDate =(Date)m.get("start_time");
			Date endDate =(Date)m.get("end_time");
			String ruleId = (String)m.get("id");
			String custgroup_type =(String)m.get("custgroup_type");
			String cepRuleId = (String)m.get("cep_rule_id");
			String custGoupId = (String)m.get("custgroup_id");
			String avoidCustGroupIds = (String)m.get("avoid_custgroup_ids");
			String rule_pre_send_data_tab = (String)m.get("pre_send_data_tab");
			String rule_send_data_tab = (String)m.get("send_data_tab");
			String dateRanges  = (String)m.get("date_ranges");
			String simpleCondtionsData  = (String)m.get("simple_condtions_data");
			String pccid = (String)m.get("pccid");
			if(!Strings.isNullOrEmpty(custGoupId) && Strings.isNullOrEmpty(rule_pre_send_data_tab)){
				//如果custId不为空才建表
				try{
				   rule_pre_send_data_tab = this.createCustTab(PopConstant.POP_RULE_PRE_SEND_PREFIX,ruleId,PopConstant.POP_USER_YYYYMMDDHHMMSSSSS);
				}catch(Exception e){
					log.warn(e);
				}finally{
					rule_pre_send_data_tab = PopConstant.POP_RULE_PRE_SEND_PREFIX+ruleId;
				}
			}
			if((!Strings.isNullOrEmpty(custGoupId) ||!Strings.isNullOrEmpty(cepRuleId)) &&Strings.isNullOrEmpty(rule_send_data_tab)){
				try{
				  rule_send_data_tab = this.createCustTab(PopConstant.POP_RULE_SEND_PREFIX,ruleId,PopConstant.POP_USER_YYYYMMDDHHMMSSSSS);
				}catch(Exception e){
					log.warn(e);
			  }finally{
				  rule_send_data_tab = PopConstant.POP_RULE_SEND_PREFIX+ruleId;
			  }
			}
			
			boolean isContainCep = false;
			if(!Strings.isNullOrEmpty(cepRuleId)){
				isContainCep = true;
			}
			boolean isContainSimpleCon = false;
			if(!Strings.isNullOrEmpty(simpleCondtionsData)){
				isContainSimpleCon = true;
			}
			
			//记录规则信息,回滚用
			Map<String, PopPolicyRuleSendBean> popPolicyRuleMap = popPolicyBaseinfoBean.getPopPolicyRuleMap();
			if(popPolicyRuleMap.get(ruleId) == null){
				PopPolicyRuleSendBean  popPolicyRuleSendBean = new PopPolicyRuleSendBean();
				popPolicyRuleSendBean.setRuleId(ruleId);
				popPolicyRuleSendBean.setRulePreSendDataTab(rule_pre_send_data_tab);
				popPolicyRuleSendBean.setRuleSendDataTab(rule_send_data_tab);
				popPolicyRuleMap.put(ruleId, popPolicyRuleSendBean);
			};
			
			//获取执行时间段
			String today = PopUtil.sendOddformat.format(new Date());
			//拆分时间
			List<Map<String,String>>  processDay = this.processDay(PopUtil.sendOddformat.format(beginDate),PopUtil.sendOddformat.format(endDate),dateRanges,custgroup_type,isContainCep,isContainSimpleCon);
			for(Map<String,String> map :processDay){
				String day = map.get("date");
				boolean isValid = Boolean.parseBoolean(map.get("isValid"));
				String insertSql = "insert into "+policyTaskTab + " (id,rule_id,exec_date,exec_status,pre_send_data_tab,send_data_tab,reject_data_tab) values(?,?,?,?,?,?,?)";
				final PopTaskBean popTaskBean = new PopTaskBean();
				String taskId =PopUtil.generateId();
				popTaskBean.setPolicyId(policyId);
				popTaskBean.setPccid(pccid);
				popTaskBean.setPolicyTaskTab(policyTaskTab);
				popTaskBean.setTaskId(taskId);
				popTaskBean.setCepRuleId(cepRuleId);
				popTaskBean.setExecDate(day);
				popTaskBean.setCustGroupId(custGoupId);
				popTaskBean.setAvoidCustGroupIds(avoidCustGroupIds);
				popTaskBean.setRulePreSendDataTab(rule_pre_send_data_tab);
				popTaskBean.setRuleSendDataTab(rule_send_data_tab);
				if(isValid == true){
					popTaskBean.setExecStatus(PopConstant.TASK_STATUS_DDPD);
				}else{
					//失效
					popTaskBean.setExecStatus(PopConstant.TASK_STATUS_PDSX);
					popTaskBean.setExecInfo("派单时间："+day+",起止时间："+beginDate+"-"+endDate+"，已过期....");
				}
				popTaskBean.setRuleId(ruleId);
				//pre_send_data_tab
				String preSendDataTab = null;
				if(!Strings.isNullOrEmpty(custGoupId)){
					preSendDataTab = this.createCustTab(PopConstant.POP_PRE_SEND_PREFIX,popTaskBean.getTaskId(),PopConstant.POP_USER_YYYYMMDDHHMMSSSSS);
					popTaskBean.setPreSendDataTab(preSendDataTab);
				}
				//send_data_tab
				String sendDataTab = null;
				if(!Strings.isNullOrEmpty(custGoupId) ||!Strings.isNullOrEmpty(cepRuleId)){
					sendDataTab = this.createCustTab(PopConstant.POP_SEND_PREFIX,popTaskBean.getTaskId(),PopConstant.POP_USER_YYYYMMDDHHMMSSSSS);
					popTaskBean.setSendDataTab(sendDataTab);
				}
				//reject_data_tab
				final String rejectTab = this.createCustTab(PopConstant.POP_REJECT_PREFIX,popTaskBean.getTaskId(),PopConstant.POP_REJECT_PREFIX_TABLE);
				popTaskBean.setRejectDataTab(rejectTab);
				jdbcTemplate.update(insertSql.toString(),new Object[]{popTaskBean.getTaskId(),popTaskBean.getRuleId(),popTaskBean.getExecDate(),popTaskBean.getExecStatus(),preSendDataTab,sendDataTab,rejectTab});
				
				if(today.equals(day) && isValid == true){
					todayTaskList.add(popTaskBean);
				}
				
				//记录已拆分任务,回滚用
				Map<String,PopTaskBean> popTaskMap = popPolicyBaseinfoBean.getPopPolicyRuleMap().get(ruleId).getPopTaskMap();
				if(popTaskMap.get(taskId) == null){
					PopTaskBean  popTaskBeanTmp = new PopTaskBean();
					popTaskBeanTmp.setTaskId(taskId);
					popTaskBeanTmp.setPreSendDataTab(preSendDataTab);
					popTaskBeanTmp.setSendDataTab(sendDataTab);
					popTaskBeanTmp.setRejectDataTab(rejectTab);
					popTaskMap.put(taskId, popTaskBeanTmp);
				}
			}
			
			//更新规则汇总表
			String updateSql1  = "update POP_POLICY_RULE set pre_send_data_tab = ?,send_data_tab = ? where id = ? ";
			jdbcTemplate.update(updateSql1,new Object[]{rule_pre_send_data_tab,rule_send_data_tab,ruleId});
			PopPolicyRuleSendBean popPolicyRuleSendBean = popPolicyRuleMap.get(ruleId);
			popPolicyRuleSendBean.setRulePreSendDataTab(rule_pre_send_data_tab);
			popPolicyRuleSendBean.setRuleSendDataTab(rule_send_data_tab);
		}
	 
		//放入待执行缓存
		for(PopTaskBean popTaskBean:todayTaskList){
			PopTaskCache.getInstance().putTask(popTaskBean);
		}
		msgCode="POP4020";
		//modify by jinlong 20150825  sendPccInfoService.sendPccInfo(pccScriptInfos);为人工传，派单这里不需要这段代码，已注释
//		log.debug("向PCC派发脚本文件开始");
//		ISendPccInfoService sendPccInfoService = SpringContext.getBean("sendPccInfoService",ISendPccInfoService.class);
//		List<PopPolicyBaseinfoFiles> files = PopPolicyBaseinfoFiles.dao().search(PopPolicyBaseinfoFiles.COL_POLICY_ID,policyId,"order by  "+PopPolicyBaseinfoFiles.COL_MANUFACTURERS);
//		Map<String,String> pccScriptInfos = new HashMap<String,String>();
//		for(PopPolicyBaseinfoFiles file:files){
//			pccScriptInfos.put(file.getStr(PopPolicyBaseinfoFiles.COL_MANUFACTURERS), file.getStr(PopPolicyBaseinfoFiles.COL_FILEPATH));
//		}
//		ResultModel result  = sendPccInfoService.sendPccInfo(pccScriptInfos);
//		if("1".equals(result.getResultCode())){//派发失败
//			throw new PopException(result.getMessage());
//		}
//		log.debug("向PCC派发脚本文件结束");
		}catch(Exception e){
			flag = false;
			log.error(ErrorCodeMap.getMsg(msgCode).replace("{0}", policyId), e);
			throw  new PopException(ErrorCodeMap.getMsg(msgCode).replace("{0}", policyId)+":"+e);
		}finally{
			updateAssignTaskRelated(popPolicyBaseinfoBean,flag);
		}
		return flag;
	}
	
	private void updateAssignTaskRelated(PopPolicyBaseinfoBean popPolicyBaseinfoBean,boolean flag) {
		if(flag){
			//任务拆分成功，更新相关状态 ,policyTaskTab  状态
			String updateSql1  = "update POP_POLICY_BASEINFO set policy_task_tab = ?,policy_status_id = ? where id = ? ";
			jdbcTemplate.update(updateSql1,new Object[]{popPolicyBaseinfoBean.getPolicyTaskTab(),PolicyStatus.SENDODER_BASE_SUCCESS.getValue(),popPolicyBaseinfoBean.getPolicyId()});
			//更新rule状态
			String updateSql2  = "update POP_POLICY_RULE set rule_status = ? where policy_id = ? ";
			jdbcTemplate.update(updateSql2,new Object[]{PolicyStatus.SENDODER_BASE_SUCCESS.getValue(),popPolicyBaseinfoBean.getPolicyId()});
		}else{
			//拆分失败，回滚
			log.debug("+++++++++++策略id:"+popPolicyBaseinfoBean.getPolicyId()+"执行派单任务拆分抱错,开始回滚..+++++++++++++");
			try {
				this.dropTable(popPolicyBaseinfoBean.getPolicyTaskTab());
				log.debug("+++++++++++drop-table:"+popPolicyBaseinfoBean.getPolicyTaskTab()+"成功!+++++++++++++");
			} catch (Exception e) {
				e.printStackTrace();
			}
			//更新policyTaskTab , 状态
			String updateSql1  = "update POP_POLICY_BASEINFO set policy_task_tab = ?,policy_status_id = ? where id = ? ";
			jdbcTemplate.update(updateSql1,new Object[]{"",PolicyStatus.SENDODER_BASE_ERROR.getValue(),popPolicyBaseinfoBean.getPolicyId()});
			Map<String,PopPolicyRuleSendBean> popPolicyRuleMap  = popPolicyBaseinfoBean.getPopPolicyRuleMap();
			
			Iterator<String> popPolicyRuleMapIter = popPolicyRuleMap.keySet().iterator();
			while (popPolicyRuleMapIter.hasNext()) {
				String ruleId = popPolicyRuleMapIter.next();
				log.debug("+++++++++++回滚规则开始...ruleId:"+ruleId+"+++++++++++++");
				PopPolicyRuleSendBean popPolicyRuleSendBean = popPolicyRuleMap.get(ruleId);
				try {
					if(popPolicyRuleSendBean != null ){
						if(!Strings.isNullOrEmpty(popPolicyRuleSendBean.getRulePreSendDataTab())){
							this.dropTable(popPolicyRuleSendBean.getRulePreSendDataTab());
							log.debug("+++++++++++drop-table:"+popPolicyRuleSendBean.getRulePreSendDataTab()+"成功!+++++++++++++");
						}
						if(!Strings.isNullOrEmpty(popPolicyRuleSendBean.getRuleSendDataTab())){
							this.dropTable(popPolicyRuleSendBean.getRuleSendDataTab());
							log.debug("+++++++++++drop-table:"+popPolicyRuleSendBean.getRuleSendDataTab()+"成功!+++++++++++++");
						}
						//update POP_POLICY_RULE 
						String updateSql2 ="update POP_POLICY_RULE set pre_send_data_tab='',send_data_tab='',rule_status=? where id = ?";
						jdbcTemplate.update(updateSql2,new Object[]{PolicyStatus.SENDODER_BASE_ERROR.getValue(),ruleId});
						
						Map<String,PopTaskBean> popTaskMap = popPolicyRuleSendBean.getPopTaskMap();
						Iterator<String> popTaskMapIter = popTaskMap.keySet().iterator();
						while (popTaskMapIter.hasNext()) {
							String taskId = popTaskMapIter.next();
							log.debug("+++++++++++回滚规则开始...ruleId:"+taskId+"+++++++++++++");
							PopTaskBean popTaskBean=popTaskMap.get(taskId);
							if(popTaskBean != null){
								String preSendDataTab = popTaskBean.getPreSendDataTab();
								if(!Strings.isNullOrEmpty(preSendDataTab)){
									this.dropTable(preSendDataTab);
									log.debug("+++++++++++drop-table:"+preSendDataTab+"成功!+++++++++++++");
								}
								String rejectDataTab = popTaskBean.getRejectDataTab();
								if(!Strings.isNullOrEmpty(rejectDataTab)){
									this.dropTable(rejectDataTab);
									log.debug("+++++++++++drop-table:"+rejectDataTab+"成功!+++++++++++++");
								}
								String sendDataTab = popTaskBean.getSendDataTab();
								if(!Strings.isNullOrEmpty(sendDataTab)){
									this.dropTable(sendDataTab);
								log.debug("+++++++++++drop-table:"+sendDataTab+"成功!+++++++++++++");
								}
							}
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			}
			
		}
	}




	/**
	 * 任务拆分
	 * 客户群,简单事件,复杂事件,至少有其一
	 * @param policyBeginDateStr 策略开始时间
	 * @param policyEndDateStr 策略结束时间
	 * @param dateRanges 规则执行时间范围
	 * @param custgroup_type 客户群类型
	 * @param isContainCep 是否包含cep
	 * @param isContainSimpleCon 是否包含简单事件
	 * @return Map<时间,是否过期>
	 * @throws ParseException 
	 * */
	private List<Map<String,String>> processDay(String policyBeginDateStr, String policyEndDateStr,String dateRanges,String custgroup_type,boolean isContainCep,boolean isContainSimpleCon) throws ParseException {
		Date today = new Date();
		String todayStr = PopUtil.sendOddformat.format(today);
		today = PopUtil.sendOddformat.parse(todayStr);
		Date policyBeginDate =  PopUtil.sendOddformat.parse(policyBeginDateStr);
		Date policyEndDate = PopUtil.sendOddformat.parse(policyEndDateStr);
		
		//根据规则执行时间范围进行 时间 拆分
		List<String> ruleExeDayList = PopUtil.getExeDayList(dateRanges);
		List<Map<String,String>> resultList  = new ArrayList<Map<String,String>>();
		
		//根据客户群,复杂事件,简单时间 按时间进行任务拆分
		if(isContainCep){
			//如果包含cep,按照 ruleExeDayList 进行拆分
			for(String dateStr:ruleExeDayList){
				//判断当前时间是否在周期内
				Date date = PopUtil.sendOddformat.parse(dateStr);
				if(today.getTime()<= date.getTime()){
					Map<String,String> map = new HashMap<String,String>();
					//如果当前时间<=执行时间 
					map.put("isValid", "true");
					map.put("date", dateStr);
					resultList.add(map);
				} 
			}
			if(CollectionUtils.isEmpty(resultList)){
				//如果没有可执行的日期即当前规则已经过期
				Map<String,String> resultMap = new HashMap<String,String>();
				resultMap.put("isValid", "false");
				resultMap.put("date", todayStr);
				resultList.add(resultMap);
			}
			return resultList;
			
		}else if(!Strings.isNullOrEmpty(custgroup_type)){
			//如果不包含复杂事件,但是包含客户群
			if(CustGroupType.NONE.getValue().equals(custgroup_type)){
			//非周期性,立即执行，根据当前时间进行派单，需要判断当前时间 是否在policyBeginDate-policyEndDate 之间。
				Map<String,String> resultMap = new HashMap<String,String>();
				//判断当前时间是否在起止时间内
				if(today.getTime()<=policyBeginDate.getTime() || (policyBeginDate.getTime()<today.getTime() && today.getTime()<=policyEndDate.getTime())){
					//如果当前时间小于等于开始时间或 当前时间在起止时间内, 那么执行时间为当前日
					resultMap.put("isValid", "true");
				}else{
					resultMap.put("isValid", "false");
					log.warn("当前派单时间:"+todayStr+"--起止时间:"+policyBeginDateStr+",endDateStr"+".超过时间范围..客户群类型:"+custgroup_type);
			      } 
				resultMap.put("date", todayStr);
				resultList.add(resultMap);
				return resultList;
			}else if(CustGroupType.DAILY.getValue().equals(custgroup_type)){
			//日周期：根据日执行周期执行，时间控制在	ruleExeDayList，每天都派
				for(String dateStr:ruleExeDayList){
					//判断当前时间是否在周期内
					Date date = PopUtil.sendOddformat.parse(dateStr);
					Map<String,String> map = new HashMap<String,String>();
					if(today.getTime()<= date.getTime()){
						//如果当前时间<=执行时间 
						map.put("isValid", "true");
					}else{
						//相反 则当前时间>执行时间 
						map.put("isValid", "false");
						log.warn("当前派单时间:"+todayStr+"--起止时间:"+policyBeginDateStr+",endDateStr"+".超过时间范围..客户群类型:"+custgroup_type);
					}
					map.put("date", dateStr);
					resultList.add(map);
				}
				return resultList;
				
			}else if(CustGroupType.MONTHLY.getValue().equals(custgroup_type)){
			//月周期，根据月周期执行时间	派单，时间控制在ruleExeDayList，不是每天都派
				SimpleDateFormat sdf = new SimpleDateFormat("d");
				List<String> list = new ArrayList<String>();
				String cmMonthCycle = PopConfigure.getInstance().getProperty("CM_CUST_MONTH_CYCLE");
				for(String str:ruleExeDayList){
		 			Date date = sdf.parse(str);
		 			if(sdf.format(date).equals(cmMonthCycle)){
		 				list.add(str);
		 			}
		 		}
			   if(CollectionUtils.isEmpty(list)){
				    //如果是null,判断当前时间是否在起止日期内,如果在则当前时间 执行派单
					Map<String,String> resultMap = new HashMap<String,String>();
					//判断当前时间是否在起止时间内
					if(today.getTime()<=policyBeginDate.getTime() || (policyBeginDate.getTime()<today.getTime() && today.getTime()<=policyEndDate.getTime())){
						//如果当前时间小于等于开始时间或 当前时间在起止时间内, 那么执行时间为当前日
						resultMap.put("isValid", "true");
					}else{
						resultMap.put("isValid", "false");
						log.warn("当前派单时间:"+todayStr+"--起止时间:"+policyBeginDateStr+",endDateStr"+".超过时间范围..客户群类型:"+custgroup_type);
				      } 
					resultMap.put("date", todayStr);
					resultList.add(resultMap);
					return resultList;
			   }else{
				   //根据当前时间判断 ,是否有失效时间
				    for(String dateStr:list){
				    	Map<String,String> map = new HashMap<String,String>();
				    	Date date = PopUtil.sendOddformat.parse(dateStr);
						if(today.getTime()<= date.getTime()){
							//如果当前时间<=执行时间 
							map.put("isValid", "true");
						}else{
							//相反 则当前时间>执行时间 
							map.put("isValid", "false");
						}
						map.put("date", dateStr);
						resultList.add(map);
				    }
				   //判断当前时间是否在 起止时间内
				    if(today.getTime()<=policyBeginDate.getTime() || (policyBeginDate.getTime()<today.getTime() && today.getTime()<=policyEndDate.getTime())){
						//如果当前时间小于等于开始时间或 当前时间在起止时间内, 那么执行时间为当前日，月周期立即执行
				    	Map<String,String> map = new HashMap<String,String>();
				    	map.put("isValid", "true");
				    	map.put("date", todayStr);
				    	resultList.add(map);
					}
				    return resultList;
			   }
			}
			
		}else if(isContainSimpleCon){
			//如果复杂事件，客户群 都是空，简单规则不为空，根据当前时间进行派单，需要判断当前时间 是否在policyBeginDate-policyEndDate 之间。
			Map<String,String> resultMap = new HashMap<String,String>();
			//判断当前时间是否在起止时间内
			if(today.getTime()<=policyBeginDate.getTime() || (policyBeginDate.getTime()<today.getTime() && today.getTime()<=policyEndDate.getTime())){
				//如果当前时间小于等于开始时间或 当前时间在起止时间内, 那么执行时间为当前日
				resultMap.put("isValid", "true");
			}else{
				resultMap.put("isValid", "false");
				log.warn("当前派单时间:"+todayStr+"--起止时间:"+policyBeginDateStr+",endDateStr"+".超过时间范围..客户群类型:"+custgroup_type);
		      } 
			resultMap.put("date", todayStr);
			resultList.add(resultMap);
			return resultList;
		}else{
			throw new PopException("即不包含复杂事件，也不包含简单事件，客户群类型又是空，无法进行任务拆分！请检查规则信息是否完整！");
		}
		return resultList;
		 
	}


	/**
	 * 根据模板表创建清单表
	 * @param tabPrefix
	 * @return
	 */
	private String createCustTab(String tabPrefix,String id,String templateTableName) throws Exception {
		String tabName = tabPrefix + id;
		jdbcTemplate.execute(PopConstant.getSqlCreateAsTable(tabName, templateTableName));
		return tabName;
	}


	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}


	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	public void dropTable(String tabName) throws Exception {
		if (StringUtil.isNotEmpty(tabName)) {
			try {
				jdbcTemplate.execute(new StringBuilder("drop table ").append(tabName).toString());
			} catch (Exception e) {
				log.warn("drop table error:" + e.getMessage());
			}
		}
	}


	@Override
	public void synchronizeCustMapToDb(Byte2ObjectOpenHashMap<Short2ObjectOpenHashMap<BitSet>> custMap,String tabName,boolean needCheckExists) throws Exception {
		Thread t = this.createSynchronizeThread(custMap, tabName, needCheckExists);
		t.setName("synchronizeCustMapToDb[" + tabName + "]");
		t.run();//暂不进行多线程处理。
	}
	
	
	/**
	 * 创建一个同步MAP用户清单数据到数据库表的线程
	 * @param custMap
	 * @param tabName
	 * @param needCheckExists是否需要检查存在
	 * @return
	 */
	private Thread createSynchronizeThread(final Byte2ObjectOpenHashMap<Short2ObjectOpenHashMap<BitSet>> custMap,
			final String tabName, final boolean needCheckExists) {
			return new Thread() {
				@Override
				public void run() {
					try {
						long t1 = System.currentTimeMillis();
						StringBuffer insertSql = null;
						insertSql = new StringBuffer("insert into ").append(tabName).append(" (PRODUCT_NO) values(?)");
						if (null != custMap) {
							jdbcTemplate.execute(insertSql.toString(), new PreparedStatementCallback() {
								@Override
								public Object doInPreparedStatement(PreparedStatement preparedstatement)
										throws SQLException, DataAccessException {
									int total = 0;
									//遍历数据集MAP
									for (Map.Entry<Byte, Short2ObjectOpenHashMap<BitSet>> e1 : custMap.entrySet()) {
										String phoneSeg1 = "1" + e1.getKey();
										for (Map.Entry<Short, BitSet> e2 : e1.getValue().entrySet()) {
											String phoneSeg2 = PopUtil.formatPhoneNo(e2.getKey());

											BitSet e3 = e2.getValue();
											for (int i = e3.nextSetBit(0); i != -1; i = e3.nextSetBit(i + 1)) {
												String phoneSeg3 = PopUtil.formatPhoneNo((short) i);
												String phoneNumber = phoneSeg1 + phoneSeg2 + phoneSeg3;

												++total;
												if (needCheckExists) { //需要检查存在，先检查存在再入库
													// 数据库检查方式太慢
													//												List cs = jdbcTemplate.queryForList("SELECT FEEDBACK_STATUS FROM "+tabName+" WHERE PRODUCT_NO = ?" ,new String[] { phoneNumber });
													//												if (CollectionUtils.isEmpty(cs)) {
													if (!doCheckExist(tabName, phoneNumber)) {
														batchUpdate(preparedstatement, phoneNumber, total, 5000);
													}
												} else { //不需要检查存在，直接入库
													batchUpdate(preparedstatement, phoneNumber, total, 5000);
												}

											}
										}
									}
									//防止遗漏数据，再执行一遍
									preparedstatement.executeBatch();
									log.info("同步数据到{}的总数:{} 条", tabName, total);
									//入库完成,删除号码检查缓存中对应表数据(不要忘记，防止缓存数据过大)
									if (needCheckExists) {
										checkPhoneExistFilter.removeDataByKey(tabName);
									}
									return null;
								}

							});
						}
						log.info("同步数据到{}共耗时:{}ms", tabName, (System.currentTimeMillis() - t1));
					} catch (Exception e) {
						log.error("同步数据到" + tabName + "时发生异常:", e);
					}
				}
			};
		}
	/**
	 * 创建一个同步MAP用户清单数据到数据库表的线程
	 * @param custMap
	 * @param tabName
	 * @param needCheckExists是否需要检查存在
	 * @return
	 */
	private Thread createSynchronizeThread(final Byte2ObjectOpenHashMap<Short2ObjectOpenHashMap<BitSet>> custMap,
			final String tabName, final boolean needCheckExists,final String column, final String value) {
			return new Thread() {
				@Override
				public void run() {
					try {
						long t1 = System.currentTimeMillis();
						StringBuffer insertSql = null;
						insertSql = new StringBuffer("insert into ").append(tabName).append(" (PRODUCT_NO) values(?)");
						if (null != custMap) {
							jdbcTemplate.execute(insertSql.toString(), new PreparedStatementCallback() {
								@Override
								public Object doInPreparedStatement(PreparedStatement preparedstatement)
										throws SQLException, DataAccessException {
									int total = 0;
									//遍历数据集MAP
									for (Map.Entry<Byte, Short2ObjectOpenHashMap<BitSet>> e1 : custMap.entrySet()) {
										String phoneSeg1 = "1" + e1.getKey();
										for (Map.Entry<Short, BitSet> e2 : e1.getValue().entrySet()) {
											String phoneSeg2 = PopUtil.formatPhoneNo(e2.getKey());

											BitSet e3 = e2.getValue();
											for (int i = e3.nextSetBit(0); i != -1; i = e3.nextSetBit(i + 1)) {
												String phoneSeg3 = PopUtil.formatPhoneNo((short) i);
												String phoneNumber = phoneSeg1 + phoneSeg2 + phoneSeg3;

												++total;
												if (needCheckExists) { //需要检查存在，先检查存在再入库
													// 数据库检查方式太慢
													//												List cs = jdbcTemplate.queryForList("SELECT FEEDBACK_STATUS FROM "+tabName+" WHERE PRODUCT_NO = ?" ,new String[] { phoneNumber });
													//												if (CollectionUtils.isEmpty(cs)) {
													if (!doCheckExist(tabName, phoneNumber)) {
														batchUpdate(preparedstatement, phoneNumber, total, 5000);
													}
												} else { //不需要检查存在，直接入库
													batchUpdate(preparedstatement, phoneNumber, total, 5000);
												}

											}
										}
									}
									//防止遗漏数据，再执行一遍
									preparedstatement.executeBatch();
									log.info("同步数据到{}的总数:{} 条", tabName, total);
									//入库完成,删除号码检查缓存中对应表数据(不要忘记，防止缓存数据过大)
									if (needCheckExists) {
										checkPhoneExistFilter.removeDataByKey(tabName);
									}
									return null;
								}

							});
						}
						log.info("同步数据到{}共耗时:{}ms", tabName, (System.currentTimeMillis() - t1));
					} catch (Exception e) {
						log.error("同步数据到" + tabName + "时发生异常:", e);
						throw  new PopException("同步数据到" + tabName + "时发生异常:"+e);
					}
				}
			};
		}

	/**
	 * 实际检查号码是否存在工作
	 * @param tabNameKey
	 * @param targetPhone
	 * @return
	 */
	private boolean doCheckExist(String tabNameKey, String targetPhone) {
		return checkPhoneExistFilter.checkExist(tabNameKey, targetPhone);
	}
	
	/**
	 * 批量更新
	 * @param preparedstatement
	 * @param phoneNumber
	 * @param total
	 * @param batchSize
	 * @throws SQLException
	 */
	private void batchUpdate(PreparedStatement preparedstatement, String phoneNumber, int total, int batchSize)
			throws SQLException {
		preparedstatement.setString(1, phoneNumber);
		preparedstatement.addBatch();
		if (total % batchSize == 0) {//每5000条提交一次
			preparedstatement.executeBatch();
		}
	}


	@Override
	public void insertCustMapToRuleCustTable(final Byte2ObjectOpenHashMap<Short2ObjectOpenHashMap<BitSet>> avoidAfterCustMap,String custgroupType, final String taskId,final String sendCustTab) throws Exception {
		//判断客户群类型
		if (CustGroupType.MONTHLY.equals(custgroupType) || CustGroupType.DAILY.equals(custgroupType)) {
		  //周期性,根据生成周期将数据持续插入到A表
			//为了防止重复插入,先将当前执行日期的数据删掉
			String dbType = PopConfigure.getInstance().getProperty("DBTYPE");
			String delSql = "delete from "+sendCustTab + " where   task_id = '"+taskId+"'";
			jdbcTemplate.execute(delSql);
		}else {
		  //非周期性
			String sqlCount="select count(1) from "+sendCustTab;
			int count = jdbcTemplate.queryForInt(sqlCount);
			if(count > 0){
				//确保正确性先验证下,此表是否有数,有数则清除
				String delSql = "delete from "+sendCustTab;
				jdbcTemplate.execute(delSql);
			}
		}
		//再插入
		Thread t = new Thread() {
			@Override
			public void run() {
				try {
					long t1 = System.currentTimeMillis();
					StringBuffer insertSql = null;
					insertSql = new StringBuffer("insert into ").append(sendCustTab).append(" (PRODUCT_NO,TASK_ID,LAST_UPDATE_TIME) values(?,?,?)");
					java.util.Date date = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date()));//获取系统时间
					final java.sql.Timestamp date1=new java.sql.Timestamp(date.getTime());//把java.util.Date类型转换为java.sql.Timestamp类型
					if (null != avoidAfterCustMap) {
						jdbcTemplate.execute(insertSql.toString(), new PreparedStatementCallback() {
							@Override
							public Object doInPreparedStatement(PreparedStatement preparedstatement)
									throws SQLException, DataAccessException {
								int total = 0;
								//遍历数据集MAP
								for (Map.Entry<Byte, Short2ObjectOpenHashMap<BitSet>> e1 : avoidAfterCustMap.entrySet()) {
									String phoneSeg1 = "1" + e1.getKey();
									for (Map.Entry<Short, BitSet> e2 : e1.getValue().entrySet()) {
										String phoneSeg2 = PopUtil.formatPhoneNo(e2.getKey());

										BitSet e3 = e2.getValue();
										for (int i = e3.nextSetBit(0); i != -1; i = e3.nextSetBit(i + 1)) {
											String phoneSeg3 = PopUtil.formatPhoneNo((short) i);
											String phoneNumber = phoneSeg1 + phoneSeg2 + phoneSeg3;
											++total;
											preparedstatement.setString(1, phoneNumber);
											preparedstatement.setString(2, taskId);
											preparedstatement.setTimestamp(3, date1);
											preparedstatement.addBatch();
											if (total % 5000 == 0) {//每5000条提交一次
												preparedstatement.executeBatch();
											}
										}
									}
								}
								//防止遗漏数据，再执行一遍
								preparedstatement.executeBatch();
								log.info("同步数据到{}的总数:{} 条", sendCustTab, total);
								return null;
							}

						});
					}
					log.info("同步数据到{}共耗时:{}ms", sendCustTab, (System.currentTimeMillis() - t1));
				} catch (Exception e) {
					log.error("同步数据到" + sendCustTab + "时发生异常:", e);
					throw  new PopException("同步数据到" + sendCustTab + "时发生异常:"+e);
				}
			}
		};
		t.setName("synchronizeCustMapToDb[" + sendCustTab + "]");
		t.run();//暂不进行多线程处理。
	}


	@Override
	public void saveDataToDb(AvoidCustBean avoidCustBean, PopTaskBean ptb) throws Exception {
		//准客户群(静态)
		String preSendDataTab = ptb.getPreSendDataTab();
		//免打扰
		String rejectDataTab = ptb.getRejectDataTab();
		//实际派发数据
		String sendDataTab = ptb.getSendDataTab();
		//规则静态汇总表
		String rulePreSendDataTab =  ptb.getRulePreSendDataTab();
		//规则实际汇总表
		String ruleSendDataTab =  ptb.getRuleSendDataTab();
		
		//根据不同的类型 入库
		if(!Strings.isNullOrEmpty(ptb.getCustGroupId()) && Strings.isNullOrEmpty(ptb.getCepRuleId())){
		//只有静态客户群 无 复杂事件 
			if(!Strings.isNullOrEmpty(preSendDataTab)){
			  synchronizeCustMapToDb(avoidCustBean.getAvoidAfterCustMap(), preSendDataTab,false);
			}
			if(!Strings.isNullOrEmpty(rejectDataTab)){
			 synchronizeCustMapToDb(avoidCustBean.getAvoidDeleteCustMap(),rejectDataTab,false);
			 }
		
		    if(!Strings.isNullOrEmpty(sendDataTab)){
			 synchronizeCustMapToDb(avoidCustBean.getAvoidAfterCustMap(), sendDataTab,false);
		    }
		    if(!Strings.isNullOrEmpty(rulePreSendDataTab)){
		     synchronizeCustMapToDb(avoidCustBean.getAvoidAfterCustMap(), rulePreSendDataTab,true);
		    }
		    if(!Strings.isNullOrEmpty(ruleSendDataTab)){
		     synchronizeCustMapToDb(avoidCustBean.getAvoidAfterCustMap(), ruleSendDataTab,true);
		    }
		}else if(!Strings.isNullOrEmpty(ptb.getCustGroupId()) && !Strings.isNullOrEmpty(ptb.getCepRuleId())){
		//既复杂事件 又有 静态客户群	
		  if(!Strings.isNullOrEmpty(preSendDataTab)){
			synchronizeCustMapToDb(avoidCustBean.getAvoidAfterCustMap(), preSendDataTab,false);
		   }
		  if(!Strings.isNullOrEmpty(rulePreSendDataTab)){
		  synchronizeCustMapToDb(avoidCustBean.getAvoidAfterCustMap(), rulePreSendDataTab,true);
		  }
		}
		
	}


	@Override
	public void deleteTaskInfo(PopTaskBean ptb) throws Exception {
		//准客户群(静态)
		String preSendDataTab = ptb.getPreSendDataTab();
		//免打扰
		String rejectDataTab = ptb.getRejectDataTab();
		//实际派发数据
		String sendDataTab = ptb.getSendDataTab();
		
		if(!Strings.isNullOrEmpty(preSendDataTab)){
			this.removeTabData(preSendDataTab);
		}
		if(!Strings.isNullOrEmpty(rejectDataTab)){
			this.removeTabData(rejectDataTab);
		}
		if(!Strings.isNullOrEmpty(sendDataTab)){
			this.removeTabData(sendDataTab);
		}
	}


	@Override
	public List<PopTaskBean> getTodayPopTaskBeanList() {
		final List<PopTaskBean> list = new ArrayList<PopTaskBean>();
		//step.1 查询所有派单成功的侧率
		StringBuffer sql1 =new StringBuffer("SELECT ");
		sql1.append(" t1.id, ");
		sql1.append(" t1.start_time, ");
		sql1.append(" t1.end_time, ");
		sql1.append(" t1.policy_task_tab  ");
		sql1.append(" FROM ");
		sql1.append(" POP_POLICY_BASEINFO t1 ");
		sql1.append(" where t1.policy_status_id = 51 ");
		
		
		final String today = PopUtil.sendOddformat.format(new Date());
		StringBuffer sql2 =new StringBuffer(" SELECT ");
		sql2.append(" t1.id,t1.rule_id,t1.pre_send_data_tab,t1.send_data_tab,t1.reject_data_tab,t2.pre_send_data_tab rule_pre_send_data_tab, t2.send_data_tab rule_send_data_tab,t2.pccid pccid,t3.custgroup_id,t3.custgroup_type,t4.cep_rule_id,t4.simple_condtions_data,t5.avoid_custgroup_ids ");
		sql2.append(" FROM {table} t1 join POP_POLICY_RULE t2 ");
		sql2.append(" on t1.rule_id = t2.id AND");
		sql2.append(" t1.exec_status = "+PopConstant.TASK_STATUS_DDPD); 
		sql2.append(" and t1.exec_date =? ");
		sql2.append(" left join POP_POLICY_RULE_CUSTGROUP t3 ");
		sql2.append(" on t3.policy_rule_id = t1.rule_id ");
		sql2.append(" left join POP_POLICY_RULE_EVENT_CON t4 ");
		sql2.append(" on t4.policy_rule_id = t1.rule_id ");
		sql2.append(" left join POP_POLICY_RULE_ACT t5 ");
		sql2.append(" on t5.policy_rule_id = t1.rule_id ");
		
		List<Map> policyTaskList  = jdbcTemplate.queryForList(sql1.toString());
		for(Map m :policyTaskList){
			final String policyId = (String)m.get("id");
			final Date beginDate =(Date)m.get("start_time");
			final Date endDate =(Date)m.get("end_time");
			final String policyTaskTab = (String)m.get("policy_task_tab");
			
			String exeSql = sql2.toString().replace("{table}", policyTaskTab);
			log.debug("getTodayPopTaskBeanList sql:"+exeSql.toString());
			jdbcTemplate.query(exeSql.toString(), new Object[]{today}, new RowMapper() {
				public Object mapRow(ResultSet resultset, int i) throws SQLException {
					try {
						String taskId = resultset.getString("id");
						String ruleId = resultset.getString("rule_id");
						String rejectDataTab=resultset.getString("reject_data_tab");
						String preSendDataTab=resultset.getString("pre_send_data_tab");
						String sendDataTab=resultset.getString("send_data_tab");
						
						String rulePreSendDataTab = resultset.getString("rule_pre_send_data_tab");
						String ruleSendDataTab = resultset.getString("rule_send_data_tab");
						
						String cepRuleId = resultset.getString("cep_rule_id");
						String custgroupType = resultset.getString("custgroup_type");
						String custgroupId = resultset.getString("custgroup_id");
						
						String avoidCustGroupIds = resultset.getString("avoid_custgroup_ids");
						String simpleCondtionsData = resultset.getString("simple_condtions_data");//add by jinl
						System.out.println(" simpleCondtionsData = "+simpleCondtionsData);
						PopTaskBean popTaskBean = new PopTaskBean();
						popTaskBean.setPolicyId(policyId);
						popTaskBean.setRuleId(ruleId);
						popTaskBean.setTaskId(taskId);
						popTaskBean.setPolicyTaskTab(policyTaskTab);
						popTaskBean.setPreSendDataTab(preSendDataTab);
						popTaskBean.setSendDataTab(sendDataTab);
						popTaskBean.setRejectDataTab(rejectDataTab);
						popTaskBean.setRuleSendDataTab(ruleSendDataTab);
						popTaskBean.setRulePreSendDataTab(rulePreSendDataTab);
						popTaskBean.setExecDate(today);
						popTaskBean.setCustGroupType(custgroupType);
						popTaskBean.setCepRuleId(cepRuleId);
						popTaskBean.setCustGroupId(custgroupId);
						popTaskBean.setAvoidCustGroupIds(avoidCustGroupIds);
						popTaskBean.setSimpleCondtionsData(simpleCondtionsData);//add by jinl 
						
						popTaskBean.setPccid(resultset.getString("pccid"));
						log.info("List<PopTaskBean> getTodayPopTaskBeanList，getPCCID:"+popTaskBean.getPccid());
						list.add(popTaskBean);
					} catch (Exception e) {
						log.warn("查询出错："+ e);
					}
					return null;
				}
			  });
		}
		
		return list;
	}
	
	
	private  void removeTabData(String tabName) throws Exception {
		String dbType = PopUtil.getDBType();
		JdbcTemplate jdbcTemplate = SpringContext.getBean("jdbcTemplate", JdbcTemplate.class);
		if (StringUtil.isNotEmpty(tabName)) {
			try {
				StringBuffer b = null;
				if (PopConstant.DB_TYPE_DB2.equals(dbType)) {
					b = new StringBuffer();
					b.append("alter table ").append(tabName).append(" activate NOT LOGGED INITIALLY WITH EMPTY TABLE");
					jdbcTemplate.execute(b.toString());
					//jdbcTemplate.execute("alter table " + tabName + " activate NOT LOGGED INITIALLY");
				}  else {
					b = new StringBuffer("delete from ").append(tabName);
					jdbcTemplate.execute(b.toString());
				}
			} catch (Exception e) {
				 log.error("Clear data of {} error:", tabName, e);
				 throw new PopException(e.getMessage());
			}
		}
	}
	
	
	public ICheckPhoneExistFilter getCheckPhoneExistFilter() {
		return checkPhoneExistFilter;
	}


	public void setCheckPhoneExistFilter(
			ICheckPhoneExistFilter checkPhoneExistFilter) {
		this.checkPhoneExistFilter = checkPhoneExistFilter;
	}




	@Override
	public void updatePopTaskExeRelation(PopTaskBean ptb,String msg,boolean flag) {
		//step1.更新policyTaskTab
		String updateSqlPolicyTaskTab  = "update "+ptb.getPolicyTaskTab()  +" set exec_status = ? ,exec_info=?,end_time = ?,start_time=? where id = ? ";
		jdbcTemplate.update(updateSqlPolicyTaskTab,new Object[]{ptb.getExecStatus(),msg,ptb.getEndTime(),ptb.getStartTime(),ptb.getTaskId()});
		if(flag){
			//step2.更新POP_POLICY_RULE
			String sql2 = "update POP_POLICY_RULE set current_task_id = '"+ptb.getTaskId() +"',rule_status = "+PolicyStatus.SENDODER_USER_SUCCESS.getValue()+"  where id = '"+ptb.getRuleId()+"'";
			jdbcTemplate.execute(sql2);
		}else{
			String sql2 = "update POP_POLICY_RULE rule_status = "+PolicyStatus.SENDODER_USER_ERROR.getValue()+"  where id = '"+ptb.getRuleId()+"'";
			jdbcTemplate.execute(sql2);
		}
	}


	@Override
	public boolean reStartPolicyRuleTask(String policyId, String ruleId,String taskId) {
		boolean falg = true;
		//基础操作通过policyId 查询task表
		PopPolicyBaseinfo popPolicyBaseinfo = (PopPolicyBaseinfo) PopPolicyBaseinfo.dao().findById(policyId);
		String policyTaskTab = popPolicyBaseinfo.getStr(popPolicyBaseinfo.COL_POLICY_TASK_TAB);
		if(Strings.isNullOrEmpty(policyTaskTab)){
			throw  new PopException("策略id:"+policyId+",任务表policyTaskTab为null,请检查数据完整性!");
		}
		//step.1 先对task做校验,是否可以重启
		String errorCode = "";
		try {
			errorCode = "POP2001";
			Map checkMap = checkIsAbleReStartForTask(policyTaskTab,ruleId,taskId);
			errorCode = "POP2002";
			boolean flag = Boolean.parseBoolean((String)checkMap.get("flag"));
			if(flag){
				//可以重启
				errorCode = "POP2003";
				PopTaskBean popTaskBean = getPopTaskBean(taskId,policyTaskTab);
				PopTaskCache.getInstance().putTask(popTaskBean);
			}else{
				throw  new PopException((String)checkMap.get("mess"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			falg = false;
			throw  new PopException(ErrorCodeMap.getMsg(errorCode));
		}
		return falg;
	}
	
	
	/**
	 * 对task可否重启做校验
	 * @throws ParseException 
	 * */
	private Map<String,String> checkIsAbleReStartForTask(String policyTaskTab, String ruleId,String taskId) throws ParseException{
		Map<String,String> resultMap = new HashMap<String,String>();
		resultMap.put("flag", "true");
	    String sql1= "select exec_status,exec_date from "+policyTaskTab+" where id = '"+taskId+"' ";
		List<Map> policyTaskList  = jdbcTemplate.queryForList(sql1.toString());
		Map m = policyTaskList.get(0);
		String execDateStr = (String)m.get("exec_date");
		int  execStatus = (Integer)m.get("exec_status");
		//step1.查询 任务状态 是否是失败 
		if(execStatus != PopConstant.TASK_STATUS_PDSX){
			resultMap.put("flag", "false");
			resultMap.put("mess", "非失败任务无法重启!");
			return resultMap;
		}
		//step2.查询 是否是过期的 实时活动
		String sql2 ="select cep_rule_id from POP_POLICY_RULE_EVENT_CON where policy_rule_id = ?";
		 
		String cepRuleId = (String) jdbcTemplate.queryForObject(sql2, new Object[] { ruleId }, new RowMapper() {
			@Override
			public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
				return  rs.getString("cep_rule_id") ;
			}
		});
		
		if(!Strings.isNullOrEmpty(cepRuleId)){
			Date execDate = PopUtil.sendOddformat.parse(execDateStr);
			String todayStr = PopUtil.sendOddformat.format(new Date());
			Date today = PopUtil.sendOddformat.parse(todayStr);
			if(today.getTime() > execDate.getTime()){
				//如果当前时间 > 执行时间 即已经过期
				resultMap.put("flag", "false");
				resultMap.put("mess", "此实时活动已经过期,无法再重启!");
				return resultMap;
			}
		}
		return resultMap;
	}


	@Override
	public PopTaskBean getPopTaskBean(String taskId, final String popTaskTable) {
		StringBuffer sql =new StringBuffer(" SELECT ");
		sql.append(" t1.id,t1.rule_id,t1.pre_send_data_tab,t1.send_data_tab,t1.reject_data_tab,t1.exec_date,t2.policy_id,t2.pre_send_data_tab rule_pre_send_data_tab, t2.send_data_tab rule_send_data_tab,t3.custgroup_id,t3.custgroup_type,t4.cep_rule_id,t5.avoid_custgroup_ids ");
		sql.append(" FROM "+popTaskTable+" t1 join POP_POLICY_RULE t2 ");
		sql.append(" on t1.rule_id = t2.id AND");
		sql.append(" t1.id = ?"); 
		sql.append(" left join POP_POLICY_RULE_CUSTGROUP t3 ");
		sql.append(" on t3.policy_rule_id = t1.rule_id ");
		sql.append(" left join POP_POLICY_RULE_EVENT_CON t4 ");
		sql.append(" on t4.policy_rule_id = t1.rule_id ");
		sql.append(" left join POP_POLICY_RULE_ACT t5 ");
		sql.append(" on t5.policy_rule_id = t1.rule_id ");
		final String todayStr = PopUtil.sendOddformat.format(new Date());
		
		 PopTaskBean popTaskBean = (PopTaskBean) jdbcTemplate.queryForObject(sql.toString(), new Object[] { taskId }, new RowMapper() {
			@Override
			public Object mapRow(ResultSet resultset, int rowNum) throws SQLException {
				 PopTaskBean popTaskBean = null;
				 try {
						String taskId = resultset.getString("id");
						String ruleId = resultset.getString("rule_id");
						String rejectDataTab=resultset.getString("reject_data_tab");
						String preSendDataTab=resultset.getString("pre_send_data_tab");
						String sendDataTab=resultset.getString("send_data_tab");
						
						String rulePreSendDataTab = resultset.getString("rule_pre_send_data_tab");
						String ruleSendDataTab = resultset.getString("rule_send_data_tab");
						
						String cepRuleId = resultset.getString("cep_rule_id");
						String custgroupType = resultset.getString("custgroup_type");
						String custgroupId = resultset.getString("custgroup_id");
						
						String avoidCustGroupIds = resultset.getString("avoid_custgroup_ids");
						String exeDate = resultset.getString("exec_date");
						String policyId = resultset.getString("policy_id");
						popTaskBean = new PopTaskBean();
						popTaskBean.setPolicyId(policyId);
						popTaskBean.setRuleId(ruleId);
						popTaskBean.setTaskId(taskId);
						popTaskBean.setPolicyTaskTab(popTaskTable);
						popTaskBean.setPreSendDataTab(preSendDataTab);
						popTaskBean.setSendDataTab(sendDataTab);
						popTaskBean.setRejectDataTab(rejectDataTab);
						popTaskBean.setRuleSendDataTab(ruleSendDataTab);
						popTaskBean.setRulePreSendDataTab(rulePreSendDataTab);
						popTaskBean.setExecDate(todayStr);
						popTaskBean.setCustGroupType(custgroupType);
						popTaskBean.setCepRuleId(cepRuleId);
						popTaskBean.setCustGroupId(custgroupId);
						popTaskBean.setAvoidCustGroupIds(avoidCustGroupIds);
					} catch (Exception e) {
						log.warn("查询出错："+ e);
					}
				 return popTaskBean;
			}
		});
		
		return popTaskBean;
	}


	@Override
	public void controlRuleOperate(String ruleId, String popTaskTable,int operStatus) throws Exception {
		//setp1. 先判断是否是 实时规则 需要起停cep
		String cepRuleSql ="select cep_rule_id from POP_POLICY_RULE_EVENT_CON where policy_rule_id = ?";
		String cepRuleId = (String) jdbcTemplate.queryForObject(cepRuleSql, new Object[] { ruleId }, new RowMapper() {
			@Override
		   public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
					 return  rs.getString("cep_rule_id") ;
			 }
		});
		
		
		String todayStr = PopUtil.sendOddformat.format(new Date());
		String step="";
		if(!Strings.isNullOrEmpty(cepRuleId)){
			//cep
			if(operStatus == PopConstant.RULE_OPERATE_STOP){
			 try {
				//暂停 向cep 发指令
				step ="POP5001"; 
				CepUtil.stopCepEvent(cepRuleId);
				//关闭监听
				if (CepReceiveThreadCache.getInstance().exist(ruleId)) {
					try {
						step ="POP5002"; 
						CepReceiveThreadCache.getInstance().remove(ruleId);
					} catch (Exception e) {
						e.printStackTrace();
						throw new PopException("关闭监听异常:",e);
					}
				}
				step ="POP5003"; 
				//update popTaskTable 状态
				String stopSql = "update "+popTaskTable +" set exec_status = ? where rule_id = ? and (exec_status=? or exec_status=? ) and exec_date >=? " ;
				jdbcTemplate.update(stopSql,new Object[]{PopConstant.TASK_STATUS_PDZT,ruleId,PopConstant.TASK_STATUS_DDPD,PopConstant.TASK_STATUS_PDCG,todayStr});
				//update PolicyRuleStatus 
				String stopSql2 = "update POP_POLICY_RULE set rule_status =?    where id = ? " ;
				jdbcTemplate.update(stopSql2,new Object[]{PolicyStatus.SENDODER_PAUSE.getValue(),ruleId});
			 }catch (Exception e) {
				 e.printStackTrace();
				 throw new PopException(ErrorCodeMap.getMsg(step),e);
			 }
		    }else if(operStatus == PopConstant.RULE_OPERATE_RESTART){
				//重启
				//1.判断当天是否有任务可以重启
				String reStart ="select id from "+popTaskTable+" where rule_id = ? and exec_status=? and exec_date = ?";
				
				String taskId  = Db.findFirst(reStart,new Object[] { ruleId,PopConstant.TASK_STATUS_PDZT,todayStr}) == null? null:(String)Db.findFirst(reStart,new Object[] {ruleId,PopConstant.TASK_STATUS_PDZT,todayStr }).get("id");
			 
				if(!Strings.isNullOrEmpty(taskId)){
					//当天有可以重启的任务
					//1.1向cep发启动指令
					try {
						step ="POP5004"; 
						CepUtil.restartCepEvent(cepRuleId);
					//1.2开启监听
					PopTaskBean getPopTaskBean = getPopTaskBean(taskId,popTaskTable);
					if (!CepReceiveThreadCache.getInstance().exist(ruleId)) {
						//给CEP发消息注册启动事件信息
						step ="POP5005"; 
						CepUtil.registCepEvent(getPopTaskBean.getCepRuleId());
						CepMessageReceiverThread thread = new CepMessageReceiverThread(getPopTaskBean.getCepRuleId(),ruleId);
						thread.start();
					}
					step ="POP5006"; 
				 	//1.3put currentTaskId cache 
					SimpleCache.getInstance().put(PopConstant.CACHE_KEY_ACTIVITY_TASKID + "_" + ruleId, taskId,PopUtil.getRemainTime());
					//1.4update 当前task 派单成功
					getPopTaskBean.setEndTime(new Timestamp(System.currentTimeMillis()));
					getPopTaskBean.setStartTime(new Timestamp(System.currentTimeMillis()));
					getPopTaskBean.setExecStatus(PopConstant.TASK_STATUS_PDCG);
					String msg = "派单成功!";
					updatePopTaskExeRelation(getPopTaskBean,msg,true);
					}catch (Exception e) {
						e.printStackTrace();
						 throw new PopException(ErrorCodeMap.getMsg(step),e);
					}
				}else{
					//无
					//2.1update失效的任务
					String stopSql = "update "+popTaskTable +" set exec_info = ?,exec_status = ?  where rule_id = ? and  exec_status= ? and exec_date <? " ;
					jdbcTemplate.update(stopSql,new Object[]{"任务由于暂定导致已实效",PopConstant.TASK_STATUS_PDSX,ruleId,PopConstant.TASK_STATUS_PDZT,todayStr});
				
					//2.2update后续任务 等待派单,等待后台任务轮询执行
					String stopSql2 = "update "+popTaskTable +" set exec_status = ?  where rule_id = ? and  exec_status= ? and exec_date >? " ;
					jdbcTemplate.update(stopSql2,new Object[]{PopConstant.TASK_STATUS_DDPD,ruleId,PopConstant.TASK_STATUS_PDZT,todayStr});
					
				}
				String stopSql2 = "update POP_POLICY_RULE set rule_status = ?    where id = ? " ;
				jdbcTemplate.update(stopSql2,new Object[]{PolicyStatus.SENDODER_USER_SUCCESS.getValue(),ruleId});
				
			}else if(operStatus == PopConstant.RULE_OPERATE_FINISH){
				try {
					//终止  向cep 指令
					step ="POP5007"; 
					CepUtil.finishCepEvent(cepRuleId);
					//关闭监听
					if (CepReceiveThreadCache.getInstance().exist(ruleId)) {
						try {
							step ="POP5002"; 
							CepReceiveThreadCache.getInstance().remove(ruleId);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					step ="POP5009"; 
					//update popTaskTable 状态
					String stopSql = "update "+popTaskTable +" set exec_status = ? where rule_id = ? and (exec_status=? or exec_status=? or exec_status=?) and exec_date >=? " ;
					jdbcTemplate.update(stopSql,new Object[]{PopConstant.TASK_STATUS_PDZZ,ruleId,PopConstant.TASK_STATUS_DDPD,PopConstant.TASK_STATUS_PDCG,PopConstant.TASK_STATUS_PDZT,todayStr});
					//update PolicyRuleStatus 
					String stopSql2 = "update POP_POLICY_RULE set rule_status = ?    where id = ? " ;
					jdbcTemplate.update(stopSql2,new Object[]{PolicyStatus.SENDODER_TERMINAL.getValue(),ruleId});
				}catch (Exception e) {
					 e.printStackTrace();
					 throw new PopException(ErrorCodeMap.getMsg(step),e);
				}
			}else{
				throw  new PopException("未知操作类型!operStatus："+operStatus);
			}
			
		}else{
			//常规规则,包含静态客户群
			if(operStatus == PopConstant.RULE_OPERATE_STOP){
				//暂停
				//判断当天是否有需要暂停的任务
				String sql ="select id from "+popTaskTable+" where rule_id = ? and exec_status=? and exec_date = ?";
				
				String taskId  = Db.findFirst(sql,new Object[] { ruleId,PopConstant.TASK_STATUS_DDPD,todayStr }) == null? null:(String)Db.findFirst(sql,new Object[] { ruleId,PopConstant.TASK_STATUS_DDPD,todayStr }).get("id");
				 
				if(!Strings.isNullOrEmpty(taskId)){
				 //需要暂停
				 //移除缓存队列
				 PopTaskBean getPopTaskBean = getPopTaskBean(taskId,popTaskTable);
				 PopTaskCache.getInstance().removeTask(getPopTaskBean);
				}
				//update 状态 暂停
				//update popTaskTable 状态
				String stopSql = "update "+popTaskTable +" set exec_status = ? where rule_id = ? and (exec_status=? or exec_status=? ) and exec_date >=? " ;
				jdbcTemplate.update(stopSql,new Object[]{PopConstant.TASK_STATUS_PDZT,ruleId,PopConstant.TASK_STATUS_DDPD,PopConstant.TASK_STATUS_PDCG,todayStr});
				//update PolicyRuleStatus 
				String stopSql2 = "update POP_POLICY_RULE set rule_status =?    where id = ? " ;
				jdbcTemplate.update(stopSql2,new Object[]{PolicyStatus.SENDODER_PAUSE.getValue(),ruleId});
				//webservie 通知pcc 暂停
				
			}else if(operStatus == PopConstant.RULE_OPERATE_RESTART){
				//重启
				//判断当天是否有需要重启的任务
				String reStart ="select id from "+popTaskTable+" where rule_id = ? and exec_status=? and exec_date = ?";
			
				String taskId  = Db.findFirst(reStart,new Object[] { ruleId,PopConstant.TASK_STATUS_PDZT,todayStr }) == null? null:(String)Db.findFirst(reStart,new Object[] { ruleId,PopConstant.TASK_STATUS_PDZT,todayStr }).get("id");
				 
				if(!Strings.isNullOrEmpty(taskId)){
				  //需要重启
				  //增加到缓存队列
				  PopTaskBean getPopTaskBean = getPopTaskBean(taskId,popTaskTable);
				  PopTaskCache.getInstance().putTask(getPopTaskBean);
				}
				//update 状态 等待派单
				//2.1update失效的任务
				String restartSql = "update "+popTaskTable +" set exec_info = ?,exec_status = ?  where rule_id = ? and  exec_status= ? and exec_date <? " ;
				jdbcTemplate.update(restartSql,new Object[]{"任务由于暂定导致已实效",PopConstant.TASK_STATUS_PDSX,ruleId,PopConstant.TASK_STATUS_PDZT,todayStr});
			
				//2.2update后续任务 等待派单,等待后台任务轮询执行
				String restartSql2 = "update "+popTaskTable +" set exec_status = ?  where rule_id = ? and  exec_status= ? and exec_date >? " ;
				jdbcTemplate.update(restartSql2,new Object[]{PopConstant.TASK_STATUS_DDPD,ruleId,PopConstant.TASK_STATUS_PDZT,todayStr});
				
				String restartSql3 = "update POP_POLICY_RULE set rule_status = ?    where id = ? " ;
				jdbcTemplate.update(restartSql3,new Object[]{PolicyStatus.SENDODER_USER_SUCCESS.getValue(),ruleId});
				//webservie 通知pcc 重启
				
			}else if(operStatus == PopConstant.RULE_OPERATE_FINISH){
				//终止
				String stopSql ="select id from "+popTaskTable+" where rule_id = ? and exec_status=? and exec_date = ?";
				String taskId  = Db.findFirst(stopSql, new Object[] { ruleId,PopConstant.TASK_STATUS_DDPD,todayStr }) == null? null:(String)Db.findFirst(stopSql, new Object[] { ruleId,PopConstant.TASK_STATUS_DDPD,todayStr }).get("id");
				if(!Strings.isNullOrEmpty(taskId)){
				 //需要终止
				 //移除缓存队列
				 PopTaskBean getPopTaskBean = getPopTaskBean(taskId,popTaskTable);
				 PopTaskCache.getInstance().removeTask(getPopTaskBean);
				}
				//update 状态终止
				//update popTaskTable 状态
				String updateSql1 = "update "+popTaskTable +" set exec_status = ? where rule_id = ? and (exec_status=? or exec_status=? or exec_status=?) and exec_date >=? " ;
				jdbcTemplate.update(updateSql1,new Object[]{PopConstant.TASK_STATUS_PDZZ,ruleId,PopConstant.TASK_STATUS_DDPD,PopConstant.TASK_STATUS_PDCG,PopConstant.TASK_STATUS_PDZT,todayStr});
				//update PolicyRuleStatus 
				String updateSql2 = "update POP_POLICY_RULE set rule_status = ?    where id = ? " ;
				jdbcTemplate.update(updateSql2,new Object[]{PolicyStatus.SENDODER_TERMINAL.getValue(),ruleId});
				//webservie 通知pcc 终止
			}else{
				throw  new PopException("未知操作类型!operStatus："+operStatus);
			}
		} 
		
	}


	@Override
	public void saveDataToDbForSql(String sql, Object[] para) {
		jdbcTemplate.update(sql, para);
	}

	/**
	 * 拿到当前过期的策略
	 * */
	@Override
	public List<PopPolicyBaseinfoBean> getOutOfDatePopPolicyList() {
		 
		List<PopPolicyBaseinfoBean>  PopPolicyList= new ArrayList<PopPolicyBaseinfoBean>();
		String preDate = DateUtils.format(new Date(), "yyyy-MM-dd");
		List<Map> policys = getJdbcTemplate().queryForList(PopConstant.QUERY_OUT_OF_DATE_POLICY_SQL,new String[] { preDate });
		if (!CollectionUtils.isEmpty(policys)) {
			for (Map policy : policys) {
				PopPolicyBaseinfoBean  popPolicyBaseinfoBean= new PopPolicyBaseinfoBean();
				String policyId = (String)policy.get("id");
			    String policyTaskTab = (String)policy.get("policy_task_tab");
			    popPolicyBaseinfoBean.setPolicyId(policyId);
			    popPolicyBaseinfoBean.setPolicyTaskTab(policyTaskTab);
			    PopPolicyList.add(popPolicyBaseinfoBean);
			}
		}
		for(PopPolicyBaseinfoBean  popPolicyBaseinfoBean:PopPolicyList){
		     String policyId  = popPolicyBaseinfoBean.getPolicyId();
		     String taskTab = popPolicyBaseinfoBean.getPolicyTaskTab();
		     //通过policyId 查询所有rule 
		     List<PopPolicyRuleSendBean> popPolicyRuleSendBeanList = getPopPolicyRuleSendBeanList(policyId,taskTab);
		     for(PopPolicyRuleSendBean popPolicyRuleSendBean :popPolicyRuleSendBeanList){
		    	 String ruleId = popPolicyRuleSendBean.getRuleId();
		    	 Map<String,PopPolicyRuleSendBean> popPolicyRuleMap = popPolicyBaseinfoBean.getPopPolicyRuleMap();
		    	 if(CollectionUtils.isEmpty(popPolicyRuleMap)){
		    		 popPolicyBaseinfoBean.setPopPolicyRuleMap(new HashMap<String,PopPolicyRuleSendBean>());
		    	 }
		    	 popPolicyBaseinfoBean.getPopPolicyRuleMap().put(ruleId, popPolicyRuleSendBean);
		     }
		}
		
		return PopPolicyList;
	}


	 
	private List<PopPolicyRuleSendBean> getPopPolicyRuleSendBeanList(String policyId,String taskTab) {
		StringBuffer sql =new StringBuffer("SELECT ");
	    sql.append(" t1.id, t1.pre_send_data_tab,t1.send_data_tab,");
		sql.append(" t0.start_time,t0.end_time,t2.date_ranges ,");
		sql.append(" t3.custgroup_type,t3.custgroup_id, ");
		sql.append(" t4.cep_rule_id, t4.simple_condtions_data ,");
		sql.append(" t5.avoid_custgroup_ids ");
		sql.append(" FROM  POP_POLICY_BASEINFO t0 join ");
		sql.append(" POP_POLICY_RULE t1  on t0.id = t1.policy_id").append(" and t1.policy_id = '"+policyId+"'");
		sql.append(" LEFT JOIN ");
		sql.append(" POP_POLICY_RULE_EXEC_TIME t2 ");
		sql.append(" ON ");
		sql.append(" t1.id = t2.policy_rule_id ");
		sql.append(" LEFT JOIN ");
		sql.append(" POP_POLICY_RULE_CUSTGROUP t3 ");
		sql.append(" ON ");
		sql.append(" t1.id = t3.policy_rule_id ");
		sql.append(" LEFT JOIN ");
		sql.append(" POP_POLICY_RULE_EVENT_CON t4 ");
		sql.append(" ON ");
		sql.append(" t1.id = t4.policy_rule_id  ");	 
		sql.append(" LEFT JOIN ");
		sql.append(" POP_POLICY_RULE_ACT t5 ");
		sql.append(" ON ");
		sql.append(" t1.id = t5.policy_rule_id  ");	 
		
		String taskSql = "select id,exec_date,exec_status from {table} where rule_id = ?";
		
		List<PopPolicyRuleSendBean>  resultList  = new ArrayList<PopPolicyRuleSendBean>();
		List<Map> policyTaskList  = jdbcTemplate.queryForList(sql.toString());
		for(Map m:policyTaskList){
			String ruleId = (String)m.get("id");
			String custgroup_type =(String)m.get("custgroup_type");
			String cepRuleId = (String)m.get("cep_rule_id");
			String custGoupId = (String)m.get("custgroup_id");
			String avoidCustGroupIds = (String)m.get("avoid_custgroup_ids");
			String rule_pre_send_data_tab = (String)m.get("pre_send_data_tab");
			String rule_send_data_tab = (String)m.get("send_data_tab");
			String dateRanges  = (String)m.get("date_ranges");
			String simpleCondtionsData  = (String)m.get("simple_condtions_data");
			PopPolicyRuleSendBean  popPolicyRuleSendBean= new PopPolicyRuleSendBean();
			popPolicyRuleSendBean.setRuleId(ruleId);
			popPolicyRuleSendBean.setCustGroupType(custgroup_type);
			popPolicyRuleSendBean.setCepRuleId(cepRuleId);
			popPolicyRuleSendBean.setCustGroupId(custGoupId);
			popPolicyRuleSendBean.setAvoidCustGroupIds(avoidCustGroupIds);
			popPolicyRuleSendBean.setRulePreSendDataTab(rule_pre_send_data_tab);
			popPolicyRuleSendBean.setRuleSendDataTab(rule_send_data_tab);
			popPolicyRuleSendBean.setDateRanges(dateRanges);
			popPolicyRuleSendBean.setSimpleCondtionsData(simpleCondtionsData);
			
			if(StringUtil.isNotEmpty(taskTab)){
				taskSql = taskSql.replace("{table}", taskTab);
				try{
				List<Map> taskList  = jdbcTemplate.queryForList(taskSql.toString(),new Object[]{ruleId});
				for(Map tmpM:taskList){
					Map<String, PopTaskBean>  PopTaskBean = popPolicyRuleSendBean.getPopTaskMap();
					if(CollectionUtils.isEmpty(PopTaskBean)){
						Map<String,PopTaskBean> popTaskMap = new HashMap<String,PopTaskBean>();
						popPolicyRuleSendBean.setPopTaskMap(popTaskMap);
					}
					PopTaskBean  popTaskBean = new PopTaskBean();
					String taskId = (String) tmpM.get("id");
					String exeDate = (String) tmpM.get("exec_date");
					short exeStatus =   Short.parseShort(tmpM.get("exec_status")+"");
					popTaskBean.setTaskId(taskId);
					popTaskBean.setExecDate(exeDate);
					popTaskBean.setExecStatus(exeStatus);
					popPolicyRuleSendBean.getPopTaskMap().put(taskId, popTaskBean);
				}
				}catch(Exception e){
					log.error(e);
				}
			}
			
			resultList.add(popPolicyRuleSendBean);
		}
		
		return resultList;
	} 
	
	
	/**
	 * 常规派单(仅手机号码)
	 * @throws Exception
	 */
	public String sendCustInfo2ChannelNotRealTime(AvoidCustBean avoidCustBean, PopTaskBean ptb, String instruction)
			throws Exception {
		Integer fileCount = 0;
		Map<String, String> map = new HashMap<String, String>();
		String fileAppend = "";
		if (fileAppend.contains("BatSubscriber")) {
			fileAppend = "Subscriber";
		} else if (fileAppend.contains("BatService")) {
			fileAppend = "Service";
		} else if (fileAppend.contains("BatUsrSessionPolicy")) {
			fileAppend = "UsrSession";
		} else {
			return "error instuction";
		}
		try {//构造指令MAP
			final Byte2ObjectOpenHashMap<Short2ObjectOpenHashMap<BitSet>> data = avoidCustBean.getAvoidAfterCustMap();
			String policyId = ptb.getPolicyId();
			final String exportFileSeparator = PopConfigure.getInstance().getProperty("EXPORT_FILE_SEPARATOR");
			String equipment = PopConfigure.getInstance().getProperty("MANUFACTURER");
			//final String currentTime = DateUtil.date2String(new Date(), DateUtil.YYYY_MM_DD);
			//下发指令标记
			String instruct = "";
			if (instruction.startsWith("ad") || instruction.startsWith("sub")) {
				instruct = "A";
			} else if (instruction.startsWith("up")) {
				instruct = "U";
			} else if (instruction.startsWith("un") || instruction.startsWith("del")) {
				instruct = "U";
			}
			final StringBuilder sb = new StringBuilder();
			String filePath = Configure.getInstance().getProperty("SYS_COMMON_UPLOAD_PATH");
			if (!filePath.endsWith(File.separator)) {
				filePath = filePath + File.separator;
			}
			//生成文件名
			String realFileName = getFileName(fileAppend);
			final String realDataFilePath = filePath + realFileName;
			//添加文件头
			sb.append("90|").append(productFileTime.substring(0, 12)).append("|" + realFileName + "|" + "|")
					.append(exportFileSeparator);
			int count = 0;
			int bufferdsize = 2048;
			Writer osw = null;
			try {
				osw = new BufferedWriter(
						new OutputStreamWriter(new FileOutputStream(realDataFilePath, false), "UTF-8"), bufferdsize);
				Byte2ObjectOpenHashMap<Short2ObjectOpenHashMap<Short2ObjectOpenHashMap<Object2ObjectOpenHashMap<String, String>>>> custList = null;
				for (Map.Entry<Byte, Short2ObjectOpenHashMap<BitSet>> e1 : data.entrySet()) {
					String phoneSeg1 = "1" + e1.getKey();
					for (Map.Entry<Short, BitSet> e2 : e1.getValue().entrySet()) {
						String phoneSeg2 = PopUtil.formatPhoneNo(e2.getKey());
						BitSet e3 = e2.getValue();
						for (int i = e3.nextSetBit(0); i != -1; i = e3.nextSetBit(i + 1)) {
							String phoneSeg3 = PopUtil.formatPhoneNo((short) i);
							String phoneNumber = phoneSeg1 + phoneSeg2 + phoneSeg3;
							//10w行一个文本文件需要重新添加文件头
							if (count % 100000 == 0) {
								sb.append("90|").append(productFileTime.substring(0, 12))
										.append("|" + realFileName + "|" + "|").append(exportFileSeparator);
							}
							sb.append(instruct).append("|");
							sb.append(phoneNumber);
							if (!productFileTime.startsWith("BatFile_Subscriber")) {
								sb.append(policyId);
							}
							sb.append("||||||");
							String lastSendContent = "";
							sb.append(lastSendContent);
							osw.write(sb.toString());
							osw.write(exportFileSeparator);
							if (++count % 10000 == 0) { //1000条写一次数据
								//log.info("10000条flush一次文件!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!taskId=" + task.getTaskId()+",campsegId="+campSeginfo.getCampsegId());
								osw.flush();
								//每10w调数据添加文件为并且重新生成文件
								if (++count % 100000 == 0) {
									sb.append("99|")
											.append(DateUtil.date2String(new Date(), DateUtil.YYYY_MM_DD_HH_MM_SS)
													.replace("_", "")).append("|").append(count).append("||");
									osw.flush();
									String fileName = getFileName(instruction);
									osw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filePath
											+ fileName, false), "UTF-8"), bufferdsize);
									map.put(String.valueOf(fileCount), filePath + fileName);
								}
							}
							sb.delete(0, sb.length());
						}
					}
				}
				osw.write(sb.toString());
				//刷新文件输出缓存
				osw.flush();
				sb.delete(0, sb.length());
				if (osw != null) {
					osw.close();
				}
			} catch (Exception e) {
				log.error("生成派单文件异常：", e);
			}

			String fileName = "";
			String needFtp = PopConfigure.getInstance().getProperty("IS_NEED_FTP");

			if (needFtp != null && needFtp.equals("1")) {
				for (int j = 1; j <= fileCount; j++) {
					fileName = map.get(fileCount);
					String[] equipments = equipment.split(",");
					for (Integer i = 1; i < equipments.length; i++) {
						System.out.println("文件名===========" + fileName);
						File theFile = new File(fileName);
						this.uploadFileByFtp(theFile, fileName, equipments[i]);
					}
					log.debug("Send to ftp");
				}
			}

		} catch (Exception e) {
			throw e;
		}
		return "sendodd is over";
	}

	/**
	 * 上传文件
	 * @param theFile
	 * @param fileName
	 * @param sendoddFactoryId
	 * @throws Exception
	 */
	private void uploadFileByFtp(File theFile, String fileName, String sendoddFactoryId) throws Exception {
		FtpConfig cfcb = FtpConfigure.getInstance().getFtpConfigByTypes(sendoddFactoryId);
		String ftpServer = cfcb.getFTP_ADDRESS();
		int ftpServerPort = Integer.parseInt(cfcb.getFTP_PORT());
		String ftpStorePath = cfcb.getFTP_PATH();
		String ftpUserName = cfcb.getFTP_USER();
		String ftpUserPwd = cfcb.getFTP_PASSWORD();
		String ftpUserPwdEncrypt = cfcb.getFTP_PASSWORD_ENCRYPT();
		String encode = cfcb.getFTP_ENCODE();

		if (ftpUserPwdEncrypt != null && ftpUserPwdEncrypt.equals("1")) {
			ftpUserPwd = DES.decrypt(ftpUserPwd);
		}
		ApacheFtpUtil apacheFtp = null;
		try {
			apacheFtp = ApacheFtpUtil.getInstance(ftpServer, ftpServerPort, ftpUserName, ftpUserPwd, encode);
			apacheFtp.changeAndMakeDir(ftpStorePath);
			apacheFtp.upload(new FileInputStream(theFile), theFile.getName());
		} catch (Exception e) {
			log.error("uploadFileByFtp(" + theFile.getAbsolutePath() + ") error:", e);
		} finally {
			try {
				apacheFtp.forceCloseConnection();
			} catch (Exception e) {
				log.error("退出FTP失败！", e);
			}
			if (cfcb != null && "1".equalsIgnoreCase(cfcb.getISDELETE_LOCAL())) {
				theFile.delete();
			}
		}

	}

	/**
	 * 生成文件名
	 * @return String
	 */

	public static String getFileName(String instruction) {
		String currentTime = DateUtil.date2String(new Date(), DateUtil.YYYY_MM_DD_HH_MM_SS).replace("_", "");
		if ("".equals(productFileTime)) {
			if (productFileTime.contains(currentTime)) {
				if (Integer.parseInt(productFileTime.substring(13)) + 1 > 9) {
					productFileTime = productFileTime + Integer.parseInt(productFileTime.substring(13)) + 1;
				} else {
					productFileTime = productFileTime + "0" + Integer.parseInt(productFileTime.substring(13)) + 1;
				}

			} else {
				productFileTime = currentTime;
				productFileTime = productFileTime + "01";
			}
		}else {
			productFileTime = currentTime;
			productFileTime = productFileTime + "01";
		}
		return "BatFile_" + instruction + "_" + productFileTime + ".txt";
	}

}
