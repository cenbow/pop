package com.ai.bdx.pop.service;


import java.util.List;

import com.ai.bdx.pop.bean.DimCpeStation;

public interface CpeSelectStationService {

	
	/**
	 * 基站查询
	 * 事务性操作
	 * @param subsid
	 */
	public List<DimCpeStation> selectCpeStationService(String stationName, String cityName);
	
	/**
	 * 手机号码查询
	 * 事务性操作
	 * @param productNo
	 */
//	public void selectCpeProductNo(String productNo)throws Exception {
//	}
}