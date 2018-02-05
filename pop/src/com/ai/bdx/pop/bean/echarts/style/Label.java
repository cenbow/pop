package com.ai.bdx.pop.bean.echarts.style;



public class Label {

	private Boolean show;
	private String position;
	private Boolean rotate;
	private Integer distance;
	private String formatter;
	private TextStyle textStyle;
	
	public TextStyle newTextStyle(){
		TextStyle o = new TextStyle();
		this.setTextStyle(o);
		return o;
	}
	
	public Boolean getShow() {
		return show;
	}
	public Label setShow(Boolean show) {
		this.show = show;
		return this;
	}
	public String getPosition() {
		return position;
	}
	public Label setPosition(String position) {
		this.position = position;
		return this;
	}
	public Boolean getRotate() {
		return rotate;
	}
	public Label setRotate(Boolean rotate) {
		this.rotate = rotate;
		return this;
	}
	public Integer getDistance() {
		return distance;
	}
	public Label setDistance(Integer distance) {
		this.distance = distance;
		return this;
	}
	public String getFormatter() {
		return formatter;
	}
	public Label setFormatter(String formatter) {
		this.formatter = formatter;
		return this;
	}
	public TextStyle getTextStyle() {
		return textStyle;
	}
	public Label setTextStyle(TextStyle textStyle) {
		this.textStyle = textStyle;
		return this;
	}
	
	
}
