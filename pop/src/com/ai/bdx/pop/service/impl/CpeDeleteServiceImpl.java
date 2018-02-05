package com.ai.bdx.pop.service.impl;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import redis.clients.jedis.exceptions.JedisConnectionException;

import com.ai.bdx.pop.dao.CpeDeleteDao;
import com.ai.bdx.pop.service.CpeDeleteService;
import com.ai.bdx.pop.util.SpringContext;
import com.ai.bdx.pop.wsclient.ISendPccInfoService;
import com.ai.bdx.pop.wsclient.impl.SendPccInfoServiceImpl;
import com.asiainfo.biapp.pop.Exception.DelSubscriberException;
import com.asiainfo.biapp.pop.Exception.FileErrorException;
import com.asiainfo.biapp.pop.Exception.MysqlDataAccessExcetion;
import com.asiainfo.biapp.pop.model.CpeUserInfo;
import com.asiainfo.biapp.pop.redis.RedisClient;

/**
 * Cpe用户销户service实现impl，变更用户状态，调用pcc接口
 * @author 林
 *
 */

public class CpeDeleteServiceImpl implements CpeDeleteService {
	private static final Logger log = LogManager.getLogger();

	private CpeDeleteDao cpeDeleteDao;

	public Map updateCpeUserStatus(List<CpeUserInfo> list)throws JedisConnectionException,MysqlDataAccessExcetion,FileErrorException,SQLException,Exception {
		int[] updates = cpeDeleteDao.updateCpeUserStatus(list);
		Map<String, String> map =null;
		
		for (int i =0;i<updates.length;i++) {
			
			
			if(updates[i]>0){
				
				log.info("cpe用户销户标识"+list.get(i).getSubsid()+"更新了 "+updates[i]+" 行");
				
			}else{
				//当某行数据更新为0时表示该行不存在或其他，然后整个文件失败回滚
				log.error("cpe用户销户标识"+list.get(i).getSubsid()+"更新失败");
				throw new Exception("cpe用户销户标识"+list.get(i).getSubsid()+"更新失败");
			}
		}
	
		map = pop2pccWebService(list);
		if(map.size()>0){
			System.out.println("------>开始调用redis删除CPE用户锁网信息...");
			Set<String> phoneSet = map.keySet();
			Iterator<String> it = phoneSet.iterator();
			RedisClient redisClient = new RedisClient();
			redisClient.genJedis();
			while(it.hasNext()){
				String phone = it.next();
				String rst = map.get(phone);
				if("1".equals(rst)){
					Long count = redisClient.delKeyValue(phone);
					System.out.println("-------删除redis数据库CPE用户"+phone+"锁网信息:"+count);
					log.info("-------删除redis数据库CPE用户"+phone+"锁网信息:"+count);
				}
			}
			
				redisClient.closeJedis();
			
		}
		
		return map;
	}
/**
 * 根据subsid查询策略号
 * 
 */
	public List queryStrategyCodeBySubsid(String subsid)throws Exception{
		List list = cpeDeleteDao.queryStrategyCodeBySubsid(subsid);
		return list;
	}
	
