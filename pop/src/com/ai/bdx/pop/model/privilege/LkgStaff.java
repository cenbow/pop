package com.ai.bdx.pop.model.privilege;

import java.util.List;

import com.asiainfo.biframe.privilege.IUser;
import com.asiainfo.biframe.privilege.cache.service.IdAndName;

/**
 * LkgStaff entity.
 * 用户衄1�7
 *
 * @author MyEclipse Persistence Tools
 */

public class LkgStaff implements java.io.Serializable, IdAndName, IUser {
	/**
	 *
	 */
	private static final long serialVersionUID = -4287913024688962118L;
	// Fields
	private String staffId;
	private String staffName;
	private String staffPwd;
	private String sex;
	private String age;
	private String nation;
	private String staffNum;
	private String certificateId;
	private String provId;
	private String cityId;
	private String countryId;
	private String areaId;
	private String sub1AreaId;
	private String sub2AreaId;
	private String dataLevel;
	private String depId;
	private String postId;
	private String phone;
	private String email;
	private String commAddress;
	private String postCode;
	private String leaderId;
	private String groupId;
	private String postType;
	private String remark;
	private String adminId;
	private String adminName;
	private String adminTime;
	private String pwdTime;
	private String headPic;
	private String getpwdType;
	private String staffTactLevel;
	private String loginTag;

	// Constructors
	/** default constructor */
	public LkgStaff() {
	}

	/** minimal constructor */
	public LkgStaff(String staffId) {
		this.staffId = staffId;
	}

	/** full constructor */
	public LkgStaff(String staffId, String staffName, String staffPwd, String sex, String age, String nation,
			String staffNum, String certificateId, String provId, String cityId, String countryId, String areaId,
			String sub1AreaId, String sub2AreaId, String dataLevel, String depId, String postId, String phone,
			String email, String commAddress, String postCode, String leaderId, String groupId, String postType,
			String remark, String adminId, String adminName, String adminTime, String pwdTime, String headPic,
			String getpwdType, String staffTactLevel, String loginTag) {
		this.staffId = staffId;
		this.staffName = staffName;
		this.staffPwd = staffPwd;
		this.sex = sex;
		this.age = age;
		this.nation = nation;
		this.staffNum = staffNum;
		this.certificateId = certificateId;
		this.provId = provId;
		this.cityId = cityId;
		this.countryId = countryId;
		this.areaId = areaId;
		this.sub1AreaId = sub1AreaId;
		this.sub2AreaId = sub2AreaId;
		this.dataLevel = dataLevel;
		this.depId = depId;
		this.postId = postId;
		this.phone = phone;
		this.email = email;
		this.commAddress = commAddress;
		this.postCode = postCode;
		this.leaderId = leaderId;
		this.groupId = groupId;
		this.postType = postType;
		this.remark = remark;
		this.adminId = adminId;
		this.adminName = adminName;
		this.adminTime = adminTime;
		this.pwdTime = pwdTime;
		this.headPic = headPic;
		this.getpwdType = getpwdType;
		this.staffTactLevel = staffTactLevel;
		this.loginTag = loginTag;
	}

	// Property accessors

	public String getStaffId() {
		return staffId;
	}

	public void setStaffId(String staffId) {
		this.staffId = staffId;
	}

	public String getStaffName() {
		return staffName;
	}

	public void setStaffName(String staffName) {
		this.staffName = staffName;
	}

	public String getStaffPwd() {
		return staffPwd;
	}

	public void setStaffPwd(String staffPwd) {
		this.staffPwd = staffPwd;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}

	public String getNation() {
		return nation;
	}

	public void setNation(String nation) {
		this.nation = nation;
	}

	public String getStaffNum() {
		return staffNum;
	}

	public void setStaffNum(String staffNum) {
		this.staffNum = staffNum;
	}

	public String getCertificateId() {
		return certificateId;
	}

