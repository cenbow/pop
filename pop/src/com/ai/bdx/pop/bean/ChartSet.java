package com.ai.bdx.pop.bean;


public class ChartSet {

	/**
	 * x轴显示的信息 对应echart中的xAxis.data项 该字段不能为空
	 */
	private String label;

	/**
	 * y轴的值 不能为null 没有值设置为空字符串 对应echart中的series.data项
	 * 
	 * begin of zhuyq3
	 * 表示百分比时请传入纯小数
	 * 常用于右侧y轴
	 * end of zhuyq3 2014-12-23 16:53:45
	 */
	private String value;

	/**
	 * 鼠标悬停信息 对应series.markPoint.data.name
	 */
	private String tooltext;

	/**
	 * 对应series.markPoint.data.type
	 */
	private String tooltype;

	/**
	 * 扩展信息 json格式
	 */
	private String extendInfo;

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getExtendInfo() {
		return extendInfo;
	}

	public void setExtendInfo(String extendInfo) {
		this.extendInfo = extendInfo;
	}

	public String getTooltext() {
		return tooltext;
	}

	public void setTooltext(String tooltext) {
		this.tooltext = tooltext;
	}

	public String getTooltype() {
		return tooltype;
	}

	public void setTooltype(String tooltype) {
		this.tooltype = tooltype;
	}

}
