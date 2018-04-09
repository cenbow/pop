package com.ailk.bdx.pop.adapter.schedule;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.directory.api.util.Strings;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ailk.bdx.pop.adapter.cache.CountryUserLocationCache;
import com.ailk.bdx.pop.adapter.util.Configure;
import com.ailk.bdx.pop.adapter.util.POPAdapterConstants;

/**
 * 定时更新农村的userLocation缓存的类
 * @author hpa
 *
 */
public class RefreshCountryUserLocationCacheSchedule {
	private static final Logger log = LogManager.getLogger();
	private final ScheduledExecutorService scheduledExec;
	
	public RefreshCountryUserLocationCacheSchedule(){
		scheduledExec = Executors.newSingleThreadScheduledExecutor();
	}
	
	public void start(){
		//定时更新农村的userLocation缓存的周期时间单位
		String timeUnit = Configure.getInstance().getProperty("REFRESH_COUNTRY_USERLOCATION_SCHEDULE_TIMEUNIT");
		
		//定时更新农村的userLocation缓存的时间间隔
		String periodStr = Configure.getInstance().getProperty("REFRESH_COUNTRY_USERLOCATION_SCHEDULE_PERIOD");
		
		if(Strings.isEmpty(timeUnit)){
			timeUnit = POPAdapterConstants.REFRESH_COUNTRY_USERLOCATION_SCHEDULE_TIMEUNIT;
		}
		
		int period = Strings.isNotEmpty(periodStr) ? Integer.parseInt(periodStr) 
				: POPAdapterConstants.REFRESH_COUNTRY_USERLOCATION_SCHEDULE_PERIOD;
		
		scheduledExec.scheduleAtFixedRate(refreshCountryUserLocationCache(), 0, period, TimeUnit.valueOf(timeUnit));
	}
	
	public Runnable refreshCountryUserLocationCache(){
		return new Runnable(){

			@Override
			public void run() {
				log.info("开始定时初始化农村的userLocation缓存>>>>>>");
				CountryUserLocationCache.getInstance().runTask();
				log.info("定时初始化农村的userLocation缓存成功！");
			}
			
		};
	}
}
