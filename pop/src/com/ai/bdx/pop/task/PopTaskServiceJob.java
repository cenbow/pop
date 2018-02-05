package com.ai.bdx.pop.task;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ai.bdx.pop.bean.PopTaskBean;
import com.ai.bdx.pop.util.PopConstant;
import com.ai.bdx.pop.util.PopTaskCache;
import com.ai.bdx.pop.util.SimpleCache;
import com.ai.bdx.pop.util.ThreadPool;
import com.asiainfo.biframe.utils.config.Configure;
import com.asiainfo.biframe.utils.string.StringUtil;
/**
 * 任务调度定时任务
 * */
public class PopTaskServiceJob implements Serializable {
	
	 /** 
	  * serialVersionUID:TODO 
	  */
	private static final long serialVersionUID = 1L;
	private static Logger log = LogManager.getLogger();
	private static AtomicBoolean isRunning = new AtomicBoolean(false);
	private static final ReentrantLock lock = new ReentrantLock();

	public void runTask() {
		//log.info("任务调度定时任务");
		try {
			lock.lock();
			String cacheOk = (String) SimpleCache.getInstance().get(PopConstant.CACHE_OK);
			if ("1".equals(cacheOk)) {
				if (!isRunning.getAndSet(true)) {
					try {
						log.debug(">>PopTaskServiceJob begining...");
						Date currentDate = Calendar.getInstance().getTime();
						ArrayList<PopTaskBean> tmpList = new ArrayList<PopTaskBean>();
						PopTaskBean ptb;
						//测试
					//	PopLoadTodayTaskServiceJob p = new PopLoadTodayTaskServiceJob();
					//	p.runTask();
						while (true) {
							ptb = PopTaskCache.getInstance().getTask();
							if (null != ptb) {
								String exeDateStr = ptb.getExecDate();
								Date exeDate =null;
								if(exeDateStr.length() == 10){
									//没有时分秒
									SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
									exeDate =  sdf.parse(exeDateStr);
								}else{
									SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
									exeDate =  sdf.parse(exeDateStr);
								}
								
								//当前时间>执行时间并且等待派单
								if (currentDate.compareTo(exeDate) >= 0 && ptb.getExecStatus() == PopConstant.TASK_STATUS_DDPD) { 
									log.debug("将任务加入执行中，任务id{}，任务开始时间{} ", ptb.getTaskId(), ptb.getExecDate());
								    ThreadPool.getInstance().execute(new PopTaskScheduleThread(ptb));
								} else {
									log.debug("缓存中查询到的任务id{}-规则id{} ,还未到执行时间放回缓存队列! 任务开始时间{},状态{}",ptb.getTaskId(), ptb.getRuleId(), ptb.getExecDate(),ptb.getExecStatus());
									tmpList.add(ptb);
								}
							} else {
								for (PopTaskBean tmct : tmpList) {
									 PopTaskCache.getInstance().putTask(tmct);
								}
								break;
							}
						}
						log.debug(">>PopTaskServiceJob end...");
					} catch (Exception e) {
						log.error("runTask error:", e);
					} finally {
						isRunning.set(false);
					}
				} else {
					log.debug("The tasks job is running......");
				}
		}else{
			log.debug("系统缓存还未初始化完成......");
		}
		} catch (Exception e) {
			log.error("", e);
		} finally {
			lock.unlock();
		}
		
	 
}
}
