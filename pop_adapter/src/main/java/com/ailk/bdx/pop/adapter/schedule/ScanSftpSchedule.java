package com.ailk.bdx.pop.adapter.schedule;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ailk.bdx.pop.adapter.bean.ScanPeriod;
import com.ailk.bdx.pop.adapter.bean.SftpConfig;
import com.ailk.bdx.pop.adapter.buffer.FileCacheBuffer;
import com.ailk.bdx.pop.adapter.cache.ReadedFileCache;
import com.ailk.bdx.pop.adapter.util.AdapterSftpUtil;
import com.ailk.bdx.pop.adapter.util.CompareUtil;
import com.ailk.bdx.pop.adapter.util.Configure;
import com.ailk.bdx.pop.adapter.util.StringUtil;
import com.ailk.bdx.pop.adapter.util.XmlConfigureUtil;
import com.google.common.base.Splitter;
import com.google.common.base.Stopwatch;
import com.google.common.collect.Lists;

/**
 * 
   * @ClassName: ScanSftpSchedule 
   * @Description: 周期性扫描SFTP目录调度任务
   * @author zhilj
   * @date 创建时间：2015-7-9
 */
public class ScanSftpSchedule {
	private static final Logger log = LogManager.getLogger();
	private final SftpConfig sftpConfig;
	private final ScheduledExecutorService scheduledExec;
	private List<String> suffixs;
	private static final String USE_FILE_POLICY = Configure.getInstance().getProperty("USE_FILE_POLICY");
	private ExecutorService taskExec;
	private static final Integer READ_FILE_THREAD_POOL_SIZE = Integer.parseInt(Configure.getInstance().getProperty("READ_FILE_THREAD_POOL_SIZE"));
	private static final String provice = Configure.getInstance().getProperty("PROVINCE");
		
	public ScanSftpSchedule(String sourceName){
		sftpConfig = (SftpConfig) XmlConfigureUtil.getInstance().getConfigItem(XmlConfigureUtil.SFTP, sourceName);
		suffixs = Lists.newArrayList(Splitter.on("|").split(sftpConfig.getFileSuffix()));
		scheduledExec = Executors.newSingleThreadScheduledExecutor();
		taskExec = Executors.newFixedThreadPool(READ_FILE_THREAD_POOL_SIZE);
	}
	
	public void start() {
		ScanPeriod sp = sftpConfig.getScanPeriod();
		scheduledExec.scheduleAtFixedRate(scanFtpTask(), 5, sp.getPeriod(),
				TimeUnit.valueOf(sp.getTimeUnit()));
	}

	private Runnable scanFtpTask() {
		return new Runnable() {
			public void run() {
				String currentDateFormat = new SimpleDateFormat(sftpConfig.getFileTimeformat()).format(new Date());
				log.debug("本轮SFTP文件读写时间周期：{}", currentDateFormat);
				if (Configure.getInstance().getProperty("isTest") != null&& "1".equals(Configure.getInstance().getProperty("isTest"))) {
					String tFileName = sftpConfig.getLocalPath()
							+ File.separator + "tmp.avl";
					new ReadFileThread(new File(tFileName), sftpConfig).start();
				} else {
					downRemoteFile();
				}
			}
		};
	}

