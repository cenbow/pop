package com.ai.bdx.pop.enums;

/**
 * 客户群类型枚举
 * @author zhangyb5
 *
 */
public enum CustGroupType {
	/**
	 * 月周期
	 */
	MONTHLY("M"),
	/**
	 * 日周期
	 */
	DAILY("D"),
	/**
	 * 非周期
	 */
	NONE("N");

	private final String value;

	CustGroupType(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
}
