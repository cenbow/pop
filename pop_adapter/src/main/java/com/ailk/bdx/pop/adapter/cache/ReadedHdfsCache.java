package com.ailk.bdx.pop.adapter.cache;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.ailk.bdx.pop.adapter.util.Configure;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.HashMultimap;
import com.google.common.io.Files;


/**
 * 最近时间段内已读文件缓存
 *
 * Map=<整点时间点,<时间串文件名1,时间串文件名2,...>>
 * 新增数据时间如超出缓存最大时间长度，会删除最早的缓存数据
 *
 * @author lyz
 *
 */
public class ReadedHdfsCache {
	private static final Logger log = LogManager.getLogger();
	/** 缓存对象. */
	private static HashMultimap<String, String> multiMap = HashMultimap.create();
	private final Lock lock = new ReentrantLock();
	private final File file;
	private static final String provice = Configure.getInstance().getProperty("PROVINCE");
	private final ScheduledExecutorService scheduledSerializeExec; // 周期性序列化地调度
	
	
	private static class ReadedHdfsCacheHolder {
		private static final ReadedHdfsCache INSTANCE = new ReadedHdfsCache();
	}

	public static final ReadedHdfsCache getInstance() {
		return ReadedHdfsCacheHolder.INSTANCE;
	}
	
	private ReadedHdfsCache(){
		file = new File(Configure.getInstance().getProperty("READEDFILE_CACHE_SERIALIZE_PATH"));
		if(!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				log.error("",e);
			}
		}
		scheduledSerializeExec = Executors.newSingleThreadScheduledExecutor(); // 周期性(11分钟)写当前小时文件
		scheduledSerializeExec.scheduleAtFixedRate(new SerializeConsumer(), 30, 60, TimeUnit.SECONDS);
		load();
	}


	/**
	 * 初始化已读缓存
	 */
	private void load() {
		try {
			List<String> lines = Files.readLines(file, Charset.forName("utf-8"));
			if (lines != null && !lines.isEmpty()) {
				Splitter splitter = Splitter.on("=");
				lock.lock();
				try {
					for (String line : lines) {
						List<String> splitLine = splitter.splitToList(line);
						multiMap.put(splitLine.get(0), splitLine.get(1));
					}
				} finally {
					lock.unlock();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 检查目标串文件是否存在
	 * @param timeStr A_F-SCA-CR1.0-BH-1001-2014062014-0556.AVL
	 * @param targetKey 2014062014
	 * @param periodUnit 间隔周期
	 * @return
	 */
	public  boolean isReaded(String timeStr,String targetKey,int periodUnit) throws ParseException{
		synchronized (multiMap) {
			if(!multiMap.containsValue(timeStr)){ //如果不存在已经阅读过的值
				DateFormat format = new SimpleDateFormat("yyyyMMddHH");
				Calendar tc = Calendar.getInstance();
				tc.setTime(format.parse(targetKey));
				Calendar sc = Calendar.getInstance();
				// 1、小于目标key 3周期前的缓存项先删除
				Set<String> keySet = multiMap.keySet();
				Iterator<String> it = keySet.iterator();
				while(it.hasNext()){
					String key = it.next();
					sc.setTime(format.parse(key));
					//判断间隔周期
					if(periodUnit == Calendar.MINUTE){
						//按分钟
						if((tc.getTimeInMillis() - sc.getTimeInMillis())/1000/60 >= 3){
 							log.debug("缓存移除的时间key="+key);
							it.remove();
							multiMap.removeAll(key);
						}
						
					}else if(periodUnit == Calendar.HOUR){
						//按小时
						if((tc.getTimeInMillis() - sc.getTimeInMillis())/1000/60/60 >= 3){
 							log.debug("缓存移除的时间key="+key);
							it.remove();
							multiMap.removeAll(key);
						}
					}else if(periodUnit == Calendar.DAY_OF_MONTH){
						//按天
						if((tc.getTimeInMillis() - sc.getTimeInMillis())/1000/60/60/24 >= 3){
 							log.debug("缓存移除的时间key="+key);
							it.remove();
							multiMap.removeAll(key);
						}
					}
				}
				// 2、不存在数据直接添加
				multiMap.put(targetKey, timeStr);
				log.debug("put-targetKey"+targetKey+"---timeStr"+timeStr);
				return false;
			}
			return true;
		}
	}

	private  Map<String, Collection<String>> getCache(){
		return Collections.unmodifiableMap(multiMap.asMap());
	}
	
	/**
	 * 序列化线程(内存快照)
	 *
	 * @return the runnable
	 */
	private class SerializeConsumer implements Runnable {
		public void run() {
			lock.lock();
			try {
				Iterator<Entry<String, String>> iter = multiMap.entries().iterator();
				Files.write(Joiner.on(System.getProperty("line.separator")).join(iter), file, Charset.forName("utf-8"));
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				lock.unlock();
			}
		}
	}
	
	public static void main(String[] args){

		for(int i=0;i<10;i++){
			int targetKey = 2014062210+i;
			int periodUnit = Calendar.HOUR;
			for(int j=0;j<10;j++){
			 int random = (int) (Math.random()*9000+1000);
			 String timeStr = "A_F-SCA-CR1.0-BH-1001-2014062014-"+random+".AVL";
			 try {
				ReadedHdfsCache.getInstance().isReaded(timeStr,targetKey+"",periodUnit);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			}
		}
		System.out.println(ReadedHdfsCache.getInstance().getCache().keySet().toString());
		
		
		
	}
	
}
