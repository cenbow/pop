package com.asiainfo.biapp.pop.redis.ClientTest;

import java.util.Iterator;
import java.util.Set;

import redis.clients.jedis.exceptions.JedisConnectionException;

import com.ai.bdx.pop.util.PopConstant;
import com.ailk.bdx.pop.adapter.jedis.JedisClientUtil;
import com.asiainfo.biapp.pop.redis.RedisClient;

public class RedisClientTest2 {
	 public static void main(String[] args) throws JedisConnectionException, Exception {
	      
		 	String subsid1="1380710";
			String userlocation1_1="130-4600029154074449025";
		
			String subsid2="1380725";
			String userlocation2_1="130-4600029154074449025";
			String userlocation2_2="130-4600029154074449026";
			String userlocation2_3="130-4600029154074449027";
			
			String subsid3="1860276";
			String userlocation3_1="130-4600028897074449281";
			String userlocation3_2="130-4600028897074449282";
			String userlocation3_3="130-4600028897074449283";
			
			String subsid4="1880270";
			String userlocation4_1="130-4600029154074449025";
			String userlocation4_2="130-4600029154074449026";
			String userlocation4_3="130-4600029154074449027";
			String userlocation4_4="130-4600028897074449281";
			String userlocation4_5="130-4600028897074449282";
			String userlocation4_6="130-4600028897074449283";
			long count=0l;
			
			RedisClient redisClient = new RedisClient();
			
			count = redisClient.delKeyValue(PopConstant.REDIS_UNL_PREFIX+subsid1);
			count = redisClient.delKeyValue(PopConstant.REDIS_UNL_PREFIX+subsid2);
			count = redisClient.delKeyValue(PopConstant.REDIS_UNL_PREFIX+subsid3);
			count = redisClient.delKeyValue(PopConstant.REDIS_UNL_PREFIX+subsid4);
			System.out.println("删除key-value="+count);
	
			count=redisClient.addKeyValues(PopConstant.REDIS_UNL_PREFIX+subsid1, userlocation1_1);
			System.out.println("1380710增加一个key多个value："+count);
			count=redisClient.addKeyValues(PopConstant.REDIS_UNL_PREFIX+subsid2, userlocation2_1,userlocation2_2,userlocation2_3);
			System.out.println("1380725增加一个key2多个value2："+count);
			count=redisClient.addKeyValues(PopConstant.REDIS_UNL_PREFIX+subsid3, userlocation3_1,userlocation3_2,userlocation3_3);
			System.out.println("1860276增加一个key3多个value3："+count);
			count=redisClient.addKeyValues(PopConstant.REDIS_UNL_PREFIX+subsid4, userlocation4_1,userlocation4_2,userlocation4_3,userlocation4_4,userlocation4_5,userlocation4_6);
			System.out.println("1880270增加一个key4多个value4："+count);
			
		//下面测试报错	WRONGTYPE Operation against a key holding the wrong kind of value
		//因为集合不能用get方式查值即使只有一个value也不行
/*			String value = redisClient.getValue(phone);  
			System.out.println("lac_ci="+value);
			
			count = redisClient.delSetValues(phone, lac_ci);
			System.out.println("删除set集合中的一个："+count);*/
			

			System.out.println("1--------------");
			Set<String> sets = redisClient.getSetLAC_CI(PopConstant.REDIS_UNL_PREFIX+subsid1);
			Iterator<String> it = sets.iterator();
			while(it.hasNext()){
				String next = it.next();
				System.out.println("sets:"+next);
			}
			System.out.println("2--------------");
			Set<String> sets2 = redisClient.getSetLAC_CI(PopConstant.REDIS_UNL_PREFIX+subsid2);
			Iterator<String> it2 = sets2.iterator();
			while(it2.hasNext()){
				String next = it2.next();
				System.out.println("sets2:"+next);
			}
			System.out.println("3-------------");
			Set<String> sets3 = redisClient.getSetLAC_CI(PopConstant.REDIS_UNL_PREFIX+subsid3);
			Iterator<String> it3 = sets3.iterator();
			while(it3.hasNext()){
				String next = it3.next();
				System.out.println("sets3:"+next);
			}
			System.out.println("4--------------");
			Set<String> sets4 = redisClient.getSetLAC_CI(PopConstant.REDIS_UNL_PREFIX+subsid4);
			Iterator<String> it4 = sets4.iterator();
			while(it4.hasNext()){
				String next = it4.next();
				System.out.println("sets4:"+next);
			}
			
			
			boolean s1 = redisClient.sismember(PopConstant.REDIS_UNL_PREFIX+subsid1, userlocation1_1);
			boolean s0 = redisClient.sismember(PopConstant.REDIS_UNL_PREFIX+subsid2, userlocation2_1);
			System.out.println("subsid1是否存在："+s0+";subsid2是否存在："+s1);
		 
//			Long lg = redisClient.delSetValues(PopConstant.REDIS_UNL_PREFIX+subsid, lac_ci);
//			System.out.println("删除一个value："+lg);
	    }

}
