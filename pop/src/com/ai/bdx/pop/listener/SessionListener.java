package com.ai.bdx.pop.listener;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ai.bdx.frame.privilegeServiceExt.service.IUserPrivilegeCommonService;
import com.ai.bdx.pop.util.PopConstant;
import com.asiainfo.biframe.privilege.IUser;
import com.asiainfo.biframe.privilege.base.constants.UserManager;
import com.asiainfo.biframe.privilege.base.vo.PrivilegeUserSession;
import com.asiainfo.biframe.utils.database.jdbc.ConnectionEx;
import com.asiainfo.biframe.utils.database.jdbc.Sqlca;
import com.asiainfo.biframe.utils.i18n.LocaleUtil;
import com.asiainfo.biframe.utils.spring.SystemServiceLocator;
import com.asiainfo.biframe.utils.string.StringUtil;

/**
 * 陕西权限登录监听器,用于向session中添加登录对象
 * @author guoyj
 *
 */
public class SessionListener implements HttpSessionBindingListener, Serializable {
	private static Logger log = LogManager.getLogger();
	private static final long serialVersionUID = -1334939621828913344L;
	private IUser m_User = null;
	private String m_strLoginOAUser = "";
	private String m_strSessionID = null;
	private String m_strClientIP = null;
	private String m_strServerAddress = "";
	private String m_browser_version = "";

	public static final String LOGOUTTYPE_ZHUDONG = "1";
	public static final String LOGOUTTYPE_BEIDONG = "2";

	/**
	 * 获取请求IP地址
	 * @param request
	 * @return
	 */
	public static String getIpAddr(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if ((ip == null) || (ip.length() == 0) || ("unknown".equalsIgnoreCase(ip))) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if ((ip == null) || (ip.length() == 0) || ("unknown".equalsIgnoreCase(ip))) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if ((ip == null) || (ip.length() == 0) || ("unknown".equalsIgnoreCase(ip))) {
			ip = request.getRemoteAddr();
		}
		String[] ips = null;
		if (StringUtil.isNotEmpty(ip)) {
			ips = ip.split(",");
			return ips[0];
		}
		return ip;
	}

	/**
	 * 登录时初始化Class值域信息
	 * @param request
	 * @param strUserID
	 * @param strOaUser
	 * @return
	 * @throws Exception
	 */
	public static SessionListener login(HttpServletRequest request, String strUserID) throws Exception {
		SessionListener session = new SessionListener();
		session.m_strClientIP = getIpAddr(request);
		session.m_strSessionID = request.getSession().getId();
		session.m_browser_version = getBrowserVersion(request.getHeader("user-agent"));
		log.info("[SessionListener] login sessionid=====" + session.m_strSessionID);
		try {
			session.m_strServerAddress = UserManager.getHostAddress();
		} catch (Exception exceo) {
			session.m_strServerAddress = "127.0.0.1";
		}

		Sqlca sqlca = null;
		try {
			IUserPrivilegeCommonService userService = (IUserPrivilegeCommonService) SystemServiceLocator.getInstance()
					.getService("userPrivilegeCommonService");
			session.m_User = userService.getUser(strUserID);
		} catch (Exception excep) {
			excep.printStackTrace();
			throw excep;
		} finally {
			if (null != sqlca) {
				sqlca.closeAll();
			}
		}
		return session;
	}

	public String getUserID() throws Exception {
		return m_User.getUserid();
	}

	public IUser getUser() throws Exception {
		return m_User;
	}

	public String getUserName() throws Exception {
		return m_User.getUsername();
	}

	/**
	 * 获取用户组ID
	 * @return
	 * @throws Exception
	 */
	public String getUserRoles() throws Exception {
		return m_User.getGroupId();
	}

	/*
	 * 绑定事件
	 */
	public void valueBound(HttpSessionBindingEvent event) {
		//因陕西表中不存在登录历史记录表，所以该绑定时间直接写入新的session对象即可
		Sqlca sqlca = null;
		try {
			sqlca = new Sqlca(new ConnectionEx());
			sqlca.setAutoCommit(false);

			String oaUserId = "";
			PrivilegeUserSession sessionUser = new PrivilegeUserSession();
			sessionUser.setUser(m_User);
			sessionUser.setClientIp(m_strClientIP);
			sessionUser.setSessionId(m_strSessionID);
			sessionUser.setOaUserId(m_strLoginOAUser);
			sessionUser.setGroupId((m_User.getGroupId() == null ? "" : m_User.getGroupId()));
			IUserPrivilegeCommonService privilegeService = (IUserPrivilegeCommonService) SystemServiceLocator.getInstance()
					.getService("userPrivilegeCommonService");
			if (privilegeService.isAdminUser(m_User.getUserid())) {
				sessionUser.setGroupId(PopConstant.SYS_ADMIN_GROUP);
			} else {
				sessionUser.setGroupId("0");
			}

			event.getSession().setAttribute("biplatform_user", sessionUser);
			log.info("[SessionListener] --valueBound put PrivilegeUserSession into \"biplatform_user\"");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null != sqlca) {
				sqlca.closeAll();
			}
		}
	}

	/*
	 * 解绑事件
	 */
	public void valueUnbound(HttpSessionBindingEvent event) {
	}

	/**
	 * 获取浏览器版本
	 * @param user_agent
	 * @return
	 */
	public static String getBrowserVersion(String user_agent) {
		if (user_agent.indexOf("MSIE 6.0") > 0) {
			return "IE 6.0";
		}
		if (user_agent.indexOf("MSIE 8.0") > 0) {
			return "IE 8.0";
		}
		if (user_agent.indexOf("MSIE 7.0") > 0) {
			return "IE 7.0";
		}
		if (user_agent.indexOf("MSIE 5.5") > 0) {
			return "IE 5.5";
		}
		if (user_agent.indexOf("MSIE 5.01") > 0) {
			return "IE 5.01";
		}
		if (user_agent.indexOf("MSIE 5.0") > 0) {
			return "IE 5.0";
		}
		if (user_agent.indexOf("MSIE 4.0") > 0) {
			return "IE 4.0";
		}
		if (user_agent.indexOf("Firefox") > 0) {
			return "Firefox";
		}
		if (user_agent.indexOf("Netscape") > 0) {
			return "Netscape";
		}
		if (user_agent.indexOf("Opera") > 0) {
			return "Opera";
		}
		return "" + LocaleUtil.getLocaleMessage("privilegeService", "privilegeService.java.other") + "";
	}

	public String getM_strLoginOAUser() {
		return m_strLoginOAUser;
	}

	public void setM_strLoginOAUser(String mStrLoginOAUser) {
		m_strLoginOAUser = mStrLoginOAUser;
	}

}