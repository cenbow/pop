package com.ai.bdx.pop.service;


import java.util.List;

import com.ai.bdx.pop.bean.CpeUserInfo;
import com.ai.bdx.pop.bean.DimCpeStation;

public interface CpeSelectCountryService {

	
	/**
	 * 村庄查询
	 * 事务性操作
	 * @param subsid
	 */
	public List<DimCpeStation> selectCpeCountryService(String countryName, String cityName);
	
	/**
	 * 手机号码查询
	 * 事务性操作
	 * @param productNo
	 */
//	public void selectCpeProductNo(String productNo)throws Exception {
//	}
}