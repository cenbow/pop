package com.ai.bdx.pop.model.privilege;

/**
 * LkgJobFuncId entity.
 * 角色功能对应主键类
 *
 * @author MyEclipse Persistence Tools
 */

public class LkgJobFuncId implements java.io.Serializable {

	// Fields

	private String jobId;
	private String funcId;

	// Constructors

	/** default constructor */
	public LkgJobFuncId() {
	}

	/** full constructor */
	public LkgJobFuncId(String jobId, String funcId) {
		this.jobId = jobId;
		this.funcId = funcId;
	}

	// Property accessors

	public String getJobId() {
		return jobId;
	}

	public void setJobId(String jobId) {
		this.jobId = jobId;
	}

	public String getFuncId() {
		return funcId;
	}

	public void setFuncId(String funcId) {
		this.funcId = funcId;
	}

	public boolean equals(Object other) {
		if ((this == other)) {
			return true;
		}
		if ((other == null)) {
			return false;
		}
		if (!(other instanceof LkgJobFuncId)) {
			return false;
		}
		LkgJobFuncId castOther = (LkgJobFuncId) other;

		return ((this.getJobId() == castOther.getJobId()) || (this.getJobId() != null && castOther.getJobId() != null && this
				.getJobId().equals(castOther.getJobId())))
				&& ((this.getFuncId() == castOther.getFuncId()) || (this.getFuncId() != null
						&& castOther.getFuncId() != null && this.getFuncId().equals(castOther.getFuncId())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + (getJobId() == null ? 0 : this.getJobId().hashCode());
		result = 37 * result + (getFuncId() == null ? 0 : this.getFuncId().hashCode());
		return result;
	}

}