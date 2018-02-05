
package com.ai.bdx.pop.model;

import com.ai.bdx.pop.base.PopModel;

/** pop_policy_rule_exec_time
	id	varchar(32)
	policy_rule_id	varchar(32)	--规则ID
	date_ranges	varchar(250)	--日期范围，多个用逗号分隔。
	time_ranges	varchar(250)	--执行时段，如：08:30-10:30,11:00-13:30，多个用逗号分隔。
	avoid_ranges	varchar(250)	--屏蔽时段范围，如：08:30-10:30,11:00-13:30，多个用逗号分隔。
*/
public class PopPolicyRuleExecTime extends PopModel<PopPolicyRuleExecTime> {
	private static final long serialVersionUID = 1428492044514L;

	public static PopPolicyRuleExecTime dao(){
		return new PopPolicyRuleExecTime();
	}
	public static final String COL_ID = "id";
	public static final String COL_POLICY_RULE_ID = "policy_rule_id";
	public static final String COL_DATE_RANGES = "date_ranges";
	public static final String COL_TIME_RANGES = "time_ranges";
	public static final String COL_AVOID_RANGES = "avoid_ranges";
}

