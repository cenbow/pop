package com.ai.bdx.pop.dao.impl;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.ai.bdx.pop.dao.IMpmUserRoleDao;
import com.asiainfo.biframe.privilege.IUserRole;

public class MpmUserRoleDaoImpl extends HibernateDaoSupport implements IMpmUserRoleDao {
	private static Logger log = LogManager.getLogger();

	public List<IUserRole> findAllRoles() {
		log.debug("get all UserRole list");

		//	    String sql = "from UserRole role where role.status=0 order by role.roleName ";
		String sql = "from LkgJob role order by role.jobName ";
		List list = getHibernateTemplate().find(sql);

		log.debug("list.size=" + list.size());
		return list;
	}

	public List<IUserRole> findRoleListByGroupId(String groupId) {
		//		String hql = "select ur from GroupRoleMap urm,UserRole ur where urm.roleId=ur.roleId and  urm.groupId = '" + groupId + "'" + " order by ur.roleName";
		//
		//	    List grmap = getHibernateTemplate().find(hql);
		//	    return grmap;
		return null;
	}

}
