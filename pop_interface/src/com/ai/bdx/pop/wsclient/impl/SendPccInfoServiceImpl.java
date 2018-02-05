package com.ai.bdx.pop.wsclient.impl;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import org.apache.axis2.databinding.types.soapencoding.Array;
import org.apache.axis2.transport.http.HTTPConstants;
import org.apache.poi.ss.usermodel.Workbook;

import com.ai.bdx.pop.wsclient.ISendPccInfoService;
import com.ai.bdx.pop.wsclient.ResultModel;
import com.ai.bdx.pop.wsclient.bean.CpeLacCi;
import com.ai.bdx.pop.wsclient.bean.CpeLacCiInfoDm;
import com.ai.bdx.pop.wsclient.bean.CpeStation;
import com.ai.bdx.pop.wsclient.bean.CpetStation;
import com.ai.bdx.pop.wsclient.bean.SprBusinessBean;
import com.ai.bdx.pop.wsclient.util.ApacheFtpUtil;
import com.ai.bdx.pop.wsclient.util.PoiExcelUtil;
import com.ai.bdx.pop.wsclient.util.PolicyIdUtil;
import com.ai.bdx.pop.wsclient.util.PropUtil;
import com.ai.bdx.pop.wsclient.util.SftpUtil;
import com.ai.bdx.pop.wsclient.util.SocketClient;
import com.ai.bdx.pop.wsclient.util.StringUtil;
import com.ai.bdx.pop.wsclient.util.TelnetUtil;
import com.chinamobile.pcrf.PCRFSoapStub;
import com.chinamobile.pcrf.PCRFSoapStub.AddUsrLocationResponse;
import com.chinamobile.pcrf.PCRFSoapStub.DelUsrLocationResponse;
import com.chinamobile.pcrf.PCRFSoapStub.UpdateUsrLocation;
import com.chinamobile.pcrf.PCRFSoapStub.UpdateUsrLocationResponse;

/**
 * 
 * @author yuanyk
 * 
 */
public class SendPccInfoServiceImpl implements ISendPccInfoService {
	private static Logger log = Logger.getLogger(SendPccInfoServiceImpl.class
			.getName());
	private static String productFileTime = "";
	private static final String lastIndex = "\r\n";
	private String prefix_phone = PropUtil.getProp("prefix_phone",
			"spr.properties");

	/**
	 * 发送策略信息脚本到PCRF
	 * 
	 * @param pccScriptInfos
	 *            ：key-厂商标示（1-华为，2-中兴，3-爱立信，...）； value-脚本路径
	 * @return
	 */
	public ResultModel sendPccInfo(Map<String, String> pccScriptInfos)
			throws Exception {
		ResultModel result = new ResultModel();
		String filePath = ""; // 文件路径
		log.info("begin to send scripts to pcrf----------->");
		if (pccScriptInfos != null) {
			for (String key : pccScriptInfos.keySet()) {
				// 1.华为
				if ("1".equals(key)) {
					filePath = pccScriptInfos.get(key);
					log.info("begin to send scripts to HuaWei-----------> and the filePath is : "
							+ filePath);
					TelnetUtil.readFileByLines(filePath);

					result.setResultCode("0");
					result.setMessage("发送策略到华为PCRF成功");
					log.info("end to send scripts to HuaWei-----------> success");
				}
				// 2.中兴
				if ("2".equals(key)) {
					filePath = pccScriptInfos.get(key);
					log.info("begin to send scripts to ZTE-----------> and the filePath is : "
							+ filePath);
					SocketClient socketClient = new SocketClient();
					socketClient.start(filePath);

					result.setResultCode("0");
					result.setMessage("发送策略到中兴PCRF成功");
					log.info("end to send scripts to ZTE-----------> success");
				}
				// 3.爱立信
				if ("3".equals(key)) {
					filePath = pccScriptInfos.get(key);
					log.info("begin to send scripts to ERICSSON-----------> and the filePath is : "
							+ filePath);
					// filePath 文件名还是路径+文件名？
					PolicyIdUtil.sendPcrfInformation(filePath);

					result.setResultCode("0");
					result.setMessage("发送策略到爱立信PCRF成功");
					log.info("end to send scripts to ERICSSON-----------> success");
				}
			}
		} else {
			log.info("入参为空，请check!");
			result.setResultCode("1");
			result.setMessage("入参为空，请check!");
		}

		return result;
	}

	/**
	 * 单个用户操作，支持的操作：开户签约， 去签约，销户
	 * 
	 * @param command
	 *            10-开户签约；20-去签约；30-销户
	 * @param phoneNo
	 *            电话号码
	 * @param policyId
	 *            策略ID
	 * @return
	 */
	public ResultModel singlePhoneOpt(short command, String phoneNo,
			String policyId, String policyType) throws Exception {
		// String policyType = "1";
		ResultModel result = new ResultModel();
		SprBusinessBean sprBean = new SprBusinessBean();
		sprBean = getSprBean(phoneNo);
		log.info("begin to singlePhoneOpt----------->");
		// 10-开户签约
		if (command == 10) {
			// 先开户
			log.info("command is -----------> " + command
					+ " and phoneNo is -----> " + phoneNo);
			log.info("begin to addSubscriber----------->");
			String res1 = addSubscriber(phoneNo, sprBean);
			log.info("end to addSubscriber----------->");

			if ("0".equals(res1)) {
				// 再签约(业务策略)
				String res2 = "-1";
				if ("1".equals(policyType)) {
					log.info("begin to subscribeService----------->");

					res2 = subscribeService(phoneNo, policyId, sprBean);

					log.info("end to subscribeService----------->");
				}

				// 再签约(用户策略)
				if ("2".equals(policyType)) {
					log.info("begin to subscribeUsrSessionPolicy----------->");

					res2 = subscribeUsrSessionPolicy(phoneNo, policyId, sprBean);

					log.info("end to subscribeUsrSessionPolicy----------->");
				}

				if ("0".equals(res2)) {
					result.setResultCode("0");
					result.setMessage("用户开户签约成功");
				} else {
					result.setResultCode("1");
					result.setMessage("用户开户签约失败");
				}
			}
		}
		// 20-去签约
		if (command == 20) {
			log.info("command is -----------> " + command
					+ " and policyId is -------> " + policyId
					+ " and phoneNo is --------> " + phoneNo);
			String res = "-1";
			if ("1".equals(policyType)) {
				log.info("begin to unsubscribeService----------->");
				res = unsubscribeService(phoneNo, policyId, sprBean);
				log.info("end to unsubscribeService----------->");
			}

			if ("2".equals(policyType)) {
				log.info("begin to unsubscribeUsrSessionPolicy----------->");
				res = unsubscribeUsrSessionPolicy(phoneNo, policyId, sprBean);
				log.info("end to unsubscribeUsrSessionPolicy----------->");
			}

			if ("0".equals(res)) {
				result.setResultCode("0");
				result.setMessage("用户去签约成功");
			} else {
				result.setResultCode("1");
				result.setMessage("用户去签约失败");
			}
		}
		// 30-销户
		if (command == 30) {
			log.info("command is -----------> " + command
					+ " and phoneNo is --------> " + phoneNo);
			log.info("begin to delSubscriber----------->");
			String res = delSubscriber(phoneNo, sprBean);
			log.info("end to delSubscriber----------->");

			if ("0".equals(res)) {
				result.setResultCode("0");
				result.setMessage("用户销户成功");
			} else {
				result.setResultCode("1");
				result.setMessage("用户销户失败");
			}
		}
		return result;
	}

	/**
	 * 批量用户操作，支持的操作：开户签约， 去签约，销户
	 * 
	 * @param command
	 *            10-开户签约；20-去签约；30-销户
	 * @param custListTabName
	 *            客户清单表名
	 * @return
	 */
	public ResultModel batchPhonesOpt(short command, String custListTabName,
			String policyId, String policyType) throws Exception {
		// String policyType="1";
		ResultModel result = new ResultModel();
		// 获取需要处理的手机号码
		List<SprBusinessBean> sprInfos = getProcessInfos(custListTabName);

		// //测试每10W条分文件传输
		// List<SprBusinessBean> sprInfos = new ArrayList<SprBusinessBean>();
		// SprBusinessBean sBean = new SprBusinessBean();
		// sBean.setFtpAddress("/cluster/home/dveadm/CARepository/DaemonBatch/base");
		// sBean.setFtpPwd("Lljxk!@34");
		// sBean.setFtpUsr("boss");
		// sBean.setIsSftp("1");
		// sBean.setSprIp("10.30.241.168");
		// sBean.setSprName("WHSPR1BER");
		// sBean.setSprPwd("Lljxk!@34");
		// sBean.setSprUsr("boss");
		// sBean.setSprWsdl("http://10.30.242.253:8080/sapcPG/sapcPG");
		// List<String> phonesTest = new ArrayList<String>();
		// for(int i=1;i<10;i++){
		// phonesTest.add("1370711"+i);
		// }
		// sBean.setPhoneList(phonesTest);
		// sprInfos.add(sBean);
		int suc_num = 0;
		// 开户签约
		if (command == 10) {
			if (sprInfos != null && sprInfos.size() > 0) {
				for (int i = 0; i < sprInfos.size(); i++) {
					SprBusinessBean sprBean = sprInfos.get(i);
					List<String> phones = sprBean.getPhoneList();

					// 开户 生成+上传文件
					log.info("command is -----------> " + command
							+ " and custListTabName is -----> "
							+ custListTabName + " and policyId is ------> "
							+ policyId);

					log.info("begin to sendBatchInfo -----------> the intf is [addBatSubscriber]");
					List<Map<String, String>> res1 = sendBatchInfo(
							"addBatSubscriber", phones, policyId, sprBean);
					log.info("end to sendBatchInfo -----------> the intf is [addBatSubscriber]");

					List<Map<String, String>> res2 = new ArrayList<Map<String, String>>();
					// 业务策略
					if ("1".equals(policyType)) {
						// 业务批量策略
						// 签约生成+上传文件
						log.info("begin to sendBatchInfo -----------> the intf is [addBatService]");
						res2 = sendBatchInfo("addBatService", phones, policyId,
								sprBean);
						log.info("end to sendBatchInfo -----------> the intf is [addBatService]");
					}

					// 用户批量策略
					if ("2".equals(policyType)) {
						log.info("begin to sendBatchInfo -----------> the intf is [addBatUsrSessionPolicy]");
						res2 = sendBatchInfo("addBatUsrSessionPolicy", phones,
								policyId, sprBean);
						log.info("end to sendBatchInfo -----------> the intf is [addBatUsrSessionPolicy]");
					}

					int res1Count = 0;
					List<Map<String, String>> returnList = new ArrayList<Map<String, String>>();
					// 开户调用接口
					log.info("begin to addBatSubscriber----------->");
					if (res1 != null && res1.size() > 0) {
						for (int j = 0; j < res1.size(); j++) {
							Map<String, String> reMap1 = res1.get(j);
							Map<String, String> returnMap = addBatSubscriber(
									reMap1.get("fileName"),
									reMap1.get("oprTime"), sprBean);
							String reString = returnMap.get("result");
							if ("0".equals(reString)) {
								res1Count++;
								returnList.add(returnMap);
								log.info("批量文件名：" + reMap1.get("fileName")
										+ "， 调用接口成功");
							} else {
								log.info("批量文件名：" + reMap1.get("fileName")
										+ "， 调用接口失败");
							}
							Thread.sleep(1000);
						}
					}
					log.info("end to addBatSubscriber----------->");

					// 生成一个批次ID
					String batchNo = getRandomNum();

					// 获取所有一个批次的签约文件名
					if (res2 != null && res2.size() > 0) {
						for (int j = 0; j < res2.size(); j++) {
							Map<String, String> reMap2 = res2.get(j);
							insertBatchInfo(batchNo, reMap2.get("fileName"),
									reMap2.get("oprTime"));
						}
					}

					// 插入task 表
					if (returnList != null && returnList.size() > 0) {
						for (int j = 0; j < returnList.size(); j++) {
							Map<String, String> returnMap = returnList.get(j);
							// 调用插入方法
							insertTaskInfo(returnMap, sprBean, policyType,
									batchNo);
						}
					}

					log.info("endendend");
					if (res1Count == res1.size()) {
						suc_num++;
					}

					// PccCheckSignThread pccCheckSignThread = new
					// PccCheckSignThread(returnList, res2, sprBean,policyType);
					// pccCheckSignThread.start();

					// //业务批量策略
					// // 签约生成调用接口
					// log.info("begin to addBatService----------->");
					// if (res2 != null && res2.size() > 0) {
					// for (int j = 0; j < res2.size(); j++) {
					// Map<String, String> reMap2 = res2.get(j);
					// String reString = addBatService(reMap2.get("fileName"),
					// reMap2.get("oprTime"),sprBean);
					// if ("0".equals(reString)) {
					// res2Count++;
					// log
					// .info("批量文件名：" + reMap2.get("fileName")
					// + "， 调用接口成功");
					// } else {
					// log
					// .info("批量文件名：" + reMap2.get("fileName")
					// + "， 调用接口失败");
					// }
					// }
					// }
					// log.info("end to addBatService----------->");

					// 用户批量策略
					// log.info("begin to addBatUsrSessionPolicy----------->");
					// if (res2 != null && res2.size() > 0) {
					// for (int j = 0; j < res2.size(); j++) {
					// Map<String, String> reMap2 = res2.get(j);
					// String reString =
					// addBatUsrSessionPolicy(reMap2.get("fileName"),
					// reMap2.get("oprTime"),sprBean);
					// if ("0".equals(reString)) {
					// res2Count++;
					// log
					// .info("批量文件名：" + reMap2.get("fileName")
					// + "， 调用接口成功");
					// } else {
					// log
					// .info("批量文件名：" + reMap2.get("fileName")
					// + "， 调用接口失败");
					// }
					// }
					// }
					// log.info("end to addBatUsrSessionPolicy----------->");

				}
				log.info("suc_num: " + suc_num);
				// 全部成功返回结果
				if (suc_num == sprInfos.size()) {
					result.setResultCode("0");
					result.setMessage("批量开户签约成功");
				} else {
					result.setResultCode("1");
					result.setMessage("批量开户签约失败");
				}
			}
		}
		// 去签约
		if (command == 20) {
			if (sprInfos != null && sprInfos.size() > 0) {
				for (int i = 0; i < sprInfos.size(); i++) {
					SprBusinessBean sprBean = sprInfos.get(i);
					List<String> phones = sprBean.getPhoneList();
					// 去签约 生成+上传文件
					log.info("command is -----------> " + command
							+ " and custListTabName is -----> "
							+ custListTabName + " and policyId is ------> "
							+ policyId);

					List<Map<String, String>> res = new ArrayList<Map<String, String>>();
					// 业务策略
					if ("1".equals(policyType)) {
						log.info("begin to sendBatchInfo -----------> the intf is [delBatService]");
						res = sendBatchInfo("delBatService", phones, policyId,
								sprBean);
						log.info("end to sendBatchInfo -----------> the intf is [delBatService]");
					}

					// 用户策略
					if ("2".equals(policyType)) {
						log.info("begin to sendBatchInfo -----------> the intf is [delBatUsrSessionPolicy]");
						res = sendBatchInfo("delBatUsrSessionPolicy", phones,
								policyId, sprBean);
						log.info("end to sendBatchInfo -----------> the intf is [delBatUsrSessionPolicy]");
					}

					int resCount = 0;

					// 业务策略
					if ("1".equals(policyType)) {
						log.info("begin to delBatService----------->");
						if (res != null && res.size() > 0) {
							for (int j = 0; j < res.size(); j++) {
								Map<String, String> reMap = res.get(j);
								String reString = delBatService(
										reMap.get("fileName"),
										reMap.get("oprTime"), sprBean);
								if ("0".equals(reString)) {
									resCount++;
									log.info("批量文件名：" + reMap.get("fileName")
											+ "， 调用接口成功");
								}
							}
						}
						log.info("end to delBatService----------->");
					}

					// 用户策略
					if ("2".equals(policyType)) {
						log.info("begin to delBatUsrSessionPolicy----------->");
						if (res != null && res.size() > 0) {
							for (int j = 0; j < res.size(); j++) {
								Map<String, String> reMap = res.get(j);
								String reString = delBatUsrSessionPolicy(
										reMap.get("fileName"),
										reMap.get("oprTime"), sprBean);
								if ("0".equals(reString)) {
									resCount++;
									log.info("批量文件名：" + reMap.get("fileName")
											+ "， 调用接口成功");
								}
							}
						}
						log.info("end to delBatUsrSessionPolicy----------->");
					}

					if (resCount == res.size()) {
						suc_num++;
					}
				}
				// 全部成功反馈结果
				if (suc_num == sprInfos.size()) {
					result.setResultCode("0");
					result.setMessage("批量去签约成功");
				} else {
					result.setResultCode("1");
					result.setMessage("批量去签约失败");
				}
			}

		}
		// 销户
		if (command == 30) {
			if (sprInfos != null && sprInfos.size() > 0) {
				for (int i = 0; i < sprInfos.size(); i++) {
					SprBusinessBean sprBean = sprInfos.get(i);
					List<String> phones = sprBean.getPhoneList();

					// 销户 生成+上传文件
					log.info("command is -----------> " + command
							+ " and custListTabName is -----> "
							+ custListTabName + " and policyId is ------> "
							+ policyId);

					log.info("begin to sendBatchInfo -----------> the intf is [delBatSubscriber]");
					List<Map<String, String>> res = sendBatchInfo(
							"delBatSubscriber", phones, policyId, sprBean);
					log.info("end to sendBatchInfo -----------> the intf is [delBatSubscriber]");

					int resCount = 0;
					log.info("begin to delBatSubscriber----------->");

					if (res != null && res.size() > 0) {
						for (int j = 0; j < res.size(); j++) {
							Map<String, String> reMap = res.get(j);
							String reString = delBatSubscriber(
									reMap.get("fileName"),
									reMap.get("oprTime"), sprBean);
							if ("0".equals(reString)) {
								resCount++;
								log.info("批量文件名：" + reMap.get("fileName")
										+ "， 调用接口成功");
							}
						}
					}
					log.info("end to delBatSubscriber----------->");

					if (resCount == res.size()) {
						suc_num++;
					}
				}
				// 全部成功反馈结果
				if (suc_num == sprInfos.size()) {
					result.setResultCode("0");
					result.setMessage("批量去签约成功");
				} else {
					result.setResultCode("1");
					result.setMessage("批量去签约失败");
				}
			}
		}
		return result;
	}
/**
 * cpe开户接口
 * @param phoneNo
 * @param policId
 * @param location
 * @return 0=失败;1=成功
 * @throws SQLException 
 */
	public String cpeUserRegister(String phoneNo,String policId,List<String> location) throws SQLException{
		String result = "0";
		boolean flag1 = false;
		boolean flag2 = false;
		List<SprBusinessBean> list = getSprBeans(phoneNo);
		log.info("begin to cpeUserRegister ----------->");
		for (int i = 0; i < list.size(); i++) {
			SprBusinessBean sprBean = list.get(i);
			
			//开始开户
			log.info(
					" phoneNo is -----> " + phoneNo
					+ " and policId is ----->"+policId
					+ " and location is ---->"+location);
			log.info("begin to addCpeSubscriber----------->");
			String res1 = addSubscriber(phoneNo, sprBean);
			
			if(res1.endsWith("0")){
				log.info("begin to unsubscribeUsrSessionPolicy----------->");
				String res0 = unsubscribeUsrSessionPolicy(phoneNo, policId, sprBean);//先去签约
				if(res0.equals("0")){
					// 再签约(业务策略)
					String res2 = "-1";
					log.info("begin to subscribeUsrSessionPolicy----------->");
					res2 = subscribeUsrSessionPolicy(phoneNo, policId, sprBean);
					if(res2.equals("0")){
						log.info("begin to addUsrLocation----------->");
						String res3 = addUsrLocation(phoneNo, policId, location, sprBean);
						if(res3.equals("0")){
							result = "1";
						}
						
					}
				}
			}
			if(i==0&&result.equals("1")){
				flag1=true;
			}
			if(i==1&&result.equals("1")){
				flag2=true;
			}
			
		}
		log.info("end to subscribeService----------->");
		if(flag1||flag2){
			result="1";
		}
		return result;
		
	}
	
	/**
	 * 锁网，当小区的个数小于九个时开始锁网
	 * @param phoneNo
	 * @param policId
	 * @param locations
	 * @return 1=成功 0=失败
	 * @throws SQLException 
	 */
	public String cpeUserNetLock(String phoneNo,String policId,List<String> locations) throws SQLException{
		String result = "0";

		boolean flag1 = false;
		boolean flag2 = false;
		List<SprBusinessBean> list = getSprBeans(phoneNo);
		for (int i = 0; i < list.size(); i++) {
			SprBusinessBean sprBean = list.get(i);
			log.info("phoneNo is "+phoneNo
					+" ,policId is "+policId
					+" ,usrlocation is "+locations);
			log.info("begin to cpeUserNetLock----------->");
			String res0 = "-1";
			res0 = unsubscribeUsrSessionPolicy(phoneNo, policId, sprBean);//先去签约
			if(res0.equals("0")){//去签约成功
				String res1 = "-1";
				res1 = subscribeUsrSessionPolicy(phoneNo, policId, sprBean);//再签约
				if(res1.equals("0")){//再签约成功
					String res2 = "-1";
					res2 = addUsrLocation(phoneNo, policId, locations, sprBean);//签约位置
					if(res2.equals("0")){
						result = "1";
					}
				}
			}
			log.info("end to cpeUserNetLock----------->");
			if(i==0&&result.equals("1")){
				flag1=true;
			}
			if(i==1&&result.equals("1")){
				flag2=true;
			}
		}
		if(flag1||flag2){
			result="1";
		}
		
		return result;
	}
	/**
	 * 参数重置
	 * @param phoneNo
	 * @param policId
	 * @param loctions
	 * @return 1=成功 0=失败
	 * @throws SQLException 
	 */
	public String cpeUserReset(String phoneNo ,String policId,List<String> loctions) throws SQLException{
		String result = "0";
		boolean flag1 = false;
		boolean flag2 = false;
		List<SprBusinessBean> list = getSprBeans(phoneNo);
		log.info("begin to cpeUserReset----------->");
		for (int i = 0; i < list.size(); i++) {
			SprBusinessBean sprBean = list.get(i);
			
			log.info(" phoneNo is "+phoneNo 
					+",policId is "+policId
					+",loctions is "+loctions);
			log.info("begin to deleUserLocation--------->");
			String res1 = deleUserLocation(phoneNo, policId, sprBean);
			log.info("end to deleUserLocation----------->");
			if("0".equals(res1)){
				log.info("begin to addUsrLocation--------->");
				String res2 = addUsrLocation(phoneNo, policId, loctions, sprBean);
				log.info("end to addUsrLocation--------->");
				if("0".equals(res2)){
					result="1";
				}
			}
			log.info("end to cpeUserReset----------->");
			if(i==0&&result.equals("1")){
				flag1=true;
			}
			if(i==1&&result.equals("1")){
				flag2=true;
			}
		}
		if(flag1||flag2){
			result="1";
		}
		return result;
	}
	
