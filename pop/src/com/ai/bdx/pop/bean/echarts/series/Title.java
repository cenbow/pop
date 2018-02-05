package com.ai.bdx.pop.bean.echarts.series;

import com.ai.bdx.pop.bean.echarts.style.TextStyle;

public class Title {
	
	private String text;
	private String link;
	private String target;
	private String subtext;
	private String sublink;
	private String subtarget;
	private String x;
	private String y;
	private String textAlign;
	private String backgroundColor;
	private String borderColor;
	private Integer borderWidth;
	private String[] padding;
	private Integer itemGap;
	private TextStyle textStyle;
	private TextStyle subtextStyle;
	
	public TextStyle newTextStyle(){
		TextStyle o = new TextStyle();
		this.setTextStyle(o);
		return o;
	}
	
	public TextStyle newSubtextStyle(){
		TextStyle o = new TextStyle();
		this.setSubtextStyle(o);
		return o;
	}
	
	public String getText() {
		return text;
	}
	public Title setText(String text) {
		this.text = text;
		return this;
	}
	public String getLink() {
		return link;
	}
	public Title setLink(String link) {
		this.link = link;
		return this;
	}
	public String getTarget() {
		return target;
	}
	public Title setTarget(String target) {
		this.target = target;
		return this;
	}
	public String getSubtext() {
		return subtext;
	}
	public Title setSubtext(String subtext) {
		this.subtext = subtext;
		return this;
	}
	public String getSublink() {
		return sublink;
	}
	public Title setSublink(String sublink) {
		this.sublink = sublink;
		return this;
	}
	public String getSubtarget() {
		return subtarget;
	}
	public Title setSubtarget(String subtarget) {
		this.subtarget = subtarget;
		return this;
	}
	public String getX() {
		return x;
	}
	public Title setX(String x) {
		this.x = x;
		return this;
	}
	public String getY() {
		return y;
	}
	public Title setY(String y) {
		this.y = y;
		return this;
	}
	public String getTextAlign() {
		return textAlign;
	}
	public Title setTextAlign(String textAlign) {
		this.textAlign = textAlign;
		return this;
	}
	public String getBackgroundColor() {
		return backgroundColor;
	}
	public Title setBackgroundColor(String backgroundColor) {
		this.backgroundColor = backgroundColor;
		return this;
	}
	public String getBorderColor() {
		return borderColor;
	}
	public Title setBorderColor(String borderColor) {
		this.borderColor = borderColor;
		return this;
	}
	public Integer getBorderWidth() {
		return borderWidth;
	}
	public Title setBorderWidth(Integer borderWidth) {
		this.borderWidth = borderWidth;
		return this;
	}
	public String[] getPadding() {
		return padding;
	}
	public Title setPadding(String[] padding) {
		this.padding = padding;
		return this;
	}
	public Integer getItemGap() {
		return itemGap;
	}
	public Title setItemGap(Integer itemGap) {
		this.itemGap = itemGap;
		return this;
	}
	public TextStyle getTextStyle() {
		return textStyle;
	}
	public Title setTextStyle(TextStyle textStyle) {
		this.textStyle = textStyle;
		return this;
	}
	public TextStyle getSubtextStyle() {
		return subtextStyle;
	}
	public Title setSubtextStyle(TextStyle subtextStyle) {
		this.subtextStyle = subtextStyle;
		return this;
	}
	
}
