package com.ai.bdx.pop.model.privilege;

/**
 * LkgStaffFuncId entity.
 * 用户功能对应主键类
 *
 * @author MyEclipse Persistence Tools
 */

public class LkgStaffFuncId implements java.io.Serializable {

	// Fields

	private String staffId;
	private String funcId;

	// Constructors

	/** default constructor */
	public LkgStaffFuncId() {
	}

	/** full constructor */
	public LkgStaffFuncId(String staffId, String funcId) {
		this.staffId = staffId;
		this.funcId = funcId;
	}

	// Property accessors

	public String getStaffId() {
		return staffId;
	}

	public void setStaffId(String staffId) {
		this.staffId = staffId;
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
		if (!(other instanceof LkgStaffFuncId)) {
			return false;
		}
		LkgStaffFuncId castOther = (LkgStaffFuncId) other;

		return ((this.getStaffId() == castOther.getStaffId()) || (this.getStaffId() != null
				&& castOther.getStaffId() != null && this.getStaffId().equals(castOther.getStaffId())))
				&& ((this.getFuncId() == castOther.getFuncId()) || (this.getFuncId() != null
						&& castOther.getFuncId() != null && this.getFuncId().equals(castOther.getFuncId())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + (getStaffId() == null ? 0 : this.getStaffId().hashCode());
		result = 37 * result + (getFuncId() == null ? 0 : this.getFuncId().hashCode());
		return result;
	}

}