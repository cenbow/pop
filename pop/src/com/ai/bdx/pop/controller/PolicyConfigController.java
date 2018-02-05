package com.ai.bdx.pop.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ai.bdx.frame.approval.service.IApprovalService;
import com.ai.bdx.frame.privilegeServiceExt.service.IUserPrivilegeCommonService;
import com.ai.bdx.pop.base.PopController;
import com.ai.bdx.pop.bean.PopPolicyRuleBean;
import com.ai.bdx.pop.enums.PolicyStatus;
import com.ai.bdx.pop.model.PopDimPolicyLevel;
import com.ai.bdx.pop.model.PopDimPolicyType;
import com.ai.bdx.pop.model.PopPolicyBaseinfo;
import com.ai.bdx.pop.model.PopPolicyRule;
import com.ai.bdx.pop.model.PopPolicyRuleAct;
import com.ai.bdx.pop.model.PopPolicyRuleCustgroup;
import com.ai.bdx.pop.model.PopPolicyRuleEventCon;
import com.ai.bdx.pop.model.PopPolicyRuleExecTime;
import com.ai.bdx.pop.service.SmsService;
import com.ai.bdx.pop.util.ApprovalCONST;
import com.ai.bdx.pop.util.CityId2CityNameUtil;
import com.ai.bdx.pop.util.LogOperateUtil;
import com.ai.bdx.pop.util.PopUtil;
import com.asiainfo.biframe.privilege.ICity;
import com.asiainfo.biframe.utils.date.DateUtil;
import com.asiainfo.biframe.utils.spring.SystemServiceLocator;
import com.asiainfo.biframe.utils.string.StringUtil;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;

/**
 * 策略配置控制层
 * @author lixq8
 */
public class PolicyConfigController extends PopController {
	private static final Logger log = LogManager.getLogger();

	/**
	 * 策略场景创建初始化
	 * @throws Exception 
	 */
	public void addInit() throws Exception {
		initAttributes();
		String from = getPara("from");
		String template_id = getPara("template_id");
		String city_priority = "";
		String city_priority_name = "";
		log.debug("策略场景创建初始化...");
		//add by jinl start
		IUserPrivilegeCommonService service = (IUserPrivilegeCommonService) SystemServiceLocator.getInstance().getService(PRIVILEGE_SERVICE_NAME);
		List<ICity> iCityList = service.getAllCity();
		setAttr("iCityList", iCityList);
		
		//add by jinl end
		List<PopDimPolicyType> dimPolicyTypes = PopDimPolicyType.dao().findAll();//所有策略类型
		List<PopDimPolicyLevel> dimPolicyLevels = PopDimPolicyLevel.dao().findAll();//所有策略等级
		List<PopPolicyBaseinfo> policyTemplates = PopPolicyBaseinfo
				.dao()
				.find("select * from pop_policy_baseinfo where template_flag=2 or (template_flag=1 and create_user_id=?) order by create_time desc",
						this.user.getUserid());//所有策略配置模板
		setAttr("dimPolicyTypes", dimPolicyTypes);
		setAttr("dimPolicyLevels", dimPolicyLevels);
		setAttr("policyTemplates", policyTemplates);
		if ("template".equals(from) && StringUtil.isNotEmpty(template_id)) {
			PopPolicyBaseinfo model = PopPolicyBaseinfo.dao().findById(template_id);
			List<PopPolicyRuleBean> rules = getAllPolicyRulesByPid(template_id, model);
			model.set(PopPolicyBaseinfo.COL_ID, null);
			model.set(PopPolicyBaseinfo.COL_POLICY_STATUS_ID, null);
			model.set(PopPolicyBaseinfo.COL_TEMPLATE_FLAG, null);
			//有效期自动设置为 开始时间:当前日期, 结束时间:当前日期+模板有效期间隔
			String valid_start_time = DateUtil.date2String(new Date(), DateUtil.YYYY_MM_DD);
			Date startTime = DateUtil.string2Date(
					DateUtil.date2String(model.getDate(PopPolicyBaseinfo.COL_START_TIME), DateUtil.YYYY_MM_DD),
					DateUtil.YYYY_MM_DD);
			Date endTime = DateUtil.string2Date(
					DateUtil.date2String(model.getDate(PopPolicyBaseinfo.COL_END_TIME), DateUtil.YYYY_MM_DD),
					DateUtil.YYYY_MM_DD);
			long interval = endTime.getTime() - startTime.getTime();
			String valid_end_time = DateUtil
					.date2String(new Date(new Date().getTime() + interval), DateUtil.YYYY_MM_DD);
			setAttr("valid_start_time", valid_start_time);
			setAttr("valid_end_time", valid_end_time);
			String policyName = model.getStr(PopPolicyBaseinfo.COL_POLICY_NAME);
			model.set(PopPolicyBaseinfo.COL_POLICY_NAME,
					policyName + DateUtil.date2String(new Date(), "yyyyMMddHHmmss"));
			
			city_priority = model.getStr(PopPolicyBaseinfo.COL_CITY_PRIORITY);
			city_priority_name = CityId2CityNameUtil.getCityNameByCache(city_priority);
			setAttr("model", model);
			setAttr("template_id", template_id);
			setAttr("rules", rules);
		}
		setAttr("opt", "add");
		setAttr("city_priority",city_priority);
		setAttr("city_priority_name",city_priority_name);
		render("policyConfig/policySceneCreate.jsp");
	}

