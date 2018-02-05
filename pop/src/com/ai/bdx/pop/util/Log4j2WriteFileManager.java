package com.ai.bdx.pop.util;

import jodd.cache.TimedCache;

import org.apache.logging.log4j.core.Logger;

/**
 * 使用log4j2写文件工具类.
 */
public class Log4j2WriteFileManager {

	/** 缓存使用过的logger. */
	private static final TimedCache<String, Logger> cacheAppenderMap;

	static {
		/** 默认缓存24小时 */
		cacheAppenderMap = new TimedCache<String, Logger>(1000 * 60 * 60 * 24);
	}

	/**
	 * Gets the logger.
	 *
	 * @param fileAbsolutePath the file absolute path
	 * @param patternLayout the pattern layout
	 * @return the logger
	 */
	public static Logger getLogger(String fileAbsolutePath, String patternLayout) {
		Logger logger = cacheAppenderMap.get(fileAbsolutePath);
		if (logger == null) {
			logger = new Log4j2SimpleLogger(fileAbsolutePath, patternLayout);
			cacheAppenderMap.put(fileAbsolutePath, logger);
		}
		return logger;
	}

	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		try {
			Logger logger1 = Log4j2WriteFileManager.getLogger("c://test//test1.log", "%m%n");
			logger1.error("test11111");

			Logger logger2 = Log4j2WriteFileManager.getLogger("c://test//test2.log", "%d{yyyy-MM-dd HH:mm:ss} %m%n");
			logger2.debug("test22222");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}