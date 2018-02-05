package com.ai.bdx.pop.dao;

import java.util.List;

import com.asiainfo.biframe.privilege.IMenuItem;
import com.asiainfo.biframe.privilege.IUserRight;

/**
 * 角色权限对应关系Dao
 * @author guoyj
 *
 */
public interface IMpmRoleRightMapDao {

	/**
	 * 通过角色IDs获取权限集合
	 * @param roleIdList
	 * @return
	 */
	public abstract List<IUserRight> getRightsByRoles(List roleIdList);

	/**
	 * 获取MCD所有菜单
	 * @return
	 */
	public abstract List<IUserRight> getMenuItems();

	public abstract IMenuItem getMenuItemById(String memuItemId);
	
	/**
	 * 获取联创菜单，全部，用于缓存；
	 * @return
	 */
	public List getAllMenu();

}
