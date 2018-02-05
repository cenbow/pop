package com.ai.bdx.pop.bean;

/**
 * 用于封装向网管发送邮件中附件 excel 中的业务信息
 * @author luozn
 *
 */
public class PolicyBussTemplateBean {
	//策略ID
	private String policyId;
	//规则ID
	private String ruleId;
	//动作或者 业务类标记
	private String actRuleFlag;
	//业务条件描述描述信息
	private String simpleConditionDesc;
	//简单条件数据信息
	private String simpleCondtionData;
	//管控动作类型
	private String controltype;
	//管控动作信息
	private String controlInformation;
	//管控动作参数
	private String controlParam;
	
	//执行通知
	private String execNotice;
	//失效通知
	private String InvaliteNotice;

	
	
	public String getExecNotice() {
		return execNotice;
	}

	public void setExecNotice(String execNotice) {
		this.execNotice = execNotice;
	}

	public String getInvaliteNotice() {
		return InvaliteNotice;
	}

	public void setInvaliteNotice(String invaliteNotice) {
		InvaliteNotice = invaliteNotice;
	}

	public String getPolicyId() {
		return policyId;
	}

	public void setPolicyId(String policyId) {
		this.policyId = policyId;
	}

	public String getRuleId() {
		return ruleId;
	}

	public void setRuleId(String ruleId) {
		this.ruleId = ruleId;
	}

	public String getActRuleFlag() {
		return actRuleFlag;
	}

	public void setActRuleFlag(String actRuleFlag) {
		this.actRuleFlag = actRuleFlag;
	}

	public String getSimpleConditionDesc() {
		return simpleConditionDesc;
	}

	public void setSimpleConditionDesc(String simpleConditionDesc) {
		this.simpleConditionDesc = simpleConditionDesc;
	}

	public String getSimpleCondtionData() {
		return simpleCondtionData;
	}

	public void setSimpleCondtionData(String simpleCondtionData) {
		this.simpleCondtionData = simpleCondtionData;
	}

	public String getControltype() {
		return controltype;
	}

	public void setControltype(String controltype) {
		this.controltype = controltype;
	}

	public String getControlInformation() {
		return controlInformation;
	}

	public void setControlInformation(String controlInformation) {
		this.controlInformation = controlInformation;
	}

	public String getControlParam() {
		return controlParam;
	}

	public void setControlParam(String controlParam) {
		this.controlParam = controlParam;
	}
	
	
	
	
	

}
