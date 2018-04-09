package com.ailk.bdx.pop.adapter.kafka;
/**
 * kafka 消费者(无groupId)
 * 
 * @author liyz
 * 
 */
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Map.Entry;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ailk.bdx.pop.adapter.kafka.receiver.ICepMessageReceiveService;
import com.ailk.bdx.pop.adapter.util.KafKaConfigure;

import kafka.api.FetchRequest;
import kafka.api.FetchRequestBuilder;
import kafka.api.PartitionOffsetRequestInfo;
import kafka.common.ErrorMapping;
import kafka.common.TopicAndPartition;
import kafka.consumer.ConsumerConfig;
import kafka.javaapi.FetchResponse;
import kafka.javaapi.OffsetResponse;
import kafka.javaapi.PartitionMetadata;
import kafka.javaapi.TopicMetadata;
import kafka.javaapi.TopicMetadataRequest;
import kafka.javaapi.consumer.SimpleConsumer;
import kafka.message.MessageAndOffset;
 

public class CepKafkaSimpleConsumer extends Thread {
		private static final Logger log =  LogManager.getLogger();
        private List<String> m_replicaBrokers = new ArrayList<String>();
        
        // broker节点的ip:port
        private List<String> seeds = new ArrayList<String>();
        // 要订阅的topic
        private String topic ;
        // 要查找的分区
        int partition;
    	private static Properties properties;
    	
    	private ICepMessageReceiveService  controlEventDataReceiverImpl;
    	
    	private boolean isReadFinishCloseConnect;
    	
