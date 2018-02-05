
package com.ai.bdx.pop.suite.model;

import com.ai.bdx.pop.base.PopModel;

/** user_group
	group_id	varchar(32)
	group_name	varchar(32)
	parent_id	varchar(32)
	status	int(10)
	create_time	timestamp(19)
	begin_date	timestamp(19)
	end_date	timestamp(19)
	user_limit	int(10)
	delete_time	varchar(64)
	sortnum	int(10)
*/
public class UserGroup extends PopModel<UserGroup> {
	private static final long serialVersionUID = 1471503389169L;

	public static UserGroup dao(){
		return new UserGroup();
	}
	public static final String COL_GROUP_ID = "group_id";
	public static final String COL_GROUP_NAME = "group_name";
	public static final String COL_PARENT_ID = "parent_id";
	public static final String COL_STATUS = "status";
	public static final String COL_CREATE_TIME = "create_time";
	public static final String COL_BEGIN_DATE = "begin_date";
	public static final String COL_END_DATE = "end_date";
	public static final String COL_USER_LIMIT = "user_limit";
	public static final String COL_DELETE_TIME = "delete_time";
	public static final String COL_SORTNUM = "sortnum";
}

