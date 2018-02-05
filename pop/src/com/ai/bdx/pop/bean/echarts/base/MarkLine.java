package com.ai.bdx.pop.bean.echarts.base;


public class MarkLine extends MarkBase{
	
	private Boolean smooth;
	
	private String[] symbol;
	private String[] symbolSize;
	private String[] symbolRotate;
	
	public String[] getSymbol() {
		return symbol;
	}
	public MarkLine setSymbol(String[] symbol) {
		this.symbol = symbol;
		return this;
	}
	public String[] getSymbolSize() {
		return symbolSize;
	}
	public MarkLine setSymbolSize(String[] symbolSize) {
		this.symbolSize = symbolSize;
		return this;
	}
	public String[] getSymbolRotate() {
		return symbolRotate;
	}
	public MarkLine setSymbolRotate(String[] symbolRotate) {
		this.symbolRotate = symbolRotate;
		return this;
	}
	public Boolean getSmooth() {
		return smooth;
	}
	public MarkLine setSmooth(Boolean smooth) {
		this.smooth = smooth;
		return this;
	}
	
	
	
}
