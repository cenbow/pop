package com.ai.bdx.pop.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 获取属性文件pop.properties文件的配置（改自suite的Configure）
 * @author zhangyb5
 *
 */
public final class Configure {
	private static final Logger logger = LogManager.getLogger();
	private static Configure configure = new Configure();
	private static final String DEFAULT_CONFIG_TYPE = "ASIAINFO_PROPERTIES";
	private static final String CONFIG_FILE_PATH = "/config/aibi_pop/pop.properties";
	private static Map modifiedTimeMap = new HashMap();
	private static Map configMap = new HashMap();

	private Configure() {
	}

	public static Configure getInstance() {
		return configure;
	}

	public String getProperty(String strKey) {
		try {
			URL confFileUrl = getClass().getResource(CONFIG_FILE_PATH);
			String fileUrl="";
        	try {
    		   fileUrl = java.net.URLDecoder.decode(confFileUrl.getFile(),"utf-8");
    		} catch (UnsupportedEncodingException e1) {
    		}  
			File fileObj = new File(fileUrl);
			long lastModified = modifiedTimeMap.get(DEFAULT_CONFIG_TYPE) != null ? ((Long) modifiedTimeMap.get(DEFAULT_CONFIG_TYPE)).longValue() : 0L;
			if (fileObj.lastModified() > lastModified) {
				initProperties(DEFAULT_CONFIG_TYPE, fileUrl);
			}
			Properties properties = (Properties) configMap.get(DEFAULT_CONFIG_TYPE);
			return properties.getProperty(strKey);
		} catch (Exception excep) {
			logger.error("getProperty(" + strKey + ")", excep);
			excep.printStackTrace();
		}
		return "";
	}

	private synchronized boolean initProperties(String configType, String fileName) throws Exception {
		Properties props = new Properties();
		File fileObj = new File(fileName);
		String absPathStr = fileObj.getAbsolutePath();
		logger.debug((new StringBuilder()).append("fileName:").append(fileName).append("\r\n Absolute Path:").append(absPathStr).toString());
		if (!fileObj.exists()) {
			throw new Exception((new StringBuilder()).append("parameter file not found:").append(fileName)
					.append("\r\nAbsolute Path:").append(absPathStr).toString());
		} else {
			FileInputStream fis = new FileInputStream(fileName);
			props.load(fis);
			fis.close();
			modifiedTimeMap.put(configType, Long.valueOf(fileObj.lastModified()));
			configMap.put(configType, props);
			return true;
		}
	}
}