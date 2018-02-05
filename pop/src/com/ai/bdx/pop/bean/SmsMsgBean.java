package com.ai.bdx.pop.bean;
/**
 * 
 * creator：sjw 
 * create date：2015年5月22日
 * useful：    短信封装实体
 * modify
 * ===================================================
 *  person             date               reason
 * ===================================================
 */
public class SmsMsgBean {

	private String phoneNo;//手机号
	private String sendMsg;//短信内容
	private String sengFlag;//发送标示
	private String channelNo;//渠道号
	private String sendtype;//系统标示
	
	public SmsMsgBean(String phoneNo,String sendMsg,String sengFlag,String channelNo,String sendtype){
		this.phoneNo=phoneNo;
		this.sendMsg=sendMsg;
		this.sengFlag=sengFlag;
		this.channelNo=channelNo;
		this.sendtype=sendtype;
	}
	
	public SmsMsgBean(String phoneNo,String sendMsg){
		this.phoneNo=phoneNo;
		this.sendMsg=sendMsg;
		this.sengFlag="0";
		this.channelNo="1065845101";
		this.sendtype="POP";
	}
	
	
	public String getPhoneNo() {
		return phoneNo;
	}
	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}
	public String getSendMsg() {
		return sendMsg;
	}
	public void setSendMsg(String sendMsg) {
		this.sendMsg = sendMsg;
	}
	public String getSengFlag() {
		return sengFlag;
	}
	public void setSengFlag(String sengFlag) {
		this.sengFlag = sengFlag;
	}
	public String getChannelNo() {
		return channelNo;
	}
	public void setChannelNo(String channelNo) {
		this.channelNo = channelNo;
	}
	public String getSendtype() {
		return sendtype;
	}
	public void setSendtype(String sendtype) {
		this.sendtype = sendtype;
	}
	
	
}
