package com.ai.bdx.pop.service.impl;

import java.io.File;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import redis.clients.jedis.exceptions.JedisException;

import com.ai.bdx.pop.adapter.buffer.FileCacheBuffer;
import com.ai.bdx.pop.adapter.cache.ReadedFileCache;
import com.ai.bdx.pop.adapter.dao.ICpeUserInfoDao;
import com.ai.bdx.pop.adapter.dao.ICpeUserLockRelDao;
import com.ai.bdx.pop.adapter.dao.IDimCpeLacCiDao;
import com.ai.bdx.pop.adapter.schedule.ReadFileThread;
import com.ai.bdx.pop.bean.CpeAccessibleLacCiBean;
import com.ai.bdx.pop.bean.CpeLockLacCiBean;
import com.ai.bdx.pop.bean.CpeUserInfo;
import com.ai.bdx.pop.jedis.JedisClientUtil;
import com.ai.bdx.pop.service.ICpeManagerService;
import com.ai.bdx.pop.util.PopConstant;
import com.ai.bdx.pop.util.SpringContext;
import com.ai.bdx.pop.util.ftp.ApacheFtpUtil;
import com.ai.bdx.pop.util.ftp.FtpCONST;
import com.ai.bdx.pop.util.ftp.FtpConfig;
import com.ai.bdx.pop.util.ftp.FtpConfigure;
import com.ai.bdx.pop.wsclient.ISendPccInfoService;
import com.asiainfo.biframe.utils.config.Configure;
import com.asiainfo.biframe.utils.string.StringUtil;
import com.google.common.base.Splitter;
import com.google.common.base.Stopwatch;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.IAtom;

/**
 * Cpe参数重置
 * @author hpa
 *
 */
