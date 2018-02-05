package com.ai.bdx.pop.wsclient.bean;

public class CpeLacCi {
	private String lac_ci_hex_code;
	private String lac_ci_dec_id;
	private String lac_hex_code;
	private String lac_dec_id;
	private String ci_hex_code;
	private String ci_dec_id;
	private String cell_name;
	private String station_code;
	private String station_name;
	private String country_name;
	private String town_name;
	private String county_id;
	private String city_id;
	private String user_location;
	public String getLac_ci_hex_code() {
		return lac_ci_hex_code;
	}
	public void setLac_ci_hex_code(String lac_ci_hex_code) {
		this.lac_ci_hex_code = lac_ci_hex_code;
	}
	public String getLac_ci_dec_id() {
		return lac_ci_dec_id;
	}
	public void setLac_ci_dec_id(String lac_ci_dec_id) {
		this.lac_ci_dec_id = lac_ci_dec_id;
	}
	public String getLac_hex_code() {
		return lac_hex_code;
	}
	public void setLac_hex_code(String lac_hex_code) {
		this.lac_hex_code = lac_hex_code;
	}
	public String getLac_dec_id() {
		return lac_dec_id;
	}
	public void setLac_dec_id(String lac_dec_id) {
		this.lac_dec_id = lac_dec_id;
	}
	public String getCi_hex_code() {
		return ci_hex_code;
	}
	public void setCi_hex_code(String ci_hex_code) {
		this.ci_hex_code = ci_hex_code;
	}
	public String getCi_dec_id() {
		return ci_dec_id;
	}
	public void setCi_dec_id(String ci_dec_id) {
		this.ci_dec_id = ci_dec_id;
	}
	public String getCell_name() {
		return cell_name;
	}
	public void setCell_name(String cell_name) {
		this.cell_name = cell_name;
	}
	public String getStation_code() {
		return station_code;
	}
	public void setStation_code(String station_code) {
		this.station_code = station_code;
	}
	public String getStation_name() {
		return station_name;
	}
	public void setStation_name(String station_name) {
		this.station_name = station_name;
	}
	public String getCountry_name() {
		return country_name;
	}
	public void setCountry_name(String country_name) {
		this.country_name = country_name;
	}
	public String getTown_name() {
		return town_name;
	}
	public void setTown_name(String town_name) {
		this.town_name = town_name;
	}
	public String getCounty_id() {
		return county_id;
	}
	public void setCounty_id(String county_id) {
		this.county_id = county_id;
	}
	public String getCity_id() {
		return city_id;
	}
	public void setCity_id(String city_id) {
		this.city_id = city_id;
	}
	public String getUser_location() {
		return user_location;
	}
	public void setUser_location(String user_location) {
		this.user_location = user_location;
	}
	@Override
	public String toString() {
		return "CpeLacCi [lac_ci_hex_code=" + lac_ci_hex_code
				+ ", lac_ci_dec_id=" + lac_ci_dec_id + ", lac_hex_code="
				+ lac_hex_code + ", lac_dec_id=" + lac_dec_id
				+ ", ci_hex_code=" + ci_hex_code + ", ci_dec_id=" + ci_dec_id
				+ ", cell_name=" + cell_name + ", station_code=" + station_code
				+ ", station_name=" + station_name + ", country_name="
				+ country_name + ", town_name=" + town_name + ", county_id="
				+ county_id + ", city_id=" + city_id + ", user_location="
				+ user_location + "]";
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((cell_name == null) ? 0 : cell_name.hashCode());
		result = prime * result
				+ ((ci_dec_id == null) ? 0 : ci_dec_id.hashCode());
		result = prime * result
				+ ((ci_hex_code == null) ? 0 : ci_hex_code.hashCode());
		result = prime * result + ((city_id == null) ? 0 : city_id.hashCode());
		result = prime * result
				+ ((country_name == null) ? 0 : country_name.hashCode());
		result = prime * result
				+ ((county_id == null) ? 0 : county_id.hashCode());
		result = prime * result
				+ ((lac_ci_dec_id == null) ? 0 : lac_ci_dec_id.hashCode());
		result = prime * result
				+ ((lac_ci_hex_code == null) ? 0 : lac_ci_hex_code.hashCode());
		result = prime * result
				+ ((lac_dec_id == null) ? 0 : lac_dec_id.hashCode());
		result = prime * result
				+ ((lac_hex_code == null) ? 0 : lac_hex_code.hashCode());
		result = prime * result
				+ ((station_code == null) ? 0 : station_code.hashCode());
		result = prime * result
				+ ((station_name == null) ? 0 : station_name.hashCode());
		result = prime * result
				+ ((town_name == null) ? 0 : town_name.hashCode());
		result = prime * result
				+ ((user_location == null) ? 0 : user_location.hashCode());
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
		CpeLacCi other = (CpeLacCi) obj;
		if (cell_name == null) {
			if (other.cell_name != null)
				return false;
		} else if (!cell_name.equals(other.cell_name))
			return false;
		if (ci_dec_id == null) {
			if (other.ci_dec_id != null)
				return false;
		} else if (!ci_dec_id.equals(other.ci_dec_id))
			return false;
		if (ci_hex_code == null) {
			if (other.ci_hex_code != null)
				return false;
		} else if (!ci_hex_code.equals(other.ci_hex_code))
			return false;
		if (city_id == null) {
			if (other.city_id != null)
				return false;
		} else if (!city_id.equals(other.city_id))
			return false;
		if (country_name == null) {
			if (other.country_name != null)
				return false;
		} else if (!country_name.equals(other.country_name))
			return false;
		if (county_id == null) {
			if (other.county_id != null)
				return false;
		} else if (!county_id.equals(other.county_id))
			return false;
		if (lac_ci_dec_id == null) {
			if (other.lac_ci_dec_id != null)
				return false;
		} else if (!lac_ci_dec_id.equals(other.lac_ci_dec_id))
			return false;
		if (lac_ci_hex_code == null) {
			if (other.lac_ci_hex_code != null)
				return false;
		} else if (!lac_ci_hex_code.equals(other.lac_ci_hex_code))
			return false;
		if (lac_dec_id == null) {
			if (other.lac_dec_id != null)
				return false;
		} else if (!lac_dec_id.equals(other.lac_dec_id))
			return false;
		if (lac_hex_code == null) {
			if (other.lac_hex_code != null)
				return false;
		} else if (!lac_hex_code.equals(other.lac_hex_code))
			return false;
		if (station_code == null) {
			if (other.station_code != null)
				return false;
		} else if (!station_code.equals(other.station_code))
			return false;
		if (station_name == null) {
			if (other.station_name != null)
				return false;
		} else if (!station_name.equals(other.station_name))
			return false;
		if (town_name == null) {
			if (other.town_name != null)
				return false;
		} else if (!town_name.equals(other.town_name))
			return false;
		if (user_location == null) {
			if (other.user_location != null)
				return false;
		} else if (!user_location.equals(other.user_location))
			return false;
		return true;
	}
	
	
}
