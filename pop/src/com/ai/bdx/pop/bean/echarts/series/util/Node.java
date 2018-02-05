package com.ai.bdx.pop.bean.echarts.series.util;

import com.ai.bdx.pop.bean.echarts.style.ItemStyle;

public class Node {
	private String source;
	private String target;
	private Integer weight;
	private ItemStyle itemStyle;
	
	public ItemStyle newItemStyle(){
		ItemStyle o = new ItemStyle();
		this.setItemStyle(o);
		return o;
	}
	
	public String getSource() {
		return source;
	}
	public Node setSource(String source) {
		this.source = source;
		return this;
	}
	public String getTarget() {
		return target;
	}
	public Node setTarget(String target) {
		this.target = target;
		return this;
	}
	public Integer getWeight() {
		return weight;
	}
	public Node setWeight(Integer weight) {
		this.weight = weight;
		return this;
	}
	public ItemStyle getItemStyle() {
		return itemStyle;
	}
	public Node setItemStyle(ItemStyle itemStyle) {
		this.itemStyle = itemStyle;
		return this;
	}
	
	
}
