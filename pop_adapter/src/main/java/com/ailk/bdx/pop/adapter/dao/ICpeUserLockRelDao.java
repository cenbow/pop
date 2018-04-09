package com.ailk.bdx.pop.adapter.dao;

import java.util.List;

import com.ailk.bdx.pop.adapter.model.CpeUserLockRel;

public interface ICpeUserLockRelDao {
	/**
	 * 添加CPE用户锁网小区关系
	 * @param cpeUserLockRel
	 * @return
	 * @throws Exception
	 */
	public boolean insert(CpeUserLockRel cpeUserLockRel)
			throws Exception;
	
	/**
	 * 根据CPE设备唯一号 查询CPE用户锁网小区关系
	 * @param subsid CPE设备唯一号
	 * @return
	 * @throws Exception
	 */
	public List<CpeUserLockRel> queryBySubsid(String subsid)
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
