package com.ai.bdx.pop.dao.impl;


import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.ai.bdx.pop.dao.CpeBroadbandDao;
import com.ai.bdx.pop.util.CpeUserInfo;

/**
 * CPE用户宽带变更DAO实现类
 * @author 
 *
 */
@Repository
public class CpeBroadbandDaoImpl implements CpeBroadbandDao {
	@Resource
	private JdbcTemplate jdbcTemplate;
	
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	private static final Logger log = LogManager.getLogger();
	@Override
	/**
	 * 批量更新用户宽带
	 */
	public int[] updateCpeNetType(final List<CpeUserInfo> list)throws Exception{
		
		int[] updates=null;
		String sql="update cpe_user_info set net_type = ?,op_status = 6 where subsid = ?";

			if(list!=null){
				List<Object[]> listArgs = new ArrayList<Object[]>();
				updates=new int[list.size()];
				
					if(list.size()==1){
							Object[] args=new Object[2];
							
							args[0]=list.get(0).getNetType();
							args[1]=list.get(0).getSubsid();
							listArgs.add(args);
							
						try {
							updates[0]=jdbcTemplate.update(sql, args);
						} catch (DataAccessException e) {
							System.out.println("宽带变更用户状态失败了"+args[1]);
							log.error("宽带变更用户状态失败了"+args[1]);
							e.printStackTrace();
							throw new Exception("宽带变更用户状态失败了"+args[1]);
						}
					}else{	
							updates=jdbcTemplate.batchUpdate(sql,  new BatchPreparedStatementSetter(){
								@Override
								public int getBatchSize() {
									return 	list.size();
								}
								@Override
								public void setValues(PreparedStatement ps, int i)
										throws SQLException {
									ps.setString(1, list.get(i).getNetType());
									ps.setString(2, list.get(i).getSubsid());
								}
							});
				}
				sql="select * from cpe_user_info where net_type!=null";
				List<Map<String,Object>> queryForList = jdbcTemplate.queryForList(sql);
		
				for (Map<String, Object> map : queryForList) {
					System.out.println("cpe用户设备标识"+map.get("subsid")+"用户状态："+map.get("net_type"));
				}
				
			}
		return updates;
	}
	/**
	 * 根据CPE设备唯一id=subsid查询对应用户信息
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<String> queryCpeUserInfoBySubsid(String subsid) {
		List<String> list =null;
		String sql="select user_location from cpe_user_lock_rel where subsid=?";
		list = jdbcTemplate.queryForList(sql, new String[]{subsid},(String.class));

		if(list!=null){
			return list;
		}else{
			return null;
		}
	
	}
	
	/**
	 * 根据cep查询旧的策略号
	 */
	@SuppressWarnings("unchecked")
	public String queryStrategyCodeBySubsid(String subsid){
		List<Map<String,String>> list = null;
		String sql="select r.strategy_code from cpe_user_info a,dim_net_type_strategy_rel r  where a.net_type = r.net_type and subsid=?";
		list = jdbcTemplate.queryForList(sql, new String[]{subsid});
		if(list.size()==1){
			return  list.get(0).get("strategy_code");
		}else{
			return null;
		}
	}

	/**
	 * 根据cep查询新的策略号
	 */
	@SuppressWarnings("unchecked")
	public String queryStrategyCodeBynetType(String net_type){
		List<Map<String,String>> list = null;
		String sql="select r.strategy_code from dim_net_type_strategy_rel r  where  r.net_type=?";
		list = jdbcTemplate.queryForList(sql, new String[]{net_type});
		if(list.size()==1){
			return  list.get(0).get("strategy_code");
		}else{
			return null;
		}
	}
}
