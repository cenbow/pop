package com.ai.bdx.pop.adapter.socket.buffer;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import redis.clients.jedis.exceptions.JedisException;

import com.ai.bdx.pop.adapter.bean.FileConfig;
import com.ai.bdx.pop.adapter.dao.ICpeUserInfoDao;
import com.ai.bdx.pop.adapter.dao.ICpeUserLockRelDao;
import com.ai.bdx.pop.adapter.dao.IDimCpeStationDao;
import com.ai.bdx.pop.bean.CpeLockNetInfo;
import com.ai.bdx.pop.bean.CpeUserInfo;
import com.ai.bdx.pop.bean.CpeUserLockRelBean;
import com.ai.bdx.pop.bean.DimCpeStation;
import com.ai.bdx.pop.jedis.JedisClientUtil;
import com.ai.bdx.pop.util.Configure;
import com.ai.bdx.pop.util.PopConstant;
import com.ai.bdx.pop.util.SpringContext;
import com.ai.bdx.pop.util.XmlConfigureUtil;
import com.ai.bdx.pop.wsclient.ISendPccInfoService;
import com.google.common.base.Joiner;
import com.google.common.base.Strings;
import com.google.common.io.Files;
import com.google.gson.Gson;
import com.jfinal.plugin.activerecord.Db;
import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.WorkHandler;

import edu.emory.mathcs.backport.java.util.Arrays;

