package com.ai.bdx.pop.util;

import java.beans.Introspector;
import java.util.Locale;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * 增强型Spring工具类.
 *
 * @author wangLei
 */
public final class SpringContext {

	/** The context. */
	private static ApplicationContext context;
	/** The log. */
	private static final Logger log = LogManager.getLogger();

	/** The instance. */
	private static SpringContext instance;

	/**
	 * Instantiates a new spring context.
	 */
	private SpringContext() {
	}

	/**
	 * Instantiates a new spring context.
	 *
	 * @param sContext the s context
	 */
	private SpringContext(ServletContext sContext) {
		try {
			if (context == null) {
				SpringContext.context = WebApplicationContextUtils.getWebApplicationContext(sContext);
				log.debug("--------->>SpringContext init successful by web...");
			}
		} catch (Exception e) {
			log.error("", e);
		}
	}

	public static ApplicationContext getCtx() {
		return context;
	}

	/**
	* 初始化方法.
	*
	* @param context the context
	* @return the spring context
	*/
	public static SpringContext init(ServletContext context) {
		if (instance == null) {
			instance = new SpringContext(context);
		}
		return instance;
	}

	/**
	* 初始化方法.
	*
	* @param SpringContext the context
	* @return the spring context
	*/
	public static SpringContext init(ApplicationContext context) {
		if (instance == null) {
			instance = new SpringContext();
			SpringContext.context = context;
		}
		return instance;
	}

	/**
	* 得到国际化信息.
	*
	* @param code the code
	* @param args the args
	* @param defaultMessage the default message
	* @param request the request
	* @return the message
	*/
	public static String getMessage(String code, Object[] args, HttpServletRequest request) {
		return getMessage(code, args, request.getLocale());
	}

	/**
	* 得到国际化信息.
	*
	* @param code the code
	* @param args the args
	* @param defaultMessage the default message
	* @param request the request
	* @return the message
	*/
	public static String getMessage(String code, Object[] args, Locale locale) {
		return context.getMessage(code, args, null, locale);
	}

	/**
	* 得到国际化信息.
	*
	* @param strKey the str key
	* @param request the request
	* @return the message
	*/
	public static String getMessage(String strKey, HttpServletRequest request) {
		return context.getMessage(strKey, null, request.getLocale());
	}

	/**
	 * 得到国际化信息.
	 *
	 * @param strKey
	 *            the str key
	 * @param request
	 *            the request
	 * @return the message
	 */
	public static String getMessage(String strKey, Locale local) {
		return context.getMessage(strKey, null, local);
	}

	/**
	* 得到国际化信息.
	*
	* @param strKey the str key
	* @param request the request
	* @return the message
	*/
	public static String getMessage(String strKey, String strLabel, HttpServletRequest request) {
		return getMessage(strKey, strLabel, request.getLocale());
	}

	/**
	* 得到国际化信息.
	*
	* @param strKey the str key
	* @param request the request
	* @return the message
	*/
	public static String getMessage(String strKey, String strLabel, Locale locale) {
		String strCName = context.getMessage(strKey, null, new Locale("zh", "CN"));
		if (!strCName.equals(strLabel)) {
			log.debug("(not error) wrong application properties key:" + strKey);
		}
		return context.getMessage(strKey, null, locale);
	}

	/**
	 * Gets the bean.
	 *
	 * @param <T> the < t>
	 * @param beanId the bean id
	 * @param clazz the clazz
	 * @return the bean
	 */
	public static <T> T getBean(String beanId, Class<T> clazz) {
		Object obj = context.getBean(beanId);

		if (obj == null) {
			return null;
		}
		try {
			return clazz.cast(obj);
		} catch (ClassCastException ex) {
			log.error("Class " + obj.getClass().toString() + " cannot cast to " + clazz.toString(), ex);
			throw ex;
		}
	}

	/**
	 * Gets the bean which just obey the rule
	 *
	 * @param <T> the < t>
	 * @param beanId the bean id
	 * @param clazz the clazz
	 * @return the bean
	 */
	public static <T> T getBean(Class<T> clazz) {
		Object obj;
		try {
			String strName = clazz.getName();
			int nLast = strName.lastIndexOf('.');
			strName = Introspector.decapitalize(strName.substring(nLast + 1));
			obj = context.getBean(strName);

			if (obj == null) {
				return null;
			}

			return clazz.cast(obj);
		} catch (Exception ex) {
			log.error("", ex);
		}

		return null;
	}
}