	protected void downRemoteFile(){		
		AdapterSftpUtil sftpInstance = null;
		try{
			sftpInstance = new AdapterSftpUtil(sftpConfig.getSftpAddress(),sftpConfig.getSftpPort(),
					sftpConfig.getSftpUser(),sftpConfig.getSftpPassword(),sftpConfig.getTimeOutMills());
			String sftpPaht = sftpConfig.getSftpPath();
			//拼接匹配的文件名正则表达式
			String dateFormat = sftpConfig.getFileTimeformat().substring(0,sftpConfig.getFileTimeformat().length()-2);
			String oneHoursAgoTime = StringUtil.getOneHoursAgoTime(dateFormat);
			
			SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
			String nowTime = sdf.format(new Date());
			String datePattern = oneHoursAgoTime+"|"+nowTime;
			String patternStr = sftpConfig.getFileMatchField().replaceAll("DATE", datePattern);
			//sftp格式获取路径下的文件名
			Vector<String> fileVector = sftpInstance.listFileInDirByFormat(sftpPaht,patternStr);
			sftpInstance.changeDir(sftpPaht);
			//添加文件名排序方式
			if(sftpConfig.isPrefixMatch()){
				CompareUtil comp = new CompareUtil(sftpConfig.getPrefixMatchLen());
				Collections.sort(fileVector,comp);
			}else{
				Collections.sort(fileVector);
			}
			String minuteCycle = "";
			//判断处理文件策略
			for(String tempFileName:fileVector){
				String fileCacheString = tempFileName.substring(sftpConfig.getFilePrefix().length(),tempFileName.length()-sftpConfig.getFileSuffix().length());
				if(!ReadedFileCache.getInstance().isReaded(fileCacheString,sftpConfig.getFileTimeformat(),sftpConfig.getSftpFilePried()*1000)) {
					log.debug("SFTP目录下匹配的文件名为:"+tempFileName);
					String targetLocalFile = sftpConfig.getLocalPath()+tempFileName;
					if(!sftpConfig.getLocalPath().endsWith(File.separator)){
						targetLocalFile = sftpConfig.getLocalPath() + File.separator +tempFileName;
					}
					Stopwatch timeWatch = Stopwatch.createStarted();
					boolean isDown = sftpInstance.downloadFile(tempFileName,targetLocalFile);
					log.debug("下载文件{}耗时{}秒:",tempFileName,timeWatch.elapsed(TimeUnit.SECONDS));
					if(isDown){
						final String targetLocalFileTmp = targetLocalFile;
						if("1".equals(USE_FILE_POLICY)) {
							log.info("处理文件：{}",targetLocalFileTmp);
							taskExec.execute(new Thread() {
								public void run() {
									FileCacheBuffer.getInstance().putFile(new File(targetLocalFileTmp), sftpConfig);
								}
							});
						}else if("2".equals(USE_FILE_POLICY)){
							//截取文件名获取时间周期(分钟)
							String fileNameTmp= tempFileName.substring(sftpConfig.getFilePrefix().length());
							if("hubei".equals(provice)){
								//湖北文件格式 timeStr = 92271-20150411233800   targetKey=20150411233800   value=92271-20150411233800
								fileNameTmp = fileNameTmp.substring(fileNameTmp.length()-sftpConfig.getFileTimeformat().length(), fileNameTmp.length());
							}else if("shaanxi".equals(provice)){
								//陕西A口文件格式 timeStr = A9401320150708001500   targetKey=201507080015   value=A9401320150708001500
								fileNameTmp = fileNameTmp.substring(fileNameTmp.length()-sftpConfig.getFileTimeformat().length()-2, fileNameTmp.length()-2);
							}else{
								fileNameTmp = fileNameTmp.substring(0, sftpConfig.getFileTimeformat().length());
							}
							if("".equals(minuteCycle)){
								minuteCycle = fileNameTmp;
								log.info("当前时间批次{}", fileNameTmp);
							}else if(minuteCycle.equals(fileNameTmp)){
								//当前文件与上一个文件属于同一批次,暂时不做处理
							}else {
								//当前文件与上一个文件不属于同一批次,等待上一批次全部执行完
								taskExec.shutdown(); 
								while (true) {  
								     if (taskExec.isTerminated()) {  
								    	 log.info("当前时间批次{}", fileNameTmp);
								    	 minuteCycle = fileNameTmp;
								         break;  
								       } 
								     Thread.sleep(1000);  
								} 
								taskExec=Executors.newFixedThreadPool(READ_FILE_THREAD_POOL_SIZE);
							}
							Thread t = new ReadFileThread(new File(targetLocalFileTmp),sftpConfig);									 
							taskExec.execute(t);
						}else if("3".equals(USE_FILE_POLICY)){
							taskExec.execute(new ReadFileThread(new File(targetLocalFileTmp),sftpConfig));
						}
					}
				}
			}
		}catch(Exception e){
			log.error("downRemoteFileBySftp error:", e);
		}finally{
			if (sftpInstance != null) {
				try {
					sftpInstance.closeChannel();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		
	}

}
