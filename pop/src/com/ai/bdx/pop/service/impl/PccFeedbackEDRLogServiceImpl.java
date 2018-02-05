/**   
 * @Title: PccFeedbackEDRLogServiceImpl.java
 * @Package com.ai.bdx.pop.service.impl
 * @Description: TODO(用一句话描述该文件做什么)
 * @author A18ccms A18ccms_gmail_com   
 * @date 2015-10-16 上午10:43:04
 * @version V1.0   
 */
package com.ai.bdx.pop.service.impl;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPInputStream;

import org.apache.commons.net.ftp.FTPFile;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ai.bdx.pop.bean.EDRBean;
import com.ai.bdx.pop.service.IPccFeedbackEDRLogService;
import com.ai.bdx.pop.util.ftp.ApacheFtpUtil;
import com.ai.bdx.pop.util.ftp.FtpCONST;
import com.ai.bdx.pop.util.ftp.FtpConfig;
import com.ai.bdx.pop.util.ftp.FtpConfigure;

/**
 * @ClassName: PccFeedbackEDRLogServiceImpl
 * @Description: POP同步PCC反馈日志job
 * @author jinlong
 * @date 2015-10-16 上午10:43:04
 * 
 */
public class PccFeedbackEDRLogServiceImpl implements IPccFeedbackEDRLogService {

	private static Logger Log = LogManager.getLogger();
	private static final String REGEX=",";
	private static final int SIZE=72;

	@Override
	public void getPccFeedbackEDRLog() {
		String fileName = "d:\\EDR_UPCC231_MPU023_0001_20150526114802.csv.gz";
		Log.debug("扫描FTP存放EDR日志文件目录开始");
		scanFtpTask();
		List<EDRBean> list = this.getPccFeedbackEDRLogFile(fileName);
		this.updatePolicyRuleFeedbackInformation(list);
	}

	/**
	 * @Title: getPccFeedbackEDRLogFile
	 * @Description: 获取EDR日志文件，该日志文件为GZ压缩文件，压缩文件内为CSV文件，文件内容为“,”(逗号)分割的文本文件.
	 * 				文件名称样例：EDR_UPCC231_MPU023_0000_20150518163205.csv.gz
	 * 				文件内容样例：110,2015-05-18 16:32:05,8613707118687,,8613707118687,,,,,,,,,,,,,,,,,,,,12270050000000000000000000000003,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,
	 * @param     
	 * @return void 
	 * @throws
	 */
	@Override
	public List<EDRBean> getPccFeedbackEDRLogFile(String fileName) {
		Log.debug("获取EDR日志文件开始");
		BufferedReader reader = null;
		//String fileName = "d:\\EDR_UPCC231_MPU023_0001_20150526114802.csv.gz";
		List<EDRBean> list = new ArrayList<EDRBean>(); 
		try{
			reader = new BufferedReader(new InputStreamReader(new GZIPInputStream(new FileInputStream(fileName)), "UTF-8"));
			//按行读取
			String line = null;
			while((line = reader.readLine()) != null){
				System.out.println(line);
				String[] edrStrings=line.split(REGEX,SIZE);
				EDRBean edrBean = setEdrStringArrayToEdrBean(edrStrings);
				list.add(edrBean);
			}
			Log.debug("获取EDR日志:"+fileName+"成功");
		}catch(Exception e){
			Log.error("获取EDR日志:"+fileName+"异常");
			e.printStackTrace();
		}finally{
			try {
				reader.close();
			} catch (IOException e) {
				Log.error("获取EDR日志reader流关闭失败");
				e.printStackTrace();
			}
			Log.debug("获取EDR日志reader流关闭成功");
		}
		Log.debug("获取EDR日志:"+fileName+"成功");
		
		return list;
	}

	/**
	 * @Title: updatePolicyRuleFeedbackInformation
	 * @Description: 更新PCC的EDR反馈信息到规则
	 * @param @param list    
	 * @return void 
	 * @throws
	 */
	@Override
	public void updatePolicyRuleFeedbackInformation(List<EDRBean> list) {
		Log.debug("更新PCC的EDR反馈信息到规则");
		
	}
	
	/**
	 * @Title: setEdrStringArrayToEdrBean
	 * @Description: String[] 转换为EDR实体类： EDRBean
	 * @param @param edrStrings
	 * @param @return    
	 * @return EDRBean 
	 * @throws
	 */
	public EDRBean setEdrStringArrayToEdrBean(String[] edrStrings) {
		EDRBean edrBean = new EDRBean();
		edrBean.setTriggerType(edrStrings[0]);//标识产生此EDR单据的消息类型
		edrBean.setTime(edrStrings[1]); //EDR单据产生的时间，格式为YYYY-MM-DD HH:MM:SS，本地时间
		edrBean.setSubscriberIdentifier(edrStrings[2]); ////IMSI或者MSISDN或用户名
		edrBean.setIMSI(edrStrings[3]); //IMSI
		edrBean.setServiceName(edrStrings[24]); //业务名称
		edrBean.setRuleName(edrStrings[25]); //Rule名称
		return edrBean;
	}

	private FtpConfig ftpConfig;
	 //initFtpConfig:初始化ftp配置信息
	/**
	 * @Title: initFtpConfig
	 * @Description: 获取FTP配置信息，例如：HUAWEI_ERD_LOG 华为ERD日志文件信息
	 * @param     
	 * @return void 
	 * @throws
	 */
	public void initFtpConfig(String ftpConst){
		ftpConfig = FtpConfigure.getInstance().getFtpConfigByTypes(ftpConst);
	}
	/**
	 * @Title: scanFtpTask
	 * @Description: 扫描FTP文件夹下的反馈日志文件
	 * @param     
	 * @return void 
	 * @throws
	 */
	private void scanFtpTask() {
		initFtpConfig(FtpCONST.HUAWEI_ERD_LOG);
		ApacheFtpUtil apacheFtp = null;
		try {
			Log.debug("登陆ftp服务器:ip={}, port={},user={},passwd={},encode={}",ftpConfig.getFTP_ADDRESS(),ftpConfig.getFTP_PORT(), ftpConfig.getFTP_USER(),ftpConfig.getFTP_PASSWORD(), ftpConfig.getFTP_ENCODE());
			apacheFtp = ApacheFtpUtil.getInstance(ftpConfig.getFTP_ADDRESS(),Integer.parseInt(ftpConfig.getFTP_PORT()), ftpConfig.getFTP_USER(),ftpConfig.getFTP_PASSWORD(), ftpConfig.getFTP_ENCODE());
			String ftpPath = ftpConfig.getFTP_PATH();
			apacheFtp.changeDir(ftpPath);
			List<String> fileList = apacheFtp.listFile(ftpPath);//获取到要下载的文件列表
			for(String str:fileList){
				System.out.println(str);
			}
			
		} catch (Exception e) {
			Log.error("", e);
		} finally {
			if (apacheFtp != null) {
				apacheFtp.forceCloseConnection();
			}
		}
		
	}
	
	
	
}
