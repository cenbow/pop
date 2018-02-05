package com.ai.bdx.pop.model.privilege;

/**
 * LkgStaffJob entity.
 * 用户角色关系表
 *
 * @author MyEclipse Persistence Tools
 */

public class LkgStaffJob implements java.io.Serializable {

	// Fields

	/**
	 *
	 */
	private static final long serialVersionUID = 3216617051173133603L;
	private LkgStaffJobId id;
	private String adminId;
	private String adminName;
	private String adminTime;

	// Constructors

	/** default constructor */
	public LkgStaffJob() {
	}

	/** minimal constructor */
	public LkgStaffJob(LkgStaffJobId id) {
		this.id = id;
	}

	/** full constructor */
	public LkgStaffJob(LkgStaffJobId id, String adminId, String adminName, String adminTime) {
		this.id = id;
		this.adminId = adminId;
		this.adminName = adminName;
		this.adminTime = adminTime;
	}

	// Property accessors

	public LkgStaffJobId getId() {
		return id;
	}

	public void setId(LkgStaffJobId id) {
		this.id = id;
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

}