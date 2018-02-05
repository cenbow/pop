package com.ai.bdx.pop.service.impl;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import redis.clients.jedis.exceptions.JedisConnectionException;

import com.ai.bdx.pop.bean.BatchStationBean;
import com.ai.bdx.pop.bean.ImportBatchCutoverBean;
import com.ai.bdx.pop.bean.ImportCpeLacCiBean;
import com.ai.bdx.pop.dao.BatchStationDao;
import com.ai.bdx.pop.service.BatchStationService;
import com.ai.bdx.pop.util.PopConstant;
import com.ai.bdx.pop.util.SpringContext;
import com.ai.bdx.pop.wsclient.impl.SendPccInfoServiceImpl;
import com.ailk.bdx.pop.adapter.jedis.JedisClientUtil;
import com.asiainfo.biapp.pop.Exception.MysqlDataAccessExcetion;
import com.asiainfo.biapp.pop.redis.RedisClient;

public class BatchStationServiceImpl implements BatchStationService {
	private BatchStationDao batchStationDao;
	private static Logger log = LogManager.getLogger();
	public BatchStationDao getBatchStationDao() {
		return batchStationDao;
	}
	public void setBatchStationDao(BatchStationDao batchStationDao) { 
		this.batchStationDao = batchStationDao;
	}
	@Override
	public Map<String, Object> cpeBsBatchcg(List<BatchStationBean>beanList ,List<Map<String, Object>> list)
			throws MysqlDataAccessExcetion,Exception {
		SendPccInfoServiceImpl sendPcc = SpringContext.getBean("sendPccInfoService",SendPccInfoServiceImpl.class);
		//生产代码
		Map<String, Object> resultsMap = sendPcc.cpeBsBatchcg(list);
		//调用pcc接口成功0，的所有手机号集合success
		List<String> success=(List) resultsMap.get("1");
		List<BatchStationBean> beanListForSuccess=getBeanListForSuccess(beanList,success);
	
			if(success.size()>0){
						batchStationDao.updateUserResetLog(beanListForSuccess);
		
			 			batchStationDao.updateUserLockedInfo(beanListForSuccess);
			 			log.info("调用pcc接口结束，成功的手机号为："+success.toString());
			 }else{
				 		log.error("基站割接时调用pcc接口失败");
			 }
		
		return resultsMap;
	}
	
	/**
	 * 根据subsid查询策略号
	 * 
	 */
		public List<Map<String,Object>>  queryStrategyCodeBySubsid(String subsid)throws MysqlDataAccessExcetion{
			List<Map<String,Object>>  list = batchStationDao.queryStrategyCodeBySubsid(subsid);
			return list;
		}
		
		/**
		 * 根据手机号查询重置日志中是否有记录
		 */
		public int queryUserResetLogByMobile(String mobile){
			int count = batchStationDao.queryUserResetLogByMobile(mobile);
			return count;
		}
		
		public List<BatchStationBean> getBeanListForSuccess(List<BatchStationBean>beanList,List<String> success){
			int c=0;
			for(int i=0;i<beanList.size();i++){
				boolean isExit=false;
//				System.out.println("bean="+beanList.get(i).toString());
				c++;
				String phone = beanList.get(i).getProductNo();
				for(int j=0;j<success.size();j++){
				
					String mobile = success.get(j);
					if(phone.equals(mobile)){
						isExit=true;
						continue;
					}
				}
				System.out.println("第"+c+"轮isExit="+isExit);
				if(!isExit){
					beanList.remove(i);
					--i;
				}
				
			}
			
			return beanList;
		}
		
		public List<Map<String, Object>> getMobileListForSuccess(List<Map<String, Object>>mobileList,List<String> success){
			int c=0;
			for(int i=0;i<mobileList.size();i++){
				boolean isExit=false;
//				System.out.println("bean="+beanList.get(i).toString());
				c++;
				String phone = mobileList.get(i).get("phoneNo").toString();
				for(int j=0;j<success.size();j++){
				
					String mobile = success.get(j);
					if(phone.equals(mobile)){
						isExit=true;
						continue;
					}
				}
				System.out.println("第"+c+"轮isExit="+isExit);
				if(!isExit){
					mobileList.remove(i);
					--i;
				}
				
			}
			
			return mobileList;
		}
		
