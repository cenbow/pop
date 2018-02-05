package com.ai.bdx.pop.dao.impl;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.ai.bdx.pop.dao.IMpmUserUserDao;
import com.ai.bdx.pop.model.privilege.LkgStaff;
import com.asiainfo.biframe.privilege.IUser;
import com.asiainfo.biframe.privilege.sysmanage.model.SearchCondition;
import com.asiainfo.biframe.utils.config.Configure;
import com.asiainfo.biframe.utils.string.DES;

public class MpmUserUserDaoImpl extends HibernateDaoSupport implements IMpmUserUserDao {

	private static Logger log = LogManager.getLogger();

	public List<IUser> listUsers() {
		//		String sql = "from User_User user where user.status=0 order by user.username";
		String sql = "from LkgStaff user order by user.staffName";
		List _users = getHibernateTemplate().find(sql);
		return _users;
	}

	public IUser findById(String id) {
		//		log.debug("getting User_User instance with id: " + id);
		//		if (id == null) {
		//			return null;
		//		}
		//		User_User instance = (User_User) getHibernateTemplate().get(User_User.class, id);
		//		if (null != instance) {
		//			log.debug("**** findById11:" + instance.getUserid() + " pwd:" + instance.getPwd());
		//			if (StringUtils.isNotBlank(instance.getPwd())) {
		//				try {
		//					instance.setDesPwd(DES.decrypt(instance.getPwd()));
		//				} catch (Exception e) {
		//					instance.setDesPwd("");
		//				}
		//			}
		//			log.debug("**** findById:" + instance.getUserid() + " pwd:" + instance.getPwd());
		//		}
		//		return instance;
		//log.debug("getting LkgStaff instance with id: " + id);
		if (id == null) {
			return null;
		}
		LkgStaff instance = (LkgStaff) getHibernateTemplate().get(LkgStaff.class, id);
		LkgStaff user = new LkgStaff();
		if (null != instance) {
			BeanUtils.copyProperties(instance, user);
			//log.debug("**** findById11:" + instance.getUserid() + " pwd:" + instance.getStaffPwd());
			if (StringUtils.isNotBlank(instance.getStaffPwd())) {
				try {
					user.setStaffPwd(DES.decrypt(instance.getStaffPwd()));
				} catch (Exception e) {
					//instance.setStaffPwd("");
					user.setStaffPwd(instance.getStaffPwd());
				}
			}
			if ("anhui".equalsIgnoreCase(Configure.getInstance().getProperty("PROVINCE"))) {
				List list = getHibernateTemplate().find("select eparchyCode from TdEparchyCityMcd where remark = ?",
						instance.getCityId());
				if (CollectionUtils.isNotEmpty(list)) {
					user.setCityId((String) list.get(0));
				}
			}
			//log.debug("**** findById:" + user.getUserid() + " pwd:" + user.getStaffPwd());
		}
		return user;
	}

	public List<IUser> findAll(SearchCondition condition) {
		log.debug("--------------in UserUserDaoImpl.findAll()..........");
		long userDaoTime1 = System.currentTimeMillis();

		String strCond = getConditionSql(condition);
		String groupIds = condition.getQueryGroupids();
		//	    String listSql = "from User_User as user" + strCond;
		StringBuffer listSql = new StringBuffer();
		listSql.append("from LkgStaff as user").append(strCond);
		//String listSql = "from LkgStaff as user" + strCond;
		if ((groupIds != null) && (groupIds.trim().length() > 0)) {
			//	      listSql = "select user from User_User user, UserGroupMap groupMap " + strCond;
			listSql.append("select user from LkgStaff user, UserGroupMap groupMap ").append(strCond);
			//			listSql = "select user from LkgStaff user, UserGroupMap groupMap " + strCond;
		}
		log.debug("--findAll:sql:" + listSql);

		long userDaoTime2 = System.currentTimeMillis();

		log.debug(new StringBuilder(64).append("userDaoTime2-userDaoTime1=").append(userDaoTime2 - userDaoTime1));
		List list = getHibernateTemplate().find(listSql.toString());
		log.debug("-----------------list.size=" + list.size());

		long userDaoTime3 = System.currentTimeMillis();
		log.debug(new StringBuilder(64).append("userDaoTime3-userDaoTime2=").append(userDaoTime3 - userDaoTime2));

		return list;
	}

