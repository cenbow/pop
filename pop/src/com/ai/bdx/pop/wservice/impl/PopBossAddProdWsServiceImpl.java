/**   
 * @Title: PopBossAddProdServiceImpl.java
 * @Package com.ai.bdx.pop.wservice
 * @Description: TODO(用一句话描述该文件做什么)
 * @author A18ccms A18ccms_gmail_com   
 * @date 2015-8-3 下午2:20:52
 * @version V1.0   
 */
package com.ai.bdx.pop.wservice.impl;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.jws.WebService;

import net.sf.json.JSONObject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dom4j.DocumentException;
import org.springframework.dao.DataAccessException;

import redis.clients.jedis.exceptions.JedisException;

import com.ai.bdx.pop.bean.PopTaskBean;
import com.ai.bdx.pop.bean.Prod;
import com.ai.bdx.pop.dao.BOSS2POPWebServiceDao;
import com.ai.bdx.pop.kafka.reveiver.ICepMessageReceiveService;
import com.ai.bdx.pop.kafka.reveiver.impl.CepMessageReceiverImpl;
import com.ai.bdx.pop.service.CpeDeleteService;
import com.ai.bdx.pop.service.ICpeManagerService;
import com.ai.bdx.pop.service.IPopSendOddService;
import com.ai.bdx.pop.service.impl.PopBossAddProdServiceImpl;
import com.ai.bdx.pop.service.impl.WebServiceChangeImsiService;
import com.ai.bdx.pop.util.PopConstant;
import com.ai.bdx.pop.util.SpringContext;
import com.ai.bdx.pop.wservice.IPopBossAddProdWsService;
import com.ailk.bdx.pop.adapter.jedis.JedisClientUtil;
import com.asiainfo.biapp.pop.Exception.DelSubscriberException;
import com.asiainfo.biapp.pop.Exception.FileErrorException;
import com.asiainfo.biapp.pop.Exception.MysqlDataAccessExcetion;
import com.asiainfo.biapp.pop.model.CpeUserInfo;
import com.asiainfo.biapp.pop.util.ftp.FtpFileUtil;
import com.asiainfo.biframe.utils.spring.SystemServiceLocator;
import com.google.common.base.Strings;

/**
 * @ClassName: PopBossAddProdServiceImpl
 * @Description: POP和BOSS接口 流量产品新增工单。
 * 				流量产品新增、退订时，BOSS要向POP平台发送工单
 * @author jinlong
 * @date 2015-8-3 下午2:20:52
 * 
 */
@WebService(endpointInterface = "com.ai.bdx.pop.wservice.IPopBossAddProdWsService")
public class PopBossAddProdWsServiceImpl implements IPopBossAddProdWsService {

	public PopBossAddProdServiceImpl popBossAddProdServiceImpl=new PopBossAddProdServiceImpl();
	
	private static final Logger Log = LogManager.getLogger();
	
