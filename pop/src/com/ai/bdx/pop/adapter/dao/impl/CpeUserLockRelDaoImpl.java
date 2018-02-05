package com.ai.bdx.pop.adapter.dao.impl;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.ParameterizedBeanPropertyRowMapper;

import com.ai.bdx.pop.adapter.dao.ICpeUserLockRelDao;
import com.ai.bdx.pop.bean.CpeLockLacCiBean;
import com.ai.bdx.pop.bean.CpeUserLockRelBean;
import com.google.common.base.Strings;

public class CpeUserLockRelDaoImpl implements ICpeUserLockRelDao{
	private static final Logger log = LogManager.getLogger(CpeUserLockRelDaoImpl.class);
	private JdbcTemplate jdbcTemplate;
	
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	@Override
	public boolean insert(CpeUserLockRelBean cpeUserLockRelBean)
			throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append(" INSERT INTO CPE_USER_LOCK_REL( ");
		sql.append(" SUBSID, ");    
		sql.append(" PRODUCT_NO, ");
		sql.append(" LOCKED_LAC_CI, ");
		sql.append(" USER_LOCATION, ");
		sql.append(" STATION_CODE ) ");
		sql.append(" SELECT ?,?,D.LAC_CI_HEX_CODE,?,D.STATION_CODE FROM DIM_CPE_LAC_CI D WHERE USER_LOCATION = ?");
		try {
			int  resInt = jdbcTemplate.update(sql.toString(), new Object[]{
				cpeUserLockRelBean.getSubsid(),
				cpeUserLockRelBean.getProductNo(),
				cpeUserLockRelBean.getUserLocation(),
				cpeUserLockRelBean.getUserLocation()
			}); 
			return resInt>0;
		} catch (Exception e) {
			log.error(e);
			throw new Exception("添加CPE用户锁网小区关系失败！");
		}
	}
	
	@Override
	public boolean batchInsert(final CpeUserLockRelBean cpeUserLockRelBean,
			final List<String> userLocationList) throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append(" INSERT INTO CPE_USER_LOCK_REL( ");
		sql.append(" SUBSID, ");    
		sql.append(" PRODUCT_NO, ");
		sql.append(" LOCKED_LAC_CI, ");
		sql.append(" USER_LOCATION, ");
		sql.append(" STATION_CODE ) ");
		sql.append(" SELECT ?,?,D.LAC_CI_HEX_CODE,?,D.STATION_CODE FROM DIM_CPE_LAC_CI D WHERE USER_LOCATION = ?");
		try {
			jdbcTemplate.batchUpdate(sql.toString(), new BatchPreparedStatementSetter(){

				@Override
				public int getBatchSize() {
					return userLocationList.size();
				}

				@Override
				public void setValues(PreparedStatement ps, int i)
						throws SQLException {
					 ps.setString(1, cpeUserLockRelBean.getSubsid());
					 ps.setString(2, cpeUserLockRelBean.getProductNo());
					 ps.setString(3, userLocationList.get(i));
					 ps.setString(4, userLocationList.get(i));
				}
				
			});
		} catch (Exception e) {
			log.error("添加CPE开户锁网小区失败！",e);
			//throw new Exception("添加CPE开户锁网小区失败！");
		}
		return false;
	}
	
	@Override
	public List<CpeLockLacCiBean> queryLockedLacCi(String subsid, String productNo)
			throws Exception {
		StringBuffer sql = new StringBuffer();
		List<Object> params = new ArrayList<Object>();
		sql.append("SELECT S.CITY_NAME,S.COUNTY_NAME,S.TOWN_NAME,I.COUNTRY_NAME, ");
		sql.append("D.CELL_NAME,I.PRODUCT_NO,I.SUBSID,I.NET_LOCK_FLAG,I.BUSI_TYPE,I.USER_STATUS,I.CREATE_TIME,I.NET_TYPE ");
		sql.append("FROM CPE_USER_INFO I ");
		sql.append("LEFT JOIN CPE_USER_LOCK_REL R ON I.SUBSID = R.SUBSID ");
		sql.append("LEFT JOIN DIM_CPE_LAC_CI D ON R.USER_LOCATION = D.USER_LOCATION ");
		sql.append("LEFT JOIN DIM_CPE_STATION S ON D.STATION_CODE = S.STATION_CODE ");
		sql.append("WHERE 1=1 ");
		if(!Strings.isNullOrEmpty(subsid)){
			params.add(subsid);
			sql.append("AND I.SUBSID = ? ");
		}
		if(!Strings.isNullOrEmpty(productNo)){
			params.add(productNo);
			sql.append("AND I.PRODUCT_NO = ? ");
		}
		try {
			return jdbcTemplate.query(sql.toString(), params.toArray(), 
					ParameterizedBeanPropertyRowMapper.newInstance(CpeLockLacCiBean.class));
		} catch (Exception e) {
			log.error("",e);
			throw new Exception("根据CPE设备唯一号SUBSID查询已经锁网小区信息失败！");
		}
		
	}
	
	@Override
	public List<String> queryUserLocationsBySubsid(String subsid) 
			throws Exception{
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT USER_LOCATION");
		sql.append(" FROM CPE_USER_LOCK_REL WHERE SUBSID = ?");
		
		try {
			return jdbcTemplate.queryForList(sql.toString(), new Object[]{subsid},String.class);
		} catch (Exception e) {
			log.error(e);
			throw new Exception("根据CPE设备唯一号：" + subsid 
					+ "查询该CPE现在的锁网小区UserLocation集合失败！");
		}
	}
	
}
