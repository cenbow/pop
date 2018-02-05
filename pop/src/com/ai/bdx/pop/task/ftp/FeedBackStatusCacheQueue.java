package com.ai.bdx.pop.task.ftp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class FeedBackStatusCacheQueue {
	private static final Logger log = LogManager.getLogger();
	/** 保证单例 */
	static class SendCepEventCacheQueueHolder {
		static Map<String,BlockingQueue<String[]>> feedBackMap=new ConcurrentHashMap<String,BlockingQueue<String[]>>();
		//static BlockingQueue<String[]> feedBackQueue = new LinkedBlockingQueue<String[]>();
	}
	public static Integer saveMaxElements=5000;
	public static String split="#_#";
	
	
   //解析文件缓存队列
	public static Map<String,BlockingQueue<String[]>> getFeedBackMap(){
		return SendCepEventCacheQueueHolder.feedBackMap;
	}
	
	/**
	 * 
	 * put:单独放入元素
	 * @param ruleid
	 * @param taskid
	 * @param arrs
	 * @return
	 * @throws InterruptedException 
	 * @return boolean
	 */
	public static boolean put(String ruleid,String taskid,String[] arrs) throws InterruptedException{
	    boolean flag=false;
	    String key=ruleid+split+taskid;
		BlockingQueue<String[]> queue=getFeedBackMap().get(key);
		if(queue==null){
			queue=new LinkedBlockingQueue<String[]>();
			flag=queue.offer(arrs);
			getFeedBackMap().put(key, queue);
		}else{
			flag=queue.offer(arrs);
		}
		return flag;
	}
	/**
	 * 
	 * putAll:批量放入元素
	 * @param ruleid
	 * @param taskid
	 * @param list
	 * @return 
	 * @return boolean
	 */
	public static boolean putAll(String ruleid,String taskid,List<String[]> list){
		 boolean flag=false;
		    String key=ruleid+split+taskid;
			BlockingQueue<String[]> queue=getFeedBackMap().get(key);
			if(queue==null){
				queue=new LinkedBlockingQueue<String[]>();
				flag=queue.addAll(list);
				getFeedBackMap().put(key, queue);
			}else{
				flag=queue.addAll(list);
			}
			return flag;
	}
	/**
	 * 
	 * getAllKey:获取所有缓存key
	 * @return 
	 * @return List<String>
	 */
	public static List<String> getAllKey() {
		List<String> keyList = new ArrayList<String>();
		for (String key : getFeedBackMap().keySet()) {
			keyList.add(key);
		}
		return keyList;
	}
	
	
	/**
	 * 
	 * pull:从换队列取数据
	 * @param key 
	 * @param popCount 每次取数据数量
	 * @return 
	 * @return List<String[]>
	 */
	public static List<String[]> pull(String key,Integer popCount){
		if(popCount==null){
			popCount=saveMaxElements;
		}
		BlockingQueue<String[]> queue=getFeedBackMap().get(key);
		if(queue!=null){
			List<String[]> arrList = new ArrayList<String[]>();
			queue.drainTo(arrList, popCount);
			return arrList;
		}else{
			return null;
		}
	}

}
 