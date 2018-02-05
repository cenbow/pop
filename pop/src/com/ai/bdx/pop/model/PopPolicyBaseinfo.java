
package com.ai.bdx.pop.model;

import com.ai.bdx.pop.base.PopModel;

/** pop_policy_baseinfo
	id	varchar(32)	--策略ID
	policy_name	varchar(75)	--策略名称
	policy_desc	varchar(500)	--策略描述
	start_time	timestamp(19)	--生效时间
	end_time	timestamp(19)	--失效时间
	policy_type_id	varchar(10)	--策略类型ID
	policy_level_id	smallint(5)	--策略等级ID
	template_flag	smallint(5)	--策略模板标识:0不是模板;1私有模板;2公有模板,默认0.
	policy_status_id	smallint(5)	--策略状态ID
	create_user_id	varchar(32)	--创建人ID
	create_time	timestamp(19)	--创建时间
	policy_task_tab	varchar(32)
	city_id	varchar(32)
	custom_service_diameter varchar(1000)	--客服口径
	business_priority int(10) --业务优先级
	city_priority varchar(10) --地市优先级
*/
public class PopPolicyBaseinfo extends PopModel<PopPolicyBaseinfo> {
	private static final long serialVersionUID = 1429169706225L;

	public static PopPolicyBaseinfo dao(){
		return new PopPolicyBaseinfo();
	}
	public static final String COL_ID = "id";
	public static final String COL_POLICY_NAME = "policy_name";
	public static final String COL_POLICY_DESC = "policy_desc";
	public static final String COL_START_TIME = "start_time";
	public static final String COL_END_TIME = "end_time";
	public static final String COL_POLICY_TYPE_ID = "policy_type_id";
	public static final String COL_POLICY_LEVEL_ID = "policy_level_id";
	public static final String COL_TEMPLATE_FLAG = "template_flag";
	public static final String COL_POLICY_STATUS_ID = "policy_status_id";
	public static final String COL_CREATE_USER_ID = "create_user_id";
	public static final String COL_CREATE_TIME = "create_time";
	public static final String COL_POLICY_TASK_TAB = "policy_task_tab";
	public static final String COL_CITY_ID = "city_id";
	public static final String COL_CUSTOM_SERVICE_DIAMETER = "custom_service_diameter";
	public static final String COL_BUSINESS_PRIORITY = "business_priority";
	public static final String COL_CITY_PRIORITY = "city_priority";
}

