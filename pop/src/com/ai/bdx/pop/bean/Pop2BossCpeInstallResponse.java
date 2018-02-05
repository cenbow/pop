package com.ai.bdx.pop.bean;

import com.ai.bdx.pop.base.PopModel;

public class Pop2BossCpeInstallResponse extends PopModel<CpeUserInfo>{
	private static final long serialVersionUID = 1L;

	/**
	 * 文件生成时间
	 */
	private String createTime;
	
	/**
	 * 文件记录行数（不包含首行）
	 */
	private String cpeCount;
	
	/**
	 * 0：解析失败；1：解析成功
	 */
	private String resultFlag;

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getCpeCount() {
		return cpeCount;
	}

	public void setCpeCount(String cpeCount) {
		this.cpeCount = cpeCount;
	}

	public String getResultFlag() {
		return resultFlag;
	}

	public void setResultFlag(String resultFlag) {
		this.resultFlag = resultFlag;
	} 
	
}
