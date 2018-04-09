package com.ailk.bdx.pop.adapter.dao.impl;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.ParameterizedBeanPropertyRowMapper;

import com.ailk.bdx.pop.adapter.common.dao.BaseJdbcDao;
import com.ailk.bdx.pop.adapter.dao.ICpeUserInfoDao;
import com.ailk.bdx.pop.adapter.model.custom.CpeLockNetInfo;
import com.ailk.bdx.pop.adapter.util.SpringContext;

public class CpeUserInfoDaoImpl  extends BaseJdbcDao implements ICpeUserInfoDao {
	private static final Logger log = LogManager.getLogger(CpeUserInfoDaoImpl.class);
	@Override
	public List<CpeLockNetInfo> queryBySubsid(String subsid) throws Exception{
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT C.SUBSID, ");
		sql.append("        C.PRODUCT_NO, ");
		//sql.append("        C.NET_TYPE, ");
		//sql.append("        C.COUNTRY_NAME,");
		//sql.append("        C.COUNTY_NAME, ");
		//sql.append("        C.CITY_NAME, ");
		sql.append("        C.PLAN_CODE, ");
		//sql.append("        C.USER_STATUS, ");
		//sql.append("        C.NET_LOCK_FLAG, ");
		//sql.append("        C.BUSI_TYPE, ");
		//sql.append("        C.OP_STATUS, ");
		sql.append("        R.STRATEGY_CODE AS POLICY_NO");
		sql.append("   FROM CPE_USER_INFO C ");
		sql.append(" LEFT JOIN DIM_NET_TYPE_STRATEGY_REL R ");
		sql.append(" ON C.NET_TYPE = R.NET_TYPE");
		sql.append("  WHERE C.SUBSID = ? ");
		/*try {
			return (CpeLockNetInfo) this.getJdbcTemplate().query.query(sql.toString(),new Object[] {subsid},
		    		new RowMapper(){

						@Override
						public Object mapRow(ResultSet rs, int rowNum)
								throws SQLException {
							if(rs != null){
								CpeLockNetInfo cpeLockNetInfo = new CpeLockNetInfo();
								cpeLockNetInfo.setSubsid(rs.getString("PRODUCT_NO"));
								cpeLockNetInfo.setProductNo("PRODUCT_NO");
								cpeLockNetInfo.setPolicyNo("STRATEGY_CODE");
								cpeLockNetInfo.setPlanCode("PLAN_CODE");
								return cpeLockNetInfo;
							}else{
								return null;
							}
						}
			
		});
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		}*/
		try {
			JdbcTemplate jdbcTemplate = SpringContext.getBean("jdbcTemplate",JdbcTemplate.class);
			return jdbcTemplate.query(sql.toString(),
					ParameterizedBeanPropertyRowMapper.
					newInstance(CpeLockNetInfo.class),subsid);
		} catch (Exception e) {
			log.error(e);
			throw new Exception("根据CPE设备号：" + subsid + "查询设备信息失败！");
		}
		
	}

}
