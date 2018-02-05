package com.ai.bdx.pop.bean.echarts.axis;


import com.ai.bdx.pop.bean.echarts.style.TextStyle;

public class DataRange {

	private String orient;
	private String x;
	private String y;
	private String backgroundColor;
	private String borderColor;
	private Integer borderWidth;
	private String[] padding;
	private Integer itemGap;
	private Integer itemWidth;
	private Integer itemHeight;
	private Integer min;
	private Integer max;
	private Integer precision;
	private Integer splitNumber;
	private Boolean calculable;
	private Boolean realtime;
	private String[] color;
	private String formatter;
	private String[] text;
	private TextStyle textStyle;
	
	public TextStyle newTextStyle(){
		TextStyle o = new TextStyle();
		this.setTextStyle(o);
		return o;
	}
	
	public String getOrient() {
		return orient;
	}
	public DataRange setOrient(String orient) {
		this.orient = orient;
		return this;
	}
	public String getX() {
		return x;
	}
	public DataRange setX(String x) {
		this.x = x;
		return this;
	}
	public String getY() {
		return y;
	}
	public DataRange setY(String y) {
		this.y = y;
		return this;
	}
	public String getBackgroundColor() {
		return backgroundColor;
	}
	public DataRange setBackgroundColor(String backgroundColor) {
		this.backgroundColor = backgroundColor;
		return this;
	}
	public String getBorderColor() {
		return borderColor;
	}
	public DataRange setBorderColor(String borderColor) {
		this.borderColor = borderColor;
		return this;
	}
	public Integer getBorderWidth() {
		return borderWidth;
	}
	public DataRange setBorderWidth(Integer borderWidth) {
		this.borderWidth = borderWidth;
		return this;
	}
	public String[] getPadding() {
		return padding;
	}
	public DataRange setPadding(String[] padding) {
		this.padding = padding;
		return this;
	}
	public Integer getItemGap() {
		return itemGap;
	}
	public DataRange setItemGap(Integer itemGap) {
		this.itemGap = itemGap;
		return this;
	}
	public Integer getItemWidth() {
		return itemWidth;
	}
	public DataRange setItemWidth(Integer itemWidth) {
		this.itemWidth = itemWidth;
		return this;
	}
	public Integer getItemHeight() {
		return itemHeight;
	}
	public DataRange setItemHeight(Integer itemHeight) {
		this.itemHeight = itemHeight;
		return this;
	}
	public Integer getMin() {
		return min;
	}
	public DataRange setMin(Integer min) {
		this.min = min;
		return this;
	}
	public Integer getMax() {
		return max;
	}
	public DataRange setMax(Integer max) {
		this.max = max;
		return this;
	}
	public Integer getPrecision() {
		return precision;
	}
	public DataRange setPrecision(Integer precision) {
		this.precision = precision;
		return this;
	}
	public Integer getSplitNumber() {
		return splitNumber;
	}
	public DataRange setSplitNumber(Integer splitNumber) {
		this.splitNumber = splitNumber;
		return this;
	}
	public Boolean getCalculable() {
		return calculable;
	}
	public DataRange setCalculable(Boolean calculable) {
		this.calculable = calculable;
		return this;
	}
	public Boolean getRealtime() {
		return realtime;
	}
	public DataRange setRealtime(Boolean realtime) {
		this.realtime = realtime;
		return this;
	}
	public String[] getColor() {
		return color;
	}
	public DataRange setColor(String[] color) {
		this.color = color;
		return this;
	}
	public String getFormatter() {
		return formatter;
	}
	public DataRange setFormatter(String formatter) {
		this.formatter = formatter;
		return this;
	}
	public String[] getText() {
		return text;
	}
	public DataRange setText(String[] text) {
		this.text = text;
		return this;
	}
	public TextStyle getTextStyle() {
		return textStyle;
	}
	public DataRange setTextStyle(TextStyle textStyle) {
		this.textStyle = textStyle;
		return this;
	}
	
}
