package com.ailk.bdx.pop.adapter.schedule;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ailk.bdx.pop.adapter.bean.FtpConfig;
import com.ailk.bdx.pop.adapter.bean.ScanPeriod;
import com.ailk.bdx.pop.adapter.buffer.FileCacheBuffer;
import com.ailk.bdx.pop.adapter.cache.ReadedFileCache;
import com.ailk.bdx.pop.adapter.util.ApacheFtpUtil;
import com.ailk.bdx.pop.adapter.util.Configure;
import com.ailk.bdx.pop.adapter.util.XmlConfigureUtil;
import com.google.common.base.Splitter;
import com.google.common.base.Stopwatch;
import com.google.common.collect.Lists;

/**
 * 周期性扫描FTP目录调度任务
 * 
 * 1、连接FTP 2、下载指定目录文件到本地，下载完成删除，断开FTP 3、读本地文件，读完删除 4、下一周期据继续
 * 
 * @author imp
 * 
 */
public class ScanFtpSchedule {
	private static final Logger log = LogManager.getLogger();
	private final FtpConfig ftpConfig;
	private final ScheduledExecutorService scheduledExec;
	private List<String> suffixs;
	
	/**
	 * 读文件策略
	 * 1:按文件名截取时间循序,一个一个发送 (优点:保证文件时间顺序)效率较低. 
	 * 2:截取文件名获取时间,按分钟批次发送(并发读取、发送当前分钟内的，保证数据全部发送完毕,再发送下一分钟数据)效率中 . 
	 * 3:一次性并发发送多个文件数据(缺点：可能同时发送多个时间段的)效率高
	 */
	private static final String USE_FILE_POLICY = Configure.getInstance().getProperty("USE_FILE_POLICY");
	
	private ExecutorService taskExec;
	
	/**
	 * 读文件线程池大小
	 */
	private static final Integer READ_FILE_THREAD_POOL_SIZE = Integer.parseInt(Configure.getInstance().getProperty("READ_FILE_THREAD_POOL_SIZE"));
	
	/**
	 * 该应用部署的省份
	 */
	private static final String provice = Configure.getInstance().getProperty("PROVINCE");

	public ScanFtpSchedule(String sourceName) {
		ftpConfig = (FtpConfig) XmlConfigureUtil.getInstance().getConfigItem(
				XmlConfigureUtil.FTP, sourceName);
		suffixs = Lists.newArrayList(Splitter.on("|").split(
				ftpConfig.getFileSuffix()));
		scheduledExec = Executors.newSingleThreadScheduledExecutor();
		taskExec = Executors.newFixedThreadPool(READ_FILE_THREAD_POOL_SIZE);
	}

	public void start() {
		ScanPeriod sp = ftpConfig.getScanPeriod();
		// scheduledExec.schedule(scanFileTask(), 5,
		// TimeUnit.valueOf(sp.getTimeUnit()));
		scheduledExec.scheduleAtFixedRate(scanFtpTask(), 5, sp.getPeriod(),
				TimeUnit.valueOf(sp.getTimeUnit()));
	}

	private Runnable scanFtpTask() {
		return new Runnable() {
			public void run() {
				String currentDateFormat = new SimpleDateFormat(
						ftpConfig.getFileTimeformat()).format(new Date());
				log.debug("本轮FTP文件读写时间周期：{}", currentDateFormat);
				if (Configure.getInstance().getProperty("isTest") != null&& "1".equals(Configure.getInstance().getProperty("isTest"))) {
					String tFileName = ftpConfig.getLocalPath()
							+ File.separator + "tmp.avl";
					new ReadFileThread(new File(tFileName), ftpConfig).start();
				} else {
					downRemoteFile();
				}
			}
		};
	}

