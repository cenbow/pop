package com.ai.bdx.pop.service.impl;


import java.util.List;

import com.ai.bdx.pop.bean.DimCpeStation;
import com.ai.bdx.pop.dao.CpeSelectCountryDao;
import com.ai.bdx.pop.service.CpeSelectCountryService;

public class CpeSelectCountryServiceImpl implements CpeSelectCountryService{

	private CpeSelectCountryDao cpeSelectCountryDao;
	public void setCpeSelectCountryDao(CpeSelectCountryDao cpeSelectCountryDao) {
		this.cpeSelectCountryDao = cpeSelectCountryDao;
	}
	public List<DimCpeStation> selectCpeCountryService(String countryName, String cityName) {
		//调用dao层方法
		return cpeSelectCountryDao.selectCountry(countryName, cityName);
	}

}
