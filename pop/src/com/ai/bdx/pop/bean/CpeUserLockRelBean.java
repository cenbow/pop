package com.ai.bdx.pop.bean;

import java.io.Serializable;

/**
 * 用户和锁网小区关系实体类
 * @author hpa
 *
 */
public class CpeUserLockRelBean implements Serializable{
	
	private static final long serialVersionUID = 1L;

	public CpeUserLockRelBean(){
	}
	
	public CpeUserLockRelBean(String subsid, String productNo, String lockedLacCi,
			String userLocation, String stationCode) {
		super();
		this.subsid = subsid;
		this.productNo = productNo;
		this.lockedLacCi = lockedLacCi;
		this.userLocation = userLocation;
		this.stationCode = stationCode;
	}

	//设备号
	private String subsid;
	
	//手机号
	private String productNo;
	
	//锁网小区
	private String lockedLacCi;
	
	//小区userLocation
	private String userLocation;
	
	//小区所属基站
	private String stationCode;

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
	 * 获取  锁网小区
	 * @return
	 */
	public String getLockedLacCi() {
		return lockedLacCi;
	}

	/**
	 * 设置  锁网小区
	 * @param lockedLacCi
	 */
	public void setLockedLacCi(String lockedLacCi) {
		this.lockedLacCi = lockedLacCi;
	}

	/**
	 * 获取  小区userLocation
	 * @return
	 */
	public String getUserLocation() {
		return userLocation;
	}

	/**
	 * 设置  小区userLocation
	 * @param userLocation
	 */
	public void setUserLocation(String userLocation) {
		this.userLocation = userLocation;
	}

	/**
	 * 获取  小区所属基站
	 * @return
	 */
	public String getStationCode() {
		return stationCode;
	}
	
	/**
	 * 设置  小区所属基站
	 * @param stationCode
	 */
	public void setStationCode(String stationCode) {
		this.stationCode = stationCode;
	}
	
}
