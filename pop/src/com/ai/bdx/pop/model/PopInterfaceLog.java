
package com.ai.bdx.pop.model;

import com.ai.bdx.pop.base.PopModel;

/** pop_interface_log
	id	varchar(32)	--主键
	interface_id	varchar(32)	--接口编号
	invoke_time	timestamp(19)	--调用时间
	invoke_type	varchar(20)	--调用类型：C-客户端；S-服务端
	invoke_param	varchar(500)	--接口参数
	invoke_result	varchar(500)	--接口调用结果，异常信息等
*/
public class PopInterfaceLog extends PopModel<PopInterfaceLog> {
	private static final long serialVersionUID = 1428386391224L;

	public static PopInterfaceLog dao(){
		return new PopInterfaceLog();
	}
	public static final String COL_ID = "id";
	public static final String COL_INTERFACE_ID = "interface_id";
	public static final String COL_INVOKE_TIME = "invoke_time";
	public static final String COL_INVOKE_TYPE = "invoke_type";
	public static final String COL_INVOKE_PARAM = "invoke_param";
	public static final String COL_INVOKE_RESULT = "invoke_result";
}

