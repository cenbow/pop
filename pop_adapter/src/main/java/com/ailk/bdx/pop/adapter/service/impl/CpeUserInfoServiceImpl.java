package com.ailk.bdx.pop.adapter.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ailk.bdx.pop.adapter.cache.CountryUserLocationCache;
import com.ailk.bdx.pop.adapter.cache.CpeUserInfoCache;
import com.ailk.bdx.pop.adapter.model.custom.CpeLockNetInfo;
import com.ailk.bdx.pop.adapter.service.ICpeUserInfoService;

public class CpeUserInfoServiceImpl implements ICpeUserInfoService {
	private static final Logger log = LogManager.getLogger(CpeUserInfoServiceImpl.class);
	
	//private final static Integer CPE_LOCKEDNET_DEFAULT_LACCICOUNT = 9;
	
	@Override
	public CpeLockNetInfo lockCpeNet(CpeLockNetInfo cpeLockNetInfo)
			throws Exception{
		/*String cpeLockedNetLacCiCountStr = Configure.getInstance().
				getProperty("CPE_LOCKEDNET_LACCICOUNT");
		int cpeLockedNetLacCiCount = 
				Strings.isNullOrEmpty(cpeLockedNetLacCiCountStr) 
				? CPE_LOCKEDNET_DEFAULT_LACCICOUNT 
				: Integer.parseInt(cpeLockedNetLacCiCountStr);*/
		//CPE设备号
		String subsid = cpeLockNetInfo.getSubsid();
		
		/*
		 * 小区lac_ci
		 */
		String userLocation = cpeLockNetInfo.getUserLocation();
		
		try{
			boolean isCPE = CpeUserInfoCache.getInstance().containsKey(subsid);
			if(!isCPE){//不是CPE设备的位置信息
				log.debug("当前记录中的终端IMEI:" + subsid + "不是CPE的设备唯一编码，");
				return null;
			}
			log.debug("当前记录是CPE设备：" + subsid + "的上网位置信息。");
		}catch(Exception e){
			log.error("判断当前记录中的终端IMEI: " + subsid + "是否是cpe异常！",e);
			return null;
		}
		
		/*try {
			*//**
			 * Redis单机版代码
			 *//*
			//String flag= RedisUtils.get(POPAdapterConstants.REDIS_CPE_PREFIX + subsid);
			
			*//**
			 * Redis集群版代码
			 *//*
			String flag = JedisClientUtil.get(POPAdapterConstants.REDIS_CPE_PREFIX + subsid);
			if(Strings.isNullOrEmpty(flag) || !POPAdapterConstants.ISCPE.equals(flag)){
				return null;
			}
		} catch (Exception e) {
			log.error("判断当前记录中的终端IMEI: " + subsid + "是否是cpe异常！",e);
			return null;
		}*/
		
		try{
			boolean isCountryUserLocation = CountryUserLocationCache
					.getInstance().contains(userLocation);
			if(!isCountryUserLocation){
				log.debug("CPE设备（" + subsid + "）当前所在的位置不是农村！");
				return null;
			}
		}catch(Exception e){
			log.error("判断当前记录中CPE设备（" + subsid + "）的userLocation: " + userLocation + "是否是农村基站小区的userLocation异常！",e);
		}
		
		/*
		 * 判断CPE所在小区是否归属农村基站
		 */
		/*
		try {
			//RedisUtils.get(POPAdapterConstants.REDIS_CBS_PREFIX + );
			List<DimCpeLacCi> dimCpeLacCiList = dimCpeLacCiDao.queryByLacCi(userLocation);
			if(dimCpeLacCiList == null || dimCpeLacCiList.size() <= 0){
				return null;
			}
			stationCode = dimCpeLacCiList.get(0).getStationCode();
			lacCiDecId = dimCpeLacCiList.get(0).getLacCiDecId();
			
			//设置  基站编码
			cpeLockNetInfo.setStationCode(stationCode);
			
			//设置 十进制 LacCi
			cpeLockNetInfo.setLacCiDecId(lacCiDecId);
		} catch (Exception e) {
			log.error("判断CPE所在小区是否归属农村基站失败！",e);
			log.error(e.getMessage());
			throw new Exception("判断CPE所在小区是否归属农村基站失败！");
		}*/
		
		//Pop处理
		/*try {
			*//**
			 * Redis单机版代码
			 *//*
			//Set<String> userLocationSet = RedisUtils.smembers(POPAdapterConstants.REDIS_UNL_PREFIX + subsid);
			
			*//**
			 * Redis集群版代码
			 *//*
			Set<String> userLocationSet = JedisClientUtil.smembers(POPAdapterConstants.REDIS_UNL_PREFIX + subsid);
			
			if(userLocationSet.contains(userLocation)){//当前小区已在锁网小区中
				log.debug("当前记录中CPE设备（" + subsid + "）的userLocation: " 
						+ userLocation + "已在锁网小区中！");
				return null;
			}
			
			if(userLocationSet != null && userLocationSet.size() >= cpeLockedNetLacCiCount){
				log.debug("当前记录中CPE设备（" + subsid + "）的userLocation: " 
						+ userLocation + "已满" + cpeLockedNetLacCiCount + "个");
				return null;
			}
			
		} catch (Exception e) {
			log.error("检查当前记录中CPE设备（" + subsid + "）的userLocation: " 
					+ userLocation + "已满" + cpeLockedNetLacCiCount + "个 和 是否已锁网小区中时发生异常！",e);
		}*/
		
		/*
		 * 4.将CPE和锁网小区关系存入Redis，key:"unl:" + subsid
		 */
		String willInsertLacCi = null;
		
		/*
		 * 4.1 智能锁网代码:将使用频度最高的userLocation加入锁网小区
		 * added by hpa
		 * start
		 */
		
		/*
		//Redis单机版代码
		//Set<String> lacCiSet = RedisUtils.zrevrange(POPAdapterConstants.REDIS_CPE_USERLOCATION_STATISTICS_PREFIX + subsid, 0, cpeLockedNetLacCiCount - 1);
		
		//Redis集群版代码
		Set<String> lacCiSet = JedisClientUtil.zrevrange(POPAdapterConstants.REDIS_CPE_USERLOCATION_STATISTICS_PREFIX + subsid, 0, cpeLockedNetLacCiCount - 1);
		
		lacCiSet.removeAll(userLocationSet);
		lacCiSet.removeAll(userLocationList);
		
		Iterator<String> lacCiIterator = lacCiSet.iterator();
		while (lacCiIterator.hasNext()) {
			willInsertLacCi = lacCiIterator.next();
		}
		if(Strings.isNullOrEmpty(willInsertLacCi)){
			willInsertLacCi = userLocation;
		}*/
		/*
		 * 智能锁网代码
		 * end
		 */
		
		/*
		 * 4.2 非智能锁网代码
		 */
		willInsertLacCi = userLocation;
		List<String> userlocationList = new ArrayList<String>();
		userlocationList.add(willInsertLacCi);
		cpeLockNetInfo.setUserLocations(userlocationList);
		
		return cpeLockNetInfo;
	}
	
}
