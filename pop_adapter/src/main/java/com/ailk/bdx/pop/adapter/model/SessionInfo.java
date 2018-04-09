 
package com.ailk.bdx.pop.adapter.model;

import java.net.Socket;
import java.util.Date;
import java.util.List;

import org.apache.mina.core.service.IoConnector;
import org.apache.mina.core.session.IoSession;

/**
 * @description 包装Session 
 * @author: lyz
 *
 */
 
public class SessionInfo {
	/**
	 * 当前session(会话)
	 */
	private List<IoSession> sessions;
 
	/**
	 * session连接上时间
	 */
	private Date date;
	
	/**
	 * 当前socket
	 * */
	private Socket socket;
    
 
	private IoConnector connector;
	
	/**
	 * 当前session序号(重连用)
	 */
    private int index = 0;
    
	/**
	 * 获取  当前session序号(重连用)
	 * @return
	 */
	public int getIndex() {
		return index;
	}

	/**
	 * 设置  当前session序号(重连用)
	 * @param index
	 */
	public void setIndex(int index) {
		this.index = index;
	}

	public IoConnector getConnector() {
		return connector;
	}

	public void setConnector(IoConnector connector) {
		this.connector = connector;
	}

	/**
	 * 获取  当前socket
	 * @return
	 */
	public Socket getSocket() {
		return socket;
	}

	/**
	 * 获取  当前socket
	 * @param socket
	 */
	public void setSocket(Socket socket) {
		this.socket = socket;
	}

	/** 获取  当前session
	 * 
	 * @return
	 */
	public List<IoSession> getSessions() {
		return sessions;
	}

	/**
	 * 设置   当前session
	 * @param sessions
	 */
	public void setSessions(List<IoSession> sessions) {
		this.sessions = sessions;
	}

	/**
	 * 获取  session连接上时间
	 * @return
	 */
	public Date getDate() {
		return date;
	}

	/**
	 * 设置  session连接上时间
	 * @param date
	 */
	public void setDate(Date date) {
		this.date = date;
	}
	 
}