	/**
	 * 预编辑基本信息或查看
	 * @throws Exception 
	 */
	public void edit() throws Exception {
		this.initAttributes();
		String id = this.getPara("id");
		String opt = "view";
		PopPolicyBaseinfo model = PopPolicyBaseinfo.dao().findById(id);
		//格式化显示有效期：生效时间，失效时间 
		String valid_start_time = DateUtil.date2String(model.getDate("start_time"), DateUtil.YYYY_MM_DD);
		String valid_end_time = DateUtil.date2String(model.getDate("end_time"), DateUtil.YYYY_MM_DD);
		setAttr("valid_start_time", valid_start_time);
		setAttr("valid_end_time", valid_end_time);
		setAttr("model", model);
		List<PopDimPolicyType> dimPolicyTypes = PopDimPolicyType.dao().findAll();
		List<PopDimPolicyLevel> dimPolicyLevels = PopDimPolicyLevel.dao().findAll();
		//add by jinl start
		IUserPrivilegeCommonService service = (IUserPrivilegeCommonService) SystemServiceLocator.getInstance().getService(PRIVILEGE_SERVICE_NAME);
		List<ICity> iCityList = service.getAllCity();
		setAttr("iCityList", iCityList);
		String city_priority = model.getStr("city_priority");
		String city_priority_name = "";
		if(city_priority!=null){
			for (Integer i = 0; i < iCityList.size(); i++) {
				if(city_priority.equals(iCityList.get(i).getCityId())){
					city_priority_name = iCityList.get(i).getCityName();
				};
			}
		}
		setAttr("city_priority", city_priority);
		setAttr("city_priority_name", city_priority_name);
		setAttr("dimPolicyTypes", dimPolicyTypes);
		setAttr("dimPolicyLevels", dimPolicyLevels);

		List<PopPolicyRuleBean> rules = getAllPolicyRulesByPid(id, model);
		setAttr("rules", rules);

		//编辑中状态、本人或超级管理员可编辑
		if (model.getInt(PopPolicyBaseinfo.COL_POLICY_STATUS_ID).shortValue() == PolicyStatus.EDITING.getValue()) {//编辑中状态
			if (model.getStr(PopPolicyBaseinfo.COL_CREATE_USER_ID).equals(this.userId)) {//本人
				opt = "edit";
			} else {
				if (service.isAdminUser(userId)) {//超级管理员
					opt = "edit";
				} else {
					log.debug("策略配置[{}],{}既不是本人又不是管理员,禁止编辑操作", model, this.user.getUserid());
				}
			}
		}

		setAttr("opt", opt);
		render("policyConfig/policySceneCreate.jsp");
	}

