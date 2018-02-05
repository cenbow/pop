package com.ai.bdx.pop.bean.echarts.series;

public class LineSeries extends BaseSeries {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2028263974706425588L;
	private String stack;
	private Integer xAxisIndex;
	private Integer yAxisIndex;
	private String barGap;
	private String barCategoryGap;
	private Integer barMinHeight;
	private Integer barWidth;
	private Integer barMaxWidth;
	private String symbol;
	private Integer symbolSize;
	private Integer symbolRotate;
	private Boolean showAllSymbol;
	private Boolean smooth;
	private Boolean large;
	private Integer largeThreshold;

	public String getStack() {
		return stack;
	}

	public void setStack(String stack) {
		this.stack = stack;
	}

	public Integer getxAxisIndex() {
		return xAxisIndex;
	}

	public void setxAxisIndex(Integer xAxisIndex) {
		this.xAxisIndex = xAxisIndex;
	}

	public Integer getyAxisIndex() {
		return yAxisIndex;
	}

	public void setyAxisIndex(Integer yAxisIndex) {
		this.yAxisIndex = yAxisIndex;
	}

	public String getBarGap() {
		return barGap;
	}

	public void setBarGap(String barGap) {
		this.barGap = barGap;
	}

	public String getBarCategoryGap() {
		return barCategoryGap;
	}

	public void setBarCategoryGap(String barCategoryGap) {
		this.barCategoryGap = barCategoryGap;
	}

	public Integer getBarMinHeight() {
		return barMinHeight;
	}

	public void setBarMinHeight(Integer barMinHeight) {
		this.barMinHeight = barMinHeight;
	}

	public Integer getBarWidth() {
		return barWidth;
	}

	public void setBarWidth(Integer barWidth) {
		this.barWidth = barWidth;
	}

	public Integer getBarMaxWidth() {
		return barMaxWidth;
	}

	public void setBarMaxWidth(Integer barMaxWidth) {
		this.barMaxWidth = barMaxWidth;
	}

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public Integer getSymbolSize() {
		return symbolSize;
	}

	public void setSymbolSize(Integer symbolSize) {
		this.symbolSize = symbolSize;
	}

	public Integer getSymbolRotate() {
		return symbolRotate;
	}

	public void setSymbolRotate(Integer symbolRotate) {
		this.symbolRotate = symbolRotate;
	}

	public Boolean getShowAllSymbol() {
		return showAllSymbol;
	}

	public void setShowAllSymbol(Boolean showAllSymbol) {
		this.showAllSymbol = showAllSymbol;
	}

	public Boolean getSmooth() {
		return smooth;
	}

	public void setSmooth(Boolean smooth) {
		this.smooth = smooth;
	}

	public Boolean getLarge() {
		return large;
	}

	public void setLarge(Boolean large) {
		this.large = large;
	}

	public Integer getLargeThreshold() {
		return largeThreshold;
	}

	public void setLargeThreshold(Integer largeThreshold) {
		this.largeThreshold = largeThreshold;
	}

}
