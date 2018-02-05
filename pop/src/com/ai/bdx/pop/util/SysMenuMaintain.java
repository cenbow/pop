package com.ai.bdx.pop.util;

import java.util.ArrayList;
import java.util.List;

import com.asiainfo.biframe.utils.config.Configure;
import com.asiainfo.biframe.utils.database.jdbc.Sqlca;

/**
 * 系统菜单工具类
 *
 * @author guoyj
 *
 */
public class SysMenuMaintain {

	public static List<String> getSubMenuItem(Sqlca sqlca, String menuItemId, boolean isCascade) {
		List list = new ArrayList();
		try {
			sqlca = new Sqlca(sqlca.getConnection());
			//			String strSql = "select FUNC_ID from LKG_FUNC where FOLDER_ID='" + menuItemId + "'";
			String strSql = "SELECT FOLDER_ID FROM LKG_FUNC_FOLDER WHERE PARENT_ID=? ORDER BY FOLDER_ID";
			String isSuite = Configure.getInstance().getProperty("IS_SUITE_PRIVILEGE");
			if ("true".equalsIgnoreCase(isSuite)) {
				strSql = "SELECT MENUITEMID as FOLDER_ID FROM SYS_MENU_ITEM WHERE PARENTID= ?";
			}
			sqlca.execute(strSql, new String[] { menuItemId });
			List menuList = new ArrayList();
			while (sqlca.next()) {
				menuList.add(sqlca.getString("FOLDER_ID"));
			}
			list.addAll(menuList);
			sqlca.close();

			if (isCascade) {
				for (int i = 0; i < menuList.size(); i++) {
					list.addAll(getSubMenuItem(sqlca, (String) menuList.get(i), isCascade));
				}
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
		return list;
	}

}
