package com.ai.bdx.pop.bean;

import java.io.Serializable;

/**
 * 
* @ClassName: PopPolicyCustGroupBean 
* @Description: TODO(客户群bean) 
* @author zhengyq3
* @date 2015年4月9日 上午12:50:00
 */
public class PopPolicyCustGroupBean implements Serializable {
	private static final long serialVersionUID = 5145565243770763792L;
	
	private String id;
	private String policy_rule_id;
	private String target_customers_id;
	private String target_customers_name;
	private String target_customers_cycle ;
	private String cust_group_type;
	private int target_customers_num;
	private String target_customers_tab_name;
	
	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}
	/**
	 * @return the policy_rule_id
	 */
	public String getPolicy_rule_id() {
		return policy_rule_id;
	}
	/**
	 * @param policy_rule_id the policy_rule_id to set
	 */
	public void setPolicy_rule_id(String policy_rule_id) {
		this.policy_rule_id = policy_rule_id;
	}
	/**
	 * @return the target_customers_id
	 */
	public String getTarget_customers_id() {
		return target_customers_id;
	}
	/**
	 * @param target_customers_id the target_customers_id to set
	 */
	public void setTarget_customers_id(String target_customers_id) {
		this.target_customers_id = target_customers_id;
	}
	/**
	 * @return the target_customers_name
	 */
	public String getTarget_customers_name() {
		return target_customers_name;
	}
	/**
	 * @param target_customers_name the target_customers_name to set
	 */
	public void setTarget_customers_name(String target_customers_name) {
		this.target_customers_name = target_customers_name;
	}
	
	/**
	 * @return the target_customers_num
	 */
	public int getTarget_customers_num() {
		return target_customers_num;
	}
	/**
	 * @param target_customers_num the target_customers_num to set
	 */
	public void setTarget_customers_num(int target_customers_num) {
		this.target_customers_num = target_customers_num;
	}
	/**
	 * @return the cust_group_type
	 */
	public String getCust_group_type() {
		return cust_group_type;
	}
	/**
	 * @param cust_group_type the cust_group_type to set
	 */
	public void setCust_group_type(String cust_group_type) {
		this.cust_group_type = cust_group_type;
	}
	/**
	 * @return the target_customers_tab_name
	 */
	public String getTarget_customers_tab_name() {
		return target_customers_tab_name;
	}
	/**
	 * @param target_customers_tab_name the target_customers_tab_name to set
	 */
	public void setTarget_customers_tab_name(String target_customers_tab_name) {
		this.target_customers_tab_name = target_customers_tab_name;
	}
	public String getTarget_customers_cycle() {
		return target_customers_cycle;
	}
	public void setTarget_customers_cycle(String target_customers_cycle) {
		this.target_customers_cycle = target_customers_cycle;
	}
	
	
	
	
	
}
