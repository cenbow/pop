package com.ai.bdx.pop.kafka.reveiver.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ai.bdx.pop.kafka.ReceiveCepMessageThread;
import com.ai.bdx.pop.kafka.reveiver.ICepMessageReceiveService;
import com.ai.bdx.pop.kafka.util.ThreadPoolJMS;
import com.ai.bdx.pop.util.PopConstant;
import com.ai.bdx.pop.util.SimpleCache;
 

public class CepMessageReceiverImpl extends ICepMessageReceiveService	{

	public  String activityCode="";
	private static final Logger log = LogManager.getLogger();
	
	@Override
	public void execute(String message) {
		String cacheOk = (String) SimpleCache.getInstance().get(PopConstant.CACHE_OK);
		if ("1".equals(cacheOk)) {
			//消息丢入线程池中
			ReceiveCepMessageThread thread = new ReceiveCepMessageThread(this.activityCode, message);
			ThreadPoolJMS.getInstance().execute(thread);
		} else {
			log.warn("缓存数据还未初始化完成，本次数据丢弃：" + message);
		}
	}

	public String getActivityCode() {
		return activityCode;
	}

	public void setActivityCode(String activityCode) {
		this.activityCode = activityCode;
	}
 

	 
	
	
}