	public void setCertificateId(String certificateId) {
		this.certificateId = certificateId;
	}

	public String getProvId() {
		return provId;
	}

	public void setProvId(String provId) {
		this.provId = provId;
	}

	public String getCityId() {
		return cityId;
	}

	public void setCityId(String cityId) {
		this.cityId = cityId;
	}

	public String getCountryId() {
		return countryId;
	}

	public void setCountryId(String countryId) {
		this.countryId = countryId;
	}

	public String getAreaId() {
		return areaId;
	}

	public void setAreaId(String areaId) {
		this.areaId = areaId;
	}

	public String getSub1AreaId() {
		return sub1AreaId;
	}

	public void setSub1AreaId(String sub1AreaId) {
		this.sub1AreaId = sub1AreaId;
	}

	public String getSub2AreaId() {
		return sub2AreaId;
	}

	public void setSub2AreaId(String sub2AreaId) {
		this.sub2AreaId = sub2AreaId;
	}

	public String getDataLevel() {
		return dataLevel;
	}

	public void setDataLevel(String dataLevel) {
		this.dataLevel = dataLevel;
	}

	public String getDepId() {
		return depId;
	}

	public void setDepId(String depId) {
		this.depId = depId;
	}

	public String getPostId() {
		return postId;
	}

	public void setPostId(String postId) {
		this.postId = postId;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getCommAddress() {
		return commAddress;
	}

	public void setCommAddress(String commAddress) {
		this.commAddress = commAddress;
	}

	public String getPostCode() {
		return postCode;
	}

	public void setPostCode(String postCode) {
		this.postCode = postCode;
	}

	public String getLeaderId() {
		return leaderId;
	}

	public void setLeaderId(String leaderId) {
		this.leaderId = leaderId;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getPostType() {
		return postType;
	}

	public void setPostType(String postType) {
		this.postType = postType;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getAdminId() {
		return adminId;
	}

	public void setAdminId(String adminId) {
		this.adminId = adminId;
	}

	public String getAdminName() {
		return adminName;
	}

	public void setAdminName(String adminName) {
		this.adminName = adminName;
	}

	public String getAdminTime() {
		return adminTime;
	}

	public void setAdminTime(String adminTime) {
		this.adminTime = adminTime;
	}

	public String getPwdTime() {
		return pwdTime;
	}

	public void setPwdTime(String pwdTime) {
		this.pwdTime = pwdTime;
	}

	public String getHeadPic() {
		return headPic;
	}

	public void setHeadPic(String headPic) {
		this.headPic = headPic;
	}

	public String getGetpwdType() {
		return getpwdType;
	}

	public void setGetpwdType(String getpwdType) {
		this.getpwdType = getpwdType;
	}

	public String getStaffTactLevel() {
		return staffTactLevel;
	}

	public void setStaffTactLevel(String staffTactLevel) {
		this.staffTactLevel = staffTactLevel;
	}

	public String getLoginTag() {
		return loginTag;
	}

	public void setLoginTag(String loginTag) {
		this.loginTag = loginTag;
	}

	public String getName() {
		return getStaffName();
	}

	public Object getPrimaryKey() {
		return getStaffId();
	}

	public String getCityid() {
		return getCityId();
	}

	public int getDepartmentid() {
		return (getDepId() == null || "".equals(getDepId())) ? -1 : Integer.valueOf(getDepId());
	}

	public String getLoginId() {
		return getStaffId();
	}

	public String getMobilePhone() {
		return getPhone();
	}

	public String getUserid() {
		return getStaffId();
	}

	public String getUsername() {
		return getStaffName();
	}

	@Override
	public String getDomainType() {
		// TODO Auto-generated method stub
		return null;
	}

	public Integer getDutyid() {
		// TODO Auto-generated method stub
		return null;
	}

	public List<String> getRoleidList() {
		// TODO Auto-generated method stub
		return null;
	}

}