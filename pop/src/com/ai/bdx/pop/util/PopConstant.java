package com.ai.bdx.pop.util;

import com.asiainfo.biframe.utils.config.Configure;
import com.asiainfo.biframe.utils.string.StringUtil;

/**
 * 常量类
 * 
 * @author zhangyb5
 * 
 */
public class PopConstant {
	//POP省份文件存储位置
	public static final String PROPERTY_CONFIG_NAME="POPINFO_PROPERTIES";
	// 系统管理员的组ID
	public static final String SYS_ADMIN_GROUP = "1";
	// 策略场景派发客户清单模板表名
	public static final String POP_USER_YYYYMMDDHHMMSSSSS = "POP_USER_YYYYMMDDHHMMSSSSS";

	// 策略场景剔除客户清单表前缀
	public static final String POP_REJECT_PREFIX = "POP_REJECT_";

	public static final String CACHE_KEY_AVOID_BOTHER_DATA = "CACHE_KEY_AVOID_BOTHER_DATA";

	public static final String POP_TASK_PREFIX = "POP_TASK_";
 
	public static final String POP_TASK_TABLE = "POP_TASK_YYYYMMDDHHMMSSSSS";
 
	public static final String POP_SEND_PREFIX = "POP_SEND_";
	public static final String POP_PRE_SEND_PREFIX = "POP_PRE_SEND_";

	public static final String POP_RULE_SEND_PREFIX = "POP_RULE_SEND_";
	public static final String POP_RULE_PRE_SEND_PREFIX = "POP_RULE_PRE_SEND_";

	public static final String POP_SEND_TABLE = "POP_SEND_YYYYMMDDHHMMSSSSS";

	public static final String POP_REJECT_PREFIX_TABLE = "POP_REJECT_YYYYMMDDHHMMSSSSS";

	public static final String POP_RULE_CUST_CACHE_PREFIX = "POP_RULE_CUST_CACHE_{RULEID}";

	public static final String DB_TYPE_ORACLE = "ORACLE";
	public static final String DB_TYPE_DB2 = "DB2";
	public static final String DB_TYPE_MYSQL = "MYSQL";
	public static final String DB_TYPE_GP = "POSTGRESQL";
	public static final String DB_TYPE_GBASE = "GBASE";
	// 缓存数据初始化完成标识，0-未完成，1-完成
	public static final String CACHE_OK = "CACHE_OK";

	public static final String CACHE_KEY_CUSTOMERS_LISTMAP = "CACHE_KEY_CUSTOMERS_LISTMAP";

	public static final String CACHE_KEY_ACTIVITY_STATUS = "CACHE_KEY_ACTIVITY_STATUS";
	public static final String CACHE_KEY_ACTIVITY_TASKID = "CACHE_KEY_ACTIVITY_TASKID";
	
	public static final String CACHE_KEY_POP_RULE_SEND_TABLENAME = "CACHE_KEY_POP_RULE_SEND_";
	public static final String CACHE_KEY_POP_TASK_SEND_TABLENAME = "CACHE_KEY_POP_TASK_SEND_";

	public static final String CACHE_KEY_ACTIVITY_CUSTGROUP_TYPE = "CACHE_KEY_ACTIVITY_CUSTGROUP_TYPE";

	public static final String CACHE_KEY_ACT_AVOID_BOTHER_TYPE_IDS = "CACHE_KEY_ACT_AVOID_BOTHER_TYPE_IDS_";

	public static final String CACHE_KEY_AVOID_BOTHER_TYPE_DATA = "CACHE_KEY_AVOID_BOTHER_TYPE_DATA";

	// 缓存时间
	public static final long CACHE_TIME = 10 * 60 * 60;
	
	
	/** 等待派单*/
	public static final short TASK_STATUS_DDPD = 0;
	/** 派单中*/
	public static final short TASK_STATUS_PDZ = 1;
	/** 派单成功*/
	public static final short TASK_STATUS_PDCG = 2;
	/** 派单失败*/
	public static final short TASK_STATUS_PDSB = 3;
	/** 派单失效*/
	public static final short TASK_STATUS_PDSX = 4;
	/** 派单暂停*/
	public static final short TASK_STATUS_PDZT = 5;
	/** 派单终止*/
	public static final short TASK_STATUS_PDZZ = 6;
	
