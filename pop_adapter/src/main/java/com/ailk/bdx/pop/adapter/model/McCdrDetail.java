package com.ailk.bdx.pop.adapter.model;

import java.util.Map;
 

 
public class McCdrDetail {
	/**
	 * 消息头表示此次消息类型
	 * 1：验证信息请求
	 * 2：验证信息通过
	 * 3：验证信息失败
	 * 4：验证通过发送数据
	 */
	private byte headType;
	/**
	 * 用户名char数组必须保证在16个长度之内
	 */
	private char[] userName;
	/**
	 * 密码char数组必须保证在16个长度之类
	 */
	private char[] passWord;
	/**
	 * 消息体长度
	 */
	private int len;
	/**
	 * 消息内容
	 */
	private String message;
	/**
	 * 需要的字段 
	 * */
	private String columnStr;
	
	private int columnLen;
	
	/**
	 * 消息内容 map存储
	 */
	private Map messageMap; 
	
	
 
//	public static class MessageDetail{
//		private byte headType;
//		/**
//		 * 开始时间
//		 * 事件开始时间: 第一条信令的时间
//		 * 例如20130510 14:00:00:000 
//		 */
//		private String	btime;
//		/**
//		 * 结束时间
//		 * 事件结束时间, 最后一条信令的时间
//		 * 例如20130510 14:00:00:000 
//		 */
//		private String	etime;
//		/**
//		 * 协议
//		 * 定义值
//		 * 111：GSM-BSSAP；
//		 * 113：RANAP；
//		 */
//		private String	protocolid;
//		/**
//		 *  1、 主叫
//		 *	2、 紧急呼叫
//		 *	3、 被叫
//		 *	4、 视频主叫
//		 *	5、 视频被叫
//		 *	6、 发短信
//		 *	7、 收短信
//		 *	8、 切入
//		 *	9、 切出（BSSAP为2G-2G,ranap为3g-3g）
//		 *	10、BSC内切换
//		 *	12、正常位置更新
//		 *	13、周期性位置更新
//		 *	14、IMSI附着
//		 *	15、IMSI分离
//		 *	16、寻呼
//		 *	17、补充业务
//		 *	25、短信状态报告
//		 *	26、切出（3G-2G）
//		 *	27、业务重建
//		 *	28、手机状态报告
//		 *	其中4、5、26只对3G有效，10只对2G有效，其他两者有效
//		 */
//		private String	eventid;
//		/**
//		 * 编码类别
//		 * 14：14位编码；24：24位编码
//		 */
//		private String spckind;           //--------------------
//		/**
//		 * MSC信令点编码
//		 * 十进制形式，例如0xCBDE，这里要填对应的十进制52190
//		 */
//		private	String msccode;
//		/**
//		 * BSC信令点编码
//		 * 十进制形式，例如0xCBDE，这里要填对应的十进制52190
//		 */
//		private String bsccode;
//		/**
//		 * 当前位置区
//		 * 十进制形式，例如0xCBDE，这里要填对应的十进制52190
//		 */
//		private String lac;
//		/**
//		 * 当前小区或者SAC
//		 * 十进制形式，例如0xCBDE，这里要填对应的十进制52190
//		 */
//		private String ci;
//		/**
//		 * 源位置区
//		 * 只对切换和位置更新有效,十进制形式，例如0xCBDE，这里要填对应的十进制52190
//		 */
//		private String olac;
//		/**
//		 * 源小区
//		 * 只对切换和位置更新有效,十进制形式，例如0xCBDE，这里要填对应的十进制52190
//		 */
//		private String oci;
//		/**
//		 * 目的位置区
//		 * 只对切换有效,十进制形式，例如0xCBDE，这里要填对应的十进制52190
//		 */
//		private String dlac;
//		/**
//		 * 目的小区
//		 * 只对切换有效,十进制形式，例如0xCBDE，这里要填对应的十进制52190
//		 */
//		private String dci;
//		/**
//		 * 开始位置区
//		 * 事件第一条消息所在小区。
//		 *对切入：同当前小区。
//		 *对呼叫：起呼或寻呼响应时所在小区，同当前小区；
//		 */
//		private String firstlac;
//		/**
//		 * 开始小区
//		 * 事件第一条消息所在小区
//		 */
//		private String	firstci;
//		/**
//		 * 结束位置区
//		 * 事件最后一条消息所在小区
//		 */
//		private String	lastlac;
//		/**
//		 * 结束小区
//		 * 事件最后一条消息所在小区
//		 */
//		private String lastci;
//		/**
//		 * 目的RNCID
//		 * Ranap切换有效
//		 * 取RELOCATION REQUEST/ Relocation Required中的TargetID值
//		 */
//		private String drncid;
//		/**
//		 * 主叫号码
//		 */
//		private String callingnum;
//		/**
//		 * 被叫号码
//		 */
//		private String callednum;
//		/**
//		 * 主叫IMSI
//		 */
//		private String callingimsi;
//		/**
//		 * 被叫IMSI
//		 */
//		private String calledimsi;
//		/**
//		 * 主叫IMEI
//		 */
//		private String calledimei;
//		/**
//		 * 事件结果
//		 */
//		private String eventresult;
//		/**
//		 * 切出请求原因
//		 * 切换申请中的原因，表示由于哪种原因导致切换
//		 */
//		private String handoutrequirecause;
//		/**
//		 * 切出开始时间
//		 * 切出：切换申请时间
//		 */
//		private String handoutoffset;
//		/**
//		 * 切出响应时间
//		 * 切出：切出申请拒绝时间或者切换命令或者切换失败的时间
//		 */
//		private String handoutrspoffset;
//		/**
//		 * 切入开始时间
//		 * 切入：切换请求时间
//		 */
//		private String handinoffset;
//		/**
//		 * 切入响应时间
//		 * 切入：切换请求证实、切换检测到、切换完成、切换失败，4种消息中最后出现的消息的时间
//		 */
//		private String handinrspoffset;
//		/**
//		 * 切出状态
//		 * BSSAP:
//		 * 0:以下都没有收到
//	     * 1:切换命令(Handover command)
//	     * 2:切换失败(Handover failure)
//	     * 3:切出申请拒绝(Handover required reject)
//		 *	RANAP:
//		 *	0:以下都没有收到
//		 *	1:切换命令(Relocation Command)
//		 *	2:切换失败(Relocation preparation failure)
//		 *	只对切出事件有效
//		 */
//		private String handoutstatus;
//		/**
//		 * 切入状态
//		 * BSSAP:
//		 * 0:以下都没有收到
//		 * 1:切换完成(Handover Complete)
//		 * 2:切换失败(Handover failure)
//		 * 3:收到切换请求证实，后续消息没有收到(Handover Request Acknowledge)
//		 * 4:收到切换检测到，后续消息没有收到(Handover Detect)
//		 * RANAP:
//		 * 0:以下都没有收到
//		 * 1:切换完成(Relocation complete)
//		 * 2:切换失败(Relocation failure)
//		 * 3:收到切换请求证实，后续消息没有收到(Relocation Request Acknowledge)
//		 * 4:收到切换检测到，后续消息没有收到(Relocation Detect)
//		 * 只对切入事件有效
//		 */
//		private String handinstatus;
//		/**
//		 * 位置更新状态
//		 * 0:以下都没有收到
//		 * 1:位置更新接受Location updating accept
//		 * 2:位置更新拒绝Location updating reject
//		 */
//		private String lustatus;
//		/**
//		 * 切换标志
//		 * 对切出和内部切换有效
//		 * 0：主叫/切入中的切出
//	     * 1：被叫中的切出
//		 */
//		private String hoflag;
//		/**
//		 * 振铃时间
//		 * 单位: 毫秒
//		 * alert时间-btime
//		 */
//		private String alertoffset;
//		/**
//		 * 应答时间
//		 * 单位: 毫秒
//		 * Connect时间-btime
//		 */
//		private String connoffset;
//		/**
//		 * 通话时长
//		 * 单位: 毫秒
//		 * Disconnect时间-connect时间，没有Disconnect时按顺序选用release、清除、RLC。都没有填充0.
//		 */
//		private String answerdur;
//		public byte getHeadType() {
//			return headType;
//		}
//		public void setHeadType(byte headType) {
//			this.headType = headType;
//		}
//		public String getBtime() {
//			return btime;
//		}
//		public void setBtime(String btime) {
//			this.btime = btime;
//		}
//		public String getEtime() {
//			return etime;
//		}
//		public void setEtime(String etime) {
//			this.etime = etime;
//		}
//		public String getProtocolid() {
//			return protocolid;
//		}
//		public void setProtocolid(String protocolid) {
//			this.protocolid = protocolid;
//		}
//		public String getEventid() {
//			return eventid;
//		}
//		public void setEventid(String eventid) {
//			this.eventid = eventid;
//		}
//		public String getSpckind() {
//			return spckind;
//		}
//		public void setSpckind(String spckind) {
//			this.spckind = spckind;
//		}
//		public String getMsccode() {
//			return msccode;
//		}
//		public void setMsccode(String msccode) {
//			this.msccode = msccode;
//		}
//		public String getBsccode() {
//			return bsccode;
//		}
//		public void setBsccode(String bsccode) {
//			this.bsccode = bsccode;
//		}
//		public String getLac() {
//			return lac;
//		}
//		public void setLac(String lac) {
//			this.lac = lac;
//		}
//		public String getCi() {
//			return ci;
//		}
//		public void setCi(String ci) {
//			this.ci = ci;
//		}
//		public String getOlac() {
//			return olac;
//		}
//		public void setOlac(String olac) {
//			this.olac = olac;
//		}
//		public String getOci() {
//			return oci;
//		}
//		public void setOci(String oci) {
//			this.oci = oci;
//		}
//		public String getDlac() {
//			return dlac;
//		}
//		public void setDlac(String dlac) {
//			this.dlac = dlac;
//		}
//		public String getDci() {
//			return dci;
//		}
//		public void setDci(String dci) {
//			this.dci = dci;
//		}
//		public String getFirstlac() {
//			return firstlac;
//		}
//		public void setFirstlac(String firstlac) {
//			this.firstlac = firstlac;
//		}
//		public String getFirstci() {
//			return firstci;
//		}
//		public void setFirstci(String firstci) {
//			this.firstci = firstci;
//		}
//		public String getLastlac() {
//			return lastlac;
//		}
//		public void setLastlac(String lastlac) {
//			this.lastlac = lastlac;
//		}
//		public String getLastci() {
//			return lastci;
//		}
//		public void setLastci(String lastci) {
//			this.lastci = lastci;
//		}
//		public String getDrncid() {
//			return drncid;
//		}
//		public void setDrncid(String drncid) {
//			this.drncid = drncid;
//		}
//		public String getCallingnum() {
//			return callingnum;
//		}
//		public void setCallingnum(String callingnum) {
//			this.callingnum = callingnum;
//		}
//		public String getCallednum() {
//			return callednum;
//		}
//		public void setCallednum(String callednum) {
//			this.callednum = callednum;
//		}
//		public String getCallingimsi() {
//			return callingimsi;
//		}
//		public void setCallingimsi(String callingimsi) {
//			this.callingimsi = callingimsi;
//		}
//		public String getCalledimsi() {
//			return calledimsi;
//		}
//		public void setCalledimsi(String calledimsi) {
//			this.calledimsi = calledimsi;
//		}
//		public String getCalledimei() {
//			return calledimei;
//		}
//		public void setCalledimei(String calledimei) {
//			this.calledimei = calledimei;
//		}
//		public String getEventresult() {
//			return eventresult;
//		}
//		public void setEventresult(String eventresult) {
//			this.eventresult = eventresult;
//		}
//		public String getHandoutrequirecause() {
//			return handoutrequirecause;
//		}
//		public void setHandoutrequirecause(String handoutrequirecause) {
//			this.handoutrequirecause = handoutrequirecause;
//		}
//		public String getHandoutoffset() {
//			return handoutoffset;
//		}
//		public void setHandoutoffset(String handoutoffset) {
//			this.handoutoffset = handoutoffset;
//		}
//		public String getHandoutrspoffset() {
//			return handoutrspoffset;
//		}
//		public void setHandoutrspoffset(String handoutrspoffset) {
//			this.handoutrspoffset = handoutrspoffset;
//		}
//		public String getHandinoffset() {
//			return handinoffset;
//		}
//		public void setHandinoffset(String handinoffset) {
//			this.handinoffset = handinoffset;
//		}
//		public String getHandinrspoffset() {
//			return handinrspoffset;
//		}
//		public void setHandinrspoffset(String handinrspoffset) {
//			this.handinrspoffset = handinrspoffset;
//		}
//		public String getHandoutstatus() {
//			return handoutstatus;
//		}
//		public void setHandoutstatus(String handoutstatus) {
//			this.handoutstatus = handoutstatus;
//		}
//		public String getHandinstatus() {
//			return handinstatus;
//		}
//		public void setHandinstatus(String handinstatus) {
//			this.handinstatus = handinstatus;
//		}
//		public String getLustatus() {
//			return lustatus;
//		}
//		public void setLustatus(String lustatus) {
//			this.lustatus = lustatus;
//		}
//		public String getHoflag() {
//			return hoflag;
//		}
//		public void setHoflag(String hoflag) {
//			this.hoflag = hoflag;
//		}
//		public String getAlertoffset() {
//			return alertoffset;
//		}
//		public void setAlertoffset(String alertoffset) {
//			this.alertoffset = alertoffset;
//		}
//		public String getConnoffset() {
//			return connoffset;
//		}
//		public void setConnoffset(String connoffset) {
//			this.connoffset = connoffset;
//		}
//		public String getAnswerdur() {
//			return answerdur;
//		}
//		public void setAnswerdur(String answerdur) {
//			this.answerdur = answerdur;
//		}
//		
//	}

	public int getColumnLen() {
		return columnLen;
	}

	public void setColumnLen(int columnLen) {
		this.columnLen = columnLen;
	}

	public byte getHeadType() {
		return headType;
	}

	public void setHeadType(byte headType) {
		this.headType = headType;
	}

	public char[] getUserName() {
		return userName;
	}

	public void setUserName(char[] userName) {
		this.userName = userName;
	}

	public char[] getPassWord() {
		return passWord;
	}

	public void setPassWord(char[] passWord) {
		this.passWord = passWord;
	}

	public int getLen() {
		return len;
	}

	public void setLen(int len) {
		this.len = len;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Map getMessageMap() {
		return messageMap;
	}

	public void setMessageMap(Map messageMap) {
		this.messageMap = messageMap;
	}

	public String getColumnStr() {
		return columnStr;
	}

	public void setColumnStr(String columnStr) {
		this.columnStr = columnStr;
	}
	
}
