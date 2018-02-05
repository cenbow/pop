package com.ai.bdx.pop.bean.echarts.series.util;

import com.ai.bdx.pop.bean.echarts.style.TextStyle;

public class Detail {
	
	private Boolean show;
	private String backgroundColor;
	private Integer borderWidth;
	private String borderColor;
	private Integer width;
	private Integer height;
	private String[] offsetCenter;
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
	public Detail setShow(Boolean show) {
		this.show = show;
		return this;
	}
	public String getBackgroundColor() {
		return backgroundColor;
	}
	public Detail setBackgroundColor(String backgroundColor) {
		this.backgroundColor = backgroundColor;
		return this;
	}
	public Integer getBorderWidth() {
		return borderWidth;
	}
	public Detail setBorderWidth(Integer borderWidth) {
		this.borderWidth = borderWidth;
		return this;
	}
	public String getBorderColor() {
		return borderColor;
	}
	public Detail setBorderColor(String borderColor) {
		this.borderColor = borderColor;
		return this;
	}
	public Integer getWidth() {
		return width;
	}
	public Detail setWidth(Integer width) {
		this.width = width;
		return this;
	}
	public Integer getHeight() {
		return height;
	}
	public Detail setHeight(Integer height) {
		this.height = height;
		return this;
	}
	public String[] getOffsetCenter() {
		return offsetCenter;
	}
	public Detail setOffsetCenter(String[] offsetCenter) {
		this.offsetCenter = offsetCenter;
		return this;
	}
	public String getFormatter() {
		return formatter;
	}
	public Detail setFormatter(String formatter) {
		this.formatter = formatter;
		return this;
	}
	public TextStyle getTextStyle() {
		return textStyle;
	}
	public Detail setTextStyle(TextStyle textStyle) {
		this.textStyle = textStyle;
		return this;
	}
	
	
}
