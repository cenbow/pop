package com.ailk.bdx.pop.adapter.bean;

public class Message {
	private String value;
	private BaseConfig config;
	
	/**
	 * 适用于多数据源  对接esper的数据源id (云南对接etl用)
	 * */
	private String interIdToEventId;

	/**
	 * 适用于多数据源  云南 etl 传来的接口id (云南对接etl用)
	 * */
	private String interId;
	
	/**
	 * 适用于多数据源,即一个接口内部接多数据源,每条数据需要带上自己eventId
	 * */
	public Message(String value, BaseConfig config,String interIdToEventId,String interId) {
		this.value = value;
		this.config = config;
		this.interIdToEventId= interIdToEventId;
		this.interId = interId;
	}
  
	public Message(String value, BaseConfig config) {
		this.value = value;
		this.config = config;
	}
	
	
	
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public BaseConfig getConfig() {
		return config;
	}

	public void setConfig(BaseConfig config) {
		this.config = config;
	}


	public String getInterIdToEventId() {
		return interIdToEventId;
	}

	public void setInterIdToEventId(String interIdToEventId) {
		this.interIdToEventId = interIdToEventId;
	}

	public String getInterId() {
		return interId;
	}

	public void setInterId(String interId) {
		this.interId = interId;
	}
	
	
}
