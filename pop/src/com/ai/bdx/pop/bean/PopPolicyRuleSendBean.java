package com.ai.bdx.pop.bean;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/***
 *@author liyz 
 *策略规则信息 实体bean
 * */
public class PopPolicyRuleSendBean  extends PopPolicyBaseinfoBean{

	//规则id
	private String ruleId;
	
	//规则静态客户清单汇总表
	private String rulePreSendDataTab;
	
	//规则实际派发客户清单汇总
	private String ruleSendDataTab;

	//当前规则状态
	private short ruleStatus;
	
	//当前任务id
	private String currentTaskId;
	
	//客户群id
	private String custGroupId;
	
	//客户群类型
	private String custGroupType;
	
	//免打扰ids
	private String avoidCustGroupIds;
	
	//复杂事件id
	private String cepRuleId;
	//简单规则
	private String simpleCondtionsData;
	
	//规则执行时间
	private String dateRanges;
    //pccid
	private String pccid;
	public String getPccid() {
		return pccid;
	}

	public void setPccid(String pccid) {
		this.pccid = pccid;
	}

	private Map<String,PopTaskBean> popTaskMap = new HashMap<String,PopTaskBean>();
	

	public String getSimpleCondtionsData() {
		return simpleCondtionsData;
	}

	public void setSimpleCondtionsData(String simpleCondtionsData) {
		this.simpleCondtionsData = simpleCondtionsData;
	}

	public String getDateRanges() {
		return dateRanges;
	}

	public void setDateRanges(String dateRanges) {
		this.dateRanges = dateRanges;
	}

	public Map<String, PopTaskBean> getPopTaskMap() {
		return popTaskMap;
	}

	public void setPopTaskMap(Map<String, PopTaskBean> popTaskMap) {
		this.popTaskMap = popTaskMap;
	}

	public String getCustGroupId() {
		return custGroupId;
	}

	public void setCustGroupId(String custGroupId) {
		this.custGroupId = custGroupId;
	}

	public String getCustGroupType() {
		return custGroupType;
	}

	public void setCustGroupType(String custGroupType) {
		this.custGroupType = custGroupType;
	}

	public String getAvoidCustGroupIds() {
		return avoidCustGroupIds;
	}

	public void setAvoidCustGroupIds(String avoidCustGroupIds) {
		this.avoidCustGroupIds = avoidCustGroupIds;
	}

	public String getCepRuleId() {
		return cepRuleId;
	}

	public void setCepRuleId(String cepRuleId) {
		this.cepRuleId = cepRuleId;
	}

	public String getRuleId() {
		return ruleId;
	}

	public void setRuleId(String ruleId) {
		this.ruleId = ruleId;
	}

	public String getRulePreSendDataTab() {
		return rulePreSendDataTab;
	}

	public void setRulePreSendDataTab(String rulePreSendDataTab) {
		this.rulePreSendDataTab = rulePreSendDataTab;
	}

	public String getRuleSendDataTab() {
		return ruleSendDataTab;
	}

	public void setRuleSendDataTab(String ruleSendDataTab) {
		this.ruleSendDataTab = ruleSendDataTab;
	}

	public short getRuleStatus() {
		return ruleStatus;
	}

	public void setRuleStatus(short ruleStatus) {
		this.ruleStatus = ruleStatus;
	}

	public String getCurrentTaskId() {
		return currentTaskId;
	}

	public void setCurrentTaskId(String currentTaskId) {
		this.currentTaskId = currentTaskId;
	}
	
}
