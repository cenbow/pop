package com.ai.bdx.pop.dao.impl;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.ai.bdx.pop.dao.IMpmUserGroupMapDao;
import com.asiainfo.biframe.privilege.IUser;
import com.asiainfo.biframe.privilege.IUserGroup;

public class MpmUserGroupMapDaoImpl extends HibernateDaoSupport implements IMpmUserGroupMapDao {
	private static Logger log = LogManager.getLogger();

	public List<IUser> findAllUserByGId(String gid) {
		log.debug("findAllUserByGId UserGroupMap groupid = " + gid);
		//		String hql = "select us from UserGroupMap ugm,User_User us where us.userid=ugm.userid and us.status=0 and ugm.groupId = '"
		//				+ gid + "'";
		StringBuffer hql = new StringBuffer();
		hql.append("select us from LkgStaff us where us.groupId = '").append(gid).append("'");
		//String hql = "select us from LkgStaff us where us.groupId = '" + gid + "'";

		List userList = getHibernateTemplate().find(hql.toString());
		return userList;
	}

	/**
	 * 得到一组用户对应的的所有用户组信息；
	 *
	 * @param uids
	 * @return
	 */
	public List<IUserGroup> getGroupByUserId(List<String> uids) {
		log.debug("findByUserId UserGroupMap userId=" + uids);
		List list = null;
		String uidsw = "";
		for (String uid : uids) {
			uidsw = uidsw + "'" + uid + "',";
		}
		uidsw = uidsw.substring(0, uidsw.length() - 1);
		//		String hql = "select ug from UserGroupMap ugm,User_Group ug where ugm.groupId=ug.groupid and ug.status=0 and ugm.userid in ("
		//				+ uidsw + ")";
		String hql = "select ugm from LkgUserGroup ugm,LkgStaff ug where ugm.groupId=ug.groupId and ug.staffId in ("
				+ uidsw + ")";

		list = getHibernateTemplate().find(hql);
		return list;
	}

	public List<IUserGroup> getGroupByUserId(String uid) {
		log.debug("findByUserId UserGroupMap userId=" + uid);
		List list = null;
		//		String hql = "select ug from UserGroupMap ugm,User_Group ug where ugm.groupId=ug.groupid and ug.status=0 and ugm.userid = '"
		//				+ uid + "'";
		String hql = "select ugm from LkgUserGroup ugm,LkgStaff ug where ugm.groupId=ug.groupId and ug.staffId = '"
				+ uid + "'";

		list = getHibernateTemplate().find(hql);
		return list;
	}

}
