package com.ai.bdx.pop.kafka;

import java.nio.charset.Charset;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
 

import com.ai.bdx.pop.kafka.reveiver.ICepMessageReceiveService;

import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.message.MessageAndMetadata;

public class MessageRunner implements Runnable {
	
	private static final Logger log = LogManager.getLogger();
	private KafkaStream<byte[], byte[]> partition;
	private ICepMessageReceiveService executor;

	MessageRunner(KafkaStream<byte[], byte[]> partition,ICepMessageReceiveService executor) {
		this.partition = partition;
		this.executor = executor;
	}

	public void run() {
		ConsumerIterator<byte[], byte[]> it = partition.iterator();
		while (it.hasNext()) {
			// connector.commitOffsets();手动提交offset,当autocommit.enable=false时使用
			MessageAndMetadata<byte[], byte[]> item = it.next();
			//log.debug("当前分区:" + item.partition() + "----偏移量:" + item.offset()+"----线程:"+Thread.currentThread().getName()+"---message:" + new String(item.message(),Charset.forName("UTF-8")));
			executor.execute(new String(item.message(),Charset.forName("UTF-8")));// UTF-8,注意异常
		}
	}

}
