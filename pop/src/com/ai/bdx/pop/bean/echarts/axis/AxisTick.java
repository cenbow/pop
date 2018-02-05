package com.ai.bdx.pop.bean.echarts.axis;

import com.ai.bdx.pop.bean.echarts.style.LineStyle;

public class AxisTick {

	private Boolean show;
	private String interval;
	private Boolean onGap;
	private Boolean inside;
	private Integer length;
	private LineStyle lineStyle;
	
	private Integer splitNumber;
	
	public LineStyle newLineStyle(){
		LineStyle o = new LineStyle();
		this.setLineStyle(o);
		return o;
	}
	
	public Boolean getShow() {
		return show;
	}
	public AxisTick setShow(Boolean show) {
		this.show = show;
		return this;
	}
	public String getInterval() {
		return interval;
	}
	public AxisTick setInterval(String interval) {
		this.interval = interval;
		return this;
	}
	public Boolean getOnGap() {
		return onGap;
	}
	public AxisTick setOnGap(Boolean onGap) {
		this.onGap = onGap;
		return this;
	}
	public Boolean getInside() {
		return inside;
	}
	public AxisTick setInside(Boolean inside) {
		this.inside = inside;
		return this;
	}
	public Integer getLength() {
		return length;
	}
	public AxisTick setLength(Integer length) {
		this.length = length;
		return this;
	}
	public LineStyle getLineStyle() {
		return lineStyle;
	}
	public AxisTick setLineStyle(LineStyle lineStyle) {
		this.lineStyle = lineStyle;
		return this;
	}

	public Integer getSplitNumber() {
		return splitNumber;
	}

	public AxisTick setSplitNumber(Integer splitNumber) {
		this.splitNumber = splitNumber;
		return this;
	}
	
}
