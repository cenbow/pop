package com.ailk.bdx.pop.adapter.kafka;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import com.ailk.bdx.pop.adapter.kafka.receiver.ICepMessageReceiveService;
 

public class CepKafkaSimpleConsumerUtil {
  
	//队列名,是否获取到信息
	public static Map<String,Boolean> map =new HashMap<String,Boolean>();
	ExecutorService threadPool =Executors.newCachedThreadPool();
 
	
	private CepKafkaSimpleConsumerUtil(){
	}

	public static class CepKafkaSimpleConsumerUtilHoder {
		public final static CepKafkaSimpleConsumerUtil instance = new CepKafkaSimpleConsumerUtil();
	}

	public static CepKafkaSimpleConsumerUtil getInstance() {
		return CepKafkaSimpleConsumerUtilHoder.instance;
	}
	
	public void startAllConsumer(String topic,int partitionSize,ICepMessageReceiveService  controlEventDataReceiverImpl,boolean isReadFinishCloseConnect){
		for(int i=0;i<partitionSize;i++){
			CepKafkaSimpleConsumer st = new CepKafkaSimpleConsumer(topic,i,controlEventDataReceiverImpl,isReadFinishCloseConnect);
			threadPool.execute(st);
		}
	};
}