	/**
	 * 查看
	 * @throws Exception
	 */
	public void view() throws Exception {
		this.initAttributes();
		String id = this.getPara("id");
		String opt = "view";
		PopPolicyBaseinfo model = PopPolicyBaseinfo.dao().findById(id);
		if(StringUtil.isNotEmpty(model)){
			//格式化显示有效期：生效时间，失效时间 
			String valid_start_time = DateUtil.date2String(model.getDate("start_time"), DateUtil.YYYY_MM_DD);
			String valid_end_time = DateUtil.date2String(model.getDate("end_time"), DateUtil.YYYY_MM_DD);
			setAttr("valid_start_time", valid_start_time);
			setAttr("valid_end_time", valid_end_time);
			setAttr("model", model);
			List<PopDimPolicyType> dimPolicyTypes = PopDimPolicyType.dao().findAll();
			List<PopDimPolicyLevel> dimPolicyLevels = PopDimPolicyLevel.dao().findAll();
			setAttr("dimPolicyTypes", dimPolicyTypes);
			setAttr("dimPolicyLevels", dimPolicyLevels);
			//add by jinl start
			IUserPrivilegeCommonService service = (IUserPrivilegeCommonService) SystemServiceLocator.getInstance().getService(PRIVILEGE_SERVICE_NAME);
			List<ICity> iCityList = service.getAllCity();
			setAttr("iCityList", iCityList);
			String city_priority = model.getStr("city_priority");
			String city_priority_name = "";
			if(city_priority!=null){
				for (Integer i = 0; i < iCityList.size(); i++) {
					if(city_priority.equals(iCityList.get(i).getCityId())){
						city_priority_name = iCityList.get(i).getCityName();
					};
				}
			}
			setAttr("city_priority_name", city_priority_name);
			List<PopPolicyRuleBean> rules = getAllPolicyRulesByPid(id, model);
			setAttr("rules", rules);
			setAttr("opt", opt);
		}
		render("policyConfig/policySceneCreate.jsp");
	}

	/**
	 * 查询策略配置下所有的配置规则
	 * @param pid 查询策略配置基本信息id
	 * @param baseinfo 
	 * @return
	 */
	private List<PopPolicyRuleBean> getAllPolicyRulesByPid(String pid, PopPolicyBaseinfo baseinfo) {
		List<PopPolicyRuleBean> ruleBeans = Lists.newArrayList();
		String sql = null;
		if (pid.equals(baseinfo.get(PopPolicyBaseinfo.COL_ID))) {//递归首次 查询规则
			sql = "select * from pop_policy_rule where (parent_id is null or parent_id ='') and policy_id=?";
		} else {//查规则下子规则
			sql = "select * from pop_policy_rule where parent_id=?";
		}
		List<PopPolicyRule> rules = PopPolicyRule.dao().find(sql, new Object[] { pid });
		for (PopPolicyRule popPolicyRule : rules) {
			String id = popPolicyRule.getStr(PopPolicyRule.COL_ID);
			PopPolicyRuleBean ruleBean = new PopPolicyRuleBean();
			ruleBean.setId(id);
			ruleBean.setPolicyRuleName(popPolicyRule.getStr(PopPolicyRule.COL_POLICY_RULE_NAME));
			ruleBean.setParentId(popPolicyRule.getStr(PopPolicyRule.COL_PARENT_ID));
			ruleBean.setPolicyId(popPolicyRule.getStr(PopPolicyRule.COL_POLICY_ID));

			PopPolicyRuleAct act = PopPolicyRuleAct.dao().findFirst(
					"select * from pop_policy_rule_act where policy_rule_id=?", new Object[] { id });
			ruleBean.setAct(act);
			PopPolicyRuleCustgroup custgroup = PopPolicyRuleCustgroup.dao().findFirst(
					"select * from pop_policy_rule_custgroup where policy_rule_id=?", new Object[] { id });
			ruleBean.setCustgroup(custgroup);
			PopPolicyRuleEventCon eventCon = PopPolicyRuleEventCon.dao().findFirst(
					"select * from pop_policy_rule_event_con where policy_rule_id=?", new Object[] { id });
			if (act != null) {
				int policy_act_type_id = act.getInt(PopPolicyRuleAct.COL_POLICY_ACT_TYPE_ID);
				if (policy_act_type_id == 1) {//管控类
					Record act_type = Db
							.findFirst(
									"select t2.name act_type_name,t3.name control_type_name,t1.control_param from pop_policy_rule_act t1, pop_dim_action_type t2, pop_dim_control_type t3 where policy_rule_id=?  and t1.policy_act_type_id=t2.id and t1.control_type_id=t3.id",
									new Object[] { id });
					if (act_type != null) {
						StringBuilder actionTypeName = new StringBuilder(act_type.getStr("act_type_name"))
								.append(": [");
						actionTypeName.append(
								Joiner.on("-").skipNulls()
										.join(act_type.getStr("control_type_name"), act_type.getInt("control_param")))
								.append("]");
						ruleBean.setActionTypeName(actionTypeName.toString());
					}
				} else if (policy_act_type_id == 2) {//营销类
					StringBuilder actionTypeName = new StringBuilder();
					//执行通知
					String exec_camp_content = act.getStr(PopPolicyRuleAct.COL_EXEC_CAMP_CONTENT);
					if (StringUtil.isNotEmpty(exec_camp_content)) {
						Record act_type = Db
								.findFirst(
										"select t2.name act_type_name,t3.name camp_channel_name,t4.name contact_freq_name from pop_policy_rule_act t1, pop_dim_action_type t2, pop_dim_camp_channel t3,pop_dim_contact_freq t4 where policy_rule_id=?  and t1.policy_act_type_id=t2.id and t1.exec_channel_id=t3.id and t1.exec_camp_frequency=t4.id",
										new Object[] { id });
						if (act_type != null) {
							actionTypeName.append(act_type.getStr("act_type_name") + ": ").append("[执行通知:");
							actionTypeName.append(
									Joiner.on("-")
											.skipNulls()
											.join(act_type.getStr("camp_channel_name"),
													act_type.getStr("contact_freq_name"), exec_camp_content)).append(
									"]");
						}
					}
					//失效通知
					String invalid_camp_content = act.getStr(PopPolicyRuleAct.COL_INVALID_CAMP_CONTENT);
					if (StringUtil.isNotEmpty(invalid_camp_content)) {
						Record act_type = Db
								.findFirst(
										"select t2.name act_type_name,t3.name camp_channel_name,t4.name contact_freq_name from pop_policy_rule_act t1, pop_dim_action_type t2, pop_dim_camp_channel t3,pop_dim_contact_freq t4 where policy_rule_id=?  and t1.policy_act_type_id=t2.id and t1.invalid_channel_id=t3.id and t1.invalid_camp_frequency=t4.id",
										new Object[] { id });
						if (act_type != null) {
							if (actionTypeName.length() == 0) {
								actionTypeName.append(act_type.getStr("act_type_name") + ": ").append("[执行通知:");
							} else {
								actionTypeName.append(", [失效通知:");
							}
							actionTypeName.append(
									Joiner.on("-")
											.skipNulls()
											.join(act_type.getStr("camp_channel_name"),
													act_type.getStr("contact_freq_name"), invalid_camp_content))
									.append("]");
						}
					}
					ruleBean.setActionTypeName(actionTypeName.toString());
				}
			}
			ruleBean.setEventCon(eventCon);
			ruleBeans.add(ruleBean);//添加规则 
			ruleBeans.addAll(getAllPolicyRulesByPid(id, baseinfo));//递归添加子规则
		}
		return ruleBeans;
	}

