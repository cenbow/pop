package com.ai.bdx.pop.kafka;

import java.util.concurrent.ConcurrentLinkedQueue;

import net.sf.json.JSONObject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.asiainfo.biframe.utils.config.Configure;
 

public class CepMessageCacheQueue {
	private static Logger log = LogManager.getLogger();

	/** 保证单例 */
	static class CepMessageCacheQueueHolder {
		static final String CACHE_KEY_CEP_MESSAGE_QUEUE = "CACHE_KEY_CEP_MESSAGE_QUEUE";
		static final String CACHE_KEY_CEP_MESSAGE_QUEUE_FTP = "CACHE_KEY_CEP_MESSAGE_QUEUE_FTP";
		//插入O表队列
		static ConcurrentLinkedQueue<JSONObject> messageQueue = new ConcurrentLinkedQueue<JSONObject>();

		//生成派单文件队列
		static ConcurrentLinkedQueue<JSONObject> messageQueueSendFtp = new ConcurrentLinkedQueue<JSONObject>();
		 
	}

	public static ConcurrentLinkedQueue<JSONObject> getCepMessageQueue() {
		return CepMessageCacheQueueHolder.messageQueue;
	}

	public static ConcurrentLinkedQueue<JSONObject> getCepMessageQueueForSendFtp() {
		return CepMessageCacheQueueHolder.messageQueueSendFtp;
	}

}
