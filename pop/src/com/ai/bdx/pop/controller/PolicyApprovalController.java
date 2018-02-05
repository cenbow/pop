package com.ai.bdx.pop.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import jxl.write.WriteException;
import net.sf.json.JSONObject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ai.bdx.frame.approval.bean.ApApproveList;
import com.ai.bdx.frame.approval.bean.ApprovalTitle;
import com.ai.bdx.frame.approval.service.IApprovalService;
import com.ai.bdx.pop.base.PopController;
import com.ai.bdx.pop.bean.MailSendInfo;
import com.ai.bdx.pop.enums.PolicyStatus;
import com.ai.bdx.pop.model.PopDimAreaType;
import com.ai.bdx.pop.model.PopDimAviodType;
import com.ai.bdx.pop.model.PopDimCampChannel;
import com.ai.bdx.pop.model.PopDimContactFreq;
import com.ai.bdx.pop.model.PopDimControlType;
import com.ai.bdx.pop.model.PopDimNetType;
import com.ai.bdx.pop.model.PopDimPolicyLevel;
import com.ai.bdx.pop.model.PopDimPolicyStatus;
import com.ai.bdx.pop.model.PopDimPolicyType;
import com.ai.bdx.pop.model.PopPolicyBaseinfo;
import com.ai.bdx.pop.model.PopPolicyBaseinfoFiles;
import com.ai.bdx.pop.model.PopPolicyRule;
import com.ai.bdx.pop.model.PopPolicyRuleAct;
import com.ai.bdx.pop.model.PopPolicyRuleCustgroup;
import com.ai.bdx.pop.model.PopPolicyRuleEventCon;
import com.ai.bdx.pop.model.PopPolicyRuleExecTime;
import com.ai.bdx.pop.service.SmsService;
import com.ai.bdx.pop.util.ApprovalCONST;
import com.ai.bdx.pop.util.CityId2CityNameUtil;
import com.ai.bdx.pop.util.LogOperateUtil;
import com.ai.bdx.pop.util.PopConfigure;
import com.ai.bdx.pop.util.excel.MailExcel;
import com.ai.bdx.pop.util.excel.PccAction;
import com.ai.bdx.pop.util.excel.PccCondition;
import com.ai.bdx.pop.util.excel.PccCustomer;
import com.ai.bdx.pop.util.excel.PccPolicyBean;
import com.ai.bdx.pop.util.excel.PccRuleBean;
import com.ai.bdx.pop.util.page.SplitPageBean;
import com.ai.bdx.pop.wsclient.IPopSendMailClient;
import com.asiainfo.biframe.privilege.IUser;
import com.asiainfo.biframe.privilege.IUserPrivilegeService;
import com.asiainfo.biframe.utils.config.Configure;
import com.asiainfo.biframe.utils.spring.SystemServiceLocator;
import com.asiainfo.biframe.utils.string.StringUtil;
import com.jfinal.plugin.activerecord.Db;

public class PolicyApprovalController extends PopController {
	private static final Logger log = LogManager.getLogger(PolicyApprovalController.class);
	private String[] pop_policy_baseinfoP_cols = new String[] { "id", "policy_name", "policy_desc", "start_time",
			"end_time", "policy_type_id", "policy_level_id", "template_flag", "policy_status_id", "create_user_id",
			"create_time", "policy_task_tab" };

	public void searchApproveInit() {
		initAttributes();
		List<PopDimPolicyType> selectDimPolicyTypes = PopDimPolicyType.dao().findAll();
		List<PopDimPolicyLevel> dimPolicyLevels = PopDimPolicyLevel.dao().findAll();
		List<PopDimPolicyStatus> popDimPolicyStatus = PopDimPolicyStatus.dao().findAll();
		setAttr("popDimPolicyStatus", popDimPolicyStatus);
		setAttr("selectDimPolicyTypes", selectDimPolicyTypes);
		setAttr("dimPolicyLevels", dimPolicyLevels);
		render("policyConfig/policySceneApproval.jsp");
	}

	public boolean isArrayContains(String depts, int deptID) {
		boolean flag = false;
		String[] arrs = null;
		if (StringUtil.isNotEmpty(depts)) {
			arrs = depts.split(",");
		}
		if (arrs != null && arrs.length > 0 && StringUtil.isNotEmpty(deptID)) {
			for (String str : arrs) {
				if (str.equals(deptID + "")) {
					flag = true;
					return flag;
				}
			}
		}
		return flag;
	}

