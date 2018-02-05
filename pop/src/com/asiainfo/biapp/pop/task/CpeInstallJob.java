package com.asiainfo.biapp.pop.task;

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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import redis.clients.jedis.exceptions.JedisConnectionException;

import com.ai.bdx.pop.jedis.JedisClientUtil;
import com.ai.bdx.pop.util.PopConstant;
import com.ai.bdx.pop.util.SpringContext;
import com.ai.bdx.pop.wsclient.bean.SprBusinessBean;
import com.ai.bdx.pop.wsclient.impl.SendPccInfoServiceImpl;

import com.asiainfo.biapp.pop.model.CpeUserInfo;
import com.asiainfo.biapp.pop.redis.RedisClient;
import com.asiainfo.biapp.pop.util.bean.CpeInstallFile2Bean;
import com.asiainfo.biapp.pop.util.ftp.FtpCONST;
import com.asiainfo.biapp.pop.util.ftp.FtpConfig;
import com.asiainfo.biapp.pop.util.ftp.FtpConfigure;
import com.asiainfo.biapp.pop.util.ftp.FtpFileUtil;
import com.asiainfo.biapp.pop.util.ftp.PropUtil;

public class CpeInstallJob implements Serializable {
	private static final Logger log = LogManager.getLogger();
	private static final long serialVersionUID = 1L;
	private FtpConfig ftpConfig;
	private static final ReentrantLock lock = new ReentrantLock();
	
	//initFtpConfig:初始化ftp配置信息
		public void initFtpConfig(){
			ftpConfig = FtpConfigure.getInstance().getFtpConfigByTypes(FtpCONST.SENDODD_INSTALL_CPEUSER);
		}
		
	public void runTask() {
		lock.lock();
		log.info("CpeInstallJob=======>CPE开户测试定时任务");
	    try{
		initFtpConfig();
		String ftpLocalPath = ftpConfig.getLOCAL_PATH();
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
				int[] updes=null;
				boolean isSuccess=true;
				String result="";
				String res="1";
				String policId="12270010000000000000000000000005";
				String location="130-4600028731112707329";
				List<String>locations=new ArrayList<String>();
				locations.add(location);
				 log.debug("FTP目录下匹配的文件名为:" + listFiles[i].getAbsolutePath());
				 SendPccInfoServiceImpl sendPcc = SpringContext.getBean("sendPccInfoService",SendPccInfoServiceImpl.class);
				 List<CpeUserInfo> list = CpeInstallFile2Bean.txtToCpeInstallBean(listFiles[i]);
				 if(list!=null &&list.size()>0){
					 	for(int j=0;j<list.size();j++){
					 		System.out.println(list.get(j).toString());
					 		log.info("begin to addCpeSubscriber----------->");
					 	try {
//					 		SprBusinessBean sprBean = sendPcc.getSprBean(list.get(j).getProductNo());
//					 		 result = sendPcc.addSubscriber(list.get(j).getProductNo(), sprBean);
//					 		 log.info("end to  addCpeSubscriber----------->result="+result);
//					 		res=sendPcc.addUsrLocation(list.get(j).getProductNo(), policId, locations, sprBean);
//					 		 if("0".equals(result)){
//					 			log.info("begin to subscribeUsrSessionPolicy----------->");
//					 			result=sendPcc.subscribeUsrSessionPolicy(list.get(j).getProductNo(),policId,sprBean);
//					 			log.info("end to subscribeUsrSessionPolicy----------->result="+result);
//					 			 if("0".equals(result)){
//					 				log.info("begin to addUsrLocation2----------->");
//					 				result=sendPcc.addUsrLocation(list.get(j).getProductNo(), policId, locations, sprBean);
//					 				log.info("end to addUsrLocation2----------->result="+result);
//					 				 if("0".equals(result)){
//					 					 res="1";
//					 					log.info("----------------------->CPE用户"+list.get(j).getProductNo()+"开户成功！");
//					 				 }
//					 			}
//					 		 }
						} catch (Exception e) {
							log.error("开户更新用户状态失败,文件异常："+listFiles[i].getName());
							isSuccess=false;
							e.printStackTrace();
						}
					 	
						//销户同步数据后返回回馈文件给boss
						 //上传
							boolean upload2boss = FtpFileUtil.upload2BOSSForInstall(listFiles[i], isSuccess,FtpCONST.SENDODD_INSTALL_CPEUSER);
							System.out.println("上传回馈文件"+listFiles[i].getName()+".back"+"---->"+upload2boss);
							//移动文件包括数据变更成功和失败的
						 try {
							moveFtpFile(listFiles[i],movePath,isSuccess);
						 } catch (IOException e) {
							log.error("移动开户文件异常："+listFiles[i].getName());
							e.printStackTrace();
						}
					if("1".equals(res)){
						log.info("开户手机号-"+list.get(j).getProductNo()+"---------->成功~");
//						 	RedisClient redisClient =null;
							Long count =null;
						 	try{
						 		JedisClientUtil.sadd(PopConstant.REDIS_UNL_PREFIX+list.get(j).getSubsid(),locations.get(0));
//						 		redisClient=new RedisClient();
//								count = redisClient.addKeyValues(PopConstant.REDIS_UNL_PREFIX+list.get(j).getSubsid(),locations.get(0));
						 	}catch(JedisConnectionException e){
								e.printStackTrace();
								log.error("-------销户更新用户状态失败,redis连接异常："+listFiles[i].getName()+e.getMessage());
							}finally{
//								if(redisClient!=null){
//									redisClient.closeJedis();
//								}

								Boolean flag=JedisClientUtil.print(PopConstant.REDIS_UNL_PREFIX+list.get(j).getSubsid());
								if(flag){
									log.info("-------添加redis数据库CPE用户"+list.get(j).getProductNo()+"锁网信息----->成功");
								}else if(!flag){
									log.info("-------添加redis数据库CPE用户"+list.get(j).getProductNo()+"锁网信息----->失败");
								}else{
									log.debug("--------redis数据库CPE用户"+list.get(j).getProductNo()+"redis操作错误");
								}
							log.info("-------redis数据库CPE用户"+list.get(j).getProductNo()+"锁网信息是否存在:"+JedisClientUtil.sismember(PopConstant.REDIS_UNL_PREFIX+list.get(j).getSubsid(), locations.get(0)));

							}
						}else{
							isSuccess=false;
							log.info("开户手机号-"+list.get(j).getProductNo()+"---------->失败~");
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
				File f ;
				if(isSuccess){
					f = new File(movePath+"/"+ftpFile.getName());
				}else{
					String errorPath=PropUtil.getProp("ERROR_PATH", "/config/aibi_pop/ftp.properties");
					if(!new File(errorPath).exists()){
						new File(errorPath).mkdir();
					}
					f=new File(errorPath+"/"+ftpFile.getName());
					System.out.println("开户文件异常："+(errorPath+"/"+ftpFile.getName()));
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

