package com.ailk.bdx.pop.adapter.service;

import com.ailk.bdx.pop.adapter.model.custom.CpeLockNetInfo;

public interface ICpeUserInfoService {
	
	/**
	 * CPE锁网方法
	 * @param cpeLockNetInfo
	 * @return CpeLockNetInfo 发送给POP的数据的封装对象（subsid,productNo,stationCode,lacCiDecId,userLocation,userLocations）
	 * @throws Exception
	 */
	public CpeLockNetInfo lockCpeNet(CpeLockNetInfo cpeLockNetInfo)throws Exception;
}
