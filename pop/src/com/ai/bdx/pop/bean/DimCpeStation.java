package com.ai.bdx.pop.bean;

import java.io.Serializable;

public class DimCpeStation implements Serializable{

	private static final long serialVersionUID = -7480853394932030866L;
	
	public DimCpeStation(){	
	}
	public DimCpeStation(String stationCode,String stationName,String countryName,
						 String townName,String countyName,String cityName,String countyId,
						 String cityId,int busiType1,int busiType,int netLockFlag,String count){
		super();
		this.stationCode = stationCode;
		this.stationName = stationName;
		this.countryName = countryName;
		this.townName = townName;
		this.countyName = countyName;
		this.cityName = cityName;
		this.countryName = countryName;
		this.cityId = cityId;
		this.busiType1 = busiType1;
		this.busiType = busiType;
		this.netLockFlag = netLockFlag;
		this.count = count;
	}

	//基站code
	private String stationCode;
	
	//基站名称
	private String stationName;
	
	//归属村
	private String countryName;
	
	//归属乡镇
	private String townName;
	
	//归属县
	private String countyName;
	
	//归属市
	private String cityName;
	
	//归属县编码
	private String countyId;
	
	//归属市编码
	private String cityId;
	
	//业务状态
	private int busiType1;
	
	//业务状态
	private int busiType;
	
	//锁网状态
	private int netLockFlag;
	
	//可接入小区
	private String count;

	public String getStationCode() {
		return stationCode;
	}

	public void setStationCode(String stationCode) {
		this.stationCode = stationCode;
	}

	public String getStationName() {
		return stationName;
	}

	public void setStationName(String stationName) {
		this.stationName = stationName;
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

	public String getCountyId() {
		return countyId;
	}

	public void setCountyId(String countyId) {
		this.countyId = countyId;
	}

	public String getCityId() {
		return cityId;
	}

	public void setCityId(String cityId) {
		this.cityId = cityId;
	}

	public int getBusiType1() {
		return busiType1;
	}

	public void setBusiType1(int busiType1) {
		this.busiType1 = busiType1;
	}

	public int getBusiType() {
		return busiType;
	}

	public void setBusiType(int busiType) {
		this.busiType = busiType;
	}

	public int getNetLockFlag() {
		return netLockFlag;
	}

	public void setNetLockFlag(int netLockFlag) {
		this.netLockFlag = netLockFlag;
	}
	public String getCount() {
		return count;
	}
	public void setCount(String count) {
		this.count = count;
	}

	
}
