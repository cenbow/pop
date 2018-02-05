package com.ai.bdx.pop.bean;

import java.io.Serializable;

/**
 * 小区表实体类
 * @author hpa
 *
 */
public class DimCpeLacCi implements Serializable{

	private static final long serialVersionUID = 1L;
	
	public DimCpeLacCi(){
		
	}
	
	public DimCpeLacCi(String lacCiHexCode, String lacCiDecId,
			String lacHexCode, String lacDecId, String ciHexCode,
			String ciDecId, String cellName, String stationCode,
			String userLocation) {
		super();
		this.lacCiHexCode = lacCiHexCode;
		this.lacCiDecId = lacCiDecId;
		this.lacHexCode = lacHexCode;
		this.lacDecId = lacDecId;
		this.ciHexCode = ciHexCode;
		this.ciDecId = ciDecId;
		this.cellName = cellName;
		this.stationCode = stationCode;
		this.userLocation = userLocation;
	}

	/**
	 * LAC_CI十六进制编码
	 */
	private String lacCiHexCode;
	
	/**
	 * LAC_CI十进制编码
	 */
	private String lacCiDecId;
	
	/**
	 * LAC十六进制编码
	 */
	private String lacHexCode;
	
	/**
	 * LAC十进制标识
	 */
	private String lacDecId;
	
	/**
	 * CI十六进制编码
	 */
	private String ciHexCode;
	
	/**
	 * CI十进制标识
	 */
	private String ciDecId;
	
	/**
	 * 小区中文名称
	 */
	private String cellName;
	
	/**
	 * 基站编码
	 */
	private String stationCode;
	
	private String userLocation;

	public String getLacCiHexCode() {
		return lacCiHexCode;
	}

	public void setLacCiHexCode(String lacCiHexCode) {
		this.lacCiHexCode = lacCiHexCode;
	}

	public String getLacCiDecId() {
		return lacCiDecId;
	}

	public void setLacCiDecId(String lacCiDecId) {
		this.lacCiDecId = lacCiDecId;
	}

	public String getLacHexCode() {
		return lacHexCode;
	}

	public void setLacHexCode(String lacHexCode) {
		this.lacHexCode = lacHexCode;
	}

	public String getLacDecId() {
		return lacDecId;
	}

	public void setLacDecId(String lacDecId) {
		this.lacDecId = lacDecId;
	}

	public String getCiHexCode() {
		return ciHexCode;
	}

	public void setCiHexCode(String ciHexCode) {
		this.ciHexCode = ciHexCode;
	}

	public String getCiDecId() {
		return ciDecId;
	}

	public void setCiDecId(String ciDecId) {
		this.ciDecId = ciDecId;
	}

	public String getCellName() {
		return cellName;
	}

	public void setCellName(String cellName) {
		this.cellName = cellName;
	}

	public String getStationCode() {
		return stationCode;
	}

	public void setStationCode(String stationCode) {
		this.stationCode = stationCode;
	}

	public String getUserLocation() {
		return userLocation;
	}

	public void setUserLocation(String userLocation) {
		this.userLocation = userLocation;
	}
	
}
