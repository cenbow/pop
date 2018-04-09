package com.ailk.bdx.pop.adapter.bean;

import com.google.common.base.Objects;

public class CPEPositionBean {
	private String subsid;
	private String productNo;
	private String userLocation;

	public CPEPositionBean(String subsid, String productNo, String userLocation) {
		super();
		this.subsid = subsid;
		this.productNo = productNo;
		this.userLocation = userLocation;
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
	
	public String getUserLocation() {
		return userLocation;
	}

	public void setUserLocation(String userLocation) {
		this.userLocation = userLocation;
	}

	@Override
	public String toString() {
		return Objects.toStringHelper(this)
				.add("subsid", subsid)
				.add("productNo", productNo)
				.add("userLocation", userLocation)
				.toString();
	}

}
