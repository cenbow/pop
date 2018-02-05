/**   
 * @Title: DimProdProductDmBean.java
 * @Package com.ai.bdx.pop.bean
 * @Description: TODO(用一句话描述该文件做什么)
 * @author A18ccms A18ccms_gmail_com   
 * @date 2015-6-11 下午5:54:44
 * @version V1.0   
 */
package com.ai.bdx.pop.bean;

import java.util.Date;

/**
 * @ClassName: DimProdProductDmBean
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author jinlong
 * @date 2015-6-11 下午5:54:44
 * 
 */
public class DimProdProductDmBean {

	private String  prodId; 			//`PROD_ID` VARCHAR(256) COMMENT '产品标识',
	private String  prodName;  			//`PROD_NAME` VARCHAR(256) COMMENT '产品名称',
	private String  prodType; 			// `PROD_TYPE` VARCHAR(256) COMMENT '产品类型',
	private String  netType;  			// `NET_TYPE` VARCHAR(256) COMMENT '网络类型',
	private String  mainProdFlag;  			// `MAIN_PROD_FLAG` SMALLINT COMMENT '主产品标志',
	private String  prodKind;  			// `PROD_KIND` VARCHAR(256) COMMENT '产品分类',
	private String  prodSrc;  			// `PROD_SRC` VARCHAR(256) COMMENT '产品来源',
	private String  prodPkgType;  			// `PROD_PKG_TYPE` INT COMMENT '产品包类型',
	private Date  effTime;  			// `EFF_TIME` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE     CURRENT_TIMESTAMP COMMENT '生效时间',
	private Date  expTime;  			// `EXP_TIME` TIMESTAMP DEFAULT '0000-00-00 00:00:00' COMMENT '失效时间',
	private String  mangCatalog;  			// `MANG_CATALOG` VARCHAR(256) COMMENT '管理目录',
	private String  sellCatalog;  			// `SELL_CATALOG` VARCHAR(256) COMMENT '销售目录',
	private String  createOrgId;  			// `CREATE_ORG_ID` VARCHAR(256) COMMENT '创建机构标识',
	private Date  createTime;  			// `CREATE_TIME` TIMESTAMP DEFAULT '0000-00-00 00:00:00' COMMENT '创建时间',
	private String  state;  			// `STATE` VARCHAR(256) COMMENT '状态',
	private Date  stateTime;  			// `STATE_TIME` TIMESTAMP DEFAULT '0000-00-00 00:00:00' COMMENT '状态时间',
	private String  prodPricingMode;  			// `PROD_PRICING_MODE` SMALLINT COMMENT '产品定价方式',
	private String  provProdFlag;  			// `PROV_PROD_FLAG` SMALLINT COMMENT '省产品标志',
	private String  happyFmlyFlag;  			// `HAPPY_FMLY_FLAG` SMALLINT COMMENT '合家欢产品标志',
	private String  mailCardFlag;  			// `MAIL_CARD_FLAG` SMALLINT COMMENT '夹寄卡产品标志',
	private String  officialFlag;  			// `OFFICIAL_FLAG` SMALLINT COMMENT '公务产品标志',
	private String  freeFlag;  			// `FREE_FLAG` SMALLINT COMMENT '公免产品标志',
	private String  testCardFlag;  			// `TEST_CARD_FLAG` SMALLINT COMMENT '测试产品标志',
	private String  staffCardFlag;  			// `STAFF_CARD_FLAG` SMALLINT COMMENT '员工产品标志',
	private String  publicPhoneFlag;  			// `PUBLIC_PHONE_FLAG` SMALLINT COMMENT '公话产品标志',
	private String  dataCardFlag;  			// `DATA_CARD_FLAG` SMALLINT COMMENT '数据卡产品标志',
	private String  m2mFlag;  			// `M2M_FLAG` SMALLINT COMMENT 'M2M产品标志',
	private String  wireless_phoneFlag;  			// `WIRELESS_PHONE_FLAG` SMALLINT COMMENT '无线座机产品标志',
	private String  flowFlag;  			// `FLOW_FLAG` SMALLINT COMMENT '流量产品标志',
	private String  infodFlag;  			// `INFOD_FLAG` SMALLINT COMMENT '个人信息化产品标志',
	private String  mainPriceFlag;  			// `MAIN_PRICE_FLAG` SMALLINT COMMENT '主资费标志',
	private String  brandId;  			// `BRAND_ID` VARCHAR(256) COMMENT '品牌标识',
	private String  statMonth;  			// `STAT_MONTH` VARCHAR(256) COMMENT '统计月份',
	private String  statDate;  			// `STAT_DATE` VARCHAR(256) COMMENT '统计日期'
	
	
	public String getProdId() {
		return prodId;
	}
	public void setProdId(String prodId) {
		this.prodId = prodId;
	}
	public String getProdName() {
		return prodName;
	}
	public void setProdName(String prodName) {
		this.prodName = prodName;
	}
	public String getProdType() {
		return prodType;
	}
	public void setProdType(String prodType) {
		this.prodType = prodType;
	}
	public String getNetType() {
		return netType;
	}
	public void setNetType(String netType) {
		this.netType = netType;
	}
	public String getMainProdFlag() {
		return mainProdFlag;
	}
	public void setMainProdFlag(String mainProdFlag) {
		this.mainProdFlag = mainProdFlag;
	}
	public String getProdKind() {
		return prodKind;
	}
	public void setProdKind(String prodKind) {
		this.prodKind = prodKind;
	}
	public String getProdSrc() {
		return prodSrc;
	}
	public void setProdSrc(String prodSrc) {
		this.prodSrc = prodSrc;
	}
	public String getProdPkgType() {
		return prodPkgType;
	}
	public void setProdPkgType(String prodPkgType) {
		this.prodPkgType = prodPkgType;
	}
	
