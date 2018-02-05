package com.ai.bdx.pop.bean.echarts.style;



public class SubItemStyle {

	private String color;
	private LineStyle lineStyle;
	private AreaStyle areaStyle;
	private ChordStyle chordStyle;
	private NodeStyle nodeStyle;
	private LinkStyle linkStyle;
	private String borderColor;
	private Integer borderWidth;
	private String barBorderColor;
	private Integer[] barBorderRadius;
	private Integer barBorderWidth;
	private Label label;
	private LabelLine labelLine;
	
	public LineStyle newLineStyle(){
		LineStyle o = new LineStyle();
		this.setLineStyle(o);
		return o;
	}
	
	public AreaStyle newAreaStyle(){
		AreaStyle o = new AreaStyle();
		this.setAreaStyle(o);
		return o;
	}
	public ChordStyle newChordStyle(){
		ChordStyle o = new ChordStyle();
		this.setChordStyle(o);
		return o;
	}
	
	public NodeStyle newNodeStyle(){
		NodeStyle o = new NodeStyle();
		this.setNodeStyle(o);
		return o;
	}
	
	public LinkStyle newLinkStyle(){
		LinkStyle o = new LinkStyle();
		this.setLinkStyle(o);
		return o;
	}
	
	public Label newLabel(){
		Label o = new Label();
		this.setLabel(o);
		return o;
	}
	
	public LabelLine newLabelLine(){
		LabelLine o = new LabelLine();
		this.setLabelLine(o);
		return o;
	}
	
	public String getColor() {
		return color;
	}
	public SubItemStyle setColor(String color) {
		this.color = color;
		return this;
	}
	public LineStyle getLineStyle() {
		return lineStyle;
	}
	public SubItemStyle setLineStyle(LineStyle lineStyle) {
		this.lineStyle = lineStyle;
		return this;
	}
	public AreaStyle getAreaStyle() {
		return areaStyle;
	}
	public SubItemStyle setAreaStyle(AreaStyle areaStyle) {
		this.areaStyle = areaStyle;
		return this;
	}
	public ChordStyle getChordStyle() {
		return chordStyle;
	}
	public SubItemStyle setChordStyle(ChordStyle chordStyle) {
		this.chordStyle = chordStyle;
		return this;
	}
	public NodeStyle getNodeStyle() {
		return nodeStyle;
	}
	public SubItemStyle setNodeStyle(NodeStyle nodeStyle) {
		this.nodeStyle = nodeStyle;
		return this;
	}
	public LinkStyle getLinkStyle() {
		return linkStyle;
	}
	public SubItemStyle setLinkStyle(LinkStyle linkStyle) {
		this.linkStyle = linkStyle;
		return this;
	}
	public String getBorderColor() {
		return borderColor;
	}
	public SubItemStyle setBorderColor(String borderColor) {
		this.borderColor = borderColor;
		return this;
	}
	public Integer getBorderWidth() {
		return borderWidth;
	}
	public SubItemStyle setBorderWidth(Integer borderWidth) {
		this.borderWidth = borderWidth;
		return this;
	}
	public String getBarBorderColor() {
		return barBorderColor;
	}
	public SubItemStyle setBarBorderColor(String barBorderColor) {
		this.barBorderColor = barBorderColor;
		return this;
	}
	public Integer[] getBarBorderRadius() {
		return barBorderRadius;
	}
	public SubItemStyle setBarBorderRadius(Integer[] barBorderRadius) {
		this.barBorderRadius = barBorderRadius;
		return this;
	}
	public Integer getBarBorderWidth() {
		return barBorderWidth;
	}
	public SubItemStyle setBarBorderWidth(Integer barBorderWidth) {
		this.barBorderWidth = barBorderWidth;
		return this;
	}
	public Label getLabel() {
		return label;
	}
	public SubItemStyle setLabel(Label label) {
		this.label = label;
		return this;
	}
	public LabelLine getLabelLine() {
		return labelLine;
	}
	public SubItemStyle setLabelLine(LabelLine labelLine) {
		this.labelLine = labelLine;
		return this;
	}
	
	
}
