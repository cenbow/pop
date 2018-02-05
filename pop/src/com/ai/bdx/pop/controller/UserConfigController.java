package com.ai.bdx.pop.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ai.bdx.frame.approval.util.TreeNode;
import com.ai.bdx.pop.base.PopController;
import com.ai.bdx.pop.suite.model.UserGroup;
import com.ai.bdx.pop.suite.model.UserRole;
import com.ai.bdx.pop.suite.model.UserUser;
import com.ai.bdx.pop.util.Configure;
import com.ai.bdx.pop.util.PopUtil;
import com.ai.bdx.pop.wsclient.util.StringUtil;
import com.asiainfo.biframe.utils.database.jdbc.ConnectionEx;
import com.asiainfo.biframe.utils.database.jdbc.Sqlca;
import com.asiainfo.biframe.utils.date.DateUtil;
import com.asiainfo.biframe.utils.string.DES;
import com.google.common.collect.Maps;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

public class UserConfigController extends PopController {
	private static final Logger log = LogManager.getLogger();

	
	
	// 密码管理
    public void pwdView() {
		render("manage/updatePwd.jsp");
	}
	
    //更新密码
    public void updatePWD() {
        String oldPwd = getPara("oldPwd");
        String newPwd = getPara("newPwd");
        String rePwd = getPara("rePwd");
        String user = (String)getSession().getAttribute("user");
        String msg = "";
        Sqlca sqlca = null;
        try {
          sqlca = new Sqlca(new ConnectionEx());
          String strUserPswd = "";
          String flag = Configure.getInstance().getProperty("IS_SUITE_PRIVILEGE");
          String sql = "select userid, pwd from user_user where userid=?";
          sqlca.execute(sql, new Object[] { user });
          if (!(sqlca.next())) {
            msg = "该用户不存在!"; 
          }

          String tpwd = sqlca.getString("pwd");
          if (!(DES.decrypt(tpwd).equals(oldPwd)))
          {
            msg = "原密码不正确！"; 
          }

          int result = sqlca.execute("update user_user set pwd=? where userid=?", new Object[] { DES.encrypt(newPwd), user });
          msg = (result > 0) ? "密码修改成功!" : "密码修改失败!";
        }
        catch (Exception e)
        {
          msg = "数据库错误!";
          e.printStackTrace();
        }
        finally
        {
          if (sqlca != null) {
            sqlca.close();
          }
        }
        getRequest().setAttribute("msg", msg);
        render("manage/updatePwd.jsp");
      }
    
	// 用户组管理
	public void userGroupView() {
		render("manage/userGroupList.jsp");
	}

	// 用户组列表
	public void listGroup() {
		int page = this.getParaToInt("page");
		int row = this.getParaToInt("rows");
		String gName = this.getPara("groupName");
		gName = StringUtils.isNotEmpty(gName) ? gName : "";
		UserGroup u = this.getModel(UserGroup.class);
		Page<UserGroup> p = u.dao().paginate(page, row, "select *",
				"from user_group where group_name like ?", "%" + gName + "%");
		Map map = new HashMap();
		map.put("rows", p.getList());
		map.put("total", p.getTotalRow());
		this.renderJson(map);
	}

	private void matchGroupName(Map<String, TreeNode> tm, TreeNode node,
			String searchTxt) {
		if (node.getText().contains(searchTxt)) {
			node.setMatch(true);
			String pId = node.getPid();
			// 置父节点标记
			while (!StringUtil.isEmpty(pId)) {
				TreeNode t = tm.get(pId);
				if (t == null) {
					break;
				}
				t.setMatch(true);
				pId = t.getPid();
			}
		}
		/*
		 * else //如果父节点有一个被标记为true,该节点也需要标记 { String pId = node.getPid();
		 * //置父节点标记 while(!StringUtil.isEmpty(pId)){ TreeNode t = tm.get(pId);
		 * if(t==null){break;} if(t.isMatch()&&tm.get(t.getPid())!=null){
		 * node.setMatch(true); break; } pId=t.getPid(); } }
		 */

	}

