package com.ai.bdx.pop.dao.impl;

import java.util.List;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.ai.bdx.pop.dao.IMpmUserRoleMapDao;
import com.asiainfo.biframe.privilege.IUserRole;

public class MpmUserRoleMapDaoImpl extends HibernateDaoSupport implements IMpmUserRoleMapDao {

	/**
	 * 用户角色关联关系查询指定用户的角色
	 * @param userId
	 * @return
	 */
	public List<IUserRole> getRolesByUserId(String userId) {
		StringBuffer sql = new StringBuffer();
		sql.append("select ur from LkgStaffJob urm,LkgJob ur where urm.id.jobId=ur.jobId and urm.id.staffId = '")
				.append(userId).append("'");
		//String sql = "select ur from LkgStaffJob urm,LkgJob ur where urm.id.jobId=ur.jobId and urm.id.staffId = '" + userId + "'";

		List urmap = getHibernateTemplate().find(sql.toString());
		return urmap;
	}

}
