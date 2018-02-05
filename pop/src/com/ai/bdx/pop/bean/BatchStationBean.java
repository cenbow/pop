package com.ai.bdx.pop.bean;

import java.util.Date;

public class BatchStationBean {
	private String subsid;
	private String productNo;
	private String userlocationOld;
	private String userlocationNew;
	private int resetFlag;
	private Date resetTime;
	private String cutoverTime;
	private String importTime;
	private String lacOld;
	private String lacNew;
	private String ciOld;
	private String ciNew;
	private String lac_ci_hex_code_old;
	private String lac_ci_hex_code_new;
	public String getSubsid() {
		return subsid;
	}
	public void setSubsid(String subsid) {
		this.subsid = subsid;
	}
	public String getProductNo() {
		return productNo;
	}
	public void setProductNo(String productNo) {
		this.productNo = productNo;
	}
	public String getUserlocationOld() {
		return userlocationOld;
	}
	public void setUserlocationOld(String userlocationOld) {
		this.userlocationOld = userlocationOld;
	}
	public String getUserlocationNew() {
		return userlocationNew;
	}
	public void setUserlocationNew(String userlocationNew) {
		this.userlocationNew = userlocationNew;
	}
	public int getResetFlag() {
		return resetFlag;
	}
	public void setResetFlag(int resetFlag) {
		this.resetFlag = resetFlag;
	}
	public Date getResetTime() {
		return resetTime;
	}
	public void setResetTime(Date resetTime) {
		this.resetTime = resetTime;
	}
	public String getCutoverTime() {
		return cutoverTime;
	}
	public void setCutoverTime(String cutoverTime) {
		this.cutoverTime = cutoverTime;
	}
	public String getImportTime() {
		return importTime;
	}
	public void setImportTime(String importTime) {
		this.importTime = importTime;
	}
	@Override
	public String toString() {
		return "BatchStationBean [subsid=" + subsid + ", productNo="
				+ productNo + ", userlocationOld=" + userlocationOld
				+ ", userlocationNew=" + userlocationNew + ", resetFlag="
				+ resetFlag + ", resetTime=" + resetTime + ", cutoverTime="
				+ cutoverTime + ", importTime=" + importTime + "]";
	}
	public String getLac_ci_hex_code_old() {
		return lac_ci_hex_code_old;
	}
	public void setLac_ci_hex_code_old(String lac_ci_hex_code_old) {
		this.lac_ci_hex_code_old = lac_ci_hex_code_old;
	}
	public String getLac_ci_hex_code_new() {
		return lac_ci_hex_code_new;
	}
	public void setLac_ci_hex_code_new(String lac_ci_hex_code_new) {
		this.lac_ci_hex_code_new = lac_ci_hex_code_new;
	}
	
	
	
}
