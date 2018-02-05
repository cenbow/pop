package com.ai.bdx.pop.dao.impl;

import java.util.List;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.ai.bdx.pop.dao.IMpmUserDeptDao;
import com.ai.bdx.pop.model.privilege.LkgStaffCompany;
import com.asiainfo.biframe.privilege.IUserCompany;

public class MpmUserDeptDaoImpl extends HibernateDaoSupport implements IMpmUserDeptDao {

	public List<IUserCompany> getDeptAll() throws Exception {
		//		String hql = " from User_Company uc where uc.status='0'";
		String hql = " from LkgStaffCompany uc where uc.status='0'";
		List list = getHibernateTemplate().find(hql);
		return list;
	}

	public IUserCompany getDeptById(String deptId) throws Exception {
		//		IUserCompany com = (IUserCompany)getHibernateTemplate().get(User_Company.class, new Integer(deptId));
		IUserCompany com = (IUserCompany) getHibernateTemplate().get(LkgStaffCompany.class, new Integer(deptId));
		return com;
	}

}
