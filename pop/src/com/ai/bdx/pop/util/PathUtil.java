package com.ai.bdx.pop.util;

import java.io.File;
import java.net.URISyntaxException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
 * 资源路径工具类
 */
public class PathUtil {
	private static String appPath;
	private static final Logger log = LogManager.getLogger(PathUtil.class);
	/**
	 * 获取应用根路径（系统绝对路径）
	 * 
	 * @return
	 */
	public static String getWebRootPath() {
		if (appPath == null) {
			try {
				appPath = new File(PathUtil.class.getResource("/").toURI()).getParentFile().getParentFile().getPath();
			} catch (URISyntaxException e) {
				String msg = "获取系统应用根路径异常.";
				log.error(msg, e);
				throw new RuntimeException(msg, e);
			}
		}
		return appPath;
	}
     /**
      * 获取执行类路径
      * @return
      */
	public static String getClassPath(){
		String classPath=null;
		try {
			classPath = new File(PathUtil.class.getResource("/").toURI()).getPath().toString();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		return classPath;
	}
	
	public static void main(String args[]){
		log.debug("getWebRootPath:"+getWebRootPath());
		log.debug("getClassPath:"+getClassPath());
	}

}
