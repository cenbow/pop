package com.ai.bdx.pop.kafka;

import java.io.IOException;
import java.util.Properties;

import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ai.bdx.pop.kafka.util.KafKaConfigure;

/**
 * kafka 生产者
 * 
 * @author liyz
 * 
 */
public class CepKafKaProducer {
	private static final Logger log = LogManager.getLogger();

	private Producer<String, String> inner;

	private static ProducerConfig config;

	private static Properties properties;

	public CepKafKaProducer()  {
		try {
			init();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	static {
		properties =  KafKaConfigure.getInstance().getProducerProperties();
		config = new ProducerConfig(properties);
	}

	public void init() throws IOException {
		inner = new Producer<String, String>(config);
	}

	/**
	 * @param topicName
	 *            队列名
	 * @param message
	 *            消息内容
	 */
	public void send(String topicName, String message) {
		if (topicName == null) {
			log.warn("topicName 是空 ，程序返回");
			return;
		}
		try {
			inner.send(new KeyedMessage<String, String>(topicName, message,message));
		} catch (Exception e) {
			close();
			log.error("++++++++++发送数据失败..准备重连kafka..:", e);
			while (true) {
				try {
					inner = new Producer<String, String>(config);
					log.info("++++++++++重连kafka成功!+++++重新发送");
					break;
				} catch (Exception e1) {
					try {
						Thread.sleep(10 * 1000);
					} catch (InterruptedException e2) {
						log.error("++++++++++重连kafka失败..10s后重连++++++++++", e2);
					}
				}
			}
		}
	}

	public void close() {
		inner.close();
	}

	
}
