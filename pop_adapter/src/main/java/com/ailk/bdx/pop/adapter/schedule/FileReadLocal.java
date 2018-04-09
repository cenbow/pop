package com.ailk.bdx.pop.adapter.schedule;

import java.io.File;

import com.ailk.bdx.pop.adapter.bean.BaseConfig;
import com.ailk.bdx.pop.adapter.util.SpringContext;

public class FileReadLocal {
	 
	/**
	 * 读文件
	 *
	 * @param s
	 */
	public static void readContent(String filename,BaseConfig config) {
		LineFileReader reader = SpringContext.getBean("lineFileReader", LineFileReader.class);
		reader.setFile(new File(filename));
		reader.setConfig(config);
		reader.readByLine();
	}

}
