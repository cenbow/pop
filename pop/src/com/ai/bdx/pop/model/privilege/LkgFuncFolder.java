package com.ai.bdx.pop.model.privilege;

import com.asiainfo.biframe.privilege.IMenuItem;

/**
 * LkgFuncFolder entity.
 * 模块目录表
 *
 * @author MyEclipse Persistence Tools
 */

public class LkgFuncFolder implements java.io.Serializable, IMenuItem {

	// Fields

	private String folderId;
	private String sysId;
	private String folderName;
	private String levelId;
	private String parentId;
	private String priorId;
	private String remark;
	private String adminId;
	private String adminName;
	private String adminTime;
	private String menuUrl;
	private String mainUrl;
	private String noValidateUrl;
	private String menuWidth;
	private String menuOrder;

	// Constructors

	/** default constructor */
	public LkgFuncFolder() {
	}

	/** minimal constructor */
	public LkgFuncFolder(String folderId) {
		this.folderId = folderId;
	}

	/** full constructor */
	public LkgFuncFolder(String folderId, String sysId, String folderName, String levelId, String parentId,
			String priorId, String remark, String adminId, String adminName, String adminTime, String menuUrl,
			String mainUrl, String noValidateUrl, String menuWidth, String menuOrder) {
		this.folderId = folderId;
		this.sysId = sysId;
		this.folderName = folderName;
		this.levelId = levelId;
		this.parentId = parentId;
		this.priorId = priorId;
		this.remark = remark;
		this.adminId = adminId;
		this.adminName = adminName;
		this.adminTime = adminTime;
		this.menuUrl = menuUrl;
		this.mainUrl = mainUrl;
		this.noValidateUrl = noValidateUrl;
		this.menuWidth = menuWidth;
		this.menuOrder = menuOrder;
	}

	// Property accessors

	public String getFolderId() {
		return folderId;
	}

	public void setFolderId(String folderId) {
		this.folderId = folderId;
	}

	public String getSysId() {
		return sysId;
	}

	public void setSysId(String sysId) {
		this.sysId = sysId;
	}

	public String getFolderName() {
		return folderName;
	}

	public void setFolderName(String folderName) {
		this.folderName = folderName;
	}

	public String getLevelId() {
		return levelId;
	}

	public void setLevelId(String levelId) {
		this.levelId = levelId;
	}

	public Integer getParentId() {
		return Integer.parseInt(parentId);
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getPriorId() {
		return priorId;
	}

	public void setPriorId(String priorId) {
		this.priorId = priorId;
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

	public String getMenuUrl() {
		return menuUrl;
	}

	public void setMenuUrl(String menuUrl) {
		this.menuUrl = menuUrl;
	}

	public String getMainUrl() {
		return mainUrl;
	}

	public void setMainUrl(String mainUrl) {
		this.mainUrl = mainUrl;
	}

	public String getNoValidateUrl() {
		return noValidateUrl;
	}

	public void setNoValidateUrl(String noValidateUrl) {
		this.noValidateUrl = noValidateUrl;
	}

	public String getMenuWidth() {
		return menuWidth;
	}

	public void setMenuWidth(String menuWidth) {
		this.menuWidth = menuWidth;
	}

	public String getMenuOrder() {
		return menuOrder;
	}

	public void setMenuOrder(String menuOrder) {
		this.menuOrder = menuOrder;
	}

	public Integer getAccessToken() {
		// TODO Auto-generated method stub
		return 0;
	}

	public String getApplicationId() {
		// TODO Auto-generated method stub
		return sysId;
	}

	public Integer getMenuItemId() {
		// TODO Auto-generated method stub
		return Integer.parseInt(getFolderId());
	}

	public String getMenuItemTitle() {
		// TODO Auto-generated method stub
		return getFolderName();
	}

	public Integer getMenuType() {
		// TODO Auto-generated method stub
		return 0;
	}

	public String getOperationType() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getResId() {
		// TODO Auto-generated method stub
		return null;
	}

	public Integer getResourceType() {
		// TODO Auto-generated method stub
		return 0;
	}

	public Integer getSortNum() {
		// TODO Auto-generated method stub
		return Integer.parseInt(getMenuOrder());
	}

	public String getUrl() {
		// TODO Auto-generated method stub
		return getMainUrl();
	}

	public String getUrlPort() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getUrlTarget() {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean isFolderOrNot() {
		// TODO Auto-generated method stub
		return true;
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