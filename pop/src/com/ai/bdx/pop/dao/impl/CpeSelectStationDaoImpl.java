package com.ai.bdx.pop.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.ParameterizedBeanPropertyRowMapper;

import com.ai.bdx.pop.bean.DimCpeStation;
import com.ai.bdx.pop.dao.CpeSelectStationDao;
import com.ai.bdx.pop.util.PopConstant;
import com.ai.bdx.pop.util.VerifyUtil;

public class CpeSelectStationDaoImpl implements CpeSelectStationDao{
	
	private JdbcTemplate jdbcTemplate;

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<DimCpeStation> selectStation(String stationName, String cityName) {
		
		List<Object> obj = new ArrayList<Object>();
		List<DimCpeStation> list = new ArrayList<DimCpeStation>();

			String sql = "select a.city_name,a.county_name,d.town_name,a.country_name,d.STATION_NAME," +
					"sum(case when a.busi_type = 1 then 1 else 0 end) busiType1," +
					"sum(case when a.net_lock_flag = 1 then 1 else 0 end) net_lock_flag," +
					"sum(case when a.busi_type = 0 then 1  else 0 end) busi_type," +
					"group_concat(ci.CELL_NAME) as count from dim_cpe_station d INNER JOIN " +
					"dim_cpe_lac_ci ci ON d.STATION_CODE = ci.STATION_CODE INNER JOIN  cpe_user_lock_rel c ON " +
					"ci.LAC_CI_DEC_ID = c.locked_lac_ci INNER JOIN" +
					" cpe_user_info a ON c.product_no = a.product_no where 1 = 1 ";

			//基站名不为空其他为空
			if(!VerifyUtil.isBlankStr(stationName)){
				sql += " and d.STATION_NAME like ? ";
				obj.add(PopConstant.PERCENT+stationName+PopConstant.PERCENT);
			}
			//城市不为空其他为空
			if(!VerifyUtil.isBlankStr(cityName) && !cityName.equals("0")){
				sql += "and d.city_name = ? ";
				obj.add(cityName);
			}
			sql += "group by c.product_no;";
			
			
			list = (List<DimCpeStation>) jdbcTemplate.query(sql,obj.toArray(),ParameterizedBeanPropertyRowMapper.newInstance(DimCpeStation.class));
			return list;
	}

}
