/**
 * Filename: DamConst.java Description: Copyright: Copyright (c)2011
 * 
 * @author: bob
 * @version: 1.0 Create at: 2013-6-7 上午10:08:03 Modification History: Date
 *           Author Version Description
 *           ------------------------------------------------------------------
 *           2013-6-7 bob 1.0 Create
 */

package com.ai.bdx.pop.adapter.common.constant;


/**
 * @ClassName: DamConst
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author bob
 * @date 2013-6-7 上午10:08:03
 */
/**
 * Title : DamConstants
 * <p/>
 * Description : 
 * <p/>
 * CopyRight : CopyRight (c) 2014
 * <p/>
 * Company : 亚信科技(中国)有限公司
 * <p/>
 * JDK Version Used : JDK 5.0 +
 * <p/>
 * Modification History	:
 * <p/>
 * <pre>NO.    Date    Modified By    Why & What is modified</pre>
 * <pre>1    2014-10-22    wangxc        Created</pre>
 * <p/>
 *
 * @author  wangxc
 * @version 1.0.0.2014-10-22
 */
public class Constants {

    public static final String BLANK = "";
    public static final Object sqlAppend = " ";

    // oracle驱动
    public final static String ORACLEDRIVER = "oracle.jdbc.driver.OracleDriver";

    // DB2驱动
    public final static String DB2DRIVER = "com.ibm.db2.jcc.DB2Driver";
    
    // MySQL驱动
    public final static String MYSQLDRIVER ="com.mysql.jdbc.Driver";
    
    // Derby驱动
    public final static String DERBYDRIVER="org.apache.derby.jdbc.EmeddedDriver";

    // 数据库类型-DB2
    public final static int DW_TYPE_DB2 = 1;
    
    //数据资产获取权限菜单识别码。
    public final static String MENU_ID_PREFIX="DPCFMENUID";

    // 数据库类型-Oracle
    public final static int DW_TYPE_ORACLE = 2;
    
    //数据库类型-Hive
    public final static int DW_TYPE_HIVE = 3;
    
    //元数据库类型-DB2
    public final static int METADW_TYPE_DB2 = 1;
   //元数据库类型-Oracle
    public final static int METADW_TYPE_ORACLE = 2;
   //元数据库类型-Mysql
    public final static int METADW_TYPE_MYSQL = 3;
   //元数据库类型-Derby
    public final static int METADW_TYPE_DERBY = 4;

    // 数据库类型-TD
    public final static int DW_TYPE_TD = 3;
    
    public interface OfflineAppDwType{
        public final static String OFFLINE_TYPE_DEL="0";
        public final static String OFFLINE_TYPE_RECOVER="1";
    }
    //应用上下线的常量
    public interface AppUpDownLine{
    	//应用审核类型
        public final static Short APPROVE_TYPE_ONLINE=1;//应用上线
        public final static Short APPROVE_TYPE_DOWNLINE=2;//应用下线
        //应用审核结果
        public final static Short APPROVAL_RESULT_PASS=1;//通过审核
        public final static Short APPROVAL_RESULT_NOPASS=0;//不通过审核
        //应用调度状态
        public final static String APP_SCHEDUAL_NOEXE="0";//未执行
        public final static String APP_SCHEDUAL_SUCCESS="1";//执行成功
        public final static String APP_SCHEDUAL_FAILED="2";//执行失败
        
      //带删除资产类型
        public final static String APP_DEL_TYPE_REMOVE="0";//待删除
        public final static String APP_DEL_TYPE_RECOVERY="1";//待恢复
        
        //待删除和待下线审批类型
        public final static String APPROVA_FLAG_PASS="1";//通过审批
        public final static String APPROVA_FLAG_NOPASS="0";//未通过审批
    }

    // dpcf产品配置文件相当路径及配置文件名
    public static final String AIBI_DPCF_PROP_FILEPATH_NAME = "config/aibi_dpcf/dpcf.properties";

    // dpcf产品配置文件中标示元数据存储库jndi名称的变量名
    public static final String AIBI_DPCF_JDBC_NAME = "JDBC_DPCF";

    // 在BI系统统一配置信息管理实例中指向dpcf产品配置信息的变量名
    public static final String AIBI_DPCF_PROPERTIES = "aibi_dpcf_properties";

    // dpcf产品中数据库信息缓存管理类注册到BI系统统一缓存管理器中的变量名
    public static final String DPCF_DW_CACHE = "dpcf_dw_cache";

    // dpcf产品中表空间信息缓存管理类注册到BI系统统一缓存管理器中的变量名
    public static final String DPCF_TABLESPACE_CACHE = "dpcf_tbs_cache";