	/** 规则暂停*/
	public static final Integer RULE_OPERATE_STOP = 1;
	/** 规则重启*/
	public static final Integer RULE_OPERATE_RESTART = 2;
	/** 规则终止*/
	public static final Integer RULE_OPERATE_FINISH = 3;

	public static final String QUERY_USER_DATA_TO_MAP_TMP_SQL = "SELECT SUBSTR(product_no,2,2) v1,SUBSTR(product_no,4,4) v2,SUBSTR(product_no,8,4) v3 FROM {0} tmp";

	//正在编辑的策略
	public static final int POP_POLICY_EDIT_STATUS = 20;
	//正在执行中的策略
	public static final int POP_POLICY_EXCUTE_STATUS = 53;
	//暂停中的策略
	public static final int POP_POLICY_PAUSE_STATUS = 54;
	//派单失败的策略
	public static final int POP_POLICY_FAILURE_STATUS = 52;
	
	//策略全局等级
	public static final int POP_POLICY_LEVEL_GLOBAL = 1;
	//策略用户等级
	public static final int POP_POLICY_LEVEL_USER = 2;
	
	public static final String INSERT_DATA_TO_TABLE ="insert into {table} {column} values{?}";
	
	public static final String QUERY_OUT_OF_DATE_POLICY_SQL = "SELECT   id , policy_task_tab FROM  POP_POLICY_BASEINFO a where   template_flag = 0 and  policy_status_id > 50 and policy_status_id < 60  and end_time <?";
	
	public static final String UPDATE_OUT_OF_DATE_TASK_SQL = "update {table} set  exec_status=? where id=?";

	public static final int   RULE_STATUS_PAIDANCHENGGONG=51;//派单成功
	public static final int   RULE_STATUS_ZHIXINGZHONG=53;//执行中
	public static final int   RULE_STATUS_ZANTING=54;//暂停
	public static final int   RULE_STATUS_WANCHENG=60;//完成
	public static final int   RULE_STATUS_ZHONGZHI=70;//终止
	public static final String PERCENT = "%";
	/**
	 * 用户状态-1:正常
	 */
	public static final int USER_STATUS_NORMAL = 1;
	
	/**
	 * 用户状态-2:欠费
	 */
	public static final int USER_STATUS_ARREARAGE = 2;
	
	/**
	 * 用户状态-3:销户
	 */
	public static final int USER_STATUS_CANCEL = 3;
	
	/**
	 * 锁网状态:0：未锁网
	 */
	public static final int NET_LOCK_FLAG_UNLOCK = 0;
	
	/**
	 * 锁网状态:1：锁网（9个）
	 */
	public static final int NET_LOCK_FLAG_LOCK_1 = 1;
	
	/**
	 * 锁网状态:2：锁网（1-8个）
	 */
	public static final int NET_LOCK_FLAG_UNLOCK_2 = 2;
	
	/**
	 * 业务状态:1：正常
	 */
	public static final int BUSI_TYPE_NORMAL = 1;
	
	/**
	 * 业务状态为正常(值为1)时的下发缺省带宽:S110_R_LTE_Default
	 */
	public static final String BUSI_TYPE_NORMAL_FLAG = "S110_R_LTE_Default";
	
	/**
	 * 业务状态:2：已锁定（农村到城市）
	 */
	public static final int BUSI_TYPE_LIMIT_FOR_CITY = 2;
	
	/**
	 * 业务状态:3：已锁定（在9个锁网小区外的农村小区userLocation上网）
	 */
	public static final int BUSI_TYPE_LIMIT_FOR_LOCKED = 3;
	
