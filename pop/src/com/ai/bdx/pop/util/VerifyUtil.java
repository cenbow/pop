package com.ai.bdx.pop.util;

import java.util.List;

public class VerifyUtil {
	
	/**
	 * 判断数组是否为null或者长度等于0
	 * @param obj
	 * @return
	 */
	public static boolean isArrayNullOrEmpty(Object[] obj) {
		boolean flg = false;
		if (null == obj || obj.length == 0) {
			flg = true;
		}
		return flg;
	}
	
	 /**
	  * 判断一个字符串是不是在一个字符串数组内出现内
	  * @param strs
	  * @param str
	  * @return
	  */
    public static boolean isInArray(String[] strs,String str){
    	for(String s:strs){
    		if(s.equalsIgnoreCase(str)){
    			return true;
    		}
    	}
		return false;
    }
    
    /**
	 * 判断字符串是否为空. 如果字符串为null或者全为空格或者为“null”，都返回true.
	 * 
	 * @param pStr 要检查的字符串.
	 * @return boolean 值.
	 */
	public static boolean isBlankStr(String pStr) {
		return pStr == null || pStr.trim().length() == 0
				|| pStr.equalsIgnoreCase("null");
	}
	
	@SuppressWarnings("rawtypes")
	public static boolean isListNullOrEmpty(List list) {
		boolean flg = false;
		if (null == list || list.size() == 0) {
			flg = true;
		}
		return flg;
	}

}