    // dpcf产品中服务器信息缓存管理类注册到BI系统统一缓存管理器中的变量名
    public static final String DPCF_SERVER_CACHE = "dpcf_server_cache";
    
 // dpcf产品中服务器信息缓存管理类注册到BI系统统一缓存管理器中的变量名
    public static final String DPCF_DATA_ASSETS_CATEGORY_CACHE = "dpcf_data_assets_category_cache";

    // 数据库告警级别参数类型
    public final static String ALARM_TYPE_ID_DW = "1";

    // 资产告警级别参数类型标识表空间
    public final static String ALARM_TYPE_ID_TABLE_SPACE = "2";

    // 数据资产告警级别参数类型
    public final static String ALARM_TYPE_ID_ASSETS = "3";

    // 默认提示类型邮件
    public final static String ALARM_MODEL_ID_DEFAULT = "2";

    // 告警规则严重
    public final static String ALARM_LEVEL_ID_SERIOUS = "1";

    // 告警规则中等
    public final static String ALARM_LEVEL_ID_MEDIUM = "2";

    // 告警规则一般
    public final static String ALARM_LEVEL_ID_GENERAL = "3";

    /** 告警规则小于 */
    public final static String ALARM_LOWER = "1";

    /** 告警规则小于等于 */
    public final static String ALARM_LOWER_EQUAL = "2";

    /** 告警规则大于 */
    public final static String ALARM_UPPER = "3";

    /** 告警规则大于等于 */
    public final static String ALARM_UPPER_EQUAL = "4";

    // 是否可用，是
    public final static String FLAG_TRUE = "1";

    // 是否可用，否
    public final static String FLAG_FALSE = "0";
    
    /**
     * 等待执行
     */
    public final static String EXEC_WAIT="0";
    
    /**
     * 执行成功
     */
    public final static String EXEC_SUCCESS="1";
    
    /**
     * 正在执行
     */
    public final static String EXEC_NOW = "8";
    
    /**
     * 执行成功并且已备份
     */
    public final static String EXEC_SUCCESS_AND_BACKUP="3";
    /**
     * 执行失败
     */
    public final static String EXEC_FAIL="2";
    
    /**
     * 执行SQL为空
     */
    public final static String EXEC_SQL_NULL="3";
    
    /**
     * 执行开关关闭错误
     */
    public final static String EXEC_FLAG_CLOSE="4";

    /**逻辑分层数据集市子层*/
    public final static String LOGIC_LAYER_DATASET = "数据集市子层";
    /**逻辑分层数据管理*/
    public final static String LOGIC_LAYER_DATAMANAGE = "数据管理层";
    /**应用等级 normal*/
    public final static String EVALUATE_LEVEL_NORMAL = "一般应用";
    /**应用等级 import*/
    public final static String EVALUATE_LEVEL_IMPORT = "重要应用";
    /**应用等级core*/
    public final static String EVALUATE_LEVEL_CORE = "核心应用";
    /**容灾无容灾*/
    public final static String RECOVERY_MODEL_NORMAL = "无容灾";
    /**容灾本地高可用*/
    public final static String RECOVERY_MODEL_IMPORT = "本地高可用";
    /**容灾应用级容灾*/
    public final static String RECOVERY_MODEL_CORE = "应用级容灾";
    
    
    /**
     * 周期 分别表示日 月 年 一次性 如：CYCLE_NONE，表示周期：一次性
     * 
     * @author wangsm3
     */
    public interface STORAGECYCLE {

        // 周期：日
        public final static String CYCLE_DAY = "1";

        // 周期：月
        public final static String CYCLE_MONTH = "2";

        // 周期：年
        public final static String CYCLE_YEAR = "3";
        
        // 周期：小时
        public final static String CYCLE_HOUR = "4";

        // 周期：一次性
        public final static String CYCLE_NONE = "0";
        
        // 周期：一次性RANGE
        public final static String CYCLE_RANGE = "5";
    }

    /**
     * 数据资产表类型 分别表示1 - 事实表 2 - 维表 3 - 其他
     * 
     * @author wangsm3 dataassetstype
     */
    public interface DATAASSETSTYPE {

        // 1 - 事实表
        public final static int DATA_ASSETS_TYPE_FACT = 1;

        // 2 - 维表
        public final static int DATA_ASSETS_TYPE_DIM = 2;

        // 3 - 其他
        public final static int DATA_ASSETS_TYPE_OTHER = 3;
        
