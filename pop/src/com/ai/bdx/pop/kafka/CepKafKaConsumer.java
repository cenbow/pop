package com.ai.bdx.pop.kafka;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
 



import com.ai.bdx.pop.kafka.reveiver.ICepMessageReceiveService;
import com.ai.bdx.pop.kafka.util.KafKaConfigure;

import kafka.consumer.Consumer;
import kafka.consumer.ConsumerConfig;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;

/**
 * kafka 消费者
 * 
 * @author liyz
 * 
 */
public class CepKafKaConsumer extends Thread{

	private static ConsumerConfig config;
	private String topic;
	private int partitionsNum;
	private ICepMessageReceiveService executor;
	private ConsumerConnector connector;
	private ExecutorService threadPool;
	private static Properties properties;
	
	private boolean isExit = false;
	
	private static final Logger log =  LogManager.getLogger();
	
	static{
		properties =  KafKaConfigure.getInstance().getConsumerProperties();
	}
	
	/**
	 * @param topic 队列名
	 * @param partitionsNum 分区数量
	 * @param executor 业务逻辑处理类
	 */
	public CepKafKaConsumer(String topic, int partitionsNum,ICepMessageReceiveService executor) throws IOException {
		this.topic = topic;
		this.partitionsNum = partitionsNum;
		this.executor = executor;
		config = new ConsumerConfig(properties);
	}

	
	/**
	 * @param topic 队列名
	 * @param groupId 队列组名
	 * @param partitionsNum 分区数量
	 * @param executor 业务逻辑处理类
	 */
	public CepKafKaConsumer(String topic, String groupId,int partitionsNum,ICepMessageReceiveService executor) throws IOException {
		this.topic = topic;
		this.partitionsNum = partitionsNum;
		this.executor = executor;
		properties.put("group.id", groupId);
		config = new ConsumerConfig(properties);
	}
	
	public void run() {
		connector = Consumer.createJavaConsumerConnector(config);
		Map<String, Integer> topics = new HashMap<String, Integer>();
		topics.put(topic, partitionsNum);
		Map<String, List<KafkaStream<byte[], byte[]>>> streams = connector.createMessageStreams(topics);
		List<KafkaStream<byte[], byte[]>> partitions = streams.get(topic);
		//根据分区设置线程池数量
		threadPool = Executors.newFixedThreadPool(partitionsNum);
		try{
			for (KafkaStream<byte[], byte[]> partition : partitions) {
				threadPool.execute(new MessageRunner(partition, executor));
			}
		}catch(Exception e){
			log.error("+++++++++++++++处理对列异常:"+e);
			close();
			if(!isExit){
				this.run();
			}
		}
	}

	public void close() {
		connector.shutdown();
		threadPool.shutdownNow();  

	}

	public ExecutorService getThreadPool() {
		return threadPool;
	}

	public void setThreadPool(ExecutorService threadPool) {
		this.threadPool = threadPool;
	}


	public boolean isExit() {
		return isExit;
	}


	public void setExit(boolean isExit) {
		this.isExit = isExit;
	}


	
	
}