		/**
		 * 将List<BatchStationBean>集合转换成调用pcc接口的集合格式
		 * @return
		 */
		public List<Map<String, Object>> batchStation2List(List<BatchStationBean> list){
			System.out.println("-------->begin batch station cutover");
			List<Map<String, Object>> listMobile = new ArrayList<Map<String,Object>>();
				if(list!=null && list.size()>0){
					for (int a=0;a<list.size();a++) {
						System.out.println("batchStationBean==第"+(a+1)+"个=="+list.get(a).toString());
					}
			//将list集合转成List<Map<String,Object>> Map-key
					List<Map<String, Object>> listCpe=null;
					 String strategyCode="";
				
						int c=0;
			for(int j=0;j<list.size();j++){
						String phone = list.get(0).getProductNo();
						String subsid = list.get(0).getSubsid();
						c++;
					
						//遍历集合
						//一个手机号对应的userlocation，策略号
						Map<String,Object> map = new HashMap<String,Object>();
						//一个手机号关联的userlocation集合
						//userlocation先放入set集合去重
						Set<String> loctions = new HashSet<String>();
						Set<String> loctionsOld = new HashSet<String>();
						//list集合存放小区位置
						List<String>loctions2=new ArrayList<String>();
						map.put("phoneNo", phone);
						map.put("subsid", subsid);
						int d=0;
						for(int i=0;i<list.size();i++){
								d++;
								String mobile = list.get(i).getProductNo();
								if(phone.equals(mobile)){
									//将新的locaton放入set集合
									loctions.add(list.get(i).getUserlocationNew());
									//将旧的locaton 也放入另一个set集合中
									loctionsOld.add(list.get(i).getUserlocationOld());
									list.remove(i);
									--i;
								
								}
								System.out.println("第"+c+"轮第"+d+"次遍历后loctions="+loctions.toString());
							}
						//将set集合转换成list集合
							for (String loc: loctions) {
								loctions2.add(loc);
							}
						
							System.out.println("全部遍历完后loctions大小="+loctions.size());
							/*if(loctions.size()>0 && loctions.size()<9){
								int h=9-loctions.size();
								for(int k=0;k<h;k++){
									loctions2.add("0");
								}
								log.info("不够9个小区补0："+loctions2.toString());
							}*/
							map.put("loctions", loctions2);
							map.put("loctionsOld", loctionsOld);
							try {
								
								listCpe=	batchStationDao.queryStrategyCodeBySubsid(subsid);
								if(listCpe!=null&& listCpe.size()>0){
									Map<String, Object> map2 = listCpe.get(0);
									 strategyCode=map2.get("strategy_code")==null?"":(String)map2.get("strategy_code");
								} 
							} catch (MysqlDataAccessExcetion e) {
								e.printStackTrace();
								log.error("查询用户策略号，数据库操作错误");
							}
							 log.info("手机号"+phone+"对应的策略号strategyCode="+strategyCode);
							map.put("policId", strategyCode);
							listMobile.add(map);
							--j;
					}
				
			}		
				return listMobile;
		}
		
		/**
		 * service 测试类
		 */
		public  List<BatchStationBean>  batchTest() {
			BatchStationBean stationBean = new BatchStationBean();
			BatchStationBean stationBean2 = new BatchStationBean();
			BatchStationBean stationBean3 = new BatchStationBean();
			List<BatchStationBean> list = new ArrayList<BatchStationBean>();
			stationBean.setSubsid("1380710");
			stationBean.setProductNo("13807109725");
			stationBean.setUserlocationOld("130-4600029154074449025");
			stationBean.setUserlocationNew("130-4600029154074449537");
			list.add(stationBean);
			stationBean2.setSubsid("1380725");
			stationBean2.setProductNo("13807259725");
			stationBean2.setUserlocationOld("130-4600029154074449026");
			stationBean2.setUserlocationNew("130-4600029154074449538");
			list.add(stationBean2);
			stationBean3.setSubsid("1380725");
			stationBean3.setProductNo("13807259725");
			stationBean3.setUserlocationOld("130-4600029154074449027");
			stationBean3.setUserlocationNew("130-4600029154074449539");
			list.add(stationBean3);
			return list;

			}
		