	public Date getEffTime() {
		return effTime;
	}
	public void setEffTime(Date effTime) {
		this.effTime = effTime;
	}
	public Date getExpTime() {
		return expTime;
	}
	public void setExpTime(Date expTime) {
		this.expTime = expTime;
	}
	public String getMangCatalog() {
		return mangCatalog;
	}
	public void setMangCatalog(String mangCatalog) {
		this.mangCatalog = mangCatalog;
	}
	public String getSellCatalog() {
		return sellCatalog;
	}
	public void setSellCatalog(String sellCatalog) {
		this.sellCatalog = sellCatalog;
	}
	public String getCreateOrgId() {
		return createOrgId;
	}
	public void setCreateOrgId(String createOrgId) {
		this.createOrgId = createOrgId;
	}
	
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	
	public Date getStateTime() {
		return stateTime;
	}
	public void setStateTime(Date stateTime) {
		this.stateTime = stateTime;
	}
	public String getProdPricingMode() {
		return prodPricingMode;
	}
	public void setProdPricingMode(String prodPricingMode) {
		this.prodPricingMode = prodPricingMode;
	}
	public String getProvProdFlag() {
		return provProdFlag;
	}
	public void setProvProdFlag(String provProdFlag) {
		this.provProdFlag = provProdFlag;
	}
	public String getHappyFmlyFlag() {
		return happyFmlyFlag;
	}
	public void setHappyFmlyFlag(String happyFmlyFlag) {
		this.happyFmlyFlag = happyFmlyFlag;
	}
	public String getMailCardFlag() {
		return mailCardFlag;
	}
	public void setMailCardFlag(String mailCardFlag) {
		this.mailCardFlag = mailCardFlag;
	}
	public String getOfficialFlag() {
		return officialFlag;
	}
	public void setOfficialFlag(String officialFlag) {
		this.officialFlag = officialFlag;
	}
	public String getFreeFlag() {
		return freeFlag;
	}
	public void setFreeFlag(String freeFlag) {
		this.freeFlag = freeFlag;
	}
	public String getTestCardFlag() {
		return testCardFlag;
	}
	public void setTestCardFlag(String testCardFlag) {
		this.testCardFlag = testCardFlag;
	}
	public String getStaffCardFlag() {
		return staffCardFlag;
	}
	public void setStaffCardFlag(String staffCardFlag) {
		this.staffCardFlag = staffCardFlag;
	}
	public String getPublicPhoneFlag() {
		return publicPhoneFlag;
	}
	public void setPublicPhoneFlag(String publicPhoneFlag) {
		this.publicPhoneFlag = publicPhoneFlag;
	}
	public String getDataCardFlag() {
		return dataCardFlag;
	}
	public void setDataCardFlag(String dataCardFlag) {
		this.dataCardFlag = dataCardFlag;
	}
	public String getM2mFlag() {
		return m2mFlag;
	}
	public void setM2mFlag(String m2mFlag) {
		this.m2mFlag = m2mFlag;
	}
	public String getWireless_phoneFlag() {
		return wireless_phoneFlag;
	}
	public void setWireless_phoneFlag(String wireless_phoneFlag) {
		this.wireless_phoneFlag = wireless_phoneFlag;
	}
	public String getFlowFlag() {
		return flowFlag;
	}
	public void setFlowFlag(String flowFlag) {
		this.flowFlag = flowFlag;
	}
	public String getInfodFlag() {
		return infodFlag;
	}
	public void setInfodFlag(String infodFlag) {
		this.infodFlag = infodFlag;
	}
	public String getMainPriceFlag() {
		return mainPriceFlag;
	}
	public void setMainPriceFlag(String mainPriceFlag) {
		this.mainPriceFlag = mainPriceFlag;
	}
	public String getBrandId() {
		return brandId;
	}
	public void setBrandId(String brandId) {
		this.brandId = brandId;
	}
	public String getStatMonth() {
		return statMonth;
	}
	public void setStatMonth(String statMonth) {
		this.statMonth = statMonth;
	}
	public String getStatDate() {
		return statDate;
	}
	public void setStatDate(String statDate) {
		this.statDate = statDate;
	}
	
	
	
     
}
