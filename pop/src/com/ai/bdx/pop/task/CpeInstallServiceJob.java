package com.ai.bdx.pop.task;

import java.io.Serializable;
import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ai.bdx.pop.service.ICpeManagerService;
import com.ai.bdx.pop.util.SpringContext;

public class CpeInstallServiceJob implements Serializable {

	private static final long serialVersionUID = 1L;
	private static Logger log = LogManager.getLogger();
	
	public void runTask(){
		try {
			ICpeManagerService cpeManagerService = SpringContext.getBean("cpeManagerService",ICpeManagerService.class);
			log.debug("POP定时同步BOSS侧CPE开户入网信息job-开始!{}",new Date());
			cpeManagerService.getBoss2PopCpeInstallInfo();
			log.debug("POP定时同步BOSS侧CPE开户入网信息job-成功!");
		} catch (Exception e) {
			log.error("POP定时同步BOSS侧CPE开户入网信息job-异常", e);
		}
	}
}
