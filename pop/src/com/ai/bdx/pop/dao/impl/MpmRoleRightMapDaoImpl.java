package com.ai.bdx.pop.dao.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.ai.bdx.pop.dao.IMpmRoleRightMapDao;
import com.ai.bdx.pop.model.privilege.LkgFunc;
import com.ai.bdx.pop.model.privilege.LkgFuncFolder;
import com.ai.bdx.pop.util.PopConfigure;
import com.asiainfo.biframe.exception.ServiceException;
import com.asiainfo.biframe.privilege.IMenuItem;
import com.asiainfo.biframe.privilege.IUserRight;
import com.asiainfo.biframe.privilege.sysmanage.model.Right;
import com.asiainfo.biframe.utils.database.jdbc.ConnectionEx;
import com.asiainfo.biframe.utils.database.jdbc.Sqlca;

public class MpmRoleRightMapDaoImpl extends HibernateDaoSupport implements IMpmRoleRightMapDao {

	private static Logger log = LogManager.getLogger();

	/**
	 * 获取所有指定菜单的所有上层Folder列表
	 * @param folderId
	 * @return
	 */
	private List<String> getAllFolderIds(Sqlca sqlca, Integer folderId) {
		List<String> folderIdList = new ArrayList<String>();
		try {
			String topMenuId = PopConfigure.getInstance().getProperty("TOP_MENU_ID");
			sqlca = new Sqlca(sqlca.getConnection());
			StringBuilder sql = new StringBuilder();
			sql.append("  WITH                                           ");
			sql.append("      tmptab                                     ");
			sql.append("      (                                          ");
			sql.append("          folder_id,                             ");
			sql.append("          folder_name,                           ");
			sql.append("          parent_id                              ");
			sql.append("      ) AS                                       ");
			sql.append("      (                                          ");
			sql.append("          SELECT                                 ");
			sql.append("              ctg.folder_id,                     ");
			sql.append("              ctg.folder_name,                   ");
			sql.append("              ctg.parent_id                      ");
			sql.append("          FROM                                   ");
			sql.append("              lkg_func_folder ctg                ");
			sql.append("          WHERE                                  ");
			sql.append("              ctg.folder_id = '" + folderId + "'           ");
			sql.append("          UNION ALL                              ");
			sql.append("          SELECT                                 ");
			sql.append("              sub.folder_id,                     ");
			sql.append("              sub.folder_name,                   ");
			sql.append("              sub.parent_id                      ");
			sql.append("          FROM                                   ");
			sql.append("              lkg_func_folder sub ,              ");
			sql.append("              tmptab super                       ");
			sql.append("          WHERE                                  ");
			sql.append("              sub.folder_id = super.parent_id    ");
			sql.append("      )                                          ");
			sql.append("  SELECT                                         ");
			sql.append("      *                                          ");
			sql.append("  FROM                                           ");
			sql.append("      tmptab                                     ");
			sql.append("  WHERE                                          ");
			sql.append("      folder_id != '" + topMenuId + "'                          ");
			sqlca.execute(sql.toString());

			while (sqlca.next()) {
				folderIdList.add(sqlca.getString("folder_id"));
			}
			if (null != sqlca) {
				sqlca.close();
			}
		} catch (Exception ex) {
			ex.printStackTrace();

			if (null != sqlca) {
				sqlca.close();
			}
		} finally {
			if (null != sqlca) {
				sqlca.close();
			}
		}
		return folderIdList;
	}

	/**
	 * 获取所有Folder集合，必须配合Id使用
	 * @param sets
	 * @return
	 */
	private List<IMenuItem> getAllfolders(Set<String> sets) {
		List<IMenuItem> folderList = new ArrayList<IMenuItem>();
		for (String str : sets) {
			IMenuItem menuItem = (IMenuItem) getHibernateTemplate().get(LkgFuncFolder.class, str);
			folderList.add(menuItem);
		}
		return folderList;
	}

