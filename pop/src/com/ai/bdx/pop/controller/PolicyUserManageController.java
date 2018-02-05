package com.ai.bdx.pop.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;

import com.ai.bdx.pop.base.PopController;
import com.ai.bdx.pop.bean.PopPolicyRuleBean;
import com.ai.bdx.pop.bean.PopUserPolicyManageBean;
import com.ai.bdx.pop.model.PopPolicyBaseinfo;
import com.ai.bdx.pop.model.PopPolicyRule;
import com.ai.bdx.pop.model.PopUserPolicyRuleData;
import com.ai.bdx.pop.util.CityId2CityNameUtil;
import com.ai.bdx.pop.util.PopConstant;
import com.ai.bdx.pop.util.SpringContext;
import com.ai.bdx.pop.wsclient.ISendPccInfoService;
import com.ai.bdx.pop.wsclient.PolicyOptType;
import com.asiainfo.biframe.utils.date.DateUtil;
import com.asiainfo.biframe.utils.string.StringUtil;
import com.google.common.collect.ImmutableMap;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;

/**
   * @ClassName: PolicyUserManagerController 
   * @Description: 用户策略管理
   * @author zhilj
   * @date 创建时间：2015-4-14
 */
public class PolicyUserManageController extends PopController{
	
	private static final ImmutableMap<String, String> POLICY_SIGN_STATUS = ImmutableMap.of("0","未签约", "1", "已签约");
	
	private static final ImmutableMap<Integer, String> POLICY_EXEC_STATUS = ImmutableMap.of(0,"执行失败", 1, "执行成功");
	
	private static final Logger log = LogManager.getLogger(PolicyUserManageController.class);
	
	private static final Integer PAGE_SIZE = 5;

	private static List<Object> condition = new ArrayList<Object>();

