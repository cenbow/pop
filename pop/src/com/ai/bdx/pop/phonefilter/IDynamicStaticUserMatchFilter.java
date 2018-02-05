package com.ai.bdx.pop.phonefilter;

 

/**
 * 动态用户和静态用户匹配
 * @author zhangyb5
 *
 */
public interface IDynamicStaticUserMatchFilter {
 
	/**
	 * 动静态用户匹配	
	 * @param activityCode 活动编码
	 * @param userAccount 用户号码
	 * @return true 校验通过，false不通过
	 */
	public boolean matchUserAccount(String activityCode, String userAccount);
	
	
}
