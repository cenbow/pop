package com.ai.bdx.pop.model.privilege;

/**
 * LkgStaffJobId entity.
 * 用户角色关系主键类
 *
 * @author MyEclipse Persistence Tools
 */

public class LkgStaffJobId implements java.io.Serializable {

	// Fields

	private String staffId;
	private String jobId;

	// Constructors

	/** default constructor */
	public LkgStaffJobId() {
	}

	/** full constructor */
	public LkgStaffJobId(String staffId, String jobId) {
		this.staffId = staffId;
		this.jobId = jobId;
	}

	// Property accessors

	public String getStaffId() {
		return staffId;
	}

	public void setStaffId(String staffId) {
		this.staffId = staffId;
	}

	public String getJobId() {
		return jobId;
	}

	public void setJobId(String jobId) {
		this.jobId = jobId;
	}

	public boolean equals(Object other) {
		if ((this == other)) {
			return true;
		}
		if ((other == null)) {
			return false;
		}
		if (!(other instanceof LkgStaffJobId)) {
			return false;
		}
		LkgStaffJobId castOther = (LkgStaffJobId) other;

		return ((this.getStaffId() == castOther.getStaffId()) || (this.getStaffId() != null
				&& castOther.getStaffId() != null && this.getStaffId().equals(castOther.getStaffId())))
				&& ((this.getJobId() == castOther.getJobId()) || (this.getJobId() != null
						&& castOther.getJobId() != null && this.getJobId().equals(castOther.getJobId())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + (getStaffId() == null ? 0 : this.getStaffId().hashCode());
		result = 37 * result + (getJobId() == null ? 0 : this.getJobId().hashCode());
		return result;
	}

}