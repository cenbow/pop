package com.ai.bdx.pop.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.ai.bdx.pop.dao.IMpmUserCityDao;
import com.ai.bdx.pop.dao.IUserScope;
import com.ai.bdx.pop.model.privilege.LkgParamArea;
import com.ai.bdx.pop.model.privilege.LkgStaffScope;
import com.ai.bdx.pop.model.privilege.TdEparchyCityMcd;
import com.asiainfo.biframe.exception.ServiceException;
import com.asiainfo.biframe.privilege.ICity;
import com.asiainfo.biframe.utils.config.Configure;

public class MpmUserCityDaoImpl extends HibernateDaoSupport implements IMpmUserCityDao {

	public List<ICity> getAllCity() {
		//		List cityList = getHibernateTemplate().find("from User_City ");
		List<ICity> cityList = getHibernateTemplate().find("from TdEparchyCityMcd order by orderId");
		return cityList;
	}

	public List<ICity> getAllCounty() {
		//		List cityList = getHibernateTemplate().find("from User_City ");
		List<ICity> cityList = getHibernateTemplate().find("from TdCitycode order by orderId");
		return cityList;
	}

	public ICity getCityById(String cityId) {
		//		ICity city = (ICity)getHibernateTemplate().get(User_City.class, cityId);
		ICity city = (ICity) getHibernateTemplate().get(TdEparchyCityMcd.class, cityId);
		return city;
	}

	public ICity getCityByOldId(String cityId) {
		//		ICity city = (ICity)getHibernateTemplate().get(User_City.class, cityId);
		List<ICity> city = getHibernateTemplate().find("from TdEparchyCityMcd where remark= '" + cityId + "'");
		return city.get(0);
	}

	public String getCountryIdByOldId(String countryId) {
		List<LkgParamArea> list = getHibernateTemplate().find("from LkgParamArea where countryId= ?",
				new Object[] { countryId });
		return list.get(0).getCountryIdNew();
	}

	public List<ICity> getCityByParentId(String parentCityId) {
		//		List cityList = getHibernateTemplate().find("from User_City uc where uc.parentId='" + parentCityId + "'");
		//无此字段
		List<ICity> cityList = getHibernateTemplate().find(
				"from TdCitycode uc where uc.eparchyId='" + parentCityId + "' order by orderId");
		return cityList;
	}

	public IUserScope getUserScope(String userId) throws ServiceException {
		IUserScope userScope = (IUserScope) getHibernateTemplate().get(LkgStaffScope.class, userId);
		return userScope;
	}

	public List<ICity> getCitysByUserId(Map queryMap) throws ServiceException {
		List<ICity> result = null;
		if (queryMap.containsKey("prov")) { //省公司查询市
			result = getAllCity();
		} else if (queryMap.containsKey("city")) { //市公司查询区县
			result = getCityByParentId(queryMap.get("city").toString()); //以code为外键查询
		}
		return result;
	}

	public List<ICity> getRecommendCitysByUserId(Map queryMap) throws ServiceException {
		List<ICity> result = null;
		if (queryMap.containsKey("prov")) { //省公司查询市
			result = getAllCity();
		} else if (queryMap.containsKey("city")) { //市公司查询区县
			result = new ArrayList<ICity>();
			ICity city = getCityById(queryMap.get("city").toString());
			result.add(city);
			// 这里特意加上省代码,以为全省的推荐对象是公共的
			ICity cityProvince = getCityById(Configure.getInstance().getProperty("CENTER_CITYID"));
			result.add(cityProvince);
		}
		return result;
	}

}
