package com.ai.bdx.pop.model.privilege;

/**
 * LkgSysMenuId entity.
 * 系统菜单主键类
 *
 * @author MyEclipse Persistence Tools
 */

public class LkgSysMenuId implements java.io.Serializable {

	// Fields

	private String funcId;
	private String staffId;

	// Constructors

	/** default constructor */
	public LkgSysMenuId() {
	}

	/** full constructor */
	public LkgSysMenuId(String funcId, String staffId) {
		this.funcId = funcId;
		this.staffId = staffId;
	}

	// Property accessors

	public String getFuncId() {
		return funcId;
	}

	public void setFuncId(String funcId) {
		this.funcId = funcId;
	}

	public String getStaffId() {
		return staffId;
	}

	public void setStaffId(String staffId) {
		this.staffId = staffId;
	}

	public boolean equals(Object other) {
		if ((this == other)) {
			return true;
		}
		if ((other == null)) {
			return false;
		}
		if (!(other instanceof LkgSysMenuId)) {
			return false;
		}
		LkgSysMenuId castOther = (LkgSysMenuId) other;

		return ((this.getFuncId() == castOther.getFuncId()) || (this.getFuncId() != null
				&& castOther.getFuncId() != null && this.getFuncId().equals(castOther.getFuncId())))
				&& ((this.getStaffId() == castOther.getStaffId()) || (this.getStaffId() != null
						&& castOther.getStaffId() != null && this.getStaffId().equals(castOther.getStaffId())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + (getFuncId() == null ? 0 : this.getFuncId().hashCode());
		result = 37 * result + (getStaffId() == null ? 0 : this.getStaffId().hashCode());
		return result;
	}

}