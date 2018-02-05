package com.ai.bdx.pop.model;

import com.ai.bdx.pop.base.PopModel;

public class CpeUserInfo extends PopModel<CpeUserInfo> {

	private static final long serialVersionUID = 1L;
	
	public static CpeUserInfo dao(){
		return new CpeUserInfo();
	}
	
	//设备号
	public static final String SUBSID = "subsid";
	
	//手机号
	public static final String PRODUCT_NO = "product_no";
	
	//带宽类型
	public static final String NET_TYPE = "net_type";
	
	//归属村
	public static final String COUNTRY_NAME = "country_name";
	
	//归属县
	public static final String COUNTY_NAME = "county_name";
	
	//归属市
	public static final String CITY_NAME_STRING = "city_name";
	
	//创建时间
	public static final String CREATE_TIME = "create_time";
	
	//订购产品代码
	public static final String PLAN_CODE = "plan_code";
	
	//小区信息
	public static final String LAC_INFO_STRING = "lac_info";
	
	//用户状态
	public static final String USER_STATUS = "user_status"; 
	
	//锁网状态
	public static final String NET_LOCK_FLAG = "net_lock_flag";
	
	//业务状态
	public static final String BUSI_TYPE = "busi_type";
	
	//状态变更时间
	public static final String STATUS_CHANGE_TIME = "status_change_time";
	
	//操作状态
	public static final String OP_STATUS = "op_status";
}
