package com.ai.bdx.pop.adapter.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import com.ai.bdx.pop.adapter.dao.IDimCpeLacCiDao;
import com.ai.bdx.pop.bean.CpeAccessibleLacCiBean;
import com.ai.bdx.pop.bean.DimCpeLacCi;
import com.google.common.base.Strings;

public class DimCpeLacCiDaoImpl implements IDimCpeLacCiDao {
	private static final Logger log = LogManager.getLogger(DimCpeLacCiDaoImpl.class);
	private JdbcTemplate jdbcTemplate;
	
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	@Override
	public int queryCountByLacCi(String lacCi)throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT COUNT(1) ");
		sql.append(" FROM DIM_CPE_LAC_CI C");
		sql.append(" WHERE C.LAC_CI_DEC_ID = ? ");
		try {
			return jdbcTemplate.queryForInt(sql.toString(), new Object[]{lacCi });
		} catch (Exception e) {
			log.error(e);
			throw new Exception("判断CPE所在小区lacCi:" + lacCi + "是否归属农村基站失败！");
		}
		
	}
	
	@Override
	public int query(DimCpeLacCi dimCpeLacCi) throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT * FROM ");
		return 0;
	}
	@Override
	public List<CpeAccessibleLacCiBean> queryAccessibleLacCi(
			CpeAccessibleLacCiBean cpeAccessibleLacCiBean) throws Exception {
		StringBuffer sql = new StringBuffer();
		List<Object> params = new ArrayList<Object>();
		sql.append("SELECT S.CITY_NAME,S.CITY_ID,S.COUNTY_NAME,S.COUNTY_ID,S.TOWN_NAME,");
		sql.append("S.COUNTRY_NAME,C.CELL_NAME,C.LAC_DEC_ID,C.USER_LOCATION ");
		sql.append("FROM CPE_USER_INFO I ");
		sql.append("inner JOIN DIM_CPE_STATION S ON I.CITY_NAME = S.CITY_NAME ");
		sql.append("inner JOIN DIM_CPE_LAC_CI C ON S.STATION_CODE = C.STATION_CODE ");
		sql.append("WHERE 1=1 ");
		if(!Strings.isNullOrEmpty(cpeAccessibleLacCiBean.getSubsid())){
			params.add(cpeAccessibleLacCiBean.getSubsid());
			sql.append("AND I.SUBSID = ? ");
		}
		if(!Strings.isNullOrEmpty(cpeAccessibleLacCiBean.getProductNo())){
			params.add(cpeAccessibleLacCiBean.getProductNo());
			sql.append("AND I.PRODUCT_NO = ? ");
		}
		if(!Strings.isNullOrEmpty(cpeAccessibleLacCiBean.getKeyword())){
			sql.append("AND (S.CITY_NAME LIKE '%").append(cpeAccessibleLacCiBean.getKeyword()).append("%' ");
			sql.append("OR S.TOWN_NAME LIKE '%").append(cpeAccessibleLacCiBean.getKeyword()).append("%' ");
			sql.append("OR S.COUNTRY_NAME LIKE '%").append(cpeAccessibleLacCiBean.getKeyword()).append("%' ");
			sql.append("OR C.CELL_NAME LIKE '%").append(cpeAccessibleLacCiBean.getKeyword()).append("%') ");
			/*params.add(cpeAccessibleLacCiBean.getKeyword());
			params.add(cpeAccessibleLacCiBean.getKeyword());
			params.add(cpeAccessibleLacCiBean.getKeyword());
			params.add(cpeAccessibleLacCiBean.getKeyword());*/
		}
		else{
			sql.append(" limit 20 ");
		}
		try {
			/*return jdbcTemplate.query(sql.toString(), params.toArray(), 
					ParameterizedBeanPropertyRowMapper.newInstance(CpeAccessibleLacCiBean.class));*/
			
			return jdbcTemplate.query(sql.toString(), params.toArray(), 
					new BeanPropertyRowMapper(CpeAccessibleLacCiBean.class));
		} catch (Exception e) {
			log.error("",e);
			throw new Exception("根据CPE设备唯一号SUBSID的已经锁网小区信息失败！");
		}
	}

	
}
