package com.ai.bdx.pop.bean;

/**
 * @ClassName: EDRBean
 * @Description: EDR日志文件实体BEAN
 * @author jinlong
 * @date 2015-10-19 下午3:03:15
 * 
 */
public class EDRBean {
	private String triggerType;//标识产生此EDR单据的消息类型
	private String time;//EDR单据产生的时间，格式为YYYY-MM-DD HH:MM:SS，本地时间
	private String subscriberIdentifier;//IMSI或者MSISDN或用户名
	private String IMSI;//IMSI
	private String MSISDN;//MSISDN
	private String IMEI;//IMEI，可以通过IMEI查询终端类型
	private String paidType;//付费类型
	private String category;//用户类别
	private String homeServiceZone;//归属服务区名称
	private String visitServiceZone;//当前服务区
	private String CGI;//拜访地小区，由MCC、MNC、LAC、CI组成
	private String SAI;//拜访地服务区域，由MCC、MNC、LAC、SAC组成
	private String RAI;//拜访地路由区，由MCC、MNC、LAC、RAC组成
	private String SGSNAddress;//拜访地SGSN地址（IPv4/IPv6地址）
	private String MCCMNC;//拜访地移动国家代码，移动网络代码
	private String roamStatus;//漫游类型
	private String roamRegion;//漫游区
	private String accessType;//接入类型(RAT)
	private String IPCanType;//接入方式
	private String UEIP;//UE IP（IPv4地址）
	private String APN;//APN
	private String downloadBandwidth;//会话级下行带宽，0-4294967295，单位为bit/s
	private String uploadBandwidth;//会话级上行带宽，0~4294967295，单位为bit/s
	private String servicePackageName;//套餐名称
	private String serviceName;//业务名称
	private String ruleName;//Rule名称
	private String quotaName;//Quota名称
	private String quotaStatus;//Quota状态
	private String quotaConsumption;//Quota累积量，如果为流量单位为KB，如果为时间单位为秒
	private String quotaBalance;//余额，如果为流量单位为KB，如果为时间单位为秒
	private String quotaRecharge;//补充配额，如果为流量单位为KB，如果为时间单位为秒
	private String quotaUsage;//本次使用量，如果为流量单位为KB，如果为时间单位为秒
	private String quotaNextResetTime;//Quota下次复位的时间，格式为YYYY-MM-DD HH:MM:SS，本地时间
	private String accountName;//Account名称
	private String accountStatus;//Account状态
	private String accountPrivilege;//Account特权信息
	private String personalValue;//自定义Value，小数点后保留4位，最小货币单位
	private String accountBalance;//Account余额，小数点后保留4位，最小货币单位
	private String accountConsumption;//Account使用量，小数点后保留4位，最小货币单位
	private String accountUsage;//Account本次使用量，小数点后保留4位，最小货币单位
	private String PCRFIPAddress;//PCRF与PCEF对接用的IP地址（IPv4地址）
	private String accountNextResetTime;//Account下次复位的时间，格式为YYYY-MM-DD HH:MM:SS，本地时间
	private String UEIPv6;//UE IP（IPv6地址）
	private String ANGWAddress;//接入网网关地址（IPv4/IPv6地址）
	private String accessNodeStatus;//小区拥塞状态。
	private String TAI;//拜访地服务区域，由MCC、MNC、TAC组成。
	private String ECGI;//拜访地小区，由MCC、MNC、ECI组成。
	private String underControl;//用户受控状态
	private String oldQuotaStatus;//修改前Quota状态
	private String oldQuotaConsumption;//修改前Quota累积量，如果为流量单位为KB，如果为时间单位为秒
	private String oldQuotaBalance;	//修改前余额，如果为流量单位为KB，如果为时间单位为秒
	private String quotaResetTime;//Quota本次（计划）复位的时间（注意不是实际复位发生时间），格式为YYYY-MM-DD HH:MM:SS，本地时间
	private String oldAccountStatus;//修改前Account状态
	private String oldAccountPrivilege;//修改前Account特权信息
	private String oldAccountBalance;//修改前Account余额，小数点后保留4位，最小货币单位
	private String oldAccountConsumption;//修改前Account使用量，小数点后保留4位，最小货币单位
	private String accountResetTime;//Account本次复位的时间（注意不是实际复位发生时间），格式为YYYY-MM-DD HH:MM:SS，本地时间
	private String quotaLastResetTime;//Quota本周期开始时间，格式为YYYY-MM-DD HH:MM:SS，本地时间
	private String roamRegion2;//漫游区2
	private String roamRegion3;//漫游区3
	private String roamRegion4;//漫游区4
	private String roamRegion5;//漫游区5
	private String roamRegion6;//漫游区6
	private String roamRegion7;//漫游区7
	private String roamRegion8;//漫游区8
	private String roamRegion9;//漫游区9
	private String roamRegion10;//漫游区10
	private String quotaValueOrStatus;//触发配额的EDR的原因
	private String visitServiceZone2;//当前服务区2
	private String visitServiceZone3;//当前服务区3
	private String visitServiceZone4;//当前服务区4
	private String visitServiceZone5;//当前服务区5
	public String getTriggerType() {
		return triggerType;
	}
	public void setTriggerType(String triggerType) {
		this.triggerType = triggerType;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getSubscriberIdentifier() {
		return subscriberIdentifier;
	}
	public void setSubscriberIdentifier(String subscriberIdentifier) {
		this.subscriberIdentifier = subscriberIdentifier;
	}
	public String getIMSI() {
		return IMSI;
	}
	public void setIMSI(String iMSI) {
		IMSI = iMSI;
	}
	public String getMSISDN() {
		return MSISDN;
	}
	public void setMSISDN(String mSISDN) {
		MSISDN = mSISDN;
	}
	public String getIMEI() {
		return IMEI;
	}
	public void setIMEI(String iMEI) {
		IMEI = iMEI;
	}
	public String getPaidType() {
		return paidType;
	}
	public void setPaidType(String paidType) {
		this.paidType = paidType;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getHomeServiceZone() {
		return homeServiceZone;
	}
	public void setHomeServiceZone(String homeServiceZone) {
		this.homeServiceZone = homeServiceZone;
	}
	public String getVisitServiceZone() {
		return visitServiceZone;
	}
	public void setVisitServiceZone(String visitServiceZone) {
		this.visitServiceZone = visitServiceZone;
	}
	public String getCGI() {
		return CGI;
	}
	public void setCGI(String cGI) {
		CGI = cGI;
	}
	public String getSAI() {
		return SAI;
	}
	public void setSAI(String sAI) {
		SAI = sAI;
	}
	public String getRAI() {
		return RAI;
	}
	public void setRAI(String rAI) {
		RAI = rAI;
	}
	public String getSGSNAddress() {
		return SGSNAddress;
	}
	public void setSGSNAddress(String sGSNAddress) {
		SGSNAddress = sGSNAddress;
	}
	public String getMCCMNC() {
		return MCCMNC;
	}
	public void setMCCMNC(String mCCMNC) {
		MCCMNC = mCCMNC;
	}
	public String getRoamStatus() {
		return roamStatus;
	}
	public void setRoamStatus(String roamStatus) {
		this.roamStatus = roamStatus;
	}
	public String getRoamRegion() {
		return roamRegion;
	}
	public void setRoamRegion(String roamRegion) {
		this.roamRegion = roamRegion;
	}
	public String getAccessType() {
		return accessType;
	}
	public void setAccessType(String accessType) {
		this.accessType = accessType;
	}
	public String getIPCanType() {
		return IPCanType;
	}
	public void setIPCanType(String iPCanType) {
		IPCanType = iPCanType;
	}
	public String getUEIP() {
		return UEIP;
	}
	public void setUEIP(String uEIP) {
		UEIP = uEIP;
	}
	public String getAPN() {
		return APN;
	}
	public void setAPN(String aPN) {
		APN = aPN;
	}
	public String getDownloadBandwidth() {
		return downloadBandwidth;
	}
	public void setDownloadBandwidth(String downloadBandwidth) {
		this.downloadBandwidth = downloadBandwidth;
	}
	public String getUploadBandwidth() {
		return uploadBandwidth;
	}
	public void setUploadBandwidth(String uploadBandwidth) {
		this.uploadBandwidth = uploadBandwidth;
	}
	public String getServicePackageName() {
		return servicePackageName;
	}
	public void setServicePackageName(String servicePackageName) {
		this.servicePackageName = servicePackageName;
	}
	public String getServiceName() {
		return serviceName;
	}
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
	public String getRuleName() {
		return ruleName;
	}
	public void setRuleName(String ruleName) {
		this.ruleName = ruleName;
	}
	public String getQuotaName() {
		return quotaName;
	}
	public void setQuotaName(String quotaName) {
		this.quotaName = quotaName;
	}
	public String getQuotaStatus() {
		return quotaStatus;
	}
	public void setQuotaStatus(String quotaStatus) {
		this.quotaStatus = quotaStatus;
	}
	public String getQuotaConsumption() {
		return quotaConsumption;
	}
	public void setQuotaConsumption(String quotaConsumption) {
		this.quotaConsumption = quotaConsumption;
	}
	public String getQuotaBalance() {
		return quotaBalance;
	}
	public void setQuotaBalance(String quotaBalance) {
		this.quotaBalance = quotaBalance;
	}
	public String getQuotaRecharge() {
		return quotaRecharge;
	}
	public void setQuotaRecharge(String quotaRecharge) {
		this.quotaRecharge = quotaRecharge;
	}
	public String getQuotaUsage() {
		return quotaUsage;
	}
	public void setQuotaUsage(String quotaUsage) {
		this.quotaUsage = quotaUsage;
	}
	public String getQuotaNextResetTime() {
		return quotaNextResetTime;
	}
	public void setQuotaNextResetTime(String quotaNextResetTime) {
		this.quotaNextResetTime = quotaNextResetTime;
	}
	public String getAccountName() {
		return accountName;
	}
	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}
	public String getAccountStatus() {
		return accountStatus;
	}
	public void setAccountStatus(String accountStatus) {
		this.accountStatus = accountStatus;
	}
	public String getAccountPrivilege() {
		return accountPrivilege;
	}
	public void setAccountPrivilege(String accountPrivilege) {
		this.accountPrivilege = accountPrivilege;
	}
	public String getPersonalValue() {
		return personalValue;
	}
	public void setPersonalValue(String personalValue) {
		this.personalValue = personalValue;
	}
	public String getAccountBalance() {
		return accountBalance;
	}
	public void setAccountBalance(String accountBalance) {
		this.accountBalance = accountBalance;
	}
	public String getAccountConsumption() {
		return accountConsumption;
	}
	public void setAccountConsumption(String accountConsumption) {
		this.accountConsumption = accountConsumption;
	}
	public String getAccountUsage() {
		return accountUsage;
	}
	public void setAccountUsage(String accountUsage) {
		this.accountUsage = accountUsage;
	}
	public String getPCRFIPAddress() {
		return PCRFIPAddress;
	}
	public void setPCRFIPAddress(String pCRFIPAddress) {
		PCRFIPAddress = pCRFIPAddress;
	}
	public String getAccountNextResetTime() {
		return accountNextResetTime;
	}
	public void setAccountNextResetTime(String accountNextResetTime) {
		this.accountNextResetTime = accountNextResetTime;
	}
	public String getUEIPv6() {
		return UEIPv6;
	}
	public void setUEIPv6(String uEIPv6) {
		UEIPv6 = uEIPv6;
	}
	public String getANGWAddress() {
		return ANGWAddress;
	}
	public void setANGWAddress(String aNGWAddress) {
		ANGWAddress = aNGWAddress;
	}
	public String getAccessNodeStatus() {
		return accessNodeStatus;
	}
	public void setAccessNodeStatus(String accessNodeStatus) {
		this.accessNodeStatus = accessNodeStatus;
	}
	public String getTAI() {
		return TAI;
	}
	public void setTAI(String tAI) {
		TAI = tAI;
	}
	public String getECGI() {
		return ECGI;
	}
	public void setECGI(String eCGI) {
		ECGI = eCGI;
	}
	public String getUnderControl() {
		return underControl;
	}
	public void setUnderControl(String underControl) {
		this.underControl = underControl;
	}
	public String getOldQuotaStatus() {
		return oldQuotaStatus;
	}
	public void setOldQuotaStatus(String oldQuotaStatus) {
		this.oldQuotaStatus = oldQuotaStatus;
	}
	public String getOldQuotaConsumption() {
		return oldQuotaConsumption;
	}
	public void setOldQuotaConsumption(String oldQuotaConsumption) {
		this.oldQuotaConsumption = oldQuotaConsumption;
	}
	public String getOldQuotaBalance() {
		return oldQuotaBalance;
	}
	public void setOldQuotaBalance(String oldQuotaBalance) {
		this.oldQuotaBalance = oldQuotaBalance;
	}
	public String getQuotaResetTime() {
		return quotaResetTime;
	}
	public void setQuotaResetTime(String quotaResetTime) {
		this.quotaResetTime = quotaResetTime;
	}
	public String getOldAccountStatus() {
		return oldAccountStatus;
	}
	public void setOldAccountStatus(String oldAccountStatus) {
		this.oldAccountStatus = oldAccountStatus;
	}
	public String getOldAccountPrivilege() {
		return oldAccountPrivilege;
	}
	public void setOldAccountPrivilege(String oldAccountPrivilege) {
		this.oldAccountPrivilege = oldAccountPrivilege;
	}
	public String getOldAccountBalance() {
		return oldAccountBalance;
	}
	public void setOldAccountBalance(String oldAccountBalance) {
		this.oldAccountBalance = oldAccountBalance;
	}
	public String getOldAccountConsumption() {
		return oldAccountConsumption;
	}
	public void setOldAccountConsumption(String oldAccountConsumption) {
		this.oldAccountConsumption = oldAccountConsumption;
	}
	public String getAccountResetTime() {
		return accountResetTime;
	}
	public void setAccountResetTime(String accountResetTime) {
		this.accountResetTime = accountResetTime;
	}
	public String getQuotaLastResetTime() {
		return quotaLastResetTime;
	}
	public void setQuotaLastResetTime(String quotaLastResetTime) {
		this.quotaLastResetTime = quotaLastResetTime;
	}
	public String getRoamRegion2() {
		return roamRegion2;
	}
	public void setRoamRegion2(String roamRegion2) {
		this.roamRegion2 = roamRegion2;
	}
	public String getRoamRegion3() {
		return roamRegion3;
	}
	public void setRoamRegion3(String roamRegion3) {
		this.roamRegion3 = roamRegion3;
	}
	public String getRoamRegion4() {
		return roamRegion4;
	}
	public void setRoamRegion4(String roamRegion4) {
		this.roamRegion4 = roamRegion4;
	}
	public String getRoamRegion5() {
		return roamRegion5;
	}
	public void setRoamRegion5(String roamRegion5) {
		this.roamRegion5 = roamRegion5;
	}
	public String getRoamRegion6() {
		return roamRegion6;
	}
	public void setRoamRegion6(String roamRegion6) {
		this.roamRegion6 = roamRegion6;
	}
	public String getRoamRegion7() {
		return roamRegion7;
	}
	public void setRoamRegion7(String roamRegion7) {
		this.roamRegion7 = roamRegion7;
	}
	public String getRoamRegion8() {
		return roamRegion8;
	}
	public void setRoamRegion8(String roamRegion8) {
		this.roamRegion8 = roamRegion8;
	}
	public String getRoamRegion9() {
		return roamRegion9;
	}
	public void setRoamRegion9(String roamRegion9) {
		this.roamRegion9 = roamRegion9;
	}
	public String getRoamRegion10() {
		return roamRegion10;
	}
	public void setRoamRegion10(String roamRegion10) {
		this.roamRegion10 = roamRegion10;
	}
	public String getQuotaValueOrStatus() {
		return quotaValueOrStatus;
	}
	public void setQuotaValueOrStatus(String quotaValueOrStatus) {
		this.quotaValueOrStatus = quotaValueOrStatus;
	}
	public String getVisitServiceZone2() {
		return visitServiceZone2;
	}
	public void setVisitServiceZone2(String visitServiceZone2) {
		this.visitServiceZone2 = visitServiceZone2;
	}
	public String getVisitServiceZone3() {
		return visitServiceZone3;
	}
	public void setVisitServiceZone3(String visitServiceZone3) {
		this.visitServiceZone3 = visitServiceZone3;
	}
	public String getVisitServiceZone4() {
		return visitServiceZone4;
	}
	public void setVisitServiceZone4(String visitServiceZone4) {
		this.visitServiceZone4 = visitServiceZone4;
	}
	public String getVisitServiceZone5() {
		return visitServiceZone5;
	}
	public void setVisitServiceZone5(String visitServiceZone5) {
		this.visitServiceZone5 = visitServiceZone5;
	}
	
}
