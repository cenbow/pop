package com.ai.bdx.pop.wsclient;

import java.util.Map;

/**
 * creator：sjw 
 * create date：2015年4月23日
 * useful： 调用webservice统一接口 
 * modify
 * ===================================================
 *  person             date               reason
 * ===================================================
 */
public interface ISendMsgClient {
    /**
     * 
     * sendPoliceyPauseMsg:通知对方策略暂停
     * @param msg 发送的消息
     * @param code  调用webservice标示，即pop_interface_config 表id
     * @return Map<String,Object>
     */
	public Map<String,Object> sendPoliceyPauseMsg(Map<String,Object> msg,String code);
	/**
	 * 
	 * sendPoliceyStop:通知对方策略终止
	 * @param msg
	 * @param code  调用webservice标示，即pop_interface_config 表id
	 * @return Map<String,Object>
	 */
	public Map<String,Object> sendPoliceyStop(Map<String,Object> msg,String code);
	/**
	 * 
	 * sendPoliceyInfo:向对方发送策略基本信息，当策略新增更新修改时调用
	 * @param msg
	 * @param code  调用webservice标示，即pop_interface_config 表id
	 * @return Map<String,Object>
	 */
	public  Map<String,Object> sendPoliceyInfo(Map<String,Object> msg,String code);
	

	
}
