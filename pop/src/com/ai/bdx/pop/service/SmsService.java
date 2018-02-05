package com.ai.bdx.pop.service;

import com.ai.bdx.pop.bean.SmsMsgBean;
import com.ai.bdx.pop.model.PopSmsSend;
import com.ai.bdx.pop.model.PopSmsTemplet;

public interface SmsService {

	/**
	 * 
	 * SendMessage:发送短信提醒方法
	 * @param PhoneNo    手机号
	 * @param Msg        短信内容
	 * @param channel    渠道号
	 * @return  
	 * @throws Exception 
	 * @return String 返回标示
	 */
	public String SendMessage(SmsMsgBean sendMsg) throws Exception;
	/**
	 * 
	 * SendMessage:发送短信
	 * @param smsCode
	 * @param PolicyID
	 * @return
	 * @throws Exception 
	 * @return String
	 */
	public String SendMessage(String smsCode,String PolicyID) throws Exception;
	/**
	 * 
	 * getSmsTempletByCode:根据CODE查询指定的短信模板
	 * @param code  短信模板code
	 * @return
	 * @throws Exception 
	 * @return PopSmsTemplet
	 */
	public PopSmsTemplet getSmsTempletByCode(String code)throws Exception;
	
}