	/**
	 * 业务状态为限速(值为2)时的下发限速带宽:S110_R_Not_CPE_LTE
	 */
	public static final String BUSI_TYPE_LIMIT_FLAG = "S110_R_Not_CPE_LTE";
	
	/**
	 * CPE锁位置业务策略ID的默认值
	 */
	public static final String CPE_DEFAULT_STRATEGY_CODES = "12270010000000000000000000000002,12270010000000000000000000000003,12270010000000000000000000000004,12270010000000000000000000000005";
	
	/**
	 * CPE锁网小区默认9个
	 */
	public static final int CPE_LOCKEDNET_DEFAULT_LACCICOUNT = 9;
	/**
	 * 操作状态:1：开户中（未调用PCC接口发起开户）
	 */
	public static final int OP_STATUS_OPENING_ACCOUNT = 1;
	
	/**
	 * 操作状态:2：开户完成（PCC接口返回开户成功）
	 */
	public static final int OP_STATUS_COMPLATE_ACCOUNT = 2;
	
	/**
	 * 操作状态:3：用户状态变更中-1
	 */
	public static final int OP_STATUS_CHANGING_ACCOUNT_STATUS = 3;
	
	/**
	 * 操作状态:4：用户状态变更完成
	 */
	public static final int OP_STATUS_ACCOUNT_COMPATE_ACCOUNT_STATUS = 4;
	
	/**
	 * 操作状态:5：用户带宽变更中
	 */
	public static final int OP_STATUS_CHANGING_BANDWIDTH = 5;
	
	/**
	 * 操作状态:6：用户带宽变更完成
	 */
	public static final int OP_STATUS_COMPLATE_BANDWIDTH = 6;
	
	/**
	 * 操作状态:7：单用户CPE参数重置中
	 */
	public static final int OP_STATUS_CHANGING_CPE_PARAMETER = 7;
	
	/**
	 * 操作状态:8：单用户CPE参数重置成功
	 */
	public static final int OP_STATUS_COMPLATE_CPE_PARAMETER = 8;
	
	/**
	 * 操作状态：CPE锁网中
	 */
	public static final int OP_STATUS_CPE_LOCKING_NET = 9;
	
	/**
	 * 操作状态：CPE锁网完成
	 */
	public static final int OP_STATUS_CPE_LOCKED_NET = 10;
	
	/**
	 * 操作状态:-1：操作异常
	 */
	public static final int OP_STATUS_EXCEPTION = -1;
	
	/**
	 * 解析成功
	 */
	public static final int ANALYSIS_SUCCESS = 1;
	
	/**
	 * 解析失败
	 */
	public static final int ANALYSIS_FAILURE = 0;
	
	/*
	 * Redis中的表-----------------------------------------------------
	 * start
	 */
	
	/**
	 * 用户S11信令数据是否是CPE位置信息缓存key前缀
	 */
	public static final String REDIS_CPE_PREFIX = "cpe:";
	
	/**
	 * CPE和锁网小区关系缓存key前缀
	 */
	public static final String REDIS_UNL_PREFIX = "unl:";
	
	/**
	 * 农村基站redis中Key前缀
	 */
	public static final String REDIS_CBS_PREFIX = "cbs:";
	
	/**
	 * CPE上网位置统计缓存前缀
	 */
	public static final String REDIS_CPE_USERLOCATION_STATISTICS_PREFIX = "cpeUserLocationStatistics:";
	
	/*
	 * Redis中的表-----------------------------------------------------
	 * end
	 */
	
	/**
	 * Adapter接入方式：
	 * 消息组件方式
	 */
	public static final String ADAPTER_METHOD_AMQP = "amqp";
	
	/**
	 * Adapter接入方式：
	 * Socket
	 */
	public static final String ADAPTER_METHOD_SOCKET = "socket";
	
	/**
	 * 调用PCC接口后,返回成功值
	 */
	public static final String PCC_RETURN_SUCCESS = "1";
	
