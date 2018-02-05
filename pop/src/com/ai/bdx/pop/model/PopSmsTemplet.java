package com.ai.bdx.pop.model;

import com.ai.bdx.pop.base.PopModel;

/**
  pop_sms_templet
 id varchar(50) NOT NULL, --短信标示
 smsContent varchar(300) DEFAULT NULL, --短信内容
 channelNo varchar(100) DEFAULT NULL, --短信渠道号
 remark varchar(500) DEFAULT NULL,-- 描述

 */
public class PopSmsTemplet extends PopModel<PopSmsTemplet> {

	/**
	 * serialVersionUID:TODO
	 */
	private static final long serialVersionUID = 1L;

	public static PopSmsTemplet dao() {
		return new PopSmsTemplet();
	}

	public static final String COL_ID = "id";
	public static final String COL_SMSCONTENT = "smsContent";
	public static final String COL_CHANNELNO = "channelNo";
	public static final String COL_REMARK = "remark";
	//public static final String COL_PROVINCE = "province";

}
