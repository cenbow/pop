package com.ai.bdx.pop.bean.echarts.feature;


public class DataZoom {
	private Boolean show;
	private Title title;
	
	public Title newTitle(){
		Title o = new Title();
		this.setTitle(o);
		return o;
	}
	
	public Boolean getShow() {
		return show;
	}
	public DataZoom setShow(Boolean show) {
		this.show = show;
		return this;
	}
	public Title getTitle() {
		return title;
	}
	public DataZoom setTitle(Title title) {
		this.title = title;
		return this;
	}
	
	
}
