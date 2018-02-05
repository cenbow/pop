package com.ai.bdx.pop.model.privilege;

/**
 * LkgSysMenu entity.
 * 系统菜单表
 *
 * @author MyEclipse Persistence Tools
 */

public class LkgSysMenu implements java.io.Serializable {

	// Fields

	private LkgSysMenuId id;
	private String recDate;

	// Constructors

	/** default constructor */
	public LkgSysMenu() {
	}

	/** minimal constructor */
	public LkgSysMenu(LkgSysMenuId id) {
		this.id = id;
	}

	/** full constructor */
	public LkgSysMenu(LkgSysMenuId id, String recDate) {
		this.id = id;
		this.recDate = recDate;
	}

	// Property accessors

	public LkgSysMenuId getId() {
		return id;
	}

	public void setId(LkgSysMenuId id) {
		this.id = id;
	}

	public String getRecDate() {
		return recDate;
	}

	public void setRecDate(String recDate) {
		this.recDate = recDate;
	}

}