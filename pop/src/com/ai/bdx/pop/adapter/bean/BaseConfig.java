package com.ai.bdx.pop.adapter.bean;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 基础数据源配置项
 * 
 * @author imp
 * 
 */
public abstract class BaseConfig {
	protected String name;
	protected String eventId;

	private String lacCiId;
	private String logicId;
	private String tranLocCiFileName;

	private List<Content> contents;

	private String isOpenDis;

	private Content content;
	//加强字段,适用于一个接口内部,对接多个数据源
	private Map<String,Content> kvContent = new HashMap<String,Content>();

	public Map<String, Content> getKvContent() {
		return kvContent;
	}

	public void setKvContent(Map<String, Content> kvContent) {
		this.kvContent = kvContent;
	}
	
	public Content getContent() {
		return content;
	}

	public void setContent(Content content) {
		this.content = content;
	}

	public List<Content> getContents() {
		return contents;
	}

	public void setContents(List<Content> contents) {
		this.contents = contents;
	}

	public String getIsOpenDis() {
		return isOpenDis;
	}

	public void setIsOpenDis(String isOpenDis) {
		this.isOpenDis = isOpenDis;
	}

	public String getTranLocCiFileName() {
		return tranLocCiFileName;
	}

	public void setTranLocCiFileName(String tranLocCiFileName) {
		this.tranLocCiFileName = tranLocCiFileName;
	}

	public String getLacCiId() {
		return lacCiId;
	}

	public void setLacCiId(String lacCiId) {
		this.lacCiId = lacCiId;
	}

	public String getLogicId() {
		return logicId;
	}

	public void setLogicId(String logicId) {
		this.logicId = logicId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEventId() {
		return eventId;
	}

	public void setEventId(String eventId) {
		this.eventId = eventId;
	}
}
