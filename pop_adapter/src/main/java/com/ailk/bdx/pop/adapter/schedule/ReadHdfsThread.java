package com.ailk.bdx.pop.adapter.schedule;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ailk.bdx.pop.adapter.bean.BaseConfig;
import com.ailk.bdx.pop.adapter.util.SpringContext;

public class ReadHdfsThread extends Thread {
	private String filePath;
	private BaseConfig config;
	private String currentDst;
	private String fileName;
	//private static final Logger log = LogManager.getLogger();
	
	
	public ReadHdfsThread(BaseConfig config,String fileName,String currentDst,String filePath) {
		this.filePath = filePath;
		this.config = config;
		setName(fileName);
		this.currentDst =currentDst;
		this.fileName =fileName;
	}

	@Override
	public void run() {
		LineHdfsReader reader = SpringContext.getBean("LineHdfsReader", LineHdfsReader.class);
		reader.setFilePath(filePath);
		reader.setCurrentDst(currentDst);
		reader.setConfig(config);
		reader.readByLine(fileName);	
	}
}
