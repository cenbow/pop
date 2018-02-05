package com.asiainfo.biapp.pop.util.ftp;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

public class PropUtil {
	private static Properties prop = new Properties(); 
	
	public static String getProp(String key,String fileName){
        try { 
//        File file=	new File(fileName);
//        if(!file.exists()){
//        	return "";
//        }
            prop.load(PropUtil.class.getResourceAsStream( fileName)); 
        } catch (IOException e) { 
            e.printStackTrace(); 
        }
        return (String) prop.get(key); 
	}
}
