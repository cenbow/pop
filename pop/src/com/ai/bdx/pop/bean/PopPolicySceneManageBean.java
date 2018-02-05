package com.ai.bdx.pop.bean;

import java.util.Date;
import java.util.List;

public class PopPolicySceneManageBean {
	private String policyId;
	private String policyName;
	private String policyTypeId;
	private int policyLevelId;
	private int policyStatusId;
	private Date startTime;
	private Date endTime;
	private String policyTypeName;
	private String policyLevelName;
	private String policyStatusName;
	private int templateFlag;
	private String policyRuleId;//规则id，在基本信息bean里存放的是若干个规则id拼接的字符串，连接符为@
	private String ruleStatusId;//规则状态
	private String ruleStatusName;//规则状态
	private String currentTaskId;//规则的当前任务Id
	private String policyTaskTab;
	/**
	 * 控制动作类型ids
	 */
	private String policyControlTypeId;
	/**
	 * 控制动作类型名称
	 */
	private String policyControlTypeName;
	/**
	 * 控制动作类型参数
	 */
	private int controlParam;
	/**
	 * 控制动作类型名称和参数的拼接
	 */
	private String policyControlName;
	private String policyRuleName;//存放的是如果个规则名称拼接的字符串，连接符为，
	/**
	 * 规则的parentId
	 */
	private String policyRuleParentId;
	private String custgroupId;
	private String custgroupName;
	private int custgroupNumber;
	
	/**
	 * 地市优先级名称
	 * added by hpa
	 */
	private String cityPriorityName;
	/**
	 * 存放策略对应的规则，以及规则列表的表头
	 */
	private List<PopPolicyRuleBean> ruleList;

	public String getPolicyId() {
		return policyId;
	}

	public void setPolicyId(String policyId) {
		this.policyId = policyId;
	}

	public String getPolicyName() {
		return policyName;
	}

	public void setPolicyName(String policyName) {
		this.policyName = policyName;
	}

	public String getPolicyTypeId() {
		return policyTypeId;
	}

	public void setPolicyTypeId(String policyTypeId) {
		this.policyTypeId = policyTypeId;
	}

	public int getPolicyLevelId() {
		return policyLevelId;
	}

	public void setPolicyLevelId(int policyLevelId) {
		this.policyLevelId = policyLevelId;
	}

	public int getPolicyStatusId() {
		return policyStatusId;
	}

	public void setPolicyStatusId(int policyStatusId) {
		this.policyStatusId = policyStatusId;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public String getPolicyTypeName() {
		return policyTypeName;
	}

	public void setPolicyTypeName(String policyTypeName) {
		this.policyTypeName = policyTypeName;
	}

	public String getPolicyLevelName() {
		return policyLevelName;
	}

	public void setPolicyLevelName(String policyLevelName) {
		this.policyLevelName = policyLevelName;
	}

	public String getPolicyStatusName() {
		return policyStatusName;
	}

	public void setPolicyStatusName(String policyStatusName) {
		this.policyStatusName = policyStatusName;
	}

	public int getTemplateFlag() {
		return templateFlag;
	}

	public void setTemplateFlag(int templateFlag) {
		this.templateFlag = templateFlag;
	}

	public String getPolicyRuleId() {
		return policyRuleId;
	}

	public void setPolicyRuleId(String policyRuleId) {
		this.policyRuleId = policyRuleId;
	}

	public String getPolicyRuleName() {
		return policyRuleName;
	}

	public void setPolicyRuleName(String policyRuleName) {
		this.policyRuleName = policyRuleName;
	}

	public String getPolicyRuleParentId() {
		return policyRuleParentId;
	}

	public void setPolicyRuleParentId(String policyRuleParentId) {
		this.policyRuleParentId = policyRuleParentId;
	}

	public String getCustgroupId() {
		return custgroupId;
	}

	public void setCustgroupId(String custgroupId) {
		this.custgroupId = custgroupId;
	}

	public String getCustgroupName() {
		return custgroupName;
	}

	public void setCustgroupName(String custgroupName) {
		this.custgroupName = custgroupName;
	}

	public int getCustgroupNumber() {
		return custgroupNumber;
	}

	public void setCustgroupNumber(int custgroupNumber) {
		this.custgroupNumber = custgroupNumber;
	}

	public List<PopPolicyRuleBean> getRuleList() {
		return ruleList;
	}

	public void setRuleList(List<PopPolicyRuleBean> ruleList) {
		this.ruleList = ruleList;
	}

	public String getPolicyControlTypeId() {
		return policyControlTypeId;
	}

	public void setPolicyControlTypeId(String policyControlTypeId) {
		this.policyControlTypeId = policyControlTypeId;
	}

	public String getPolicyControlTypeName() {
		return policyControlTypeName;
	}

	public void setPolicyControlTypeName(String policyControlTypeName) {
		this.policyControlTypeName = policyControlTypeName;
	}

	public int getControlParam() {
		return controlParam;
	}

	public void setControlParam(int controlParam) {
		this.controlParam = controlParam;
	}

	public String getPolicyControlName() {
		return policyControlName;
	}

	public void setPolicyControlName(String policyControlName) {
		this.policyControlName = policyControlName;
	}

	public String getRuleStatusId() {
		return ruleStatusId;
	}

	public void setRuleStatusId(String ruleStatusId) {
		this.ruleStatusId = ruleStatusId;
	}

	public String getRuleStatusName() {
		return ruleStatusName;
	}

	public void setRuleStatusName(String ruleStatusName) {
		this.ruleStatusName = ruleStatusName;
	}

	public String getCurrentTaskId() {
		return currentTaskId;
	}

	public void setCurrentTaskId(String currentTaskId) {
		this.currentTaskId = currentTaskId;
	}

	public String getPolicyTaskTab() {
		return policyTaskTab;
	}

	public void setPolicyTaskTab(String policyTaskTab) {
		this.policyTaskTab = policyTaskTab;
	}

	public String getCityPriorityName() {
		return cityPriorityName;
	}

	public void setCityPriorityName(String cityPriorityName) {
		this.cityPriorityName = cityPriorityName;
	}
}
