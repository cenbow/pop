package com.ai.bdx.pop.bean.echarts.style;



public class AxisPointer {	
	private String type;
	private LineStyle lineStyle;
	private LineStyle crossStyle;
	private AreaStyle shadowStyle;
	
	public LineStyle newLineStyle(){
		LineStyle o = new LineStyle();
		this.setLineStyle(o);
		return o;
	}
	
	public LineStyle newCrossStyle(){
		LineStyle o = new LineStyle();
		this.setCrossStyle(o);
		return o;
	}
	
	public AreaStyle newShadowStyle(){
		AreaStyle o = new AreaStyle();
		this.setShadowStyle(o);
		return o;
	}
	
	public String getType() {
		return type;
	}
	public AxisPointer setType(String type) {
		this.type = type;
		return this;
	}
	public LineStyle getLineStyle() {
		return lineStyle;
	}
	public AxisPointer setLineStyle(LineStyle lineStyle) {
		this.lineStyle = lineStyle;
		return this;
	}
	public LineStyle getCrossStyle() {
		return crossStyle;
	}
	public AxisPointer setCrossStyle(LineStyle crossStyle) {
		this.crossStyle = crossStyle;
		return this;
	}
	public AreaStyle getShadowStyle() {
		return shadowStyle;
	}
	public AxisPointer setShadowStyle(AreaStyle shadowStyle) {
		this.shadowStyle = shadowStyle;
		return this;
	}
	
	
}
