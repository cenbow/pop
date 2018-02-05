package com.ai.bdx.pop.phonefilter.impl.local;

import it.unimi.dsi.fastutil.bytes.Byte2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.shorts.Short2ObjectOpenHashMap;

import java.util.BitSet;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;

import com.ai.bdx.pop.phonefilter.IDynamicStaticUserMatchFilter;
import com.ai.bdx.pop.util.ContactControlUtil;
import com.ai.bdx.pop.util.PopConstant;
import com.ai.bdx.pop.util.SimpleCache;
import com.asiainfo.biframe.utils.string.StringUtil;

public class DynamicStaticUserMatchFilterImpl implements IDynamicStaticUserMatchFilter {
	private static Logger log = LogManager.getLogger();
	private JdbcTemplate jdbcTemplate;
 

	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
 

 
  
	/**
	 * 匹配用户号码
	 * @param activityCode
	 * @param userAccount
	 */
	public boolean matchUserAccount(String activityCode, String userAccount) {
		log.debug("user_account:" + userAccount);
	 
		if (notNeedMatchUserAccount(activityCode)) {
			return true;
		}

		String ruleCustKey = PopConstant.POP_RULE_CUST_CACHE_PREFIX.replace("{RULEID}",activityCode);
		
		//与静态客户群匹配
		Byte2ObjectOpenHashMap<Short2ObjectOpenHashMap<BitSet>> custMap = (Byte2ObjectOpenHashMap<Short2ObjectOpenHashMap<BitSet>>) SimpleCache
				.getInstance().get(ruleCustKey);
		
		if (custMap == null) {//如果缓存中没有清单，则重新加载
			custMap = ContactControlUtil.loadCustListByActiveCode(activityCode);
		}
		if (ContactControlUtil.matchUserAccount(custMap, userAccount)) {
			return true;
		}
		return false;
	}

	/**
	 * 检查活动是否需要匹配静态用户
	 * @param ruleId
	 */
	private boolean notNeedMatchUserAccount(String ruleId) {
		boolean flag = false;
		try {
			String custType = (String) SimpleCache.getInstance().get(PopConstant.CACHE_KEY_ACTIVITY_CUSTGROUP_TYPE + "_" + ruleId);
			if (custType == null) {
				List<Map> data = this.getJdbcTemplate().queryForList(
						"select custgroup_type from POP_POLICY_RULE_CUSTGROUP where policy_rule_id = ?",
						new String[] { ruleId });
			 
				if (CollectionUtils.isEmpty(data)) {
					custType = "0";
				} else {
					custType = "1";
				}
				SimpleCache.getInstance().put(PopConstant.CACHE_KEY_ACTIVITY_CUSTGROUP_TYPE + "_" + ruleId,
						custType, PopConstant.CACHE_TIME);
			}
			if ("0".equals(custType)) {//无静态客户群
				flag = true;
			}
		} catch (Exception e) {
			log.error("notNeedMatchUserAccount[" + ruleId + "] error:", e);
		}
		return flag;
	}
}
