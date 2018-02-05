package com.ai.bdx.pop.util;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.asiainfo.biframe.utils.config.Configure;
import com.asiainfo.biframe.utils.string.StringUtil;

public class PopConfigure {

	private PopConfigure() {
	}

	public static PopConfigure getInstance() {
		return configure;
	}


	public void setConfFileName(String file) throws Exception {
		String province = Configure.getInstance().getProperty("PROVINCE");
		String mode = Configure.getInstance().getProperty("RUN_MODE");
		log.info("province------------------" + province);
		initProperties("POP_PROPERTIES", file, province, mode);
	}

	public String getProperty(String strKey) {
		try {
			return getProperty("POP_PROPERTIES", strKey);
		} catch (Exception e) {
			log.error("", e);
		}
		return null;
	}

	public String getProperty(String configType, String strKey) throws Exception {
		String province = Configure.getInstance().getProperty("PROVINCE");
		String mode = Configure.getInstance().getProperty("RUN_MODE");
		if (StringUtil.isEmpty(configType)) {
			throw new Exception("----Configure--err-------:configType is null");
		}
		try {
			if (configMap.get(configType) == null) {
				throw new Exception((new StringBuilder()).append("----Configure--err-------:configType[")
						.append(configType).append("]is not initialized").toString());
			}
			File defaultFileObj = new File((String) fileMap.get(configType + "_default"));
			File provinceFileObj = new File((String) fileMap.get(configType + "_" + province));
			if (defaultFileObj.lastModified() > ((Long) modifiedTimeMap.get(configType + "_default")).longValue()
					|| provinceFileObj.lastModified() > ((Long) modifiedTimeMap.get(configType + "_" + province))
							.longValue()) {
				initProperties(configType, (String) fileNameMap.get(configType), province, mode);
			}
			Properties properties = (Properties) configMap.get(configType);
			return StringUtil.obj2Str(properties.getProperty(strKey));
		} catch (Exception excep) {
			log.error("", excep);
		}
		return "";
	}

	private synchronized boolean initProperties(String configType, String file, String province, String mode)
			throws Exception {
		if (StringUtil.isEmpty(configType)) {
			throw new Exception("----Configure--err-------:configType is null");
		}
		if (StringUtil.isEmpty(file)) {
			throw new Exception("----Configure--err-------:fileName is null");
		}
		Properties properties = new Properties();
		String defaultFile = file.replace("{PROVINCE}", "default").replace("{MODE}", mode.toLowerCase());
		File defaultFileObj = new File(defaultFile);
		String defaultPath = defaultFileObj.getAbsolutePath();
		if (!defaultFileObj.exists()) {
			throw new Exception("Parameter file not found:\r\nAbsolute Path:" + defaultPath);
		} else {
			log.debug("Load default properties fileName:\r\n Absolute Path:" + defaultPath);
			FileInputStream fis = new FileInputStream(defaultFile);
			properties.load(fis);
			File privincePathObj = null;
			String provincePath = "";
			if (StringUtil.isNotEmpty(province)) { //把默认配置文件和各个省配置文件合并
				String provinceFile = file.replace("{PROVINCE}", province.toLowerCase()).replace("{MODE}",
						mode.toLowerCase());
				;
				privincePathObj = new File(provinceFile);
				if (privincePathObj.exists()) {
					provincePath = privincePathObj.getAbsolutePath();
					log.debug("Load province properties fileName:\r\n Absolute Path:" + provincePath);
					FileInputStream fisp = new FileInputStream(provinceFile);
					Properties propsProvince = new Properties();
					propsProvince.load(fisp);
					fisp.close();
					Set entries = propsProvince.entrySet();
					if (entries != null) {
						Iterator iterator = entries.iterator();
						while (iterator.hasNext()) {
							Map.Entry entry = (Map.Entry) iterator.next();
							Object key = entry.getKey();
							Object value = entry.getValue();
							if (!properties.containsKey(key.toString())) {
								properties.put(key, value);
							} else {
								properties.remove(key);
								properties.put(key, value);
							}
						}
					}
				}
			}
			fis.close();

			modifiedTimeMap.put(configType + "_default", Long.valueOf(defaultFileObj.lastModified()));
			modifiedTimeMap.put(configType + "_" + province, Long.valueOf(privincePathObj.lastModified()));
			fileMap.put(configType + "_" + province, provincePath);
			fileMap.put(configType + "_default", defaultPath);
			fileNameMap.put(configType, file);
			configMap.put(configType, properties);
			return true;
		}
	}

	public String getAbsPath() {
		return getAbsPath(DEFAULT_CONFIG_TYPE + "_default");
	}

	public String getAbsPath(String configType) {
		return (String) fileMap.get(configType + "_default");
	}

	private static final Logger log = LogManager.getLogger();
	private static PopConfigure configure = new PopConfigure();
	private static final String DEFAULT_CONFIG_TYPE = "POP_PROPERTIES";
	private static Map modifiedTimeMap = new HashMap();
	private static Map fileMap = new HashMap();
	private static Map fileNameMap = new HashMap();
	private static Map configMap = new HashMap();

}
