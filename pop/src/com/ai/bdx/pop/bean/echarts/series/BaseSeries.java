package com.ai.bdx.pop.bean.echarts.series;

import java.util.List;

import com.ai.bdx.pop.bean.echarts.axis.Tooltip;
import com.ai.bdx.pop.bean.echarts.base.MarkLine;
import com.ai.bdx.pop.bean.echarts.base.MarkPoint;
import com.ai.bdx.pop.bean.echarts.style.ItemStyle;

public class BaseSeries<T> implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2595022149090687369L;
	private String name;
	private String type;

	private List<T> data;

	private Tooltip tooltip;
	private Boolean clickable;
	private ItemStyle itemStyle;

	private MarkPoint markPoint;
	private MarkLine markLine;

	private String[] center;
	private Object radius;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public List<T> getData() {
		return data;
	}

	public void setData(List<T> data) {
		this.data = data;
	}

	public Tooltip getTooltip() {
		return tooltip;
	}

	public void setTooltip(Tooltip tooltip) {
		this.tooltip = tooltip;
	}

	public Boolean getClickable() {
		return clickable;
	}

	public void setClickable(Boolean clickable) {
		this.clickable = clickable;
	}

	public ItemStyle getItemStyle() {
		return itemStyle;
	}

	public void setItemStyle(ItemStyle itemStyle) {
		this.itemStyle = itemStyle;
	}

	public MarkPoint getMarkPoint() {
		return markPoint;
	}

	public void setMarkPoint(MarkPoint markPoint) {
		this.markPoint = markPoint;
	}

	public MarkLine getMarkLine() {
		return markLine;
	}

	public void setMarkLine(MarkLine markLine) {
		this.markLine = markLine;
	}

	public String[] getCenter() {
		return center;
	}

	public void setCenter(String[] center) {
		this.center = center;
	}

	public Object getRadius() {
		return radius;
	}

	public void setRadius(Object radius) {
		this.radius = radius;
	}
}
