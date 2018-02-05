package com.ai.bdx.pop.dao;

import java.util.List;
import java.util.Map;

import com.asiainfo.biframe.exception.ServiceException;
import com.asiainfo.biframe.privilege.ICity;

public interface IMpmUserCityDao {

	/**
	 * 取得所有地市信息；
	 *
	 * @return
	 * @throws ServiceException
	 */
	public abstract List<ICity> getAllCity();

	/**
	 * 取得所有区县信息；
	 *
	 * @return
	 * @throws ServiceException
	 */
	public abstract List<ICity> getAllCounty();

	/**
	 * 取得地市信息；
	 *
	 * @param cityId
	 * @return
	 * @throws ServiceException
	 */
	public abstract ICity getCityById(String paramString);

	/**
	 * 取得地市信息；
	 *
	 * @param cityId
	 * @return
	 * @throws ServiceException
	 */
	public abstract ICity getCityByOldId(String paramString);

	/**
	 * 取得当前地市下所有地市信息；
	 *
	 * @param paramString
	 * @return
	 * @throws ServiceException
	 */
	public abstract List<ICity> getCityByParentId(String paramString);

	/**
	 * 通过用户ID获取用户可访问地市数据Scope
	 * @param userId
	 * @return
	 * @throws ServiceException
	 */
	public abstract IUserScope getUserScope(String userId) throws ServiceException;

	/**
	 * 根据查询对象查询用户数据
	 * @param queryMap
	 * @return
	 * @throws ServiceException
	 */
	public abstract List<ICity> getCitysByUserId(Map queryMap) throws ServiceException;

	/**
	 * 查询推荐对象用户地市范围数据
	 * @param queryMap
	 * @return
	 * @throws ServiceException
	 */
	public abstract List<ICity> getRecommendCitysByUserId(Map queryMap) throws ServiceException;

	public String getCountryIdByOldId(String countryId);
}