	private String getConditionSql(SearchCondition condition) {
		String sql = " where 1=1";
		if ((condition.getQueryUserid() != null) && (condition.getQueryUserid().trim().length() > 0)) {
			//			sql = sql + " and user.userid like '%" + condition.getQueryUserid()
			//					+ "%'";
			sql = sql + " and user.staffId like '%" + condition.getQueryUserid() + "%'";
		}
		if ((condition.getQueryUsername() != null) && (condition.getQueryUsername().trim().length() > 0)) {
			//			sql = sql + " and user.username like '%"
			//					+ condition.getQueryUsername() + "%'";
			sql = sql + " and user.staffName like '%" + condition.getQueryUsername() + "%'";
		}
		if ((StringUtils.isNotBlank(condition.getQueryCityid()))
				&& (!"-1".equalsIgnoreCase(condition.getQueryCityid()))) {
			//			sql = sql + " and user.cityid='" + condition.getQueryCityid() + "'";
			sql = sql + " and user.cityId='" + condition.getQueryCityid() + "'";
		}

		if ((condition.getQueryDutyid() != null) && (condition.getQueryDutyid().intValue() > 0)) {
			//			sql = sql + " and user.dutyid=" + condition.getQueryDutyid() + "";
			sql = sql + " and user.dutyid=" + condition.getQueryDutyid() + "";
		}

		if (condition.getQueryDepartmentid() > 0) {
			//			sql = sql + " and user.departmentid=" + condition.getQueryDepartmentid();
			sql = sql + " and user.depId=" + condition.getQueryDepartmentid();
		}
		if (condition.getQueryStatus() != -1) {
			//			sql = sql + " and user.status=" + condition.getQueryStatus();
			sql = sql + " and user.status=" + condition.getQueryStatus();
		}
		if ((condition.getQueryUserids() != null) && (condition.getQueryUserids().trim().length() > 0)) {
			String sqlin = "";
			String roles = "";
			String[] strRolesArry = condition.getQueryUserids().split(",");
			int cnt = 1;
			if (strRolesArry.length > 1000) {
				int nDeep = 0;
				for (int i = 0; i < strRolesArry.length; i++) {
					roles = roles + strRolesArry[i] + ",";
					cnt++;
					if (cnt > 1000) {
						if (nDeep == 0) {
							sqlin = sqlin + " and (";
						}
						if (nDeep > 0) {
							sqlin = sqlin + " or ";
						}
						//						sqlin = sqlin + "  user.userid in ("
						//								+ roles.substring(0, roles.lastIndexOf(","))
						//								+ ") ";
						sqlin = sqlin + "  user.staffId in (" + roles.substring(0, roles.lastIndexOf(",")) + ") ";
						roles = "";
						cnt = 1;
						nDeep++;
					}
				}
				if (roles.length() > 0) {
					if (nDeep == 0) {
						sqlin = sqlin + " and (";
					}
					if (nDeep > 0) {
						sqlin = sqlin + " or ";
					}
					//					sqlin = sqlin + " user.userid in(" + roles.substring(0, roles.lastIndexOf(",")) + ") ";
					sqlin = sqlin + " user.staffId in(" + roles.substring(0, roles.lastIndexOf(",")) + ") ";
				}
				sqlin = sqlin + ")";
			} else {
				//				sqlin = " and user.userid in (" + condition.getQueryUserids() + ") ";
				sqlin = " and user.staffId in (" + condition.getQueryUserids() + ") ";
			}

			sql = sql + sqlin;
		}

		if ((condition.getQueryGroupids() != null) && (condition.getQueryGroupids().trim().length() > 0)) {
			String sqlin = "";
			String roles = "";
			String[] strRolesArry = condition.getQueryGroupids().split(",");
			int cnt = 1;
			if (strRolesArry.length > 1000) {
				int nDeep = 0;
				for (int i = 0; i < strRolesArry.length; i++) {
					roles = roles + strRolesArry[i] + ",";
					cnt++;
					if (cnt > 1000) {
						if (nDeep == 0) {
							sqlin = sqlin + " and (";
						}
						if (nDeep > 0) {
							sqlin = sqlin + " or ";
						}
						//						sqlin = sqlin + "  groupMap.groupId in ("
						//								+ roles.substring(0, roles.lastIndexOf(","))
						//								+ ") ";
						sqlin = sqlin + "  groupMap.groupId in (" + roles.substring(0, roles.lastIndexOf(",")) + ") ";
						roles = "";
						cnt = 1;
						nDeep++;
					}
				}
				if (roles.length() > 0) {
					if (nDeep == 0) {
						sqlin = sqlin + " and (";
					}
					if (nDeep > 0) {
						sqlin = sqlin + " or ";
					}
					//					sqlin = sqlin + " groupMap.groupId in("
					//							+ roles.substring(0, roles.lastIndexOf(",")) + ") ";
					sqlin = sqlin + " groupMap.groupId in(" + roles.substring(0, roles.lastIndexOf(",")) + ") ";
				}
				sqlin = sqlin + ")";
			} else {
				//				sqlin = " and groupMap.groupId in (" + condition.getQueryGroupids() + ") ";
				sqlin = " and groupMap.groupId in (" + condition.getQueryGroupids() + ") ";
			}
			//			sql = sql + " and user.userid=groupMap.userid " + sqlin;
			sql = sql + " and user.staffId=groupMap.userid " + sqlin;
		}

		if ((condition.getBeginDeleteTime() != null) && (condition.getEndDeleteTime() == null)) {
			//			sql = sql + " and user.deleteTime >= '"
			//					+ condition.getBeginDeleteTime() + "'";
			sql = sql + " and user.deleteTime >= '" + condition.getBeginDeleteTime() + "'";
		}
		if ((condition.getBeginDeleteTime() == null) && (condition.getEndDeleteTime() != null)) {
			//			sql = sql + " and user.deleteTime <= '"
			//					+ condition.getEndDeleteTime() + "'";
			sql = sql + " and user.deleteTime <= '" + condition.getEndDeleteTime() + "'";
		}

		if ((condition.getBeginDeleteTime() != null) && (condition.getEndDeleteTime() != null)) {
			//			sql = sql + " and user.deleteTime >= '"
			//					+ condition.getBeginDeleteTime() + "'";
			//			sql = sql + " and user.deleteTime <= '"
			//					+ condition.getEndDeleteTime() + "'";
			sql = sql + " and user.deleteTime >= '" + condition.getBeginDeleteTime() + "'";
			sql = sql + " and user.deleteTime <= '" + condition.getEndDeleteTime() + "'";
		}

		return sql;
	}

	public LkgStaff findEntityById(String id) {
		log.debug("getting LkgStaff instance with id: " + id);
		if (id == null) {
			return null;
		}
		LkgStaff instance = (LkgStaff) getHibernateTemplate().get(LkgStaff.class, id);
		return instance;
	}

}
