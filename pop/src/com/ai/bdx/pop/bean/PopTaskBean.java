package com.ai.bdx.pop.bean;

import java.sql.Timestamp;

/**
 * @author liyz
 * 任务bean
 * **/
public class PopTaskBean extends PopPolicyRuleSendBean {

	//taskId
	private String taskId;

	private String execDate;

	private Timestamp taskStartTime;

	private Timestamp taskEndTime;

	private short execStatus;

	private String execInfo;

	private String preSendDataTab;

	private String sendDataTab;

	private String rejectDataTab;

	//重试次数
	private int retry = 0;

	/** 预派发数*/
	private long preSendDataCount;
	/** 实派发数*/
	private long sendDataCount;
	/** 剔除数*/
	private long rejectDataCount;

	public int getRetry() {
		return retry;
	}

	public void setRetry(int retry) {
		this.retry = retry;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getExecDate() {
		return execDate;
	}

	public void setExecDate(String execDate) {
		this.execDate = execDate;
	}

	public short getExecStatus() {
		return execStatus;
	}

	public void setExecStatus(short execStatus) {
		this.execStatus = execStatus;
	}

	public String getExecInfo() {
		return execInfo;
	}

	public void setExecInfo(String execInfo) {
		this.execInfo = execInfo;
	}

	public String getPreSendDataTab() {
		return preSendDataTab;
	}

	public void setPreSendDataTab(String preSendDataTab) {
		this.preSendDataTab = preSendDataTab;
	}

	public String getSendDataTab() {
		return sendDataTab;
	}

	public void setSendDataTab(String sendDataTab) {
		this.sendDataTab = sendDataTab;
	}

	public String getRejectDataTab() {
		return rejectDataTab;
	}

	public void setRejectDataTab(String rejectDataTab) {
		this.rejectDataTab = rejectDataTab;
	}

	public Timestamp getTaskStartTime() {
		return taskStartTime;
	}

	public void setTaskStartTime(Timestamp taskStartTime) {
		this.taskStartTime = taskStartTime;
	}

	public Timestamp getTaskEndTime() {
		return taskEndTime;
	}

	public void setTaskEndTime(Timestamp taskEndTime) {
		this.taskEndTime = taskEndTime;
	}

	public long getPreSendDataCount() {
		return preSendDataCount;
	}

	public void setPreSendDataCount(long preSendDataCount) {
		this.preSendDataCount = preSendDataCount;
	}

	public long getSendDataCount() {
		return sendDataCount;
	}

	public void setSendDataCount(long sendDataCount) {
		this.sendDataCount = sendDataCount;
	}

	public long getRejectDataCount() {
		return rejectDataCount;
	}

	public void setRejectDataCount(long rejectDataCount) {
		this.rejectDataCount = rejectDataCount;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PopTaskBean other = (PopTaskBean) obj;
		if (taskId == null) {
			if (other.taskId != null)
				return false;
		} else if (!taskId.equals(other.taskId))
			return false;
		return true;
	}

}
