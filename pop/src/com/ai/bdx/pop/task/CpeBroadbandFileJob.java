package com.ai.bdx.pop.task;


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
import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.JobExecutionContext;

import com.ai.bdx.pop.bean.CpeBroadbandFileBean;
import com.ai.bdx.pop.service.CpeBroadbandService;
import com.ai.bdx.pop.util.CpeUserInfo;
import com.ai.bdx.pop.util.ftp.FtpCONST;
import com.ai.bdx.pop.util.ftp.FtpConfig;
import com.ai.bdx.pop.util.ftp.FtpConfigure;


public class CpeBroadbandFileJob {
	private static final Logger log = LogManager.getLogger();
	@Resource
	private FtpConfig ftpConfig;
	@Resource
	private CpeBroadbandService cpeBroadbandService;
	//initFtpConfig:初始化ftp配置信息
		public void initFtpConfig(JobExecutionContext context){
			ftpConfig = FtpConfigure.getInstance().getFtpConfigByTypes(FtpCONST.UPDATE_BROADBAND);
			
		}
		
	public void runTask(JobExecutionContext context) {
		initFtpConfig(context);
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
		System.out.println("errir文件定时读取任务获取列表......");
		File[]	listFiles=new File(ftpLocalPath).listFiles(new FileFilter(){

			@Override
			public boolean accept(File f) {
				if(f.isDirectory()){
					return false;
				}
				if(f.isFile()){
					String fileName = f.getName();
					
					if(fileName.endsWith(".txt") && fileName.startsWith("BOSS2POP_CPEWIDTHCHG_", 0))return true;
					else return false;
				}else
					return false;
			}
			
		});
		if(listFiles!=null&&listFiles.length>0){
			Arrays.sort(listFiles);
		
			for(int i=0;i<listFiles.length;i++){
				System.out.println("开始移动宽带错误文件........."+listFiles[i].getAbsolutePath());
				int[] updes=null;
				boolean isSuccess=true;
				 log.debug("FTP目录下匹配的文件名为:" + listFiles[i].getAbsolutePath());
				 
				 List<CpeUserInfo> list = CpeBroadbandFileBean.txtToCpeDeleteBean(listFiles[i]);
				 if(list!=null &&list.size()>0){
					 	for(int j=0;j<list.size();j++){
					 		if(list.get(i)==null)list.remove(i);
					 		System.out.println(list.get(j).toString());
					 	}
					 	try {
								cpeBroadbandService.updateCpeNetType(list);
						} catch (Exception e) {
							log.error("error更新用户状态失败,文件异常："+listFiles[i].getName()+e.getMessage());
							isSuccess=false;
						}
					 	
				 }
				//同步数据后返回回馈文件给boss

					//移动文件包括数据变更成功和失败的
				 try {
					moveFtpFile(listFiles[i],movePath,isSuccess);
				} catch (IOException e) {
					log.error("移动error文件异常："+listFiles[i].getName());
					e.printStackTrace();
				}
				
				 
		}
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
