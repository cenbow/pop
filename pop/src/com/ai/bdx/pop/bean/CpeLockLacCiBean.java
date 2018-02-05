package com.ai.bdx.pop.bean;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 单用户参数重置
 * @author hpa
 *
 */
public class CpeLockLacCiBean implements Serializable{

	private static final long serialVersionUID = -7480853394932030866L;
	
	//设备号
	private String subsid;

	// 手机号
	private String productNo;

	// 归属市
	private String cityName;
	
	// 归属县
	private String countyName;
	
	//所属乡镇
	private String townName;

	// 归属村
	private String countryName;
	
	// 小区中文名称
	private String cellName;

	// 业务状态
	private int busiType = -1;
	
	// 用户状态
	private int userStatus;
	
	//锁网状态
	private int netLockFlag;

	// 带宽类型
	private String netType;

	// 创建时间
	private Timestamp createTime;
	
	public CpeLockLacCiBean(){
	}
	
	public CpeLockLacCiBean(String subsid, String productNo, String cityName,
			String countyName,String townName, String countryName, String cellName,
			int busiType, int netLockFlag, int userStatus, String netType,
			Timestamp createTime) {
		super();
		this.subsid = subsid;
		this.productNo = productNo;
		this.cityName = cityName;
		this.countyName = countyName;
		this.countryName = countryName;
		this.townName = townName;
		this.cellName = cellName;
		this.netLockFlag = netLockFlag;
		this.busiType = busiType;
		this.userStatus = userStatus;
		this.netType = netType;
		this.createTime = createTime;
	}

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

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public String getCountyName() {
		return countyName;
	}

	public void setCountyName(String countyName) {
		this.countyName = countyName;
	}

	public String getCountryName() {
		return countryName;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

	public String getTownName() {
		return townName;
	}

	public void setTownName(String townName) {
		this.townName = townName;
	}

	public String getCellName() {
		return cellName;
	}

	public void setCellName(String cellName) {
		this.cellName = cellName;
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

	public int getUserStatus() {
		return userStatus;
	}

	public void setUserStatus(int userStatus) {
		this.userStatus = userStatus;
	}

	public String getNetType() {
		return netType;
	}

	public void setNetType(String netType) {
		this.netType = netType;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}
	
}
