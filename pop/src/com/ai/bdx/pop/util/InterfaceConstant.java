package com.ai.bdx.pop.util;

/**
 * 接口常量类
 * @author zhangyb5
 *
 */
public class InterfaceConstant {
	/** 调用CM的code 客户群代码*/
	public static final String CALLWS_CUSTOMERS_CM_CODE = "AIBI_CM_CUSTOMERS_CUST";
	/** 调用CEP复杂事件规代码*/
	public static final String CALLWS_CEP_EVENT_CODE = "AIBI_CEP_CREATE_CODE";
	/** 调用CEP复杂事件获取LACCI信息代码*/
	public static final String CALLWS_CEP_LACCI_CODE = "AIBI_CEP_LACCI_CODE";
	/** 调用CEP复杂事件获取LACCI具体信息代码*/
	public static final String CALLWS_CEP_QUERYLACCI_CODE = "AIBI_CEP_QUERYLACCI_CODE";
	/** 调用DACP获取SESSION鉴权地址*/
	public static final String AIBI_DACP_SESSION = "AIBI_DACP_SESSION";
	/** 调用DACP同步DIM_LOGIC_AREA接口地址*/
	public static final String AIBI_GET_DACP_DIM_LOGIC_AREA = "AIBI_GET_DACP_DIM_LOGIC_AREA";
	/** 调用DACP同步DIM_PROD_PRODUCT_DM接口地址*/
	public static final String AIBI_DACP_DIM_PROD_PRODUCT_DM = "AIBI_DACP_DIM_PROD_PRODUCT_DM";
	/** 调用DACP同步DIM_TERM_INFO_TAC_MM接口地址*/
	public static final String AIBI_DACP_DIM_TERM_INFO_TAC_MM = "AIBI_DACP_DIM_TERM_INFO_TAC_MM";

	
	/**接口调用类型**/
	public static final String INVOKE_TYPE_CLIENT = "CLIENT";//客户端
	public static final String INVOKE_TYPE_SERVER = "SERVER";//服务端
}
