package com.ai.bdx.pop.model.privilege;

import com.asiainfo.biframe.privilege.ICity;

/**
 * LkgParamArea entity.
 *
 * @author MyEclipse Persistence Tools
 */

public class LkgParamArea implements java.io.Serializable, ICity {

	// Fields

	private String provId;
	private String cityId;
	private String countryId;
	private String areaId;
	private String sub1AreaId;
	private String sub2AreaId;
	private String provName;
	private String cityName;
	private String countryName;
	private String areaName;
	private String sub1AreaName;
	private String sub2AreaName;
	private String cityIdNew;
	private String countryIdNew;
	private String areaIdNew;

	// Constructors

	/** default constructor */
	public LkgParamArea() {
	}

	/** full constructor */
	public LkgParamArea(String provId, String cityId, String countryId, String areaId, String sub1AreaId,
			String sub2AreaId, String provName, String cityName, String countryName, String areaName,
			String sub1AreaName, String sub2AreaName, String cityIdNew, String countryIdNew, String areaIdNew) {
		this.provId = provId;
		this.cityId = cityId;
		this.countryId = countryId;
		this.areaId = areaId;
		this.sub1AreaId = sub1AreaId;
		this.sub2AreaId = sub2AreaId;
		this.provName = provName;
		this.cityName = cityName;
		this.countryName = countryName;
		this.areaName = areaName;
		this.sub1AreaName = sub1AreaName;
		this.sub2AreaName = sub2AreaName;
		this.countryIdNew = countryIdNew;
		this.areaIdNew = areaIdNew;
		this.cityIdNew = cityIdNew;
	}

	// Property accessors

	public String getProvId() {
		return provId;
	}

	public void setProvId(String provId) {
		this.provId = provId;
	}

	public String getCityId() {
		return cityId;
	}

	public void setCityId(String cityId) {
		this.cityId = cityId;
	}

	public String getCountryId() {
		return countryId;
	}

	public void setCountryId(String countryId) {
		this.countryId = countryId;
	}

	public String getAreaId() {
		return areaId;
	}

	public void setAreaId(String areaId) {
		this.areaId = areaId;
	}

	public String getSub1AreaId() {
		return sub1AreaId;
	}

	public void setSub1AreaId(String sub1AreaId) {
		this.sub1AreaId = sub1AreaId;
	}

	public String getSub2AreaId() {
		return sub2AreaId;
	}

	public void setSub2AreaId(String sub2AreaId) {
		this.sub2AreaId = sub2AreaId;
	}

	public String getProvName() {
		return provName;
	}

	public void setProvName(String provName) {
		this.provName = provName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public String getCountryName() {
		return countryName;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

	public String getAreaName() {
		return areaName;
	}

	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}

	public String getSub1AreaName() {
		return sub1AreaName;
	}

	public void setSub1AreaName(String sub1AreaName) {
		this.sub1AreaName = sub1AreaName;
	}

	public String getSub2AreaName() {
		return sub2AreaName;
	}

	public void setSub2AreaName(String sub2AreaName) {
		this.sub2AreaName = sub2AreaName;
	}

	public String getCityName() {
		return cityName;
	}

	public String getDmCityId() {
		return null;
	}

	public String getDmCountyId() {
		return countryId;
	}

	public String getDmDeptId() {
		return areaId;
	}

	public String getParentId() {
		return provId;
	}

	public int getSortNum() {
		return 0;
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

	public String getCountryIdNew() {
		return countryIdNew;
	}

	public void setCountryIdNew(String countryIdNew) {
		this.countryIdNew = countryIdNew;
	}

	public String getAreaIdNew() {
		return areaIdNew;
	}

	public void setAreaIdNew(String areaIdNew) {
		this.areaIdNew = areaIdNew;
	}

	public String getCityIdNew() {
		return cityIdNew;
	}

	public void setCityIdNew(String cityIdNew) {
		this.cityIdNew = cityIdNew;
	}

}