        //4 - 视图
        public final static int DATA_ASSETS_TYPE_VIEW=4;
    }

    /**
     * 数据资产表状态 0，待审批 1，已经审批 2，审批不通过
     * 
     * @author wangsm3 assetsstatus dataassetstype
     */
    public interface ASSETSSTATUS {

        // 0，待审批
        public final static String ASSETS_STATUS_0 = "0";

        // 1，已经审批
        public final static String ASSETS_STATUS_1 = "1";

        // 2，审批不通过
        public final static String ASSETS_STATUS_2 = "2";
    }
    
    /**
     * 数据资产变更表表状态 0：未处理 1：已同步 2，忽略
     * 
     * @author wangsm3 assetsstatus dataassetstype
     */
    public interface AssetsChangeStatus {
        
        // 0，未处理
        public final static String ASSETS_CHANGE_UNTREATED = "0";
        
        // 1，已同步
        public final static String ASSETS_CHANGE_SYNC = "1";
        
        // 2，忽略
        public final static String ASSETS_CHANGE_IGNORE = "2";
    }
    /**
     * Title : AssetsChangeType
     * <p/>
     * Description :资产变更类型
     * <p/>
     * CopyRight : CopyRight (c) 2013
     * <p/>
     * Company : 亚信联创科技(中国)有限公司
     * <p/>
     * JDK Version Used : JDK 5.0 +
     * <p/>
     * Modification History	:
     * <p/>
     * <pre>NO.    Date    Modified By    Why & What is modified</pre>
     * <pre>1    2013-8-23    qiubo        Created</pre>
     * <p/>
     *
     * @author  qiubo
     * @version 1.0.0.2013-8-23
     */
    public interface AssetsChangeType{
        /** 字段增加 */
        public final static String ASSETS_CHANGE_TYPE_COL_ADD="1";
        /**  字段类型变化*/
        public final static String ASSETS_CHANGE_TYPE_COL_TYPE_CHG="2";
        /**表资产新增  */
        public final static String ASSETS_CHANGE_TYPE_TABLE_ADD="3";
        /** 字段删除 */
        public final static String ASSETS_CHANGE_TYPE_COL_DEL="4";
        /** 超期的资产 */
        public final static String ASSETS_CHANGE_TYPE_OVER_CYCLE="5";
        /** 删除的资产 */
        public final static String ASSETS_CHANGE_TYPE_DEL_ASSETS="6";
    }
    /**
     * ORACLE数据库
     */
    public static String DB_TYPE_ORACLE = "ORACLE";

    /**
     * DB2数据库
     */
    public static String DB_TYPE_DB2 = "DB2";
    
    /**
     * MYSQL数据库
     */
    public static String DB_TYPE_MYSQL = "MYSQL";

    /**
     * 分类类别一级level 取各大类信息
     */
    public static String CATEGORYBYLEVEL = "1";
    
    /**
     * 分类类别所有的level为1的parentId 即 根节点
     */
    public static String CATEGORYROOT = "-1";
    
    /** 层次分类 */
    public static String LAYER_CATEGORY = "1";
    /** 主题分类 */
    public static String THEME_CATEGORY = "2";
    /** 业务分类 */
    public static String BUSINESS_CATEGORY = "3";
    
    public static String ALARM_REPLACE_STR = "$$DA";


    /**
     *  资产增加字段。
     */
    public static String ASSETS_CHG_COLUMN_ADD = "1";

    /**
     * 资产减少字段。
     */
    public static String ASSETS_CHG_COLUMN_MINUS = "4";
    /**
     * 资产字段数据类型发生变更
     */
    public static String ASSETS_CHG_COLUMN_TYPE = "2";

    /**
     * 新增表
     */
    public static String ASSETS_CHG_TABLE_ADD = "3";
    
    
    public static int STORAGE_CYCLE_INIT = 3;
    
    public static int MOVE_CYCLE_INIT = 1;
    
    public static int DATA_ASSETS_VALUE_MAJOR = 1;

    //资产信息完整性标志 - 不完整
    public static String ASSETS_INTEGRITY_FLAG_NO = "0";
    
  //资产信息完整性标志 - 完整
    public static String ASSETS_INTEGRITY_FLAG_YES = "1";
  
    //选中每年最后一天  
    public static String LAST_DAY_OF_YEAR_FLAG_YES = "1";
    
  //选中每月最后一天  
    public static String LAST_DAY_OF_MONTH_FLAG_YES = "1";
    
    //删除日期类型 每年最后一天
    public static String DEL_DATE_TYPE_LAST_DAY_OF_YEAR = "2";
    
