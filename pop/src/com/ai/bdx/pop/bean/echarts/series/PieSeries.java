package com.ai.bdx.pop.bean.echarts.series;

import com.ai.bdx.pop.bean.echarts.series.util.Data4Pie;

public class PieSeries extends BaseSeries<Data4Pie> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7541646393534952767L;
	
	private Integer startAngle;
	private Integer minAngle;
	private Boolean clockWise;
	private String roseType;
	private Integer selectedOffset;
	private String selectedMode;

	public Integer getStartAngle() {
		return startAngle;
	}

	public PieSeries setStartAngle(Integer startAngle) {
		this.startAngle = startAngle;
		return this;
	}

	public Integer getMinAngle() {
		return minAngle;
	}

	public PieSeries setMinAngle(Integer minAngle) {
		this.minAngle = minAngle;
		return this;
	}

	public Boolean getClockWise() {
		return clockWise;
	}

	public PieSeries setClockWise(Boolean clockWise) {
		this.clockWise = clockWise;
		return this;
	}

	public String getRoseType() {
		return roseType;
	}

	public PieSeries setRoseType(String roseType) {
		this.roseType = roseType;
		return this;
	}

	public Integer getSelectedOffset() {
		return selectedOffset;
	}

	public PieSeries setSelectedOffset(Integer selectedOffset) {
		this.selectedOffset = selectedOffset;
		return this;
	}

	public String getSelectedMode() {
		return selectedMode;
	}

	public PieSeries setSelectedMode(String selectedMode) {
		this.selectedMode = selectedMode;
		return this;
	}

}
