/**
 * aibi-component-ds
 * ConfigUtil.java
 * com.asiainfo.biapp.ds.util
 * TODO
 * jianghui
 * 上午11:36:17
 */
package com.ailk.bdx.pop.adapter.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ProdPolicyIdUtil {
	private static Properties redisProp = new Properties();
	static
	{
		InputStream redisInputStream = ProdPolicyIdUtil.class.getResourceAsStream("/config/aibi_pop/policy.properties");   
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
	public static String getPolicyPropVal(String propName){
	    return redisProp.getProperty(propName);
	}
	
}
