package com.ai.bdx.pop.adapter.buffer;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ai.bdx.pop.adapter.bean.BaseConfig;
import com.ai.bdx.pop.adapter.bean.KeyMessage;
import com.ai.bdx.pop.adapter.bean.Message;
import com.ai.bdx.pop.adapter.dispatch.IDispatchStrategy;
import com.ai.bdx.pop.adapter.filter.IDataFilter;
import com.ai.bdx.pop.kafka.CepKafKaProducer;
import com.ai.bdx.pop.util.SpringContext;
import com.ai.bdx.pop.util.ftp.FtpConfig;
import com.asiainfo.biframe.utils.config.Configure;
import com.asiainfo.biframe.utils.string.StringUtil;
import com.google.common.base.Joiner;
import com.google.common.collect.Maps;
import com.google.common.io.Files;
import com.google.common.io.LineProcessor;


/**
 * 内存中缓存文件，生成队列，并且消费者消费
 * @author huixw
 *
 */
public class FileCacheBuffer {
	
	private static final Logger log = LogManager.getLogger();
	
	private static final int FILE_SIZE_MAX = 10;
	
	//待处理文件队列
	private final ConcurrentLinkedQueue<String> fileProcess;
	
	private static final ReentrantLock lock = new ReentrantLock();
	
	private IDispatchStrategy dispatchStrategy;
	private final IDataFilter dataFilter;
	
	private final ConcurrentHashMap<String, Integer> fileStatus;
	private final ConcurrentHashMap<String, ConcurrentLinkedQueue<KeyMessage>> fileQueue;
	private final ConcurrentHashMap<String, FtpConfig> fileConfigMap;
	private static final String ADAPTER_JOIN_POP_FOR_MINA = Configure.getInstance().getProperty("ADAPTER_JOIN_POP_FOR_MINA");
	
	private static final int FILE_PROCESSING = 0;
	private static final int FILE_DONE = 1;
	private CepKafKaProducer cepKafKaProducer;
	private static final String ROUNTINGKEY_PREFIX = Configure.getInstance().getProperty("ROUNTINGKEY_PREFIX");
	
	private static final FileCacheBuffer instance = new FileCacheBuffer();

	
	public static FileCacheBuffer getInstance() {
		return instance;
	}
	
	private FileCacheBuffer() {
		this.fileProcess = new  ConcurrentLinkedQueue<String>();
		this.fileStatus = new ConcurrentHashMap<String,Integer>();
		this.fileQueue = new ConcurrentHashMap<String, ConcurrentLinkedQueue<KeyMessage>>();
		this.fileConfigMap = new ConcurrentHashMap<String, FtpConfig>();
		this.dispatchStrategy = SpringContext.getBean("dispatchStrategy", IDispatchStrategy.class);
		this.dataFilter = SpringContext.getBean("dataFilter", IDataFilter.class);
		if(!"1".equals(ADAPTER_JOIN_POP_FOR_MINA)){
			cepKafKaProducer = new CepKafKaProducer();
		}
		handleData();
	}
	
	public boolean canProcess(){
		return fileProcess.size() < FILE_SIZE_MAX;
	}
	
	public void putFile(final File file, final FtpConfig ftpConfig) {
		
		//根据读文件的顺序将文件放入待处理文件队列中
		lock.lock();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		final ConcurrentLinkedQueue<KeyMessage> queue = new ConcurrentLinkedQueue<KeyMessage>();
		final String key = file.getName()+"&"+sdf.format(Calendar.getInstance().getTime());
		fileStatus.put(key, FileCacheBuffer.FILE_PROCESSING);
		fileQueue.put(key, queue);
		fileConfigMap.put(key, ftpConfig);
		fileProcess.offer(key);
		//log.info("文件{}，读入内存+++++开始",file.getName());
		lock.unlock();
	    try {
				Files.readLines(file, Charset.forName("utf-8"), new LineProcessor() {
						@Override
						public Object getResult() {
							return null;
						}
		
						@Override
						public boolean processLine(String msg) throws IOException {
							KeyMessage keyMsg = dataFilter.adapte(new Message(msg,ftpConfig));
							if(keyMsg != null) {
								queue.offer(keyMsg);
							}
							return true;
						}
						
					});
				} catch (IOException e) {
					log.error("",e);
				} finally {
					log.info("文件{}，读入内存+++++结束",file.getName());
					fileStatus.put(key, FileCacheBuffer.FILE_DONE);
					
					String is_del_local=ftpConfig.getISDELETE_REMOTE();
					if(StringUtil.isNotEmpty(is_del_local)&&"1".equals(is_del_local)){
						file.delete();
					}
				}
		
	}
	
	public void handleData() {
		Thread handleThread = new Thread(){
			public void run() {
				while(true){
					try{
						final String key = fileProcess.poll();
						if(key == null){
							Thread.sleep(500);
							continue;
						}
						String fileName = key.split("&")[0];
						//log.info("文件{}，发到esper+++++开始",fileName);
						log.info("待处理队列个数 {}",fileProcess.size());
						final ConcurrentLinkedQueue<KeyMessage> queue = fileQueue.get(key);
						FtpConfig config = fileConfigMap.get(key);
						final String eventId = config.getEVENTID();
						final AtomicInteger count = new AtomicInteger();
						Thread[] thread = new Thread[5];
						for(int i=0; i<5; i++) {
							thread[i] = new Thread() {
								public void run(){
									while(true) {
										try{
											KeyMessage keyMsg = queue.poll();
											if(keyMsg == null && fileStatus.get(key)==FileCacheBuffer.FILE_DONE) {
												break;
											}
											if(keyMsg != null) {
											    count.incrementAndGet();
												sendMessage(keyMsg, eventId);
											}
										} catch(Exception e) {
											log.error("",e);
										}
									}
								}
							};
							thread[i].setName("FileCacheBuffer消息发送线程"+i);
							thread[i].start();
						}
						//等待线程处理结束
						for(int i=0; i<5; i++){
							thread[i].join();
						}
						fileStatus.remove(key);
						fileQueue.remove(key);
						fileConfigMap.remove(key);
						log.info("文件{}，发到esper+++++结束,共计{}条",fileName,count.get());
					}catch (Exception e) {
						log.error("",e);
					}finally{
						
					}
				}
			}
		};
		handleThread.setName("消费者线程handleData");
		handleThread.start();
	}
	
	public void sendMessage(KeyMessage keyMsg, String eventId) {
		int index = dispatchStrategy.dispatch(keyMsg.getKey());
		if (index != -1) {
			//判断是对接kafka 还是 直接接esper
			if("1".equals(ADAPTER_JOIN_POP_FOR_MINA)){
				//直接接esper
				Map dataMap = Maps.newHashMap();
				dataMap.put("ipSeq", index+"");
				dataMap.put("message", Joiner.on("").join(eventId,",",keyMsg.getValue()));
				//HandlerData2EsperObserver.sendMessageToCep(dataMap);
			}else{
				//cepKafKaProducer.send(ROUNTINGKEY_PREFIX + index, Joiner.on("").join(eventId,",",keyMsg.getValue()));
			}
			log.debug("+++++当前线程名:"+Thread.currentThread().getName()+"内容信息:{},dispatch:{}",keyMsg.getValue(),index);
		}
		 
	}

}








