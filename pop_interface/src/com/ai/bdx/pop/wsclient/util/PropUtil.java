package com.ai.bdx.pop.wsclient.util;

import java.io.IOException;
import java.util.Properties;

public class PropUtil {
	private static Properties prop = new Properties(); 
	
	public static String getProp(String key,String fileName){
        try { 
            prop.load(PropUtil.class.getClassLoader().getResourceAsStream( 
            		fileName)); 
        } catch (IOException e) { 
            e.printStackTrace(); 
        }
        return (String) prop.get(key); 
	}
}