public class PopEventHandler 
	implements EventHandler<PopEvent>, WorkHandler<PopEvent> {
	private static final Logger log = 
			LogManager.getLogger(PopEventHandler.class);
	
	private static final ReentrantLock lock = new ReentrantLock();
	
	/**
	 * CPE锁网小区默认值，值为9个
	 */
	private final static Integer CPE_LOCKEDNET_DEFAULT_LACCICOUNT = 9;
	
	/**
	 * CPE锁网小区最大值，默认9个
	 */
	private final static Integer CPE_LOCKEDNET_LACCICOUNT;
	
	/**
	 * CPE上网位置统计周期（单位：小时）
	 */
	private final static Integer CPE_POSITION_DEFAULT_STATISTICS_PERIOD = 7;
	
	private static final FileConfig fileConfig;
	
	/**
	 * CPE开户数据内容编码方式
	 * 在/config/aibi_pop/province/${hubei}/source-config.xml配置文件配置
	 */
	private static String charsetName;
	
	/**
	 * 文件名前缀
	 */
	private static String fileSuffix;
	
	/**
	 * 文件最后序列号
	 */
	private static int serialNoLength;
	
	/**
	 * 反馈文件所在目录
	 */
	private static String feedbackFileDirectory;
	
	/**
	 * CPE开户成功的文件存放路径
	 */
	private static String processedFileDirectory;
	
	/**
	 * CPE开户异常文件存放路径
	 */
	private static String errorFileDirectory;
	
	/**
	 * 反馈文件名前缀
	 */
	private static String feedbackFilePrefix;
	
	/**
	 * 反馈文件名时间串
	 */
	//private static String feedbackFileTimeformat;
	
	/**
	 * 反馈文件名后缀
	 */
	private static String feedbackFileSuffix;
	
	/**
	 * CPE用户信息Dao类
	 */
	private static ICpeUserInfoDao cpeUserInfoDao;
	
	private static Gson gson;
	
	/**
	 * CPE与锁网小区Dao类
	 */
	private static ICpeUserLockRelDao cpeUserLockRelDao;
	
	private static IDimCpeStationDao dimCpeStationDao;
	
	private static ISendPccInfoService sendPccInfoService;
	
	static{
		fileConfig = (FileConfig) XmlConfigureUtil.getInstance()
				.getConfigItem(XmlConfigureUtil.FILE,
						PopConstant.CPE_INSTALL_SOURCE_NAME);
		charsetName = fileConfig.getCharsetName();
		fileSuffix = fileConfig.getFileSuffix();
		serialNoLength = fileConfig.getSerialNoLength();
		feedbackFileDirectory = fileConfig.getFeedbackFileDirectory();
		processedFileDirectory = fileConfig.getProcessedFileDirectory();
		errorFileDirectory = fileConfig.getErrorFileDirectory();
		feedbackFilePrefix = fileConfig.getFeedbackFilePrefix();
		//feedbackFileTimeformat = fileConfig.getFeedbackFileTimeformat();
		feedbackFileSuffix = fileConfig.getFeedbackFileSuffix();
		
		//#CPE锁网小区最大值字符串，默认9个
		String cpeLockedNetLacCiCountStr = Configure.getInstance().getProperty(
				"CPE_LOCKEDNET_LACCICOUNT");
		CPE_LOCKEDNET_LACCICOUNT = Strings.isNullOrEmpty(cpeLockedNetLacCiCountStr) 
				? CPE_LOCKEDNET_DEFAULT_LACCICOUNT
				: Integer.parseInt(cpeLockedNetLacCiCountStr);
		
		gson = new Gson();
		cpeUserInfoDao = SpringContext.getBean("cpeUserInfoDao", ICpeUserInfoDao.class);
		cpeUserLockRelDao = SpringContext.getBean("cpeUserLockRelDao", ICpeUserLockRelDao.class);
		dimCpeStationDao = SpringContext.getBean("dimCpeStationDao", IDimCpeStationDao.class);
		sendPccInfoService = SpringContext.getBean("sendPccInfoService", ISendPccInfoService.class);
	}
	
	@Override
	public void onEvent(PopEvent event, long sequence, boolean endOfBatch) 
			throws Exception {
		sendEventData(event.getValue());
	}
	
	@Override
	public void onEvent(PopEvent event) throws Exception {
		sendEventData(event.getValue());
	}
	
	public void sendEventData(String eventInfo) throws Exception {
		try{
			lock.lock();
			
			//从缓存中提取事件类型及属性数据
			log.info("pop socket server received message: " + eventInfo);
			
			CpeLockNetInfo cpeLockNetInfo = gson.fromJson(eventInfo, CpeLockNetInfo.class);
			
			//CPE用户编码
			String subsid = cpeLockNetInfo.getSubsid();
			CpeUserInfo cpeUserInfoTable = cpeUserInfoDao.query(subsid);
			
			//锁网小区userLocation集合
			List<String> userLocationList = cpeLockNetInfo.getUserLocations();
			
			//锁网关系：0-未锁网；1-锁网完成；2锁网中
			int netLockFlag = cpeUserInfoTable.getNetLockFlag();
			
			//业务状态：1-正常；2-城市限速；3-农村限速（在锁网小区外）
			int busiType = cpeUserInfoTable.getBusiType();
			
			CpeUserInfo cpeUserInfo = new CpeUserInfo();
			
			cpeUserInfo.setSubsid(subsid);
			String phoneNo = cpeLockNetInfo.getProductNo();
			/**
			 * 不调PCC接口仅修改锁网状态netLockFlag和业务状态busiType部分
			 */
			try{
				if((userLocationList == null || userLocationList.size() == 0) &&
						(netLockFlag == PopConstant.NET_LOCK_FLAG_LOCK_1
						 || netLockFlag == PopConstant.NET_LOCK_FLAG_UNLOCK_2) ){
					if((busiType == PopConstant.BUSI_TYPE_NORMAL 
						 || busiType == PopConstant.BUSI_TYPE_LIMIT_FOR_LOCKED)){
						cpeUserInfo.setBusiType(PopConstant.BUSI_TYPE_LIMIT_FOR_CITY);
						cpeUserInfoDao.update(cpeUserInfo);
						log.debug("已开户CPE用户编码（CPE用户编码为" + subsid + ", 手机号码为" + phoneNo + "）当前所在的位置在城市基站故处于限速状态！");
						return;
					}else{
						return;
					}
					
				}
			}catch(Exception e){
				log.error("将锁网状态为1-锁网（9个）和锁网中（1-8个）且当前在城市基站的CPE用户编码（CPE用户编码为" 
						+ subsid + ", 手机号码为" + phoneNo + "）修改为2-城市限速状态失败！", e);
				return;
			}
			
			List<String> canAddedUserLocationList = new ArrayList<String>();
			
			//当前该CPE已有的锁网小区的userLocation个数
			long lockedUserLocationCount = 0;
			
			//从Redis Cluster中查询CPE锁网小区userLocation
			Set<String> lockedUserLocationSet = null;
			
			//从数据库中的cpe_user_lock_rel表查询CPE锁网小区userLocation
			List<String> lockedUserLocationList = null;
			
			if(netLockFlag == PopConstant.NET_LOCK_FLAG_LOCK_1){//1-锁网完成
				try{
					boolean contained = JedisClientUtil.sismembers(PopConstant.REDIS_UNL_PREFIX 
							+ subsid,userLocationList.get(0));
					if(!contained && busiType == PopConstant.BUSI_TYPE_NORMAL){
						busiType = PopConstant.BUSI_TYPE_LIMIT_FOR_LOCKED;
					}else if(contained && busiType == PopConstant.BUSI_TYPE_LIMIT_FOR_CITY){
						busiType = PopConstant.BUSI_TYPE_NORMAL;
					}else if(contained && busiType == PopConstant.BUSI_TYPE_LIMIT_FOR_LOCKED){
						busiType = PopConstant.BUSI_TYPE_NORMAL;
					}else{
						return;
					}
					cpeUserInfoDao.update(cpeUserInfo);
					return;
				}catch(Exception e){
					lockedUserLocationList = cpeUserLockRelDao.queryUserLocationsBySubsid(subsid);
					if(lockedUserLocationList != null && lockedUserLocationList.size() > 0){
						boolean contained = lockedUserLocationList.contains(userLocationList.get(0));
						if(!contained && busiType == PopConstant.BUSI_TYPE_NORMAL){
							busiType = PopConstant.BUSI_TYPE_LIMIT_FOR_LOCKED;
						}else if(contained && busiType == PopConstant.BUSI_TYPE_LIMIT_FOR_CITY){
							busiType = PopConstant.BUSI_TYPE_NORMAL;
						}else if(contained && busiType == PopConstant.BUSI_TYPE_LIMIT_FOR_LOCKED){
							busiType = PopConstant.BUSI_TYPE_NORMAL;
						}else{
							return;
						}
						try{
							cpeUserInfoDao.update(cpeUserInfo);
						}catch(Exception e2){
							log.error("自动锁网流程将CPE（CPE用户编码为" + subsid + ", 手机号码为" + phoneNo + "）的业务状态改为" + busiType + "失败！", e);
						}
						return;
					}
				}
			}else if(netLockFlag == PopConstant.NET_LOCK_FLAG_UNLOCK_2){//2-锁网中
				try{
					/**
					 * Redis单机版代码
					 */
					//userLocationSet = RedisUtils.smembers(PopConstant.REDIS_UNL_PREFIX + subsid);
					
					/**
					 * Redis集群版代码
					 */
					lockedUserLocationSet = JedisClientUtil.smembers(PopConstant.REDIS_UNL_PREFIX + subsid);
					
					/**
					 * Redis抛异常测试
					 */
					/*String throwExceptionCPE = "15871665320,13687154577,15271473854,18271854662";
					if(throwExceptionCPE.contains(phoneNo)){
						throw new JedisException("Redis抛异常测试（手机号码为"+phoneNo+"）");
					}*/
				}catch(Exception e){
					log.error("从Redis集群中查询CPE(" + subsid + ")的锁网关系失败，开始执行去签约操作！");
					
					//去签约，删除数据库中的锁网关系，将锁网状态置为0-未开户；业务状态置为1-正常
					toCpeUnUserPolicy(subsid,phoneNo,cpeUserInfoTable.getPolicyId());
					return;
				}
				
				try{
					if(lockedUserLocationSet != null && lockedUserLocationSet.size() >= CPE_LOCKEDNET_LACCICOUNT){
						cpeUserInfo.setNetLockFlag(PopConstant.NET_LOCK_FLAG_LOCK_1);
						
						cpeUserInfo.setBusiType(lockedUserLocationSet.contains(
								userLocationList.get(userLocationList.size() - 1)) 
								? PopConstant.BUSI_TYPE_NORMAL : PopConstant.BUSI_TYPE_LIMIT_FOR_LOCKED);
						
						try{
							cpeUserInfoDao.update(cpeUserInfo);
						}catch(Exception e2){
							log.error("自动锁网流程将CPE（CPE用户编码为" + subsid + ", 手机号码为" + phoneNo + "）的业务状态改为" + busiType + "失败！", e2);
						}
						log.debug("当前记录中CPE设备（" + subsid + "）的userLocationList: " 
								+ Joiner.on(",").join(lockedUserLocationSet) + " 已满,共" + CPE_LOCKEDNET_LACCICOUNT + "个");
						return;
					}
					for(String userLocation : userLocationList){
						if(lockedUserLocationSet == null){
							canAddedUserLocationList.add(userLocation);
						}else{
							if(canAddedUserLocationList.size() < CPE_LOCKEDNET_LACCICOUNT - lockedUserLocationSet.size()){
								if(lockedUserLocationSet.contains(userLocation)){
									log.debug("当前记录中CPE设备（" + subsid + "）的userLocation: " 
											+ userLocation + "已在锁网小区中！");
									continue;
								}else{
									canAddedUserLocationList.add(userLocation);
								}
							}else{
								break;
							}
						}
					}
					if(canAddedUserLocationList.size() <= 0){//当前位置的userLocation已经在该CPE的锁网小区userLocation中
						
						if(busiType != PopConstant.BUSI_TYPE_NORMAL){
							cpeUserInfo.setBusiType(PopConstant.BUSI_TYPE_NORMAL);
							try{
								cpeUserInfoDao.update(cpeUserInfo);
							}catch(Exception e){
								log.error("自动锁网流程将CPE（CPE用户编码为" + subsid + ", 手机号码为" + phoneNo + "）的业务状态改为" + busiType + "失败！", e);
							}
						}
						log.debug("当前记录中CPE设备（" + subsid + "）的userLocation: " 
								+ userLocationList.get(0) + "已在锁网小区中！");
						return;
					}
					//计算该CPE已锁网小区的个数
					lockedUserLocationCount = lockedUserLocationSet.size();
				}catch(Exception e){//操作Redis Cluster异常时
					/*log.error("从Redis Cluster中查询CPE subsid:" + subsid + "的已锁网小区失败！");
					log.info("切换到数据库中查询CPE subsid:" + subsid + "的已锁网小区");
					try {
						lockedUserLocationList = cpeUserLockRelDao.queryUserLocationsBySubsid(subsid);
					} catch (Exception e1) {
						log.error("从数据库查询CPE subsid:" + subsid + "的已锁网小区失败！",e);
						return;
					}
					if(lockedUserLocationList != null && lockedUserLocationList.size() >= CPE_LOCKEDNET_LACCICOUNT){
						
						cpeUserInfo.setNetLockFlag(PopConstant.NET_LOCK_FLAG_LOCK_1);
						
						cpeUserInfo.setBusiType(lockedUserLocationList.contains(
								userLocationList.get(userLocationList.size() - 1)) 
								? PopConstant.BUSI_TYPE_NORMAL : PopConstant.BUSI_TYPE_LIMIT_FOR_LOCKED);
						
						cpeUserInfoDao.update(cpeUserInfo);
						log.debug("当前记录中CPE设备（" + subsid + "）的userLocationList: " 
								+ Joiner.on(",").join(lockedUserLocationList) + " 已满" + CPE_LOCKEDNET_LACCICOUNT + "个");
						return;
					}
					for(String userLocation : userLocationList){
						if(lockedUserLocationList == null){
							canAddedUserLocationList.add(userLocation);
						}else{
							if(canAddedUserLocationList.size() < CPE_LOCKEDNET_LACCICOUNT - lockedUserLocationList.size()){
								if(lockedUserLocationList.contains(userLocation)){
									log.debug("当前记录中CPE设备（" + subsid + "）的userLocation: " 
											+ userLocation + "已在锁网小区中！");
									continue;
								}else{
									canAddedUserLocationList.add(userLocation);
								}
							}else{
								break;
							}
						}
					}
					if(canAddedUserLocationList.size() <= 0){//当前位置的userLocation已经在该CPE的锁网小区userLocation中
						if(busiType != PopConstant.BUSI_TYPE_NORMAL){
							cpeUserInfo.setBusiType(PopConstant.BUSI_TYPE_NORMAL);
							cpeUserInfoDao.update(cpeUserInfo);
						}
						log.debug("当前记录中CPE设备（" + subsid + "）的userLocation: " 
								+ userLocationList.get(0) + "已在锁网小区中！");
						return;
					}
					//计算该CPE已锁网小区的个数
					lockedUserLocationCount = lockedUserLocationList.size();*/
				}
			}else if(netLockFlag == PopConstant.NET_LOCK_FLAG_UNLOCK){
				if(userLocationList == null || userLocationList.size() == 0){
					userLocationList.add("130-4600000000000000001");
					canAddedUserLocationList.addAll(userLocationList);
					log.debug("CPE设备（"+ subsid + "）处于未锁网状态，请当前位置为城市位置，将其userLocation设置为130-4600000000000000001");
				}
			}
			
			//统计访问频次的代码移到了pop_adapter工程
			/*try{
				//统计Cpe的上网小区userLocation的访问频次
				doStatisticsCpeUserLocation(subsid,canAddedUserLocationList);
			}catch(JedisConnectionException e){
				log.error("连接JedisConnectionException异常!");
				log.error("统计该CPE设备" + subsid + "访问小区UserLocation的频次信息失败！");
			}catch(JedisClusterMaxRedirectionsException e){
				log.error("发生JedisClusterMaxRedirectionsException异常！");
				log.error("统计该CPE设备" + subsid + "访问小区UserLocation的频次信息失败！");
			}catch(JedisException e){
				log.error("发生JedisException异常！");
				log.error("统计该CPE设备" + subsid + "访问小区UserLocation的频次信息失败！");
			}catch(Exception e){
				log.error("统计该CPE设备" + subsid + "访问小区UserLocation的频次信息失败！");
			}*/
			
			log.debug("CPE设备subsid：" + subsid + "已有的锁网小区个数为：" + lockedUserLocationCount);
			try {
				cpeUserInfo.setProductNo(phoneNo);
				String result = "1";
				if (netLockFlag == PopConstant.NET_LOCK_FLAG_UNLOCK) {//开户	
					
					/**
					 * 来之数据库
					 */
					String policId = cpeUserInfoTable.getPolicyId();
					//开户数据文件名称
					String fileName = cpeUserInfoTable.getFileName();
					log.info("调用PCC接口sendPccInfoService.cpeUserRegister('" + phoneNo + "','" 
							+ policId + "','" + Joiner.on(",").join(canAddedUserLocationList) + "')接口，开始CPE用户开户...");
					Set<String> userLocationSet = null;
					try{
						userLocationSet = JedisClientUtil.zrevrange(PopConstant.REDIS_CPE_USERLOCATION_STATISTICS_PREFIX + subsid, 
								0, CPE_LOCKEDNET_LACCICOUNT - 4);
						/**
						 * Redis抛异常测试
						 */
						/*String throwExceptionCPE = "15871665320,13687154577,15271473854,18271854662";
						if(throwExceptionCPE.contains(phoneNo)){
							throw new JedisException("Redis抛异常测试（手机号码为"+phoneNo+"）");
						}*/
					}catch(Exception e){
						log.error("开户流程将CPE（CPE用户编码为" + subsid + ", 手机号码为" + phoneNo + 
								"）从redis访问频次中获取" + (CPE_LOCKEDNET_LACCICOUNT - 4) + "个userLocation时异常，开户流程中断！");
						//去签约，删除数据库中的锁网关系，将锁网状态置为0-未开户；业务状态置为1-正常
						toCpeUnUserPolicy(subsid,phoneNo,cpeUserInfoTable.getPolicyId());
						return;
					}
					
					//去重集合
					userLocationSet.addAll(userLocationList);
					
					canAddedUserLocationList.clear();
					canAddedUserLocationList.addAll(userLocationSet);
					result = sendPccInfoService.cpeUserRegister(phoneNo, 
							policId, canAddedUserLocationList);
					log.info("调用PCC接口sendPccInfoService.cpeUserRegister('" + phoneNo + "','" 
							+ policId + "','" + Joiner.on(",").join(canAddedUserLocationList) + "')接口，调用CPE用户开户接口反馈结果：" + result);
					if(PopConstant.PCC_REGISTER_SIGNED_SUCCESS_ADDED_SUCESS.equals(result)){
						
						//锁网状态:2：锁网（1-8个）
						cpeUserInfo.setNetLockFlag(PopConstant.NET_LOCK_FLAG_UNLOCK_2);
						
						if(canAddedUserLocationList.contains("130-4600000000000000001")){
							canAddedUserLocationList.remove("130-4600000000000000001");
							//锁网状态:1：正常
							cpeUserInfo.setBusiType(PopConstant.BUSI_TYPE_LIMIT_FOR_CITY);
						}else{
							//锁网状态:1：正常
							cpeUserInfo.setBusiType(PopConstant.BUSI_TYPE_NORMAL);
						}
						//操作状态:2：开户完成（PCC接口返回开户成功）
						cpeUserInfo.setOpStatus(PopConstant.OP_STATUS_COMPLATE_ACCOUNT);
						
						log.info("PCC开户接口反馈结果：账号信息为phoneNo:" + phoneNo + ",subsid:" 
								+ subsid + ",userLocationList:" + Joiner.on(",").join(canAddedUserLocationList) 
								+ " 的CPE用户开户成功！目前的操作状态为：开户成功（opStatus:2）。");
						
						try{
							cpeUserInfoDao.update(cpeUserInfo);
						}catch(Exception e){
							log.error("自动锁网流程将CPE（CPE用户编码为" + subsid + ", 手机号码为" + phoneNo + "）的状态失败！", e);
						}
						//防止出现不一致数据，因此先删除锁网关系
						try{
							JedisClientUtil.del(PopConstant.REDIS_UNL_PREFIX + subsid);
						}catch(Exception e){
							log.error("开户成功后先删除CPE（CPE用户编码为" + subsid + ", 手机号码为" + phoneNo + 
								"）的Redis中锁网关系失败！", e);
						}
						//记录CPE锁网小区关系数据
						doRecordCPEInstall(subsid, phoneNo, canAddedUserLocationList, true);
						
						return;
					}/*else if(PopConstant.PCC_REGISTER_SIGNED_SUCCESS_ADDED_ERROR.equals(result)){
						
						//锁网状态:2：锁网（1-8个）
						cpeUserInfo.setNetLockFlag(PopConstant.NET_LOCK_FLAG_UNLOCK_2);
						
						if(!canAddedUserLocationList.contains("130-4600000000000000001")){//农村开户
							//锁网状态:3：农村限速（在锁网小区外的农村小区上网）
							cpeUserInfo.setBusiType(PopConstant.BUSI_TYPE_NORMAL);
						}else{//城市开户
							//锁网状态:2：城市限速
							cpeUserInfo.setBusiType(PopConstant.BUSI_TYPE_LIMIT_FOR_CITY);
						}
						
						//操作状态:2：开户完成（PCC接口返回开户成功）
						cpeUserInfo.setOpStatus(PopConstant.OP_STATUS_COMPLATE_ACCOUNT);
						
						cpeUserInfoDao.update(cpeUserInfo);
						return;
					}*/else if(PopConstant.PCC_REGISTER_SIGNED_ERROR_ADDED_ERROR.equals(result)){
						
						//去签约，删除数据库中的锁网关系，将锁网状态置为0-未开户；业务状态置为1-正常
						toCpeUnUserPolicy(subsid,phoneNo,cpeUserInfoTable.getPolicyId());
						
						cpeUserInfo.setNetLockFlag(PopConstant.NET_LOCK_FLAG_UNLOCK);
						cpeUserInfo.setBusiType(PopConstant.BUSI_TYPE_NORMAL);
						cpeUserInfo.setOpStatus(PopConstant.OP_STATUS_EXCEPTION);
						try{
							cpeUserInfoDao.update(cpeUserInfo);
						}catch(Exception e){
							log.error("自动锁网流程将CPE（CPE用户编码为" + subsid + ", 手机号码为" + phoneNo + "）的状态失败！", e);
						}
						
						log.error("PCC开户接口反馈结果：账号信息为phoneNo:" + phoneNo + ",subsid:" 
								+ subsid + ",userLocationList:" + Joiner.on(",").join(canAddedUserLocationList) 
								+ "的CPE用户开户失败！目前的操作状态为：开户操作异常（opStatus:-1）。");
						
						if(Strings.isNullOrEmpty(processedFileDirectory)){
							log.error("请source-config.xml文件中设置processedFileDirectory文件路径");
							return;
						}
						if(!processedFileDirectory.endsWith(File.separator)) {
							processedFileDirectory += File.separator;
						}
						
						if (!new File(processedFileDirectory).exists()) {
							log.error("source-config.xml文件中配置的processedFileDirectory：" + processedFileDirectory + "不存在！");
						}
						
						//开户异常时将这个开户数据文件从已处理目录
						FileUtils.moveFileToDirectory(new File(processedFileDirectory + fileName), new File(errorFileDirectory), true);
					}
					
					//生成反馈数据文件
					writeFeedbackFile(fileName,result);
				}else if(netLockFlag == PopConstant.NET_LOCK_FLAG_UNLOCK_2){//锁网中
					/**
					 * 来之数据库
					 */
					String policId = cpeUserInfoTable.getPolicyId();
					/**
					 * 锁网时会把原来的userLocation删除了，因此，需要将该CPE的已有锁网小区和新扫描的小区userLocation发给PCC。
					 */
					List<String> insertUserLocationList 
						= new ArrayList<String>(Arrays.asList(new String[canAddedUserLocationList.size()]));
					Collections.copy(insertUserLocationList, canAddedUserLocationList);
					
					//查询Redis Cluster不发生异常时
					if(lockedUserLocationSet != null && lockedUserLocationSet.size() > 0){
						insertUserLocationList.addAll(new ArrayList<String>(lockedUserLocationSet));
					}
					
					//如果查询Redis Cluster发生异常，则从数据库中查询cpe的已有锁网小区的userLocation
					if(lockedUserLocationList != null && lockedUserLocationList.size() > 0){
						insertUserLocationList.addAll(lockedUserLocationList);
					}
					log.info("调用PCC接口sendPccInfoService.cpeUserNetLock('" + phoneNo + "," 
							+  policId + "," +  insertUserLocationList + "')接口，开始CPE小区锁网...");
					result = sendPccInfoService.cpeUserNetLock(phoneNo, 
							policId, insertUserLocationList);
					
					if (PopConstant.PCC_RETURN_SUCCESS.equals(result)) {
						if(lockedUserLocationCount >= CPE_LOCKEDNET_LACCICOUNT - 1){//
							cpeUserInfo.setNetLockFlag(PopConstant.NET_LOCK_FLAG_LOCK_1);
						}else {
							cpeUserInfo.setNetLockFlag(PopConstant.NET_LOCK_FLAG_UNLOCK_2);
						}
						cpeUserInfo.setBusiType(PopConstant.BUSI_TYPE_NORMAL);
						
						if(insertUserLocationList.size() < CPE_LOCKEDNET_LACCICOUNT){
							cpeUserInfo.setOpStatus(PopConstant.OP_STATUS_CPE_LOCKING_NET);
							log.info("PCC锁网接口反馈结果：账号信息为phoneNo:" + phoneNo + ",subsid:" 
									+ subsid + ",userLocationList:" + Joiner.on(",").join(insertUserLocationList) 
									+ " 的CPE用户锁网成功！目前的操作状态是：锁网中（opStatus:9）。");
						}else{
							cpeUserInfo.setOpStatus(PopConstant.OP_STATUS_CPE_LOCKED_NET);
							log.info("PCC锁网接口反馈结果：账号信息为phoneNo:" + phoneNo + ",subsid:" 
									+ subsid + ",userLocationList:" + Joiner.on(",").join(insertUserLocationList) 
									+ " 的CPE用户锁网成功！目前的操作状态是：锁网完成（opStatus:10）。");
						}
						try{
							cpeUserInfoDao.update(cpeUserInfo);
						}catch(Exception e){
							log.error("自动锁网流程将CPE（CPE用户编码为" + subsid + ", 手机号码为" + phoneNo + "）的状态失败！", e);
						}
						/**
						 * 记录CPE锁网数据
						 */
						doRecordCPEInstall(subsid, phoneNo, canAddedUserLocationList, false);
						
					}else {
						cpeUserInfo.setOpStatus(PopConstant.OP_STATUS_EXCEPTION);
						cpeUserInfo.setBusiType(PopConstant.BUSI_TYPE_LIMIT_FOR_LOCKED);
						log.error("PCC锁网接口反馈结果：账号信息为phoneNo:" + phoneNo + ",subsid:" 
								+ subsid + ",userLocationList:" + Joiner.on(",").join(insertUserLocationList) 
								+ "的CPE用户锁网失败！目前的操作状态是：锁网失败（opStatus:-1）。");
						try{
							cpeUserInfoDao.update(cpeUserInfo);
						}catch(Exception e){
							log.error("自动锁网流程将CPE（CPE用户编码为" + subsid + ", 手机号码为" + phoneNo + "）的状态失败！", e);
						}
					}
				}
			} catch (Exception e) {
				if (lockedUserLocationCount == 0) {
					log.info("账号信息为phoneNo:" + phoneNo + ",subsid:" 
							+ subsid + ",userLocationList:" + Joiner.on(",").join(canAddedUserLocationList) 
							+ " 的CPE用户开户失败！目前的操作状态是：开户失败（opStatus:-1）。");
				}else{
					log.info("账号信息为phoneNo:" + phoneNo + ",subsid:" 
							+ subsid + ",userLocationList:" + Joiner.on(",").join(canAddedUserLocationList) 
							+ " 的CPE用户锁网失败！目前的操作状态是：锁网失败（opStatus:-1）。");
				}
				log.error(e.getMessage(), e);
				throw e;
			}
		}catch(Exception e){
			throw e;
		}finally{
			lock.unlock();
		}
	}
	
	
	private void toCpeUnUserPolicy(String subsid, String phoneNo,String policyNo) throws Exception {
		try{
			String result = doCpeUnUserPolicy(phoneNo, policyNo);
			if(result == "1"){//成功
				int netLockFlag = PopConstant.NET_LOCK_FLAG_UNLOCK;
				int busiType = PopConstant.BUSI_TYPE_NORMAL;
				//操作状态:8-单用户CPE参数重置成功
				int opStatus = PopConstant.OP_STATUS_COMPLATE_CPE_PARAMETER;
				CpeUserInfo cpeUserInfo = new CpeUserInfo();
				cpeUserInfo.setSubsid(subsid);
				cpeUserInfo.setProductNo(phoneNo);
				cpeUserInfo.setNetLockFlag(netLockFlag);
				cpeUserInfo.setBusiType(busiType);
				cpeUserInfo.setOpStatus(opStatus);
				cpeUserInfoDao.update(cpeUserInfo);
				
				Db.update("delete from cpe_user_lock_rel where subsid ='" + subsid + "'");
				log.info("delete from cpe_user_lock_rel where subsid ='" + subsid + "' 成功！");
			}
		}catch(Exception e){
			throw e;
		}
		
	}
	
	/**
	 * 去签约参数重置
	 * @param phoneNo
	 * @param policId
	 * @return
	 * @throws Exception
	 */
	private String doCpeUnUserPolicy(String phoneNo,String policId)throws Exception{
		String result = "0";	
		ISendPccInfoService sendPccInfoService 
			= SpringContext.getBean("sendPccInfoService", 
							ISendPccInfoService.class);
		try {
			//log.info("调用PCC接口sendPccInfoService.cpeUserReset(" + phoneNo + "," + policId + "," + loctions.toArray() + ")，开始CPE用户参数重置>>> >>>");
			log.info("因Redis集群故障开始对账号信息为phoneNo:" + phoneNo + ",policId:" 
					+ policId + "CPE用户做去签约操作>>> >>>");
			result = sendPccInfoService.cpeUnUserPolicy(phoneNo, policId);
			//log.info("调用PCC接口sendPccInfoService.cpeUserReset(" + phoneNo + "," + policId + "," + loctions.toArray() + ")，CPE用户参数重置成功！");
			log.info("因Redis集群故障开始对账号信息为phoneNo:" + phoneNo + ",policId:" 
					+ policId + "CPE用户做去签约操作成功！");
			return result;
		} catch (Exception e) {
			//log.info("调用PCC接口sendPccInfoService.cpeUserReset(" + phoneNo + "," + policId + "," + loctions.toArray() + ")，CPE用户参数重置失败！");
			log.info("因Redis集群故障开始对账号信息为phoneNo:" + phoneNo + ",policId:" 
					+ policId + "CPE用户去签约操作失败！");
			throw new Exception("CPE单用户去签约参数重置失败！");
		}
	}
	
	/**
	 * 记录CPE开户或者锁网数据
	 * @param subsid
	 * @param phoneNo
	 * @param stationCode
	 * @param lacCiDecId
	 * @param userLocationList
	 */
	public void doRecordCPEInstall(String subsid, String phoneNo, 
			List<String> insertUserLocationList,boolean isInstallCpe){
		
		try {
			
			/*
			 * 7. 更新数据库锁网信息
			 * 可异步处理
			 */
			CpeUserLockRelBean cpeUserLockRelBean = new CpeUserLockRelBean();
			cpeUserLockRelBean.setSubsid(subsid);
			cpeUserLockRelBean.setProductNo(phoneNo);
			try {
				if(insertUserLocationList.size() > 0){
					cpeUserLockRelDao.batchInsert(cpeUserLockRelBean,insertUserLocationList);
				}else{
					return;
				}
			} catch (Exception e) {
				log.error(e.getMessage());
			}
			
			/*
			 * 6. 将CPE和锁网小区关系存入Redis，key:"unl:" + subsid
			 * 可异步处理
			 */
			for(String userLocation : insertUserLocationList){
				try {
					//单机版Redis
					//RedisUtils.sadd(PopConstant.REDIS_UNL_PREFIX + subsid, userLocationList.get(0));
					
					//Redis集群版代码
					JedisClientUtil.sadd(PopConstant.REDIS_UNL_PREFIX + subsid, userLocation);
				} catch (Exception e) {
					log.error("保存CPECPE用户编码为" + subsid + ", 手机号码为" + phoneNo + "的锁网小区关系失败！",e);
				}
			}
			
			if(isInstallCpe){
				List<DimCpeStation> dimCpeStationList = dimCpeStationDao.queryPosition(insertUserLocationList.get(0));
				if(dimCpeStationList != null && dimCpeStationList.size() > 0){
					CpeUserInfo cpeUserInfo = new CpeUserInfo();
					cpeUserInfo.setSubsid(subsid);
					cpeUserInfo.setCityName(dimCpeStationList.get(0).getCityName());
					cpeUserInfo.setCountyName(dimCpeStationList.get(0).getCountyName());
					cpeUserInfoDao.updatePosition(cpeUserInfo);
				}else{
					log.error("从基站维表和基站小区维表中无法查到CPE's subsid: " + subsid 
							+ ",userLocaiton: " + insertUserLocationList.get(0) + "的所属县 和 所属市！");
				}
			}
		} catch (Throwable e) {
			log.error(e.getMessage(),e);
		}
	}
	
	/**
	 * 统计Cpe的上网小区userLocation的访问频次，
	 * 统计的生命周期在pop.properties文件中配置，
	 * 配置项为CPE_POSITION_STATISTICS_PERIOD
	 * @param subsid
	 * @param userLocationList
	 */
	public void doStatisticsCpeUserLocation(String subsid, 
			List<String> userLocationList)throws Exception{
		//#CPE上网位置统计周期（单位：小时），默认7天
		String cpePositionStatisticsPeriodStr = Configure.getInstance().
					getProperty("CPE_POSITION_STATISTICS_PERIOD");
		int cpePositionStatisticsPeriod = 
					Strings.isNullOrEmpty(cpePositionStatisticsPeriodStr) 
					? CPE_POSITION_DEFAULT_STATISTICS_PERIOD 
					: Integer.parseInt(cpePositionStatisticsPeriodStr);
		try {
			for(String userLoction : userLocationList){
				//单机版Redis
				/*Double count = RedisUtils.zincrby(PopConstant.REDIS_CPE_USERLOCATION_STATISTICS_PREFIX + subsid, 
						1, userLoction);*/
				
				//Redis集群版代码
				Double count = JedisClientUtil.zincrby(PopConstant.REDIS_CPE_USERLOCATION_STATISTICS_PREFIX + subsid, 
						1, userLoction);
				
				if(count.intValue() == 1){//刚开户
					//单机版Redis
					//RedisUtils.expire(PopConstant.REDIS_CPE_USERLOCATION_STATISTICS_PREFIX + subsid, 3600 * cpePositionStatisticsPeriod);
					
					//Redis集群版代码
					JedisClientUtil.expire(PopConstant.REDIS_CPE_USERLOCATION_STATISTICS_PREFIX + subsid, 3600 * cpePositionStatisticsPeriod);
				}
			}
		} catch (Exception e) {
			throw e;
		}
	}
	
	/**
	 * 在Local上生成反馈数据文件
	 * @param cpeStatusChangeReponseList
	 * fileName，例如：BOSS2POP_CPEINSTALL_20160617102030001.txt
	 */
	private void writeFeedbackFile(String fileName,String resultFlag){
		String fullfileName = feedbackFileDirectory + File.separator 
				+ fileName + "." + feedbackFileSuffix;
		
		String createTime = fileName.substring(feedbackFilePrefix.length(), 
				   fileName.length() - serialNoLength - 1 - fileSuffix.length());
		
		if (!new File(fileName).exists()) {
			try {
				new File(fileName).createNewFile();
			} catch (IOException e) {
					log.error("创建反馈文件失败！",e);
			}
		}
		
		try {
			String line = createTime + ",1," 
					+ resultFlag;
			Files.write(line, new File(fullfileName), Charset.forName(charsetName));
			log.info("生成CPE开户反馈文件" + fullfileName + "成功");
		} catch (IOException e) {
			log.error(e);
		}
		
	}
	
}