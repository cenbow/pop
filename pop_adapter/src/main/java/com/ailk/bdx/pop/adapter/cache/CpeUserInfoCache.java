package com.ailk.bdx.pop.adapter.cache;


import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import com.ailk.bdx.pop.adapter.common.dao.constant.Constants;
import com.ailk.bdx.pop.adapter.util.SpringContext;

/**
 * CPE的设备唯一号（SUBSID）缓存类
 * @author hpa
 *
 */
public class CpeUserInfoCache {
	private final static Logger log;
	private final static String SELECT_SQL;
	private final ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();
	private final static Object2ObjectOpenHashMap<String,Object2ObjectOpenHashMap<String,String>> cpeUserInfoMap;
	private JdbcTemplate jdbcTemplate = SpringContext.getBean("jdbcTemplate", JdbcTemplate.class);
	
	static{
		log = LogManager.getLogger();
		SELECT_SQL = "SELECT SUBSID, BUSI_TYPE, NET_LOCK_FLAG, USER_STATUS FROM CPE_USER_INFO where user_status != " + Constants.USER_STATUS_CANCEL;
		cpeUserInfoMap = new Object2ObjectOpenHashMap<String,Object2ObjectOpenHashMap<String,String>>(1);
	}
	
	static class CpeUserInfoCacheHolder{
		static CpeUserInfoCache instance = new CpeUserInfoCache();
	}
	
	private CpeUserInfoCache(){
		super();
	}
	
	public static CpeUserInfoCache getInstance(){
		return CpeUserInfoCacheHolder.instance;
	}
	
	public boolean init(){
		boolean result  = true;
		try{
			readWriteLock.writeLock().lock();
			loadData2Cache();
		}catch(Exception e){
			log.error("",e);
			result = false;
		}finally{
			log.info("已成功从CPE用户信息表中成功将 " + cpeUserInfoMap.size() + " 个CPE的设备唯一号（SUBSID）加载到缓存中！");
			readWriteLock.writeLock().unlock();
		}
		
		return result;
	}
	
	public void runTask(){
		try{
			readWriteLock.writeLock().lock();
			loadData2Cache();
		}catch(Exception e){
			log.error("",e);
		}finally{
			log.info("已成功从CPE用户信息表中成功将 " + cpeUserInfoMap.size() + " 个CPE的设备唯一号（SUBSID）加载到缓存中！");
			readWriteLock.writeLock().unlock();
		}
		
	}
	
	public Object2ObjectOpenHashMap<String,Object2ObjectOpenHashMap<String,String>> loadData2Cache(){
		jdbcTemplate.query(SELECT_SQL, new RowMapper(){
			Object2ObjectOpenHashMap<String,String> map;
			@Override
			public Object mapRow(ResultSet rs, int rowNum) 
					throws SQLException {
				map = new Object2ObjectOpenHashMap<String,String>(1);
				String k = rs.getString("SUBSID");
				
				map.put("BUSI_TYPE",rs.getString("BUSI_TYPE"));
				map.put("NET_LOCK_FLAG",rs.getString("NET_LOCK_FLAG"));
				/*if(!cpeUserInfoMap.containsKey(k)){
					cpeUserInfoMap.put(k, map);
				}*/
				cpeUserInfoMap.put(k, map);
				return null;
			}
			
		});
		return cpeUserInfoMap;
	}
	
	/**
	 * 判断是否包含该CPE subsid
	 * @param v
	 * @return
	 */
	public boolean containsKey(String k){
		return cpeUserInfoMap.containsKey(k);
	}
	
	/**
	 * 根据CPE用户编码查询签约策略
	 * @param subsid
	 * @return
	 */
	public Object2ObjectOpenHashMap<String, String> get(String subsid){
		return cpeUserInfoMap.get(subsid);
	}
	
}