	/**
	 * 下载远程文件
	 */
	private void downRemoteFile(){
		
		ApacheFtpUtil apacheFtp = null;
		try{
			apacheFtp = ApacheFtpUtil.getInstance(ftpConfig.getFtpAddress(), ftpConfig.getFtpPort(), ftpConfig.getFtpUser(), ftpConfig.getFtpPassword(), ftpConfig.getCharsetName());
			
			String ftpPath = ftpConfig.getFtpPath();
			
			String yyyy = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
			String MM = String.valueOf(Calendar.getInstance().get(Calendar.MONTH)+1);
			String dd = String.valueOf(Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
			if(MM.length() == 1){
				MM = "0"+MM;
			}
			if(dd.length() == 1){
				dd = "0"+dd;
			}
			ftpPath = ftpPath.replaceAll("\\{yyyy\\}", yyyy)
					.replaceAll("\\{MM\\}", MM)
					.replaceAll("\\{dd\\}", dd);
			List<String> fileList = apacheFtp.listFile(ftpPath);
			apacheFtp.changeDir(ftpPath);
			Collections.sort(fileList);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			String toady = sdf.format(new Date());
			//判断处理文件策略
			if("1".equals(USE_FILE_POLICY)) {
				for(String tempFileName:fileList){
					if(checkFileName(tempFileName)){
						//20140411_174014004.AVL 湖北截完即92271-20150411233800
						if(tempFileName.indexOf(toady) != -1){
						String fileCacheString = tempFileName.substring(ftpConfig.getFilePrefix().length(),tempFileName.length()-ftpConfig.getFileSuffix().length());
						if(!ReadedFileCache.getInstance().isReaded(fileCacheString,ftpConfig.getFileTimeformat(),ftpConfig.getFtpFilePried()*1000)) {
							log.debug("FTP目录下匹配的文件名为:"+tempFileName);
							String targetLocalFile = ftpConfig.getLocalPath() + tempFileName;
							if(!ftpConfig.getLocalPath().endsWith(File.separator)){
								targetLocalFile = ftpConfig.getLocalPath() + File.separator +tempFileName;
							}
							Stopwatch timeWatch = Stopwatch.createStarted();
							boolean isDown = apacheFtp.download(targetLocalFile, tempFileName , ftpConfig.isDeleteRemote());
							log.debug("下载文件{}耗时{}秒:",tempFileName,timeWatch.elapsed(TimeUnit.SECONDS));
							if(isDown){
								final String targetLocalFileTmp = targetLocalFile;
								log.info("处理文件：{}",targetLocalFileTmp);
									 taskExec.execute(new Thread() {
											public void run() {
												FileCacheBuffer.getInstance().putFile(new File(targetLocalFileTmp), ftpConfig);
											}
										});
							}
						}
					}
					}
				}
			}else if("2".equals(USE_FILE_POLICY)){
				String minuteCycle = "";
				for(String tempFileName:fileList){
					if(checkFileName(tempFileName)){
						if(tempFileName.indexOf(toady) != -1){
						//20140411_174014004.AVL
						String fileCacheString = tempFileName.substring(ftpConfig.getFilePrefix().length(),tempFileName.length()-ftpConfig.getFileSuffix().length());
						if(!ReadedFileCache.getInstance().isReaded(fileCacheString,ftpConfig.getFileTimeformat(),ftpConfig.getFtpFilePried()*1000)) {
							log.debug("FTP目录下匹配的文件名为:"+tempFileName);
							String targetLocalFile = ftpConfig.getLocalPath() + tempFileName;
							if(!ftpConfig.getLocalPath().endsWith(File.separator)){
								targetLocalFile = ftpConfig.getLocalPath() + File.separator +tempFileName;
							}
							Stopwatch timeWatch = Stopwatch.createStarted();
							boolean isDown = apacheFtp.download(targetLocalFile, tempFileName , ftpConfig.isDeleteRemote());
							log.debug("下载文件{}耗时{}秒:",tempFileName,timeWatch.elapsed(TimeUnit.SECONDS));
							if(isDown){
								final String targetLocalFileTmp = targetLocalFile;
								//截取文件名获取时间周期(分钟)
								String fileNameTmp= tempFileName.substring(ftpConfig.getFilePrefix().length());
								
								if("hubei".equals(provice)){
									//湖北文件格式 timeStr = 92271-20150411233800   targetKey=20150411233800   value=92271-20150411233800
									fileNameTmp = fileNameTmp.substring(fileNameTmp.length()-ftpConfig.getFileTimeformat().length(), fileNameTmp.length());
								}else{
									fileNameTmp = fileNameTmp.substring(0, ftpConfig.getFileTimeformat().length());
								}
								
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
								     Thread.sleep(1000);  
								   } 
								   taskExec=Executors.newFixedThreadPool(READ_FILE_THREAD_POOL_SIZE);
								}
								Thread t = new ReadFileThread(new File(targetLocalFileTmp),ftpConfig);
						 
								taskExec.execute(t);
							}
						}
					  }
					}
				}
			} else if ("3".equals(USE_FILE_POLICY)) {
				for (String tempFileName : fileList) {
					if (checkFileName(tempFileName)) {
						if (tempFileName.indexOf(toady) != -1) {
							/*
							 * 例如：DPI-LOG-20160512110706001.AVL 
							 * 计算文件的时间戳部分字符串
							 */
							String fileCacheString = tempFileName.substring(
									ftpConfig.getFilePrefix().length(),
									tempFileName.length()
											- ftpConfig.getFileSuffix()
													.length());
							if (!ReadedFileCache.getInstance().isReaded(
									fileCacheString,
									ftpConfig.getFileTimeformat(),
									ftpConfig.getFtpFilePried() * 1000)) {
								log.debug("FTP目录下匹配的文件名为:" + tempFileName);
								String targetLocalFile = ftpConfig
										.getLocalPath() + tempFileName;
								if (!ftpConfig.getLocalPath().endsWith(
										File.separator)) {
									targetLocalFile = ftpConfig.getLocalPath()
											+ File.separator + tempFileName;
								}
								Stopwatch timeWatch = Stopwatch.createStarted();
								boolean isDown = apacheFtp.download(
										targetLocalFile, tempFileName,
										ftpConfig.isDeleteRemote());
								log.debug("下载文件{}耗时{}秒:", tempFileName,
										timeWatch.elapsed(TimeUnit.SECONDS));
								if (isDown) {
									final String targetLocalFileTmp = targetLocalFile;
									taskExec.execute(new ReadFileThread(
											new File(targetLocalFileTmp),
											ftpConfig));
								}
							}
						}
					}
				}
			}
		}catch (Exception e) {
			log.error("",e);
		}finally{
			if(apacheFtp != null){
				apacheFtp.forceCloseConnection();
			}
		}
	}

	private boolean checkFileName(String fileName) {
		if (!fileName.startsWith(ftpConfig.getFilePrefix())) {
			return false;
		}
		for (String suffix : suffixs) {
			if (fileName.endsWith(suffix)) {
				return true;
			}
		}
		return false;
	}

}