	/**
	 * 新建或修改策略配置基本信息
	 */
	public void saveBaseInfo() {
		this.initAttributes();
		String id = this.getPara("popPolicyBaseinfo.id");
		String template_id = this.getPara("template_id");
		HashMap<Object, Object> result = Maps.newHashMap();
		try {
			if (StringUtil.isEmpty(id)) {//新建或从模板新建
				String newId = PopUtil.generateId();
				PopPolicyBaseinfo popPolicyBaseinfo = this.getModel(PopPolicyBaseinfo.class);
				popPolicyBaseinfo.set(PopPolicyBaseinfo.COL_ID, newId)
						.set(PopPolicyBaseinfo.COL_START_TIME, getParaToDate("start_time"))
						.set(PopPolicyBaseinfo.COL_END_TIME, getParaToDate("end_time"))
						.set(PopPolicyBaseinfo.COL_TEMPLATE_FLAG, getParaToInt("template_flag", 0))
						.set(PopPolicyBaseinfo.COL_CREATE_USER_ID, user.getUserid())
						.set(PopPolicyBaseinfo.COL_CREATE_TIME, new java.util.Date())
						.set(PopPolicyBaseinfo.COL_POLICY_STATUS_ID, PolicyStatus.EDITING.getValue())
						.set(PopPolicyBaseinfo.COL_CITY_ID, user.getCityid());
				if (StringUtil.isNotEmpty(template_id)) {
					//从策略配置复制规则信息
					copyPolicyRules(template_id, newId, user.getUserid());
				}
				popPolicyBaseinfo.save();
				log.debug("策略创建:{}", popPolicyBaseinfo.toString());

				result.put("success", 1);
				result.put("id", newId);
				//记录日志
				String desc = popPolicyBaseinfo.toString();
				LogOperateUtil.log(LogOperateUtil.POLICY_MAN_ADD,desc,this.getRequest());
			} else {//修改
				PopPolicyBaseinfo popPolicyBaseinfo = PopPolicyBaseinfo.dao().findById(id);
				String oldDesc =  popPolicyBaseinfo.toString();
				//修改前状态检查,只有编辑中可修改
				if (popPolicyBaseinfo.getInt(PopPolicyBaseinfo.COL_POLICY_STATUS_ID) == PolicyStatus.EDITING.getValue()) {
					PopPolicyBaseinfo _popPolicyBaseinfo = this.getModel(PopPolicyBaseinfo.class);//表单提交要修改的信息
					popPolicyBaseinfo
							.set(PopPolicyBaseinfo.COL_POLICY_NAME,
									_popPolicyBaseinfo.get(PopPolicyBaseinfo.COL_POLICY_NAME))
							.set(PopPolicyBaseinfo.COL_POLICY_DESC,
									_popPolicyBaseinfo.get(PopPolicyBaseinfo.COL_POLICY_DESC))
							.set(PopPolicyBaseinfo.COL_START_TIME, getParaToDate("start_time"))
							.set(PopPolicyBaseinfo.COL_END_TIME, getParaToDate("end_time"))
							.set(PopPolicyBaseinfo.COL_POLICY_TYPE_ID,
									_popPolicyBaseinfo.get(PopPolicyBaseinfo.COL_POLICY_TYPE_ID))
							.set(PopPolicyBaseinfo.COL_POLICY_LEVEL_ID,
									_popPolicyBaseinfo.get(PopPolicyBaseinfo.COL_POLICY_LEVEL_ID))
							.set(PopPolicyBaseinfo.COL_CUSTOM_SERVICE_DIAMETER, 
									_popPolicyBaseinfo.get(PopPolicyBaseinfo.COL_CUSTOM_SERVICE_DIAMETER))
							.set(PopPolicyBaseinfo.COL_BUSINESS_PRIORITY, 
											_popPolicyBaseinfo.get(PopPolicyBaseinfo.COL_BUSINESS_PRIORITY))
							.set(PopPolicyBaseinfo.COL_CITY_PRIORITY,_popPolicyBaseinfo.get(PopPolicyBaseinfo.COL_CITY_PRIORITY)).update();

					log.debug("修改策略配置基本信息:{}", popPolicyBaseinfo.toString());
					result.put("success", 1);
					result.put("id", popPolicyBaseinfo.get(PopPolicyBaseinfo.COL_ID));
					//记录日志
					String desc = popPolicyBaseinfo.toString();
					desc = String.format("修改前的数据:%s\n,修改后的数据:%s",oldDesc,desc);
					LogOperateUtil.log(LogOperateUtil.POLICY_MAN_UPDATE,desc,this.getRequest());
				} else {
					PolicyStatus status = PolicyStatus.valueOf(popPolicyBaseinfo.getInt(
							PopPolicyBaseinfo.COL_POLICY_STATUS_ID).shortValue());
					result.put("success", 2);
					result.put("msg", String.format("信息当前状态为:%s,禁止修改", status.getName()));
				}
			}
		} catch (Exception e) {
			log.error("保存策略配置基本信息异常:" + e.getMessage(), e);
			result.put("success", 2);
			result.put("msg", e.getMessage());
			String desc = String.format("保存策略配置基本信息失败:%s", e.getMessage());
			LogOperateUtil.log(LogOperateUtil.POLICY_MAN_ADD,desc,this.getRequest());
		}

		renderJson(result);
	}

