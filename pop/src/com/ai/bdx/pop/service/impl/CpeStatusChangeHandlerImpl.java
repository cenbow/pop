package com.ai.bdx.pop.service.impl;


import java.util.List;
import java.util.Set;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.commons.lang.xwork.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ai.bdx.pop.adapter.bean.BaseConfig;
import com.ai.bdx.pop.adapter.bean.Content;
import com.ai.bdx.pop.adapter.bean.CpeStatusChangeReponse;
import com.ai.bdx.pop.adapter.dao.ICpeUserInfoDao;
import com.ai.bdx.pop.bean.CpeUserInfo;
import com.ai.bdx.pop.jedis.JedisClientUtil;
import com.ai.bdx.pop.service.CpeStatusChangeHandler;
import com.ai.bdx.pop.util.Configure;
import com.ai.bdx.pop.util.PopConstant;
import com.google.common.base.Strings;

public class CpeStatusChangeHandlerImpl implements CpeStatusChangeHandler{
	private static final Logger log = 
			LogManager.getLogger(CpeStatusChangeHandlerImpl.class);
	private static final ReentrantLock lock = new ReentrantLock();
	
	private static String CPE_STRATEGY_CODES;
	private static int CPE_LOCKEDNET_LACCICOUNT;
	static{
		CPE_STRATEGY_CODES = Configure.getInstance().getProperty("CPE_STRATEGY_CODES");
		if(Strings.isNullOrEmpty(CPE_STRATEGY_CODES)){
			CPE_STRATEGY_CODES = PopConstant.CPE_DEFAULT_STRATEGY_CODES;
		}
		
		//#CPE锁网小区最大值字符串，默认9个
		String cpeLockedNetLacCiCountStr = Configure.getInstance().getProperty(
				"CPE_LOCKEDNET_LACCICOUNT");
		CPE_LOCKEDNET_LACCICOUNT = Strings
				.isNullOrEmpty(cpeLockedNetLacCiCountStr) ? PopConstant.CPE_LOCKEDNET_DEFAULT_LACCICOUNT
				: Integer.parseInt(cpeLockedNetLacCiCountStr);
	}
	//CPE用户DAO对象
	private ICpeUserInfoDao cpeUserInfoDao;
	
	public void setCpeUserInfoDao(ICpeUserInfoDao cpeUserInfoDao) {
		this.cpeUserInfoDao = cpeUserInfoDao;
	}

