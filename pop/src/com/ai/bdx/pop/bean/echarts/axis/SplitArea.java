package com.ai.bdx.pop.bean.echarts.axis;

import com.ai.bdx.pop.bean.echarts.style.AreaStyle;

public class SplitArea {
	private Boolean show;
	private Boolean onGap;
	private AreaStyle areaStyle;
	
	public AreaStyle newAreaStyle(){
		AreaStyle o = new AreaStyle();
		this.setAreaStyle(o);
		return o;
	}
	
	public Boolean getShow() {
		return show;
	}
	public SplitArea setShow(Boolean show) {
		this.show = show;
		return this;
	}
	public Boolean getOnGap() {
		return onGap;
	}
	public SplitArea setOnGap(Boolean onGap) {
		this.onGap = onGap;
		return this;
	}
	public AreaStyle getAreaStyle() {
		return areaStyle;
	}
	public SplitArea setAreaStyle(AreaStyle areaStyle) {
		this.areaStyle = areaStyle;
		return this;
	}
}
