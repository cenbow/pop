package com.ailk.bdx.pop.adapter.buffer;

import com.ailk.bdx.pop.adapter.bean.Message;

public interface IDataBuffer{
	/**
	 * 放数据
	 * @param msg
	 */
	public void push(Message msg);

	public void start();

	public void shutdown();
}
