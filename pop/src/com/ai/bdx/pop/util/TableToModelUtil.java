package com.ai.bdx.pop.util;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

/**
 * 数据库表转换成model对象小工具类
 * 
 * 1 bean属性按原始数据库字段经过去掉下划线,并大写处理首字母等等.
 * 2 生成的bean带了数据库的字段说明.
 * 
 * @author wanglei
*/
public class TableToModelUtil {

	private String tablename = "";
	private String tableComment = "";
	private List<String> colnames;
	private List<String> colComments;
	private List<Integer> colTypes;
	private List<String> colTypeNames;
	private List<Integer> colSizes; // 列名大小
	private List<Integer> colScale; // 列名小数精度

	String jdbcUrl = "jdbc:mysql://10.1.253.202:3306/mcddev";
	String userName = "mcd";
	String password = "mcd";
	String driveClass = "com.mysql.jdbc.Driver";

	/**
	 * Table to entity.
	 *
	 * @param tName the t name
	 * @throws ClassNotFoundException the class not found exception
	 * @throws SQLException the sQL exception
	 */
	public void tableToEntity(String tName) throws ClassNotFoundException, SQLException {
		tablename = tName;
		Class.forName(driveClass);
		Connection conn = DriverManager.getConnection(jdbcUrl, userName, password);
		ResultSet rs = null;
		ResultSet rs1 = null;
		try {
			DatabaseMetaData dmd = conn.getMetaData();
			rs = dmd.getTables("", "", tName, new String[] { "TABLE" });
			colnames = new ArrayList<String>();
			colComments = new ArrayList<String>();
			colTypes = new ArrayList<Integer>();
			colTypeNames = new ArrayList<String>();
			colSizes = new ArrayList<Integer>();
			colScale = new ArrayList<Integer>();
			while (rs.next()) {
				tableComment = rs.getString("REMARKS");
				rs1 = dmd.getColumns("", "", tName, "");
				while (rs1.next()) {
					colnames.add(rs1.getString("COLUMN_NAME").toLowerCase());
					colTypes.add(rs1.getInt("DATA_TYPE"));
					colTypeNames.add(rs1.getString("TYPE_NAME"));
					colComments.add(rs1.getString("REMARKS"));
					colSizes.add(rs1.getInt("COLUMN_SIZE"));
					colScale.add(rs1.getInt("DECIMAL_DIGITS"));
				}
				rs1.close();
			}

			String content = parse();
			try {
				FileWriter fw = new FileWriter("src/main/java/com/ai/bdx/pop/model/" + initcap(tablename) + ".java");
				PrintWriter pw = new PrintWriter(fw);
				pw.println(content);
				pw.flush();
				pw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) { /* ignored */
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) { /* ignored */
				}
			}
		}
	}

	/**
	 * 解析处理(生成实体类主体代码)
	 */
	private String parse() {
		StringBuffer sb = new StringBuffer();
		sb.append("\r\npackage com.ai.bdx.pop.model;\r\n");
		sb.append("\r\nimport com.ai.bdx.pop.base.PopModel;\r\n");

		//表注释
		processColnames(sb);

		sb.append("public class " + initcap(tablename) + " extends PopModel<" + initcap(tablename) + "> {\r\n");
		processAllAttrs(sb);
		sb.append("\tpublic static " + initcap(tablename) + " dao(){\r\n");
		sb.append("\t\treturn new " + initcap(tablename) + "();\r\n");
		sb.append("\t}\r\n");

		processAllMethod(sb);
		sb.append("}\r\n");
		//System.out.println(sb.toString());
		return sb.toString();

	}

	private boolean isNotEmpty(String obj) {
		return obj != null && obj.trim().length() > 0;
	}

	/**
	 * 处理列名,同时把数据库列名,列类型写到注释中以便查看,
	 * @param sb
	 */
	private void processColnames(StringBuffer sb) {
		sb.append("\r\n/** " + tablename.toLowerCase()).append(isNotEmpty(tableComment) ? "	--" + tableComment : "")
				.append("\r\n");
		String colsiz = "";
		for (int i = 0; i < colnames.size(); i++) {
			colsiz = null == colSizes.get(i) || colSizes.get(i).intValue() <= 0 ? "" : (null == colScale.get(i)
					|| colScale.get(i) <= 0 ? "(" + colSizes.get(i).intValue() + ")" : "(" + colSizes.get(i).intValue()
					+ "," + colScale.get(i).intValue() + ")");
			sb.append("\t" + colnames.get(i).toLowerCase() + "	" + colTypeNames.get(i).toLowerCase() + colsiz)
					.append(isNotEmpty(colComments.get(i)) ? "	--" + colComments.get(i) : "").append("\r\n");
			char[] ch = colnames.get(i).toCharArray();
			char c = 'a';
			if (ch.length > 3) {
				for (int j = 0; j < ch.length; j++) {
					c = ch[j];
					if (c == '_') {
						if (ch[j + 1] >= 'a' && ch[j + 1] <= 'z') {
							ch[j + 1] = (char) (ch[j + 1] - 32);
						}
					}
				}
			}
		}
		sb.append("*/\r\n");
	}

	/**
	 * 生成所有的方法
	 * 
	 * @param sb
	 */
	private void processAllMethod(StringBuffer sb) {
		for (int i = 0; i < colnames.size(); i++) {
			sb.append("\tpublic static final String " + "COL_" + colnames.get(i).toUpperCase() + " = " + "\""
					+ colnames.get(i).toLowerCase() + "\";\r\n");
		}
	}

	/**
	 * 解析输出属性
	 * 
	 * @return
	 */
	private void processAllAttrs(StringBuffer sb) {
		sb.append("\tprivate static final long serialVersionUID = " + System.currentTimeMillis() + "L;\r\n");
		//		for (int i = 0; i < colnames.length; i++) {
		//			sb.append("\tprivate " + sqlType2JavaType(colTypes[i]) + " "
		//					+ colnames[i] + ";\r\n");
		//		}
		sb.append("\r\n");
	}

	/**
	 * 把输入字符串的首字母改成大写
	 * @param str
	 * @return
	 */
	private String initcap(String str) {
		char[] ch = str.toCharArray();
		ch[0] = Character.toUpperCase(ch[0]);

		int appear = -1;
		for (int i = 0; i < ch.length; i++) {
			if (ch[i] == '_') {
				appear = (i + 1);
			}
			if (i == appear || i == 0) {
				ch[i] = Character.toUpperCase(ch[i]);
			} else {
				ch[i] = Character.toLowerCase(ch[i]);
			}
		}
		String res = new String(ch);
		res = res.replace("_", "");

		return res;
	}

	/**
	 * 
	 * @param sqlType
	 * @param scale
	 * @return
	 */
	@SuppressWarnings("unused")
	private String sqlType2JavaType(int sqlType) {
		if (sqlType == Types.VARCHAR || sqlType == Types.LONGVARCHAR || sqlType == Types.CLOB || sqlType == Types.NCHAR
				|| sqlType == Types.NVARCHAR || sqlType == Types.LONGNVARCHAR || sqlType == Types.LONGVARCHAR
				|| sqlType == Types.CHAR) {// STRING
			return "String";
		} else if (sqlType == Types.INTEGER || sqlType == Types.BIT || sqlType == Types.TINYINT
				|| sqlType == Types.BIGINT) {// INT
			return "int";
		} else if (sqlType == Types.DATE || sqlType == Types.TIME || sqlType == Types.TIMESTAMP
				|| sqlType == Types.NUMERIC) {// DATE
			return "Date";
		} else if (sqlType == Types.VARBINARY || sqlType == Types.BINARY || sqlType == Types.BLOB) {
			return "byte[]";
		} else if (sqlType == Types.BOOLEAN) {
			return "boolean";
		} else if (sqlType == Types.FLOAT) {
			return "float";
		} else if (sqlType == Types.DOUBLE) {
			return "double";
		} else {
			return null;
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		TableToModelUtil t = new TableToModelUtil();
		try {
			String tables = "pop_interface_config,pop_interface_log,pop_policy_rule,pop_policy_rule_act,pop_policy_rule_custgroup,pop_policy_rule_event_con,pop_policy_rule_exec_time,pop_user_policy_rule_data";
			for (String table : tables.split(",")) {
				t.tableToEntity(table);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
