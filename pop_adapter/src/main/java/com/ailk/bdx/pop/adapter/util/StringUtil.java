package com.ailk.bdx.pop.adapter.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.google.common.base.Strings;

/**
   * @ClassName: StringUtil 
   * @Description: 字符串工具类
   * @author zhilj
   * @date 创建时间：2015-7-11
 */
public class StringUtil {
		/**
		   * @Description: 如果字符串是null，则返回"",否则返回该字符串
		   * @param processStr 待处理字符串
		   * @Return: String
		 */
		public static String getSpecifyString(String processStr){
			
			String resultStr = Strings.isNullOrEmpty(processStr)?"":processStr;
			
			return resultStr;
		}
		/**
		   * @Description: 如果字符串是null或者字符串是impactStr，则返回"",否则返回该字符串
		   * @param processStr 待处理字符串
		   * @param impactStr  处理条件
		   * @Return: String
		 */
		public static String getSpecifyString(String processStr,String impactStr){
			
			String resultStr = "";
			
			if(Strings.isNullOrEmpty(processStr)||impactStr.equals(processStr)){
				return resultStr;
			}else{
				return processStr;
			}
		}
		/**
		   * @Description: 按照传入的时间格式，获取当前一个小时的时间
		   * @param dateFormat
		   * @Return: String
		 */
		public static String getOneHoursAgoTime (String dateFormat) {
			String oneHoursAgoTime =  "" ;
	        Calendar calendar = Calendar. getInstance ();
	        calendar.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY) - 1);
	        oneHoursAgoTime =  new  SimpleDateFormat(dateFormat).format(calendar.getTime());
	        return  oneHoursAgoTime;
		 }

}
