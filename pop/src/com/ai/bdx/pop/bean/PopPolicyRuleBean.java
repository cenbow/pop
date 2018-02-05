package com.ai.bdx.pop.bean;

import java.util.List;

import com.ai.bdx.pop.model.PopDimActionType;
import com.ai.bdx.pop.model.PopPolicyRuleAct;
import com.ai.bdx.pop.model.PopPolicyRuleCustgroup;
import com.ai.bdx.pop.model.PopPolicyRuleEventCon;
import com.jfinal.plugin.activerecord.Record;

/**
 * 配置策略规则业务bean
 * @author lixq8
 *
 */
public class PopPolicyRuleBean {

	private String id;
	private String policyRuleName;
	private String policyId;
	private String parentId;
	//实际派发客户清单汇总
	private String sendDataTab;
	//客户群
	private PopPolicyRuleCustgroup custgroup;
	//策略规则与事件规则关系
	private PopPolicyRuleEventCon eventCon;
	//策略规则与执行动作（管控/营销）关系
	private PopPolicyRuleAct act;
	//策略动作类型
	private PopDimActionType actionType;
	//动作类型名称
	private String actionTypeName;
	//状态
	private String statusName;

	private String custgroupNumber;

	/** 可暂停*/
	private boolean canPause;
	/** 可启动*/
	private boolean canStart;
	/** 可终止*/
	private boolean canFinish;
	/** 可查看任务列表*/
	private boolean canViewTask;
	/** 任务列表*/
	private List<Record> taskList;
	//根据集团规范生成策略ID用于编写脚本
	private String policyDescId;
	
	/**
	 * PCCID
	 */
	private String pccId;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the sendDataTab
	 */
	public String getSendDataTab() {
		return sendDataTab;
	}

	/**
	 * @param sendDataTab the sendDataTab to set
	 */
	public void setSendDataTab(String sendDataTab) {
		this.sendDataTab = sendDataTab;
	}

	public String getPolicyRuleName() {
		return policyRuleName;
	}

	public void setPolicyRuleName(String policyRuleName) {
		this.policyRuleName = policyRuleName;
	}

	public PopPolicyRuleCustgroup getCustgroup() {
		return custgroup;
	}

	public void setCustgroup(PopPolicyRuleCustgroup custgroup) {
		this.custgroup = custgroup;
	}

	public PopPolicyRuleEventCon getEventCon() {
		return eventCon;
	}

	public void setEventCon(PopPolicyRuleEventCon eventCon) {
		this.eventCon = eventCon;
	}

	public PopPolicyRuleAct getAct() {
		return act;
	}

	public void setAct(PopPolicyRuleAct act) {
		this.act = act;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getPolicyId() {
		return policyId;
	}

	public void setPolicyId(String policyId) {
		this.policyId = policyId;
	}

	public PopDimActionType getActionType() {
		return actionType;
	}

	public void setActionType(PopDimActionType actionType) {
		this.actionType = actionType;
	}

	public String getActionTypeName() {
		return actionTypeName;
	}

	public void setActionTypeName(String actionTypeName) {
		this.actionTypeName = actionTypeName;
	}

	public String getStatusName() {
		return statusName;
	}

	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}

	public String getCustgroupNumber() {
		return custgroupNumber;
	}

	public void setCustgroupNumber(String custgroupNumber) {
		this.custgroupNumber = custgroupNumber;
	}

	public boolean isCanPause() {
		return canPause;
	}

	public void setCanPause(boolean canPause) {
		this.canPause = canPause;
	}

	public boolean isCanStart() {
		return canStart;
	}

	public void setCanStart(boolean canStart) {
		this.canStart = canStart;
	}

	public boolean isCanFinish() {
		return canFinish;
	}

	public void setCanFinish(boolean canFinish) {
		this.canFinish = canFinish;
	}

	public boolean isCanViewTask() {
		return canViewTask;
	}

	public void setCanViewTask(boolean canViewTask) {
		this.canViewTask = canViewTask;
	}

	public List<Record> getTaskList() {
		return taskList;
	}

	public void setTaskList(List<Record> taskList) {
		this.taskList = taskList;
	}

	public String getPolicyDescId() {
		return policyDescId;
	}

	public void setPolicyDescId(String policyDescId) {
		this.policyDescId = policyDescId;
	}

	public String getPccId() {
		return pccId;
	}

	public void setPccId(String pccId) {
		this.pccId = pccId;
	}
	
}