	/**
	 * 参数重置（去签约）
	 * @param phoneNo
	 * @param policId
	 * @return 1=成功，2=失败
	 * @throws SQLException
	 */
	public String cpeUnUserPolicy(String phoneNo,String policId) throws SQLException{
		String result = "0";
		boolean flag1 = false;
		boolean flag2 = false;
		List<SprBusinessBean> list = getSprBeans(phoneNo);
		for (int i = 0; i < list.size(); i++) {
			SprBusinessBean sprBean = list.get(i);
			log.info("begin to cpeUnUserPolicy(去签约)----------->");
			log.info(" phoneNo is "+phoneNo 
					+",policId is "+policId
					);
			String res = unsubscribeUsrSessionPolicy(phoneNo, policId, sprBean);
			log.info("unsubscribeUsrSessionPolicy result is ----------->"+res);
			if(res.equals("0")){
				result = "1";
			}
			
			if(i==0&&result.equals("1")){
				flag1=true;
			}
			if(i==1&&result.equals("1")){
				flag2=true;
			}
		}
		if(flag1||flag2){
			result="1";
		}
		return result;
		
	}
	/**
	 * 
	 * @param list （phoneNo=手机号  policId=策略号  loctions=基站小区号 ）
	 * @return Map key=1 成功的手机号码 ,key=0失败的手机号码集合
	 * @throws SQLException 
	 */
	public Map<String,Object> cpeBsBatchcg(List<Map<String,Object>> list) throws SQLException{
		Map<String,Object> result = new HashMap<String,Object>();
		List<String> fail = new ArrayList<String>();
		List<String> success = new ArrayList<String>();
		for (int i = 0; i < list.size(); i++) {
			Map<String, Object> map = list.get(i);
			String phoneNo = map.get("phoneNo").toString();
			log.info(phoneNo+"-begin cpeUserReset-------->>");
			String policId = map.get("policId").toString();
			List<String> loctions = (List<String>) map.get("loctions");
			String res = cpeUserReset(phoneNo, policId, loctions);
			if(res.equals("1")){
				success.add(phoneNo);
			}else{
				fail.add(phoneNo);
			}
			log.info(phoneNo+"-end cpeUserReset<<---------");
		}
		result.put("0", fail);
		result.put("1", success);
	
		return result;
		
	}
	/**
	 * 带宽变更
	 * CPE设备编号
	 * @param phoneNo
	 * 手机号
	 * @param policId
	 * 策略编号
	 * @param oldpolicId
	 * 旧的策略编号
	 * @param usrlocations
	 * 小区位置编号
	 * @return 1=成功  0=失败
	 * @throws SQLException 
	 */ 
	public String cpeUserNetTypeChange(String phoneNo,String policId,String oldpolicId,List<String> usrlocations) throws SQLException{
		String result ="0";
		boolean flag1 = false;
		boolean flag2 = false;
		List<SprBusinessBean> list = getSprBeans(phoneNo);
		for (int i = 0; i < list.size(); i++) {
			SprBusinessBean sprBean = list.get(i);
			log.info("begin to cpeUserNetTypeChange----------->");
			
			//开始带宽变更
			log.info(
					 " and phoneNo is -----> " + phoneNo
					+ " and policId is ----->"+policId
					+ " and oldpolicId is ----->"+oldpolicId
					+ "and usrlocations ----->"+usrlocations);
			log.info("begin to updateUsrSessionPolicy----------->");
			//先去签约旧的用户策略
			log.info("begin to unsubscribeUsrSessionPolicy------>");
			
				
			
				String res1 = unsubscribeUsrSessionPolicy(phoneNo, oldpolicId, sprBean);
				
				if(res1.equals("0")){
					String res2 = subscribeUsrSessionPolicy(phoneNo, policId, sprBean);
					if(res2.equals("0")){
						String res3 = addUsrLocation(phoneNo, policId, usrlocations, sprBean);
						if(res3.equals("0")){
							result="1";
						}
					}
				log.info("end to updateUsrSessionPolicy----------->");
			
				}
			log.info("end to cpeUserNetTypeChange----------->");
			if(result.equals("0")){
				subscribeUsrSessionPolicy(phoneNo, oldpolicId, sprBean);
				addUsrLocation(phoneNo, oldpolicId, usrlocations, sprBean);
			}
			if(i==0&&result.equals("1")){
				flag1=true;
			}
			if(i==1&&result.equals("1")){
				flag2=true;
			}
		}
		if(flag1||flag2){
			result="1";
		}
		
		return result;
			
	}
	
	/**
	 * cpe用户状态变更（销户）
	 * @param phoneNo
	 * 电话号码
	 * @return
	 * @throws SQLException 
	 * @throws RemoteException 
	 */
	
	public String cpeUserStatusChange(String phoneNo) throws SQLException, RemoteException{
		String result ="0";
		boolean flag1 = false;
		boolean flag2 = false;
		List<SprBusinessBean> list = getSprBeans(phoneNo);
		for (int i = 0; i < list.size(); i++) {
			SprBusinessBean sprBean = list.get(i);
			log.info("begin to cpeUserStatusChange----------->");
			//开始销户
			log.info( " and phoneNo is -----> " + phoneNo);
			log.info("begin to delSubscriber----------->");
			String res1 = delSubscriber(phoneNo, sprBean);
			if("0".equals(res1)){
				result="1";
			}
			log.info("end to delSubscriber----------->");
			if(i==0&&result.equals("1")){
				flag1=true;
			}
			if(i==1&&result.equals("1")){
				flag2=true;
			}
		}
		if(flag1||flag2){
			result="1";
		}
		
		return result;
	}
	
