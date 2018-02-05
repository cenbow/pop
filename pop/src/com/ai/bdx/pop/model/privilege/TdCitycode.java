package com.ai.bdx.pop.model.privilege;

import java.io.Serializable;

import com.asiainfo.biframe.privilege.ICity;

/**
 * VwCitycode entity.
 * 区县列表
 *
 * @author MyEclipse Persistence Tools
 */

public class TdCitycode implements Serializable, ICity {

	// Fields

	/**
	 *
	 */
	private static final long serialVersionUID = -2519489912121305306L;
	private String cityCode;
	private String cityName;
	private String eparchyId;
	private String remark;
	private String orderId;

	// Constructors

	/** default constructor */
	public TdCitycode() {
	}

	/** minimal constructor */
	public TdCitycode(String cityCode) {
		this.cityCode = cityCode;
	}

	/** full constructor */
	public TdCitycode(String cityCode, String cityName, String eparchyId, String orderId) {
		this.cityCode = cityCode;
		this.cityName = cityName;
		this.eparchyId = eparchyId;
		this.orderId = orderId;
	}

	// Property accessors

	public String getCityCode() {
		return cityCode;
	}

	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public String getEparchyId() {
		return eparchyId;
	}

	public void setEparchyId(String eparchyId) {
		this.eparchyId = eparchyId;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getCityId() {
		return getCityCode();
	}

	public String getDmCityId() {
		return null;
	}

	public String getDmCountyId() {
		return null;
	}

	public String getDmDeptId() {
		return null;
	}

	public String getParentId() {
		return getEparchyId();
	}

	public int getSortNum() {
		return Integer.parseInt(getOrderId());
	}

	@Override
	public String getDmTypeCode() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDmTypeId() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

}