	/**
	 * 保存成模板
	 */
	public void saveTemplate() {
		this.initAttributes();
		String id = this.getPara("popPolicyBaseinfo.id");
		HashMap<Object, Object> result = Maps.newHashMap();
		String newId = null;
		try {
			newId = PopUtil.generateId();
			PopPolicyBaseinfo popPolicyBaseinfo = this.getModel(PopPolicyBaseinfo.class);
			popPolicyBaseinfo.set(PopPolicyBaseinfo.COL_ID, newId)
					.set(PopPolicyBaseinfo.COL_START_TIME, getParaToDate("start_time"))
					.set(PopPolicyBaseinfo.COL_END_TIME, getParaToDate("end_time"))
					.set(PopPolicyBaseinfo.COL_TEMPLATE_FLAG, getParaToInt("template_flag"))
					.set(PopPolicyBaseinfo.COL_CREATE_USER_ID, user.getUserid())
					.set(PopPolicyBaseinfo.COL_CREATE_TIME, new java.util.Date())
					.set(PopPolicyBaseinfo.COL_POLICY_STATUS_ID, PolicyStatus.EDITING.getValue());
			//从策略配置复制规则信息
			copyPolicyRules(id, newId, user.getUserid());
			popPolicyBaseinfo.save();
			result.put("success", 1);
			result.put("id", newId);
			log.debug("策略模板创建:{}", popPolicyBaseinfo.toString());
			//记录日志
			String desc = popPolicyBaseinfo.toString();
			LogOperateUtil.log(LogOperateUtil.POLICY_MAN_TEM_ADD,desc,this.getRequest());
		} catch (Exception e) {
			String desc = String.format("策略模板创建失败:%s",e.getMessage());
			LogOperateUtil.log(LogOperateUtil.POLICY_MAN_TEM_ADD,desc,this.getRequest());
			log.error("保存成模板异常:" + e.getMessage(), e);
			result.put("success", 2);
			result.put("msg", e.getMessage());
		}
		renderJson(result);
	}

