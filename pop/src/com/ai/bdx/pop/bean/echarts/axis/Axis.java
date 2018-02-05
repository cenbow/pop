package com.ai.bdx.pop.bean.echarts.axis;

import java.util.List;

public class Axis {

	private String type;
	private String position;
	private String name;
	private String nameLocation;
	private Object nameTextStyle;
	// boolean Integer[]
	private Object boundaryGap;
	private Integer min;
	private Integer max;
	private Boolean scale;
	private Integer precision;
	private Integer power;
	private Integer splitNumber;
	private AxisLine axisLine;
	private AxisTick axisTick;
	private AxisLabel axisLabel;
	private SplitLine splitLine;
	private SplitArea splitArea;
	private List<? extends Object> data;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNameLocation() {
		return nameLocation;
	}

	public void setNameLocation(String nameLocation) {
		this.nameLocation = nameLocation;
	}

	public Object getNameTextStyle() {
		return nameTextStyle;
	}

	public void setNameTextStyle(Object nameTextStyle) {
		this.nameTextStyle = nameTextStyle;
	}

	public Object getBoundaryGap() {
		return boundaryGap;
	}

	public void setBoundaryGap(Object boundaryGap) {
		this.boundaryGap = boundaryGap;
	}

	public Integer getMin() {
		return min;
	}

	public void setMin(Integer min) {
		this.min = min;
	}

	public Integer getMax() {
		return max;
	}

	public void setMax(Integer max) {
		this.max = max;
	}

	public Boolean getScale() {
		return scale;
	}

	public void setScale(Boolean scale) {
		this.scale = scale;
	}

	public Integer getPrecision() {
		return precision;
	}

	public void setPrecision(Integer precision) {
		this.precision = precision;
	}

	public Integer getPower() {
		return power;
	}

	public void setPower(Integer power) {
		this.power = power;
	}

	public Integer getSplitNumber() {
		return splitNumber;
	}

	public void setSplitNumber(Integer splitNumber) {
		this.splitNumber = splitNumber;
	}

	public AxisLine getAxisLine() {
		return axisLine;
	}

	public void setAxisLine(AxisLine axisLine) {
		this.axisLine = axisLine;
	}

	public AxisTick getAxisTick() {
		return axisTick;
	}

	public void setAxisTick(AxisTick axisTick) {
		this.axisTick = axisTick;
	}

	public AxisLabel getAxisLabel() {
		return axisLabel;
	}

	public void setAxisLabel(AxisLabel axisLabel) {
		this.axisLabel = axisLabel;
	}

	public SplitLine getSplitLine() {
		return splitLine;
	}

	public void setSplitLine(SplitLine splitLine) {
		this.splitLine = splitLine;
	}

	public SplitArea getSplitArea() {
		return splitArea;
	}

	public void setSplitArea(SplitArea splitArea) {
		this.splitArea = splitArea;
	}

	public List<? extends Object> getData() {
		return data;
	}

	public void setData(List<? extends Object> data) {
		this.data = data;
	}

}
