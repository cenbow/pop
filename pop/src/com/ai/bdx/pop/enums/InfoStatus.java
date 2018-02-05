package com.ai.bdx.pop.enums;

/**
 * 系统信息状态
 * @author zhangyb5
 *
 */
public enum InfoStatus {
	/**
	 * 未处理
	 */
	UNDO(Short.valueOf("0")),
	/**
	 * 非周期
	 */
	DONE(Short.valueOf("1"));

	private final short value;

	InfoStatus(short value) {
		this.value = value;
	}

	public short getValue() {
		return value;
	}
}
