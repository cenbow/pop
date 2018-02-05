/**   
 * @Title: PopPccCheckSignServiceJob.java
 * @Package com.ai.bdx.pop.task
 * @Description: TODO(用一句话描述该文件做什么)
 * @author A18ccms A18ccms_gmail_com   
 * @date 2015-8-20 上午11:48:27
 * @version V1.0   
 */
package com.ai.bdx.pop.task;

import java.io.Serializable;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ai.bdx.pop.util.SpringContext;
import com.ai.bdx.pop.wsclient.ISendPccInfoService;

/**
 * @ClassName: PopPccCheckSignServiceJob
 * @Description: 派单检查pcc标识定时任务
 * @author jinlong
 * @date 2015-8-20 上午11:48:27
 * 
 */
public class PopPccCheckSignServiceJob implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2411037246829411496L;

	private static Logger log = LogManager.getLogger();
	
	public void runTask() {
		try {
			ISendPccInfoService sendPccInfoService = SpringContext.getBean("sendPccInfoService",ISendPccInfoService.class);
			log.debug("派单检查pcc标识定时任务-开始!");
			sendPccInfoService.pccCheckSignJob();
			log.debug("派单检查pcc标识定时任务-成功!");
		} catch (Exception e) {
			log.error("派单检查pcc标识定时任务-异常", e);
		}

	}
}