public class CpeManagerServiceImpl implements
		ICpeManagerService {
	private static final Logger log = LogManager.getLogger();
	private FtpConfig ftpConfig;
	private ExecutorService taskExec;
	/**
	 * 读文件线程池大小
	 */
	private static Integer READ_FILE_THREAD_POOL_SIZE;
	
	/**
	 * 读文件策略
	 * 1:按文件名截取时间循序,一个一个发送 (优点:保证文件时间顺序)效率较低. 
	 * 2:截取文件名获取时间,按分钟批次发送(并发读取、发送当前分钟内的，保证数据全部发送完毕,再发送下一分钟数据)效率中 . 
	 * 3:一次性并发发送多个文件数据(缺点：可能同时发送多个时间段的)效率高
	 */
	private static String USE_FILE_POLICY;
	/*static{
		READ_FILE_THREAD_POOL_SIZE = 
				Integer.parseInt(Strings.isNullOrEmpty(Configure.getInstance().getProperty("READ_FILE_THREAD_POOL_SIZE")) 
						? "1" : Configure.getInstance().getProperty("READ_FILE_THREAD_POOL_SIZE"));
		USE_FILE_POLICY = Configure.getInstance().getProperty("USE_FILE_POLICY");
	}*/
	
	private List<String> suffixs;
	
	private ICpeUserLockRelDao cpeUserLockRelDao;
	private IDimCpeLacCiDao dimCpeLacCiDao;
	private ICpeUserInfoDao cpeUserInfoDao;
	
	public void setCpeUserLockRelDao(ICpeUserLockRelDao cpeUserLockRelDao) {
		this.cpeUserLockRelDao = cpeUserLockRelDao;
	}
	
	public void setDimCpeLacCiDao(IDimCpeLacCiDao dimCpeLacCiDao) {
		this.dimCpeLacCiDao = dimCpeLacCiDao;
	}

	public void setCpeUserInfoDao(ICpeUserInfoDao cpeUserInfoDao) {
		this.cpeUserInfoDao = cpeUserInfoDao;
	}

	/**
	 * @Title: initFtpConfig
	 * @Description: 获取FTP配置信息，例如：HUAWEI_ERD_LOG 华为ERD日志文件信息
	 * @param     
	 * @return void 
	 * @throws
	 */
	public void initFtpConfig(String ftpConst){
		READ_FILE_THREAD_POOL_SIZE = 
				Integer.parseInt(Strings.isNullOrEmpty(Configure.getInstance().getProperty("READ_FILE_THREAD_POOL_SIZE")) 
						? "1" : Configure.getInstance().getProperty("READ_FILE_THREAD_POOL_SIZE"));
		USE_FILE_POLICY = Configure.getInstance().getProperty("USE_FILE_POLICY");
		
		ftpConfig = FtpConfigure.getInstance().getFtpConfigByTypes(ftpConst);
		taskExec = Executors.newFixedThreadPool(READ_FILE_THREAD_POOL_SIZE);
	}
	
	@Override
	public void getBoss2PopCpeInstallInfo() {
		initFtpConfig(FtpCONST.BOSS_CPE_INSTALL);
		String currentDateFormat = new SimpleDateFormat(
				ftpConfig.getFILETIMEFORMAT()).format(new Date());
		log.debug("本轮FTP文件读写时间周期：{}", currentDateFormat);
		suffixs = Lists.newArrayList(Splitter.on("|").split(
				ftpConfig.getFILESUFFIX()));
		downRemoteFile();
	}
	
	/**
	 * 下载远程文件
	 */
	private void downRemoteFile(){
		
		ApacheFtpUtil apacheFtp = null;
		try{
			apacheFtp = ApacheFtpUtil.getInstance(ftpConfig.getFTP_ADDRESS(), 
					Integer.parseInt(ftpConfig.getFTP_PORT()), ftpConfig.getFTP_USER(), 
					ftpConfig.getFTP_PASSWORD(), ftpConfig.getFTP_ENCODE());
			
			String ftpPath = ftpConfig.getFTP_PATH();
			
			String yyyy = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
			String MM = String.valueOf(Calendar.getInstance().get(Calendar.MONTH)+1);
			String dd = String.valueOf(Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
			if(MM.length() == 1){
				MM = "0"+MM;
			}
			if(dd.length() == 1){
				dd = "0"+dd;
			}
			ftpPath = ftpPath.replaceAll("\\{yyyy\\}", yyyy)
					.replaceAll("\\{MM\\}", MM)
					.replaceAll("\\{dd\\}", dd);
			List<String> fileList = apacheFtp.listFile(ftpPath);
			apacheFtp.changeDir(ftpPath);
			Collections.sort(fileList);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			String toady = sdf.format(new Date());
			
			boolean isDelRemote=false;
			String is_del_remote=ftpConfig.getISDELETE_REMOTE();
			if(StringUtil.isNotEmpty(is_del_remote)&&"1".equals(is_del_remote)){
				 isDelRemote=true;
			}
			//判断处理文件策略
			if("1".equals(USE_FILE_POLICY)) {
				for(String tempFileName:fileList){
					if(checkFileName(tempFileName)){
						if(tempFileName.indexOf(toady) != -1){
						String fileCacheString = tempFileName.substring(ftpConfig.getFILEPREFIX().length(),tempFileName.length()-ftpConfig.getFILESUFFIX().length());
						if(!ReadedFileCache.getInstance().isReaded(fileCacheString,ftpConfig.getFILETIMEFORMAT(),Integer.parseInt(ftpConfig.getPERIOD())*1000)) {
							log.debug("FTP目录下匹配的文件名为:"+tempFileName);
							String targetLocalFile = ftpConfig.getLOCAL_PATH() + tempFileName;
							if(!ftpConfig.getLOCAL_PATH().endsWith(File.separator)){
								targetLocalFile = ftpConfig.getLOCAL_PATH() + File.separator +tempFileName;
							}
							Stopwatch timeWatch = Stopwatch.createStarted();
							boolean isDown = apacheFtp.download(targetLocalFile, tempFileName , isDelRemote);
							log.debug("下载文件{}耗时{}秒:",tempFileName,timeWatch.elapsed(TimeUnit.SECONDS));
							if(isDown){
								final String targetLocalFileTmp = targetLocalFile;
								log.info("处理文件：{}",targetLocalFileTmp);
									 taskExec.execute(new Thread() {
											public void run() {
												FileCacheBuffer.getInstance().putFile(new File(targetLocalFileTmp), ftpConfig);
											}
									 });
							}
						}
					 }
					}
				}
			}else if("2".equals(USE_FILE_POLICY)){
				String minuteCycle = "";
				for(String tempFileName:fileList){
					if(checkFileName(tempFileName)){
						if(tempFileName.indexOf(toady) != -1){
						//20140411_174014004.AVL
						String fileCacheString = tempFileName.substring(ftpConfig.getFILEPREFIX().length(),tempFileName.length()-ftpConfig.getFILESUFFIX().length());
						if(!ReadedFileCache.getInstance().isReaded(fileCacheString,ftpConfig.getFILETIMEFORMAT(),Integer.parseInt(ftpConfig.getPERIOD())*1000)) {
							log.debug("FTP目录下匹配的文件名为:"+tempFileName);
							String targetLocalFile = ftpConfig.getLOCAL_PATH() + tempFileName;
							if(!ftpConfig.getLOCAL_PATH().endsWith(File.separator)){
								targetLocalFile = ftpConfig.getLOCAL_PATH() + File.separator +tempFileName;
							}
							Stopwatch timeWatch = Stopwatch.createStarted();
							boolean isDown = apacheFtp.download(targetLocalFile, tempFileName , isDelRemote);
							log.debug("下载文件{}耗时{}秒:",tempFileName,timeWatch.elapsed(TimeUnit.SECONDS));
							if(isDown){
								final String targetLocalFileTmp = targetLocalFile;
								//截取文件名获取时间周期(分钟)
								String fileNameTmp= tempFileName.substring(ftpConfig.getFILEPREFIX().length());
								
								//if("hubei".equals(provice)){
									//湖北文件格式 timeStr = 92271-20150411233800   targetKey=20150411233800   value=92271-20150411233800
									fileNameTmp = fileNameTmp.substring(fileNameTmp.length()-ftpConfig.getFILETIMEFORMAT().length(), fileNameTmp.length());
								/*}else{
									fileNameTmp = fileNameTmp.substring(0, ftpConfig.getFILETIMEFORMAT().length());
								}*/
								
								if("".equals(minuteCycle)){
									minuteCycle = fileNameTmp;
									log.info("当前时间批次{}", fileNameTmp);
								}else if(minuteCycle.equals(fileNameTmp)){
									//当前文件与上一个文件属于同一批次,暂时不做处理
								}else{
									//当前文件与上一个文件不属于同一批次,等待上一批次全部执行完
								   taskExec.shutdown();  
								   while (true) {  
								     if (taskExec.isTerminated()) {  
								    	 log.info("当前时间批次{}", fileNameTmp);
								    	 minuteCycle = fileNameTmp;
								         break;  
								     } 
								     //log.info("上个时间批次{},未执行完成,等待..", minuteCycle);
								     Thread.sleep(1000);  
								   } 
								   taskExec=Executors.newFixedThreadPool(READ_FILE_THREAD_POOL_SIZE);
								}
								Thread t = new ReadFileThread(new File(targetLocalFileTmp),ftpConfig,apacheFtp);
								taskExec.execute(t);
							}
						}
					  }
					}
				}
			} else if ("3".equals(USE_FILE_POLICY)) {
				for (String tempFileName : fileList) {
					if (checkFileName(tempFileName)) {
						if (tempFileName.indexOf(toady) != -1) {
							/*
							 * 例如：DPI-LOG-20160512110706001.AVL 
							 * 计算文件的时间戳部分字符串
							 */
							String fileCacheString = tempFileName.substring(
									ftpConfig.getFILEPREFIX().length(),
									tempFileName.length()
											- ftpConfig.getFILESUFFIX()
													.length());
							if (!ReadedFileCache.getInstance().isReaded(
									fileCacheString,
									ftpConfig.getFILETIMEFORMAT(),
									Integer.parseInt(ftpConfig.getPERIOD()) * 1000)) {
								log.debug("FTP目录下匹配的文件名为:" + tempFileName);
								String targetLocalFile = ftpConfig
										.getLOCAL_PATH() + tempFileName;
								if (!ftpConfig.getLOCAL_PATH().endsWith(
										File.separator)) {
									targetLocalFile = ftpConfig.getLOCAL_PATH()
											+ File.separator + tempFileName;
								}
								Stopwatch timeWatch = Stopwatch.createStarted();
								boolean isDown = apacheFtp.download(
										targetLocalFile, tempFileName,
										isDelRemote);
								log.debug("下载文件{}耗时{}秒:", tempFileName,
										timeWatch.elapsed(TimeUnit.SECONDS));
								if (isDown) {
									final String targetLocalFileTmp = targetLocalFile;
									taskExec.execute(new ReadFileThread(
											new File(targetLocalFileTmp),
											ftpConfig,apacheFtp));
								}
							}
						}
					}
				}
			}
		}catch (Exception e) {
			log.error("",e);
		}finally{
			if(apacheFtp != null){
				apacheFtp.forceCloseConnection();
			}
		}
	}
	
	private boolean checkFileName(String fileName) {
		if (!fileName.startsWith(ftpConfig.getFILEPREFIX())) {
			return false;
		}
		for (String suffix : suffixs) {
			if (fileName.endsWith(suffix)) {
				return true;
			}
		}
		return false;
	}
	
	@Override
	public void cpeUserReset(final String subsid, String productNo,
			String userLocations,int cpeLockedNetLacCiCount,int tableNetLockFlag)throws JedisException, Exception {
		ReentrantLock lock = new ReentrantLock();
		try{
			lock.lock();
			/**
			 * 1. 更新Redis中用户锁网（表）的LacCi信息
			 * 1.1 对前端传入的userLocations做处理
			*/
			//需要从redis中取得使用统计最多的lacCi个数
			Integer needLacCiCount = 0;
			String[] userLocationArray;
			List<String> initUserLocationList = null;
			final List<String> sqlList = Lists.newArrayList();
				
			log.info("CPE设备:" + subsid + ",手机号：" + productNo 
					+ "用户的单用户参数重置时前端页面选择的userLocations为: " + userLocations + ",userLocations的长度: " + userLocations.length() + ",netLockFlag: " + tableNetLockFlag);
			
			if(tableNetLockFlag != PopConstant.NET_LOCK_FLAG_LOCK_1 
					|| (tableNetLockFlag == PopConstant.NET_LOCK_FLAG_LOCK_1 && !Strings.isNullOrEmpty(userLocations))){
				if(Strings.isNullOrEmpty(userLocations)){//前端没有选择锁网小区时
					needLacCiCount = cpeLockedNetLacCiCount;
					userLocationArray = new String[]{};
					initUserLocationList = new ArrayList<String>();
				}else{
					userLocationArray = userLocations.split(",");
					needLacCiCount = cpeLockedNetLacCiCount - userLocationArray.length;
						
					/**
					 * 如果前端选择的LacCi个数不够锁网最大值cpeLockedNetLacCiCount个时，从Redis最近1个星期该CPE
					 * 常用lac_ci次数统计缓存（表key:cpeUserLocationStatistics:cpe号,value:sorted_set 根据userLocation的访
					 * 问次数降序排序）中取出（cpeLockedNetLacCiCount - 前端传入LacCi个数）
					*/
					initUserLocationList = 
							new ArrayList<String>(Arrays.asList(new String[userLocationArray.length]));
					Collections.copy(initUserLocationList, Arrays.asList(userLocationArray));
				}
				log.info("CPE设备:" + subsid + ",手机号：" + productNo 
						+ "用户还需要从redis的访问频次中获取 " + needLacCiCount + " 个userLocation");
				try{
					if(needLacCiCount > 0){//需要查询Redis，找到7天内使用最多的cpeLockedNetLacCiCount个小区
						Set<String> userLocationSet = new HashSet<String>();
						
						/*
						 * Redis单机版本代码
						 * 从Redis的有序集合中取出key为PopConstant.REDIS_CPE_USERLOCATION_STATISTICS_PREFIX + subsid的
						 * 0 至(cpeLockedNetLacCiCount - 1)个访问次数最多的小区的userLocation
						 */
						/*lacCiSet = RedisUtils.zrevrange(PopConstant.REDIS_CPE_USERLOCATION_STATISTICS_PREFIX + subsid, 
								0, cpeLockedNetLacCiCount - 1);*/
						
						/*
						 * Redis集群版本代码
						 * 从Redis的有序集合中取出key为PopConstant.REDIS_CPE_USERLOCATION_STATISTICS_PREFIX + subsid的
						 * 0 至(cpeLockedNetLacCiCount - 1)个访问次数最多的小区的userLocation
						 */
						try{
							userLocationSet = JedisClientUtil.zrevrange(PopConstant.REDIS_CPE_USERLOCATION_STATISTICS_PREFIX + subsid, 
									0, cpeLockedNetLacCiCount - 1);
							
							/**
							 * Redis抛异常测试
							 */
							/*String throwExceptionCPE = "15871665320,13687154577,15271473854,18271854662";
							if(throwExceptionCPE.contains(productNo)){
								throw new JedisException("Redis抛异常测试（手机号码为"+productNo+"）");
							}*/
						}catch(Exception e){
							log.error("查询Redis集群的访问频次异常，请联系运维人员！", e);
							throw new JedisException("参数重置失败，请稍后重试！");
						}
						
						String userLocation = null;
						if(userLocationSet != null){
							Iterator<String> userLocationIterator = userLocationSet.iterator();
							while (userLocationIterator.hasNext()) {//添加去重后的userLocation
								userLocation = userLocationIterator.next();
								if(needLacCiCount > 0 && !initUserLocationList.contains(userLocation)){
									initUserLocationList.add(userLocation);
									needLacCiCount--;
								}
								if(needLacCiCount <= 0){
									break;
								}
							}
						}
					}
				}catch (JedisException e) {
					String message = "CpeManagerServiceImpl.cpeUserReset方法中连Redis执行操作：zrevrange " 
							+ PopConstant.REDIS_CPE_USERLOCATION_STATISTICS_PREFIX + subsid + " " + 0 + " " + needLacCiCount + "异常！";
					log.error(message,e);
					log.error("从用户常用上网排行榜中获取" + needLacCiCount + "小区的userLocation失败！");
					throw new JedisException(e.getMessage());
				}catch(Exception e){
					log.error("", e);
					throw new Exception("计算锁网小区异常，参数重置失败！");
				}
				
				final StringBuffer insertSql = new StringBuffer();
				insertSql.append("INSERT INTO CPE_USER_LOCK_REL(" +
						"SUBSID," +
						"PRODUCT_NO," +
						"LOCKED_LAC_CI," +
						"USER_LOCATION," +
						"STATION_CODE)");
				insertSql.append(" SELECT '").append(subsid).append("','").append(productNo).append("',");
				insertSql.append("LAC_CI_DEC_ID").append(",").append("USER_LOCATION").append(",").append("STATION_CODE ");
				insertSql.append("FROM DIM_CPE_LAC_CI  WHERE USER_LOCATION = '%s' ");
				
				/**
				 * 添加客户人员选择的小区和最近一小时该CPE使用最频繁的小区，总和达到CPE_LOCKEDNET_LACCICOUNT个
				 */
				int lacCiCount = 0;
				//List<String> userLocationList = new ArrayList<String>();
				String sql = null;
				for(String userLocation : initUserLocationList){
					lacCiCount++;
						
					//userLocationList.add(userLocation);
					sql = String.format(insertSql.toString(),userLocation);
					sqlList.add(sql);
					log.info("CPE设备:" + subsid + ",手机号：" + productNo + "向CPE锁网关系表中插入数据的sql: " + sql);
				}
				log.info("CPE设备:" + subsid + ",手机号：" + productNo + "向CPE锁网关系表中插入数据的批量sql个数为: " + sqlList.size());
				
				int opStatus = PopConstant.OP_STATUS_EXCEPTION;
				int busiType = 1;
				int netLockFlag = 0;//未锁网
				try{
					String policyNo = Db.queryStr("SELECT R.STRATEGY_CODE FROM CPE_USER_INFO C " +
							"LEFT JOIN DIM_NET_TYPE_STRATEGY_REL R " +
							"ON C.NET_TYPE = R.NET_TYPE " +
							"WHERE C.SUBSID = ? ", subsid );
					//设置
					String result = "0";
					
					if(lacCiCount == 0){//调用带去签约的单用户参数重置接口
						result = cpeUnUserPolicy(productNo, policyNo);
						if(result == "1"){//成功
							//操作状态:8-单用户CPE参数重置成功
							opStatus = PopConstant.OP_STATUS_COMPLATE_CPE_PARAMETER;
						}
						netLockFlag = PopConstant.NET_LOCK_FLAG_UNLOCK;
						busiType = PopConstant.BUSI_TYPE_NORMAL;
					}else if(lacCiCount < cpeLockedNetLacCiCount ){//调用(非去签约的)单用户参数重置接口
						//result = doCpeUserReset(productNo, policyNo, initUserLocationList);
						result = cpeUserNetLock(productNo, policyNo, initUserLocationList);
						if(result == "1"){
							netLockFlag = PopConstant.NET_LOCK_FLAG_UNLOCK_2;
							busiType = PopConstant.BUSI_TYPE_NORMAL;
							//操作状态:8-单用户CPE参数重置成功
							opStatus = PopConstant.OP_STATUS_COMPLATE_CPE_PARAMETER;
						}else{
							netLockFlag = PopConstant.NET_LOCK_FLAG_UNLOCK;
							busiType = PopConstant.BUSI_TYPE_NORMAL;
						}
					}else if(lacCiCount >= cpeLockedNetLacCiCount){//调用(非去签约的)单用户参数重置接口
						//result = doCpeUserReset(productNo, policyNo, initUserLocationList);
						result = cpeUserNetLock(productNo, policyNo, initUserLocationList);
						netLockFlag = PopConstant.NET_LOCK_FLAG_LOCK_1;
						if(result == "1"){
							netLockFlag = PopConstant.NET_LOCK_FLAG_LOCK_1;
							busiType = PopConstant.BUSI_TYPE_NORMAL;
							//操作状态:8-单用户CPE参数重置成功
							opStatus = PopConstant.OP_STATUS_COMPLATE_CPE_PARAMETER;
						}else{
							netLockFlag = PopConstant.NET_LOCK_FLAG_UNLOCK;
							busiType = PopConstant.BUSI_TYPE_NORMAL;
						}
					}
					Db.update("UPDATE CPE_USER_INFO SET NET_LOCK_FLAG = ?,BUSI_TYPE = ?, OP_STATUS = ? WHERE SUBSID = ?"
							, netLockFlag, busiType, opStatus, subsid);
					if("1".equals(result)){
						updateCpeLockRel(subsid, initUserLocationList, sqlList);
					}
				}catch(Exception e){
					log.error("调用单用户参数重置异常！将CPE("+subsid+")的锁网状态置为0-未开户，业务状态置为1-正常。", e);
					netLockFlag = PopConstant.NET_LOCK_FLAG_UNLOCK;
					busiType = PopConstant.BUSI_TYPE_NORMAL;
					opStatus = PopConstant.OP_STATUS_EXCEPTION;
					Db.update("UPDATE CPE_USER_INFO SET NET_LOCK_FLAG = ?,BUSI_TYPE = ?, OP_STATUS = ? WHERE SUBSID = ?"
							, netLockFlag, busiType, opStatus, subsid);
					throw new Exception("调用PCC接口异常，请稍后重置！");
				}
			}else{//对于锁网状态为1-锁网完成的用户去签约
				String policyNo = Db.queryStr("SELECT R.STRATEGY_CODE FROM CPE_USER_INFO C " +
						"LEFT JOIN DIM_NET_TYPE_STRATEGY_REL R " +
						"ON C.NET_TYPE = R.NET_TYPE " +
						"WHERE C.SUBSID = ? ", subsid );
				String result = cpeUnUserPolicy(productNo, policyNo);
				if(result == "1"){//成功
					int netLockFlag = PopConstant.NET_LOCK_FLAG_UNLOCK;
					int busiType = PopConstant.BUSI_TYPE_NORMAL;
					int opStatus = PopConstant.OP_STATUS_COMPLATE_CPE_PARAMETER;
					Db.update("UPDATE CPE_USER_INFO SET NET_LOCK_FLAG = ?,BUSI_TYPE = ?, OP_STATUS = ? WHERE SUBSID = ?", netLockFlag,busiType, opStatus, subsid);
					
					//删除Redis和数据库中的锁网关系
					delCpeLockRel(subsid);
					
					try{
						//删除CPE访问频次
						JedisClientUtil.del(PopConstant.REDIS_CPE_USERLOCATION_STATISTICS_PREFIX + subsid);
						log.info("单用户参数重置操作删除锁网状态为1-锁网完成的CPE(CPE用户编码为" + subsid + ",手机号码为" + productNo + ")手机号码的Redis中访问频次:del cpeUserLocationStatistics:" + subsid + " 成功！");
					}catch(Exception e){
						log.error("单用户参数重置操作删除锁网状态为1-锁网完成的CPE(CPE用户编码为" + subsid + ",手机号码为" + productNo + ")手机号码的Redis中访问频次:del cpeUserLocationStatistics:" + subsid + " 失败！");
						throw new JedisException("参数重置失败，请稍后重试！");
					}
				}
				
			}
			
		}catch(JedisException e){
			throw new JedisException(e.getMessage());
		}catch(Exception e){
			throw e;
		}finally{
			lock.unlock();
		}
		
	}
	
	/**
	 * 先删除Redis和数据库中的CPE锁网关系，再向Redis和数据库中添加CPE锁网关系
	 * @param subsid
	 * @param initUserLocationList
	 * @param sqlList
	 */
	private void updateCpeLockRel(final String subsid,
			List<String> initUserLocationList, 
			final List<String> sqlList)throws JedisException, SQLException{
		/**
		 * 清除Redis用户锁网(表key:unl:cpe号,value:set 锁网的lac_ci )中该Cpe的数据
		 */
		try {
			/*
			 * Redis单机版本代码
			 */
			/*RedisUtils.del(PopConstant.REDIS_UNL_PREFIX + subsid);*/
			
			/*
			 * Redis集群版本代码
			 * 
			 */
			JedisClientUtil.del(PopConstant.REDIS_UNL_PREFIX + subsid);
			//JedisClientUtil.expire(PopConstant.REDIS_UNL_PREFIX + subsid, 0);
		} catch (Exception e) {
			String message = "CpeManagerServiceImpl.cpeUserReset方法中连Redis执行操作：expire " 
					+ PopConstant.REDIS_UNL_PREFIX + subsid + " " + 0 + "异常！";
			log.error(message + e);
			log.error("删除用户锁网userLocation小区信息失败！");
			throw new JedisException("删除用户锁网userLocation小区信息失败！");
		}
		
		try{
			for(String userLocation : initUserLocationList){
				/*
				 * Redis单机版本代码
				 */
				/*RedisUtils.sadd(PopConstant.REDIS_UNL_PREFIX + subsid, userLocation);*/
				
				/*
				 * Redis集群版本代码
				 * 
				 */
				JedisClientUtil.sadd(PopConstant.REDIS_UNL_PREFIX + subsid, userLocation);
			}
		}catch (Exception e) {
			String message = "CpeManagerServiceImpl.cpeUserReset方法中连Redis执行sadd操作失败！";
			log.error(message, e);
			log.error("添加用户锁网LacCi小区信息失败！");
			throw new JedisException("添加用户锁网LacCi小区信息失败！");
		}
		
		Db.tx(new IAtom(){

			@Override
			public boolean run() throws SQLException {
				/**
				 * 删除用户锁网关系表中subsid对应的所有记录
				 */
				try {
					Db.update("delete from cpe_user_lock_rel where subsid ='" + subsid + "'");
					log.info("delete from cpe_user_lock_rel where subsid ='" + subsid + "' 成功！");
				} catch (Exception e) {
					String message = "CpeManagerServiceImpl.cpeUserReset方法中连数据库执行" 
							+ "delete from cpe_user_lock_rel where subsid ='" + subsid + "'" + "操作失败！";
					log.error(message, e);
					throw new SQLException("在数据库中删除用户锁网userLocation小区信息失败！");
				}
				
				
				/**
				 * 3. 将前端传入的N个（小于最大锁网值9个）LacCi和从Redis中Cpe使用统计（表）取的前（9个-
				 */
				try {
					if(sqlList.size()>0)
					{
						log.info("向数据库锁网关系表中开始插入" + sqlList.size() + " 记录");
						Db.batch(sqlList, sqlList.size());
						log.info("CPE设备:" + subsid + ",向数据库中的CPE锁网关系表中插入" + sqlList.size() + " 记录成功！");
					}
				} catch (Exception e) {
					String message = "CpeManagerServiceImpl.cpeUserReset方法中连数据库批量添加用户锁网LacCi小区信息操作失败！";
					log.error(message, e);
					throw new SQLException("在数据库中批量添加用户锁网userLocation小区信息失败！");
				}
				
				return true;
			}
		});
		
	}
	
	/**
	 * 删除Redis和数据库中的锁网关系
	 * @param subsid
	 * @param initUserLocationList
	 * @param sqlList
	 */
	private void delCpeLockRel(final String subsid)throws JedisException, SQLException{
		/**
		 * 清除Redis用户锁网(表key:unl:cpe号,value:set 锁网的lac_ci )中该Cpe的数据
		 */
		try {
			/*
			 * Redis单机版本代码
			 */
			/*RedisUtils.del(PopConstant.REDIS_UNL_PREFIX + subsid);*/
			
			/*
			 * Redis集群版本代码
			 * 
			 */
			JedisClientUtil.del(PopConstant.REDIS_UNL_PREFIX + subsid);
			//JedisClientUtil.expire(PopConstant.REDIS_UNL_PREFIX + subsid, 0);
		} catch (Exception e) {
			String message = "CpeManagerServiceImpl.cpeUserReset方法中连Redis执行操作：expire " 
					+ PopConstant.REDIS_UNL_PREFIX + subsid + " " + 0 + "异常！";
			log.error(message, e);
			log.error("删除用户锁网userLocation小区信息失败！");
			throw new JedisException("参数重置失败，请稍后重试！");
		}
		
		Db.tx(new IAtom(){

			@Override
			public boolean run() throws SQLException {
				/**
				 * 删除用户锁网关系表中subsid对应的所有记录
				 */
				try {
					Db.update("delete from cpe_user_lock_rel where subsid ='" + subsid + "'");
					log.info("delete from cpe_user_lock_rel where subsid ='" + subsid + "' 成功！");
				} catch (Exception e) {
					String message = "CpeManagerServiceImpl.cpeUserReset方法中连数据库执行" 
							+ "delete from cpe_user_lock_rel where subsid ='" + subsid + "'" + "操作失败！";
					log.error(message, e);
					throw new SQLException("在数据库中删除用户锁网userLocation小区信息失败！");
				}
				
				return true;
			}
		});
		
	}
	
	/**
	 * 
	 * @param phoneNo
	 * @param policId
	 * @param loctions
	 * @return
	 * @throws Exception
	 */
	private String doCpeUserReset(String phoneNo, String policId, List<String> loctions)throws Exception{
		String result = "0";	
		ISendPccInfoService sendPccInfoService 
			= SpringContext.getBean("sendPccInfoService", 
							ISendPccInfoService.class);
		try {
			//log.info("调用PCC接口sendPccInfoService.cpeUserReset(" + phoneNo + "," + policId + "," + loctions.toArray() + ")，开始CPE用户参数重置>>> >>>");
			log.info("账号信息为phoneNo:" + phoneNo + ",policId:" 
					+ policId + "userLocationList:" + loctions.toArray() + "的CPE用户开始参数重置>>> >>>");
			result = sendPccInfoService.cpeUserReset(phoneNo ,policId,loctions);
			//log.info("调用PCC接口sendPccInfoService.cpeUserReset(" + phoneNo + "," + policId + "," + loctions.toArray() + ")，CPE用户参数重置成功！");
			log.info("账号信息为phoneNo:" + phoneNo + ",policId:" 
					+ policId + "userLocationList:" + loctions.toArray() + "的CPE用户参数重置成功！");
			return result;
		} catch (Exception e) {
			//log.info("调用PCC接口sendPccInfoService.cpeUserReset(" + phoneNo + "," + policId + "," + loctions.toArray() + ")，CPE用户参数重置失败！");
			log.info("账号信息为phoneNo:" + phoneNo + ",policId:" 
					+ policId + "userLocationList:" + loctions.toArray() + "的CPE用户参数重置失败！");
			throw new Exception("CPE单用户参数重置失败！");
		}
	}
	
	/**
	 * 去签约参数重置
	 * @param phoneNo
	 * @param policId
	 * @return
	 * @throws Exception
	 */
	private String cpeUserNetLock(String phoneNo,String policId, List<String> userLocationList)throws Exception{
		String result = "0";	
		ISendPccInfoService sendPccInfoService 
			= SpringContext.getBean("sendPccInfoService", 
							ISendPccInfoService.class);
		try {
			//log.info("调用PCC接口sendPccInfoService.cpeUserReset(" + phoneNo + "," + policId + "," + loctions.toArray() + ")，开始CPE用户参数重置>>> >>>");
			log.info("账号信息为phoneNo:" + phoneNo + ",policId:" 
					+ policId + ",userLocationList个数："  
					+ userLocationList.size() +  "，CPE用户开始调用带userLocation的去签约参数重置接口>>> >>>");
			result = sendPccInfoService.cpeUserNetLock(phoneNo, policId, userLocationList);
			//log.info("调用PCC接口sendPccInfoService.cpeUserReset(" + phoneNo + "," + policId + "," + loctions.toArray() + ")，CPE用户参数重置成功！");
			log.info("账号信息为phoneNo:" + phoneNo + ",policId:" 
					+ policId + ",userLocationList个数："  
					+ userLocationList.size() +"，CPE用户调用带userLocation的去签约参数重置接口成功！");
			return result;
		} catch (Exception e) {
			//log.info("调用PCC接口sendPccInfoService.cpeUserReset(" + phoneNo + "," + policId + "," + loctions.toArray() + ")，CPE用户参数重置失败！");
			log.info("账号信息为phoneNo:" + phoneNo + ",policId:" 
					+ policId + ",userLocationList个数："  
					+ userLocationList.size() + "，CPE用户调用带userLocation的去签约参数重置接口失败！");
			throw new Exception("CPE单用户去签约参数重置失败！");
		}
	}
	
	/**
	 * 去签约参数重置
	 * @param phoneNo
	 * @param policId
	 * @return
	 * @throws Exception
	 */
	private String cpeUnUserPolicy(String phoneNo,String policId)throws Exception{
		String result = "0";	
		ISendPccInfoService sendPccInfoService 
			= SpringContext.getBean("sendPccInfoService", 
							ISendPccInfoService.class);
		try {
			//log.info("调用PCC接口sendPccInfoService.cpeUserReset(" + phoneNo + "," + policId + "," + loctions.toArray() + ")，开始CPE用户参数重置>>> >>>");
			log.info("账号信息为phoneNo:" + phoneNo + ",policId:" 
					+ policId + "CPE用户开始去签约参数重置>>> >>>");
			result = sendPccInfoService.cpeUnUserPolicy(phoneNo, policId);
			//log.info("调用PCC接口sendPccInfoService.cpeUserReset(" + phoneNo + "," + policId + "," + loctions.toArray() + ")，CPE用户参数重置成功！");
			log.info("账号信息为phoneNo:" + phoneNo + ",policId:" 
					+ policId + "CPE用户去签约参数重置成功！");
			return result;
		} catch (Exception e) {
			//log.info("调用PCC接口sendPccInfoService.cpeUserReset(" + phoneNo + "," + policId + "," + loctions.toArray() + ")，CPE用户参数重置失败！");
			log.info("账号信息为phoneNo:" + phoneNo + ",policId:" 
					+ policId + "CPE用户去签约参数重置失败！");
			throw new Exception("CPE单用户去签约参数重置失败！");
		}
	}
	
	private Runnable cpeUserResetRunnable(final String phoneNo, final String policId, final List<String> loctions){
		return new Runnable() {
			
			@Override
			public void run() {
				ISendPccInfoService sendPccInfoService 
					= SpringContext.getBean("sendPccInfoService", 
							ISendPccInfoService.class);
				try {
					log.info("调用PCC接口sendPccInfoService.cpeUserReset(" + phoneNo + "," + policId + "," + loctions.toArray() + ")，开始CPE用户参数重置");
					sendPccInfoService.cpeUserReset(phoneNo ,policId,loctions);
					log.info("调用PCC接口sendPccInfoService.cpeUserReset(" + phoneNo + "," + policId + "," + loctions.toArray() + ")，CPE用户参数重置成功！");
				} catch (Exception e) {
					log.info("调用PCC接口sendPccInfoService.cpeUserReset(" + phoneNo + "," + policId + "," + loctions.toArray() + ")，CPE用户参数重置失败！");
				}
			}
		};
	}

	@Override
	public List<CpeLockLacCiBean> queryLockedLacCi(String subsid,String productNo) throws Exception {
		try {
			return cpeUserLockRelDao.queryLockedLacCi(subsid, productNo);
		} catch (Exception e) {
			log.error(e.getMessage());
			throw new Exception(e.getMessage());
		}
	}

	@Override
	public List<CpeAccessibleLacCiBean> queryAccessibleLacCi(
			CpeAccessibleLacCiBean cpeAccessibleLacCiBean) throws Exception {
		try {
			return dimCpeLacCiDao.queryAccessibleLacCi(cpeAccessibleLacCiBean);
		} catch (Exception e) {
			log.error(e.getMessage());
			throw new Exception(e.getMessage());
		}
	}

	@Override
	public void batchUpdate(List<CpeUserInfo> cpeUserInfoList,String fileName) throws Exception {
		//pop_adapter将redis改为缓存后，注掉此部分代码。
		/*try{
			for(CpeUserInfo cpeUserInfo : cpeUserInfoList){
				JedisClientUtil.set(PopConstant.REDIS_CPE_PREFIX + cpeUserInfo.getSubsid(),"1");
			}
		}catch(JedisException e){//操作redis异常时,可以只把数据记录到数据库中，不影响使用
			log.error("将CPE开户数据文件fileName：" + "," + fileName + "，其内容：" 
					+ new Gson().toJson(cpeUserInfoList) + "在Redis中记录失败！",e);
		}*/
		
		try {
			cpeUserInfoDao.batchUpdate(cpeUserInfoList,fileName);
		}catch (Exception e) {
			log.error(e.getMessage());
			throw new Exception("将CPE开户数据文件fileName：" + "," + fileName + "在数据库中记录" 
					+ new Gson().toJson(cpeUserInfoList) + "失败！");
		}
	}

	@Override
	public List<Map<String, Object>> queryBatchResetCpe(int netLockFlag, int busiType)
			throws Exception {
		try{
			List<Map<String, Object>> map =  cpeUserInfoDao.query(netLockFlag, busiType);
			return map;
		}catch(Exception e){
			log.error(e.getMessage());
			throw new Exception("根据锁网状态、业务状态、用户状态统计CPE信息失败！");
		}
	}
	
	public List<Map<String, Object>> queryAllResetByFlag(Map<String,Object> param) throws Exception
	{
		try{
			List<Map<String, Object>> map =  cpeUserInfoDao.queryAllResetByFlag(param);
			return map;
		}catch(Exception e){
			log.error(e.getMessage());
			throw new Exception("根据锁网状态、业务状态、用户状态统计CPE信息失败！");
		}
	}
	

}
