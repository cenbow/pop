package com.ai.bdx.pop.task.ftp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.ai.bdx.pop.util.PopConstant;
import com.ai.bdx.pop.util.SpringContext;
import com.ai.bdx.pop.wsclient.impl.SendPccInfoServiceImpl;
import com.ailk.bdx.pop.adapter.jedis.JedisClientUtil;
import com.asiainfo.biapp.pop.model.CpeUserInfo;
import com.asiainfo.biapp.pop.util.bean.CpeInstallFile2Bean;
import com.asiainfo.biapp.pop.util.ftp.ApacheFtpUtil;
import com.asiainfo.biapp.pop.util.ftp.FtpCONST;
import com.asiainfo.biapp.pop.util.ftp.FtpConfig;
import com.asiainfo.biapp.pop.util.ftp.FtpConfigure;
import com.asiainfo.biapp.pop.util.ftp.PropUtil;
import com.asiainfo.biframe.utils.string.StringUtil;

public class ScanFtpScheduleForDeleteDownload {
	private static final Logger log = LogManager.getLogger();
	private FtpConfig ftpConfig;
	private static final ReentrantLock lock = new ReentrantLock();

    //initFtpConfig:初始化ftp配置信息
	public void initFtpConfig(){
		ftpConfig = FtpConfigure.getInstance().getFtpConfigByTypes(FtpCONST.DOWNLOAD_DELETE_CPEUSER);
	}
	

