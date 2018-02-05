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
import java.rmi.RemoteException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.locks.ReentrantLock;

import javax.annotation.Resource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.JobExecutionContext;

import com.ai.bdx.pop.service.CpeDeleteService;
import com.ai.bdx.pop.util.SpringContext;
import com.ai.bdx.pop.wsclient.bean.SprBusinessBean;
import com.ai.bdx.pop.wsclient.impl.SendPccInfoServiceImpl;
import com.asiainfo.biapp.pop.Exception.MysqlDataAccessExcetion;
import com.asiainfo.biapp.pop.model.CpeUserInfo;

import com.asiainfo.biapp.pop.util.bean.CpeDeleteFile2Bean;
import com.asiainfo.biapp.pop.util.bean.CpeInstallFile2Bean;
import com.asiainfo.biapp.pop.util.ftp.FtpCONST;
import com.asiainfo.biapp.pop.util.ftp.FtpConfig;
import com.asiainfo.biapp.pop.util.ftp.FtpConfigure;
import com.asiainfo.biapp.pop.util.ftp.FtpFileUtil;
import com.asiainfo.biapp.pop.util.ftp.PropUtil;

public class CpeErrorDeleteFileJob {
	private static final Logger log = LogManager.getLogger();
	private FtpConfig ftpConfig;
	private static final ReentrantLock lock = new ReentrantLock();
	private CpeDeleteService cpeDeleteService;
	//initFtpConfig:初始化ftp配置信息
		public void initFtpConfig(){
			ftpConfig = FtpConfigure.getInstance().getFtpConfigByTypes(FtpCONST.DOWNLOAD_DELETE_CPEUSER);
			
		}
		
	public void runTask() {
		lock.lock();
		log.info("======>销户异常文件读取定时任务");
	try{
		initFtpConfig();
		String ftpLocalPath = ftpConfig.getERROR_PATH();
		
		File ftpDir=new File(ftpLocalPath);
		if(!ftpDir.exists()){
			ftpDir.mkdir();
		}
		String movePath = ftpConfig.getMOVE_PATH();
		File moveDir=new File(movePath);
		if(!moveDir.exists()){
			moveDir.mkdir();
		}
		log.info("销户异常文件定时读取任务......");
		BlockingQueue<Map>  queue = ScanFtpScheduleForDeleteCpeUser.getCpeDeleteQueue();
		if(queue!=null&&queue.size()>0){
			cpeDeleteService =SpringContext.getBean("cpeDeleteService",CpeDeleteService.class);
			log.info("从销户异常队列( size: "+queue.size()+" )中拿一个文件出来读....");
				Map poll = queue.poll();
				String fileName = poll.get("fileName").toString();
				int count = Integer.parseInt(poll.get("count").toString());
				File errorFile = new File(ftpLocalPath+"/"+fileName);
			if(errorFile!=null && errorFile.exists() && count<3){
				log.info("开始处理销户异常文件........."+errorFile.getAbsolutePath());
				String result="";
				boolean isSuccess=true;
				
				 List<CpeUserInfo> list = CpeDeleteFile2Bean.txtToCpeDeleteBean(errorFile);
				 if(list!=null &&list.size()>0){
					 	for(int j=0;j<list.size();j++){
					 	
					 	try {

					 		result=cpeDeleteService.cpeDeleteCpeUser(list.get(j));
					 		if(!"1".equals(result)){
					 				isSuccess=false;
					 				Map<String, Object> errorMap = new HashMap<String,Object>();
									errorMap.put("fileName",errorFile.getName() );
									errorMap.put("count",count+1);
									queue.offer(errorMap);
					 		}
					 		
						} catch(MysqlDataAccessExcetion e){
							isSuccess=false;
							Map<String, Object> errorMap = new HashMap<String,Object>();
							errorMap.put("fileName",errorFile.getName() );
							errorMap.put("count",count);
							queue.offer(errorMap);
							log.error("销户更新用户状态失败,Mysql操作异常："+errorFile.getName());
						} catch (Exception e) {
							Map<String, Object> errorMap = new HashMap<String,Object>();
							errorMap.put("fileName",errorFile.getName() );
							errorMap.put("count",count+1);
							queue.offer(errorMap);
							log.error("error销户更新用户状态失败："+errorFile.getName()+e.getMessage());
							isSuccess=false;
						}
					 	}
				 }
				 if(isSuccess){
					 boolean upload2boss = FtpFileUtil.upload2BOSS(errorFile, isSuccess);
					log.info("销户上传回馈文件动作"+errorFile.getName()+".back"+"---->"+upload2boss);
					moveFtpFile(errorFile,movePath,isSuccess);
				 }
			
		}
		
		}
	}catch(Exception e){
		log.error("", e);
	}finally{
		lock.unlock();
	}
	
			 
   }

	/**
	 * 移动ftp文件
	 * @throws IOException 
	 */
	public    boolean    moveFtpFile(File ftpFile,String movePath,boolean isSuccess) throws IOException {
		
		boolean flag=true;
		if(ftpFile==null || !ftpFile.exists()){
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
					if(!f.exists()){
						f.createNewFile();
					}
			
				
				os=new FileOutputStream(f);
				ow=new OutputStreamWriter(os,"utf-8");
				pw=new PrintWriter(ow);
				while(true){
					String line = br.readLine();
					if(line==null)break;
					pw.write(line+"\n");
					
				}
				}else return false;
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
