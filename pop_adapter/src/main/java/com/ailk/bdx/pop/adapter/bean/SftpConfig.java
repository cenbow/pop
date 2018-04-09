package com.ailk.bdx.pop.adapter.bean;
/**
   * @ClassName: SftpConfig 
   * @Description: Sftp配置项
   * @author zhilj
   * @date 创建时间：2015-7-9
 */
public class SftpConfig extends BaseConfig{
	
	private ScanPeriod scanPeriod;
	private int timeOutMills;
	private String sftpAddress;// SFTP服务器地址
	private String sftpPort;// 端口
	private String sftpUser;// 用户名
	private String sftpPassword;// 密码
	private int sftpPasswordEncrypt;// 密码是否加密（0-不加密;1-加密）
	private String sftpPath;// 服务器路径
	private String localPath;
	private String filePrefix;
	private String fileTimeformat;
	private String fileSuffix;
	private String fileMatchField;
	private boolean deleteRemote;// 是否删除远程文件
	private String charsetName;// sftp传输编码
	private boolean deleteLocal;// 是否删除本地文件
	private int sftpFilePried; //sftp文件生成周期
	private boolean prefixMatch;// 前缀是否匹配，是否变化
	private int prefixMatchLen;// 匹配前缀变化的长度
	
	public SftpConfig() {
	}

	public SftpConfig(ScanPeriod scanPeriod, String sftpAddress, String sftpPort,String sftpUser, String sftpPassword,
			String sftpPath, String localPath, String filePrefix,String fileTimeformat, String fileSuffix, boolean deleteRemote,
			String charsetName, boolean deleteLocal, int sftpFilePried) {
		this.scanPeriod = scanPeriod;
		this.sftpAddress = sftpAddress;
		this.sftpPort = sftpPort;
		this.sftpUser = sftpUser;
		this.sftpPassword = sftpPassword;
		this.sftpPath = sftpPath;
		this.localPath = localPath;
		this.filePrefix = filePrefix;
		this.fileTimeformat = fileTimeformat;
		this.fileSuffix = fileSuffix;
		this.deleteRemote = deleteRemote;
		this.charsetName = charsetName;
		this.deleteLocal = deleteLocal;
		this.sftpFilePried = sftpFilePried;
	}

	/**
	 * @return the scanPeriod
	 */
	public ScanPeriod getScanPeriod() {
		return scanPeriod;
	}

	/**
	 * @param scanPeriod the scanPeriod to set
	 */
	public void setScanPeriod(ScanPeriod scanPeriod) {
		this.scanPeriod = scanPeriod;
	}

	/**
	 * @return the sftpAddress
	 */
	public String getSftpAddress() {
		return sftpAddress;
	}

	/**
	 * @param sftpAddress the sftpAddress to set
	 */
	public void setSftpAddress(String sftpAddress) {
		this.sftpAddress = sftpAddress;
	}

	/**
	 * @return the timeOutMills
	 */
	public int getTimeOutMills() {
		return timeOutMills;
	}

	/**
	 * @param timeOutMills the timeOutMills to set
	 */
	public void setTimeOutMills(int timeOutMills) {
		this.timeOutMills = timeOutMills;
	}

	/**
	 * @return the sftpPort
	 */
	public String getSftpPort() {
		return sftpPort;
	}

	/**
	 * @param sftpPort the sftpPort to set
	 */
	public void setSftpPort(String sftpPort) {
		this.sftpPort = sftpPort;
	}

	/**
	 * @return the sftpUser
	 */
	public String getSftpUser() {
		return sftpUser;
	}

	/**
	 * @param sftpUser the sftpUser to set
	 */
	public void setSftpUser(String sftpUser) {
		this.sftpUser = sftpUser;
	}

	/**
	 * @return the sftpPassword
	 */
	public String getSftpPassword() {
		return sftpPassword;
	}

	/**
	 * @param sftpPassword the sftpPassword to set
	 */
	public void setSftpPassword(String sftpPassword) {
		this.sftpPassword = sftpPassword;
	}

