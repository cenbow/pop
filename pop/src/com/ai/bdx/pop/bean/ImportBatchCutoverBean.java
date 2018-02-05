package com.ai.bdx.pop.bean;
/**
 * 基站批量割接上传文件入库时的bean
 * @author 林
 *
 */
public class ImportBatchCutoverBean {
	private String lac_ci_hex_code_old;		//原lac_ci的十六进制
	private String lac_ci_dec_id_old;			//原lac_ci的十进制
	private String lac_hex_code_old;			//原lac的十六进制
	private String lac_dec_id_old;				//原lac的十进制
	private String ci_hex_code_old;				//原ci的十六进制
	private String ci_dec_id_old;					//原ci的十进制
	private String lac_ci_hex_code_new;	//新lac_ci的十六进制
	private String lac_ci_dec_id_new;			//新lac_ci的十进制
	private String lac_hex_code_new;			//新lac的十六进制
	private String lac_dec_id_new;				//新lac的十进制
	private String ci_hex_code_new;			//新ci的十六进制
	private String ci_dec_id_new;				//新ci的十进制
	private String cell_name_old;					//原小区名
	private String cell_name_new;				//新小区名
	private String enodeid_old;						//原基站编码
	private String enodeid_new;					//新基站编码
	private String enodeName_old;				//原基站名称
	private String enodeName_new;				//新基站名称
	private String chg_date;							//割接时间
	private String import_time;						//导入时间
	private String city_name;							//地区名称
	private String county_name;					//区县名称
	public String getLac_ci_hex_code_old() {
		return lac_ci_hex_code_old;
	}
	public void setLac_ci_hex_code_old(String lac_ci_hex_code_old) {
		this.lac_ci_hex_code_old = lac_ci_hex_code_old;
	}
	public String getLac_ci_dec_id_old() {
		return lac_ci_dec_id_old;
	}
	public void setLac_ci_dec_id_old(String lac_ci_dec_id_old) {
		this.lac_ci_dec_id_old = lac_ci_dec_id_old;
	}
	public String getLac_hex_code_old() {
		return lac_hex_code_old;
	}
	public void setLac_hex_code_old(String lac_hex_code_old) {
		this.lac_hex_code_old = lac_hex_code_old;
	}
	public String getLac_dec_id_old() {
		return lac_dec_id_old;
	}
	public void setLac_dec_id_old(String lac_dec_id_old) {
		this.lac_dec_id_old = lac_dec_id_old;
	}
	public String getCi_hex_code_old() {
		return ci_hex_code_old;
	}
	public void setCi_hex_code_old(String ci_hex_code_old) {
		this.ci_hex_code_old = ci_hex_code_old;
	}
	public String getCi_dec_id_old() {
		return ci_dec_id_old;
	}
	public void setCi_dec_id_old(String ci_dec_id_old) {
		this.ci_dec_id_old = ci_dec_id_old;
	}
	public String getLac_ci_hex_code_new() {
		return lac_ci_hex_code_new;
	}
	public void setLac_ci_hex_code_new(String lac_ci_hex_code_new) {
		this.lac_ci_hex_code_new = lac_ci_hex_code_new;
	}
	public String getLac_ci_dec_id_new() {
		return lac_ci_dec_id_new;
	}
	public void setLac_ci_dec_id_new(String lac_ci_dec_id_new) {
		this.lac_ci_dec_id_new = lac_ci_dec_id_new;
	}
	public String getLac_hex_code_new() {
		return lac_hex_code_new;
	}
	public void setLac_hex_code_new(String lac_hex_code_new) {
		this.lac_hex_code_new = lac_hex_code_new;
	}
	public String getLac_dec_id_new() {
		return lac_dec_id_new;
	}
	public void setLac_dec_id_new(String lac_dec_id_new) {
		this.lac_dec_id_new = lac_dec_id_new;
	}
	public String getCi_hex_code_new() {
		return ci_hex_code_new;
	}
	public void setCi_hex_code_new(String ci_hex_code_new) {
		this.ci_hex_code_new = ci_hex_code_new;
	}
	public String getCi_dec_id_new() {
		return ci_dec_id_new;
	}
	public void setCi_dec_id_new(String ci_dec_id_new) {
		this.ci_dec_id_new = ci_dec_id_new;
	}
	public String getCell_name_old() {
		return cell_name_old;
	}
	public void setCell_name_old(String cell_name_old) {
		this.cell_name_old = cell_name_old;
	}
	public String getCell_name_new() {
		return cell_name_new;
	}
	public void setCell_name_new(String cell_name_new) {
		this.cell_name_new = cell_name_new;
	}
	public String getEnodeid_old() {
		return enodeid_old;
	}
	public void setEnodeid_old(String enodeid_old) {
		this.enodeid_old = enodeid_old;
	}
	public String getEnodeid_new() {
		return enodeid_new;
	}
	public void setEnodeid_new(String enodeid_new) {
		this.enodeid_new = enodeid_new;
	}
	public String getChg_date() {
		return chg_date;
	}
	public void setChg_date(String chg_date) {
		this.chg_date = chg_date;
	}
	public String getImport_time() {
		return import_time;
	}
	public void setImport_time(String import_time) {
		this.import_time = import_time;
	}
	public String getCity_name() {
		return city_name;
	}
	public void setCity_name(String city_name) {
		this.city_name = city_name;
	}
	public String getCounty_name() {
		return county_name;
	}
	public void setCounty_name(String county_name) {
		this.county_name = county_name;
	}
	
	public String getEnodeName_old() {
		return enodeName_old;
	}
	public void setEnodeName_old(String enodeName_old) {
		this.enodeName_old = enodeName_old;
	}
	public String getEnodeName_new() {
		return enodeName_new;
	}
	public void setEnodeName_new(String enodeName_new) {
		this.enodeName_new = enodeName_new;
	}
	@Override
	public String toString() {
		return "ImportBatchCutoverBean [lac_ci_hex_code_old="
				+ lac_ci_hex_code_old + ", lac_ci_dec_id_old="
				+ lac_ci_dec_id_old + ", lac_hex_code_old=" + lac_hex_code_old
				+ ", lac_dec_id_old=" + lac_dec_id_old + ", ci_hex_code_old="
				+ ci_hex_code_old + ", ci_dec_id_old=" + ci_dec_id_old
				+ ", lac_ci_hex_code_new=" + lac_ci_hex_code_new
				+ ", lac_ci_dec_id_new=" + lac_ci_dec_id_new
				+ ", lac_hex_code_new=" + lac_hex_code_new
				+ ", lac_dec_id_new=" + lac_dec_id_new + ", ci_hex_code_new="
				+ ci_hex_code_new + ", ci_dec_id_new=" + ci_dec_id_new
				+ ", cell_name_old=" + cell_name_old + ", cell_name_new="
				+ cell_name_new + ", enodeid_old=" + enodeid_old
				+ ", enodeid_new=" + enodeid_new + ", enodeName_old="
				+ enodeName_old + ", enodeName_new=" + enodeName_new
				+ ", chg_date=" + chg_date + ", import_time=" + import_time
				+ ", city_name=" + city_name + ", county_name=" + county_name
				+ "]";
	}
	
}
