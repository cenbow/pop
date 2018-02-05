package com.ai.bdx.pop.task;

import java.io.File;
import java.io.FileFilter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ai.bdx.pop.adapter.bean.FileConfig;
import com.ai.bdx.pop.adapter.bean.FtpConfig;
import com.ai.bdx.pop.adapter.schedule.CpeInstallLineFileReader;
import com.ai.bdx.pop.util.PopConstant;
import com.ai.bdx.pop.util.XmlConfigureUtil;
import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;

/**
 * CPE开户定时业务类
 * @author hpa
 *
 */
public class CpeInstallAdapterServiceJob implements Serializable{

	private static final long serialVersionUID = 4513507713639314486L;
	private static Logger log = 
			LogManager.getLogger(CpeInstallAdapterServiceJob.class);
	private static final FileConfig fileConfig;
	private static final FtpConfig ftpConfig;
	private static final List<String> suffixs;
	private static final ReentrantLock lock = new ReentrantLock();
	private CpeInstallLineFileReader cpeInstallLineFileReader;
	
	public void setCpeInstallLineFileReader(
			CpeInstallLineFileReader cpeInstallLineFileReader) {
		this.cpeInstallLineFileReader = cpeInstallLineFileReader;
	}

	static{
		fileConfig = (FileConfig) XmlConfigureUtil.getInstance()
				.getConfigItem(XmlConfigureUtil.FILE,
						PopConstant.CPE_INSTALL_SOURCE_NAME);
		ftpConfig = (FtpConfig) XmlConfigureUtil.getInstance()
				.getConfigItem(XmlConfigureUtil.FTP,
						PopConstant.CPE_INSTALL_SOURCE_NAME);
		
		if(fileConfig != null && !Strings.isNullOrEmpty(fileConfig.getFileSuffix())){
			suffixs = Lists.newArrayList(Splitter.on("|").split(fileConfig.getFileSuffix()));
		}else if(ftpConfig != null && !Strings.isNullOrEmpty(ftpConfig.getFileSuffix())){
			suffixs = Lists.newArrayList(Splitter.on("|").split(ftpConfig.getFileSuffix()));
		}else {//默认支持解析avl和txt为扩展名的文件
			suffixs = new ArrayList<String>();
			suffixs.add("avl");
			suffixs.add("txt");
		}
	}
	
	public void runTask() {
		try{
			lock.lock();
			log.info("开始本轮扫描BOSS提供的CPE开户数据文件... ...");
			String path = fileConfig.getFileDirectory();
			if(Strings.isNullOrEmpty(path)){
				log.error("请source-config.xml文件中设置fileDirectory文件路径");
				return;
			}
			if(!path.endsWith(File.separator)) {
				path += File.separator;
			}
			
			if (!new File(path).exists()) {
				log.error("source-config.xml文件中配置的fileDirectory：" + path + "不存在！");
			}
			
			File[] listFiles=new File(path).listFiles(new FileFilter(){

				@Override
				public boolean accept(File f) {
					if(f.isDirectory()){
						return false;
					}
					if(f.isFile()){
						String fileName = f.getName();
						
						if(!fileName.startsWith(fileConfig.getFilePrefix())) {
							log.debug("文件" + fileName + "不是以" + fileConfig.getFilePrefix() + "开头的文件！" );
							return false;
						}
						if(suffixs != null){
							for(String suffix : suffixs){
								if(fileName.endsWith(suffix)) {
									return true;
								}
							}
							return false;
						}else{
							return true;
						}
					}else{
						return false;
					}
				}
				
			});
			/*CpeInstallLineFileReader cpeInstallLineFileReader = 
					SpringContext.getBean("cpeInstallLineFileReader", CpeInstallLineFileReader.class);*/
			if(listFiles != null && listFiles.length > 0){
				Arrays.sort(listFiles);
				for(int i = 0;i < listFiles.length;i++){
					cpeInstallLineFileReader.setConfig(fileConfig);
					cpeInstallLineFileReader.setFileName(listFiles[i].getName());
					cpeInstallLineFileReader.setAbsolutePath(listFiles[i].getAbsolutePath());
					cpeInstallLineFileReader.dealByLine();
				}
			}else {
				log.debug("本轮扫描未发现BOSS提供的CPE开户数据文件!");
			}
		}catch(Exception e){
			log.error("",e);
		}finally{
			lock.unlock();
		}
	}
}