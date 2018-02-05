package com.ai.bdx.pop.model.privilege;

import com.ai.bdx.pop.dao.IUserScope;

/**
 * LkgStaffScope entity. 员工数据权限表(省市)
 *
 * @author MyEclipse Persistence Tools
 */

public class LkgStaffScope implements java.io.Serializable, IUserScope {
	// Fields

	private String staffId;
	private String provScope;
	private String cityScope;
	private String countryScope;
	private String areaScope;
	private String sub1AreaScope;
	private String sub2AreaScope;
	private String adminId;
	private String adminName;
	private String adminTime;

	// Constructors

	/** default constructor */
	public LkgStaffScope() {
	}

	/** minimal constructor */
	public LkgStaffScope(String staffId) {
		this.staffId = staffId;
	}

	/** full constructor */
	public LkgStaffScope(String staffId, String provScope, String cityScope, String countryScope, String adminId,
			String adminName, String adminTime, String areaScope, String sub1AreaScope, String sub2AreaScope) {
		this.staffId = staffId;
		this.provScope = provScope;
		this.cityScope = cityScope;
		this.countryScope = countryScope;
		this.adminId = adminId;
		this.adminName = adminName;
		this.adminTime = adminTime;
		this.areaScope = areaScope;
		this.sub1AreaScope = sub1AreaScope;
		this.sub2AreaScope = sub2AreaScope;
	}

	// Property accessors
	public String getStaffId() {
		return staffId;
	}

	public void setStaffId(String staffId) {
		this.staffId = staffId;
	}

	public String getProvScope() {
		return provScope;
	}

	public void setProvScope(String provScope) {
		this.provScope = provScope;
	}

	public String getCityScope() {
		return cityScope;
	}

	public void setCityScope(String cityScope) {
		this.cityScope = cityScope;
	}

	public String getCountryScope() {
		return countryScope;
	}

	public void setCountryScope(String countryScope) {
		this.countryScope = countryScope;
	}

	public String getAdminId() {
		return adminId;
	}

	public void setAdminId(String adminId) {
		this.adminId = adminId;
	}

	public String getAdminName() {
		return adminName;
	}

	public void setAdminName(String adminName) {
		this.adminName = adminName;
	}

	public String getAdminTime() {
		return adminTime;
	}

	public void setAdminTime(String adminTime) {
		this.adminTime = adminTime;
	}

	public String getAreaScope() {
		return areaScope;
	}

	public void setAreaScope(String areaScope) {
		this.areaScope = areaScope;
	}

	public String getSub1AreaScope() {
		return sub1AreaScope;
	}

	public void setSub1AreaScope(String sub1AreaScope) {
		this.sub1AreaScope = sub1AreaScope;
	}

	public String getSub2AreaScope() {
		return sub2AreaScope;
	}

	public void setSub2AreaScope(String sub2AreaScope) {
		this.sub2AreaScope = sub2AreaScope;
	}

}