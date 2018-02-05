package com.ai.bdx.pop.buffer;
 

import net.sf.json.JSONObject;
 


public interface IDataHandle{

	/**
	 * 单条字符串拉取处理
	 * @return
	 */
	public void handle(JSONObject msg);
	
}