		/**
		 * 参数重置查询未重置，批量基站重置查询service实现类
		 */
		@Override
		public List<BatchStationBean> getBatchStationInfosForNoReset(String startTime,
				String endTime, Integer resetFalg) {
			//查询前先更新下
			updateBatchStationTableBefore();
			updateBatchStationLogTableBefore();
			List<BatchStationBean> listBean=null;
			if(resetFalg==0){
				List<Map<String,Object>> list = batchStationDao.queryBatchStationInfosForNoReset(startTime, endTime, resetFalg);
				if(list!=null&&list.size()>0){
						listBean=new ArrayList<BatchStationBean>();
					for(int i=0;i<list.size();i++){
						BatchStationBean stationBean = new BatchStationBean();
						Map<String, Object> m = list.get(i);
						
						//为计算userlocation获得lac和ci
						String lacOld = m.get("lac_dec_id_old").toString();
						String ciOld = m.get("ci_dec_id_old").toString();
						String lacNew = m.get("lac_dec_id_new").toString();
						String ciNew= m.get("ci_dec_id_new").toString();
						String lac_ci_old = m.get("lac_ci_hex_code_old").toString();
						String lac_ci_new = m.get("lac_ci_hex_code_new").toString();
						
						int lac_lengthOld=5-lacOld.length();
						int ci_lengthOld=9-ciOld.length();
						int lac_lengthNew = 5-lacNew.length();
						int ci_lengthNew = 9-ciNew.length();
					//不够规定位数左边补0	
						if(lac_lengthOld>0){
							for(int b=0;b<lac_lengthOld;b++){
								lacOld="0"+lacOld;
							}
						}
						if(ci_lengthOld>0){
							for(int b=0;b<lac_lengthOld;b++){
								ciOld="0"+ciOld;
							}
						}
						if(lac_lengthNew>0){
							for(int b=0;b<lac_lengthNew;b++){
								lacNew="0"+lacNew;
							}
						}
						if(ci_lengthNew>0){
							for(int b=0;b<ci_lengthNew;b++){
								ciNew="0"+ciNew;
							}
						}
						
					//拼接成userlocation
						String userlocationOld=PopConstant.USER_LOCATION_PREFIX+lacOld+ciOld;
						String userlocationNew=PopConstant.USER_LOCATION_PREFIX+lacNew+ciNew;
					//将其他字段拼接下转成bean对象
						stationBean.setSubsid(m.get("subsid").toString());
						stationBean.setProductNo(m.get("product_no").toString());
						stationBean.setUserlocationOld(m.get("user_location").toString());
						stationBean.setUserlocationNew(userlocationNew);
						stationBean.setLac_ci_hex_code_old(lac_ci_old);
						stationBean.setLac_ci_hex_code_new(lac_ci_new);
						SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						String cutoverTime="";
						if(m.get("chg_date")!=null){
							cutoverTime = sdf.format((Date)m.get("chg_date"));
						}
						stationBean.setCutoverTime(cutoverTime);
						String importTime="";
						if(m.get("import_time")!=null){
							importTime = sdf.format((Date)m.get("import_time"));
						}
						stationBean.setImportTime(importTime);
						stationBean.setResetFlag(Integer.parseInt(m.get("reset_flag").toString()));
						listBean.add(stationBean);
					}
					for(int j=0;j<listBean.size();j++){
						System.out.println("查询批量重置信息："+listBean.get(j).toString());
					}
				}
			}
			if(listBean!=null){
				log.info("-----查询批量未重置信息条数："+listBean.size()+" 条");
			}else{
				log.info("----未查到批量未重置信息！");
			}
			return listBean;
		}
		
