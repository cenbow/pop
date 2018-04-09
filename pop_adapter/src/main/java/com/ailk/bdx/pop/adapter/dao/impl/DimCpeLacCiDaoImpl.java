package com.ailk.bdx.pop.adapter.dao.impl;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.ParameterizedBeanPropertyRowMapper;

import com.ailk.bdx.pop.adapter.common.dao.BaseJdbcDao;
import com.ailk.bdx.pop.adapter.dao.IDimCpeLacCiDao;
import com.ailk.bdx.pop.adapter.model.DimCpeLacCi;
import com.ailk.bdx.pop.adapter.util.SpringContext;

public class DimCpeLacCiDaoImpl extends BaseJdbcDao implements IDimCpeLacCiDao {
	private static final Logger log = LogManager.getLogger(DimCpeLacCiDaoImpl.class);
	@Override
	public List<DimCpeLacCi> queryByLacCi(String userLocation)throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT LAC_CI_HEX_CODE,LAC_CI_DEC_ID,LAC_HEX_CODE,LAC_DEC_ID,CI_HEX_CODE,CI_DEC_ID,CELL_NAME,STATION_CODE,USER_LOCATION ");
		sql.append(" FROM DIM_CPE_LAC_CI C");
		sql.append(" WHERE C.USER_LOCATION = ? ");
		JdbcTemplate jdbcTemplate = SpringContext.getBean("jdbcTemplate",JdbcTemplate.class);
		try {
			return jdbcTemplate.query(sql.toString(),
		    		ParameterizedBeanPropertyRowMapper.newInstance(DimCpeLacCi.class),userLocation);
		} catch (Exception e) {
			log.error(e);
			throw new Exception("判断CPE所在小区userLocation:" + userLocation + "是否归属农村基站失败！");
		}
		
	}

}