	// 获取用户组
	public void groupTree() {
		String searchText = this.getPara("searchText");
		UserGroup u = this.getModel(UserGroup.class);
		List<UserGroup> us = u.dao().findAll();
		List<TreeNode> ts = new ArrayList<TreeNode>();
		Map<String, TreeNode> tm = new HashMap<String, TreeNode>();
		for (UserGroup tmpu : us) {
			String gName = tmpu.getStr(UserGroup.COL_GROUP_NAME);
			TreeNode t = new TreeNode(tmpu.getStr(UserGroup.COL_GROUP_ID),
					tmpu.getStr(UserGroup.COL_PARENT_ID), gName);
			t.setMatch(false);
			t.setQtip(gName);
			ts.add(t);
			if (StringUtils.isNotEmpty(searchText)) {
				tm.put(t.getId(), t);
			}
		}
		// 去掉不含
		if (StringUtils.isNotEmpty(searchText)) {
			for (TreeNode t : ts) {
				if (!t.isMatch()) {
					matchGroupName(tm, t, searchText);
				}
			}
			List<TreeNode> ts1 = new ArrayList<TreeNode>();
			for (TreeNode t : ts) {
				if (t.isMatch()) {
					ts1.add(t);
				}
			}
			ts = ts1;
		}
		this.renderJson(ts);
	}

	// 保存用户组
	public void saveOrUpdateGroup() {
		String gName = this.getPara("gName");
		String pId = this.getPara("userGroup_id");
		String gId = this.getPara("gId");
		pId = StringUtils.isEmpty(pId) ? "0" : pId;
		String result = "";
		try {
			UserGroup u = this.getModel(UserGroup.class);
			if (StringUtil.isEmpty(gId)) {
				u.set(UserGroup.COL_GROUP_ID, PopUtil.generateId())
						.set(UserGroup.COL_GROUP_NAME, gName)
						.set(UserGroup.COL_PARENT_ID, pId)
						.set(UserGroup.COL_BEGIN_DATE, DateUtil.getToday())
						.set(UserGroup.COL_END_DATE, DateUtil.getToday())
						.set(UserGroup.COL_STATUS, 0).save();
			} else {
				UserGroup u1 = u.dao().findById(gId);
				u1.set(UserGroup.COL_GROUP_NAME, gName)
						.set(UserGroup.COL_PARENT_ID, pId).update();
			}
			result = "保存成功!";
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			result = "保存失败!";
		}
		this.renderJson(result);
	}

	// 删除
	public void deleteGroup() {
		String gId = getPara("gId");
		Map<String, Object> result = Maps.newHashMap();
		try {
			gId = "'" + StringUtils.replace(gId, ",", "','") + "'";
			Db.use("suite").update(String
					.format("delete from user_group where group_id in(%s) or parent_id in(%s)",
							gId, gId));
			result.put("msg", "删除用户组成功!");
		} catch (Exception e) {
			log.error("删除用户组发生异常", e);
			result.put("msg", "删除用户组发生异常");
		}
		this.renderJson(result);
	}

	// 角色管理
	public void userRoleView() {
		render("manage/userRoleList.jsp");
	}

	// 用户组列表
	public void listUserRole() {
		int page = this.getParaToInt("page");
		int row = this.getParaToInt("rows");
		String rName = this.getPara("roleName");
		rName = StringUtils.isNotEmpty(rName) ? rName : "";
		UserRole u = this.getModel(UserRole.class);
		Page<UserRole> p = u.dao().paginate(page, row, "select *",
				"from user_role where role_name like ?", "%" + rName + "%");
		Map map = new HashMap();
		map.put("rows", p.getList());
		map.put("total", p.getTotalRow());
		this.renderJson(map);
	}

	// 获取所有权限
	public void getPriTree() {
		String roleId = this.getPara("roleId");
		List<Record> rs = Db.use("suite")
				.find("select a.MENUITEMID,a.MENUITEMTITLE,a.parentid,b.OPERATORID from sys_menu_item a left join "
						+ " (select a.MENUITEMID,a.MENUITEMTITLE,b.OPERATORID "
						+ " from sys_menu_item a ,sys_menuitem_right b "
						+ " where a.MENUITEMID=b.RESOURCEID and OPERATORID=?) b"
						+ " on a.MENUITEMID = b.MENUITEMID where a.MENUITEMID like '919%'",
						roleId);
		List<TreeNode> ts = new ArrayList<TreeNode>();
		for (Record r : rs) {
			String name = r.getStr("menuitemtitle");
			TreeNode t = new TreeNode(r.getInt("menuitemid").toString(), r
					.getInt("parentid").toString(), name);
			t.setQtip(name);
			ts.add(t);
			if (StringUtil.isNotEmpty(r.getStr("OPERATORID"))) {
				t.setChecked(true);
			}
		}
		this.renderJson(ts);
	}

