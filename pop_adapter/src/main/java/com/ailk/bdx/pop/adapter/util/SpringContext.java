package com.ailk.bdx.pop.adapter.util;

import java.beans.Introspector;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Spring工具类（改自CI的SpringContext）
 * @author zhangyb5
 *
 */
public final class SpringContext {

	/** The context. */
	private static AbstractApplicationContext context;
	/** The log. */
	private static final Logger logger = LogManager.getLogger();
	private static final String CONFIG_FILE_PATH = "/application-adapter-configure.xml";
	/** The instance. */
	private static SpringContext instance;

	static {
		instance = new SpringContext();
	}

	/**
	 * Instantiates a new spring context.
	 */
	private SpringContext() {
		try {
			logger.info("load spring configure file({}) start",CONFIG_FILE_PATH);
			context = new ClassPathXmlApplicationContext(CONFIG_FILE_PATH);
			logger.info("load spring configure file({}) end",CONFIG_FILE_PATH);
		} catch (Exception e) {
			logger.error("load spring configure file(" + CONFIG_FILE_PATH + ") fail,please check it!", e);
			e.printStackTrace();
		}
	}

	/**
	 * 获取Application Context实例
	 * @return
	 */
	public static ApplicationContext getSpringApplicationContext() {
		if (context == null) {
			instance = new SpringContext();
		}
		return instance.context;
	}

	/**
	 * 根据BeanId获取Bean实例
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
			logger.error("Class " + obj.getClass().toString() + " cannot cast to " + clazz.toString(), ex);
			throw ex;
		}
	}

	/**
	 * 按照特定规则（类名）获取Bean实例
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
			int nLast = strName.lastIndexOf(".");
			strName = Introspector.decapitalize(strName.substring(nLast + 1));
			obj = context.getBean(strName);
			if (obj == null) {
				return null;
			}
			return clazz.cast(obj);
		} catch (Exception ex) {
			logger.error("", ex);
			ex.printStackTrace();
		}

		return null;
	}

	/**
	 * 按照特定类型获取该类型的bean实例（单例），包括子类
	 *
	 * @param clazz the clazz
	 * @return the beans
	 */
	public static Map getBeans(Class clazz) {
		try {
			return context.getBeansOfType(clazz);
		} catch (Exception ex) {
			logger.error("", ex);
			ex.printStackTrace();
		}
		return null;
	}

	public static void destroy(){
		context.destroy();
	}
}
