package com.ai.bdx.pop.jedis;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import com.ai.bdx.pop.util.Configure;


public class RedisUtils {
    
    private static final Log log = LogFactory.getLog(RedisUtils.class); 

    private static JedisPool jedisPool;//非切片连接池
    
    private static final Object lock = new Object();
    
    private static final int DEFAULT_TIME_OUT = 30000;
    
	/**
	 * 构建redis切片连接池
	 * 
	 * @param ip
	 * @param port
	 * @return JedisPool
	 */
    public static JedisPool getJedisPool() {
        if (jedisPool == null) {
            synchronized (lock) {
                //redis服务器对应的IP和端口
                String redisIp = Configure.getInstance().getProperty("redis.single.pool.ip");
                Integer redisPort = Integer.valueOf(Configure.getInstance().getProperty("redis.single.pool.port"));
//                Integer redisPort = Integer.valueOf(PropertiesUtils.getProperties("REDIS_POOL_MAX"));
//                Integer redisPort = Integer.valueOf(PropertiesUtils.getProperties("REDIS_SERVER_PORT"));
                if (jedisPool == null) {
                    JedisPoolConfig config = new JedisPoolConfig();
                    //设置连接池初始化大小和最大容量
                    
                    // 控制一个pool可分配多少个jedis实例，通过pool.getResource()来获取；
                    // 如果赋值为-1，则表示不限制；如果pool已经分配了maxActive个jedis实例，则此时pool的状态为exhausted(耗尽)。
                    config.setMaxTotal(300);
                    // 控制一个pool最多有多少个状态为idle(空闲的)的jedis实例。
                    config.setMaxIdle(1000);
                    // 表示当borrow(引入)一个jedis实例时，最大的等待时间，如果超过等待时间，则直接抛出JedisConnectionException；
                    config.setMaxWaitMillis(1000 * 30);
                    // 在borrow一个jedis实例时，是否提前进行validate操作；如果为true，则得到的jedis实例均是可用的；
                    
                    // 写
                    
                    
                    /*config.setMinEvictableIdleTimeMillis(5000);
    				config.setMaxWaitMillis(Long.parseLong(Configure.getInstance().getProperty("redis.cluster.pool.MaxWaitMillis")));
    				//最大连接数, 默认8个
    				config.setMaxTotal(Integer.parseInt(Configure.getInstance().getProperty("redis.cluster.pool.MaxTotal")));
    				//最小空闲连接数, 默认0
    				config.setMinIdle(Integer.parseInt(Configure.getInstance().getProperty("redis.cluster.pool.MinIdle")));
    				//最大空闲连接数, 默认8个
    				config.setMaxIdle(Integer.parseInt(Configure.getInstance().getProperty("redis.cluster.pool.setMaxIdle")));
    				//逐出扫描的时间间隔(毫秒) 如果为负数,则不运行逐出线程, 默认-1
    				config.setTimeBetweenEvictionRunsMillis(5000);*/
    				config.setTestOnBorrow(true);
                    jedisPool = new JedisPool(config, redisIp, redisPort,DEFAULT_TIME_OUT);
                    
                }
            }
        }
        return jedisPool;
    }
	
	
	
    /**
     * 返还到连接池
     * 
     * @param pool 
     * @param redis
     */
    public static void returnJedisResource(Jedis redis) {
        if (redis != null) {
            redis.close();
        }
    }
    
    
    /**
     * 获取数据
     * 
     * @param key
     * @return
     */
    public static String getForString(String key){
        List<String> values = mgetForString(key);
        if(values == null) {
            return null;
        } else {
            return values.get(0);
        }
    }
    
    /**
     * 获取数据
     * 
     * @param key
     * @return
     */
    public static List<String> mgetForString(String... key){
        List<String> value = null;
        JedisPool pool = null;
        Jedis jedis = null;
        try {
            pool = getJedisPool();
            jedis = pool.getResource();
            value = jedis.mget(key);
        } catch (Exception e) {
            log.error(e);
        } finally {
            //返还到连接池
            returnJedisResource(jedis);
        }
        return value;
    }
    
    public static void setForString(String key,String value){
   	    JedisPool pool = null;
        Jedis jedis = null;
       try {
           pool = getJedisPool();
           jedis = pool.getResource();
           jedis.set(key, value);
       } catch (Exception e) {
           log.error(e);
       } finally {
           //返还到连接池
       	  returnJedisResource(jedis);
       }
   	
   }
    
    /**
     * 设置哈希结构的字段和值
     * @param key
     * @param value
     */
    public static void setForHashObj(String key, Map<String, String> value) {
        JedisPool pool = null;
        Jedis jedis = null;
        try {
            pool = getJedisPool();
            jedis = pool.getResource();
            jedis.hmset(key, value);
        } catch (Exception e) {
            log.error(e);
        } finally {
            // 返还到连接池
            returnJedisResource(jedis);
        }
    }
    
    /**
     * 设置哈希结构的字段和值
     * @param key
     * @param value
     */
    public static void removeForHashObj(String key, String...fields) {
        JedisPool pool = null;
        Jedis jedis = null;
        try {
            pool = getJedisPool();
            jedis = pool.getResource();
            jedis.hdel(key, fields);
        } catch (Exception e) {
            log.error(e);
        } finally {
            // 返还到连接池
            returnJedisResource(jedis);
        }
    }
    
