package com.ai.bdx.pop.task;

import java.io.File;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ai.bdx.pop.adapter.bean.FileConfig;
import com.ai.bdx.pop.adapter.schedule.CpeStatusChangeFileReader;
import com.ai.bdx.pop.util.PopConstant;
import com.ai.bdx.pop.util.SpringContext;
import com.ai.bdx.pop.util.XmlConfigureUtil;
import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;

/**
 * CPE用户状态变更业务逻辑类
 * @author hpa
 *
 */
public class CpeUserStatusChangeServiceJob implements Serializable{
	private static final long serialVersionUID = 1L;
	private static Logger log = 
			LogManager.getLogger(CpeUserStatusChangeServiceJob.class);
	private static final FileConfig fileConfig;
	//private static final String USE_FILE_POLICY;
	//private static final Integer READ_FILE_THREAD_POOL_SIZE;
	private static final List<String> suffixs;
	private static final ReentrantLock lock = new ReentrantLock();
	
	static{
		/**
		 * 读文件策略
		 * 1:按文件名截取时间循序,一个一个发送 (优点:保证文件时间顺序)效率较低. 
		 * 2:截取文件名获取时间,按分钟批次发送(并发读取、发送当前分钟内的，保证数据全部发送完毕,再发送下一分钟数据)效率中 . 
		 * 3:一次性并发发送多个文件数据(缺点：可能同时发送多个时间段的)效率高 
		 */
		//USE_FILE_POLICY = Configure.getInstance().getProperty("USE_FILE_POLICY");
		
		// 读文件线程池大小
		/*READ_FILE_THREAD_POOL_SIZE = Integer.parseInt(
				Configure.getInstance().getProperty("READ_FILE_THREAD_POOL_SIZE"));*/
		
		fileConfig = (FileConfig) XmlConfigureUtil.getInstance()
				.getConfigItem(XmlConfigureUtil.FILE,
						PopConstant.CPE_USER_STATUS_CHANGE_SOURCE_NAME);
		
		if(!Strings.isNullOrEmpty(fileConfig.getFileSuffix())){
			suffixs = Lists.newArrayList(Splitter.on("|").split(fileConfig.getFileSuffix()));
		}else {//默认支持解析avl和txt为扩展名的文件
			suffixs = new ArrayList<String>();
			suffixs.add("avl");
			suffixs.add("txt");
		}
	}
	
	public void runTask() {
		try{
			lock.lock();
			String currentDateFormat = new SimpleDateFormat(fileConfig.getFileTimeformat()).format(new Date());
			log.info("本轮文件读写时间周期：{}", currentDateFormat);
			
			String path = fileConfig.getFileDirectory();
			if(Strings.isNullOrEmpty(path)){
				log.error("请source-config.xml文件中设置fileDirectory文件路径");
				return;
			}
			if(!path.endsWith(File.separator)) {
				path += File.separator;
			}
			
			File dir = new File(path);
			
			List<String> fileNames = Arrays.asList(dir.list());
			Collections.sort(fileNames);
			
			//判断处理文件策略
			
		    //一次性同时发送多个文件数据(可能同时发送多个时间段的)
		    for (String fileName : fileNames) {
				final String filePath = path + fileName;
					if (checkFileName(fileName)) {
						try {
							 /*if(!ReadedFileCache.getInstance().isReaded(
							    	fileName.substring(fileConfig.getFilePrefix().length()),
							    	fileConfig.getFileTimeformat(),
							    	fileConfig.getFilePried()*1000)){*/
							    log.info("读取文件:{}..", fileName);       
							    //taskExec.execute(new ReadFileThread(new File(filePath),fileConfig));
							    CpeStatusChangeFileReader cpeStatusChangeFileReader 
							    	= SpringContext.getBean("cpeStatusChangeFileReader", CpeStatusChangeFileReader.class);
							    cpeStatusChangeFileReader.setConfig(fileConfig);
							    cpeStatusChangeFileReader.setFileName(filePath);
							    cpeStatusChangeFileReader.setJustFileName(fileName);
							    cpeStatusChangeFileReader.readByLine();
							/*}else{
								log.info("文件:{},已读-不再处理..", fileName);	
							}*/
						} catch (Exception e) {
							log.error(e);
						}
					}else{
						log.info("文件名:{},不符合规范-不进行处理..", fileName);	
					}
		      }
		}catch(Exception e){
			log.error("",e);
		}finally{
			lock.unlock();
		}
		
	}
	
	private boolean checkFileName(String fileName){
		if(!fileName.startsWith(fileConfig.getFilePrefix())) {
			return false;
		}
		if(suffixs !=null){
			for(String suffix : suffixs){
				if(fileName.endsWith(suffix)) {
					return true;
				}
			}
		}else{
			return true;
		}
		
		return false;
	}
	
}