	/**
	 * @return the sftpPath
	 */
	public String getSftpPath() {
		return sftpPath;
	}

	/**
	 * @param sftpPath the sftpPath to set
	 */
	public void setSftpPath(String sftpPath) {
		this.sftpPath = sftpPath;
	}

	/**
	 * @return the localPath
	 */
	public String getLocalPath() {
		return localPath;
	}

	/**
	 * @param localPath the localPath to set
	 */
	public void setLocalPath(String localPath) {
		this.localPath = localPath;
	}

	/**
	 * @return the filePrefix
	 */
	public String getFilePrefix() {
		return filePrefix;
	}

	/**
	 * @param filePrefix the filePrefix to set
	 */
	public void setFilePrefix(String filePrefix) {
		this.filePrefix = filePrefix;
	}

	/**
	 * @return the fileTimeformat
	 */
	public String getFileTimeformat() {
		return fileTimeformat;
	}

	/**
	 * @param fileTimeformat the fileTimeformat to set
	 */
	public void setFileTimeformat(String fileTimeformat) {
		this.fileTimeformat = fileTimeformat;
	}

	/**
	 * @return the fileSuffix
	 */
	public String getFileSuffix() {
		return fileSuffix;
	}

	/**
	 * @param fileSuffix the fileSuffix to set
	 */
	public void setFileSuffix(String fileSuffix) {
		this.fileSuffix = fileSuffix;
	}

	/**
	 * @return the deleteRemote
	 */
	public boolean isDeleteRemote() {
		return deleteRemote;
	}

	/**
	 * @param deleteRemote the deleteRemote to set
	 */
	public void setDeleteRemote(boolean deleteRemote) {
		this.deleteRemote = deleteRemote;
	}

	/**
	 * @return the charsetName
	 */
	public String getCharsetName() {
		return charsetName;
	}

	/**
	 * @param charsetName the charsetName to set
	 */
	public void setCharsetName(String charsetName) {
		this.charsetName = charsetName;
	}

	/**
	 * @return the deleteLocal
	 */
	public boolean isDeleteLocal() {
		return deleteLocal;
	}

	/**
	 * @param deleteLocal the deleteLocal to set
	 */
	public void setDeleteLocal(boolean deleteLocal) {
		this.deleteLocal = deleteLocal;
	}

	/**
	 * @return the sftpFilePried
	 */
	public int getSftpFilePried() {
		return sftpFilePried;
	}

	/**
	 * @param sftpFilePried the sftpFilePried to set
	 */
	public void setSftpFilePried(int sftpFilePried) {
		this.sftpFilePried = sftpFilePried;
	}

	/**
	 * @return the fileMatchField
	 */
	public String getFileMatchField() {
		return fileMatchField;
	}

	/**
	 * @param fileMatchField the fileMatchField to set
	 */
	public void setFileMatchField(String fileMatchField) {
		this.fileMatchField = fileMatchField;
	}

	/**
	 * @return the sftpPasswordEncrypt
	 */
	public int getSftpPasswordEncrypt() {
		return sftpPasswordEncrypt;
	}

	/**
	 * @param sftpPasswordEncrypt the sftpPasswordEncrypt to set
	 */
	public void setSftpPasswordEncrypt(int sftpPasswordEncrypt) {
		this.sftpPasswordEncrypt = sftpPasswordEncrypt;
	}

	/**
	 * @return the prefixMatch
	 */
	public boolean isPrefixMatch() {
		return prefixMatch;
	}

	/**
	 * @param prefixMatch the prefixMatch to set
	 */
	public void setPrefixMatch(boolean prefixMatch) {
		this.prefixMatch = prefixMatch;
	}

	/**
	 * @return the prefixMatchLen
	 */
	public int getPrefixMatchLen() {
		return prefixMatchLen;
	}

	/**
	 * @param prefixMatchLen the prefixMatchLen to set
	 */
	public void setPrefixMatchLen(int prefixMatchLen) {
		this.prefixMatchLen = prefixMatchLen;
	}
}
