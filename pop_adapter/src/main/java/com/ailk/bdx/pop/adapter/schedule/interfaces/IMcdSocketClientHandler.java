package com.ailk.bdx.pop.adapter.schedule.interfaces;

import org.apache.mina.core.service.IoHandlerAdapter;

import com.ailk.bdx.pop.adapter.bean.SocketConfig;

public abstract class IMcdSocketClientHandler extends IoHandlerAdapter {
	
	private SocketConfig socketConfig;
	
	public  abstract void setFileConfig(String sourceName);

}
