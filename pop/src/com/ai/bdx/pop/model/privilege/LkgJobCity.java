package com.ai.bdx.pop.model.privilege;

/**
 * LkgJobCity entity.
 * 角色功能地市对应表
 *
 * @author MyEclipse Persistence Tools
 */

public class LkgJobCity implements java.io.Serializable {

	// Fields

	/**
	 *
	 */
	private static final long serialVersionUID = 4080436106595078936L;
	private LkgJobCityId id;
	private String funcId;
	private String depId;
	private String adminId;
	private String adminName;
	private String adminTime;

	// Constructors

	/** default constructor */
	public LkgJobCity() {
	}

	/** minimal constructor */
	public LkgJobCity(LkgJobCityId id) {
		this.id = id;
	}

	/** full constructor */
	public LkgJobCity(LkgJobCityId id, String funcId, String depId, String adminId, String adminName, String adminTime) {
		this.id = id;
		this.funcId = funcId;
		this.depId = depId;
		this.adminId = adminId;
		this.adminName = adminName;
		this.adminTime = adminTime;
	}

	// Property accessors

	public LkgJobCityId getId() {
		return id;
	}

	public void setId(LkgJobCityId id) {
		this.id = id;
	}

	public String getFuncId() {
		return funcId;
	}

	public void setFuncId(String funcId) {
		this.funcId = funcId;
	}

	public String getDepId() {
		return depId;
	}

	public void setDepId(String depId) {
		this.depId = depId;
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