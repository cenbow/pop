package com.ai.bdx.pop.service.impl.province.demo;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ai.bdx.pop.bean.SmsMsgBean;
import com.ai.bdx.pop.model.PopSmsSend;
import com.ai.bdx.pop.service.impl.AbstractSmsServcieImpl;

public class SmsServcieImpl extends AbstractSmsServcieImpl{
	private static Logger log = LogManager.getLogger();

	@Override
	public String SendMessage(SmsMsgBean sendMsg) throws Exception {
		// TODO Auto-generated method stub
		//在这里添加自己的短信发送业务逻辑
		return null;
	}


	
}
