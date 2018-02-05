/**
 * aibi-component-ds
 * ConfigUtil.java
 * com.asiainfo.biapp.ds.util
 * TODO
 * jianghui
 * 上午11:36:17
 */
package com.ai.bdx.pop.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigDataUtil {
	private static Properties redisProp = new Properties();
	static
	{
		InputStream redisInputStream = 
				ConfigDataUtil.class.getClassLoader()
				.getResourceAsStream("config"+File.separator+"aibi_pop"+
				File.separator+"redis.properties");   
		try
		{   
	        redisProp.load(redisInputStream);
	    }catch (IOException e1) {   
	    	e1.printStackTrace();   
	   }
	}
	/**
	 * 根据属性名称从配置文件redis.properties中获取值
	 * @param propName
	 * @return
	 */
	public static String getRedisPropVal(String propName){
	    return redisProp.getProperty(propName);
	}
}
