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
import java.io.Serializable;
import java.io.Writer;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

import javax.annotation.Resource;
import com.ai.bdx.pop.util.CpeUserInfo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import com.ai.bdx.pop.bean.CpeBroadbandFileBean;
import com.ai.bdx.pop.service.CpeBroadbandService;
import com.ai.bdx.pop.util.ftp.PropUtil;




/**
 * CPE用户宽带扫描文件
 * 
 * @author 
 *
 */
@Component
public class ScanFtpBroadbandCpeUser implements Serializable {

	private static final long serialVersionUID = 1L;
	private static final Logger log = LogManager.getLogger();
	private static final ReentrantLock lock = new ReentrantLock();
	@Resource
	private CpeBroadbandService cpeBroadbandService;
	
	public void setCpeBroadbandService(CpeBroadbandService cpeBroadbandService) {
		this.cpeBroadbandService = cpeBroadbandService;
	}
	public void scanFtpTask() {
		
		lock.lock();
		log.info("ScanFtpBroadbandCpeUser=======>CPE宽带变更定时任务");
		try{
			//读取ftp文件的路径
			String ftpLocalPath = PropUtil.getProp("FTP_PATH", "/config/aibi_pop/popftp.properties");
			log.info(ftpLocalPath);
			File ftpDir=new File(ftpLocalPath);
			if(!ftpDir.exists()){
				ftpDir.mkdir();
			}
			//成功后转移文件路径
			String movePath = PropUtil.getProp("MOVE_PATH", "/config/aibi_pop/popftp.properties");
			log.info(movePath);
			File moveDir=new File(movePath);
			if(!moveDir.exists()){
				moveDir.mkdir();
			}
			
		//获取到要下载的文件列表
			File[]	listFiles=new File(ftpLocalPath).listFiles(new FileFilter(){

				@Override
				public boolean accept(File f) {
					if(f.isDirectory()){
						return false;
					}
					if(f.isFile()){
						String fileName = f.getName();
						
						if(fileName.endsWith(".txt") && fileName.startsWith("BOSS2POP_CPEWIDTHCHG_", 0))return true;
						else {
							return false;
						}
					}else{
						return false;
					}
				}
			});
			if(listFiles!=null&&listFiles.length>0){
				Arrays.sort(listFiles);
				for(int i=0;i<listFiles.length;i++){
					log.info("开始保存数据宽带变更文件........."+listFiles[i].getAbsolutePath());
					int[] updes = null;
					boolean isSuccess = true;
					log.info("FTP目录下匹配的文件名为:" + listFiles[i].getAbsolutePath());
					//读取text文件
					List<CpeUserInfo> list = CpeBroadbandFileBean.txtToCpeDeleteBean(listFiles[i]);
					if(list!=null &&list.size()>0){
						 	for(int j=0;j<list.size();j++){
						 		if(list.get(i)==null){
						 			list.remove(i);
						 		}
						 		System.out.println(list.get(j).toString());
						 	}
						 	try {
						 		//调用service层pcc接口
						 		cpeBroadbandService.pop2pccWebService(list);
							} catch (Exception e) { 
								log.error("宽带更新状态失败,文件异常："+listFiles[i].getName()+e);
								isSuccess=false;
							}
						 	log.info("宽带文件"+listFiles[i].getName()+"-----》操作成功");
					 }
					//宽带同步数据后返回回馈文件给boss
					//上传
					boolean upload2boss = FtpFileUtil.upload2BOSS(listFiles[i], isSuccess);
					log.info("宽带上传文件动作"+listFiles[i].getName()+".back"+"---->"+upload2boss);
					//移动文件包括数据变更成功和失败的
					try { 
						moveFtpFile(listFiles[i],movePath,isSuccess);
					} catch (Exception e) {
						log.error("宽带移动文件动作"+listFiles[i].getName()+"------->异常");
						e.printStackTrace();
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
	public boolean moveFtpFile(File ftpFile,String movePath,boolean isSuccess) throws IOException {
		
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
				File f ;
				if(isSuccess){
					f = new File(movePath+"/"+ftpFile.getName());
				}else{
					//错误文件的路径
					String errorPath=PropUtil.getProp("ERROR_PATH", "/config/aibi_pop/popftp.properties");
					if(!new File(errorPath).exists()){
						new File(errorPath).mkdir();
					}
					f=new File(errorPath+"/"+ftpFile.getName());
					log.info("宽带变更文件错了："+(errorPath+"/"+ftpFile.getName()));
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
				 log.info("移动文件"+ftpFile.getName()+".............>成功");
				 ftpFile.delete();
			 }else{
				 log.error("移动文件"+ftpFile.getName()+".............>失败");
			 }
	
	return flag;
	}
	
}
