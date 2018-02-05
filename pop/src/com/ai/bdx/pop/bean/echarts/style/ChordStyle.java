package com.ai.bdx.pop.bean.echarts.style;



public class ChordStyle {
	
	private LineStyle lineStyle;
	
	public LineStyle newLineStyle(){
		LineStyle o = new LineStyle();
		this.setLineStyle(o);
		return o;
	}

	public LineStyle getLineStyle() {
		return lineStyle;
	}

	public ChordStyle setLineStyle(LineStyle lineStyle) {
		this.lineStyle = lineStyle;
		return this;
	}
	
}
