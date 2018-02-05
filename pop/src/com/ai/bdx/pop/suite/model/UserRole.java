
package com.ai.bdx.pop.suite.model;

import com.ai.bdx.pop.base.PopModel;

/** user_role
	role_id	varchar(32)
	role_name	varchar(128)
	parent_id	varchar(32)
	role_type	int(10)
	resourcetype	int(10)
	classify_id	varchar(32)
	status	int(10)
	create_time	timestamp(19)
	begin_date	timestamp(19)
	end_date	timestamp(19)
	user_limit	int(10)
	delete_time	varchar(64)
	create_group	varchar(32)
*/
public class UserRole extends PopModel<UserRole> {
	private static final long serialVersionUID = 1471503389307L;

	public static UserRole dao(){
		return new UserRole();
	}
	public static final String COL_ROLE_ID = "role_id";
	public static final String COL_ROLE_NAME = "role_name";
	public static final String COL_PARENT_ID = "parent_id";
	public static final String COL_ROLE_TYPE = "role_type";
	public static final String COL_RESOURCETYPE = "resourcetype";
	public static final String COL_CLASSIFY_ID = "classify_id";
	public static final String COL_STATUS = "status";
	public static final String COL_CREATE_TIME = "create_time";
	public static final String COL_BEGIN_DATE = "begin_date";
	public static final String COL_END_DATE = "end_date";
	public static final String COL_USER_LIMIT = "user_limit";
	public static final String COL_DELETE_TIME = "delete_time";
	public static final String COL_CREATE_GROUP = "create_group";
}