	/**
	 * 查询待审批列表
	 */
	public void approvalSeachList() {
		List<Object> values = new ArrayList<Object>();
		initAttributes();
		SplitPageBean splitPage = newSplitPage();

		List<PopDimPolicyType> selectDimPolicyTypes = PopDimPolicyType.dao().findAll();
		List<PopDimPolicyLevel> dimPolicyLevels = PopDimPolicyLevel.dao().findAll();
		List<PopDimPolicyStatus> popDimPolicyStatus = PopDimPolicyStatus.dao().findAll();
		setAttr("popDimPolicyStatus", popDimPolicyStatus);
		setAttr("selectDimPolicyTypes", selectDimPolicyTypes);
		setAttr("dimPolicyLevels", dimPolicyLevels);
		PopPolicyBaseinfo serachPolicy = this.getModel(PopPolicyBaseinfo.class, "popPolicyBaseinfo");
		String startDate = this.getPara("startDate");
		String endDate = this.getPara("endDate");

		StringBuffer b = new StringBuffer();
		b.append("select ").append(getBaseinfoCols("b")).append(",b.city_priority from pop_policy_baseinfo b where 1=1 ");
		b.append(" and b.policy_status_id in(30,31,32)");
		if (StringUtil.isNotEmpty(startDate)) {
			b.append(" and date_format(b.create_time,'%Y-%m-%d')>='").append(startDate).append("'");
		}
		if (StringUtil.isNotEmpty(endDate)) {
			b.append("  and date_format(b.create_time,'%Y-%m-%d')<='").append(endDate).append("'");
		}
		if (serachPolicy != null) {
			String policy_id = serachPolicy.get(PopPolicyBaseinfo.COL_ID);
			String policy_name = serachPolicy.get(PopPolicyBaseinfo.COL_POLICY_NAME);
			String policy_type = serachPolicy.get(PopPolicyBaseinfo.COL_POLICY_TYPE_ID);
			Integer policy_level = serachPolicy.get(PopPolicyBaseinfo.COL_POLICY_LEVEL_ID);
			Integer policy_stat = serachPolicy.get(PopPolicyBaseinfo.COL_POLICY_STATUS_ID);
			if (StringUtil.isNotEmpty(policy_id)) {
				b.append(" and b.id =?" );
				values.add(policy_id);
			}
			if (StringUtil.isNotEmpty(policy_name)) {
				b.append(" and b.policy_name like ? ");
				values.add("%"+policy_name+"%");
			}
			if (StringUtil.isNotEmpty(policy_type)) {
				b.append(" and b.policy_type_id= ? ");
				values.add(policy_type);
			}
			if (StringUtil.isNotEmpty(policy_level)) {
				b.append(" and b.policy_level_id= ? ");
				values.add(policy_level);
			}
			if (StringUtil.isNotEmpty(policy_stat)) {
				b.append(" and b.policy_status_id= ?");
				values.add(policy_stat);
			}
		}
		b.append(" and  b.id in(select a.approval_id from ap_approve_list a  where  a.approve_token =").append(
				ApprovalCONST.APPROVE_TOKEN_HOLD);
		b.append(" and  a.approve_type='").append(ApprovalCONST.FLOW_TYPE_APPROVL).append("' and a.approve_userid='")
				.append(userId).append("') ");
		b.append(" order by create_time desc");
		List<PopPolicyBaseinfo> popPolicyBaseinfoList=null;
		try {
			if (splitPage != null) {
				List<PopPolicyBaseinfo> infoList = PopPolicyBaseinfo.dao().find(b.toString(),values.toArray());
				splitPage.setTotalCount(infoList.size());

				b.append(" limit ").append(splitPage.getFirstRecord()).append(",").append(splitPage.getMaxRecord());
			}
			log.debug("查询待审批策略sql={}", b.toString());
			popPolicyBaseinfoList = PopPolicyBaseinfo.dao().find(b.toString(),values.toArray());
			if(popPolicyBaseinfoList != null){
				for(PopPolicyBaseinfo popPolicyBaseinfo : popPolicyBaseinfoList){
					try {
						popPolicyBaseinfo.put(PopPolicyBaseinfo.COL_CITY_PRIORITY, CityId2CityNameUtil.getCityNameByCache(popPolicyBaseinfo.getStr(PopPolicyBaseinfo.COL_CITY_PRIORITY)));
					} catch (Exception e) {
						log.error("根据地市ID查询城市名称异常：" + e);
					}
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		setAttr("popPolicyBaseinfo", popPolicyBaseinfoList);
		setAttr("splitPage", splitPage);
		String sbDeptID = PopConfigure.getInstance().getProperty("DEPT_ID_SB");
		setAttr("isShangbao", isArrayContains(sbDeptID, user.getDepartmentid()));

		render("policyConfig/policySceneApprovalList.jsp");
	}

	// 确认列表初始化
	public void searchConfirmInit() {
		initAttributes();

		List<PopDimPolicyType> selectDimPolicyTypes = PopDimPolicyType.dao().findAll();
		List<PopDimPolicyLevel> dimPolicyLevels = PopDimPolicyLevel.dao().findAll();
		List<PopDimPolicyStatus> popDimPolicyStatus = PopDimPolicyStatus.dao().findAll();
		setAttr("popDimPolicyStatus", popDimPolicyStatus);
		setAttr("selectDimPolicyTypes", selectDimPolicyTypes);
		setAttr("dimPolicyLevels", dimPolicyLevels);
		render("policyConfig/policySceneConfirm.jsp");
	}

	// 确认列表查询
	public void confirmSeachList() {
		List<Object> values = new ArrayList<Object>();
		initAttributes();
		SplitPageBean splitPage = newSplitPage();
		List<PopDimPolicyType> selectDimPolicyTypes = PopDimPolicyType.dao().findAll();
		List<PopDimPolicyLevel> dimPolicyLevels = PopDimPolicyLevel.dao().findAll();
		List<PopDimPolicyStatus> popDimPolicyStatus = PopDimPolicyStatus.dao().findAll();
		setAttr("popDimPolicyStatus", popDimPolicyStatus);
		setAttr("selectDimPolicyTypes", selectDimPolicyTypes);
		setAttr("dimPolicyLevels", dimPolicyLevels);
		PopPolicyBaseinfo serachPolicy = this.getModel(PopPolicyBaseinfo.class, "popPolicyBaseinfo");
		String startDate = this.getPara("startDate");
		String endDate = this.getPara("endDate");

		StringBuffer b = new StringBuffer();
		b.append("select ").append(getBaseinfoCols("b")).append(",b.city_priority from pop_policy_baseinfo b where 1=1 ");
		b.append(" and b.policy_status_id in(40,41,42)");
		if (StringUtil.isNotEmpty(startDate)) {
			b.append(" and date_format(b.create_time,'%Y-%m-%d')>='").append(startDate).append("'");
		}
		if (StringUtil.isNotEmpty(endDate)) {
			b.append("  and date_format(b.create_time,'%Y-%m-%d')<='").append(endDate).append("'");
		}
		if (serachPolicy != null) {
			String policy_id = serachPolicy.get(PopPolicyBaseinfo.COL_ID);
			String policy_name = serachPolicy.get(PopPolicyBaseinfo.COL_POLICY_NAME);
			String policy_type = serachPolicy.get(PopPolicyBaseinfo.COL_POLICY_TYPE_ID);
			Integer policy_level = serachPolicy.get(PopPolicyBaseinfo.COL_POLICY_LEVEL_ID);
			Integer policy_stat = serachPolicy.get(PopPolicyBaseinfo.COL_POLICY_STATUS_ID);
			if (StringUtil.isNotEmpty(policy_id)) {
				b.append(" and b.id = ? ");
				values.add(policy_id);
			}
			if (StringUtil.isNotEmpty(policy_name)) {
				b.append(" and b.policy_name like ? ");
				values.add("%"+policy_name+"%");
			}
			if (StringUtil.isNotEmpty(policy_type)) {
				b.append(" and b.policy_type_id='").append(policy_type).append("'");
			}
			if (StringUtil.isNotEmpty(policy_level)) {
				b.append(" and b.policy_level_id=").append(policy_level);
			}
			if (StringUtil.isNotEmpty(policy_stat)) {
				b.append(" and b.policy_status_id=").append(policy_stat);
			}
		}
		b.append(" and  b.id in(select a.approval_id from ap_approve_list a  where  a.approve_token =").append(
				ApprovalCONST.APPROVE_TOKEN_HOLD);
		b.append(" and  a.approve_type='").append(ApprovalCONST.FLOW_TYPE_CONFIRM).append("' and a.approve_userid='")
				.append(userId).append("') ");

		b.append(" order by create_time desc");
		if (splitPage != null) {
			try {
				List<PopPolicyBaseinfo> BaseinfoList = PopPolicyBaseinfo.dao().find(b.toString(),values.toArray());
				splitPage.setTotalCount(BaseinfoList.size());
				b.append(" limit ").append(splitPage.getFirstRecord()).append(",").append(splitPage.getMaxRecord());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		log.debug("查询待确认策略sql={}", b.toString());
		List<PopPolicyBaseinfo> popPolicyBaseinfo=null;
		try {
			popPolicyBaseinfo = PopPolicyBaseinfo.dao().find(b.toString(),values.toArray());
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		if(popPolicyBaseinfo != null){
			for(PopPolicyBaseinfo item : popPolicyBaseinfo){
				try {
					item.put(PopPolicyBaseinfo.COL_CITY_PRIORITY, CityId2CityNameUtil.getCityNameByCache(item.getStr(PopPolicyBaseinfo.COL_CITY_PRIORITY)));
				} catch (Exception e) {
					log.error("根据地市ID查询城市名称异常：" + e);
				}
			}
		}
		setAttr("popPolicyBaseinfo", popPolicyBaseinfo);
		setAttr("splitPage", splitPage);
		render("policyConfig/policySceneConfirmList.jsp");
	}

	// 审批通过
	public void approvalPass() {
		initAttributes();
		String ids = this.getPara("id");
		if (StringUtil.isNotEmpty(ids)) {
			try {
				IApprovalService service = (IApprovalService) SystemServiceLocator.getInstance().getService(
						ApprovalCONST.APPROVE_SERVICE);
				String[] arrs = ids.split(",");
				if (arrs != null && arrs.length > 0) {
					for (int i = 0; i < arrs.length; i++) {
						String b = service.updateApprovalToken(arrs[i], ApprovalCONST.FLOW_TYPE_APPROVL,
								ApprovalCONST.APPROVE_FLAG_ONE, userId);
						// 更新策略状态
						PopPolicyBaseinfo baseInfo = PopPolicyBaseinfo.dao().findById(arrs[i]);
						String flowModelID = service.getApproveDrvType(ApprovalCONST.APPROVDRVDIMTABLE_CHANGJING,
								baseInfo.getStr(PopPolicyBaseinfo.COL_POLICY_TYPE_ID));
						if (ApprovalCONST.APPROVE_LEVEL_NEXTHAS.equals(b) || ApprovalCONST.APPROVE_LEVEL_HAS.equals(b)) {// 当还有下级或者同级审批确认人时候，状态审批中
							baseInfo.set(PopPolicyBaseinfo.COL_POLICY_STATUS_ID, String.valueOf(PolicyStatus.APPROVING));
							String sql = "update pop_policy_baseinfo b set b.policy_status_id=? where b.id=?";
							Db.update(sql, PolicyStatus.APPROVING.getValue(), arrs[i]);
							// 更新规则
							String ruleSQL = "update pop_policy_rule set rule_status=? where policy_id=?";
							Db.update(ruleSQL, PolicyStatus.APPROVING.getValue(), arrs[i]);
							SmsService smsServcie = (SmsService) SystemServiceLocator.getInstance().getService(
									"IPopSmsService");
							smsServcie.SendMessage("SP001", arrs[i]);
						} else if (ApprovalCONST.APPROVE_LEVEL_OVER.equals(b)) {// 当没有下级审批确认人时候，状态变成审批通过
							baseInfo.set(PopPolicyBaseinfo.COL_POLICY_STATUS_ID, String.valueOf(PolicyStatus.APPROVED));
							String sql = "update pop_policy_baseinfo b set b.policy_status_id=? where b.id=?";
							Db.update(sql, PolicyStatus.APPROVED.getValue(), arrs[i]);
							// 更新规则
							String ruleSQL = "update pop_policy_rule set rule_status=? where policy_id=?";
							Db.update(ruleSQL, PolicyStatus.APPROVED.getValue(), arrs[i]);
							// 发送提醒短信
							SmsService smsServcie = (SmsService) SystemServiceLocator.getInstance().getService(
									"IPopSmsService");
							smsServcie.SendMessage("SP002", arrs[i]);
						}

						// 继续判断，如果没有下级审批人且状态是审批通过，把令牌传递给第一个（批）确认人
						if (ApprovalCONST.APPROVE_LEVEL_OVER.equals(b)
								&& String.valueOf(PolicyStatus.APPROVED).equals(
										baseInfo.getStr(PopPolicyBaseinfo.COL_POLICY_STATUS_ID))) {

							// 只有基本信息无审批无确认环节的已经在提交时直接把状态修改为待派单，此处无需判断
							if (ApprovalCONST.FLOW_MODEL_TWO.equals(flowModelID)) {// 基本信息审批，无确认过程，审批通过之后直接待派单
								// 更新策略
								String sql = "update pop_policy_baseinfo b set b.policy_status_id=? where b.id=?";
								Db.update(sql, PolicyStatus.UNSENDODER.getValue(), arrs[i]);
								// 更新规则
								String ruleSQL = "update pop_policy_rule set rule_status=? where policy_id=?";
								Db.update(ruleSQL, PolicyStatus.UNSENDODER.getValue(), arrs[i]);

							} else if (ApprovalCONST.FLOW_MODEL_THREE.equals(flowModelID)) {// 基本信息确认 确认完成之后直接派单
								log.error("审批确认过程中没有确认环节只有审批环节，无需操作！");
							} else if (ApprovalCONST.FLOW_MODEL_FOUR.equals(flowModelID)) {// 基本信息审批 确认
																							// 确认完成之后直接派单
								// 当审批通过后把令牌传递给第一确认人
								service.updateFirstApproveOrConfirmUserToken(arrs[i], ApprovalCONST.FLOW_TYPE_CONFIRM);
								// 更新策略
								String sql = "update pop_policy_baseinfo b set b.policy_status_id=? where b.id=?";
								Db.update(sql, PolicyStatus.UNCONFIRM.getValue(), arrs[i]);
								// 更新规则
								String ruleSQL = "update pop_policy_rule set rule_status=? where policy_id=?";
								Db.update(ruleSQL, PolicyStatus.UNCONFIRM.getValue(), arrs[i]);

							}
							SmsService smsServcie = (SmsService) SystemServiceLocator.getInstance().getService(
									"IPopSmsService");
							smsServcie.SendMessage("QR001", arrs[i]);
						}
					}
				}
				setAttr("sucFlag", 1);
				//记录日志
				String desc = String.format("策略审批通过,策略编号:%s", ids);
				LogOperateUtil.log(LogOperateUtil.POLICY_MAN_APPROVAL_PASS,desc,this.getRequest());
			} catch (Exception e) {
				log.error("error message：{}", e);
				log.error("用户{}通过审批{}失败", this.userId, ids);
				//记录日志
				String desc = String.format("策略审批通过失败:%s,策略编号:%s", e.getMessage(),ids);
				LogOperateUtil.log(LogOperateUtil.POLICY_MAN_APPROVAL_PASS,desc,this.getRequest());
			}
		}
		approvalSeachList();
		render("policyConfig/policySceneApprovalList.jsp");
	}

	// 拒绝流程初始化拒绝意见填写窗口
	public void approvalNotPassInit() {
		initAttributes();
		String ids = this.getPara("id");
		this.setAttr("approvl_ids", ids);
		render("policyConfig/policySceneApprovalRefuse.jsp");
	}

	// 审批不通过
	public void approvalNotPass() {
		initAttributes();
		String approvl_id = getPara("approvl_id");
		String advice = getPara("approve_advice");
		try {
			IApprovalService service = (IApprovalService) SystemServiceLocator.getInstance().getService(
					ApprovalCONST.APPROVE_SERVICE);
			if (StringUtil.isNotEmpty(approvl_id)) {
				String[] id_arrs = approvl_id.split(",");
				for (int i = 0; i < id_arrs.length; i++) {
					String app_id = id_arrs[i];
					PopPolicyBaseinfo baseInfo = PopPolicyBaseinfo.dao().findById(app_id);
					String drvID = baseInfo.getStr(PopPolicyBaseinfo.COL_POLICY_TYPE_ID);
					String drvTypeID = ApprovalCONST.APPROVDRVDIMTABLE_CHANGJING;
					service.updateApprovalNotPass(app_id, ApprovalCONST.FLOW_TYPE_APPROVL, userId, drvTypeID, drvID,
							advice);
					// 更新策略状态
					String sql = "update pop_policy_baseinfo b set b.policy_status_id=? where b.id=?";
					Db.update(sql, PolicyStatus.EDITING.getValue(), app_id);
					// 更新规则
					String ruleSQL = "update pop_policy_rule set rule_status=? where policy_id=?";
					Db.update(ruleSQL, PolicyStatus.EDITING.getValue(), app_id);

					SmsService smsServcie = (SmsService) SystemServiceLocator.getInstance()
							.getService("IPopSmsService");
					smsServcie.SendMessage("TH001", app_id);
				}
			}
			setAttr("approvl_ids", approvl_id);
			setAttr("approveAdvice", advice);
			setAttr("sucFlag", 1);
			//记录日志
			String desc = String.format("策略审批拒绝,策略编号:%s", approvl_id);
			LogOperateUtil.log(LogOperateUtil.POLICY_MAN_APPROVAL_ADDJECT,desc,this.getRequest());
		} catch (Exception e) {
			log.error("error message：{}", e);
			log.error("用户{}拒绝审批{}失败", this.userId, approvl_id);
			//记录日志
			String desc = String.format("策略审批拒绝失败:%s,策略编号:%s", e.getMessage(), approvl_id);
			LogOperateUtil.log(LogOperateUtil.POLICY_MAN_APPROVAL_ADDJECT,desc,this.getRequest());
		}

		render("policyConfig/policySceneApprovalRefuse.jsp");
	}

	// 初始化转发其他人
	public void initTranOtherUser() throws Exception {
		initAttributes();
		String ids = this.getPara("id");
		this.setAttr("approvl_ids", ids);
		render("policyConfig/policySceneApprovalTranOtherUser.jsp");
	}

	/**
	 * tranOtherUser:转发其他人
	 * 
	 * @throws Exception
	 * @return void
	 */
	public void tranOtherUser() throws Exception {
		initAttributes();
		String ids = this.getPara("approvl_id");
		String tranUserID = this.getPara("tran_user_id");

		if (StringUtil.isNotEmpty(ids) && StringUtil.isNotEmpty(tranUserID)) {
			try {
				IApprovalService service = (IApprovalService) SystemServiceLocator.getInstance().getService(
						ApprovalCONST.APPROVE_SERVICE);
				IUserPrivilegeService userPrivilegeService = (IUserPrivilegeService) SystemServiceLocator.getInstance()
						.getService("userPrivilegeService");
				String[] arrs = ids.split(",");
				IUser user = userPrivilegeService.getUser(tranUserID);
				String tranUserName = user.getUsername();
				if (arrs != null && arrs.length > 0) {
					for (int i = 0; i < arrs.length; i++) {
						// 审批先转发到转发人
						int saveResult = service.saveTranOtherUser(arrs[i], tranUserID);
						// 当前人审批通过
						String b = service.updateApprovalToken(arrs[i], ApprovalCONST.FLOW_TYPE_APPROVL,
								ApprovalCONST.APPROVE_FLAG_ONE, userId);
						PopPolicyBaseinfo baseInfo = PopPolicyBaseinfo.dao().findById(arrs[i]);
						// 更新策略状态
						if (ApprovalCONST.APPROVE_LEVEL_NEXTHAS.equals(b) || ApprovalCONST.APPROVE_LEVEL_HAS.equals(b)) {// 当还有下级或者同级审批确认人时候，状态审批中
							baseInfo.set(PopPolicyBaseinfo.COL_POLICY_STATUS_ID, String.valueOf(PolicyStatus.APPROVING));
							String sql = "update pop_policy_baseinfo b set b.policy_status_id=? where b.id=?";
							Db.update(sql, PolicyStatus.APPROVING.getValue(), arrs[i]);
							// 更新规则
							String ruleSQL = "update pop_policy_rule set rule_status=? where policy_id=?";
							Db.update(ruleSQL, PolicyStatus.APPROVING.getValue(), arrs[i]);

							SmsService smsServcie = (SmsService) SystemServiceLocator.getInstance().getService(
									"IPopSmsService");
							smsServcie.SendMessage("SP001", arrs[i]);

						} else if (ApprovalCONST.APPROVE_LEVEL_OVER.equals(b)) {// 当没有下级审批确认人时候，状态变成审批通过
							baseInfo.set(PopPolicyBaseinfo.COL_POLICY_STATUS_ID, String.valueOf(PolicyStatus.APPROVED));
							String sql = "update pop_policy_baseinfo b set b.policy_status_id=? where b.id=?";
							Db.update(sql, PolicyStatus.APPROVED.getValue(), arrs[i]);
							// 更新规则
							String ruleSQL = "update pop_policy_rule set rule_status=? where policy_id=?";
							Db.update(ruleSQL, PolicyStatus.APPROVED.getValue(), arrs[i]);

							SmsService smsServcie = (SmsService) SystemServiceLocator.getInstance().getService(
									"IPopSmsService");
							smsServcie.SendMessage("SP002", arrs[i]);
						}
					}
				}

				setAttr("approvl_ids", ids);
				setAttr("user_id", tranUserID);
				setAttr("user_name", tranUserName);
				setAttr("sucFlag", 1);
				//记录日志
				String desc = String.format("策略审批转发,策略编号:%s,转发给:%s", ids,tranUserID);
				LogOperateUtil.log(LogOperateUtil.POLICY_MAN_APPROVAL_TURN,desc,this.getRequest());
			} catch (Exception e) {
				log.error("error message：{}", e);
				log.error("用户{}通过审批{}失败", this.userId, ids);
				//记录日志
				String desc = String.format("策略审批转发失败:%s,策略编号:%s,转发给:%s",  e.getMessage(),ids,tranUserID);
				LogOperateUtil.log(LogOperateUtil.POLICY_MAN_APPROVAL_TURN,desc,this.getRequest());
			}
		}
		approvalSeachList();
		render("policyConfig/policySceneApprovalList.jsp");

		render("policyConfig/policySceneApprovalTranOtherUser.jsp");
	}

	// 确认通过
	public void approvalConfirmPass() {
		initAttributes();
		String ids = this.getPara("id");
		if (StringUtil.isNotEmpty(ids)) {
			try {
				IApprovalService service = (IApprovalService) SystemServiceLocator.getInstance().getService(
						ApprovalCONST.APPROVE_SERVICE);
				String[] arrs = ids.split(",");
				if (arrs != null && arrs.length > 0) {
					for (int i = 0; i < arrs.length; i++) {
						String b = service.updateApprovalToken(arrs[i], ApprovalCONST.FLOW_TYPE_CONFIRM,
								ApprovalCONST.APPROVE_FLAG_ONE, userId);
						// 更新策略状态
						PopPolicyBaseinfo baseInfo = PopPolicyBaseinfo.dao().findById(arrs[i]);
						String flowModelID = service.getApproveDrvType(ApprovalCONST.APPROVDRVDIMTABLE_CHANGJING,
								baseInfo.getStr(PopPolicyBaseinfo.COL_POLICY_TYPE_ID));
						if (ApprovalCONST.APPROVE_LEVEL_NEXTHAS.equals(b) || ApprovalCONST.APPROVE_LEVEL_HAS.equals(b)) {// 当还有下级同级审批确认人时候，状态修改成确认中
							baseInfo.set(PopPolicyBaseinfo.COL_POLICY_STATUS_ID,
									String.valueOf(PolicyStatus.CONFIRMING));
							String sql = "update pop_policy_baseinfo b set b.policy_status_id=? where b.id=?";
							Db.update(sql, PolicyStatus.CONFIRMING.getValue(), arrs[i]);

							// 更新规则
							String ruleSQL = "update pop_policy_rule set rule_status=? where policy_id=?";
							Db.update(ruleSQL, PolicyStatus.CONFIRMING.getValue(), arrs[i]);

							SmsService smsServcie = (SmsService) SystemServiceLocator.getInstance().getService(
									"IPopSmsService");
							smsServcie.SendMessage("QR001", arrs[i]);

						} else if (ApprovalCONST.APPROVE_LEVEL_OVER.equals(b)) {// 当没有下级或者同级审批确认人时候，状态变成审批通过
							baseInfo.set(PopPolicyBaseinfo.COL_POLICY_STATUS_ID, String.valueOf(PolicyStatus.CONFIRMED));
							String sql = "update pop_policy_baseinfo b set b.policy_status_id=? where b.id=?";
							Db.update(sql, PolicyStatus.CONFIRMED.getValue(), arrs[i]);

							// 更新规则
							String ruleSQL = "update pop_policy_rule set rule_status=? where policy_id=?";
							Db.update(ruleSQL, PolicyStatus.CONFIRMED.getValue(), arrs[i]);

							SmsService smsServcie = (SmsService) SystemServiceLocator.getInstance().getService(
									"IPopSmsService");
							smsServcie.SendMessage("QR002", arrs[i]);
						}
						// 如果没下级确认人，直接修改成待派单
						if (ApprovalCONST.APPROVE_LEVEL_OVER.equals(b)
								&& String.valueOf(PolicyStatus.CONFIRMED).equals(
										baseInfo.getStr(PopPolicyBaseinfo.COL_POLICY_STATUS_ID))) {

							// 只有基本信息无审批无确认环节的已经在提交时直接把状态修改为待派单，此处无需判断
							if (ApprovalCONST.FLOW_MODEL_TWO.equals(flowModelID)) {// 基本信息审批，无确认过程，审批通过之后直接待派单
								log.error("审批确认过程中没有审批环节只有确认环节，无需操作！");
							} else if (ApprovalCONST.FLOW_MODEL_THREE.equals(flowModelID)) {// 基本信息确认 确认完成之后直接派单
								String sql = "update pop_policy_baseinfo b set b.policy_status_id=? where b.id=?";
								Db.update(sql, PolicyStatus.UNSENDODER.getValue(), arrs[i]);

								// 更新规则
								String ruleSQL = "update pop_policy_rule set rule_status=? where policy_id=?";
								Db.update(ruleSQL, PolicyStatus.UNSENDODER.getValue(), arrs[i]);

							} else if (ApprovalCONST.FLOW_MODEL_FOUR.equals(flowModelID)) {// 基本信息 审批 确认
																							// 确认完成之后直接派单
								String sql = "update pop_policy_baseinfo b set b.policy_status_id=? where b.id=?";
								Db.update(sql, PolicyStatus.UNSENDODER.getValue(), arrs[i]);

								// 更新规则
								String ruleSQL = "update pop_policy_rule set rule_status=? where policy_id=?";
								Db.update(ruleSQL, PolicyStatus.UNSENDODER.getValue(), arrs[i]);

							}
							SmsService smsServcie = (SmsService) SystemServiceLocator.getInstance().getService(
									"IPopSmsService");
							smsServcie.SendMessage("PD001", arrs[i]);

						}
					}
				}
				setAttr("sucFlag", 1);
				//记录日志
				String desc = String.format("策略审批确认通过,策略编号:%s", ids);
				LogOperateUtil.log(LogOperateUtil.POLICY_MAN_APPROVAL_CONFIRM_PASS,desc,this.getRequest());
			} catch (Exception e) {
				log.error("error message：{}", e);
				log.error("用户{}通过审批{}失败", this.userId, ids);
				//记录日志
				String desc = String.format("策略审批确认通过失败:%s,策略编号:%s",  e.getMessage(),ids);
				LogOperateUtil.log(LogOperateUtil.POLICY_MAN_APPROVAL_CONFIRM_PASS,desc,this.getRequest());
			}
		}
		confirmSeachList();
		render("policyConfig/policySceneConfirmList.jsp");
	}

	// 确认流程初始化确认意见填写窗口
	public void approvalConfirmNotPassInit() {
		initAttributes();
		String ids = this.getPara("id");
		this.setAttr("confirm_ids", ids);
		render("policyConfig/policySceneConfirmRefuse.jsp");
	}

	// 确认不通过
	public void approvalConfirmNotPass() {
		initAttributes();
		String approvl_id = getPara("approvl_id");
		String advice = getPara("approve_advice");
		try {
			IApprovalService service = (IApprovalService) SystemServiceLocator.getInstance().getService(
					ApprovalCONST.APPROVE_SERVICE);
			if (StringUtil.isNotEmpty(approvl_id)) {
				String[] id_arrs = approvl_id.split(",");
				for (int i = 0; i < id_arrs.length; i++) {
					String app_id = id_arrs[i];
					PopPolicyBaseinfo baseInfo = PopPolicyBaseinfo.dao().findById(app_id);
					String drvID = baseInfo.getStr(PopPolicyBaseinfo.COL_POLICY_TYPE_ID);
					String drvTypeID = ApprovalCONST.APPROVDRVDIMTABLE_CHANGJING;
					service.updateApprovalNotPass(app_id, ApprovalCONST.FLOW_TYPE_CONFIRM, userId, drvTypeID, drvID,
							advice);
					// 更新策略状态
					String sql = "update pop_policy_baseinfo b set b.policy_status_id=? where b.id=?";
					Db.update(sql, PolicyStatus.EDITING.getValue(), app_id);

					// 更新规则
					String ruleSQL = "update pop_policy_rule set rule_status=? where policy_id=?";
					Db.update(ruleSQL, PolicyStatus.EDITING.getValue(), app_id);

					SmsService smsServcie = (SmsService) SystemServiceLocator.getInstance()
							.getService("IPopSmsService");
					smsServcie.SendMessage("TH001", app_id);
				}
			}
			setAttr("confirm_ids", approvl_id);
			setAttr("approveAdvice", advice);
			setAttr("sucFlag", 1);
			//记录日志
			String desc = String.format("策略审批确认不通过,策略编号:%s", approvl_id);
			LogOperateUtil.log(LogOperateUtil.POLICY_MAN_APPROVAL_CONFIRM_NOPASS,desc,this.getRequest());
		} catch (Exception e) {
			log.error("error message：{}", e);
			log.error("用户{}拒绝审批{}失败", this.userId, approvl_id);
			//记录日志
			String desc = String.format("策略审批确认不通过失败:%s,策略编号:%s", e.getMessage(), approvl_id);
			LogOperateUtil.log(LogOperateUtil.POLICY_MAN_APPROVAL_CONFIRM_NOPASS,desc,this.getRequest());
		}
		render("policyConfig/policySceneConfirmRefuse.jsp");
	}

	public static Map<Object, Object> List2Map(List<?> list, String type) {
		Map<Object, Object> map = new HashMap<Object, Object>();
		if (list != null && list.size() > 0) {
			if (type.equals("PopDimPolicyType")) {
				Iterator<?> it = list.iterator();
				while (it.hasNext()) {
					PopDimPolicyType p = (PopDimPolicyType) it.next();
					map.put(p.get(PopDimPolicyType.COL_ID), p.get(PopDimPolicyType.COL_NAME));
				}
			}
			if (type.equals("PopDimPolicyLevel")) {
				Iterator<?> it = list.iterator();
				while (it.hasNext()) {
					PopDimPolicyLevel p = (PopDimPolicyLevel) it.next();
					map.put(p.get(PopDimPolicyType.COL_ID), p.get(PopDimPolicyType.COL_NAME));
				}
			}
			if (type.equals("PopDimAreaType")) {
				Iterator<?> it = list.iterator();
				while (it.hasNext()) {
					PopDimAreaType p = (PopDimAreaType) it.next();
					map.put(p.get(PopDimPolicyType.COL_ID), p.get(PopDimPolicyType.COL_NAME));
				}
			}
		}
		return map;
	}

	// 查看进度
	public void viewProcess() {
		initAttributes();
		String policy_id = this.getPara("policy_id");
		if (StringUtil.isNotEmpty(policy_id)) {
			try {
				IApprovalService approvalService = (IApprovalService) SystemServiceLocator.getInstance().getService(
						ApprovalCONST.APPROVE_SERVICE);
				List<ApApproveList> approveList = approvalService.getApprovalProcessDesc(policy_id, null);
				TreeMap<String, String> typeMap = approvalService.getApprovalTypeProcess(policy_id);
				PopPolicyBaseinfo policyBaseInfo = PopPolicyBaseinfo.dao().findById(policy_id);
				Integer stat = policyBaseInfo.getInt(PopPolicyBaseinfo.COL_POLICY_STATUS_ID);// 根据状态判断当前处于哪个阶段
				LinkedHashSet<ApprovalTitle> typeSet = new LinkedHashSet<ApprovalTitle>();
				// 0是策划
				ApprovalTitle cehua = new ApprovalTitle();
				cehua.setKey(0);
				cehua.setDesc("策划");
				cehua.setStyle(this.getStyle(stat, 0));
				typeSet.add(cehua);

				if (typeMap != null) {
					Iterator<?> it = typeMap.keySet().iterator();
					while (it.hasNext()) {
						ApprovalTitle app = new ApprovalTitle();
						String key = it.next().toString();
						app.setKey(Integer.parseInt(key));
						app.setStyle(this.getStyle(stat, Integer.parseInt(key)));
						app.setDesc(typeMap.get(key));
						typeSet.add(app);
					}
				}
				// -1 是派单，其他可以一直累加
				ApprovalTitle paidan = new ApprovalTitle();
				paidan.setKey(999);
				paidan.setDesc("派单");
				paidan.setStyle(this.getStyle(stat, 999));
				typeSet.add(paidan);

				setAttr("typeSet", typeSet);// 导航列表
				setAttr("approveList", approveList);// 导航列表
				setAttr("stat", stat);// 当前状态
				setAttr("policyBaseInfo", policyBaseInfo);// 策划信息
				IUserPrivilegeService userPrivilegeService = (IUserPrivilegeService) SystemServiceLocator.getInstance()
						.getService("userPrivilegeService");
				IUser user = userPrivilegeService.getUser(policyBaseInfo.getStr(PopPolicyBaseinfo.COL_CREATE_USER_ID));
				setAttr("cehuaren", user.getUsername());// 策划人用户名称
				List<PopDimPolicyStatus> popDimPolicyStatus = PopDimPolicyStatus.dao().findAll();
				setAttr("popDimPolicyStatus", popDimPolicyStatus);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		render("policyConfig/policySceneApprovalProcess.jsp");
	}

	private String getStyle(Integer stat, Integer key) {
		String style = "step_pending";// step_ok step_on
		Integer current_stat = 0;
		if (stat <= 20) {// 策划中
			current_stat = 0;
		} else if (30 <= stat && stat < 40) {// 审批中
			current_stat = 1;
		} else if (40 <= stat && stat < 50) {// 确认中
			current_stat = 2;
		} else if (stat >= 50) {// 已派单
			current_stat = 999;
		}
		if (key < current_stat) {
			style = "step_pending";
		} else if (key == current_stat) {
			style = "step_on";
		} else if (key > current_stat) {
			style = "step_ok";
		}
		return style;
	}

	/**
	 * 获取所有列
	 * 
	 * @return
	 */
	public String getBaseinfoCols(String pref) {
		StringBuffer b = new StringBuffer();
		if (pop_policy_baseinfoP_cols.length > 0) {
			for (int i = 0; i < pop_policy_baseinfoP_cols.length; i++) {
				b.append(pref + "." + pop_policy_baseinfoP_cols[i]);
				if (i < (pop_policy_baseinfoP_cols.length - 1)) {
					b.append(",");
				}
			}
		} else {
			b.append(" * ");
		}
		return b.toString();
	}

	/**
	 * 
	 * reportCorporation: 上报集团，发送邮件
	 * 
	 * @return void
	 */
	public void reportCorporation() {
		initAttributes();
		String id = this.getPara("id");
		log.debug("策略【{}】准备上报集团", id);
		boolean sendFlag = false;
		// 维表取值
		if (StringUtil.isNotEmpty(id)) {
			try {
				String[] ids = id.split(",");
				if (ids != null && ids.length > 0) {
					for (String policy_id : ids) {

						PopPolicyBaseinfo baseInfo = PopPolicyBaseinfo.dao().findById(policy_id);
						String policy_name = baseInfo.getStr(PopPolicyBaseinfo.COL_POLICY_NAME);
						// String
						// create_date=baseInfo.getStr(PopPolicyBaseinfo.COL_CREATE_TIME);
						String create_user = baseInfo.getStr(PopPolicyBaseinfo.COL_CREATE_USER_ID);
						// 查询真实姓名
						IUserPrivilegeService userPrivilegeService = (IUserPrivilegeService) SystemServiceLocator
								.getInstance().getService("userPrivilegeService");
						IUser user = userPrivilegeService.getUser(create_user);

						String Templetefile = MailExcel.getFilePath();// 生成的模板文件路径
						File sFile = new File(Templetefile);
						if (!sFile.exists()) {
							sFile.mkdirs();
						}
						String FilePath = Templetefile + File.separator + MailExcel.getNow() + "_" + policy_name
								+ ".xlsx";

						// ********************封装数据start*********************************
						PccPolicyBean policyBean = new PccPolicyBean();
						policyBean.setPolicy_id(policy_id);
						policyBean.setPolicy_name(policy_name);

						// 查询规则
						HashMap<String, Object> rueSearchMap = new HashMap<String, Object>();
						rueSearchMap.put("policy_id", policy_id);
						List<PopPolicyRule> ruleList = PopPolicyRule.dao().search(rueSearchMap);
						if (ruleList != null && ruleList.size() > 0) {
							List<PccRuleBean> ruleBeanList = new ArrayList<PccRuleBean>();
							for (int ruleIndex = 0; ruleIndex < ruleList.size(); ruleIndex++) {
								PopPolicyRule policyRule = ruleList.get(ruleIndex);
								PccRuleBean ruleBean = new PccRuleBean();
								ruleBean.setRule_id(policyRule.getStr(PopPolicyRule.COL_ID));
								ruleBean.setRule_name(policyRule.getStr(PopPolicyRule.COL_POLICY_RULE_NAME));

								// 查询客户群pop_policy_rule_custgroup
								HashMap<String, Object> custoGroupMap = new HashMap<String, Object>();
								custoGroupMap.put("policy_rule_id", policyRule.getStr(PopPolicyRule.COL_ID));
								List<PopPolicyRuleCustgroup> custoGroupList = PopPolicyRuleCustgroup.dao().search(
										custoGroupMap);
								if (custoGroupList != null && custoGroupList.size() > 0) {
									List<PccCustomer> custGrouops = new ArrayList<PccCustomer>();
									for (int groupIndex = 0; groupIndex < custoGroupList.size(); groupIndex++) {
										PopPolicyRuleCustgroup cGroup = custoGroupList.get(groupIndex);
										PccCustomer cust = new PccCustomer();
										cust.setCustomerGroupID(cGroup.getStr(PopPolicyRuleCustgroup.COL_CUSTGROUP_ID));
										cust.setCustomerGroupName(cGroup
												.getStr(PopPolicyRuleCustgroup.COL_CUSTGROUP_NAME));
										// 处理客户群参数
										TreeMap<String, String> custAttr = new TreeMap<String, String>();
										// 取客户群扩展属性，逻辑待添加
										cust.setParam(custAttr);
										custGrouops.add(cust);
									}
									policyBean.getCustomers().addAll(custGrouops);
								}

								// 查询执行时间pop_policy_rule_exec_time
								HashMap<String, Object> execTimeMap = new HashMap<String, Object>();
								execTimeMap.put("policy_rule_id", policyRule.getStr(PopPolicyRule.COL_ID));
								List<PopPolicyRuleExecTime> execTimeList = PopPolicyRuleExecTime.dao().search(
										execTimeMap);
								String execDate = "";
								String connectTime = "";
								String noConnectTime = "";
								if (execTimeList != null && execTimeList.size() > 0) {
									PopPolicyRuleExecTime execTime = execTimeList.get(0);
									execDate = execTime.getStr(PopPolicyRuleExecTime.COL_DATE_RANGES);
									connectTime = execTime.getStr(PopPolicyRuleExecTime.COL_TIME_RANGES);
									noConnectTime = execTime.getStr(PopPolicyRuleExecTime.COL_AVOID_RANGES);
								}

								// 查询条件内容pop_policy_rule_event_con
								HashMap<String, Object> ruleEventMap = new HashMap<String, Object>();
								ruleEventMap.put("policy_rule_id", policyRule.getStr(PopPolicyRule.COL_ID));
								List<PopPolicyRuleEventCon> ruleEventList = PopPolicyRuleEventCon.dao().search(
										ruleEventMap);
								if (ruleEventList != null && ruleEventList.size() > 0) {
									int seq_index = 1;
									List<PccCondition> condList = new ArrayList<PccCondition>();
									PopPolicyRuleEventCon ruleCon = ruleEventList.get(0);
									String conData = ruleCon.getStr(PopPolicyRuleEventCon.COL_SIMPLE_CONDTIONS_DATA);
									if (StringUtil.isNotEmpty(conData)) {
										// {"net_type":"3","apn":"CMNET","terminal_manu":"MTK","terminal_brand":"荣耀","area_type_id":"3",
										// "busi_id":"841","user_grade":"11","user_flow_status":"1","user_flow":"1","time":"11","package_type":"11"}

										String terminal_manu = "";
										String terminal_brand = "";
										String terminal_model = "";

										JSONObject json = JSONObject.fromObject(conData);
										Iterator<?> it = json.keys();
										if (it != null & it.hasNext()) {
											while (it.hasNext()) {
												String key = (String) it.next();
												String value = json.getString(key);
												// 解析接入类型
												if (key.equalsIgnoreCase("net_type") && StringUtil.isNotEmpty(value)) {
													PccCondition cond = new PccCondition();
													cond.setNumber(String.valueOf(seq_index));
													PopDimNetType netType = PopDimNetType.dao().findById(value);
													cond.setConditionName("接入类型");
													cond.setOper("等于");
													cond.setVal(netType.getStr(PopDimNetType.COL_NAME));
													condList.add(cond);
													seq_index++;
												} else if (key.equalsIgnoreCase("apn") && StringUtil.isNotEmpty(value)) {// 解析APN
													PccCondition condAPN = new PccCondition();
													condAPN.setNumber(String.valueOf(seq_index));
													condAPN.setConditionName("APN");
													condAPN.setOper("等于");
													condAPN.setVal(value);
													condList.add(condAPN);
													seq_index++;
												} else if (key.equalsIgnoreCase("busi_id")
														&& StringUtil.isNotEmpty(value)) {// 业务
													PccCondition condBusiId = new PccCondition();
													condBusiId.setNumber(String.valueOf(seq_index));
													condBusiId.setConditionName("业务");
													condBusiId.setOper("等于");
													condBusiId.setVal(value);
													condList.add(condBusiId);
													seq_index++;
												} else if (key.equalsIgnoreCase("user_grade")
														&& StringUtil.isNotEmpty(value)) {// 用户等级
													PccCondition condUser_grade = new PccCondition();
													condUser_grade.setNumber(String.valueOf(seq_index));
													condUser_grade.setConditionName("用户等级");
													condUser_grade.setOper("等于");
													condUser_grade.setVal(value);
													condList.add(condUser_grade);
													seq_index++;
												} else if (key.equalsIgnoreCase("user_flow_status")
														&& StringUtil.isNotEmpty(value)) {// 流量状态
													PccCondition condUser_flow_status = new PccCondition();
													condUser_flow_status.setNumber(String.valueOf(seq_index));
													condUser_flow_status.setConditionName("业务");
													condUser_flow_status.setOper("等于");
													condUser_flow_status.setVal(value);
													condList.add(condUser_flow_status);
													seq_index++;
												} else if (key.equalsIgnoreCase("user_flow")
														&& StringUtil.isNotEmpty(value)) {// 流量
													PccCondition condUser_flow = new PccCondition();
													condUser_flow.setNumber(String.valueOf(seq_index));
													condUser_flow.setConditionName("流量");
													condUser_flow.setOper("等于");
													condUser_flow.setVal(value);
													condList.add(condUser_flow);
													seq_index++;
												} else if (key.equalsIgnoreCase("area_type_id")
														&& StringUtil.isNotEmpty(value)) {//位置
													PopDimAreaType area = PopDimAreaType.dao().findById(value);
													PccCondition condType_id = new PccCondition();
													condType_id.setNumber(String.valueOf(seq_index));
													condType_id.setConditionName("位置");
													condType_id.setOper("等于");
													condType_id.setVal(area.getStr(PopDimAreaType.COL_NAME));
													condList.add(condType_id);
													seq_index++;
												} else if (key.equalsIgnoreCase("time") && StringUtil.isNotEmpty(value)) {//时间
													PccCondition condTime = new PccCondition();
													condTime.setNumber(String.valueOf(seq_index));
													condTime.setConditionName("时间");
													condTime.setOper("等于");
													condTime.setVal(value);
													condList.add(condTime);
													seq_index++;
												} else if (key.equalsIgnoreCase("package_type")
														&& StringUtil.isNotEmpty(value)) {//套餐类型
													PccCondition condPackage_type = new PccCondition();
													condPackage_type.setNumber(String.valueOf(seq_index));
													condPackage_type.setConditionName("套餐类型");
													condPackage_type.setOper("等于");
													condPackage_type.setVal(value);
													condList.add(condPackage_type);
													seq_index++;
												} else if (key.equalsIgnoreCase("terminal_manu")
														&& StringUtil.isNotEmpty(value)) {
													terminal_manu = value;
												} else if (key.equalsIgnoreCase("terminal_brand")
														&& StringUtil.isNotEmpty(value)) {
													terminal_brand = value;
												} else if (key.equalsIgnoreCase("terminal_model")
														&& StringUtil.isNotEmpty(value)) {
													terminal_model = value;
												}

											}
											// 终端
											if (StringUtil.isNotEmpty(terminal_manu)
													&& StringUtil.isNotEmpty(terminal_brand)
													&& StringUtil.isNotEmpty(terminal_model)) {
												PccCondition condTerminal = new PccCondition();
												condTerminal.setNumber(String.valueOf(seq_index + 1));
												condTerminal.setConditionName("终端类型");
												condTerminal.setOper("等于");
												condTerminal.setVal(terminal_manu + "  " + terminal_brand + "  "
														+ terminal_model);
												condList.add(condTerminal);
											}
										}
									}
									ruleBean.setCondition(condList);
								}

								// 查询动作pop_policy_rule_act
								HashMap<String, Object> ruleActMap = new HashMap<String, Object>();
								ruleActMap.put("policy_rule_id", policyRule.getStr(PopPolicyRule.COL_ID));
								List<PopPolicyRuleAct> ruleActList = PopPolicyRuleAct.dao().search(ruleActMap);
								if (ruleActList != null && ruleActList.size() > 0) {
									int seq = 1;
									List<PccAction> actList = new ArrayList<PccAction>();
									for (int ruleActIndex = 0; ruleActIndex < ruleActList.size(); ruleActIndex++) {
										PopPolicyRuleAct ruleAct = ruleActList.get(ruleActIndex);
										// 策略动作类型
										Integer act_type_id = ruleAct.get(PopPolicyRuleAct.COL_POLICY_ACT_TYPE_ID);
										// PopDimActionType
										// actType=PopDimActionType.dao().findById(act_type_id);
										if (act_type_id.equals(1)) {// 管控
											// CONTROL_TYPE_ID 管控动作类型
											int control_type_id = ruleAct.getInt(PopPolicyRuleAct.COL_CONTROL_TYPE_ID);
											// CONTROL_PARAM 管控动作参数
											int control_param = ruleAct.getInt(PopPolicyRuleAct.COL_CONTROL_PARAM);
											PopDimControlType controlType = PopDimControlType.dao().findById(
													control_type_id);
											PccAction action = new PccAction();
											action.setNumber(String.valueOf(seq));
											action.setActName(controlType.getStr(PopDimControlType.COL_CODE));
											action.setInformation("");
											action.setInstruction("");
											action.setOper("=");
											action.setVal(String.valueOf(control_param));
											actList.add(action);
											seq++;
										} else if (act_type_id.equals(2)) {// 营销
											// AVOID_CUSTGROUP_IDS 免打扰客户类型ID
											String avd_custgroup_ids = ruleAct
													.getStr(PopPolicyRuleAct.COL_AVOID_CUSTGROUP_IDS);
											PopDimAviodType dimAviodType = PopDimAviodType.dao().findById(
													avd_custgroup_ids);

											// EXEC_CAMP_CONTENT 执行通知内容
											String exec_cam_content = ruleAct
													.getStr(PopPolicyRuleAct.COL_EXEC_CAMP_CONTENT);
											// EXEC_CHANNEL_ID 执行通知方式ID
											int exec_channel_id = ruleAct.getInt(PopPolicyRuleAct.COL_EXEC_CHANNEL_ID);
											PopDimCampChannel campChannel = PopDimCampChannel.dao().findById(
													exec_channel_id);

											// EXEC_CAMP_FREQUENCY 执行通知频次
											int exec_camp_frequency = ruleAct
													.getInt(PopPolicyRuleAct.COL_EXEC_CAMP_FREQUENCY);
											PopDimContactFreq contactFreq = PopDimContactFreq.dao().findById(
													exec_camp_frequency);
											// 生效
											PccAction action = new PccAction();
											action.setNumber(String.valueOf(seq));
											action.setActName(campChannel.getStr(PopDimCampChannel.COL_NAME));
											action.setInformation("生效通知：执行周期:" + execDate + ",接触时段:" + connectTime
													+ ",屏蔽时段:" + noConnectTime + ",接触频次:"
													+ contactFreq.getStr(PopDimContactFreq.COL_NAME) + ",免打扰客户类型:"
													+ dimAviodType.getStr(PopDimAviodType.COL_NAME));// 辅助信息
											action.setInstruction("短信内容：" + exec_cam_content);// 说明
											// action.setOper("");
											action.setVal("默认值");
											actList.add(action);
											seq++;

											// INVALID_CAMP_CONTENT 失效通知内容
											String invalid_camp_content = ruleAct
													.getStr(PopPolicyRuleAct.COL_INVALID_CAMP_CONTENT);
											// INVALID_CHANNEL_ID 失效通知方式ID
											int invalid_channel_id = ruleAct
													.getInt(PopPolicyRuleAct.COL_INVALID_CHANNEL_ID);
											campChannel = PopDimCampChannel.dao().findById(invalid_channel_id);
											// INVALID_CAMP_FREQUENCY 失效通知频次
											int invalid_camp_frequency = ruleAct
													.getInt(PopPolicyRuleAct.COL_INVALID_CAMP_FREQUENCY);
											contactFreq = PopDimContactFreq.dao().findById(invalid_camp_frequency);
											// 失效
											PccAction iaction = new PccAction();
											iaction.setNumber(String.valueOf(seq));
											iaction.setActName(campChannel.getStr(PopDimCampChannel.COL_NAME));
											iaction.setInformation("失效通知：执行周期:" + execDate + ",接触时段:" + connectTime
													+ ",屏蔽时段:" + noConnectTime + ",接触频次:"
													+ contactFreq.getStr(PopDimContactFreq.COL_NAME) + ",免打扰客户类型:"
													+ dimAviodType.getStr(PopDimAviodType.COL_NAME));// 辅助信息
											iaction.setInstruction("短信内容：" + invalid_camp_content);// 说明
											// action.setOper("");
											iaction.setVal("默认值");
											actList.add(iaction);
											seq++;

										} else {
											log.error("没有定义的动作类型！");
										}
										ruleBean.setAction(actList);
										// 要通过字段拼接
										// action.setActName(ruleAct.getStr(PopPolicyRuleAct.COL_EXEC_CAMP_CONTENT));
									}
								}
								ruleBeanList.add(ruleBean);
							}
							policyBean.setRule(ruleBeanList);
						}
						// ********************封装数据end*********************************
						//调用生成模板程序
						MailExcel.createExcel(policyBean, FilePath);
						// 组织邮件主题和内容信息
						String subject = "有关新增PCC策略【" + policy_name + "】审批通报";
						String content = "您好，由【" + user.getUsername() + "】策划的策略【" + policy_name + "】需要您的审批，请您尽快查阅邮件回复。";

						// 组织发件人信息
						String fromAddress = "";
						String filePath = Configure.getInstance().getProperty("SYS_COMMON_UPLOAD_PATH");
						// 组织附件信息
						HashMap<String, Object> maps = new HashMap<String, Object>();
						maps.put("policy_id", policy_id);
						List<PopPolicyBaseinfoFiles> fileList = PopPolicyBaseinfoFiles.dao().search(maps);
						List<String> attachFileNames = new ArrayList<String>();
						attachFileNames.add(FilePath);//添加上报模板文件
						if (fileList != null && fileList.size() > 0) {//添加脚本附件
							for (int i = 0; i < fileList.size(); i++) {
								PopPolicyBaseinfoFiles file = fileList.get(i);
								attachFileNames.add(filePath + file.getStr(PopPolicyBaseinfoFiles.COL_FILEPATH));
							}
						}

						// 发送邮件整理发送邮件信息
						String mailServerHost = PopConfigure.getInstance().getProperty("mailServerHost");
						String mailServerPort = PopConfigure.getInstance().getProperty("mailServerPort");
						String userName = PopConfigure.getInstance().getProperty("userName");
						String password = PopConfigure.getInstance().getProperty("password");
						String validate = PopConfigure.getInstance().getProperty("validate");
						//String 
						fromAddress = PopConfigure.getInstance().getProperty("fromAddress");
						String toAddress = PopConfigure.getInstance().getProperty("toAddress");
						String toCarbonCopyAddress = PopConfigure.getInstance().getProperty("toCarbonCopyAddress");
						String toBlindCarbonCopyAddress = PopConfigure.getInstance().getProperty(
								"toBlindCarbonCopyAddress");
						log.info(
								"上报集团邮件信息：mailServerHost={},mailServerPort={},userName={},password={},validate={},fromAddress={},toAddress={},toCarbonCopyAddress={},toBlindCarbonCopyAddress={},subject={},content={}",
								mailServerHost, mailServerPort, userName, password, validate, fromAddress, toAddress,
								toCarbonCopyAddress, toBlindCarbonCopyAddress, subject, content);
						if (StringUtil.isNotEmpty(mailServerHost) && StringUtil.isNotEmpty(mailServerPort)
								&& StringUtil.isNotEmpty(userName) && StringUtil.isNotEmpty(password)
								&& StringUtil.isNotEmpty(validate) && StringUtil.isNotEmpty(fromAddress)
								&& StringUtil.isNotEmpty(toAddress) && StringUtil.isNotEmpty(content)
								&& StringUtil.isNotEmpty(subject)) {

							MailSendInfo senderInfo = new MailSendInfo();
							senderInfo.setMailServerHost(mailServerHost);
							senderInfo.setMailServerPort(mailServerPort);
							senderInfo.setUserName(userName);
							senderInfo.setPassword(password);
							senderInfo.setValidate(Boolean.valueOf(validate));
							senderInfo.setFromAddress(fromAddress);

							String[] arr_toAddress = toAddress.split(",");
							senderInfo.setToAddress(arr_toAddress);

							if (StringUtil.isNotEmpty(toCarbonCopyAddress)) {
								String[] arr_toCarbonCopyAddress = toCarbonCopyAddress.split(",");
								if (arr_toCarbonCopyAddress.length > 0) {
									senderInfo.setToCarbonCopyAddress(arr_toCarbonCopyAddress);
								}
							}

							if (StringUtil.isNotEmpty(toBlindCarbonCopyAddress)) {
								String[] arr_toBlindCarbonCopyAddress = toBlindCarbonCopyAddress.split(",");
								if (arr_toBlindCarbonCopyAddress.length > 0) {
									senderInfo.setToBlindCarbonCopyAddress(arr_toBlindCarbonCopyAddress);
								}
							}
							senderInfo.setSubject(subject);
							senderInfo.setAttachFileNames(attachFileNames);
							senderInfo.setContent(content);
							IPopSendMailClient sendMailClient = (IPopSendMailClient) SystemServiceLocator.getInstance()
									.getService("mailSendClient");
							sendFlag = sendMailClient.sendMail(senderInfo);
							log.debug("上报集团邮件发送结果：sendFlag={}", sendFlag);
						}

					}
				}

			} catch (FileNotFoundException e1) {
				log.error("上报集团邮件异常:{}", e1);
			} catch (WriteException e2) {
				log.error("上报集团邮件异常:{}", e2);
			} catch (IOException e3) {
				log.error("上报集团邮件异常:{}", e3);
			} catch (Exception e4) {
				log.error("上报集团邮件异常:{}", e4);
			}
		}
		setAttr("isSuccess", sendFlag == true ? "1" : "0");
		renderJson();
	}

}
