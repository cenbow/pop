package com.ai.bdx.pop.model.privilege;

/**
 * LkgSys entity.
 * 子系统表
 *
 * @author MyEclipse Persistence Tools
 */

public class LkgSys implements java.io.Serializable {

	// Fields

	private String sysId;
	private String sysName;
	private String sysMenuUrl;
	private String image1Url;
	private String image2Url;
	private String image3Url;
	private String remark;
	private String sysOrder;
	private String adminId;
	private String adminName;
	private String adminTime;
	private String sysType;
	private String noValidateUrl;

	// Constructors

	/** default constructor */
	public LkgSys() {
	}

	/** minimal constructor */
	public LkgSys(String sysId, String sysType) {
		this.sysId = sysId;
		this.sysType = sysType;
	}

	/** full constructor */
	public LkgSys(String sysId, String sysName, String sysMenuUrl, String image1Url, String image2Url,
			String image3Url, String remark, String sysOrder, String adminId, String adminName, String adminTime,
			String sysType, String noValidateUrl) {
		this.sysId = sysId;
		this.sysName = sysName;
		this.sysMenuUrl = sysMenuUrl;
		this.image1Url = image1Url;
		this.image2Url = image2Url;
		this.image3Url = image3Url;
		this.remark = remark;
		this.sysOrder = sysOrder;
		this.adminId = adminId;
		this.adminName = adminName;
		this.adminTime = adminTime;
		this.sysType = sysType;
		this.noValidateUrl = noValidateUrl;
	}

	// Property accessors

	public String getSysId() {
		return sysId;
	}

	public void setSysId(String sysId) {
		this.sysId = sysId;
	}

	public String getSysName() {
		return sysName;
	}

	public void setSysName(String sysName) {
		this.sysName = sysName;
	}

	public String getSysMenuUrl() {
		return sysMenuUrl;
	}

	public void setSysMenuUrl(String sysMenuUrl) {
		this.sysMenuUrl = sysMenuUrl;
	}

	public String getImage1Url() {
		return image1Url;
	}

	public void setImage1Url(String image1Url) {
		this.image1Url = image1Url;
	}

	public String getImage2Url() {
		return image2Url;
	}

	public void setImage2Url(String image2Url) {
		this.image2Url = image2Url;
	}

	public String getImage3Url() {
		return image3Url;
	}

	public void setImage3Url(String image3Url) {
		this.image3Url = image3Url;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getSysOrder() {
		return sysOrder;
	}

	public void setSysOrder(String sysOrder) {
		this.sysOrder = sysOrder;
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

	public String getSysType() {
		return sysType;
	}

	public void setSysType(String sysType) {
		this.sysType = sysType;
	}

	public String getNoValidateUrl() {
		return noValidateUrl;
	}

	public void setNoValidateUrl(String noValidateUrl) {
		this.noValidateUrl = noValidateUrl;
	}

}