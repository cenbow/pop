package com.ailk.bdx.pop.adapter.handle;

import java.util.Collection;

import com.ailk.bdx.pop.adapter.bean.Message;


public interface IDataHandle{

	/**
	 * 单条字符串拉取处理
	 * @return
	 */
	public void handle(Message msg);

	/**
	 * 字符串集合拉取处理
	 * @param valueList
	 */
	public void handleCollection(Collection<Message> valueList);
	
 
}
