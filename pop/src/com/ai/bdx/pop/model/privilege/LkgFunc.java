package com.ai.bdx.pop.model.privilege;

import com.asiainfo.biframe.privilege.IMenuItem;

/**
 * LkgFunc entity.
 * 功能表
 *
 * @author MyEclipse Persistence Tools
 */

public class LkgFunc implements java.io.Serializable, IMenuItem {

	// Fields
	/**
	 *
	 */
	private static final long serialVersionUID = 6928748296574655412L;
	private String funcId;
	private String sysId;
	private String funcName;
	private String pageUrl;
	private String folderId;
	private String remark;
	private String back1NameFlag;
	private String back2NameFlag;
	private String back3NameFlag;
	private String back4NameFlag;
	private String back5NameFlag;
	private String back6NameFlag;
	private String startDate;
	private String endDate;
	private String adminId;
	private String adminName;
	private String adminTime;
	private String dealState;
	private String useState;
	private String placeState;
	private String dsType;
	private String operType;
	private String operSubType;

	// Constructors

	/** default constructor */
	public LkgFunc() {
	}

	/** minimal constructor */
	public LkgFunc(String funcId) {
		this.funcId = funcId;
	}

	/** full constructor */
	public LkgFunc(String funcId, String sysId, String funcName, String pageUrl, String folderId, String remark,
			String back1NameFlag, String back2NameFlag, String back3NameFlag, String back4NameFlag,
			String back5NameFlag, String back6NameFlag, String startDate, String endDate, String adminId,
			String adminName, String adminTime, String dealState, String useState, String placeState, String dsType,
			String operType, String operSubType) {
		this.funcId = funcId;
		this.sysId = sysId;
		this.funcName = funcName;
		this.pageUrl = pageUrl;
		this.folderId = folderId;
		this.remark = remark;
		this.back1NameFlag = back1NameFlag;
		this.back2NameFlag = back2NameFlag;
		this.back3NameFlag = back3NameFlag;
		this.back4NameFlag = back4NameFlag;
		this.back5NameFlag = back5NameFlag;
		this.back6NameFlag = back6NameFlag;
		this.startDate = startDate;
		this.endDate = endDate;
		this.adminId = adminId;
		this.adminName = adminName;
		this.adminTime = adminTime;
		this.dealState = dealState;
		this.useState = useState;
		this.placeState = placeState;
		this.dsType = dsType;
		this.operType = operType;
		this.operSubType = operSubType;
	}

	// Property accessors

	public String getFuncId() {
		return funcId;
	}

	public void setFuncId(String funcId) {
		this.funcId = funcId;
	}

	public String getSysId() {
		return sysId;
	}

	public void setSysId(String sysId) {
		this.sysId = sysId;
	}

	public String getFuncName() {
		return funcName;
	}

	public void setFuncName(String funcName) {
		this.funcName = funcName;
	}

	public String getPageUrl() {
		return pageUrl;
	}

	public void setPageUrl(String pageUrl) {
		this.pageUrl = pageUrl;
	}

	public String getFolderId() {
		return folderId;
	}

	public void setFolderId(String folderId) {
		this.folderId = folderId;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getBack1NameFlag() {
		return back1NameFlag;
	}

	public void setBack1NameFlag(String back1NameFlag) {
		this.back1NameFlag = back1NameFlag;
	}

	public String getBack2NameFlag() {
		return back2NameFlag;
	}

	public void setBack2NameFlag(String back2NameFlag) {
		this.back2NameFlag = back2NameFlag;
	}

	public String getBack3NameFlag() {
		return back3NameFlag;
	}

	public void setBack3NameFlag(String back3NameFlag) {
		this.back3NameFlag = back3NameFlag;
	}

	public String getBack4NameFlag() {
		return back4NameFlag;
	}

	public void setBack4NameFlag(String back4NameFlag) {
		this.back4NameFlag = back4NameFlag;
	}

	public String getBack5NameFlag() {
		return back5NameFlag;
	}

	public void setBack5NameFlag(String back5NameFlag) {
		this.back5NameFlag = back5NameFlag;
	}

	public String getBack6NameFlag() {
		return back6NameFlag;
	}

	public void setBack6NameFlag(String back6NameFlag) {
		this.back6NameFlag = back6NameFlag;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
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

	public String getDealState() {
		return dealState;
	}

	public void setDealState(String dealState) {
		this.dealState = dealState;
	}

	public String getUseState() {
		return useState;
	}

	public void setUseState(String useState) {
		this.useState = useState;
	}

	public String getPlaceState() {
		return placeState;
	}

	public void setPlaceState(String placeState) {
		this.placeState = placeState;
	}

	public String getDsType() {
		return dsType;
	}

	public void setDsType(String dsType) {
		this.dsType = dsType;
	}

	public String getOperType() {
		return operType;
	}

	public void setOperType(String operType) {
		this.operType = operType;
	}

	public String getOperSubType() {
		return operSubType;
	}

	public void setOperSubType(String operSubType) {
		this.operSubType = operSubType;
	}

	public Integer getAccessToken() {
		return 0;
	}

	public String getApplicationId() {
		return sysId;
	}

	public Integer getMenuItemId() {
		return Integer.parseInt(getFuncId());
	}

	public String getMenuItemTitle() {
		return getFuncName();
	}

	public Integer getMenuType() {
		return 0;
	}

	public String getOperationType() {
		return null;
	}

	public Integer getParentId() {
		return Integer.parseInt(getFolderId());
	}

	public String getResId() {
		return null;
	}

	public Integer getResourceType() {
		return 0;
	}

	public Integer getSortNum() {
		return Integer.parseInt(getRemark());
	}

	public String getUrl() {
		return getPageUrl();
	}

	public String getUrlPort() {
		return null;
	}

	public String getUrlTarget() {
		return null;
	}

	public boolean isFolderOrNot() {
		return false;
	}

	public String getPic1() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getPic2() {
		// TODO Auto-generated method stub
		return null;
	}
}