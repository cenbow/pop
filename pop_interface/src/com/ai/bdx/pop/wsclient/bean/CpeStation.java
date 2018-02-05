package com.ai.bdx.pop.wsclient.bean;

public class CpeStation {
	private String stationCode;     
	private String stationName;        
	private String countryName;        
	private String townName;            
	private String countyName;          
	private String cityName;            
	private String countyId;            
	private String cityId;
	
	public String getStationCode() {
		return stationCode;
	}
	public void setStationCode(String stationCode) {
		this.stationCode = stationCode;
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
	public String getCountyId() {
		return countyId;
	}
	public void setCountyId(String countyId) {
		this.countyId = countyId;
	}
	public String getCityId() {
		return cityId;
	}
	public void setCityId(String cityId) {
		this.cityId = cityId;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cityId == null) ? 0 : cityId.hashCode());
		result = prime * result
				+ ((cityName == null) ? 0 : cityName.hashCode());
		result = prime * result
				+ ((countryName == null) ? 0 : countryName.hashCode());
		result = prime * result
				+ ((countyId == null) ? 0 : countyId.hashCode());
		result = prime * result
				+ ((countyName == null) ? 0 : countyName.hashCode());
		result = prime * result
				+ ((stationCode == null) ? 0 : stationCode.hashCode());
		result = prime * result
				+ ((stationName == null) ? 0 : stationName.hashCode());
		result = prime * result
				+ ((townName == null) ? 0 : townName.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CpeStation other = (CpeStation) obj;
		if (cityId == null) {
			if (other.cityId != null)
				return false;
		} else if (!cityId.equals(other.cityId))
			return false;
		if (cityName == null) {
			if (other.cityName != null)
				return false;
		} else if (!cityName.equals(other.cityName))
			return false;
		if (countryName == null) {
			if (other.countryName != null)
				return false;
		} else if (!countryName.equals(other.countryName))
			return false;
		if (countyId == null) {
			if (other.countyId != null)
				return false;
		} else if (!countyId.equals(other.countyId))
			return false;
		if (countyName == null) {
			if (other.countyName != null)
				return false;
		} else if (!countyName.equals(other.countyName))
			return false;
		if (stationCode == null) {
			if (other.stationCode != null)
				return false;
		} else if (!stationCode.equals(other.stationCode))
			return false;
		if (stationName == null) {
			if (other.stationName != null)
				return false;
		} else if (!stationName.equals(other.stationName))
			return false;
		if (townName == null) {
			if (other.townName != null)
				return false;
		} else if (!townName.equals(other.townName))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "Cpestation [stationCode=" + stationCode + ", stationName="
				+ stationName + ", countryName=" + countryName + ", townName="
				+ townName + ", countyName=" + countyName + ", cityName="
				+ cityName + ", countyId=" + countyId + ", cityId=" + cityId
				+ "]";
	}
	
	
	
}