	/**
	 * 复制规则及其下所有子规则
	 * @param sourceId 源策略配置基本信息id
	 * @param destId	要新生成的源策略配置基本信息id
	 * @param currentUserId 当前登录用户id
	 */
	private void copyPolicyRules(String sourceId, String destId, String currentUserId) throws Exception {
		List<PopPolicyRule> rules = PopPolicyRule.dao().find(
				"select * from pop_policy_rule where policy_id=? and (parent_id is null or parent_id ='')", sourceId);
		for (PopPolicyRule popPolicyRule : rules) {
			String oldRuleId = popPolicyRule.getStr(PopPolicyRule.COL_ID);
			String newRuleId = PopUtil.generateId();
			PopPolicyRule newRule = new PopPolicyRule();
			newRule.set(PopPolicyRule.COL_ID, newRuleId);
			newRule.set(PopPolicyRule.COL_CREATE_TIME, new java.util.Date());
			newRule.set(PopPolicyRule.COL_CREATE_USER_ID, currentUserId);
			newRule.set(PopPolicyRule.COL_PARENT_ID, "");
			newRule.set(PopPolicyRule.COL_POLICY_ID, destId);
			newRule.set(PopPolicyRule.COL_POLICY_RULE_NAME, popPolicyRule.getStr(PopPolicyRule.COL_POLICY_RULE_NAME));
			newRule.set(PopPolicyRule.COL_RULE_PRIORITY, popPolicyRule.getInt(PopPolicyRule.COL_RULE_PRIORITY));
			newRule.set(PopPolicyRule.COL_COLLISION_DETECTION_POLICY_ID,
					popPolicyRule.getStr(PopPolicyRule.COL_COLLISION_DETECTION_POLICY_ID));
			newRule.set(PopPolicyRule.COL_RULE_STATUS, PolicyStatus.EDITING.getValue());
			newRule.set(PopPolicyRule.COL_PCCID, popPolicyRule.getStr(PopPolicyRule.COL_PCCID));
			copySubPolicyRules(popPolicyRule, newRule);//复制子规则
			copyRuleLinkData(oldRuleId, newRuleId);//复制规则关联的数据(策略规则与客户群关系、策略规则与执行动作（管控/营销）关系、策略规则与事件规则关系、策略规则与执行时间关系)
			newRule.save();//保存规则
		}
	}

	//复制子规则
	private void copySubPolicyRules(PopPolicyRule soucre, PopPolicyRule dest) throws Exception {
		List<PopPolicyRule> rules = PopPolicyRule.dao().find("select * from pop_policy_rule where parent_id= ?",
				soucre.get(PopPolicyRule.COL_ID));
		for (PopPolicyRule popPolicyRule : rules) {
			String oldRuleId = popPolicyRule.getStr(PopPolicyRule.COL_ID);
			String newRuleId = PopUtil.generateId();
			PopPolicyRule newRule = new PopPolicyRule();
			newRule.set(PopPolicyRule.COL_ID, newRuleId);
			newRule.set(PopPolicyRule.COL_CREATE_TIME, dest.get(PopPolicyRule.COL_CREATE_TIME));
			newRule.set(PopPolicyRule.COL_CREATE_USER_ID, dest.get(PopPolicyRule.COL_CREATE_USER_ID));
			newRule.set(PopPolicyRule.COL_PARENT_ID, dest.get(PopPolicyRule.COL_ID));
			newRule.set(PopPolicyRule.COL_POLICY_ID, dest.get(PopPolicyRule.COL_POLICY_ID));
			newRule.set(PopPolicyRule.COL_POLICY_RULE_NAME, popPolicyRule.getStr(PopPolicyRule.COL_POLICY_RULE_NAME));
			newRule.set(PopPolicyRule.COL_RULE_PRIORITY, popPolicyRule.getInt(PopPolicyRule.COL_RULE_PRIORITY));
			newRule.set(PopPolicyRule.COL_COLLISION_DETECTION_POLICY_ID,
					popPolicyRule.getStr(PopPolicyRule.COL_COLLISION_DETECTION_POLICY_ID));
			newRule.set(PopPolicyRule.COL_RULE_STATUS, PolicyStatus.EDITING.getValue());
			newRule.set(PopPolicyRule.COL_PCCID, popPolicyRule.getStr(PopPolicyRule.COL_PCCID));
			copySubPolicyRules(popPolicyRule, newRule);//复制子规则
			copyRuleLinkData(oldRuleId, newRuleId);//复制规则关联的数据(策略规则与客户群关系、策略规则与执行动作（管控/营销）关系、策略规则与事件规则关系、策略规则与执行时间关系)
			newRule.save();//保存规则
		}
	}

