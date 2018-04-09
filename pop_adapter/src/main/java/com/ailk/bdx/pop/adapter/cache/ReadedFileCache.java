package com.ailk.bdx.pop.adapter.cache;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.util.CollectionUtils;

import com.ailk.bdx.pop.adapter.util.Configure;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.HashMultimap;
import com.google.common.io.Files;

/**
 * 最近时间段内已读文件缓存
 *
 * Map=<整点时间点,<时间串文件名1,时间串文件名2,...>> 新增数据时间如超出缓存最大时间长度，会删除最早的缓存数据
 *
 * @author imp
 *
 */
public class ReadedFileCache {
	
	private static final Logger log = LogManager.getLogger();
	
	/** 缓存对象. */
	private final HashMultimap<String, String> multiMap = HashMultimap.create();
	private final ScheduledExecutorService scheduledSerializeExec; // 周期性调度序列化地线程
	private final Lock lock = new ReentrantLock();
	private final File file;
	private static final String provice = Configure.getInstance().getProperty("PROVINCE");
	
	private static class ReadedFileCacheHolder {
		private static final ReadedFileCache INSTANCE = new ReadedFileCache();
	}

	public static final ReadedFileCache getInstance() {
		return ReadedFileCacheHolder.INSTANCE;
	}

	private ReadedFileCache() {
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

	/**
	 * 检查目标串文件是否存在
	 *
	 * @param timeStr
	 *            20140411_1740|14004.AVL
	 * @param keyFormat
	 *            yyyyMMdd_HHmm
	 * @param periodUnit
	 *            间隔周期
	 * @return
	 */
	public boolean isReaded(String timeStr, String keyFormat, long periodUnit)
			throws ParseException {
		boolean isReaded = false;
		lock.lock();
		try {
			if (!multiMap.containsValue(timeStr)) { // 如果不存在已经阅读过的值
				String targetKey;
				if("hubei".equals(provice)){
					//湖北A口文件格式 timeStr = 92271-20150411233800   targetKey=20150411233800   value=92271-20150411233800
					targetKey = timeStr.substring(timeStr.length()-keyFormat.length() - 4, timeStr.length() - 2);
				}else if("shaanxi".equals(provice)){
					//陕西A口文件格式 timeStr = A9401320150708001500   targetKey=201507080015   value=A9401320150708001500
					targetKey = timeStr.substring(timeStr.length()-keyFormat.length()-2, timeStr.length()-2);
				}else{
					targetKey = timeStr.substring(0, keyFormat.length()); // 目标key
				}
				DateFormat format = new SimpleDateFormat(keyFormat);
				Calendar tc = Calendar.getInstance();
				tc.setTime(format.parse(targetKey));
				Calendar sc = Calendar.getInstance();
				// 1、小于目标key 3周期前的缓存项先删除
				Set<String> keySet = multiMap.keySet();
				Iterator<String> it = keySet.iterator();
				while (it.hasNext()) {
					String key = it.next();
					sc.setTime(format.parse(key));
					if (tc.getTimeInMillis() - sc.getTimeInMillis() >= 3 * periodUnit) {
						System.out.println("缓存移除的时间key="+key);
						it.remove();
						multiMap.removeAll(key);
					}
				}
				System.out.println("Calendar.getInstance().getTimeInMillis() - tc.getTimeInMillis():" 
						+ (Calendar.getInstance().getTimeInMillis() - tc.getTimeInMillis()));
//				// 2、大于三个文件生成周期的文件不处理
				if(Calendar.getInstance().getTimeInMillis() - tc.getTimeInMillis() >= 3 * periodUnit) {
					return true;
				}
				
				// 3、不存在数据直接添加
				// 找出最小的缓存项
				if (CollectionUtils.isEmpty(keySet)) {
					multiMap.put(targetKey, timeStr);
				} else {
					String minKey = Collections.min(keySet);
					// System.out.println("当前最小key="+minKey);
					sc.setTime(format.parse(minKey));
					if (tc.compareTo(sc) >= 0) {
						multiMap.put(targetKey, timeStr);
					} else {
						isReaded = true;
					}
				}
			}else{
				isReaded = true;
			}
		} finally {
			lock.unlock();
		}
		return isReaded;
	}
	
}
