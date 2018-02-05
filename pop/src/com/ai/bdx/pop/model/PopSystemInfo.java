package com.ai.bdx.pop.model;

import com.ai.bdx.pop.base.PopModel;

/** pop_system_info
	id	varchar(32)	--主键
	info_type	smallint(5)	--信息类型，0-系统公告；1—审批&确认通知；2-系统告警信息
	user_id	varchar(20)	--信息接收人ID
	content	varchar(2500)	--信息内容
	create_time	timestamp(19)	--生成时间
	status	smallint(5)	--状态，0-未处理，1-已处理
*/
public class PopSystemInfo extends PopModel<PopSystemInfo> {
	private static final long serialVersionUID = 1428738843749L;

	public static PopSystemInfo dao() {
		return new PopSystemInfo();
	}

	public static final String COL_ID = "id";
	public static final String COL_INFO_TYPE = "info_type";
	public static final String COL_USER_ID = "user_id";
	public static final String COL_CONTENT = "content";
	public static final String COL_CREATE_TIME = "create_time";
	public static final String COL_STATUS = "status";

}
