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
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.locks.ReentrantLock;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import redis.clients.jedis.exceptions.JedisException;

import com.ai.bdx.pop.service.CpeDeleteService;
import com.ai.bdx.pop.util.PopConstant;
import com.ai.bdx.pop.util.SpringContext;
import com.ailk.bdx.pop.adapter.jedis.JedisClientUtil;
import com.asiainfo.biapp.pop.Exception.DelSubscriberException;
import com.asiainfo.biapp.pop.Exception.FileErrorException;
import com.asiainfo.biapp.pop.Exception.MysqlDataAccessExcetion;
import com.asiainfo.biapp.pop.model.CpeUserInfo;
import com.asiainfo.biapp.pop.util.bean.CpeDeleteFile2Bean;
import com.asiainfo.biapp.pop.util.ftp.FtpFileUtil;
import com.asiainfo.biapp.pop.util.ftp.PropUtil;

/**
 * CPE用户销户扫描文件
 * @author 林
 *
 */

public class ScanFtpScheduleForDeleteCpeUser {

	private static final Logger log = LogManager.getLogger();
	private static final ReentrantLock lock = new ReentrantLock();
	private static BlockingQueue<Map> deleteQueue=new LinkedBlockingQueue<Map>();
	private CpeDeleteService cpeDeleteService;

  
	public void scanFtpTask() {
		
		lock.lock();
		log.info("ScanFtpScheduleForDeleteCpeUser=======>CPE销户定时任务");
		try{
			String ftpLocalPath = PropUtil.getProp("FTP_PATH", "/config/aibi_pop/ftp.properties");
			
			File ftpDir=new File(ftpLocalPath);
			if(!ftpDir.exists()){
				ftpDir.mkdir();
			}
			String movePath = PropUtil.getProp("MOVE_PATH", "/config/aibi_pop/ftp.properties");
			File moveDir=new File(movePath);
			if(!moveDir.exists()){
				moveDir.mkdir();
			}
			
		//获取到要下载的文件列表

			File[]	listFiles=new File(ftpLocalPath).listFiles(new FileFilter(){
			//文件过滤，符合条件的留下
				@Override
				public boolean accept(File f) {
					if(f.isDirectory()){
						return false;
					}
					if(f.isFile()){
						String fileName = f.getName();
						
						if(fileName.endsWith(".txt") && fileName.startsWith("BOSS2POP_CPEUSERSTATUSCHG_", 0))return true;
						else return false;
					}else
						return false;
				}
				
			});
			if(listFiles!=null&&listFiles.length>0){
				Arrays.sort(listFiles);
				cpeDeleteService =SpringContext.getBean("cpeDeleteService",CpeDeleteService.class);
				for(int i=0;i<listFiles.length;i++){
					log.info("开始保存数据销户文件........."+listFiles[i].getAbsolutePath());
					String result="";
					boolean isConnect=true;
					boolean isFileSuccess=true;
					 log.debug("FTP目录下匹配的文件名为:" + listFiles[i].getAbsolutePath());
					 List<CpeUserInfo> list = CpeDeleteFile2Bean.txtToCpeDeleteBean(listFiles[i]);
					 if(list!=null){
						 log.info("文件"+listFiles[i].getName()+"销户的用户数为:"+list.size());
					 }else{
						 log.info("获取文件"+listFiles[i].getName()+"时发生异常，集合为空");
					 }
					 if(list!=null &&list.size()>0){
						
						 	for(int j=0;j<list.size();j++){
					
						 	try {
						 	
						 		result=cpeDeleteService.cpeDeleteCpeUser(list.get(j));
						 		if(!"1".equals(result)){
						 			isFileSuccess=false;
						 		}
								 log.info("用户销户调用pcc接口返回值="+result);
							}catch(MysqlDataAccessExcetion e){
								isConnect=false;
								e.printStackTrace();
								log.error("销户更新用户状态失败,Mysql操作异常："+listFiles[i].getName()+e.getMessage());
							} catch(JedisException e){
								isConnect=false;
								e.printStackTrace();
								log.error("销户更新用户状态失败,redis连接异常："+listFiles[i].getName()+e.getMessage());
							}catch(SQLException e){
								isConnect=false;
								e.printStackTrace();
								log.error("销户更新用户状态失败,调用PCC接口时Mysql连接异常："+listFiles[i].getName()+e.getMessage());
							}catch (FileErrorException e) { 
									isFileSuccess=false;
									log.error("销户更新用户状态失败,文件异常："+listFiles[i].getName()+e.getMessage());
							}catch(RemoteException e){
								isConnect=false;
								e.printStackTrace();
								log.error("销户更新用户状态失败,调用PCC接口时webservice连接异常："+listFiles[i].getName()+e.getMessage());
	
							}catch (DelSubscriberException e) { 
								isFileSuccess=false;
								log.error("销户更新用户状态失败,调用PCC销户接口失败："+listFiles[i].getName()+e.getMessage());
							}catch (Exception e) { 
								if(e!=null){
										if(e.getMessage().startsWith("Could not open JDBC Connection for transaction", 0)){
											isConnect=false;
											e.printStackTrace();
											log.error("销户更新用户状态失败,数据库连接异常："+listFiles[i].getName()+e.getMessage());
										}else if(e.getMessage().startsWith("Could not commit JDBC transaction", 0)){
											isFileSuccess=false;
											log.error("销户更新用户状态失败,数据库连接异常："+listFiles[i].getName()+e.getMessage());
										}else{
											isFileSuccess=false;
											log.error("销户更新用户状态失败:"+listFiles[i].getName()+e.getMessage());
										}
								}
								
							}
						 	if(!isFileSuccess){
						 		Map<String, Object> errorMap = new HashMap<String,Object>();
								errorMap.put("fileName",listFiles[i].getName() );
								errorMap.put("count",1);
								deleteQueue.offer(errorMap);
						 	}
						
						 	//销户同步数据后返回回馈文件给boss
							 //上传
							 if(isConnect){
								boolean upload2boss = FtpFileUtil.upload2BOSS(listFiles[i], isFileSuccess);
								log.info("销户上传回馈文件动作"+listFiles[i].getName()+".back"+"---->"+upload2boss);
							 }
								//移动文件包括数据变更成功和失败的
							try { 
								moveFtpFile(listFiles[i],movePath,isFileSuccess,isConnect);
							} catch (Exception e) {
								log.error("销户移动文件动作"+listFiles[i].getName()+"------->异常");
								e.printStackTrace();
							}
						
					//调用redis，清除cpe锁网信息	 	
				if("1".equals(result)){
					log.info("销户手机号-"+list.get(j).getProductNo()+"---------->操作成功！");
						 		Long count =null;
						 	try{
						 		count = JedisClientUtil.delKeyValue(PopConstant.REDIS_UNL_PREFIX+list.get(j).getSubsid());
						 		log.info("删除key："+PopConstant.REDIS_UNL_PREFIX+list.get(j).getSubsid()+"----"+count+"个");
						 		Boolean flag=JedisClientUtil.print(PopConstant.REDIS_UNL_PREFIX+list.get(j).getSubsid());
								if(!flag){
									log.info("-------删除redis数据库CPE用户"+list.get(j).getProductNo()+"锁网信息----->成功");
								}else if(flag){
									log.info("-------删除redis数据库CPE用户"+list.get(j).getProductNo()+"锁网信息----->失败");
								}else{
									log.error("--------redis数据库CPE用户"+list.get(j).getProductNo()+"redis操作错误");
								}
							log.info("-------redis数据库CPE用户"+list.get(j).getSubsid()+"set集合是否存在:"+JedisClientUtil.isExistSet(PopConstant.REDIS_UNL_PREFIX+list.get(j).getSubsid()));
						 	}catch(JedisException e){
								e.printStackTrace();
								log.error("-------销户更新用户状态失败,redis连接异常："+listFiles[i].getName()+e.getMessage());
							}finally{
								
							}
						}else{
							log.info("销户手机号-"+list.get(j).getProductNo()+"---------->操作失败！");
						}
					
					 }
						 	
					
		

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
					log.debug("销户文件异常转移路径："+(errorPath+"/"+ftpFile.getName()));
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
	public static BlockingQueue getCpeDeleteQueue(){
		if(deleteQueue==null){
		return 	new LinkedBlockingQueue<Map>();
		}else{
			return deleteQueue;
		}
	}
}
