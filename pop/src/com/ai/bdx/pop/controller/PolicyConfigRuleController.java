package com.ai.bdx.pop.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.frontend.ClientProxy;
import org.apache.cxf.transport.http.HTTPConduit;
import org.apache.cxf.transports.http.configuration.HTTPClientPolicy;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ai.bdx.pop.base.PopController;
import com.ai.bdx.pop.bean.LacciBean;
import com.ai.bdx.pop.bean.LacciJsonBean;
import com.ai.bdx.pop.bean.PopPolicyCustGroupBean;
import com.ai.bdx.pop.enums.PolicyStatus;
import com.ai.bdx.pop.exception.PopInterfaceException;
import com.ai.bdx.pop.model.PopDimAreaType;
import com.ai.bdx.pop.model.PopDimAviodType;
import com.ai.bdx.pop.model.PopDimBusiType;
import com.ai.bdx.pop.model.PopDimCampChannel;
import com.ai.bdx.pop.model.PopDimContactFreq;
import com.ai.bdx.pop.model.PopDimControlType;
import com.ai.bdx.pop.model.PopDimNetType;
import com.ai.bdx.pop.model.PopDimPccId;
import com.ai.bdx.pop.model.PopDimTerminalInfo;
import com.ai.bdx.pop.model.PopInterfaceConfig;
import com.ai.bdx.pop.model.PopPolicyBaseinfo;
import com.ai.bdx.pop.model.PopPolicyRule;
import com.ai.bdx.pop.model.PopPolicyRuleAct;
import com.ai.bdx.pop.model.PopPolicyRuleCustgroup;
import com.ai.bdx.pop.model.PopPolicyRuleEventCon;
import com.ai.bdx.pop.model.PopPolicyRuleExecTime;
import com.ai.bdx.pop.util.InterfaceConstant;
import com.ai.bdx.pop.util.LogOperateUtil;
import com.ai.bdx.pop.util.PopUtil;
import com.ai.bdx.pop.wsclient.impl.PopCmCustomersWsClientImpl;
import com.ailk.bdx.cep.wservice.ICepWsService;
import com.asiainfo.biframe.utils.config.Configure;
import com.asiainfo.biframe.utils.date.DateUtil;
import com.asiainfo.biframe.utils.spring.SystemServiceLocator;
import com.asiainfo.biframe.utils.string.StringUtil;
import com.google.common.base.Joiner;
import com.google.common.base.Stopwatch;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

/**
 * 策略规则控制层
 * @author zhengyq3
 */
public class PolicyConfigRuleController extends PopController {
	private static final Logger log = LogManager.getLogger();
	
	/**
	 * add by jinl 20150910
	 * 调用ICEP接口所需传递的参数sysId。暂时使用"DACP"，如果用"POP"，需要在现场CEP表DIM_SYS_INFO中，增加POP。
	 */
	private static final String sysId = "DACP";
	
	/**
	 * add by jinl 20150910
	 * 调用ICEP接口所需传递的参数pageSize。
	 */
	private static final int pageSize =  10;	

	/**
	 * 
	* @Title: addRule 
	* @Description: 规则新建预处理   
	* @return void  返回类型 
	* @throws
	 */
	public void forAddRule() {
		List<PopDimControlType> dimControlTypes = PopDimControlType.dao().findAll();
		List<PopDimCampChannel> popDimCampChannel = PopDimCampChannel.dao().findAll();
		List<PopDimContactFreq> popDimContactFreq = PopDimContactFreq.dao().findAll();
		//PopCmCustomersWsClientImpl popCmCustomersWsClientImpl = new PopCmCustomersWsClientImpl();
		String policy_id = getPara("policy_id");
		PopPolicyBaseinfo popPolicyBaseinfo = PopPolicyBaseinfo.dao().findById(policy_id);
		String startDate = DateUtil.date2String(popPolicyBaseinfo.getTimestamp(PopPolicyBaseinfo.COL_START_TIME),
				DateUtil.YYYY_MM_DD);
		String endDate = DateUtil.date2String(popPolicyBaseinfo.getTimestamp(PopPolicyBaseinfo.COL_END_TIME),
				DateUtil.YYYY_MM_DD);
		String rule_id = PopUtil.generateId();
		//add by jinl 20160421 获取CEPlacci地址
		PopInterfaceConfig pc = (PopInterfaceConfig) PopInterfaceConfig.dao().findById(
				InterfaceConstant.CALLWS_CEP_QUERYLACCI_CODE);
		String cepQueryLacciInfoUrl = "";
		if (pc != null) {
			cepQueryLacciInfoUrl = pc.getStr(PopInterfaceConfig.COL_INTERFACE_ADDRESS);
		}
		setAttr("dimControlTypes", dimControlTypes);
		setAttr("popDimCampChannel", popDimCampChannel);
		setAttr("popDimContactFreq", popDimContactFreq);
		setAttr("startDate", startDate);
		setAttr("endDate", endDate);
		setAttr("flag", "add");
		setAttr("model_names", "");
		setAttr("model_ids", "");
		setAttr("rule_id", rule_id);
		setAttr("cepQueryLacciInfoUrl", cepQueryLacciInfoUrl);
		render("policyConfig/policyConfigRuleCreate.jsp");
	}

	/**
	* @Title: editInit 
	* @Description: 规则编辑预处理     
	* @return void    返回类型 
	* @throws
	 */
	public void editInit() {
		String rule_id = this.getPara("rule_id");
		PopPolicyRule popPolicyRule = (PopPolicyRule) PopPolicyRule.dao().findById(rule_id);
		PopPolicyRuleAct popPolicyRuleAct = (PopPolicyRuleAct) PopPolicyRuleAct.dao().searchFirst("policy_rule_id",
				rule_id);
		PopPolicyRuleEventCon popPolicyRuleEventCon = PopPolicyRuleEventCon.dao()
				.searchFirst("policy_rule_id", rule_id);
		PopPolicyRuleExecTime popPolicyRuleExecTime = PopPolicyRuleExecTime.dao()
				.searchFirst("policy_rule_id", rule_id);
		PopPolicyRuleCustgroup popPolicyRuleCustgroup = PopPolicyRuleCustgroup.dao().searchFirst("policy_rule_id",
				rule_id);
		PopPolicyBaseinfo popPolicyBaseinfo = PopPolicyBaseinfo.dao().findById(
				popPolicyRule.get(PopPolicyRule.COL_POLICY_ID));
		String startDate = DateUtil.date2String(popPolicyBaseinfo.getTimestamp(PopPolicyBaseinfo.COL_START_TIME),
				DateUtil.YYYY_MM_DD);
		String endDate = DateUtil.date2String(popPolicyBaseinfo.getTimestamp(PopPolicyBaseinfo.COL_END_TIME),
				DateUtil.YYYY_MM_DD);

		List<PopDimControlType> dimControlTypes = PopDimControlType.dao().findAll();
		List<PopDimCampChannel> popDimCampChannel = PopDimCampChannel.dao().findAll();
		List<PopDimContactFreq> popDimContactFreq = PopDimContactFreq.dao().findAll();

		String avoid_custgroup_names = "";
		if (popPolicyRuleAct != null) {
			String strRuleAct = popPolicyRuleAct.getStr(PopPolicyRuleAct.COL_AVOID_CUSTGROUP_IDS);
			if (strRuleAct != null) {
				String[] avoid_custgroup_ids = strRuleAct.split(",");
				for (String avoid_custgroup_id : avoid_custgroup_ids) {
					PopDimAviodType popDimAviodType = PopDimAviodType.dao().searchFirst("id", avoid_custgroup_id);
					if (popDimAviodType != null) {
						avoid_custgroup_names += popDimAviodType.getStr(PopDimAviodType.COL_NAME) + ",";
					}
				}

				if (avoid_custgroup_names.length() > 0) {
					avoid_custgroup_names = avoid_custgroup_names.substring(0, avoid_custgroup_names.length() - 1);
				}
			}
		}

		String flag = (String) this.getRequest().getAttribute("flag");
		if (flag != null && flag.equals("view")) {
			flag = "view";
		} else {
			flag = "edit";
		}
		List<String> model_names = Lists.newArrayList();
		List<String> model_ids = Lists.newArrayList();
		List<Record> models = Db
				.find("select * from v_dim_term_info where model_id in(select term_id from pop_policy_rule_term_rel where policy_rule_id = ?)",
						new Object[] { rule_id });
		for (Record record : models) {
			model_ids.add(record.getStr("model_id"));
			model_names.add(record.getStr("model_name"));
		}
		//简单规则 终端及位置信息
		String simple_condtions_data = popPolicyRuleEventCon.getStr(PopPolicyRuleEventCon.COL_SIMPLE_CONDTIONS_DATA);
		if (StringUtil.isNotEmpty(simple_condtions_data)) {
			try {
				JSONObject json = JSONObject.fromObject(simple_condtions_data);
				if (json.containsKey("terminal_manu")) {
					String terminal_manu = json.getString("terminal_manu");
					setAttr("terminal_manu", terminal_manu);
				}
				if (json.containsKey("terminal_brand")) {
					String brand_id = json.getString("terminal_brand");
					setAttr("brand_name", brand_id);
				}
				if (json.containsKey("area_type_id")) {
					String area_type = json.getString("area_type_id");
					String area_type_name = area_type;
					if(StringUtil.isNotEmpty(area_type))
					{
						List<String> records = Db
								.query("select name from pop_dim_area_type where id in(" + area_type + ")");
						area_type_name = Joiner.on(',').join(records);
					}
					setAttr("area_type_name", area_type_name);
				}
			} catch (Exception e) {
				log.error("获取简单规则 终端及位置信息异常", e);
			}
		}

		//add by jinl 20160421 获取CEPlacci地址
		PopInterfaceConfig pc = (PopInterfaceConfig) PopInterfaceConfig.dao().findById(
				InterfaceConstant.CALLWS_CEP_QUERYLACCI_CODE);
		String cepQueryLacciInfoUrl = "";
		if (pc != null) {
			cepQueryLacciInfoUrl = pc.getStr(PopInterfaceConfig.COL_INTERFACE_ADDRESS);
		}
		setAttr("cepQueryLacciInfoUrl", cepQueryLacciInfoUrl);	
		setAttr("dimControlTypes", dimControlTypes);
		setAttr("popDimCampChannel", popDimCampChannel);
		setAttr("popPolicyRule", popPolicyRule);
		setAttr("popPolicyRuleAct", popPolicyRuleAct);
		setAttr("avoid_custgroup_names", avoid_custgroup_names);
		setAttr("popPolicyRuleExecTime", popPolicyRuleExecTime);
		setAttr("popDimContactFreq", popDimContactFreq);
		setAttr("popPolicyRuleCustgroup", popPolicyRuleCustgroup);
		setAttr("popPolicyRuleEventCon", popPolicyRuleEventCon);
		setAttr("rule_id", rule_id);
		setAttr("startDate", startDate);
		setAttr("endDate", endDate);
		setAttr("flag", flag);
		setAttr("model_names", Joiner.on(',').join(model_names));
		setAttr("model_ids", Joiner.on(',').join(model_ids));
		render("policyConfig/policyConfigRuleCreate.jsp");
		
	}

