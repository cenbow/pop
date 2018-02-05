package com.ai.bdx.pop.adapter.bean;

/**
 * 定义数据源FILE、FTP、SFTP、HDFS或socket的一条记录的元数据相关信息
 */
public class Content {
	private String split;//字段分隔符
	private String sourceFileField; //原始字段
	private String eventid; //可处理的事件类型范围
	private int eventidIndex; //事件类型字段索引
	private String convertIndex; //需要转换的字段索引
	private String keyMap; //key逻辑关系Map
	private String targetField; //目标字段
	private String targetIndex; //目标字段索引
	private int sourceKeyIndex;  //目标字段索引
	private String targetFieldIndex;//转换索引
	private String keyIndex; //key值索引
 
    //拓展字段
	private String extendColumn; //拓展字段
	private String extendIndex;  //拓展字段索引
	private String extendIMISIndex; //拓展需要转换的字段索引
	private int extendIMEIIndex;//拓展IMEI索引,需要截前8位判断
	
	//湖北用,用这俩字段获取事件人手机号码
	private String activeEventIDs;//主动行为事件类型
	private String passiveEventIDs;//被动行为事件类型
	private String extendPhoneIndex;//主叫被叫索引
	private String targetFieldExtends; //湖北添加号码对应省份地市字段
	private String phoneControlAreaNums;//号码对照省市的位数
	private String defaultProregNo;//默认省份地市编码
	private String subAcceUrlNums;//截取URL位数
	private String replaceAcceUrlComma;//acce_url中逗号替换
	
	//安徽新增4G信令新增字段
	private String  dataFilterIndex;//数据过滤字段索引
	private String dataFilterValue;//数据过滤字段值
	
	//小接口id 云南对接etl用
	private String id;
	
	public Content() {
		
	}

	public Content(String split, String sourceFileField, String eventid,
			int eventidIndex, String convertIndex,
			String keyMap, String targetField, String targetIndex,int sourceKeyIndex,String targetFieldIndex,String keyIndex ,String extendColumn,String extendIndex,String extendIMISIndex,String id,int extendIMEIIndex,String activeEventIDs,String passiveEventIDs,String extendPhoneIndex
			/*String targetFieldExtends,String phoneControlAreaNums,String defaultProregNo,String subAcceUrlNums,String replaceAcceUrlComma*/) {
		this.split = split;
		this.sourceFileField = sourceFileField;
		this.eventid = eventid;
		this.eventidIndex = eventidIndex;
		this.convertIndex = convertIndex;
		this.keyMap = keyMap;
		this.targetField = targetField;
		this.targetIndex = targetIndex;
		this.sourceKeyIndex = sourceKeyIndex;
		this.targetFieldIndex =targetFieldIndex;
		this.keyIndex = keyIndex;
		this.extendColumn=extendColumn;
		this.extendIndex=extendIndex;
		this.extendIMISIndex=extendIMISIndex;
		this.id = id;
		this.extendIMEIIndex = extendIMEIIndex;
		this.activeEventIDs = activeEventIDs;
		this.passiveEventIDs = passiveEventIDs;
		this.extendPhoneIndex = extendPhoneIndex;
		/*this.targetFieldExtends = targetFieldExtends;
		this.phoneControlAreaNums = phoneControlAreaNums;
		this.defaultProregNo = defaultProregNo;
		this.subAcceUrlNums = subAcceUrlNums;
		this.replaceAcceUrlComma = replaceAcceUrlComma;*/
	}

	public String getKeyMap() {
		return keyMap;
	}

	public void setKeyMap(String keyMap) {
		this.keyMap = keyMap;
	}

	public int getEventidIndex() {
		return eventidIndex;
	}

	public void setEventidIndex(int eventidIndex) {
		this.eventidIndex = eventidIndex;
	}


	public String getSplit() {
		return split;
	}

	public void setSplit(String split) {
		this.split = split;
	}

	public String getSourceFileField() {
		return sourceFileField;
	}

	public void setSourceFileField(String sourceFileField) {
		this.sourceFileField = sourceFileField;
	}

