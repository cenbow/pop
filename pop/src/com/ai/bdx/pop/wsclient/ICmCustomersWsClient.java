package com.ai.bdx.pop.wsclient;


/**
 * 调用COC提供的服务的接口类；
 * @author zhouxing
 *
 */
public interface ICmCustomersWsClient {

	
	/**
	 * 调用COC接口获取客户群列表接口
	 */
	public String  getCustomersList(String userId)throws Exception;

	/**	
	查询单个客户群接口
	*/
	public String getTargetCustomersObj(String id) throws Exception;
	
	/**	
	查询单个周期客户群接口
	*/
	public String  getTargetCustomersCycleObj(String id, String date) throws Exception;
	
}
