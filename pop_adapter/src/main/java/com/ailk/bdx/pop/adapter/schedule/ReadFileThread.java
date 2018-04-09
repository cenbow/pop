package com.ailk.bdx.pop.adapter.schedule;

import java.io.File;

import com.ailk.bdx.pop.adapter.bean.BaseConfig;

public class ReadFileThread extends Thread {
	private final File file;
	private final BaseConfig config;

	public ReadFileThread(File file, BaseConfig config) {
		this.file = file;
		this.config = config;
		setName(file.getName());
	}

	@Override
	public void run() {
		FileReadLocal.readContent(file.getAbsolutePath(), config);
	}
}
