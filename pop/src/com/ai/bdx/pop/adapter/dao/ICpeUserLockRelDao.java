package com.ai.bdx.pop.adapter.dao;

import java.util.List;

import com.ai.bdx.pop.bean.CpeLockLacCiBean;
import com.ai.bdx.pop.bean.CpeUserLockRelBean;

public interface ICpeUserLockRelDao {
	/**
	 * 添加CPE用户锁网小区关系
	 * @param cpeUserLockRel
	 * @return
	 * @throws Exception
	 */
	public boolean insert(CpeUserLockRelBean cpeUserLockRelBean)
			throws Exception;
	
	/**
	 * 批量插入CPE用户锁网小区关系
	 * @param cpeUserLockRelBean
	 * @param userLocationList
	 * @return
	 * @throws Exception
	 */
	public boolean batchInsert(CpeUserLockRelBean cpeUserLockRelBean,List<String> userLocationList)
		throws Exception;
	
	/**
	 * 查询一个CPE设备的已有锁网小区
	 * @param subsid
	 * @param productNo
	 * @throws Exception
	 */
	public List<CpeLockLacCiBean> queryLockedLacCi(String subsid,String productNo)
			throws Exception;
	
	/**
	 * 根据CPE设备唯一号查询该CPE现在的锁网小区UserLocation 集合
	 * @param subsid CPE设备唯一号
	 * @return
	 * @throws Exception
	 */
	public List<String> queryUserLocationsBySubsid(String subsid)
			throws Exception;
}
