package com.ai.bdx.pop.base;

import java.io.File;
import java.io.UnsupportedEncodingException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.base.CaseFormat;
import com.jfinal.config.Routes;

/**
 * 自动路由实现，免配置，便捷
 * 
 * @author wanglei
 *
 */
public class AutoBindRoutes extends Routes {

	private static final Logger log = LogManager.getLogger();

	private static final String PACKAGE_PATH = "com/ai/bdx/pop/controller/";
	private static final String CONTROLLER_SUFFIX = "Controller";
	private static final String CLASS_SUFFIX = ".class";

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void config() {
		String url = this.getClass().getClassLoader().getResource("/").getPath() + PACKAGE_PATH;
		try {
			url = java.net.URLDecoder.decode(url, "utf-8");
		} catch (UnsupportedEncodingException e1) {
			log.error("", e1);
		}
		File file = new File(url);
		File[] arrayFile = file.listFiles();
		if (arrayFile.length > 0) {
			for (File fileIn : arrayFile) {
				if (fileIn.getName().endsWith(CONTROLLER_SUFFIX + CLASS_SUFFIX)) {
					try {
						String className = fileIn.getName().substring(0,
								fileIn.getName().length() - CLASS_SUFFIX.length());
						Class clazz = Class.forName(PACKAGE_PATH.replace("/", ".") + className);
						String org = className.substring(0, className.length() - CONTROLLER_SUFFIX.length());
						String controllerKey = "/" + CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_CAMEL, org);

						add(controllerKey, clazz);
						log.debug("注册路由:  " + controllerKey + " —> " + clazz.getSimpleName());
					} catch (ClassNotFoundException e) {
						log.error("", e);
					}
				}

			}
		}
	}

}
