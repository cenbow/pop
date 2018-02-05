package com.ai.bdx.pop.bean;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/***
 * @author liyz
 * 策略基本信息实体bean
 * */
public class PopPolicyBaseinfoBean {

	//策略id policyId
	private String policyId;
	
	//start_time
	private Timestamp startTime;
	
	//end_time
	private Timestamp endTime;

	//策略清单表
	private String policyTaskTab;
	
	private Map<String,PopPolicyRuleSendBean> popPolicyRuleMap = new HashMap<String,PopPolicyRuleSendBean>();

	public Map<String, PopPolicyRuleSendBean> getPopPolicyRuleMap() {
		return popPolicyRuleMap;
	}

	public void setPopPolicyRuleMap(
			Map<String, PopPolicyRuleSendBean> popPolicyRuleMap) {
		this.popPolicyRuleMap = popPolicyRuleMap;
	}

	public String getPolicyTaskTab() {
		return policyTaskTab;
	}

	public void setPolicyTaskTab(String policyTaskTab) {
		this.policyTaskTab = policyTaskTab;
	}

	public String getPolicyId() {
		return policyId;
	}

	public void setPolicyId(String policyId) {
		this.policyId = policyId;
	}

	public Timestamp getStartTime() {
		return startTime;
	}

	public void setStartTime(Timestamp startTime) {
		this.startTime = startTime;
	}

	public Timestamp getEndTime() {
		return endTime;
	}

	public void setEndTime(Timestamp endTime) {
		this.endTime = endTime;
	}
	
	
}
