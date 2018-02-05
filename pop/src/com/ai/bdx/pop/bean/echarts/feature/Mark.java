package com.ai.bdx.pop.bean.echarts.feature;

import com.ai.bdx.pop.bean.echarts.style.LineStyle;

public class Mark {
	private Boolean show;
	private Title title;
	private LineStyle lineStyle;
	
	public Title newTitle(){
		Title o = new Title();
		this.setTitle(o);
		return o;
	}
	
	public LineStyle newLineStyle(){
		LineStyle o = new LineStyle();
		this.setLineStyle(o);
		return o;
	}
	
	public Boolean getShow() {
		return show;
	}
	public Mark setShow(Boolean show) {
		this.show = show;
		return this;
	}
	public Title getTitle() {
		return title;
	}
	public Mark setTitle(Title title) {
		this.title = title;
		return this;
	}
	public LineStyle getLineStyle() {
		return lineStyle;
	}
	public Mark setLineStyle(LineStyle lineStyle) {
		this.lineStyle = lineStyle;
		return this;
	}
	
	
}
