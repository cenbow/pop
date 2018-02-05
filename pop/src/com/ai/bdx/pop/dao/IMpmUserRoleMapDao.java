package com.ai.bdx.pop.dao;

import java.util.List;

import com.asiainfo.biframe.privilege.IUserRole;

public interface IMpmUserRoleMapDao {

	/**
	 * 用户角色关联关系查询指定用户的角色
	 * @param userId
	 * @return
	 */
	public abstract List<IUserRole> getRolesByUserId(String userId);

}
