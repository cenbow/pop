package com.ai.bdx.pop.jedis;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.apache.log4j.Logger;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.exceptions.JedisClusterMaxRedirectionsException;
import redis.clients.jedis.exceptions.JedisConnectionException;
import redis.clients.jedis.exceptions.JedisException;

import com.ai.bdx.pop.util.AdapterUtil;
import com.ai.bdx.pop.util.Configure;

/**
  * @Description: JedisCluster工厂<br>
  * @author zuowg <br>
  * @date 2015-9-4 下午06:09:28 <br>
  * Copyright: (C) Copyright 1993-2010 AsiaInfo Holdings, Inc<br>
  * Company: 北京亚信智慧数据科技有限公司
 */
public class JedisClusterFactory {
	private static Logger log = Logger.getLogger(JedisClusterFactory.class);
	/**JedisCluster单例*/
	private static JedisCluster jedisCluster;
	/**集群节点 */
	private static String clusterNodes;

	public static final String PUB_CHN_V_LOG_LIST = "PUB_CHN_V_LOG_LIST";
	
	public static final String PUB_CHN_V_LOG = "PUB_CHN_V_LOG";
	
	public static final String PUB_CHN_LOG = "PUB_CHN_LOG";
	
	public static final String PUB_CHN_CALL_LOG = "PUB_CHN_CALL_LOG";
	
	/**
	 * 构建JedisClusterFactory时初始化操作
	 */
	static{
		clusterNodes = Configure.getInstance().getProperty("redis.cluster.nodes");
	}

	/**
	 * 获取JedisCluster单一实例
	 * @return
	 */
	public synchronized static JedisCluster getJedisCluster()throws Exception{
		try{
			if(jedisCluster == null)
				jedisCluster = genJedisCluster();
			return jedisCluster;
		}catch(Exception e){
			throw e;
		}
		
	}

	/**
	 * 从连接池中获取JedisCluster实例
	 * @return
	 */
	private static JedisCluster genJedisCluster()throws 
		JedisClusterMaxRedirectionsException, JedisConnectionException, Exception{
		JedisCluster jc = null;
		try {
			Set<HostAndPort> haps = parseHostAndPort(clusterNodes);
			if(haps != null){
				GenericObjectPoolConfig config = new GenericObjectPoolConfig();
				/*
				//连接耗尽时是否阻塞, false报异常,ture阻塞直到超时, 默认true
				config.setBlockWhenExhausted(true);
				//设置的逐出策略类名, 默认DefaultEvictionPolicy(当连接超过最大空闲时间,或连接数超过最大空闲连接数)
				config.setEvictionPolicyClassName("org.apache.commons.pool2.impl.DefaultEvictionPolicy");
				//是否启用pool的jmx管理功能, 默认true
				config.setJmxEnabled(true);
				//MBean ObjectName = new ObjectName("org.apache.commons.pool2:type=GenericObjectPool,name=" + "pool" + i); 默 认为"pool", JMX不熟,具体不知道是干啥的...默认就好.
				config.setJmxNamePrefix("pool");
				//是否启用后进先出, 默认true
				config.setLifo(true);
				//最大空闲连接数, 默认8个
				config.setMaxIdle(8);
				//最大连接数, 默认8个
				config.setMaxTotal(8);
				//获取连接时的最大等待毫秒数(如果设置为阻塞时BlockWhenExhausted),如果超时就抛异常, 小于零:阻塞不确定的时间,  默认-1
				config.setMaxWaitMillis(-1);
				//逐出连接的最小空闲时间 默认1800000毫秒(30分钟)
				config.setMinEvictableIdleTimeMillis(1800000);
				//最小空闲连接数, 默认0
				config.setMinIdle(0);
				//每次逐出检查时 逐出的最大数目 如果为负数就是 : 1/abs(n), 默认3
				config.setNumTestsPerEvictionRun(3);
				//对象空闲多久后逐出, 当空闲时间>该值 且 空闲连接>最大空闲数 时直接逐出,不再根据MinEvictableIdleTimeMillis判断  (默认逐出策略)
				config.setSoftMinEvictableIdleTimeMillis(1800000);
				//在获取连接的时候检查有效性, 默认false
				config.setTestOnBorrow(false);
				//在空闲时检查有效性, 默认false
				config.setTestWhileIdle(false);
				//逐出扫描的时间间隔(毫秒) 如果为负数,则不运行逐出线程, 默认-1
				config.setTimeBetweenEvictionRunsMillis(-1);
				*/
				//config.setMinEvictableIdleTimeMillis(5000);
				config.setMaxWaitMillis(Long.parseLong(Configure.getInstance().getProperty("redis.cluster.pool.MaxWaitMillis")));
				//最大连接数, 默认8个
				config.setMaxTotal(Integer.parseInt(Configure.getInstance().getProperty("redis.cluster.pool.MaxTotal")));
				//最小空闲连接数, 默认0
				config.setMinIdle(Integer.parseInt(Configure.getInstance().getProperty("redis.cluster.pool.MinIdle")));
				//最大空闲连接数, 默认8个
				config.setMaxIdle(Integer.parseInt(Configure.getInstance().getProperty("redis.cluster.pool.setMaxIdle")));
				//逐出扫描的时间间隔(毫秒) 如果为负数,则不运行逐出线程, 默认-1
				config.setTimeBetweenEvictionRunsMillis(5000);
				jc = new JedisCluster(haps, Integer.parseInt(Configure.getInstance().getProperty("redis.cluster.pool.timeout")), Integer.parseInt(Configure.getInstance().getProperty("redis.cluster.pool.maxRedirections")), config);
				log.info(">>>>>> redis cluster连接节点 =" + jc.getClusterNodes());
			}
		}catch(JedisException e){
			log.error("生成一个Redis集群连接池异常！",e);
			throw e;
		}catch (Exception e) {
			log.error("请检查Redis连接池的参数配置！", e);
			try {
				if(jc != null){
					jc.close();
				}
			} catch (Exception e2) {
				log.error("关闭redis失败！", e2);
				throw e2;
			}
		}
		return jc;
	}

	/**
	 * 将集群结点字符串转换为Set对象
	 * @param clusterNodes
	 * 		字符串型的Redis集群结点
	 * @return
	 * 		Set<HostAndPort> 对象
	 */
	private static Set<HostAndPort> parseHostAndPort(String clusterNodes) {
        try {
            Set<HostAndPort> haps = new HashSet<HostAndPort>();
            //String clusterNodes = "20.26.20.14:7000,20.26.20.14:7001,20.26.20.15:7000,20.26.20.15:7001,20.26.20.61:7000,20.26.20.61:7001";
            String[] ipPorts = clusterNodes.split(",");
            for (String ipPort : ipPorts) {
                boolean isIpPort = AdapterUtil.isIpPort(ipPort);
                if (!isIpPort) {
                    throw new IllegalArgumentException("ip 或 port 不合法");
                }
                String[] ipAndPort = ipPort.split(":");
                HostAndPort hap = new HostAndPort(ipAndPort[0], Integer.parseInt(ipAndPort[1]));
                haps.add(hap);
            }
            return haps;
        } catch (Exception ex) {
        	log.error("解析 jedis 配置文件失败", ex);
        	return null;
        }
    }

}