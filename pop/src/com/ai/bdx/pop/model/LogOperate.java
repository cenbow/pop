
package com.ai.bdx.pop.model;

import com.ai.bdx.pop.base.PopModel;

/** log_operate
	id	varchar(32)
	name	varchar(255)
	desc	varchar(4096)
	create_time	datetime(19)
	create_user	varchar(255)
	remark	varchar(4096)
*/
public class LogOperate extends PopModel<LogOperate> {
	private static final long serialVersionUID = 1471829269045L;

	public static LogOperate dao(){
		return new LogOperate();
	}
	public static final String COL_ID = "id";
	public static final String COL_NAME = "name";
	public static final String COL_DESC = "desc";
	public static final String COL_CREATE_TIME = "create_time";
	public static final String COL_CREATE_USER = "create_user";
	public static final String COL_REMARK = "remark";
}

