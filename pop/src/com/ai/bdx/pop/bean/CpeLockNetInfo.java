package com.ai.bdx.pop.bean;

import java.io.Serializable;
import java.util.List;

public class CpeLockNetInfo implements Serializable{
	private static final long serialVersionUID = 1L;

	public CpeLockNetInfo(){
		
	}
	
	public CpeLockNetInfo(String subsid, String productNo, String planCode,
			String policyNo, String lacCiDecId, String userLocation,
			List<String> userLocations, String stationCode, int netLockFlag, int busiType) {
		super();
		this.subsid = subsid;
		this.productNo = productNo;
		this.planCode = planCode;
		this.policyNo = policyNo;
		this.lacCiDecId = lacCiDecId;
		this.userLocation = userLocation;
		this.userLocations = userLocations;
		this.stationCode = stationCode;
		this.netLockFlag = netLockFlag;
	}

	/**
	 * CPE设备的唯一标识USIM
	 */
	private String subsid;
	
	/**
	 * 手机号码
	 */
	private String productNo;
	
	/**
	 * 订购产品代码
	 */
	private String planCode;
	
	/**
	 * 策略号
	 */
	private String policyNo;
	
	/**
	 * 十进制LacCi
	 */
	private String lacCiDecId;
	
	/**
	 * 小区位置
	 */
	private String userLocation;
	
	/**
	 * 基站编码
	 */
	private String stationCode;
	
	/**
	 * 9个锁网的小区
	 */
	private List<String> userLocations;
	
	/**
	 * 锁网关系：0-未锁网；1-锁网完成；2锁网中
	 */
	private int netLockFlag;
	
	/**
	 * 业务状态：1-正常；2-城市限速；3-农村限速（在锁网小区外）
	 */
	private int busiType;

	/**
	 * 获取  CPE设备的唯一标识USIM
	 * @return
	 */
	public String getSubsid() {
		return subsid;
	}

	/**
	 * 设置  CPE设备的唯一标识USIM
	 * @param subsid
	 */
	public void setSubsid(String subsid) {
		this.subsid = subsid;
	}

	/**
	 * 获取 手机号
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
	 * 获取  策略号
	 * @return
	 */
	public String getPolicyNo() {
		return policyNo;
	}

	/**
	 * 设置  策略号
	 * @param policyNo
	 */
	public void setPolicyNo(String policyNo) {
		this.policyNo = policyNo;
	}

	/**
	 * 获取  十进制LacCi
	 * @return
	 */
	public String getLacCiDecId() {
		return lacCiDecId;
	}

	/**
	 * 设置  十进制LacCi
	 * @param lacCiDecId
	 */
	public void setLacCiDecId(String lacCiDecId) {
		this.lacCiDecId = lacCiDecId;
	}
	
	/**
	 * 获取  PCC可以识别的基站小区信息
	 * @return
	 */
	public String getUserLocation() {
		return userLocation;
	}

	/**
	 * 设置  PCC可以识别的基站小区信息
	 * @param userLocation
	 */
	public void setUserLocation(String userLocation) {
		this.userLocation = userLocation;
	}

	/**
	 * 获取  基站编码
	 * @return
	 */
	public String getStationCode() {
		return stationCode;
	}

	/**
	 * 设置  基站编码
	 * @param stationCode
	 */
	public void setStationCode(String stationCode) {
		this.stationCode = stationCode;
	}

	/**
	 * 获取  9个锁网的userLocation list
	 * @return
	 */
	public List<String> getUserLocations() {
		return userLocations;
	}

	/**
	 * 设置  9个锁网的小区
	 * @param userlocation list
	 */
	public void setUserLocations(List<String> userLocations) {
		this.userLocations = userLocations;
	}

	/**
	 * 获取  锁网关系：0-未锁网；1-锁网完成；2锁网中
	 * @return
	 */
	public int getNetLockFlag() {
		return netLockFlag;
	}

	/**
	 * 设置   锁网关系：0-未锁网；1-锁网完成；2锁网中
	 * @param netLockFlag
	 */
	public void setNetLockFlag(int netLockFlag) {
		this.netLockFlag = netLockFlag;
	}

	/**
	 * 获取  业务状态：1-正常；2-城市限速；3-农村限速（在锁网小区外）
	 * @return
	 */
	public int getBusiType() {
		return busiType;
	}

	/**
	 * 设置  业务状态：1-正常；2-城市限速；3-农村限速（在锁网小区外）
	 * @param busiType
	 */
	public void setBusiType(int busiType) {
		this.busiType = busiType;
	}
	
}