	@Override
	public CpeStatusChangeReponse handle(String line, BaseConfig config)throws Exception {
		CpeStatusChangeReponse cpeStatusChangeReponse = new CpeStatusChangeReponse();
		try{
			lock.lock();
			Content content = config.getContent();
			//要的事件类型范围
			/*List<String> eventIdList = 
					splitterComma.splitToList(content.getEventid());*/
			
			//切割原始数据行
			String[] columns = StringUtils.
					splitByWholeSeparatorPreserveAllTokens(line, content.getSplit());
			for(int i=0;i<columns.length;i++){
				columns[i] =columns[i].trim();
			}
			
			String[] targetIndex = content.getTargetIndex().split(",");
			String serviceName,ruleName;
			int busiType = 0;
			try {
				//锁位置业务的策略ID
				serviceName = columns[Integer.parseInt(targetIndex[2])];
				
				//限速/恢复速度规则名
				ruleName = columns[Integer.parseInt(targetIndex[3])];
				if(Strings.isNullOrEmpty(serviceName) || Strings.isNullOrEmpty(ruleName)){
					log.debug("EDR日志：" + line + " 的第25个字段锁位置业务的策略ID或者第26个字段限速/恢复速度规则名为空，本条记录无效，不作任何处理！");
					return null;
				}
				
				//如果当前记录不是CPE锁位置业务的限速/恢复速度日志
				if(!CPE_STRATEGY_CODES.contains(serviceName) || 
						(!PopConstant.BUSI_TYPE_NORMAL_FLAG.equalsIgnoreCase(ruleName) && 
						 !PopConstant.BUSI_TYPE_LIMIT_FLAG.equalsIgnoreCase(ruleName))){
					log.debug("EDR日志：" + line + " 不是CPE锁位置业务的限速/恢复速度日志！");
					return null;
				}
				
				if(PopConstant.BUSI_TYPE_NORMAL_FLAG.equalsIgnoreCase(ruleName)){
					busiType = PopConstant.BUSI_TYPE_NORMAL;
				}else if(PopConstant.BUSI_TYPE_LIMIT_FLAG.equalsIgnoreCase(ruleName)){
					busiType = PopConstant.BUSI_TYPE_LIMIT_FOR_CITY;
				}
			} catch (Exception e) {
				log.error("解析EDR日志：" + line + " 的第25个字段锁位置业务的策略ID或者第26个字段限速/恢复速度规则名发生异常！",e);
				return null;
			}
			
			cpeStatusChangeReponse.setBusiStatus(busiType);
			
			//key字段索引手机号码、imei-国际移动设备识别码
			String[] keyIndex = content.getKeyIndex().split(",");
			String productNo,subsid;
			try {
				productNo = columns[Integer.parseInt(keyIndex[0])];
				
				//CPE设备唯一编码，即imei
				subsid = columns[Integer.parseInt(keyIndex[1])];
				if(Strings.isNullOrEmpty(subsid) || Strings.isNullOrEmpty(productNo)){
					log.debug("解析EDR日志：" + line + " 的第5个字段手机号  或者  第6个字段IMEI字段为空！");
					return null;
				}
			} catch (Exception e) {
				log.error("解析EDR日志：" + line + " 的第6个字段IMEI字段发生异常！",e);
				return null;
			}
			if(productNo.startsWith("86")){
				productNo = productNo.substring(2);
			}
			
			cpeStatusChangeReponse.setSubsid(subsid);
			cpeStatusChangeReponse.setProductNo(productNo);
			
			CpeUserInfo inputIpeUserInfo = new CpeUserInfo();
			inputIpeUserInfo.setSubsid(subsid);
			inputIpeUserInfo.setProductNo(productNo);
			inputIpeUserInfo.setNetType(serviceName);
			
			/*
			 * 查询锁网状态： 0：未锁网 1：锁网（9个） 2：锁网（1-8个），即中间状态
			 */
			try{
				/**
				 * Redis单机版代码
				 */
				//userLocationSet = RedisUtils.smembers(PopConstant.REDIS_UNL_PREFIX + subsid);
				
				/**
				 * Redis集群版代码
				 */
				Set<String> lockedUserLocationSet = JedisClientUtil.smembers(PopConstant.REDIS_UNL_PREFIX + subsid);
				if(lockedUserLocationSet == null){
					cpeStatusChangeReponse.setNetLockFlag(PopConstant.NET_LOCK_FLAG_UNLOCK);
				}else{
					if(lockedUserLocationSet.size() <= 0){
						cpeStatusChangeReponse.setNetLockFlag(PopConstant.NET_LOCK_FLAG_UNLOCK);
					}else if(lockedUserLocationSet.size() < CPE_LOCKEDNET_LACCICOUNT){
						cpeStatusChangeReponse.setNetLockFlag(PopConstant.NET_LOCK_FLAG_UNLOCK_2);
					}else{
						cpeStatusChangeReponse.setNetLockFlag(PopConstant.NET_LOCK_FLAG_LOCK_1);
					}
				}
				
			}catch(Exception e){
				log.error("从Redis 查询EDR日志中的CPE: " + subsid + ",phoneNo: " 
						+ productNo + ",policyId: " + serviceName + " 的锁网状态失败！",e);
				log.debug("切换到数据库中查询EDR日志中的CPE: " + subsid + ",phoneNo: " 
						+ productNo + ",policyId: " + serviceName + " 的锁网状态... ...");
				try {
					List<CpeUserInfo> cpeUserInfoList = cpeUserInfoDao.query(inputIpeUserInfo);
					if(cpeUserInfoList == null || cpeUserInfoList.size() <= 0){
						log.debug("从数据库中查询EDR日志中的CPE: " + subsid + ",phoneNo: " 
								+ productNo + ",policyId: " + serviceName + " 的已经销户！",e);
						return null;
					}
					/*
					 * 锁网状态： 0：未锁网 1：锁网（9个） 2：锁网（1-8个），即中间状态
					 */
					cpeStatusChangeReponse.setNetLockFlag(cpeUserInfoList.get(0).getNetLockFlag());
					log.debug("从数据库中查询EDR日志中的CPE: " + subsid + ",phoneNo: " 
							+ productNo + ",policyId: " + serviceName + " 的锁网状态成功！");
				} catch (Exception e2) {
					log.error(e2.getMessage());
					log.error("从数据库中查询EDR日志中的CPE: " + subsid + ",phoneNo: " 
							+ productNo + ",policyId: " + serviceName + " 的锁网状态失败！",e2);
					throw e2;
				}
			}
			
			inputIpeUserInfo.setBusiType(busiType);
			inputIpeUserInfo.setNetLockFlag(-1);
			inputIpeUserInfo.setOpStatus(0);
			try{
				boolean isSuccess = cpeUserInfoDao.update(inputIpeUserInfo);
				if(!isSuccess){
					log.warn("EDR日志中CPE " + subsid + "未开户\\欠费\\已销户！");
					return null;
				}
			}catch(Exception e){
				log.error("根据EDR日志中的CPE: " + subsid + ",phoneNo: " 
						+ productNo + ",netType: " + busiType + " 修改用户的业务状态(1：正常;2：限速（农村到城市）)失败！",e);
				throw e;
			}
		}catch(Exception e){
			log.error("",e);
		}finally{
			lock.unlock();
		}
		
		return cpeStatusChangeReponse;
	}
	
}