	public void scanFtpTask() {
//		log.info("=======>CPE销户远程下载定时任务");
		initFtpConfig();
//		ApacheFtpUtil apacheFtp = null;
//		try {
//			log.debug("销户下载登陆ftp服务器:ip={}, port={},user={},passwd={},encode={}",ftpConfig.getFTP_ADDRESS(),ftpConfig.getFTP_PORT(), ftpConfig.getFTP_USER(),ftpConfig.getFTP_PASSWORD(), ftpConfig.getFTP_ENCODE());
//		//	System.out.println("销户下载登陆ftp服务器:ip={}, port={},user={},passwd={},encode={}"+ftpConfig.getFTP_ADDRESS()+ftpConfig.getFTP_PORT()+ ftpConfig.getFTP_USER()+ftpConfig.getFTP_PASSWORD()+ftpConfig.getFTP_ENCODE());
//			apacheFtp = ApacheFtpUtil.getInstance(ftpConfig.getFTP_ADDRESS(),Integer.parseInt(ftpConfig.getFTP_PORT()), ftpConfig.getFTP_USER(),ftpConfig.getFTP_PASSWORD(), ftpConfig.getFTP_ENCODE());
//			String ftpPath = ftpConfig.getFTP_PATH();
//			apacheFtp.changeDir(ftpPath);
//			List<String> fileList = apacheFtp.listFile(ftpPath);//获取到要下载的文件列表
//			downRemoteFile(fileList,apacheFtp);
//		} catch (Exception e) {
//			log.error("", e);
//		} finally {
//			if (apacheFtp != null) {
//				apacheFtp.forceCloseConnection();
//			}
//		}
		
		lock.lock();
		log.info("=======>CPE锁网测试定时任务");
		try{
		String ftpLocalPath ="/home/pop/POP2";
		
		File ftpDir=new File(ftpLocalPath);
		if(!ftpDir.exists()){
			ftpDir.mkdir();
		}
		String movePath = ftpConfig.getMOVE_PATH();
		File moveDir=new File(movePath);
		if(!moveDir.exists()){
			moveDir.mkdir();
		}
		File[]	listFiles=new File(ftpLocalPath).listFiles(new FileFilter(){

			@Override
			public boolean accept(File f) {
				if(f.isDirectory()){
					return false;
				}
				if(f.isFile()){
					String fileName = f.getName();
					
					if(fileName.endsWith(".txt") && fileName.startsWith("BOSS2POP_CPEINSTALL_", 0))return true;
					else return false;
				}else
					return false;
			}
			
		});
		if(listFiles!=null&&listFiles.length>0){
			Arrays.sort(listFiles);
		
			for(int i=0;i<listFiles.length;i++){
				System.out.println("开始处理开户文件........."+listFiles[i].getName());
				boolean isConnect=true;
				boolean isSuccess=true;
				String result="1";
				String policId="12270010000000000000000000000005";
				String location="130-4600029194077814529";
				String location2="130-4600029154074449026";
				String location3="130-4600029154074449025";
				String location4="130-4600029154074449027";
				String location5="130-4600028897074449281";
				String location6="130-4600028897074449282";
				String location7="130-4600028897074449283";
				String location8="130-4600029194077814529";
//				String locaton9="130-4600029154074449537";
//				String loction10="130-4600029154074449538";
				List<String>locations=new ArrayList<String>();
				locations.add(location);
				locations.add(location2);
				locations.add(location3);
				locations.add(location4);
				locations.add(location5);
				locations.add(location6);
				locations.add(location7);
				locations.add(location8);
//				locations.add(locaton9);
//				locations.add(loction10);
				 log.debug("FTP目录下匹配的文件名为:" + listFiles[i].getAbsolutePath());
				 SendPccInfoServiceImpl sendPcc = SpringContext.getBean("sendPccInfoService",SendPccInfoServiceImpl.class);
				 List<CpeUserInfo> list = CpeInstallFile2Bean.txtToCpeInstallBean(listFiles[i]);
				 if(list!=null &&list.size()>0){
					 	for(int j=0;j<list.size();j++){
					 		System.out.println(list.get(j).toString());
					 		log.info("begin to 锁网----------->");
					 	try {
//					 		result = sendPcc.cpeUserNetLock(list.get(j).getProductNo(), policId,locations);
					 		 log.info("end to  锁网----------->result="+result);
					 		
						} catch (Exception e) {
							log.error("锁网用户"+list.get(j).getProductNo()+"失败,文件："+listFiles[i].getName());
							isSuccess=false;
							e.printStackTrace();
						}
					 	
					 	try { 
							moveFtpFile(listFiles[i],movePath,isSuccess,isConnect);
						} catch (Exception e) {
							log.error("销户移动文件动作"+listFiles[i].getName()+"------->异常");
							e.printStackTrace();
						}
						if("1".equals(result)){
							log.info("锁网手机号-"+list.get(j).getProductNo()+"---------->成功@");
							Long count =null;
						 	try{
						 		for(int c=0;c<locations.size();c++){
						 			count = JedisClientUtil.addKeyValues(PopConstant.REDIS_UNL_PREFIX+list.get(j).getSubsid(),locations.get(c));
						 			log.info("添加小区loction："+count+"个");
						 		}
						 		Boolean flag=JedisClientUtil.print(PopConstant.REDIS_UNL_PREFIX+list.get(j).getSubsid());
								if(flag){
									log.info("-------添加redis数据库CPE用户"+list.get(j).getProductNo()+"锁网信息----->成功");
								}else if(!flag){
									log.info("-------添加redis数据库CPE用户"+list.get(j).getProductNo()+"锁网信息----->失败");
								}else{
									log.error("--------redis数据库CPE用户"+list.get(j).getProductNo()+"redis操作错误");
								}
							log.info("-------redis数据库CPE用户"+list.get(j).getProductNo()+"set集合是否存在:"+JedisClientUtil.isExistSet(PopConstant.REDIS_UNL_PREFIX+list.get(j).getSubsid()));
						 	}catch(Exception e){
								e.printStackTrace();
								log.error("-------锁网更新用户状态失败,redis连接异常："+listFiles[i].getName()+e.getMessage());
							}finally{
			
							}
						}else{
							isSuccess=false;
							log.info("锁网手机号-"+list.get(j).getProductNo()+"---------->失败@");
						}
					 	}
				 }
				
					
		}
		}
		}catch(Exception e){
			log.error("",e);
		}finally{
			lock.unlock();
		}
	}
	/**
	 * 下载远程文件
	 * @throws IOException 
	 */
	private void downRemoteFile(List<String> remoteFileList,ApacheFtpUtil apacheFtp) throws IOException {
		if(remoteFileList!=null&&remoteFileList.size()>0){
		Collections.sort(remoteFileList);
		log.info("开始下载销户文件.........");
			// 判断处理文件策略
				for (String ftpFileName : remoteFileList) {
				 log.debug("FTP目录下匹配的文件名为:" + ftpFileName);
		//		 System.out.println("FTP目录下匹配的文件名为:" + ftpFileName);
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
					   
					 log.info("下载销户文件"+ftpFileName+"。。。。成功");
				 }
				
			}
					log.info("下载销户文件结束。。。。");
		}
	}
	
	/**
	 * 移动ftp文件
	 * @throws IOException 
	 */
	public    boolean    moveFtpFile(File ftpFile,String movePath,boolean isSuccess,boolean isConnect) throws IOException {
		
		boolean flag=true;
		if(ftpFile==null || !ftpFile.exists()|| !isConnect){
			
			return false;
		}
			
			InputStream is=null;
			Reader rd=null;
			BufferedReader br=null;
			
			OutputStream os=null;
			Writer ow=null;
			PrintWriter pw=null;
			try {
				
				is=new FileInputStream(ftpFile);
				rd=new InputStreamReader(is,"utf-8");
				br=new BufferedReader(rd);
				File f =null;
				if(isSuccess){
					f = new File(movePath+"/"+ftpFile.getName());
				}else{
					String errorPath=PropUtil.getProp("ERROR_PATH", "/config/aibi_pop/ftp.properties");
				if(new File(errorPath)!=null){
					if(!new File(errorPath).exists()){
						new File(errorPath).mkdir();
					}
					f=new File(errorPath+"/"+ftpFile.getName());
				}else{
					flag=false;
					return flag;
				}
					log.debug("锁网文件异常转移路径："+(errorPath+"/"+ftpFile.getName()));
					if(!f.exists()){
						f.createNewFile();
					}
				}
				
				os=new FileOutputStream(f);
				ow=new OutputStreamWriter(os,"utf-8");
				pw=new PrintWriter(ow);
				while(true){
					String line = br.readLine();
					if(line==null)break;
					pw.write(line+"\n");
					
				}
			} catch (Exception e) {
				e.printStackTrace();
				flag=false;
				return flag;
			}finally{
				if(br!=null){
					try {
						br.close();
					} catch (IOException e) {
						
						e.printStackTrace();
					}
				}
				if(pw!=null){
					pw.close();
				}
			}
			 if(flag){
				 System.out.println("移动文件"+ftpFile.getName()+".............>成功");
				 ftpFile.delete();
			 }else{
				 System.out.println("移动文件"+ftpFile.getName()+".............>失败");
			 }
	
	return flag;
	}
	
	
}