    //删除日期类型  每月最后一天
    public static String DEL_DATE_TYPE_LAST_DAY_OF_MONTH = "3";
    
    //删除日期类型  固定日期
    public static String DEL_DATE_TYPE_DEFINITE_DATE = "1";
    
    //删除日期类型  固定月份
    public static String DEL_DATE_TYPE_DEFINITE_MONTH = "4";
    
    
    
    /** 
     * 数据库扫描成功
     */
    public static String DB_SCAN_SUCCESS="2";
    /** 
     * 数据库扫描失败
     */
    public static String DB_SCAN_FAIL="1";
    /** 
     * 数据库扫描正在执行
     */
    public static String DB_SCAN_RUN="0";
    
    public interface TableDetailStroageType{
        public final String TABLE_STORAGE="1";
        public final String INDEX_STORAGE="2";
        
    }
    public interface monitorCycleId{
        /** 时监控*/
        public final static String monitor_cycle_id_1="1";
        /** 日监控*/
        public final static String monitor_cycle_id_2="2";
     
    }
    /**
     * @description 应用基本信息表的DPCF_APPLICATION_INFO.STATUS 状态
     * @author qihy
     *
     */
    public interface APPLICATIONINFO_STATUS{
//    	  1，上线待审批
    	public final static String ONLINE_APPROVE="1"; 
//        2，已上线
    	public final static String ALREADY_ONLINE="2"; 
//        3，上线审批不通过
    	public final static String ONLINE_APPROVE_NOPASS="3"; 
//        4，下线待审批
    	public final static String DOWNLINE_APPROVE="4"; 
//        5，已下线
    	public final static String ALREADY_DOWNLINE="5"; 
//        6，下线审批不通过
    	public final static String DOWNLINE_APPROVE_NOPASS="6"; 
//        7,应用下线表删除
        public final static String DOWNLINE_TBL_DELETE="7";
    	 
    }
    
    /**
     * 规则模板表DPCF_RULE_TEMPLATE.CYCLE_TYPE字段维值
     * @author gaobo
     *
     */
    public interface RULE_CYCLE_TYPE{
        /**一次性周期*/
        public final static int NONE = 0;
        /**日周期*/
    	public final static int DAY = 1;
    	/**月周期*/
    	public final static int MONTH = 2;
    	/**rang分区*/
    	public final static int RANGE = 5;
    	
    }
    /**月末/年末不保留*/
    public final static int NOT_SAVE = 0;
    /**月末/年末保留*/
    public final static int IS_SAVE = 1;
    
    /**月末最后一天保留*/
    public final static String MONTH_END_SAVE = "1";
    
    /**年末最后一天保留*/
    public final static String YEAR_END_SAVE = "2";
    
    
    /**
     * 规则模板表DPCF_RULE_TEMPLATE.SAVE_DURA_TYPE字段维值
     * @author gaobo
     *
     */
    public interface SAVE_RULE_DURA_TYPE{
    	/**以天为单位*/
    	public final static int DAY = 1;
    	/**以月为单位*/
    	public final static int MONTH = 2;
    	/**以年为单位   */
    	public final static int YEAR = 3;
    	/**永久*/
    	public final static int FOREVER = -1;
    }
    
    /**保留时长类型=-1(永久)时保留时长默认值*/
    public final static int DEFAULT_SAVE_DURA = -999;
    /**资产表信息有配置规则*/
    public final static String HAS_RULE = "1";
    /**资产表信息没有配置规则*/
    public final static String NO_RULE = "0";
    /**
     * 
     * Title : 数据迁移部分的常量
     *      */
    public interface MIGRATE{
        public final static String QUERYTYPE_HISTORY = "1";//查询历史数据
        public final static String QUERYTYPE_PROCESSING = "0";//查询迁移中的数据
    }
    
    /**RANGE分区checkbox被选中、自动删除规则被选中*/
    public final static String HAS_CHECKED= "1";
    
    
    /**
     * range分区的粒度
     * @author gaobo
     *
     */
    public interface RANGE_TYPE{
        /**日粒度*/
        public final static String DAY = "1";
        /**月粒度*/
        public final static String MONTH = "2";
        
    }
    
    /**
     * 价值策略分析维度
     * @author Administrator
     *
     */
    public interface ValueDimen{
    	/**访问频次维度ID*/
    	public final static String VISIT_CNT="1";
    	/**关联应用数维度ID*/
    	public final static String RELA_APP_CNT="2";
    }

    /**开启扫描全部超期表功能的标志*/
    public final static String OPEN_SCAN_ALL_OVER_PERIOD = "1";
    
