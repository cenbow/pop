package com.ailk.bdx.pop.adapter.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class KafKaConfigure {

	private static final Logger logger = LogManager.getLogger();
	
	private static KafKaConfigure configure = new KafKaConfigure();
	
	private String producerFileName= "/cep-kafka-producter.properties";
	
	private String consumerFileName= "/cep-kafka-consumer.properties";
	
	private Properties consumerProperties = null;
	private Properties producerProperties = null;
	
	private KafKaConfigure(){
		loadPropertyFile(consumerFileName);
		loadPropertyFile(producerFileName);
	}
	
	public static KafKaConfigure getInstance() {
		return configure;
	}
	
	public void loadPropertyFile(String fileName) {
		
		URL confFileUrl = getClass().getResource(fileName);
		String fileUrl="";
    	try {
		   fileUrl = java.net.URLDecoder.decode(confFileUrl.getFile(),"utf-8");
		} catch (UnsupportedEncodingException e1) {
		} 
		File fileObj = new File(fileUrl);
		String absPathStr = fileObj.getAbsolutePath();
		logger.debug("fileName:{}\r\n Absolute Path: {}", fileName, absPathStr);
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(fileUrl);
			if("/cep-kafka-producter.properties".equals(fileName)){
				producerProperties = new Properties();
				producerProperties.load(fis);
			}else if("/cep-kafka-consumer.properties".equals(fileName)){
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