	/**
	 * @Title: addProdService
	 * @Description: BOSS订购退订（BOSS到POP实时的订购退订流量产品数据接口）
	 * @param @param xmlStr
	 * @param @return    
	 * @return String 
	 * @throws
	 */
	@Override
	public String addProdService(String xmlStr) {
		// TODO Auto-generated method stub
		Log.info("POP服务端接收到BOSS请求信息===="+xmlStr);
		int retcode = 0;
		int retinfo = 0;
		String retmsg = "";
		if(Strings.isNullOrEmpty(xmlStr)){
			Log.info("BOSS请求XML为NULL或空字符串");
			retcode = 100;
			retinfo = 100;
			retmsg = "失败，失败原因,BOSS请求XML为NULL或空字符串";
			return bulidResponseXml(retcode,retinfo,retmsg);
		}
		Log.info("解析BOSS请求XML开始");
		//1、解析request 的 XML
		List<Prod> list = new ArrayList<Prod>();
		try {
			list = popBossAddProdServiceImpl.xmlToList(xmlStr);

		} catch (Exception e) {
			Log.error("BOSS请求XML解析异常",e);
			retcode = 100;
			retinfo = 100;
			retmsg = "失败，失败原因,BOSS请求XML解析异常";
			return bulidResponseXml(retcode,retinfo,retmsg);
		}	
			
			//2、获取正在派单的活动信息，取出产品编号和策略规则ID信息
		String res = "100";
		BOSS2POPWebServiceDao boss2POPwebServiceDao;
		
			Prod prod = list.get(0);
			try {
				boss2POPwebServiceDao = SpringContext.getBean("boss2popWebServiceDao",BOSS2POPWebServiceDao.class);
				res = boss2POPwebServiceDao.bossToPopGprsOtp(prod);
			} catch (DataAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				retcode = 100;
				retinfo = 100;
				retmsg = "失败，失败原因,数据格式不规范";
				return bulidResponseXml(retcode,retinfo,retmsg);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				retcode = 100;
				retinfo = 100;
				retmsg = "失败，失败原因,时间格式不规范";
				return bulidResponseXml(retcode,retinfo,retmsg);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		if(res.equals("100")){
			retcode = 100;
			retinfo = 100;
			retmsg = "失败，签约失败";
			return bulidResponseXml(retcode,retinfo,retmsg);
		}
		
		return bulidResponseXml(retcode,retinfo,retmsg);
	}
	
	




	@Override
	public String cpeInstallAdapterTrigger(String xmlStr) {
		// TODO Auto-generated method stub
		Log.info("POP服务端接收到BOSS请求信息===="+xmlStr);
		int retcode = 0;
		int retinfo = 0;
		String retmsg = "";
		if(Strings.isNullOrEmpty(xmlStr)){
			Log.info("BOSS请求XML为NULL或空字符串");
			retcode = 100;
			retinfo = 100;
			retmsg = "失败，失败原因,BOSS请求XML为NULL或空字符串";
			return bulidResponseXml(retcode,retinfo,retmsg);
		}
		Log.info("解析BOSS请求XML开始");
		//1、解析request 的 XML
		List list = new ArrayList();
		try {
			list = popBossAddProdServiceImpl.xmlToUserList(xmlStr);

		} catch (Exception e) {
			Log.error("BOSS请求XML解析异常",e);
			retcode = 100;
			retinfo = 100;
			retmsg = "失败，失败原因,BOSS请求XML解析异常";
			return bulidResponseXml(retcode,retinfo,retmsg);
		}
		//
		ICpeManagerService cpeManagerService = null;
		try {
			cpeManagerService = (ICpeManagerService) SystemServiceLocator.getInstance().getService("cpeManagerService");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			cpeManagerService.batchUpdate(list, "webservice");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			retmsg = "失败,失败原因,用户已开户或用户信息不规范";
			retcode = 0;
			retinfo = 0;
			return bulidResponseXml(retcode,retinfo,retmsg);
		}
		return bulidResponseXml(1,1,"用户开户成功!");
	}



	@Override
	public String scanFtpForDeleteTrigger(String xmlStr) {
		// TODO Auto-generated method stub
		Log.info("POP服务端接收到BOSS请求信息===="+xmlStr);
		int retcode = 0;
		int retinfo = 0;
		String retmsg = "";
		if(Strings.isNullOrEmpty(xmlStr)){
			Log.info("BOSS请求XML为NULL或空字符串");
			retcode = 100;
			retinfo = 100;
			retmsg = "失败，失败原因,BOSS请求XML为NULL或空字符串";
			return bulidResponseXml(retcode,retinfo,retmsg);
		}
		Log.info("解析BOSS请求XML开始");
		List<CpeUserInfo> list = new ArrayList();
		//1、解析request 的 XML
		try {
			list = popBossAddProdServiceImpl.xmlToUserList(xmlStr);
		} catch (Exception e) {
			Log.error("BOSS请求XML解析异常",e);
			retcode = 100;
			retinfo = 100;
			retmsg = "失败，失败原因,BOSS请求XML解析异常";
			return bulidResponseXml(retcode,retinfo,retmsg);
		}
		//2.开始销户
		CpeDeleteService cpeDeleteService;
		if(list.size()>0){
			cpeDeleteService =SpringContext.getBean("cpeDeleteService",CpeDeleteService.class);
			String result="";
			boolean isConnect=true;
			boolean isFileSuccess=true;
			for(int j=0;j<list.size();j++){
			 	try {
			 		result=cpeDeleteService.cpeDeleteCpeUser(list.get(j));
			 		if(!"1".equals(result)){
			 			isFileSuccess=false;
			 		}
					 Log.info("用户销户调用pcc接口返回值="+result);
				}catch(MysqlDataAccessExcetion e){
					isConnect=false;
					e.printStackTrace();
					Log.error("销户更新用户状态失败,Mysql操作异常："+e.getMessage());
				} catch(JedisException e){
					isConnect=false;
					e.printStackTrace();
					Log.error("销户更新用户状态失败,redis连接异常："+e.getMessage());
				}catch(SQLException e){
					isConnect=false;
					e.printStackTrace();
					Log.error("销户更新用户状态失败,调用PCC接口时Mysql连接异常："+e.getMessage());
				}catch (FileErrorException e) { 
						isFileSuccess=false;
						Log.error("销户更新用户状态失败,文件异常："+e.getMessage());
				}catch(RemoteException e){
					isConnect=false;
					e.printStackTrace();
					Log.error("销户更新用户状态失败,调用PCC接口时webservice连接异常："+e.getMessage());

				}catch (DelSubscriberException e) { 
					isFileSuccess=false;
					Log.error("销户更新用户状态失败,调用PCC销户接口失败："+e.getMessage());
				}catch (Exception e) { 
					if(e!=null){
							if(e.getMessage().startsWith("Could not open JDBC Connection for transaction", 0)){
								isConnect=false;
								e.printStackTrace();
								Log.error("销户更新用户状态失败,数据库连接异常："+e.getMessage());
							}else if(e.getMessage().startsWith("Could not commit JDBC transaction", 0)){
								isFileSuccess=false;
								Log.error("销户更新用户状态失败,数据库连接异常："+e.getMessage());
							}else{
								isFileSuccess=false;
								Log.error("销户更新用户状态失败:"+e.getMessage());
							}
					}
					
				}
			
			 //调用redis，清除cpe锁网信息	 	
			 if("1".equals(result)){
				Log.info("销户手机号-"+list.get(j).getProductNo()+"---------->操作成功！");
				Long count =null;
			 	try{
			 		count = JedisClientUtil.delKeyValue(PopConstant.REDIS_UNL_PREFIX+list.get(j).getSubsid());
			 		Log.info("删除key："+PopConstant.REDIS_UNL_PREFIX+list.get(j).getSubsid()+"----"+count+"个");
			 		Boolean flag=JedisClientUtil.print(PopConstant.REDIS_UNL_PREFIX+list.get(j).getSubsid());
					if(!flag){
						Log.info("-------删除redis数据库CPE用户"+list.get(j).getProductNo()+"锁网信息----->成功");
					}else if(flag){
						Log.info("-------删除redis数据库CPE用户"+list.get(j).getProductNo()+"锁网信息----->失败");
					}else{
						Log.error("--------redis数据库CPE用户"+list.get(j).getProductNo()+"redis操作错误");
					}
					Log.info("-------redis数据库CPE用户"+list.get(j).getSubsid()+"set集合是否存在:"+JedisClientUtil.isExistSet(PopConstant.REDIS_UNL_PREFIX+list.get(j).getSubsid()));
			 	}catch(JedisException e){
					e.printStackTrace();
					Log.error("-------销户更新用户状态失败,redis连接异常："+e.getMessage());
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}finally{
					
				}
			}else{
				Log.info("销户手机号-"+list.get(j).getProductNo()+"---------->操作失败！");
			}
		}
		}
		
		return bulidResponseXml(50, 50, "文件解析无误");
	}



	@Override
	public String cpeImsiChangeTrigger(String xmlStr) {
		// TODO Auto-generated method stub
		Log.info("POP服务端接收到BOSS请求信息===="+xmlStr);
		int retcode = 0;
		int retinfo = 0;
		String retmsg = "";
		if(Strings.isNullOrEmpty(xmlStr)){
			Log.info("BOSS请求XML为NULL或空字符串");
			retcode = 100;
			retinfo = 100;
			retmsg = "失败，失败原因,BOSS请求XML为NULL或空字符串";
			return bulidResponseXml(retcode,retinfo,retmsg);
		}
		Log.info("解析BOSS请求XML开始");
		//1、解析request 的 XML
		List list = new ArrayList();
		try {
			list = popBossAddProdServiceImpl.xmlToUserList(xmlStr);
		
		} catch (Exception e) {
			Log.error("BOSS请求XML解析异常",e);
		retcode = 100;
		retinfo = 100;
		retmsg = "失败，失败原因,BOSS请求XML解析异常";
			return bulidResponseXml(retcode,retinfo,retmsg);
		}
		//
		WebServiceChangeImsiService  changeImsi;
		if(list.size()>0){
			changeImsi =(WebServiceChangeImsiService) SpringContext.getBean("webServiceChangeImsi",WebServiceChangeImsiService.class);
			for (int i = 0; i < list.size(); i++) {
				Map map = (Map) list.get(i);
				String prono = (String) map.get("productno");
				String oldMsi = (String) map.get("oldmsi");
				String newMsi = (String) map.get("newmsi");
				try {
					changeImsi.changeImsi(prono, oldMsi, newMsi);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return bulidResponseXml(50, 50, "文件解析无误");
	}
	
	/**
	 * @Title: 返回ResponseXml
	 * @Description: TODO
	 * @param @param retcode
	 * @param @param retinfo
	 * @param @param retmsg
	 * @param @return    
	 * @return String 
	 * @throws
	 */
	public String bulidResponseXml(int retcode,int retinfo,String retmsg){
		StringBuffer operation_response =new StringBuffer("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		operation_response.append("<operation_response>");//协议头节点
		operation_response.append("<msgrspheader>");//协议头节点
		operation_response.append("<retinfo>");//返回结果信息
		operation_response.append("<retcode>");//Int 返回码 XML节点 0代表成功 100代表失败
		operation_response.append(retcode);			//Int 返回码
		operation_response.append("</retcode>");
		operation_response.append("<rettype>");//Int 返回码类型  XML节点 0代表成功 100代表失败
		operation_response.append(retinfo);			//Int 返回码类型
		operation_response.append("</rettype>");
		operation_response.append("<retmsg>");//String V512 返回消息 XML节点
		operation_response.append(retmsg);			//String V512 返回消息
		operation_response.append("</retmsg>");
		operation_response.append("</retinfo>");
		operation_response.append("</msgrspheader>");
		operation_response.append("</operation_response>");
		return operation_response.toString();
	}
	
	
}