	/**
	 * 开户时签约成功且添加userLocation成功
	 */
	public static final String PCC_REGISTER_SIGNED_SUCCESS_ADDED_SUCESS = "1";
	
	/**
	 * 开户时签约失败且添加userLocation失败
	 */
	public static final String PCC_REGISTER_SIGNED_ERROR_ADDED_ERROR = "0";
	
	/**
	 * 开户时策略签约成功，当时userLocation添加失败
	 */
	public static final String PCC_REGISTER_SIGNED_SUCCESS_ADDED_ERROR = "2";
	
	/**
	 * 调用PCC接口后,返回失败值
	 */
	public static final String PCC_RETURN_FAILURE = "0";
	
	/**
	 * CPE开户数据源名称
	 */
	public static final String CPE_INSTALL_SOURCE_NAME = "cpeInstall";
	
	/**
	 * CPE城市和农村位置变更数据源名称
	 */
	public static final String CPE_USER_STATUS_CHANGE_SOURCE_NAME = "cpeUserStatusChange";
	
	public static final String MCCMNC = "46000";
	
	/**
	 * userlocation的前缀
	 */
	public static final String USER_LOCATION_PREFIX = "130-46000";
	
	/**
	 * 获得根据模板表创建表的SQL语句
	 * 
	 * @param newTab
	 * @param tmpTable
	 * @return
	 */
	public static String getSqlCreateAsTable(String newTab, String tmpTable) {
		StringBuilder strRet = new StringBuilder();
		String tabSpace = Configure.getInstance().getProperty("MPM_TABLESPACE");
		String dbType = getDBType();
		if (dbType.equalsIgnoreCase(PopConstant.DB_TYPE_ORACLE) || dbType.equalsIgnoreCase(PopConstant.DB_TYPE_GP)) {
			if (StringUtil.isNotEmpty(tabSpace)) {// 如果包含表空间，则添加
				strRet.append("create table ").append(newTab).append(" tablespace ").append(tabSpace)
						.append(" as select * from ").append(tmpTable).append(" where 1=2 ");
			} else {
				strRet.append("create table ").append(newTab).append(" as select * from ").append(tmpTable)
						.append(" where 1=2 ");
			}
		} else if (dbType.equalsIgnoreCase(PopConstant.DB_TYPE_DB2)) {
			if (StringUtil.isNotEmpty(tabSpace)) {// 如果包含表空间，则添加
				strRet.append("create table ").append(newTab).append(" like ").append(tmpTable).append(" in ")
						.append(tabSpace).append(" NOT LOGGED INITIALLY ");
			} else {
				strRet.append("create table ").append(newTab).append(" like ").append(tmpTable)
						.append(" NOT LOGGED INITIALLY ");
			}
		} else if (dbType.equalsIgnoreCase(PopConstant.DB_TYPE_MYSQL)) {// 不需指定表空间
			strRet.append("create table ").append(newTab).append(" as select * from ").append(tmpTable)
					.append(" where 1=2 ");
		}
		return strRet.toString();
	}

	public static String getDBType() {
		String strDBType = Configure.getInstance().getProperty("DBTYPE");
		return strDBType.toUpperCase();
	}
	
//	/**
//	 * zhuyq3
//	 * @param key
//	 * @return
//	 */
//	public static C3p0Plugin getC3p0Plugin(String key) {
//		if (key.equals(C3P0PLUGIN_KEY_4_POC)) {
//			String jdbcUrl = "jdbc:mysql://10.1.253.202:3306/POC";
//			return new C3p0Plugin(jdbcUrl, "poc", "poc");
//		}
//		return null;
//	}
//	
//	public static final String C3P0PLUGIN_KEY_4_POC = "poc";
//	public static final String C3P0PLUGIN_KEY_4_QA = "qa";
//	
//	/**
//	 * add zhuyq3 2015-4-14 13:56:26
//	 */
//	public static final String HYPERLINK_4_SCHEDULE = "";
//	public static final String HYPERLINK_4_SYSTEM_INFO = "";

}
