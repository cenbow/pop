
package com.ai.bdx.pop.model;

import com.ai.bdx.pop.base.PopModel;

/** pop_policy_rule_act
	id	varchar(32)
	policy_rule_id	varchar(32)	--规则ID
	policy_act_type_id	smallint(5)	--策略动作类型ID
	avoid_custgroup_ids	varchar(250)	--免打扰客户类型ID
	exec_camp_content	varchar(2000)	--执行通知内容
	exec_channel_id	smallint(5)	--执行通知方式ID
	exec_camp_frequency	smallint(5)	--执行通知频次
	invalid_camp_content	varchar(2000)	--失效通知内容
	invalid_channel_id	smallint(5)	--失效通知方式ID
	invalid_camp_frequency	smallint(5)	--失效通知频次
	control_type_id	smallint(5)	--管控动作类型ID
	control_param	smallint(5)	--管控动作参数
*/
public class PopPolicyRuleAct extends PopModel<PopPolicyRuleAct> {
	private static final long serialVersionUID = 1428492044777L;

	public static PopPolicyRuleAct dao(){
		return new PopPolicyRuleAct();
	}
	public static final String COL_ID = "id";
	public static final String COL_POLICY_RULE_ID = "policy_rule_id";
	public static final String COL_POLICY_ACT_TYPE_ID = "policy_act_type_id";
	public static final String COL_AVOID_CUSTGROUP_IDS = "avoid_custgroup_ids";
	public static final String COL_EXEC_CAMP_CONTENT = "exec_camp_content";
	public static final String COL_EXEC_CHANNEL_ID = "exec_channel_id";
	public static final String COL_EXEC_CAMP_FREQUENCY = "exec_camp_frequency";
	public static final String COL_INVALID_CAMP_CONTENT = "invalid_camp_content";
	public static final String COL_INVALID_CHANNEL_ID = "invalid_channel_id";
	public static final String COL_INVALID_CAMP_FREQUENCY = "invalid_camp_frequency";
	public static final String COL_CONTROL_TYPE_ID = "control_type_id";
	public static final String COL_CONTROL_PARAM = "control_param";
}