	public List<IUserRight> getRightsByRoles(List roleIdList) {
		log.debug(" in getRightsByRoles(List roleIdList)...");
		String topMenuId = PopConfigure.getInstance().getProperty("TOP_MENU_ID");
		List<IUserRight> result = new ArrayList<IUserRight>();
		// 查询用户拥有的菜单节点List
		StringBuilder sql = new StringBuilder();
		sql.append("      SELECT F                                                                 ");
		sql.append("        FROM LkgFunc F                                                         ");
		sql.append("       WHERE EXISTS (SELECT 1                                                  ");
		sql.append("                FROM LkgJobFunc JF                                             ");
		sql.append("               WHERE F.funcId = JF.id.funcId                                   ");
		sql.append("                 AND JF.id.jobId IN (" + list2String(roleIdList) + "))             ");
		sql.append("      AND F.funcId like '" + topMenuId + "%'                                                  ");
		sql.append("      ORDER BY F.folderId,F.remark                                             ");
		List<IMenuItem> funcList = getHibernateTemplate().find(sql.toString());
		Set<String> folderIdsSet = new HashSet<String>();

		// 查询用户拥有的所有Folder Id集合
		if (funcList != null && !funcList.isEmpty()) {
			Sqlca sqlca = null;
			try {
				sqlca = new Sqlca(new ConnectionEx());
				for (IMenuItem func : funcList) {
					Integer folder_id = func.getParentId(); // 获取每个菜单的的folderID
					List<String> folderIds = getAllFolderIds(sqlca, folder_id);
					for (String folderId : folderIds) {
						folderIdsSet.add(folderId);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				throw new ServiceException(e.getMessage());
			} finally {
				if (sqlca != null) {
					sqlca.closeAll();
				}
			}
		}

		funcList.addAll(getAllfolders(folderIdsSet));

		if (funcList != null) {
			for (IMenuItem func : funcList) {
				Right right = new Right();
				right.setRoleType(1); //RoleType
				right.setResourceId(func.getMenuItemId().toString()); //菜单ID
				right.setResourceName(func.getMenuItemTitle()); //菜单名
				right.setParentId(func.getParentId().toString()); //父菜单ID
				right.setRightId(func.getUrl()); // 设置URL
				right.setOperationType("-1"); //OPERATION_KEY
				right.setOperationName(func.getMenuItemTitle()); //菜单名
				right.setHasCheckFrame(false);// CheckFrameField
				right.setResourceType(func.getParentId()); //ResourceType
				right.setTopId("0"); //TopId=0

				result.add(right);
			}
		}
		return result;
	}

	public List<IUserRight> getMenuItems() {
		log.debug(" in getMenuItems()...");
		List<IUserRight> result = new ArrayList<IUserRight>();
		String topMenuId = PopConfigure.getInstance().getProperty("TOP_MENU_ID");
		StringBuilder sql = new StringBuilder();
		sql.append("      SELECT F                                                               ");
		sql.append("      FROM LkgFunc F                                                         ");
		sql.append("      WHERE F.funcId like '" + topMenuId + "%'                                            ");
		sql.append("      ORDER BY F.folderId,F.remark                                           ");

		List<IMenuItem> funcList = getHibernateTemplate().find(sql.toString());
		Set<String> folderIdsSet = new HashSet<String>();

		// 查询用户拥有的所有Folder Id集合
		if (funcList != null && !funcList.isEmpty()) {
			Sqlca sqlca = null;
			try {
				sqlca = new Sqlca(new ConnectionEx());
				for (IMenuItem func : funcList) {
					Integer folder_id = func.getParentId(); // 获取每个菜单的的folderID
					List<String> folderIds = getAllFolderIds(sqlca, folder_id);
					for (String folderId : folderIds) {
						folderIdsSet.add(folderId);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				throw new ServiceException(e.getMessage());
			} finally {
				if (sqlca != null) {
					sqlca.closeAll();
				}
			}
		}

		funcList.addAll(getAllfolders(folderIdsSet));

		if (funcList != null) {
			for (IMenuItem func : funcList) {
				Right right = new Right();
				right.setRoleType(1); //RoleType
				right.setResourceId(func.getMenuItemId().toString()); //菜单ID
				right.setResourceName(func.getMenuItemTitle()); //菜单名
				right.setParentId(func.getParentId().toString()); //父菜单ID
				right.setRightId(func.getUrl()); // 设置URL
				right.setOperationType("-1"); //OPERATION_KEY
				right.setOperationName(func.getMenuItemTitle()); //菜单名
				right.setHasCheckFrame(false);// CheckFrameField
				right.setResourceType(func.getParentId()); //ResourceType
				right.setTopId("0"); //TopId=0

				result.add(right);
			}
		}
		return result;
	}

	private String list2String(List<String> strList) {
		StringBuilder b = new StringBuilder();
		if (strList != null && !strList.isEmpty()) {
			for (String str : strList) {
				b.append("'").append(str).append("',");
			}
			return b.substring(0, b.length() - 1);
		}
		return null;
	}

	public IMenuItem getMenuItemById(String memuItemId) {
		return (IMenuItem) getHibernateTemplate().get(LkgFunc.class, memuItemId);
	}

	public List getAllMenu() {
		String topMenuId = PopConfigure.getInstance().getProperty("TOP_MENU_ID");

		StringBuilder sql = new StringBuilder();
		sql.append("      SELECT F                                                               ");
		sql.append("      FROM LkgFunc F                                                         ");
		sql.append("      WHERE F.funcId like '" + topMenuId + "%'                                            ");
		sql.append("      ORDER BY F.folderId,F.remark                                           ");

		List<LkgFunc> funcList = getHibernateTemplate().find(sql.toString());

		return funcList;
	}

}
