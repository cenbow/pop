package com.ai.bdx.pop.service;

import com.ai.bdx.pop.adapter.bean.BaseConfig;
import com.ai.bdx.pop.adapter.bean.CpeStatusChangeReponse;

public interface CpeStatusChangeHandler {
	/**
	 * message：单条字符串和数据源配置对象
	 * @param message
	 */
	public CpeStatusChangeReponse handle(String line, BaseConfig config)throws Exception;
}
