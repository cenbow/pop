/**   
 * @Title: PccFeedbackEDRLogService.java
 * @Package com.ai.bdx.pop.service
 * @Description: POP同步PCC反馈日志job
 * @author A18ccms A18ccms_gmail_com   
 * @date 2015-10-16 上午10:42:08
 * @version V1.0   
 */
package com.ai.bdx.pop.service;

import java.util.List;

import com.ai.bdx.pop.bean.EDRBean;

/**
 * @ClassName: PccFeedbackEDRLogService
 * @Description: POP同步PCC反馈日志job
 * @author jinlong
 * @date 2015-10-16 上午10:42:08
 * 
 */
public interface IPccFeedbackEDRLogService {

	/**
	 * @Title: POP同步PCC反馈日志方法
	 * @Description: TODO
	 * @param     
	 * @return void 
	 * @throws
	 */
	public void getPccFeedbackEDRLog();
	
	/**
	 * @return 
	 * @Title: getPccFeedbackEDRLogFile
	 * @Description: 获取EDR日志文件，该日志文件为GZ压缩文件，压缩文件内为CSV文件，文件内容为“,”(逗号)分割的文本文件.
	 * 				文件名称样例：EDR_UPCC231_MPU023_0000_20150518163205.csv.gz
	 * 				文件内容样例：110,2015-05-18 16:32:05,8613707118687,,8613707118687,,,,,,,,,,,,,,,,,,,,12270050000000000000000000000003,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,
	 * @param @param fileName
	 * @param @return     
	 * @return List<EDRBean> 
	 * @throws
	 */
	public List<EDRBean> getPccFeedbackEDRLogFile(String fileName);

	/**
	 * @Title: updatePolicyRuleFeedbackInformation
	 * @Description: 更新PCC的EDR反馈信息到规则
	 * @param @param list    
	 * @return void 
	 * @throws
	 */
	public void updatePolicyRuleFeedbackInformation(List<EDRBean> list);

	
	
}
