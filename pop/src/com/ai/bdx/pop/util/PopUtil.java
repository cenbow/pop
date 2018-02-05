package com.ai.bdx.pop.util;

 
import java.lang.reflect.Field;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.ResultSet;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hyperic.sigar.NetFlags;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;
import org.springframework.jdbc.core.JdbcTemplate;

import com.ai.bdx.pop.base.Db2Dialect;
import com.ai.bdx.pop.base.PopModel;
import com.ai.bdx.pop.enums.PolicyStatus;
import com.ai.bdx.pop.exception.PopException;
import com.asiainfo.biframe.utils.config.Configure;
import com.asiainfo.biframe.utils.string.StringUtil;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.dialect.Dialect;
import com.jfinal.plugin.activerecord.dialect.MysqlDialect;
import com.jfinal.plugin.activerecord.dialect.OracleDialect;
import com.jfinal.plugin.activerecord.dialect.PostgreSqlDialect;

/**
 * CEP工具类
 * 
 * @author wanglei
 *
 */
public class PopUtil {
	public static String lastTime = "";//记录最新的时间戳
	private static Logger log = LogManager.getLogger();
	public static final char[] CHARMAP;
	static {
		CHARMAP = new char[64];
		for (int i = 0; i < 10; i++) {
			CHARMAP[i] = (char) ('0' + i);
		}
		for (int i = 10; i < 36; i++) {
			CHARMAP[i] = (char) ('a' + i - 10);
		}
		for (int i = 36; i < 62; i++) {
			CHARMAP[i] = (char) ('A' + i - 36);
		}
		CHARMAP[62] = '_';
		CHARMAP[63] = '-';
	}
	private static final ImmutableMap<String, String> DB_TO_DRIVER = ImmutableMap.of("db2",
			"com.ibm.db2.jcc.DB2Driver", "oracle", "oracle.jdbc.driver.OracleDriver", "postgresql",
			"org.postgresql.Driver", "mysql", "com.mysql.jdbc.Driver");

	private static final ImmutableMap<String, ? extends Dialect> DB_TO_DIALECT = ImmutableMap.of("db2",
			new Db2Dialect(), "oracle", new OracleDialect(), "postgresql", new PostgreSqlDialect(), "mysql",
			new MysqlDialect());

