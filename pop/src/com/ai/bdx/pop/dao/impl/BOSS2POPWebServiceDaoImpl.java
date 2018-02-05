package com.ai.bdx.pop.dao.impl;

import java.rmi.RemoteException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;

import com.ai.bdx.pop.adapter.dao.impl.CpeUserInfoDaoImpl;
import com.ai.bdx.pop.bean.Prod;
import com.ai.bdx.pop.dao.BOSS2POPWebServiceDao;
import com.ai.bdx.pop.util.SpringContext;
import com.ai.bdx.pop.wsclient.ISendPccInfoService;
import com.ai.bdx.pop.wsclient.ResultModel;
import com.ailk.bdx.pop.adapter.util.ProdPolicyIdUtil;
import com.jfinal.plugin.activerecord.Db;

public class BOSS2POPWebServiceDaoImpl implements BOSS2POPWebServiceDao{
	
	private static final Logger log = LogManager.getLogger(BOSS2POPWebServiceDaoImpl.class);
	private JdbcTemplate jdbcTemplate;
	private ISendPccInfoService sendPccInfoService;
	

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	public void setSendPccInfoService(ISendPccInfoService sendPccInfoService){
		this.sendPccInfoService = sendPccInfoService;
	}
	
	
	public String bossToPopGprsOtp(Prod prod) throws DataAccessException, ParseException{
		String res ="0";
		insertProdToMysql(prod);
		res =batchPhoneOpt(prod);
		log.info("手机号:"+prod.getMobileno()+"发送至PCC的结果编码为:"+res);
		return  res;
	}

	public int insertProdToMysql(Prod prod) throws DataAccessException, ParseException{
		// TODO Auto-generated method stub
		StringBuffer sql = new StringBuffer();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd hhmmss");
		sql.append("INSERT INTO SUB_GPRS_PROD(" +
				"PRODID," +
				"MOBILENO," +
				"EFF_TIME," +
				"REGION," +
				"CHANNEL," +
				"OPTYPE," +
				"CREATE_TIME)");
		sql.append(" VALUES(?,?,?,?,?,?,?)");
		int result = jdbcTemplate.update(sql.toString(),new Object[]{
			prod.getProdid(),
			prod.getMobileno(),
			new Timestamp(sdf.parse(prod.getEff_time()).getTime()),
			prod.getRegion(),
			prod.getChannel(),
			prod.getOptype(),
			new Timestamp(new Date().getTime())});
		return result;
	}


	public void batchUpdate(final List<Prod> list) {
		// TODO Auto-generated method stub
		StringBuffer sql = new StringBuffer();
		final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd hhmmss");
		sql.append("INSERT INTO SUB_GPRS_PROD(" +
				"PRODID," +
				"MOBILENO," +
				"EFF_TIME," +
				"EXP_TIME," +
				"REGION," +
				"CHANNEL," +
				"OPTYPE," +
				"CREATE_TIME)");
		sql.append(" VALUES(?,?,?,?,?,?,?,?)");
		try {
			jdbcTemplate.batchUpdate(sql.toString(), new BatchPreparedStatementSetter(){

				@Override
				public int getBatchSize() {
					// TODO Auto-generated method stub
					return list.size();
				}

				@Override
				public void setValues(PreparedStatement pre, int i)
						throws SQLException {
					// TODO Auto-generated method stub
					pre.setString(1, list.get(i).getProdid());
					pre.setString(2, list.get(i).getMobileno());
					try {
						pre.setTimestamp(3,new Timestamp(sdf.parse(list.get(i).getEff_time()).getTime()));
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						log.error("！",e);
						pre.setTimestamp(3,new Timestamp(new Date().getTime()));
						e.printStackTrace();
					}
					try {
						pre.setTimestamp(4,new Timestamp(sdf.parse(list.get(i).getExp_time()).getTime()));
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						pre.setTimestamp(4,new Timestamp(new Date().getTime()));
						e.printStackTrace();
					}
					pre.setString(5, list.get(i).getRegion());
					pre.setString(6, list.get(i).getChannel());
					pre.setString(7, list.get(i).getOptype());
					pre.setTimestamp(8,new Timestamp(new Date().getTime()));
				}
				
			});
		} catch (DataAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public String batchPhoneOpt(Prod prod){
			
		    String result = "100";
			String command = prod.getOptype();//1为开户,
			String phoneNo = prod.getMobileno();
			String status = "0";
			String prodid = prod.getProdid().toUpperCase();
			String policyId = "11000030000000000000000000000004";
			policyId = ProdPolicyIdUtil.getPolicyPropVal("PROD."+prodid+".POLICYID")==null?ProdPolicyIdUtil.getPolicyPropVal("PROD.OTHER01.POLICYID"):ProdPolicyIdUtil.getPolicyPropVal("PROD."+prodid+".POLICYID");
			status = ProdPolicyIdUtil.getPolicyPropVal("PROD."+prodid+".STATUS")==null?ProdPolicyIdUtil.getPolicyPropVal("PROD.OTHER01.STATUS"):ProdPolicyIdUtil.getPolicyPropVal("PROD."+prodid+".STATUS");
			/*if(){
				policyId = Db.queryStr("SELECT R.STRATEGY_CODE DIM_NET_TYPE_STRATEGY_REL R WHERE R.net_type = ? ", prodid);
			}*/
			try {
				result =  sendPccInfoService.singlePhoneOptWeb(command, phoneNo, policyId,status);
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return result;
	}
	
	 public  String getLastDayOfMonth(int year,int month)
	    {
	        Calendar cal = Calendar.getInstance();
	        //设置年份
	        cal.set(Calendar.YEAR,year);
	        //设置月份
	        cal.set(Calendar.MONTH, month-1);
	        //获取某月最大天数
	        int lastDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
	        //设置日历中月份的最大天数
	        cal.set(Calendar.DAY_OF_MONTH, lastDay);
	        //格式化日期
	        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	        String lastDayOfMonth = sdf.format(cal.getTime()).substring(8, 10);
	         
	        return lastDayOfMonth;
	    }
	
	 public static void main(String[] args) {
		 	String result = "100";
			
			String status = "0";
			String prodid = "244410".toUpperCase();
			String policyId = "11000010000000000000000000000010";
			policyId = ProdPolicyIdUtil.getPolicyPropVal("PROD."+prodid+".POLICYID")==null?ProdPolicyIdUtil.getPolicyPropVal("PROD.OTHER01.POLICYID"):ProdPolicyIdUtil.getPolicyPropVal("PROD."+prodid+".POLICYID");
			status = ProdPolicyIdUtil.getPolicyPropVal("PROD."+prodid+".STATUS")==null?ProdPolicyIdUtil.getPolicyPropVal("PROD.OTHER01.STATUS"):ProdPolicyIdUtil.getPolicyPropVal("PROD."+prodid+".STATUS");
		
			System.out.println(policyId+","+status);
	 }
}
