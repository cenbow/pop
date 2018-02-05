package com.ai.bdx.pop.adapter.dao;

import java.util.List;
import java.util.Map;

import com.ai.bdx.pop.bean.CpeUserInfo;

public interface ICpeUserInfoDao {
	/**
	 * 添加CPE用户锁网小区关系
	 * @param cpeUserLockRel
	 * @return
	 * @throws Exception
	 */
	public boolean update(CpeUserInfo cpeUserInfo)
			throws Exception;
	
	public boolean updatePosition(CpeUserInfo cpeUserInfo)
			throws Exception;
	
	
	public CpeUserInfo query(String subsid)throws Exception;
	
	public List<CpeUserInfo> query(CpeUserInfo cpeUserInfo)throws Exception;
	
	public List<Map<String, Object>> query(int netLockFlag, int busiType)
			throws Exception;
	
	/**
	 * 批量记录CPE开户信息
	 * @param cpeUserInfoList
	 * @throws Exception
	 */
	public void batchUpdate(List<CpeUserInfo> cpeUserInfoList,String fileName)throws Exception;
	public List<Map<String, Object>> queryAllResetByFlag(Map<String,Object> param) throws Exception;
}
