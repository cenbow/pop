package com.ai.bdx.pop.bean;


import java.util.List;

public class ChartDataset {

	/**
	 * 图例名称
	 * 对应echart中的series.name
	 * 该字段不能为空
	 */
	private String seriesName;
	
	/**
	 * 渲染成图表类型
	 * 对应echart中的series.type
	 */
	private String renderAs;
	
	/**
	 * 该组数据所在的y轴是左侧y轴还是右侧y轴
	 * 如果是左侧y轴 则值为P
	 * 如果是右侧y轴则值为S
	 * 在双y轴时 该字段不能为空
	 */
	private String parentYAxis;
	
	/**
	 * 堆积图中一个柱子的组名
	 * 该属性只用在堆积图中 
	 * 在非堆积图中可以为空
	 * 例如有ABCD四组数据，如果A和C的stackName都为 "组1"
	 * B和D的组名都为 "组2"，那么A和C堆积成一根柱子，B和D堆积成一根柱子
	 */
	private String stackName;
	
	/**
	 * 数据信息
	 * 对应echart中的xAxis.data和series.data
	 */
	private List<ChartSet> sets;

	public String getSeriesName() {
		return seriesName;
	}

	public void setSeriesName(String seriesName) {
		this.seriesName = seriesName;
	}

	public String getRenderAs() {
		return renderAs;
	}

	public void setRenderAs(String renderAs) {
		this.renderAs = renderAs;
	}

	public List<ChartSet> getSets() {
		return sets;
	}

	public void setSets(List<ChartSet> sets) {
		this.sets = sets;
	}

	public String getParentYAxis() {
		return parentYAxis;
	}

	public void setParentYAxis(String parentYAxis) {
		this.parentYAxis = parentYAxis;
	}

	public String getStackName() {
		return stackName;
	}

	public void setStackName(String stackName) {
		this.stackName = stackName;
	}
	
}
