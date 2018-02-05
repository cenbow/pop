package com.ai.bdx.pop.enums;

/**
 * 策略状态枚举
 * @author lixq8
 *
 */
public enum PolicyStatus {

	/**编辑中*/
	EDITING((short) 20, "编辑中"),
	/**待审批 */
	UNAPPROVE((short) 30, "待审批"),
	/**审批中*/
	APPROVING((short) 31, "审批中"),
	/**审批通过 */
	APPROVED((short) 32, "审批通过"),
	/**待确认 */
	UNCONFIRM((short) 40, "待确认"),
	/**确认中 */
	CONFIRMING((short) 41, "确认中"),
	/**确认通过 */
	CONFIRMED((short) 42, "确认通过"),
	/**待派单 */
	UNSENDODER((short) 50, "待派单"),
	/**派发策略基本信息成功*/
	SENDODER_BASE_SUCCESS((short) 51, "派发基本信息成功"),
	/**派发策略基本信息失败*/
	SENDODER_BASE_ERROR((short) 52, "派发基本信息失败"),
	/**派发用户成功*/
	SENDODER_USER_SUCCESS((short) 53, "派发用户成功"),
	/**派发用户失败*/
	SENDODER_USER_ERROR((short) 54, "派发用户失败"),
	/**暂停 */
	SENDODER_PAUSE((short) 58, "暂停"),
	/**完成 */
	SENDODER_DONE((short) 60, "完成"),
	/**终止 */
	SENDODER_TERMINAL((short) 70, "终止"),

	NONE(Short.MAX_VALUE, "未知");

	private final short value;
	private final String name;

	PolicyStatus(short value, String name) {
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
	public static PolicyStatus valueOf(short value) {
		PolicyStatus policyStatus = null;
		switch (value) {
		case 20:
			policyStatus = PolicyStatus.EDITING;
			break;
		case 30:
			policyStatus = PolicyStatus.UNAPPROVE;
			break;
		case 31:
			policyStatus = PolicyStatus.APPROVING;
			break;
		case 32:
			policyStatus = PolicyStatus.APPROVED;
			break;
		case 40:
			policyStatus = PolicyStatus.UNCONFIRM;
			break;
		case 41:
			policyStatus = PolicyStatus.CONFIRMING;
			break;
		case 42:
			policyStatus = PolicyStatus.CONFIRMED;
			break;
		case 50:
			policyStatus = PolicyStatus.UNSENDODER;
			break;
		case 51:
			policyStatus = PolicyStatus.SENDODER_BASE_SUCCESS;
			break;
		case 52:
			policyStatus = PolicyStatus.SENDODER_BASE_ERROR;
			break;	
		case 53:
			policyStatus = PolicyStatus.SENDODER_USER_SUCCESS;
			break;		
		case 54:
			policyStatus = PolicyStatus.SENDODER_USER_ERROR;
			break;	
		case 58:
			policyStatus = PolicyStatus.SENDODER_PAUSE;
			break;	
		case 60:
			policyStatus = PolicyStatus.SENDODER_DONE;
			break;
		case 70:
			policyStatus = PolicyStatus.SENDODER_TERMINAL;
			break;
		default:
			policyStatus = PolicyStatus.NONE;
			break;
		}

		return policyStatus;
	}
}
