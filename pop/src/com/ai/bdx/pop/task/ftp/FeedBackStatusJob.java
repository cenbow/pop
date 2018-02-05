package com.ai.bdx.pop.task.ftp;

import java.io.Serializable;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.ai.bdx.pop.service.IPopStatusService;
import com.asiainfo.biframe.utils.spring.SystemServiceLocator;

public class FeedBackStatusJob implements Serializable{

	private static final Logger log = LogManager.getLogger();
	 /** 
	  * serialVersionUID:TODO 
	  */
	private static final long serialVersionUID = 1L;
	
	public void runTask() {
		Integer popCount=5000;
		List<String> keyList=FeedBackStatusCacheQueue.getAllKey();
		if(keyList!=null&&keyList.size()>0){
			 for(String key:keyList){
				 List<String[]> list=FeedBackStatusCacheQueue.pull(key, popCount);
				//String key=ruleid+"#_#"+taskid;
				 String[] ids=key.split(FeedBackStatusCacheQueue.split);
				 String ruleid=ids[0];
				 String taskid=ids[1];
				 if(list!=null&&list.size()>0){
						try {
							IPopStatusService service=(IPopStatusService) SystemServiceLocator.getInstance().getService("IPopStatusService");
							service.updateSendOddStatus(ruleid, taskid, list);
						} catch (Exception e) {
							log.error("更新状态失败,异常");
						}
					}
			 }
			
		}
	}

}
