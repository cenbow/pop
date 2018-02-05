package com.ai.bdx.pop.adapter.dao.impl;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.ParameterizedBeanPropertyRowMapper;

import com.ai.bdx.pop.adapter.dao.IDimCpeStationDao;
import com.ai.bdx.pop.bean.DimCpeStation;

public class DimCpeStationDaoImpl implements IDimCpeStationDao{
	private static final Logger log = LogManager.getLogger(DimCpeLacCiDaoImpl.class);
	private JdbcTemplate jdbcTemplate;
	
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public List<DimCpeStation> queryPosition(String userLocation) throws Exception{
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT DISTINCT S.CITY_NAME," +
				"S.COUNTY_NAME " +
				" FROM DIM_CPE_LAC_CI L " +
				"RIGHT JOIN DIM_CPE_STATION S " +
				"ON L.STATION_CODE = S.STATION_CODE WHERE L.USER_LOCATION = ?");
		try{
			return jdbcTemplate.query(sql.toString(),new Object[]{userLocation}, 
					ParameterizedBeanPropertyRowMapper.newInstance(DimCpeStation.class));
		}catch (Exception e) {
			log.error("",e);
			throw new Exception("根据CPE设备唯一号：" + userLocation + "查询CPE用户信息失败！");
		}
	}
}
