package com.ai.bdx.pop.task;

import java.io.Serializable;
import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ai.bdx.pop.service.IPccFeedbackEDRLogService;
import com.ai.bdx.pop.util.SpringContext;

/**
 * @ClassName: GetPccFeedbackEDRLogServiceJob
 * @Description: POP同步PCC反馈日志job
 * @author jinlong
 * @date 2015-10-15 下午5:11:37
 * 
 */
public class GetPccFeedbackEDRLogServiceJob implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8283544270910376644L;

	private static Logger Log = LogManager.getLogger();

	public void runTask(){
		try {
			IPccFeedbackEDRLogService pccFeedbackEDRLogService = SpringContext.getBean("IPccFeedbackEDRLogService",IPccFeedbackEDRLogService.class);
			Log.debug("POP同步PCC反馈日志job-开始!{}",new Date());
			pccFeedbackEDRLogService.getPccFeedbackEDRLog();
			Log.debug("POP同步PCC反馈日志job-成功!");
		} catch (Exception e) {
			Log.error("POP同步PCC反馈日志job-异常", e);
		}
	}
}
