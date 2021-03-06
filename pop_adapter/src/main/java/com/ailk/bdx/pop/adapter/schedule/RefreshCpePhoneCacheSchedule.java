package com.ailk.bdx.pop.adapter.schedule;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.directory.api.util.Strings;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ailk.bdx.pop.adapter.cache.CpePhoneCache;
import com.ailk.bdx.pop.adapter.util.Configure;
import com.ailk.bdx.pop.adapter.util.POPAdapterConstants;

/**
 * 定时更新CPE的手机号码（PhoneNo）缓存的类
 * @author hpa
 *
 */
public class RefreshCpePhoneCacheSchedule {
	private static final Logger log = LogManager.getLogger();
	private final ScheduledExecutorService scheduledExec;
	
	public RefreshCpePhoneCacheSchedule(){
		scheduledExec = Executors.newSingleThreadScheduledExecutor();
	}
	
	public void start(){
		//定时更新CPE的设备唯一号（SUBSID）缓存的周期时间单位
		String timeUnit = Configure.getInstance().getProperty("REFRESH_CPE_USER_INFO_SCHEDULE_TIMEUNIT");
		
		//定时更新CPE的设备唯一号（SUBSID）缓存的时间间隔
		String periodStr = Configure.getInstance().getProperty("REFRESH_CPE_USER_INFO_SCHEDULE_PERIOD");
		
		if(Strings.isEmpty(timeUnit)){
			timeUnit = POPAdapterConstants.REFRESH_CPE_USER_INFO_SCHEDULE_DEFAULT_TIMEUNIT;
		}
		
		int period = Strings.isNotEmpty(periodStr) ? Integer.parseInt(periodStr) 
				: POPAdapterConstants.REFRESH_CPE_USER_INFO_SCHEDULE_DEFAULT_PERIOD;
		
		
		scheduledExec.scheduleAtFixedRate(refreshCpePhoneCache(), 0, period, TimeUnit.valueOf(timeUnit));
	}
	
	public Runnable refreshCpePhoneCache(){
		return new Runnable(){

			@Override
			public void run() {
				log.info("开始定时初始化CPE的手机号码（PhoneNo）缓存>>>>>>");
				CpePhoneCache.getInstance().runTask();
				log.info("定时初始化CPE的手机号码（PhoneNo）缓存成功！");
			}
			
		};
	}
}
