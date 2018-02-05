package com.ai.bdx.pop.adapter.dao;

import java.util.List;

import com.ai.bdx.pop.bean.DimCpeStation;

public interface IDimCpeStationDao {
	/**
	 * 根据userLocation查询 所属市 和 所属县
	 * @return
	 */
	public List<DimCpeStation> queryPosition(String userLocation)throws Exception;
}
