/**   
 * @Title: Prod.java
 * @Package com.ai.bdx.pop.bean
 * @Description: TODO(用一句话描述该文件做什么)
 * @author A18ccms A18ccms_gmail_com   
 * @date 2015-8-11 上午11:44:46
 * @version V1.0   
 */
package com.ai.bdx.pop.bean;

/**
 * @ClassName: Prod
 * @Description: pop 和 boss接口
 * @author jinlong
 * @date 2015-8-11 上午11:44:46
 * 
 */
public class Prod {
	
	private String prodid;//产品编码
	private String mobileno;//手机号码
	private String eff_time;//生效时间
	private String exp_time;//失效时间
	private String region;//地区
	private String channel;//渠道
	private String optype;//操作类型
	public String getProdid() {
		return prodid;
	}
	public void setProdid(String prodid) {
		this.prodid = prodid;
	}
	public String getMobileno() {
		return mobileno;
	}
	public void setMobileno(String mobileno) {
		this.mobileno = mobileno;
	}
	public String getEff_time() {
		return eff_time;
	}
	public void setEff_time(String eff_time) {
		this.eff_time = eff_time;
	}
	public String getExp_time() {
		return exp_time;
	}
	public void setExp_time(String exp_time) {
		this.exp_time = exp_time;
	}
	public String getRegion() {
		return region;
	}
	public void setRegion(String region) {
		this.region = region;
	}
	public String getChannel() {
		return channel;
	}
	public void setChannel(String channel) {
		this.channel = channel;
	}
	public String getOptype() {
		return optype;
	}
	public void setOptype(String optype) {
		this.optype = optype;
	}
	
	
	
	

}
