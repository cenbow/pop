package com.ai.bdx.pop.util;

import java.sql.Connection;
import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import com.ai.bdx.pop.exception.PopException;
import com.asiainfo.biframe.utils.config.Configure;
import com.asiainfo.biframe.utils.string.StringUtil;

/**
 * 数据源工厂
 * @author zhangyb5
 *
 */
public class DataSourceFactory {
	private static final Logger log = LogManager.getLogger();
	private static Hashtable<String, DataSource> dss = new Hashtable<String, DataSource>();

	private DataSourceFactory() {

	}

	/**
	 * 获取数据源，如果未找到，则返回null
	 * @param jndi
	 * @return
	 */
	public static DataSource getDataSource(String jndi) {
		if (!dss.containsKey(jndi)) {
			addDataSource(jndi, null);
		}
		return dss.get(jndi);
	}

	/**
	 * 获取数据源，如果未找到，则返回null
	 * @param jndi
	 * @return
	 */
	public static DataSource getDataSource(String jndi, String url) {
		if (!dss.containsKey(jndi)) {
			addDataSource(jndi, url);
		}
		return dss.get(jndi);
	}

	private static void addDataSource(String jndi, String url) {
		try {
			DataSource ds = null;
			if (StringUtil.isEmpty(url)) {
				Context ctx = new InitialContext();
				String app_server_type = Configure.getInstance().getProperty("APP_SERVER_TYPE");
				Context envContext = null;
				if (app_server_type != null
						&& ("weblogic".equalsIgnoreCase(app_server_type) || "websphere"
								.equalsIgnoreCase(app_server_type))) {
					envContext = ctx;
				} else {
					envContext = (Context) ctx.lookup("java:comp/env");
				}
				if (StringUtil.isNotEmpty(jndi)) {
					String strDbJndi = jndi;
					if (strDbJndi.startsWith("java:comp/env/")) {
						strDbJndi = strDbJndi.substring(14);
					}
					ds = (DataSource) envContext.lookup(strDbJndi);
				}
			} else {//通过url构造DriverManagerDataSource
				int index = url.indexOf("?");
				String jdbcUrl = url.substring(0, index);
				String[] params = url.substring(index + 1).split("&");
				DriverManagerDataSource driverManagerDataSource = new DriverManagerDataSource();
				if (jdbcUrl.contains("voltdb")) {
					driverManagerDataSource.setDriverClassName("org.voltdb.jdbc.Driver");
				} else {
					throw new PopException("The jdbc url[" + url + "] is not supported!");
				}
				driverManagerDataSource.setUrl(jdbcUrl);
				driverManagerDataSource.setUsername(params[0].split("=")[1]);
				driverManagerDataSource.setPassword(params[1].split("=")[1]);
				ds = driverManagerDataSource;
			}
			if (ds != null) {
				dss.put(jndi, ds);
			} else {
				throw new Exception("can't find new datasource!" + jndi);
			}
		} catch (Exception e) {
			log.error("addDataSource(" + jndi + "),error", e);
		}
	}

	/**
	 * 获取指定数据源的链接，如果未找到，则返回null
	 * @param jndi
	 * @return
	 */
	public static Connection getConnection(String jndi) {
		DataSource ds = getDataSource(jndi);
		return DataSourceUtils.getConnection(ds);
	}

	/**
	 *关闭连接
	 * @param jndi
	 * @return
	 */
	public static void closeConnection(String jndi, Connection conn) {
		if (StringUtil.isNotEmpty(jndi) && conn != null) {
			DataSource ds = getDataSource(jndi);
			if (ds != null) {
				DataSourceUtils.releaseConnection(conn, ds);
			}
		}
	}
}
