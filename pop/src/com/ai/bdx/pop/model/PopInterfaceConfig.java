
package com.ai.bdx.pop.model;

import com.ai.bdx.pop.base.PopModel;

/** pop_interface_config
	id	varchar(32)	--接口编号
	interface_name	varchar(75)	--名称，描述
	interface_address	varchar(150)	--接口地址，URL
	interface_type	varchar(20)	--接口类型：http,webservice,socket。
	interface_class	varchar(150)	--接口Java类名，包括包名。
	interface_param	varchar(250)	--固定参数
*/
public class PopInterfaceConfig extends PopModel<PopInterfaceConfig> {
	private static final long serialVersionUID = 1428386390668L;

	public static PopInterfaceConfig dao(){
		return new PopInterfaceConfig();
	}
	public static final String COL_ID = "id";
	public static final String COL_INTERFACE_NAME = "interface_name";
	public static final String COL_INTERFACE_ADDRESS = "interface_address";
	public static final String COL_INTERFACE_TYPE = "interface_type";
	public static final String COL_INTERFACE_CLASS = "interface_class";
	public static final String COL_INTERFACE_PARAM = "interface_param";
}

