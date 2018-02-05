package com.ai.bdx.pop.bean.echarts.series.util;

import com.ai.bdx.pop.bean.echarts.style.TextStyle;


public class Title {
	private Boolean show;
	private String[] offsetCenter;
	private TextStyle textStyle;
	
	public TextStyle newTextStyle(){
		TextStyle o = new TextStyle();
		this.setTextStyle(o);
		return o;
	}
	
	public Boolean getShow() {
		return show;
	}
	public Title setShow(Boolean show) {
		this.show = show;
		return this;
	}
	public String[] getOffsetCenter() {
		return offsetCenter;
	}
	public Title setOffsetCenter(String[] offsetCenter) {
		this.offsetCenter = offsetCenter;
		return this;
	}
	public TextStyle getTextStyle() {
		return textStyle;
	}
	public Title setTextStyle(TextStyle textStyle) {
		this.textStyle = textStyle;
		return this;
	}
	
	
}
