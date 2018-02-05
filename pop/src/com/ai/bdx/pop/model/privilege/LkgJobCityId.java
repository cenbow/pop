package com.ai.bdx.pop.model.privilege;

/**
 * LkgJobCityId entity.
 * 角色功能地市主键类
 *
 * @author MyEclipse Persistence Tools
 */

public class LkgJobCityId implements java.io.Serializable {

	// Fields

	/**
	 *
	 */
	private static final long serialVersionUID = -5873320002940072377L;
	private String jobId;
	private String cityId;

	// Constructors

	/** default constructor */
	public LkgJobCityId() {
	}

	/** full constructor */
	public LkgJobCityId(String jobId, String cityId) {
		this.jobId = jobId;
		this.cityId = cityId;
	}

	// Property accessors

	public String getJobId() {
		return jobId;
	}

	public void setJobId(String jobId) {
		this.jobId = jobId;
	}

	public String getCityId() {
		return cityId;
	}

	public void setCityId(String cityId) {
		this.cityId = cityId;
	}

	public boolean equals(Object other) {
		if ((this == other)) {
			return true;
		}
		if ((other == null)) {
			return false;
		}
		if (!(other instanceof LkgJobCityId)) {
			return false;
		}
		LkgJobCityId castOther = (LkgJobCityId) other;

		return ((this.getJobId() == castOther.getJobId()) || (this.getJobId() != null && castOther.getJobId() != null && this
				.getJobId().equals(castOther.getJobId())))
				&& ((this.getCityId() == castOther.getCityId()) || (this.getCityId() != null
						&& castOther.getCityId() != null && this.getCityId().equals(castOther.getCityId())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + (getJobId() == null ? 0 : this.getJobId().hashCode());
		result = 37 * result + (getCityId() == null ? 0 : this.getCityId().hashCode());
		return result;
	}

}