    /** DB2 9.7版本的双精度格式的版本号*/
    public final static Double VALUE_OF_DB2V9_7 = 9.7;
    
    public interface RANKING_TYPE{
    	public static String PROJECT="2";//按照应用分类
    	public static String ASSETS = "3";//按照资产分类
    	public static String INDUSTRY = "1"; //按照行业分类
    }
    
    public interface AssetsLevel{
    	public static String low = "3";
    	public static String mid = "2";
    	public static String high = "1";
    }
    
   public static int STATUS_DELETE = 0;
   public static int STATUS_ONLINE = 1;
   public static int STATUS_OFFLINE = 2;
   
   public static int NOT_FLOATING_FLAG = 0;
   public static int IS_FLOATING_FLAG = 1;
   
   public static int ISMERGE_TRUE = 1;
   public static int ISMERGE_FALSE = 0;
   
   public static int ISREFERENCE_TRUE = 1;
   public static int ISREFERENCE_FALSE = 2;
   
   public interface RootCategory{
       /**
        * 指标分类
        */
       public static final int CATEGORY_KPI = 1;
       /**
        * 数据源分类;
        */
       public static final int CATEGORY_DATASOURCE = 2;
       /**
        * 图表分类
        */
       public static final int CATEGORY_CHART = 3;
   }
   
   /**
    * 数据源数据表名前缀
    */
   public final static String DATASOURCE_DATA_TABLE_PREX = "DATASOURCE_DATA_";
   
   /**
    * 维度列名前缀
    */
   public final static String DATASOURCE_DATA_DIM_COLUMN_PREX = "DIM_";
   
   /**
    * 指标列名前缀
    */
   public final static String DATASOURCE_DATA_KPICOLUMN_COLUMN_PREX = "KPICOLUMN_";
   
   public final static int DATASOURCE_DATA_COLUMN_VARCHAR_LENGTH = 4000;
   
   /**
    * 数据源数据名后缀数字字符串长度
    */
   public final static int DATASOURCE_DATA_TABLENAME_SUFFIX_LENGTH = 10;
   
   /**
    * 维度数据失效
    */
   public final static int KPI_DIM_STATUS_INVALID = 3;
   
   /**
    * 数据源新建和修改时，1表示数据源数据表格中的一行是维度信息
    */
   public final static int COLUMN_IS_DIM = 1;
   
   /**
    * 数据源新建和修改时，2表示数据源数据表格中的一行是指标列信息
    */
   public final static int COLUMN_IS_KPICOLUMN = 2;
   
   public interface KPI_PERIOD{
       public final static String DAILLY="1";
       public final static String MONTHY="2";
   }
   //是否为样例数据
   public interface IS_SAMPLE{
       public final static int SAMPLE=1;
       public final static int NOT_SAMPLE = 0;
       
   }
   
   
   public interface SERIES_TYPE {
	public final static String  LINE = "line";
	public final static String  BAR = "bar";
	public final static String  PIE = "pie";
	
}
   

   /**
    * 维度数据
    * @author liaojy
    *
    */
   public interface KPI_DIM_DATA{
	   /**
	    * 中国移动
	    */
	   public final static int TELECOMS_OPERATOR_MOBILE = 0;
	   /**
	    * 中国联通
	    */
	   public final static int TELECOMS_OPERATOR_UNICOM = 1;
	   /**
	    * 中国电信
	    */
	   public final static int TELECOMS_OPERATOR_TELECOM = 2;
	   /**
	    * 中国网通
	    */
	   public final static int TELECOMS_OPERATOR_NETCOM = 3;
	   /**
	    * 状态正常
	    */
	   public final static int KPI_DIM_STATE_NORMAL = 0;
	   /**
	    * 状态新增
	    */
	   public final static int KPI_DIM_STATE_NEW = 1;
	   /**
	    * 状态修改
	    */
	   public final static int KPI_DIM_STATE_MODIFY = 2;
	   /**
	    * 状态失效
	    */
	   public final static int KPI_DIM_STATE__INVALID = 3;
	   /**
	    * 地区未知
	    */
	   public final static int UNKNOWN_FLAG_TRUE = 0;
	   /**
	    * 地区不是未知
	    */
	   public final static int UNKNOWN_FLAG_FALSE = 1;
	   /**
	    * 省中心
	    */
	   public final static int UNKNOWN_FLAG_CENTER = 2;
	   


   }

   public interface IS_DRILL_DOWN{
       public final static int CAN_DRILL_DOWN = 1;
       public final static int NOT_DRILL_DOWN = 0;
   }

}
