package com.ailk.bdx.pop.adapter.dao;


import java.util.List;

import com.ailk.bdx.pop.adapter.model.custom.CpeLockNetInfo;

public interface ICpeUserInfoDao {
	/**
	 * 根据CPE设备号查询设备信息
	 * @param subsid
	 * @return
	 */
	public List<CpeLockNetInfo> queryBySubsid(String subsid)
			throws Exception;
}
