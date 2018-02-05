package com.ai.bdx.pop.bean;

import java.io.Serializable;

/**
 * @ClassName: LacciBean
 * @Description: Lacci(ICEP获取的基站信息bean)
 * @author jinlong
 * @date 2015-9-7 下午5:49:43
 * 
 */
public class LacciBean implements Serializable  {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6849914986334350963L;
	
	/**
	 * lacci ID
	 */
	private String lacci;
	/**
	 * lacci 名称
	 */
	private String lacci_name;
	public String getLacci() {
		return lacci;
	}
	public void setLacci(String lacci) {
		this.lacci = lacci;
	}
	public String getLacci_name() {
		return lacci_name;
	}
	public void setLacci_name(String lacci_name) {
		this.lacci_name = lacci_name;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
