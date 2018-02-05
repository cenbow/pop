
package com.ailk.bdx.pop.adapter.jedis;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;

import redis.clients.jedis.JedisCluster;

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
	 * @param obj
	 * @return
	 */
	public static boolean saveObject(String key,Object obj) throws Exception{
		byte[] byteKey = key.getBytes();
		
		try {
			JedisCluster jc = JedisClusterFactory.getJedisCluster();
			jc.sadd(byteKey, AdapterUtil.objectToBytes(obj));
			System.out.println("bytekey="+byteKey+";value="+AdapterUtil.objectToBytes(obj));
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
	public static boolean saveObject(String key,Object obj,int expire) throws Exception{
		JedisCluster jc = JedisClusterFactory.getJedisCluster();
		byte[] byteKey = key.getBytes();
		try {
			jc.set(byteKey, AdapterUtil.objectToBytes(obj));
			jc.expire(byteKey, expire);
			return true;
		} catch (Exception e) {
			logger.debug("保存对象失败：",e);
			throw e;
		}
	}
	
	public static boolean saveObjectAsJson(String key,Object obj)throws Exception {
		JedisCluster jc = JedisClusterFactory.getJedisCluster();
		byte[] byteKey = key.getBytes();
		try {
			jc.set(byteKey, objectMapper.writeValueAsBytes(obj));
			return true;
		} catch (Exception e) {
			logger.debug("保存对象失败：",e);
			throw e;
		}
	}
	
	/**
	 * 保存对象}
	 * @param key
	 * @param obj
	 * @return
	 */
	public static boolean saveObjectAsJson(String key,Object obj,int expire)throws Exception {
		JedisCluster jc = JedisClusterFactory.getJedisCluster();
		byte[] byteKey = key.getBytes();
		try {
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
	public static Object readObjectFromRedis(String key)throws Exception {
		try{
			byte[] byteKey = key.getBytes();
			return readObjectFromRedis(byteKey);
		}catch(Exception e){
			logger.error("", e);
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
			logger.error("", e);
			throw e;
		}
	}
	
	public static String getStringVlue(String key)throws Exception{
		try{
			JedisCluster jc = JedisClusterFactory.getJedisCluster();
			System.out.println(key+"="+jc.get(key));
			return jc.get(key);
		}catch(Exception e){
			logger.error("", e);
			throw e;
		}
	}
	
	/**增/改
	 * 增加键值对--直接覆盖原始key-value
	 */
	public  static String setKeyValue(String key,String value)throws Exception{
		try{
			JedisCluster cluster = JedisClusterFactory.getJedisCluster();
			String rst = cluster.set(key, value);
			return rst;
		}catch(Exception e){
			logger.error("", e);
			throw e;
		}	
	}
	/**
	 * 根据key查询对应的value
	 * @param key
	 * @return
	 */
	public static String getValue(String key)throws Exception{
		try{
			JedisCluster cluster = JedisClusterFactory.getJedisCluster();
			String value = cluster.get(key);
			return value;
		}catch(Exception e){
			logger.error("", e);
			throw e;
		}
	}
	/**
	 * 删除键值对
	 */
	public static Long delKeyValue(String key)throws Exception{
		try{
			JedisCluster cluster = JedisClusterFactory.getJedisCluster();
			Long count = cluster.del(key);
			return count;
		}catch(Exception e){
			logger.error("", e);
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
			return count;
		} catch (Exception e) {
			logger.error("", e);
			throw e;
		}
		
	}
	
	/**
	 * 获取keys1、keys2、...对应的value1、value2 ...
	 * @param keys
	 * @return
	 */
	public static List<String> getListLAC_CI(String... keys)throws Exception{
		try{
			JedisCluster cluster = JedisClusterFactory.getJedisCluster();
			 List<String> lac_cis = cluster.mget(keys);
			return lac_cis;
		}catch(Exception e){
			logger.error("", e);
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
			logger.error("", e);
			throw e;
		}
	}
	
	/**
	 * 删除sets集合中某个(些)元素
	 */
	public static Long  delSetValues(String sets,String...  values)throws Exception{
		try{
			JedisCluster cluster = JedisClusterFactory.getJedisCluster();
			Long count = cluster.srem(sets, values);
			return count;
		}catch(Exception e){
			logger.error("", e);
			throw e;
		}
	}
	
	/**
	 * 判断sets中某元素是否存在
	 * @throws Exception 
	 */
	public static boolean sismember(String sets,String value)throws Exception{
		try{
			JedisCluster cluster = JedisClusterFactory.getJedisCluster();
			Boolean sismember = cluster.sismember(sets, value);
			return sismember;
		}catch(Exception e){
			logger.error("", e);
			throw e;
		}
		
	}
	@SuppressWarnings("finally")
	public static Boolean print(String key)throws Exception{
		Boolean flag=true;
		try {
			Set<String> set = getSetLAC_CI(key);
			if(set!=null&&set.size()>0){
				Iterator<String> it = set.iterator();
				while(it.hasNext()){
					String next = it.next();
					System.out.println("key:"+key+"---value:"+next);
				}
			}else{
				flag=false;
				logger.info("key="+key+"为空或null");
			}
			return flag;
		} catch (Exception e) {
			logger.error("", e);
			flag=null;
			e.printStackTrace();
			throw e;
		}
	
	}
	public static boolean isExistSet(String sets)throws Exception{
		try{
			JedisCluster cluster = JedisClusterFactory.getJedisCluster();
			Boolean flag = cluster.exists(sets);
			return flag;
		}catch(Exception e){
			logger.error("", e);
			throw e;
		}
		
	}
	
}