    /**
     * 获取哈希结构的值
     * @param key
     * @param field
     * @return
     */
    public static String getForHashObj(String key, String field) {
        List<String> values = mgetForHashObj(key, field);
        if(values == null) {
            return null;
        } else {
            return values.get(0);
        }
    }
    
    /**
     * 获取哈希结构的值
     * @param key
     * @param value
     * @return
     */
    public static List<String> mgetForHashObj(String key, String... value) {
        List<String> result = null;
        JedisPool pool = null;
        Jedis jedis = null;
        try {
            pool = getJedisPool();
            jedis = pool.getResource();
            result = jedis.hmget(key, value);
        } catch (Exception e) {
            log.error(e);
        } finally {
            // 返还到连接池
            returnJedisResource(jedis);
        }
        return result;
    }
    
    /**
     * 获取同一个键下的哈希对象
     * @param key
     * @return
     */
    public static Map<String,String> getForHashObjByKey(String key) {
        Map<String,String> result = null;
        JedisPool pool = null;
        Jedis jedis = null;
        try {
            pool = getJedisPool();
            jedis = pool.getResource();
            result = jedis.hgetAll(key);
        } catch (Exception e) {
            log.error(e);
        } finally {
            // 返还到连接池
            returnJedisResource(jedis);
        }
        return result;
    }
    
    /**
     * 获取一个list中的所有值
     * @param key
     * @return
     */
    public static List<String> getForListByKey(String key) {
    	List<String> result = null;
    	JedisPool pool = null;
    	Jedis jedis = null;
    	try {
    		pool = getJedisPool();
    		jedis = pool.getResource();
    		result =  jedis.lrange(key, 0, -1); 
    	} catch (Exception e) {
            log.error(e);
        } finally {
            // 返还到连接池
            returnJedisResource(jedis);
        }
    	return result;
    }
    
    /**
     * 向list中添加值
     * @param key
     * @param value
     */
    public static void setForList(String key, String value) {
    	JedisPool pool = null;
        Jedis jedis = null;
        try {
            pool = getJedisPool();
            jedis = pool.getResource();
            jedis.lpush(key, value);
        } catch (Exception e) {
            log.error(e);
        } finally {
            // 返还到连接池
            returnJedisResource(jedis);
        }
    }
    
    public static Set<String> zrevrange(String key,long start,long end){
    	Set<String> result = null;
    	JedisPool pool = null;
    	Jedis jedis = null;
    	try {
    		pool = getJedisPool();
    		jedis = pool.getResource();
    		result =  jedis.zrevrange(key, start, end);
    	} catch (Exception e) {
            log.error(e);
            System.out.println(e);
            e.printStackTrace();
        } finally {
            // 返还到连接池
            returnJedisResource(jedis);
        }
    	return result;
    }
    
    public static void del(String key){
    	JedisPool pool = null;
    	Jedis jedis = null;
    	try {
    		pool = getJedisPool();
    		jedis = pool.getResource();
    		jedis.del(key);
    	} catch (Exception e) {
            log.error(e);
            System.out.println(e);
            e.printStackTrace();
        } finally {
            // 返还到连接池
            returnJedisResource(jedis);
        }
    }
    
    public static void sadd(String key,String ... members){
    	JedisPool pool = null;
    	Jedis jedis = null;
    	try {
    		pool = getJedisPool();
    		jedis = pool.getResource();
    		jedis.sadd(key,members);
    	} catch (Exception e) {
            log.error(e);
            System.out.println(e);
            e.printStackTrace();
        } finally {
            // 返还到连接池
            returnJedisResource(jedis);
        }
    }
    
    /**
	 * 返回集合 key 的基数(集合中元素的数量).
	 * 当 key 不存在时，返回 0.
	 * @param key
	 * @return
	 */
    public static long scard(String key){
    	JedisPool pool = null;
    	Jedis jedis = null;
    	long result = 0;
    	try {
    		pool = getJedisPool();
    		jedis = pool.getResource();
    		result = jedis.scard(key);
    	} catch (Exception e) {
            log.error(e);
            System.out.println(e);
            e.printStackTrace();
        } finally {
            // 返还到连接池
            returnJedisResource(jedis);
        }
    	return result;
    }
    
    public static Long zcard(String key){
    	Long result = null;
    	JedisPool pool = null;
    	Jedis jedis = null;
    	try {
    		pool = getJedisPool();
    		jedis = pool.getResource();
    		result =  jedis.zcard(key);
    	} catch (Exception e) {
            log.error(e);
            System.out.println(e);
            e.printStackTrace();
        } finally {
            // 返还到连接池
            returnJedisResource(jedis);
        }
    	return result;
    }
    
    public static double zincrby(String key,double increment,String member) {
    	double result = 0;
    	JedisPool pool = null;
    	Jedis jedis = null;
    	try {
    		pool = getJedisPool();
    		jedis = pool.getResource();
    		result = jedis.zincrby(key, increment, member);
    	} catch (Exception e) {
            log.error(e);
            System.out.println(e);
            e.printStackTrace();
        } finally {
            // 返还到连接池
            returnJedisResource(jedis);
        }
    	return result;
    }
    
    public static void expire(String key,int seconds){
    	JedisPool pool = null;
    	Jedis jedis = null;
    	try {
    		pool = getJedisPool();
    		jedis = pool.getResource();
    		jedis.expire(key, seconds);
    	} catch (Exception e) {
            log.error(e);
            System.out.println(e);
            e.printStackTrace();
        } finally {
            // 返还到连接池
            returnJedisResource(jedis);
        }
    }
}

