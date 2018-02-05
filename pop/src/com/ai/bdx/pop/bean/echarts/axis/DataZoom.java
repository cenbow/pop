package com.ai.bdx.pop.bean.echarts.axis;


public class DataZoom {

	private Boolean show;
	private String orient;
	private Integer x;
	private Integer y;
	private Integer width;
	private Integer height;
	private String backgroundColor;
	private String dataBackgroundColor;
	private String fillerColor;
	private String handleColor;
	private Integer[] xAxisIndex;
	private Integer[] yAxisIndex;
	private Integer start;
	private Integer end;
	private Boolean realtime;
	private Boolean zoomLock;
	
	public Boolean getShow() {
		return show;
	}
	public DataZoom setShow(Boolean show) {
		this.show = show;
		return this;
	}
	public String getOrient() {
		return orient;
	}
	public DataZoom setOrient(String orient) {
		this.orient = orient;
		return this;
	}
	public Integer getX() {
		return x;
	}
	public DataZoom setX(Integer x) {
		this.x = x;
		return this;
	}
	public Integer getY() {
		return y;
	}
	public DataZoom setY(Integer y) {
		this.y = y;
		return this;
	}
	public Integer getWidth() {
		return width;
	}
	public DataZoom setWidth(Integer width) {
		this.width = width;
		return this;
	}
	public Integer getHeight() {
		return height;
	}
	public DataZoom setHeight(Integer height) {
		this.height = height;
		return this;
	}
	public String getBackgroundColor() {
		return backgroundColor;
	}
	public DataZoom setBackgroundColor(String backgroundColor) {
		this.backgroundColor = backgroundColor;
		return this;
	}
	public String getDataBackgroundColor() {
		return dataBackgroundColor;
	}
	public DataZoom setDataBackgroundColor(String dataBackgroundColor) {
		this.dataBackgroundColor = dataBackgroundColor;
		return this;
	}
	public String getFillerColor() {
		return fillerColor;
	}
	public DataZoom setFillerColor(String fillerColor) {
		this.fillerColor = fillerColor;
		return this;
	}
	public String getHandleColor() {
		return handleColor;
	}
	public DataZoom setHandleColor(String handleColor) {
		this.handleColor = handleColor;
		return this;
	}
	public Integer[] getxAxisIndex() {
		return xAxisIndex;
	}
	public DataZoom setxAxisIndex(Integer[] xAxisIndex) {
		this.xAxisIndex = xAxisIndex;
		return this;
	}
	public Integer[] getyAxisIndex() {
		return yAxisIndex;
	}
	public DataZoom setyAxisIndex(Integer[] yAxisIndex) {
		this.yAxisIndex = yAxisIndex;
		return this;
	}
	public Integer getStart() {
		return start;
	}
	public DataZoom setStart(Integer start) {
		this.start = start;
		return this;
	}
	public Integer getEnd() {
		return end;
	}
	public DataZoom setEnd(Integer end) {
		this.end = end;
		return this;
	}
	public Boolean getRealtime() {
		return realtime;
	}
	public DataZoom setRealtime(Boolean realtime) {
		this.realtime = realtime;
		return this;
	}
	public Boolean getZoomLock() {
		return zoomLock;
	}
	public DataZoom setZoomLock(Boolean zoomLock) {
		this.zoomLock = zoomLock;
		return this;
	}
	
	
}
