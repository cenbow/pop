package com.ai.bdx.pop.bean.echarts.series.util;

import java.io.Serializable;

public class Data4Map implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 238366250473284054L;
	/**
	 * 
	 * @author zhuyq3@asiainfo.com
	 * 
	 */

	private String name;
	private double value;

	public Data4Map(String name, double value) {
		super();
		this.name = name;
		this.value = value;
	}

	public Data4Map() {
		super();
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
