package com.ai.bdx.pop.dao.impl;

import java.util.List;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.ai.bdx.pop.dao.IMpmGroupRoleMapDao;
import com.asiainfo.biframe.privilege.IUserRole;

public class MpmGroupRoleMapDaoImpl extends HibernateDaoSupport implements IMpmGroupRoleMapDao {

	public List<IUserRole> findRoleListByGroupId(String groupId) {
		//		String hql = "select ur from GroupRoleMap urm,UserRole ur where urm.roleId=ur.roleId and  urm.groupId = '" + groupId + "'" + " order by ur.roleName";
		//
		//	    List grmap = getHibernateTemplate().find(hql);
		//	    return grmap;

		//因为缺少组与角色对应表，所以直接返回空
		return null;
	}

}
