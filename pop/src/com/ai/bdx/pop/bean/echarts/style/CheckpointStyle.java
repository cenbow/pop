package com.ai.bdx.pop.bean.echarts.style;



public class CheckpointStyle {

	private String symbol;
	private String symbolSize;
	private String color;
	private String borderColor;
	private String borderWidth;
	
	private Label label;
	
	public Label newLabel(){
		Label o = new Label();
		this.setLabel(o);
		return o;
	}

	public String getSymbol() {
		return symbol;
	}

	public CheckpointStyle setSymbol(String symbol) {
		this.symbol = symbol;
		return this;
	}

	public String getSymbolSize() {
		return symbolSize;
	}

	public CheckpointStyle setSymbolSize(String symbolSize) {
		this.symbolSize = symbolSize;
		return this;
	}

	public String getColor() {
		return color;
	}

	public CheckpointStyle setColor(String color) {
		this.color = color;
		return this;
	}

	public String getBorderColor() {
		return borderColor;
	}

	public CheckpointStyle setBorderColor(String borderColor) {
		this.borderColor = borderColor;
		return this;
	}

	public String getBorderWidth() {
		return borderWidth;
	}

	public CheckpointStyle setBorderWidth(String borderWidth) {
		this.borderWidth = borderWidth;
		return this;
	}

	public Label getLabel() {
		return label;
	}

	public CheckpointStyle setLabel(Label label) {
		this.label = label;
		return this;
	}
	
	
}
