package com.ai.bdx.pop.bean.echarts;


import java.util.List;

import com.ai.bdx.pop.bean.echarts.axis.Axis;
import com.ai.bdx.pop.bean.echarts.axis.DataRange;
import com.ai.bdx.pop.bean.echarts.axis.Grid;
import com.ai.bdx.pop.bean.echarts.axis.Legend;
import com.ai.bdx.pop.bean.echarts.axis.Polar;
import com.ai.bdx.pop.bean.echarts.axis.RoamController;
import com.ai.bdx.pop.bean.echarts.axis.Timeline;
import com.ai.bdx.pop.bean.echarts.axis.Toolbox;
import com.ai.bdx.pop.bean.echarts.axis.Tooltip;
import com.ai.bdx.pop.bean.echarts.feature.DataZoom;
import com.ai.bdx.pop.bean.echarts.series.BaseSeries;
import com.ai.bdx.pop.bean.echarts.series.Title;

public class Option implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 289438966287886032L;
	private String backgroundColor;
	private String[] color;
	private Boolean renderAsImage;
	private Boolean calculable;
	private Boolean animation;

	private Timeline timeline;
	private Title title;
	private Toolbox toolbox;
	private Tooltip tooltip;
	private Legend legend;
	private DataRange dataRange;
	private DataZoom dataZoom;
	private RoamController roamController;
	private Grid grid;
	private Axis[] xAxis;
	private Axis[] yAxis;
	@SuppressWarnings("rawtypes")
	private List<? extends BaseSeries> series;
	private List<Polar> polar;

	private String chartType;

	private List<Option> options;

	public String getBackgroundColor() {
		return backgroundColor;
	}

	public void setBackgroundColor(String backgroundColor) {
		this.backgroundColor = backgroundColor;
	}

	public String[] getColor() {
		return color;
	}

	public void setColor(String[] color) {
		this.color = color;
	}

	public Boolean getRenderAsImage() {
		return renderAsImage;
	}

	public void setRenderAsImage(Boolean renderAsImage) {
		this.renderAsImage = renderAsImage;
	}

	public Boolean getCalculable() {
		return calculable;
	}

	public void setCalculable(Boolean calculable) {
		this.calculable = calculable;
	}

	public Boolean getAnimation() {
		return animation;
	}

	public void setAnimation(Boolean animation) {
		this.animation = animation;
	}

	public Timeline getTimeline() {
		return timeline;
	}

	public void setTimeline(Timeline timeline) {
		this.timeline = timeline;
	}

	public Title getTitle() {
		return title;
	}

	public void setTitle(Title title) {
		this.title = title;
	}

	public Toolbox getToolbox() {
		return toolbox;
	}

	public void setToolbox(Toolbox toolbox) {
		this.toolbox = toolbox;
	}

	public Tooltip getTooltip() {
		return tooltip;
	}

	public void setTooltip(Tooltip tooltip) {
		this.tooltip = tooltip;
	}

	public Legend getLegend() {
		return legend;
	}

	public void setLegend(Legend legend) {
		this.legend = legend;
	}

	public DataRange getDataRange() {
		return dataRange;
	}

	public void setDataRange(DataRange dataRange) {
		this.dataRange = dataRange;
	}

	public DataZoom getDataZoom() {
		return dataZoom;
	}

	public void setDataZoom(DataZoom dataZoom) {
		this.dataZoom = dataZoom;
	}

	public RoamController getRoamController() {
		return roamController;
	}

	public void setRoamController(RoamController roamController) {
		this.roamController = roamController;
	}

	public Grid getGrid() {
		return grid;
	}

	public void setGrid(Grid grid) {
		this.grid = grid;
	}

	public Axis[] getxAxis() {
		return xAxis;
	}

	public void setxAxis(Axis[] xAxis) {
		this.xAxis = xAxis;
	}

	public Axis[] getyAxis() {
		return yAxis;
	}

	public void setyAxis(Axis[] yAxis) {
		this.yAxis = yAxis;
	}

	public List<? extends BaseSeries> getSeries() {
		return series;
	}

	public void setSeries(List<? extends BaseSeries> series) {
		this.series = series;
	}

	public List<Option> getOptions() {
		return options;
	}

	public void setOptions(List<Option> options) {
		this.options = options;
	}

	public List<Polar> getPolar() {
		return polar;
	}

	public void setPolar(List<Polar> polar) {
		this.polar = polar;
	}

	public String getChartType() {
		return chartType;
	}

	public void setChartType(String chartType) {
		this.chartType = chartType;
	}

}
