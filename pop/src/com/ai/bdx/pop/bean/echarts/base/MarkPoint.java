package com.ai.bdx.pop.bean.echarts.base;


public class MarkPoint extends MarkBase{
	
	private String symbol;
	private Integer symbolSize;
	private Integer symbolRotate;
	private Boolean large;
	
	public String getSymbol() {
		return symbol;
	}
	public MarkPoint setSymbol(String symbol) {
		this.symbol = symbol;
		return this;
	}
	public Integer getSymbolSize() {
		return symbolSize;
	}
	public MarkPoint setSymbolSize(Integer symbolSize) {
		this.symbolSize = symbolSize;
		return this;
	}
	public Integer getSymbolRotate() {
		return symbolRotate;
	}
	public MarkPoint setSymbolRotate(Integer symbolRotate) {
		this.symbolRotate = symbolRotate;
		return this;
	}
	public Boolean getLarge() {
		return large;
	}
	public MarkPoint setLarge(Boolean large) {
		this.large = large;
		return this;
	}
	
}
