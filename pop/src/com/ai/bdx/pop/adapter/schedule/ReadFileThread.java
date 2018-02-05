package com.ai.bdx.pop.adapter.schedule;

import java.io.File;

import com.ai.bdx.pop.util.ftp.ApacheFtpUtil;
import com.ai.bdx.pop.util.ftp.FtpConfig;

public class ReadFileThread extends Thread {
	private final File file;
	private final FtpConfig config;
	private final ApacheFtpUtil apacheFtp;
	

	public ReadFileThread(File file, FtpConfig config,ApacheFtpUtil apacheFtp) {
		this.file = file;
		this.config = config;
		this.apacheFtp = apacheFtp;
		setName(file.getName());
	}

	@Override
	public void run() {
		FileReadLocal.readContent(file.getAbsolutePath(), config, apacheFtp);
	}
}
