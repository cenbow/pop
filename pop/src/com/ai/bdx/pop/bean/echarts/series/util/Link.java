package com.ai.bdx.pop.bean.echarts.series.util;

import com.ai.bdx.pop.bean.echarts.style.ItemStyle;

public class Link {

	private String name;
	private Integer value;
	private Integer[] initial;
	private Boolean fixX;
	private Boolean fixY;
	private Boolean ignore;
	private String symbol;
	private Integer symbolSize;
	private Boolean draggable;
	private Integer category;
	private ItemStyle itemStyle;
	
	public ItemStyle newItemStyle(){
		ItemStyle o = new ItemStyle();
		this.setItemStyle(o);
		return o;
	}
	
	public String getName() {
		return name;
	}
	public Link setName(String name) {
		this.name = name;
		return this;
	}
	public Integer getValue() {
		return value;
	}
	public Link setValue(Integer value) {
		this.value = value;
		return this;
	}
	public Integer[] getInitial() {
		return initial;
	}
	public Link setInitial(Integer[] initial) {
		this.initial = initial;
		return this;
	}
	public Boolean getFixX() {
		return fixX;
	}
	public Link setFixX(Boolean fixX) {
		this.fixX = fixX;
		return this;
	}
	public Boolean getFixY() {
		return fixY;
	}
	public Link setFixY(Boolean fixY) {
		this.fixY = fixY;
		return this;
	}
	public Boolean getIgnore() {
		return ignore;
	}
	public Link setIgnore(Boolean ignore) {
		this.ignore = ignore;
		return this;
	}
	public String getSymbol() {
		return symbol;
	}
	public Link setSymbol(String symbol) {
		this.symbol = symbol;
		return this;
	}
	public Integer getSymbolSize() {
		return symbolSize;
	}
	public Link setSymbolSize(Integer symbolSize) {
		this.symbolSize = symbolSize;
		return this;
	}
	public Boolean getDraggable() {
		return draggable;
	}
	public Link setDraggable(Boolean draggable) {
		this.draggable = draggable;
		return this;
	}
	public Integer getCategory() {
		return category;
	}
	public Link setCategory(Integer category) {
		this.category = category;
		return this;
	}
	public ItemStyle getItemStyle() {
		return itemStyle;
	}
	public Link setItemStyle(ItemStyle itemStyle) {
		this.itemStyle = itemStyle;
		return this;
	}
}
