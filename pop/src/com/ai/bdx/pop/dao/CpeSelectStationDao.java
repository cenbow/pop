package com.ai.bdx.pop.dao;


import java.util.List;

import com.ai.bdx.pop.bean.DimCpeStation;

public interface CpeSelectStationDao {

	public List<DimCpeStation> selectStation(String stationName, String cityName);

}
