package com.ai.bdx.pop.kafka;

import net.sf.json.JSONObject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

 


import com.ai.bdx.pop.phonefilter.IUnifiedContactControlFilter;
import com.ai.bdx.pop.util.SpringContext;
import com.asiainfo.biframe.utils.string.StringUtil;

public class ReceiveCepMessageThread extends Thread {
	private static Logger log = LogManager.getLogger();
	
	public static IUnifiedContactControlFilter iucc = SpringContext.getBean("unifiedContactControlFilter",IUnifiedContactControlFilter.class);
	private String activityCode;//活动编码
	private String message; //需要处理的消息
	
	public ReceiveCepMessageThread(String activityCode, String message) {
		this.activityCode = activityCode;
		this.message = message;
	}
	
	@Override
	public void run() {
		try {
			if (StringUtil.isNotEmpty(message)) {
				long t1 = System.currentTimeMillis();
				iucc.realTimeContactControl(activityCode, JSONObject.fromObject(message));
			//	log.debug("ReceiveCepMessage cost time :" + (System.currentTimeMillis() - t1) + " ms.");
			}
		} catch (Throwable e) {
			log.error("ReceiveCepMessage error:[" + message + "]", e);
		}
	}
	
	 

}
