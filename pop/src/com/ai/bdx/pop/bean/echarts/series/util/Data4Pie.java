package com.ai.bdx.pop.bean.echarts.series.util;

import java.io.Serializable;

public class Data4Pie implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2286342552461755750L;
	private String name;
	private String value;

	public Data4Pie(String name, String value) {
		this.name = name;
		this.value = value;
	}

	public Data4Pie() {
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}
