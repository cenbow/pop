package com.asiainfo.biapp.pop.redis;

import java.rmi.ConnectException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ailk.bdx.pop.adapter.jedis.JedisClusterFactory;
import com.ailk.bdx.pop.adapter.util.ConfigDataUtil;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;
import redis.clients.jedis.exceptions.JedisConnectionException;
import redis.clients.jedis.exceptions.JedisDataException;
import redis.clients.jedis.exceptions.JedisException;

public class RedisClient {
	private static Jedis jedis=null;//非切片额客户端连接
    private static JedisPool jedisPool;//非切片连接池
    private static ShardedJedis shardedJedis;//切片额客户端连接
    private ShardedJedisPool shardedJedisPool;//切片连接池
    private static Logger log = LogManager.getLogger();
   
    /**
     * 获得jedis
     * 
     */
    public RedisClient()throws JedisConnectionException{
    	genJedis();
    }
    public  Jedis genJedis()throws JedisConnectionException{
    	
    		try {
				initialPool();
		    	jedis=jedisPool.getResource();
			} catch (JedisConnectionException e) {
				e.printStackTrace();
				throw new JedisConnectionException("redis连接异常"+e.getMessage());
			}catch (Exception e) {
				e.printStackTrace();
			}
    		if(jedis!=null){
    			log.info("redis初始化结束"+jedis.toString());
    		}else{
    			log.info("redis初始化失败");
    		}
    	
    		
    		return jedis;
    }
    /**
     * 初始化非切片池
     */
    private static void initialPool() 
    { 
    	log.info("redis初始化开始");
        // 池基本配置 
    	GenericObjectPoolConfig config = new GenericObjectPoolConfig();
        config.setMinEvictableIdleTimeMillis(5000);
		config.setMaxWaitMillis(Long.parseLong(ConfigDataUtil.getRedisPropVal("redis.cluster.pool.MaxWaitMillis")));
		//最大连接数, 默认8个
		config.setMaxTotal(Integer.parseInt(ConfigDataUtil.getRedisPropVal("redis.cluster.pool.MaxTotal")));
		//最小空闲连接数, 默认0
		config.setMinIdle(Integer.parseInt(ConfigDataUtil.getRedisPropVal("redis.cluster.pool.MinIdle")));
		//最大空闲连接数, 默认8个
		config.setMaxIdle(Integer.parseInt(ConfigDataUtil.getRedisPropVal("redis.cluster.pool.setMaxIdle")));
		//逐出扫描的时间间隔(毫秒) 如果为负数,则不运行逐出线程, 默认-1
		config.setTimeBetweenEvictionRunsMillis(5000);
        
        jedisPool = new JedisPool(config,ConfigDataUtil.getRedisPropVal("redis.single.pool.ip"),Integer.parseInt(ConfigDataUtil.getRedisPropVal("redis.single.pool.port")),Integer.parseInt(ConfigDataUtil.getRedisPropVal("redis.cluster.pool.timeout")));
        
        
    }
    /** 
     * 初始化切片池 
     */ 
    private void initialShardedPool() 
    { 
        // 池基本配置 
        JedisPoolConfig config = new JedisPoolConfig(); 
        config.setMaxIdle(20); 
        config.setMaxIdle(5); 
        config.setMaxWaitMillis(5000l); 
        config.setTestOnBorrow(false); 
        // slave链接 
        List<JedisShardInfo> shards = new ArrayList<JedisShardInfo>(); 
        shards.add(new JedisShardInfo("192.168.17.134", 6379, "master")); 

        // 构造池 
        shardedJedisPool = new ShardedJedisPool(config, shards); 
    } 


	
 
     private void StringOperate() {
        
     }

     private void ListOperate() {
        
     }

     private void SetOperate() {
        
     }

     private void SortedSetOperate() {
      
     }
   
     private void HashOperate() {
        
     }

 	/**增/改
 	 * 增加键值对--直接覆盖原始key-value
 	 */
 	public   String setKeyValue(String key,String value){
 		
 		 String rst=null;
		try {
			if(jedis!=null){
				rst = jedis.set(key, value);
			}
			
		} catch (Exception e) {
			
			e.printStackTrace();
		}
 		 return rst;
 	}
 	/**
 	 * 根据key查询对应的value
 	 * @param key
 	 * @return
 	 */
 	public  String getValue(String key){
 	
 		String value=null;
		try {
			if(jedis!=null){
				value = jedis.get(key);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
 		return value;
 	}
 	/**
 	 * 删除键值对
 	 */
 	public  Long delKeyValue(String key)throws JedisConnectionException{
 	
 		Long count=null;
		try {
			if(jedis!=null){
				count=0L;
				Boolean exists = jedis.exists(key);
				System.out.println("exists:"+exists);
				if(exists){
					count = jedis.del(key);
				}
			}
		} catch(JedisConnectionException e){
			e.printStackTrace();
			throw new JedisConnectionException(e);
		}catch (Exception e) {
			// TODO Auto-generated catch block
		}
 		return count;
 	}
 	
 	/**
 	 * 新增一个key--多个value
 	 */
 	public  Long addKeyValues(String key,String... values){
 	
 		Long count=null;
		try {
			if(jedis!=null){
				count = jedis.sadd(key, values);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
 		return count;
 	}
 	
 	/**
 	 * 获取keys1、keys2、...对应的value1、value2 ...
 	 * @param keys
 	 * @return
 	 */
 	public  List<String> getListLAC_CI(String... keys){
 		
 		 List<String> lac_cis=null;
		try {
			if(jedis!=null){
				lac_cis = jedis.mget(keys);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
 		return lac_cis;
 	}
 	
 	/**
 	 * 获得set集合sets的所有元素
 	 * @param mobile
 	 * @return
 	 */
 	public  Set<String> getSetLAC_CI(String sets){
 		
 		Set<String> lac_cis=null;
		try {
			if(jedis!=null){
				lac_cis = jedis.smembers(sets);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
 		return lac_cis;
 	}
 	
 	/**
 	 * 删除sets集合中某个(些)元素
 	 */
 	public  Long  delSetValues(String sets,String...  values)throws JedisConnectionException,Exception{
 	
 		Long count=0L;
		try {
			if(jedis!=null){
				count = jedis.srem(sets, values);
			}
		}catch(JedisConnectionException e){
			e.printStackTrace();
			throw new JedisConnectionException(e);
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
 		return count;
 	}
 	
 	/**
 	 * 判断sets中某元素是否存在
 	 */
 	public   boolean sismember(String sets,String value){
 		
 		Boolean sismember=null;
		try {
			if(jedis!=null){
				sismember = jedis.sismember(sets, value);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
 		return sismember;
 	}
 	
 	/**
 	 * 关闭连接
 	 */
 	public void closeJedis(){
 		try {
 			if(jedis!=null)
			jedis.close();
		} catch (Exception e) {
			
			e.printStackTrace();
		}
 	}
 	@SuppressWarnings("finally")
	public static Boolean print(String sets){
		Boolean flag=true;
		try {
			if(jedis!=null){
			Set<String> set = jedis.smembers(sets);
			if(set!=null&&set.size()>0){
				for (String loction : set) {
					log.debug("key="+sets+"----loction="+loction);
				}
			}else{
				flag=false;
				log.debug("key="+sets+"为空或null");
			}
			}
		} catch (Exception e) {
			log.debug("redis操作错误");
			flag=null;
			e.printStackTrace();
		}finally{
			return flag;
		}
	
	}
	
	public static Boolean isExistSet(String sets){
		Boolean flag=null;
		if(jedis!=null){
		 flag = jedis.exists(sets);
		}
		return flag;
	}
}
