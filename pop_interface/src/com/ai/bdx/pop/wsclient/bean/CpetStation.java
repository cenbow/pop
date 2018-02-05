package com.ai.bdx.pop.wsclient.bean;

public class CpetStation {
	private String userLocation;
	private String enodebId;
	private String provinceName;
	private String stationName;        
	private String countryName;        
	private String townName;             
	private String countyName;          
	private String cityName;
	private String cellName;
	private String cell_id;
	private String status;
	private String createTime;
	private String updateTime;
	public String getUserLocation() {
		return userLocation;
	}
	public void setUserLocation(String userLocation) {
		this.userLocation = userLocation;
	}
	public String getEnodebId() {
		return enodebId;
	}
	public void setEnodebId(String enodebId) {
		this.enodebId = enodebId;
	}
	public String getProvinceName() {
		return provinceName;
	}
	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}
	public String getStationName() {
		return stationName;
	}
	public void setStationName(String stationName) {
		this.stationName = stationName;
	}
	public String getCountryName() {
		return countryName;
	}
	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}
	public String getTownName() {
		return townName;
	}
	public void setTownName(String townName) {
		this.townName = townName;
	}
	public String getCountyName() {
		return countyName;
	}
	public void setCountyName(String countyName) {
		this.countyName = countyName;
	}
	public String getCityName() {
		return cityName;
	}
	public void setCityName(String cityName) {
		this.cityName = cityName;
	}
	public String getCell_id() {
		return cell_id;
	}
	public void setCell_id(String cell_id) {
		this.cell_id = cell_id;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}
	@Override
	public String toString() {
		return "CpetStation [userLocation=" + userLocation + ", enodebId="
				+ enodebId + ", provinceName=" + provinceName
				+ ", stationName="
				+ stationName + ", countryName=" + countryName + ", townName="
				+ townName + ", countyName=" + countyName + ", cityName="
				+ cityName + ",cellName"+cellName+", cell_id=" + cell_id + ", status=" + status
				+ ", createTime=" + createTime + ", updateTime=" + updateTime
				+ "]";
	}
	public String getCellName() {
		return cellName;
	}
	public void setCellName(String cellName) {
		this.cellName = cellName;
	}
	
	
}
