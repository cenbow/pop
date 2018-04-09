package com.ailk.bdx.pop.adapter.bean;

/**
 * Ftp xml配置
 *
 * @author imp
 *
 */
public class FtpConfig extends BaseConfig {
	private ScanPeriod scanPeriod;
	private String ftpAddress;// FTP服务器地址
	private int ftpPort;// 端口
	private String ftpUser;// 用户名
	private String ftpPassword;// 密码
	private int ftpPasswordEncrypt;// 密码是否加密（0-不加密;1-加密）
	private String ftpPath;// 服务器路径
	private String localPath;
	private String filePrefix;
	private String fileTimeformat;
	private String fileSuffix;
	private boolean deleteRemote;// 是否删除远程文件
	private String charsetName;// ftp传输编码
	private boolean deleteLocal;// 是否删除本地文件
	private int ftpFilePried; //ftp文件生成周期

	public FtpConfig() {
	}

	public FtpConfig(ScanPeriod scanPeriod, String ftpAddress, int ftpPort,
			String ftpUser, String ftpPassword, int ftpPasswordEncrypt,
			String ftpPath, String localPath, String filePrefix,
			String fileTimeformat, String fileSuffix, boolean deleteRemote,
			String charsetName, boolean deleteLocal, int ftpFilePried) {
		this.scanPeriod = scanPeriod;
		this.ftpAddress = ftpAddress;
		this.ftpPort = ftpPort;
		this.ftpUser = ftpUser;
		this.ftpPassword = ftpPassword;
		this.ftpPasswordEncrypt = ftpPasswordEncrypt;
		this.ftpPath = ftpPath;
		this.localPath = localPath;
		this.filePrefix = filePrefix;
		this.fileTimeformat = fileTimeformat;
		this.fileSuffix = fileSuffix;
		this.deleteRemote = deleteRemote;
		this.charsetName = charsetName;
		this.deleteLocal = deleteLocal;
		this.ftpFilePried = ftpFilePried;
	}



	public String getLocalPath() {
		return localPath;
	}

	public void setLocalPath(String localPath) {
		this.localPath = localPath;
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

	public ScanPeriod getScanPeriod() {
		return scanPeriod;
	}

	public void setScanPeriod(ScanPeriod scanPeriod) {
		this.scanPeriod = scanPeriod;
	}

	public String getFtpAddress() {
		return ftpAddress;
	}

	public void setFtpAddress(String ftpAddress) {
		this.ftpAddress = ftpAddress;
	}

	public int getFtpPort() {
		return ftpPort;
	}

	public void setFtpPort(int ftpPort) {
		this.ftpPort = ftpPort;
	}

	public String getFtpPath() {
		return ftpPath;
	}

	public void setFtpPath(String ftpPath) {
		this.ftpPath = ftpPath;
	}

	public String getFtpUser() {
		return ftpUser;
	}

	public void setFtpUser(String ftpUser) {
		this.ftpUser = ftpUser;
	}

	public String getFtpPassword() {
		return ftpPassword;
	}

	public void setFtpPassword(String ftpPassword) {
		this.ftpPassword = ftpPassword;
	}

	public int getFtpPasswordEncrypt() {
		return ftpPasswordEncrypt;
	}

	public void setFtpPasswordEncrypt(int ftpPasswordEncrypt) {
		this.ftpPasswordEncrypt = ftpPasswordEncrypt;
	}

	public boolean isDeleteRemote() {
		return deleteRemote;
	}

	public void setDeleteRemote(boolean deleteRemote) {
		this.deleteRemote = deleteRemote;
	}

	public String getCharsetName() {
		return charsetName;
	}

	public void setCharsetName(String charsetName) {
		this.charsetName = charsetName;
	}

	public boolean isDeleteLocal() {
		return deleteLocal;
	}

	public void setDeleteLocal(boolean deleteLocal) {
		this.deleteLocal = deleteLocal;
	}

	public int getFtpFilePried() {
		return ftpFilePried;
	}

	public void setFtpFilePried(int ftpFilePried) {
		this.ftpFilePried = ftpFilePried;
	}
}
