package com.ai.bdx.pop.dao;

import java.util.List;

import com.asiainfo.biframe.privilege.IUserRole;

public interface IMpmGroupRoleMapDao {

	public abstract List<IUserRole> findRoleListByGroupId(String groupId);
}
