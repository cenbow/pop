package com.ai.bdx.pop.bean.echarts.series;

import java.io.Serializable;

import com.ai.bdx.pop.bean.echarts.series.util.EchartsConstants;

/**
 * 4 MarkPoint and MarkLine data
 * @author _momo
 *
 */
public class Data4Mark implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3608842673564705911L;

	String type;
	String name;
	double value;
	int xAxis;
	int yAxis;
	int symbolSize = EchartsConstants.DEFAULT_MARKPOINT_SYMBOLSIZE;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}

	public int getxAxis() {
		return xAxis;
	}

	public void setxAxis(int xAxis) {
		this.xAxis = xAxis;
	}

	public int getyAxis() {
		return yAxis;
	}

	public void setyAxis(int yAxis) {
		this.yAxis = yAxis;
	}

	public int getSymbolSize() {
		return symbolSize;
	}

	public void setSymbolSize(int symbolSize) {
		this.symbolSize = symbolSize;
	}

	public Data4Mark(String name, String type) {
		super();
		this.name = name;
		this.type = type;
	}

}
