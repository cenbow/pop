package com.ailk.bdx.pop.adapter.model;

/**
 * adapter直接对接pop,传输实体类
 * @author lyz
 * */
public class TranModel {
 
	/**
	 * 消息体长度
	 */
	private int len;
	
	/**
	 * 消息内容
	 */
	private String message;

 
	public int getLen() {
		return len;
	}

	public void setLen(int len) {
		this.len = len;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	
}
