package com.ai.bdx.pop.model.privilege;

import com.asiainfo.biframe.privilege.IUserCompany;
import com.asiainfo.biframe.privilege.cache.service.IdAndName;

/**
 * LkgStaffCompany entity.
 * 用户公司/部门表
 *
 * @author MyEclipse Persistence Tools
 */

public class LkgStaffCompany implements java.io.Serializable, IdAndName, IUserCompany {
	/**
	 *
	 */
	private static final long serialVersionUID = -6945896746431944014L;
	// Fields
	private Integer deptid;
	private Integer parentid;
	private String title;
	private String notes;
	private String serviceCode;
	private Integer sortnum;
	private String status;
	private String deleteTime;

	// Constructors

	/** default constructor */
	public LkgStaffCompany() {
	}

	/** minimal constructor */
	public LkgStaffCompany(Integer deptid, Integer parentid, String title, Integer sortnum, String status) {
		this.deptid = deptid;
		this.parentid = parentid;
		this.title = title;
		this.sortnum = sortnum;
		this.status = status;
	}

	/** full constructor */
	public LkgStaffCompany(Integer deptid, Integer parentid, String title, String notes, String serviceCode,
			Integer sortnum, String status, String deleteTime) {
		this.deptid = deptid;
		this.parentid = parentid;
		this.title = title;
		this.notes = notes;
		this.serviceCode = serviceCode;
		this.sortnum = sortnum;
		this.status = status;
		this.deleteTime = deleteTime;
	}

	// Property accessors

	public Integer getDeptid() {
		return deptid;
	}

	public void setDeptid(Integer deptid) {
		this.deptid = deptid;
	}

	public Integer getParentid() {
		return parentid;
	}

	public void setParentid(Integer parentid) {
		this.parentid = parentid;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public String getServiceCode() {
		return serviceCode;
	}

	public void setServiceCode(String serviceCode) {
		this.serviceCode = serviceCode;
	}

	public Integer getSortnum() {
		return sortnum;
	}

	public void setSortnum(Integer sortnum) {
		this.sortnum = sortnum;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getDeleteTime() {
		return deleteTime;
	}

	public void setDeleteTime(String deleteTime) {
		this.deleteTime = deleteTime;
	}

	public String getName() {
		return getTitle();
	}

	public Object getPrimaryKey() {
		return getDeptid();
	}

}