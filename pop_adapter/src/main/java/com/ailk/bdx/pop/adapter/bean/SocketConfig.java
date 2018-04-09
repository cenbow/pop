package com.ailk.bdx.pop.adapter.bean;

import java.util.List;

public class SocketConfig extends BaseConfig{
	private String socketIp;
	private int socketPort;
	private long timeOutMills;
	private String socketCharset;
    private String socketUserName;
    private String socketPassword;

    private int readbufSize;
    private int receivebuffer;

    
	public SocketConfig(){}

	public SocketConfig(String socketIp, int socketPort, long timeOutMills,
			String socketCharset,String socketUserName,String socketPassword,int readbufSize,int receivebuffer) {
		this.socketIp = socketIp;
		this.socketPort = socketPort;
		this.timeOutMills = timeOutMills;
		this.socketCharset = socketCharset;
		this.socketUserName = socketUserName;
		this.socketPassword = socketPassword;
		this.readbufSize = readbufSize;
		this.receivebuffer = receivebuffer;
	}

	
	

	public String getSocketIp() {
		return socketIp;
	}
	public void setSocketIp(String socketIp) {
		this.socketIp = socketIp;
	}
	public int getSocketPort() {
		return socketPort;
	}
	public void setSocketPort(int socketPort) {
		this.socketPort = socketPort;
	}

	public long getTimeOutMills() {
		return timeOutMills;
	}

	public void setTimeOutMills(long timeOutMills) {
		this.timeOutMills = timeOutMills;
	}


	public String getSocketCharset() {
		return socketCharset;
	}

	public void setSocketCharset(String socketCharset) {
		this.socketCharset = socketCharset;
	}

	public String getSocketUserName() {
		return socketUserName;
	}

	public void setSocketUserName(String socketUserName) {
		this.socketUserName = socketUserName;
	}

	public String getSocketPassword() {
		return socketPassword;
	}

	public void setSocketPassword(String socketPassword) {
		this.socketPassword = socketPassword;
	}

	public int getReadbufSize() {
		return readbufSize;
	}

	public void setReadbufSize(int readbufSize) {
		this.readbufSize = readbufSize;
	}

	public int getReceivebuffer() {
		return receivebuffer;
	}

	public void setReceivebuffer(int receivebuffer) {
		this.receivebuffer = receivebuffer;
	}

	
}
