package com.ai.bdx.pop.adapter.handle;

import com.ai.bdx.pop.adapter.bean.BaseConfig;
import com.ai.bdx.pop.bean.CpeUserInfo;
import com.ai.bdx.pop.bean.Pop2BossCpeInstallResponse;

public interface ICpeInstallDataHandler {
	
	/**
	 * 解析BOSS提供的CPE开户文件首行
	 * @param line
	 * @param split
	 */
	public Pop2BossCpeInstallResponse analyseFirstLine(String line,BaseConfig config);
	
	/**
	 * 解析BOSS提供的CPE开户文件正文内容
	 * @param line
	 * @return
	 */
	public CpeUserInfo analyse(String line,BaseConfig config);
	
}
