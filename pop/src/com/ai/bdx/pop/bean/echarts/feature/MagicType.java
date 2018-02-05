package com.ai.bdx.pop.bean.echarts.feature;


public class MagicType {
	
	private Boolean show;
	private Title title;
	private String[] type;
	
	public Title newTitle(){
		Title o = new Title();
		this.setTitle(o);
		return o;
	}
	
	public Boolean getShow() {
		return show;
	}
	public MagicType setShow(Boolean show) {
		this.show = show;
		return this;
	}
	public Title getTitle() {
		return title;
	}
	public MagicType setTitle(Title title) {
		this.title = title;
		return this;
	}
	public String[] getType() {
		return type;
	}
	public MagicType setType(String[] type) {
		this.type = type;
		return this;
	}
	
	
}
