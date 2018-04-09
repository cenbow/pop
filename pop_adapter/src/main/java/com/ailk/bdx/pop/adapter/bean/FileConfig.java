package com.ailk.bdx.pop.adapter.bean;

import java.util.List;

public class FileConfig extends BaseConfig{
	private ScanPeriod scanPeriod;
	private String fileDirectory;
	private String filePrefix;
	private String fileTimeformat;
	private String fileSuffix;
	private long bufferSize;

	private String charsetName;
	private boolean deleteLocal;
	

	private int filePried; //ftp文件生成周期
 
	public FileConfig() {
	}

	public FileConfig(ScanPeriod scanPeriod, String fileDirectory,
			String filePrefix, String fileTimeformat, String fileSuffix,
			long bufferSize, String charsetName, boolean deleteLocal, int filePried) {
		this.scanPeriod = scanPeriod;
		this.fileDirectory = fileDirectory;
		this.filePrefix = filePrefix;
		this.fileTimeformat = fileTimeformat;
		this.fileSuffix = fileSuffix;
		this.bufferSize = bufferSize;
		this.charsetName = charsetName;
		this.deleteLocal = deleteLocal;
		this.filePried = filePried;
	}


	public int getFilePried() {
		return filePried;
	}

	public void setFilePried(int filePried) {
		this.filePried = filePried;
	}

	public ScanPeriod getScanPeriod() {
		return scanPeriod;
	}

	public void setScanPeriod(ScanPeriod scanPeriod) {
		this.scanPeriod = scanPeriod;
	}

	public String getFileDirectory() {
		return fileDirectory;
	}

	public void setFileDirectory(String fileDirectory) {
		this.fileDirectory = fileDirectory;
	}

	public String getFilePrefix() {
		return filePrefix;
	}

	public void setFilePrefix(String filePrefix) {
		this.filePrefix = filePrefix;
	}

	public String getFileTimeformat() {
		return fileTimeformat;
	}

	public void setFileTimeformat(String fileTimeformat) {
		this.fileTimeformat = fileTimeformat;
	}

	public String getFileSuffix() {
		return fileSuffix;
	}

	public void setFileSuffix(String fileSuffix) {
		this.fileSuffix = fileSuffix;
	}

	public String getCharsetName() {
		return charsetName;
	}

	public void setCharsetName(String charsetName) {
		this.charsetName = charsetName;
	}

	public long getBufferSize() {
		return bufferSize;
	}

	public void setBufferSize(long bufferSize) {
		this.bufferSize = bufferSize;
	}

	public boolean isDeleteLocal() {
		return deleteLocal;
	}

	public void setDeleteLocal(boolean deleteLocal) {
		this.deleteLocal = deleteLocal;
	}

}
