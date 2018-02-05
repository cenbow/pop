/**
 * 
 */
package com.ai.bdx.pop.jedis;

import java.util.Set;
import org.apache.log4j.Logger;
import redis.clients.jedis.JedisCluster;
import com.ai.bdx.pop.util.AdapterUtil;
import com.ailk.bdx.pop.adapter.jedis.JedisClusterFactory;
import com.fasterxml.jackson.databind.ObjectMapper;

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
	 * @param obj
	 * @return
	 */
	public static boolean saveObject(String key,Object obj)throws Exception {
		try {
			JedisCluster jc = JedisClusterFactory.getJedisCluster();
			byte[] byteKey = key.getBytes();
			jc.sadd(byteKey, AdapterUtil.objectToBytes(obj));
			return true;
		} catch (Exception e) {
			logger.debug("保存对象失败：",e);
			throw e;
		}
	}
	
	/**
	 * 保存对象
	 * @param key
	 * @param obj
	 * @return
	 */
	public static boolean saveObject(String key,Object obj,int expire)throws Exception {
	
		try {
			JedisCluster jc = JedisClusterFactory.getJedisCluster();
			byte[] byteKey = key.getBytes();
			jc.set(byteKey, AdapterUtil.objectToBytes(obj));
			jc.expire(byteKey, expire);
			return true;
		} catch (Exception e) {
			logger.debug("保存对象失败：",e);
			throw e;
		}
	}
	
	public static boolean saveObjectAsJson(String key,Object obj)throws Exception {
		try {
			JedisCluster jc = JedisClusterFactory.getJedisCluster();
			byte[] byteKey = key.getBytes();
			jc.set(byteKey, objectMapper.writeValueAsBytes(obj));
			return true;
		} catch (Exception e) {
			logger.debug("保存对象失败：",e);
			throw e;
		}
	}
	
	/**
	 * 保存对象
	 * @param key
	 * @param obj
	 * @return
	 */
	public static boolean saveObjectAsJson(String key,Object obj,int expire) throws Exception{
		try {
			JedisCluster jc = JedisClusterFactory.getJedisCluster();
			byte[] byteKey = key.getBytes();
			jc.set(byteKey, objectMapper.writeValueAsBytes(obj));
			jc.expire(byteKey, expire);
			return true;
		} catch (Exception e) {
			logger.debug("保存对象失败：",e);
			throw e;
		}
	}
	
	/**
	 * 从Redis获取对象
	 * @param key
	 * @return
	 */
	public static Object readObjectFromRedis(String key) throws Exception{
		try{
			byte[] byteKey = key.getBytes();
			return readObjectFromRedis(byteKey);
		}catch(Exception e){
			logger.debug("",e);
			throw e;
		}
		
	}
	
	public static Object readObjectFromRedis(byte[] key)throws Exception {
		try {
			JedisCluster jc = JedisClusterFactory.getJedisCluster();
			return AdapterUtil.bytesToObject(jc.get(key));
		} catch (Exception e) {
			logger.debug("获取对象失败：",e);
			throw e;
		}
	}
	
	public static Object readObjectFromJson(String key,Class<?> classType)throws Exception {
		try {
			JedisCluster jc = JedisClusterFactory.getJedisCluster();
			return objectMapper.readValue(jc.get(key), classType);
		} catch (Exception e) {
			logger.debug("获取对象失败：",e);
			throw e;
		}
	}
	
	public static boolean isExists(byte[] key)throws Exception {
		try{
			JedisCluster jc = JedisClusterFactory.getJedisCluster();
			return jc.exists(key);
		}catch(Exception e){
			logger.error("",e);
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
	public static Set<String> zrevrange(String key,long start,long end)throws Exception{
		try{
			JedisCluster jc = JedisClusterFactory.getJedisCluster();
			return jc.zrevrange(key, start, end);
		}catch(Exception e){
			logger.error("获取Redis中的有序集合中的key: " + key + " start: " + start + " end: " + end + " 失败！",e);
			throw e;
		}
		
	}
	
	/**
	 * 删除给定的一个或多个 key 
	 * @param key
	 */
	public static void del(String... key)throws Exception{
		try{
			JedisCluster jc = JedisClusterFactory.getJedisCluster();
			jc.del(key);
		}catch(Exception e){
			logger.error("删除Redis中key: " + key + " 失败！",e);
			throw e;
		}
	}
	
	/**
	 * 将一个或多个 member 元素加入到集合 key 当中，已经存在于集合的 member 元素将被忽略
	 * @param key
	 * @param members
	 */
	public static void sadd(String key,String ... members)throws Exception{
		try{
			JedisCluster jc = JedisClusterFactory.getJedisCluster();
			jc.sadd(key, members);
		}catch(Exception e){
			logger.error("向Redis中集合中添加key: " + key + " members: " + members + " 失败！",e);
			throw e;
		}
	}
	
	/**
	 * 为有序集 key 的成员 member 的 score 值加上增量 increment 。
	 * 可以通过传递一个负数值 increment ，让 score 减去相应的值，比如 ZINCRBY key -5 member ，就是让 member 的 score 值减去 5 。
     * 当 key 不存在，或 member 不是 key 的成员时， ZINCRBY key increment member 等同于 ZADD key increment member 。
     * 当 key 不是有序集类型时，返回一个错误。
     * score 值可以是整数值或双精度浮点数。
	 * @param key
	 * @param increment
	 * @param member
	 * @return
	 * @throws Exception
	 */
	public static double zincrby(String key,double increment,String member)throws Exception{
		try{
			JedisCluster jc = JedisClusterFactory.getJedisCluster();
			return jc.zincrby(key, increment, member);
		}catch(Exception e){
			logger.error("",e);
			throw new Exception(e);
		}
	}
	
	/**
	 * 返回有序集 key 中，成员 member 的 score 值
	 * 如果 member 元素不是有序集 key 的成员，或 key 不存在，返回 nil
	 * @param key
	 * @param member
	 * @return
	 * @throws Exception
	 */
	public static double zscore(String key,String member)throws Exception{
		try{
			JedisCluster jc = JedisClusterFactory.getJedisCluster();
			return jc.zscore(key, member);
		}catch(Exception e){
			logger.error("",e);
			throw e;
		}
	}
	
	public static void expire(String key,int seconds)throws Exception{
		try{
			JedisCluster jc = JedisClusterFactory.getJedisCluster();
			jc.expire(key, seconds);
		}catch(Exception e){
			logger.error("",e);
			throw e;
		}
    }
	
	public static void set(String key,String value)throws Exception{
		try{
			JedisCluster jc = JedisClusterFactory.getJedisCluster();
			jc.set(key, value);
		}catch(Exception e){
			logger.error("",e);
			throw e;
		}
	}
	
	/**
	 * 返回集合 key 的基数(集合中元素的数量).
	 * 当 key 不存在时，返回 0.
	 * @param key
	 * @return
	 */
	public static long scard(String key)throws Exception{
		try{
			JedisCluster jc = JedisClusterFactory.getJedisCluster();
			return jc.scard(key);
		}catch(Exception e){
			logger.error("",e);
			throw e;
		}
	}
	
	/**
	 * 判断sets中某元素是否存在
	 */
	public static boolean sismember(String sets,String value) throws Exception{
		try{
			JedisCluster cluster = JedisClusterFactory.getJedisCluster();
			Boolean sismember = cluster.sismember(sets, value);
			return sismember;
		}catch(Exception e){
			logger.debug("获取对象失败：", e);
			throw e;
		}
	}
	
	public static Set<String> smembers(String key) throws Exception{
		try {
			JedisCluster jc = JedisClusterFactory.getJedisCluster();
			if(jc!=null){
				return jc.smembers(key);
			}else{
				return null;
			}
		} catch (Exception e) {
			logger.debug("获取对象失败：",e);
			throw e;
		}
	}
	
	public static boolean sismembers(String key,String member) throws Exception{
		try {
			JedisCluster jc = JedisClusterFactory.getJedisCluster();
			return jc.sismember(key, member);
		} catch (Exception e) {
			logger.debug("获取对象失败：" + e);
			throw e;
		}
	}
	
	/**
	 * 获得set集合sets的所有元素
	 * @param mobile
	 * @return
	 */
	public static Set<String> getSetLAC_CI(String sets)throws Exception{
		try{
			JedisCluster cluster = JedisClusterFactory.getJedisCluster();
			Set<String> lac_cis = cluster.smembers(sets);
			return lac_cis;
		}catch(Exception e){
			logger.error("",e);
			throw e;
		}
	}

	@SuppressWarnings("finally")
	public static Boolean print(String sets)throws Exception{
		Boolean flag=true;
		try {
			Set<String> set = smembers(sets);
			if(set!=null&&set.size()>0){
				for (String loction : set) {
					logger.info("key="+sets+"----loction="+loction);
				}
			}else{
				flag=false;
				logger.info("key="+sets+"为空或null");
			}
		} catch (Exception e) {
			logger.error("redis操作错误",e);
			flag=null;
			e.printStackTrace();
			throw e;
		}finally{
			return flag;
		}
	
	}
	
	public static boolean isExistSet(String sets)throws Exception{
		try{
			JedisCluster cluster = JedisClusterFactory.getJedisCluster();
			Boolean flag = cluster.exists(sets);
			return flag;
		}catch(Exception e){
			logger.error("",e);
			throw e;
		}
		
	}

	/**
	 * 新增一个key--多个value
	 */
	public static Long addKeyValues(String key,String... values)throws Exception{
		Long count=null;
		try {
			JedisCluster cluster = JedisClusterFactory.getJedisCluster();
			count = cluster.sadd(key, values);
		} catch (Exception e) {
			logger.error("", e);
			throw e;
		}
		return count;
	}
	/**
	 * 删除键值对
	 */
	public static Long delKeyValue(String key)throws Exception{
		Long count=null;
		try {
			JedisCluster cluster = JedisClusterFactory.getJedisCluster();
			count = cluster.del(key);
		} catch (Exception e) {
			logger.error("", e);
			throw e;
		}
		return count;
	}
	
	public static void main(String[] args) throws Exception{
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
