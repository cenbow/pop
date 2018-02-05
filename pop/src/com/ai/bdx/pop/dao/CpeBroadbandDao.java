package com.ai.bdx.pop.dao;


import java.util.List;

import com.ai.bdx.pop.util.CpeUserInfo;



/**
 * CPE用户销户是变更状态到mysql  DAO接口
 * @author 林
 *
 */
public interface CpeBroadbandDao {
	//批量变更宽带变更
	public int[] updateCpeNetType(List<CpeUserInfo> list)throws Exception;
	//根据用户id查询用户信息
	public List<String> queryCpeUserInfoBySubsid(String subsid);
	//查询旧的策略号
	public String queryStrategyCodeBySubsid(String subsid);
	//查询新的策略号
	public String queryStrategyCodeBynetType(String net_type);
}