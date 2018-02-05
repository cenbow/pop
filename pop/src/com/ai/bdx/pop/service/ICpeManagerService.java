package com.ai.bdx.pop.service;

import java.util.List;
import java.util.Map;

import redis.clients.jedis.exceptions.JedisException;

import com.ai.bdx.pop.bean.CpeAccessibleLacCiBean;
import com.ai.bdx.pop.bean.CpeLockLacCiBean;
import com.ai.bdx.pop.bean.CpeUserInfo;

public interface ICpeManagerService {
	/**
	 * 定期扫描存放BOSSCPE用户开户信息的FTP上的指定目录
	 */
	public void getBoss2PopCpeInstallInfo();
	
	/**
	 * CPE单用户参数重置
	 * @param subsid
	 * @param productNo
	 * @param userLocations
	 * @param cpeLockedNetLacCiCount
	 * @throws Exception
	 */
	public void cpeUserReset(String subsid,
			String productNo,
			String userLocations,
			int cpeLockedNetLacCiCount,int netLockFlag)throws JedisException, Exception ;
	
	/**
	 * 查询一个CPE设备的已有锁网小区
	 * @return
	 * @throws Exception
	 */
	public List<CpeLockLacCiBean> queryLockedLacCi(String subsid,String productNo)throws Exception;
	
	/**
	 * 查询一个CPE的可接入小区
	 * @param cpeAccessibleLacCiBean
	 * @return
	 * @throws Exception
	 */
	public List<CpeAccessibleLacCiBean> queryAccessibleLacCi(
			CpeAccessibleLacCiBean cpeAccessibleLacCiBean) throws Exception;
	
	/**
	 * 在Redis和数据库 中批量记录CPE开户信息
	 * @param list
	 * @throws Exception
	 */
	public void batchUpdate(List<CpeUserInfo> cpeUserInfoList,String fileName)
			throws Exception;
	
	/**
	 * 根据锁网状态、业务状态、用户状态查询CPE信息
	 * @param netLockFlag
	 * @param busiType
	 * @param userStatus
	 * @throws Exception
	 */
	public List<Map<String, Object>> queryBatchResetCpe(int netLockFlag, int busiType)
			throws Exception;
	
	/*
	 * 根据锁网状态、业务状态、用户状态查询用户cpe信息
	 * @param param 查询列表
	 */
	public List<Map<String, Object>> queryAllResetByFlag(Map<String,Object> param)
			throws Exception;
	
}