	//复制规则关联的数据(策略规则与客户群关系、策略规则与执行动作（管控/营销）关系、策略规则与事件规则关系、策略规则与执行时间关系)
	private void copyRuleLinkData(String oldRuleId, String newRuleId) throws Exception {
		//复制策略规则与客户群关系
		PopPolicyRuleCustgroup popPolicyRuleCustgroup = PopPolicyRuleCustgroup.dao().searchFirst("policy_rule_id",
				oldRuleId);
		if (popPolicyRuleCustgroup != null) {
			PopPolicyRuleCustgroup newCustGroup = new PopPolicyRuleCustgroup();
			newCustGroup.copyBean(popPolicyRuleCustgroup);
			newCustGroup.set(PopPolicyRuleCustgroup.COL_ID, PopUtil.generateId());
			newCustGroup.set(PopPolicyRuleCustgroup.COL_POLICY_RULE_ID, newRuleId);
			newCustGroup.save();
		}
		//复制策略规则与执行动作（管控/营销）关系
		PopPolicyRuleAct popPolicyRuleAct = PopPolicyRuleAct.dao().searchFirst("policy_rule_id", oldRuleId);
		if (popPolicyRuleAct != null) {
			PopPolicyRuleAct newRuleAct = new PopPolicyRuleAct();
			newRuleAct.copyBean(popPolicyRuleAct);
			newRuleAct.set(PopPolicyRuleAct.COL_ID, PopUtil.generateId());
			newRuleAct.set(PopPolicyRuleAct.COL_POLICY_RULE_ID, newRuleId);
			newRuleAct.save();
		}
		//复制策略规则与事件规则关系
		PopPolicyRuleEventCon popPolicyRuleEventCon = PopPolicyRuleEventCon.dao().searchFirst("policy_rule_id",
				oldRuleId);
		if (popPolicyRuleEventCon != null) {
			PopPolicyRuleEventCon newRuleEventCon = new PopPolicyRuleEventCon();
			newRuleEventCon.copyBean(popPolicyRuleEventCon);
			newRuleEventCon.set(PopPolicyRuleEventCon.COL_ID, PopUtil.generateId());
			newRuleEventCon.set(PopPolicyRuleEventCon.COL_POLICY_RULE_ID, newRuleId);
			newRuleEventCon.save();
		}
		//复制策略规则与执行时间关系
		PopPolicyRuleExecTime popPolicyRuleExecTime = PopPolicyRuleExecTime.dao().searchFirst("policy_rule_id",
				oldRuleId);
		if (popPolicyRuleExecTime != null) {
			PopPolicyRuleExecTime newRuleExecTime = new PopPolicyRuleExecTime();
			newRuleExecTime.copyBean(popPolicyRuleExecTime);
			newRuleExecTime.set(PopPolicyRuleExecTime.COL_ID, PopUtil.generateId());
			newRuleExecTime.set(PopPolicyRuleExecTime.COL_POLICY_RULE_ID, newRuleId);
			newRuleExecTime.save();
		}
	}

	/**
	 * 检查策略名称是否重复
	 */
	public void checkPolicyNameRepeat() {
		this.initAttributes();
		HashMap<Object, Object> result = Maps.newHashMap();
		try {
			StringBuilder sql = new StringBuilder("select * from pop_policy_baseinfo where policy_name=?");
			List<Object> paras = Lists.newArrayList();
			paras.add(this.getPara("policy_name"));
			if (StringUtil.isNotEmpty(this.getPara("policy_id"))) {
				sql.append(" and id <> ?");
				paras.add(this.getPara("policy_id"));
			}
			PopPolicyBaseinfo baseInfo = PopPolicyBaseinfo.dao().findFirst(sql.toString(), paras.toArray());
			result.put("success", 1);
			result.put("flag", baseInfo != null);
		} catch (Exception e) {
			log.error("检查策略名称是否重复异常", e);
			result.put("success", 2);
		}
		renderJson(result);
	}

