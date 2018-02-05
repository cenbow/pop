package com.ai.bdx.pop.util;

import com.ai.bdx.pop.enums.PolicyStatus;
 

public class CepCONST {

	//前端与外部系统交互交换机
	public static final String EXCHANGE_DIRECT_CTRLDATA = "controldata.direct";
	//CEP监听队列名称
	public static final String ROUTE_EPL_EXTERNAL_REQ = "com.ailk.bdx.cep.epl_external_req";

	//MCD收取CEP反馈数据
	public static final String EXCHANGE_DIRECT_EVENTDATA = "eventdata.direct";
	//MCD接收CEP反馈数据路由前缀
	public static final String ROUTE_EPL_RES_PRFIFX = "com.ailk.bdx.cep.esper.result_";

	//获取需要监听的活动
	 
	public static final String QUERY_CEP_EVENT_INIT_SQL = " SELECT "+
														  "  a.policy_task_tab,b.id,b.current_task_id, c.cep_rule_id "+
														  "  FROM "+
														  "  POP_POLICY_BASEINFO a , "+
														  "  POP_POLICY_RULE b , "+
														  "  POP_POLICY_RULE_EVENT_CON c "+
														  "  WHERE "+
														  "      b.rule_status =  "+PolicyStatus.SENDODER_USER_SUCCESS.getValue()+
														  "  and  "+
														  "      a.id = b.policy_id "+
														  "  and  "+
														  "  	 c.policy_rule_id = b.id "+
														  "  and       "+
														  "      c.cep_rule_id is not null "+
														  "  and    "+
														  "      c.cep_rule_id != '' ";
			 
	public static final String QUERY_POP_RULE_TASK_SQL = "select id from {table} where exec_date = ? and rule_id = ? and exec_status = ?";
	public static final String QUERY_POP_RULE_TASK_SQL_ORACLE = "select id from {table} where exec_date = to_date('{today}','yyyy-mm-dd')  and rule_id = ?  and exec_status=?";
	
	
	
}
