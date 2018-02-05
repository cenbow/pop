package com.ai.bdx.pop.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.ParameterizedBeanPropertyRowMapper;

import com.ai.bdx.pop.bean.DimCpeStation;
import com.ai.bdx.pop.dao.CpeSelectCountryDao;
import com.ai.bdx.pop.util.PopConstant;
import com.ai.bdx.pop.util.VerifyUtil;

public class CpeSelectCountryDaoImpl implements CpeSelectCountryDao{
	
	private JdbcTemplate jdbcTemplate;

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<DimCpeStation> selectCountry(String countryName, String cityName) {
		
		List<Object> obj = new ArrayList<Object>();
		List<DimCpeStation> list = new ArrayList<DimCpeStation>();

			String sql = "SELECT m.station_code,m.city_name,m.county_name,m.country_name,m.town_name,m.STATION_NAME," +
					"m.busitype1,m.net_lock_flag,m.busi_type,GROUP_CONCAT(M.CELL_NAME) as count" +
					" FROM (SELECT d.station_code as station_code,d.city_name AS city_name,d.county_name AS county_name," +
					"c.country_name AS country_name,d.STATION_NAME AS STATION_NAME," +
					"ci.CELL_NAME AS CELL_NAME,d.TOWN_NAME as TOWN_NAME," +
					"sum(case when c.busi_type = 1 then 1 else 0 end) AS busiType1," +
					"sum(case when c.net_lock_flag = 1 then 1 else 0 end) AS net_lock_flag," +
					"sum(case when c.busi_type = 0 then 1 else 0 end) AS busi_type " +
					"FROM dim_cpe_station d  LEFT JOIN dim_cpe_lac_ci ci on d.station_code = ci.station_code " +
					"LEFT JOIN cpe_user_lock_rel r ON ci.LAC_CI_DEC_ID = r.locked_lac_ci " +
					" LEFT JOIN cpe_user_info c ON " +
					"r.product_no = c.product_no  WHERE 1 = 1 ";

			//村庄名不为空其他为空
			if(!VerifyUtil.isBlankStr(countryName)){
				sql += " and c.country_name like ? ";
				obj.add(PopConstant.PERCENT+countryName+PopConstant.PERCENT);
			}
			//城市不为空其他为空
			if(!VerifyUtil.isBlankStr(cityName) && !cityName.equals("0")){
				sql += "and d.city_name = ? ";
				obj.add(cityName);
			}
			sql += "group by c.busi_type,c.net_lock_flag ORDER BY c.create_time" +
					" DESC LIMIT 3) AS M having m.station_code is not null;";
			System.out.println(sql);
			
			list = (List<DimCpeStation>) jdbcTemplate.query(sql,obj.toArray(),ParameterizedBeanPropertyRowMapper.newInstance(DimCpeStation.class));
			return list;
	}
}
