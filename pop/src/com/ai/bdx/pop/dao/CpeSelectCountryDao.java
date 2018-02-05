package com.ai.bdx.pop.dao;


import java.util.List;

import com.ai.bdx.pop.bean.CpeUserInfo;
import com.ai.bdx.pop.bean.DimCpeStation;

public interface CpeSelectCountryDao {

	public List<DimCpeStation> selectCountry(String countryName, String cityName);

}
