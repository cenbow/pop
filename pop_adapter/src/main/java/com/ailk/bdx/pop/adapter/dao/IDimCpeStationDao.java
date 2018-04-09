package com.ailk.bdx.pop.adapter.dao;


/**
 * 农村基站维表Dao接口类
 * @author hpa
 *
 */
public interface IDimCpeStationDao {
	/**
	 * 根据农村基站Code查询符合基站的个数
	 * @param stationCode
	 */
	public Integer queryByStationCode(String stationCode)
			throws Exception;
}
