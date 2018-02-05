package com.ai.bdx.pop.util;

import java.io.Serializable;
import java.sql.Timestamp;



public class CpeUserInfo implements Serializable{
	private static final long serialVersionUID = -7480853394932030866L;

	public CpeUserInfo(){
	}
	
	public CpeUserInfo(String subsid, String productNo, String netType,
			String countryName, String countyName, String cityName,
			Timestamp createTime, String planCode, String lacInfo,
			Integer userStatus, Integer netLockFlag, Integer busitype,
			Timestamp statusChangeTime, Integer opStatus,String townName,
			String count, Integer busiType) {
		super();
		this.subsid = subsid;
		this.productNo = productNo;
		this.netType = netType;
		this.countryName = countryName;
		this.countyName = countyName;
		this.cityName = cityName;
		this.townName = townName;
		this.createTime = createTime;
		this.planCode = planCode;
		this.lacInfo = lacInfo;
		this.userStatus = userStatus;
		this.netLockFlag = netLockFlag;
		this.busiType = busiType;
		this.statusChangeTime = statusChangeTime;
		this.opStatus = opStatus;
		this.count = count;
		
	}

	//可接入小区
	private String count;
	
	//设备号
	private String subsid;
	
	//手机号
	private String productNo;
	
	//带宽类型
	private String netType;
	
	//归属村
	private String countryName;
	
	//归属县
	private String countyName;
	
	//归属镇
	private String townName;

	//归属市
	private String cityName;
	
	//创建时间
	private Timestamp createTime;
	
	//订购产品代码
	private String planCode;
	
	//小区信息
	private String lacInfo;
	
	//用户状态
	private Integer userStatus; 
	
	//锁网状态
	private Integer netLockFlag;
	
	//业务状态
	private Integer busiType;
	
	//状态变更时间
	private Timestamp statusChangeTime;
	
	//操作状态
	private Integer opStatus;

	/**
	 * 获取  设备号
	 * @return
	 */
	public String getSubsid() {
		return subsid;
	}

	/**
	 * 设置  设备号
	 * @param subsid
	 */
	public void setSubsid(String subsid) {
		this.subsid = subsid;
	}

	/**
	 * 获取  手机号
	 * @return
	 */
	public String getProductNo() {
		return productNo;
	}

	/**
	 * 设置  手机号
	 * @param productNo
	 */
	public void setProductNo(String productNo) {
		this.productNo = productNo;
	}

	/**
	 * 获取  带宽类型
	 * @return
	 */
	public String getNetType() {
		return netType;
	}

	/**
	 * 设置  带宽类型
	 * @param netType
	 */
	public void setNetType(String netType) {
		this.netType = netType;
	}

	/**
	 * 获取  归属村
	 * @return
	 */
	public String getCountryName() {
		return countryName;
	}

	/**
	 * 设置  归属村
	 * @param countryName
	 */
	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

	/**
	 * 获取  归属县
	 * @return
	 */
	public String getCountyName() {
		return countyName;
	}

	/**
	 * 设置  归属县
	 * @param countyName
	 */
	public void setCountyName(String countyName) {
		this.countyName = countyName;
	}

	/**
	 * 获取  归属市
	 * @return
	 */
	public String getCityName() {
		return cityName;
	}

	/**
	 * 设置  归属市
	 * @param cityName
	 */
	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	/**
	 * 获取  创建时间
	 * @return
	 */
	public Timestamp getCreateTime() {
		return createTime;
	}

	/**
	 * 设置  创建时间
	 * @param createTime
	 */
	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	/**
	 * 获取  订购产品代码
	 * @return
	 */
	public String getPlanCode() {
		return planCode;
	}

	/**
	 * 设置  订购产品代码
	 * @param planCode
	 */
	public void setPlanCode(String planCode) {
		this.planCode = planCode;
	}

	/**
	 * 获取  小区信息
	 * @return
	 */
	public String getLacInfo() {
		return lacInfo;
	}

	/**
	 * 设置  小区信息
	 * @param lacInfo
	 */
	public void setLacInfo(String lacInfo) {
		this.lacInfo = lacInfo;
	}

	/**
	 * 获取  用户状态
	 * @return
	 */
	public Integer getUserStatus() {
		return userStatus;
	}

	/**
	 * 设置  用户状态
	 * @param userStatus
	 */
	public void setUserStatus(Integer userStatus) {
		this.userStatus = userStatus;
	}

	/**
	 * 获取  锁网状态
	 * @return
	 */
	public Integer getNetLockFlag() {
		return netLockFlag;
	}

	/**
	 * 锁网状态
	 * @param net_lock_flag
	 */
	public void setNetLockFlag(Integer netLockFlag) {
		this.netLockFlag = netLockFlag;
	}

	/**
	 * 业务状态
	 * @return
	 */
	public Integer getBusiType() {
		return busiType;
	}

	/**
	 * 业务状态
	 * @param busi_type
	 */
	public void setBusiType(Integer busiType) {
		this.busiType = busiType;
	}

	/**
	 * 状态变更时间
	 * @return
	 */
	public Timestamp getStatusChangeTime() {
		return statusChangeTime;
	}

	/**
	 * 状态变更时间
	 * @param statusChangeTime
	 */
	public void setStatusChangeTime(Timestamp statusChangeTime) {
		this.statusChangeTime = statusChangeTime;
	}

	/**
	 * 操作状态
	 * @return
	 */
	public Integer getOpStatus() {
		return opStatus;
	}

	/**
	 * 操作状态
	 * @param opStatus
	 */
	public void setOpStatus(Integer opStatus) {
		this.opStatus = opStatus;
	}

	public String getTownName() {
		return townName;
	}

	public void setTownName(String townName) {
		this.townName = townName;
	}

	public String getCount() {
		return count;
	}

	public void setCount(String count) {
		this.count = count;
	}
	
}
