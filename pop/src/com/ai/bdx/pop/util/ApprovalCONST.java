package com.ai.bdx.pop.util;

import java.util.HashMap;

/**
 * 审批常量类
 * 
 * @author Administrator
 *
 */
public class ApprovalCONST {

	private static HashMap<String,String> flowTypeMap=new HashMap<String,String>();//类型
	private static HashMap<String,String> approveFlagMap=new HashMap<String,String>();//审批状态
	
	public static void main(String args[]){
		System.out.print(getFlowTypeDesc("1"));
	}
	
	
	
	public static String getApproveFlagDesc(String flowType,String approveFlag){
		initMap();
		return	approveFlagMap.get(approveFlag).replace("{#}", flowTypeMap.get(flowType));
	}
	public static String getFlowTypeDesc(String flowType){
		initMap();
		return flowTypeMap.get(flowType);
	}
	public static final String FLOW_TYPE_CONFIRM = "2";// 确认流程标示
	public static final String FLOW_TYPE_APPROVL = "1";// 审批流程标示
	
	public static final String APPROVE_FLAG_FALSE ="0";
	public static final String APPROVE_FLAG_TRUE ="1";
	
	// 审批流程-通过邮件审批 mtl_approve_flow_def表的approve_flow_id字段
	public static final String APPROVE_FLOW_ID_BY_OA = "-1";

	public static final String APPROVE_SERVICE = "approvalService";

	//1 ：同级审批人还有未审批的 2：同级审批人全部审批过,还有下级审批人，把令牌传递下级审批 3：当前流程结束即不存在下级审批人了
	public static final String APPROVE_LEVEL_HAS = "1";
	public static final String APPROVE_LEVEL_NEXTHAS = "2";
	public static final String APPROVE_LEVEL_OVER = "3";
	
	// 审批人类�?策划人所属部门的审批�?
	public static final short MPM_APPROVE_OBJ_TYPE_SELF_DEPT = 1;
	// 审批人 指定部门的审批人
	public static final short MPM_APPROVE_OBJ_TYPE_APPOINT_DEPT = 2;
	// 审批人 指定的审批人
	public static final short MPM_APPROVE_OBJ_TYPE_APPOINT_APPROVER = 3;
	// 审批人上N级部门的审批�?
	public static final short MPM_APPROVE_OBJ_TYPE_TOPN_DEPT = 4;
	//不持有令�?
	public static final Integer APPROVE_TOKEN_NOT_HOLD = 0;
	//持有令牌
	public static final Integer APPROVE_TOKEN_HOLD = 1;
	
	/** 用于审批流转的客户群数条件id */
	public static final String CUSTGROUP_NUM_COND_ID = "20000000";
	
	
	
	//ap_approve_drv_dimtable标示ID，这个配置和表ID字段设置一致
	public static final String   APPROVDRVDIMTABLE_CHANGJING="1";//场景类型
	public static final String   APPROVDRVDIMTABLE_YEWU="2";//业务分类
	
	
	//流程模式
	public static final String FLOW_MODEL_ONE="flow_0001";//基本信息
	public static final String FLOW_MODEL_TWO="flow_0002";//基本信息 审批	
	public static final String FLOW_MODEL_THREE="flow_0003";//基本信息 确认
	public static final String FLOW_MODEL_FOUR="flow_0004";//基本信息 审批 确认
	
	//0 - 待审批 确认  	1 - 通过 	2 - 不通过 	9 - 终止
	public static final String APPROVE_FLAG_ZERO="0";
	public static final String APPROVE_FLAG_ONE="1";
	public static final String APPROVE_FLAG_TWO="2";
	public static final String APPROVE_FLAG_NINE="9";
	
	
	private static void initMap(){
		flowTypeMap.put(FLOW_TYPE_CONFIRM, "确认");
		flowTypeMap.put(FLOW_TYPE_APPROVL, "审批");
		
		approveFlagMap.put(APPROVE_FLAG_ZERO, "待{#}");
		approveFlagMap.put(APPROVE_FLAG_ONE, "{#}通过");
		approveFlagMap.put(APPROVE_FLAG_TWO, "{#}不通过");
		approveFlagMap.put(APPROVE_FLAG_NINE, "{#}终止");
		
	}
		
}
