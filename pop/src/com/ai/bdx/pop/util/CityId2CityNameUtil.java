package com.ai.bdx.pop.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.cxf.common.util.StringUtils;

import com.ai.bdx.frame.privilegeServiceExt.service.IUserPrivilegeCommonService;
import com.asiainfo.biframe.privilege.ICity;
import com.asiainfo.biframe.utils.spring.SystemServiceLocator;

public class CityId2CityNameUtil {
	private static final String CACHE_KEY_4_CITY_ID_NAME_MAP = "city_id_name_map_";
	protected static final String PRIVILEGE_SERVICE_NAME = "userPrivilegeCommonService";
	@SuppressWarnings("unchecked")
	public static String getCityNameByCache(String cityId) throws Exception {
		if (StringUtils.isEmpty(cityId)) {
			return "";
		}
		Map<String, String> map = (Map<String, String>) SimpleCache.getInstance().get(CACHE_KEY_4_CITY_ID_NAME_MAP);
		if (map == null || map.isEmpty()) {
			return StringUtils.isEmpty(getCityName(cityId)) ? "" : getCityName(cityId);
		}else{
			return StringUtils.isEmpty(map.get(cityId)) ? getCityName(cityId) : map.get(cityId);
		}
	}
	
	public static String getCityName(String cityId) throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		IUserPrivilegeCommonService service = (IUserPrivilegeCommonService) SystemServiceLocator.getInstance().getService(PRIVILEGE_SERVICE_NAME);
		List<ICity> list = service.getAllCity();
		
		for (Iterator<ICity> ite = list.iterator(); ite.hasNext(); ) {
			ICity city = ite.next();
			if(city.getCityId() != null && city.getCityId().equalsIgnoreCase(cityId)){
				SimpleCache.getInstance().put(CACHE_KEY_4_CITY_ID_NAME_MAP, map, PopConstant.CACHE_TIME * 2);
				return city.getCityName();
			}
		}
		return "";
	}
}
