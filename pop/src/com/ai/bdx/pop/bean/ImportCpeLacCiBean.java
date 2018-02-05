package com.ai.bdx.pop.bean;

public class ImportCpeLacCiBean {
	private String lac_ci_hex_code;		//lac_ci的十六进制
	private String lac_ci_dec_id;			//lac_ci的十进制
	private String lac_hex_code;			//lac的十六进制
	private String lac_dec_id;				//lac的十进制
	private String ci_hex_code;				//ci的十六进制
	private String ci_dec_id;					//ci的十进制
	private String station_code;				//小区名
	private String enodeid;						//基站编码
	private String user_location;			//userlocation
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
	public String getStation_code() {
		return station_code;
	}
	public void setStation_code(String station_code) {
		this.station_code = station_code;
	}
	public String getEnodeid() {
		return enodeid;
	}
	public void setEnodeid(String enodeid) {
		this.enodeid = enodeid;
	}
	public String getUser_location() {
		return user_location;
	}
	public void setUser_location(String user_location) {
		this.user_location = user_location;
	}
	@Override
	public String toString() {
		return "ImportCpeLacCiBean [lac_ci_hex_code=" + lac_ci_hex_code
				+ ", lac_ci_dec_id=" + lac_ci_dec_id + ", lac_hex_code="
				+ lac_hex_code + ", lac_dec_id=" + lac_dec_id
				+ ", ci_hex_code=" + ci_hex_code + ", ci_dec_id=" + ci_dec_id
				+ ", station_code=" + station_code + ", enodeid=" + enodeid
				+ ", user_location=" + user_location + "]";
	}
}
