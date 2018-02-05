package com.ai.bdx.pop.bean.echarts.axis;

import java.util.HashMap;
import java.util.Map;

public class RoamController {

	private Boolean show;
	private String x;
	private String y;
	private Integer width;
	private Integer height;
	private String backgroundColor;
	private String borderColor;
	private Integer borderWidth;
	private String[] padding;
	private String fillerColor;
	private String handleColor;
	private Integer step;
	private Map<String,Object> mapTypeControl;
	
	public Map<String,Object> newMapTypeControl(){
		Map<String,Object> o = new HashMap<String,Object>();
		this.setMapTypeControl(o);
		return o;
	}
	
	public Boolean getShow() {
		return show;
	}
	public RoamController setShow(Boolean show) {
		this.show = show;
		return this;
	}
	public String getX() {
		return x;
	}
	public RoamController setX(String x) {
		this.x = x;
		return this;
	}
	public String getY() {
		return y;
	}
	public RoamController setY(String y) {
		this.y = y;
		return this;
	}
	public Integer getWidth() {
		return width;
	}
	public RoamController setWidth(Integer width) {
		this.width = width;
		return this;
	}
	public Integer getHeight() {
		return height;
	}
	public RoamController setHeight(Integer height) {
		this.height = height;
		return this;
	}
	public String getBackgroundColor() {
		return backgroundColor;
	}
	public RoamController setBackgroundColor(String backgroundColor) {
		this.backgroundColor = backgroundColor;
		return this;
	}
	public String getBorderColor() {
		return borderColor;
	}
	public RoamController setBorderColor(String borderColor) {
		this.borderColor = borderColor;
		return this;
	}
	public Integer getBorderWidth() {
		return borderWidth;
	}
	public RoamController setBorderWidth(Integer borderWidth) {
		this.borderWidth = borderWidth;
		return this;
	}
	public String[] getPadding() {
		return padding;
	}
	public RoamController setPadding(String[] padding) {
		this.padding = padding;
		return this;
	}
	public String getFillerColor() {
		return fillerColor;
	}
	public RoamController setFillerColor(String fillerColor) {
		this.fillerColor = fillerColor;
		return this;
	}
	public String getHandleColor() {
		return handleColor;
	}
	public RoamController setHandleColor(String handleColor) {
		this.handleColor = handleColor;
		return this;
	}
	public Integer getStep() {
		return step;
	}
	public RoamController setStep(Integer step) {
		this.step = step;
		return this;
	}
	public Map<String, Object> getMapTypeControl() {
		return mapTypeControl;
	}
	public RoamController setMapTypeControl(Map<String, Object> mapTypeControl) {
		this.mapTypeControl = mapTypeControl;
		return this;
	}
	
	
}
