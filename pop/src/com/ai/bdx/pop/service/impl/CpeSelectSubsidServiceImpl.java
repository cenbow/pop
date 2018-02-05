package com.ai.bdx.pop.service.impl;


import java.util.List;

import com.ai.bdx.pop.dao.CpeSelectSubsidDao;
import com.ai.bdx.pop.service.CpeSelectSubsidService;
import com.ai.bdx.pop.util.CpeUserInfo;

public class CpeSelectSubsidServiceImpl implements CpeSelectSubsidService{

	private CpeSelectSubsidDao cpeSelectSubsidDao;
	
	public void setCpeSelectSubsidDao(CpeSelectSubsidDao cpeSelectSubsidDao) {
		this.cpeSelectSubsidDao = cpeSelectSubsidDao;
	}
	@Override
	public List<CpeUserInfo> selectCpeSubsidService(String subsid, String productNo,
			String cityName, int busiType) {
		//调用dao层
		return cpeSelectSubsidDao.selectCpe(subsid,productNo,cityName,busiType);
	}

}
