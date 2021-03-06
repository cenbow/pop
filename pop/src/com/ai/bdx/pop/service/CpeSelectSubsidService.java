package com.ai.bdx.pop.service;


import java.util.List;

import com.ai.bdx.pop.util.CpeUserInfo;



public interface CpeSelectSubsidService {

	
	/**
	 * 单个号码查询
	 * 事务性操作
	 * @param subsid
	 */
	public List<CpeUserInfo> selectCpeSubsidService(String subsid,String productNo,String cityName,int busiType);
	
	/**
	 * 手机号码查询
	 * 事务性操作
	 * @param productNo
	 */
//	public void selectCpeProductNo(String productNo)throws Exception {
//	}
}