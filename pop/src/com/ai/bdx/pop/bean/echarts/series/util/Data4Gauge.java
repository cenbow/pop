package com.ai.bdx.pop.bean.echarts.series.util;

import java.io.Serializable;

public class Data4Gauge implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 238366250473284054L;
	/**
	 * 
	 * @author zhuyq3@asiainfo.com
	 * 
	 */

	private String value;
	private String name;

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
