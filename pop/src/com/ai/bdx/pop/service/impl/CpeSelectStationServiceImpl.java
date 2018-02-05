package com.ai.bdx.pop.service.impl;

import java.util.List;

import com.ai.bdx.pop.bean.DimCpeStation;
import com.ai.bdx.pop.dao.CpeSelectStationDao;
import com.ai.bdx.pop.service.CpeSelectStationService;

public class CpeSelectStationServiceImpl implements CpeSelectStationService{

	private CpeSelectStationDao cpeSelectStationDao;
	public void setCpeSelectStationDao(CpeSelectStationDao cpeSelectStationDao) {
		this.cpeSelectStationDao = cpeSelectStationDao;
	}
	public List<DimCpeStation> selectCpeStationService(String stationName, String cityName) {
		//调用dao层
		return cpeSelectStationDao.selectStation(stationName,cityName);
	}

}
