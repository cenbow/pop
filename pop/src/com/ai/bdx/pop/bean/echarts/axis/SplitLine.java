package com.ai.bdx.pop.bean.echarts.axis;

import com.ai.bdx.pop.bean.echarts.style.LineStyle;



public class SplitLine {
	private Boolean show;
	private Boolean onGap;
	private LineStyle lineStyle;
	
	private Integer length;
	
	public LineStyle newLineStyle(){
		LineStyle o = new LineStyle();
		this.setLineStyle(o);
		return o;
	}
	
	public Boolean getShow() {
		return show;
	}
	public SplitLine setShow(Boolean show) {
		this.show = show;
		return this;
	}
	public Boolean getOnGap() {
		return onGap;
	}
	public SplitLine setOnGap(Boolean onGap) {
		this.onGap = onGap;
		return this;
	}
	public LineStyle getLineStyle() {
		return lineStyle;
	}
	public SplitLine setLineStyle(LineStyle lineStyle) {
		this.lineStyle = lineStyle;
		return this;
	}

	public Integer getLength() {
		return length;
	}

	public SplitLine setLength(Integer length) {
		this.length = length;
		return this;
	}
	
}
