package com.ailk.bdx.pop.adapter.cache.impl.hubei;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ailk.bdx.pop.adapter.cache.interfaces.IPhoneConvertCache;
import com.ailk.bdx.pop.adapter.util.Configure;
import com.google.common.base.Splitter;
import com.google.common.base.Stopwatch;
import com.google.common.base.Strings;
import com.google.common.io.Files;
import com.google.common.io.LineProcessor;

/**
   * @ClassName: PhoneConvertCache 
   * @Description: 手机号码对照省份地市
   * @author zhilj
   * @date 创建时间：2015-6-9
 */
public class PhoneConvertCache  extends IPhoneConvertCache{
	private static final Logger log = LogManager.getLogger();
	private Splitter splitter;
	private ConcurrentHashMap<String,String> cacheBuilder;
	
	public PhoneConvertCache(){
		log.info("手机号码对照省份地市内存开始....");
		splitter = Splitter.on(",").omitEmptyStrings().trimResults();
		cacheBuilder = new ConcurrentHashMap<String,String>();
		log.info("加载手机号码->省份地市数据至内存 开始....");
		load();
		log.info("加载手机号码->省份地市数据至内存成功....");
	}
	
	public void load() {
		log.info("************开始加载号码对照省份地市缓存********************");		
		String configFile =  Configure.getInstance().getProperty("CONVERT_PHONE_PROREG_FILE_PATH");
		if(Strings.isNullOrEmpty(configFile)){
			log.error("加载号码对照缓存异常,没有配置文件路径");
			return ;
		}
		File file = new File(this.getFilePath(configFile));
		if(file!=null&&file.exists()&&ifFileUpdated(file)){				
			Stopwatch watch = Stopwatch.createStarted();
			try {
				Files.readLines(file, Charset.forName("UTF-8"),new LineProcessor<Object>() {
					@Override
					public boolean processLine(String line) throws IOException {
						try{
							String[] splitList = line.split(",");
							cacheBuilder.put(splitList[0], splitList[1]);
						} catch(Exception e) {
							log.error("",e);
						}
						return true;
					}
					@Override
					public List<String> getResult() {
						return null;
					}
				});
				
			} catch (IOException e) {			
				log.error("加载IMIS转换文件异常", e);
			}finally{			
				log.info("****************加载完号码对照缓存耗时{}s",watch.elapsed(TimeUnit.SECONDS));
			}
			}else{
				log.error("找不到IMIS转换文件或者文件没有更新，不加载文件");
			}	
	}
	/**
	 * 从缓存中提取
	 */
	public String get(String key) {
		return cacheBuilder.get(key);
	}
}
 