        static{
        	properties =  KafKaConfigure.getInstance().getProducerProperties();
    	}
        
        
        public CepKafkaSimpleConsumer(String topic,int partitionSize,ICepMessageReceiveService  controlEventDataReceiverImpl,boolean isReadFinishCloseConnect) {
 
                m_replicaBrokers = new ArrayList<String>();
                
                String brokerList = (String) properties.get("metadata.broker.list");
                String[] brokerListArray = brokerList.split(",");
                for(int i=0;i<brokerListArray.length;i++){
                	 seeds.add(brokerListArray[i]);
                }
                
                this.topic = topic;
                this.partition = partitionSize;
                this.controlEventDataReceiverImpl =controlEventDataReceiverImpl;
                this.isReadFinishCloseConnect = isReadFinishCloseConnect;
        }
        
        
        
       

 
        @Override
        public void run(){
        	boolean blag = true;
        	while(blag){
	        	try {
	        		blag =false;
					consume(topic,partition,seeds);
				} catch (Exception e) {
					log.error(e);
					blag = true;
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}
				}
        	}
        	log.info(topic+":关闭连接成功..");
        }
        
        private void consume(String a_topic, int a_partition, List<String> a_seedBrokers) throws Exception {
 
                // 获取指定Topic partition的元数据
        		PartitionMetadata metadata = findLeader(a_seedBrokers, a_topic, a_partition);
        		
	            if (metadata == null) {
	            	throw new Exception("没有找到队列:"+a_topic+",分区:"+a_partition+"元数据,请确保BorkersList:"+a_seedBrokers.toString()+"有此队列及分区,请检查服务器状态!...等待1秒后,重新连接..");
	    	    }
            
 
                if (metadata.leader() == null) {
                	throw new Exception("没有找到队列:"+a_topic+",分区:"+a_partition+"leader,请检查BorkersList:"+a_seedBrokers.toString()+"状态!...等待1秒后,重新连接..");
                }
 
                String leadBroker = metadata.leader().host();
                
                Integer port = metadata.leader().port();
 
                String clientName = "Client_" + a_topic + "_" + a_partition;
                

                SimpleConsumer consumer = new SimpleConsumer(leadBroker, port, 100000, 64 * 1024, clientName);
 
                long readOffset = getLastOffset(consumer, a_topic, a_partition, kafka.api.OffsetRequest.LatestTime(), clientName);
 
                boolean flag = true;
                while (flag) {
 
                        if (consumer == null) {
                        		leadBroker = metadata.leader().host();
                              	port = metadata.leader().port();
                                consumer = new SimpleConsumer(leadBroker, port, 100000, 64 * 1024, clientName);
                        }
 
                        FetchRequest req = new FetchRequestBuilder().clientId(clientName).addFetch(a_topic, a_partition, readOffset, 100000).build();
                        FetchResponse fetchResponse =null;
                        try{
                        	  fetchResponse = consumer.fetch(req);
                        }catch(Exception e){
                        	consumer.close();
                            consumer = null;
                            log.warn("警告: [" + leadBroker+":"+port+ "] ,可能已经down,请检查服务器,准备为队列"+a_topic+",分区 :"+a_partition+",寻找新leader...");
                            metadata = findNewLeader(leadBroker, a_topic, a_partition,port, a_seedBrokers);
                            continue;
                        }

                        if (fetchResponse.hasError()) {
 
                                // Something went wrong!
 
                                short code = fetchResponse.errorCode(a_topic, a_partition);
 
                                System.out.println("Error fetching data from the Broker:" + leadBroker + " Reason: " + code);
 
                                if (code == ErrorMapping.OffsetOutOfRangeCode()) {
 
                                        // We asked for an invalid offset. For simple case ask for
 
                                        // the last element to reset
 
                                        readOffset = getLastOffset(consumer, a_topic, a_partition, kafka.api.OffsetRequest.LatestTime(), clientName);
 
                                        continue;
 
                                }
 
                                consumer.close();
 
                                consumer = null;
 
                                metadata = findNewLeader(leadBroker, a_topic, a_partition,port,a_seedBrokers);
 
                                continue;
 
                        }
 

                        long numRead = 0;
 
                        for (MessageAndOffset messageAndOffset : fetchResponse.messageSet(a_topic, a_partition)) {
 
                                long currentOffset = messageAndOffset.offset();
 
                                if (currentOffset < readOffset) {
 
                                        log.warn("Found an old offset: " + currentOffset + " Expecting: " + readOffset);
 
                                        continue;
 
                                }
 

                                readOffset = messageAndOffset.nextOffset();
 
                                ByteBuffer payload = messageAndOffset.message().payload();
 

                                byte[] bytes = new byte[payload.limit()];
 
                                payload.get(bytes);
 
                                //业务逻辑
                                controlEventDataReceiverImpl.execute(new String(bytes, "UTF-8"));
 
                                numRead++;
                                if(isReadFinishCloseConnect){
                                	log.info("队列:"+a_topic+",数据获取成功...");
                                	consumer.close();
                                	CepKafkaSimpleConsumerUtil.map.put(a_topic, true);
                                	flag =false;
                                	break;
                                }
 
                        }
 

                        if (numRead == 0) {
 
                                try {
                                		if(isReadFinishCloseConnect && CepKafkaSimpleConsumerUtil.map.size()!=0 &&CepKafkaSimpleConsumerUtil.map.get(a_topic) == true){
                                			flag =false;
                                			consumer.close();
                                			break;
                                		}
                                        Thread.sleep(1000);
 
                                } catch (InterruptedException ie) {
 
                                }
 
                        }
 
                }
 
 
        }
 

        public static long getLastOffset(SimpleConsumer consumer, String topic, int partition, long whichTime, String clientName) {
 
                TopicAndPartition topicAndPartition = new TopicAndPartition(topic, partition);
 
                Map<TopicAndPartition, PartitionOffsetRequestInfo> requestInfo = new HashMap<TopicAndPartition, PartitionOffsetRequestInfo>();
 
                requestInfo.put(topicAndPartition, new PartitionOffsetRequestInfo(whichTime, 1));
 
                kafka.javaapi.OffsetRequest request = new kafka.javaapi.OffsetRequest(requestInfo, kafka.api.OffsetRequest.CurrentVersion(), clientName);
 
                OffsetResponse response = consumer.getOffsetsBefore(request);
 

                if (response.hasError()) {
 
                		log.warn("Error fetching data Offset Data the Broker. Reason: " + response.errorCode(topic, partition));
 
                        return 0;
 
                }
 
                long[] offsets = response.offsets(topic, partition);
 
                return offsets[0];
 
        }
 

        /**
 
         * @param a_oldLeader
 
         * @param a_topic
 
         * @param a_partition
 
         * @param a_port
 
         * @return String
 
         * @throws Exception
 
         *             找一个leader broker
 
         */
 
        private PartitionMetadata findNewLeader(String a_oldLeader, String a_topic, int a_partition, int a_port,List<String> seeds_brokerList) throws Exception {
        	 int i =0;
        	 boolean flag = true;
        	 PartitionMetadata metadata = null;
        	 while (flag) {

                    boolean goToSleep = false;
                    
                    //老oldLeader = 复制者本身, 即复制者也挂掉.需要重新定义复制者
                    String oldLeader = a_oldLeader+":"+a_port;
                    
                    if(m_replicaBrokers.size() ==1 &&m_replicaBrokers.get(0).equalsIgnoreCase(oldLeader)){
                    	log.debug("当前分区老leader只有一个复制者:"+m_replicaBrokers.get(0)+",并且复制者就是挂掉的分区,重置broker..broker数量:"+seeds.size()+"++"+seeds.toString());
                    	m_replicaBrokers.clear();
                    	for(String seed:seeds){
                    		m_replicaBrokers.add(seed);
                    	}
                    }
                    
                    metadata = findLeader(m_replicaBrokers, a_topic, a_partition);

                    if (metadata == null) {

                            goToSleep = true;

                    } else if (metadata.leader() == null) {

                            goToSleep = true;

                    } else if (oldLeader.equalsIgnoreCase(metadata.leader().host()+":"+metadata.leader().port()) && i == 0) {

                            // first time through if the leader hasn't changed give

                            // ZooKeeper a second to recover

                            // second time, assume the broker did recover before failover,

                            // or it was a non-Broker issue

                            //

                            goToSleep = true;

                    } else {
                    	    goToSleep = false;
                    		flag = false;
                    }

                    if (goToSleep) {

                            try {
                            	
                            		log.info("获取:"+a_topic+",分区:"+a_partition+",leader失败...,1秒后重新获取.....");
                                    Thread.sleep(1000);
                                    i++;

                            } catch (InterruptedException ie) {

                            }

                    }

            }
             return metadata;

    }


        private PartitionMetadata findLeader(List<String> a_seedBrokers, String a_topic, int a_partition) {
        		
                PartitionMetadata returnMetaData = null;
                
                log.debug("开始为队列:"+a_topic+",分区:"+a_partition+"寻找leader++++seedBrokers:"+a_seedBrokers.toString());
                boolean isSuccessFindLeader  = false;
                 for (String seed : a_seedBrokers) {
 
                        SimpleConsumer consumer = null;
                        String str[] = seed.split(":");
                        try {
 
                                consumer = new SimpleConsumer(str[0], Integer.parseInt(str[1]), 100000, 64 * 1024, "leaderLookup");
 
                                List<String> topics = Collections.singletonList(a_topic);
 
                                TopicMetadataRequest req = new TopicMetadataRequest(topics);
                                kafka.javaapi.TopicMetadataResponse resp = null;
                                try {
                                       resp = consumer.send(req);
                                }catch(Exception e){
                                		log.warn("警告:当前 broker[" + seed.toString() + "] ,无法访问"+a_topic+".可能已经down,请检查服务器!!! ");
                                	   continue;
                                }

                                List<TopicMetadata> metaData = resp.topicsMetadata();
 
                                for (TopicMetadata item : metaData) {
 
                                        for (PartitionMetadata part : item.partitionsMetadata()) {
 
                                                if (part.partitionId() == a_partition) {
                                                       returnMetaData = part;
                                                        if(returnMetaData != null){
                                                         try{
                                                        	isSuccessFindLeader = true;
                                                        	log.info("+++++++++++当前队列:"+a_topic+",分区:"+a_partition+"获取leader成功!!!...leader-id:"+returnMetaData.leader().id()+",leader-ip:"+returnMetaData.leader().host()+":"+returnMetaData.leader().port()+"+++++++++++");
                                                        }catch(Exception e){
                                                     }
                                                  }
                                                        break;
                                                }
 
                                        }
 
                                }
                                if(isSuccessFindLeader){
                                	break;
                                }
                        } catch (Exception e) {
                        	log.error("Error communicating with Broker [" + seed.toString() + "] to find Leader for [" + a_topic + ", " + a_partition + "] Reason: " + e);
                        } finally {
                                if (consumer != null)
                                        consumer.close();
                        }
 
                }
 
                if (returnMetaData != null) {
 
                       m_replicaBrokers.clear();
 
                       for (kafka.cluster.Broker replica : returnMetaData.replicas()) {
                                m_replicaBrokers.add(replica.host()+":"+replica.port());
                       }
                       try{
                    	   log.debug("+++++++++++获取-队列:"+a_topic+",分区:"+a_partition+",leader-id:"+returnMetaData.leader().id()+",leader-ip:"+returnMetaData.leader().host()+":"+returnMetaData.leader().port()+"复制者成功!!!,当前replicationer:"+m_replicaBrokers.toString()+"+++++++++++");
                       }catch(Exception e){
                       }
                }
                return returnMetaData;
 
        }




 
}