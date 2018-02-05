package com.ai.bdx.pop.model.privilege;

import com.asiainfo.biframe.privilege.IUserGroup;

/**
 * LkgUserGroup entity.
 * 用户组表
 *
 * @author MyEclipse Persistence Tools
 */

public class LkgUserGroup implements java.io.Serializable, IUserGroup {
	/**
	 *
	 */
	private static final long serialVersionUID = -1345601730364713233L;
	// Fields
	private String groupId;
	private String groupName;
	private String validFlag;
	private String upperGroupId;
	private String dataLevel;
	private String remark;

	// Constructors
	/** default constructor */
	public LkgUserGroup() {
	}

	/** minimal constructor */
	public LkgUserGroup(String groupId) {
		this.groupId = groupId;
	}

	/** full constructor */
	public LkgUserGroup(String groupId, String groupName, String validFlag, String upperGroupId, String dataLevel,
			String remark) {
		this.groupId = groupId;
		this.groupName = groupName;
		this.validFlag = validFlag;
		this.upperGroupId = upperGroupId;
		this.dataLevel = dataLevel;
		this.remark = remark;
	}

	// Property accessors

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getValidFlag() {
		return validFlag;
	}

	public void setValidFlag(String validFlag) {
		this.validFlag = validFlag;
	}

	public String getUpperGroupId() {
		return upperGroupId;
	}

	public void setUpperGroupId(String upperGroupId) {
		this.upperGroupId = upperGroupId;
	}

	public String getDataLevel() {
		return dataLevel;
	}

	public void setDataLevel(String dataLevel) {
		this.dataLevel = dataLevel;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getGroupid() {
		return getGroupId();
	}

	public String getGroupname() {
		return getGroupName();
	}

	public String getParentid() {
		return getUpperGroupId();
	}

}