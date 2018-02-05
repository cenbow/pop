package com.ai.bdx.pop.wsclient;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;


/**
 * PCC接口类
 * @author luozn
 */

public interface ISendPccInfoService {
	
	
	/**
	 * 发送策略信息脚本到PCRF
	 * @param pccScriptInfos ：key-厂商标示（1-华为，2-中兴，3-爱立信，...）；value-脚本路径
	 * 
	 * @return
	 */
	public ResultModel sendPccInfo(Map<String,String> pccScriptInfos)
			throws Exception;
	
	

	/**
	 * 单个用户操作，支持的操作：开户签约， 去签约，销户
	 * @param command  10-开户签约；20-去签约；30-销户
	 * @param phoneNo  电话号码
	 * @param policyId 策略ID
	 * @return
	 */
	public ResultModel singlePhoneOpt(short command ,String phoneNo,
			String policyId,String policyType)throws Exception;
	
	
	/**
	 * 单个用户操作，支持的操作：开户签约， 去签约，销户
	 * @param command 10-开户签约；20-去签约；30-销户
	 * @param custListTabName 客户清单表名
	 * @return
	 */
	public ResultModel batchPhonesOpt(short command,String custListTabName,
			String policyId,String policyType)throws Exception;

	/**
	 * 派单检查状态
	 * @Title: pccCheckSignJob
	 * @Description: TODO
	 * @param @throws Exception    
	 * @return void 
	 * @throws
	 */
	public void pccCheckSignJob() throws Exception;
	
	/**
	 * 参数重置
	 * @param phoneNo
	 * @param policId
	 * @param loctions
	 * @return
	 * @throws SQLException 
	 */
	public String cpeUserReset(String phoneNo ,String policId,List<String> loctions) 
		 throws SQLException;
	
	/**
	 * 去签约参数重置
	 * @param phoneNo
	 * @param policId
	 * @return
	 * @throws SQLException
	 */
	public String cpeUnUserPolicy(String phoneNo,String policId)throws SQLException;
	
	/**
	 * cpe开户接口
	 * @param phoneNo 手机号
	 * @param policId 策略号
	 * @param location 小区userLocation集合
	 * @return 0=失败（签约失败，添加位置信息失败）;1=成功; 2=签约成功，添加位置信息失败
	 * @throws SQLException 
	 */

	public String cpeUserRegister(String phoneNo,
			String policId,List<String> location) throws SQLException;
	
	/**
	 * 锁网，当小区的个数小于九个时开始锁网
	 * @param phoneNo 手机号
	 * @param policId 策略号
	 * @param locations 小区userLocation集合
	 * @return 1-是;0-否
	 * @throws SQLException 
	 */
	public String cpeUserNetLock(String phoneNo,String policId,
			List<String> locations) throws SQLException;
	
	/**
	 * 带宽变更
	 * @param usim
	 * CPE设备编号
	 * @param phoneNo
	 * 手机号
	 * @param serviceCode
	 * 业务编号
	 * @param policId
	 * 策略号
	 * @return
	 * @throws SQLException 
	 */
	public String cpeUserNetTypeChange(String phoneNo,String policId,String oldpolicId,List<String> usrlocations) 
			throws SQLException;
	
	/**
	 * cpe用户状态变更（销户）
	 * @param phoneNo
	 * 电话号码
	 * @return
	 * @throws SQLException 
	 */
	
	public String cpeUserStatusChange(String phoneNo) 
			throws SQLException;
	
	/**
	 * 
	 * @param list （phoneNo=手机号  policId=策略号  loctions=基站小区号 ）
	 * @return Map key=1 成功的手机号码 ,key=0失败的手机号码集合
	 * @throws SQLException 
	 */
	public Map<String,Object> cpeBsBatchcg(List<Map<String,Object>> list) 
			throws SQLException;


	/**
	 * 通过webservice开户签约
	 * @param command 操作指令 1=订购 ,2=退订,3=限速,4=取消限速
	 * @param phoneNo 手机号
	 * @param policyId 策略号
	 * @param usrBillCycleDate 结算日期(开户用)
	 * @return
	 * @throws SQLException 
	 * @throws RemoteException 
	 */
	public String singlePhoneOptWeb(String command, String phoneNo,
			String policyId, String usrBillCycleDate) throws SQLException, RemoteException;
}
