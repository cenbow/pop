package com.ai.bdx.pop.task;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ai.bdx.pop.bean.PopTaskBean;
import com.ai.bdx.pop.service.IPopSendOddService;
import com.ai.bdx.pop.util.PopTaskCache;
import com.asiainfo.biframe.utils.spring.SystemServiceLocator;
/**
 * 加载需要执行的当天任务到缓存 
 * @author liyz
 * */
public class PopLoadTodayTaskServiceJob {
	private static Logger log = LogManager.getLogger();
	
	public void runTask() {
		try{
			log.debug("加载当天任务到缓存..开始!");
			IPopSendOddService privilegeService = (IPopSendOddService) SystemServiceLocator.getInstance().getService("IPopSendOddService");
			List<PopTaskBean> tasks = privilegeService.getTodayPopTaskBeanList();
			for (PopTaskBean task : tasks) {
				PopTaskCache.getInstance().putTask(task);
			}
			log.debug("加载当天任务到缓存..成功!");
		}catch(Exception e){
			log.error("加载当天任务到缓存失败!"+e);
		}
		
	}
	
}
