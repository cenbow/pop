package com.ai.bdx.pop.enums;

public enum InfoType {
	/**
	 * 系统公告
	 */
	NOTICE(Short.valueOf("0")),
	/**
	 * 审批&确认通知
	 */
	FLOW(Short.valueOf("1")),
	/**
	 * 系统告警
	 */
	WARN(Short.valueOf("2"));

	private final short value;

	InfoType(short value) {
		this.value = value;
	}

	public short getValue() {
		return value;
	}
}
