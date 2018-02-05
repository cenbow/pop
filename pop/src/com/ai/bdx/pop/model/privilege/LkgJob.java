package com.ai.bdx.pop.model.privilege;

import com.asiainfo.biframe.privilege.IUserRole;

/**
 * LkgJob entity.
 * 角色表
 *
 * @author MyEclipse Persistence Tools
 */

public class LkgJob implements java.io.Serializable, IUserRole {
	// Fields

	/**
	 *
	 */
	private static final long serialVersionUID = 8083811153258973143L;
	private String jobId;
	private String jobName;
	private String remark;
	private String adminId;
	private String adminName;
	private String adminTime;

	// Constructors

	/** default constructor */
	public LkgJob() {
	}

	/** minimal constructor */
	public LkgJob(String jobId) {
		this.jobId = jobId;
	}

	/** full constructor */
	public LkgJob(String jobId, String jobName, String remark, String adminId, String adminName, String adminTime) {
		this.jobId = jobId;
		this.jobName = jobName;
		this.remark = remark;
		this.adminId = adminId;
		this.adminName = adminName;
		this.adminTime = adminTime;
	}

	// Property accessors

	public String getJobId() {
		return jobId;
	}

	public void setJobId(String jobId) {
		this.jobId = jobId;
	}

	public String getJobName() {
		return jobName;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
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

	public String getParentId() {
		// 这里缺少角色上级字段
		return null;
	}

	public String getRoleId() {
		return getJobId();
	}

	public String getRoleName() {
		return getJobName();
	}

	public String getClassifyId() {
		// TODO Auto-generated method stub
		return null;
	}

}