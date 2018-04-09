package com.ailk.bdx.pop.adapter.dao;

import java.util.List;

import com.ailk.bdx.pop.adapter.model.DimCpeLacCi;

public interface IDimCpeLacCiDao {
	/**
	 * 根据小区userLocation查询小区信息
	 * @param userLocation
	 * @return
	 */
	public List<DimCpeLacCi> queryByLacCi(String userLocation)
		throws Exception;
}
