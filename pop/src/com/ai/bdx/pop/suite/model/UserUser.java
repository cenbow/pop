
package com.ai.bdx.pop.suite.model;

import com.ai.bdx.pop.base.PopModel;


/** user_user
	userid	varchar(32)
	cityid	varchar(30)
	actual_cityid	varchar(10)
	departmentid	int(10)
	username	varchar(32)
	pwd	varchar(64)
	status	int(10)
	begindate	timestamp(19)
	enddate	timestamp(19)
	createtime	timestamp(19)
	birthday	date(10)
	mobilephone	varchar(24)
	mobilephone1	varchar(24)
	mobilephone2	varchar(24)
	homephone	varchar(24)
	officephone	varchar(24)
	officefax	varchar(24)
	email	varchar(64)
	email1	varchar(64)
	email2	varchar(64)
	notes	varchar(255)
	dutyid	int(10)
	sex	varchar(8)
	age	varchar(4)
	address	varchar(255)
	postalcode	varchar(24)
	nation	varchar(32)
	passportno	varchar(24)
	director	varchar(255)
	delete_time	varchar(64)
	sensitive_data_level	varchar(16)
	domain_type	varchar(2)
*/
public class UserUser extends PopModel<UserUser> {
	private static final long serialVersionUID = 1471417239498L;

	public static UserUser dao(){
		return new UserUser();
	}
	public static final String COL_USERID = "userid";
	public static final String COL_CITYID = "cityid";
	public static final String COL_ACTUAL_CITYID = "actual_cityid";
	public static final String COL_DEPARTMENTID = "departmentid";
	public static final String COL_USERNAME = "username";
	public static final String COL_PWD = "pwd";
	public static final String COL_STATUS = "status";
	public static final String COL_BEGINDATE = "begindate";
	public static final String COL_ENDDATE = "enddate";
	public static final String COL_CREATETIME = "createtime";
	public static final String COL_BIRTHDAY = "birthday";
	public static final String COL_MOBILEPHONE = "mobilephone";
	public static final String COL_MOBILEPHONE1 = "mobilephone1";
	public static final String COL_MOBILEPHONE2 = "mobilephone2";
	public static final String COL_HOMEPHONE = "homephone";
	public static final String COL_OFFICEPHONE = "officephone";
	public static final String COL_OFFICEFAX = "officefax";
	public static final String COL_EMAIL = "email";
	public static final String COL_EMAIL1 = "email1";
	public static final String COL_EMAIL2 = "email2";
	public static final String COL_NOTES = "notes";
	public static final String COL_DUTYID = "dutyid";
	public static final String COL_SEX = "sex";
	public static final String COL_AGE = "age";
	public static final String COL_ADDRESS = "address";
	public static final String COL_POSTALCODE = "postalcode";
	public static final String COL_NATION = "nation";
	public static final String COL_PASSPORTNO = "passportno";
	public static final String COL_DIRECTOR = "director";
	public static final String COL_DELETE_TIME = "delete_time";
	public static final String COL_SENSITIVE_DATA_LEVEL = "sensitive_data_level";
	public static final String COL_DOMAIN_TYPE = "domain_type";
}

