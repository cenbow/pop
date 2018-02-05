package com.ai.bdx.pop.bean.echarts.base;


public class Data {
	private String name;
	private String value;
	
	public Data(){
		
	}
	
	public Data(String name,String value){
		this.name = name;
		this.value = value;
	}
	
	public String getName() {
		return name;
	}
	public Data setName(String name) {
		this.name = name;
		return this;
	}
	public String getValue() {
		return value;
	}
	public Data setValue(String value) {
		this.value = value;
		return this;
	}
	
	
}
