package com.ai.bdx.pop.adapter.bean;


public class FileConfig extends BaseConfig{
	//文件所在目录
	private String fileDirectory;
	
	//反馈文件所在目录
	private String feedbackFileDirectory;
	
	//文件名前缀
	private String filePrefix;
	
	//反馈文件名前缀
	private String feedbackFilePrefix;
	
	//文件名时间串
	private String fileTimeformat;
	
	//反馈文件名时间串
	private String feedbackFileTimeformat;
	
	//时间字符串后的数字序号长度
	private int serialNoLength;
	
	//文件名后缀
	private String fileSuffix;
	
	//反馈文件名后缀
	private String feedbackFileSuffix;
	
	private long bufferSize;

	private String charsetName;
	
	private boolean deleteLocal;
	
	private String processedFileDirectory;
	
	private String errorFileDirectory;

	private int filePried; //file文件生成周期
 
	public FileConfig() {
	}

	public FileConfig(String fileDirectory, String feedbackFileDirectory,
			String filePrefix, String feedbackFilePrefix,
			String fileTimeformat, String feedbackFileTimeformat,int serialNoLength,
			String fileSuffix, String feedbackFileSuffix, long bufferSize,
			String charsetName, boolean deleteLocal,String processedFileDirectory, int filePried) {
		super();
		this.fileDirectory = fileDirectory;
		this.feedbackFileDirectory = feedbackFileDirectory;
		this.filePrefix = filePrefix;
		this.feedbackFilePrefix = feedbackFilePrefix;
		this.fileTimeformat = fileTimeformat;
		this.feedbackFileTimeformat = feedbackFileTimeformat;
		this.serialNoLength = serialNoLength;
		this.fileSuffix = fileSuffix;
		this.feedbackFileSuffix = feedbackFileSuffix;
		this.bufferSize = bufferSize;
		this.charsetName = charsetName;
		this.deleteLocal = deleteLocal;
		this.processedFileDirectory = processedFileDirectory;
		this.errorFileDirectory = errorFileDirectory;
		this.filePried = filePried;
	}


	public int getFilePried() {
		return filePried;
	}

	public void setFilePried(int filePried) {
		this.filePried = filePried;
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
	
	/**
	 * 获取  时间字符串后的数字序号长度
	 * @return
	 */
	public int getSerialNoLength() {
		return serialNoLength;
	}
	
	/**
	 * 设置  时间字符串后的数字序号长度
	 * @param serialNoLength
	 */
	public void setSerialNoLength(int serialNoLength) {
		this.serialNoLength = serialNoLength;
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

	public String getFeedbackFileDirectory() {
		return feedbackFileDirectory;
	}

	public void setFeedbackFileDirectory(String feedbackFileDirectory) {
		this.feedbackFileDirectory = feedbackFileDirectory;
	}

	public String getFeedbackFilePrefix() {
		return feedbackFilePrefix;
	}

	public void setFeedbackFilePrefix(String feedbackFilePrefix) {
		this.feedbackFilePrefix = feedbackFilePrefix;
	}

	public String getFeedbackFileTimeformat() {
		return feedbackFileTimeformat;
	}

	public void setFeedbackFileTimeformat(String feedbackFileTimeformat) {
		this.feedbackFileTimeformat = feedbackFileTimeformat;
	}

	public String getFeedbackFileSuffix() {
		return feedbackFileSuffix;
	}

	public void setFeedbackFileSuffix(String feedbackFileSuffix) {
		this.feedbackFileSuffix = feedbackFileSuffix;
	}

	public String getProcessedFileDirectory() {
		return processedFileDirectory;
	}

	public void setProcessedFileDirectory(String processedFileDirectory) {
		this.processedFileDirectory = processedFileDirectory;
	}

	public String getErrorFileDirectory() {
		return errorFileDirectory;
	}

	public void setErrorFileDirectory(String errorFileDirectory) {
		this.errorFileDirectory = errorFileDirectory;
	}
	
}
