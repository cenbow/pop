package com.ai.bdx.pop.model.privilege;

/**
 * LkgStaffFunc entity.
 * 用户功能对应表
 *
 * @author MyEclipse Persistence Tools
 */

public class LkgStaffFunc implements java.io.Serializable {

	// Fields

	private LkgStaffFuncId id;
	private String operateFlag;
	private String rightFlag;
	private String back1Flag;
	private String back2Flag;
	private String back3Flag;
	private String back4Flag;
	private String back5Flag;
	private String back6Flag;
	private String dataFlag;
	private String adminId;
	private String adminName;
	private String adminTime;

	// Constructors

	/** default constructor */
	public LkgStaffFunc() {
	}

	/** minimal constructor */
	public LkgStaffFunc(LkgStaffFuncId id, String operateFlag, String rightFlag) {
		this.id = id;
		this.operateFlag = operateFlag;
		this.rightFlag = rightFlag;
	}

	/** full constructor */
	public LkgStaffFunc(LkgStaffFuncId id, String operateFlag, String rightFlag, String back1Flag, String back2Flag,
			String back3Flag, String back4Flag, String back5Flag, String back6Flag, String dataFlag, String adminId,
			String adminName, String adminTime) {
		this.id = id;
		this.operateFlag = operateFlag;
		this.rightFlag = rightFlag;
		this.back1Flag = back1Flag;
		this.back2Flag = back2Flag;
		this.back3Flag = back3Flag;
		this.back4Flag = back4Flag;
		this.back5Flag = back5Flag;
		this.back6Flag = back6Flag;
		this.dataFlag = dataFlag;
		this.adminId = adminId;
		this.adminName = adminName;
		this.adminTime = adminTime;
	}

	// Property accessors

	public LkgStaffFuncId getId() {
		return id;
	}

	public void setId(LkgStaffFuncId id) {
		this.id = id;
	}

	public String getOperateFlag() {
		return operateFlag;
	}

	public void setOperateFlag(String operateFlag) {
		this.operateFlag = operateFlag;
	}

	public String getRightFlag() {
		return rightFlag;
	}

	public void setRightFlag(String rightFlag) {
		this.rightFlag = rightFlag;
	}

	public String getBack1Flag() {
		return back1Flag;
	}

	public void setBack1Flag(String back1Flag) {
		this.back1Flag = back1Flag;
	}

	public String getBack2Flag() {
		return back2Flag;
	}

	public void setBack2Flag(String back2Flag) {
		this.back2Flag = back2Flag;
	}

	public String getBack3Flag() {
		return back3Flag;
	}

	public void setBack3Flag(String back3Flag) {
		this.back3Flag = back3Flag;
	}

	public String getBack4Flag() {
		return back4Flag;
	}

	public void setBack4Flag(String back4Flag) {
		this.back4Flag = back4Flag;
	}

	public String getBack5Flag() {
		return back5Flag;
	}

	public void setBack5Flag(String back5Flag) {
		this.back5Flag = back5Flag;
	}

	public String getBack6Flag() {
		return back6Flag;
	}

	public void setBack6Flag(String back6Flag) {
		this.back6Flag = back6Flag;
	}

	public String getDataFlag() {
		return dataFlag;
	}

	public void setDataFlag(String dataFlag) {
		this.dataFlag = dataFlag;
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