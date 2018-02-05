package com.ai.bdx.pop.adapter.filter;

import com.ai.bdx.pop.adapter.bean.KeyMessage;
import com.ai.bdx.pop.adapter.bean.Message;

/**
 * 过滤接口
 * @author guoyj
 *
 */
public interface IDataFilter {

	/**
	 * 数据适配过程
	 * @param obj
	 * @return
	 */
	public KeyMessage adapte(Message msg);
	
	
}