	public void saveTest() {
		//Db.find("", paras);
		render("policyConfig/test.jsp");
	}

	/**
	 * 提交策略审批
	 * @throws Exception
	 */
	public void submitPolicy() throws Exception {
		initAttributes();
		HashMap<Object, Object> result = Maps.newHashMap();

		String policy_id = this.getPara("policy_id");
		PopPolicyBaseinfo policyBaseInfo = PopPolicyBaseinfo.dao().findById(policy_id);
		try {
			String drvID = policyBaseInfo.getStr(PopPolicyBaseinfo.COL_POLICY_TYPE_ID);
			Integer status = policyBaseInfo.getInt(PopPolicyBaseinfo.COL_POLICY_STATUS_ID);
			//审批
			IApprovalService approvalService = (IApprovalService) SystemServiceLocator.getInstance().getService(ApprovalCONST.APPROVE_SERVICE);
			approvalService.doApproval(policy_id, status, this.userId, ApprovalCONST.APPROVDRVDIMTABLE_CHANGJING, drvID);
			
			String flowModelID = approvalService.getApproveDrvType(ApprovalCONST.APPROVDRVDIMTABLE_CHANGJING, policyBaseInfo.getStr(PopPolicyBaseinfo.COL_POLICY_TYPE_ID));
			if(ApprovalCONST.FLOW_MODEL_ONE.equals(flowModelID)){
				String sql="update pop_policy_baseinfo b set b.policy_status_id=? where b.id=?";
				Db.update(sql,PolicyStatus.UNSENDODER.getValue(),policy_id);
				//更新规则
				String ruleSQL="update pop_policy_rule set rule_status=? where policy_id=?";
				Db.update(ruleSQL,PolicyStatus.UNSENDODER.getValue(),policy_id);
			}else if (ApprovalCONST.FLOW_MODEL_TWO.equals(flowModelID)) {// 基本信息审批
				//将策略修改为待审批
				Db.update("update pop_policy_baseinfo set policy_status_id=? where id=?",PolicyStatus.UNAPPROVE.getValue(), policy_id);
				//更新规则状态为待审批
				Db.update("update pop_policy_rule set rule_status =? where policy_id=?", PolicyStatus.UNAPPROVE.getValue(),policy_id);
				
			}else if(ApprovalCONST.FLOW_MODEL_THREE.equals(flowModelID)){//基本信息确认 确认完成之后直接派单
				//将策略修改为待确认
				Db.update("update pop_policy_baseinfo set policy_status_id=? where id=?",PolicyStatus.UNCONFIRM.getValue(), policy_id);
				//更新规则状态为待确认
				Db.update("update pop_policy_rule set rule_status =? where policy_id=?", PolicyStatus.UNCONFIRM.getValue(),policy_id);
				
			}else if(ApprovalCONST.FLOW_MODEL_FOUR.equals(flowModelID)){//基本信息审批 确认  确认完成之后直接派单
				//将策略修改为待审批
				Db.update("update pop_policy_baseinfo set policy_status_id=? where id=?",PolicyStatus.UNAPPROVE.getValue(), policy_id);
				//更新规则状态为待审批
				Db.update("update pop_policy_rule set rule_status =? where policy_id=?", PolicyStatus.UNAPPROVE.getValue(),policy_id);
				
			}
			result.put("success", 1);
			result.put("msg", policyBaseInfo.getStr(PopPolicyBaseinfo.COL_POLICY_NAME) + "策略提交审批成功");
			
			//发送提醒短信
			SmsService smsServcie=(SmsService) SystemServiceLocator.getInstance().getService("IPopSmsService");
			smsServcie.SendMessage("SP001",policy_id);
			
			//记录日志
			String desc = String.format("提交策略审批,策略编号:%s", policy_id);
			LogOperateUtil.log(LogOperateUtil.POLICY_MAN_SUB,desc,this.getRequest());
			
		} catch (Exception e) {
			log.error("提交策略审批异常:", e);
			result.put("success", 2);
			result.put("msg", policyBaseInfo.getStr(PopPolicyBaseinfo.COL_POLICY_NAME) + "策略提交审批失败:" + e.getMessage());
			String desc = String.format("提交策略审批异常:%s,策略编号:%s", e.getMessage(),policy_id);
			LogOperateUtil.log(LogOperateUtil.POLICY_MAN_SUB,desc,this.getRequest());
		}
		renderJson(result);
	}
}
