package com.ai.bdx.pop.bean.echarts.base;


public class Effect {
	private Boolean show;
	private Boolean loop;
	private Integer period;
	private Integer scaleSize;
	private String color;
	private String shadowColor;
	private Integer shadowBlur;
	
	public Boolean getShow() {
		return show;
	}
	public Effect setShow(Boolean show) {
		this.show = show;
		return this;
	}
	public Boolean getLoop() {
		return loop;
	}
	public Effect setLoop(Boolean loop) {
		this.loop = loop;
		return this;
	}
	public Integer getPeriod() {
		return period;
	}
	public Effect setPeriod(Integer period) {
		this.period = period;
		return this;
	}
	public Integer getScaleSize() {
		return scaleSize;
	}
	public Effect setScaleSize(Integer scaleSize) {
		this.scaleSize = scaleSize;
		return this;
	}
	public String getColor() {
		return color;
	}
	public Effect setColor(String color) {
		this.color = color;
		return this;
	}
	public String getShadowColor() {
		return shadowColor;
	}
	public Effect setShadowColor(String shadowColor) {
		this.shadowColor = shadowColor;
		return this;
	}
	public Integer getShadowBlur() {
		return shadowBlur;
	}
	public Effect setShadowBlur(Integer shadowBlur) {
		this.shadowBlur = shadowBlur;
		return this;
	}
	
	
}
