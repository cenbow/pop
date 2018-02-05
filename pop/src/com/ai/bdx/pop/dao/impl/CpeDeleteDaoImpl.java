 package com.ai.bdx.pop.dao.impl;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.SQLNonTransientException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;


import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.ai.bdx.pop.dao.CpeDeleteDao;
import com.asiainfo.biapp.pop.Exception.FileErrorException;
import com.asiainfo.biapp.pop.Exception.MysqlDataAccessExcetion;
import com.asiainfo.biapp.pop.model.CpeUserInfo;
import com.asiainfo.biapp.pop.util.TimeUtil;
/**
 * CPE用户销户DAO实现类
 * @author 林
 *
 */

public class CpeDeleteDaoImpl implements CpeDeleteDao {

	private JdbcTemplate jdbcTemplate;
	private static final Logger log = LogManager.getLogger();

	/**
	 * 批量更新用户状态2或3
	 */
	public int[] updateCpeUserStatus(final List<CpeUserInfo> list)throws MysqlDataAccessExcetion,FileErrorException, Exception{
		
		int[] updates=null;
		String sql="update cpe_user_info set user_status=? ,net_lock_flag=0 ,status_change_time=? where subsid=?";

			if(list!=null){
				List<Object[]> listArgs = new ArrayList<Object[]>();
				updates=new int[list.size()];
				
					if(list.size()==1){
							Object[] args=new Object[3];
							
							args[0]=list.get(0).getUserStatus();
							args[1]=TimeUtil.getDateString(new Date(), "yyyy-MM-dd HH:mm:ss");
							args[2]=list.get(0).getSubsid();
							listArgs.add(args);
							
						try {
							updates[0]=jdbcTemplate.update(sql, args);
						}catch (DataAccessException e) {
							log.error("销户变更用户状态失败了:数据库访问异常");
							e.printStackTrace();
							throw new MysqlDataAccessExcetion("销户变更用户状态失败了：数据库访问异常"+args[2]);
						}catch (Exception e) {
							log.error("销户变更用户状态失败了"+args[2]);
							e.printStackTrace();
							throw new FileErrorException("销户变更用户状态失败了"+args[2]);
						}
					}else{	
						try{
							updates=jdbcTemplate.batchUpdate(sql,  new BatchPreparedStatementSetter(){
		
								@Override
								public int getBatchSize() {
								
									return 	list.size();
								}
		
								@Override
								public void setValues(PreparedStatement ps, int i)
										throws SQLException {
									ps.setInt(1, list.get(i).getUserStatus());
									ps.setString(2, TimeUtil.getDateString(new Date(), "yyyy-MM-dd HH:mm:ss"));
									ps.setString(3, list.get(i).getSubsid());
									
								}
								
							});
						}catch (DataAccessException e) {
							log.error("销户变更用户状态失败了:数据库访问异常");
							e.printStackTrace();
							throw new MysqlDataAccessExcetion("销户变更用户状态失败了：数据库访问异常");
						}catch (Exception e) {
							log.error("销户变更用户状态失败了:"+e.getMessage());
							e.printStackTrace();
							throw new FileErrorException("销户变更用户状态失败了:"+e.getMessage());
						}
				}
				sql="select * from cpe_user_info where user_status=2 or user_status=3";
				List<Map<String,Object>> queryForList = jdbcTemplate.queryForList(sql);
		
				for (Map<String, Object> map : queryForList) {
					System.out.println("cpe用户设备标识"+map.get("subsid")+"用户状态："+map.get("user_status"));
				}
				
			}
		return updates;
	}
	/**
	 * 根据CPE设备唯一id=subsid查询对应用户信息
	 */
	@Override
	public CpeUserInfo queryCpeUserInfoBySubsid(String subsid) {
		List<CpeUserInfo> list =null;
		String sql="select * from cpe_user_info where subsid=?";
		list = jdbcTemplate.queryForList(sql, new String[]{subsid},CpeUserInfo.class );
		if(list!=null){
			return list.get(0);                            
		}else{
			return null;
		}
	
	}
	//根据用户id查询用户的策略号（带宽与之对应的那个）
	public List queryStrategyCodeBySubsid(String subsid)throws MysqlDataAccessExcetion{
		String sql="select * from cpe_user_info u join dim_net_type_strategy_rel r on u.net_type=r.net_type where u.subsid=?";
		List<Map<String,Object>> list = null;
	
		try {
			list = jdbcTemplate.queryForList(sql, new String[]{subsid});
		} catch (DataAccessException e) {
			e.printStackTrace();
			throw new MysqlDataAccessExcetion("查询用户策略号，数据库操作错误");
		}
		
		return list;
	}
	@Override
	public int deleteCpeUser(CpeUserInfo cui) throws 
			MysqlDataAccessExcetion {
		String sql="update cpe_user_info set user_status=? ,net_lock_flag=0 ,status_change_time=? where subsid=?";
		int update=0;
		Object[] args=new Object[3];
		args[0]=cui.getUserStatus();
		args[1]=TimeUtil.getDateString(new Date(), "yyyy-MM-dd HH:mm:ss");
		args[2]=cui.getSubsid();
		try {
			update=jdbcTemplate.update(sql, args);
		}catch (DataAccessException e) {
			log.error("销户变更用户状态失败了:数据库访问异常");
			e.printStackTrace();
			throw new MysqlDataAccessExcetion("销户变更用户状态失败了：数据库访问异常"+args[2]);
		}catch (Exception e) {
			log.error("销户变更用户状态失败了"+args[1]);
			e.printStackTrace();
	//		throw new Exception("销户变更用户状态失败了"+args[2]);
		}
		return update;
	}
	
	@Override
	public void deleteUserLockedInfo(CpeUserInfo cui) {
		String sql="delete from cpe_user_lock_rel where subsid=? and product_no=?";
		try{
			jdbcTemplate.update(sql, new String[]{cui.getSubsid(),cui.getProductNo()});
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	

}
