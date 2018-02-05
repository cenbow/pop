package com.asiainfo.biapp.pop.redis.ClientTest;

import redis.clients.jedis.Jedis;




	public class RedisClientTest3 {  
	      static String constr = "192.168.17.134" ;  
       public static Jedis getRedis(){  
        Jedis jedis = new Jedis(constr) ;  
         return jedis ;  
      }  

     public static void main(String[] args){  
        Jedis j = RedisClientTest3. getRedis() ;  
            String output ;  
	         j.set( "hello", "world" ) ;  
	          output = j.get( "hello") ;  
         System. out.println(output) ;  
      }  

	}
