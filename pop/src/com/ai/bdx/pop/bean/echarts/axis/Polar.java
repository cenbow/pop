package com.ai.bdx.pop.bean.echarts.axis;

import com.ai.bdx.pop.bean.echarts.style.AreaStyle;
import com.ai.bdx.pop.bean.echarts.style.LineStyle;
import com.ai.bdx.pop.bean.echarts.style.NameStyle;

public class Polar implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3566112978557712609L;
	private String[] center;
	private String radius;
	private Integer startAngle;
	private Integer splitNumber;
	private NameStyle name;
	private Integer[] boundaryGap;
	private Boolean scale;
	private Integer precision;
	private Integer power;

	private LineStyle axisLine;
	private AxisLabel axisLabel;
	private LineStyle splitLine;
	private AreaStyle splitArea;
	private String type;
	private Indicator[] indicator;

	public NameStyle newName() {
		NameStyle o = new NameStyle();
		this.setName(o);
		return o;
	}

	public LineStyle newAxisLine() {
		LineStyle o = new LineStyle();
		this.setAxisLine(o);
		return o;
	}

	public AxisLabel newAxisLabel() {
		AxisLabel o = new AxisLabel();
		this.setAxisLabel(o);
		return o;
	}

	public LineStyle newSplitLine() {
		LineStyle o = new LineStyle();
		this.setSplitLine(o);
		return o;
	}

	public AreaStyle newSplitArea() {
		AreaStyle o = new AreaStyle();
		this.setSplitArea(o);
		return o;
	}

	public String[] getCenter() {
		return center;
	}

	public Polar setCenter(String[] center) {
		this.center = center;
		return this;
	}

	public String getRadius() {
		return radius;
	}

	public Polar setRadius(String radius) {
		this.radius = radius;
		return this;
	}

	public Integer getStartAngle() {
		return startAngle;
	}

	public Polar setStartAngle(Integer startAngle) {
		this.startAngle = startAngle;
		return this;
	}

	public Integer getSplitNumber() {
		return splitNumber;
	}

	public Polar setSplitNumber(Integer splitNumber) {
		this.splitNumber = splitNumber;
		return this;
	}

	public Integer[] getBoundaryGap() {
		return boundaryGap;
	}

	public Polar setBoundaryGap(Integer[] boundaryGap) {
		this.boundaryGap = boundaryGap;
		return this;
	}

	public Boolean getScale() {
		return scale;
	}

	public Polar setScale(Boolean scale) {
		this.scale = scale;
		return this;
	}

	public Integer getPrecision() {
		return precision;
	}

	public Polar setPrecision(Integer precision) {
		this.precision = precision;
		return this;
	}

	public Integer getPower() {
		return power;
	}

	public Polar setPower(Integer power) {
		this.power = power;
		return this;
	}

	public String getType() {
		return type;
	}

	public Polar setType(String type) {
		this.type = type;
		return this;
	}

	public NameStyle getName() {
		return name;
	}

	public Polar setName(NameStyle name) {
		this.name = name;
		return this;
	}

	public LineStyle getAxisLine() {
		return axisLine;
	}

	public Polar setAxisLine(LineStyle axisLine) {
		this.axisLine = axisLine;
		return this;
	}

	public AxisLabel getAxisLabel() {
		return axisLabel;
	}

	public Polar setAxisLabel(AxisLabel axisLabel) {
		this.axisLabel = axisLabel;
		return this;
	}

	public LineStyle getSplitLine() {
		return splitLine;
	}

	public Polar setSplitLine(LineStyle splitLine) {
		this.splitLine = splitLine;
		return this;
	}

	public AreaStyle getSplitArea() {
		return splitArea;
	}

	public Polar setSplitArea(AreaStyle splitArea) {
		this.splitArea = splitArea;
		return this;
	}

	public Indicator[] getIndicator() {
		return indicator;
	}

	public void setIndicator(Indicator[] indicator) {
		this.indicator = indicator;
	}

}