		/**
		 * 参数重置查询已重置，批量基站重置查询service实现类
		 */
		@Override
		public List<BatchStationBean> getBatchStationInfosForReset(String startTime,
				String endTime, Integer resetFalg) {

			List<BatchStationBean> listBean=null;
				if(resetFalg==1){
					List<Map<String,Object>> list = batchStationDao.queryBatchStationInfosForReset(startTime, endTime, resetFalg);
					if(list!=null&&list.size()>0){
							listBean=new ArrayList<BatchStationBean>();
						for(int i=0;i<list.size();i++){
							BatchStationBean stationBean = new BatchStationBean();
							Map<String, Object> m = list.get(i);
							
							//为计算userlocation获得lac和ci
							String lacOld = m.get("lac_dec_id_old").toString();
							String ciOld = m.get("ci_dec_id_old").toString();
							String lacNew = m.get("lac_dec_id_new").toString();
							String ciNew= m.get("ci_dec_id_new").toString();
							String lac_ci_old = m.get("lac_ci_hex_code_old").toString();
							String lac_ci_new = m.get("lac_ci_hex_code_new").toString();
							int lac_lengthOld=5-lacOld.length();
							int ci_lengthOld=9-ciOld.length();
							int lac_lengthNew = 5-lacNew.length();
							int ci_lengthNew = 9-ciNew.length();
						//不够规定位数左边补0	
							if(lac_lengthOld>0){
								for(int b=0;b<lac_lengthOld;b++){
									lacOld="0"+lacOld;
								}
							}
							if(ci_lengthOld>0){
								for(int b=0;b<lac_lengthOld;b++){
									ciOld="0"+ciOld;
								}
							}
							if(lac_lengthNew>0){
								for(int b=0;b<lac_lengthNew;b++){
									lacNew="0"+lacNew;
								}
							}
							if(ci_lengthNew>0){
								for(int b=0;b<ci_lengthNew;b++){
									ciNew="0"+ciNew;
								}
							}
							
						//拼接成userlocation
							String userlocationOld=PopConstant.USER_LOCATION_PREFIX+lacOld+ciOld;
							String userlocationNew=PopConstant.USER_LOCATION_PREFIX+lacNew+ciNew;
						//将其他字段拼接下转成bean对象
							stationBean.setSubsid(m.get("subsid").toString());
							stationBean.setProductNo(m.get("product_no").toString());
							stationBean.setUserlocationOld(userlocationOld);
							stationBean.setUserlocationNew(m.get("user_location").toString());
							stationBean.setLac_ci_hex_code_old(lac_ci_old);
							stationBean.setLac_ci_hex_code_new(lac_ci_new);
							SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
							String cutoverTime="";
							if(m.get("chg_date")!=null){
								cutoverTime = sdf.format((Date)m.get("chg_date"));
							}
							stationBean.setCutoverTime(cutoverTime);
							String importTime="";
							if(m.get("import_time")!=null){
								importTime = sdf.format((Date)m.get("import_time"));
							}
							stationBean.setImportTime(importTime);
							stationBean.setResetFlag(Integer.parseInt(m.get("reset_flag").toString()));
							listBean.add(stationBean);
						}
						for(int j=0;j<listBean.size();j++){
							System.out.println("查询批量重置信息："+listBean.get(j).toString());
						}
					}
				}
			if(listBean!=null){
				log.info("-----查询批量已重置信息条数："+listBean.size()+" 条");
			}else{
				log.info("----未查到批量已重置信息！");
			}
			return listBean;
		}
		
		/**
		 * 参数重置查询所有，批量基站重置查询service实现类
		 */
		@Override
		public List<BatchStationBean> getBatchStationInfos(String startTime,
				String endTime, Integer resetFalg) {
			
			List<BatchStationBean> listBean=null;
			List<BatchStationBean> listNoReset=null;
			List<BatchStationBean> listReset= null;
			
				if(resetFalg==0){
					listNoReset= getBatchStationInfosForNoReset(startTime,endTime,0);
					return listNoReset;
				}else if(resetFalg==1){
					listReset= getBatchStationInfosForReset(startTime,endTime,1);
					return listReset;
				}else{
					listBean=new ArrayList<BatchStationBean>();
					listNoReset= getBatchStationInfosForNoReset(startTime,endTime,0);
						if(listNoReset!=null && listNoReset.size()>0){
							for (BatchStationBean noResetBean : listNoReset) {
								listBean.add(noResetBean);
							}
						}
						listReset= getBatchStationInfosForReset(startTime,endTime,1);
						if(listReset!=null && listReset.size()>0){
							for (BatchStationBean resetBean : listReset) {
								listBean.add(resetBean);
							}
						}
		    	}
				if(listBean!=null && listBean.size()>0){
					log.info("-----查询批量重置全部信息条数："+listBean.size()+" 条");
				}else{
					log.info("----未查到批量重置全部信息！");
				}
			return listBean;
		}
		
