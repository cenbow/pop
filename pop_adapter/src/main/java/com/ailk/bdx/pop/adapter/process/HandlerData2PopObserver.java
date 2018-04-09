package com.ailk.bdx.pop.adapter.process;

import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ailk.bdx.pop.adapter.model.SessionInfo;
import com.ailk.bdx.pop.adapter.model.TranModel;
import com.ailk.bdx.pop.adapter.util.AdapterUtil;
import com.ailk.bdx.pop.adapter.util.Configure;
/**
 * @description 观察者模式实现-往Pop传数据
 * @author  lyz
 */
 
public class HandlerData2PopObserver implements Observer {
	/**
	 * 保存当前Pop连接的iosession.
	 * 采用线程安全的ConcurrentHashMap.
	 */
	public final static Map<String, SessionInfo> sessionsMapping 
		= new ConcurrentHashMap<String, SessionInfo>();
	
	private static final Logger log = LogManager.getLogger(HandlerData2PopObserver.class);
	private static final ThreadLocal<Integer> tlCount = new ThreadLocal<Integer>();
 
	private static SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static int minaClientNum;
	private static long num =1;
	
	static{
		 minaClientNum = Integer.parseInt(Configure.getInstance().getProperty("ADAPTER_MINA_CLIENT_NUM"));
	}
	
	@Override
	public void update(Observable o, Object arg) {
		if (sessionsMapping.size() <= 0) {
			return;
		}
		Map<String, String> dataMap = (Map<String, String>) arg;
		String ipSeq = dataMap.get("ipSeq");
		String message = dataMap.get("message");
		
		TranModel  tm = new TranModel();
		tm.setLen(message.getBytes(Charset.forName("UTF-8")).length);
		tm.setMessage(message);
		
		SessionInfo sessionInfo = sessionsMapping.get(ipSeq);
		if(sessionInfo!=null && sessionInfo.getSessions()!=null){
			Integer currentIndex = tlCount.get();
			
			int nextIndex = AdapterUtil.getNextIndex(currentIndex);
			
			if(nextIndex >= sessionInfo.getSessions().size()){
				//log.warn("当前ipSeq序号:"+ipSeq+"当前客户端session数量:"+sessionInfo.getSessions().size()+",小于预先客户端设置数量:"+minaClientNum+",此条消息丢弃...等待连接全部建立后发送!");
				return;
			}
			if(sessionInfo.getSessions().get(nextIndex).isConnected()){
				sessionInfo.getSessions().get(nextIndex).write(tm);
				/*if(num == 1){
					System.out.println("++++++++初始时间++++++初始时间++++++++++初始时间--- "+System.currentTimeMillis());
				}
				num++; */
				if(nextIndex == 0){
					tlCount.set(++nextIndex);	
				}else{
					tlCount.set(nextIndex);	
				}	
				
				//log.info("+++++当前线程名:"+Thread.currentThread().getName()+"当前索引:"+nextIndex);
				log.debug("+++++当前线程名:" + Thread.currentThread().getName() 
						+ "+++++发送给pop序号:" + ipSeq+"-----消息内容:" + message + "++++++++++++");
			}
		}
	}
	
	public static void sendMessageToPop(Map<String, String> arg){
		if (sessionsMapping.size() <= 0) {
			return;
		}
		
		Map<String, String> dataMap = (Map<String, String>) arg;
		String ipSeq = dataMap.get("ipSeq");
		String message = dataMap.get("message");
		
		TranModel  tm = new TranModel();
		tm.setLen(message.getBytes(Charset.forName("UTF-8")).length);
		tm.setMessage(message);
		
		//获取连接到pop的SessionInfo
		SessionInfo sessionInfo = sessionsMapping.get(ipSeq);
		
		if(sessionInfo!=null && sessionInfo.getSessions()!=null){
			//Integer currentIndex = tlCount.get();
			Integer currentIndex = null;
			int nextIndex = AdapterUtil.getNextIndex(currentIndex);
			
			if(nextIndex>=sessionInfo.getSessions().size()){
				//log.warn("当前ipSeq序号:"+ipSeq+"当前客户端session数量:"+sessionInfo.getSessions().size()+",小于预先客户端设置数量:"+minaClientNum+",此条消息丢弃...等待连接全部建立后发送!");
				return;
			}
			
			if(sessionInfo.getSessions().get(nextIndex).isConnected() && 
					!sessionInfo.getSessions().get(nextIndex).isClosing()){
				sessionInfo.getSessions().get(nextIndex).write(tm);
				//num++;
				if(nextIndex == 0){
					tlCount.set(++nextIndex);	
				}else{
					tlCount.set(nextIndex);	
				}
				log.debug("+++++当前线程名:" + Thread.currentThread().getName() 
						+ "+++++发送给Pop序号:" + ipSeq + "-----消息内容:" + message + "++++++++++++");
			}
		}
	}
	
}