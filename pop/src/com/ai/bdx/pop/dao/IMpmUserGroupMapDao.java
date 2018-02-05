package com.ai.bdx.pop.dao;

import java.util.List;

import com.asiainfo.biframe.exception.ServiceException;
import com.asiainfo.biframe.privilege.IUser;
import com.asiainfo.biframe.privilege.IUserGroup;

public interface IMpmUserGroupMapDao {
	/**
	 * 得到该用户同组的所有用户列表；
	 *
	 * @param gid
	 * @return
	 * @throws ServiceException
	 */
	public abstract List<IUser> findAllUserByGId(String gid);

	/**
	 * 得到一组用户对应的的所有用户组信息；
	 *
	 * @param uids
	 * @return
	 */
	public abstract List<IUserGroup> getGroupByUserId(List<String> uids);

	/**
	 * 取得用户有权限的所有地市列表；
	 *
	 * @param userId
	 * @return
	 * @throws ServiceException
	 */
	public abstract List<IUserGroup> getGroupByUserId(String uid);
}
