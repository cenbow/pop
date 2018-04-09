package com.ailk.bdx.pop.adapter.util;

import java.util.Comparator;
/**
 * 
   * @ClassName: CompareUtil 
   * @Description: 添加比较工具类，为了比较前缀不固定的，但又要那时间排序的字符串
   * @author zhilj
   * @date 创建时间：2015-8-7
 */
public class CompareUtil implements Comparator<String>{
	
	public static int prefixLen;
	
	public CompareUtil(int prefix){
		prefixLen = prefix;
	}
	
	@Override
	public int compare(String str1, String str2) {
		return str1.substring(prefixLen).compareTo(str2.substring(prefixLen));
		
	}
}
