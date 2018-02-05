
package com.ai.bdx.pop.model;

import com.ai.bdx.pop.base.PopModel;

/** pop_policy_rule_custgroup
	id	varchar(32)
	policy_rule_id	varchar(32)	--规则ID
	custgroup_id	varbinary(32)	--客户群ID
	custgroup_name	varchar(75)	--客户群名称
	custgroup_type	varchar(32)	--客户群类型：N非周期；M月周期；D日周期
	custgroup_number	int(10)	--客户群规模
*/
public class PopPolicyRuleCustgroup extends PopModel<PopPolicyRuleCustgroup> {
	private static final long serialVersionUID = 1428492044379L;

	public static PopPolicyRuleCustgroup dao(){
		return new PopPolicyRuleCustgroup();
	}
	public static final String COL_ID = "id";
	public static final String COL_POLICY_RULE_ID = "policy_rule_id";
	public static final String COL_CUSTGROUP_ID = "custgroup_id";
	public static final String COL_CUSTGROUP_NAME = "custgroup_name";
	public static final String COL_CUSTGROUP_TYPE = "custgroup_type";
	public static final String COL_CUSTGROUP_NUMBER = "custgroup_number";
}