	/**
	 * 通过需要调用的接口 生成文件FTP到SPR
	 * 
	 * @param instruction
	 *            需要调用接口名
	 * @return
	 */
	public List<Map<String, String>> sendBatchInfo(String instruction,
			List<String> phones, String policyId, SprBusinessBean sprBean) {
		List<Map<String, String>> result = new ArrayList<Map<String, String>>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		String fileAppend = "";
		Integer fileCount = 0;
		String nullIndex;
		Map<String, String> map = new HashMap<String, String>();
		String realFileName;
		if (instruction.contains("BatSubscriber")) {
			fileAppend = "Subscriber";
			nullIndex = PropUtil.getProp("subscriber_nullIndex",
					"sprbatch.properties");
		} else if (instruction.contains("BatService")) {
			fileAppend = "Service";
			nullIndex = PropUtil.getProp("service_nullIndex",
					"sprbatch.properties");
		} else if (instruction.contains("BatUsrSessionPolicy")) {
			fileAppend = "UsrSession";
			nullIndex = PropUtil.getProp("usrSession_nullIndex",
					"sprbatch.properties");
		} else {
			return result;
		}
		String instruct = "";
		if (instruction.startsWith("ad") || instruction.startsWith("sub")) {
			instruct = "A";
		} else if (instruction.startsWith("up")) {
			instruct = "U";
		} else if (instruction.startsWith("un")
				|| instruction.startsWith("del")) {
			instruct = "D";
		}
		final StringBuilder sb = new StringBuilder();
		// 本地生成文件路径
		String filePath = PropUtil.getProp("ftpLocalPath", "ftp.properties");

		String maxPhoneNum = PropUtil.getProp("maxPhoneNum", "ftp.properties");
		int mphoneNum = 100000;
		if (!StringUtil.isEmpty(maxPhoneNum)) {
			mphoneNum = Integer.valueOf(maxPhoneNum);
		}
		if (!filePath.endsWith(File.separator)) {
			filePath = filePath + File.separator;
		}
		// 生成文件名
		Map<String, String> resultMap = new HashMap<String, String>();
		resultMap = getFileName(fileAppend);
		realFileName = resultMap.get("fileName");
		// String oprTime = resultMap.get("oprTime");
		result.add(resultMap);
		final String realDataFilePath = filePath + realFileName;
		// 添加文件头
		// sb.append("90|").append(productFileTime.substring(0, 12)).append(
		// "|" + realFileName + "|" + "|").append(lastIndex);
		int count = 0;
		int bufferdsize = 2048;
		Writer osw = null;
		FileOutputStream fos = null;
		OutputStreamWriter sw = null;
		try {
			fos = new FileOutputStream(realDataFilePath, false);
			sw = new OutputStreamWriter(fos, "UTF-8");
			osw = new BufferedWriter(sw, bufferdsize);
			map.put(String.valueOf(fileCount), realDataFilePath);
			fileCount++;
			if (phones != null && phones.size() > 0) {
				for (int i = 0; i < phones.size(); i++) {
					String phone = phones.get(i);
					if (!StringUtil.isEmpty(prefix_phone)) {
						phone = prefix_phone + phone;
					}
					// 10w行一个文本文件需要重新添加文件头 (华为问题暂用90000)
					if (count % mphoneNum == 0) {
						sb.append("90|")
								.append(productFileTime.substring(0, 14))
								.append("|" + realFileName + "|" + "|")
								.append(lastIndex);
					}
					sb.append(instruct).append("|");
					sb.append(phone);
					if (!realFileName.startsWith("BatFile_Subscriber")) {
						sb.append("|").append(policyId);
					}
					if (realFileName.startsWith("BatFile_Subscriber")) {
						sb.append("|").append(phone);
					}
					sb.append(nullIndex);
					String lastSendContent = "";
					sb.append(lastSendContent);
					osw.write(sb.toString());
					osw.write(lastIndex);
					count++;
					if (count % 10000 == 0) { // 10000条写一次数据
						// log.info("10000条flush一次文件!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!taskId="
						// +
						// task.getTaskId()+",campsegId="+campSeginfo.getCampsegId());
						osw.flush();
						// 每10w调数据添加文件为并且重新生成文件 (华为问题暂用90000)
						if (count % mphoneNum == 0) {
							sb.delete(0, sb.length());
							sb.append("99|").append(sdf.format(new Date()))
									.append("|").append(count).append("||");
							osw.write(sb.toString());
							osw.flush();
							Map<String, String> reMap = new HashMap<String, String>();
							// Thread.sleep(1000);
							reMap = getFileName(fileAppend);
							realFileName = reMap.get("fileName");
							// String opTime = reMap.get("oprTime");
							result.add(reMap);
							fos = new FileOutputStream(filePath + realFileName,
									false);
							sw = new OutputStreamWriter(fos, "UTF-8");
							osw = new BufferedWriter(sw, bufferdsize);
							map.put(String.valueOf(fileCount), filePath
									+ realFileName);
							fileCount++;
							count = 0;
						}
					}
					sb.delete(0, sb.length());
				}
				sb.append("99|").append(sdf.format(new Date())).append("|")
						.append(count).append("||");
				osw.write(sb.toString());
				// 刷新文件输出缓存
				osw.flush();
				sb.delete(0, sb.length());
				if (osw != null) {
					osw.close();
				}
				if (sw != null) {
					sw.close();
				}
				if (fos != null) {
					fos.close();
				}

				// FTP上传文件
				String fileName = "";

				for (int j = 0; j < fileCount; j++) {
					fileName = map.get(j + "");
					log.info("文件名===========" + fileName);
					File theFile = new File(fileName);
					// 0:ftp 1:sftp
					if ("0".equals(sprBean.getIsSftp())) {
						this.uploadFileByFtp(theFile, fileName, sprBean);
					} else {
						SftpUtil.upload(theFile, sprBean);
					}
					log.info("Send to ftp");
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 上传文件
	 * 
	 * @param theFile
	 * @param fileName
	 * @param sendoddFactoryId
	 * @throws Exception
	 */
	private void uploadFileByFtp(File theFile, String fileName,
			SprBusinessBean sprBean) throws Exception {
		// FtpConfig cfcb =
		// FtpConfigure.getInstance().getFtpConfigByTypes(sendoddFactoryId);
		// String ftpServer = cfcb.getFTP_ADDRESS();
		// int ftpServerPort = Integer.parseInt(cfcb.getFTP_PORT());
		// String ftpStorePath = cfcb.getFTP_PATH();
		// String ftpUserName = cfcb.getFTP_USER();
		// String ftpUserPwd = cfcb.getFTP_PASSWORD();
		// String ftpUserPwdEncrypt = cfcb.getFTP_PASSWORD_ENCRYPT();
		// String encode = cfcb.getFTP_ENCODE();
		//
		// if (ftpUserPwdEncrypt != null && ftpUserPwdEncrypt.equals("1")) {
		// ftpUserPwd = DES.decrypt(ftpUserPwd);
		// }
		String ftpServer = sprBean.getSprIp();
		int ftpServerPort = Integer.valueOf(PropUtil.getProp("ftpServerPort",
				"ftp.properties"));
		String ftpStorePath = sprBean.getFtpAddress();
		String ftpUserName = sprBean.getFtpUsr();
		String ftpUserPwd = sprBean.getFtpPwd();
		String encode = PropUtil.getProp("encode", "ftp.properties");
		String deleteLocal = PropUtil.getProp("deleteLocal", "ftp.properties");
		ApacheFtpUtil apacheFtp = null;
		FileInputStream fis = new FileInputStream(theFile);
		try {
			log.info("ftpServer: " + ftpServer + " ftpServerPort: "
					+ ftpServerPort + " ftpUserName: " + ftpUserName
					+ " ftpUserPwd: " + ftpUserPwd + " encode: " + encode);
			apacheFtp = ApacheFtpUtil.getInstance(ftpServer, ftpServerPort,
					ftpUserName, ftpUserPwd, encode);
			log.info("++++++++++++++++");
			apacheFtp.changeAndMakeDir(ftpStorePath);
			apacheFtp.upload(fis, theFile.getName());
		} catch (Exception e) {
			log.info("uploadFileByFtp(" + theFile.getAbsolutePath()
					+ ") error:");
			e.printStackTrace();
		} finally {
			try {
				apacheFtp.forceCloseConnection();
				fis.close();
			} catch (Exception e) {
				log.info("退出FTP失败！");
				e.printStackTrace();
			}
			// 是否需要删除本地文件
			if (!StringUtil.isEmpty(deleteLocal) && "1".equals(deleteLocal)) {
				// apacheFtp.delete(theFile.getAbsolutePath());
				// 垃圾回收
				System.gc();
				if (!theFile.delete()) {
					log.info("请关闭使用该文件的所有进程或者流！！");
				} else {
					log.info("文件删除成功！");
				}
			}
		}

	}

	/**
	 * 获取文件名
	 * 
	 * @param instruction
	 *            调用接口名
	 * @return
	 */
	public Map<String, String> getFileName(String instruction) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		String currentTime = sdf.format(new Date());
		Map<String, String> result = new HashMap<String, String>();
		if ("".equals(productFileTime)) {

			productFileTime = currentTime;
			productFileTime = productFileTime + "_01";

		} else {
			if (productFileTime.contains(currentTime)) {
				// if
				// (Integer.parseInt(productFileTime.substring((productFileTime.length()-1),productFileTime.length()))
				// + 1 > 9) {
				// productFileTime = productFileTime +
				// (Integer.parseInt(productFileTime.substring((productFileTime.length()-1),productFileTime.length()))
				// + 1);
				// } else {
				// productFileTime = productFileTime +
				// (Integer.parseInt(productFileTime.substring((productFileTime.length()-1),productFileTime.length()))
				// + 1);
				// }

				String[] strings = productFileTime.split("_");
				// 最后一位SN标记位
				String sn = strings[strings.length - 1];
				// 最后一位数字+1
				String sn1 = String.valueOf((Integer.parseInt(productFileTime
						.substring((productFileTime.length() - 1),
								productFileTime.length())) + 1));
				if (sn.startsWith("0") && Integer.valueOf(sn1) != 10) {
					for (int i = 0; i < strings.length; i++) {
						if (i == 0) {
							productFileTime = strings[0] + "_";
						} else if (i == strings.length - 1) {
							productFileTime = productFileTime + "0" + sn1;
						} else {
							productFileTime = productFileTime + "_"
									+ strings[i];
						}
					}
				} else {
					for (int i = 0; i < strings.length; i++) {
						if (i == 0) {
							productFileTime = strings[0] + "_";
						} else if (i == strings.length - 1) {
							productFileTime = productFileTime
									+ (Integer.valueOf(sn) + 1);
						} else {
							productFileTime = productFileTime + "_"
									+ strings[i];
						}
					}
				}
			} else {
				productFileTime = currentTime;
				productFileTime = productFileTime + "_01";
			}
		}
		result.put("fileName", "BatFile_" + instruction + "_" + productFileTime
				+ ".txt"); // 文件名
		String oprTime = "";
		oprTime = productFileTime;
		result.put("oprTime", oprTime.substring(0, 14)); // 生成文件时间
		return result;
	}

	/**
	 * 用户开户,并设置用户状态,和结算周期
	 * @author lifangyuan
	 * @time 2017年5月2日14:58:19
	 * @param phoneNo 手机号   
	 * @param status 状态
	 * @param usrBillCycleDate 结算日期         
	 * @return
	 */
	public String addSubscriber(String phoneNo,String status,String usrBillCycleDate,SprBusinessBean sprBean) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		status = "1";
		String result = "-1";
		try {
			PCRFSoapStub stub = new PCRFSoapStub(sprBean.getSprWsdl());//soap
			stub._getServiceClient().getOptions()
					.setProperty(HTTPConstants.CHUNKED, false);
			PCRFSoapStub.AddSubscriber addSubscriber47 = new PCRFSoapStub.AddSubscriber();
			PCRFSoapStub.SInSubscriberParaVO siInSubscriberParaVO = new PCRFSoapStub.SInSubscriberParaVO();
			PCRFSoapStub.SPccSubscriber sPccSubscriber = new PCRFSoapStub.SPccSubscriber();
			PCRFSoapStub.SAVP savp = new PCRFSoapStub.SAVP();
			PCRFSoapStub.SAVP savp2 = new PCRFSoapStub.SAVP();
			PCRFSoapStub.SAVP savp3 = new PCRFSoapStub.SAVP();
			PCRFSoapStub.SAVP savp4 = new PCRFSoapStub.SAVP();//2017年4月28日17:56:29
			PCRFSoapStub.SAVP savp5 = new PCRFSoapStub.SAVP();
			savp.setKey("usrIdentifier");
			if (!StringUtil.isEmpty(prefix_phone)) {
				phoneNo = prefix_phone + phoneNo;
			}
			savp.setValue(phoneNo);
			
			savp2.setKey("operateTime");
			savp2.setValue(sdf.format(new Date()));
			savp3.setKey("usrNotifyMSISDN");
			savp3.setValue(phoneNo);
			savp4.setKey("usrStatus");
			savp4.setValue(status);
			savp5.setKey("usrBillCycleDate");
			savp5.setValue(usrBillCycleDate);
			
			sPccSubscriber.addAttribute(savp);
			sPccSubscriber.addAttribute(savp2);
			sPccSubscriber.addAttribute(savp3);
			sPccSubscriber.addAttribute(savp4);
			if(Integer.parseInt(usrBillCycleDate)>=0){
				sPccSubscriber.addAttribute(savp5);
			}
			
			PCRFSoapStub.Header header48 = new PCRFSoapStub.Header();
			header48.setUsername(sprBean.getSprUsr());
			header48.setPassword(sprBean.getSprPwd());
			header48.setAddress(sprBean.getSprIp());
			header48.setTime(sdf.format(new Date()));
			header48.setSerial("" + ((Math.random() * 9 + 1) * 10000000));// 8位随机数
			siInSubscriberParaVO.setSubscriber(sPccSubscriber);
			addSubscriber47.setInPara(siInSubscriberParaVO);
			PCRFSoapStub.AddSubscriberResponse re = stub.addSubscriber(
					addSubscriber47, header48);
			PCRFSoapStub.SReturnVO sReturnVO = re.getResult();
			log.info("result code: " + sReturnVO.getResultCode());
			// 0 成功 110001 用户已存在
			if (sReturnVO.getResultCode() == 0
						|| sReturnVO.getResultCode() == 110001) {
				result = "0";
			}
		} catch (Exception e) {
			e.printStackTrace();
			return result;
		}
		return result;
	}
	
	/**
	 * 用户开户
	 * 
	 * @param phoneNo
	 *            手机号
	 * @return
	 */
	public String addSubscriber(String phoneNo, SprBusinessBean sprBean) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		String result = "-1";
		try {

			// URL url = new URL(sprBean.getSprWsdl());
			// // URL url = new
			// URL("file:/C:/Users/Administrator/Desktop/pcrf_new.wsdl");
			// // Header header = new Header();
			// // header.setUsername("WHBOSS");
			// // header.setPassword("2013*hxwHW");
			// PCRFSoap pcrf = new PCRFSoap(url,new QName(
			// "http://www.chinamobile.com/PCRF/", "PCRFSoap"));
			// ScfPccSoapServiceEndpoint scf = pcrf
			// .getChinaMobilePCRFSoapServiceEndpointPort();
			// SInSubscriberParaVO sInSubscriberParaVO = new
			// SInSubscriberParaVO();
			// SPccSubscriber sPccSubscriber = new SPccSubscriber();
			// // 手机号码
			// SAVP savp = new SAVP();
			// savp.setKey("usrIdentifier");
			// if(!StringUtil.isEmpty(prefix_phone)){
			// phoneNo = prefix_phone + phoneNo;
			// }
			// savp.setValue(phoneNo);
			//
			// //执行时间
			// SAVP savp2 = new SAVP();
			// savp2.setKey("operateTime");
			// savp2.setValue(sdf.format(new Date()));
			//
			//
			// sPccSubscriber.getAttribute().add(savp);
			// sPccSubscriber.getAttribute().add(savp2);
			// sInSubscriberParaVO.setSubscriber(sPccSubscriber);
			// SReturnVO sReturnVO = scf.addSubscriber(sInSubscriberParaVO);
			//
			// if ("0".equals(sReturnVO.getResultCode())) {
			// result = "0";
			// }

			PCRFSoapStub stub = new PCRFSoapStub(sprBean.getSprWsdl());
			stub._getServiceClient().getOptions()
					.setProperty(HTTPConstants.CHUNKED, false);
			PCRFSoapStub.AddSubscriber addSubscriber47 = new PCRFSoapStub.AddSubscriber();
			PCRFSoapStub.SInSubscriberParaVO siInSubscriberParaVO = new PCRFSoapStub.SInSubscriberParaVO();
			PCRFSoapStub.SPccSubscriber sPccSubscriber = new PCRFSoapStub.SPccSubscriber();
			PCRFSoapStub.SSubscribedService subscribedService = new PCRFSoapStub.SSubscribedService();
			PCRFSoapStub.SAVP savp = new PCRFSoapStub.SAVP();
			PCRFSoapStub.SAVP savp2 = new PCRFSoapStub.SAVP();
			PCRFSoapStub.SAVP savp3 = new PCRFSoapStub.SAVP();
			savp.setKey("usrIdentifier");
			if (!StringUtil.isEmpty(prefix_phone)) {
				phoneNo = prefix_phone + phoneNo;
			}
			savp.setValue(phoneNo);
			savp2.setKey("operateTime");
			savp2.setValue(sdf.format(new Date()));
			savp3.setKey("usrNotifyMSISDN");
			savp3.setValue(phoneNo);
			sPccSubscriber.addAttribute(savp);
			sPccSubscriber.addAttribute(savp2);
			sPccSubscriber.addAttribute(savp3);

			PCRFSoapStub.Header header48 = new PCRFSoapStub.Header();
			header48.setUsername(sprBean.getSprUsr());
			header48.setPassword(sprBean.getSprPwd());
			header48.setAddress(sprBean.getSprIp());
			header48.setTime(sdf.format(new Date()));
			header48.setSerial("" + ((Math.random() * 9 + 1) * 10000000));// 8位随机数
			siInSubscriberParaVO.setSubscriber(sPccSubscriber);
			addSubscriber47.setInPara(siInSubscriberParaVO);
			PCRFSoapStub.AddSubscriberResponse re = stub.addSubscriber(
					addSubscriber47, header48);
			PCRFSoapStub.SReturnVO sReturnVO = re.getResult();
			log.info("result code: " + sReturnVO.getResultCode());
			// 0 成功 110001 用户已存在
			if (sReturnVO.getResultCode() == 0
						|| sReturnVO.getResultCode() == 110001) {
				result = "0";
			}
		} catch (Exception e) {
			e.printStackTrace();
			return result;
		}
		return result;
	}

	/**
	 * 业务策略签约
	 * 
	 * @param phoneNo
	 *            手机号
	 * @param policyId
	 *            策略ID
	 * @return
	 */
	public String subscribeService(String phoneNo, String policyId,
			SprBusinessBean sprBean) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		String result = "-1";
		try {
			// URL url = new URL(sprBean.getSprWsdl());
			// PCRFSoap pcrf = new PCRFSoap(url,new QName(
			// "http://www.chinamobile.com/PCRF/", "PCRFSoap"));
			// ScfPccSoapServiceEndpoint scf = pcrf
			// .getChinaMobilePCRFSoapServiceEndpointPort();
			// SInSubscriptionParaVO sInSubscriptionParaVO = new
			// SInSubscriptionParaVO();
			// SPccSubscriber sPccSubscriber = new SPccSubscriber();
			// // 手机号码
			// SAVP savp = new SAVP();
			// savp.setKey("usrIdentifier");
			// if(!StringUtil.isEmpty(prefix_phone)){
			// phoneNo = prefix_phone + phoneNo;
			// }
			// savp.setValue(phoneNo);
			//
			// // 策略ID
			// SAVP savp2 = new SAVP();
			// savp2.setKey("ServiceCode");
			// savp2.setValue(policyId);
			//
			// //执行时间
			// SAVP savp3 = new SAVP();
			// savp3.setKey("operateTime");
			// savp3.setValue(sdf.format(new Date()));
			//
			// sPccSubscriber.getAttribute().add(savp);
			// sPccSubscriber.getAttribute().add(savp2);
			// sPccSubscriber.getAttribute().add(savp3);
			// sInSubscriptionParaVO.setSubscriber(sPccSubscriber);
			// SReturnVO sReturnVO =
			// scf.subscribeService(sInSubscriptionParaVO);
			//
			// if ("0".equals(sReturnVO.getResultCode())) {
			// result = "0";
			// }

			PCRFSoapStub stub = new PCRFSoapStub(sprBean.getSprWsdl());
			stub._getServiceClient().getOptions()
					.setProperty(HTTPConstants.CHUNKED, false);
			PCRFSoapStub.SubscribeService subscribeService = new PCRFSoapStub.SubscribeService();
			PCRFSoapStub.SInSubscriptionParaVO siInSubscriberParaVO = new PCRFSoapStub.SInSubscriptionParaVO();
			PCRFSoapStub.SPccSubscriber sPccSubscriber = new PCRFSoapStub.SPccSubscriber();
			PCRFSoapStub.SSubscribedService subscribedService = new PCRFSoapStub.SSubscribedService();
			PCRFSoapStub.SAVP savp = new PCRFSoapStub.SAVP();
			PCRFSoapStub.SAVP savp2 = new PCRFSoapStub.SAVP();
			PCRFSoapStub.SAVP savp3 = new PCRFSoapStub.SAVP();
			savp.setKey("usrIdentifier");
			if (!StringUtil.isEmpty(prefix_phone)) {
				phoneNo = prefix_phone + phoneNo;
			}
			savp.setValue(phoneNo);
			savp2.setKey("operateTime");
			savp2.setValue(sdf.format(new Date()));
			savp3.setKey("ServiceCode");
			savp3.setValue(policyId);
			
			sPccSubscriber.addAttribute(savp);
			subscribedService.addAttribute(savp2);
			subscribedService.addAttribute(savp3);
			
			PCRFSoapStub.Header header48 = new PCRFSoapStub.Header();
			header48.setUsername(sprBean.getSprUsr());
			header48.setPassword(sprBean.getSprPwd());
			header48.setAddress(sprBean.getSprIp());
			header48.setTime(sdf.format(new Date()));
			header48.setSerial("" + ((Math.random() * 9 + 1) * 10000000));// 8位随机数
			siInSubscriberParaVO.setSubscriber(sPccSubscriber);
			siInSubscriberParaVO.addSubscribedService(subscribedService);
			subscribeService.setInPara(siInSubscriberParaVO);
			try {
				PCRFSoapStub.SubscribeServiceResponse re = stub
						.subscribeService(subscribeService, header48);
				PCRFSoapStub.SReturnVO sReturnVO = re.getResult();
				log.info("result code: " + sReturnVO.getResultCode());
				if (sReturnVO.getResultCode() == 0) {
					result = "0";
				}
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 删除业务策略信息
	 * 
	 * @param phoneNo
	 *            手机号
	 * @param policyId
	 *            策略ID
	 * @return
	 */
	public String unsubscribeService(String phoneNo, String policyId,
			SprBusinessBean sprBean) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		String result = "-1";
		try {
			// URL url = new URL(sprBean.getSprWsdl());
			// PCRFSoap pcrf = new PCRFSoap(url,new QName(
			// "http://www.chinamobile.com/PCRF/", "PCRFSoap"));
			// ScfPccSoapServiceEndpoint scf = pcrf
			// .getChinaMobilePCRFSoapServiceEndpointPort();
			// SInSubscriptionParaVO sInSubscriptionParaVO = new
			// SInSubscriptionParaVO();
			// SPccSubscriber sPccSubscriber = new SPccSubscriber();
			// // 手机号码
			// SAVP savp = new SAVP();
			// savp.setKey("usrIdentifier");
			// if(!StringUtil.isEmpty(prefix_phone)){
			// phoneNo = prefix_phone + phoneNo;
			// }
			// savp.setValue(phoneNo);
			//
			// // 策略ID
			// SAVP savp2 = new SAVP();
			// savp2.setKey("ServiceCode");
			// savp2.setValue(policyId);
			//
			// //执行时间
			// SAVP savp3 = new SAVP();
			// savp3.setKey("operateTime");
			// savp3.setValue(sdf.format(new Date()));
			//
			// sPccSubscriber.getAttribute().add(savp);
			// sPccSubscriber.getAttribute().add(savp2);
			// sPccSubscriber.getAttribute().add(savp3);
			//
			// sInSubscriptionParaVO.setSubscriber(sPccSubscriber);
			// SReturnVO sReturnVO =
			// scf.unSubscribeService(sInSubscriptionParaVO);
			//
			// if ("0".equals(sReturnVO.getResultCode())) {
			// result = "0";
			// }

			PCRFSoapStub stub = new PCRFSoapStub(sprBean.getSprWsdl());
			stub._getServiceClient().getOptions()
					.setProperty(HTTPConstants.CHUNKED, false);

			PCRFSoapStub.UnSubscribeService unSubscribeService = new PCRFSoapStub.UnSubscribeService();
			PCRFSoapStub.SInSubscriptionParaVO siInSubscriberParaVO = new PCRFSoapStub.SInSubscriptionParaVO();
			PCRFSoapStub.SPccSubscriber sPccSubscriber = new PCRFSoapStub.SPccSubscriber();
			PCRFSoapStub.SSubscribedService subscribedService = new PCRFSoapStub.SSubscribedService();

			PCRFSoapStub.SAVP savp = new PCRFSoapStub.SAVP();
			PCRFSoapStub.SAVP savp2 = new PCRFSoapStub.SAVP();
			PCRFSoapStub.SAVP savp3 = new PCRFSoapStub.SAVP();
			savp.setKey("usrIdentifier");
			if (!StringUtil.isEmpty(prefix_phone)) {
				phoneNo = prefix_phone + phoneNo;
			}
			savp.setValue(phoneNo);

			savp2.setKey("operateTime");
			savp2.setValue(sdf.format(new Date()));
			savp3.setKey("ServiceCode");
			savp3.setValue(policyId);
			sPccSubscriber.addAttribute(savp);
			subscribedService.addAttribute(savp2);
			subscribedService.addAttribute(savp3);
			PCRFSoapStub.Header header48 = new PCRFSoapStub.Header();
			header48.setUsername(sprBean.getSprUsr());
			header48.setPassword(sprBean.getSprPwd());
			header48.setAddress(sprBean.getSprIp());
			header48.setTime(sdf.format(new Date()));
			header48.setSerial("" + ((Math.random() * 9 + 1) * 10000000));// 8位随机数
			siInSubscriberParaVO.setSubscriber(sPccSubscriber);
			siInSubscriberParaVO.addSubscribedService(subscribedService);
			unSubscribeService.setInPara(siInSubscriberParaVO);
			try {
				PCRFSoapStub.UnSubscribeServiceResponse re = stub
						.unSubscribeService(unSubscribeService, header48);
				PCRFSoapStub.SReturnVO sReturnVO = re.getResult();
				log.info("result code: " + sReturnVO.getResultCode());
				if (sReturnVO.getResultCode() == 0) {
					result = "0";
				}
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 销户
	 * 
	 * @param phoneNo
	 *            手机号
	 * @return
	 * @throws RemoteException 
	 */
	public String delSubscriber(String phoneNo, SprBusinessBean sprBean) throws RemoteException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		String result = "-1";
		try {
			// URL url = new URL(sprBean.getSprWsdl());
			// PCRFSoap pcrf = new PCRFSoap(url,new QName(
			// "http://www.chinamobile.com/PCRF/", "PCRFSoap"));
			// ScfPccSoapServiceEndpoint scf = pcrf
			// .getChinaMobilePCRFSoapServiceEndpointPort();
			// SInSubscriberParaVO sInSubscriberParaVO = new
			// SInSubscriberParaVO();
			// SPccSubscriber sPccSubscriber = new SPccSubscriber();
			// // 手机号码
			// SAVP savp = new SAVP();
			// savp.setKey("usrIdentifier");
			// if(!StringUtil.isEmpty(prefix_phone)){
			// phoneNo = prefix_phone + phoneNo;
			// }
			// savp.setValue(phoneNo);
			//
			// //执行时间
			// SAVP savp2 = new SAVP();
			// savp2.setKey("operateTime");
			// savp2.setValue(sdf.format(new Date()));
			//
			// sPccSubscriber.getAttribute().add(savp);
			// sPccSubscriber.getAttribute().add(savp2);
			//
			// sInSubscriberParaVO.setSubscriber(sPccSubscriber);
			// SReturnVO sReturnVO = scf.delSubscriber(sInSubscriberParaVO);
			//
			// if ("0".equals(sReturnVO.getResultCode())) {
			// result = "0";
			// }

			PCRFSoapStub stub = new PCRFSoapStub(sprBean.getSprWsdl());
			stub._getServiceClient().getOptions()
					.setProperty(HTTPConstants.CHUNKED, false);

			PCRFSoapStub.DelSubscriber delSubscriber = new PCRFSoapStub.DelSubscriber();
			PCRFSoapStub.SInSubscriberParaVO siInSubscriberParaVO = new PCRFSoapStub.SInSubscriberParaVO();
			PCRFSoapStub.SPccSubscriber sPccSubscriber = new PCRFSoapStub.SPccSubscriber();

			PCRFSoapStub.SAVP savp = new PCRFSoapStub.SAVP();
			PCRFSoapStub.SAVP savp2 = new PCRFSoapStub.SAVP();
			savp.setKey("usrIdentifier");
			if (!StringUtil.isEmpty(prefix_phone)) {
				phoneNo = prefix_phone + phoneNo;
			}
			savp.setValue(phoneNo);
			savp2.setKey("operateTime");
			savp2.setValue(sdf.format(new Date()));
			sPccSubscriber.addAttribute(savp);
			sPccSubscriber.addAttribute(savp2);
			PCRFSoapStub.Header header48 = new PCRFSoapStub.Header();
			header48.setUsername(sprBean.getSprUsr());
			header48.setPassword(sprBean.getSprPwd());
			header48.setAddress(sprBean.getSprIp());
			header48.setTime(sdf.format(new Date()));
			header48.setSerial("" + ((Math.random() * 9 + 1) * 10000000));// 8位随机数
			siInSubscriberParaVO.setSubscriber(sPccSubscriber);
			delSubscriber.setInPara(siInSubscriberParaVO);
			
				PCRFSoapStub.DelSubscriberResponse re = stub.delSubscriber(
						delSubscriber, header48);
				PCRFSoapStub.SReturnVO sReturnVO = re.getResult();
				log.info("result code: " + sReturnVO.getResultCode());

				if (sReturnVO.getResultCode() == 0||sReturnVO.getResultCode() == 110000) {
					result = "0";
				}
			
		}catch (Exception e) {
			e.printStackTrace();
			return result;
		}
		return result;
	}

	/**
	 * cpe销户
	 * 
	 * @param phoneNo
	 *            手机号
	 * @return
	 */
	public String delcpeSubscriber(String phoneNo,String subsid, SprBusinessBean sprBean) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		String result = "-1";
		try {
			// URL url = new URL(sprBean.getSprWsdl());
			// PCRFSoap pcrf = new PCRFSoap(url,new QName(
			// "http://www.chinamobile.com/PCRF/", "PCRFSoap"));
			// ScfPccSoapServiceEndpoint scf = pcrf
			// .getChinaMobilePCRFSoapServiceEndpointPort();
			// SInSubscriberParaVO sInSubscriberParaVO = new
			// SInSubscriberParaVO();
			// SPccSubscriber sPccSubscriber = new SPccSubscriber();
			// // 手机号码
			// SAVP savp = new SAVP();
			// savp.setKey("usrIdentifier");
			// if(!StringUtil.isEmpty(prefix_phone)){
			// phoneNo = prefix_phone + phoneNo;
			// }
			// savp.setValue(phoneNo);
			//
			// //执行时间
			// SAVP savp2 = new SAVP();
			// savp2.setKey("operateTime");
			// savp2.setValue(sdf.format(new Date()));
			//
			// sPccSubscriber.getAttribute().add(savp);
			// sPccSubscriber.getAttribute().add(savp2);
			//
			// sInSubscriberParaVO.setSubscriber(sPccSubscriber);
			// SReturnVO sReturnVO = scf.delSubscriber(sInSubscriberParaVO);
			//
			// if ("0".equals(sReturnVO.getResultCode())) {
			// result = "0";
			// }

			PCRFSoapStub stub = new PCRFSoapStub(sprBean.getSprWsdl());
			stub._getServiceClient().getOptions()
					.setProperty(HTTPConstants.CHUNKED, false);

			PCRFSoapStub.DelSubscriber delSubscriber = new PCRFSoapStub.DelSubscriber();
			PCRFSoapStub.SInSubscriberParaVO siInSubscriberParaVO = new PCRFSoapStub.SInSubscriberParaVO();
			PCRFSoapStub.SPccSubscriber sPccSubscriber = new PCRFSoapStub.SPccSubscriber();

			PCRFSoapStub.SAVP savp = new PCRFSoapStub.SAVP();
			PCRFSoapStub.SAVP savp2 = new PCRFSoapStub.SAVP();
			PCRFSoapStub.SAVP savp3 = new PCRFSoapStub.SAVP();
			savp.setKey("usrIdentifier");
			if (!StringUtil.isEmpty(prefix_phone)) {
				phoneNo = prefix_phone + phoneNo;
			}
			savp.setValue(phoneNo);
			savp2.setKey("operateTime");
			savp2.setValue(sdf.format(new Date()));
			savp3.setKey("usrIMSI ");
			savp3.setValue(subsid);
			sPccSubscriber.addAttribute(savp);
			sPccSubscriber.addAttribute(savp2);
			sPccSubscriber.addAttribute(savp3);
			PCRFSoapStub.Header header48 = new PCRFSoapStub.Header();
			header48.setUsername(sprBean.getSprUsr());
			header48.setPassword(sprBean.getSprPwd());
			header48.setAddress(sprBean.getSprIp());
			header48.setTime(sdf.format(new Date()));
			header48.setSerial("" + ((Math.random() * 9 + 1) * 10000000));// 8位随机数
			siInSubscriberParaVO.setSubscriber(sPccSubscriber);
			delSubscriber.setInPara(siInSubscriberParaVO);
			try {
				PCRFSoapStub.DelSubscriberResponse re = stub.delSubscriber(
						delSubscriber, header48);
				PCRFSoapStub.SReturnVO sReturnVO = re.getResult();
				log.info("result code: " + sReturnVO.getResultCode());

				if (sReturnVO.getResultCode() == 0) {
					result = "0";
				}
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	/**
	 * 修改用户属性签约信息
	 * 
	 * @param phoneNo
	 *            手机号
	 * @return
	 */
	public String updateSubscriber(String phoneNo, SprBusinessBean sprBean) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		String result = "-1";
		try {
			// URL url = new URL(sprBean.getSprWsdl());
			// PCRFSoap pcrf = new PCRFSoap(url,new QName(
			// "http://www.chinamobile.com/PCRF/", "PCRFSoap"));
			// ScfPccSoapServiceEndpoint scf = pcrf
			// .getChinaMobilePCRFSoapServiceEndpointPort();
			// SInSubscriberParaVO sInSubscriberParaVO = new
			// SInSubscriberParaVO();
			// SPccSubscriber sPccSubscriber = new SPccSubscriber();
			// // 手机号码
			// SAVP savp = new SAVP();
			// savp.setKey("usrIdentifier");
			// if(!StringUtil.isEmpty(prefix_phone)){
			// phoneNo = prefix_phone + phoneNo;
			// }
			// savp.setValue(phoneNo);
			//
			// //执行时间
			// SAVP savp2 = new SAVP();
			// savp2.setKey("operateTime");
			// savp2.setValue(sdf.format(new Date()));
			//
			// sPccSubscriber.getAttribute().add(savp);
			// sPccSubscriber.getAttribute().add(savp2);
			//
			// sInSubscriberParaVO.setSubscriber(sPccSubscriber);
			// SReturnVO sReturnVO = scf.updateSubscriber(sInSubscriberParaVO);
			//
			// if ("0".equals(sReturnVO.getResultCode())) {
			// result = "0";
			// }

			PCRFSoapStub stub = new PCRFSoapStub(sprBean.getSprWsdl());
			stub._getServiceClient().getOptions()
					.setProperty(HTTPConstants.CHUNKED, false);

			PCRFSoapStub.UpdateSubscriber updateSubscriber = new PCRFSoapStub.UpdateSubscriber();
			PCRFSoapStub.SInSubscriberParaVO siInSubscriberParaVO = new PCRFSoapStub.SInSubscriberParaVO();
			PCRFSoapStub.SPccSubscriber sPccSubscriber = new PCRFSoapStub.SPccSubscriber();
			PCRFSoapStub.SAVP savp = new PCRFSoapStub.SAVP();
			PCRFSoapStub.SAVP savp2 = new PCRFSoapStub.SAVP();
			savp.setKey("usrIdentifier");
			if (!StringUtil.isEmpty(prefix_phone)) {
				phoneNo = prefix_phone + phoneNo;
			}
			savp.setValue(phoneNo);
			savp2.setKey("operateTime");
			savp2.setValue(sdf.format(new Date()));
			sPccSubscriber.addAttribute(savp);
			sPccSubscriber.addAttribute(savp2);
			PCRFSoapStub.Header header48 = new PCRFSoapStub.Header();
			header48.setUsername(sprBean.getSprUsr());
			header48.setPassword(sprBean.getSprPwd());
			header48.setAddress(sprBean.getSprIp());
			header48.setTime(sdf.format(new Date()));
			header48.setSerial("" + ((Math.random() * 9 + 1) * 10000000));// 8位随机数
			siInSubscriberParaVO.setSubscriber(sPccSubscriber);
			updateSubscriber.setInPara(siInSubscriberParaVO);
			try {
				PCRFSoapStub.UpdateSubscriberResponse re = stub
						.updateSubscriber(updateSubscriber, header48);
				PCRFSoapStub.SReturnVO sReturnVO = re.getResult();
				log.info("result code: " + sReturnVO.getResultCode());

				if (sReturnVO.getResultCode() == 0) {
					result = "0";
				}
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 修改用户属性签约信息,修改用户状态
	 * 
	 * @param phoneNo 手机号
	 * @param status 用户状态
	 * @return
	 */
	public String updateSubscriber(String phoneNo, String status,SprBusinessBean sprBean) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		String result = "-1";
		try {
			PCRFSoapStub stub = new PCRFSoapStub(sprBean.getSprWsdl());
			stub._getServiceClient().getOptions()
					.setProperty(HTTPConstants.CHUNKED, false);
			PCRFSoapStub.UpdateSubscriber updateSubscriber = new PCRFSoapStub.UpdateSubscriber();
			PCRFSoapStub.SInSubscriberParaVO siInSubscriberParaVO = new PCRFSoapStub.SInSubscriberParaVO();
			PCRFSoapStub.SPccSubscriber sPccSubscriber = new PCRFSoapStub.SPccSubscriber();
			PCRFSoapStub.SAVP savp = new PCRFSoapStub.SAVP();
			PCRFSoapStub.SAVP savp2 = new PCRFSoapStub.SAVP();
			PCRFSoapStub.SAVP savp3 = new PCRFSoapStub.SAVP();
			PCRFSoapStub.SAVP savp4 = new PCRFSoapStub.SAVP();
			savp.setKey("usrIdentifier");
			if (!StringUtil.isEmpty(prefix_phone)) {
				phoneNo = prefix_phone + phoneNo;
			}
			savp.setValue(phoneNo);
			savp2.setKey("operateTime");
			savp2.setValue(sdf.format(new Date()));
			savp3.setKey("usrNotifyMSISDN");
			savp3.setValue(phoneNo);
			savp4.setKey("usrStatus");
			savp4.setValue(status);
			sPccSubscriber.addAttribute(savp);
			sPccSubscriber.addAttribute(savp2);
			sPccSubscriber.addAttribute(savp3);
			sPccSubscriber.addAttribute(savp4);
			PCRFSoapStub.Header header48 = new PCRFSoapStub.Header();
			header48.setUsername(sprBean.getSprUsr());
			header48.setPassword(sprBean.getSprPwd());
			header48.setAddress(sprBean.getSprIp());
			header48.setTime(sdf.format(new Date()));
			header48.setSerial("" + ((Math.random() * 9 + 1) * 10000000));// 8位随机数
			siInSubscriberParaVO.setSubscriber(sPccSubscriber);
			updateSubscriber.setInPara(siInSubscriberParaVO);
			try {
				PCRFSoapStub.UpdateSubscriberResponse re = stub
						.updateSubscriber(updateSubscriber, header48);
				PCRFSoapStub.SReturnVO sReturnVO = re.getResult();
				log.info(phoneNo+" result code: " + sReturnVO.getResultCode());

				if (sReturnVO.getResultCode() == 0) {
					result = "0";
				}
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * 修改业务策略信息
	 * 
	 * @param phoneNo
	 *            手机号
	 * @param policyId
	 *            策略ID
	 * @return
	 */
	public String updateSubscribedService(String phoneNo, String policyId,
			SprBusinessBean sprBean) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		String result = "-1";
		try {
			// URL url = new URL(sprBean.getSprWsdl());
			// PCRFSoap pcrf = new PCRFSoap(url,new QName(
			// "http://www.chinamobile.com/PCRF/", "PCRFSoap"));
			// ScfPccSoapServiceEndpoint scf = pcrf
			// .getChinaMobilePCRFSoapServiceEndpointPort();
			// SInSubscriptionParaVO sInSubscriptionParaVO = new
			// SInSubscriptionParaVO();
			// SPccSubscriber sPccSubscriber = new SPccSubscriber();
			// // 手机号码
			// SAVP savp = new SAVP();
			// savp.setKey("usrIdentifier");
			// if(!StringUtil.isEmpty(prefix_phone)){
			// phoneNo = prefix_phone + phoneNo;
			// }
			// savp.setValue(phoneNo);
			//
			// // 策略ID
			// SAVP savp2 = new SAVP();
			// savp2.setKey("ServiceCode");
			// savp2.setValue(policyId);
			//
			// //执行时间
			// SAVP savp3 = new SAVP();
			// savp3.setKey("operateTime");
			// savp3.setValue(sdf.format(new Date()));
			//
			// sPccSubscriber.getAttribute().add(savp);
			// sPccSubscriber.getAttribute().add(savp2);
			// sPccSubscriber.getAttribute().add(savp3);
			//
			// sInSubscriptionParaVO.setSubscriber(sPccSubscriber);
			// SReturnVO sReturnVO = scf
			// .updateSubscribedService(sInSubscriptionParaVO);
			//
			// if ("0".equals(sReturnVO.getResultCode())) {
			// result = "0";
			// }

			PCRFSoapStub stub = new PCRFSoapStub(sprBean.getSprWsdl());
			stub._getServiceClient().getOptions()
					.setProperty(HTTPConstants.CHUNKED, false);

			PCRFSoapStub.UpdateSubscribedService updateSubscribedService = new PCRFSoapStub.UpdateSubscribedService();
			PCRFSoapStub.SInSubscriptionParaVO subscriptionParaVO = new PCRFSoapStub.SInSubscriptionParaVO();
			PCRFSoapStub.SPccSubscriber sPccSubscriber = new PCRFSoapStub.SPccSubscriber();
			PCRFSoapStub.SSubscribedService subscribedService = new PCRFSoapStub.SSubscribedService();

			PCRFSoapStub.SAVP savp = new PCRFSoapStub.SAVP();
			PCRFSoapStub.SAVP savp2 = new PCRFSoapStub.SAVP();
			PCRFSoapStub.SAVP savp3 = new PCRFSoapStub.SAVP();

			savp.setKey("usrIdentifier");
			if (!StringUtil.isEmpty(prefix_phone)) {
				phoneNo = prefix_phone + phoneNo;
			}
			savp.setValue(phoneNo);
			savp2.setKey("operateTime");
			savp2.setValue(sdf.format(new Date()));

			savp3.setKey("ServiceCode");
			savp3.setValue(policyId);
			sPccSubscriber.addAttribute(savp);
			subscribedService.addAttribute(savp2);
			subscribedService.addAttribute(savp3);

			PCRFSoapStub.Header header48 = new PCRFSoapStub.Header();
			header48.setUsername(sprBean.getSprUsr());
			header48.setPassword(sprBean.getSprPwd());
			header48.setAddress(sprBean.getSprIp());
			header48.setTime(sdf.format(new Date()));
			header48.setSerial("" + ((Math.random() * 9 + 1) * 10000000));// 8位随机数
			subscriptionParaVO.setSubscriber(sPccSubscriber);
			subscriptionParaVO.addSubscribedService(subscribedService);
			updateSubscribedService.setInPara(subscriptionParaVO);
			try {
				PCRFSoapStub.UpdateSubscribedServiceResponse re = stub
						.updateSubscribedService(updateSubscribedService,
								header48);
				PCRFSoapStub.SReturnVO sReturnVO = re.getResult();
				log.info("result code: " + sReturnVO.getResultCode());

				if (sReturnVO.getResultCode() == 0) {
					result = "0";
				}
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 用户策略签约
	 * 
	 * @param phoneNo
	 *            手机号
	 * @param policyId
	 *            策略ID
	 * @return
	 */
	public String subscribeUsrSessionPolicy(String phoneNo, String policyId,
			SprBusinessBean sprBean) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		String result = "-1";
		try {
			// URL url = new URL(sprBean.getSprWsdl());
			// PCRFSoap pcrf = new PCRFSoap(url,new QName(
			// "http://www.chinamobile.com/PCRF/", "PCRFSoap"));
			// ScfPccSoapServiceEndpoint scf = pcrf
			// .getChinaMobilePCRFSoapServiceEndpointPort();
			// SInUsrSessionPolicyParaVO sInUsrSessionPolicyParaVO = new
			// SInUsrSessionPolicyParaVO();
			// SPccSubscriber sPccSubscriber = new SPccSubscriber();
			// // 手机号码
			// SAVP savp = new SAVP();
			// savp.setKey("usrIdentifier");
			// if(!StringUtil.isEmpty(prefix_phone)){
			// phoneNo = prefix_phone + phoneNo;
			// }
			// savp.setValue(phoneNo);
			//
			// // 策略ID
			// SAVP savp2 = new SAVP();
			// savp2.setKey("usrSessionPolicyCode");
			// savp2.setValue(policyId);
			//
			// //执行时间
			// SAVP savp3 = new SAVP();
			// savp3.setKey("operateTime");
			// savp3.setValue(sdf.format(new Date()));
			//
			// sPccSubscriber.getAttribute().add(savp);
			// sPccSubscriber.getAttribute().add(savp2);
			// sPccSubscriber.getAttribute().add(savp3);
			//
			// sInUsrSessionPolicyParaVO.setSubscriber(sPccSubscriber);
			// SReturnVO sReturnVO = scf
			// .subscribeUsrSessionPolicy(sInUsrSessionPolicyParaVO);
			//
			// if ("0".equals(sReturnVO.getResultCode())) {
			// result = "0";
			// }

			PCRFSoapStub stub = new PCRFSoapStub(sprBean.getSprWsdl());
			stub._getServiceClient().getOptions()
					.setProperty(HTTPConstants.CHUNKED, false);

			PCRFSoapStub.SubscribeUsrSessionPolicy subscribeUsrSessionPolicy = new PCRFSoapStub.SubscribeUsrSessionPolicy();
			PCRFSoapStub.SInUsrSessionPolicyParaVO sInUsrSessionPolicyParaVO = new PCRFSoapStub.SInUsrSessionPolicyParaVO();
			PCRFSoapStub.SPccSubscriber sPccSubscriber = new PCRFSoapStub.SPccSubscriber();
			PCRFSoapStub.SUsrSessionPolicy sUsrSessionPolicy = new PCRFSoapStub.SUsrSessionPolicy();

			PCRFSoapStub.SAVP savp = new PCRFSoapStub.SAVP();
			PCRFSoapStub.SAVP savp2 = new PCRFSoapStub.SAVP();
			PCRFSoapStub.SAVP savp3 = new PCRFSoapStub.SAVP();

			savp.setKey("usrIdentifier");
			if (!StringUtil.isEmpty(prefix_phone)) {
				phoneNo = prefix_phone + phoneNo;
			}
			savp.setValue(phoneNo);
			savp2.setKey("operateTime");
			savp2.setValue(sdf.format(new Date()));

			savp3.setKey("usrSessionPolicyCode");
			savp3.setValue(policyId);

			sPccSubscriber.addAttribute(savp);
			sUsrSessionPolicy.addAttribute(savp2);
			sUsrSessionPolicy.addAttribute(savp3);

			PCRFSoapStub.Header header48 = new PCRFSoapStub.Header();
			header48.setUsername(sprBean.getSprUsr());
			header48.setPassword(sprBean.getSprPwd());
			header48.setAddress(sprBean.getSprIp());
			header48.setTime(sdf.format(new Date()));
			header48.setSerial("" + ((Math.random() * 9 + 1) * 10000000));// 8位随机数
			sInUsrSessionPolicyParaVO.setSubscriber(sPccSubscriber);
			sInUsrSessionPolicyParaVO
					.addSubscribedUsrSessionPolicy(sUsrSessionPolicy);
			subscribeUsrSessionPolicy.setInPara(sInUsrSessionPolicyParaVO);
				PCRFSoapStub.SubscribeUsrSessionPolicyResponse re = stub
						.subscribeUsrSessionPolicy(subscribeUsrSessionPolicy,
								header48);
				PCRFSoapStub.SReturnVO sReturnVO = re.getResult();
				log.info("result code: " + sReturnVO.getResultCode());

				if (sReturnVO.getResultCode() == 0||sReturnVO.getResultCode() == 110009) {
					result = "0";
				}
		} catch (Exception e) {
			e.printStackTrace();
			return result;
		}
		return result;
	}
	
	/**
	 * 用户策略签约，并设置结算周期
	 * 
	 * @param phoneNo
	 *            手机号
	 * @param policyId
	 *            策略ID
	 * @return
	 */
	public String subscribeUsrSessionPolicy(String phoneNo, String policyId,String notificationCycle,
			SprBusinessBean sprBean) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		String result = "-1";
		try {

			PCRFSoapStub stub = new PCRFSoapStub(sprBean.getSprWsdl());
			stub._getServiceClient().getOptions()
					.setProperty(HTTPConstants.CHUNKED, false);

			PCRFSoapStub.SubscribeUsrSessionPolicy subscribeUsrSessionPolicy = new PCRFSoapStub.SubscribeUsrSessionPolicy();
			PCRFSoapStub.SInUsrSessionPolicyParaVO sInUsrSessionPolicyParaVO = new PCRFSoapStub.SInUsrSessionPolicyParaVO();
			PCRFSoapStub.SPccSubscriber sPccSubscriber = new PCRFSoapStub.SPccSubscriber();
			PCRFSoapStub.SUsrSessionPolicy sUsrSessionPolicy = new PCRFSoapStub.SUsrSessionPolicy();

			PCRFSoapStub.SAVP savp = new PCRFSoapStub.SAVP();
			PCRFSoapStub.SAVP savp2 = new PCRFSoapStub.SAVP();
			PCRFSoapStub.SAVP savp3 = new PCRFSoapStub.SAVP();
			PCRFSoapStub.SAVP savp4 = new PCRFSoapStub.SAVP();

			savp.setKey("usrIdentifier");
			if (!StringUtil.isEmpty(prefix_phone)) {
				phoneNo = prefix_phone + phoneNo;
			}
			savp.setValue(phoneNo);
			
			savp2.setKey("operateTime");
			savp2.setValue(sdf.format(new Date()));

			savp3.setKey("usrSessionPolicyCode");
			savp3.setValue(policyId);

			savp4.setKey("NotificationCycle");
			savp4.setValue(notificationCycle);
			
			sPccSubscriber.addAttribute(savp);
			sUsrSessionPolicy.addAttribute(savp2);
			sUsrSessionPolicy.addAttribute(savp3);
			if(Integer.parseInt(notificationCycle)>=0){
				sUsrSessionPolicy.addAttribute(savp4);
			}

			PCRFSoapStub.Header header48 = new PCRFSoapStub.Header();
			header48.setUsername(sprBean.getSprUsr());
			header48.setPassword(sprBean.getSprPwd());
			header48.setAddress(sprBean.getSprIp());
			header48.setTime(sdf.format(new Date()));
			header48.setSerial("" + ((Math.random() * 9 + 1) * 10000000));// 8位随机数
			sInUsrSessionPolicyParaVO.setSubscriber(sPccSubscriber);
			sInUsrSessionPolicyParaVO
					.addSubscribedUsrSessionPolicy(sUsrSessionPolicy);
			subscribeUsrSessionPolicy.setInPara(sInUsrSessionPolicyParaVO);
				PCRFSoapStub.SubscribeUsrSessionPolicyResponse re = stub
						.subscribeUsrSessionPolicy(subscribeUsrSessionPolicy,
								header48);
				PCRFSoapStub.SReturnVO sReturnVO = re.getResult();
				log.info("result code: " + sReturnVO.getResultCode());

				if (sReturnVO.getResultCode() == 0||sReturnVO.getResultCode() == 110009) {
					result = "0";
				}
		} catch (Exception e) {
			e.printStackTrace();
			return result;
		}
		return result;
	}
	
	/**
	 * 用户策略签约,用户签约策略，并设置策略的开始时间和结算时间
	 * 
	 * @param phoneNo  手机号
	 * @param policyId  策略ID
	 * @param startDateTime 开始时间
	 * @param endDateTime 结束时间
	 * @return
	 */
	public String subscribeUsrSessionPolicy(String phoneNo, String policyId,String startDateTime,String endDateTime,
			SprBusinessBean sprBean) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		String result = "-1";
		try {
			PCRFSoapStub stub = new PCRFSoapStub(sprBean.getSprWsdl());
			stub._getServiceClient().getOptions()
					.setProperty(HTTPConstants.CHUNKED, false);

			PCRFSoapStub.SubscribeUsrSessionPolicy subscribeUsrSessionPolicy = new PCRFSoapStub.SubscribeUsrSessionPolicy();
			PCRFSoapStub.SInUsrSessionPolicyParaVO sInUsrSessionPolicyParaVO = new PCRFSoapStub.SInUsrSessionPolicyParaVO();
			PCRFSoapStub.SPccSubscriber sPccSubscriber = new PCRFSoapStub.SPccSubscriber();
			PCRFSoapStub.SUsrSessionPolicy sUsrSessionPolicy = new PCRFSoapStub.SUsrSessionPolicy();

			PCRFSoapStub.SAVP savp = new PCRFSoapStub.SAVP();
			PCRFSoapStub.SAVP savp2 = new PCRFSoapStub.SAVP();
			PCRFSoapStub.SAVP savp3 = new PCRFSoapStub.SAVP();
			PCRFSoapStub.SAVP savp4 = new PCRFSoapStub.SAVP();//开始时间
			PCRFSoapStub.SAVP savp5 = new PCRFSoapStub.SAVP();//结束时间

			savp.setKey("usrIdentifier");
			if (!StringUtil.isEmpty(prefix_phone)) {
				phoneNo = prefix_phone + phoneNo;
			}
			savp.setValue(phoneNo);
			savp2.setKey("operateTime");
			savp2.setValue(sdf.format(new Date()));

			savp3.setKey("usrSessionPolicyCode");
			savp3.setValue(policyId);
			savp4.setKey("SessionPolicyStartDateTime");
			savp4.setValue(startDateTime);
			savp5.setKey("SessionPolicyEndDateTime");
			savp5.setValue(endDateTime);

			sPccSubscriber.addAttribute(savp);
			sUsrSessionPolicy.addAttribute(savp2);
			sUsrSessionPolicy.addAttribute(savp3);
			sUsrSessionPolicy.addAttribute(savp4);
			sUsrSessionPolicy.addAttribute(savp5);

			PCRFSoapStub.Header header48 = new PCRFSoapStub.Header();
			header48.setUsername(sprBean.getSprUsr());
			header48.setPassword(sprBean.getSprPwd());
			header48.setAddress(sprBean.getSprIp());
			header48.setTime(sdf.format(new Date()));
			header48.setSerial("" + ((Math.random() * 9 + 1) * 10000000));// 8位随机数
			sInUsrSessionPolicyParaVO.setSubscriber(sPccSubscriber);
			sInUsrSessionPolicyParaVO
					.addSubscribedUsrSessionPolicy(sUsrSessionPolicy);
			subscribeUsrSessionPolicy.setInPara(sInUsrSessionPolicyParaVO);
				PCRFSoapStub.SubscribeUsrSessionPolicyResponse re = stub
						.subscribeUsrSessionPolicy(subscribeUsrSessionPolicy,
								header48);
				PCRFSoapStub.SReturnVO sReturnVO = re.getResult();
				log.info("result code: " + sReturnVO.getResultCode());

				if (sReturnVO.getResultCode() == 0||sReturnVO.getResultCode() == 110009) {
					result = "0";
				}
		} catch (Exception e) {
			e.printStackTrace();
			return result;
		}
		return result;
	}

	/**
	 * 修改用户策略信息
	 * 
	 * @param phoneNo
	 *            手机号码
	 * @param policyId
	 *            策略ID
	 * @return
	 */
	public String updateUsrSessionPolicy(String phoneNo, String policyId,
			SprBusinessBean sprBean) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		String result = "-1";
		try {
			// URL url = new URL(sprBean.getSprWsdl());
			// PCRFSoap pcrf = new PCRFSoap(url,new QName(
			// "http://www.chinamobile.com/PCRF/", "PCRFSoap"));
			// ScfPccSoapServiceEndpoint scf = pcrf
			// .getChinaMobilePCRFSoapServiceEndpointPort();
			// SInUsrSessionPolicyParaVO sInUsrSessionPolicyParaVO = new
			// SInUsrSessionPolicyParaVO();
			// SPccSubscriber sPccSubscriber = new SPccSubscriber();
			// // 手机号码
			// SAVP savp = new SAVP();
			// savp.setKey("usrIdentifier");
			// if(!StringUtil.isEmpty(prefix_phone)){
			// phoneNo = prefix_phone + phoneNo;
			// }
			// savp.setValue(phoneNo);
			//
			// // 策略ID
			// SAVP savp2 = new SAVP();
			// savp2.setKey("usrSessionPolicyCode");
			// savp2.setValue(policyId);
			//
			// //执行时间
			// SAVP savp3 = new SAVP();
			// savp3.setKey("operateTime");
			// savp3.setValue(sdf.format(new Date()));
			//
			// sPccSubscriber.getAttribute().add(savp);
			// sPccSubscriber.getAttribute().add(savp2);
			// sPccSubscriber.getAttribute().add(savp3);
			//
			// sInUsrSessionPolicyParaVO.setSubscriber(sPccSubscriber);
			// SReturnVO sReturnVO = scf
			// .updateUsrSessionPolicy(sInUsrSessionPolicyParaVO);
			//
			// if ("0".equals(sReturnVO.getResultCode())) {
			// result = "0";
			// }

			PCRFSoapStub stub = new PCRFSoapStub(sprBean.getSprWsdl());
			stub._getServiceClient().getOptions()
					.setProperty(HTTPConstants.CHUNKED, false);

			PCRFSoapStub.UpdateUsrSessionPolicy updateUsrSessionPolicy = new PCRFSoapStub.UpdateUsrSessionPolicy();
			PCRFSoapStub.SInUsrSessionPolicyParaVO sInUsrSessionPolicyParaVO = new PCRFSoapStub.SInUsrSessionPolicyParaVO();
			PCRFSoapStub.SPccSubscriber sPccSubscriber = new PCRFSoapStub.SPccSubscriber();
			PCRFSoapStub.SUsrSessionPolicy sUsrSessionPolicy = new PCRFSoapStub.SUsrSessionPolicy();

			PCRFSoapStub.SAVP savp = new PCRFSoapStub.SAVP();
			PCRFSoapStub.SAVP savp2 = new PCRFSoapStub.SAVP();
			PCRFSoapStub.SAVP savp3 = new PCRFSoapStub.SAVP();

			savp.setKey("usrIdentifier");
			if (!StringUtil.isEmpty(prefix_phone)) {
				phoneNo = prefix_phone + phoneNo;
			}
			savp.setValue(phoneNo);
			savp2.setKey("operateTime");
			savp2.setValue(sdf.format(new Date()));

			savp3.setKey("usrSessionPolicyCode");
			savp3.setValue(policyId);

			sPccSubscriber.addAttribute(savp);
			sUsrSessionPolicy.addAttribute(savp2);
			sUsrSessionPolicy.addAttribute(savp3);

			PCRFSoapStub.Header header48 = new PCRFSoapStub.Header();
			header48.setUsername(sprBean.getSprUsr());
			header48.setPassword(sprBean.getSprPwd());
			header48.setAddress(sprBean.getSprIp());
			header48.setTime(sdf.format(new Date()));
			header48.setSerial("" + ((Math.random() * 9 + 1) * 10000000));// 8位随机数
			sInUsrSessionPolicyParaVO.setSubscriber(sPccSubscriber);
			sInUsrSessionPolicyParaVO
					.addSubscribedUsrSessionPolicy(sUsrSessionPolicy);
			updateUsrSessionPolicy.setInPara(sInUsrSessionPolicyParaVO);
			try {
				PCRFSoapStub.UpdateUsrSessionPolicyResponse re = stub
						.updateUsrSessionPolicy(updateUsrSessionPolicy,
								header48);
				PCRFSoapStub.SReturnVO sReturnVO = re.getResult();
				log.info("result code: " + sReturnVO.getResultCode());

				if (sReturnVO.getResultCode() == 0) {
					result = "0";
				}
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 删除用户策略信息
	 * 
	 * @param phoneNo
	 *            手机号
	 * @param policyId
	 *            策略ID
	 * @return
	 */
	public String unsubscribeUsrSessionPolicy(String phoneNo, String policyId,
			SprBusinessBean sprBean) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		String result = "-1";
		try {
			// URL url = new URL(sprBean.getSprWsdl());
			// PCRFSoap pcrf = new PCRFSoap(url,new QName(
			// "http://www.chinamobile.com/PCRF/", "PCRFSoap"));
			// ScfPccSoapServiceEndpoint scf = pcrf
			// .getChinaMobilePCRFSoapServiceEndpointPort();
			// SInUsrSessionPolicyParaVO sInUsrSessionPolicyParaVO = new
			// SInUsrSessionPolicyParaVO();
			// SPccSubscriber sPccSubscriber = new SPccSubscriber();
			// // 手机号码
			// SAVP savp = new SAVP();
			// savp.setKey("usrIdentifier");
			// if(!StringUtil.isEmpty(prefix_phone)){
			// phoneNo = prefix_phone + phoneNo;
			// }
			// savp.setValue(phoneNo);
			//
			// // 策略ID
			// SAVP savp2 = new SAVP();
			// savp2.setKey("usrSessionPolicyCode");
			// savp2.setValue(policyId);
			//
			// //执行时间
			// SAVP savp3 = new SAVP();
			// savp3.setKey("operateTime");
			// savp3.setValue(sdf.format(new Date()));
			//
			// sPccSubscriber.getAttribute().add(savp);
			// sPccSubscriber.getAttribute().add(savp2);
			// sPccSubscriber.getAttribute().add(savp3);
			//
			// sInUsrSessionPolicyParaVO.setSubscriber(sPccSubscriber);
			// SReturnVO sReturnVO = scf
			// .unsubscribeUsrSessionPolicy(sInUsrSessionPolicyParaVO);
			//
			// if ("0".equals(sReturnVO.getResultCode())) {
			// result = "0";
			// }

			PCRFSoapStub stub = new PCRFSoapStub(sprBean.getSprWsdl());
			stub._getServiceClient().getOptions()
					.setProperty(HTTPConstants.CHUNKED, false);

			PCRFSoapStub.UnsubscribeUsrSessionPolicy unsubscribeUsrSessionPolicy = new PCRFSoapStub.UnsubscribeUsrSessionPolicy();
			PCRFSoapStub.SInUsrSessionPolicyParaVO sInUsrSessionPolicyParaVO = new PCRFSoapStub.SInUsrSessionPolicyParaVO();
			PCRFSoapStub.SPccSubscriber sPccSubscriber = new PCRFSoapStub.SPccSubscriber();
			PCRFSoapStub.SUsrSessionPolicy sUsrSessionPolicy = new PCRFSoapStub.SUsrSessionPolicy();

			PCRFSoapStub.SAVP savp = new PCRFSoapStub.SAVP();
			PCRFSoapStub.SAVP savp2 = new PCRFSoapStub.SAVP();
			PCRFSoapStub.SAVP savp3 = new PCRFSoapStub.SAVP();

			savp.setKey("usrIdentifier");
			if (!StringUtil.isEmpty(prefix_phone)) {
				phoneNo = prefix_phone + phoneNo;
			}
			savp.setValue(phoneNo);
			savp2.setKey("operateTime");
			savp2.setValue(sdf.format(new Date()));

			savp3.setKey("usrSessionPolicyCode");
			savp3.setValue(policyId);

			sPccSubscriber.addAttribute(savp);
			sUsrSessionPolicy.addAttribute(savp2);
			sUsrSessionPolicy.addAttribute(savp3);

			PCRFSoapStub.Header header48 = new PCRFSoapStub.Header();
			header48.setUsername(sprBean.getSprUsr());
			header48.setPassword(sprBean.getSprPwd());
			header48.setAddress(sprBean.getSprIp());
			header48.setTime(sdf.format(new Date()));
			header48.setSerial("" + ((Math.random() * 9 + 1) * 10000000));// 8位随机数
			sInUsrSessionPolicyParaVO.setSubscriber(sPccSubscriber);
			sInUsrSessionPolicyParaVO
					.addSubscribedUsrSessionPolicy(sUsrSessionPolicy);
			unsubscribeUsrSessionPolicy.setInPara(sInUsrSessionPolicyParaVO);
			PCRFSoapStub.UnsubscribeUsrSessionPolicyResponse re = stub
						.unsubscribeUsrSessionPolicy(
								unsubscribeUsrSessionPolicy, header48);
			PCRFSoapStub.SReturnVO sReturnVO = re.getResult();
			log.info("result code: " + sReturnVO.getResultCode());
			
			/*if(sReturnVO.getResultCode() ==110000){
				insertNoProductNo(phoneNo,110000);
			}*/
			if (sReturnVO.getResultCode() == 0 || sReturnVO.getResultCode() == 110010) {
					result = "0";
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			return result;
		}
		return result;
	}

	/**
	 * CPE用户开户
	 * 
	 * @param subid
	 *            CPE设备号
	 * 
	 * @param phoneNo
	 *            手机号
	 * @return
	 */
	public String addCpeSubscriber(String subid, String phoneNo,
			SprBusinessBean sprBean) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		String result = "-1";
		try {
			PCRFSoapStub stub = new PCRFSoapStub(sprBean.getSprWsdl());
			stub._getServiceClient().getOptions()
					.setProperty(HTTPConstants.CHUNKED, false);
			PCRFSoapStub.AddSubscriber addSubscriber47 = new PCRFSoapStub.AddSubscriber();
			PCRFSoapStub.SInSubscriberParaVO siInSubscriberParaVO = new PCRFSoapStub.SInSubscriberParaVO();
			PCRFSoapStub.SPccSubscriber sPccSubscriber = new PCRFSoapStub.SPccSubscriber();
			PCRFSoapStub.SAVP savp = new PCRFSoapStub.SAVP();
			PCRFSoapStub.SAVP savp1 = new PCRFSoapStub.SAVP();
			PCRFSoapStub.SAVP savp2 = new PCRFSoapStub.SAVP();
			PCRFSoapStub.SAVP savp3 = new PCRFSoapStub.SAVP();

			savp.setKey("usrIMSI");
			savp.setValue(subid);
			System.out.println(subid);
			savp1.setKey("usrIdentifier");
			if (!StringUtil.isEmpty(prefix_phone)) {
				phoneNo = prefix_phone + phoneNo;
			}
			savp1.setValue(phoneNo);
			savp2.setKey("operateTime");
			savp2.setValue(sdf.format(new Date()));
			savp3.setKey("usrNotifyMSISDN");
			savp3.setValue(phoneNo);
			sPccSubscriber.addAttribute(savp);
			sPccSubscriber.addAttribute(savp1);
			sPccSubscriber.addAttribute(savp2);
			sPccSubscriber.addAttribute(savp3);
			PCRFSoapStub.Header header48 = new PCRFSoapStub.Header();
			header48.setUsername(sprBean.getSprUsr());
			header48.setPassword(sprBean.getSprPwd());
			header48.setAddress(sprBean.getSprIp());
			header48.setTime(sdf.format(new Date()));
			header48.setSerial("" + ((Math.random() * 9 + 1) * 10000000));// 8位随机数
			siInSubscriberParaVO.setSubscriber(sPccSubscriber);
			addSubscriber47.setInPara(siInSubscriberParaVO);
			try {
				PCRFSoapStub.AddSubscriberResponse re = stub.addSubscriber(
						addSubscriber47, header48);
				PCRFSoapStub.SReturnVO sReturnVO = re.getResult();
				log.info("result code: " + sReturnVO.getResultCode());
				// 0 成功 110001 用户已存在
				if (sReturnVO.getResultCode() == 0
						|| sReturnVO.getResultCode() == 110001) {
					result = "0";
				}
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
/**
 * 添加小区
 * @param phone
 * 用户手机号
 * @param policyId
 * 策略号
 * @param usrlocations
 * 九个小区信息
 * @param sprBean
 * @return
 */
	public String addUsrLocation(String phoneNo, String policyId,
			List<String> usrlocations, SprBusinessBean sprBean) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		String result = "-1";
		//9个小区以字符串数据的形式传入
		if(usrlocations==null || usrlocations.isEmpty()||usrlocations.size()==0)
		{
			return result;
		}
		String userlocation = usrlocations.get(0);
		for (int i = 1; i < usrlocations.size(); i++) {
			userlocation+=","+usrlocations.get(i);
		}
		System.out.println(userlocation);
		try {
			PCRFSoapStub stub = new PCRFSoapStub(sprBean.getSprWsdl());
			stub._getServiceClient().getOptions()
					.setProperty(HTTPConstants.CHUNKED, false);
			PCRFSoapStub.AddUsrLocation addUsrLocation74 = new PCRFSoapStub.AddUsrLocation();
			PCRFSoapStub.SInUsrLocationParaVO siInSubscriberParaVO = new PCRFSoapStub.SInUsrLocationParaVO();
			PCRFSoapStub.SPccSubscriber sPccSubscriber = new PCRFSoapStub.SPccSubscriber();
			PCRFSoapStub.SUsrLocation sUsrLocation = new PCRFSoapStub.SUsrLocation();
			PCRFSoapStub.SAVP savp = new PCRFSoapStub.SAVP();
			PCRFSoapStub.SAVP savp1 = new PCRFSoapStub.SAVP();
			PCRFSoapStub.SAVP savp2 = new PCRFSoapStub.SAVP();
			PCRFSoapStub.SAVP savp3 = new PCRFSoapStub.SAVP();

			savp.setKey("usrIdentifier");
			if (!StringUtil.isEmpty(prefix_phone)) {
				phoneNo = prefix_phone + phoneNo;
			}
			savp.setValue(phoneNo);
			savp1.setKey("operateTime");
			savp1.setValue(sdf.format(new Date()));
			savp2.setKey("usrSessionPolicyCode");
			savp2.setValue(policyId);
			savp3.setKey("UsrLocation");
			savp3.setValue(userlocation);
			sPccSubscriber.addAttribute(savp);
			sUsrLocation.addAttribute(savp1);
			sUsrLocation.addAttribute(savp2);
			sUsrLocation.addAttribute(savp3);
			PCRFSoapStub.Header header74 = new PCRFSoapStub.Header();
			header74.setUsername(sprBean.getSprUsr());
			header74.setPassword(sprBean.getSprPwd());
			header74.setAddress(sprBean.getSprIp());
			header74.setTime(sdf.format(new Date()));
			header74.setSerial("" + ((Math.random() * 9 + 1) * 10000000));// 8位随机数
			siInSubscriberParaVO.setSubscriber(sPccSubscriber);
			siInSubscriberParaVO.addSubscribedUsrLocation(sUsrLocation);
			System.out.println("add susrlocation");
			addUsrLocation74.setInPara(siInSubscriberParaVO);
			AddUsrLocationResponse re = stub.addUsrLocation(
						addUsrLocation74, header74);
			PCRFSoapStub.SReturnVO sReturnVO = re.getResult();
			log.info("result code: " + sReturnVO.getResultCode());
			// 0 成功 110001 用户已存在
			if (sReturnVO.getResultCode() == 0) {
				result = "0";
			}
		} catch (Exception e) {
			e.printStackTrace();
			return result;
		}
		return result;
	}
	
		
	public String updateUserLocation(String phoneNo,String poloicId,List<String> locations,SprBusinessBean sprBean){
		String result = "-1";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		//9个小区以字符串数据的形式传入
		String userlocation = locations.get(0);
		for (int i = 1; i < locations.size(); i++) {
			userlocation+=","+locations.get(i);
		}
		System.out.println(userlocation);
		try {
			PCRFSoapStub stub = new PCRFSoapStub(sprBean.getSprWsdl());
			stub._getServiceClient().getOptions()
					.setProperty(HTTPConstants.CHUNKED, false);
			PCRFSoapStub.UpdateUsrLocation updateUsrLocation26 = new PCRFSoapStub.UpdateUsrLocation();
			PCRFSoapStub.SInUsrLocationParaVO siInSubscriberParaVO = new PCRFSoapStub.SInUsrLocationParaVO();
			PCRFSoapStub.SPccSubscriber sPccSubscriber = new PCRFSoapStub.SPccSubscriber();
			PCRFSoapStub.SUsrLocation sUsrLocation = new PCRFSoapStub.SUsrLocation();
			PCRFSoapStub.SAVP savp = new PCRFSoapStub.SAVP();
			PCRFSoapStub.SAVP savp1 = new PCRFSoapStub.SAVP();
			PCRFSoapStub.SAVP savp2 = new PCRFSoapStub.SAVP();
			PCRFSoapStub.SAVP savp3 = new PCRFSoapStub.SAVP();

			savp.setKey("usrIdentifier");
			if (!StringUtil.isEmpty(prefix_phone)) {
				phoneNo = prefix_phone + phoneNo;
			}
			savp.setValue(phoneNo);
			savp1.setKey("usrSessionPolicyCode");
			savp1.setValue(poloicId);
			savp2.setKey("operateTime");
			savp2.setValue(sdf.format(new Date()));
			savp3.setKey("UsrLocation");
			savp3.setValue(userlocation);
			sPccSubscriber.addAttribute(savp);
			sUsrLocation.addAttribute(savp1);
			sUsrLocation.addAttribute(savp2);
			sUsrLocation.addAttribute(savp3);
			PCRFSoapStub.Header header27 = new PCRFSoapStub.Header();
			header27.setUsername(sprBean.getSprUsr());
			header27.setPassword(sprBean.getSprPwd());
			header27.setAddress(sprBean.getSprIp());
			header27.setTime(sdf.format(new Date()));
			header27.setSerial("" + ((Math.random() * 9 + 1) * 10000000));// 8位随机数
			siInSubscriberParaVO.setSubscriber(sPccSubscriber);
			siInSubscriberParaVO.addSubscribedUsrLocation(sUsrLocation);
			updateUsrLocation26.setInPara(siInSubscriberParaVO);
			try {
				UpdateUsrLocationResponse re = stub.updateUsrLocation(updateUsrLocation26, header27);
				PCRFSoapStub.SReturnVO sReturnVO = re.getResult();
				log.info("result code: " + sReturnVO.getResultCode());
				// 0 成功 
				if (sReturnVO.getResultCode() == 0) {
					result = "0";
				}
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	
	public String deleUserLocation(String phoneNo,String poloicId,SprBusinessBean sprBean){
		String result = "-1";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		//9个小区以字符串数据的形式传入
		try {
			PCRFSoapStub stub = new PCRFSoapStub(sprBean.getSprWsdl());
			stub._getServiceClient().getOptions()
					.setProperty(HTTPConstants.CHUNKED, false);
			PCRFSoapStub.DelUsrLocation delUsrLocation86 = new PCRFSoapStub.DelUsrLocation();
			PCRFSoapStub.SInUsrLocationParaVO siInSubscriberParaVO = new PCRFSoapStub.SInUsrLocationParaVO();
			PCRFSoapStub.SPccSubscriber sPccSubscriber = new PCRFSoapStub.SPccSubscriber();
			PCRFSoapStub.SUsrLocation sUsrLocation = new PCRFSoapStub.SUsrLocation();
			PCRFSoapStub.SAVP savp = new PCRFSoapStub.SAVP();
			PCRFSoapStub.SAVP savp1 = new PCRFSoapStub.SAVP();
			PCRFSoapStub.SAVP savp2 = new PCRFSoapStub.SAVP();

			savp.setKey("usrIdentifier");
			if (!StringUtil.isEmpty(prefix_phone)) {
				phoneNo = prefix_phone + phoneNo;
			}
			savp.setValue(phoneNo);
			savp1.setKey("usrSessionPolicyCode");
			savp1.setValue(poloicId);
			savp2.setKey("operateTime");
			savp2.setValue(sdf.format(new Date()));
			sPccSubscriber.addAttribute(savp);
			sUsrLocation.addAttribute(savp1);
			sUsrLocation.addAttribute(savp2);
			PCRFSoapStub.Header header27 = new PCRFSoapStub.Header();
			header27.setUsername(sprBean.getSprUsr());
			header27.setPassword(sprBean.getSprPwd());
			header27.setAddress(sprBean.getSprIp());
			header27.setTime(sdf.format(new Date()));
			header27.setSerial("" + ((Math.random() * 9 + 1) * 10000000));// 8位随机数
			siInSubscriberParaVO.setSubscriber(sPccSubscriber);
			siInSubscriberParaVO.addSubscribedUsrLocation(sUsrLocation);
			delUsrLocation86.setInPara(siInSubscriberParaVO);
			DelUsrLocationResponse re = stub.delUsrLocation(delUsrLocation86, header27);
			PCRFSoapStub.SReturnVO sReturnVO = re.getResult();
			log.info("result code: " + sReturnVO.getResultCode());
				// 0 成功 
			if (sReturnVO.getResultCode() == 0||sReturnVO.getResultCode() == 110018) {
				result = "0";
			}
		} catch (Exception e) {
			e.printStackTrace();
			return result;
		}
		return result;
	}
	/**
	 * 批量新增用户属性签约信息
	 * 
	 * @param fileName
	 *            文件名
	 * @param operateTime
	 *            操作时间 yyyymmddhhmmss hh为24小时
	 * @return
	 */
	public Map<String, String> addBatSubscriber(String fileName,
			String operateTime, SprBusinessBean sprBean) {
		// String result = "-1";
		Map<String, String> result = new HashMap<String, String>();
		// SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");

		try {
			// URL url = new URL(sprBean.getSprWsdl());
			// PCRFSoap pcrf = new PCRFSoap(url,new QName(
			// "http://www.chinamobile.com/PCRF/", "PCRFSoap"));
			// ScfPccSoapServiceEndpoint scf = pcrf
			// .getChinaMobilePCRFSoapServiceEndpointPort();
			// SInBatFileParaVO sInBatFileParaVO = new SInBatFileParaVO();
			// SPccBatFile sPccBatFile = new SPccBatFile();
			// SAVP savp = new SAVP();
			// savp.setKey("FileName");
			// savp.setValue(fileName);
			//
			// SAVP savp2 = new SAVP();
			// savp2.setKey("operateTime");
			// savp2.setValue(operateTime);
			//
			// sPccBatFile.getAttribute().add(savp);
			// sPccBatFile.getAttribute().add(savp2);
			//
			// sInBatFileParaVO.setBATInfo(sPccBatFile);
			// SReturnVOBAT sReturnVOBAT =
			// scf.addBatSubscriber(sInBatFileParaVO);
			//
			// if ("0".equals(sReturnVOBAT.getResultCode())) {
			// result = "0";
			// }

			PCRFSoapStub stub = new PCRFSoapStub(sprBean.getSprWsdl());
			stub._getServiceClient().getOptions()
					.setProperty(HTTPConstants.CHUNKED, false);

			PCRFSoapStub.AddBatSubscriber addBatSubscriber = new PCRFSoapStub.AddBatSubscriber();
			PCRFSoapStub.SInBatFileParaVO sBatFileParaVO = new PCRFSoapStub.SInBatFileParaVO();
			PCRFSoapStub.SPccBatFile sPccBatFile = new PCRFSoapStub.SPccBatFile();
			PCRFSoapStub.SAVP savp = new PCRFSoapStub.SAVP();
			PCRFSoapStub.SAVP savp2 = new PCRFSoapStub.SAVP();

			savp.setKey("FileName");
			savp.setValue(fileName);
			savp2.setKey("operateTime");
			savp2.setValue(operateTime);

			sPccBatFile.addAttribute(savp);
			sPccBatFile.addAttribute(savp2);

			PCRFSoapStub.Header header48 = new PCRFSoapStub.Header();
			header48.setUsername(sprBean.getSprUsr());
			header48.setPassword(sprBean.getSprPwd());
			header48.setAddress(sprBean.getSprIp());
			header48.setTime(operateTime);
			header48.setSerial("" + ((Math.random() * 9 + 1) * 10000000));// 8位随机数
			sBatFileParaVO.setBATInfo(sPccBatFile);
			addBatSubscriber.setInPara(sBatFileParaVO);
			try {
				PCRFSoapStub.AddBatSubscriberResponse re = stub
						.addBatSubscriber(addBatSubscriber, header48);
				PCRFSoapStub.SReturnVO_BAT sReturnVO = re.getResult();

				PCRFSoapStub.SBatData[] sBatDatas = sReturnVO.getResultData();
				if (sBatDatas.length > 0 && sBatDatas != null) {
					log.info("sBatDatas length: " + sBatDatas.length);
					String TaskID = sBatDatas[0].getAttribute()[0].getValue();
					String Ep_Sn = sBatDatas[0].getAttribute()[1].getValue();

					log.info("result code: " + sReturnVO.getResultCode()
							+ " TaskID: " + TaskID + " Ep_Sn: " + Ep_Sn);
					if (sReturnVO.getResultCode() == 0) {
						result.put("result", "0");
						result.put("TaskID",
								sBatDatas[0].getAttribute()[0].getValue());
						result.put("Ep_Sn",
								sBatDatas[0].getAttribute()[1].getValue());
						result.put("operateTime", operateTime);
					} else {
						result.put("result", "1");
					}
				} else {
					result.put("result", "1");
				}
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}

	/**
	 * 批量修改用户的基本签约信息
	 * 
	 * @param fileName
	 *            文件名
	 * @param operateTime
	 *            操作时间 yyyymmddhhmmss hh为24小时
	 * @return
	 */
	public String updateBatSubscriber(String fileName, String operateTime,
			SprBusinessBean sprBean) {
		String result = "-1";
		try {
			// URL url = new URL(sprBean.getSprWsdl());
			// PCRFSoap pcrf = new PCRFSoap(url,new QName(
			// "http://www.chinamobile.com/PCRF/", "PCRFSoap"));
			// ScfPccSoapServiceEndpoint scf = pcrf
			// .getChinaMobilePCRFSoapServiceEndpointPort();
			// SInBatFileParaVO sInBatFileParaVO = new SInBatFileParaVO();
			// SPccBatFile sPccBatFile = new SPccBatFile();
			// SAVP savp = new SAVP();
			// savp.setKey("FileName");
			// savp.setValue(fileName);
			//
			// SAVP savp2 = new SAVP();
			// savp2.setKey("operateTime");
			// savp2.setValue(operateTime);
			//
			// sPccBatFile.getAttribute().add(savp);
			// sPccBatFile.getAttribute().add(savp2);
			//
			// sInBatFileParaVO.setBATInfo(sPccBatFile);
			// SReturnVOBAT sReturnVOBAT =
			// scf.updateBatSubscriber(sInBatFileParaVO);
			//
			// if ("0".equals(sReturnVOBAT.getResultCode())) {
			// result = "0";
			// }

			PCRFSoapStub stub = new PCRFSoapStub(sprBean.getSprWsdl());
			stub._getServiceClient().getOptions()
					.setProperty(HTTPConstants.CHUNKED, false);

			PCRFSoapStub.UpdateBatSubscriber updateBatSubscriber = new PCRFSoapStub.UpdateBatSubscriber();
			PCRFSoapStub.SInBatFileParaVO sBatFileParaVO = new PCRFSoapStub.SInBatFileParaVO();
			PCRFSoapStub.SPccBatFile sPccBatFile = new PCRFSoapStub.SPccBatFile();
			PCRFSoapStub.SAVP savp = new PCRFSoapStub.SAVP();
			PCRFSoapStub.SAVP savp2 = new PCRFSoapStub.SAVP();

			savp.setKey("FileName");
			savp.setValue(fileName);
			savp2.setKey("operateTime");
			savp2.setValue(operateTime);

			sPccBatFile.addAttribute(savp);
			sPccBatFile.addAttribute(savp2);

			PCRFSoapStub.Header header48 = new PCRFSoapStub.Header();
			header48.setUsername(sprBean.getSprUsr());
			header48.setPassword(sprBean.getSprPwd());
			header48.setAddress(sprBean.getSprIp());
			header48.setTime(operateTime);
			header48.setSerial("" + ((Math.random() * 9 + 1) * 10000000));// 8位随机数
			sBatFileParaVO.setBATInfo(sPccBatFile);
			updateBatSubscriber.setInPara(sBatFileParaVO);
			try {
				PCRFSoapStub.UpdateBatSubscriberResponse re = stub
						.updateBatSubscriber(updateBatSubscriber, header48);
				PCRFSoapStub.SReturnVO_BAT sReturnVO = re.getResult();
				log.info("result code: " + sReturnVO.getResultCode());

				if (sReturnVO.getResultCode() == 0) {
					result = "0";
				}
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}

	/**
	 * 从PCRF/SPR中批量删除用户的基本签约信息
	 * 
	 * @param fileName
	 *            文件名
	 * @param operateTime
	 *            操作时间 yyyymmddhhmmss hh为24小时
	 * @return
	 */
	public String delBatSubscriber(String fileName, String operateTime,
			SprBusinessBean sprBean) {
		String result = "-1";
		try {
			// URL url = new URL(sprBean.getSprWsdl());
			// PCRFSoap pcrf = new PCRFSoap(url,new QName(
			// "http://www.chinamobile.com/PCRF/", "PCRFSoap"));
			// ScfPccSoapServiceEndpoint scf = pcrf
			// .getChinaMobilePCRFSoapServiceEndpointPort();
			// SInBatFileParaVO sInBatFileParaVO = new SInBatFileParaVO();
			// SPccBatFile sPccBatFile = new SPccBatFile();
			// SAVP savp = new SAVP();
			// savp.setKey("FileName");
			// savp.setValue(fileName);
			//
			// SAVP savp2 = new SAVP();
			// savp2.setKey("operateTime");
			// savp2.setValue(operateTime);
			//
			// sPccBatFile.getAttribute().add(savp);
			// sPccBatFile.getAttribute().add(savp2);
			//
			// sInBatFileParaVO.setBATInfo(sPccBatFile);
			// SReturnVOBAT sReturnVOBAT =
			// scf.delBatSubscriber(sInBatFileParaVO);
			//
			// if ("0".equals(sReturnVOBAT.getResultCode())) {
			// result = "0";
			// }

			PCRFSoapStub stub = new PCRFSoapStub(sprBean.getSprWsdl());
			stub._getServiceClient().getOptions()
					.setProperty(HTTPConstants.CHUNKED, false);

			PCRFSoapStub.DelBatSubscriber delBatSubscriber = new PCRFSoapStub.DelBatSubscriber();
			PCRFSoapStub.SInBatFileParaVO sBatFileParaVO = new PCRFSoapStub.SInBatFileParaVO();
			PCRFSoapStub.SPccBatFile sPccBatFile = new PCRFSoapStub.SPccBatFile();
			PCRFSoapStub.SAVP savp = new PCRFSoapStub.SAVP();
			PCRFSoapStub.SAVP savp2 = new PCRFSoapStub.SAVP();

			savp.setKey("FileName");
			savp.setValue(fileName);
			savp2.setKey("operateTime");
			savp2.setValue(operateTime);

			sPccBatFile.addAttribute(savp);
			sPccBatFile.addAttribute(savp2);

			PCRFSoapStub.Header header48 = new PCRFSoapStub.Header();
			header48.setUsername(sprBean.getSprUsr());
			header48.setPassword(sprBean.getSprPwd());
			header48.setAddress(sprBean.getSprIp());
			header48.setTime(operateTime);
			header48.setSerial("" + ((Math.random() * 9 + 1) * 10000000));// 8位随机数
			sBatFileParaVO.setBATInfo(sPccBatFile);
			delBatSubscriber.setInPara(sBatFileParaVO);
			try {
				PCRFSoapStub.DelBatSubscriberResponse re = stub
						.delBatSubscriber(delBatSubscriber, header48);
				PCRFSoapStub.SReturnVO_BAT sReturnVO = re.getResult();
				log.info("result code: " + sReturnVO.getResultCode());

				if (sReturnVO.getResultCode() == 0) {
					result = "0";
				}
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}

	/**
	 * 批量订购业务策略
	 * 
	 * @param fileName
	 *            文件名
	 * @param operateTime
	 *            操作时间 yyyymmddhhmmss hh为24小时
	 * @return
	 */
	public String addBatService(String fileName, String operateTime,
			SprBusinessBean sprBean) {
		String result = "-1";
		try {
			// URL url = new URL(sprBean.getSprWsdl());
			// PCRFSoap pcrf = new PCRFSoap(url,new QName(
			// "http://www.chinamobile.com/PCRF/", "PCRFSoap"));
			// ScfPccSoapServiceEndpoint scf = pcrf
			// .getChinaMobilePCRFSoapServiceEndpointPort();
			// SInBatFileParaVO sInBatFileParaVO = new SInBatFileParaVO();
			// SPccBatFile sPccBatFile = new SPccBatFile();
			// SAVP savp = new SAVP();
			// savp.setKey("FileName");
			// savp.setValue(fileName);
			//
			// SAVP savp2 = new SAVP();
			// savp2.setKey("operateTime");
			// savp2.setValue(operateTime);
			//
			// sPccBatFile.getAttribute().add(savp);
			// sPccBatFile.getAttribute().add(savp2);
			//
			// sInBatFileParaVO.setBATInfo(sPccBatFile);
			// SReturnVOBAT sReturnVOBAT = scf.addBatService(sInBatFileParaVO);
			//
			// if ("0".equals(sReturnVOBAT.getResultCode())) {
			// result = "0";
			// }

			PCRFSoapStub stub = new PCRFSoapStub(sprBean.getSprWsdl());
			stub._getServiceClient().getOptions()
					.setProperty(HTTPConstants.CHUNKED, false);

			PCRFSoapStub.AddBatService addBatService = new PCRFSoapStub.AddBatService();
			PCRFSoapStub.SInBatFileParaVO sBatFileParaVO = new PCRFSoapStub.SInBatFileParaVO();
			PCRFSoapStub.SPccBatFile sPccBatFile = new PCRFSoapStub.SPccBatFile();
			PCRFSoapStub.SAVP savp = new PCRFSoapStub.SAVP();
			PCRFSoapStub.SAVP savp2 = new PCRFSoapStub.SAVP();

			savp.setKey("FileName");
			savp.setValue(fileName);
			savp2.setKey("operateTime");
			savp2.setValue(operateTime);

			sPccBatFile.addAttribute(savp);
			sPccBatFile.addAttribute(savp2);

			PCRFSoapStub.Header header48 = new PCRFSoapStub.Header();
			header48.setUsername(sprBean.getSprUsr());
			header48.setPassword(sprBean.getSprPwd());
			header48.setAddress(sprBean.getSprIp());
			header48.setTime(operateTime);
			header48.setSerial("" + ((Math.random() * 9 + 1) * 10000000));// 8位随机数
			sBatFileParaVO.setBATInfo(sPccBatFile);
			addBatService.setInPara(sBatFileParaVO);
			try {
				PCRFSoapStub.AddBatServiceResponse re = stub.addBatService(
						addBatService, header48);
				PCRFSoapStub.SReturnVO_BAT sReturnVO = re.getResult();
				log.info("result code: " + sReturnVO.getResultCode());

				if (sReturnVO.getResultCode() == 0) {
					result = "0";
				}
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}

	/**
	 * 批量修改用户的业务策略信息
	 * 
	 * @param fileName
	 *            文件名
	 * @param operateTime
	 *            操作时间 yyyymmddhhmmss hh为24小时
	 * @return
	 */
	public String updateBatService(String fileName, String operateTime,
			SprBusinessBean sprBean) {
		String result = "-1";
		try {
			// URL url = new URL(sprBean.getSprWsdl());
			// PCRFSoap pcrf = new PCRFSoap(url,new QName(
			// "http://www.chinamobile.com/PCRF/", "PCRFSoap"));
			// ScfPccSoapServiceEndpoint scf = pcrf
			// .getChinaMobilePCRFSoapServiceEndpointPort();
			// SInBatFileParaVO sInBatFileParaVO = new SInBatFileParaVO();
			// SPccBatFile sPccBatFile = new SPccBatFile();
			// SAVP savp = new SAVP();
			// savp.setKey("FileName");
			// savp.setValue(fileName);
			//
			// SAVP savp2 = new SAVP();
			// savp2.setKey("operateTime");
			// savp2.setValue(operateTime);
			//
			// sPccBatFile.getAttribute().add(savp);
			// sPccBatFile.getAttribute().add(savp2);
			//
			// sInBatFileParaVO.setBATInfo(sPccBatFile);
			// SReturnVOBAT sReturnVOBAT =
			// scf.updateBatService(sInBatFileParaVO);
			//
			// if ("0".equals(sReturnVOBAT.getResultCode())) {
			// result = "0";
			// }

			PCRFSoapStub stub = new PCRFSoapStub(sprBean.getSprWsdl());
			stub._getServiceClient().getOptions()
					.setProperty(HTTPConstants.CHUNKED, false);

			PCRFSoapStub.UpdateBatService updateBatService = new PCRFSoapStub.UpdateBatService();
			PCRFSoapStub.SInBatFileParaVO sBatFileParaVO = new PCRFSoapStub.SInBatFileParaVO();
			PCRFSoapStub.SPccBatFile sPccBatFile = new PCRFSoapStub.SPccBatFile();
			PCRFSoapStub.SAVP savp = new PCRFSoapStub.SAVP();
			PCRFSoapStub.SAVP savp2 = new PCRFSoapStub.SAVP();

			savp.setKey("FileName");
			savp.setValue(fileName);
			savp2.setKey("operateTime");
			savp2.setValue(operateTime);

			sPccBatFile.addAttribute(savp);
			sPccBatFile.addAttribute(savp2);

			PCRFSoapStub.Header header48 = new PCRFSoapStub.Header();
			header48.setUsername(sprBean.getSprUsr());
			header48.setPassword(sprBean.getSprPwd());
			header48.setAddress(sprBean.getSprIp());
			header48.setTime(operateTime);
			header48.setSerial("" + ((Math.random() * 9 + 1) * 10000000));// 8位随机数
			sBatFileParaVO.setBATInfo(sPccBatFile);
			updateBatService.setInPara(sBatFileParaVO);
			try {
				PCRFSoapStub.UpdateBatServiceResponse re = stub
						.updateBatService(updateBatService, header48);
				PCRFSoapStub.SReturnVO_BAT sReturnVO = re.getResult();
				log.info("result code: " + sReturnVO.getResultCode());

				if (sReturnVO.getResultCode() == 0) {
					result = "0";
				}
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}

	/**
	 * 从PCRF/SPR中批量删除用户的业务策略信息
	 * 
	 * @param fileName
	 *            文件名
	 * @param operateTime
	 *            操作时间 yyyymmddhhmmss hh为24小时
	 * @return
	 */
	public String delBatService(String fileName, String operateTime,
			SprBusinessBean sprBean) {
		String result = "-1";
		try {
			// URL url = new URL(sprBean.getSprWsdl());
			// PCRFSoap pcrf = new PCRFSoap(url,new QName(
			// "http://www.chinamobile.com/PCRF/", "PCRFSoap"));
			// ScfPccSoapServiceEndpoint scf = pcrf
			// .getChinaMobilePCRFSoapServiceEndpointPort();
			// SInBatFileParaVO sInBatFileParaVO = new SInBatFileParaVO();
			// SPccBatFile sPccBatFile = new SPccBatFile();
			// SAVP savp = new SAVP();
			// savp.setKey("FileName");
			// savp.setValue(fileName);
			//
			// SAVP savp2 = new SAVP();
			// savp2.setKey("operateTime");
			// savp2.setValue(operateTime);
			//
			// sPccBatFile.getAttribute().add(savp);
			// sPccBatFile.getAttribute().add(savp2);
			//
			// sInBatFileParaVO.setBATInfo(sPccBatFile);
			// SReturnVOBAT sReturnVOBAT = scf.delBatService(sInBatFileParaVO);
			//
			// if ("0".equals(sReturnVOBAT.getResultCode())) {
			// result = "0";
			// }

			PCRFSoapStub stub = new PCRFSoapStub(sprBean.getSprWsdl());
			stub._getServiceClient().getOptions()
					.setProperty(HTTPConstants.CHUNKED, false);

			PCRFSoapStub.DelBatService delBatService = new PCRFSoapStub.DelBatService();
			PCRFSoapStub.SInBatFileParaVO sBatFileParaVO = new PCRFSoapStub.SInBatFileParaVO();
			PCRFSoapStub.SPccBatFile sPccBatFile = new PCRFSoapStub.SPccBatFile();
			PCRFSoapStub.SAVP savp = new PCRFSoapStub.SAVP();
			PCRFSoapStub.SAVP savp2 = new PCRFSoapStub.SAVP();

			savp.setKey("FileName");
			savp.setValue(fileName);
			savp2.setKey("operateTime");
			savp2.setValue(operateTime);

			sPccBatFile.addAttribute(savp);
			sPccBatFile.addAttribute(savp2);

			PCRFSoapStub.Header header48 = new PCRFSoapStub.Header();
			header48.setUsername(sprBean.getSprUsr());
			header48.setPassword(sprBean.getSprPwd());
			header48.setAddress(sprBean.getSprIp());
			header48.setTime(operateTime);
			header48.setSerial("" + ((Math.random() * 9 + 1) * 10000000));// 8位随机数
			sBatFileParaVO.setBATInfo(sPccBatFile);
			delBatService.setInPara(sBatFileParaVO);
			try {
				PCRFSoapStub.DelBatServiceResponse re = stub.delBatService(
						delBatService, header48);
				PCRFSoapStub.SReturnVO_BAT sReturnVO = re.getResult();
				log.info("result code: " + sReturnVO.getResultCode());

				if (sReturnVO.getResultCode() == 0) {
					result = "0";
				}
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}

	/**
	 * 批量订购用户策略
	 * 
	 * @param fileName
	 *            文件名
	 * @param operateTime
	 *            操作时间 yyyymmddhhmmss hh为24小时
	 * @return
	 */
	public String addBatUsrSessionPolicy(String fileName, String operateTime,
			SprBusinessBean sprBean) {
		String result = "-1";
		try {
			// URL url = new URL(sprBean.getSprWsdl());
			// PCRFSoap pcrf = new PCRFSoap(url,new QName(
			// "http://www.chinamobile.com/PCRF/", "PCRFSoap"));
			// ScfPccSoapServiceEndpoint scf = pcrf
			// .getChinaMobilePCRFSoapServiceEndpointPort();
			// SInBatFileParaVO sInBatFileParaVO = new SInBatFileParaVO();
			// SPccBatFile sPccBatFile = new SPccBatFile();
			// SAVP savp = new SAVP();
			// savp.setKey("FileName");
			// savp.setValue(fileName);
			//
			// SAVP savp2 = new SAVP();
			// savp2.setKey("operateTime");
			// savp2.setValue(operateTime);
			//
			// sPccBatFile.getAttribute().add(savp);
			// sPccBatFile.getAttribute().add(savp2);
			//
			// sInBatFileParaVO.setBATInfo(sPccBatFile);
			// SReturnVOBAT sReturnVOBAT = scf
			// .addBatUsrSessionPolicy(sInBatFileParaVO);
			//
			// if ("0".equals(sReturnVOBAT.getResultCode())) {
			// result = "0";
			// }

			PCRFSoapStub stub = new PCRFSoapStub(sprBean.getSprWsdl());
			stub._getServiceClient().getOptions()
					.setProperty(HTTPConstants.CHUNKED, false);

			PCRFSoapStub.AddBatUsrSessionPolicy addBatUsrSessionPolicy = new PCRFSoapStub.AddBatUsrSessionPolicy();
			PCRFSoapStub.SInBatFileParaVO sBatFileParaVO = new PCRFSoapStub.SInBatFileParaVO();
			PCRFSoapStub.SPccBatFile sPccBatFile = new PCRFSoapStub.SPccBatFile();
			PCRFSoapStub.SAVP savp = new PCRFSoapStub.SAVP();
			PCRFSoapStub.SAVP savp2 = new PCRFSoapStub.SAVP();

			savp.setKey("FileName");
			savp.setValue(fileName);
			savp2.setKey("operateTime");
			savp2.setValue(operateTime);

			sPccBatFile.addAttribute(savp);
			sPccBatFile.addAttribute(savp2);

			PCRFSoapStub.Header header48 = new PCRFSoapStub.Header();
			header48.setUsername(sprBean.getSprUsr());
			header48.setPassword(sprBean.getSprPwd());
			header48.setAddress(sprBean.getSprIp());
			header48.setTime(operateTime);
			header48.setSerial("" + ((Math.random() * 9 + 1) * 10000000));// 8位随机数
			sBatFileParaVO.setBATInfo(sPccBatFile);
			addBatUsrSessionPolicy.setInPara(sBatFileParaVO);
			try {
				PCRFSoapStub.AddBatUsrSessionPolicyResponse re = stub
						.addBatUsrSessionPolicy(addBatUsrSessionPolicy,
								header48);
				PCRFSoapStub.SReturnVO_BAT sReturnVO = re.getResult();
				log.info("result code: " + sReturnVO.getResultCode());

				if (sReturnVO.getResultCode() == 0) {
					result = "0";
				}
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}

	/**
	 * 批量修改用户的用户策略信息
	 * 
	 * @param fileName
	 *            文件名
	 * @param operateTime
	 *            操作时间 yyyymmddhhmmss hh为24小时
	 * @return
	 */
	public String updateBatUsrSessionPolicy(String fileName,
			String operateTime, SprBusinessBean sprBean) {
		String result = "-1";
		try {
			// URL url = new URL(sprBean.getSprWsdl());
			// PCRFSoap pcrf = new PCRFSoap(url,new QName(
			// "http://www.chinamobile.com/PCRF/", "PCRFSoap"));
			// ScfPccSoapServiceEndpoint scf = pcrf
			// .getChinaMobilePCRFSoapServiceEndpointPort();
			// SInBatFileParaVO sInBatFileParaVO = new SInBatFileParaVO();
			// SPccBatFile sPccBatFile = new SPccBatFile();
			// SAVP savp = new SAVP();
			// savp.setKey("FileName");
			// savp.setValue(fileName);
			//
			// SAVP savp2 = new SAVP();
			// savp2.setKey("operateTime");
			// savp2.setValue(operateTime);
			//
			// sPccBatFile.getAttribute().add(savp);
			// sPccBatFile.getAttribute().add(savp2);
			//
			// sInBatFileParaVO.setBATInfo(sPccBatFile);
			// SReturnVOBAT sReturnVOBAT = scf
			// .updateBatUsrSessionPolicy(sInBatFileParaVO);
			//
			// if ("0".equals(sReturnVOBAT.getResultCode())) {
			// result = "0";
			// }

			PCRFSoapStub stub = new PCRFSoapStub(sprBean.getSprWsdl());
			stub._getServiceClient().getOptions()
					.setProperty(HTTPConstants.CHUNKED, false);

			PCRFSoapStub.UpdateBatUsrSessionPolicy updateBatUsrSessionPolicy = new PCRFSoapStub.UpdateBatUsrSessionPolicy();
			PCRFSoapStub.SInBatFileParaVO sBatFileParaVO = new PCRFSoapStub.SInBatFileParaVO();
			PCRFSoapStub.SPccBatFile sPccBatFile = new PCRFSoapStub.SPccBatFile();
			PCRFSoapStub.SAVP savp = new PCRFSoapStub.SAVP();
			PCRFSoapStub.SAVP savp2 = new PCRFSoapStub.SAVP();

			savp.setKey("FileName");
			savp.setValue(fileName);
			savp2.setKey("operateTime");
			savp2.setValue(operateTime);

			sPccBatFile.addAttribute(savp);
			sPccBatFile.addAttribute(savp2);

			PCRFSoapStub.Header header48 = new PCRFSoapStub.Header();
			header48.setUsername(sprBean.getSprUsr());
			header48.setPassword(sprBean.getSprPwd());
			header48.setAddress(sprBean.getSprIp());
			header48.setTime(operateTime);
			header48.setSerial("" + ((Math.random() * 9 + 1) * 10000000));// 8位随机数
			sBatFileParaVO.setBATInfo(sPccBatFile);
			updateBatUsrSessionPolicy.setInPara(sBatFileParaVO);
			try {
				PCRFSoapStub.UpdateBatUsrSessionPolicyResponse re = stub
						.updateBatUsrSessionPolicy(updateBatUsrSessionPolicy,
								header48);
				PCRFSoapStub.SReturnVO_BAT sReturnVO = re.getResult();
				log.info("result code: " + sReturnVO.getResultCode());

				if (sReturnVO.getResultCode() == 0) {
					result = "0";
				}
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}

	/**
	 * 从PCRF/SPR中批量删除用户的用户策略信息
	 * 
	 * @param fileName
	 *            文件名
	 * @param operateTime
	 *            操作时间 yyyymmddhhmmss hh为24小时
	 * @return
	 */
	public String delBatUsrSessionPolicy(String fileName, String operateTime,
			SprBusinessBean sprBean) {
		String result = "-1";
		try {
			// URL url = new URL(sprBean.getSprWsdl());
			// PCRFSoap pcrf = new PCRFSoap(url,new QName(
			// "http://www.chinamobile.com/PCRF/", "PCRFSoap"));
			// ScfPccSoapServiceEndpoint scf = pcrf
			// .getChinaMobilePCRFSoapServiceEndpointPort();
			// SInBatFileParaVO sInBatFileParaVO = new SInBatFileParaVO();
			// SPccBatFile sPccBatFile = new SPccBatFile();
			// SAVP savp = new SAVP();
			// savp.setKey("FileName");
			// savp.setValue(fileName);
			//
			// SAVP savp2 = new SAVP();
			// savp2.setKey("operateTime");
			// savp2.setValue(operateTime);
			//
			// sPccBatFile.getAttribute().add(savp);
			// sPccBatFile.getAttribute().add(savp2);
			//
			// sInBatFileParaVO.setBATInfo(sPccBatFile);
			// SReturnVOBAT sReturnVOBAT = scf
			// .delBatUsrSessionPolicy(sInBatFileParaVO);
			//
			// if ("0".equals(sReturnVOBAT.getResultCode())) {
			// result = "0";
			// }

			PCRFSoapStub stub = new PCRFSoapStub(sprBean.getSprWsdl());
			stub._getServiceClient().getOptions()
					.setProperty(HTTPConstants.CHUNKED, false);

			PCRFSoapStub.DelBatUsrSessionPolicy delBatUsrSessionPolicy = new PCRFSoapStub.DelBatUsrSessionPolicy();
			PCRFSoapStub.SInBatFileParaVO sBatFileParaVO = new PCRFSoapStub.SInBatFileParaVO();
			PCRFSoapStub.SPccBatFile sPccBatFile = new PCRFSoapStub.SPccBatFile();
			PCRFSoapStub.SAVP savp = new PCRFSoapStub.SAVP();
			PCRFSoapStub.SAVP savp2 = new PCRFSoapStub.SAVP();

			savp.setKey("FileName");
			savp.setValue(fileName);
			savp2.setKey("operateTime");
			savp2.setValue(operateTime);

			sPccBatFile.addAttribute(savp);
			sPccBatFile.addAttribute(savp2);

			PCRFSoapStub.Header header48 = new PCRFSoapStub.Header();
			header48.setUsername(sprBean.getSprUsr());
			header48.setPassword(sprBean.getSprPwd());
			header48.setAddress(sprBean.getSprIp());
			header48.setTime(operateTime);
			header48.setSerial("" + ((Math.random() * 9 + 1) * 10000000));// 8位随机数
			sBatFileParaVO.setBATInfo(sPccBatFile);
			delBatUsrSessionPolicy.setInPara(sBatFileParaVO);
			try {
				PCRFSoapStub.DelBatUsrSessionPolicyResponse re = stub
						.delBatUsrSessionPolicy(delBatUsrSessionPolicy,
								header48);
				PCRFSoapStub.SReturnVO_BAT sReturnVO = re.getResult();
				log.info("result code: " + sReturnVO.getResultCode());

				if (sReturnVO.getResultCode() == 0) {
					result = "0";
				}
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}

	/**
	 * 批量操作任务执行状态查询接口
	 * 
	 * @param TaskID
	 *            任务ID
	 * @param Ep_Sn
	 *            设备名称
	 * @return
	 */
	public String PCRF_CHKBAT(String TaskID, String Ep_Sn,
			SprBusinessBean sprBean, String operateTime) {
		log.info("taskid: " + TaskID + " Ep_Sn: " + Ep_Sn);
		String result = "-1";
		try {
			PCRFSoapStub stub = new PCRFSoapStub(sprBean.getSprWsdl());
			stub._getServiceClient().getOptions()
					.setProperty(HTTPConstants.CHUNKED, false);
			PCRFSoapStub.PCRF_CHKBAT pChkbat = new PCRFSoapStub.PCRF_CHKBAT();
			PCRFSoapStub.SInBatFileParaVO sBatFileParaVO = new PCRFSoapStub.SInBatFileParaVO();
			PCRFSoapStub.SPccBatFile sBatFile = new PCRFSoapStub.SPccBatFile();
			PCRFSoapStub.SAVP savp1 = new PCRFSoapStub.SAVP();
			PCRFSoapStub.SAVP savp2 = new PCRFSoapStub.SAVP();
			savp1.setKey("TaskID");
			savp1.setValue(TaskID);

			savp2.setKey("Ep_Sn");
			savp2.setValue(Ep_Sn);
			sBatFile.addAttribute(savp1);
			sBatFile.addAttribute(savp2);
			sBatFileParaVO.setBATInfo(sBatFile);
			pChkbat.setInPara(sBatFileParaVO);

			PCRFSoapStub.Header header48 = new PCRFSoapStub.Header();
			header48.setUsername(sprBean.getSprUsr());
			header48.setPassword(sprBean.getSprPwd());
			header48.setAddress(sprBean.getSprIp());
			header48.setTime(operateTime);
			header48.setSerial("" + ((Math.random() * 9 + 1) * 10000000));// 8位随机数
			PCRFSoapStub.PCRF_CHKBATResponse pChkbatResponse = stub
					.pCRF_CHKBAT(pChkbat, header48);
			PCRFSoapStub.SReturnVO_BAT pcReturnVOBAT = pChkbatResponse
					.getResult();
			log.info("PCRF_CHKBAT result code: "
					+ pcReturnVOBAT.getResultCode());

			if (pcReturnVOBAT.getResultCode() == 0) {
				PCRFSoapStub.SBatData[] sBatDatas = pcReturnVOBAT
						.getResultData();
				if (sBatDatas != null && sBatDatas.length > 0) {
					PCRFSoapStub.SAVP[] savps = sBatDatas[0].getAttribute();
					if (savps != null && savps.length > 0) {
						for (int i = 0; i < savps.length; i++) {
							if ("STATUS".equals(savps[i].getKey())) {
								String status = savps[i].getValue();
								log.info("status : " + status);
								if ("COMPLETED".equals(status)) {
									result = "0";
								}
							}
						}
					}
				}
			} else {
				result = pcReturnVOBAT.getResultCode() + "";
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}

	/**
	 * 通过JDBC连接数据库查询批量SPR参数
	 * 
	 * @param tableName
	 *            表名
	 * @return
	 */
	public List<SprBusinessBean> getProcessInfos(String tableName) {
		List<SprBusinessBean> rsList = new ArrayList<SprBusinessBean>();
		HashSet<String> deviceIps = new HashSet<String>();

		Connection conn = null;

		String sql;

		// MySQL的JDBC URL编写方式：jdbc:mysql://主机名称：连接端口/数据库的名称?参数=值

		// 避免中文乱码要指定useUnicode和characterEncoding

		// 执行数据库操作之前要在数据库管理系统上创建一个数据库，名字自己定，

		// 下面语句之前就要先创建javademo数据库

		String url = PropUtil.getProp("url", "mysql.properties");
		// String url = PropUtil.getProp("local_url", "mysql.properties");
		// //本地测试用

		try {

			// 之所以要使用下面这条语句，是因为要使用MySQL的驱动，所以我们要把它驱动起来，

			// 可以通过Class.forName把它加载进去，也可以通过初始化来驱动起来，下面三种形式都可以

			Class.forName("com.mysql.jdbc.Driver");// 动态加载mysql驱动

			// or:

			// com.mysql.jdbc.Driver driver = new com.mysql.jdbc.Driver();

			// or：

			// new com.mysql.jdbc.Driver();

			System.out.println("成功加载MySQL驱动程序");

			// 一个Connection代表一个数据库连接

			conn = DriverManager.getConnection(url);

			Statement stmt = conn.createStatement();

			sql = "select t1.PRODUCT_NO,t3.spr_name,t3.SPR_IP,t3.SPR_WSDL,t3.SPR_FTP_ADDRESS,t3.SPR_FTP_USR,t3.SPR_FTP_PWD,t2.ISSFTP,t3.SPR_USR,t3.SPR_PWD "
					+ "from "
					+ tableName
					+ " t1 "
					+ "left join POP_SPR_SECTION t2 on substring(t1.product_no,1,7) = t2.SECTION_NO "
					+ "left join POP_SPR_PROPERTY t3 on (case when t2.isbase = '0' then t2.BASE_SPR_NAME else t2.BACKUP_SPR_NAME end) = t3.SPR_NAME;";

			log.info("select sql : " + sql);
			ResultSet rs = stmt.executeQuery(sql);
			List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
			ResultSetMetaData md = rs.getMetaData(); // 获得结果集结构信息,元数据
			int columnCount = md.getColumnCount(); // 获得列数

			// 结果集封装到list存储
			while (rs.next()) {
				Map<String, Object> rowData = new HashMap<String, Object>();
				for (int i = 1; i <= columnCount; i++) {
					rowData.put(md.getColumnName(i), rs.getObject(i));
				}
				list.add(rowData);
			}

			if (list != null && list.size() > 0) {
				for (int i = 0; i < list.size(); i++) {
					Map<String, Object> rowData = list.get(i);
					if (rowData.get("SPR_IP") != null) {
						deviceIps.add(String.valueOf(rowData.get("SPR_IP")));
					}
				}
			}

			// 添加主SPR数据
			if (deviceIps != null && deviceIps.size() > 0) {
				for (String ip : deviceIps) {
					SprBusinessBean sprBean = new SprBusinessBean();
					sprBean.setSprIp(ip);
					List<String> phoneList = new ArrayList<String>();
					if (list != null && list.size() > 0) {
						for (int i = 0; i < list.size(); i++) {
							Map<String, Object> rowData = list.get(i);
							if (ip.equals(rowData.get("SPR_IP"))
									&& rowData.get("SPR_IP") != null) {
								sprBean.setFtpAddress(String.valueOf(rowData
										.get("SPR_FTP_ADDRESS")));
								sprBean.setFtpUsr(String.valueOf(rowData
										.get("SPR_FTP_USR")));
								sprBean.setFtpPwd(String.valueOf(rowData
										.get("SPR_FTP_PWD")));
								sprBean.setSprName(String.valueOf(rowData
										.get("SPR_NAME")));
								sprBean.setSprWsdl(String.valueOf(rowData
										.get("SPR_WSDL")));
								sprBean.setIsSftp(String.valueOf(rowData
										.get("ISSFTP")));
								sprBean.setSprUsr(String.valueOf(rowData
										.get("SPR_USR")));
								sprBean.setSprPwd(String.valueOf(rowData
										.get("SPR_PWD")));
								phoneList.add(String.valueOf(rowData
										.get("product_no")));
							}
						}
						sprBean.setPhoneList(phoneList);
					}
					rsList.add(sprBean);
				}
			}
		} catch (SQLException e) {

			log.info("MySQL操作错误");

			e.printStackTrace();

		} catch (Exception e) {

			e.printStackTrace();

		} finally {

			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}

		}
		return rsList;
	}

	/**
	 * 通过手机号获取该号段的SPR接口必要信息
	 * 
	 * @param phone
	 * @return sprBean
	 */
	public SprBusinessBean getSprBean(String phone) throws SQLException {
		SprBusinessBean sprBean = new SprBusinessBean();
		Connection conn = null;

		String sql;

		// MySQL的JDBC URL编写方式：jdbc:mysql://主机名称：连接端口/数据库的名称?参数=值

		// 避免中文乱码要指定useUnicode和characterEncoding

		// 执行数据库操作之前要在数据库管理系统上创建一个数据库，名字自己定，

		// 下面语句之前就要先创建javademo数据库

		String url = PropUtil.getProp("url", "mysql.properties");
		//String url = PropUtil.getProp("local_url", "mysql.properties");
		// //本地测试用

		try {

			// 之所以要使用下面这条语句，是因为要使用MySQL的驱动，所以我们要把它驱动起来，

			// 可以通过Class.forName把它加载进去，也可以通过初始化来驱动起来，下面三种形式都可以

			Class.forName("com.mysql.jdbc.Driver");// 动态加载mysql驱动

			// or:

			// com.mysql.jdbc.Driver driver = new com.mysql.jdbc.Driver();

			// or：

			// new com.mysql.jdbc.Driver();


			// 一个Connection代表一个数据库连接

			conn = DriverManager.getConnection(url);
			System.out.println("成功加载MySQL驱动程序");

			Statement stmt = conn.createStatement();

			sql = "select t2.spr_name,t2.SPR_IP,t2.SPR_WSDL,t2.SPR_FTP_ADDRESS,t2.SPR_FTP_USR,t2.SPR_FTP_PWD,t2.SPR_USR,t2.SPR_PWD "
					+ "from POP_SPR_SECTION t1 "
					+ "left join POP_SPR_PROPERTY t2 on (case when t1.isbase = '0' then t1.BASE_SPR_NAME else t1.BACKUP_SPR_NAME end) = t2.SPR_NAME "
					+ "where substring(" + phone + ",1,7) = t1.SECTION_NO;";
			System.out.println(sql);
			ResultSet rs = stmt.executeQuery(sql);

			while (rs.next()) {
				sprBean.setSprName(rs.getString("SPR_NAME"));
				sprBean.setSprIp(rs.getString("SPR_IP"));
				sprBean.setSprWsdl(rs.getString("SPR_WSDL"));
				sprBean.setFtpAddress(rs.getString("SPR_FTP_ADDRESS"));
				sprBean.setFtpUsr(rs.getString("SPR_FTP_USR"));
				sprBean.setFtpPwd(rs.getString("SPR_FTP_PWD"));
				sprBean.setSprUsr(rs.getString("SPR_USR"));
				sprBean.setSprPwd(rs.getString("SPR_PWD"));
				log.info("SPR_NAME: " + sprBean.getSprName() + " | SPR_IP: "
						+ sprBean.getSprIp() + " | SPR_WSDL: "
						+ sprBean.getSprWsdl() + " | SPR_FTP_ADDRESS: "
						+ sprBean.getFtpAddress() + " | SPR_FTP_USR: "
						+ sprBean.getFtpUsr() + " | SPR_FTP_PWD: "
						+ sprBean.getFtpPwd() + " | SPR_USR: "
						+ sprBean.getSprUsr() + " | SPR_PWD: "
						+ sprBean.getSprPwd());
			}

		} catch (SQLException e) {

			log.info("MySQL操作错误");
			
			e.printStackTrace();
			throw new SQLException("MySQL操作错误");
		} catch (Exception e) {

			e.printStackTrace();

		} finally {

			try {
				if(conn!=null)
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}

		}
		return sprBean;
	}

	
	/**
	 * 通过手机号获取该号段的SPR接口必要信息
	 * 
	 * @param phone
	 * @return sprBean
	 */
	public List<SprBusinessBean> getSprBeans(String phone) throws SQLException {
		List<SprBusinessBean> list= new ArrayList<SprBusinessBean>();
		SprBusinessBean sprBean = null;
		Connection conn = null;

		String sql;

		String url = PropUtil.getProp("url", "mysql.properties");
		//String url = PropUtil.getProp("local_url", "mysql.properties");
		// //本地测试用

		try {

			Class.forName("com.mysql.jdbc.Driver");// 动态加载mysql驱动

			
			conn = DriverManager.getConnection(url);
			System.out.println("成功加载MySQL驱动程序");

			Statement stmt = conn.createStatement();

			sql = "select t2.spr_name,t2.SPR_IP,t2.SPR_WSDL,t2.SPR_FTP_ADDRESS,t2.SPR_FTP_USR,t2.SPR_FTP_PWD,t2.SPR_USR,t2.SPR_PWD "
					+ "from  (SELECT section_no , base_spr_name spr_name FROM POP_SPR_SECTION UNION ALL SELECT section_no, backup_spr_name spr_name FROM  POP_SPR_SECTION)  t1 " 
					+ "left join POP_SPR_PROPERTY t2 on t1.SPR_NAME = t2.SPR_NAME "
					+ "where substring(" + phone + ",1,7) = t1.SECTION_NO;";
			System.out.println(sql);
			ResultSet rs = stmt.executeQuery(sql);

			while (rs.next()) {
				sprBean = new SprBusinessBean();
				sprBean.setSprName(rs.getString("SPR_NAME"));
				sprBean.setSprIp(rs.getString("SPR_IP"));
				sprBean.setSprWsdl(rs.getString("SPR_WSDL"));
				sprBean.setFtpAddress(rs.getString("SPR_FTP_ADDRESS"));
				sprBean.setFtpUsr(rs.getString("SPR_FTP_USR"));
				sprBean.setFtpPwd(rs.getString("SPR_FTP_PWD"));
				sprBean.setSprUsr(rs.getString("SPR_USR"));
				sprBean.setSprPwd(rs.getString("SPR_PWD"));
		/*		log.info("SPR_NAME: " + sprBean.getSprName() + " | SPR_IP: "
						+ sprBean.getSprIp() + " | SPR_WSDL: "
						+ sprBean.getSprWsdl() + " | SPR_FTP_ADDRESS: "
						+ sprBean.getFtpAddress() + " | SPR_FTP_USR: "
						+ sprBean.getFtpUsr() + " | SPR_FTP_PWD: "
						+ sprBean.getFtpPwd() + " | SPR_USR: "
						+ sprBean.getSprUsr() + " | SPR_PWD: "
						+ sprBean.getSprPwd());*/
				list.add(sprBean);
			}

		} catch (SQLException e) {

			log.info("MySQL操作错误");
			
			e.printStackTrace();
			throw new SQLException("MySQL操作错误");
		} catch (Exception e) {

			e.printStackTrace();

		} finally {

			try {
				if(conn!=null)
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}

		}
		return list;
	}

	
	/**
	 * 插入查询任务状态信息记录
	 * 
	 * @param phone
	 * @return sprBean
	 */
	public int insertTaskInfo(Map<String, String> returnMap,
			SprBusinessBean sprBusinessBean, String policyType, String batchNo) {
		Connection conn = null;
		int result = 0;
		String sql;

		// MySQL的JDBC URL编写方式：jdbc:mysql://主机名称：连接端口/数据库的名称?参数=值

		// 避免中文乱码要指定useUnicode和characterEncoding

		// 执行数据库操作之前要在数据库管理系统上创建一个数据库，名字自己定，

		// 下面语句之前就要先创建javademo数据库

		String url = PropUtil.getProp("url", "mysql.properties");
		// String url = PropUtil.getProp("local_url", "mysql.properties");
		// //本地测试用

		try {

			// 之所以要使用下面这条语句，是因为要使用MySQL的驱动，所以我们要把它驱动起来，

			// 可以通过Class.forName把它加载进去，也可以通过初始化来驱动起来，下面三种形式都可以

			Class.forName("com.mysql.jdbc.Driver");// 动态加载mysql驱动

			// or:

			// com.mysql.jdbc.Driver driver = new com.mysql.jdbc.Driver();

			// or：

			// new com.mysql.jdbc.Driver();

			System.out.println("成功加载MySQL驱动程序");

			// 一个Connection代表一个数据库连接

			conn = DriverManager.getConnection(url);

			Statement stmt = conn.createStatement();

			sql = "insert into POP_SUBSCRIBER_TASK(TASKID,EP_SN,OPERATE_TIME,SPR_WSDL,SPR_USR,SPR_PWD,SPR_IP,POLICY_TYPE,BATCH_NO) "
					+ "values ('"
					+ returnMap.get("TaskID")
					+ "','"
					+ returnMap.get("Ep_Sn")
					+ "','"
					+ returnMap.get("operateTime")
					+ "','"
					+ sprBusinessBean.getSprWsdl()
					+ "','"
					+ sprBusinessBean.getSprUsr()
					+ "','"
					+ sprBusinessBean.getSprPwd()
					+ "','"
					+ sprBusinessBean.getSprIp()
					+ "','"
					+ policyType
					+ "','"
					+ batchNo + "');";
			log.info("insert sql: " + sql);
			result = stmt.executeUpdate(sql);

		} catch (SQLException e) {

			log.info("MySQL操作错误");

			e.printStackTrace();

		} catch (Exception e) {

			e.printStackTrace();

		} finally {

			try {
				if(conn!=null)
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}

		}
		return result;
	}

	/**
	 * 插入批次文件信息记录
	 * 
	 * @param phone
	 * @return sprBean
	 */
	public int insertBatchInfo(String batchNo, String fileName, String oprTime) {
		Connection conn = null;
		int result = 0;
		String sql;

		// MySQL的JDBC URL编写方式：jdbc:mysql://主机名称：连接端口/数据库的名称?参数=值

		// 避免中文乱码要指定useUnicode和characterEncoding

		// 执行数据库操作之前要在数据库管理系统上创建一个数据库，名字自己定，

		// 下面语句之前就要先创建javademo数据库

		String url = PropUtil.getProp("url", "mysql.properties");
		// String url = PropUtil.getProp("local_url", "mysql.properties");
		// //本地测试用

		try {

			// 之所以要使用下面这条语句，是因为要使用MySQL的驱动，所以我们要把它驱动起来，

			// 可以通过Class.forName把它加载进去，也可以通过初始化来驱动起来，下面三种形式都可以

			Class.forName("com.mysql.jdbc.Driver");// 动态加载mysql驱动

			// or:

			// com.mysql.jdbc.Driver driver = new com.mysql.jdbc.Driver();

			// or：

			// new com.mysql.jdbc.Driver();

			System.out.println("成功加载MySQL驱动程序");

			// 一个Connection代表一个数据库连接

			conn = DriverManager.getConnection(url);

			Statement stmt = conn.createStatement();

			sql = "insert into POP_SUBSCRIBER_BATCH_INFO(BATCH_NO,FILE_NAME,OPERATE_TIME) "
					+ "values ('"
					+ batchNo
					+ "','"
					+ fileName
					+ "','"
					+ oprTime + "');";
			log.info("insert sql: " + sql);
			result = stmt.executeUpdate(sql);

		} catch (SQLException e) {

			log.info("MySQL操作错误");

			e.printStackTrace();

		} catch (Exception e) {

			e.printStackTrace();

		} finally {

			try {
				if(conn!=null)
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}

		}
		return result;
	}

	/**
	 * 删除任务记录
	 * 
	 * @param phone
	 * @return sprBean
	 */
	public int deleteTaskInfo(String batchNo) {
		Connection conn = null;
		int result = 0;
		String sql;

		// MySQL的JDBC URL编写方式：jdbc:mysql://主机名称：连接端口/数据库的名称?参数=值

		// 避免中文乱码要指定useUnicode和characterEncoding

		// 执行数据库操作之前要在数据库管理系统上创建一个数据库，名字自己定，

		// 下面语句之前就要先创建javademo数据库

		String url = PropUtil.getProp("url", "mysql.properties");
		// String url = PropUtil.getProp("local_url", "mysql.properties");
		// //本地测试用

		try {

			// 之所以要使用下面这条语句，是因为要使用MySQL的驱动，所以我们要把它驱动起来，

			// 可以通过Class.forName把它加载进去，也可以通过初始化来驱动起来，下面三种形式都可以

			Class.forName("com.mysql.jdbc.Driver");// 动态加载mysql驱动

			// or:

			// com.mysql.jdbc.Driver driver = new com.mysql.jdbc.Driver();

			// or：

			// new com.mysql.jdbc.Driver();

			System.out.println("成功加载MySQL驱动程序");

			// 一个Connection代表一个数据库连接

			conn = DriverManager.getConnection(url);

			Statement stmt = conn.createStatement();

			sql = "delete from POP_SUBSCRIBER_TASK where BATCH_NO ='" + batchNo
					+ "';";
			log.info("delete sql: " + sql);
			result = stmt.executeUpdate(sql);

		} catch (SQLException e) {

			log.info("MySQL操作错误");

			e.printStackTrace();

		} catch (Exception e) {

			e.printStackTrace();

		} finally {

			try {
				if(conn!=null)
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}

		}
		return result;
	}

	/**
	 * 删除批次记录
	 * 
	 * @param phone
	 * @return sprBean
	 */
	public int deleteBatchInfo(String fileName) {
		Connection conn = null;
		int result = 0;
		String sql;

		// MySQL的JDBC URL编写方式：jdbc:mysql://主机名称：连接端口/数据库的名称?参数=值

		// 避免中文乱码要指定useUnicode和characterEncoding

		// 执行数据库操作之前要在数据库管理系统上创建一个数据库，名字自己定，

		// 下面语句之前就要先创建javademo数据库

		String url = PropUtil.getProp("url", "mysql.properties");
		// String url = PropUtil.getProp("local_url", "mysql.properties");
		// //本地测试用

		try {

			// 之所以要使用下面这条语句，是因为要使用MySQL的驱动，所以我们要把它驱动起来，

			// 可以通过Class.forName把它加载进去，也可以通过初始化来驱动起来，下面三种形式都可以

			Class.forName("com.mysql.jdbc.Driver");// 动态加载mysql驱动

			// or:

			// com.mysql.jdbc.Driver driver = new com.mysql.jdbc.Driver();

			// or：

			// new com.mysql.jdbc.Driver();

			System.out.println("成功加载MySQL驱动程序");

			// 一个Connection代表一个数据库连接

			conn = DriverManager.getConnection(url);

			Statement stmt = conn.createStatement();

			sql = "delete from POP_SUBSCRIBER_BATCH_INFO where FILE_NAME ='"
					+ fileName + "';";
			log.info("delete sql: " + sql);
			result = stmt.executeUpdate(sql);

		} catch (SQLException e) {

			log.info("MySQL操作错误");

			e.printStackTrace();

		} catch (Exception e) {

			e.printStackTrace();

		} finally {

			try {
				if(conn!=null)
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}

		}
		return result;
	}

	/**
	 * 获取当前POP_SUBSCRIBER_TASK表中的数据
	 * 
	 * @return
	 */
	public List<SprBusinessBean> getTaskInfo() {
		List<SprBusinessBean> list = new ArrayList<SprBusinessBean>();
		Connection conn = null;
		String sql;

		// MySQL的JDBC URL编写方式：jdbc:mysql://主机名称：连接端口/数据库的名称?参数=值

		// 避免中文乱码要指定useUnicode和characterEncoding

		// 执行数据库操作之前要在数据库管理系统上创建一个数据库，名字自己定，

		// 下面语句之前就要先创建javademo数据库

		String url = PropUtil.getProp("url", "mysql.properties");
		// String url = PropUtil.getProp("local_url", "mysql.properties");
		// //本地测试用

		try {

			// 之所以要使用下面这条语句，是因为要使用MySQL的驱动，所以我们要把它驱动起来，

			// 可以通过Class.forName把它加载进去，也可以通过初始化来驱动起来，下面三种形式都可以

			Class.forName("com.mysql.jdbc.Driver");// 动态加载mysql驱动

			// or:

			// com.mysql.jdbc.Driver driver = new com.mysql.jdbc.Driver();

			// or：

			// new com.mysql.jdbc.Driver();

			System.out.println("成功加载MySQL驱动程序");

			// 一个Connection代表一个数据库连接

			conn = DriverManager.getConnection(url);

			Statement stmt = conn.createStatement();

			sql = "select * from POP_SUBSCRIBER_TASK;";
			ResultSet rs = stmt.executeQuery(sql);

			while (rs.next()) {
				SprBusinessBean sprBusinessBean = new SprBusinessBean();
				sprBusinessBean.setEpSn(rs.getString("EP_SN"));
				sprBusinessBean.setTaskId(rs.getString("TASKID"));
				sprBusinessBean.setSprWsdl(rs.getString("SPR_WSDL"));
				sprBusinessBean.setSprIp(rs.getString("SPR_IP"));
				sprBusinessBean.setSprUsr(rs.getString("SPR_USR"));
				sprBusinessBean.setSprPwd(rs.getString("SPR_PWD"));
				sprBusinessBean.setOperateTime(rs.getString("OPERATE_TIME"));
				sprBusinessBean.setPolicyType(rs.getString("POLICY_TYPE"));
				sprBusinessBean.setBatchNo(rs.getString("BATCH_NO"));
				list.add(sprBusinessBean);
			}

		} catch (SQLException e) {

			log.info("MySQL操作错误");

			e.printStackTrace();

		} catch (Exception e) {

			e.printStackTrace();

		} finally {

			try {
				if(conn!=null)
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}

		}
		return list;
	}

	/**
	 * 获取当前POP_SUBSCRIBER_BATCH_INFO表中的数据
	 * 
	 * @return
	 */
	public List<SprBusinessBean> getBatchInfo(String batchNo) {
		List<SprBusinessBean> list = new ArrayList<SprBusinessBean>();
		Connection conn = null;
		String sql;

		// MySQL的JDBC URL编写方式：jdbc:mysql://主机名称：连接端口/数据库的名称?参数=值

		// 避免中文乱码要指定useUnicode和characterEncoding

		// 执行数据库操作之前要在数据库管理系统上创建一个数据库，名字自己定，

		// 下面语句之前就要先创建javademo数据库

		String url = PropUtil.getProp("url", "mysql.properties");
		// String url = PropUtil.getProp("local_url", "mysql.properties");
		// //本地测试用

		try {

			// 之所以要使用下面这条语句，是因为要使用MySQL的驱动，所以我们要把它驱动起来，

			// 可以通过Class.forName把它加载进去，也可以通过初始化来驱动起来，下面三种形式都可以

			Class.forName("com.mysql.jdbc.Driver");// 动态加载mysql驱动

			// or:

			// com.mysql.jdbc.Driver driver = new com.mysql.jdbc.Driver();

			// or：

			// new com.mysql.jdbc.Driver();

			System.out.println("成功加载MySQL驱动程序");

			// 一个Connection代表一个数据库连接

			conn = DriverManager.getConnection(url);

			Statement stmt = conn.createStatement();

			sql = "select * from POP_SUBSCRIBER_BATCH_INFO where BATCH_NO ="
					+ batchNo + ";";
			ResultSet rs = stmt.executeQuery(sql);

			while (rs.next()) {
				SprBusinessBean sprBusinessBean = new SprBusinessBean();
				sprBusinessBean.setOperateTime(rs.getString("OPERATE_TIME"));
				sprBusinessBean.setBatchNo(rs.getString("BATCH_NO"));
				sprBusinessBean.setFileNames(rs.getString("FILE_NAME"));
				list.add(sprBusinessBean);
			}

		} catch (SQLException e) {

			log.info("MySQL操作错误");

			e.printStackTrace();

		} catch (Exception e) {

			e.printStackTrace();

		} finally {

			try {
				if(conn!=null)
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}

		}
		return list;
	}

	/**
	 * POP定时任务去查询POP_SUBSCRIBER_TASK表，该表的记录表示PCC设备正在执行。
	 * 
	 * @throws Exception
	 */
	public void pccCheckSignJob() throws Exception {
		log.info("task is ok!!!!");
		List<SprBusinessBean> sprLists = getTaskInfo();
		// 按批次号分批
		HashSet<String> batchNoSet = new HashSet<String>();
		List<List<SprBusinessBean>> batchList = new ArrayList<List<SprBusinessBean>>();
		if (sprLists != null && sprLists.size() > 0) {
			log.info("sprLists size: " + sprLists.size());
			for (int i = 0; i < sprLists.size(); i++) {
				SprBusinessBean sprBusinessBean = sprLists.get(i);
				batchNoSet.add(sprBusinessBean.getBatchNo());
			}
		}

		if (batchNoSet != null && batchNoSet.size() > 0) {
			for (String batch : batchNoSet) {
				List<SprBusinessBean> list = new ArrayList<SprBusinessBean>();
				for (int i = 0; i < sprLists.size(); i++) {
					SprBusinessBean sprBusinessBean = sprLists.get(i);
					if (batch.equals(sprBusinessBean.getBatchNo())) {
						list.add(sprBusinessBean);
					}
				}
				batchList.add(list);
			}
		}

		if (batchList != null && batchList.size() > 0) {
			log.info("batchList size: " + batchList.size());
			for (int i = 0; i < batchList.size(); i++) {
				List<SprBusinessBean> list = batchList.get(i);
				int comNum = 0; // 该组完成数
				if (list != null && list.size() > 0) {
					log.info("list size: " + list.size());
					for (int j = 0; j < list.size(); j++) {
						SprBusinessBean sprBusinessBean = list.get(j);
						// log.info("taskid: "+sprBusinessBean.getTaskId()+" Epsn: "+sprBusinessBean.getEpSn());
						String result = PCRF_CHKBAT(
								sprBusinessBean.getTaskId(),
								sprBusinessBean.getEpSn(), sprBusinessBean,
								sprBusinessBean.getOperateTime());
						if ("0".equals(result)) {
							comNum++;
						}
					}
					// 该组全部开户完成
					if (comNum == list.size()) {
						// 对该批次下所有的文件进行签约 查询该批次下所有的文件信息
						List<SprBusinessBean> list2 = getBatchInfo(list.get(0)
								.getBatchNo());
						if (list2 != null && list2.size() > 0) {
							int succNum = 0;
							for (SprBusinessBean sprBusinessBean : list2) {
								// 业务策略
								if ("1".equals(list.get(0).getPolicyType())) {
									log.info("begin to addBatService----------->");
									String resString = addBatService(
											sprBusinessBean.getFileNames(),
											sprBusinessBean.getOperateTime(),
											list.get(0));
									log.info("end to addBatService----------->");

									// 删除该条批次数据
									if ("0".equals(resString)) {
										succNum++;
										deleteBatchInfo(sprBusinessBean
												.getFileNames());
									}
								}
								// 用户策略
								if ("2".equals(list.get(0).getPolicyType())) {
									log.info("begin to addBatUsrSessionPolicy----------->");
									String reString = addBatUsrSessionPolicy(
											sprBusinessBean.getFileNames(),
											sprBusinessBean.getOperateTime(),
											list.get(0));
									log.info("end to addBatUsrSessionPolicy----------->");

									// 删除该条批次数据
									if ("0".equals(reString)) {
										succNum++;
										deleteBatchInfo(sprBusinessBean
												.getFileNames());
									}
								}
							}
							// 全部成功删除任务表数据
							if (list2.size() == succNum) {
								deleteTaskInfo(list.get(0).getBatchNo());
							}
						}
					}
				}
			}
		}
	}

	
	/**
	 * 通过webservice开户签约
	 * @param command 操作指令 1=订购 ,2=退订,3=限速,4=取消限速
	 * @param phoneNo 手机号
	 * @param policyId 策略号
	 * @param status 是否有账单日
	 * @return
	 * @throws SQLException 
	 * @throws RemoteException 
	 */
	public String singlePhoneOptWeb(String command, String phoneNo,
			String policyId, String status) throws SQLException, RemoteException{
		String result = "0";
		log.info("begin to singlePhoneOptWeb ,command is "+command+"----------->");
		boolean flag1 = false;
		boolean flag2 = false;
		String usrBillCycleDate ="-1";
		List<SprBusinessBean> list = getSprBeans(phoneNo);
		for (int i = 0; i < list.size(); i++) {
			SprBusinessBean sprBean = list.get(i);
			String spr_name = sprBean.getSprName();
			log.info("手机号:"+phoneNo+"\n"
					 + "结算周期为(若为-1,则表示不发结算周期):"+usrBillCycleDate+"\n"
					 + "策略ID为:"+policyId+"\n"
					 + "手签约机器为:"+sprBean.getSprIp()+","+spr_name+"\n");
			
			String note = spr_name.substring(spr_name.length()-3);
			if(status.equals("0")){
				usrBillCycleDate="-1";
			}else{
				if(note.endsWith("BZX")){
					usrBillCycleDate="99";
				}else{
					usrBillCycleDate="1";
				}
			}
			
			if(command.equals("1")){
				//订购,用户开户并签约用户策略
				String res = addSubscriber(phoneNo, "1", usrBillCycleDate, sprBean);
				if(res.equals("0")){
					String res1 = subscribeUsrSessionPolicy(phoneNo, policyId, usrBillCycleDate, sprBean);
					if(res1.equals("0")){
						result = "1";
					}
				}
			}else if(command.equals("2")){
				//退订，直接销户
				String res = delSubscriber(phoneNo, sprBean);
				if(res.equals("0")){
					result = "1";
				}
			}else if(command.equals("3")){
				String res1 = updateSubscriber(phoneNo, "2", sprBean);
				if(res1.equals("0")){
					result = "1";
				}
			}else if(command.equals("4")){
				String res1 = updateSubscriber(phoneNo, "1", sprBean);
				if(res1.equals("0")){
					result = "1";
				}
			}
			if(i==0&&result.equals("1")){
				flag1=true;
			}
			if(i==1&&result.equals("1")){
				flag2=true;
			}
		}
		if(flag1||flag2){
			result="1";
		}
		
		return result;
	}
	
	
	public void insertNoProductNo(String phoneNo,int staus) throws SQLException{
		
		Connection conn = null;
		String sql;
		String url = PropUtil.getProp("url", "mysql.properties");
		//String url = PropUtil.getProp("local_url", "mysql.properties");
		try {
			Class.forName("com.mysql.jdbc.Driver");// 动态加载mysql驱动
			conn = DriverManager.getConnection(url);
			System.out.println("成功加载MySQL驱动程序");
			Statement stmt = conn.createStatement();
			sql = "insert  into DnotexistPhone values ('"+phoneNo+"','"+staus+"')";
			System.out.println(sql);
			stmt.execute(sql);
		} catch (SQLException e) {

			log.info("MySQL操作错误");
			
			e.printStackTrace();
			throw new SQLException("MySQL操作错误");
		} catch (Exception e) {

			e.printStackTrace();

		} finally {

			try {
				if(conn!=null)
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}

		}
	}
	/**
	 * 生成随机数：当前年月日时分秒+五位随机数
	 * 
	 * @return
	 */
	public static String getRandomNum() {

		SimpleDateFormat simpleDateFormat;

		simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");

		Date date = new Date();

		String str = simpleDateFormat.format(date);

		Random random = new Random();

		int rannum = (int) (random.nextDouble() * (99999 - 10000 + 1)) + 10000;// 获取5位随机数

		return str + rannum;// 当前时间
	}

	
	public static void main(String[] args) throws Exception {
		SendPccInfoServiceImpl impl = new SendPccInfoServiceImpl();
	
		String res3 = impl.singlePhoneOptWeb("2", "13419570290", "11000010000000000000000000000010", "1");
		System.out.println(res3);
		//String res4 = impl.singlePhoneOptWeb("4", "13419570290", "11000030000000000000000000000004", "0");
			
	
		/*int len = args.length;
		String method = args[0];
		
		if(method.equals("singlePhoneOptWeb")){
			*//**
			 * args[1] commond 操作指令 1=订购 ,2=退订,3=限速,4=取消限速
			 * args[2] phoneNo 手机号
			 * args[3] policyId 策略id 11000010000000000000000000000010
			 * args[4] usrBillCycleDate 结算周期
			 *//*
			impl.singlePhoneOptWeb(args[1], args[2], args[3], args[4]);
		}else if(method.equals("cpeUserRegister")){
			
			if(len<4){
				log.info("开户失败,请填写根据操作手册填写相关参数");
				System.exit(0);
			}
			List<String> location = new ArrayList<String>();
			for (int i = 3; i < args.length; i++) {
				location.add(args[i]);
			}
			*//**
			 * cpe开户接口
			 * @param args[1] phoneNo 手机号
			 * @param args[2] policId 策略号，默认为 12270010000000000000000000000004
			 * @param location 位置信息
			 * @return 0=失败;1=成功
			 * @throws SQLException 
			 *//*
			impl.cpeUserRegister(args[1],args[2], location);
		}else if(method.equals("cpeUserReset")){
			if(len<4){
				log.info("开户失败,请填写根据操作手册填写相关参数");
				System.exit(0);
			}
			List<String> location = new ArrayList<String>();
			for (int i = 3; i < args.length; i++) {
				location.add(args[i]);
			}
			*//**
			 * 参数重置,重新签约位置信息
			 * @param args[1] phoneNo
			 * @param args[2] policId 策略号，默认为 12270010000000000000000000000004
			 * @param loctions 位置信息
			 * @return 1=成功 0=失败
			 * @throws SQLException 
			 *//*
			
			impl.cpeUserReset(args[1],args[2], location);
		}else if(method.equals("cpeUnUserPolicy")){
			*//**
			 * 参数重置（去签约，删除用户策略）
			 * @param args[1] phoneNo 手机号
			 * @param args[2] policId 策略号，默认为 12270010000000000000000000000004
			 * @return 1=成功，2=失败
			 * @throws SQLException
			 *//*
			impl.cpeUnUserPolicy(args[1],args[2]);
		}else if(method.equals("cpeUserNetLock")){
			if(len<4){
				log.info("锁网失败,请填写根据操作手册填写相关参数");
				System.exit(0);
			}
			List<String> location = new ArrayList<String>();
			for (int i = 3; i < args.length; i++) {
				location.add(args[i]);
			}
			*//**
			 * 锁网，当小区的个数小于九个时开始锁网
			 * @param args[1] phoneNo 手机号
			 * @param args[2] policId 策略号，默认为 12270010000000000000000000000004
			 * @param location 位置信息
			 * @return 1=成功 0=失败
			 * @throws SQLException 
			 *//*
			impl.cpeUserNetLock(args[1],args[2], location);
		}else if(method.equals("cpeUserStatusChange")){
			
			*//**
			 * cpe用户状态变更（销户）
			 * @param phoneNo
			 * 电话号码
			 * @return
			 * @throws SQLException 
			 * @throws RemoteException 
			 *//*
			impl.cpeUserStatusChange(args[1]);
		}*/
		
	}
}
