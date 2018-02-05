package com.ai.bdx.pop.service.impl;

import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;

import com.ai.bdx.pop.jedis.JedisClientUtil;
import com.ai.bdx.pop.util.PopConstant;


public class WebServiceChangeImsiService {
	private static Logger log = 
			LogManager.getLogger(WebServiceChangeImsiService.class);
	private JdbcTemplate jdbcTemplate;
		
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
			this.jdbcTemplate = jdbcTemplate;
	}
	
	public void changeImsi(String prono,String oldMsi,String newMsi) throws Exception
	{
		changeDbImgiInfo(prono, oldMsi, newMsi);
		changeRedisImsiInfo(prono, oldMsi, newMsi);
	}
	
	private void changeDbImgiInfo(String proNo,String oldMsi,String newMsi) throws Exception
	{
		String[] sqls = {
				String.format("update cpe_user_info set subsid='%s' where product_no='%s' and subsid='%s' ",newMsi,proNo,oldMsi),
				String.format("update cpe_user_lock_rel set subsid='%s' where product_no='%s' and subsid='%s' ",newMsi,proNo,oldMsi)
			};
			int[] result = jdbcTemplate.batchUpdate(sqls);
	}
	
	//修正redis中的数据
	private void changeRedisImsiInfo(String proNo,String oldMsi,String newMsi) throws Exception
	{
		//修改锁网关系
		String oldKey=PopConstant.REDIS_UNL_PREFIX+oldMsi;
		String newKey=PopConstant.REDIS_UNL_PREFIX+newMsi;
		Set<String> tmpset = JedisClientUtil.smembers(oldKey);
		if(tmpset!=null&&tmpset.size()>0)
		{
			for(String s:tmpset)
			{
				JedisClientUtil.addKeyValues(newKey, s);
			}
		}
		JedisClientUtil.delKeyValue(oldKey);
		//修改上网位置统计
		oldKey = PopConstant.REDIS_CPE_USERLOCATION_STATISTICS_PREFIX+oldMsi;
		newKey = PopConstant.REDIS_CPE_USERLOCATION_STATISTICS_PREFIX+newMsi;
		tmpset = JedisClientUtil.zrevrange(oldKey, 0, -1);
		if(tmpset!=null&&tmpset.size()>0)
		{
			for(String s:tmpset)
			{
				
				double score = JedisClientUtil.zscore(oldKey, s);
				JedisClientUtil.zincrby(newKey, score, s);
			}
		}
		JedisClientUtil.delKeyValue(oldKey);
	} 
}
