package com.ai.bdx.pop.adapter.bean;

import java.io.Serializable;

/**
 * 用户CPE由城市进入农村、由农村进入城市时，
 * POP处理完PCC侧的PCRF上网日志后，
 * POP要返回给BOSS的数据实体类
 * @author hpa
 *
 */
public class CpeStatusChangeReponse implements Serializable{
	
	private static final long serialVersionUID = 3140178743215645662L;

	/**
	 * 手机号码
	 */
	private String productNo;
	
	/**
	 * 用户号，即CPE设备唯一号EMEI
	 */
	private String subsid;
	
	/**
	 * 基站限速状态：
	 * 0：正常
	 * 1：限速（农村到城市）
	 */
	private int busiStatus;
	
	/**
	 * 锁网状态：
	 * 0：未锁网
	 * 1：锁网（9个）
	 * 2：锁网（1-8个）
	 */
	private int netLockFlag;
	
	public CpeStatusChangeReponse() {
		
	}

	public CpeStatusChangeReponse(String productNo, String subsid,
			int busiStatus, int netLockFlag) {
		super();
		this.productNo = productNo;
		this.subsid = subsid;
		this.busiStatus = busiStatus;
		this.netLockFlag = netLockFlag;
	}

	/**
	 * 获取  手机号码
	 * @return
	 */
	public String getProductNo() {
		return productNo;
	}

	/**
	 * 设置 手机号码
	 * @param productNo
	 */
	public void setProductNo(String productNo) {
		this.productNo = productNo;
	}

	/**
	 * 获取 用户号，即CPE设备唯一号EMEI
	 * @return
	 */
	public String getSubsid() {
		return subsid;
	}

	/**
	 * 设置  用户号，即CPE设备唯一号EMEI
	 * @param subsid
	 */
	public void setSubsid(String subsid) {
		this.subsid = subsid;
	}
	
	/**
	 * 获取  基站限速状态：
	 * 0：正常
	 * 1：限速（农村到城市）
	 * @return
	 */
	public int getBusiStatus() {
		return busiStatus;
	}

	/**
	 * 获取  基站限速状态：
	 * 1：正常
	 * 2：限速（农村到城市）
	 * @param busiStatus
	 */
	public void setBusiStatus(int busiStatus) {
		this.busiStatus = busiStatus;
	}

	/**
	 * 获取 锁网状态：
	 * 0：未锁网
	 * 1：锁网（9个）
	 * 2：锁网（1-8个）
	 * @return
	 */
	public int getNetLockFlag() {
		return netLockFlag;
	}

	/**
	 * 设置  锁网状态：
	 * 0：未锁网
	 * 1：锁网（9个）
	 * 2：锁网（1-8个）
	 * @param netLockFlag
	 */
	public void setNetLockFlag(int netLockFlag) {
		this.netLockFlag = netLockFlag;
	}
	
}
