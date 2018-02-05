package com.ai.bdx.pop.bean.echarts.base;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ai.bdx.pop.bean.echarts.style.ItemStyle;

public class MarkBase {

	private Boolean show;
	private Boolean loop;
	private Integer period;
	private Integer scaleSize;
	
	private String color;
	private String shadowColor;
	private String shadowBlur;
	
	private ItemStyle itemStyle;
	private Map<String,String[]> geoCoord;
	private Effect effect;
	
	private List<Object> data = new ArrayList<Object>();
	
	public ItemStyle newItemStyle(){
		ItemStyle o = new ItemStyle();
		this.setItemStyle(o);
		return o;
	}
	
	public Map<String,String[]> newGeoCoord(){
		Map<String,String[]> o = new HashMap<String,String[]>();
		this.setGeoCoord(o);
		return o;
	}
	
	public List<Object> newData(){
		List<Object> o = new ArrayList<Object>();
		this.setData(o);
		return o;
	}
	
	public Effect newEffect(){
		Effect o = new Effect();
		this.setEffect(o);
		return o;
	}

	public String getColor() {
		return color;
	}

	public MarkBase setColor(String color) {
		this.color = color;
		return this;
	}

	public String getShadowColor() {
		return shadowColor;
	}

	public MarkBase setShadowColor(String shadowColor) {
		this.shadowColor = shadowColor;
		return this;
	}

	public String getShadowBlur() {
		return shadowBlur;
	}

	public MarkBase setShadowBlur(String shadowBlur) {
		this.shadowBlur = shadowBlur;
		return this;
	}

	public Boolean getShow() {
		return show;
	}

	public MarkBase setShow(Boolean show) {
		this.show = show;
		return this;
	}

	public Boolean getLoop() {
		return loop;
	}

	public MarkBase setLoop(Boolean loop) {
		this.loop = loop;
		return this;
	}

	public ItemStyle getItemStyle() {
		return itemStyle;
	}

	public MarkBase setItemStyle(ItemStyle itemStyle) {
		this.itemStyle = itemStyle;
		return this;
	}

	public Map<String, String[]> getGeoCoord() {
		return geoCoord;
	}

	public MarkBase setGeoCoord(Map<String, String[]> geoCoord) {
		this.geoCoord = geoCoord;
		return this;
	}

	public Effect getEffect() {
		return effect;
	}

	public MarkBase setEffect(Effect effect) {
		this.effect = effect;
		return this;
	}

	public MarkBase setPeriod(Integer period) {
		this.period = period;
		return this;
	}

	public MarkBase setScaleSize(Integer scaleSize) {
		this.scaleSize = scaleSize;
		return this;
	}

	public List<Object> getData() {
		return data;
	}

	public MarkBase setData(List<Object> data) {
		this.data = data;
		return this;
	}

	public Integer getPeriod() {
		return period;
	}

	public Integer getScaleSize() {
		return scaleSize;
	}
	
	
}
