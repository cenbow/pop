package com.ai.bdx.pop.listener;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;
import org.quartz.Scheduler;

import com.ai.bdx.pop.exception.PopException;
import com.ai.bdx.pop.kafka.CepReceiveThreadCache;
import com.ai.bdx.pop.kafka.util.KafKaConfigure;
import com.ai.bdx.pop.task.PopCacheTaskThread;
import com.ai.bdx.pop.util.PopConfigure;
import com.ai.bdx.pop.util.PopConstant;
import com.ai.bdx.pop.util.SimpleCache;
import com.ai.bdx.pop.util.SpringContext;
import com.ai.bdx.pop.util.ThreadPool;
import com.asiainfo.biframe.manager.context.ContextManager;
import com.asiainfo.biframe.servlet.SystemCommonListener;
import com.asiainfo.biframe.utils.config.Configure;

public class PopContextLoaderListener extends SystemCommonListener {
	private static Logger log = LogManager.getLogger();

	public void contextInitialized(ServletContextEvent event) {
		try {
			ServletContext servletContext = event.getServletContext();
			//log4j2初始化
			String log4jPath = servletContext.getRealPath("/WEB-INF/classes/config/aibi_pop/log4j2.xml");
			System.setProperty("log4j.configurationFile", log4jPath);
			System.setProperty("-DLog4jContextSelector","org.apache.logging.log4j.core.async.AsyncLoggerContextSelector");//使用异步日志
			System.setProperty("log4j.skipJansi", "true");
			LoggerContext context = (LoggerContext) LogManager.getContext(false);
			context.reconfigure();
			//配置文件初始化
			String confFilePath1 = servletContext.getRealPath("/WEB-INF/classes/config/aibi_pop/pop.properties");
			this.loadProperties(confFilePath1);
			super.contextInitialized(event);
			SpringContext.init(event.getServletContext());
			
			Configure.getInstance().addConfFileName("ASIAINFO_PROPERTIES", confFilePath1);
			String confFilePath2 = servletContext
					.getRealPath("/WEB-INF/classes/config/aibi_pop/province/{PROVINCE}/{MODE}/pop.properties");
			PopConfigure.getInstance().setConfFileName(confFilePath2);
			//是否是WEBOS权限
			if ("false".equalsIgnoreCase(Configure.getInstance().getProperty("IS_SUITE_PRIVILEGE"))) {
				String confFilePath3 = servletContext
						.getRealPath("/WEB-INF/classes/config/aibi_privilegeService/privilege.properties");
				Configure.getInstance().addConfFileName("AIBI_PRIVILEGE_PROPERTIES", confFilePath3);
			}
 
			String producerFileName= servletContext.getRealPath("/WEB-INF/classes/config/aibi_pop/cep-kafka-producter.properties");
			String consumerFileName= servletContext.getRealPath("/WEB-INF/classes/config/aibi_pop/cep-kafka-consumer.properties");
			
			KafKaConfigure.getInstance().init(consumerFileName, producerFileName);
			
			
			String cacheOk = (String) SimpleCache.getInstance().get(PopConstant.CACHE_OK);
			if (null == cacheOk || "0".equals(cacheOk)) {
				SimpleCache.getInstance().put(PopConstant.CACHE_OK, "0");
			}
			Thread popCacheTaskThread = new PopCacheTaskThread();
			popCacheTaskThread.setName("启动时初始化线程(缓存数据)");
			popCacheTaskThread.start();
			
			CepReceiveThreadCache.getInstance().init();
		} catch (Exception ce) {
			log.error("初始化数据异常：", ce);
		}
	}

	private void loadProperties(String fileName) {
		try {
			Properties props = new Properties();
			File fileObj = new File(fileName);
			String absPathStr = fileObj.getAbsolutePath();
			log.debug("Load file:{}", absPathStr);
			if (!fileObj.exists()) {
				throw new Exception((new StringBuilder()).append("parameter file not found:").append(fileName)
						.append("\r\nAbsolute Path:").append(absPathStr).toString());
			} else {
				FileInputStream fis = new FileInputStream(fileName);
				props.load(fis);
				fis.close();
				System.getProperties().putAll(props);
			}
		} catch (Exception e) {
			throw new PopException(e);
		}
	}

	@Override
	public void contextDestroyed(ServletContextEvent event) {
		try {
			super.contextDestroyed(event);
			ContextManager context = new ContextManager();
			context.finalizeComponents(event);
			ThreadPool.getInstance().shutdownNow();
			this.shutdownJob("jobTriggerFactory");
		} catch (Exception e) {
			log.error("destroy resource error:", e);
		}
	}

	private void shutdownJob(String jobName) {
		try {
			Scheduler job = SpringContext.getBean(jobName, Scheduler.class);
			if (job != null && job.isStarted()) {
				job.shutdown();
				Thread.sleep(1000);
			}
		} catch (Exception e) {
		}
	}
}
