package com.ai.bdx.pop.model.privilege;

import java.io.Serializable;

import com.asiainfo.biframe.privilege.ICity;
import com.asiainfo.biframe.privilege.cache.service.IdAndName;
import com.asiainfo.biframe.utils.config.Configure;

/**
 * TdEparchyCityMcd entity.
 * 用户地市对象列表
 *
 * @author MyEclipse Persistence Tools
 */

public class TdEparchyCityMcd implements Serializable, IdAndName, ICity {
	/**
	 *
	 */
	private static final long serialVersionUID = 207168765566022067L;

	// Fields
	private String areaCode;//安徽
	private String eparchyId;
	private String eparchyCode;
	private String eparchyName;
	private String rehandleFlag;
	private String freqFlag; //新家的字段
	private String orderId;
	private String remark;//安徽

	// Constructors

	/** default constructor */
	public TdEparchyCityMcd() {
	}

	/** minimal constructor */
	public TdEparchyCityMcd(String eparchyId, String eparchyCode) {
		this.eparchyId = eparchyId;
		this.eparchyCode = eparchyCode;
	}

	/** full constructor */
	public TdEparchyCityMcd(String areaCode, String eparchyId, String eparchyCode, String eparchyName,
			String rehandleFlag, String freqFlag, String orderId, String remark) {
		this.eparchyId = eparchyId;
		this.eparchyCode = eparchyCode;
		this.eparchyName = eparchyName;
		this.rehandleFlag = rehandleFlag;
		this.freqFlag = freqFlag;
		this.orderId = orderId;
		this.areaCode = areaCode;
		this.remark = remark;
	}

	// Property accessors
	public String getAreaCode() {
		return areaCode;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getEparchyId() {
		return eparchyId;
	}

	public void setEparchyId(String eparchyId) {
		this.eparchyId = eparchyId;
	}

	public String getEparchyCode() {
		return eparchyCode;
	}

	public void setEparchyCode(String eparchyCode) {
		this.eparchyCode = eparchyCode;
	}

	public String getEparchyName() {
		return eparchyName;
	}

	public void setEparchyName(String eparchyName) {
		this.eparchyName = eparchyName;
	}

	public String getRehandleFlag() {
		return rehandleFlag;
	}

	public void setRehandleFlag(String rehandleFlag) {
		this.rehandleFlag = rehandleFlag;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getFreqFlag() {
		return freqFlag;
	}

	public void setFreqFlag(String freqFlag) {
		this.freqFlag = freqFlag;
	}

	public String getName() {
		return getCityName();
	}

	public Object getPrimaryKey() {
		return getCityId();
	}

	public String getCityId() {
		if ("anhui".equalsIgnoreCase(Configure.getInstance().getProperty("PROVINCE"))) {
			return getEparchyCode();
		}
		return getEparchyId();
	}

	public String getCityName() {
		return getEparchyName();
	}

	public String getDmCityId() {
		return getEparchyCode();
	}

	public String getDmCountyId() {
		return null;
	}

	public String getDmDeptId() {
		return null;
	}

	public String getParentId() {
		return null;
	}

	public int getSortNum() {
		return Integer.parseInt(getOrderId());
	}

	@Override
	public String getDmTypeCode() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDmTypeId() {
		// TODO Auto-generated method stub
		return null;
	}

}