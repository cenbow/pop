package com.ai.bdx.pop.bean.echarts.feature;


public class DataView {
	private Boolean show;
	private String title;
	private Boolean readOnly;
	private String[] lang;
	
	public Boolean getShow() {
		return show;
	}
	public DataView setShow(Boolean show) {
		this.show = show;
		return this;
	}
	public String getTitle() {
		return title;
	}
	public DataView setTitle(String title) {
		this.title = title;
		return this;
	}
	public Boolean getReadOnly() {
		return readOnly;
	}
	public DataView setReadOnly(Boolean readOnly) {
		this.readOnly = readOnly;
		return this;
	}
	public String[] getLang() {
		return lang;
	}
	public DataView setLang(String[] lang) {
		this.lang = lang;
		return this;
	}
	
	
}
