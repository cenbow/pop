package com.ai.bdx.pop.bean.echarts.style;



public class NodeStyle {
	
	private String brushType;
	private String color;
	private String strokeColor;
	private Integer lineWidth;
	
	public String getBrushType() {
		return brushType;
	}
	public NodeStyle setBrushType(String brushType) {
		this.brushType = brushType;
		return this;
	}
	public String getColor() {
		return color;
	}
	public NodeStyle setColor(String color) {
		this.color = color;
		return this;
	}
	public String getStrokeColor() {
		return strokeColor;
	}
	public NodeStyle setStrokeColor(String strokeColor) {
		this.strokeColor = strokeColor;
		return this;
	}
	public Integer getLineWidth() {
		return lineWidth;
	}
	public NodeStyle setLineWidth(Integer lineWidth) {
		this.lineWidth = lineWidth;
		return this;
	}
	
	
}
