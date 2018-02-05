package com.ai.bdx.pop.model;

import com.ai.bdx.pop.base.PopModel;
/**
 	pop_sms_send
	phoneNo varchar(15) NOT NULL,--手机号
	sendMsg varchar(500) DEFAULT NULL,-- 短信内容
	sengFlag varchar(20) DEFAULT NULL, --发送标示
	channelNo varchar(50) DEFAULT NULL,-- 渠道号
	sendtype varchar(255) DEFAULT NULL-- 系统标示
*/
public class PopSmsSend extends PopModel<PopSmsSend>{

	
	 /** 
	  * serialVersionUID:TODO 
	  */
	private static final long serialVersionUID = 1L;
	
	public static PopSmsTemplet dao() {
		return new PopSmsTemplet();
	}

	public static final String COL_PHONENO = "phoneNo";
	public static final String COL_SENDMSG = "sendMsg";
	public static final String COL_SENDFLAG = "sengFlag";
	public static final String COL_CHANNELNO = "channelNo";
	public static final String COL_SENDTYPE = "sendtype";
	
	
	
}
