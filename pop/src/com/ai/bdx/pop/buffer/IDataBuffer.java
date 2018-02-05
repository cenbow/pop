package com.ai.bdx.pop.buffer;

import net.sf.json.JSONObject;

 

public interface IDataBuffer{
	/**
	 * 放数据
	 * @param msg
	 */
	public void push(JSONObject jsonObj);

	public void start();

	public void shutdown();
}
