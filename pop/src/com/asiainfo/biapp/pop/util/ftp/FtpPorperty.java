package com.asiainfo.biapp.pop.util.ftp;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class FtpPorperty {
	private static Properties pro=new Properties();
	static {
		InputStream is = FtpPorperty.class.getResourceAsStream("/config/aibi_pop/ftp.properties");
		try {
			pro.load(is);
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			if(is!=null){
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	public static String getProperty(String key){
		String value = pro.getProperty(key);
		return value;
	}
}
