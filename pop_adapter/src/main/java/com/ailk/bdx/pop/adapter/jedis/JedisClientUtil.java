/**
 * 
 */
package com.ailk.bdx.pop.adapter.jedis;

import java.util.Set;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;

import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.exceptions.JedisException;

import com.ailk.bdx.pop.adapter.util.AdapterUtil;

/**
 * @author zuowg
 *
 */
public class JedisClientUtil {

	private final static Logger logger = Logger.getLogger(JedisClientUtil.class);
	
	private final static ObjectMapper objectMapper = new ObjectMapper();
	
	/**
	 * 保存对象
	 * @param key
	 * @param objs
	 * @return
	 */
	public static boolean saveObject(String key,Object obj) 
			throws Exception{
		try {
			JedisCluster jc = JedisClusterFactory.getJedisCluster();
			byte[] byteKey = key.getBytes();
			jc.sadd(byteKey, AdapterUtil.objectToBytes(obj));
			return true;
		} catch (Exception e) {
			logger.debug("saveObject(String key,Object obj)失败 ：", e);
			throw e;
		}
	}
	
	/**
	 * 保存对象
	 * @param key
	 * @param obj
	 * @return
	 */
	public static boolean saveObject(String key,Object obj,int expire) 
			throws Exception{
		try {
			JedisCluster jc = JedisClusterFactory.getJedisCluster();
			byte[] byteKey = key.getBytes();
			jc.set(byteKey, AdapterUtil.objectToBytes(obj));
			jc.expire(byteKey, expire);
			return true;
		} catch (Exception e) {
			logger.debug("saveObject(String key,Object obj,int expire)失败：", e);
			throw e;
		}
	}
	
	public static boolean saveObjectAsJson(String key,Object obj) 
			throws Exception{
		try {
			JedisCluster jc = JedisClusterFactory.getJedisCluster();
			byte[] byteKey = key.getBytes();
			jc.set(byteKey, objectMapper.writeValueAsBytes(obj));
			return true;
		} catch (Exception e) {
			logger.debug("saveObjectAsJson(String key,Object obj)：", e);
			return false;
		}
	}
	
	/**
	 * 保存对象
	 * @param key
	 * @param obj
	 * @return
	 */
	public static boolean saveObjectAsJson(String key,Object obj,int expire) 
			throws Exception{
		try {
			JedisCluster jc = JedisClusterFactory.getJedisCluster();
			byte[] byteKey = key.getBytes();
			jc.set(byteKey, objectMapper.writeValueAsBytes(obj));
			jc.expire(byteKey, expire);
			return true;
		} catch (Exception e) {
			logger.debug("saveObjectAsJson(String key,Object obj,int expire) 失败：", e);
			throw e;
		}
	}
	
	/**
	 * 从Redis获取对象
	 * @param key
	 * @return
	 */
	public static Object readObjectFromRedis(String key) 
			throws Exception{
		try{
			byte[] byteKey = key.getBytes();
			return readObjectFromRedis(byteKey);
		}catch(Exception e){
			logger.error("readObjectFromRedis(String key)失败：", e);
			throw e;
		}
	}
	
	public static Object readObjectFromRedis(byte[] key) 
			throws Exception{
		try {
			JedisCluster jc = JedisClusterFactory.getJedisCluster();
			return AdapterUtil.bytesToObject(jc.get(key));
		} catch (Exception e) {
			logger.debug("readObjectFromRedis(byte[] key) 失败：", e);
			throw e;
		}
	}
	
	public static Object readObjectFromJson(String key,Class<?> classType) 
			throws Exception{
		JedisCluster jc = JedisClusterFactory.getJedisCluster();
		try {
			return objectMapper.readValue(jc.get(key), classType);
		} catch (Exception e) {
			logger.debug("readObjectFromJson(String key,Class<?> classType)失败：", e);
			throw e;
		}
	}
	
	public static boolean isExists(byte[] key) 
			throws Exception{
		try{
			JedisCluster jc = JedisClusterFactory.getJedisCluster();
			return jc.exists(key);
		}catch(Exception e){
			logger.error("isExists(byte[] key) 失败：", e);
			throw e;
		}
	}
	
	public static String get(String key) throws Exception{
		try {
			JedisCluster jc = JedisClusterFactory.getJedisCluster();
			return jc.get(key);
		} catch (Exception e) {
			logger.debug("get(String key)失败：", e);
			e.printStackTrace();
			throw e;
		}
	}

	public static Set<String> smembers(String key) 
			throws Exception{
		try {
			JedisCluster jc = JedisClusterFactory.getJedisCluster();
			return jc.smembers(key);
		} catch (Exception e) {
			logger.debug("smembers(String key)失败：", e);
			throw e;
		}
	}
	
	public static boolean sismembers(String key,String member) 
			throws JedisException, Exception{
		try {
			JedisCluster jc = JedisClusterFactory.getJedisCluster();
			return jc.sismember(key, member);
		} catch (Exception e) {
			logger.debug("sismembers(String key,String member)失败：", e);
			throw new JedisException(e);
		}
	}
	
	/**
	 * 将一个或多个 member 元素加入到集合 key 当中，已经存在于集合的 member 元素将被忽略
	 * @param key
	 * @param members
	 */
	public static void sadd(String key,String ... members)
			throws Exception{
		try{
			JedisCluster jc = JedisClusterFactory.getJedisCluster();
			jc.sadd(key, members);
		}catch(Exception e){
			logger.error("向Redis中集合中添加key: " + key + " members: " + members + " 失败！", e);
			throw e;
		}
	}
	
	/**
	 * 删除给定的一个或多个 key 
	 * @param key
	 */
	public static void del(String... key)
			throws Exception{
		try{
			JedisCluster jc = JedisClusterFactory.getJedisCluster();
			jc.del(key);
		}catch(Exception e){
			logger.error("删除Redis中key: " + key + " 失败！", e);
			throw e;
		}
	}
	
	/**
	 * 返回有序集 key 中，指定区间内的成员
	 * @param key
	 * @param start
	 * @param end
	 * @return
	 */
	public static Set<String> zrevrange(String key,long start,long end)
			throws Exception{
		try{
			JedisCluster jc = JedisClusterFactory.getJedisCluster();
			return jc.zrevrange(key, start, end);
		}catch(Exception e){
			logger.error("获取Redis中的有序集合中的key: " + key + " start: " + start + " end: " + end + " 失败：", e);
			throw e;
		}
		
	}
	
	public static double zincrby(String key,double increment,String member)
			throws Exception{
		try{
			JedisCluster jc = JedisClusterFactory.getJedisCluster();
			return jc.zincrby(key, increment, member);
		}catch(Exception e){
			logger.error("zincrby(String key,double increment,String member)失败：", e);
			throw e;
		}
		
	}
	
	public static void expire(String key,int seconds)
			throws Exception{
		try{
			JedisCluster jc = JedisClusterFactory.getJedisCluster();
			jc.expire(key, seconds);
		}catch(Exception e){
			logger.error("expire(String key,int seconds)失败：",e);
			throw e;
		}
    }
	
	public static void main(String[] args){
		try {
			JedisCluster jc = JedisClusterFactory.getJedisCluster();
			String personValue = jc.get("person");
			System.out.println("personValue:" + personValue);
		} catch (Exception e) {
			e.printStackTrace();
			logger.debug("保存对象失败："+e);
		}
	}
}
