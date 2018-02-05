// Decompiled by DJ v3.11.11.95 Copyright 2009 Atanas Neshkov  Date: 2010-8-17 11:07:25
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3)
// Source File Name:   Sqlca.java

package com.asiainfo.biframe.utils.database.jdbc;

import java.io.BufferedReader;
import java.math.BigDecimal;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.asiainfo.biframe.utils.config.Configure;
import com.asiainfo.biframe.utils.string.DES;

// Referenced classes of package com.asiainfo.biframe.utils.database.jdbc:
//            ConnectionEx

public class Sqlca {

	private Sqlca() {
		unicodeToGB = true;
		gbToUnicode = true;
		sqlRows = 0;
		connection = null;
		connectionEx = null;
		strSqlType = "UNKNOWN";
		strSQL = null;
		lastNullFlag = false;
		statement = null;
		strDBMS = null;
		batchStatement = null;
		sqlCode = 0;
		sqlResultSet = null;
		sqlNotice = null;
		preStat = null;
		sqlAutoRollback = false;
		sqlAutoCommit = false;
	}

	public Sqlca(Connection newConnection) throws Exception {
		this();
		connection = newConnection;
		try {
			if (connection != null) {
				setAutoCommit(sqlAutoCommit); // NOPMD by Administrator on 11-8-26 8:54
			}
			if (connection != null && connection.getTransactionIsolation() < 1) {
				connection.setTransactionIsolation(2);
			}
		} catch (Exception e) {
			throw e;
		}
		gbToUnicode = Boolean.valueOf(Configure.getInstance().getProperty("KPI_DB_CHARSET")).booleanValue();
		unicodeToGB = Boolean.valueOf(Configure.getInstance().getProperty("KPI_DB_CHARSET")).booleanValue();
	}

	public Sqlca(ConnectionEx conn) throws Exception {
		this();
		connection = conn.getConnection();
		connectionEx = conn;
		try {
			if (connection != null) {
				setAutoCommit(sqlAutoCommit); // NOPMD by Administrator on 11-8-26 8:54
			}
			if (connection != null) {
				connection.setTransactionIsolation(2);
			}
		} catch (Exception e) {
			throw e;
		}
		gbToUnicode = Boolean.valueOf(Configure.getInstance().getProperty("KPI_DB_CHARSET")).booleanValue();
		unicodeToGB = Boolean.valueOf(Configure.getInstance().getProperty("KPI_DB_CHARSET")).booleanValue();
	}

	public Sqlca(ConnectionEx conn, boolean bGBToUnicode, boolean bUnicodeToGB) throws Exception {
		this();
		connection = conn.getConnection();
		connectionEx = conn;
		try {
			if (connection != null) {
				setAutoCommit(sqlAutoCommit); // NOPMD by Administrator on 11-8-26 8:54
			}
			if (connection != null) {
				connection.setTransactionIsolation(2);
			}
		} catch (Exception e) {
			throw e;
		}
		gbToUnicode = bGBToUnicode;
		unicodeToGB = bUnicodeToGB;
	}

	public void setAutoCommit(boolean bAuto) throws Exception {
		sqlAutoCommit = bAuto;
		if (connection == null) {
			Failure("did not get a connection to the database", 0);
		}
		try {
			connection.setAutoCommit(sqlAutoCommit);
		} catch (SQLException e) {
			sqlFailure("automatic commit of property failed!", e, 0);
		}
	}

	public void setAutoRollback(boolean autoRollbackWhensqlFailure) throws Exception {
		sqlAutoRollback = autoRollbackWhensqlFailure;
	}

	public void setConnection(Connection newConnection) throws Exception {
		connection = newConnection;
	}

	public void setUnicodeToGB(boolean bFlag) {
		unicodeToGB = bFlag;
	}

	public void setGBToUnicode(boolean bFlag) {
		gbToUnicode = bFlag;
	}

	public Connection getConnection() {
		return connection;
	}

	public void setSql(String sqlStr) // NOPMD
			throws Exception {
		if (connection == null) {
			Failure("did not get a connection to the database!", 0);
		}
		if (sqlStr == null || sqlStr.trim().length() < 1) {
			Failure("sql statement is empty!", 0);
		}
		strSQL = sqlStr;
		sqlCode = 0;
		sqlRows = 0;
		sqlNotice = null;
		sqlStr = sqlStr.trim();
		strSqlType = sqlStr.substring(0, 4).toUpperCase();
		if (strSqlType.equals("SELE")) {
			strSqlType = "SELECT";
		} else if (strSqlType.equals("WITH")) {
			strSqlType = "SELECT";
		} else if (strSqlType.equals("DELE")) {
			strSqlType = "DELETE";
		} else if (strSqlType.equals("UPDA")) {
			strSqlType = "UPDATE";
		} else if (strSqlType.equals("INSE")) {
			strSqlType = "INSERT";
		} else {
			strSqlType = "UNKNOWN";
		}
		close();
		openCount++;
		String strType = getDBMSType();
		if (strType.equalsIgnoreCase("SQLSERVER")) {
			connection.setAutoCommit(true);
		}
		statement = connection.createStatement();
	}

	protected int execute() throws Exception {
		sqlRows = 0;
		if (statement == null) {
			sqlRows = -1;
			Failure("there is no enforceable SQL!", 0);
		}
		try {
			if (strSqlType.equals("SELECT")) {
				sqlResultSet = statement.executeQuery(strSQL);
			} else {
				sqlRows = statement.executeUpdate(strSQL);
			}
		} catch (SQLException e) {
			sqlRows = -1;
			sqlFailure((new StringBuilder()).append("execute SQL:\n  ").append(strSQL).append(" error!").toString(), e,
					0);
			return -1;
		}
		return sqlRows;
	}

	/**
	 * 
	 * execute:执行sql不带参数sql
	 * @param strSQL
	 * @throws Exception 
	 * @return int
	 */

	public int execute(String strSQL) // NOPMD
			throws Exception {
		if (unicodeToGB) {
			strSQL = unicodeToGB(strSQL);
		}
		setSql(strSQL);
		return execute();
	}

	public void addBatch(String sql) throws Exception {
		try {
			if (batchStatement == null) {
				batchStatement = connection.createStatement();
			}
			batchStatement.addBatch(sql);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw e;
		}
	}

