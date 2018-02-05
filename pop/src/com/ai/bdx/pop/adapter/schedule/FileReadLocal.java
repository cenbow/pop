package com.ai.bdx.pop.adapter.schedule;

import java.io.File;

import com.ai.bdx.pop.util.SpringContext;
import com.ai.bdx.pop.util.ftp.ApacheFtpUtil;
import com.ai.bdx.pop.util.ftp.FtpConfig;

public class FileReadLocal {
	 
	/**
	 * 读文件
	 *
	 * @param s
	 */
	public static void readContent(String filename,FtpConfig config,ApacheFtpUtil apacheFtp) {
		LineFileReader reader = SpringContext.getBean("lineFileReader", LineFileReader.class);
		reader.setFile(new File(filename));
		reader.setConfig(config);
		reader.setApacheFtp(apacheFtp);
		reader.readByLine();
	}

}
