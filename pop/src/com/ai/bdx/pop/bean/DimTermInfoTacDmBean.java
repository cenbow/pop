/**   
 * @Title: DimTermInfoTacDmBean.java
 * @Package com.ai.bdx.pop.bean
 * @Description: TODO(用一句话描述该文件做什么)
 * @author A18ccms A18ccms_gmail_com   
 * @date 2015-6-12 下午2:25:43
 * @version V1.0   
 */
package com.ai.bdx.pop.bean;

/**
 * @ClassName: DimTermInfoTacDmBean
 * @Description: TODO(dim_term_info_tac_dm 实体类)
 * @author jinlong
 * @date 2015-6-12 下午2:25:43
 * 
 */
public class DimTermInfoTacDmBean {
	private String termId;	//	   `TERM_ID` VARCHAR(256),
	private String tac;	//       `TAC` VARCHAR(256),
	private String manufacturer;	//       `MANUFACTURER` VARCHAR(256),
	private String termBrand;	//       `TERM_BRAND` VARCHAR(256),
	private String model;	//       `MODEL` VARCHAR(256),
	private String iscustomFlag;	//       `ISCUSTOM_FLAG` SMALLINT,
	private String termType;	//       `TERM_TYPE` VARCHAR(256),
	private String dualsimFlag;	//       `DUALSIM_FLAG` SMALLINT,
	private String dualmodeType;	//       `DUALMODE_TYPE` VARCHAR(256),
	private String supwcdmaFlag;	//       `SUPWCDMA_FLAG` SMALLINT,
	private String gsmFlag;	//       `GSM_FLAG` SMALLINT,
	private String cdma2000Flag;	//       `CDMA2000_FLAG` SMALLINT,
	private String tdFlag;	//       `TD_FLAG` SMALLINT,
	private String mmsFlag;	//       `MMS_FLAG` SMALLINT,
	private String gprsFlag;	//       `GPRS_FLAG` SMALLINT,
	private String edgeFlag;	//       `EDGE_FLAG` SMALLINT,
	private String wifiFlag;	//       `WIFI_FLAG` SMALLINT,
	private String issmartFlag;	//       `ISSMART_FLAG` SMALLINT,
	private String ios;	//       `IOS` VARCHAR(256),
	private String iosVersion;	//       `IOS_VERSION` VARCHAR(256),
	private String mainScreenSize;	//       `MAIN_SCREEN_SIZE` VARCHAR(256),
	private String listingTime;	//       `LISTING_TIME` VARCHAR(256),
	private String listingPrice;	//       `LISTING_PRICE` VARCHAR(256),
	private String currentPrice;	//       `CURRENT_PRICE` VARCHAR(256),
	private String currentUpTime;	//       `CURRENT_UP_TIME` VARCHAR(256),
	private String hspaFlag;	//       `HSPA_FLAG` SMALLINT,
	private String lteFddFlag;	//       `LTE_FDD_FLAG` SMALLINT,
	private String lteTddFlag;	//       `LTE_TDD_FLAG` SMALLINT,
	private String lteSingleCardDualFlag;	//       `LTE_SINGLE_CARD_DUAL_FLAG` SMALLINT,
	private String volteFlag;	//       `VOLTE_FLAG` SMALLINT,
	private String csfbFlag;	//       `CSFB_FLAG` SMALLINT,
	private String wapFlag;	//       `WAP_FLAG` SMALLINT,
	private String wwwFlag;	//       `WWW_FLAG` SMALLINT,
	private String gpsFlag;	//       `GPS_FLAG` SMALLINT,
	private String preCameraPix;	//       `PRE_CAMERA_PIX` bigint,
	private String bacCameraPix;	//       `BAC_CAMERA_PIX` bigint,
	private String screenPix;	//       `SCREEN_PIX` VARCHAR(256),
	private String colorScreenDepth;	//       `COLOR_SCREEN_DEPTH` bigint,
	private String writeInput;	//       `WRITE_INPUT` VARCHAR(256),
	private String touchType;	//       `TOUCH_TYPE` VARCHAR(256),
	private String termStyle;	//       `TERM_STYLE` VARCHAR(256),
	private String javaFlag;	//       `JAVA_FLAG` SMALLINT,
	private String usbFlag;	//       `USB_FLAG` SMALLINT,
	private String bluetoothFlag;	//       `BLUETOOTH_FLAG` SMALLINT,
	private String infraredFlag;	//       `INFRARED_FLAG` SMALLINT,
	private String douCardDaulFlag;	//       `DOU_CARD_DAUL_FLAG` SMALLINT,
	private String statMonth;	//       `STAT_MONTH` VARCHAR(256),
	private String statDate;	//       `STAT_DATE` VARCHAR(256),
	public String getTermId() {
		return termId;
	}
	public void setTermId(String termId) {
		this.termId = termId;
	}
	public String getTac() {
		return tac;
	}
	public void setTac(String tac) {
		this.tac = tac;
	}
	public String getManufacturer() {
		return manufacturer;
	}
	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}
	public String getTermBrand() {
		return termBrand;
	}
	public void setTermBrand(String termBrand) {
		this.termBrand = termBrand;
	}
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
	public String getIscustomFlag() {
		return iscustomFlag;
	}
	public void setIscustomFlag(String iscustomFlag) {
		this.iscustomFlag = iscustomFlag;
	}
	public String getTermType() {
		return termType;
	}
	public void setTermType(String termType) {
		this.termType = termType;
	}
	public String getDualsimFlag() {
		return dualsimFlag;
	}
	public void setDualsimFlag(String dualsimFlag) {
		this.dualsimFlag = dualsimFlag;
	}
	public String getDualmodeType() {
		return dualmodeType;
	}
	public void setDualmodeType(String dualmodeType) {
		this.dualmodeType = dualmodeType;
	}
	public String getSupwcdmaFlag() {
		return supwcdmaFlag;
	}
	public void setSupwcdmaFlag(String supwcdmaFlag) {
		this.supwcdmaFlag = supwcdmaFlag;
	}
	public String getGsmFlag() {
		return gsmFlag;
	}
	public void setGsmFlag(String gsmFlag) {
		this.gsmFlag = gsmFlag;
	}
	public String getCdma2000Flag() {
		return cdma2000Flag;
	}
	public void setCdma2000Flag(String cdma2000Flag) {
		this.cdma2000Flag = cdma2000Flag;
	}
	public String getTdFlag() {
		return tdFlag;
	}
	public void setTdFlag(String tdFlag) {
		this.tdFlag = tdFlag;
	}
	public String getMmsFlag() {
		return mmsFlag;
	}
	public void setMmsFlag(String mmsFlag) {
		this.mmsFlag = mmsFlag;
	}
	public String getGprsFlag() {
		return gprsFlag;
	}
	public void setGprsFlag(String gprsFlag) {
		this.gprsFlag = gprsFlag;
	}
	public String getEdgeFlag() {
		return edgeFlag;
	}
	public void setEdgeFlag(String edgeFlag) {
		this.edgeFlag = edgeFlag;
	}
	public String getWifiFlag() {
		return wifiFlag;
	}
	public void setWifiFlag(String wifiFlag) {
		this.wifiFlag = wifiFlag;
	}
	public String getIssmartFlag() {
		return issmartFlag;
	}
	public void setIssmartFlag(String issmartFlag) {
		this.issmartFlag = issmartFlag;
	}
	public String getIos() {
		return ios;
	}
	public void setIos(String ios) {
		this.ios = ios;
	}
	
	public String getIosVersion() {
		return iosVersion;
	}
	public void setIosVersion(String iosVersion) {
		this.iosVersion = iosVersion;
	}
	public String getMainScreenSize() {
		return mainScreenSize;
	}
	public void setMainScreenSize(String mainScreenSize) {
		this.mainScreenSize = mainScreenSize;
	}
	public String getListingTime() {
		return listingTime;
	}
	public void setListingTime(String listingTime) {
		this.listingTime = listingTime;
	}
	public String getListingPrice() {
		return listingPrice;
	}
	public void setListingPrice(String listingPrice) {
		this.listingPrice = listingPrice;
	}
	public String getCurrentPrice() {
		return currentPrice;
	}
	public void setCurrentPrice(String currentPrice) {
		this.currentPrice = currentPrice;
	}
	public String getCurrentUpTime() {
		return currentUpTime;
	}
	public void setCurrentUpTime(String currentUpTime) {
		this.currentUpTime = currentUpTime;
	}
	public String getHspaFlag() {
		return hspaFlag;
	}
	public void setHspaFlag(String hspaFlag) {
		this.hspaFlag = hspaFlag;
	}
	public String getLteFddFlag() {
		return lteFddFlag;
	}
	public void setLteFddFlag(String lteFddFlag) {
		this.lteFddFlag = lteFddFlag;
	}
	public String getLteTddFlag() {
		return lteTddFlag;
	}
	public void setLteTddFlag(String lteTddFlag) {
		this.lteTddFlag = lteTddFlag;
	}
	public String getLteSingleCardDualFlag() {
		return lteSingleCardDualFlag;
	}
	public void setLteSingleCardDualFlag(String lteSingleCardDualFlag) {
		this.lteSingleCardDualFlag = lteSingleCardDualFlag;
	}
	public String getVolteFlag() {
		return volteFlag;
	}
	public void setVolteFlag(String volteFlag) {
		this.volteFlag = volteFlag;
	}
	public String getCsfbFlag() {
		return csfbFlag;
	}
	public void setCsfbFlag(String csfbFlag) {
		this.csfbFlag = csfbFlag;
	}
	public String getWapFlag() {
		return wapFlag;
	}
	public void setWapFlag(String wapFlag) {
		this.wapFlag = wapFlag;
	}
	public String getWwwFlag() {
		return wwwFlag;
	}
	public void setWwwFlag(String wwwFlag) {
		this.wwwFlag = wwwFlag;
	}
	public String getGpsFlag() {
		return gpsFlag;
	}
	public void setGpsFlag(String gpsFlag) {
		this.gpsFlag = gpsFlag;
	}
	public String getPreCameraPix() {
		return preCameraPix;
	}
	public void setPreCameraPix(String preCameraPix) {
		this.preCameraPix = preCameraPix;
	}
	public String getBacCameraPix() {
		return bacCameraPix;
	}
	public void setBacCameraPix(String bacCameraPix) {
		this.bacCameraPix = bacCameraPix;
	}
	public String getScreenPix() {
		return screenPix;
	}
	public void setScreenPix(String screenPix) {
		this.screenPix = screenPix;
	}
	public String getColorScreenDepth() {
		return colorScreenDepth;
	}
	public void setColorScreenDepth(String colorScreenDepth) {
		this.colorScreenDepth = colorScreenDepth;
	}
	public String getWriteInput() {
		return writeInput;
	}
	public void setWriteInput(String writeInput) {
		this.writeInput = writeInput;
	}
	public String getTouchType() {
		return touchType;
	}
	public void setTouchType(String touchType) {
		this.touchType = touchType;
	}
	public String getTermStyle() {
		return termStyle;
	}
	public void setTermStyle(String termStyle) {
		this.termStyle = termStyle;
	}
	public String getJavaFlag() {
		return javaFlag;
	}
	public void setJavaFlag(String javaFlag) {
		this.javaFlag = javaFlag;
	}
	public String getUsbFlag() {
		return usbFlag;
	}
	public void setUsbFlag(String usbFlag) {
		this.usbFlag = usbFlag;
	}
	public String getBluetoothFlag() {
		return bluetoothFlag;
	}
	public void setBluetoothFlag(String bluetoothFlag) {
		this.bluetoothFlag = bluetoothFlag;
	}
	public String getInfraredFlag() {
		return infraredFlag;
	}
	public void setInfraredFlag(String infraredFlag) {
		this.infraredFlag = infraredFlag;
	}
	public String getDouCardDaulFlag() {
		return douCardDaulFlag;
	}
	public void setDouCardDaulFlag(String douCardDaulFlag) {
		this.douCardDaulFlag = douCardDaulFlag;
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
