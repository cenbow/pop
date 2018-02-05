package com.ai.bdx.pop.wsclient.bean;

public class CpeLacCiInfoDm {
	private String lac_ci_hex_code;
	private String lac_ci_dec_id;
	private String lac_hex_code;
	private String lac_dec_id;
	private String ci_hex_code;
	private String ci_dec_id;
	private String cell_name;//小区名称
	private String cgi;
	private String station_code;
	private String station_name;
	private String country_name;
	private String county_id;
	private String city_id;
	private String user_location;
	private String create_time;
	private String update_time;
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
	public String getCgi() {
		return cgi;
	}
	public void setCgi(String cgi) {
		this.cgi = cgi;
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
	public String getCreate_time() {
		return create_time;
	}
	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}
	public String getUpdate_time() {
		return update_time;
	}
	public void setUpdate_time(String update_time) {
		this.update_time = update_time;
	}
	@Override
	public String toString() {
		return "CpeLacCiInfoDm [lac_ci_hex_code=" + lac_ci_hex_code
				+ ", lac_ci_dec_id=" + lac_ci_dec_id + ", lac_hex_code="
				+ lac_hex_code + ", lac_dec_id=" + lac_dec_id
				+ ", ci_hex_code=" + ci_hex_code + ", ci_dec_id=" + ci_dec_id
				+ ", cell_name=" + cell_name + ", cgi=" + cgi
				+ ", station_code=" + station_code + ", station_name="
				+ station_name + ", country_name=" + country_name
				+ ", county_id=" + county_id + ", city_id=" + city_id
				+ ", user_location=" + user_location + ", create_time="
				+ create_time + ", update_time=" + update_time + "]";
	}
	
	
}
