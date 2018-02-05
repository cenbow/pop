package com.ai.bdx.pop.bean.echarts.style;



public class LabelLine {

	private Boolean show;
	private Integer length;
	private LineStyle lineStyle;
	
	public LineStyle newLineStyle(){
		LineStyle o = new LineStyle();
		this.setLineStyle(o);
		return o;
	}
	
	public Boolean getShow() {
		return show;
	}
	public LabelLine setShow(Boolean show) {
		this.show = show;
		return this;
	}
	public Integer getLength() {
		return length;
	}
	public LabelLine setLength(Integer length) {
		this.length = length;
		return this;
	}
	public LineStyle getLineStyle() {
		return lineStyle;
	}
	public LabelLine setLineStyle(LineStyle lineStyle) {
		this.lineStyle = lineStyle;
		return this;
	}
	
	
}
