package com.ai.bdx.pop.task.ftp;


import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.JobExecutionContext;

import com.ai.bdx.pop.util.ftp.ApacheFtpUtil;
import com.ai.bdx.pop.util.ftp.FtpCONST;
import com.ai.bdx.pop.util.ftp.FtpConfig;
import com.ai.bdx.pop.util.ftp.FtpConfigure;
import com.asiainfo.biframe.utils.string.StringUtil;

public class ScanFtpScheduleForBroadbandDownload {
	private static final Logger log = LogManager.getLogger();
	private FtpConfig ftpConfig;
	

    //initFtpConfig:初始化ftp配置信息
	public void initFtpConfig(JobExecutionContext context){
		ftpConfig = FtpConfigure.getInstance().getFtpConfigByTypes(FtpCONST.UPDATE_BROADBAND);
	}
	

	public void scanFtpTask(JobExecutionContext context) {
		initFtpConfig(context);
		ApacheFtpUtil apacheFtp = null;
		try {
			log.debug("宽带下载登陆ftp服务器:ip={}, port={},user={},passwd={},encode={}",ftpConfig.getFTP_ADDRESS(),ftpConfig.getFTP_PORT(), ftpConfig.getFTP_USER(),ftpConfig.getFTP_PASSWORD(), ftpConfig.getFTP_ENCODE());
			System.out.println("宽带下载登陆ftp服务器:ip={}, port={},user={},passwd={},encode={}"+ftpConfig.getFTP_ADDRESS()+ftpConfig.getFTP_PORT()+ ftpConfig.getFTP_USER()+ftpConfig.getFTP_PASSWORD()+ftpConfig.getFTP_ENCODE());
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
		System.out.println("开始下载宽带文件.........");
			// 判断处理文件策略
				for (String ftpFileName : remoteFileList) {
				 log.debug("FTP目录下匹配的文件名为:" + ftpFileName);
				 System.out.println("FTP目录下匹配的文件名为:" + ftpFileName);
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
				 //下载文件
				 boolean isDown = apacheFtp.download(localFilePath, ftpFileName,isDelRemote);
				 if(isDown){
					   
					 System.out.println("下载宽带文件"+ftpFileName+"。。。。成功");
				 }
				
			}
			System.out.println("下载宽带文件结束。。。。");
		}
	}
	
	
	
}
