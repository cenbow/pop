package com.ai.bdx.pop.bean;

import java.util.List;

/**
 * 图表的基本信息 以fusionchart api为对应 echart会有对应的字段
 * 
 * @author hanjn
 * 
 */
public class ChartInfoBase {

	/**
	 * 图表的标题 对应echart中的title.text 该字段不能为空
	 */
	private String caption;

	/**
	 * 图表的副标题 对应echart中的title.subtext
	 */
	private String subcaption;

	/**
	 * 图表的类型 Column2D Pie2D Line Bar2D Area2D StackedColumn2DLineDY 双y轴堆积图
	 * MSStackedColumn2DLineDY 多系列双y轴堆积图 StackedColumn2D 单y轴堆积图 MSCombiDY2D
	 * 双y轴组合图 柱子+折线等 Doughnut2D 圆环图 Gauges2D 仪表盘 MapChina 中国地图 MapBeiJing 北京地图
	 * Tornado 旋风图 (左右条形图) 对应echart中的bar line等 该字段不能为空
	 */
	private String chartType;

	/**
	 * x轴名称 对应echart中的xAxis.name
	 */
	// private String xAxisName;

	/**
	 * y轴名称 对应echart中的yAxis.name 该字段不能为空
	 */
	private String yAxisName;

	/**
	 * 右侧y轴名称 只有双y轴图表用到该属性 对应echart中的yAxis.name的第二组数据 在双y轴时 该字段不能为空
	 */
	private String syAxisName;

	/**
	 * 扩展信息
	 */
	private String extendJsonInfo;

	/**
	 * 图表的数据信息 该字段不能为空
	 */
	private List<ChartDataset> datasets;

	public String getCaption() {
		return caption;
	}

	public void setCaption(String caption) {
		this.caption = caption;
	}

	public String getSubcaption() {
		return subcaption;
	}

	public void setSubcaption(String subcaption) {
		this.subcaption = subcaption;
	}

	public String getChartType() {
		return chartType;
	}

	public void setChartType(String chartType) {
		this.chartType = chartType;
	}

	public List<ChartDataset> getDatasets() {
		return datasets;
	}

	public void setDatasets(List<ChartDataset> datasets) {
		this.datasets = datasets;
	}

	// public String getxAxisName() {
	// return xAxisName;
	// }
	//
	// public void setxAxisName(String xAxisName) {
	// this.xAxisName = xAxisName;
	// }

	public String getyAxisName() {
		return yAxisName;
	}

	public void setyAxisName(String yAxisName) {
		this.yAxisName = yAxisName;
	}

	public String getExtendJsonInfo() {
		return extendJsonInfo;
	}

	public void setExtendJsonInfo(String extendJsonInfo) {
		this.extendJsonInfo = extendJsonInfo;
	}

	public String getSyAxisName() {
		return syAxisName;
	}

	public void setSyAxisName(String syAxisName) {
		this.syAxisName = syAxisName;
	}

}
