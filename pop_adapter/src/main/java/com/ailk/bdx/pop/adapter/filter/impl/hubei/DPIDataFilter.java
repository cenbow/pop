package com.ailk.bdx.pop.adapter.filter.impl.hubei;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import redis.clients.jedis.exceptions.JedisException;

import com.ailk.bdx.pop.adapter.bean.Content;
import com.ailk.bdx.pop.adapter.bean.KeyMessage;
import com.ailk.bdx.pop.adapter.bean.Message;
import com.ailk.bdx.pop.adapter.cache.CountryUserLocationCache;
import com.ailk.bdx.pop.adapter.cache.CpeUserInfoCache;
import com.ailk.bdx.pop.adapter.common.dao.constant.Constants;
import com.ailk.bdx.pop.adapter.filter.IDataFilter;
import com.ailk.bdx.pop.adapter.jedis.JedisClientUtil;
import com.ailk.bdx.pop.adapter.model.custom.CpeLockNetInfo;
import com.ailk.bdx.pop.adapter.util.AdapterUtil;
import com.ailk.bdx.pop.adapter.util.Configure;
import com.google.common.base.Strings;
import com.google.gson.Gson;

/**
 * 过滤DPI测提供的上网日志的实现类
 * @author hpa
 *
 */
public class DPIDataFilter implements IDataFilter {
	//private static final Splitter splitterComma = Splitter.on(",");
	//private static final Splitter splitterUnderline = Splitter.on("_");
	private static final Logger log = LogManager.getLogger();
	/**
	 * CPE上网位置统计周期（单位：小时）
	 */
	private final static int CPE_POSITION_DEFAULT_STATISTICS_PERIOD = 7;
	private static int cpePositionStatisticsPeriod;
	private static final String DATAFILTERAPNVALUE;
	private final static Gson gson;
	static{
		DATAFILTERAPNVALUE = Configure.getInstance().getProperty("DATAFILTERAPNVALUE");
		
		//#CPE上网位置统计周期（单位：小时），默认7天
		String cpePositionStatisticsPeriodStr = Configure.getInstance().
							getProperty("CPE_POSITION_STATISTICS_PERIOD");
		cpePositionStatisticsPeriod = 
				Strings.isNullOrEmpty(cpePositionStatisticsPeriodStr) 
				? CPE_POSITION_DEFAULT_STATISTICS_PERIOD 
				: Integer.parseInt(cpePositionStatisticsPeriodStr);
		gson = new Gson();
	}
	@Override
	public KeyMessage adapte(Message msg){
		String value = msg.getValue();
		Content content = msg.getConfig().getContent();
		
		String[] splitList;
		//根据不同的分隔符拆分上网日志记录
		if("t".equals(content.getSplit())){
		      splitList = StringUtils.splitByWholeSeparatorPreserveAllTokens(value,"\t|\t");
		}else{
			  splitList = StringUtils.splitByWholeSeparatorPreserveAllTokens(value, content.getSplit());
			  for(int i=0;i<splitList.length;i++){
					splitList[i] =splitList[i].trim();
			  }
		}
		//过滤标识该条记录是CPE的位置信息
		//String dataFilterValue = splitList[Integer.parseInt(content.getDataFilterIndex())];
		
		//网管侧没有改造时，需要查询缓存确定该条message是否是CPE的位置信息
		//if(Strings.isNullOrEmpty(DATAFILTERAPNVALUE)){
			//key字段索引 IMSI-用户IMSI（TBCD编码）
			String[] keyIndex = content.getKeyIndex().split(",");
			String subsid, productNo;
			try {
				subsid = splitList[Integer.parseInt(keyIndex[0])];
				productNo = splitList[Integer.parseInt(keyIndex[1])];
				if(Strings.isNullOrEmpty(subsid) || Strings.isNullOrEmpty(productNo)){
					return null;
				}
			} catch (Exception e) {
				log.error("没有设置key字段索引" + e);
				return null;
			}
			
			try{
				boolean isCPE = CpeUserInfoCache.getInstance().containsKey(subsid);
				if(!isCPE){//不是CPE设备的位置信息
					log.debug("当前记录中的终端IMSI:" + subsid + "不是CPE用户编码！");
					return null;
				}
				log.debug("当前记录是CPE设备：" + subsid + "的上网位置信息，手机号码：" + productNo);
			}catch(Exception e){
				log.error("判断当前记录中的终端IMSI: " + subsid + "是否是cpe异常！",e);
				return null;
			}
			String tac,eci;
			try {
				String[] convertIndex = content.getConvertIndex().split(",");
				tac = splitList[Integer.parseInt(convertIndex[0])];
				eci = splitList[Integer.parseInt(convertIndex[1])];
			} catch (Exception e) {
				log.error("没有设置待转换的字段索引" + e);
				return null;
			}
			
			String userLocation = null;
			try {
				userLocation = AdapterUtil.convertTac_Eci2UserLocation(tac, eci);
				log.debug("当前行的tac:" + tac + ",eci:" + eci + " > userLocation：" + userLocation);
			} catch (Exception e) {
				log.debug("当前处理的数据：" + value);
				log.error("将imsi(" + subsid + ")的tac:"+ tac + "，eci:" + eci + "转为UserLocation时失败，当前记录不是CPE的位置信息！");
				return null;
			}
			CpeLockNetInfo cpeLockNetInfo = new CpeLockNetInfo();
			List<String> userlocationList = new ArrayList<String>();
			try{
				Object2ObjectOpenHashMap<String, String> map = CpeUserInfoCache.getInstance().get(subsid);
				if(map == null){
					return null;
				}
				
				//锁网状态: 0：未锁网;1：锁网（9个）;2：锁网（1-8个）
				String netLockFlagStr = map.get("NET_LOCK_FLAG");
				int netLockFlag = Strings.isNullOrEmpty(netLockFlagStr) ? 0 : Integer.parseInt(netLockFlagStr);
				
				//业务状态：1-正常; 2：已锁定（农村到城市）; 3：已锁定（在9个锁网小区外的农村小区userLocation上网）
				String busiTypeStr = map.get("BUSI_TYPE");
				int busiType = Strings.isNullOrEmpty(busiTypeStr) ? 1 : Integer.parseInt(busiTypeStr);
				
				boolean isCountryUserLocation = CountryUserLocationCache
						.getInstance().contains(userLocation);
				
				if(isCountryUserLocation){//农村
					doStatisticsCpeUserLocation(subsid,userLocation);
					boolean contained = JedisClientUtil.sismembers(Constants.REDIS_UNL_PREFIX + subsid,userLocation);
					/**
					 * Redis抛异常测试
					 */
					/*String throwExceptionCPE = "15871665320,13687154577,15271473854,18271854662";
					if(throwExceptionCPE.contains(productNo)){
						throw new JedisException("Redis抛异常测试（手机号码为"+productNo+"）");
					}*/
					if(netLockFlag == Constants.NET_LOCK_FLAG_UNLOCK_2 && contained && busiType == Constants.BUSI_TYPE_NORMAL){
						log.debug("CPE用户编码为（" + subsid + "）的CPE设备当前所在的位置" + userLocation + "已在锁网关系中且业务状态已经为1-正常！");
						return null;
					}else if(netLockFlag == Constants.NET_LOCK_FLAG_LOCK_1 && contained && busiType == Constants.BUSI_TYPE_NORMAL){
						log.debug("CPE用户编码为（" + subsid + "）的CPE设备当前所在的位置" + userLocation + "已在9个锁网关系中且业务状态已经为1-正常！");
						return null;
					}else if(netLockFlag == Constants.NET_LOCK_FLAG_LOCK_1 && !contained && busiType == Constants.BUSI_TYPE_LIMIT_FOR_LOCKED){
						log.debug("CPE用户编码为（" + subsid + "）的CPE设备当前所在的位置" + userLocation + "不在已在9锁网关系中且业务状态已经为3-限速(在农村基站但不在锁网小区中)！");
						return null;
					}else{
						userlocationList.add(userLocation);
						cpeLockNetInfo.setUserLocations(userlocationList);
						//cpeLockNetInfo.setUserLocation(userLocation);
						log.info("CPE设备（" + subsid + "）当前所在的位置userLocation: " + userLocation + "在农村且锁网状态为" + netLockFlag + ",业务状态为：" + busiType);
					}
				}else{//城市
					if(netLockFlag == Constants.NET_LOCK_FLAG_UNLOCK_2 
							 && busiType == Constants.BUSI_TYPE_LIMIT_FOR_CITY){
						
						log.debug("CPE用户编码为（" + subsid + "）的CPE设备当前所在的位置在城市基站且锁网状态已经为2-锁网中（1-8个）且业务状态已经为1-限速状态！");
						return null;
					}else if(netLockFlag == Constants.NET_LOCK_FLAG_LOCK_1 
							&& busiType == Constants.BUSI_TYPE_LIMIT_FOR_CITY){
						
						log.debug("CPE用户编码为（" + subsid + "）的CPE设备当前所在的位置在城市基站且锁网状态已经为1-锁网（9个）且业务状态已经为2-限速状态！");
						return null;
					}else{//当前处理网速正常状态
						//调用锁网限速接口，将扫描到的城市userLocation换为与PCC约定的限速固定userLocation
						/**
						 * 1.net_lock_flag为0时调开户接口还是锁网接口；
						 * 2.net_lock_flag为2时调用锁网接口，userLocationList是传一个约定限速固定的userLocation还是传现有的和固定的并集；
						 * 3.net_lock_flag为1时调用锁网接口
						 */
						//在POP端将城市小区转为130-4600000000000000001
						//userlocationList.add("130-4600000000000000001");
						cpeLockNetInfo.setUserLocations(userlocationList);
						log.info("CPE设备（" + subsid + "）当前所在的位置userLocation: " + userLocation + "在城市且锁网状态为0-未锁网！");
					}
				}
				//cpeLockNetInfo.setNetLockFlag(netLockFlag);
				//cpeLockNetInfo.setBusiType(busiType);
			}catch(JedisException e){
				log.error("处理CPE用户编码为（" + subsid + "）的CPE设备的userLocation: " 
						+ userLocation + "时访问Redis集群异常，直接发送给POP处理！",e);
				userlocationList.add(userLocation);
				cpeLockNetInfo.setUserLocations(userlocationList);
			}catch(Exception e){
				log.error("判断当前记录中CPE用户编码为（" + subsid + "）的CPE设备的userLocation: " 
						+ userLocation + "是否是农村基站小区的userLocation异常！",e);
				
				return null;
			}
			//CPE设备号
			cpeLockNetInfo.setSubsid(subsid);
				
			//手机号码
			cpeLockNetInfo.setProductNo(productNo);
				
			return new KeyMessage("1", gson.toJson(cpeLockNetInfo));
				
			/*ICpeUserInfoService cpeUserInfoService = SpringContext.getBean("cpeUserInfoService", ICpeUserInfoService.class);
			try {
				CpeLockNetInfo returnCpeLockNetInfo = cpeUserInfoService.lockCpeNet(cpeLockNetInfo);
				if (returnCpeLockNetInfo != null) {
					log.info("向POP发送的数据内容(Json 字符串): " + new Gson().toJson(returnCpeLockNetInfo));
					return new KeyMessage("1", new Gson().toJson(returnCpeLockNetInfo));
				}
				return null;
			} catch (Exception e) {
				log.error(e.getMessage());
				return null;
			}*/
			
		/*}else{
			if(!DATAFILTERAPNVALUE.equalsIgnoreCase(dataFilterValue)){
				return null;
			}
			*//**
			 * 网管侧关于APN字段（区分哪些ODS28_LTEC1_S1MME_HM中的数据是CPE的数据）后
			 *//*
		}*/
		//网管侧改造后，即可通过APN字段，来过滤标识哪些是CPE的用户，哪些不是
		//不是CPE的位置信息
		//return null;
	}

