package com.ai.bdx.pop.adapter.dao.impl;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.helpers.Strings;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.ParameterizedBeanPropertyRowMapper;

import com.ai.bdx.pop.adapter.dao.ICpeUserInfoDao;
import com.ai.bdx.pop.bean.CpeUserInfo;
import com.ai.bdx.pop.util.PopConstant;
import com.asiainfo.biapp.pop.util.TimeUtil;
import com.google.common.collect.Lists;

public class CpeUserInfoDaoImpl implements ICpeUserInfoDao {
	private static final Logger log = LogManager.getLogger(CpeUserInfoDaoImpl.class);
	private JdbcTemplate jdbcTemplate;
	
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public boolean update(CpeUserInfo cpeUserInfo) 
			throws Exception{
		StringBuffer sql = new StringBuffer();
		List<Object> paramsObjects = new ArrayList<Object>();
		sql.append(" UPDATE CPE_USER_INFO");
		sql.append(" SET STATUS_CHANGE_TIME = ?,");
		paramsObjects.add(TimeUtil.getNowTime());
		if(cpeUserInfo.getNetLockFlag() != -1){
			sql.append(" NET_LOCK_FLAG = ?,");
			paramsObjects.add(cpeUserInfo.getNetLockFlag());
		}
		
		if (cpeUserInfo.getOpStatus() != 0) {
			sql.append(" OP_STATUS = ?,"); 
			paramsObjects.add(cpeUserInfo.getOpStatus());
		}
		if (cpeUserInfo.getBusiType() == PopConstant.BUSI_TYPE_NORMAL ||
				cpeUserInfo.getBusiType() == PopConstant.BUSI_TYPE_LIMIT_FOR_CITY ||
				cpeUserInfo.getBusiType() == PopConstant.BUSI_TYPE_LIMIT_FOR_LOCKED) {
			sql.append(" BUSI_TYPE = ?,");
			paramsObjects.add(cpeUserInfo.getBusiType());
		}
		if(sql.toString().endsWith(",")){
			sql.deleteCharAt(sql.toString().length() - 1);
		}
		sql.append(" WHERE 1 = 1");
		if(Strings.isNotEmpty(cpeUserInfo.getSubsid())){
			sql.append(" AND SUBSID = ?");
			paramsObjects.add(cpeUserInfo.getSubsid());
		}
		
		if(Strings.isNotEmpty(cpeUserInfo.getProductNo())){
			sql.append(" AND PRODUCT_NO = ?");
			paramsObjects.add(cpeUserInfo.getProductNo());
		}
		sql.append(" AND USER_STATUS != " + PopConstant.USER_STATUS_CANCEL);
		try {
			log.info(sql.toString() + ";参数列表：(" + "时间参数略"
					+ (cpeUserInfo.getNetLockFlag() == -1 ? "" : "," + cpeUserInfo.getNetLockFlag() 
					+ (cpeUserInfo.getOpStatus() == 0 ? "" : "," + cpeUserInfo.getOpStatus())
					+ (cpeUserInfo.getBusiType() == -1 ? "" : "," + cpeUserInfo.getBusiType())
					+ (Strings.isEmpty(cpeUserInfo.getSubsid()) ? "" : "," + cpeUserInfo.getSubsid())
					+ (Strings.isEmpty(cpeUserInfo.getProductNo()) ? "" : "," + cpeUserInfo.getProductNo())) + ")");
			int resInt = jdbcTemplate.update(sql.toString(), paramsObjects.toArray()); 
			return resInt>0;
		} catch (Exception e) {
			log.error(sql.toString() + ";参数列表：(" + "时间参数略"
					+ (cpeUserInfo.getNetLockFlag() == -1 ? "" : "," + cpeUserInfo.getNetLockFlag() 
					+ (cpeUserInfo.getOpStatus() == 0 ? "" : "," + cpeUserInfo.getOpStatus())
					+ (cpeUserInfo.getBusiType() == -1 ? "" : "," + cpeUserInfo.getBusiType())
					+ (Strings.isEmpty(cpeUserInfo.getSubsid()) ? "" : "," + cpeUserInfo.getSubsid())
					+ (Strings.isEmpty(cpeUserInfo.getProductNo()) ? "" : "," + cpeUserInfo.getProductNo())) + ")，修改状态失败！");
			throw new Exception("修改锁网状态或者操作状态失败！");
		}
	}
	
