package com.asiainfo.biapp.pop.model;

import java.util.Date;
/**
 * CPE用户信息实体类
 * @author 林
 *
 */
public class CpeUserInfo {
	private String subsid;		//设备唯一标识
	private String productNo;		//CPE用户手机号
	private String netType;			//带宽类型
	private String countryName;		//村庄
	private String countyName;		//县
	private String cityName;				//市
	private Date createTime;//cpe用户创建时间
	private String createTimeStr;
	private String planCode;	//订购产品代码
	private String lacInfo;		//小区信息
	private int userStatus;		//用户状态US20：销户(3)；US24：欠费销户(2 )  
	private int netLockFlag;	//锁网状态0=否|1=是|2=中间状态
	private int busiType;		//业务状态
	private Date statusChangeTime;//业务变更时间
	private int opStatus;			//操作时间
	public String getSubsid() {
		return subsid;
	}
	public void setSubsid(String subsid) {
		this.subsid = subsid;
	}
	public String getProductNo() {
		return productNo;
	}
	public void setProductNo(String productNo) {
		this.productNo = productNo;
	}
	public String getNetType() {
		return netType;
	}
	public void setNetType(String netType) {
		this.netType = netType;
	}
	public String getCountryName() {
		return countryName;
	}
	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}
	public String getCountyName() {
		return countyName;
	}
	public void setCountyName(String countyName) {
		this.countyName = countyName;
	}
	public String getCityName() {
		return cityName;
	}
	public void setCityName(String cityName) {
		this.cityName = cityName;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getPlanCode() {
		return planCode;
	}
	public void setPlanCode(String planCode) {
		this.planCode = planCode;
	}
	public String getLacInfo() {
		return lacInfo;
	}
	public void setLacInfo(String lacInfo) {
		this.lacInfo = lacInfo;
	}
	public int getUserStatus() {
		return userStatus;
	}
	public void setUserStatus(int userStatus) {
		this.userStatus = userStatus;
	}
	public int getNetLockFlag() {
		return netLockFlag;
	}
	public void setNetLockFlag(int netLockFlag) {
		this.netLockFlag = netLockFlag;
	}
	public int getBusiType() {
		return busiType;
	}
	public void setBusiType(int busiType) {
		this.busiType = busiType;
	}
	public Date getStatusChangeTime() {
		return statusChangeTime;
	}
	public void setStatusChangeTime(Date statusChangeTime) {
		this.statusChangeTime = statusChangeTime;
	}
	public int getOpStatus() {
		return opStatus;
	}
	public void setOpStatus(int opStatus) {
		this.opStatus = opStatus;
	}
	public String getCreateTimeStr() {
		return createTimeStr;
	}
	public void setCreateTimeStr(String createTimeStr) {
		this.createTimeStr = createTimeStr;
	}
	@Override
	public String toString() {
		return "CpeInstallbean [subsid=" + subsid + ", productNo=" + productNo
				+ ", netType=" + netType + ", countryName=" + countryName
				+ ", createTimeStr=" + createTimeStr + ", planCode=" + planCode
				+ ", lacInfo=" + lacInfo + ", netLockFlag=" + netLockFlag + ",userStatus="+userStatus+"]";
	}
	
}
