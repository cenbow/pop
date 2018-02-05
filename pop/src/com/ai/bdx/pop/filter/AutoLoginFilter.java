package com.ai.bdx.pop.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ai.bdx.pop.listener.SessionListener;
import com.asiainfo.biframe.privilege.base.constants.UserManager;
import com.asiainfo.biframe.utils.string.DES;

/*
 * Created on 2005-10-28 16:19:57
 *
 * <p>Title: </p>
 * <p>Description: 处理从其他服务器转过来的模块请求服务，实现用户自动登录</p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: asiainfo.,Ltd</p>
 * @author weilin.wu  wuwl2@asiainfo.com
 * @version 1.0
 */
public class AutoLoginFilter implements Filter {
	private static Logger log = LogManager.getLogger();
	protected boolean autoLogin = false;
	private static String skips[] = null;

	public AutoLoginFilter() {
		super();
		// TODO Auto-generated constructor stub
	}

	public void init(FilterConfig arg0) throws ServletException {
		String value = arg0.getInitParameter("autoLogin");
		if (value != null && (value.equalsIgnoreCase("yes") || value.equalsIgnoreCase("true"))) {
			autoLogin = true;
		}
		String skip = arg0.getInitParameter("skip");
		if (skip != null) {
			skips = skip.split(";");
		}
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
			ServletException {

		HttpServletRequest hrequest = (HttpServletRequest) request;
		HttpServletResponse hresponse = (HttpServletResponse) response;

		if (this.needSkipUserLoginCheck(hrequest)) {
			chain.doFilter(request, response);
			return;
		}
		String userId = "";
		if (!autoLogin) {
			if (hrequest.getSession(false) != null
					&& hrequest.getSession().getAttribute(UserManager.ASIA_SESSION_NAME) != null) {
				try {
					userId = ((SessionListener) hrequest.getSession().getAttribute(UserManager.ASIA_SESSION_NAME))
							.getUserID();
				} catch (Exception e) {
					log.error("", e);
				}
			} else {
				String script = "<script language='javascript'>top.location.href='" + hrequest.getContextPath()
						+ "/'</script>";
				script = new String(script.getBytes("utf-8"), "iso8859-1");
				hresponse.getOutputStream().print(script);
				return;
			}
		} else {
			String url = hrequest.getRequestURL()
					+ ((hrequest.getQueryString() == null) ? "" : ("?" + hrequest.getQueryString()));
			log.debug("URL:{}", url);
			userId = hrequest.getParameter("userId");

			if (userId == null || userId.length() == 0 || "null".equals(userId)) {
				if (hrequest.getSession(false) != null
						&& hrequest.getSession().getAttribute(UserManager.ASIA_SESSION_NAME) != null) {
					try {
						userId = ((SessionListener) hrequest.getSession().getAttribute(UserManager.ASIA_SESSION_NAME))
								.getUserID();
					} catch (Exception e) {
						log.error("", e);
					}
				} else {
					String script = "<script language='javascript'>top.location.href='" + hrequest.getContextPath()
							+ "/'</script>";
					script = new String(script.getBytes("utf-8"), "iso8859-1");
					hresponse.getOutputStream().print(script);
					return;
				}
			} else {
				log.debug("----------------------------------- remote ip:" + request.getRemoteAddr());
				try {
					log.debug("before decrypt:" + userId);
					userId = DES.decrypt(userId);

					if (hrequest.getSession(false) == null) {
						log.info(">>Session has been invalidated!...create a new session!");
						hrequest.getSession().setAttribute(UserManager.ASIA_SESSION_NAME,
								SessionListener.login(hrequest, userId));
					} else {
						SessionListener mysession = (SessionListener) hrequest.getSession().getAttribute(
								UserManager.ASIA_SESSION_NAME);
						//用户请求可能是从其他服务器转过来的，如果带了userId参数，并且有值，则构造一个登录SessionListener
						if (mysession == null) {
							log.info(">>New user [" + userId + "] from" + hrequest.getRemoteAddr() + " login in...");
							hrequest.getSession().setAttribute(UserManager.ASIA_SESSION_NAME,
									SessionListener.login(hrequest, userId));
						} else if (mysession != null) {
							if (!mysession.getUserID().equalsIgnoreCase(userId)) {
								log.info(">>Request has a session, but a new user [" + userId + "] from"
										+ hrequest.getRemoteAddr() + " login in...");
								hrequest.getSession().setAttribute(UserManager.ASIA_SESSION_NAME,
										SessionListener.login(hrequest, userId));
							}
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		chain.doFilter(hrequest, hresponse);
	}

	private boolean isVisitMenu(String userId, HttpServletRequest request) {

		return true;
	}

	private boolean needSkipUserLoginCheck(HttpServletRequest r) {
		boolean result = false;
		String uri = r.getRequestURI();

		//过滤掉直接输入根路径 或者 根路径 + / 这些
		if (uri.equals(r.getContextPath()) || uri.equals(r.getContextPath() + "/")) {
			return true;
		}

		for (String s : skips) {
			if (uri.indexOf(s) > -1) {
				result = true;
				break;
			}
		}

		return result;
	}

	public void destroy() {
		autoLogin = false;
	}

}