	/**
	 * 调用pcc接口
	 */
	public Map<String ,String> pop2pccWebService(List<CpeUserInfo> list)throws Exception{
		//调用pcc销户接口delSubscriber
		//首先先查出策略号，即带宽与策略一一对应的那个码12270010000000000000000000000002
	  //但没有什么用，因为传参时只传了个手机号码啊？	 	
		Map<String, String> m = new HashMap<String,String>();
		SendPccInfoServiceImpl sendPcc = SpringContext.getBean("sendPccInfoService",SendPccInfoServiceImpl.class);
		for(int k=0;k<list.size();k++){
			 String statusChange ="";
			 
//				List<Map<String, Object>> listCpe;
//				try {
//					listCpe = cpeDeleteService.queryStrategyCodeBySubsid(list.get(k).getSubsid());
//			
//				if(listCpe!=null && listCpe.size()>0){
//				
//					 Map<String, Object> map = listCpe.get(0);
//					
//					 String phone=map.get("product_no")==null?"":(String)map.get("product_no");
//					 String subsid=map.get("subsid")==null?"":(String)map.get("subsid");
//					 String planCode=map.get("plan_code")==null?"":(String)map.get("plan_code");
//					 String strategyCode=map.get("strategy_code")==null?"":(String)map.get("strategy_code");
//					 int user_status = (Integer)map.get("user_status");
//					 String status="";
//					 if(user_status==2){
//						 status="US24";
//					 }
//					 if(user_status==3){
//						 status="US20";
//					 }
					 System.out.println("开始调用pcc接口进行用户状态变更操作");
					//调用pcc接口将上面的参数传递给pcc
					
					 System.out.println("手机号为"+list.get(k).getProductNo()+"的用户状态为:"+list.get(k).getUserStatus());
			//为了本地测试暂时注释下面2行，生产测试必须再次打开
					 statusChange = sendPcc.cpeUserStatusChange("13810998891");
					 System.out.println("用户销户调用pcc接口返回值="+statusChange);
					 m.put(list.get(k).getProductNo(), statusChange);
//				}
//				}catch (Exception e) {
//					 m.put(list.get(k).getSubsid(), statusChange);
//					 System.out.println("手机号为"+list.get(k).getProductNo()+"的用户状态为:"+list.get(k).getStatusChangeTime()+"用户CPE设备id="+list.get(k).getSubsid());
//					e.printStackTrace();
//					log.error("调用PCC销户接口失败，手机号为"+list.get(k).getProductNo()+"的用户状态为:"+list.get(k).getStatusChangeTime()+"用户CPE设备id="+list.get(k).getSubsid());
//				}
					
				if(!"1".equals(statusChange)){
				//如果调用失败再调2次，3次都失败抛异常
					statusChange = sendPcc.cpeUserStatusChange("13810998891");
					 m.put(list.get(k).getProductNo(), statusChange);
					if(!"1".equals(statusChange)){
						statusChange = sendPcc.cpeUserStatusChange("13810998891");
						 m.put(list.get(k).getProductNo(), statusChange);
						if(!"1".equals(statusChange)){
							log.error("调用PCC销户接口失败返回值="+statusChange+",手机号为"+list.get(k).getProductNo()+",用户状态为:"+list.get(k).getStatusChangeTime()+",用户CPE设备id="+list.get(k).getSubsid());
							throw new Exception("调用PCC销户接口失败返回值="+statusChange+",手机号为"+list.get(k).getProductNo()+",用户状态为:"+list.get(k).getStatusChangeTime()+",用户CPE设备id="+list.get(k).getSubsid());

						}
					}
					
				}
					 
			}
		
	 	
		
		return m;
	}
	
	/**
	 * 调用pcc接口一次一个
	 * @throws SQLException 
	 * @throws RemoteException 
	 */
	public String  pop2pccWebService(CpeUserInfo cui)throws SQLException, RemoteException{
	
		ISendPccInfoService sendPcc = SpringContext.getBean("sendPccInfoService",SendPccInfoServiceImpl.class);
		
					 log.info("开始调用pcc接口进行用户销户操作，销户手机号为："+cui.getProductNo());
					 String	 result = sendPcc.cpeUserStatusChange(cui.getProductNo());
				
		return result;
	}
	@Override
	public String cpeDeleteCpeUser(CpeUserInfo cui) throws MysqlDataAccessExcetion, FileErrorException, SQLException,DelSubscriberException, RemoteException{
		
		int update = cpeDeleteDao.deleteCpeUser(cui);
		if(update>0){
			log.info("cpe用户销户标识"+cui.getSubsid()+"更新了 "+update+" 行");
		}else{
			log.error("cpe用户销户标识"+cui.getSubsid()+"更新失败");
			throw new FileErrorException("cpe用户销户标识"+cui.getSubsid()+"更新失败");
		}
		String result = pop2pccWebService(cui);
		
		if(!"1".equals(result)){
			 log.info("用户销户调用pcc接口返回值="+result);
			throw new DelSubscriberException("调用PCC销户接口失败返回值="+result+",手机号为"+cui.getProductNo()+",用户状态为:"+cui.getStatusChangeTime()+",用户CPE设备id="+cui.getSubsid());
		}else{
			cpeDeleteDao.deleteUserLockedInfo(cui);
		}
		return result;
	}
public void setCpeDeleteDao(CpeDeleteDao dao){ 
	this.cpeDeleteDao=dao;
}
public CpeDeleteDao getCpeDeleteDao(){
	return cpeDeleteDao;
}
}
