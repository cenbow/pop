package com.ai.bdx.pop.bean.echarts.series;

/**
 * 值域选择，每个图表最多仅有一个值域控件
 * 
 * @author hanjn
 * 
 */
public class ChartDataRange {

	/**
	 * 分割段数
	 */
	private int splitNumber;

	/**
	 * 值域颜色标识，颜色数组长度必须>=2，颜色代表从数值高到低的变化， 即颜色数组低位代表数值高的颜色标识 ，支持Alpha通道上的变化（rgba）
	 */
	private String[] colors;

	private String[] ranges;

	/**
	 * 值域文字显示，splitNumber生效时默认以计算所得数值作为值域文字显示， 可指定长度为2的文本数组显示简介的值域文本，如['高', '低']
	 */
	private String[] texts;

	public int getSplitNumber() {
		return splitNumber;
	}

	public void setSplitNumber(int splitNumber) {
		this.splitNumber = splitNumber;
	}

	public String[] getColors() {
		return colors;
	}

	public void setColors(String[] colors) {
		this.colors = colors;
	}

	public String[] getTexts() {
		return texts;
	}

	public void setTexts(String[] texts) {
		this.texts = texts;
	}

	public String[] getRanges() {
		return ranges;
	}

	public void setRanges(String[] ranges) {
		this.ranges = ranges;
	}

}
