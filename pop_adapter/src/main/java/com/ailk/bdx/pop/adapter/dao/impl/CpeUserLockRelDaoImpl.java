package com.ailk.bdx.pop.adapter.dao.impl;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.ParameterizedBeanPropertyRowMapper;

import com.ailk.bdx.pop.adapter.common.dao.BaseJdbcDao;
import com.ailk.bdx.pop.adapter.dao.ICpeUserLockRelDao;
import com.ailk.bdx.pop.adapter.model.CpeUserLockRel;
import com.ailk.bdx.pop.adapter.util.SpringContext;

public class CpeUserLockRelDaoImpl extends BaseJdbcDao 
	implements ICpeUserLockRelDao {
	private static final Logger log = LogManager.
			getLogger(CpeUserLockRelDaoImpl.class);
	@Override
	public boolean insert(CpeUserLockRel cpeUserLockRel) 
			throws Exception{
		StringBuffer   sql = new StringBuffer();
		sql.append(" INSERT INTO CPE_USER_LOCK_REL( ");
		sql.append(" SUBSID, ");    
		sql.append(" PRODUCT_NO, ");
		sql.append(" LOCKED_LAC_CI, ");
		sql.append(" USER_LOCATION , ");
		sql.append(" STATION_CODE )  VALUES (?,?,?,?,?)");
		JdbcTemplate jdbcTemplate = SpringContext.
				getBean("jdbcTemplate",JdbcTemplate.class);
		try {
			int  resInt = jdbcTemplate. update(sql.toString(), new Object[]{
				cpeUserLockRel.getSubsid(),
				cpeUserLockRel.getProductNo(),
				cpeUserLockRel.getLockedLacCi(),
				cpeUserLockRel.getUserLocation(),
				cpeUserLockRel.getStationCode()
			}); 
			return resInt>0;
		} catch (Exception e) {
			log.error(e);
			throw new Exception("添加CPE用户锁网小区关系失败！");
		}
	}

	@Override
	public List<CpeUserLockRel> queryBySubsid(String subsid) 
			throws Exception{
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT SUBSID,PRODUCT_NO,LOCKED_LAC_CI," +
				"USER_LOCATION,STATION_CODE");
		sql.append(" FROM CPE_USER_LOCK_REL WHERE SUBSID = ?");
		JdbcTemplate jdbcTemplate = SpringContext.
				getBean("jdbcTemplate",JdbcTemplate.class);
		try {
			return jdbcTemplate.query(sql.toString(), 
					ParameterizedBeanPropertyRowMapper.
					newInstance(CpeUserLockRel.class), subsid);
		} catch (Exception e) {
			log.error(e);
			throw new Exception("根据CPE设备唯一号：" + subsid 
					+ "查询CPE用户锁网小区关系失败！");
		}
	}

	@Override
	public List<String> queryUserLocationsBySubsid(String subsid) 
			throws Exception{
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT USER_LOCATION");
		sql.append(" FROM CPE_USER_LOCK_REL WHERE SUBSID = ?");
		JdbcTemplate jdbcTemplate = SpringContext.getBean("jdbcTemplate",JdbcTemplate.class);
		
		try {
			return jdbcTemplate.queryForList(sql.toString(), String.class,new Object[]{subsid});
		} catch (Exception e) {
			log.error(e);
			throw new Exception("根据CPE设备唯一号：" + subsid 
					+ "查询该CPE现在的锁网小区UserLocation集合失败！");
		}
	}

}
