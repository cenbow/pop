package com.ai.bdx.pop.task.ftp;

import java.io.File;
import java.io.IOException;
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

import com.ai.bdx.pop.util.PopConfigure;
import com.ai.bdx.pop.util.ftp.ApacheFtpUtil;
import com.ai.bdx.pop.util.ftp.FtpCONST;
import com.ai.bdx.pop.util.ftp.FtpConfig;
import com.ai.bdx.pop.util.ftp.FtpConfigure;
import com.asiainfo.biframe.utils.string.StringUtil;
import com.google.common.base.Splitter;
import com.google.common.base.Stopwatch;
import com.google.common.collect.Lists;

public class ScanFtpSchedule {

	private static final Logger log = LogManager.getLogger();
	private FtpConfig ftpConfig;
	

    //initFtpConfig:初始化ftp配置信息
	public void initFtpConfig(){
		ftpConfig = FtpConfigure.getInstance().getFtpConfigByTypes(FtpCONST.FEEDBACKSTATUS);
	}
	
	private void scanFtpTask() {
		initFtpConfig();
		ApacheFtpUtil apacheFtp = null;
		try {
			log.debug("登陆ftp服务器:ip={}, port={},user={},passwd={},encode={}",ftpConfig.getFTP_ADDRESS(),ftpConfig.getFTP_PORT(), ftpConfig.getFTP_USER(),ftpConfig.getFTP_PASSWORD(), ftpConfig.getFTP_ENCODE());
			apacheFtp = ApacheFtpUtil.getInstance(ftpConfig.getFTP_ADDRESS(),Integer.parseInt(ftpConfig.getFTP_PORT()), ftpConfig.getFTP_USER(),ftpConfig.getFTP_PASSWORD(), ftpConfig.getFTP_ENCODE());
			String ftpPath = ftpConfig.getFTP_PATH();
			apacheFtp.changeDir(ftpPath);
			List<String> fileList = apacheFtp.listFile(ftpPath);//获取到要下载的文件列表
			downRemoteFile(fileList,apacheFtp);
		} catch (Exception e) {
			log.error("", e);
		} finally {
			if (apacheFtp != null) {
				apacheFtp.forceCloseConnection();
			}
		}
		
	}
	/**
	 * 下载远程文件
	 * @throws IOException 
	 */
	private void downRemoteFile(List<String> remoteFileList,ApacheFtpUtil apacheFtp) throws IOException {
		if(remoteFileList!=null&&remoteFileList.size()>0){
		Collections.sort(remoteFileList);
	
			// 判断处理文件策略
				for (String ftpFileName : remoteFileList) {
				 log.debug("FTP目录下匹配的文件名为:" + ftpFileName);
				 String LocalPath = ftpConfig.getLOCAL_PATH();
				 if(LocalPath!=null){//判断本地路径的文件夹是否存在，如果存在直接下载到该文件夹，否则直接创建文件夹
				    File dir=new File(LocalPath);
				    if(!dir.exists()){
				    	dir.mkdirs();
				    }
				 }
				 String localFilePath=LocalPath+ftpFileName;
				 if (!LocalPath.endsWith(File.separator)) {
					 localFilePath = LocalPath+ File.separator + ftpFileName;
					}
				 boolean isDelRemote=false;
				 boolean isDelLocal=false;
				 String is_del_remote=ftpConfig.getISDELETE_REMOTE();
				 String is_del_local=ftpConfig.getISDELETE_LOCAL();
				 if(StringUtil.isNotEmpty(is_del_remote)&&"1".equals(is_del_remote)){
					 isDelRemote=true;
				 }
				 if(StringUtil.isNotEmpty(is_del_local)&&"1".equals(is_del_local)){
					 isDelLocal=true;
				 }
				 boolean isDown = apacheFtp.download(localFilePath, ftpFileName,isDelRemote);
				 if(isDown){
					 final String targetLocalFile=localFilePath ;
					 FtpFileUtil readFile=new FtpFileUtil(targetLocalFile,isDelLocal,ftpConfig.getFTP_ENCODE());
					 readFile.readFileByLine();
				 }
				
			}
		}
	}

}
