package com.ai.bdx.pop.service;

import java.util.List;

/**
 * 
 * creator：sjw 
 * create date：2015年4月23日
 * useful：    回填状态接口
 * PCC向POP平台实时反馈策略执行结果(ftp取)并同步状态到任务用户发送表及规则用户汇总表
 * modify
 * ===================================================
 *  person             date               reason
 * ===================================================
 */
public interface IPopStatusService {

	/**
	 * 
	 * updateSendOddStatus:批量更新派单号码状态和时间回填
	 * @param ruleid 规则id
	 * @param taskid 任务id
	 * @param phoneStatusList  String[]={手机号，状态，时间},时间格式 yyyy-MM-dd HH:mm:ss
	 * @return void
	 */
	public void updateSendOddStatus(String ruleid,String taskid,List<String[]> phoneStatusList);
	/**
	 * 
	 * updateSendOddStatus:更新派单号码状态和时间回填
	 * @param ruleid
	 * @param taskid
	 * @param phoneStatus 
	 * @return void
	 */
	public void updateSendOddStatus(String ruleid,String taskid,String[] phoneStatus);
}
