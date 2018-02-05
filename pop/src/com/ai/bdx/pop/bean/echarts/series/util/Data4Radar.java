package com.ai.bdx.pop.bean.echarts.series.util;

import java.io.Serializable;

/**
 * 自定义结构体，用于组装echarts中雷达图形所需数据
 * 
 * @author zhuyq3@asiainfo.com
 * 
 */
public class Data4Radar implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2833691514336777789L;

	private String name;
	private String[] value;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String[] getValue() {
		return value;
	}

	public void setValue(String[] value) {
		this.value = value;
	}

}
