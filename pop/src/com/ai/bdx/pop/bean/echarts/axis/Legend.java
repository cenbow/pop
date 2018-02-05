package com.ai.bdx.pop.bean.echarts.axis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ai.bdx.pop.bean.echarts.style.TextStyle;

public class Legend {
	
	private String orient;
	private String x;
	private String y;
	private String backgroundColor;
	private String borderColor;
	private Integer borderWidth;
	private String[] padding;
	private Integer itemGap;
	private TextStyle textStyle;
	private String formatter;
	//boolean string
	private Object selectedMode;
	private Map<String,Object> selected;
	private List<Object> data;
	
	public TextStyle newTextStyle(){
		TextStyle o = new TextStyle();
		this.setTextStyle(o);
		return o;
	}

	public Map<String,Object> newSelected(){
		Map<String,Object> o = new HashMap<String,Object>();
		this.setSelected(o);
		return o;
	}

	public List<Object> newData(){
		List<Object> o = new ArrayList<Object>();
		this.setData(o);
		return o;
	}

	
	public String getOrient() {
		return orient;
	}
	public Legend setOrient(String orient) {
		this.orient = orient;
		return this;
	}
	public String getX() {
		return x;
	}
	public Legend setX(String x) {
		this.x = x;
		return this;
	}
	public String getY() {
		return y;
	}
	public Legend setY(String y) {
		this.y = y;
		return this;
	}
	public String getBackgroundColor() {
		return backgroundColor;
	}
	public Legend setBackgroundColor(String backgroundColor) {
		this.backgroundColor = backgroundColor;
		return this;
	}
	public String getBorderColor() {
		return borderColor;
	}
	public Legend setBorderColor(String borderColor) {
		this.borderColor = borderColor;
		return this;
	}
	public Integer getBorderWidth() {
		return borderWidth;
	}
	public Legend setBorderWidth(Integer borderWidth) {
		this.borderWidth = borderWidth;
		return this;
	}
	public String[] getPadding() {
		return padding;
	}
	public Legend setPadding(String[] padding) {
		this.padding = padding;
		return this;
	}
	public Integer getItemGap() {
		return itemGap;
	}
	public Legend setItemGap(Integer itemGap) {
		this.itemGap = itemGap;
		return this;
	}
	public TextStyle getTextStyle() {
		return textStyle;
	}
	public Legend setTextStyle(TextStyle textStyle) {
		this.textStyle = textStyle;
		return this;
	}
	public String getFormatter() {
		return formatter;
	}
	public Legend setFormatter(String formatter) {
		this.formatter = formatter;
		return this;
	}
	public Object getSelectedMode() {
		return selectedMode;
	}
	public Legend setSelectedMode(Object selectedMode) {
		this.selectedMode = selectedMode;
		return this;
	}
	public Map<String, Object> getSelected() {
		return selected;
	}
	public Legend setSelected(Map<String, Object> selected) {
		this.selected = selected;
		return this;
	}
	public List<Object> getData() {
		return data;
	}
	public Legend setData(List<Object> data) {
		this.data = data;
		return this;
	}
	
	
}
