package com.ailk.bdx.pop.adapter.dao.impl;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;

import com.ailk.bdx.pop.adapter.common.dao.BaseJdbcDao;
import com.ailk.bdx.pop.adapter.dao.IDimCpeStationDao;
import com.ailk.bdx.pop.adapter.util.SpringContext;

/**
 * 农村基站维表Dao实现类
 * @author hpa
 *
 */
public class DimCpeStationDaoImpl extends BaseJdbcDao implements IDimCpeStationDao {
	private static final Logger log = LogManager.getLogger(DimCpeStationDaoImpl.class);
	@Override
	public Integer queryByStationCode(String stationCode)throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT COUNT(1) ");
		sql.append(" FROM DIM_CPE_STATION S");
		sql.append(" WHERE S.STATION_CODE = ? ");
		JdbcTemplate jdbcTemplate = SpringContext.
				getBean("jdbcTemplate",JdbcTemplate.class);
		try {
			return jdbcTemplate.queryForInt(sql.toString(), new Object[]{stationCode});
		} catch (Exception e) {
			log.error(e);
			throw new Exception("根据农村基站stationCode：" 
					+ stationCode + "查询符合基站的个数失败！");
		}
		
	}

}
