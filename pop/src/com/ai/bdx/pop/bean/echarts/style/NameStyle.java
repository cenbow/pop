package com.ai.bdx.pop.bean.echarts.style;



public class NameStyle {
	private Boolean show;
	private String formatter;
	private TextStyle textStyle;
	
	public TextStyle newTextStyle(){
		TextStyle o = new TextStyle();
		this.setTextStyle(o);
		return o;
	}
	
	public Boolean getShow() {
		return show;
	}
	public NameStyle setShow(Boolean show) {
		this.show = show;
		return this;
	}
	public String getFormatter() {
		return formatter;
	}
	public NameStyle setFormatter(String formatter) {
		this.formatter = formatter;
		return this;
	}
	public TextStyle getTextStyle() {
		return textStyle;
	}
	public NameStyle setTextStyle(TextStyle textStyle) {
		this.textStyle = textStyle;
		return this;
	}
	
	
}
