package com.ai.bdx.pop.util.excel;

import java.util.TreeMap;
/**
 * 
 * @author Administrator
 *客户群信息
 */
public class PccCustomer {

	private String customerGroupID;//客户全ID
	
	private String customerGroupName;//客户群名称
	private TreeMap<String,String> param=new TreeMap<String,String>();//客户群信息TreeMap<筛选标准,阈值>
	public String getCustomerGroupName() {
		return customerGroupName;
	}
	public void setCustomerGroupName(String customerGroupName) {
		this.customerGroupName = customerGroupName;
	}
	public TreeMap<String, String> getParam() {
		return param;
	}
	public void setParam(TreeMap<String, String> param) {
		this.param = param;
	};
	public String getCustomerGroupID() {
		return customerGroupID;
	}
	public void setCustomerGroupID(String customerGroupID) {
		this.customerGroupID = customerGroupID;
	}
}