	/**
	 * 统计Cpe的上网小区userLocation的访问频次，
	 * 统计的生命周期在pop.properties文件中配置，
	 * 配置项为CPE_POSITION_STATISTICS_PERIOD
	 * @param subsid
	 * @param userLocationList
	 */
	public void doStatisticsCpeUserLocation(String subsid, 
			String userLocation)throws JedisException, Exception{
		try {
			//for(String userLoction : userLocationList){
				//单机版Redis
				/*Double count = RedisUtils.zincrby(PopConstant.REDIS_CPE_USERLOCATION_STATISTICS_PREFIX + subsid, 
						1, userLoction);*/
				
				//Redis集群版代码
				Double count = JedisClientUtil.zincrby(Constants.REDIS_CPE_USERLOCATION_STATISTICS_PREFIX + subsid, 
						1, userLocation);
				
				if(count.intValue() == 1){//刚开户
					//单机版Redis
					//RedisUtils.expire(PopConstant.REDIS_CPE_USERLOCATION_STATISTICS_PREFIX + subsid, 3600 * cpePositionStatisticsPeriod);
					
					//Redis集群版代码
					JedisClientUtil.expire(Constants.REDIS_CPE_USERLOCATION_STATISTICS_PREFIX + subsid, 3600 * cpePositionStatisticsPeriod);
				}
			//}
		} catch (Exception e) {
			throw new JedisException(e);
		}
	}
	
}