	public int[] executeBatch() throws Exception {
		try {
			if (batchStatement == null) {
				return null;
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw e;
		}
		return batchStatement.executeBatch();
	}

	public int getSqlRows() {
		return sqlRows;
	}

	protected void sqlFailure(String notice, SQLException e, int flag) throws Exception {
		sqlCode = -e.getErrorCode();
		sqlNotice = (new StringBuilder()).append("sql statement").append(strSQL).append("\n").append(notice)
				.append("\n").append("  error code :").append((Integer.valueOf(sqlCode)).toString()).append(";\n")
				.append("  error message:").append(GBToUnicode(e.getMessage())).append("\n").toString(); // NOPMD
		throw new Exception((new StringBuilder()).append("SQL Error:").append(sqlNotice)
				.append(GBToUnicode((new StringBuilder()).append(notice).append(e.getMessage()).toString())).toString());
	}

	public void closeAll() {
		try {
			close();
			if (batchStatement != null) {
				batchStatement.close();
				batchStatement = null;
			}
			if (null != connectionEx) {
				if (!connectionEx.isClosed()) {
					connectionEx.close();
				}
				connectionEx = null;
			} else if (null != connection) {
				if (!connection.isClosed()) {
					connection.close();
				}
				connection = null;
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}

	public void close() {
		try {
			this.commit();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}

		sqlNotice = null;
		try {
			if (sqlResultSet != null) {
				sqlResultSet.close();
				sqlResultSet = null;
			}
			if (statement != null) {
				openCount--;
				statement.close();
				statement = null;
			}
			if (preStat != null) {
				openCount--;
				preStat.close();
				preStat = null;
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}

	private void Failure(String notice, int flag) // NOPMD by Administrator on 11-8-26 8:54
			throws Exception {
		sqlCode = 50;
		sqlNotice = notice;
		if (flag == 1 && sqlAutoRollback && !strSqlType.equals("SELECT")) {
			try {
				connection.rollback();
			} catch (SQLException e1) {
			}
		}
		throw new Exception(sqlNotice);
	}

	private String unicodeToGB(String strIn) {
		if (!unicodeToGB) {
			return strIn;
		}
		String strOut = null;
		if (strIn == null || strIn.trim().equals("")) {
			return strIn;
		}
		try {
			byte b[] = strIn.getBytes("GBK");
			strOut = new String(b, "ISO8859_1");
		} catch (Exception e) {
			strOut = strIn;
		}
		return strOut;
	}

	private String GBToUnicode(String strIn) // NOPMD
	{
		if (!gbToUnicode) {
			return strIn;
		}
		String strOut = null;
		if (strIn == null || strIn.trim().equals("")) {
			return strIn;
		}
		try {
			byte b[] = strIn.getBytes("ISO8859_1");
			strOut = new String(b, "GBK");
		} catch (Exception e) {
			strOut = strIn;
		}
		return strOut;
	}

	public ResultSetMetaData getMetaData() throws Exception {
		return sqlResultSet.getMetaData();
	}

	public boolean next() throws Exception {
		return sqlResultSet.next();
	}

	public void commit() throws Exception {
		try {
			if (connection != null && !connection.isClosed()) {
				connection.commit();
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}

	public void rollback() throws Exception {
		connection.rollback();
	}

	public void setDBMSType(String dbType) {
		strDBMS = dbType;
	}

	public String getDBMSType() throws Exception {
		if (null == strDBMS) {
			DatabaseMetaData m = connection.getMetaData();
			strDBMS = m.getDatabaseProductName();
			strDBMS = strDBMS.toUpperCase();
			if (strDBMS.indexOf("MYSQL") >= 0) {
				strDBMS = "MYSQL";
			} else if (strDBMS.indexOf("ORACLE") >= 0) {
				strDBMS = "ORACLE";
			} else if (strDBMS.indexOf("POSTGRESQL") >= 0) {
				strDBMS = "POSTGRESQL";
			} else if (strDBMS.indexOf("ACCESS") >= 0) {
				strDBMS = "ACESS";
			} else if (strDBMS.indexOf("SQL SERVER") >= 0) {
				strDBMS = "SQLSERVER";
			} else if (strDBMS.indexOf("DB2") >= 0) {
				strDBMS = "DB2";
			} else if (strDBMS.indexOf("TERA") >= 0) {
				strDBMS = "TERA";
			} else if (strDBMS.indexOf("SYBASE") >= 0) {
				strDBMS = "SYBASE";
			} else if (strDBMS.indexOf("ADAPTIVE") >= 0) {
				strDBMS = "SYBASE";
			} else if (strDBMS.indexOf("GBASE") >= 0) {
				strDBMS = "MYSQL";
			} else {
				throw new Exception("does not support the database type!");
			}
		}
		return strDBMS.toUpperCase();
	}

	public String getString(int columnIndex) throws Exception {
		String retValue = null;
		if (sqlResultSet == null) {
			throw new Exception((new StringBuilder()).append("take the first ").append(columnIndex)
					.append("value is wrong! because the result set to a null value").toString());
		}
		try {
			retValue = sqlResultSet.getString(columnIndex);
			lastNullFlag = sqlResultSet.wasNull();
		} catch (SQLException e) {
			sqlFailure((new StringBuilder()).append("take the first ").append(columnIndex).append("value is wrong!")
					.toString(), e, 0);
		}
		if (lastNullFlag || null == retValue) {
			retValue = "";
		} else {
			retValue = retValue.trim();
		}
		return GBToUnicode(retValue);
	}

	public String getString(String columnName) throws Exception {
		String retValue = null;
		if (sqlResultSet == null) {
			throw new Exception((new StringBuilder()).append("take out ").append(columnName)
					.append("value is wrong! because the result set to a null value").toString());
		}
		try {
			retValue = sqlResultSet.getString(columnName);
			lastNullFlag = sqlResultSet.wasNull();
		} catch (SQLException e) {
			sqlFailure((new StringBuilder()).append("take out ").append(columnName).append(" value is wrong!")
					.toString(), e, 0);
		}
		if (lastNullFlag || null == retValue) {
			retValue = "";
		} else {
			retValue = retValue.trim();
		}
		return GBToUnicode(retValue);
	}

	public String getClob(int columnIndex) throws Exception {
		String strRet = null;
		Clob retValue = null;
		if (sqlResultSet == null) {
			throw new Exception((new StringBuilder()).append("take the first ").append(columnIndex)
					.append("value is wrong! because the result set to a null value").toString());
		}
		try {
			retValue = sqlResultSet.getClob(columnIndex);
			lastNullFlag = sqlResultSet.wasNull();
		} catch (SQLException e) {
			sqlFailure((new StringBuilder()).append("take the first ").append(columnIndex).append("value is wrong!")
					.toString(), e, 0);
		}
		if (lastNullFlag || null == retValue) {
			strRet = "";
		} else {
			strRet = clobToString(retValue);
		}
		return GBToUnicode(strRet);
	}

	public String getClob(String columnName) throws Exception {
		String strRet = null;
		Clob retValue = null;
		if (sqlResultSet == null) {
			throw new Exception((new StringBuilder()).append("take out ").append(columnName)
					.append("value is wrong! because the result set to a null value").toString());
		}
		try {
			retValue = sqlResultSet.getClob(columnName);
			lastNullFlag = sqlResultSet.wasNull();
		} catch (SQLException e) {
			sqlFailure((new StringBuilder()).append("take out ").append(columnName).append("value is wrong!")
					.toString(), e, 0);
		}
		if (lastNullFlag || null == retValue) {
			strRet = "";
		} else {
			strRet = clobToString(retValue);
		}
		return GBToUnicode(strRet);
	}

	private String clobToString(Clob clob) throws Exception {
		StringBuffer content = new StringBuffer();
		try {
			String s = "";
			BufferedReader br = new BufferedReader(clob.getCharacterStream());
			while ((s = br.readLine()) != null) {
				content.append(s);
			}
			return content.toString();
		} catch (Exception e) {
			throw new Exception("-------get value failed--------");
		}
	}

	public int getInt(int columnIndex) throws Exception {
		int retValue = -1;
		if (sqlResultSet == null) {
			throw new Exception((new StringBuilder()).append("take the first ").append(columnIndex)
					.append("value is wrong! because the result set to a null value").toString());
		}
		try {
			retValue = sqlResultSet.getInt(columnIndex);
			lastNullFlag = sqlResultSet.wasNull();
		} catch (SQLException e) {
			sqlFailure((new StringBuilder()).append("take the first ").append(columnIndex).append(" value is wrong!")
					.toString(), e, 0);
		}
		return retValue;
	}

	public int getInt(String columnName) throws Exception {
		if (sqlResultSet == null) {
			throw new Exception((new StringBuilder()).append("take out ").append(columnName)
					.append("value is wrong! because the result set to a null value").toString());
		}
		int retValue;
		try {
			retValue = sqlResultSet.getInt(columnName);
			lastNullFlag = false;
			if (sqlResultSet.wasNull()) {
				lastNullFlag = true;
			}
		} catch (SQLException e) {
			sqlFailure((new StringBuilder()).append("take out ").append(columnName).append(" value error!").toString(),
					e, 0);
			return 0;
		}
		return retValue;
	}

	public Integer getInteger(int columnIndex) throws Exception {
		Integer retValue = null;
		if (sqlResultSet == null) {
			throw new Exception((new StringBuilder()).append("take the first ").append(columnIndex)
					.append("value is wrong! because the result set to a null value").toString());
		}
		try {
			int tempVar = sqlResultSet.getInt(columnIndex);
			lastNullFlag = false;
			if (sqlResultSet.wasNull()) {
				lastNullFlag = true;
				retValue = null;
			} else {
				retValue = Integer.valueOf(tempVar); // NOPMD
			}
		} catch (SQLException e) {
			sqlFailure((new StringBuilder()).append("take the first ").append(columnIndex).append(" value error!")
					.toString(), e, 0);
		}
		return retValue;
	}

	public Integer getInteger(String columnName) throws Exception {
		Integer retValue = null;
		if (sqlResultSet == null) {
			throw new Exception((new StringBuilder()).append("take out ").append(columnName)
					.append("value is wrong! because the result set to a null value").toString());
		}
		try {
			int tempVar = sqlResultSet.getInt(columnName);
			lastNullFlag = false;
			if (sqlResultSet.wasNull()) {
				lastNullFlag = true;
				retValue = null;
			} else {
				retValue = Integer.valueOf(tempVar); // NOPMD
			}
		} catch (SQLException e) {
			sqlFailure((new StringBuilder()).append("take out ").append(columnName).append(" value error!").toString(),
					e, 0);
		}
		return retValue;
	}

	public long getLong(int columnIndex) throws Exception {
		long retValue = 0L;
		if (sqlResultSet == null) {
			throw new Exception((new StringBuilder()).append("take the first ").append(columnIndex)
					.append("value is wrong! because the result set to a null value").toString());
		}
		try {
			retValue = sqlResultSet.getLong(columnIndex);
			lastNullFlag = false;
			if (sqlResultSet.wasNull()) {
				lastNullFlag = true;
			}
		} catch (SQLException e) {
			sqlFailure((new StringBuilder()).append("take the first ").append(columnIndex).append(" value error!")
					.toString(), e, 0);
			return 0L;
		}
		return retValue;
	}

	public long getLong(String columnName) throws Exception {
		if (sqlResultSet == null) {
			throw new Exception((new StringBuilder()).append("take out ").append(columnName)
					.append("value is wrong! because the result set to a null value").toString());
		}
		long retValue;
		try {
			retValue = sqlResultSet.getLong(columnName);
			lastNullFlag = false;
			if (sqlResultSet.wasNull()) {
				lastNullFlag = true;
			}
		} catch (SQLException e) {
			sqlFailure((new StringBuilder()).append("take out ").append(columnName).append(" value error!").toString(),
					e, 0);
			return 0L;
		}
		return retValue;
	}

	public double getDouble(int columnIndex) throws Exception {
		double retValue = 0.0D;
		if (sqlResultSet == null) {
			throw new Exception((new StringBuilder()).append("take the first ").append(columnIndex)
					.append("value is wrong! because the result set to a null value").toString());
		}
		try {
			retValue = sqlResultSet.getDouble(columnIndex);
			lastNullFlag = false;
			if (sqlResultSet.wasNull()) {
				lastNullFlag = true;
			}
		} catch (SQLException e) {
			sqlFailure((new StringBuilder()).append("take the first ").append(columnIndex).append(" value error!")
					.toString(), e, 0);
		}
		return retValue;
	}

	public double getDouble(String columnName) throws Exception {
		if (sqlResultSet == null) {
			throw new Exception((new StringBuilder()).append("take out ").append(columnName)
					.append("value is wrong! because the result set to a null value").toString());
		}
		double retValue;
		try {
			retValue = sqlResultSet.getDouble(columnName);
			lastNullFlag = false;
			if (sqlResultSet.wasNull()) {
				lastNullFlag = true;
			}
		} catch (SQLException e) {
			sqlFailure((new StringBuilder()).append("take out ").append(columnName).append(" value error!").toString(),
					e, 0);
			return 0.0D;
		}
		return retValue;
	}

	public boolean getBoolean(int columnIndex) throws Exception {
		boolean retValue = false;
		if (sqlResultSet == null) {
			throw new Exception((new StringBuilder()).append("take the first ").append(columnIndex)
					.append("value is wrong! because the result set to a null value").toString());
		}
		try {
			retValue = sqlResultSet.getBoolean(columnIndex);
			lastNullFlag = false;
			if (sqlResultSet.wasNull()) {
				lastNullFlag = true;
			}
		} catch (SQLException e) {
			sqlFailure((new StringBuilder()).append("take the first ").append(columnIndex).append(" value error!")
					.toString(), e, 0);
		}
		return retValue;
	}

	public boolean getBoolean(String columnName) throws Exception {
		boolean retValue = false;
		if (sqlResultSet == null) {
			throw new Exception((new StringBuilder()).append("take out ").append(columnName)
					.append("value is wrong! because the result set to a null value").toString());
		}
		try {
			retValue = sqlResultSet.getBoolean(columnName);
			lastNullFlag = false;
			if (sqlResultSet.wasNull()) {
				lastNullFlag = true;
			}
		} catch (SQLException e) {
			sqlFailure((new StringBuilder()).append("take out ").append(columnName).append(" value error!").toString(),
					e, 0);
		}
		return retValue;
	}

	public Date getDate(int columnIndex) throws Exception {
		Date retValue = null;
		if (sqlResultSet == null) {
			throw new Exception((new StringBuilder()).append("take the first ").append(columnIndex)
					.append("value is wrong! because the result set to a null value").toString());
		}
		try {
			Timestamp tmp = sqlResultSet.getTimestamp(columnIndex);
			if (tmp != null) {
				retValue = new Date(tmp.getTime());
			} else {
				retValue = null;
			}
			lastNullFlag = false;
			if (sqlResultSet.wasNull()) {
				retValue = null;
				lastNullFlag = true;
			}
		} catch (SQLException e) {
			sqlFailure((new StringBuilder()).append("take the first ").append(columnIndex).append(" value error!")
					.toString(), e, 0);
		}
		return retValue;
	}

	public Date getDate(String columnName) throws Exception {
		Date retValue = null;
		if (sqlResultSet == null) {
			throw new Exception((new StringBuilder()).append("take out ").append(columnName)
					.append("value is wrong! because the result set to a null value").toString());
		}
		try {
			Timestamp tmp = sqlResultSet.getTimestamp(columnName);
			if (tmp != null) {
				retValue = new Date(tmp.getTime());
			} else {
				retValue = null;
			}
			lastNullFlag = false;
			if (sqlResultSet.wasNull()) {
				lastNullFlag = true;
			}
		} catch (SQLException e) {
			sqlFailure((new StringBuilder()).append("take out ").append(columnName).append(" value error!").toString(),
					e, 0);
		}
		return retValue;
	}

	public Timestamp getTimestamp(int columnIndex) throws Exception {
		Timestamp retValue = null;
		if (sqlResultSet == null) {
			throw new Exception((new StringBuilder()).append("take the first ").append(columnIndex)
					.append("value is wrong! because the result set to a null value").toString());
		}
		try {
			retValue = sqlResultSet.getTimestamp(columnIndex);
			lastNullFlag = false;
			if (sqlResultSet.wasNull()) {
				retValue = null;
				lastNullFlag = true;
			}
		} catch (SQLException e) {
			sqlFailure((new StringBuilder()).append("take the first ").append(columnIndex).append(" value error!")
					.toString(), e, 0);
		}
		return retValue;
	}

	public Timestamp getTimestamp(String columnName) throws Exception {
		Timestamp retValue = null;
		if (sqlResultSet == null) {
			throw new Exception((new StringBuilder()).append("take out ").append(columnName)
					.append("value is wrong! because the result set to a null value").toString());
		}
		try {
			retValue = sqlResultSet.getTimestamp(columnName);
			lastNullFlag = false;
			if (sqlResultSet.wasNull()) {
				retValue = null;
				lastNullFlag = true;
			}
		} catch (SQLException e) {
			sqlFailure((new StringBuilder()).append("take the first ").append(columnName).append(" value error!")
					.toString(), e, 0);
		}
		return retValue;
	}

	public float getFloat(int columnIndex) throws Exception {
		if (sqlResultSet == null) {
			throw new Exception((new StringBuilder()).append("take the first ").append(columnIndex)
					.append("value is wrong! because the result set to a null value").toString());
		}
		float retValue;
		try {
			retValue = sqlResultSet.getFloat(columnIndex);
			lastNullFlag = false;
			if (sqlResultSet.wasNull()) {
				lastNullFlag = true;
			}
		} catch (SQLException e) {
			sqlFailure((new StringBuilder()).append("take the first ").append(columnIndex).append(" value error!")
					.toString(), e, 0);
			return 0.0F;
		}
		return retValue;
	}

	public float getFloat(String columnName) throws Exception {
		if (sqlResultSet == null) {
			throw new Exception((new StringBuilder()).append("take out ").append(columnName)
					.append("value is wrong! because the result set to a null value").toString());
		}
		float retValue;
		try {
			retValue = sqlResultSet.getFloat(columnName);
			lastNullFlag = false;
			if (sqlResultSet.wasNull()) {
				lastNullFlag = true;
			}
		} catch (SQLException e) {
			sqlFailure(
					(new StringBuilder()).append("take out ").append(columnName).append(" value error!!").toString(),
					e, 0);
			return 0.0F;
		}
		return retValue;
	}

	public BigDecimal getBigDecimal(int columnIndex) throws Exception {
		if (sqlResultSet == null) {
			throw new Exception((new StringBuilder()).append("take the first ").append(columnIndex)
					.append("value is wrong! because the result set to a null value").toString());
		}
		BigDecimal retValue = sqlResultSet.getBigDecimal(columnIndex);
		lastNullFlag = false;
		if (sqlResultSet.wasNull()) {
			retValue = null;
			lastNullFlag = true;
		}
		return retValue;
	}

	public BigDecimal getBigDecimal(String columnName) throws Exception {
		if (sqlResultSet == null) {
			throw new Exception((new StringBuilder()).append("take out ").append(columnName)
					.append("value is wrong! because the result set to a null value").toString());
		}
		BigDecimal retValue = sqlResultSet.getBigDecimal(columnName);
		lastNullFlag = false;
		if (sqlResultSet.wasNull()) {
			retValue = null;
			lastNullFlag = true;
		}
		return retValue;
	}

	/**
	 * @deprecated Method getMatrix is deprecated
	 */

	@Deprecated
	public String[][] getMatrix() {
		if (sqlResultSet == null) {
			return null;
		}
		int colnum = 0;
		Vector table = new Vector();
		try {
			colnum = sqlResultSet.getMetaData().getColumnCount();
			String row[];
			for (; next(); table.addElement(row)) {
				row = new String[colnum];
				for (int i = 0; i < colnum; i++) {
					row[i] = getString(i + 1);
				}

			}

		} catch (Exception ex) {
			System.err.println((new StringBuilder()).append("Execute err : ").append(ex.getMessage()).toString()); // NOPMD by Administrator on 11-8-26 8:56
			return null;
		}
		String matrix[][] = new String[table.size() + 1][colnum];
		for (int col = 0; col < colnum; col++) {
			matrix[0][col] = (new StringBuilder()).append("Filed").append(col).toString();
		}

		for (int rowl = 1; rowl < matrix.length; rowl++) {
			matrix[rowl] = (String[]) table.elementAt(rowl - 1);
		}

		return matrix;
	}

	public String getSql2TimeStamp(String strDateStr, String strH, String strM, String strS) throws Exception {
		if (null == strDateStr || strDateStr.length() < 1) {
			return null;
		}
		if (strDateStr.indexOf("0000-00-00") >= 0) {
			return "null";
		}
		String strType = getDBMSType();
		String strRet = "";
		if (strType.equalsIgnoreCase("MYSQL")) {
			strRet = (new StringBuilder()).append("'").append(strDateStr).append(" ").append(strH).append(":")
					.append(strM).append(":").append(strS).append("'").toString();
		} else if (strType.equalsIgnoreCase("DB2")) {
			strRet = (new StringBuilder()).append("timestamp('").append(strDateStr).append(" ").append(strH)
					.append(":").append(strM).append(":").append(strS).append("')").toString();
		} else if (strType.equalsIgnoreCase("ORACLE") || strType.equalsIgnoreCase("POSTGRESQL")) {
			strRet = (new StringBuilder()).append("to_date('").append(strDateStr).append(" ").append(strH).append(":")
					.append(strM).append(":").append(strS).append("','YYYY-mm-dd hh24:mi:ss')").toString();
		} else if (strType.equalsIgnoreCase("ACESS")) {
			strRet = (new StringBuilder()).append("'").append(strDateStr).append("'").toString();
		} else if (strType.equalsIgnoreCase("SQLSERVER")) {
			strRet = (new StringBuilder()).append("CONVERT(Datetime,'").append(strDateStr).append(" ").append(strH)
					.append(":").append(strM).append(":").append(strS).append("',20)").toString();
		} else if (strType.equalsIgnoreCase("TERA")) {
			strRet = (new StringBuilder()).append(strDateStr).append(" (FORMAT 'YYYY-MM-DD')").toString();
		} else if (strType.equalsIgnoreCase("SYBASE")) {
			strRet = (new StringBuilder()).append("cast('").append(strDateStr).append(" ").append(strH).append(":")
					.append(strM).append(":").append(strS).append("' as Datetime)").toString();
		} else {
			throw new Exception("can't get the current date of the function definition");
		}
		return strRet;
	}

	public String getSql2TimeStamp(Date date) throws Exception {
		SimpleDateFormat dFormat = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat dFormat2 = new SimpleDateFormat("HH");
		SimpleDateFormat dFormat3 = new SimpleDateFormat("mm");
		SimpleDateFormat dFormat4 = new SimpleDateFormat("ss");
		return getSql2TimeStamp(dFormat.format(date), dFormat2.format(date), dFormat3.format(date),
				dFormat4.format(date));
	}

	public String getSql2Date(String strDateStr, String splitStr) // NOPMD
			throws Exception {
		if (null == strDateStr || strDateStr.length() < 1) {
			return null;
		}
		if (strDateStr.indexOf("0000-00-00") >= 0) {
			return "null";
		}
		String strType = getDBMSType();
		String strRet = "";
		int i = strDateStr.indexOf(" ");
		if (i > 0) {
			strDateStr = strDateStr.substring(0, i);
		}
		if (strType.equalsIgnoreCase("MYSQL")) {
			strRet = (new StringBuilder()).append("'").append(strDateStr).append("'").toString();
		} else if (strType.equalsIgnoreCase("DB2")) {
			strRet = (new StringBuilder()).append("date('").append(strDateStr).append("')").toString();
		} else if (strType.equalsIgnoreCase("ORACLE") || strType.equalsIgnoreCase("POSTGRESQL")) {
			if ("-".equals(splitStr)) {
				strRet = (new StringBuilder()).append("to_date('").append(strDateStr).append("','YYYY-mm-dd')")
						.toString();
			} else {
				strRet = (new StringBuilder()).append("to_date('").append(strDateStr).append("','YYYY/mm/dd')")
						.toString();
			}
		} else if (strType.equalsIgnoreCase("ACESS")) {
			strRet = (new StringBuilder()).append("'").append(strDateStr).append("'").toString();
		} else if (strType.equalsIgnoreCase("SQLSERVER")) {
			if ("-".equals(splitStr)) {
				strRet = (new StringBuilder()).append("convert(varchar(10), convert(datetime,'").append(strDateStr)
						.append("'), 111)").toString();
			} else {
				strRet = (new StringBuilder()).append("CONVERT(Datetime,'").append(strDateStr).append("',20)")
						.toString();
			}
		} else if (strType.equalsIgnoreCase("TERA")) {
			if ("-".equals(splitStr)) {
				strRet = (new StringBuilder()).append("cast('").append(strDateStr)
						.append("' as date FORMAT 'YYYY-MM-DD')").toString();
			} else {
				strRet = (new StringBuilder()).append("cast('").append(strDateStr)
						.append("' as date FORMAT 'YYYY/MM/DD')").toString();
			}
		} else if (strType.equalsIgnoreCase("SYBASE")) {
			strRet = (new StringBuilder()).append("cast('").append(strDateStr).append("' as Date)").toString();
		} else {
			throw new Exception("can't get the current date of the function definition");
		}
		return strRet;
	}

	public String getSql2DateYYYYMMDD(String strDateStr) // NOPMD
			throws Exception {
		if (null == strDateStr || strDateStr.length() < 1) {
			return null;
		}
		if (strDateStr.indexOf("000000") >= 0) {
			return "null";
		}
		String strType = getDBMSType();
		String strRet = "";
		int i = strDateStr.indexOf(" ");
		if (i > 0) {
			strDateStr = strDateStr.substring(0, i);
		}
		if (strType.equalsIgnoreCase("MYSQL")) {
			strRet = (new StringBuilder()).append("'").append(strDateStr).append("'").toString();
		} else if (strType.equalsIgnoreCase("DB2")) {
			strRet = (new StringBuilder()).append("date('").append(strDateStr).append("')").toString();
		} else if (strType.equalsIgnoreCase("ORACLE") || strType.equalsIgnoreCase("POSTGRESQL")) {
			strRet = (new StringBuilder()).append("to_date('").append(strDateStr).append("','YYYYmmdd')").toString();
		} else if (strType.equalsIgnoreCase("ACESS")) {
			strRet = (new StringBuilder()).append("'").append(strDateStr).append("'").toString();
		} else if (strType.equalsIgnoreCase("SQLSERVER")) {
			strRet = (new StringBuilder()).append("convert(varchar, convert(datetime, '").append(strDateStr)
					.append("'), 112)").toString();
		} else if (strType.equalsIgnoreCase("TERA")) {
			strRet = (new StringBuilder()).append(strDateStr).append(" (FORMAT 'YYYYMMDD')").toString();
		} else if (strType.equalsIgnoreCase("SYBASE")) {
			strRet = (new StringBuilder()).append("cast('").append(strDateStr).append("' as Date)").toString();
		} else {
			throw new Exception("can't get the current date of the function definition");
		}
		return strRet;
	}

	public String getSql2ColumnName(String colName) throws Exception {
		String strType = getDBMSType();
		String strRet = "";
		if (strType.equalsIgnoreCase("MYSQL")) {
			strRet = (new StringBuilder()).append("Date_Format(").append(colName).append(",'%Y-%m-%d %H:%i:%s')")
					.toString();
		} else if (strType.equalsIgnoreCase("ORACLE") || strType.equalsIgnoreCase("POSTGRESQL")) {
			strRet = (new StringBuilder()).append("to_char(").append(colName).append(",'YYYY-mm-dd hh24:mi:ss')")
					.toString();
		} else if (strType.equalsIgnoreCase("DB2")) {
			strRet = (new StringBuilder()).append("ts_fmt(").append(colName).append(",'yyyy-mm-dd hh:mi:ss')")
					.toString();
		} else if (strType.equalsIgnoreCase("ACESS")) {
			strRet = colName;
		} else if (strType.equalsIgnoreCase("SQLSERVER")) {
			strRet = (new StringBuilder()).append("CONVERT(Varchar,").append(colName).append(",120)").toString();
		} else if (strType.equalsIgnoreCase("TERA")) {
			strRet = (new StringBuilder()).append(colName).append(" (FORMAT 'YYYY-MM-DD')").toString();
		} else if (strType.equalsIgnoreCase("SYBASE")) {
			strRet = (new StringBuilder()).append("convert(char(10),").append(colName)
					.append(",23) || ' ' || convert(char(8),").append(colName).append(",108)").toString();
		} else {
			throw new Exception("can't get the current date of the function definition");
		}
		return strRet;
	}

	public String getSql2DateTimeNow() throws Exception {
		String strType = getDBMSType();
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

	public String getSql2DateNow() throws Exception {
		String strType = getDBMSType();
		String strRet = "";
		if (strType.equalsIgnoreCase("MYSQL")) {
			strRet = "now()";
		} else if (strType.equalsIgnoreCase("ORACLE")) {
			strRet = "sysdate";
		} else if (strType.equalsIgnoreCase("ACESS")) {
			strRet = "date()";
		} else if (strType.equalsIgnoreCase("SQLSERVER")) {
			strRet = "getdate()";
		} else if (strType.equalsIgnoreCase("DB2")) {
			strRet = "current date";
		} else if (strType.equalsIgnoreCase("POSTGRESQL")) {
			strRet = "current_date";
		} else if (strType.equalsIgnoreCase("TERA")) {
			strRet = "cast((date (format 'yyyy-mm-dd' )) as char(10)) ||' '|| time";
		} else if (strType.equalsIgnoreCase("SYBASE")) {
			strRet = "getdate()";
		} else {
			throw new Exception("can't get the current date of the function definition");
		}
		return strRet;
	}

	public String escapeString(String str) throws Exception {
		if (str == null || str.length() < 1) {
			return "";
		}
		String strType = getDBMSType();
		String strRet = "";
		for (int i = 0; i < str.length(); i++) {
			char c = str.charAt(i);
			if (c == '\'') {
				if (strType.equalsIgnoreCase("MYSQL")) {
					strRet = (new StringBuilder()).append(strRet).append("\\'").toString();
					continue;
				}
				if (strType.equalsIgnoreCase("ORACLE") || strType.equalsIgnoreCase("DB2")
						|| strType.equalsIgnoreCase("TERA") || strType.equalsIgnoreCase("SYBASE")) {
					strRet = (new StringBuilder()).append(strRet).append("''").toString();
				} else {
					strRet = (new StringBuilder()).append(strRet).append(c).toString();
				}
				continue;
			}
			if (c == '"') {
				if (strType.equalsIgnoreCase("MYSQL")) {
					strRet = (new StringBuilder()).append(strRet).append("\\\"").toString();
					continue;
				}
				if (strType.equalsIgnoreCase("ORACLE") || strType.equalsIgnoreCase("DB2")
						|| strType.equalsIgnoreCase("TERA") || strType.equalsIgnoreCase("SYBASE")) {
					strRet = (new StringBuilder()).append(strRet).append("\"").toString();
				} else {
					strRet = (new StringBuilder()).append(strRet).append(c).toString();
				}
				continue;
			}
			if (c == '\\') {
				if (strType.equalsIgnoreCase("MYSQL")) {
					strRet = (new StringBuilder()).append(strRet).append("\\\\").toString();
				} else {
					strRet = (new StringBuilder()).append(strRet).append(c).toString();
				}
			} else {
				strRet = (new StringBuilder()).append(strRet).append(c).toString();
			}
		}

		return strRet;
	}

	public String getSqlLimit(String strSql, int limitnum) // NOPMD by Administrator on 11-8-26 8:54
			throws Exception {
		String strType = getDBMSType();
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

	public static String getCountTotalSql(String table, String strPrimaryKey, String condition, String tail) {
		if (table == null) {
			return "";
		}
		String sql = (new StringBuilder()).append("select count(").append(strPrimaryKey).append(") from ").toString();
		sql = (new StringBuilder()).append(sql).append(table).append("    ").toString();
		if (condition != null && !condition.equals("")) {
			sql = (new StringBuilder()).append(sql).append("where ").append(condition).toString();
		}
		if (tail != null && !tail.equals("")) {
			sql = (new StringBuilder()).append(sql).append(tail).toString();
		}
		return sql;
	}

	public static String getCountTotalSql(String sql, String primaryKey) // NOPMD
	{
		StringBuffer countSQL = new StringBuffer();
		if (primaryKey == null || primaryKey.length() < 1) {
			primaryKey = "*";
		}
		countSQL.append((new StringBuilder()).append("select count(").append(primaryKey).append(") from ( ").toString());
		countSQL.append(sql);
		countSQL.append(" ) tt");
		return countSQL.toString();
	}

	public String getPagedSql(String srcSql, int currPage, int pageSize) throws Exception {
		String dbType = getDBMSType();
		int begin = (currPage - 1) * pageSize;
		int end = begin + pageSize;
		String strRet = srcSql;
		if (dbType.equals("MYSQL")) {
			strRet = (new StringBuilder()).append(srcSql).append(" limit ").append(begin).append(",").append(pageSize)
					.toString();
		} else

		if (dbType.equals("POSTGRESQL")) {
			strRet = (new StringBuilder()).append(srcSql).append(" limit ").append(pageSize).append(" offset ")
					.append(begin).toString();
		} else

		if (dbType.equals("ORACLE")) {
			end++;
			strRet = (new StringBuilder()).append("SELECT * FROM (SELECT ROWNUM AS NUMROW, c.* from (").append(srcSql)
					.append(") c) WHERE NUMROW >").append(begin).append(" AND NUMROW <").append(end).toString();
		} else if (dbType.equals("DB2")) {
			StringBuffer rownumber = (new StringBuffer(50)).append(" rownumber() over(");
			int orderByIndex = srcSql.toLowerCase().indexOf("order by");

			if (orderByIndex > 0) {
				String strOrder = srcSql.substring(orderByIndex);
				String[] arryStr = strOrder.split(" ");

				StringBuilder sb = new StringBuilder();
				for (int i = 0; i < arryStr.length; i++) {
					if (arryStr[i].lastIndexOf(".") > -1) {
						String[] arrStr = arryStr[i].split(",");
						arryStr[i] = "";
						for (int j = 0; j < arrStr.length; j++) {
							arrStr[j] = arrStr[j].substring(arrStr[j].lastIndexOf(".") + 1);
							arryStr[i] += arrStr[j];
							if (j != arrStr.length - 1) {
								arryStr[i] += ",";
							}
						}
					}

					sb.append(arryStr[i]).append(" ");
				}
				rownumber.append(sb.toString());
			}
			rownumber.append(") as row_,");
			StringBuffer pagingSelect = (new StringBuffer(srcSql.length() + 100))
					.append("select * from ( ")
					.append(" select ")
					.append(rownumber.toString())
					.append("temp_.* from (")
					.append(srcSql)
					.append(" ) as temp_")
					.append(" ) as temp2_")
					.append((new StringBuilder()).append(" where row_  between ").append(begin).append("+1 and ")
							.append(end).toString());
			strRet = pagingSelect.toString();
		} else if (!dbType.equals("SYBASE") && "TERA".equalsIgnoreCase(dbType)) {
			StringBuffer buffer = new StringBuffer(srcSql.length() + 100);
			buffer.append(srcSql);
			int orderByIndex = buffer.toString().toLowerCase().lastIndexOf("order by");
			if (orderByIndex > 0) {
				String orderBy = buffer.substring(orderByIndex);
				buffer.insert(orderByIndex, " QUALIFY row_number() OVER( ");
				buffer.append(" ) ");
				buffer.append((new StringBuilder()).append(" between ").append(begin).append(" and ").append(end)
						.toString());
				buffer.append(orderBy);
			} else {
				buffer.append((new StringBuilder()).append(" QUALIFY sum(1) over (rows unbounded preceding) between ")
						.append(begin).append(" and ").append(end).toString());
			}
			strRet = buffer.toString();
		}
		return strRet;
	}

	public String getPagedSql(String sql, String column, String strPrimaryKey, int curpage, int pagesize)
			throws Exception {
		String strDBType = getDBMSType();
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

	private static int getAfterSelectInsertPoint(String sql) {
		return 16 + (sql.startsWith("select distinct") ? 15 : 6);
	}

	public String getSqlSubString(String strColName, int pos, int len) throws Exception {
		String strType = getDBMSType();
		String strRet = "";
		if (strType.equalsIgnoreCase("MYSQL") || strType.equalsIgnoreCase("SYBASE")) {
			if (len == -1) {
				strRet = (new StringBuilder()).append("substring(").append(strColName).append(",").append(pos)
						.append(")").toString();
			} else {
				strRet = (new StringBuilder()).append("substring(").append(strColName).append(",").append(pos)
						.append(",").append(len).append(")").toString();
			}
		} else if (strType.equalsIgnoreCase("ORACLE") || strType.equalsIgnoreCase("DB2")
				|| strType.equalsIgnoreCase("POSTGRESQL")) {
			if (len == -1) {
				strRet = (new StringBuilder()).append("substr(").append(strColName).append(",").append(pos).append(")")
						.toString();
			} else {
				strRet = (new StringBuilder()).append("substr(").append(strColName).append(",").append(pos).append(",")
						.append(len).append(")").toString();
			}
		} else if (strType.equalsIgnoreCase("TERA")) {
			if (len == -1) {
				strRet = (new StringBuilder()).append("substring(").append(strColName).append(" form ").append(pos)
						.append(")").toString();
			} else {
				strRet = (new StringBuilder()).append("substring(").append(strColName).append(" from ").append(pos)
						.append(" for ").append(len).append(")").toString();
			}
		} else {
			throw new Exception("function definition can not be achieved");
		}
		return strRet;
	}

	public String getSqlSubDate(String interv) throws Exception {
		String strType = getDBMSType();
		String strRet = "";
		if (strType.equalsIgnoreCase("MYSQL")) {
			strRet = (new StringBuilder()).append("SUBDATE(now(),INTERVAL ").append(interv).append(" minute)")
					.toString();
		} else if (strType.equalsIgnoreCase("ORACLE")) {
			strRet = (new StringBuilder()).append("(sysdate-").append(interv).append("/(24*60))").toString();
		} else if (strType.equalsIgnoreCase("DB2")) {
			strRet = (new StringBuilder()).append("(current timestamp-").append(interv).append(" minute)").toString();
		} else if (strType.equalsIgnoreCase("SYBASE")) {
			strRet = (new StringBuilder()).append("dateadd(mi,-").append(interv).append(",getdate())").toString();
		} else {
			throw new Exception("can't get the current date of the function definition");
		}
		return strRet;
	}

	public String getSqlAddDate(String interv, String unit) // NOPMD
			throws Exception {
		String strRet = "";
		String strType = getDBMSType();
		unit = unit.trim().toUpperCase();
		interv = interv.trim();
		if (!interv.startsWith("-")) {
			interv = (new StringBuilder()).append("+").append(interv).toString();
		}
		if (strType.equalsIgnoreCase("MYSQL")) {
			strRet = (new StringBuilder()).append("ADDDATE(now(),INTERVAL ").append(interv).append(" ").append(unit)
					.append(")").toString();
		} else if (strType.equalsIgnoreCase("ORACLE")) {
			if (unit.compareTo("MINUTE") == 0) {
				strRet = (new StringBuilder()).append("(sysdate").append(interv).append("/(24*60))").toString();
			} else if (unit.compareTo("SECOND") == 0) {
				strRet = (new StringBuilder()).append("(sysdate").append(interv).append("/(24*60*60))").toString();
			} else if (unit.compareTo("HOUR") == 0) {
				strRet = (new StringBuilder()).append("(sysdate").append(interv).append("/24)").toString();
			} else if (unit.compareTo("DAY") == 0) {
				strRet = (new StringBuilder()).append("(sysdate").append(interv).append(")").toString();
			} else if (unit.compareTo("MONTH") == 0) {
				strRet = (new StringBuilder()).append("(add_months(sysdate,").append(interv).append("))").toString();
			} else if (unit.compareTo("YEAR") == 0) {
				strRet = (new StringBuilder()).append("(add_months(sysdate,(").append(interv).append("*12)))")
						.toString();
			}
		} else if (strType.equalsIgnoreCase("DB2")) {
			strRet = (new StringBuilder()).append("(current timestamp ").append(interv).append(" ").append(unit)
					.append(")").toString();
		} else if (strType.equalsIgnoreCase("SYBASE")) {
			if (unit.compareTo("MINUTE") == 0) {
				strRet = (new StringBuilder()).append("dateadd(mi,").append(interv).append(",getdate())").toString();
			} else if (unit.compareTo("SECOND") == 0) {
				strRet = (new StringBuilder()).append("dateadd(ss,").append(interv).append(",getdate())").toString();
			} else if (unit.compareTo("HOUR") == 0) {
				strRet = (new StringBuilder()).append("dateadd(hh,").append(interv).append(",getdate())").toString();
			} else if (unit.compareTo("DAY") == 0) {
				strRet = (new StringBuilder()).append("dateadd(dd,").append(interv).append(",getdate())").toString();
			} else if (unit.compareTo("MONTH") == 0) {
				strRet = (new StringBuilder()).append("dateadd(mm,").append(interv).append(",getdate())").toString();
			} else if (unit.compareTo("YEAR") == 0) {
				strRet = (new StringBuilder()).append("dateadd(yy,").append(interv).append(",getdate())").toString();
			}
		} else {
			throw new Exception("can't get the current date of the function definition");
		}
		return strRet;
	}

	public String getSqlDateAddMonth(String monthNum) throws Exception {
		String strType = getDBMSType();
		String strRet = "";
		if (strType.equalsIgnoreCase("MYSQL")) {
			strRet = (new StringBuilder()).append("DATE_ADD(curdate(),INTERVAL ").append(monthNum).append(" month)")
					.toString();
		} else if (strType.equalsIgnoreCase("ORACLE")) {
			strRet = (new StringBuilder()).append("to_char(add_months(sysdate,").append(monthNum)
					.append("),'YYYY-mm-dd')").toString();
		} else if (strType.equalsIgnoreCase("DB2")) {
			strRet = (new StringBuilder()).append("char((current date + ").append(monthNum).append(" month))")
					.toString();
		} else if (strType.equalsIgnoreCase("SYBASE")) {
			strRet = (new StringBuilder()).append("dateadd(mm,").append(monthNum).append(",getdate())").toString();
		} else if (strType.equalsIgnoreCase("TERA")) {
			strRet = (new StringBuilder()).append("add_months(date,").append(monthNum).append(")").toString();
		} else {
			throw new Exception("can't get the current date of the function definition");
		}
		return strRet;
	}

	public String getSqlEncrypt(String strPwd) throws Exception {
		StringBuffer strRet = new StringBuffer();
		strRet.append("'");
		strRet.append(DES.encrypt(strPwd));
		strRet.append("'");
		return strRet.toString();
	}

	public String getSql_intTochar(String strColName) // NOPMD
			throws Exception {
		String strType = getDBMSType();
		String strRet = "";
		if (strType.equalsIgnoreCase("MYSQL")) {
			strRet = strColName;
		} else if (strType.equalsIgnoreCase("ORACLE")) {
			strRet = (new StringBuilder()).append("cast(").append(strColName).append(" as varchar2(32))").toString();
		} else if (strType.equalsIgnoreCase("POSTGRESQL")) {
			strRet = (new StringBuilder()).append("cast(").append(strColName).append(" as varchar)").toString();
		} else if (strType.equalsIgnoreCase("DB2")) {
			strRet = (new StringBuilder()).append("rtrim(char(").append(strColName).append("))").toString();
		} else if (strType.equalsIgnoreCase("SYBASE")) {
			strRet = (new StringBuilder()).append("convert(char,").append(strColName).append(")").toString();
		} else if (strType.equalsIgnoreCase("SQLSERVER")) {
			strRet = (new StringBuilder()).append("cast(").append(strColName).append(" as varchar(12))").toString();
		} else if (strType.equalsIgnoreCase("TERA")) {
			strRet = (new StringBuilder()).append("cast(").append(strColName).append(" as varchar(12))").toString();
		} else {
			throw new Exception("function definition can not be achieved");
		}
		return strRet;
	}

	public String getSql_charToint(String strColName) // NOPMD
			throws Exception {
		String strType = getDBMSType();
		String strRet = "";
		if (strType.equalsIgnoreCase("MYSQL")) {
			strRet = strColName;
		} else if (strType.equalsIgnoreCase("ORACLE") || strType.equalsIgnoreCase("TERA")
				|| strType.equalsIgnoreCase("POSTGRESQL")) {
			strRet = (new StringBuilder()).append("cast(").append(strColName).append(" as integer)").toString();
		} else if (strType.equalsIgnoreCase("DB2")) {
			strRet = (new StringBuilder()).append("int(").append(strColName).append(")").toString();
		} else if (strType.equalsIgnoreCase("SYBASE")) {
			strRet = (new StringBuilder()).append("convert(int,").append(strColName).append(")").toString();
		} else if (strType.equalsIgnoreCase("SQLSERVER")) {
			strRet = (new StringBuilder()).append("cast(").append(strColName).append(" as integer)").toString();
		} else {
			throw new Exception("function definition can not be achieved");
		}
		return strRet;
	}

	public String getSql_charToDouble(String strColName) // NOPMD
			throws Exception {
		String strType = getDBMSType();
		String strRet = "";
		if (strType.equalsIgnoreCase("MYSQL")) {
			strRet = strColName;
		} else if (strType.equalsIgnoreCase("ORACLE") || strType.equalsIgnoreCase("POSTGRESQL")) {
			strRet = (new StringBuilder()).append("cast(").append(strColName).append(" as numeric)").toString();
		} else if (strType.equalsIgnoreCase("DB2")) {
			strRet = (new StringBuilder()).append("double(").append(strColName).append(")").toString();
		} else {
			throw new Exception("function definition can not be achieved");
		}
		return strRet;
	}

	public String getSqlRound(String str1, String str2) throws Exception {
		String strType = getDBMSType();
		String strRet = "";
		if (strType.equalsIgnoreCase("TERA")) {
			strRet = (new StringBuilder()).append(" cast ((").append(str1).append(") as decimal(10,").append(str2)
					.append(")) ").toString();
		} else {
			strRet = (new StringBuilder()).append(" round(").append(str1).append(",").append(str2).append(") ")
					.toString();
		}
		return strRet;
	}

	public String getSqlNotEqual() throws Exception {
		String strType = getDBMSType();
		String strRet = "";
		if (strType.equalsIgnoreCase("TERA")) {
			strRet = "<>";
		} else {
			strRet = "!=";
		}
		return strRet;
	}

	public String getSqlNvl(String str1, String str2) throws Exception {
		String strType = getDBMSType();
		String strRet = "";
		if (strType.equalsIgnoreCase("DB2")) {
			strRet = (new StringBuilder()).append("value(").append(str1).append(",").append(str2).append(")")
					.toString();
		} else if (strType.equalsIgnoreCase("ORACLE") || strType.equalsIgnoreCase("POSTGRESQL")) {
			strRet = (new StringBuilder()).append("nvl(").append(str1).append(",").append(str2).append(")").toString();
		} else if (strType.equalsIgnoreCase("MYSQL")) {
			strRet = (new StringBuilder()).append("ifnull(").append(str1).append(",").append(str2).append(")")
					.toString();
		} else if (strType.equalsIgnoreCase("SYBASE")) {
			strRet = (new StringBuilder()).append("isnull(").append(str1).append(",").append(str2).append(")")
					.toString();
		} else if (strType.equalsIgnoreCase("SQLSERVER")) {
			strRet = (new StringBuilder()).append("isnull(").append(str1).append(",").append(str2).append(")")
					.toString();
		} else if (strType.equalsIgnoreCase("TERA")) {
			strRet = (new StringBuilder()).append("COALESCE(").append(str1).append(",").append(str2).append(")")
					.toString();
		} else {
			throw new Exception("function definition can not be achieved");
		}
		return strRet;
	}

	public String getCreateAsTableSql(String newtable, String templettable) throws Exception {
		String ss = "";
		String strDBType = getDBMSType();
		if ("ORACLE".equalsIgnoreCase(strDBType) || "POSTGRESQL".equalsIgnoreCase(strDBType)) {
			ss = (new StringBuilder()).append("create table ").append(newtable).append(" as select * from ")
					.append(templettable).append(" where 1=2").toString();
		} else if ("DB2".equalsIgnoreCase(strDBType)) {
			ss = (new StringBuilder()).append("create table ").append(newtable).append(" like ").append(templettable)
					.toString();
		} else if ("TERA".equalsIgnoreCase(strDBType)) {
			ss = (new StringBuilder()).append("create table ").append(newtable).append(" as ").append(templettable)
					.append(" with no data").toString();
		}
		return ss;
	}

	public String getCreateAsTableSql(String newtable, String templettable, String tableSpace) throws Exception {
		String ss = getCreateAsTableSql(newtable, templettable);
		if (tableSpace == null || tableSpace.length() < 1) {
			return ss;
		}
		String strDBType = getDBMSType();
		if ("ORACLE".equalsIgnoreCase(strDBType) || "POSTGRESQL".equalsIgnoreCase(strDBType)) {
			ss = ss.replaceAll(newtable,
					(new StringBuilder()).append(newtable).append(" tablespace ").append(tableSpace).toString());
		} else if ("DB2".equalsIgnoreCase(strDBType)) {
			ss = (new StringBuilder()).append(ss).append(" in ").append(tableSpace).toString();
		}
		return ss;
	}

	public String getCreateTableInTableSpaceSql(String tableDDLSql, String tableSpace) // NOPMD by Administrator on 11-8-26 8:56
			throws Exception {
		if (tableSpace == null || tableSpace.length() < 1) {
			return tableDDLSql;
		}
		String strDBType = getDBMSType();
		if ("ORACLE".equalsIgnoreCase(strDBType) || "POSTGRESQL".equalsIgnoreCase(strDBType)) {
			tableDDLSql = (new StringBuilder()).append(tableDDLSql).append(" tablespace ").append(tableSpace)
					.toString();
		} else if ("DB2".equalsIgnoreCase(strDBType)) {
			tableDDLSql = (new StringBuilder()).append(tableDDLSql).append(" in ").append(tableSpace).toString();
		}
		return tableDDLSql;
	}

	public String getCreateIndexInTableSpaceSql(String createIndexSql, String tableSpace) // NOPMD by Administrator on 11-8-26 8:56
			throws Exception {
		if (tableSpace == null || tableSpace.length() < 1) {
			return createIndexSql;
		}
		String strDBType = getDBMSType();
		if ("ORACLE".equalsIgnoreCase(strDBType)) {
			createIndexSql = (new StringBuilder()).append(createIndexSql).append(" using index tablespace ")
					.append(tableSpace).toString();
		} else if (!"DB2".equalsIgnoreCase(strDBType)) {
			;
		}
		return createIndexSql;
	}

	public String getCheckTableIsExistSql(String tableName) throws Exception {
		String strSql = "";
		String strDBType = getDBMSType();
		if (strDBType.equals("DB2")) {
			strSql = (new StringBuilder()).append("select count(*) from syscat.tables where tabname='")
					.append(tableName.toUpperCase()).append("'").toString();
		} else if (strDBType.equals("ORACLE")) {
			strSql = (new StringBuilder()).append("select count(*) from TAB where tname='")
					.append(tableName.toUpperCase()).append("'").toString();
		} else if (strDBType.equals("TERA")) {
			strSql = (new StringBuilder()).append("select count(*) from dbc.tables where tablename='")
					.append(tableName.toUpperCase()).append("'").toString();
		} else if (strDBType.equals("GBASE")) {
			strSql = (new StringBuilder())
					.append("SELECT count(*) FROM information_schema.TABLES WHERE upper(TABLE_NAME)=upper('")
					.append(tableName.toUpperCase()).append("')").toString();
		}
		return strSql;
	}

	public String getSql_dateTochar(String strColName, String mask) // NOPMD by Administrator on 11-8-26 8:56
			throws Exception {
		String strRet = "";
		strRet = (new StringBuilder()).append("substr(").append(getSql2ColumnName(strColName)).append(",1,")
				.append(mask.trim().length()).append(")").toString();
		return strRet;
	}

	public String getSqlOptimizeStart(String tablename, String cpuParallelSize) throws Exception {
		String strRet = "";
		String strDBType = getDBMSType();
		if (strDBType.equalsIgnoreCase("ORACLE")) {
			strRet = (new StringBuilder()).append("select /*+ parallel( ").append(tablename).append(" ,")
					.append(cpuParallelSize).append(") */ ").toString();
		} else {
			strRet = "select ";
		}
		return strRet;
	}

	public String getSqlOptimizeEnd() throws Exception {
		String strRet = "";
		String strDBType = getDBMSType();
		if (strDBType.equalsIgnoreCase("ORACLE")) {
			strRet = ",row_number() over( order by 100) ";
		}
		return strRet;
	}

	public String getSqlNolog(String tablename) throws Exception {
		String strRet = "";
		String strDBType = getDBMSType();
		if (strDBType.equalsIgnoreCase("ORACLE")) {
			strRet = (new StringBuilder()).append("alter table ").append(tablename).append(" nologging").toString();
		}
		return strRet;
	}

	public String getSqlOptimizeInsert() throws Exception {
		String strRet = "";
		String strDBType = getDBMSType();
		if (strDBType.equalsIgnoreCase("ORACLE")) {
			strRet = "insert /*+append*/ into ";
		} else {
			strRet = "insert into ";
		}
		return strRet;
	}

	public String getCreateTableSql(String dataspace, String db2schemasuff, String dataspaceindex, String partitionKey)
			throws Exception {
		String strRet = "";
		String dbType = getDBMSType();
		if (dbType.equalsIgnoreCase("ORACLE") || dbType.equalsIgnoreCase("POSTGRESQL")) {
			strRet = (new StringBuilder()).append(" tablespace  ").append(dataspace).toString();
		} else if (dbType.equalsIgnoreCase("DB2")) {
			strRet = (new StringBuilder()).append(db2schemasuff).append(dataspace).append(dataspaceindex).toString();
			if (null != partitionKey && !"".equals(partitionKey)) {
				strRet = (new StringBuilder()).append(strRet).append(" PARTITIONING KEY (").append(partitionKey)
						.append(") USING HASHING").toString();
			}
			strRet = (new StringBuilder()).append(strRet).append(" NOT LOGGED INITIALLY ").toString();
		}
		return strRet;
	}

	public String getSqlIsNull(String strColName, boolean isNull) throws Exception {
		String strRet = "";
		String dbType = getDBMSType();
		if (isNull) {
			strRet = (new StringBuilder()).append(strColName).append(" is null").toString();
			if (dbType.equalsIgnoreCase("DB2")) {
				strRet = (new StringBuilder()).append(strRet).append(" or ").append(strColName).append("=''")
						.toString();
			}
		} else {
			strRet = (new StringBuilder()).append(strColName).append(" is not null").toString();
			if (dbType.equalsIgnoreCase("DB2")) {
				strRet = (new StringBuilder()).append(strRet).append(" and not ").append(strColName).append("=''")
						.toString();
			}
		}
		return strRet;
	}

	public String getSqlSystables() throws Exception {
		String strRet = "select * from(";
		String dbType = getDBMSType();
		if (dbType.equalsIgnoreCase("ORACLE")) {
			strRet = (new StringBuilder()).append(strRet)
					.append("select owner tabschema,table_name tabname,comments remarks From All_Tab_Comments")
					.toString();
			strRet = (new StringBuilder()).append(strRet).append(") where 1=1").toString();
			strRet = (new StringBuilder()).append(strRet).append(" and tabschema not like 'SYS%'").toString();
		} else if (dbType.equalsIgnoreCase("DB2")) {
			strRet = (new StringBuilder()).append(strRet).append("select tabschema,tabname,remarks from syscat.tables")
					.toString();
			strRet = (new StringBuilder()).append(strRet).append(")as ta where 1=1").toString();
			strRet = (new StringBuilder()).append(strRet).append(" and tabschema not like 'SYSIBM%'").toString();
		} else if (dbType.equalsIgnoreCase("MYSQL")) {
			strRet = (new StringBuilder())
					.append(strRet)
					.append("select table_schema tabschema,table_name tabname,table_comment remarks from information_schema.tables")
					.toString();
			strRet = (new StringBuilder()).append(strRet).append(")as ta where 1=1").toString();
			strRet = (new StringBuilder()).append(strRet).append(" and tabschema='").append(getDatabaseName())
					.append("'").toString();
		}
		return strRet;
	}

	public String getDatabaseName() throws Exception {
		String dbname = "";
		String dbType = getDBMSType();
		String url = connection.getMetaData().getURL();
		if (dbType.equalsIgnoreCase("MYSQL")) {
			int s = url.indexOf("/", 13) + 1;
			int e = url.indexOf("?");
			dbname = url.substring(s, e).toLowerCase();
			log.debug(dbname);
		}
		return dbname;
	}

	public String getSqlConcat(String strColNames) throws Exception {
		String strRet = "";
		String dbType = getDBMSType();
		String names[] = strColNames.split(",");
		if (dbType.equalsIgnoreCase("MYSQL")) {
			for (int i = 0; i < names.length; i++) {
				if (i == 0) {
					strRet = names[0];
				} else {
					strRet = (new StringBuilder()).append("CONCAT(").append(strRet).append(",").append(names[i])
							.append(")").toString();
				}
			}

		} else if (dbType.equalsIgnoreCase("DB2")) {
			strRet = strColNames.replaceAll(",", "||");
		} else if (dbType.equalsIgnoreCase("ORACLE")) {
			strRet = strColNames.replaceAll(",", "||");
		}
		return strRet;
	}

	private static Logger log = LogManager.getLogger(Sqlca.class);
	protected final String NULL_STRING = "";
	protected boolean unicodeToGB;
	protected boolean gbToUnicode;
	protected int sqlCode;
	protected String sqlNotice;
	protected int sqlRows;
	protected Connection connection;
	protected ConnectionEx connectionEx;
	protected ResultSet sqlResultSet;
	protected boolean sqlAutoCommit;
	protected boolean sqlAutoRollback;
	protected String strSqlType;
	protected String strSQL;
	protected boolean lastNullFlag;
	protected Statement statement;

	protected String strDBMS;
	private static int openCount = 0;
	protected Statement batchStatement;
	static int g_nCount = 0; // NOPMD by Administrator on 11-8-26 8:56

	public static void main(String[] args) {
		String strOrder = "ORDER BY comm.EDIT_DATE,comm.ABC DESC";
		strOrder = strOrder.replaceAll("*.", "");
		log.debug(strOrder); // NOPMD by Administrator on 11-8-26 8:54
	}

	//****************************新增方法*****************************
	protected java.sql.PreparedStatement preStat;

	public int execute(String strSQL, Object[] args) // NOPMD
			throws Exception {
		if (unicodeToGB) {
			strSQL = unicodeToGB(strSQL);
		}
		setSql(strSQL, args);
		return preparedStatementExecute();
	}

	public void setSql(String sqlStr, Object[] args) // NOPMD
			throws Exception {
		if (connection == null) {
			Failure("did not get a connection to the database!", 0);
		}
		if (sqlStr == null || sqlStr.trim().length() < 1) {
			Failure("sql preStat is empty!", 0);
		}
		strSQL = sqlStr;
		sqlCode = 0;
		sqlRows = 0;
		sqlNotice = null;
		sqlStr = sqlStr.trim();
		strSqlType = sqlStr.substring(0, 4).toUpperCase();
		if (strSqlType.equals("SELE")) {
			strSqlType = "SELECT";
		} else if (strSqlType.equals("WITH")) {
			strSqlType = "SELECT";
		} else if (strSqlType.equals("DELE")) {
			strSqlType = "DELETE";
		} else if (strSqlType.equals("UPDA")) {
			strSqlType = "UPDATE";
		} else if (strSqlType.equals("INSE")) {
			strSqlType = "INSERT";
		} else {
			strSqlType = "UNKNOWN";
		}
		close();
		openCount++;
		String strType = getDBMSType();
		if (strType.equalsIgnoreCase("SQLSERVER")) {
			connection.setAutoCommit(true);
		}
		preStat = connection.prepareStatement(sqlStr);
		//设置数据组参数
		if (args != null && args.length > 0) {
			for (int i = 0; i < args.length; i++) {
				preStat.setObject(i + 1, args[i]);
			}
		}
	}

	protected int preparedStatementExecute() throws Exception {
		sqlRows = 0;
		if (preStat == null) {
			sqlRows = -1;
			Failure("there is no enforceable SQL!", 0);
		}
		try {
			if (strSqlType.equals("SELECT")) {
				sqlResultSet = preStat.executeQuery();
			} else {
				sqlRows = preStat.executeUpdate();
			}
		} catch (SQLException e) {
			sqlRows = -1;
			sqlFailure((new StringBuilder()).append("execute SQL:\n  ").append(strSQL).append(" error!").toString(), e,
					0);
			return -1;
		}
		return sqlRows;
	}

	/**
	 * PrepareStatement方式sql批处理，防sql注入
	 * @param sql
	 * @param args
	 * @throws Exception
	 */
	public void addBatch(String sql, Object[] args) throws Exception {
		try {
			if (preStat == null) {
				preStat = connection.prepareStatement(sql);
			}
			//设置数据组参数
			if (args != null && args.length > 0) {
				for (int i = 0; i < args.length; i++) {
					preStat.setObject(i + 1, args[i]);
				}
			}
			preStat.addBatch();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw e;
		}
	}

	/**
	 * PrepareStatement方式sql批处理，防sql注入
	 * @return
	 * @throws Exception
	 */
	public int[] preparedStatementExecuteBatch() throws Exception {
		try {
			if (preStat == null) {
				return null;
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw e;
		}
		return preStat.executeBatch();
	}
}
