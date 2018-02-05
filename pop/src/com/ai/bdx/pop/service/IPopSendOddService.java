package com.ai.bdx.pop.service;

import it.unimi.dsi.fastutil.bytes.Byte2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.shorts.Short2ObjectOpenHashMap;

import java.util.BitSet;
import java.util.List;

import com.ai.bdx.pop.bean.AvoidCustBean;
import com.ai.bdx.pop.bean.PopPolicyBaseinfoBean;
import com.ai.bdx.pop.bean.PopTaskBean;
 

/**
 * POP 派单接口
 *
 * @author liyz
 *
 */
public interface IPopSendOddService {
	
	
	/**
	 * 获取免打扰之后的客户群 和 剔除的客户群
	 * @param avoidTypeId 免打扰类型id
	 * @param custId 客户群id
	 * @param custType 客户类型	
	 * @param execDate 任务执行日期
	 * */
	public AvoidCustBean getAvoidAfterCustAndDelCust(String custId, String avoidTypeId,String custType,String execDate);
	
	
	/***
	 * 创建策略规任务
	 * @param policyId 策略Id
	 * @return 
	 * */
	public boolean createPolicyRuleTask(String policyId);
	
	
	/**
	 * 将Map中的手机号码同步到数据库
	 * @param custMap
	 * @param tabName
	 * @throws Exception
	 */
	public void synchronizeCustMapToDb(Byte2ObjectOpenHashMap<Short2ObjectOpenHashMap<BitSet>> custMap, String tabName,boolean needCheckExists)throws Exception;


	/**
	 * 将Map中的手机号码同步到-实际派发清单表
	 * @param avoidAfterCustMap 
	 * @param custgroupType
	 * @param execDate
	 * @throws Exception
	 */
	public void insertCustMapToRuleCustTable(Byte2ObjectOpenHashMap<Short2ObjectOpenHashMap<BitSet>> avoidAfterCustMap,String custgroupType, String taskId,String sendCustTab) throws Exception;


	
	/***
	 * 同步数据到db
	 * */
	public void saveDataToDb(AvoidCustBean avoidCustBean, PopTaskBean ptb)  throws Exception ;

	/**
	 * 初始化task任务
	 */
	public void deleteTaskInfo(PopTaskBean ptb) throws Exception;

	/**
	 * 更新状态
	 * */
	public void updatePopTaskExeRelation(PopTaskBean ptb,String ms,boolean flag);

	
	/**
	 * 获取今天需要执行的派单任务
	 * */
	public List<PopTaskBean> getTodayPopTaskBeanList();
	
	
	/**
	 * 重新执行失败的任务
	 * @param policyId 
	 * @param ruleId
	 * @param taskId
	 * @return boolean
	 * */
	public boolean reStartPolicyRuleTask(String policyId,String ruleId,String taskId);
	
	
	/***
	 * 通过taskId 拿到 taskBean
	 * @param taskId
	 * @param popTaskTable
	 * */
	public PopTaskBean getPopTaskBean(String taskId,String popTaskTable);
	
	
	/***
	 * 规则: 暂停 ,重启,终止 
	 * 	 1:规则暂停
	 *   2:规则重启
  	 *	 3:规则终止
	 * */
	public void controlRuleOperate(String ruleId,String popTaskTable,int operStatus) throws Exception; 
	
	
	/**
	 * 执行sql语句
	 * @param sql 执行sql
	 * @param para 参数
	 * */
	
	public void saveDataToDbForSql(String sql,Object[] para);
	
	/**
	 * 拿到当前过期的策略
	 * */
	public List<PopPolicyBaseinfoBean> getOutOfDatePopPolicyList();
	 
	/**
	 * 派单 手机号FTP
	 * @param avoidCustBean
	 * @param ptb
	 */
	public String  sendCustInfo2ChannelNotRealTime( AvoidCustBean avoidCustBean, PopTaskBean ptb,String instruction) throws Exception ;

	 
	
}
