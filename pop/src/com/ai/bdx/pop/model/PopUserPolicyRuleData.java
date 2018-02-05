
package com.ai.bdx.pop.model;

import com.ai.bdx.pop.base.PopModel;

/** pop_user_policy_rule_data
	product_no	varchar(11)	--手机号码
	rule_id	varchar(32)	--策略编码（策略规则ID）
	policy_rule_name	varchar(75)	--策略名称
	policy_type_id	smallint(5)	--策略类型ID
	policy_level_id	smallint(5)	--策略等级ID
	act_type_id	smallint(5)	--动作类型ID
	policy_rule_content	text(65535)	--策略规则内容
	create_time	timestamp(19)	--创建时间
	policy_sign_time	timestamp(19)	--策略签约时间
	policy_sign_status	smallint(5)	--策略签约状态
*/
public class PopUserPolicyRuleData extends PopModel<PopUserPolicyRuleData> {
	private static final long serialVersionUID = 1429707440698L;

	public static PopUserPolicyRuleData dao(){
		return new PopUserPolicyRuleData();
	}
	public static final String COL_PRODUCT_NO = "product_no";
	public static final String COL_RULE_ID = "rule_id";
	public static final String COL_POLICY_RULE_NAME = "policy_rule_name";
	public static final String COL_POLICY_TYPE_ID = "policy_type_id";
	public static final String COL_POLICY_LEVEL_ID = "policy_level_id";
	public static final String COL_ACT_TYPE_ID = "act_type_id";
	public static final String COL_POLICY_RULE_CONTENT = "policy_rule_content";
	public static final String COL_CREATE_TIME = "create_time";
	public static final String COL_POLICY_SIGN_TIME = "policy_sign_time";
	public static final String COL_POLICY_SIGN_STATUS = "policy_sign_status";
}

