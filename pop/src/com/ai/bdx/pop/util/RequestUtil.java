package com.ai.bdx.pop.util;

import javax.servlet.http.HttpServletRequest;

/**
 * Request参数获取工具类
 * @author lixiangqian
 *
 */
public class RequestUtil {
	
	/**
	 * 获取经过html转义后的参数
	 * @param request
	 * @param paramName
	 * @return 获取不到,返回null
	 */
	public static String getEscapedString(HttpServletRequest request, String paramName) {
		String value = request.getParameter(paramName);
		return org.apache.commons.lang.StringEscapeUtils.escapeHtml(value);
	}
	
	/**
	 * 获取经过html转义后的参数
	 * @param request
	 * @param paramName
	 * @return 获取不到,返回默认值defaultValue
	 */
	public static String getEscapedString(HttpServletRequest request, String paramName, String defaultValue) {
		String value = request.getParameter(paramName);
		if (value == null) {
			return defaultValue;
		}
		return org.apache.commons.lang.StringEscapeUtils.escapeHtml(value);
	}
	
	/**
	 * 获取参数
	 * @param request
	 * @param paramName
	 * @return 获取不到,返回null
	 */
	public static String getString(HttpServletRequest request, String paramName) {
		return request.getParameter(paramName);
	}
	
	/**
	 * 获取参数
	 * @param request
	 * @param paramName
	 * @return 获取不到,返回默认值defaultValue
	 */
	public static String getString(HttpServletRequest request, String paramName, String defaultValue) {
		String value = request.getParameter(paramName);
		if (value == null) {
			return defaultValue;
		}
		return value;
	}
}
