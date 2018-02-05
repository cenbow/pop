package com.ai.bdx.pop.util;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.concurrent.PriorityBlockingQueue;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ai.bdx.pop.bean.PopTaskBean;
import com.ai.bdx.pop.service.IPopSendOddService;
import com.asiainfo.biframe.utils.spring.SystemServiceLocator;
 

public class PopTaskCache {
	private static Logger log = LogManager.getLogger();
	private PriorityBlockingQueue<PopTaskBean> taskQueue = null; //全局任务队列
	private static final int QUEUE_SIZE = 1000;
	
	/** 保证单例 */
	static class PopTaskCacheHolder {
		static PopTaskCache instance = new PopTaskCache();
	}
	

	public static PopTaskCache getInstance() {
		return PopTaskCacheHolder.instance;
	}
	
	
	private PopTaskCache(){
		taskQueue = new PriorityBlockingQueue<PopTaskBean>(QUEUE_SIZE,new PopTaskComparator());
	}

	
	/**
	 * 获取并删除缓存队列中的任务,设置任务的开始时间
	 * @return
	 */
	public PopTaskBean getTask() {
		PopTaskBean task = taskQueue.poll();
		if (null != task) {
			task.setStartTime(new Timestamp(System.currentTimeMillis()));
			return task;
		}
		return null;
	}
	
	/**
	 * 删除缓存，并将数据库中的任务移除至历史表（包括终止的和执行成功的任务）
	 * @param task
	 * @return
	 */
	public void removeTask(PopTaskBean task) {
		try {
			taskQueue.remove(task);
		} catch (Exception e) {
			log.error("Remove task error:", e);
		}
	}
	
	/**
	 * 将任务放入缓存队列
	 * @param task
	 */
	public void putTask(PopTaskBean task) {
		try {
			taskQueue.put(task);
		} catch (Exception e) {
			log.error("Put task error:", e);
		} 
	}
	
	
	/**
	 * 初始化数据库中的派单任务
	 * @return
	 */
	public boolean init() {
		boolean flag = true;
		log.debug("开始初始化派单任务.....");
		try {
			taskQueue.clear();
			IPopSendOddService privilegeService = (IPopSendOddService) SystemServiceLocator.getInstance().getService("IPopSendOddService");
			List<PopTaskBean> tasks = privilegeService.getTodayPopTaskBeanList();
			for (PopTaskBean task : tasks) {
				taskQueue.put(task);
			}
			log.debug("初始化派单任务共计：" + tasks.size());
		} catch (Exception e) {
			flag = false;
			log.debug("初始化派单任务异常.....", e);
		} 
		log.debug("结束初始化派单任务.....");
		return flag;
	}
	
}
