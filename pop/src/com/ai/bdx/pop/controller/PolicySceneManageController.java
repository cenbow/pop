package com.ai.bdx.pop.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ai.bdx.frame.privilegeServiceExt.service.IUserPrivilegeCommonService;
import com.ai.bdx.pop.base.PopController;
import com.ai.bdx.pop.bean.PopPolicyRuleBean;
import com.ai.bdx.pop.bean.PopPolicySceneManageBean;
import com.ai.bdx.pop.enums.PolicyStatus;
import com.ai.bdx.pop.exception.PopException;
import com.ai.bdx.pop.model.PopDimPolicyStatus;
import com.ai.bdx.pop.model.PopPolicyBaseinfo;
import com.ai.bdx.pop.model.PopPolicyRule;
import com.ai.bdx.pop.model.PopPolicyRuleAct;
import com.ai.bdx.pop.model.PopPolicyRuleCustgroup;
import com.ai.bdx.pop.model.PopPolicyRuleEventCon;
import com.ai.bdx.pop.service.IPopSendOddService;
import com.ai.bdx.pop.util.CityId2CityNameUtil;
import com.ai.bdx.pop.util.LogOperateUtil;
import com.ai.bdx.pop.util.PopConstant;
import com.asiainfo.biframe.utils.spring.SystemServiceLocator;
import com.asiainfo.biframe.utils.string.StringUtil;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

public class PolicySceneManageController extends PopController {

	private static final Logger log = LogManager.getLogger(PolicySceneManageController.class);

	public static Map<String, Object> searchConditionMap = new HashMap<String, Object>();
	
	public void index() {
		this.initAttributes();
		render("policyManage/policySceneManageIndex.jsp");
	}

