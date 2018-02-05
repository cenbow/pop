/**   
 * @Title: IPopBossAddProdService.java
 * @Package com.ai.bdx.pop.wservice
 * @Description: TODO(用一句话描述该文件做什么)
 * @author A18ccms A18ccms_gmail_com   
 * @date 2015-7-29 上午11:24:22
 * @version V1.0   
 */
package com.ai.bdx.pop.wservice;

import javax.jws.WebService;

/**
 * @ClassName: IPopBossAddProdService
 * @Description: POP和BOSS接口 流量产品新增工单。
 * 				流量产品新增、退订时，BOSS要向POP平台发送工单
 * @author jinlong
 * @date 2015-7-29 上午11:24:22
 * 
 */
@WebService
public interface IPopBossAddProdWsService {

	/**
	 * @Title: addProdService
	 * @Description: BOSS订购
	 * @param @param xmlStr
	 * @param @return    
	 * @return String 
	 * @throws
	 */
	public String addProdService(String xmlStr);
	
	/**
	 * @Title cpeInstallAdapterTrigger
	 * @Description:BOSS-->POP开户
	 * @param xmlStr
	 * @return String
	 */
	public String cpeInstallAdapterTrigger(String xmlStr);
	
	/**
	 * @Title scanFtpForDeleteTrigger
	 * @Description:BOSS-->POP销户
	 * @param xmlStr
	 * @return String
	 */
	public String scanFtpForDeleteTrigger(String xmlStr);
	
	/**
	 * @Title cpeImsiChangeTrigger
	 * @Description:BOSS-->POP IMSI变更
	 * @param xmlStr
	 * @return String
	 */
	 
	 public String cpeImsiChangeTrigger(String xmlStr);
}
