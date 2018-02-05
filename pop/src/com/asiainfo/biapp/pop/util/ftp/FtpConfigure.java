package com.asiainfo.biapp.pop.util.ftp;

import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import com.asiainfo.biapp.pop.util.*;
import com.asiainfo.biframe.utils.config.Configure;
import com.asiainfo.biframe.utils.string.StringUtil;


public class FtpConfigure {
	private static final Logger log = LogManager.getLogger();
	private static FtpConfigure configure = new FtpConfigure();
	private Map<String, Long> modifiedTimeMap = new HashMap<String, Long>();
	private Map<String, FtpConfig> configMap = new HashMap<String, FtpConfig>();
	private String configFile = "";

	private FtpConfigure() {
//		configFile = "/config/aibi_mpm/province/{PROVICE}/ftp-config.xml";
		configFile = "/config/aibi_pop/province/default/dev/ftp-config.xml";
//		String provice="";
//	
//			try {
//				provice = Configure.getInstance().getProperty("PROVINCE");
//			} catch (Exception e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//	
//		System.out.println("省："+provice);
//		if (StringUtil.isNotEmpty(provice)) {
//			configFile = configFile.replace("{PROVICE}", provice.toLowerCase());
//			
//		} else {
//			configFile = configFile.replace("{PROVICE}", "default");
//		}
	}
	public static FtpConfigure getInstance() {
		return configure;
	}
	/**
	 * 获取某类型所有的FTP配置
	 * @param type
	 * @return
	 */
	public FtpConfig getFtpConfigByTypes(String type) {
		FtpConfig cfcbs = null;
		try {
	//		URL confFileUrl = getClass().getResource(configFile);
			URL confFileUrl = FtpConfigure.class.getResource(configFile);
			System.out.println("confFileUrl="+confFileUrl);
			String  jarFileUrl = java.net.URLDecoder.decode(confFileUrl.getFile(),"utf-8");
			File fileObj = new File(jarFileUrl);
			long lastModified = modifiedTimeMap.get(fileObj.getName()) != null ? (modifiedTimeMap.get(fileObj.getName())).longValue() : 0L;
			if (fileObj.lastModified() > lastModified) {
				initConfigs(jarFileUrl);
				modifiedTimeMap.put(fileObj.getName(), fileObj.lastModified());
			}
			for (String key : configMap.keySet()) {
				if(key.equalsIgnoreCase(type)){
					cfcbs=configMap.get(key);
				}
			}
		} catch (Exception excep) {
			log.error("getConfigBeanByTypes(" + type + ")", excep);
		}
		return cfcbs;
	}

	private synchronized boolean initConfigs(String fileName) throws Exception {
		File fileObj = new File(fileName);
		String absPathStr = fileObj.getAbsolutePath();
		log.debug("Load fileName:" + fileName + "\r\n Absolute Path:" + absPathStr);
		if (!fileObj.exists()) {
			throw new Exception("parameter file not found:" + fileName + "\r\nAbsolute Path:" + absPathStr);
		} else {
			SAXReader reader = new SAXReader();
			Document doc = reader.read(fileName);
			Element root = doc.getRootElement();
			for (Object ftp : root.elements("FTP")) {
				Element eftp = (Element) ftp;
				StringBuffer ftpxml = new StringBuffer(XmlBeanConverter.XML_HEADER).append("<ROOT>");
				for (Object subftp : eftp.elements()) {
					Element esubftp = (Element) subftp;
					ftpxml.append(esubftp.asXML());
				}
				ftpxml.append("</ROOT>");
				FtpConfig cfg = new FtpConfig();
				XmlBeanConverter.xmlToBean(ftpxml.toString(), cfg);
				configMap.put(eftp.attributeValue("type"), cfg);
			}
			return true;
		}
	}

}
