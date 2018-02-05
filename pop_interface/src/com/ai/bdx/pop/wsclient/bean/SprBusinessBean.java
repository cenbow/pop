package com.ai.bdx.pop.wsclient.bean;

import java.util.List;

/**
 * 记录SPR接口调用的必要信息
 * @author yuanyk
 *
 */
public class SprBusinessBean {
	/**
	 * 手机号（单个）
	 */
	private String phone;
	/**
	 * 该号段SPR的名称
	 */
	private String sprName;
	/**
	 * 该号段SPR的IP地址
	 */
	private String sprIp;
	/**
	 * 该SPR的wsdl串
	 */
	private String sprWsdl;
	/**
	 * FTP上传路径
	 */
	private String ftpAddress;
	/**
	 * FTP用户名
	 */
	private String ftpUsr;
	/**
	 * FTP密码
	 */
	private String ftpPwd;
	/**
	 * 批量该SPR手机号
	 */
	private List<String> phoneList;
	/**
	 * 是否SFTP协议
	 */
	private String isSftp;
	private String sprUsr;
	private String sprPwd;
	
	/**
	 * 任务ID
	 */
	private String taskId;
	/**
	 * 设备名称
	 */
	private String epSn;
	/**
	 * 操作时间
	 */
	private String operateTime;
	/**
	 * 策略类型
	 */
	private String policyType;
	/**
	 * 批次号
	 */
	private String batchNo;
	/**
	 * 文件名组
	 */
	private String fileNames;
	
	public String getSprUsr() {
		return sprUsr;
	}
	public void setSprUsr(String sprUsr) {
		this.sprUsr = sprUsr;
	}
	public String getSprPwd() {
		return sprPwd;
	}
	public void setSprPwd(String sprPwd) {
		this.sprPwd = sprPwd;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getSprName() {
		return sprName;
	}
	public void setSprName(String sprName) {
		this.sprName = sprName;
	}
	public String getSprIp() {
		return sprIp;
	}
	public void setSprIp(String sprIp) {
		this.sprIp = sprIp;
	}
	public String getSprWsdl() {
		return sprWsdl;
	}
	public void setSprWsdl(String sprWsdl) {
		this.sprWsdl = sprWsdl;
	}
	public String getFtpAddress() {
		return ftpAddress;
	}
	public void setFtpAddress(String ftpAddress) {
		this.ftpAddress = ftpAddress;
	}
	public String getFtpUsr() {
		return ftpUsr;
	}
	public void setFtpUsr(String ftpUsr) {
		this.ftpUsr = ftpUsr;
	}
	public String getFtpPwd() {
		return ftpPwd;
	}
	public void setFtpPwd(String ftpPwd) {
		this.ftpPwd = ftpPwd;
	}
	public List<String> getPhoneList() {
		return phoneList;
	}
	public void setPhoneList(List<String> phoneList) {
		this.phoneList = phoneList;
	}
	public String getIsSftp() {
		return isSftp;
	}
	public void setIsSftp(String isSftp) {
		this.isSftp = isSftp;
	}
	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	public String getEpSn() {
		return epSn;
	}
	public void setEpSn(String epSn) {
		this.epSn = epSn;
	}
	public String getOperateTime() {
		return operateTime;
	}
	public void setOperateTime(String operateTime) {
		this.operateTime = operateTime;
	}
	public String getPolicyType() {
		return policyType;
	}
	public void setPolicyType(String policyType) {
		this.policyType = policyType;
	}
	public String getBatchNo() {
		return batchNo;
	}
	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}
	public String getFileNames() {
		return fileNames;
	}
	public void setFileNames(String fileNames) {
		this.fileNames = fileNames;
	}
}
