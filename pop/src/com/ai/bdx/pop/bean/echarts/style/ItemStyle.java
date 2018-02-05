package com.ai.bdx.pop.bean.echarts.style;



public class ItemStyle {
	private SubItemStyle normal;
	private SubItemStyle emphasis;
	
	public SubItemStyle newNormal(){
		SubItemStyle o = new SubItemStyle();
		this.setNormal(o);
		return o;
	}
	
	public SubItemStyle newEmphasis(){
		SubItemStyle o = new SubItemStyle();
		this.setEmphasis(o);
		return o;
	}
	
	public SubItemStyle getNormal() {
		return normal;
	}
	public ItemStyle setNormal(SubItemStyle normal) {
		this.normal = normal;
		return this;
	}
	public SubItemStyle getEmphasis() {
		return emphasis;
	}
	public ItemStyle setEmphasis(SubItemStyle emphasis) {
		this.emphasis = emphasis;
		return this;
	}
	
	
}

