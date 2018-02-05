package com.ai.bdx.pop.service;


import java.util.List;
import java.util.Map;

import com.ai.bdx.pop.util.CpeUserInfo;



/**
 * CPE用户宽带变更是变更状态到mysql  SERVICE接口
 * @author 
 *
 */
public interface CpeBroadbandService {
	//宽带变更接口
	public Map updateCpeNetType(List<CpeUserInfo> list)throws Exception;
	//PCC接口
	public Map<String ,String> pop2pccWebService(List<CpeUserInfo> list)throws Exception;
}
