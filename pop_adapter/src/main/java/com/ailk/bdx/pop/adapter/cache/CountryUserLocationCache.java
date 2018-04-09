package com.ailk.bdx.pop.adapter.cache;

import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import com.ailk.bdx.pop.adapter.util.SpringContext;

/**
 * 农村的userLocation缓存类
 * @author hpa
 *
 */
public class CountryUserLocationCache {
	private static Logger log = LogManager.getLogger();
	private final static String SELECT_SQL = "SELECT USER_LOCATION FROM DIM_CPE_LAC_CI ";
	private final ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();
	private final static ObjectOpenHashSet<String> countryUserLocationSet = new ObjectOpenHashSet<String>();
	
	static class CountryUserLocationCacheHolder{
		static CountryUserLocationCache instance = new CountryUserLocationCache();
	}
	
	private CountryUserLocationCache(){
		super();
	}
	
	public static CountryUserLocationCache getInstance(){
		return CountryUserLocationCacheHolder.instance;
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
			log.info("已成功将 " + countryUserLocationSet.size() + " 个农村小区的userLocation加载到缓存中！");
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
			log.info("已成功将 " + countryUserLocationSet.size() + " 个农村小区的userLocation加载到缓存中！");
			readWriteLock.writeLock().unlock();
		}
		
	}
	
	public ObjectOpenHashSet<String> loadData2Cache(){
		JdbcTemplate jdbcTemplate = 
				SpringContext.getBean("jdbcTemplate", JdbcTemplate.class);
		jdbcTemplate.query(SELECT_SQL, new RowMapper(){

			@Override
			public Object mapRow(ResultSet rs, int rowNum) 
					throws SQLException {
				String v = rs.getString("USER_LOCATION");
				if(!countryUserLocationSet.contains(v)){
					countryUserLocationSet.add(v);
				}
				return null;
			}
			
		});
		return countryUserLocationSet;
	}
	
	/**
	 * 判断是否包含该小区 userLocation
	 * @param v
	 * @return
	 */
	public boolean contains(String v){
		return countryUserLocationSet.contains(v);
	}
	
}
