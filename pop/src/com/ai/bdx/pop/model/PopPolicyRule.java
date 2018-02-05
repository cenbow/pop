
package com.ai.bdx.pop.model;

import com.ai.bdx.pop.base.PopModel;

/** pop_policy_rule
	id	varchar(32)	--规则ID
	policy_id	varchar(32)	--策略基本信息ID
	parent_id	varchar(32)	--父规则ID
	policy_rule_name	varchar(75)	--策略规则名称
	collision_detection_policy_id	varchar(32)	--冲突检测策略ID
	rule_priority	int(10)	--优先级，系统自动计算出数值
	create_user_id	varchar(32)	--创建人ID
	create_time	timestamp(19)	--创建时间
	pre_send_data_tab	varchar(32)
	send_data_tab	varchar(32)
	current_task_id	varchar(32)
	rule_status	smallint(5)
	pccid varchar(32)
*/
public class PopPolicyRule extends PopModel<PopPolicyRule> {
	private static final long serialVersionUID = 1428547372493L;

	public static PopPolicyRule dao(){
		return new PopPolicyRule();
	}
	public static final String COL_ID = "id";
	public static final String COL_POLICY_ID = "policy_id";
	public static final String COL_PARENT_ID = "parent_id";
	public static final String COL_POLICY_RULE_NAME = "policy_rule_name";
	public static final String COL_COLLISION_DETECTION_POLICY_ID = "collision_detection_policy_id";
	public static final String COL_RULE_PRIORITY = "rule_priority";
	public static final String COL_CREATE_USER_ID = "create_user_id";
	public static final String COL_CREATE_TIME = "create_time";
	public static final String COL_PRE_SEND_DATA_TAB = "pre_send_data_tab";
	public static final String COL_SEND_DATA_TAB = "send_data_tab";
	public static final String COL_CURRENT_TASK_ID = "current_task_id";
	public static final String COL_RULE_STATUS = "rule_status";
	public static final String COL_POLICY_DESC_ID = "policy_desc_id";
	public static final String COL_PCCID = "pccid";

}

