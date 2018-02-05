package com.asiainfo.biapp.pop.redis.ClientTest;

import java.util.Iterator;
import java.util.Set;
import redis.clients.jedis.exceptions.JedisConnectionException;
import com.ai.bdx.pop.util.PopConstant;
import com.ailk.bdx.pop.adapter.jedis.JedisClientUtil;


public class RedisClientTest {
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
			
			String subsid4="1590710";
			String userlocation4_1="130-4600029154074449025";
			String userlocation4_2="130-4600029154074449026";
			String userlocation4_3="130-4600029154074449027";
			String userlocation4_4="130-4600028897074449281";
			String userlocation4_5="130-4600028897074449282";
			String userlocation4_6="130-4600028897074449283";
			long count=0l;
			

	
			
	
			count=JedisClientUtil.addKeyValues(PopConstant.REDIS_UNL_PREFIX+subsid1, userlocation1_1);
			System.out.println("1380710增加一个key多个value："+count);
			count=JedisClientUtil.addKeyValues(PopConstant.REDIS_UNL_PREFIX+subsid2, userlocation2_1,userlocation2_2,userlocation2_3);
			System.out.println("1380725增加一个key2多个value2："+count);
			count=JedisClientUtil.addKeyValues(PopConstant.REDIS_UNL_PREFIX+subsid3, userlocation3_1,userlocation3_2,userlocation3_3);
			System.out.println("1860276增加一个key3多个value3："+count);
			count=JedisClientUtil.addKeyValues(PopConstant.REDIS_UNL_PREFIX+subsid4, userlocation4_1,userlocation4_2,userlocation4_3,userlocation4_4,userlocation4_5,userlocation4_6);
			System.out.println("1880270增加一个key4多个value4："+count);

			System.out.println("1--------------");
			Set<String> sets = JedisClientUtil.getSetLAC_CI(PopConstant.REDIS_UNL_PREFIX+subsid1);
			Iterator<String> it = sets.iterator();
			while(it.hasNext()){
				String next = it.next();
				System.out.println("sets:"+next);
			}
			System.out.println("2--------------");
			Set<String> sets2 = JedisClientUtil.getSetLAC_CI(PopConstant.REDIS_UNL_PREFIX+subsid2);
			Iterator<String> it2 = sets2.iterator();
			while(it2.hasNext()){
				String next = it2.next();
				System.out.println("sets2:"+next);
			}
			System.out.println("3-------------");
			Set<String> sets3 = JedisClientUtil.getSetLAC_CI(PopConstant.REDIS_UNL_PREFIX+subsid3);
			Iterator<String> it3 = sets3.iterator();
			while(it3.hasNext()){
				String next = it3.next();
				System.out.println("sets3:"+next);
			}
			System.out.println("4--------------");
			Set<String> sets4 = JedisClientUtil.getSetLAC_CI(PopConstant.REDIS_UNL_PREFIX+subsid4);
			Iterator<String> it4 = sets4.iterator();
			while(it4.hasNext()){
				String next = it4.next();
				System.out.println("sets4:"+next);
			}
			
			
			boolean s1 = JedisClientUtil.sismember(PopConstant.REDIS_UNL_PREFIX+subsid1, userlocation1_1);
			boolean s0 = JedisClientUtil.sismember(PopConstant.REDIS_UNL_PREFIX+subsid2, userlocation2_1);
			System.out.println("subsid1是否存在："+s0+";subsid2是否存在："+s1);
			
			count = JedisClientUtil.delKeyValue(PopConstant.REDIS_UNL_PREFIX+subsid1);
			System.out.println("删除key-value="+count);
			count = JedisClientUtil.delKeyValue(PopConstant.REDIS_UNL_PREFIX+subsid2);
			System.out.println("删除key-value="+count);
			count = JedisClientUtil.delKeyValue(PopConstant.REDIS_UNL_PREFIX+subsid3);
			System.out.println("删除key-value="+count);
			count = JedisClientUtil.delKeyValue(PopConstant.REDIS_UNL_PREFIX+subsid4);
			System.out.println("删除key-value="+count);
			
			 s1 = JedisClientUtil.sismember(PopConstant.REDIS_UNL_PREFIX+subsid1, userlocation1_1);
			 s0 = JedisClientUtil.sismember(PopConstant.REDIS_UNL_PREFIX+subsid2, userlocation2_1);
			System.out.println("subsid1是否存在："+s0+";subsid2是否存在："+s1);


	    }

}
