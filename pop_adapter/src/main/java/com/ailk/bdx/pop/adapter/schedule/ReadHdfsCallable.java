package com.ailk.bdx.pop.adapter.schedule;

import it.unimi.dsi.fastutil.objects.Object2ObjectAVLTreeMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;

import java.util.concurrent.Callable;

import com.ailk.bdx.pop.adapter.bean.BaseConfig;
import com.ailk.bdx.pop.adapter.util.SpringContext;

public class ReadHdfsCallable 
	implements Callable<Object2ObjectOpenHashMap<String,Object2ObjectAVLTreeMap<String,Long>>> {

	private String filePath;
	private BaseConfig config;
	private String currentDst;
	private String fileName;

	public ReadHdfsCallable(BaseConfig config,String fileName,String currentDst,String filePath) {
		this.filePath = filePath;
		this.config = config;
		this.currentDst = currentDst;
		this.fileName = fileName;
	}


	@Override
	public Object2ObjectOpenHashMap<String, Object2ObjectAVLTreeMap<String, Long>> call()
			throws Exception {
		LineHdfsReader reader = SpringContext.getBean("LineHdfsReader", LineHdfsReader.class);
		reader.setFilePath(filePath);
		reader.setCurrentDst(currentDst);
		reader.setConfig(config);
		reader.readByLine(fileName);
		return null;
	}
	
}