	@Override
	public boolean updatePosition(CpeUserInfo cpeUserInfo) 
			throws Exception{
		StringBuffer sql = new StringBuffer();
		List<Object> paramsObjects = new ArrayList<Object>();
		sql.append(" UPDATE CPE_USER_INFO");
		sql.append(" SET CITY_NAME = ?,");
		paramsObjects.add(cpeUserInfo.getCityName());
		
		sql.append(" COUNTY_NAME = ?");
		paramsObjects.add(cpeUserInfo.getCountyName());
		
		sql.append(" WHERE SUBSID = ?");
		paramsObjects.add(cpeUserInfo.getSubsid());
		try {
			int resInt = jdbcTemplate.update(sql.toString(), paramsObjects.toArray()); 
			return resInt>0;
		} catch (Exception e) {
			log.error(e);
			throw new Exception("修改锁网状态或者操作状态失败！");
		}
	}

	@Override
	public CpeUserInfo query(String subsid) throws Exception{
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT C.SUBSID," +
				"C.PRODUCT_NO," +
				"C.NET_TYPE," +
				"R.STRATEGY_CODE AS POLICY_ID," +
				"C.COUNTRY_NAME," +
				"C.COUNTY_NAME," +
				"C.CITY_NAME," +
				"C.CREATE_TIME," +
				"C.PLAN_CODE," +
				"C.LAC_INFO," +
				"C.USER_STATUS," +
				"C.NET_LOCK_FLAG," +
				"C.BUSI_TYPE," +
				"C.STATUS_CHANGE_TIME," +
				"C.OP_STATUS, " +
				"C.FILE_NAME " +
				" FROM CPE_USER_INFO C " +
				"LEFT JOIN DIM_NET_TYPE_STRATEGY_REL R " +
				"ON C.NET_TYPE = R.NET_TYPE WHERE SUBSID = ?");
		try{
			return (CpeUserInfo) jdbcTemplate.queryForObject(sql.toString(),new Object[]{subsid}, 
					ParameterizedBeanPropertyRowMapper.newInstance(CpeUserInfo.class));
		}catch (Exception e) {
			log.error(e);
			throw new Exception("根据CPE设备唯一号：" + subsid + "查询CPE用户信息失败！");
		}
	}
	
	@Override
	public List<CpeUserInfo> query(CpeUserInfo cpeUserInfo)
			throws Exception {
		StringBuffer sql = new StringBuffer();
		List<Object> params = new ArrayList<Object>();
		sql.append("SELECT C.SUBSID," +
				"C.PRODUCT_NO," +
				"C.NET_TYPE," +
				"R.STRATEGY_CODE AS POLICY_ID," +
				"C.COUNTRY_NAME," +
				"C.COUNTY_NAME," +
				"C.CITY_NAME," +
				"C.CREATE_TIME," +
				"C.PLAN_CODE," +
				"C.LAC_INFO," +
				"C.USER_STATUS," +
				"C.NET_LOCK_FLAG," +
				"C.BUSI_TYPE," +
				"C.STATUS_CHANGE_TIME," +
				"C.OP_STATUS, " +
				"C.FILE_NAME " +
				" FROM CPE_USER_INFO C " +
				"LEFT JOIN DIM_NET_TYPE_STRATEGY_REL R " +
				"ON C.NET_TYPE = R.NET_TYPE WHERE 1=1 ");
		if(Strings.isNotEmpty(cpeUserInfo.getSubsid())){
			sql.append(" AND C.SUBSID = ?");
			params.add(cpeUserInfo.getSubsid());
		}
		if(Strings.isNotEmpty(cpeUserInfo.getProductNo())){
			sql.append(" AND C.PRODUCT_NO = ?");
			params.add(cpeUserInfo.getProductNo());
		}
		if(Strings.isNotEmpty(cpeUserInfo.getNetType())){
			sql.append(" AND R.STRATEGY_CODE = ?");
			params.add(cpeUserInfo.getNetType());
		}
		sql.append(" AND C.USER_STATUS != " + PopConstant.USER_STATUS_CANCEL);
		
		try{
			return jdbcTemplate.query(sql.toString(),params.toArray(), 
					ParameterizedBeanPropertyRowMapper.newInstance(CpeUserInfo.class));
		}catch (Exception e) {
			log.error(e);
			throw new Exception("根据CPE设备唯一号：" + params + "查询CPE用户信息失败！");
		}
	}
	
	@Override
	public List<Map<String, Object>> query(int netLockFlag, int busiType)
			throws Exception {
		try{
			StringBuffer sql = new StringBuffer();
			StringBuffer groupByPart = new StringBuffer();
			StringBuffer wherePart = new StringBuffer();
			sql.append("SELECT ");
			List<Object> params = Lists.newArrayList();
			wherePart.append("WHERE 1 = 1 ");
			if(netLockFlag != -1){
				sql.append("NET_LOCK_FLAG ");
				wherePart.append("AND NET_LOCK_FLAG = ? ");
				groupByPart.append("NET_LOCK_FLAG ");
				params.add(netLockFlag);
			}
			
			if(busiType != -1 ){
				if(netLockFlag != -1){
					sql.append(", BUSI_TYPE ");
					groupByPart.append(", BUSI_TYPE ");
				}else if(netLockFlag == -1){
					sql.append("BUSI_TYPE ");
					groupByPart.append("BUSI_TYPE ");
				}
				wherePart.append(" AND BUSI_TYPE = ? ");
				params.add(busiType);
			}
			
			sql.append(", COUNT(1) FROM CPE_USER_INFO ");
			sql.append(wherePart.toString());
			sql.append("GROUP BY ");
			sql.append(groupByPart.toString());
			return jdbcTemplate.queryForList(sql.toString(), params.toArray());
		}catch(Exception e){
			log.error(e);
		}
		return null;
	}

	@Override
	public void batchUpdate(final List<CpeUserInfo> cpeUserInfoList,String fileName) throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append("INSERT INTO CPE_USER_INFO(" +
				"PRODUCT_NO," +
				"SUBSID," +
				"NET_TYPE," +
				"COUNTRY_NAME," +
				"CREATE_TIME," +
				"PLAN_CODE," +
				"STATUS_CHANGE_TIME," +
				"USER_STATUS," +
				"NET_LOCK_FLAG," +
				"BUSI_TYPE," +
				"OP_STATUS," + 
				"FILE_NAME)");
		sql.append(" VALUES(?,?,?,?,?,?,?,");
		sql.append(PopConstant.USER_STATUS_NORMAL + ",");
		sql.append(PopConstant.NET_LOCK_FLAG_UNLOCK + ",");
		sql.append(PopConstant.BUSI_TYPE_NORMAL + ",");
		sql.append(PopConstant.OP_STATUS_OPENING_ACCOUNT + ",'");
		sql.append(fileName + "')");
		try {
			jdbcTemplate.batchUpdate(sql.toString(), new BatchPreparedStatementSetter(){

				@Override
				public int getBatchSize() {
					return cpeUserInfoList.size();
				}

				@Override
				public void setValues(PreparedStatement ps, int i)
						throws SQLException {
					 ps.setString(1, cpeUserInfoList.get(i).getProductNo());
					 ps.setString(2, cpeUserInfoList.get(i).getSubsid());
					 ps.setString(3, cpeUserInfoList.get(i).getNetType());
					 ps.setString(4, cpeUserInfoList.get(i).getCountryName());
					 ps.setTimestamp(5, cpeUserInfoList.get(i).getCreateTime());
					 ps.setString(6, cpeUserInfoList.get(i).getPlanCode());
					 ps.setTimestamp(7, TimeUtil.getNowTime());
				}
				
			});
		} catch (Exception e) {
			log.error("记录CPE开户信息失败！",e);
			throw new Exception("记录CPE开户信息失败！");
		}
		
	}

	public List<Map<String, Object>> queryAllResetByFlag(Map<String,Object> param)
			throws Exception {
		List<Map<String, Object>>  list = new ArrayList<Map<String, Object>>();
		try{
			StringBuffer sql = new StringBuffer();
			StringBuffer groupByPart = new StringBuffer();
			//StringBuffer wherePart = new StringBuffer();
			sql.append("SELECT * from cpe_user_info where 1=1  ");
			List<Object> params = Lists.newArrayList();
			if(param.containsKey("netLockFlag"))
			{
				sql.append(" and net_lock_flag = ? ");
				params.add(param.get("netLockFlag"));
			}
			if(param.containsKey("busiType"))
			{
					sql.append(" and busi_type = ? ");
					params.add(param.get("busiType"));
			} 
			if(param.containsKey("lineLimit"))
			{
				sql.append(" limit ? ");
				params.add(param.get("lineLimit"));
			}   
			sql.append(groupByPart.toString());
			System.out.println(sql.toString());
			return jdbcTemplate.queryForList(sql.toString(), params.toArray());
		}catch(Exception e){
			log.error(e);
		}
		return list;
	}
}
