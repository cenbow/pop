package com.ai.bdx.pop.wsclient.impl;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;

import com.ai.bdx.pop.wsclient.bean.SprBusinessBean;
import com.ai.bdx.pop.wsclient.impl.SendPccInfoServiceImpl;

public class MysqlUtil {
	private static Properties env;
	private static InputStream is=null;
	
	static{
		env = new Properties();
		try {
			
			is=MysqlUtil.class.getClassLoader().getResourceAsStream("mysql.properties");
			env.load(is);
		} catch (IOException e) {
			
			e.printStackTrace();
		}
	}
	private static final ThreadLocal<Connection> tl=new ThreadLocal<Connection>();
	
	public static Connection getCurrentConnection()throws Exception{
		Connection conn = tl.get();
		if(conn==null){
			Class.forName("com.mysql.jdbc.Driver");
			conn=DriverManager.getConnection(env.getProperty("url"));
			tl.set(conn);
			
		}
		return conn;
	}
//	public static void main(String[] args) throws Exception {
//		SprBusinessBean sprBean = new SprBusinessBean();
//		Connection conn = null;
//		conn = MysqlUtil.getCurrentConnection();
////		PreparedStatement ps = conn.prepareStatement("select * from cpe_user_info");
////		ResultSet rs = ps.executeQuery();
////		while(rs.next()){
////			System.out.println(rs.getString("subsid"));
////		}
//		System.out.println("测试获得安全连接"+conn.toString());
//		Statement stmt = conn.createStatement();
//		 SendPccInfoServiceImpl sendPcc = new SendPccInfoServiceImpl();
//		 System.out.println("用户的销户状态为:US24;SendPccInfoServiceImpl="+sendPcc.toString());
//		 String phone="13810998891";
//		String sql = "select t2.spr_name,t2.SPR_IP,t2.SPR_WSDL,t2.SPR_FTP_ADDRESS,t2.SPR_FTP_USR,t2.SPR_FTP_PWD,t2.SPR_USR,t2.SPR_PWD "
//					+ "from POP_SPR_SECTION t1 "
//					+ "left join POP_SPR_PROPERTY t2 on (case when t1.isbase = '0' then t1.BASE_SPR_NAME else t1.BACKUP_SPR_NAME end) = t2.SPR_NAME "
//					+ "where substring(" + phone + ",1,7) = t1.SECTION_NO;";
//		ResultSet rs = stmt.executeQuery(sql);
//
//		while (rs.next()) {
//			sprBean.setSprName(rs.getString("SPR_NAME"));
//			sprBean.setSprIp(rs.getString("SPR_IP"));
//			sprBean.setSprWsdl(rs.getString("SPR_WSDL"));
//			sprBean.setFtpAddress(rs.getString("SPR_FTP_ADDRESS"));
//			sprBean.setFtpUsr(rs.getString("SPR_FTP_USR"));
//			sprBean.setFtpPwd(rs.getString("SPR_FTP_PWD"));
//			sprBean.setSprUsr(rs.getString("SPR_USR"));
//			sprBean.setSprPwd(rs.getString("SPR_PWD"));
//			
//		}
//		System.out.println("sprBean=="+sprBean.toString());
//		 String statusChange = sendPcc.delSubscriber("13810998891",sprBean);
//		 System.out.println("用户销户调用pcc接口返回值="+statusChange);
//		
//	}
}
