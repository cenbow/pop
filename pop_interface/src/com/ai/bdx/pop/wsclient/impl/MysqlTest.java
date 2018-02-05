package com.ai.bdx.pop.wsclient.impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ai.bdx.pop.wsclient.util.PropUtil;

public class MysqlTest {
	public static void main(String[] args) throws Exception {

		Connection conn = null;

		String sql;

		// MySQL的JDBC URL编写方式：jdbc:mysql://主机名称：连接端口/数据库的名称?参数=值

		// 避免中文乱码要指定useUnicode和characterEncoding

		// 执行数据库操作之前要在数据库管理系统上创建一个数据库，名字自己定，

		// 下面语句之前就要先创建javademo数据库

		String url = PropUtil.getProp("url", "mysql.properties");
		
		try {

			// 之所以要使用下面这条语句，是因为要使用MySQL的驱动，所以我们要把它驱动起来，

			// 可以通过Class.forName把它加载进去，也可以通过初始化来驱动起来，下面三种形式都可以

			Class.forName("com.mysql.jdbc.Driver");// 动态加载mysql驱动

			// or:

			// com.mysql.jdbc.Driver driver = new com.mysql.jdbc.Driver();

			// or：

			// new com.mysql.jdbc.Driver();

			System.out.println("成功加载MySQL驱动程序");

			// 一个Connection代表一个数据库连接
			conn = DriverManager.getConnection(url);

			// Statement里面带有很多方法，比如executeUpdate可以实现插入，更新和删除等

			Statement stmt = conn.createStatement();

			// sql =
			// "create table student(NO char(20),name varchar(20),primary key(NO))";

			// int result = stmt.executeUpdate(sql);//
			// executeUpdate语句会返回一个受影响的行数，如果返回-1就没有成功

//			System.out.println("创建数据表成功");

//			sql = "insert into student(NO,name) values('2012001','陶伟基')";
//
//			int result = stmt.executeUpdate(sql);
//
//			sql = "insert into student(NO,name) values('2012002','周小俊')";
//
//			result = stmt.executeUpdate(sql);

			sql = "select * from phones";

			ResultSet rs = stmt.executeQuery(sql);// executeQuery会返回结果的集合，否则返回空值

			System.out.println("手机号");
			List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
			
			ResultSetMetaData md = rs.getMetaData(); //获得结果集结构信息,元数据
		    int columnCount = md.getColumnCount();   //获得列数 
			while (rs.next()) {

				Map<String,Object> rowData = new HashMap<String,Object>();
			      for (int i = 1; i <= columnCount; i++) {
			        rowData.put(md.getColumnName(i), rs.getObject(i));
			      }
			      list.add(rowData);
			      
//				System.out
//
//				.println(rs.getString("phone"));// 入如果返回的是int类型可以用getInt()

			}
			
			if(list!=null && list.size()>0){
				for(int i=0;i<list.size();i++){
					Map<String,Object> rowData = list.get(i);
					System.out.println(rowData.get("phone"));
				}
			}
			
			if(list!=null && list.size()>0){
				for(int i=0;i<list.size();i++){
					Map<String,Object> rowData = list.get(i);
					System.out.println(rowData.get("phone"));
				}
			}

		} catch (SQLException e) {

			System.out.println("MySQL操作错误");

			e.printStackTrace();

		} catch (Exception e) {

			e.printStackTrace();

		} finally {

			conn.close();

		}

	}
}
