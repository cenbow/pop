package com.ai.bdx.pop.bean.echarts.axis;

import com.ai.bdx.pop.bean.echarts.feature.Feature;
import com.ai.bdx.pop.bean.echarts.style.TextStyle;

public class Toolbox {
	private Boolean show;
	private String orient;
	private String x;
	private String y;
	private String backgroundColor;
	private String borderColor;
	private Integer borderWidth;
	private String[] padding;
	private Integer itemGap;
	private Integer itemSize;
	private String[] color;
	private String disableColor;
	private String effectiveColor;
	private Boolean showTitle;
	private TextStyle textStyle;
	private Feature feature;

	public TextStyle newTextStyle() {
		TextStyle o = new TextStyle();
		this.setTextStyle(o);
		return o;
	}

	public Feature newFeature() {
		Feature o = new Feature();
		this.setFeature(o);
		return o;
	}

	public Boolean getShow() {
		return show;
	}

	public Toolbox setShow(Boolean show) {
		this.show = show;
		return this;
	}

	public String getOrient() {
		return orient;
	}

	public Toolbox setOrient(String orient) {
		this.orient = orient;
		return this;
	}

	public String getX() {
		return x;
	}

	public Toolbox setX(String x) {
		this.x = x;
		return this;
	}

	public String getY() {
		return y;
	}

	public Toolbox setY(String y) {
		this.y = y;
		return this;
	}

	public String getBackgroundColor() {
		return backgroundColor;
	}

	public Toolbox setBackgroundColor(String backgroundColor) {
		this.backgroundColor = backgroundColor;
		return this;
	}

	public String getBorderColor() {
		return borderColor;
	}

	public Toolbox setBorderColor(String borderColor) {
		this.borderColor = borderColor;
		return this;
	}

	public Integer getBorderWidth() {
		return borderWidth;
	}

	public Toolbox setBorderWidth(Integer borderWidth) {
		this.borderWidth = borderWidth;
		return this;
	}

	public String[] getPadding() {
		return padding;
	}

	public Toolbox setPadding(String[] padding) {
		this.padding = padding;
		return this;
	}

	public Integer getItemGap() {
		return itemGap;
	}

	public Toolbox setItemGap(Integer itemGap) {
		this.itemGap = itemGap;
		return this;
	}

	public Integer getItemSize() {
		return itemSize;
	}

	public Toolbox setItemSize(Integer itemSize) {
		this.itemSize = itemSize;
		return this;
	}

	public String[] getColor() {
		return color;
	}

	public Toolbox setColor(String[] color) {
		this.color = color;
		return this;
	}

	public String getDisableColor() {
		return disableColor;
	}

	public Toolbox setDisableColor(String disableColor) {
		this.disableColor = disableColor;
		return this;
	}

	public String getEffectiveColor() {
		return effectiveColor;
	}

	public Toolbox setEffectiveColor(String effectiveColor) {
		this.effectiveColor = effectiveColor;
		return this;
	}

	public Boolean getShowTitle() {
		return showTitle;
	}

	public Toolbox setShowTitle(Boolean showTitle) {
		this.showTitle = showTitle;
		return this;
	}

	public TextStyle getTextStyle() {
		return textStyle;
	}

	public Toolbox setTextStyle(TextStyle textStyle) {
		this.textStyle = textStyle;
		return this;
	}

	public Feature getFeature() {
		return feature;
	}

	public Toolbox setFeature(Feature feature) {
		this.feature = feature;
		return this;
	}

}