	public void index() {
		this.initAttributes();
		render("policyManage/policyUserManageIndex.jsp");
	}
	/**
	   * @Description: 查询用户策略管理信息
	   * @Return: void
	 */
	public void search(){
		this.initAttributes();
		int page = getParaToInt("page", 1);
		String policyNo = getPara("policyNo");//策略规则ID
		String policyTypeId = getPara("policyTypeId");//策略类型
		String startTime = getPara("startDate");
		String endTime = getPara("endDate");
		String policyControlTypeId = getPara("policyControlTypeId");//策略动作
		
		//封装前台返回数据信息
		List<PopUserPolicyManageBean> policyUserInfoList = new ArrayList<PopUserPolicyManageBean>();
		PopUserPolicyManageBean bean = null;
		StringBuffer select = new StringBuffer();
		StringBuffer policyBaseinfoSQL = new StringBuffer();
		select.append("select p.product_no,p.rule_id,p.policy_rule_name,p.policy_type_id,p.policy_level_id,");
		select.append("p.act_type_id,p.policy_rule_content,p.create_time,p.policy_sign_time,p.policy_sign_status,");
		select.append("pt.name policy_type_name,pl.name policy_level_name,pb.city_priority ");
		policyBaseinfoSQL.append("from pop_user_policy_rule_data p,pop_dim_policy_type pt,pop_dim_policy_level pl, pop_policy_rule rule,pop_policy_baseinfo pb ");
		policyBaseinfoSQL.append("where 1=1 and p.policy_level_id = pl.id and p.policy_type_id = pt.id and rule.id = p.rule_id and rule.policy_id = pb.id and rule.create_user_id= ? ");
		condition.clear();
		condition.add(userId);
		if(StringUtil.isNotEmpty(getPara("user_phone"))){
		policyBaseinfoSQL.append("and product_no like ? ");
			condition.add("%" + getPara("user_phone") + "%");}
		if(StringUtil.isNotEmpty(policyNo)){
			policyBaseinfoSQL.append("and rule_id like ?  ");
			condition.add("%" + policyNo + "%");}
		if(StringUtil.isNotEmpty(policyTypeId)){
			String type [] =policyTypeId.split(",");
			policyBaseinfoSQL.append(" and(");
			for(int i=0;i<type.length;i++){
				policyBaseinfoSQL.append("  policy_type_id  = ?");			
				condition.add(type[i]);
				if(i!=type.length-1){
					policyBaseinfoSQL.append(" or ");					
				}else{
					policyBaseinfoSQL.append(" ) ");
				}
			}	
		}
		if(StringUtil.isNotEmpty(policyControlTypeId)){
			String type [] =policyControlTypeId.split(",");
			policyBaseinfoSQL.append(" and(");
			for(int i=0;i<type.length;i++){
				policyBaseinfoSQL.append("  act_type_id  = ?");			
				condition.add(type[i]);
				if(i!=type.length-1){
					policyBaseinfoSQL.append(" or ");					
				}else{
					policyBaseinfoSQL.append(" ) ");
				}
			}	
		}
		if(StringUtil.isNotEmpty(startTime)){
			policyBaseinfoSQL.append("and p.create_time >= ?");
			condition.add(DateUtil.string2Date(startTime,"yyyy-MM-dd"));
			}
		if(StringUtil.isNotEmpty(endTime)){
			policyBaseinfoSQL.append("and p.policy_sign_time <= ?");
			condition.add(DateUtil.string2Date(endTime,"yyyy-MM-dd"));}
		policyBaseinfoSQL.append(" and p.rule_status_id !="+PopConstant.RULE_STATUS_ZHONGZHI);
		policyBaseinfoSQL.append(" order by p.create_time desc " );
		log.debug("------------用户策略管理查询语句----------:"+select.toString()+policyBaseinfoSQL.toString());
		Page<PopUserPolicyRuleData> popPolicyBaseinfoList  = PopUserPolicyRuleData.dao().paginate(page,PAGE_SIZE,select.toString(), policyBaseinfoSQL.toString(),condition.toArray());
		if (popPolicyBaseinfoList == null || popPolicyBaseinfoList.getList().size() == 0) {
			render("policyConfig/policyTemplateManageList.jsp");
			return;
		}
		PopUserPolicyRuleData ruleData = null;
		String ruleId;
		String cityPriority;
		String sql = "select city_priority from pop_policy_rule pr left join pop_policy_baseinfo pb on pr.policy_id = pb.id  where pr.id = ? ";
		for (int i = 0; i < popPolicyBaseinfoList.getList().size(); i++) {
			ruleData = popPolicyBaseinfoList.getList().get(i);
			bean = ruleData.toBean(PopUserPolicyManageBean.class);
			bean.setPolicySignStatusName(POLICY_SIGN_STATUS.get(bean.getPolicySignStatus()));
			ruleId = bean.getRuleId();
			cityPriority = Db.findFirst(sql, new Object[] { ruleId }).getStr("city_priority");
			try {
				bean.setCityPriorityName(CityId2CityNameUtil.getCityNameByCache(cityPriority));
			} catch (Exception e) {
				log.error("根据地市ID查询城市名称异常：" + e);
			}
			policyUserInfoList.add(bean);
		}
		setAttr("POP_POLICY_LEVEL_GLOBAL", PopConstant.POP_POLICY_LEVEL_GLOBAL);//全局等级
		setAttr("POP_POLICY_LEVEL_USER", PopConstant.POP_POLICY_LEVEL_USER);//客户等级	
		setAttr("policyUserInfoList", policyUserInfoList);
		setAttr("totalRowOfPolicySceneManage", popPolicyBaseinfoList.getTotalRow());
		setAttr("totalPageOfPolicySceneManage", popPolicyBaseinfoList.getTotalPage());
		setAttr("pageNumberOfPolicySceneManage", popPolicyBaseinfoList.getPageNumber());
		setAttr("pageSizeOfPolicySceneManage", popPolicyBaseinfoList.getPageSize());
		setAttr("visitsTimes",getPara("visitsTimes"));
		render("policyManage/policyUserManageInfoList.jsp");
	}
	/**
	   * @Description:查看策略执行信息
	   * @Return: void
	 */
	public void queryPolicyExecInfo() {
			this.initAttributes();
			JdbcTemplate jd = SpringContext.getBean("jdbcTemplate", JdbcTemplate.class);
			String ruleId = this.getPara("ruleId");
			String productNo = this.getPara("productNo");
			PopPolicyRule model = PopPolicyRule.dao().findById(ruleId);
			if(model==null){
				setAttr("infoSize", "0");
				render("policyManage/policyExecView.jsp");
				return;
			}
			PopPolicyRuleBean ruleBean = model.toBean(PopPolicyRuleBean.class);
			String table = ruleBean.getSendDataTab();
			if(StringUtil.isEmpty(table)){
				setAttr("infoSize", "0");
				render("policyManage/policyExecView.jsp");
				return;
			}
			StringBuffer querySql = new StringBuffer();
			querySql.append("select product_no,feedback_status, feedback_time from ");
			querySql.append(""+table);
			querySql.append(" where product_no=? and length(feedback_status)>0 and length(feedback_time)>0 ");
			log.debug("------------查询派发清单表----------:"+querySql.toString());
			List<Map> list = jd.queryForList(querySql.toString(), new Object[] {productNo});
			if(CollectionUtils.isNotEmpty(list)){			
				if(list.get(0).get("feedback_time")!=null){
					setAttr("infoSize", "1");
					setAttr("execStatus", POLICY_EXEC_STATUS.get(list.get(0).get("feedback_status")));
					setAttr("ruleId", ruleId);
					setAttr("execTime", list.get(0).get("feedback_time"));
					render("policyManage/policyExecView.jsp");
					return;
				}			
			}
			setAttr("infoSize", "0");
			render("policyManage/policyExecView.jsp");
		}
	/**
	   * @Description: 终止用户策略签约信息
	   * @Return: void
	 */
	public void terminaterPolicyRule(){
		this.initAttributes();
		JdbcTemplate jdbcTemplate = SpringContext.getBean("jdbcTemplate", JdbcTemplate.class);
		String ruleData = this.getPara("ruleData","");
		String[] str = ruleData.split(",");
		Map<String, String> map = new HashMap<String, String>();
		//update pop_user_policy_rule_data 状态
		String stopSql = "update pop_user_policy_rule_data set rule_status_id = ? where rule_id = ? and product_no = ? " ;
		ISendPccInfoService sendPccInfoService = SpringContext.getBean("sendPccInfoService",ISendPccInfoService.class);
		try {
			if(str.length==1){
				String ruleId = str[0].split("_")[0];
				String productNo = str[0].split("_")[1];
				jdbcTemplate.update(stopSql,new Object[]{PopConstant.RULE_STATUS_ZHONGZHI,ruleId,productNo});
				//add by jinl 20150819 根据需求增加参数 策略级别
			 	PopPolicyRule rule = PopPolicyRule.dao().findById(ruleId);
			 	String policy_level_id="";
			 	if(rule!=null){
				 	String policyId=rule.getStr(PopPolicyRule.COL_POLICY_ID);
				 	PopPolicyBaseinfo baseInfo = PopPolicyBaseinfo.dao().findById(policyId);
				 	if(baseInfo!=null){
				 		policy_level_id = baseInfo.get(PopPolicyBaseinfo.COL_POLICY_LEVEL_ID).toString();
				 	}
			 	}
			 	//去签约
				sendPccInfoService.singlePhoneOpt(PolicyOptType.CLOSE.getValue(), productNo, ruleId,policy_level_id);
				//调用pcc接口告诉pcc该用户签约信息需要终止（删除）待定，正和厂商谈论接口
			}else{
				for(String item :str){
					String ruleId = item.split("_")[0];
					String productNo = item.split("_")[1];
					jdbcTemplate.update(stopSql,new Object[]{PopConstant.RULE_STATUS_ZHONGZHI,ruleId,productNo});
					//add by jinl 20150819 根据需求增加参数 策略级别
				 	PopPolicyRule rule = PopPolicyRule.dao().findById(ruleId);
				 	String policy_level_id="";
				 	if(rule!=null){
					 	String policyId=rule.getStr(PopPolicyRule.COL_POLICY_ID);
					 	PopPolicyBaseinfo baseInfo = PopPolicyBaseinfo.dao().findById(policyId);
					 	if(baseInfo!=null){
					 		policy_level_id = baseInfo.get(PopPolicyBaseinfo.COL_POLICY_LEVEL_ID).toString();
					 	}
				 	}
					//去签约
					sendPccInfoService.singlePhoneOpt(PolicyOptType.CLOSE.getValue(), productNo, ruleId,policy_level_id);
					//调用pcc接口批量删除签约信息，正和厂商谈论接口
				}
			}
			map.put("success", "1");
		} catch (Exception e) {
			map.put("success", "0");
			map.put("msg", e.getMessage());
			e.printStackTrace();
		}			
		this.renderJson(map);
	}

}
