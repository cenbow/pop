package com.ai.bdx.pop.bean.echarts.axis;

import java.io.Serializable;

public class Indicator implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2829293847877090005L;

	private String text;
	private String max;

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getMax() {
		return max;
	}

	public void setMax(String max) {
		this.max = max;
	}

	public Indicator(String text, String max) {
		super();
		this.text = text;
		this.max = max;
	}

	public Indicator() {
		super();
		// TODO Auto-generated constructor stub
	}

}
