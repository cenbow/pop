
package com.ai.bdx.pop.model;

import com.ai.bdx.pop.base.PopModel;

/** pop_policy_rule_event_con
	id	varchar(32)
	policy_rule_id	varchar(32)	--规则ID
	cep_rule_id	varchar(32)	--复杂事件规则ID
	cep_rule_desc	text(65535)	--复杂事件规则中文描述
	simple_condtions_data	varchar(1000)	--简单规则数据，json结构，如:{"net_type":"网络类型ID","terminal_type":"终端类型","terminal_brand":"终端品牌","terminal_model":"终端型号","area_type_id":"区域类型","busi_type":"业务大类","busi_sub_type":"业务小类"}
	simple_condtions_desc	varchar(1000)	--简单规则中文描述
	cep_choosedlacci_desc text(65535)--cep基站信息中文描述
	cep_lacciAndStationTypeJsons  text(65535)--cep基站信息 json信息
*/
public class PopPolicyRuleEventCon extends PopModel<PopPolicyRuleEventCon> {
	private static final long serialVersionUID = 1428492044654L;

	public static PopPolicyRuleEventCon dao(){
		return new PopPolicyRuleEventCon();
	}
	public static final String COL_ID = "id";
	public static final String COL_POLICY_RULE_ID = "policy_rule_id";
	public static final String COL_CEP_RULE_ID = "cep_rule_id";
	public static final String COL_CEP_RULE_DESC = "cep_rule_desc";
	public static final String COL_SIMPLE_CONDTIONS_DATA = "simple_condtions_data";
	public static final String COL_SIMPLE_CONDTIONS_DESC = "simple_condtions_desc";
	/**
	 * add by jinl 20160418 存放选择的基站信息 "cep基站信息中文描述"
	 */
	public static final String COL_CEP_CHOOSEDLACCI_DESC = "cep_choosedlacci_desc";
	/**
	 * add by jinl 20160418 存放选择的基站信息 返回的JSON信息
	 */
	public static final String COL_CEP_LACCIANDSTATIONTYPEJSONS = "cep_lacciAndStationTypeJsons";
}

