package com.ai.bdx.pop.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.ParameterizedBeanPropertyRowMapper;


import com.ai.bdx.pop.dao.CpeSelectSubsidDao;
import com.ai.bdx.pop.util.CpeUserInfo;
import com.ai.bdx.pop.util.PopConstant;
import com.ai.bdx.pop.util.VerifyUtil;

public class CpeSelectSubsidDaoImpl implements CpeSelectSubsidDao{
	
	private JdbcTemplate jdbcTemplate;

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<CpeUserInfo> selectCpe(String subsid, String productNo,String cityName, int busiType) {
		StringBuffer sql = new StringBuffer();
		List<Object> obj = new ArrayList<Object>();
		List<CpeUserInfo> infolist = new ArrayList<CpeUserInfo>();
		
			sql.append("select * from (SELECT a.subsid,a.product_no,a.city_name,n.TOWN_NAME,a.county_name,");
			sql.append("a.country_name,a.busi_type,a.user_status,a.create_time,");
			sql.append("a.net_type,c.CELL_NAME as count FROM cpe_user_info a LEFT JOIN ");
			sql.append(" cpe_user_lock_rel b ON a.subsid = b.subsid left JOIN dim_cpe_lac_ci c ");
			sql.append("ON b.user_location = c.user_location LEFT JOIN dim_cpe_station n ON c.STATION_CODE = n.STATION_CODE WHERE 1=1 ");

			//CPE号不为空其他为空
			if(!VerifyUtil.isBlankStr(subsid) ){
				sql.append(" and a.SUBSID like ? ");
				obj.add(PopConstant.PERCENT+subsid+PopConstant.PERCENT);
			}
			//手机号不为空其他为空
			if(!VerifyUtil.isBlankStr(productNo) ){
				sql.append(" and a.PRODUCT_NO like ? ");
				obj.add(PopConstant.PERCENT+productNo+PopConstant.PERCENT);
			}
			//所属城市不为空其他为空
			if(!VerifyUtil.isBlankStr(cityName) && !cityName.equals("0")){
				sql.append(" and a.CITY_NAME = ? ");
				obj.add(cityName);
			}
			//业务状态不为空其他为空
			if(busiType == PopConstant.BUSI_TYPE_NORMAL 
					|| busiType == PopConstant.BUSI_TYPE_LIMIT_FOR_LOCKED
					|| busiType == PopConstant.BUSI_TYPE_LIMIT_FOR_CITY){
				sql.append(" and a.BUSI_TYPE = ? ");
				obj.add(busiType);
			}
			sql.append(" ) q where SUBSID is not null;");
			System.out.println(sql.toString());
			infolist = jdbcTemplate.query(sql.toString(),obj.toArray(),ParameterizedBeanPropertyRowMapper.newInstance(CpeUserInfo.class));
//			infolist = jdbcTemplate.queryForList(sql.toString(),new Object[]{cityName,busiType},CpeUserInfo.class);
			return infolist;

	}

}
