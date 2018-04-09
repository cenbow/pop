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
 * CPE的PhoneNo缓存类
 * @author hpa
 *
 */
public class CpePhoneCache {
	private static Logger log = LogManager.getLogger();
	private final static String SELECT_SQL = "SELECT PRODUCT_NO FROM CPE_USER_INFO ";
	private final ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();
	private final static ObjectOpenHashSet<String> cpeUserInfoSet = new ObjectOpenHashSet<String>();;
	
	static class CpePhoneCacheHolder{
		static CpePhoneCache instance = new CpePhoneCache();
	}
	
	private CpePhoneCache(){
		super();
	}
	
	public static CpePhoneCache getInstance(){
		return CpePhoneCacheHolder.instance;
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
			log.info("已成功从CPE用户信息表中成功将 " + cpeUserInfoSet.size() + " 个CPE的手机号码（PhoneNo）加载到缓存中！");
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
			log.info("已成功从CPE用户信息表中成功将 " + cpeUserInfoSet.size() + " 个CPE的手机号码（PhoneNo）加载到缓存中！");
			readWriteLock.writeLock().unlock();
		}
		
	}
	
	public ObjectOpenHashSet<String> loadData2Cache(){
		//cpeUserInfoMap2 = new Byte2ObjectOpenHashMap<Short2ObjectOpenHashMap<Short2ObjectOpenHashMap>>(1);
		JdbcTemplate jdbcTemplate = 
				SpringContext.getBean("jdbcTemplate", JdbcTemplate.class);
		jdbcTemplate.query(SELECT_SQL, new RowMapper(){

			@Override
			public Object mapRow(ResultSet rs, int rowNum) 
					throws SQLException {
				String v = rs.getString("PRODUCT_NO");
				if(!cpeUserInfoSet.contains(v)){
					cpeUserInfoSet.add(v);
				}
				return null;
			}
			
		});
		return cpeUserInfoSet;
	}
	
	/**
	 * 判断是否包含该CPE phoneNo
	 * @param v
	 * @return
	 */
	public boolean contains(String v){
		return cpeUserInfoSet.contains(v);
	}
	
	public static void main(String[] args){
		CpePhoneCache.getInstance().cpeUserInfoSet.add("18872489608");
		
		System.out.println(CpePhoneCache.getInstance().cpeUserInfoSet.get("18872489608"));
	}
	
}
