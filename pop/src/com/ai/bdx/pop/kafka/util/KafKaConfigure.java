package com.ai.bdx.pop.kafka.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class KafKaConfigure {

	private static final Logger logger = LogManager.getLogger();
	
	private static KafKaConfigure configure = new KafKaConfigure();
	
	private Properties consumerProperties = null;
	private Properties producerProperties = null;
	
	private KafKaConfigure(){
	}
	
	public static KafKaConfigure getInstance() {
		return configure;
	}
	
	public void init(String consumerFileName,String producerFileName){
		loadPropertyFile(consumerFileName);
		loadPropertyFile(producerFileName);
	}
	
	private void loadPropertyFile(String filePath) {
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(filePath);
			String fileName = filePath.substring(filePath.lastIndexOf("\\")+1, filePath.length());
			if(fileName.indexOf("cep-kafka-producter.properties") != -1){
				producerProperties = new Properties();
				producerProperties.load(fis);
			}else if(fileName.indexOf("cep-kafka-consumer.properties") != -1){
				consumerProperties = new Properties();
				consumerProperties.load(fis);
			} 
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if(fis != null){
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}

	public Properties getConsumerProperties() {
		return consumerProperties;
	}


	public Properties getProducerProperties() {
		return producerProperties;
	}
	

}