	// 保存角色和相应权限
	public void saveOrUpdateRole() {
		String rName = this.getPara("rName");
		String rId = this.getPara("roleId");
		String pIds = this.getPara("priIds");
		String result = "";
		try {
			UserRole u = this.getModel(UserRole.class);
			if (StringUtil.isEmpty(rId)) {
				rId = PopUtil.generateId();
				u.set(UserRole.COL_ROLE_ID, rId)
						.set(UserRole.COL_ROLE_NAME, rName)
						.set(UserRole.COL_RESOURCETYPE, "50")
						.set(UserRole.COL_ROLE_TYPE, "1")
						.set(UserRole.COL_USER_LIMIT, 0)
						.set(UserRole.COL_CREATE_TIME, DateUtil.getToday())
						.set(UserRole.COL_BEGIN_DATE, DateUtil.getToday())
						.set(UserRole.COL_END_DATE, DateUtil.getToday())
						.set(UserRole.COL_STATUS, 0).save();
			} else {
				UserRole u1 = u.dao().findById(rId);
				u1.set(UserRole.COL_ROLE_NAME, rName).update();
			}
			// 先删除权限
			String delSql = String.format(
					"delete from sys_menuitem_right  where OPERATORID = '%s'",
					rId);
			List<String> ss = new ArrayList<String>();
			ss.add(delSql);
			if (StringUtil.isNotEmpty(pIds)) {
				String[] tpIds = pIds.split(",");
				for (String is : tpIds) {
					String sql = String
							.format("insert into sys_menuitem_right values('%s',%s,%s,'%s',%s,%s)",
									rId, 0, 50, is, -1, 0);
					ss.add(sql);
				}
			}
			Db.use("suite").batch(ss, ss.size());
			result = "保存成功!";
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			result = "保存失败!";
		}
		this.renderJson(result);
	}

	// 删除角色
	public void deleteRole() {
		String rId = getPara("rId");
		Map<String, Object> result = Maps.newHashMap();
		try {
			rId = "'" + StringUtils.replace(rId, ",", "','") + "'";
			
			List<String> ss = new ArrayList<String>();
			ss.add(String.format("delete from user_role where role_id in(%s)",
					rId));
			ss.add(String.format(
					"delete from sys_menuitem_right where operatorid in(%s)",
					rId));
			Db.use("suite").batch(ss, ss.size());
			result.put("msg", "删除角色成功!");
		} catch (Exception e) {
			log.error("删除角色发生异常", e);
			result.put("msg", "删除角色发生异常");
		}
		this.renderJson(result);
	}

	// 用户管理
	public void userView() {
		render("manage/userList.jsp");
	}

	// 用户列表
	public void listUser() {
		int page = this.getParaToInt("page");
		int row = this.getParaToInt("rows");
		String uId = this.getPara("quserId");
		String uName = this.getPara("quserName");
		StringBuilder sb = new StringBuilder();
		if (StringUtil.isNotEmpty(uId)) {
			sb.append(String.format(" and userid like '%%%s%%'", uId));
		}
		if (StringUtil.isNotEmpty(uName)) {
			sb.append(String.format(" and username like '%%%s%%'", uName));
		}
		String sql = " from user_user where 1=1 " + sb.toString();
		UserUser u = this.getModel(UserUser.class);
		Page<UserUser> p = u.dao().paginate(page, row, "select *", sql);
		Map map = new HashMap();
		map.put("rows", p.getList());
		map.put("total", p.getTotalRow());
		this.renderJson(map);
	}

