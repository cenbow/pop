package com.ailk.bdx.pop.adapter.schedule;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ailk.bdx.pop.adapter.bean.FileConfig;
import com.ailk.bdx.pop.adapter.bean.ScanPeriod;
import com.ailk.bdx.pop.adapter.buffer.FileCacheBuffer;
import com.ailk.bdx.pop.adapter.cache.ReadedFileCache;
import com.ailk.bdx.pop.adapter.util.Configure;
import com.ailk.bdx.pop.adapter.util.XmlConfigureUtil;
import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;

/**
 * 周期性扫描文件
 *
 * @author guoyj
 *
 */
public class ScanFileSchedule {
	private static final Logger log = LogManager.getLogger();
	private final FileConfig fileConfig;
	private final ScheduledExecutorService scheduledExec;
	private final String province;
	private ExecutorService taskExec;
	private List<String> suffixs;
	private static final String USE_FILE_POLICY = Configure.getInstance().getProperty("USE_FILE_POLICY");
	private static final Integer READ_FILE_THREAD_POOL_SIZE = Integer.parseInt(Configure.getInstance().getProperty("READ_FILE_THREAD_POOL_SIZE"));
	
	private static final String IS_TEST = Configure.getInstance().getProperty("IS_TEST");

	
	public ScanFileSchedule(String sourceName) {

		fileConfig = (FileConfig) XmlConfigureUtil.getInstance().getConfigItem(XmlConfigureUtil.FILE, sourceName);
		if(!Strings.isNullOrEmpty(fileConfig.getFileSuffix())){
			suffixs = Lists.newArrayList(Splitter.on("|").split(fileConfig.getFileSuffix()));
		}
		scheduledExec = Executors.newSingleThreadScheduledExecutor();
		province = Configure.getInstance().getProperty("PROVINCE");
		taskExec=Executors.newFixedThreadPool(READ_FILE_THREAD_POOL_SIZE);
	}

	private Runnable scanFileTask() {
		return new Runnable() {
			public void run() {
				String currentDateFormat = new SimpleDateFormat(fileConfig.getFileTimeformat()).format(new Date());
				log.info("本轮文件读写时间周期：{}", currentDateFormat);
				
				String path = fileConfig.getFileDirectory();
				if(!path.endsWith(File.separator)) {
					path += File.separator;
				}
				
				File dir = new File(path);
				
				List<String> fileNames = Arrays.asList(dir.list());
				Collections.sort(fileNames);
				
				//判断处理文件策略
				if("1".equals(USE_FILE_POLICY)) {
				//按文件名时间循序,一个一个发送
					for (String fileName : fileNames) {
						final String filePath = path + fileName;
							if (checkFileName(fileName)) {
								try {
								    if(!ReadedFileCache.getInstance().isReaded(fileName.substring(fileConfig.getFilePrefix().length()),fileConfig.getFileTimeformat(),fileConfig.getFilePried()*1000)) {
										    taskExec.execute(new Thread(fileName) {
												public void run() {
													FileCacheBuffer.getInstance().putFile(new File(filePath), fileConfig);
												}
									});
									  }else{
											log.info("文件:{},已读-不再处理..", fileName);	
								  }
								} catch (Exception e) {
									e.printStackTrace();
								}
							}else{
								log.info("文件名:{},不符合规范-不进行处理..", fileName);	
							}
					}
			    }else if("2".equals(USE_FILE_POLICY)){
			    //截取文件名获取时间,按分钟批次发送(保证当前分钟内数据全部发送完毕,再发送下一分钟数据)
			    	String minuteCycle = "";
                    for (String fileName : fileNames) {
						final String filePath = path + fileName;
							if (checkFileName(fileName)) {
								try {
								  if(!ReadedFileCache.getInstance().isReaded(fileName.substring(fileConfig.getFilePrefix().length(),fileName.length()-fileConfig.getFileSuffix().length()),fileConfig.getFileTimeformat(),fileConfig.getFilePried()*1000)) {
									//截取文件名获取时间周期(分钟)
									String fileNameTmp= fileName.substring(fileConfig.getFilePrefix().length());
									fileNameTmp = fileNameTmp.substring(0, fileConfig.getFileTimeformat().length());
									if("".equals(minuteCycle)){
										minuteCycle = fileNameTmp;
										log.info("当前时间批次{}", fileNameTmp);
									}else if(minuteCycle.equals(fileNameTmp)){
										//当前文件与上一个文件属于同一批次,暂时不做处理
									}else{
										//当前文件与上一个文件不属于同一批次,等待上一批次全部执行完
									   taskExec.shutdown();  
									   while (true) {  
									     if (taskExec.isTerminated()) {  
									    	 log.info("当前时间批次{}", fileNameTmp);
									    	 minuteCycle = fileNameTmp;
									         break;  
									       } 
									  //   log.info("上个时间批次{},未执行完成,等待..", minuteCycle);
									     Thread.sleep(500);  
									   } 
									   taskExec=Executors.newFixedThreadPool(READ_FILE_THREAD_POOL_SIZE);
									}
									Thread t = new ReadFileThread(new File(filePath),fileConfig);
							 
									taskExec.execute(t);
						 
								  }else{
										log.info("文件:{},已读-不再处理..", fileName);	
							}
								} catch (Exception e) {
									e.printStackTrace();
								}
							}else{
								log.info("文件名:{},不符合规范-不进行处理..", fileName);	
							}
					}
			    }else if("3".equals(USE_FILE_POLICY)){
			    //一次性同时发送多个文件数据(可能同时发送多个时间段的)
			    	for (String fileName : fileNames) {
						final String filePath = path + fileName;
							if (checkFileName(fileName)) {
								try {
								    if(!ReadedFileCache.getInstance().isReaded(fileName.substring(fileConfig.getFilePrefix().length()),fileConfig.getFileTimeformat(),fileConfig.getFilePried()*1000)) {
								    		log.info("读取文件:{}..", fileName);       
								    		taskExec.execute(new ReadFileThread(new File(filePath),fileConfig));
									  }else{
											log.info("文件:{},已读-不再处理..", fileName);	
								  }
								} catch (Exception e) {
									e.printStackTrace();
								}
							}else{
								log.info("文件名:{},不符合规范-不进行处理..", fileName);	
							}
					}
			    }
				
			}
		};
	}
	
	private boolean checkFileName(String fileName){
		if(!fileName.startsWith(fileConfig.getFilePrefix())) {
			return false;
		}
		if(suffixs !=null){
			for(String suffix : suffixs){
				if(fileName.endsWith(suffix)) {
					return true;
				}
			}
		}else{
			return true;
		}
		
		return false;
	}


	public void start() {
		ScanPeriod sp = fileConfig.getScanPeriod();
		try {
			//初始延迟10秒执行,让mina先创建.(1防止mina没创建就发送数据,2防止sp.getTimeUnit() 周期设置过大时初始化延迟问题)
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		scheduledExec.scheduleAtFixedRate(scanFileTask(), 0, sp.getPeriod(), TimeUnit.valueOf(sp.getTimeUnit()));
	
		//Thread t = new Thread(scanFileTask());
		//t.start();
	}

	public void shutdown() {
		scheduledExec.shutdown();
	}
}

