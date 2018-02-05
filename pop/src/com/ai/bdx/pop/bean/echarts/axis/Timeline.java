package com.ai.bdx.pop.bean.echarts.axis;

import java.util.ArrayList;
import java.util.List;

import com.ai.bdx.pop.bean.echarts.style.CheckpointStyle;
import com.ai.bdx.pop.bean.echarts.style.ItemStyle;
import com.ai.bdx.pop.bean.echarts.style.Label;
import com.ai.bdx.pop.bean.echarts.style.LineStyle;

public class Timeline {
	
	private Boolean show;
	private String type;
	private Boolean notMerge;
	private Boolean realtime;
	private String x;
	private String y;
	private String x2;
	private String y2;
	private String width;
	private String height;
	private String backgroundColor;
	
	private Integer borderWidth;
	private String borderColor;
	private String[] padding;
	private String controlPosition;
	private Boolean autoPlay;
	private Boolean loop;
	private Integer playInterval;
	private LineStyle lineStyle;
	private Label label;
	private CheckpointStyle checkpointStyle;
	private ItemStyle controlStyle;
	private String symbol;
	private Integer symbolSize;
	private Integer currentIndex;
	private List<Object> data;
	
	public LineStyle newLineStyle(){
		LineStyle o = new LineStyle();
		this.setLineStyle(o);
		return o;
	}
	
	public Label newLabel(){
		Label o = new Label();
		this.setLabel(o);
		return o;
	}
	
	public CheckpointStyle newCheckpointStyle(){
		CheckpointStyle o = new CheckpointStyle();
		this.setCheckpointStyle(o);
		return o;
	}
	
	public ItemStyle newControlStyle(){
		ItemStyle o = new ItemStyle();
		this.setControlStyle(o);
		return o;
	}
	
	public List<Object> newData(){
		List<Object> o = new ArrayList<Object>();
		this.setData(o);
		return o;
	}
	
	public Boolean getShow() {
		return show;
	}
	public Timeline setShow(Boolean show) {
		this.show = show;
		return this;
	}
	public String getType() {
		return type;
	}
	public Timeline setType(String type) {
		this.type = type;
		return this;
	}
	public Boolean getNotMerge() {
		return notMerge;
	}
	public Timeline setNotMerge(Boolean notMerge) {
		this.notMerge = notMerge;
		return this;
	}
	public Boolean getRealtime() {
		return realtime;
	}
	public Timeline setRealtime(Boolean realtime) {
		this.realtime = realtime;
		return this;
	}
	public String getX() {
		return x;
	}
	public Timeline setX(String x) {
		this.x = x;
		return this;
	}
	public String getY() {
		return y;
	}
	public Timeline setY(String y) {
		this.y = y;
		return this;
	}
	public String getX2() {
		return x2;
	}
	public Timeline setX2(String x2) {
		this.x2 = x2;
		return this;
	}
	public String getY2() {
		return y2;
	}
	public Timeline setY2(String y2) {
		this.y2 = y2;
		return this;
	}
	public String getWidth() {
		return width;
	}
	public Timeline setWidth(String width) {
		this.width = width;
		return this;
	}
	public String getHeight() {
		return height;
	}
	public Timeline setHeight(String height) {
		this.height = height;
		return this;
	}
	public String getBackgroundColor() {
		return backgroundColor;
	}
	public Timeline setBackgroundColor(String backgroundColor) {
		this.backgroundColor = backgroundColor;
		return this;
	}
	public Integer getBorderWidth() {
		return borderWidth;
	}
	public Timeline setBorderWidth(Integer borderWidth) {
		this.borderWidth = borderWidth;
		return this;
	}
	public String getBorderColor() {
		return borderColor;
	}
	public Timeline setBorderColor(String borderColor) {
		this.borderColor = borderColor;
		return this;
	}
	public String[] getPadding() {
		return padding;
	}
	public Timeline setPadding(String[] padding) {
		this.padding = padding;
		return this;
	}
	public String getControlPosition() {
		return controlPosition;
	}
	public Timeline setControlPosition(String controlPosition) {
		this.controlPosition = controlPosition;
		return this;
	}
	public Boolean getAutoPlay() {
		return autoPlay;
	}
	public Timeline setAutoPlay(Boolean autoPlay) {
		this.autoPlay = autoPlay;
		return this;
	}
	public Boolean getLoop() {
		return loop;
	}
	public Timeline setLoop(Boolean loop) {
		this.loop = loop;
		return this;
	}
	public Integer getPlayInterval() {
		return playInterval;
	}
	public Timeline setPlayInterval(Integer playInterval) {
		this.playInterval = playInterval;
		return this;
	}
	
	public String getSymbol() {
		return symbol;
	}
	public Timeline setSymbol(String symbol) {
		this.symbol = symbol;
		return this;
	}
	public Integer getSymbolSize() {
		return symbolSize;
	}
	public Timeline setSymbolSize(Integer symbolSize) {
		this.symbolSize = symbolSize;
		return this;
	}
	public Integer getCurrentIndex() {
		return currentIndex;
	}
	public Timeline setCurrentIndex(Integer currentIndex) {
		this.currentIndex = currentIndex;
		return this;
	}
	public List<Object> getData() {
		return data;
	}
	public Timeline setData(List<Object> data) {
		this.data = data;
		return this;
	}
	public LineStyle getLineStyle() {
		return lineStyle;
	}
	public Timeline setLineStyle(LineStyle lineStyle) {
		this.lineStyle = lineStyle;
		return this;
	}
	public Label getLabel() {
		return label;
	}
	public Timeline setLabel(Label label) {
		this.label = label;
		return this;
	}
	public CheckpointStyle getCheckpointStyle() {
		return checkpointStyle;
	}
	public Timeline setCheckpointStyle(CheckpointStyle checkpointStyle) {
		this.checkpointStyle = checkpointStyle;
		return this;
	}
	public ItemStyle getControlStyle() {
		return controlStyle;
	}
	public Timeline setControlStyle(ItemStyle controlStyle) {
		this.controlStyle = controlStyle;
		return this;
	}
	
	

}