	/**
	 * 策略查询
	 * 0.查询参数初始化
	 * 1.先查出基本信息
	 * 2.根据基本信息整理规则查询ID
	 * 3.查询规则、和规则相关的信息
	 * 4.将查询出来的信息补充封装
	 */
	public void search() {
		List<Object> list = new ArrayList<Object>();
		this.initAttributes();

		//0. 查询参数初始化
		int page = getParaToInt("page", 1);
		int searchByForm = getParaToInt("searchByForm", 0);
		String policyName = this.getPara("policyName", "").trim();
		String policyNo = this.getPara("policyNo", "").trim();//策略编码，对应的是策略规则ID
		String policyTypeId = this.getPara("policyTypeId", "");
		String policyLevelId = this.getPara("policyLevelId", "");
		String startTime = this.getPara("startDate", "");
		String endTime = this.getPara("endDate", "");
		String rulePriority = this.getPara("rulePriority", "");//优先级
		//String policyActTypeId = this.getPara("policyActTypeId","");//活动类型
		String policyControlTypeId = this.getPara("policyControlTypeId", "");//活动管控类型，对应界面上的策略动作

		String zeroTime = " 00:00:00";
		startTime = startTime + zeroTime;
		String lastMoment = " 23:59:59";
		endTime = endTime + lastMoment;

		int rulePriorityInt = 0;
		if (StringUtil.isNotEmpty(rulePriority)) {
			try {
				rulePriorityInt = Integer.parseInt(rulePriority);
			} catch (NumberFormatException e) {
				e.printStackTrace();
				throw new PopException("优先级字符转换错误！优先级只能是数字。");
			}
		}

		//searchByForm为1这说明是通过提交form的查询，否则是点击页码查询
		//点击页码查询时不会取提交表单，后台也不会获得表单参数，说以需要使用之前的表单参数
		if (searchByForm == 1) {
			searchConditionMap.put("policyName", policyName);
			searchConditionMap.put("policyNo", policyNo);
			searchConditionMap.put("policyTypeId", policyTypeId);
			searchConditionMap.put("policyLevelId", policyLevelId);
			searchConditionMap.put("startTime", startTime);
			searchConditionMap.put("endTime", endTime);
			searchConditionMap.put("rulePriorityInt", rulePriorityInt);
			searchConditionMap.put("policyControlTypeId", policyControlTypeId);
		} else {
			policyName = (String) searchConditionMap.get("policyName");
			policyNo = (String) searchConditionMap.get("policyNo");
			policyTypeId = (String) searchConditionMap.get("policyTypeId");
			policyLevelId = (String) searchConditionMap.get("policyLevelId");
			startTime = (String) searchConditionMap.get("startTime");
			endTime = (String) searchConditionMap.get("endTime");
			rulePriorityInt = (Integer) searchConditionMap.get("rulePriorityInt");
			policyControlTypeId = (String) searchConditionMap.get("policyControlTypeId");
		}

		//权限控制
		boolean isAdmin = false;
		try {
			IUserPrivilegeCommonService service = (IUserPrivilegeCommonService) SystemServiceLocator.getInstance().getService(PRIVILEGE_SERVICE_NAME);
			if (service.isAdminUser(userId)) {
				isAdmin = true;
			}
		} catch (PopException e) {
			e.printStackTrace();
			throw new PopException("获取权限失败！");
		} catch (Exception e) {
			e.printStackTrace();
		}

		//将表单参数整理成sql查询条件
		StringBuffer tempSqlCondStr = new StringBuffer();
		if (!policyTypeId.equals("") && policyTypeId.indexOf(",") != -1) {
			String[] idArray = policyTypeId.split(",");
			for (int i = 0; i < idArray.length; i++) {
				tempSqlCondStr.append("'");
				tempSqlCondStr.append(idArray[i]);
				tempSqlCondStr.append("'");
				if (i != idArray.length - 1) {
					tempSqlCondStr.append(",");
				}
			}
			policyTypeId = tempSqlCondStr.toString();
			tempSqlCondStr = null;
		}

		//封装策略信息的list
		List<PopPolicySceneManageBean> popPolicySceneManageBeanList = new ArrayList<PopPolicySceneManageBean>();

		//1.先查出基本信息（策略基本信息和其他相关联的信息）
		StringBuffer policyBaseinfoSQL = new StringBuffer();
		policyBaseinfoSQL.append("SELECT ");
		policyBaseinfoSQL.append(" distinct info.id as policy_id, ");
		policyBaseinfoSQL.append(" info.policy_name, ");
		policyBaseinfoSQL.append("info.policy_desc, ");
		policyBaseinfoSQL.append("info.template_flag, ");
		policyBaseinfoSQL.append("info.policy_level_id, ");
		policyBaseinfoSQL.append("info.policy_status_id, ");
		policyBaseinfoSQL.append("statu.name as policy_status_Name, ");
		policyBaseinfoSQL.append("info.policy_type_id, ");
		policyBaseinfoSQL.append("pt.name as policy_type_Name, ");
		policyBaseinfoSQL.append("info.start_time, ");
		policyBaseinfoSQL.append("info.end_time, ");
		policyBaseinfoSQL.append("info.policy_task_tab, ");
		policyBaseinfoSQL.append("info.city_priority ");
		policyBaseinfoSQL.append("FROM ");
		policyBaseinfoSQL.append("pop_policy_baseinfo info, ");
		if (StringUtil.isNotEmpty(rulePriority) || StringUtil.isNotEmpty(policyControlTypeId)
				|| StringUtil.isNotEmpty(policyNo)) {
			policyBaseinfoSQL.append("POP_POLICY_RULE pr,");
		}
		if (StringUtil.isNotEmpty(policyControlTypeId)) {
			policyBaseinfoSQL.append("pop_policy_rule_act pra, ");
		}
		policyBaseinfoSQL.append("POP_DIM_POLICY_STATUS statu, ");
		policyBaseinfoSQL.append("POP_DIM_POLICY_TYPE pt ");
		policyBaseinfoSQL.append(" WHERE 1=1 ");
		if (!isAdmin) {
			policyBaseinfoSQL.append(" AND info.create_user_id= '" + userId + "' ");
		}
		if (StringUtil.isNotEmpty(rulePriority) || StringUtil.isNotEmpty(policyControlTypeId)
				|| StringUtil.isNotEmpty(policyNo)) {
			policyBaseinfoSQL.append(" and info.id=pr.policy_id ");
		}
		if (StringUtil.isNotEmpty(policyControlTypeId)) {
			policyBaseinfoSQL.append(" and pra.policy_rule_id= pr.id ");
		}
		policyBaseinfoSQL.append(" and info.policy_status_id= statu.id ");
		policyBaseinfoSQL.append(" and info.policy_type_id= pt.id ");
		policyBaseinfoSQL.append(" AND info.template_flag=0 ");
		if (StringUtil.isNotEmpty(policyName)) {
			//policyBaseinfoSQL.append(" and info.policy_name like '%" + policyName + "%' ");
			policyBaseinfoSQL.append(" and info.policy_name like ? ");
			list.add("%"+policyName+"%");
		}
		if (StringUtil.isNotEmpty(policyNo)) {
			//policyBaseinfoSQL.append(" and pr.id like '%" + policyNo + "%' ");
			policyBaseinfoSQL.append(" and pr.id like ? ");
			list.add("%"+policyNo+"%");
		}
		if (StringUtil.isNotEmpty(policyTypeId)) {
			policyBaseinfoSQL.append(" and info.policy_type_id in (" + policyTypeId + ") ");
		}
		if (StringUtil.isNotEmpty(policyLevelId)) {
			policyBaseinfoSQL.append(" and info.policy_level_id in (" + policyLevelId + ") ");
		}
		if (!startTime.equals(zeroTime)) {
			list.add(startTime);
			policyBaseinfoSQL.append(" and info.start_time>=? ");
		}
		if (!endTime.equals(lastMoment)) {
			list.add(endTime);
			policyBaseinfoSQL.append(" and info.end_time<=? ");
		}
		if (StringUtil.isNotEmpty(rulePriority)) {
			list.add(rulePriorityInt);
			policyBaseinfoSQL.append(" and pr.rule_priority =? ");
		}
		if (StringUtil.isNotEmpty(policyControlTypeId)) {
			policyBaseinfoSQL.append(" and pra.control_type_id in (" + policyControlTypeId + ") ");
		}
		policyBaseinfoSQL.append(" order by info.create_time desc ");
		log.debug("PolicySceneManageController.search() policyBaseinfoSQL:" + policyBaseinfoSQL.toString());
		Page<PopPolicyBaseinfo> popPolicyBaseinfoList=null;
		try {
			if(list.size()>0){
				 popPolicyBaseinfoList = PopPolicyBaseinfo.dao().paginate(page, 10, "select * ",
							"from (" + policyBaseinfoSQL.toString() + ") a ",list.toArray());
			}else{
				popPolicyBaseinfoList = PopPolicyBaseinfo.dao().searchPaginate(page, 10, "select * ",
						"from (" + policyBaseinfoSQL.toString() + ") a ");
			}
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		if (popPolicyBaseinfoList == null || popPolicyBaseinfoList.getList().size() == 0) {
			setAttr("POP_POLICY_EDIT_STATUS", PopConstant.POP_POLICY_EDIT_STATUS);//编辑状态
			setAttr("POP_POLICY_EXCUTE_STATUS", PopConstant.POP_POLICY_EXCUTE_STATUS);//执行中
			setAttr("POP_POLICY_FAILURE_STATUS", PopConstant.POP_POLICY_FAILURE_STATUS);//执行失败
			setAttr("POP_POLICY_PAUSE_STATUS", PopConstant.POP_POLICY_PAUSE_STATUS);//暂停
			setAttr("popPolicySceneManageBeanList", popPolicySceneManageBeanList);
//			setAttr("totalRowOfPolicySceneManage", popPolicyBaseinfoList.getTotalRow());
//			setAttr("totalPageOfPolicySceneManage", popPolicyBaseinfoList.getTotalPage());
//			setAttr("pageNumberOfPolicySceneManage", popPolicyBaseinfoList.getPageNumber());
//			setAttr("pageSizeOfPolicySceneManage", popPolicyBaseinfoList.getPageSize());
			setAttr("list", popPolicyBaseinfoList);
			render("policyManage/policySceneManageList.jsp");
			return;
		}

		//2.查询策略下规则信息
		List<PopPolicyBaseinfo> baseInfos = popPolicyBaseinfoList.getList();
		for (PopPolicyBaseinfo popPolicyBaseinfo : baseInfos) {
			PopPolicySceneManageBean popPolicyBaseinfoBean = popPolicyBaseinfo.toBean(PopPolicySceneManageBean.class);
			try {
				popPolicyBaseinfoBean.setCityPriorityName(CityId2CityNameUtil.getCityNameByCache(popPolicyBaseinfo.getStr(PopPolicyBaseinfo.COL_CITY_PRIORITY)));
			} catch (Exception e) {
				log.error("根据地市ID查询城市名称异常：" + e);
			}
			popPolicyBaseinfoBean.setRuleList(getAllPolicyRulesByPid(popPolicyBaseinfoBean.getPolicyId(),
					popPolicyBaseinfoBean));
			popPolicySceneManageBeanList.add(popPolicyBaseinfoBean);
		}

		setAttr("POP_POLICY_EDIT_STATUS", PopConstant.POP_POLICY_EDIT_STATUS);//编辑状态
		setAttr("POP_POLICY_EXCUTE_STATUS", PopConstant.POP_POLICY_EXCUTE_STATUS);//执行中
		setAttr("POP_POLICY_FAILURE_STATUS", PopConstant.POP_POLICY_FAILURE_STATUS);//执行失败
		setAttr("POP_POLICY_PAUSE_STATUS", PopConstant.POP_POLICY_PAUSE_STATUS);//暂停
		setAttr("popPolicySceneManageBeanList", popPolicySceneManageBeanList);
//		setAttr("totalRowOfPolicySceneManage", popPolicyBaseinfoList.getTotalRow());
//		setAttr("totalPageOfPolicySceneManage", popPolicyBaseinfoList.getTotalPage());
//		setAttr("pageNumberOfPolicySceneManage", popPolicyBaseinfoList.getPageNumber());
//		setAttr("pageSizeOfPolicySceneManage", popPolicyBaseinfoList.getPageSize());
		setAttr("list", popPolicyBaseinfoList);
		render("policyManage/policySceneManageList.jsp");
	}

	/**
	 * 删除策略
	 * 1. 需要删除六张表中的数据：POP_POLICY_BASEINFO POP_POLICY_RULE POP_POLICY_RULE_CUSTGROUP
	 * POP_POLICY_RULE_EXEC_TIME POP_POLICY_RULE_ACT POP_POLICY_RULE_EVENT_CON
	 * 2. 调用CEP接口删除CEP中的数据
	 */
	public void delPolicyBaseinfo() {
		this.initAttributes();

		Map<String, String> map = new HashMap<String, String>();
		String policyId = this.getPara("policyId", "");
		try {
			String policyName = this.getPara("policyName", "");
			if (StringUtil.isEmpty(policyId)) {
				throw new PopException("删除策略【" + policyName + "】失败，策略Id为空!");
			}

			List<PopPolicyBaseinfo> popPolicyBaseinfoList = PopPolicyBaseinfo.dao().find(
					"select info.* FROM pop_policy_baseinfo info WHERE info.id='" + policyId
							+ "' and info.policy_status_id =" + PopConstant.POP_POLICY_EDIT_STATUS);
			if (popPolicyBaseinfoList == null || popPolicyBaseinfoList.isEmpty()) {
				throw new PopException("删除策略失败，策略【" + policyName + "】不是编辑状态，不能删除!");
			}

			//删除规则及其关联表数据
			List<PopPolicyRule> rules = PopPolicyRule.dao().search(PopPolicyRule.COL_POLICY_ID, policyId);
			if (!rules.isEmpty()) {
				List<String> ruleList = Lists.newArrayList();
				for (PopPolicyRule popPolicyRule : rules) {
					ruleList.add(popPolicyRule.getStr(PopPolicyRule.COL_ID));
				}
				String idStr = Joiner.on(", ").join(ruleList);
				StringBuffer sb = new StringBuffer();
				//1.删除策略规则与事件规则关系 POP_POLICY_RULE_EVENT_CON 
				sb.append("delete FROM Pop_Policy_Rule_Event_Con WHERE policy_rule_id IN (" + idStr + ")");
				Db.update(sb.toString());
				sb = new StringBuffer();
				//(另外还要删除复杂事件规则，这里暂时不做)

				//2.删除策略规则与客户群关系 POP_POLICY_RULE_CUSTGROUP
				sb.append("delete FROM POP_POLICY_RULE_CUSTGROUP WHERE policy_rule_id IN (" + idStr + ")");
				Db.update(sb.toString());
				sb = new StringBuffer();

				//3.删除策略规则与执行时间关系 POP_POLICY_RULE_EXEC_TIME
				sb.append("delete FROM POP_POLICY_RULE_EXEC_TIME WHERE policy_rule_id IN (" + idStr + ")");
				Db.update(sb.toString());
				sb = new StringBuffer();

				//4.策略规则与执行动作（管控/营销）关系 POP_POLICY_RULE_ACT
				sb.append("DELETE FROM POP_POLICY_RULE_ACT WHERE policy_rule_id IN (" + idStr + ")");
				Db.update(sb.toString());
				sb = new StringBuffer();

				//5.策略规则信息 POP_POLICY_RULE
				sb.append("DELETE FROM POP_POLICY_RULE WHERE id IN (" + idStr + ")");
				Db.update(sb.toString());
			}
			//6.策略规则信息 POP_POLICY_BASEINFO
			Db.update("DELETE FROM pop_policy_baseinfo WHERE id='" + policyId + "'");

			map.put("success", "1");
			
			//记录日志
			String desc = String.format("删除策略,策略编号:%s", policyId);
			LogOperateUtil.log(LogOperateUtil.POLICY_MAN_DEL,desc,this.getRequest());
		} catch (PopException e) {
			map.put("success", "0");
			map.put("msg", e.getMessage());
			//记录日志
			String desc = String.format("删除策略失败:%s,策略编号:%s", e.getMessage(),policyId);
			LogOperateUtil.log(LogOperateUtil.POLICY_MAN_DEL,desc,this.getRequest());
		}
		this.renderJson(map);
	}

	/**
	 * 暂停策略
	 * 1. 需要调用CEP接口暂停CEP相关任务
	 * 2. 更新pop_policy_baseinfo表中的数据状态
	 */
	public void pausePolicyBaseinfo() {
		this.initAttributes();

		Map<String, String> map = new HashMap<String, String>();
		String policyId = this.getPara("policyId", "");
		String policyName = this.getPara("policyName", "");
		try {
			String ruleId = this.getPara("ruleId", "");
			if (StringUtil.isEmpty(policyId)) {
				throw new PopException("暂停策略【" + policyName + "】失败，策略Id为空!");
			}

			List<PopPolicyBaseinfo> popPolicyBaseinfoList = PopPolicyBaseinfo.dao().find(
					"select info.* FROM pop_policy_baseinfo info WHERE info.id='" + policyId
							+ "' and info.policy_status_id =" + PopConstant.POP_POLICY_EXCUTE_STATUS);
			if (popPolicyBaseinfoList == null || popPolicyBaseinfoList.isEmpty()) {
				throw new PopException("暂停策略【" + policyName + "】失败，策略不是运行状态，不能暂停!");
			}

			//整理规则ID作为查询条件
			if (StringUtil.isNotEmpty(ruleId)) {
				String[] ruleIdArray = ruleId.split("@");
				StringBuffer idStr = new StringBuffer();
				for (int i = 0; i < ruleIdArray.length; i++) {
					idStr.append("'");
					idStr.append(ruleIdArray[i]);
					idStr.append("'");
					if (i != ruleIdArray.length - 1) {
						idStr.append(",");
					}
				}
				//1.暂停策略需要调用接口，后边再做    idStr

			}
			//2.策略规则信息 POP_POLICY_BASEINFO
			Db.update("UPDATE pop_policy_baseinfo info SET info.policy_status_id="
					+ PopConstant.POP_POLICY_PAUSE_STATUS + " WHERE info.id='" + policyId + "'");

			map.put("success", "1");
			//记录日志
			String desc = String.format("暂停策略,策略编号:%s", policyId);
			LogOperateUtil.log(LogOperateUtil.POLICY_TASK_PAUSE,desc,this.getRequest());
		} catch (PopException e) {
			map.put("success", "0");
			map.put("msg", e.getMessage());
			//记录日志
			String desc = String.format("暂停策略失败:%s,策略编号:%s",e.getMessage(), policyId);
			LogOperateUtil.log(LogOperateUtil.POLICY_TASK_PAUSE,desc,this.getRequest());
		}
		this.renderJson(map);
	}

	/**
	 * 恢复策略运行
	 * 1. 需要调用CEP接口暂停CEP相关任务
	 * 2. 更新pop_policy_baseinfo表中的数据状态
	 */
	public void reStartPolicyBaseinfo() {
		this.initAttributes();

		Map<String, String> map = new HashMap<String, String>();
		String policyId = this.getPara("policyId", "");
		String policyName = this.getPara("policyName", "");
		try {
			String ruleId = this.getPara("ruleId", "");
			if (StringUtil.isEmpty(policyId)) {
				throw new PopException("启动策略【" + policyName + "】失败，策略Id为空!");
			}

			List<PopPolicyBaseinfo> popPolicyBaseinfoList = PopPolicyBaseinfo.dao().find(
					"select info.* FROM pop_policy_baseinfo info WHERE info.id='" + policyId
							+ "' and ( info.policy_status_id =" + PopConstant.POP_POLICY_FAILURE_STATUS
							+ " or info.policy_status_id =" + PopConstant.POP_POLICY_PAUSE_STATUS + ")");
			if (popPolicyBaseinfoList == null || popPolicyBaseinfoList.isEmpty()) {
				throw new PopException("启动策略【" + policyName + "】失败，策略不是暂停或者派单失败状态，不能启动。");
			}

			//整理规则ID作为查询条件
			if (StringUtil.isNotEmpty(ruleId)) {
				String[] ruleIdArray = ruleId.split("@");
				StringBuffer idStr = new StringBuffer();
				for (int i = 0; i < ruleIdArray.length; i++) {
					idStr.append("'");
					idStr.append(ruleIdArray[i]);
					idStr.append("'");
					if (i != ruleIdArray.length - 1) {
						idStr.append(",");
					}
				}
				//1.重启策略需要调用接口，后边再做    idStr

			}
			//2.策略规则信息 POP_POLICY_BASEINFO
			Db.update("UPDATE pop_policy_baseinfo info SET info.policy_status_id="
					+ PopConstant.POP_POLICY_EXCUTE_STATUS + " WHERE info.id='" + policyId + "'");

			map.put("success", "1");
			//记录日志
			String desc = String.format("启动策略,策略编号:%s", policyId);
			LogOperateUtil.log(LogOperateUtil.POLICY_TASK_RESTART,desc,this.getRequest());
		} catch (PopException e) {
			map.put("success", "0");
			map.put("msg", e.getMessage());
			//记录日志
			String desc = String.format("启动策略失败:%s,策略编号:%s,", e.getMessage(),policyId);
			LogOperateUtil.log(LogOperateUtil.POLICY_TASK_RESTART,desc,this.getRequest());
		}
		this.renderJson(map);
	}

	/**
	 * 派单
	 */
	public void sendOrderPolicyBaseinfo() {
		this.initAttributes();

		Map<String, String> map = new HashMap<String, String>();
		try {
			map.put("success", "1");
		} catch (PopException e) {
			map.put("success", "0");
			map.put("msg", e.getMessage());
		}
		this.renderJson(map);
	}

	/**
	 * 暂停规则
	 */
	public void pauseRule() {
		Map<String, String> result = new HashMap<String, String>();
		String ruleId = getPara("ruleId");
		try {
			String popTaskTable = Db
					.findFirst(
							"select policy_task_tab from pop_policy_baseinfo where id in(select policy_id from pop_policy_rule where id=?)",
							new Object[] { ruleId }).getStr("policy_task_tab");
			IPopSendOddService sendOddService = (IPopSendOddService) SystemServiceLocator.getInstance().getService(
					"IPopSendOddService");
			sendOddService.controlRuleOperate(ruleId, popTaskTable, PopConstant.RULE_OPERATE_STOP);
			result.put("success", "1");
			// 记录日志
			String desc = String.format("暂停规则,规则编号:%s", ruleId);
			LogOperateUtil.log(LogOperateUtil.POLICY_RULE_PAUSE,desc,this.getRequest());
		} catch (Exception e) {
			result.put("success", "0");
			result.put("msg", e.getMessage());
			//记录日志
			String desc = String.format("暂停规则失败:%s,规则编号:%s", e.getMessage(),ruleId);
			LogOperateUtil.log(LogOperateUtil.POLICY_RULE_PAUSE,desc,this.getRequest());
		}
		this.renderJson(result);
	}

	/**
	 * 重启规则
	 */
	public void reStartRule() {
		Map<String, String> result = new HashMap<String, String>();
		String ruleId = getPara("ruleId");
		try {
			String popTaskTable = Db
					.findFirst(
							"select policy_task_tab from pop_policy_baseinfo where id in(select policy_id from pop_policy_rule where id=?)",
							new Object[] { ruleId }).getStr("policy_task_tab");
			IPopSendOddService sendOddService = (IPopSendOddService) SystemServiceLocator.getInstance().getService(
					"IPopSendOddService");
			sendOddService.controlRuleOperate(ruleId, popTaskTable, PopConstant.RULE_OPERATE_RESTART);
			result.put("success", "1");
			//记录日志
			String desc = String.format("重启规则,规则编号:%s", ruleId);
			LogOperateUtil.log(LogOperateUtil.POLICY_RULE_RESTART,desc,this.getRequest());
		} catch (Exception e) {
			result.put("success", "0");
			result.put("msg", e.getMessage());
			//记录日志
			String desc = String.format("重启规则失败:%s,规则编号:%s",e.getMessage(), ruleId);
			LogOperateUtil.log(LogOperateUtil.POLICY_RULE_RESTART,desc,this.getRequest());
		}
		this.renderJson(result);
	}

	/**
	 * 终止规则
	 */
	public void terminaterRule() {
		Map<String, String> result = new HashMap<String, String>();
		String ruleId = getPara("ruleId");
		try {
			String popTaskTable = Db
					.findFirst(
							"select policy_task_tab from pop_policy_baseinfo where id in(select policy_id from pop_policy_rule where id=?)",
							new Object[] { ruleId }).getStr("policy_task_tab");
			IPopSendOddService sendOddService = (IPopSendOddService) SystemServiceLocator.getInstance().getService(
					"IPopSendOddService");
			sendOddService.controlRuleOperate(ruleId, popTaskTable, PopConstant.RULE_OPERATE_FINISH);
			result.put("success", "1");
			//记录日志
			String desc = String.format("终止规则,规则编号:%s", ruleId);
			LogOperateUtil.log(LogOperateUtil.POLICY_RULE_STOP,desc,this.getRequest());
		} catch (Exception e) {
			result.put("success", "0");
			result.put("msg", e.getMessage());
			//记录日志
			String desc = String.format("终止规则失败:%s,规则编号:%s", e.getMessage(),ruleId);
			LogOperateUtil.log(LogOperateUtil.POLICY_RULE_STOP,desc,this.getRequest());
		}
		this.renderJson(result);
	}

	/**
	 * 递归查询策略下所有规则
	 * @param pid
	 * @param baseinfo
	 * @return
	 */
	private List<PopPolicyRuleBean> getAllPolicyRulesByPid(String pid, PopPolicySceneManageBean baseinfo) {
		List<PopPolicyRuleBean> ruleBeans = Lists.newArrayList();
		String sql = null;
		if (pid.equals(baseinfo.getPolicyId())) {//递归首次 查询规则
			sql = "select * from pop_policy_rule where (parent_id is null or parent_id ='') and policy_id=?";
		} else {//查规则下子规则
			sql = "select * from pop_policy_rule where parent_id=?";
		}
		List<PopPolicyRule> rules = PopPolicyRule.dao().find(sql, new Object[] { pid });

		//获取状态
		//状态
		List<Record> popDimPolicyStatus = Db.find("select * from pop_dim_policy_status");
		Map<Integer, String> popDimPolicyStatusMap = new HashMap<Integer, String>();
		for (int i = 0; i < popDimPolicyStatus.size(); i++) {
			Record r = popDimPolicyStatus.get(i);
			popDimPolicyStatusMap.put(r.getInt(PopDimPolicyStatus.COL_ID), r.getStr(PopDimPolicyStatus.COL_NAME));
		}
		for (PopPolicyRule popPolicyRule : rules) {
			String id = popPolicyRule.getStr(PopPolicyRule.COL_ID);
			PopPolicyRuleBean ruleBean = new PopPolicyRuleBean();
			ruleBean.setId(id);
			ruleBean.setPolicyRuleName(popPolicyRule.getStr(PopPolicyRule.COL_POLICY_RULE_NAME));
			String pId = popPolicyRule.getStr(PopPolicyRule.COL_PARENT_ID);
			ruleBean.setParentId(pId);
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
			//客户群数累计
			PopPolicyRuleCustgroup popPolicyRuleCustgroup = PopPolicyRuleCustgroup.dao().searchFirst("policy_rule_id",
					id);
			if (popPolicyRuleCustgroup != null) {
				baseinfo.setCustgroupNumber(baseinfo.getCustgroupNumber()
						+ popPolicyRuleCustgroup.getInt(PopPolicyRuleCustgroup.COL_CUSTGROUP_NUMBER));
			}
			//策略动作类型累计
			if (StringUtil.isNotEmpty(ruleBean.getActionTypeName())) {
				if (StringUtil.isEmpty(baseinfo.getPolicyControlName())) {
					baseinfo.setPolicyControlName(ruleBean.getActionTypeName());
				} else {
					baseinfo.setPolicyControlName(Joiner.on("").skipNulls()
							.join(baseinfo.getPolicyControlName(), "; ", ruleBean.getActionTypeName()));
				}
			}
			//状态
			Object stt = popPolicyRule.get(PopPolicyRule.COL_RULE_STATUS);
			int status = -1;
			if (StringUtil.isNotEmpty(stt)) {
				status = Integer.parseInt(String.valueOf(stt));
			} else {
				//状态为空时跳过
				continue;
			}
			String statusName = popDimPolicyStatusMap.get(status);
			ruleBean.setStatusName(statusName);

			//是否可执行启停操作判断
			if (status == PolicyStatus.SENDODER_DONE.getValue() || status == PolicyStatus.SENDODER_TERMINAL.getValue()) {//规则状态:完成、s终止时，不可启动、暂停或终止
				ruleBean.setCanFinish(false);
				ruleBean.setCanPause(false);
				ruleBean.setCanStart(false);
			} else if (status == PolicyStatus.SENDODER_PAUSE.getValue()) {//规则状态:暂停时
				ruleBean.setCanFinish(true);
				ruleBean.setCanPause(false);
				ruleBean.setCanStart(true);
			} else if (status == PolicyStatus.SENDODER_USER_ERROR.getValue()) {//规则状态:失败时
				ruleBean.setCanFinish(true);
				ruleBean.setCanPause(false);
				ruleBean.setCanStart(false);
			} else if (status == PolicyStatus.SENDODER_BASE_SUCCESS.getValue()) {//规则状态:派单成功
				ruleBean.setCanFinish(true);
				ruleBean.setCanPause(true);
				ruleBean.setCanStart(false);
			} else {
				ruleBean.setCanFinish(false);
				ruleBean.setCanPause(false);
				ruleBean.setCanStart(false);
				log.debug("策略规则[{}]当前状态为:{},禁止启动、暂停或终止操作", popPolicyRule.getStr(PopPolicyRule.COL_POLICY_RULE_NAME),
						statusName);
			}
			if (status >= PolicyStatus.SENDODER_BASE_SUCCESS.getValue()) {
				ruleBean.setCanViewTask(true);
			} else {
				ruleBean.setCanViewTask(false);
			}

			//查询规则下所有任务
			List<Record> tasks = Lists.newArrayList();
			if (StringUtil.isNotEmpty(baseinfo.getPolicyTaskTab())) {
				String findTaskSql = String
						.format("select  id, exec_date, exec_status, exec_info, pre_send_data_tab, send_data_tab, reject_data_tab  from %s where rule_id=?",
								baseinfo.getPolicyTaskTab());
				tasks = Db.find(findTaskSql, new Object[] { id });
				for (Record task : tasks) {
					long preSendDataCount;
					long sendDataCount;
					long rejectDataCount;
					if (StringUtil.isNotEmpty(task.getStr("pre_send_data_tab"))) {
						preSendDataCount = Db.queryLong(String.format("select count(1) from %s",
								task.getStr("pre_send_data_tab")));
						task.set("preSendDataCount", preSendDataCount);
					} else {
						task.set("preSendDataCount", "--");
					}
					if (StringUtil.isNotEmpty(task.getStr("send_data_tab"))) {
						sendDataCount = Db.queryLong(String.format("select count(1) from %s",
								task.getStr("send_data_tab")));
						task.set("sendDataCount", sendDataCount);
					} else {
						task.set("sendDataCount", "--");
					}
					if (StringUtil.isNotEmpty(task.getStr("reject_data_tab"))) {
						rejectDataCount = Db.queryLong(String.format("select count(1) from %s",
								task.getStr("reject_data_tab")));
						task.set("rejectDataCount", rejectDataCount);
					} else {
						task.set("rejectDataCount", "--");
					}
					boolean can_restart = false;
					short exec_status = task.getInt("exec_status").shortValue();
					String exec_status_name = "未知";
					if (exec_status == PopConstant.TASK_STATUS_PDCG) {
						exec_status_name = "执行成功";
					} else if (exec_status == PopConstant.TASK_STATUS_PDSB) {
						exec_status_name = "执行失败";
						can_restart = true;
					} else if (exec_status == PopConstant.TASK_STATUS_DDPD) {
						exec_status_name = "等待执行";
					} else if (exec_status == PopConstant.TASK_STATUS_PDSX) {
						exec_status_name = "任务失效";
					} else if (exec_status == PopConstant.TASK_STATUS_PDZT) {
						exec_status_name = "任务暂停";
						can_restart = true;
					} else if (exec_status == PopConstant.TASK_STATUS_PDZZ) {
						exec_status_name = "任务终止";
					}
					task.set("exec_status_name", exec_status_name);
					task.set("can_restart", can_restart);
				}
			}
			ruleBean.setTaskList(tasks);
			ruleBeans.add(ruleBean);//添加规则 
			ruleBeans.addAll(getAllPolicyRulesByPid(id, baseinfo));//递归添加子规则
		}
		return ruleBeans;
	}

	/**
	 * 重启任务
	 */
	public void reStartTask() {
		Map<String, String> result = new HashMap<String, String>();
		String policyId = getPara("policyId");
		String ruleId = getPara("ruleId");
		String taskId = getPara("taskId");
		try {
			IPopSendOddService sendOddService = (IPopSendOddService) SystemServiceLocator.getInstance().getService(
					"IPopSendOddService");
			sendOddService.reStartPolicyRuleTask(policyId, ruleId, taskId);
			result.put("success", "1");
		} catch (Exception e) {
			result.put("success", "0");
			result.put("msg", e.getMessage());
			log.error("重启任务[policyId=" + policyId + ",ruleId=" + ruleId + ",taskId=" + taskId + "]失败", e);
		}
		this.renderJson(result);
	}
	
}
