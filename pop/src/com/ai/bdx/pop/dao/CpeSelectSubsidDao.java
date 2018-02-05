package com.ai.bdx.pop.dao;


import java.util.List;

import com.ai.bdx.pop.util.CpeUserInfo;



public interface CpeSelectSubsidDao {

	public List<CpeUserInfo> selectCpe(String subsid, String productNo, String cityName, int busiType);

}
