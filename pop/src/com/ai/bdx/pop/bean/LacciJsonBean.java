package com.ai.bdx.pop.bean;

/**
 * @ClassName: LacciJson
 * @Description: 获取基站信息返回JSON，用于转换对象的BEAN
 * @author jinlong
 * @date 2016-4-18 下午3:39:32
 * 
 */
public class LacciJsonBean {

	private String lacci;
	
	private String station_type;

	public String getLacci() {
		return lacci;
	}

	public void setLacci(String lacci) {
		this.lacci = lacci;
	}

	public String getStation_type() {
		return station_type;
	}

	public void setStation_type(String station_type) {
		this.station_type = station_type;
	}
	
	
}