	public String getEventid() {
		return eventid;
	}

	public void setEventid(String eventid) {
		this.eventid = eventid;
	}

	public String getConvertIndex() {
		return convertIndex;
	}

	public void setConvertIndex(String convertIndex) {
		this.convertIndex = convertIndex;
	}

	public String getTargetField() {
		return targetField;
	}

	public void setTargetField(String targetField) {
		this.targetField = targetField;
	}

	public String getTargetIndex() {
		return targetIndex;
	}

	public void setTargetIndex(String targetIndex) {
		this.targetIndex = targetIndex;
	}

  

	public int getSourceKeyIndex() {
		return sourceKeyIndex;
	}

	public void setSourceKeyIndex(int sourceKeyIndex) {
		this.sourceKeyIndex = sourceKeyIndex;
	}

	public String getTargetFieldIndex() {
		return targetFieldIndex;
	}

	public void setTargetFieldIndex(String targetFieldIndex) {
		this.targetFieldIndex = targetFieldIndex;
	}

	public String getExtendColumn() {
		return extendColumn;
	}

	public void setExtendColumn(String extendColumn) {
		this.extendColumn = extendColumn;
	}

	public String getExtendIndex() {
		return extendIndex;
	}

	public void setExtendIndex(String extendIndex) {
		this.extendIndex = extendIndex;
	}

	public String getExtendIMISIndex() {
		return extendIMISIndex;
	}

	public void setExtendIMISIndex(String extendIMISIndex) {
		this.extendIMISIndex = extendIMISIndex;
	}


	public String getKeyIndex() {
		return keyIndex;
	}

	public void setKeyIndex(String keyIndex) {
		this.keyIndex = keyIndex;
	}
	
	public String getId(){
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getExtendIMEIIndex() {
		return extendIMEIIndex;
	}

	public void setExtendIMEIIndex(int extendIMEIIndex) {
		this.extendIMEIIndex = extendIMEIIndex;
	}

	public String getActiveEventIDs() {
		return activeEventIDs;
	}

	public void setActiveEventIDs(String activeEventIDs) {
		this.activeEventIDs = activeEventIDs;
	}

	public String getPassiveEventIDs() {
		return passiveEventIDs;
	}

	public void setPassiveEventIDs(String passiveEventIDs) {
		this.passiveEventIDs = passiveEventIDs;
	}



	public String getExtendPhoneIndex() {
		return extendPhoneIndex;
	}



	public void setExtendPhoneIndex(String extendPhoneIndex) {
		this.extendPhoneIndex = extendPhoneIndex;
	}

	public String getTargetFieldExtends() {
		return targetFieldExtends;
	}


	public void setTargetFieldExtends(String targetFieldExtends) {
		this.targetFieldExtends = targetFieldExtends;
	}

	public String getPhoneControlAreaNums() {
		return phoneControlAreaNums;
	}

	public void setPhoneControlAreaNums(String phoneControlAreaNums) {
		this.phoneControlAreaNums = phoneControlAreaNums;
	}

	public String getDefaultProregNo() {
		return defaultProregNo;
	}

	public void setDefaultProregNo(String defaultProregNo) {
		this.defaultProregNo = defaultProregNo;
	}

	public String getReplaceAcceUrlComma() {
		return replaceAcceUrlComma;
	}

	public void setReplaceAcceUrlComma(String replaceAcceUrlComma) {
		this.replaceAcceUrlComma = replaceAcceUrlComma;
	}

	public String getSubAcceUrlNums() {
		return subAcceUrlNums;
	}

	public void setSubAcceUrlNums(String subAcceUrlNums) {
		this.subAcceUrlNums = subAcceUrlNums;
	}

	public String getDataFilterIndex() {
		return dataFilterIndex;
	}

	public void setDataFilterIndex(String dataFilterIndex) {
		this.dataFilterIndex = dataFilterIndex;
	}

	public String getDataFilterValue() {
		return dataFilterValue;
	}

	public void setDataFilterValue(String dataFilterValue) {
		this.dataFilterValue = dataFilterValue;
	}

}
