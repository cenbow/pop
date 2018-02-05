package com.ai.bdx.pop.util.excel;

import java.util.ArrayList;
import java.util.List;
/**
 * 
 * creator：sjw 
 * create date：2015年6月7日
 * useful：策略封装
 */
public class PccPolicyBean {

	private String policy_id;//策略ID
	private String policy_name;//策略名称
	private List<PccRuleBean> rule=new ArrayList<PccRuleBean>() ;//规则列表
	private List<PccCustomer> customers=new ArrayList<PccCustomer>();//客户群信息
	
	public List<PccCustomer> getCustomers() {
		return customers;
	}
	public void setCustomers(List<PccCustomer> customers) {
		this.customers = customers;
	}
	public String getPolicy_id() {
		return policy_id;
	}
	public void setPolicy_id(String policy_id) {
		this.policy_id = policy_id;
	}
	public String getPolicy_name() {
		return policy_name;
	}
	public void setPolicy_name(String policy_name) {
		this.policy_name = policy_name;
	}
	public List<PccRuleBean> getRule() {
		return rule;
	}
	public void setRule(List<PccRuleBean> rule) {
		this.rule = rule;
	}
	
	
	
}
