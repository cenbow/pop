package com.ailk.bdx.pop.adapter.cache.interfaces;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ailk.bdx.pop.adapter.util.Configure;
import com.google.common.base.Splitter;
import com.google.common.base.Stopwatch;
import com.google.common.base.Strings;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.io.Files;
import com.google.common.io.LineProcessor;
/**
 * 号码转换基础缓存类
 * <IMSI,PHONE>
 *
 * @author liyz
 *
 */
public abstract class IPhoneConvertCache {
	private static final Logger log = LogManager.getLogger();

	public Splitter splitter;
	//默认(IMSI,手机号)
	public  Cache<String, String> cacheBuilder ;

	public void init(){
		splitter = Splitter.on(",").omitEmptyStrings().trimResults();
		String cacheSize =  Configure.getInstance().getProperty("CONVERT_IMSI_CACHE_SIZE");
		cacheBuilder = CacheBuilder.newBuilder().maximumSize(Integer.parseInt(cacheSize)).initialCapacity(1000*10000).build();
		log.info("加载IMSI->手机号数据至内存开始....");
		load();
		log.info("加载IMSI->手机号数据至内存 成功....");
	}
	/**
	 * 
	 * getFilePath:获取文件路径
	 * @param configPath
	 * @return 
	 * @return String
	 */
	public String getFilePath(String configPath){
		   String path = Thread.currentThread().getContextClassLoader().getResource(".").getPath();
			try {
				path = java.net.URLDecoder.decode(path,"utf-8");
	 		} catch (UnsupportedEncodingException e1) {
	 		}
			String provice = Configure.getInstance().getProperty("PROVINCE");
			String configFile=null;
			if(!Strings.isNullOrEmpty(configPath)){	
				if (!Strings.isNullOrEmpty(provice)) {
					configFile = configPath.replace("{PROVICE}", provice.trim().toLowerCase());
				}else{
					configFile = configPath.replace("{PROVICE}", "default");
				}
			}
			if(configFile!=null){
				return path+configFile;
			}else{
				return null;
			}
		}
	/**
	 * 加载数据
	 */
	public void load() {
		log.info("************开始加载号码对照缓存********************");
		String configFile =  Configure.getInstance().getProperty("CONVERT_IMSI_FILE_PATH");
		if(Strings.isNullOrEmpty(configFile)){
			log.error("加载号码对照缓存异常,没有配置文件路径");
			return ;
		}
		File file = new File(getFilePath(configFile));
		Stopwatch watch = Stopwatch.createStarted();
		try {
			if(file!=null&&file.exists()&&ifFileUpdated(file)){
				Files.readLines(file, Charset.forName("UTF-8"),
						new LineProcessor<Object>() {
							//@Override
							public boolean processLine(String line)
									throws IOException {
								List<String> splitList = splitter.splitToList(line);
								cacheBuilder.put(splitList.get(1), splitList.get(0));
								return true;
							}
							//@Override
							public List<String> getResult() {
								return null;
							}
						});
			}
			
		} catch (IOException e) {
			log.error("加载IMIS转换文件异常", e);
		}finally{
			log.info("****************加载完号码对照缓存耗时{}s",watch.elapsed(TimeUnit.SECONDS));
		}
	}
	
	public void loadLacci() {
		
	}
	
	public void loadPhoneHead(){
		
	}
	/**
	 * 从缓存提数据
	 *
	 * @param key
	 * @return
	 */
	public Object get(String...objects) {
	    String str=objects[0];
		return cacheBuilder.getIfPresent(str);
	}
	
	public String get(String key) {
		return cacheBuilder.getIfPresent(key);
	}
	
	public boolean containsValue(String value){
		return false;
	}
	
	public String keyString(String str){
		return null;
	}
	
	public String getShortNum(String key) {
		return null;
	}
	
	public String getCityId(String key) {
		return null;
	}
	
	public String[] getValue(String key){
		return null;
	}
	
	public String getLacciProperty(String key){
		return null;
	}
	
	public String getPhoneHeadProperty(String key) {
		return null;
	}

	
	public void loadOutPhone(){
		log.info("当前省份没有实现加载外网手机号任务...");
	}
	
	
	//文件更新读取判断
	public Map<String,Long>  fileUpdateCahe=new HashMap<String,Long>();
	/**
	 * isFileUpdate:判断文件有没有更新
	 * @param fileName
	 * @return boolean
	 */
	public boolean ifFileUpdated(String path,String fileName){
		String filePath="";
	   if(path.endsWith(File.separator)){
		   filePath=path+fileName;
	   }else{
		   filePath=path+File.separator+fileName;   
	   }
	   return ifFileUpdated(filePath);
	}
	/**
	 * 
	 * ifFileUpdated:判断文件有没有更新
	 * @param file
	 * @return 
	 * @return boolean
	 */
	public boolean ifFileUpdated(File file){
		boolean updateFlag=false;
		Long lastUpdateTime=fileUpdateCahe.get(file.getName());
		if(lastUpdateTime==null||lastUpdateTime<=0L){//如果没有记录该文件的更新时间，默认成文件已更新
			updateFlag=true;
			return updateFlag;
		}
		if(file!=null){
			long currentUpdateTime=file.lastModified();
			if(currentUpdateTime>lastUpdateTime){
				fileUpdateCahe.put(file.getName(), Long.valueOf(currentUpdateTime));//记录当前修改时间
				updateFlag=true;
			}
		}
		return updateFlag;
	}
	public void putFileUpdateCahe(File file,Long date){
		fileUpdateCahe.put(file.getName(), date);
	}
	
	
	/**
	 * 
	 * ifFileUpdated:判断文件有没有更新
	 * @param filePath
	 * @return 
	 * @return boolean
	 */
	public boolean ifFileUpdated(String filePath){
		boolean updateFlag=false;
		if(!Strings.isNullOrEmpty(filePath)){
			File file=new File(filePath);
			updateFlag=ifFileUpdated(file);
		}
		return updateFlag;
	}
	
/*	public Date conertLong2Date(long dateLong){
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(dateLong);
		return c.getTime();
	}*/
}
