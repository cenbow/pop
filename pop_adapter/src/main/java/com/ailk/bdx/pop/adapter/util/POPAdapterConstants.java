package com.ailk.bdx.pop.adapter.util;

/**
 * 常量类
 * @author hpa
 *
 */
public class POPAdapterConstants {
	/*
	 * Redis中的表
	 */
	/**
	 * 是已经注册了CPE
	 */
	public static final String ISCPE = "1";
	
	/**
	 * 不是已经注册的CPE
	 */
	public static final String NOTCPE = "0";
	
	/**
	 * 用户S11信令数据是否是CPE位置信息缓存key前缀
	 */
	public static final String REDIS_CPE_PREFIX = "cpe:";
	
	/**
	 * CPE和锁网小区关系缓存key前缀
	 */
	public static final String REDIS_UNL_PREFIX = "unl:";
	
	/**
	 * 农村基站redis中Key前缀
	 */
	public static final String REDIS_CBS_PREFIX = "cbs:";
	
	/**
	 * CPE上网位置统计缓存前缀
	 */
	public static final String REDIS_CPE_USERLOCATION_STATISTICS_PREFIX = "cpeUserLocationStatistics:";
	
	/**
	 * 定时更新CPE的设备唯一号（SUBSID）缓存的周期时间单位
	 */
	public static final String REFRESH_CPE_USER_INFO_SCHEDULE_DEFAULT_TIMEUNIT = "MINUTES";
	
	/**
	 * 定时更新CPE的设备唯一号（SUBSID）缓存的时间间隔
	 */
	public static final int REFRESH_CPE_USER_INFO_SCHEDULE_DEFAULT_PERIOD = 5;
	
	/**
	 * 定时更新农村的userLocation缓存的周期时间单位
	 */
	public static final String REFRESH_COUNTRY_USERLOCATION_SCHEDULE_TIMEUNIT = "HOURS";
	
	/**
	 * 定时更新农村的userLocation缓存的时间间隔
	 */
	public static final int REFRESH_COUNTRY_USERLOCATION_SCHEDULE_PERIOD = 2;
}