	// 获取所有角色
	public void getAllRoleTree() {
		String userId = this.getPara("userId");
		UserRole u = this.getModel(UserRole.class);
		try {
			List<UserRole> rs = u.dao().findAll();
			List<TreeNode> ts = new ArrayList<TreeNode>();
			Map<String,Object> um = new HashMap<String,Object>();
            if(StringUtil.isNotEmpty(userId))
            {
        		List<Record> us = Db.use("suite").find("select * from user_role_map where USERID ='"+userId+"'");
        		for(Record u1:us)
        		{
        			um.put(u1.getStr("role_id"), u1);
        		}
            }
			for (UserRole r : rs) {
				String name = r.getStr("role_name");
				TreeNode t = new TreeNode(r.getStr("role_id"), null, name);
				t.setQtip(name);
				if(um.containsKey(r.getStr("role_id")))
				{
					t.setChecked(true);
				}
				ts.add(t);
			}
			this.renderJson(ts);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

	}
    //获取用户详细信息
    public void getUserDetail()
    {
    	Map<String, Object> result = Maps.newHashMap();
    	String userid = this.getPara("uId");
    	UserUser u = this.getModel(UserUser.class);
    	try {
    		UserUser ud = u.dao().findById(userid);
			//获取用户组
			result.put("userGroup", Db.use("suite").findFirst("select a.*,b.GROUP_NAME from user_group_map a,user_group b where a.GROUP_ID=b.GROUP_ID and userid='"+userid+"'"));
			result.put("userRoles", Db.use("suite").find("select a.*,b.ROLE_NAME from user_role_map a,user_role b where a.role_id=b.role_id and userid='"+userid+"'"));
			result.put("user", ud);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
    	this.renderJson(result); 
    }
    
	// 保存角色和相应权限
	public void saveOrUpdateUser() {
		String userid = this.getPara("userid");
		String username = this.getPara("username");
		String mobile = this.getPara("mobile");
		String userGroup = this.getPara("userGroup_id");
		String userRoleIds = this.getPara("userRoleIds");
		String email = this.getPara("email");
		String uoperate = this.getPara("uoperate");
		String result = "";
		String pwd = "111111";
		try {
			UserUser u = this.getModel(UserUser.class);
			if ("add".equals(uoperate)) {
				if(u.dao().findById(userid)!=null)
				{
					throw new Exception("用户账号已经存在!");
				}
				u.set(UserUser.COL_USERID, userid)
						.set(UserUser.COL_USERNAME, username)
						.set(UserUser.COL_MOBILEPHONE, mobile)
						.set(UserUser.COL_EMAIL, email)
						.set(UserUser.COL_CREATETIME, DateUtil.getToday())
						.set(UserUser.COL_STATUS, 0)
						.set(UserUser.COL_BEGINDATE, DateUtil.getToday())
						.set(UserUser.COL_ENDDATE, DateUtil.getToday())
						.set(UserUser.COL_PWD, DES.encrypt(pwd)).save();
			} else {
				UserUser u1 = u.dao().findById(userid);
				u1.set(UserUser.COL_USERNAME, username)
						.set(UserUser.COL_MOBILEPHONE, mobile)
						.set(UserUser.COL_EMAIL, email).update();
			}
			List<String> ss = new ArrayList<String>();
			// 设置用户组
			ss.add(String.format(
					" delete from user_group_map where userid = '%s'", userid));
			ss.add(String.format(
					" insert into user_group_map values('%s','%s') ", userid,
					userGroup));
			// 设置角色,用户组为超级用户不用设置角色
			if(!"1".equals(userGroup))
			{
				if (StringUtil.isNotEmpty(userRoleIds)) {
					ss.add(String.format(" delete from user_role_map where userid = '%s'", userid));
					String[] ids = userRoleIds.split(",");
					for (String id : ids) {
						ss.add(String.format(
								" insert into user_role_map values('%s','%s') ",
								userid, id));
					}
				}
			}
			Db.use("suite").batch(ss, ss.size());
			result = "保存成功!";
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			result = "保存失败!"+e.getMessage();
		}
		this.renderJson(result);
	}

	// 删除用户
	public void deleteUser() {
		String userid = getPara("userid");
		Map<String, Object> result = Maps.newHashMap();
		try {
			userid = "'" + StringUtils.replace(userid, ",", "','") + "'";
			List<String> ss = new ArrayList<String>();
			ss.add(String.format("delete from user_group_map where userid in(%s)",
					userid));
			ss.add(String.format(
					"delete from user_role_map where userid in(%s)",
					userid));
			ss.add(String.format(
					"delete from user_user where userid in(%s)",
					userid));
			Db.use("suite").batch(ss, ss.size());
			result.put("msg", "删除用户成功!");
		} catch (Exception e) {
			log.error("删除用户发生异常", e);
			result.put("msg", "删除用户发生异常");
		}
		this.renderJson(result);
	}
	public static void main(String[] args) throws Exception {
		String s ="A730718C2D988AC9120987C0812A1A8B";
		System.out.println(DES.decrypt(s));
	}
}