	private static final int[] NUM_DATA = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 };
	private static final Pattern p = Pattern.compile("(\\$\\{(\\w+)\\})+");

	public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
	public static  SimpleDateFormat sendOddformat = new SimpleDateFormat("yyyy-MM-dd");
	public static  SimpleDateFormat cacheDateformat = new SimpleDateFormat("yyyyMMdd");
	
	private static final String QUERY_AVOID_BOTHER_TYPE_IDS_SQL = "SELECT avoid_custgroup_ids FROM  POP_POLICY_RULE_ACT  WHERE policy_rule_id = ?";
	

	public static String getDriverByType(String dbType) {
		String strType = (dbType == null) ? "" : dbType.toLowerCase();
		return DB_TO_DRIVER.get(strType);
	}

	public static Dialect getDialectByType(String dbType) {
		String strType = (dbType == null) ? "" : dbType.toLowerCase();
		return DB_TO_DIALECT.get(strType);
	}

	public synchronized static String generateUUID() {
		String uuid = UUID.randomUUID().toString();
		uuid = uuid.replaceAll("-", "").toUpperCase();
		uuid = "0" + uuid;
		uuid = hexTo64(uuid);
		return uuid;
	}

	public static String hexTo64(String hex) {
		StringBuffer r = new StringBuffer();
		int index = 0;
		int[] buff = new int[3];
		int l = hex.length();
		for (int i = 0; i < l; i++) {
			index = i % 3;
			buff[index] = Integer.parseInt("" + hex.charAt(i), 16);
			if (index == 2) {
				r.append(CHARMAP[buff[0] << 2 | buff[1] >>> 2]);
				r.append(CHARMAP[(buff[1] & 3) << 4 | buff[2]]);
			}
		}
		return r.toString();
	}

	/**
	 * 根据时间戳生成Id，默认17位
	 * @return YYYYMMDDHHMMSSXXX
	 */
	public synchronized static String generateId() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		String dateStr = sdf.format(new Date());
		//唯一性保证
		if (StringUtil.isNotEmpty(lastTime) && Long.valueOf(lastTime) >= Long.valueOf(dateStr)) {
			dateStr = generateId();
		} else {
			lastTime = dateStr;
		}
		return dateStr;
	}

	/**
	 * 生成指定长度的随机数字
	 * @param len
	 * @return
	 */
	public static String genFixLenNumStr(int len) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < len; i++) {
			int index = (int) (Math.random() * 10);
			sb.append(NUM_DATA[index]);
		}
		return sb.toString();
	}

	/**
	 * 提取字符串中${}大括号中包含的数字字符串
	 * @param str
	 * @return
	 */
	public static List<String> extractVariable(String str) {
		Matcher matcher = p.matcher(str);
		List<String> list = Lists.newArrayList();
		while (matcher.find()) {
			String str1 = matcher.group(2);
			if (!list.contains(str1)) {
				list.add(matcher.group(2));
			}
		}
		return list;
	}

	/**
	 * 从数据库查询到的List<Model>转换成json数组字符串
	 * @param list
	 * @return
	 */
	public static String convertModelList2JSONString(@SuppressWarnings("rawtypes") List<PopModel> list) {
		if (list == null) {
			return "";
		}
		@SuppressWarnings("rawtypes")
		List<Map> list2 = Lists.newArrayList();
		for (@SuppressWarnings("rawtypes")
		Model model : list) {
			Map<String, Object> map = Maps.newHashMap();
			for (String name : model.getAttrNames()) {
				map.put(name, model.get(name));
			}
			list2.add(map);
		}
		return new Gson().toJson(list2);
	}

	/**
	 * 将Model对象转换为json字符串
	 * @param model
	 * @return
	 */
	public static String convertModel2JSONString(@SuppressWarnings("rawtypes") Model model) {
		Map<String, Object> map = Maps.newHashMap();
		for (String name : model.getAttrNames()) {
			map.put(name, model.get(name));
		}
		return new Gson().toJson(map);
	}

	/**
	 * 从数据库查询到的List<Record>转换成json数组字符串
	 * @param list
	 * @return
	 */
	public static String convertRecordList2JSONString(List<Record> list) {
		if (list == null) {
			return "";
		}
		@SuppressWarnings("rawtypes")
		List<Map> list2 = Lists.newArrayList();
		for (Record record : list) {
			Map<String, Object> map = Maps.newHashMap();
			for (String name : record.getColumnNames()) {
				map.put(name, record.get(name));
			}
			list2.add(map);
		}
		return new Gson().toJson(list2);
	}

	public static String getSql2Date(String dateTime) throws Exception {
		return getSql2Date(dateTime, "-");

	}

	public static String getSql2Date(String strDateStr, String splitStr) // NOPMD
			throws Exception {
		String strDBType = getDBType();
		return getSql2Date(strDBType, strDateStr, splitStr);
	}

	public static String getSqlDateTime() throws Exception {
		return getSql2DateTimeNow();
	}

	public static String getSql2DateTimeNow() throws Exception {
		String strType = getDBType();
		String strRet = "";
		if (strType.equalsIgnoreCase("MYSQL")) {
			strRet = "now()";
		} else if (strType.equalsIgnoreCase("ORACLE")) {
			strRet = "sysdate";
		} else if (strType.equalsIgnoreCase("ACESS")) {
			strRet = "date()+time()";
		} else if (strType.equalsIgnoreCase("SQLSERVER")) {
			strRet = "getdate()";
		} else if (strType.equalsIgnoreCase("POSTGRESQL")) {
			strRet = "current_timestamp";
		} else if (strType.equalsIgnoreCase("DB2")) {
			strRet = "current timestamp";
		} else if (strType.equalsIgnoreCase("TERA")) {
			strRet = "cast((date (format 'yyyy-mm-dd' )) as char(10)) ||' '|| time";
		} else if (strType.equalsIgnoreCase("SYBASE")) {
			strRet = "getdate()";
		} else {
			throw new Exception("can't get the current date of the function definition");
		}
		return strRet;
	}

	public static String getDBType() {
		String strDBType = Configure.getInstance().getProperty("DBTYPE");
		return strDBType.toUpperCase();
	}

	/**
	 *
	 * @param strDateStr
	 * @param splitStr
	 * @return
	 * @throws Exception
	 */
	public static String getSql2Date(String dbType, String sDateStr, String splitStr) throws Exception {
		String strDateStr = sDateStr;
		if (Strings.isNullOrEmpty(strDateStr)) {
			return null;
		}
		if (strDateStr.indexOf("0000-00-00") >= 0) {
			return "null";
		}
		String strRet = "";
		int i = strDateStr.indexOf(" ");
		if (i > 0) {
			strDateStr = strDateStr.substring(0, i);
		}

		if (dbType.equalsIgnoreCase("MYSQL") || dbType.equalsIgnoreCase("GBASE")) {
			strRet = "'" + strDateStr + "'";
		} else if (dbType.equalsIgnoreCase("DB2")) {
			strRet = "date('" + strDateStr + "')";
		} else if (dbType.equalsIgnoreCase("ORACLE") || dbType.equalsIgnoreCase("POSTGRESQL")) {
			if ("-".equals(splitStr)) {
				strRet = "to_date('" + strDateStr + "','YYYY-mm-dd')";
			} else {
				strRet = "to_date('" + strDateStr + "','YYYY/mm/dd')";
			}
		} else if (dbType.equalsIgnoreCase("ACESS")) {
			strRet = "'" + strDateStr + "'";
		} else if (dbType.equalsIgnoreCase("SQLSERVER")) {
			if ("-".equals(splitStr)) {
				strRet = "convert(varchar(10), convert(datetime,'" + strDateStr + "'), 111)";
			} else {
				strRet = "CONVERT(Datetime,'" + strDateStr + "',20)";
			}
		} else if (dbType.equalsIgnoreCase("TERA")) {
			if ("-".equals(splitStr)) {
				strRet = "cast('" + strDateStr + "' as date FORMAT 'YYYY-MM-DD')";
			} else {
				strRet = "cast('" + strDateStr + "' as date FORMAT 'YYYY/MM/DD')";
			}
		} else if (dbType.equalsIgnoreCase("SYBASE")) {
			strRet = "cast('" + strDateStr + "' as Date)";
		} else {
			throw new Exception("can't get the current date of the function definition");
		}
		return strRet;
	}

	/**
	 *
	 * @param strDateStr
	 * @param splitStr
	 * @return
	 * @throws Exception
	 */
	public static String getSql2Date2(String dbType, String sDateStr, String splitStr) throws Exception {
		String strDateStr = sDateStr;
		if (Strings.isNullOrEmpty(strDateStr)) {
			return null;
		}
		if (strDateStr.indexOf("0000-00-00") >= 0) {
			return "null";
		}
		String strRet = "";

		if (dbType.equalsIgnoreCase("MYSQL") || dbType.equalsIgnoreCase("GBASE")) {
			if ("-".equals(splitStr)) {
				strRet = "date_format(" + strDateStr + ",'%Y-%m-%d')";
			}else{
				strRet = "date_format(" + strDateStr + ",'%Y/%m/%d')";
			}
			
		} else if (dbType.equalsIgnoreCase("DB2")) {
			strRet = "TIMESTAMP('" + strDateStr + "')";
		} else if (dbType.equalsIgnoreCase("ORACLE") || dbType.equalsIgnoreCase("POSTGRESQL")) {
			if ("-".equals(splitStr)) {
				strRet = "to_date('" + strDateStr + "','YYYY-mm-dd HH24:mi:ss')";
			} else {
				strRet = "to_date('" + strDateStr + "','YYYY/mm/dd HH24:mi:ss')";
			}
		} else if (dbType.equalsIgnoreCase("ACESS")) {
			strRet = "'" + strDateStr + "'";
		} else if (dbType.equalsIgnoreCase("SQLSERVER")) {
			if ("-".equals(splitStr)) {
				strRet = "convert(varchar(10), convert(datetime,'" + strDateStr + "'), 111)";
			} else {
				strRet = "CONVERT(Datetime,'" + strDateStr + "',20)";
			}
		} else if (dbType.equalsIgnoreCase("TERA")) {
			if ("-".equals(splitStr)) {
				strRet = "cast('" + strDateStr + "' as date FORMAT 'YYYY-MM-DD HH24:mi:ss')";
			} else {
				strRet = "cast('" + strDateStr + "' as date FORMAT 'YYYY/MM/DD HH24:mi:ss')";
			}
		} else if (dbType.equalsIgnoreCase("SYBASE")) {
			strRet = "cast('" + strDateStr + "' as Date)";
		} else {
			throw new Exception("can't get the current date of the function definition");
		}
		return strRet;
	}

	/**
	 * 将数据库中的日期类型转换为本地语言规定的数据格式； 数据库中的日期格式为yyyy-MM-dd
	 *
	 * @param date
	 * @return
	 */
	public static String parseOutDate(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat(DEFAULT_DATE_FORMAT);
		return sdf.format(date);
	}

	public static String getSqlLimit(String strSql, int limitnum) // NOPMD by Administrator on 11-8-26 8:54
			throws Exception {
		String strType = getDBType();
		String strRet = "";
		if (strType.equalsIgnoreCase("MYSQL")) {
			strRet = (new StringBuilder()).append(strSql).append(" limit ").append(limitnum).toString();
		} else if (strType.equalsIgnoreCase("ORACLE")) {
			limitnum++;
			strRet = (new StringBuilder()).append("select * from (").append(strSql).append(") where ROWNUM<")
					.append(limitnum).toString();
		} else if (strType.equalsIgnoreCase("DB2")) {
			strRet = (new StringBuilder()).append(strSql).append("fetch first ").append(limitnum).append(" rows only")
					.toString();
		} else if (strType.equalsIgnoreCase("SYBASE")) {
			strRet = (new StringBuilder()).append("select top ").append(limitnum).append(" * from(").append(strSql)
					.append(") a").toString();
		} else if (strType.equalsIgnoreCase("SQLSERVER")) {
			strRet = (new StringBuilder()).append("select top ").append(limitnum).append(" * from(").append(strSql)
					.append(") a").toString();
		} else if ("TERA".equalsIgnoreCase(strType)) {
			StringBuffer buffer = new StringBuffer(strSql.length() + 100);
			buffer.append(strSql);
			int orderByIndex = buffer.toString().toLowerCase().lastIndexOf("order by");
			if (orderByIndex > 0) {
				String orderBy = buffer.substring(orderByIndex);
				buffer.insert(orderByIndex, " QUALIFY row_number() OVER( ");
				buffer.append(" ) ");
				buffer.append((new StringBuilder()).append(" between 1 and ").append(limitnum).toString());
				buffer.append(orderBy);
			} else {
				buffer.append((new StringBuilder())
						.append(" QUALIFY sum(1) over (rows unbounded preceding) between 1 and ").append(limitnum)
						.toString());
			}
			strRet = buffer.toString();
		} else {
			throw new Exception("function definition can not be achieved");
		}
		return strRet;
	}

	/**
	 *
	 * @param sql
	 * @param column
	 * @param strPrimaryKey
	 * @param curpage
	 * @param pagesize
	 * @return
	 */
	public static String getPagedSql(String sql, String column, String strPrimaryKey, int curpage, int pagesize)
			throws Exception {
		String strDBType = getDBType();
		StringBuffer buffer = null;
		buffer = new StringBuffer();
		if ("ORACLE".equalsIgnoreCase(strDBType)) {
			buffer.append("select * from ( ");
			buffer.append("select ").append(column).append(" rownum as my_rownum from( ");
			buffer.append(sql).append(") ");
			int pageAll = pagesize * curpage + pagesize;
			buffer.append((new StringBuilder()).append("where rownum <= ").append(pageAll).append(") a ").toString());
			buffer.append((new StringBuilder()).append("where a.my_rownum > ").append(pagesize * curpage).toString());
		} else if ("DB2".equalsIgnoreCase(strDBType)) {
			buffer.append("select * from ( ");
			buffer.append("select ")
					.append(column)
					.append((new StringBuilder()).append("  rownumber() over (order by ").append(strPrimaryKey)
							.append(") as my_rownum from( ").toString());
			buffer.append(sql).append(") as temp ");
			buffer.append((new StringBuilder()).append("fetch first ").append(pagesize * curpage + pagesize)
					.append(" rows only) as a ").toString());
			buffer.append((new StringBuilder()).append("where a.my_rownum > ").append(pagesize * curpage).toString());
		} else if ("TERA".equalsIgnoreCase(strDBType)) {
			buffer.append(sql);
			int orderByIndex = buffer.toString().toLowerCase().lastIndexOf("order by");
			if (orderByIndex > 0) {
				String orderBy = buffer.substring(orderByIndex);
				buffer.insert(orderByIndex, " QUALIFY row_number() OVER( ");
				buffer.append(" ) ");
				buffer.append((new StringBuilder()).append(" between ").append(pagesize * curpage).append(" and ")
						.append(pagesize * curpage + pagesize).toString());
				buffer.append(orderBy);
			} else {
				buffer.append((new StringBuilder()).append(" QUALIFY sum(1) over (rows unbounded preceding) between ")
						.append(pagesize * curpage).append(" and ").append(pagesize * curpage + pagesize).toString());
			}
		}
		return buffer.toString();
	}

	public static String replaceInputValue(String str) {
		return str.replaceAll("'", "\\\\'");
	}

 
	private static Date str2Date(String str) {
		if (str == null)
			return null;
		try {
			return sendOddformat.parse(str);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static List<String> process(String date1, String date2) {
		List<String> list = new ArrayList();
		list.add(date1);
		String tmp = sendOddformat.format(str2Date(date1).getTime() + 3600 * 24 * 1000);
		while (tmp.compareTo(date2) <= 0) {
			list.add(tmp);
			tmp = sendOddformat.format(str2Date(tmp).getTime() + 3600 * 24 * 1000);
		}
		return list;
	}
	
	
	/**
	 * 根据时间范围拆分时间
	 * @param str格式:2015-04-13~2015-05-14,2015-04-17~2015-05-1
	 * 
	 * */
	public static List<String> getExeDayList(String str){
		List<String> dayList = new ArrayList<String>();
		if(str.indexOf(",")!=-1){
			String[] a = str.split(",");
			for(int i=0;i<a.length;i++){
				List<String> tmp = process(a[i].split("~")[0],a[i].split("~")[1]);
				dayList.addAll(tmp);
			}
		}else if(str.indexOf("~")!=-1){
			List<String> tmp = process(str.split("~")[0],str.split("~")[1]);
			dayList.addAll(tmp);
		}else{
			dayList.add(str);
		}
		return dayList;
	}
	
	
	
	

	public static String formatPhoneNo(short num) {
		String str = String.valueOf(num);
		int n = str.length();
		StringBuilder sb = new StringBuilder();
		switch (n) {
		case 1:
			sb.append("000").append(str);
			break;
		case 2:
			sb.append("00").append(str);
			break;
		case 3:
			sb.append("0").append(str);
			break;
		case 4:
			sb.append(str);
			break;
		default:
			sb.append(str);
			break;
		}
		return sb.toString();

	}
	
	/**
	 * 清理表数据
	 * @param tabName
	 * @throws Exception
	 */
	public static void clearTabData(String tabName) throws Exception {
		String dbType = getDBType();
		JdbcTemplate jdbcTemplate = SpringContext.getBean("jdbcTemplate", JdbcTemplate.class);
		if (StringUtil.isNotEmpty(tabName)) {
			try {
				StringBuffer b = null;
				if (PopConstant.DB_TYPE_DB2.equals(dbType)) {
					b = new StringBuffer();
					b.append("alter table ").append(tabName).append(" activate NOT LOGGED INITIALLY WITH EMPTY TABLE");
					jdbcTemplate.execute(b.toString());
					//jdbcTemplate.execute("alter table " + tabName + " activate NOT LOGGED INITIALLY");
				}  else {
					b = new StringBuffer("delete from ").append(tabName);
					jdbcTemplate.execute(b.toString());
				}
			} catch (Exception e) {
				 log.error("Clear data of {} error:", tabName, e);
				 throw new PopException(e.getMessage());
			}
		}
	}
	
	/**
	 * 清理表数据
	 * @param tabName
	 * @throws Exception
	 */
	public static void clearTabDataForSql(String sql) throws Exception {
		JdbcTemplate jdbcTemplate = SpringContext.getBean("jdbcTemplate", JdbcTemplate.class);
		if (StringUtil.isNotEmpty(sql)) {
			try {
				jdbcTemplate.execute(sql);
			} catch (Exception e) {
				 log.error("Clear data of {} error:", e);
				 throw new PopException(e.getMessage());
			}
		}
	}
	
	
	/**
	 * @param activeCode
	 * @param column 字段，两个用逗号分隔
	 * @return 0：否；1：是；-1：无意义
	 */
	public static int loadFlagByActiveCode(String activityCode, String column) {
		JdbcTemplate jt = SpringContext.getBean("jdbcTemplate", JdbcTemplate.class);
		int flag = 0;
		try {
			if (StringUtil.isNotEmpty(column)) {
				List<Map> data = jt.queryForList("select " + column + " from POP_POLICY_RULE where id = ? and rule_status = ?",new Object[] { activityCode,PolicyStatus.SENDODER_USER_SUCCESS.getValue() });
				if (data != null && data.size() == 1) {
					Map<String, Object> dm = data.get(0);
					  if (column.equalsIgnoreCase("rule_status")) {//活动状态
						flag = 1;
						SimpleCache.getInstance().put(PopConstant.CACHE_KEY_ACTIVITY_STATUS + "_" + activityCode, flag,PopUtil.getRemainTime());
					}  
				} else {//设置该活动为无效活动（Pop中不存在此活动）
					flag = 0;
					SimpleCache.getInstance().put(PopConstant.CACHE_KEY_ACTIVITY_STATUS + "_" + activityCode, flag,PopUtil.getRemainTime());
				}
			}
		} catch (Exception e) {
			log.error("loadFlagByActiveCode[" + activityCode + "] error:", e);
		}
		return flag;
	}

	/**
	 * 根据活动编号获取免打扰客户类型
	 * @param actId
	 * @return
	 */
	public static String getAvoidBotherTypeIdsByActId(String actId) {
		String avoidBotherTypeIds = null;
		try {
			avoidBotherTypeIds = (String) SimpleCache.getInstance().get(PopConstant.CACHE_KEY_ACT_AVOID_BOTHER_TYPE_IDS + "_" + actId);
			if (StringUtil.isEmpty(avoidBotherTypeIds)) {
				JdbcTemplate jt = SpringContext.getBean("jdbcTemplate", JdbcTemplate.class);
				avoidBotherTypeIds = (String) jt.queryForObject(QUERY_AVOID_BOTHER_TYPE_IDS_SQL,
						new String[] { actId }, String.class);
				if (avoidBotherTypeIds == null) {
					avoidBotherTypeIds = "";
				}
				SimpleCache.getInstance().put(PopConstant.CACHE_KEY_ACT_AVOID_BOTHER_TYPE_IDS + "_" + actId,avoidBotherTypeIds, PopConstant.CACHE_TIME);
			}
		} catch (Exception e) {
			log.error("getAvoidBotherTypeIdsByActId(" + actId + ") error:", e);
		}
		return avoidBotherTypeIds;
	}
	
 
	/**
	 * 将Map转换为Bean对象
	 * @param data
	 * @param obj
	 * @throws Exception
	 */
	public static void mapToBean(Map<String, Object> data, Object obj) throws Exception {
		Field fields[] = obj.getClass().getDeclaredFields();
		for (int i = 0; i < fields.length; i++) {
			typeMapper(fields[i], obj, data.get(property2ColumnName(fields[i].getName())));
		}
	}

	/**
	 * 将ResultSet转换为Bean对象
	 * @param rs
	 * @param obj
	 * @throws Exception
	 */
	public static void resultSetToBean(ResultSet rs, Object obj) throws Exception {
		Field fields[] = obj.getClass().getDeclaredFields();
		for (int i = 0; i < fields.length; i++) {
			typeMapper(fields[i], obj, rs.getObject(property2ColumnName(fields[i].getName())));
		}
	}

	public static String property2ColumnName(String propertyName) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < propertyName.length(); i++) {
			char flag = propertyName.charAt(i);
			if (Character.isUpperCase(flag)) {
				sb.append("_").append(Character.toLowerCase(flag));
			} else {
				sb.append(flag);
			}
		}
		return sb.toString();
	}

	/**
	 * 通过反射将值设置到对象中
	 * @param field
	 * @param obj
	 * @param value
	 * @throws Exception
	 */
	public static void typeMapper(Field field, Object obj, Object value) throws Exception {
		if (value != null) {
			field.setAccessible(true);
			String type = field.getType().getName();
			if (type.equals("java.lang.String")) {
				field.set(obj, value.toString());
			} else if (type.equals("int") || type.equals("java.lang.Integer")) {
				field.set(obj, Integer.valueOf(value.toString()));
			} else if (type.equals("long") || type.equals("java.lang.Long")) {
				field.set(obj, Long.valueOf(value.toString()));
			} else if (type.equals("float") || type.equals("java.lang.Float")) {
				field.set(obj, Float.valueOf(value.toString()));
			} else if (type.equals("double") || type.equals("java.lang.Double")) {
				field.set(obj, Double.valueOf(value.toString()));
			} else if (type.equals("boolean") || type.equals("java.lang.Boolean")) {
				field.set(obj, Boolean.valueOf(value.toString()));
			} else if (type.equals("java.util.Date")) {
				field.set(obj, (java.util.Date) value);
			} else {
				field.set(obj, value);
			}
			field.setAccessible(false);
		}
	}
	
	/***
	 * 从当前开始算到凌晨12点时间
	 * */
	public static long getRemainTime() {
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(new Date());
		calendar.set(Calendar.HOUR, -12);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		calendar.add(calendar.DATE, 1);
		long tomorrow = calendar.getTimeInMillis();

		return tomorrow - System.currentTimeMillis();
	}
	
	public static String convertTac_Eci2UserLocation(String tac,String eci) throws Exception{
		return "130-46000" + addPrecursorZero(tac,5) + addPrecursorZero(eci,9);
	}
	
	public static String convertTacEci2UserLocation(String tacEci) throws Exception{
		return "130-46000" + tacEci;
	}
	
	/**
	 * 将LacCi转换为userLocation
	 * @param lacCi
	 * @return userlocation
	 */
	public static String convertLacCi2UserLocation(String lacCi) {
		//String lac = 
		return null;
	}
	
	/**
	 * 将数字字符串前面补0生成定长数字字符串
	 * @param value 目标数字字符串
	 * @param fixedLength 要生成的定长数字字符串的总长
	 * @return
	 */
	public static String addPrecursorZero(String value,int fixedLength )
			throws Exception{
		String format = "%0" + fixedLength + "d";
		String targetValue = value;
		try {
			targetValue = String.format(format, Integer.parseInt(value));
		} catch (Exception e) {
			throw new Exception("value不是数字字符串或者fixedLength小于等于0");
		}
		return targetValue;
	}
	
	/**
	 * 获取Esper实例的IP地址
	 * @return
	 * @throws UnknownHostException 
	 */
	public static String getEsperInstanceHostAddress() throws UnknownHostException {
		String localIp = Configure.getInstance().getProperty("LOCAL_IP");
		if (StringUtil.isEmpty(localIp)) {
			InetAddress addr = InetAddress.getLocalHost();
			localIp = addr.getHostAddress();
			if (NetFlags.LOOPBACK_ADDRESS.equals(localIp)) {
				Sigar sigar = new Sigar();
				try {
					localIp = sigar.getNetInterfaceConfig().getAddress();
				} catch (SigarException e) {
					log.error("获取本机IP地址异常：", e);
				} finally {
					sigar.close();
				}
			}
		}
		return localIp;
	}
 
	public static void main(String[] args) {
		//CepUtil.extractVariable("");
		try {
			//System.out.println(PopUtil.addPrecursorZero("53243", 2));
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

}
