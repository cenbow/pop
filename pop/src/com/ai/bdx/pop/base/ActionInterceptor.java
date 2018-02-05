package com.ai.bdx.pop.base;

import java.net.URLDecoder;
import java.net.URLEncoder;

import javax.servlet.http.HttpSession;

import org.apache.cxf.common.util.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ai.bdx.pop.util.Security;
import com.jfinal.aop.Interceptor;
import com.jfinal.core.ActionInvocation;
import com.jfinal.core.Controller;

public class ActionInterceptor implements Interceptor {
	private static final Logger log = LogManager.getLogger();

	@Override
	public void intercept(ActionInvocation ai) {
		long t1 = System.currentTimeMillis();
		log.info("Path:{} Cost Time:{} S.", ai.getActionKey(), (System.currentTimeMillis() - t1) / 1000.0);
		//拦截cpe的几个页面
		Controller ctrl = ai.getController();
		String token = ctrl.getSessionAttr("token_cpe");
		String sessuserid = ctrl.getSessionAttr("user");
		String reqUserId = ctrl.getPara("userId");
		try {
			//如果session中没有token说明是外部链接进来需要验证，boss用
			String url = ctrl.getRequest().getRequestURI();
			if(StringUtils.isEmpty(sessuserid))
			{
				boolean result = true;
				if(StringUtils.isEmpty(token)&&((url.indexOf("cpeSelect")>=0)||url.indexOf("cpeManager")>=0||url.indexOf("batchStationCutover")>=0))
				{
					result = false;
					String reqToken = ctrl.getPara("token");
					if(!StringUtils.isEmpty(reqUserId)&&!(StringUtils.isEmpty(reqToken)))
					{
						if(Security.decrypt(reqToken,Security.CPE_TOKEN_KEY).equals(reqUserId))
						{
							result = true;
							HttpSession  sess = ctrl.getSession(true);
							sess.setMaxInactiveInterval(4*60*60);
							sess.setAttribute("user", reqUserId);
							sess.setAttribute("token_cpe", token);
						}
					}
				}
				if(result)
				{
					ai.invoke();
				}
				else
				{
					ctrl.renderText("请求页面错误!");
				}
			}
			else
			{
				ai.invoke();
			}
		} catch (Exception e) {
			// TODO: handle exception
			ctrl.renderText("请求页面错误!");
		}
	}
	public static void main(String[] args) throws Exception{
	
	}
}
