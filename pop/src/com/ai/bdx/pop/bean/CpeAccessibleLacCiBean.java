package com.ai.bdx.pop.bean;

import java.io.Serializable;

public class CpeAccessibleLacCiBean implements Serializable{

	private static final long serialVersionUID = 5786470928656158637L;
	
	// 设备号
	private String subsid;

	// 手机号
	private String productNo;
	
	private String cityId;

	// 归属市
	private String cityName;
	
	private String countyId;

	// 归属县
	private String countyName;

	// 所属乡镇
	private String townName;

	// 归属村
	private String countryName;

	// 小区中文名称
	private String cellName;
	
	private String lacCi;
	
	private String userLocation;
	
	private String keyword;
	
	public CpeAccessibleLacCiBean(){
		
	}

	public CpeAccessibleLacCiBean(String subsid, String productNo,
			String cityId, String cityName, String countyId, String countyName,
			String townName, String countryName, String cellName, String lacCi,
			String userLocation,String keyword) {
		super();
		this.subsid = subsid;
		this.productNo = productNo;
		this.cityId = cityId;
		this.cityName = cityName;
		this.countyId = countyId;
		this.countyName = countyName;
		this.townName = townName;
		this.countryName = countryName;
		this.cellName = cellName;
		this.lacCi = lacCi;
		this.userLocation = userLocation;
		this.keyword = keyword;
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

	public String getCityId() {
		return cityId;
	}

	public void setCityId(String cityId) {
		this.cityId = cityId;
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

	public String getCountyName() {
		return countyName;
	}

	public void setCountyName(String countyName) {
		this.countyName = countyName;
	}

	public String getTownName() {
		return townName;
	}

	public void setTownName(String townName) {
		this.townName = townName;
	}

	public String getCountryName() {
		return countryName;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

	public String getCellName() {
		return cellName;
	}

	public void setCellName(String cellName) {
		this.cellName = cellName;
	}

	public String getLacCi() {
		return lacCi;
	}

	public void setLacCi(String lacCi) {
		this.lacCi = lacCi;
	}

	public String getUserLocation() {
		return userLocation;
	}

	public void setUserLocation(String userLocation) {
		this.userLocation = userLocation;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

}
