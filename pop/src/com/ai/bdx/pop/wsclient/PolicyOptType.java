package com.ai.bdx.pop.wsclient;

/**
 * 策略操作类型枚举
 * @author lixq8
 *
 */
public enum PolicyOptType {

	/**编辑中*/
	OPEN((short) 10, "开户并签约"),
	/**待审批 */
	CLOSE((short) 20, "去签约"),
	/**审批中*/
	CANCEL((short) 30, "销户");

	private final short value;
	private final String name;

	PolicyOptType(short value, String name) {
		this.value = value;
		this.name = name;
	}

	public short getValue() {
		return value;
	}

	public String getName() {
		return name;
	}

	/**
	 * 通过值获取相应枚举 
	 * @param value
	 * @return
	 */
	public static PolicyOptType valueOf(short value) {
		PolicyOptType policyOptType = null;
		switch (value) {
		case 10:
			policyOptType = PolicyOptType.OPEN;
			break;
		case 20:
			policyOptType = PolicyOptType.CLOSE;
			break;
		case 30:
			policyOptType = PolicyOptType.CANCEL;
			break;
		}
		return policyOptType;
	}
}
