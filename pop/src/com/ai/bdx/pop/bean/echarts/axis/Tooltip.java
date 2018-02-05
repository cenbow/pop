package com.ai.bdx.pop.bean.echarts.axis;


import com.ai.bdx.pop.bean.echarts.style.AxisPointer;
import com.ai.bdx.pop.bean.echarts.style.TextStyle;

public class Tooltip {
	private Boolean show;
	private Boolean showContent;
	private String trigger;
	private String[] position;
	private String formatter;
	private String islandFormatter;
	private Integer showDelay;
	private Integer hideDelay;
	private Double transitionDuration;
	private String backgroundColor;
	private String borderColor;
	private Integer borderRadius;
	private Integer borderWidth;
	private String[] padding;
	private AxisPointer axisPointer;
	private TextStyle textStyle;
	
	public AxisPointer newAxisPointer(){
		AxisPointer o = new AxisPointer();
		this.setAxisPointer(o);
		return o;
	}
	
	public TextStyle newTextStyle(){
		TextStyle o = new TextStyle();
		this.setTextStyle(o);
		return o;
	}
	
	public Boolean getShow() {
		return show;
	}
	public Tooltip setShow(Boolean show) {
		this.show = show;
		return this;
	}
	public Boolean getShowContent() {
		return showContent;
	}
	public Tooltip setShowContent(Boolean showContent) {
		this.showContent = showContent;
		return this;
	}
	public String getTrigger() {
		return trigger;
	}
	public Tooltip setTrigger(String trigger) {
		this.trigger = trigger;
		return this;
	}
	public String[] getPosition() {
		return position;
	}
	public Tooltip setPosition(String[] position) {
		this.position = position;
		return this;
	}
	public String getFormatter() {
		return formatter;
	}
	public Tooltip setFormatter(String formatter) {
		this.formatter = formatter;
		return this;
	}
	public String getIslandFormatter() {
		return islandFormatter;
	}
	public Tooltip setIslandFormatter(String islandFormatter) {
		this.islandFormatter = islandFormatter;
		return this;
	}
	public Integer getShowDelay() {
		return showDelay;
	}
	public Tooltip setShowDelay(Integer showDelay) {
		this.showDelay = showDelay;
		return this;
	}
	public Integer getHideDelay() {
		return hideDelay;
	}
	public Tooltip setHideDelay(Integer hideDelay) {
		this.hideDelay = hideDelay;
		return this;
	}
	public Double getTransitionDuration() {
		return transitionDuration;
	}
	public Tooltip setTransitionDuration(Double transitionDuration) {
		this.transitionDuration = transitionDuration;
		return this;
	}
	public String getBackgroundColor() {
		return backgroundColor;
	}
	public Tooltip setBackgroundColor(String backgroundColor) {
		this.backgroundColor = backgroundColor;
		return this;
	}
	public String getBorderColor() {
		return borderColor;
	}
	public Tooltip setBorderColor(String borderColor) {
		this.borderColor = borderColor;
		return this;
	}
	public Integer getBorderRadius() {
		return borderRadius;
	}
	public Tooltip setBorderRadius(Integer borderRadius) {
		this.borderRadius = borderRadius;
		return this;
	}
	public Integer getBorderWidth() {
		return borderWidth;
	}
	public Tooltip setBorderWidth(Integer borderWidth) {
		this.borderWidth = borderWidth;
		return this;
	}
	public String[] getPadding() {
		return padding;
	}
	public Tooltip setPadding(String[] padding) {
		this.padding = padding;
		return this;
	}
	public AxisPointer getAxisPointer() {
		return axisPointer;
	}
	public Tooltip setAxisPointer(AxisPointer axisPointer) {
		this.axisPointer = axisPointer;
		return this;
	}
	public TextStyle getTextStyle() {
		return textStyle;
	}
	public Tooltip setTextStyle(TextStyle textStyle) {
		this.textStyle = textStyle;
		return this;
	}
	
	
}
