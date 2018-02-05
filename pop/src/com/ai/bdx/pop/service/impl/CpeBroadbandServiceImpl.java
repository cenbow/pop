package com.ai.bdx.pop.service.impl;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;


import com.ai.bdx.pop.dao.CpeBroadbandDao;
import com.ai.bdx.pop.service.CpeBroadbandService;
import com.ai.bdx.pop.util.CpeUserInfo;
import com.ai.bdx.pop.util.SpringContext;
import com.ai.bdx.pop.wsclient.ISendPccInfoService;


/**
 * Cpe用户宽带service实现impl，变更宽带状态
 * @author 
 *
 */
@Service
public class CpeBroadbandServiceImpl implements CpeBroadbandService {
	private static final Logger log = LogManager.getLogger();
	@Resource
	private CpeBroadbandDao cpeBroadbandDao;
	
	public void setCpeBroadbandDao(CpeBroadbandDao cpeBroadbandDao) {
		this.cpeBroadbandDao = cpeBroadbandDao;
	}
	
	@Override
	public Map updateCpeNetType(List<CpeUserInfo> list)throws Exception{
		//调用DAO层的宽带变更方法
		int[] updates = cpeBroadbandDao.updateCpeNetType(list);
		Map<String, String> map =null;
		System.out.println("dao被调用了");
		for (int i =0;i<updates.length;i++) {
			if(updates[i]>0){
				System.out.println("cpe用户宽带标识"+list.get(i).getSubsid()+"更新了 "+updates[i]+" 行");
				
			}else{
				//当某行数据更新为0时表示该行不存在或其他，然后整个文件失败回滚
				log.error("cpe用户宽带标识"+list.get(i).getSubsid()+"更新失败");
				throw new Exception("cpe用户宽带标识"+list.get(i).getSubsid()+"更新失败");
			}
		}
		return map;
	}

	
	/**
	 * 调用pcc接口
	 */
	@SuppressWarnings("unchecked")
	public Map<String ,String> pop2pccWebService(List<CpeUserInfo> list)throws Exception{
		Map<String, String> m = new HashMap<String,String>();
		ISendPccInfoService sendPcc = SpringContext.getBean("sendPccInfoService", ISendPccInfoService.class);
		String subsid ;
		String net_type ;
		for(int k=0;k<list.size();k++){
			 String broadbandChange ="";
			 	subsid = list.get(k).getSubsid();
			 	net_type = list.get(k).getNetType();
					 //调用查询小区
					 List<String> usrlocations= cpeBroadbandDao.queryCpeUserInfoBySubsid(subsid);
					 //查询旧的策略号
					 String oldpolicId = cpeBroadbandDao.queryStrategyCodeBySubsid(subsid);
					 //查询新的策略号
					 String policId = cpeBroadbandDao.queryStrategyCodeBynetType(net_type);
					 
					 log.info("开始调用pcc接口进行用户宽带变更变更操作");
					
					 log.info("手机号为"+list.get(k).getProductNo()+"的用户带宽状态为:"+list.get(k).getNetType()+"SendPccInfoServiceImpl="+sendPcc.toString());
					 //调用pcc接口将上面的参数传递给pcc，返回1表示正常
					 broadbandChange = sendPcc.cpeUserNetTypeChange(list.get(k).getProductNo(),policId,oldpolicId,usrlocations);
					 log.info("用户宽带变更调用pcc接口返回值="+broadbandChange);
					 m.put(list.get(k).getProductNo(), broadbandChange);
					
//				if(!"1".equals(broadbandChange)){
//				//如果调用失败再调2次，3次都失败抛异常
//					broadbandChange = sendPcc.cpeUserNetTypeChange(list.get(k).getProductNo(),newtype,oldtype,location);
//					 m.put(list.get(k).getProductNo(), broadbandChange);
//					if(!"1".equals(broadbandChange)){
//						broadbandChange = sendPcc.cpeUserNetTypeChange(list.get(k).getProductNo(),newtype,oldtype,location);
//						 m.put(list.get(k).getProductNo(), broadbandChange);
//						if(!"1".equals(broadbandChange)){
//							log.error("调用PCC宽带接口失败返回值="+broadbandChange+",手机号为"+list.get(k).getProductNo()+",新的策略号:"+newtype+",旧的策略号="+oldtype+",小区="+location);
//							throw new Exception("调用PCC宽带接口失败返回值="+broadbandChange+",手机号为"+list.get(k).getProductNo()+",新的策略号:"+newtype+",旧的策略号="+oldtype+",小区="+location);
//						}
//					}
//				}
			}
		//调用宽带变更方法
		Map<String, String> map = updateCpeNetType(list);
		return map;
	}
}