		/**
		 * 上传文件service实现类插入import表和log表
		 * @throws Exception 
		 * @throws MysqlDataAccessExcetion 
		 */
		@Override
		public String cpeBatchStationImport(List<ImportBatchCutoverBean> stationExcel) throws MysqlDataAccessExcetion, Exception {
				log.info("开始导入割接数据..........");
		
				List<ImportBatchCutoverBean> importBatchCutoverList = new ArrayList<ImportBatchCutoverBean>();
				List<BatchStationBean> batchStationBeanList = new ArrayList<BatchStationBean>();
				for(int i=0;i<stationExcel.size();i++){
					
					String enodeid_old = stationExcel.get(i).getEnodeid_old();
					String lac_dec_id_old = stationExcel.get(i).getLac_dec_id_old();
					String ci_dec_id_old = stationExcel.get(i).getCi_dec_id_old();

					String enodeid_new = stationExcel.get(i).getEnodeid_new();
					String lac_dec_id_new = stationExcel.get(i).getLac_dec_id_new();
					String ci_dec_id_new = stationExcel.get(i).getCi_dec_id_new();
					//根据原lac和原ci及原基站id到小区同步表中查询lac_ci的十六进制和十进制
					List<Map<String,Object>> list1 = batchStationDao.queryLacCiByXiaoQuTable(enodeid_old, lac_dec_id_old, ci_dec_id_old);
					

					if(list1!=null && list1.size()>0){
						Map<String, Object> map1 = list1.get(0);
						String lac_ci_hex_code_old = map1.get("lac_ci_hex_code").toString();
						String lac_ci_dec_id_old =map1.get("lac_ci_dec_id").toString();
						stationExcel.get(i).setLac_ci_hex_code_old(lac_ci_hex_code_old);
						stationExcel.get(i).setLac_ci_dec_id_old(lac_ci_dec_id_old);
					}else{//在小区同步表中未找到原lac_ci字段
						log.info(".....在小区同步表中未找到原lac_ci字段");
						return "-1";
					}
					
					//根据新lac和新ci及新基站id到小区同步表中查询lac_ci的十六进制和十进制
					//如果查不到怎么处理？自己拼接还是拒绝导入？---拒绝导入
					
					List<Map<String,Object>> list2 = batchStationDao.queryLacCiByXiaoQuTable(enodeid_new, lac_dec_id_new, ci_dec_id_new);
					

					if(list2!=null && list2.size()>0){
						Map<String, Object> map2 = list2.get(0);
						String lac_ci_hex_code_new = map2.get("lac_ci_hex_code").toString();
						String lac_ci_dec_id_new = map2.get("lac_ci_dec_id").toString();
						stationExcel.get(i).setLac_ci_hex_code_new(lac_ci_hex_code_new);
						stationExcel.get(i).setLac_ci_dec_id_new(lac_ci_dec_id_new);
					}else{//在小区同步表中未找到新lac_ci字段
						log.info(".....在小区同步表中未找到新lac_ci字段");
						return "-1";
					}
					//判断基站割接文件数据是否在基站割接表中存在，判断条件为原lac_ci的十六进制和新lac_ci的十六进制
					String lac_ci_hex_code_old = stationExcel.get(i).getLac_ci_hex_code_old();
					String lac_ci_hex_code_new = stationExcel.get(i).getLac_ci_hex_code_new();
					//查询导入表是否已存在那条记录
					
					List<Map<String,Object>> list3 = batchStationDao.queryLacCiByImportTable(lac_ci_hex_code_old, lac_ci_hex_code_new);

					//若存在
					if(list3!=null && list3.size()>0){		//说明文件中的一条数据在导入表中已经存在了
						log.info("lac_ci_hex_code_old="+lac_ci_hex_code_old+"和lac_ci_hex_code_new="+lac_ci_hex_code_new+"在割接导入表中已存在");
						
					}else{		//割接导入表中不存在,加入插入导入表队列中
						importBatchCutoverList.add(stationExcel.get(i));
					}
					//再查询割接日志表中的信息是否存在
				
				}
				for (ImportBatchCutoverBean importBatchCutoverBean : stationExcel) {
					System.out.println("导入割接表bean："+importBatchCutoverBean.toString());
				}
				//excle表循环后调用dao插入方法分别插入到导入表
				//将导入表中不存在的插入到导入表中
				boolean isOK=true;
				int[] updates = batchStationDao.cpeStationChgImport(importBatchCutoverList);
				if(updates!=null){
					for (int j : updates) {
						if(j<=0){
							isOK=false;break;
						}
					}
					if(!isOK){
						throw new Exception("导入割接文件失败!");
					}
				}else{
					log.info("上传割接文件在在割接导入表中已存在");
					
				}
				
				boolean isLocked=true;
				//开始插入割接日志表中，插入日志表时还的查一下手机号和cpe设备号
				for(int i=0;i<stationExcel.size();i++){
					String	lac_ci_hex_code_old = stationExcel.get(i).getLac_ci_hex_code_old();
					String 	lac_ci_hex_code_new = stationExcel.get(i).getLac_ci_hex_code_new();
					
					List<Map<String,Object>> list4 = batchStationDao.queryResetFlagByLogTable(lac_ci_hex_code_old, lac_ci_hex_code_new);
					//若存在	
					if(list4!=null && list4.size()>0){
						log.info("lac_ci_hex_code_old="+lac_ci_hex_code_old+"和lac_ci_hex_code_new="+lac_ci_hex_code_new+"在割接日志表中已存在");
					}else{		//割接日志表中不存在，加入到插入日志表队列中
						List<BatchStationBean> listBean = batchStationDao.getResetLogBylac_ci(lac_ci_hex_code_old, lac_ci_hex_code_new);
						if(listBean!=null && listBean.size()>0){
							for (BatchStationBean batchStationBean2 : listBean) {
								batchStationBeanList.add(batchStationBean2);
							}
							
						}else{
							isLocked=false;
							log.info("lac_ci_hex_code_old="+lac_ci_hex_code_old+";lac_dec_id_old="+stationExcel.get(i).getLac_dec_id_old()+";ci_dec_id_old="+stationExcel.get(i).getCi_dec_id_old()+"----->未锁网");
						}
					
					
				}
			}
				
				//将日志表中不存在的插入到日志表中
				boolean isOK2=true;
				int[] updates2 = batchStationDao.cpeUserResetLogImport(batchStationBeanList);
				if(updates2!=null){
					for (int i : updates2) {
						if(i<=0){
							isOK2=false;break;
						}
					}
					if(!isOK2){
						throw new Exception("导入割接文件失败!");
					}
					
				}else{
					if(isLocked){
						log.info("上传割接文件在在日志表中已存在");
					}
					
				}
				if(updates==null && updates2==null && isLocked){
					log.info("上传割接文件在在数据库中已存在");
					return "0";
				}
				
				return "1";
				
		}
	/**
	 * 更新割接日志表-导入表中有的日志表中没有
	 */
		@Override
		public void updateBatchStationTableBefore() {
			List<BatchStationBean> listBean=null;
			List<Map<String,Object>> list =null;
			try {
				list = batchStationDao.queryBatchStationTableBefore();
				if(list!=null&&list.size()>0){
					listBean=new ArrayList<BatchStationBean>();
				for(int i=0;i<list.size();i++){
					BatchStationBean stationBean = new BatchStationBean();
					Map<String, Object> m = list.get(i);
					String lac_ci_hex_code_old = m.get("lac_ci_hex_code_old").toString();
					String lac_ci_hex_code_new = m.get("lac_ci_hex_code_new").toString();

					stationBean.setSubsid(m.get("subsid").toString());
					stationBean.setProductNo(m.get("product_no").toString());
					stationBean.setLac_ci_hex_code_old(lac_ci_hex_code_old);
					stationBean.setLac_ci_hex_code_new(lac_ci_hex_code_new);

					SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					String cutoverTime="";
					if(m.get("chg_date")!=null){
						cutoverTime = sdf.format((Date)m.get("chg_date"));
					}
					stationBean.setCutoverTime(cutoverTime);
					String importTime="";
					if(m.get("import_time")!=null){
						importTime = sdf.format((Date)m.get("import_time"));
					}
					stationBean.setImportTime(importTime);
					stationBean.setResetFlag(0);
					listBean.add(stationBean);
				}
				
			}

				//将listBean插入日志表中
				if(listBean!=null && listBean.size()>0){
					for(int j=0;j<listBean.size();j++){
						System.out.println("first..查询批量重置信息before："+listBean.get(j).toString());
					}
					log.info("first..开始更新日志表");
					batchStationDao.cpeUserResetLogImport(listBean);
					log.info("first..更新日志表结束");
				}
			} catch (MysqlDataAccessExcetion e) {
				log.error("查询基站割接前更新日志表时数据库操作错误！"+e.getMessage());
				e.printStackTrace();
			} catch (Exception e) {
				log.error("查询基站割接前更新日志表时操作错误！"+e.getMessage());
				e.printStackTrace();
			}
			
		}
		/**
		 * 更新割接日志表-锁网表中有的日志表中没有
		 */
			@Override
			public void updateBatchStationLogTableBefore() {
				List<BatchStationBean> listBean=null;
				List<Map<String,Object>> list =null;
				try {
					list = batchStationDao.queryBatchStationLogTableBefore();
					if(list!=null&&list.size()>0){
						listBean=new ArrayList<BatchStationBean>();
					for(int i=0;i<list.size();i++){
						BatchStationBean stationBean = new BatchStationBean();
						Map<String, Object> m = list.get(i);
						String lac_ci_hex_code_old = m.get("lac_ci_hex_code_old").toString();
						String lac_ci_hex_code_new = m.get("lac_ci_hex_code_new").toString();
						

						stationBean.setSubsid(m.get("subsid").toString());
						stationBean.setProductNo(m.get("product_no").toString());
						stationBean.setLac_ci_hex_code_old(lac_ci_hex_code_old);
						stationBean.setLac_ci_hex_code_new(lac_ci_hex_code_new);

						SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						String cutoverTime="";
						if(m.get("chg_date")!=null){
							cutoverTime = sdf.format((Date)m.get("chg_date"));
						}
						stationBean.setCutoverTime(cutoverTime);
						String importTime="";
						if(m.get("import_time")!=null){
							importTime = sdf.format((Date)m.get("import_time"));
						}
						stationBean.setImportTime(importTime);
						stationBean.setResetFlag(0);
						listBean.add(stationBean);
					}
					
				}

					//将listBean插入日志表中
					if(listBean!=null && listBean.size()>0){
						for(int j=0;j<listBean.size();j++){
							System.out.println("second..查询批量重置信息before："+listBean.get(j).toString());
						}
						log.info("second..开始更新log日志表");
						batchStationDao.cpeUserResetLogImport(listBean);
						log.info("second..更新log日志表结束");
					}
				} catch (MysqlDataAccessExcetion e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
	@Override
	public BatchStationBean getSelectedBatchStationInfosByIds(
			String subsid, String productNo, String user_location) {
		BatchStationBean stationBean = null;
		List<Map<String,Object>> list = batchStationDao.getSelectedBatchStationInfosByIds(subsid, productNo, user_location);
		if(list!=null&&list.size()>0){
			for(int i=0;i<list.size();i++){
				 stationBean = new BatchStationBean();
				Map<String, Object> m = list.get(0);
				
				//为计算userlocation获得lac和ci
				String lacOld = m.get("lac_dec_id_old").toString();
				String ciOld = m.get("ci_dec_id_old").toString();
				String lacNew = m.get("lac_dec_id_new").toString();
				String ciNew= m.get("ci_dec_id_new").toString();
				
				int lac_lengthOld=5-lacOld.length();
				int ci_lengthOld=9-ciOld.length();
				int lac_lengthNew = 5-lacNew.length();
				int ci_lengthNew = 9-ciNew.length();

				if(lac_lengthNew>0){
					for(int b=0;b<lac_lengthNew;b++){
						lacNew="0"+lacNew;
					}
				}
				if(ci_lengthNew>0){
					for(int b=0;b<ci_lengthNew;b++){
						ciNew="0"+ciNew;
					}
				}
				
			//拼接成userlocation
				String userlocationOld=PopConstant.USER_LOCATION_PREFIX+lacOld+ciOld;
				String userlocationNew=PopConstant.USER_LOCATION_PREFIX+lacNew+ciNew;
			//将其他字段拼接下转成bean对象
				stationBean.setSubsid(m.get("subsid").toString());
				stationBean.setProductNo(m.get("product_no").toString());
				stationBean.setUserlocationOld(m.get("user_location").toString());
				stationBean.setLac_ci_hex_code_old(m.get("lac_ci_old").toString());
				stationBean.setLac_ci_hex_code_new(m.get("lac_ci_new").toString());
				stationBean.setUserlocationNew(userlocationNew);
				SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String cutoverTime="";
				if(m.get("chg_date")!=null){
					cutoverTime = sdf.format((Date)m.get("chg_date"));
				}
				stationBean.setCutoverTime(cutoverTime);
				String importTime="";
				if(m.get("import_time")!=null){
					importTime = sdf.format((Date)m.get("import_time"));
				}
				stationBean.setImportTime(importTime);
				stationBean.setResetFlag(Integer.parseInt(m.get("reset_flag").toString()));
				
			}
			
		}
		
		return stationBean;
	}
	@Override
	public void updateRedisInfoForSuccess(
			List<Map<String,Object>>mobileListForSuccess) {
		//更新redisCPE用户的userlocation 
		
	 	Long count =null;
	
			
		try{
			for(int i=0;i<mobileListForSuccess.size();i++){
					String key="";
			 		Map<String, Object> m = mobileListForSuccess.get(i);
			 		String subsid = m.get("subsid").toString();
					List<Object> loctions = (List<Object>) m.get("loctions");
			 		Object[] locs=(Object[]) loctions.toArray();
			 		String[] lo=new String[locs.length];
			 		for(int k=0;k<locs.length;k++){
			 			lo[k]=locs[k].toString();
			 		}
			 		 key=PopConstant.REDIS_UNL_PREFIX+subsid;
			 		JedisClientUtil.delKeyValue(key);
			 		count = JedisClientUtil.addKeyValues(key,lo);
			 		log.info("参数重置成功后更新CPE在redis中的信息："+count+"行");
			 		Boolean flag=JedisClientUtil.print(key);
					if(flag){
						log.info("-------更新redis集群数据库CPE用户KEY="+key+"----->成功");
					}else if(!flag){
						log.info("-------更新redis集群数据库CPE用户KEY="+key+"----->失败");
					}else{
						log.error("--------更新redis数据库CPE用户KEY="+key+"操作错误");
					}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	
	}

    /**
     * 批量基站变更
     */
	public  List<String> cpeBatchStationChange(List<BatchStationBean> beanList) throws Exception
	{
		List<String> succs = new ArrayList<String>();
		ArrayList<BatchStationBean> beanList2=(ArrayList<BatchStationBean>)beanList;
		//克隆beanlist集合
		ArrayList<BatchStationBean> beanListCopy = (ArrayList<BatchStationBean>)beanList2.clone();
		//将克隆后的集合传给batchStation2List方法
		List<Map<String,Object>> mobileList = this.batchStation2List(beanListCopy);
		//将原始的beanlist和根据beanlist转成的mobilelist传给service
		Map<String, Object> resultsMap = this.cpeBsBatchcg(beanList,mobileList);
		//"1"表示调用pcc接口成功，success表示成功的手机集合
		succs = (List<String>) resultsMap.get("1");
		//根据成功的手机集合过滤原始的beanlist，返回所有成功手机号的beanlist
		List<Map<String,Object>> mobileListForSuccess=this.getMobileListForSuccess(mobileList, succs);
		//对成功的手beanlist更新redis信息
		this.updateRedisInfoForSuccess(mobileListForSuccess);
		return succs;
	}
	
}