	/**
	 * 
	* @Title: edit 
	* @Description: 修改策略规则   
	* @return void  返回类型 
	* @throws
	 */
	public void edit() {
		Map<String, String> map = Maps.newHashMap();
		String rule_id = this.getPara("rule_id");
		//add by jinl 获取PCCIDdisplay_pcc_id_name
		String new_rule_id = this.getPara("display_pcc_id_name");
		//将旧的rule_id，在PPCID表中更新成0未使用
		PopDimPccId oldPopDimPccId = PopDimPccId.dao().findById(rule_id);
		if (oldPopDimPccId != null) {
			oldPopDimPccId.set(PopDimPccId.use_flag, 0).update();
		}
		//将新的rule_id，在PPCID表中更新成1已使用
		PopDimPccId newPopDimPccId = PopDimPccId.dao().findById(new_rule_id);
		if (newPopDimPccId != null) {
			newPopDimPccId.set(PopDimPccId.use_flag, 1).update();
		}
		//update 将rule关联表中 old_rule_id 设置为新rule_id

		String col_rule_priority = this.getPara("rule_priority");
		if (col_rule_priority != null && col_rule_priority.equals("")) {
			col_rule_priority = null;//表中是整型 , ""不能保存
		}

		String col_policy_rule_name = this.getModel(PopPolicyRule.class).getStr(PopPolicyRule.COL_POLICY_RULE_NAME);

		//修改策略规则信息
		PopPolicyRule popPolicyRule = PopPolicyRule.dao().findById(rule_id);
		String oldDesc =  popPolicyRule.toString();
		//add by jinl 20150608
		PopPolicyRule newPopPolicyRule = popPolicyRule;
		try {
			if (popPolicyRule != null) {
				popPolicyRule.set(PopPolicyRule.COL_RULE_PRIORITY, col_rule_priority)
						.set(PopPolicyRule.COL_POLICY_RULE_NAME, col_policy_rule_name).update();
				//add by jinl 20150608 删除旧规则，创建新规则
				popPolicyRule.deleteById(rule_id);
				newPopPolicyRule.set(PopPolicyRule.COL_ID, rule_id).set(PopPolicyRule.COL_PCCID, new_rule_id).save();
			}

			//修改策略规则与客户群关系
			String custgroup_id = getPara("custgroup_id");
			if (StringUtil.isNotEmpty(custgroup_id)) {
				PopCmCustomersWsClientImpl popCmCustomersWsClientImpl = new PopCmCustomersWsClientImpl();
				String strJson = "";
				try {
					strJson = popCmCustomersWsClientImpl.getTargetCustomersObj(custgroup_id);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Gson gson = new Gson();
				PopPolicyCustGroupBean popPolicyBean = gson.fromJson(strJson, PopPolicyCustGroupBean.class);
				String target_customers_cycle = popPolicyBean.getTarget_customers_cycle() == null ? "" : popPolicyBean
						.getTarget_customers_cycle();
				String custgroup_type = "N";//非周期
				if ("2".equalsIgnoreCase(target_customers_cycle)) {
					custgroup_type = "M";//月周期
				} else if ("3".equalsIgnoreCase(target_customers_cycle)) {
					custgroup_type = "D";//日周期
				}
				popPolicyBean.setCust_group_type(custgroup_type);

				PopPolicyRuleCustgroup popPolicyRuleCustgroup = PopPolicyRuleCustgroup.dao().searchFirst(
						"policy_rule_id", rule_id);
				if (popPolicyRuleCustgroup != null) {
					popPolicyRuleCustgroup
							.set(PopPolicyRuleCustgroup.COL_CUSTGROUP_ID, popPolicyBean.getTarget_customers_id())
							.set(PopPolicyRuleCustgroup.COL_CUSTGROUP_TYPE, popPolicyBean.getCust_group_type())
							.set(PopPolicyRuleCustgroup.COL_CUSTGROUP_NAME, popPolicyBean.getTarget_customers_name())
							.set(PopPolicyRuleCustgroup.COL_CUSTGROUP_NUMBER, popPolicyBean.getTarget_customers_num())
							.set(PopPolicyRuleCustgroup.COL_CUSTGROUP_TYPE, popPolicyBean.getCust_group_type())
							.set(PopPolicyRuleCustgroup.COL_POLICY_RULE_ID, rule_id).update();
				}
				else //
				{
					PopPolicyRuleCustgroup custGroup = new PopPolicyRuleCustgroup();
					custGroup
					.set(PopPolicyRuleCustgroup.COL_ID, PopUtil.generateId())
					.set(PopPolicyRuleCustgroup.COL_CUSTGROUP_ID, popPolicyBean.getTarget_customers_id())
					.set(PopPolicyRuleCustgroup.COL_CUSTGROUP_TYPE, popPolicyBean.getCust_group_type())
					.set(PopPolicyRuleCustgroup.COL_CUSTGROUP_NAME, popPolicyBean.getTarget_customers_name())
					.set(PopPolicyRuleCustgroup.COL_CUSTGROUP_NUMBER, popPolicyBean.getTarget_customers_num())
					.set(PopPolicyRuleCustgroup.COL_CUSTGROUP_TYPE, popPolicyBean.getCust_group_type())
					.set(PopPolicyRuleCustgroup.COL_POLICY_RULE_ID, rule_id).update();
					custGroup.save();
				}
			}

			//修改策略规则与执行动作（管控/营销）关系
			String policy_act_type_id = getPara("policy_act_type_id");
			Db.update("delete from pop_policy_rule_act where policy_rule_id=?", rule_id);
			if (policy_act_type_id != null && policy_act_type_id.equals("1")) {//策略动作类型 是管控类
				String control_type_id = getPara("control_type_id");
				String control_param = getPara("control_param");
				if (control_param != null && control_param.equals("")) {
					control_param = null;//表中是整型 , ""不能保存
				}
				this.getModel(PopPolicyRuleAct.class).set(PopPolicyRuleAct.COL_ID, PopUtil.generateId())
						.set(PopPolicyRuleAct.COL_POLICY_RULE_ID, rule_id)
						.set(PopPolicyRuleAct.COL_POLICY_ACT_TYPE_ID, policy_act_type_id)
						.set(PopPolicyRuleAct.COL_CONTROL_TYPE_ID, control_type_id)
						.set(PopPolicyRuleAct.COL_CONTROL_PARAM, control_param).save();
			} else if (policy_act_type_id != null && policy_act_type_id.equals("2")) {//策略动作类型 是营销类
				String exec_channel_id = getPara("exec_channel_id");
				String exec_camp_frequency = getPara("exec_camp_frequency");
				String exec_camp_content = getPara("exec_camp_content");
				String invalid_channel_id = getPara("invalid_channel_id");
				String invalid_camp_frequency = getPara("invalid_camp_frequency");
				String invalid_camp_content = getPara("invalid_camp_content");
				String avoid_custgroup_ids = getPara("avoid_custgroup_ids");

				this.getModel(PopPolicyRuleAct.class).set(PopPolicyRuleAct.COL_POLICY_RULE_ID, rule_id)
						.set(PopPolicyRuleAct.COL_ID, PopUtil.generateId())
						.set(PopPolicyRuleAct.COL_POLICY_ACT_TYPE_ID, policy_act_type_id)
						.set(PopPolicyRuleAct.COL_EXEC_CHANNEL_ID, exec_channel_id)
						.set(PopPolicyRuleAct.COL_EXEC_CAMP_FREQUENCY, exec_camp_frequency)
						.set(PopPolicyRuleAct.COL_EXEC_CAMP_CONTENT, exec_camp_content)
						.set(PopPolicyRuleAct.COL_INVALID_CHANNEL_ID, invalid_channel_id)
						.set(PopPolicyRuleAct.COL_INVALID_CAMP_FREQUENCY, invalid_camp_frequency)
						.set(PopPolicyRuleAct.COL_INVALID_CAMP_CONTENT, invalid_camp_content)
						.set(PopPolicyRuleAct.COL_AVOID_CUSTGROUP_IDS, avoid_custgroup_ids).save();
			}

			//修改策略规则与事件规则关系
			String cep_rule_id = getPara("cep_rule_id");
			String cep_rule_desc = getPara("cep_rule_desc");
			String simple_condtions_data = getPara("simple_condtions_data");
			String simple_condtions_desc = getPara("simple_condtions_desc");
			//add by jinl 20160418
			String cep_choosedlacci_desc = getPara("choosedLacciAndCount");
			String lacciAndStationTypeJsons = getPara("lacciAndStationTypeJsons");

			PopPolicyRuleEventCon popPolicyRuleEventCon = PopPolicyRuleEventCon.dao().searchFirst("policy_rule_id",
					rule_id);
			popPolicyRuleEventCon.set(PopPolicyRuleEventCon.COL_CEP_RULE_ID, cep_rule_id)
					.set(PopPolicyRuleEventCon.COL_CEP_RULE_DESC, cep_rule_desc)
					.set(PopPolicyRuleEventCon.COL_SIMPLE_CONDTIONS_DATA, simple_condtions_data)
					.set(PopPolicyRuleEventCon.COL_SIMPLE_CONDTIONS_DESC, simple_condtions_desc)
					.set(PopPolicyRuleEventCon.COL_POLICY_RULE_ID, rule_id)
					.set(PopPolicyRuleEventCon.COL_CEP_CHOOSEDLACCI_DESC, cep_choosedlacci_desc)
					.set(PopPolicyRuleEventCon.COL_CEP_LACCIANDSTATIONTYPEJSONS, lacciAndStationTypeJsons).update();

			//删除所以关联的终端,再建立关联
			Db.update("delete from pop_policy_rule_term_rel where policy_rule_id=?", new Object[] { rule_id });
			String model_ids = getPara("model_ids");
			if (StringUtil.isNotEmpty(model_ids)) {
				List<String> sqlList = Lists.newArrayList();
				String[] ids = model_ids.split(",");
				for (int i = 0; i < ids.length; i++) {
					sqlList.add(String.format("insert into pop_policy_rule_term_rel values('%s','%s')", ids[i],
							rule_id));
				}
				Db.batch(sqlList, sqlList.size());
			}

			//修改策略规则与执行时间关系 
			String date_ranges = getPara("date_ranges");
			String time_ranges = getPara("time_ranges");
			String avoid_range = getPara("avoid_ranges");
			PopPolicyRuleExecTime popPolicyRuleExecTime = PopPolicyRuleExecTime.dao().searchFirst("policy_rule_id",
					rule_id);
			if (popPolicyRuleExecTime != null) {
				popPolicyRuleExecTime.set(PopPolicyRuleExecTime.COL_DATE_RANGES, date_ranges)
						.set(PopPolicyRuleExecTime.COL_TIME_RANGES, time_ranges)
						.set(PopPolicyRuleExecTime.COL_AVOID_RANGES, avoid_range)
						.set(PopPolicyRuleExecTime.COL_POLICY_RULE_ID, rule_id).update();
			}

			map.put("ruleName", popPolicyRule.getStr(PopPolicyRule.COL_POLICY_RULE_NAME));
			map.put("success", "1");
			//记录日志
			String desc =  popPolicyRule.toString();
			desc = String.format("修改前的数据:%s\n,修改后的数据:%s",oldDesc,desc);
			LogOperateUtil.log(LogOperateUtil.POLICY_MAN_RULE_UPDATE,desc,this.getRequest());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			map.put("success", "0");
			map.put("ruleName", popPolicyRule.getStr(PopPolicyRule.COL_POLICY_RULE_NAME));
			map.put("msg", e.getMessage());
			log.error("修改规则报错", e);
			e.printStackTrace();
			//记录日志
			String desc = String.format("修改策略规则失败:%s,策略规则编号:%s",e.getMessage(), rule_id);
			LogOperateUtil.log(LogOperateUtil.POLICY_MAN_RULE_UPDATE,desc,this.getRequest());
		}

		this.renderJson(map);

	}

	/**
	* @Title: view 
	* @Description: 查看策略规则
	* @return void  返回类型 
	* @throws
	 */
	public void view() {
		setAttr("flag", "view");
		this.editInit();
	}

	/**
	* @Title: createRule 
	* @Description:  创建规则    
	* @return void    返回类型 
	* @throws
	 */
	public void createRule() {
		initAttributes();

		Map<String, String> map = Maps.newHashMap();

		String col_policy_rule_name = this.getModel(PopPolicyRule.class).getStr(PopPolicyRule.COL_POLICY_RULE_NAME);
		String col_rule_priority = getPara("rule_priority");
		if (col_rule_priority != null && col_rule_priority.equals("")) {
			col_rule_priority = null;//表中是整型 , ""不能保存
		}

		String col_policy_id = this.getModel(PopPolicyRule.class).getStr(PopPolicyRule.COL_POLICY_ID);
		String col_PARENT_ID = this.getModel(PopPolicyRule.class).getStr(PopPolicyRule.COL_PARENT_ID);
		//add by jinl 20150607 规则ID从页面“策略ID”获取
		String display_pcc_id = getPara("display_pcc_id");
		String rule_id =getPara("rule_id");;
		if (rule_id == null || "".equals(rule_id) || rule_id.length() == 0) {
			rule_id = PopUtil.generateId();
		}

		try {
            PopPolicyRule pr = this.getModel(PopPolicyRule.class);
			//保存策略规则信息
			pr.set(PopPolicyRule.COL_ID, rule_id)
					.set(PopPolicyRule.COL_POLICY_ID, col_policy_id).set(PopPolicyRule.COL_PARENT_ID, col_PARENT_ID)
					.set(PopPolicyRule.COL_POLICY_RULE_NAME, col_policy_rule_name)
					.set(PopPolicyRule.COL_RULE_PRIORITY, col_rule_priority)
					.set(PopPolicyRule.COL_RULE_STATUS, PolicyStatus.EDITING.getValue())
					.set(PopPolicyRule.COL_CREATE_USER_ID, userId).set(PopPolicyRule.COL_PCCID, display_pcc_id).save();

			//保存策略规则与客户群关系
			String custgroup_id = getPara("custgroup_id");
			if (StringUtil.isNotEmpty(custgroup_id)) {
				PopCmCustomersWsClientImpl popCmCustomersWsClientImpl = new PopCmCustomersWsClientImpl();
				String strJson = "";
				try {
					strJson = popCmCustomersWsClientImpl.getTargetCustomersObj(custgroup_id);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Gson gson = new Gson();
				PopPolicyCustGroupBean popPolicyBean = gson.fromJson(strJson, PopPolicyCustGroupBean.class);
				String target_customers_cycle = popPolicyBean.getTarget_customers_cycle() == null ? "" : popPolicyBean
						.getTarget_customers_cycle();
				String custgroup_type = "N";//非周期
				if ("2".equalsIgnoreCase(target_customers_cycle)) {
					custgroup_type = "M";//月周期
				} else if ("3".equalsIgnoreCase(target_customers_cycle)) {
					custgroup_type = "D";//日周期
				}
				popPolicyBean.setCust_group_type(custgroup_type);
				;

				this.getModel(PopPolicyRuleCustgroup.class).set(PopPolicyRuleCustgroup.COL_POLICY_RULE_ID, rule_id)
						.set(PopPolicyRuleCustgroup.COL_ID, PopUtil.generateId())
						.set(PopPolicyRuleCustgroup.COL_CUSTGROUP_ID, popPolicyBean.getTarget_customers_id())
						.set(PopPolicyRuleCustgroup.COL_CUSTGROUP_NAME, popPolicyBean.getTarget_customers_name())
						.set(PopPolicyRuleCustgroup.COL_CUSTGROUP_TYPE, popPolicyBean.getCust_group_type())
						.set(PopPolicyRuleCustgroup.COL_CUSTGROUP_NUMBER, popPolicyBean.getTarget_customers_num())
						.set(PopPolicyRuleCustgroup.COL_CUSTGROUP_TYPE, popPolicyBean.getCust_group_type()).save();

			}

			//保存策略规则与执行动作（管控/营销）关系
			String policy_act_type_id = getPara("policy_act_type_id");
			if (policy_act_type_id != null && policy_act_type_id.equals("1")) {//策略动作类型 是管控类
				String control_type_id = getPara("control_type_id");
				String control_param = getPara("control_param");
				if (control_param != null && control_param.equals("")) {
					control_param = null;//表中是整型 , ""不能保存
				}
				this.getModel(PopPolicyRuleAct.class).set(PopPolicyRuleAct.COL_ID, PopUtil.generateId())
						.set(PopPolicyRuleAct.COL_POLICY_RULE_ID, rule_id)
						.set(PopPolicyRuleAct.COL_POLICY_ACT_TYPE_ID, policy_act_type_id)
						.set(PopPolicyRuleAct.COL_CONTROL_TYPE_ID, control_type_id)
						.set(PopPolicyRuleAct.COL_CONTROL_PARAM, control_param).save();
			} else if (policy_act_type_id != null && policy_act_type_id.equals("2")) {//策略动作类型 是营销类
				String exec_channel_id = getPara("exec_channel_id");
				String exec_camp_frequency = getPara("exec_camp_frequency");
				String exec_camp_content = getPara("exec_camp_content");
				String invalid_channel_id = getPara("invalid_channel_id");
				String invalid_camp_frequency = getPara("invalid_camp_frequency");
				String invalid_camp_content = getPara("invalid_camp_content");
				String avoid_custgroup_ids = getPara("avoid_custgroup_ids");

				this.getModel(PopPolicyRuleAct.class).set(PopPolicyRuleAct.COL_POLICY_RULE_ID, rule_id)
						.set(PopPolicyRuleAct.COL_ID, PopUtil.generateId())
						.set(PopPolicyRuleAct.COL_POLICY_ACT_TYPE_ID, policy_act_type_id)
						.set(PopPolicyRuleAct.COL_EXEC_CHANNEL_ID, exec_channel_id)
						.set(PopPolicyRuleAct.COL_EXEC_CAMP_FREQUENCY, exec_camp_frequency)
						.set(PopPolicyRuleAct.COL_EXEC_CAMP_CONTENT, exec_camp_content)
						.set(PopPolicyRuleAct.COL_INVALID_CHANNEL_ID, invalid_channel_id)
						.set(PopPolicyRuleAct.COL_INVALID_CAMP_FREQUENCY, invalid_camp_frequency)
						.set(PopPolicyRuleAct.COL_INVALID_CAMP_CONTENT, invalid_camp_content)
						.set(PopPolicyRuleAct.COL_AVOID_CUSTGROUP_IDS, avoid_custgroup_ids).save();

			}

			//保存策略规则与事件规则关系
			String cep_rule_id = getPara("cep_rule_id");
			String cep_rule_desc = getPara("cep_rule_desc");
			String simple_condtions_data = getPara("simple_condtions_data");
			String simple_condtions_desc = getPara("simple_condtions_desc");
			//add by jinl 20160418
			String cep_choosedlacci_desc = getPara("choosedLacciAndCount");
			String lacciAndStationTypeJsons = getPara("lacciAndStationTypeJsons");

			this.getModel(PopPolicyRuleEventCon.class).set(PopPolicyRuleEventCon.COL_ID, PopUtil.generateId())
					.set(PopPolicyRuleEventCon.COL_POLICY_RULE_ID, rule_id)
					.set(PopPolicyRuleEventCon.COL_CEP_RULE_ID, cep_rule_id)
					.set(PopPolicyRuleEventCon.COL_CEP_RULE_DESC, cep_rule_desc)
					.set(PopPolicyRuleEventCon.COL_SIMPLE_CONDTIONS_DATA, simple_condtions_data)
					.set(PopPolicyRuleEventCon.COL_SIMPLE_CONDTIONS_DESC, simple_condtions_desc)
					.set(PopPolicyRuleEventCon.COL_CEP_CHOOSEDLACCI_DESC, cep_choosedlacci_desc)
					.set(PopPolicyRuleEventCon.COL_CEP_LACCIANDSTATIONTYPEJSONS, lacciAndStationTypeJsons).save();

			String model_ids = getPara("model_ids");
			if (StringUtil.isNotEmpty(simple_condtions_data) && StringUtil.isNotEmpty(model_ids)) {
				List<String> sqlList = Lists.newArrayList();
				String[] ids = model_ids.split(",");
				for (int i = 0; i < ids.length; i++) {
					sqlList.add(String
							.format("insert into pop_policy_rule_term_rel values('%s','%s')", ids[i], rule_id));
				}
				Db.batch(sqlList, sqlList.size());
			}

			//保存策略规则与执行时间关系  

			String date_ranges = getPara("date_ranges");
			String time_range = getPara("time_ranges");
			String avoid_range = getPara("avoid_ranges");
			this.getModel(PopPolicyRuleExecTime.class).set(PopPolicyRuleExecTime.COL_POLICY_RULE_ID, rule_id)
					.set(PopPolicyRuleExecTime.COL_ID, PopUtil.generateId())
					.set(PopPolicyRuleExecTime.COL_DATE_RANGES, date_ranges)
					.set(PopPolicyRuleExecTime.COL_TIME_RANGES, time_range)
					.set(PopPolicyRuleExecTime.COL_AVOID_RANGES, avoid_range).save();

			//	add by jinl 20150605
			//更新pccid表状态为1
			PopDimPccId popDimPccId = PopDimPccId.dao().findById(display_pcc_id);
			if (popDimPccId != null) {
				popDimPccId.set(PopDimPccId.use_flag, 1).update();
			}
			map.put("success", "1");
			map.put("ruleName", col_policy_rule_name);
			//记录日志
			String desc = pr.toString();
			LogOperateUtil.log(LogOperateUtil.POLICY_MAN_RULE_ADD,desc,this.getRequest());
		} catch (Exception e) {
			map.put("success", "0");
			map.put("ruleName", col_policy_rule_name);
			map.put("msg", e.getMessage());
			log.error("修改规则报错", e);
			//记录日志
			String desc = String.format("创建策略规则失败:%s",e.getMessage());
			LogOperateUtil.log(LogOperateUtil.POLICY_MAN_RULE_ADD,desc,this.getRequest());
		}
		this.renderJson(map);
	}

	/**
	* @Title: ruleSet 
	* @Description: 跳转规则设置页面   
	* @return void    返回类型 
	* @throws
	 */
	public void ruleSet() {
		List<PopDimAreaType> dimAreaTypes = PopDimAreaType.dao().findAll();
		List<PopDimNetType> dimNetType = PopDimNetType.dao().findAll();
		List<PopDimTerminalInfo> dimTerminalInfo = PopDimTerminalInfo.dao().findAll();
		List<PopDimBusiType> dimBusiType = PopDimBusiType.dao().findAll();
		String cepEventUrl = getCepEventUrl();
		String simple_condtions_data = this.getPara("simple_condtions_data");
		JSONObject obj = null;
		String scd_net_type = "";
		String scd_net_type_name = "";
		String scd_terminal_manu = "";
		String scd_terminal_brand = "";
		String scd_area_type_id = "";
		String scd_area_type_name = "";
		String scd_busi_id = "";
		String scd_busi_name = "";
		String scd_apn_id = "";
		String user_grade = "";
		String user_flow_status = "";
		String user_flow = "";
		String time = "";
		String package_type = "";
		//终端-厂商
		List<Map<String, Object>> manufacturers = Lists.newArrayList();
		List<Map<String, Object>> brands = Lists.newArrayList();
		Stopwatch stopwatch = Stopwatch.createStarted();
		List<Record> manufas = Db
				.findByCache("simple_rule_select", "manufacturer_query",
						"select DISTINCT manufacturer as manufacturer_name from dim_term_info_tac_dm_yyyymm order by manufacturer_name");
		stopwatch.stop();
		long millis = stopwatch.elapsed(TimeUnit.MILLISECONDS);
		log.debug("终端-厂商 查询耗时: {}ms", millis);
		for (Record string : manufas) {
			Map<String, Object> e = Maps.newHashMap();
			e.put("id", string.getStr("manufacturer_name"));
			e.put("name", string.getStr("manufacturer_name"));
			manufacturers.add(e);
		}
		try {
			if (StringUtil.isNotEmpty(simple_condtions_data)) {
				simple_condtions_data = URLDecoder.decode(simple_condtions_data, "utf-8");
				obj = JSONObject.fromObject(simple_condtions_data);
				//接入类型
				scd_net_type = obj.getString("net_type");
				if (StringUtil.isNotEmpty(scd_net_type)) {
					List<Record> records = Db.find("select name from pop_dim_net_type where id in(" + scd_net_type
							+ ")");
					List<String> temp = Lists.newArrayList();
					for (Record object : records) {
						temp.add(object.getStr("name"));
					}
					scd_net_type_name = Joiner.on(',').join(temp);
				}
				if (obj.containsKey("apn")) {
					scd_apn_id = obj.getString("apn");
				}
				if (obj.containsKey("user_grade")) {
					user_grade = obj.getString("user_grade");
				}
				if (obj.containsKey("user_flow_status")) {
					user_flow_status = obj.getString("user_flow_status");
				}
				if (obj.containsKey("user_flow")) {
					user_flow = obj.getString("user_flow");
				}
				if (obj.containsKey("time")) {
					time = obj.getString("time");
				}
				if (obj.containsKey("package_type")) {
					package_type = obj.getString("package_type");
				}
				//终端信息
				if (obj.containsKey("terminal_manu")) {
					scd_terminal_manu = obj.getString("terminal_manu");
					if (StringUtil.isNotEmpty(scd_terminal_manu)) {
						String[] terms = scd_terminal_manu.split(",");
						for (int i = 0; i < terms.length; i++) {
							terms[i] = String.format("'%s'", terms[i]);
						}
						List<String> brand_names = Db
								.query("select DISTINCT brand_name from v_dim_term_info where manufacturer_name in("
										+ Joiner.on(',').join(terms) + ")");
						for (String string : brand_names) {
							Map<String, Object> e = Maps.newHashMap();
							e.put("id", string);
							e.put("name", string);
							brands.add(e);
						}
					}
				}
				scd_terminal_brand = obj.getString("terminal_brand");
				//区域类型
				scd_area_type_id = obj.getString("area_type_id");
				if (StringUtil.isNotEmpty(scd_area_type_id)) {
					List<String> records = Db.query("select name from pop_dim_area_type where id in("
							+ scd_area_type_id + ")");
					scd_area_type_name = Joiner.on(',').join(records);
				}
				//业务
				if (obj.containsKey("busi_id")) {
					scd_busi_id = obj.getString("busi_id");
					if (StringUtil.isNotEmpty(scd_busi_id)) {
						String[] busis = scd_busi_id.split(",");
						for (int i = 0; i < busis.length; i++) {
							busis[i] = String.format("'%s'", busis[i]);
						}
						List<String> records = Db.query("select name from v_dim_busi where id in("
								+ Joiner.on(',').join(busis) + ")");
						scd_busi_name = Joiner.on(',').join(records);
					}
				}
			}
		} catch (Exception e) {
			System.out.println("JSON转化失败");
		}
		setAttr("simple_condtions_data", simple_condtions_data);
		setAttr("scd_net_type", scd_net_type);
		setAttr("scd_net_type_name", scd_net_type_name);
		setAttr("scd_apn_id", scd_apn_id);
		setAttr("scd_terminal_manu", scd_terminal_manu);
		setAttr("scd_terminal_brand", scd_terminal_brand);
		setAttr("scd_area_type_id", scd_area_type_id);
		setAttr("scd_area_type_name", scd_area_type_name);
		setAttr("scd_busi_id", scd_busi_id);
		setAttr("scd_busi_name", scd_busi_name);
		setAttr("dimAreaTypes", dimAreaTypes);
		setAttr("dimNetType", dimNetType);
		setAttr("dimTerminalInfo", dimTerminalInfo);
		setAttr("dimBusiType", dimBusiType);
		setAttr("cepEventUrl", cepEventUrl);
		setAttr("user_grade", user_grade);
		setAttr("user_flow_status", user_flow_status);
		setAttr("user_flow", user_flow);
		setAttr("time", time);
		setAttr("package_type", package_type);
		setAttr("manufacturers", new Gson().toJson(manufacturers));
		setAttr("brands", new Gson().toJson(brands));
		render("policyConfig/ruleSetWindow.jsp");
	}
	
	/**
	 * @Title: chooseLacciInfoWindow
	 * @Description: 选择基站信息
	 * @param     
	 * @return void 
	 * @author jinl
	 * @throws
	 */
	public void chooseLacciInfoWindow() {
		List<PopDimAreaType> dimAreaTypes = PopDimAreaType.dao().findAll();
		List<PopDimNetType> dimNetType = PopDimNetType.dao().findAll();
		List<PopDimTerminalInfo> dimTerminalInfo = PopDimTerminalInfo.dao().findAll();
		List<PopDimBusiType> dimBusiType = PopDimBusiType.dao().findAll();
		String cepEventLacciInfoUrl = getCepEventLacciInfoUrl();
		String simple_condtions_data = this.getPara("simple_condtions_data");
		JSONObject obj = null;
		String scd_net_type = "";
		String scd_net_type_name = "";
		String scd_terminal_manu = "";
		String scd_terminal_brand = "";
		String scd_area_type_id = "";
		String scd_area_type_name = "";
		String scd_busi_id = "";
		String scd_busi_name = "";
		String scd_apn_id = "";
		String user_grade = "";
		String user_flow_status = "";
		String user_flow = "";
		String time = "";
		String package_type = "";
		//终端-厂商
		List<Map<String, Object>> manufacturers = Lists.newArrayList();
		List<Map<String, Object>> brands = Lists.newArrayList();
		Stopwatch stopwatch = Stopwatch.createStarted();
		List<Record> manufas = Db
				.findByCache("simple_rule_select", "manufacturer_query",
						"select DISTINCT manufacturer as manufacturer_name from dim_term_info_tac_dm_yyyymm order by manufacturer_name");
		stopwatch.stop();
		long millis = stopwatch.elapsed(TimeUnit.MILLISECONDS);
		log.debug("终端-厂商 查询耗时: {}ms", millis);
		for (Record string : manufas) {
			Map<String, Object> e = Maps.newHashMap();
			e.put("id", string.getStr("manufacturer_name"));
			e.put("name", string.getStr("manufacturer_name"));
			manufacturers.add(e);
		}
		try {
			if (StringUtil.isNotEmpty(simple_condtions_data)) {
				simple_condtions_data = URLDecoder.decode(simple_condtions_data, "utf-8");
				obj = JSONObject.fromObject(simple_condtions_data);
				//接入类型
				scd_net_type = obj.getString("net_type");
				if (StringUtil.isNotEmpty(scd_net_type)) {
					List<Record> records = Db.find("select name from pop_dim_net_type where id in(" + scd_net_type
							+ ")");
					List<String> temp = Lists.newArrayList();
					for (Record object : records) {
						temp.add(object.getStr("name"));
					}
					scd_net_type_name = Joiner.on(',').join(temp);
				}
				if (obj.containsKey("apn")) {
					scd_apn_id = obj.getString("apn");
				}
				if (obj.containsKey("user_grade")) {
					user_grade = obj.getString("user_grade");
				}
				if (obj.containsKey("user_flow_status")) {
					user_flow_status = obj.getString("user_flow_status");
				}
				if (obj.containsKey("user_flow")) {
					user_flow = obj.getString("user_flow");
				}
				if (obj.containsKey("time")) {
					time = obj.getString("time");
				}
				if (obj.containsKey("package_type")) {
					package_type = obj.getString("package_type");
				}
				//终端信息
				if (obj.containsKey("terminal_manu")) {
					scd_terminal_manu = obj.getString("terminal_manu");
					if (StringUtil.isNotEmpty(scd_terminal_manu)) {
						String[] terms = scd_terminal_manu.split(",");
						for (int i = 0; i < terms.length; i++) {
							terms[i] = String.format("'%s'", terms[i]);
						}
						List<String> brand_names = Db
								.query("select DISTINCT brand_name from v_dim_term_info where manufacturer_name in("
										+ Joiner.on(',').join(terms) + ")");
						for (String string : brand_names) {
							Map<String, Object> e = Maps.newHashMap();
							e.put("id", string);
							e.put("name", string);
							brands.add(e);
						}
					}
				}
				scd_terminal_brand = obj.getString("terminal_brand");
				//区域类型
				scd_area_type_id = obj.getString("area_type_id");
				if (StringUtil.isNotEmpty(scd_area_type_id)) {
					List<String> records = Db.query("select name from pop_dim_area_type where id in("
							+ scd_area_type_id + ")");
					scd_area_type_name = Joiner.on(',').join(records);
				}
				//业务
				if (obj.containsKey("busi_id")) {
					scd_busi_id = obj.getString("busi_id");
					if (StringUtil.isNotEmpty(scd_busi_id)) {
						String[] busis = scd_busi_id.split(",");
						for (int i = 0; i < busis.length; i++) {
							busis[i] = String.format("'%s'", busis[i]);
						}
						List<String> records = Db.query("select name from v_dim_busi where id in("
								+ Joiner.on(',').join(busis) + ")");
						scd_busi_name = Joiner.on(',').join(records);
					}
				}
			}
		} catch (Exception e) {
			System.out.println("JSON转化失败");
		}
		setAttr("simple_condtions_data", simple_condtions_data);
		setAttr("scd_net_type", scd_net_type);
		setAttr("scd_net_type_name", scd_net_type_name);
		setAttr("scd_apn_id", scd_apn_id);
		setAttr("scd_terminal_manu", scd_terminal_manu);
		setAttr("scd_terminal_brand", scd_terminal_brand);
		setAttr("scd_area_type_id", scd_area_type_id);
		setAttr("scd_area_type_name", scd_area_type_name);
		setAttr("scd_busi_id", scd_busi_id);
		setAttr("scd_busi_name", scd_busi_name);
		setAttr("dimAreaTypes", dimAreaTypes);
		setAttr("dimNetType", dimNetType);
		setAttr("dimTerminalInfo", dimTerminalInfo);
		setAttr("dimBusiType", dimBusiType);
		setAttr("cepEventLacciInfoUrl", cepEventLacciInfoUrl);
		setAttr("user_grade", user_grade);
		setAttr("user_flow_status", user_flow_status);
		setAttr("user_flow", user_flow);
		setAttr("time", time);
		setAttr("package_type", package_type);
		setAttr("manufacturers", new Gson().toJson(manufacturers));
		setAttr("brands", new Gson().toJson(brands));
		render("policyConfig/chooseLacciInfoWindow.jsp");
	}
	
	/**
	 * @Title: getCepEventLacciInfoUrl
	 * @Description: 获取查基站信息URL
	 * @param @return    
	 * @return String 
	 * @author jinl
	 * @throws
	 */
	public String getCepEventLacciInfoUrl() {
		PopInterfaceConfig pc = (PopInterfaceConfig) PopInterfaceConfig.dao().findById(
				InterfaceConstant.CALLWS_CEP_LACCI_CODE);
		String url = "";
		String param = "";
		String cepEventUrl = "";
		if (pc != null) {
			url = pc.getStr(PopInterfaceConfig.COL_INTERFACE_ADDRESS);
			param = pc.getStr(PopInterfaceConfig.COL_INTERFACE_PARAM);

			if (StringUtil.isNotEmpty(url)) {
				log.debug("获取复杂事件接口地址");
				cepEventUrl = url + "?" + param;
			} else {
				throw new PopInterfaceException("未配置复杂事件接口[" + InterfaceConstant.CALLWS_CEP_LACCI_CODE + "]的接口地址");
			}

		} else {
			throw new PopInterfaceException("未配置复杂事件接口[" + InterfaceConstant.CALLWS_CEP_LACCI_CODE + "]");
		}

		return cepEventUrl;

	}
	
	/**
	 * @Title: getLACCIInfoExportFile
	 * @Description: 获取导出文件
	 * @param @return    
	 * @return String
	 * @author jinl 
	 * @throws
	 */
	public void getLACCIInfoExportFile() throws IOException{
		
			this.initAttributes();
			HashMap<Object, Object> result = Maps.newHashMap();
			String lacciJson = "";
			if (StringUtil.isNotEmpty(this.getPara("lacciJson"))) {
				lacciJson = this.getPara("lacciJson");
			}
			if("".equals(lacciJson)||lacciJson==null){
				PopPolicyRuleEventCon popPolicyRuleEventCon = PopPolicyRuleEventCon.dao()
						.searchFirst("policy_rule_id", this.getPara("policy_rule_id"));
				lacciJson = popPolicyRuleEventCon.getStr(PopPolicyRuleEventCon.COL_CEP_LACCIANDSTATIONTYPEJSONS);
			}
			//lacci json 格式解析
			JSONObject jsonObject = JSONObject.fromObject(lacciJson);
			net.sf.json.JSONArray lacciJions = jsonObject.getJSONArray("lacciJions");
			List<LacciJsonBean> jsonList = (List<LacciJsonBean>)JSONArray.toCollection(lacciJions, LacciJsonBean.class);
			
			//导出文件
			String resultFilePath = "";
			this.getRequest().setCharacterEncoding("UTF-8");
			this.getResponse().setContentType("text/html; charset=UTF-8");
			PrintWriter out = getResponse().getWriter();
			// 上传文件的保存路径
			String configPath = "jiaoben";
			String dirTemp = "jiaoben/temp";
			String filePath = Configure.getInstance().getProperty("SYS_COMMON_UPLOAD_PATH");
			// 文件保存目录路径
			String savePath = configPath;
			// 临时文件目录
			String tempPath = dirTemp;

			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			String ymd = sdf.format(new java.util.Date());
			savePath += File.separator + ymd + File.separator;
			// 创建文件夹
			File dirFile = new File(filePath+savePath);
			if (!dirFile.exists()) {
				dirFile.mkdirs();
			}
			tempPath += File.separator + ymd + File.separator;
			// 创建临时文件夹
			File dirTempFile = new File(filePath+tempPath);
			if (!dirTempFile.exists()) {
				dirTempFile.mkdirs();
			}

			DiskFileItemFactory factory = new DiskFileItemFactory();
			factory.setSizeThreshold(20 * 1024 * 1024); // 设定使用内存超过5M时，将产生临时文件并存储于临时目录中。
			factory.setRepository(new File(filePath+tempPath)); // 设定存储临时文件的目录。

			ServletFileUpload upload = new ServletFileUpload(factory);
			upload.setHeaderEncoding("UTF-8");
			String plocyid = "";
			String manufacturers = "";
			String FName="";
			String path="";
				String fileName = "基站信息.txt";
					String fileExt = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
					SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
					String newFileName = df.format(new java.util.Date()) + "_"+ new Random().nextInt(1000) + "." + fileExt;
					try {
						path=savePath+newFileName;
						File uploadedFile = new File(filePath+savePath, newFileName);
						OutputStream os = new FileOutputStream(uploadedFile);
						String lacci="";
						String station_type="";
						String temp = "";
						String CI="";
						String LAC="";
						String SAC="";
						String TAC="";
						String ECGI="";
						//输出基站信息
						for(LacciJsonBean lacciJsonBean: jsonList){
							lacci = lacciJsonBean.getLacci();
							station_type =lacciJsonBean.getStation_type();
							/*例如：
							 *"lacci":"576384021","station_type":"2"
							 *--后五位为CI
							 *CI:84021
							 *--剩余的前面部分是LAC
							 *LAC:5763
							 *--拼完结果
							 *2G|LAC:5763|CI:84021; 
							 */
							if("2".equals(station_type)){
								temp = station_type+"G|"+lacci+";";
								CI= lacci.substring(lacci.length()-5, lacci.length());
								LAC=lacci.substring(0, lacci.length()-5);
								temp = station_type+"G|"+"LAC:"+LAC+"|CI:"+CI+";";
							}
							/*例如：
							 *{"lacci":"576384022","station_type":"3"}
							 *3G|LAC:5763|SAC:12345
							 *--后五位
							 *SAC:84022
							 *--前面是LAC
							 *LAC:5763
							 *--拼完结果
							 *3G|LAC:5763|SAC:84021; 
							 */
							if("3".equals(station_type)){
								temp = station_type+"G|"+lacci+";";
								SAC= lacci.substring(lacci.length()-5, lacci.length());
								LAC=lacci.substring(0, lacci.length()-5);
								temp = station_type+"G|"+"LAC:"+LAC+"|SAC:"+SAC+";";
							}
							/*例如：
							 *{"lacci":"576384023","station_type":"4"}
							 *--前五位
							 *TAC:57638
							 *--后面所有的
							 *ECGI:4023
							 *4G|TAC:57638|ECGI:4023;
							 */
							if("4".equals(station_type)){
								temp = station_type+"G|"+lacci+";";
								ECGI= lacci.substring(5, lacci.length());
								TAC=lacci.substring(0, 5);
								temp = station_type+"G|"+"TAC:"+TAC+"|ECGI:"+ECGI+";";
							}
							//输出流
							os.write(temp.getBytes("UTF-8"));
						}
						// 关闭流
						os.flush();
						os.close();
						//文件生成路径
						resultFilePath = filePath+savePath + "/" + newFileName;
						log.debug("文件生成成功！路径：" + resultFilePath);
					} catch (Exception e) {
						log.error("文件生成发生异常", e);
					}finally{
						result.put("success", "1");
						result.put("filename", newFileName);
						result.put("path", filePath+savePath + "/");
						result.put("resultFilePath", resultFilePath);
						renderJson(result);
					}
		
					
	}
	
	/**
	 * @Title: downloadExportFile
	 * @Description: 下载文件
	 * @param @throws IOException    
	 * @return void 
	 * @author jinl
	 * @throws
	 */
	public void downloadExportFile() throws IOException{
		 String filename = this.getRequest().getParameter("filename");
	       String path = this.getRequest().getParameter("path");
	       //path="D:/temp/pop/jiaoben/20160419/";
	       //filename="20160419155018_282.txt";
	       String laccifilepath =this.getRequest().getParameter("laccifilepath");
	       this.getResponse().setHeader("Content-disposition", "attachment;filename="
		              + filename);
		   this.getResponse().setContentType("text/html");
	       BufferedInputStream bis = null;
	         BufferedOutputStream bos = null;
	         try {
	             bis = new BufferedInputStream(new FileInputStream(path+filename));
	             bos = new BufferedOutputStream(this.getResponse().getOutputStream());

	             byte[] buff = new byte[2048];
	             int bytesRead;

	             while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
	                 bos.write(buff, 0, bytesRead);
	             }
	         } catch (final IOException e) {
	             System.out.println("出现IOException." + e);
	         } finally {
	             if (bis != null)
	                 bis.close();
	             if (bos != null)
	                 bos.close();
	         }
	         

	}

	public String getCepEventUrl() {
		PopInterfaceConfig pc = (PopInterfaceConfig) PopInterfaceConfig.dao().findById(
				InterfaceConstant.CALLWS_CEP_EVENT_CODE);
		String url = "";
		String param = "";
		String cepEventUrl = "";
		if (pc != null) {
			url = pc.getStr(PopInterfaceConfig.COL_INTERFACE_ADDRESS);
			param = pc.getStr(PopInterfaceConfig.COL_INTERFACE_PARAM);

			if (StringUtil.isNotEmpty(url)) {
				log.debug("获取复杂事件接口地址");
				cepEventUrl = url + "?" + param;
			} else {
				throw new PopInterfaceException("未配置复杂事件接口[" + InterfaceConstant.CALLWS_CEP_EVENT_CODE + "]的接口地址");
			}

		} else {
			throw new PopInterfaceException("未配置复杂事件接口[" + InterfaceConstant.CALLWS_CEP_EVENT_CODE + "]");
		}

		return cepEventUrl;

	}
	

	/**
	 * @Title: cepLacciInfoCallback
	 * @Description: 获取LACCI信息回调页面
	 * @param     
	 * @return void 
	 * @author jinl
	 * @throws
	 */
	public void cepLacciInfoCallback() {
		render("policyConfig/cepLacciInfoCallback.jsp");
	}

	/**
	* @Title: cepEventRuleCallback 
	* @Description: TODO(调用复制事件后回调)     
	* @return void    返回类型 
	* @throws
	 */
	public void cepEventRuleCallback() {
		render("policyConfig/cepEventRuleCallback.jsp");
	}

	/**
	 * 	删除规则及其下的子规则
	 */
	public void delete() {
		this.initAttributes();
		String rule_id = getPara("rule_id");
		Map<String, Object> result = Maps.newHashMap();
		try {
			cascadeDeleteByPid(rule_id);//级联删除规则下所有子规则
			List<String> sqlList = Lists.newArrayList();
			sqlList.add("delete from pop_policy_rule_custgroup where policy_rule_id='" + rule_id + "'");
			sqlList.add("delete from pop_policy_rule_event_con where policy_rule_id='" + rule_id + "'");
			sqlList.add("delete from pop_policy_rule_event_con where policy_rule_id='" + rule_id + "'");
			sqlList.add("delete from pop_policy_rule_term_rel where policy_rule_id='" + rule_id + "'");
			sqlList.add("delete from pop_policy_rule_exec_time where policy_rule_id='" + rule_id + "'");
			Db.batch(sqlList, sqlList.size());
			//add by jinl 20150605
			//更新pccid表状态为0
			PopDimPccId popDimPccId = PopDimPccId.dao().findById(rule_id);
			if (popDimPccId != null) {
				popDimPccId.set(PopDimPccId.use_flag, 0).update();
			}
			PopPolicyRule.dao().deleteById(rule_id);//删除自身
			result.put("success", "1");
			//记录日志
			String desc = String.format("删除策略规则,策略规则编号:%s", rule_id);
			LogOperateUtil.log(LogOperateUtil.POLICY_MAN_RULE_DEL,desc,this.getRequest());
		} catch (Exception e) {
			result.put("success", "2");
			result.put("msg", e.getMessage());
			log.error("删除规则及其下的子规则发生异常", e);
			//记录日志
			String desc = String.format("删除策略规则失败:%s,策略规则编号:%s",e.getMessage(),rule_id);
			LogOperateUtil.log(LogOperateUtil.POLICY_MAN_RULE_DEL,desc,this.getRequest());
		}
		renderJson(result);
	}

	/**
	 * 通过父规则id级联删除规则
	 * @param pid
	 */
	private void cascadeDeleteByPid(String pid) {
		List<PopPolicyRule> rules = PopPolicyRule.dao().find("select * from pop_policy_rule where parent_id=?", pid);
		for (PopPolicyRule popPolicyRule : rules) {
			String rule_id = popPolicyRule.getStr(PopPolicyRule.COL_ID);
			cascadeDeleteByPid(rule_id);
			List<String> sqlList = Lists.newArrayList();
			sqlList.add("delete from pop_policy_rule_custgroup where policy_rule_id='" + rule_id + "'");
			sqlList.add("delete from pop_policy_rule_event_con where policy_rule_id='" + rule_id + "'");
			sqlList.add("delete from pop_policy_rule_act where policy_rule_id='" + rule_id + "'");
			sqlList.add("delete from pop_policy_rule_exec_time where policy_rule_id='" + rule_id + "'");
			Db.batch(sqlList, sqlList.size());
			popPolicyRule.delete();
		}
	}

	/**
	 * 检查规则名称是否重复
	 */
	public void checkRulePolicyNameRepeat() {
		this.initAttributes();
		HashMap<Object, Object> result = Maps.newHashMap();
		try {
			StringBuilder sql = new StringBuilder("select * from pop_policy_rule where policy_rule_name=?");
			List<Object> paras = Lists.newArrayList();
			paras.add(this.getPara("policy_rule_name"));

			if (StringUtil.isNotEmpty(this.getPara("policy_id"))) {
				sql.append(" and policy_id = ?");
				paras.add(this.getPara("policy_id"));
			}

			if (StringUtil.isNotEmpty(this.getPara("policy_rule_id"))) {
				PopPolicyRule popPolicyRule = PopPolicyRule.dao().searchFirst("id", this.getPara("policy_rule_id"));
				sql.append(" and policy_id =?");
				paras.add(popPolicyRule.get(PopPolicyRule.COL_POLICY_ID));
				sql.append(" and id <> ?");
				paras.add(this.getPara("policy_rule_id"));
			}

			PopPolicyRule popPolicyRule = PopPolicyRule.dao().findFirst(sql.toString(), paras.toArray());
			result.put("success", 1);
			result.put("flag", popPolicyRule != null);
		} catch (Exception e) {
			log.error("检查策略名称是否重复异常", e);
			result.put("success", 2);
		}
		renderJson(result);
	}

	/**
	 * 获取终端信息json(类别,品牌,型号)
	 */
	public void getTerminalJson() {
		List<Map<String, Object>> list = Lists.newArrayList();
		List<PopDimTerminalInfo> popDimTerminalInfoList = null;
		String flag = getPara("flag");
		String parent_id = getPara("parent_id");

		if (flag.equals("terminal_type")) {
			popDimTerminalInfoList = PopDimTerminalInfo.dao().find(
					"SELECT *  FROM pop_dim_terminal_info  GROUP BY type_id");
			for (PopDimTerminalInfo popDimTerminalInfo : popDimTerminalInfoList) {
				Map<String, Object> dataJson = Maps.newHashMap();
				dataJson.put("name", popDimTerminalInfo.get(PopDimTerminalInfo.COL_TYPE_NAME));
				dataJson.put("id", popDimTerminalInfo.get(PopDimTerminalInfo.COL_TYPE_ID));
				list.add(dataJson);
			}
		} else if (flag.equals("terminal_brand")) {
			popDimTerminalInfoList = PopDimTerminalInfo.dao().find(
					"SELECT *  FROM pop_dim_terminal_info where type_id=?  GROUP BY brand_id", parent_id);
			for (PopDimTerminalInfo popDimTerminalInfo : popDimTerminalInfoList) {
				Map<String, Object> dataJson = Maps.newHashMap();
				dataJson.put("name", popDimTerminalInfo.get(PopDimTerminalInfo.COL_BRAND_NAME));
				dataJson.put("id", popDimTerminalInfo.get(PopDimTerminalInfo.COL_BRAND_ID));
				list.add(dataJson);
			}
		} else if (flag.equals("terminal_model")) {
			popDimTerminalInfoList = PopDimTerminalInfo.dao().find(
					"SELECT *  FROM pop_dim_terminal_info where brand_id=?  ", parent_id);
			for (PopDimTerminalInfo popDimTerminalInfo : popDimTerminalInfoList) {
				Map<String, Object> dataJson = Maps.newHashMap();
				dataJson.put("name", popDimTerminalInfo.get(PopDimTerminalInfo.COL_MODEL_NAME));
				dataJson.put("id", popDimTerminalInfo.get(PopDimTerminalInfo.COL_MODEL_ID));
				list.add(dataJson);
			}
		}

		renderJson(list);

	}

	/**
	 * 获取业务类别json(业务大类,业务小类)
	 */
	public void getBusiTypeJson() {
		List<Map<String, Object>> list = Lists.newArrayList();
		List<PopDimBusiType> popDimBusiTypeaList = null;
		String flag = getPara("flag");
		String parent_id = getPara("parent_id");

		if (flag.equals("busi_type")) {
			popDimBusiTypeaList = PopDimBusiType.dao().find("SELECT *  FROM pop_dim_busi_type  where p_id=0");
			for (PopDimBusiType popDimBusiType : popDimBusiTypeaList) {
				Map<String, Object> dataJson = Maps.newHashMap();
				dataJson.put("name", popDimBusiType.get(PopDimBusiType.COL_NAME));
				dataJson.put("id", popDimBusiType.get(PopDimBusiType.COL_ID));
				list.add(dataJson);
			}
		} else if (flag.equals("busi_sub_type")) {
			if (StringUtil.isEmpty(parent_id)) {
				parent_id = null;
			}
			popDimBusiTypeaList = PopDimBusiType.dao().find("SELECT *  FROM pop_dim_busi_type where p_id=?", parent_id);
			for (PopDimBusiType popDimBusiType : popDimBusiTypeaList) {
				Map<String, Object> dataJson = Maps.newHashMap();
				dataJson.put("name", popDimBusiType.get(PopDimBusiType.COL_NAME));
				dataJson.put("id", popDimBusiType.get(PopDimBusiType.COL_ID));
				list.add(dataJson);
			}
		}

		renderJson(list);

	}

	public void searchBrand() {
		String terminal_manu = getPara("terminal_manu");
		List<Map<String, Object>> result = Lists.newArrayList();
		if (StringUtil.isNotEmpty(terminal_manu)) {
			String[] terms = terminal_manu.split(",");
			StringBuilder sb = new StringBuilder();
			for (String string : terms) {
				sb.append(String.format("'%s',", string));
			}
			List<String> brand_names = Db
					.query("select DISTINCT brand_name from v_dim_term_info where manufacturer_name in("
							+ sb.substring(0, sb.length() - 1) + ")");
			for (String string : brand_names) {
				Map<String, Object> e = Maps.newHashMap();
				e.put("id", string);
				e.put("name", string);
				result.add(e);
			}
		}
		renderJson(result);
	}

	/**
	 * 查询规则关联的终端
	 */
	public void searchTerm() {
		String rule_id = getPara("rule_id");
		List<Map<String, Object>> result = Lists.newArrayList();
		if (StringUtil.isNotEmpty(rule_id)) {
			List<Record> terms = Db
					.find("select * from v_dim_term_info where model_id in(select term_id from pop_policy_rule_term_rel where policy_rule_id = ?)",
							new Object[] { rule_id });
			for (Record record : terms) {
				Map<String, Object> e = Maps.newHashMap();
				e.put("id", record.getStr("model_id"));
				e.put("name", record.getStr("model_name"));
				result.add(e);
			}
		}
		renderJson(result);
	}

	/**
	 * 查询规则关联的区域
	 */
	public void searchArea() {
		String area_type_id = getPara("area_type_id");
		List<Map<String, Object>> result = Lists.newArrayList();
		if (StringUtil.isNotEmpty(area_type_id)) {
			List<Record> terms = Db
					.find("select lacci,lacci_name from pop_dim_area_type_lacci_rel where area_type_id in("
							+ area_type_id + ")");
			for (Record record : terms) {
				Map<String, Object> e = Maps.newHashMap();
				e.put("id", record.getStr("lacci"));
				e.put("name", record.getStr("lacci_name"));
				result.add(e);
			}
		}
		renderJson(result);
	}
	
	/**
	 * @Title: getLACCIInfo
	 * @Description: POP获取ICEP基站LACCI列表
	 * @author: jinlong
	 * @param @param sysId
	 * @param @param eplId
	 * @param @param pageSize
	 * @param @param page
	 * @param @return    
	 * @return String 
	 * @throws
	 */
	public String getLACCIInfo(String sysId, String eplId, int pageSize, int page){
        ICepWsService iCepWsService = null;
        String jsonObj ="";
		try {
			iCepWsService = (ICepWsService) SystemServiceLocator.getInstance().getService("iCepWsService");
			log.info("configTimeout设置超时时间开始");
			configTimeout(iCepWsService);
			log.info("configTimeout设置超时时间成功");
			jsonObj = iCepWsService.queryEplLacciInfo(sysId, eplId, pageSize, page);
		} catch (Exception e) {
			log.error("获取ICEP的LACCI信息失败",e);
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        log.info("获取ICEP的LACCI信息成功返回信息："+jsonObj);
		return jsonObj;
	}
	
	/**
	 * @Title: showLACCIInfo
	 * @Description: POP显示ICEP基站LACCI列表
	 * @param     
	 * @return void 
	 * @throws
	 */
	public void showLACCIInfo() {
		String eplId = this.getPara("eplId");//例如："20150906165252138272"
		String pageStr = this.getPara("page");
		int page = 1;
		if(pageStr!=null){
			page =   Integer.parseInt(pageStr);
		}
		
		String  jsonObject = getLACCIInfo(sysId, eplId, pageSize, page);//调用获取ICEP基站信息接口方法
		JSONObject obj = null;
		if(!"".equals(jsonObject)&&jsonObject!=null){
			obj = JSONObject.fromObject(jsonObject);
			String code = obj.getString("code");
			if("0".equals(code)){
				String data = obj.getString("data");
				log.info("获取ICEP查询LACCI基站方法返回data数据："+data);
				if("[]".equals(data)){
					log.info("获取ICEP查询LACCI基站方法返回data数据：[]");	
				}else{
				int totalRow = obj.getInt("totalRow");
				int totalPage = obj.getInt("totalPage");
				Gson gson = new Gson();
				List<LacciBean> LacciBeanList = gson.fromJson(data, new TypeToken<List<LacciBean>>(){}.getType());
				for(int i = 0; i < LacciBeanList.size() ; i++)
				{
					LacciBean lacciBean = LacciBeanList.get(i);
					log.info("Lacci=="+lacciBean.getLacci()+"Lacci_name()"+lacciBean.getLacci_name());
				}
				Page<LacciBean> pageList = new Page(LacciBeanList, page, pageSize, totalPage, totalRow);
				setAttr("pageList",pageList);
				setAttr("searchParams", "eplId="+eplId);
				setAttr("eplId",eplId);
				setAttr("LacciBeanList", LacciBeanList);
				setAttr("jsonObject", jsonObject);
				}
			}else if("1".equals(code)){
				log.info("获取ICEP查询LACCI基站方法返回data数据为空数组[]");
			}else{
				log.info("获取ICEP查询LACCI基站方法返回状态码code无法确认，其非0非1");
			}
		}else{
			log.info("获取ICEP查询LACCI基站方法的返回json格式信息为空字符串");
		}
		render("policyConfig/showLACCIInfo.jsp");
	}
	
	public static final int CXF_CLIENT_CONNECT_TIMEOUT = 60 * 60 * 1000;
	public static final int CXF_CLIENT_RECEIVE_TIMEOUT = 60 * 60 * 1000;

	public static void configTimeout(Object service) {
		Client proxy = ClientProxy.getClient(service);
		HTTPConduit conduit = (HTTPConduit) proxy.getConduit();
		HTTPClientPolicy policy = new HTTPClientPolicy();
		policy.setConnectionTimeout(CXF_CLIENT_CONNECT_TIMEOUT);
		policy.setReceiveTimeout(CXF_CLIENT_RECEIVE_TIMEOUT);
		conduit.setClient(policy);
	}


}
