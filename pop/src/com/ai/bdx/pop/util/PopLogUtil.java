package com.ai.bdx.pop.util;

import java.io.File;
import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;

import com.asiainfo.biframe.utils.config.Configure;
import com.asiainfo.biframe.utils.date.DateUtil;

/**
 * 日志工具
 * 1.日志文件路径在pop.properties中配置，默认部署目录
 * 2.日志文件名称动态传入（不带后缀），后缀统一为.log
 * @author zhangyb5
 *
 */
public class PopLogUtil {
	private static org.apache.logging.log4j.Logger log = LogManager.getLogger();

	public static void log(String filename, String msg) {
		try {
			String path = Configure.getInstance().getProperty("SYS_COMMON_LOG_PATH");
			String byDay = Configure.getInstance().getProperty("LOGFILE_BY_DAY");//默认按日分目录
			String date = "";
			String logFilePath = path;
			if (!logFilePath.endsWith(File.separator)) {
				logFilePath = path + File.separator;
			}
			logFilePath += "contactcontrollogs" + File.separator;
			if ("1".equals(byDay)) {
				date = DateUtil.date2String(new Date(), DateUtil.YYYYMMDD);
				logFilePath += date + File.separator + filename + ".log";
			} else {
				logFilePath += filename + ".log";
			}
			Logger logger = Log4j2WriteFileManager.getLogger(logFilePath, "%d{yyyy-MM-dd HH:mm:ss} %m%n");
			logger.info(msg);
		} catch (Exception e) {
			log.error("", e);
		}
	}
}
