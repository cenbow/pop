package com.ai.bdx.pop.controller;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import redis.clients.jedis.exceptions.JedisException;

import com.ai.bdx.pop.base.PopController;
import com.ai.bdx.pop.bean.CpeAccessibleLacCiBean;
import com.ai.bdx.pop.bean.CpeLockLacCiBean;
import com.ai.bdx.pop.model.CpeUserInfo;
import com.ai.bdx.pop.model.CpeUserLockRel;
import com.ai.bdx.pop.service.ICpeManagerService;
import com.ai.bdx.pop.util.LogOperateUtil;
import com.ai.bdx.pop.util.PopConstant;
import com.ai.bdx.pop.util.SpringContext;
import com.ai.bdx.pop.wsclient.util.StringUtil;
import com.asiainfo.biframe.utils.config.Configure;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;

/**
 * CPE控制类：
 * 该类完成的功能：
 * 1、单用户参数重置：
 *   1) queryLockedLacCi：查询一个CPE设备的已有锁网小区、
 *   2) queryAccessibleLacCi：查询一个CPE的可接入小区、
 *   3) cpeUserReset：根据用户号码重置CPE参数
 * @author hpa
 *
 */
public class CpeManagerController extends PopController {
	private static final Logger log 
		= LogManager.getLogger(CpeManagerController.class);
	private final static Integer CPE_LOCKEDNET_DEFAULT_LACCICOUNT = 9;
	
	/**
	 * 跳转到单用户CPE参数重置页面
	 */
	public void index(){
		render("cpeManager/resetCpeParamIndex.jsp");
	}
	
	/**
	 * 跳转到参数重置-批量用户  页面
	 */
	public void batchResetCpeParamIndex(){
		Map<String,String> netLockFlagMap = new HashMap<String,String>();
		Map<String,String> busiTypeMap = new HashMap<String,String>();
		
		netLockFlagMap.put("0", "未锁网");
		
		netLockFlagMap.put("2", "锁网中");
		
		netLockFlagMap.put("1", "锁网完成");
		
		
		busiTypeMap.put("1", "正常");
		
		busiTypeMap.put("2", "城市限速");
		
		busiTypeMap.put("3", "农村限速（在9个锁网小区外上网）");
		
		setAttr("netLockFlagMap",netLockFlagMap);
		setAttr("busiTypeMap",busiTypeMap);
		render("cpeManager/batchResetCpeParam.jsp");
	}
	
	/**
	 * 根据锁网状态、业务状态、用户状态查询CPE信息
	 */
	public void queryBatchResetCpe(){
		int netLockFlag = this.getParaToInt("netLockFlag");
		int busiType = this.getParaToInt("busiType");
		List<Map<String, Object>>  cpeList = new ArrayList<Map<String,Object>>();
		try {
			if(netLockFlag != -1 || busiType != -1){
				ICpeManagerService cpeManagerService = 
						SpringContext.getBean(
								"cpeManagerService",
								ICpeManagerService.class);
				cpeList =  cpeManagerService.queryBatchResetCpe(netLockFlag,busiType);
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			log.error("queryBatchResetCpe Error:"+e.toString());
		}
		setAttr("dimCpeStationList", cpeList);
	    this.getRequest().setAttribute("netLockFlag", netLockFlag);
	    this.getRequest().setAttribute("busiType", busiType);
		batchResetCpeParamIndex();
	}
	
	/**
	 *  查询一个CPE设备的已有锁网小区
	 */
	public void queryLockedLacCi(){
		String subsid = getPara("subsid");
		String productNo = getPara("productNo");
		String createTime = "";
		String netType = "";
		List<CpeLockLacCiBean> cpeLockLacCiBeanList = new ArrayList<CpeLockLacCiBean>();
		if (!Strings.isNullOrEmpty(subsid) || !Strings.isNullOrEmpty(productNo)) {
			ICpeManagerService cpeManagerService = 
					SpringContext.getBean(
							"cpeManagerService",
							ICpeManagerService.class);
			try {
				cpeLockLacCiBeanList = cpeManagerService.queryLockedLacCi(subsid, productNo);
			} catch (Exception e) {
				log.error(e.getMessage());
			}
		}
		
		if(cpeLockLacCiBeanList != null && cpeLockLacCiBeanList.size() > 0){
			if(Strings.isNullOrEmpty(productNo)){
				productNo = cpeLockLacCiBeanList.get(0).getProductNo();
			}
			if(Strings.isNullOrEmpty(subsid)){
				subsid = cpeLockLacCiBeanList.get(0).getSubsid();
			}
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			
			createTime = sdf.format(cpeLockLacCiBeanList.get(0).getCreateTime());
			netType	= cpeLockLacCiBeanList.get(0).getNetType();
		}
		
		setAttr("cpeLockLacCiBeanList", cpeLockLacCiBeanList);
		setAttr("subsid", subsid == null ? "" : subsid);
		setAttr("productNo", productNo == null ? "" : productNo);
		setAttr("netType", netType);
		setAttr("createTime",createTime);
		//定向到单用户参数重置页面
		render("cpeManager/resetCpeParamIndex.jsp");
	}
	
	/**
	 * 查询一个CPE的可接入小区
	 */
	public void queryAccessibleLacCi() {
		//CPE编码
		String subsid = this.getPara("subsid");
				
		//手机号码
		String productNo = this.getPara("productNo");
		
		String keyword = this.getPara("keyword");
		
		if(!Strings.isNullOrEmpty(keyword)){
			try {
				keyword = new String(keyword.getBytes("iso8859-1"),"utf-8");
			} catch (UnsupportedEncodingException e) {
				log.error("将关键字编码方式由iso8859-1转为utf-8时发生异常！",e);
			}
		}
		
		List<CpeAccessibleLacCiBean> cpeAccessibleLacCilist = null;
		if(!Strings.isNullOrEmpty(subsid) || !Strings.isNullOrEmpty(productNo)){
			CpeAccessibleLacCiBean cpeAccessibleLacCiBean = new CpeAccessibleLacCiBean();
			cpeAccessibleLacCiBean.setSubsid(subsid);
			cpeAccessibleLacCiBean.setProductNo(productNo);
			cpeAccessibleLacCiBean.setKeyword(keyword);
			ICpeManagerService cpeManagerService = 
					SpringContext.getBean(
							"cpeManagerService",
							ICpeManagerService.class);
			try {
				cpeAccessibleLacCilist = 
						cpeManagerService.queryAccessibleLacCi(cpeAccessibleLacCiBean);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if(cpeAccessibleLacCilist == null){
			cpeAccessibleLacCilist = new ArrayList<CpeAccessibleLacCiBean>();
		}
		setAttr("cpeAccessibleLacCilist", cpeAccessibleLacCilist);
		
		//定向到单用户参数重置页面
		/*render("cpeMagnager/resetCpeParamIndex.jsp");*/
		renderJson(cpeAccessibleLacCilist);
	}
	
	/**
	 * 根据用户号码重置CPE参数
	 */
	public void cpeUserReset() throws JedisException{
		Map<String, Object> result = Maps.newHashMap();
		String cpeLockedNetLacCiCountStr = Configure.getInstance().
				getProperty("CPE_LOCKEDNET_LACCICOUNT");
		int cpeLockedNetLacCiCount = 
				Strings.isNullOrEmpty(cpeLockedNetLacCiCountStr) 
				? CPE_LOCKEDNET_DEFAULT_LACCICOUNT 
				: Integer.parseInt(cpeLockedNetLacCiCountStr);
		//CPE编码
		String subsid = this.getPara("subsid");
		
		//手机号码
		String productNo = this.getPara("productNo");
		productNo = productNo == null ? "" : productNo.trim();
		subsid = subsid == null ? "" : subsid.trim();
		
		if(Strings.isNullOrEmpty(subsid) && Strings.isNullOrEmpty(productNo)){
			result.put("flag", 2);//
			result.put("message", "CPE设备号和手机号码不能同时为空！");
			renderJson(result);
			return;
		}
		
		//小区的lacCi之间以逗号“,”分割
		String userLocations = this.getPara("userLocations");
		
		/*
		 * 前端传入的参数中只有手机号码，没有subsid时
		 */
		int netLockFlag = -1;
		try{
			CpeUserInfo cpeUserInfo = null;
			if(productNo.length() == 0){
				cpeUserInfo = CpeUserInfo.dao().
						findFirst("SELECT SUBSID, NET_LOCK_FLAG, USER_STATUS " +
								"FROM CPE_USER_INFO WHERE SUBSID = ? ", subsid);
				if(cpeUserInfo == null){
					result.put("flag", 2);//
					result.put("message", "参数重置失败，没有查询到CPE用户编码为" + subsid + "的CPE账号信息！");
					renderJson(result);
					return;
				}
				productNo = cpeUserInfo.getStr(CpeUserInfo.PRODUCT_NO);
				log.info("productNo:" + productNo);
				log.info("subsid:" + subsid);
			}else{
				cpeUserInfo = CpeUserInfo.dao().
						findFirst("SELECT SUBSID, NET_LOCK_FLAG, USER_STATUS " +
								"FROM CPE_USER_INFO WHERE PRODUCT_NO = ? ", productNo);
				if(cpeUserInfo == null){
					result.put("flag", 2);//
					result.put("message", "参数重置失败，没有查询到手机号码为" + productNo + "的CPE账号信息！");
					renderJson(result);
					return;
				}
				subsid = cpeUserInfo.getStr(CpeUserInfo.SUBSID);
				log.info("subsid:" + subsid);
				log.info("productNo:" + productNo);
			}
			
			netLockFlag = cpeUserInfo.getInt(CpeUserInfo.NET_LOCK_FLAG);
			int userStatus = cpeUserInfo.getInt(CpeUserInfo.USER_STATUS);
			if(userStatus == PopConstant.USER_STATUS_ARREARAGE){
				result.put("flag", 2);//
				result.put("message", "参数重置失败，你的CPE账号已经欠费！");
				String desc = String.format("CPE重置失败:%s,CPE编码:%s,电话号码:%s","参数重置失败，你的CPE账号已经欠费！", subsid,productNo);
				LogOperateUtil.log(LogOperateUtil.CPE_PARAM_RESET,desc,this.getRequest());
				renderJson(result);
				return;
			}else if(userStatus == PopConstant.USER_STATUS_CANCEL){
				result.put("flag", 2);//
				result.put("message", "参数重置失败，你的CPE账号已经销户！");
				String desc = String.format("CPE重置失败:%s,CPE编码:%s,电话号码:%s","参数重置失败，你的CPE账号已经销户！", subsid,productNo);
				LogOperateUtil.log(LogOperateUtil.CPE_PARAM_RESET,desc,this.getRequest());
				renderJson(result);
				return;
			}
			//记录日志
			String desc = String.format("CPE重置,CPE编码:%s,电话号码:%s", subsid,productNo);
			LogOperateUtil.log(LogOperateUtil.CPE_PARAM_RESET,desc,this.getRequest());
		}catch(Exception e){
			result.put("flag", 2);//
			result.put("message", "参数重置失败，查询CPE用户信息时发生异常！");
			renderJson(result);
			String desc = String.format("CPE重置失败:%s,CPE编码:%s,电话号码:%s", e.getMessage(),subsid,productNo);
			LogOperateUtil.log(LogOperateUtil.CPE_PARAM_RESET,desc,this.getRequest());
			return;
		}
		
		ICpeManagerService cpeManagerService = 
				SpringContext.getBean(
						"cpeManagerService",
						ICpeManagerService.class);
		try {
			cpeManagerService.cpeUserReset(subsid, 
					productNo, userLocations, cpeLockedNetLacCiCount, netLockFlag);
			result.put("flag", 1);
			result.put("message", "参数重置成功！");
		}catch(JedisException e1){
			result.put("flag", 2);
			result.put("message", e1.getMessage());
		}catch (Exception e) {
			result.put("flag", 2);
			result.put("message", "参数重置失败，" + e.getMessage());
		}
		renderJson(result);
	}
	
	/**
	 * 根据CPE设备的唯一标识USIM（不支持模糊匹配）、手机号码（不支持模糊匹配），
	 * 查询此用户的基本信息和可接入小区信息列表（小区名称）
	 * 该方法未使用
	 */
	public void searchCpeUserInfo(){
		initAttributes();
		
		//CPE设备的唯一标识USIM
		String subsid = this.getPara("subsid");
		
		//手机号码
		String productNo = this.getPara("productNo");
		StringBuffer sqlInfo = new StringBuffer();
		StringBuffer sqlRel = new StringBuffer();
		List<Object>params = new ArrayList<Object>();
		sqlInfo.append("SELECT " +
				"SUBSID," +
				"PRODUCT_NO," +
				"NET_TYPE," +
				"CITY_NAME," +
				"COUNTY_NAME," +
				"COUNTRY_NAME," +
				"BUSI_TYPE," +
				"USER_STATUS," +
				"CREATE_TIME");
		sqlInfo.append(" FROM CPE_USER_INFO C");
		sqlInfo.append(" WHERE 1 = 1 ");
		
		sqlRel.append("SELECT LOCKED_LAC_CI,CELL_NAME");
		sqlRel.append(" FROM DIM_CPE_LAC_CI D LEFT JOIN CPE_USER_LOCK_REL C ");
		sqlRel.append(" ON C.LOCKED_LAC_CI = D.LAC_CI_DEC_ID");
		sqlRel.append(" WHERE 1 = 1");
		
		if (!Strings.isNullOrEmpty(subsid)) {
			sqlInfo.append(" AND C.SUBSID = ? ");
			sqlRel.append(" AND C.SUBSID = ? ");
			params.add(subsid);
		}
		
		if (!Strings.isNullOrEmpty(productNo)) {
			sqlInfo.append(" AND C.PRODUCT_NO = ? ");
			sqlRel.append(" AND C.PRODUCT_NO = ? ");
			params.add(productNo);
		}
		//CpeUserInfo.dao().find(sql.toString(), new Object[]{});
		CpeUserInfo cpeUserInfo = CpeUserInfo.dao().findFirst(sqlInfo.toString(), params.toArray());
		List<CpeUserLockRel> cpeUserLockRelList = CpeUserLockRel.dao().find(sqlRel.toString(), params.toArray());
		//List<Record> cpeInfoResult = Db.find(sqlInfo.toString(), params.toArray());
		//List<Record> cpeRelResult =Db.find(sqlRel.toString(),params.toArray());
		
		//renderJson(PopUtil.convertRecordList2JSONString(cpeInfoResult));
		//renderJson(PopUtil.convertRecordList2JSONString(cpeRelResult));
		setAttr("cpeUserInfo", cpeUserInfo);
		setAttr("cpeUserLockRelList", cpeUserLockRelList);
		render("cpeManager/resetCpeParamIndex.jsp");
	}
	
	public void resetAllCpeByFlag(){
		Map<String, Object> result = Maps.newHashMap();
		int allCount = 0;
		int successfulCount = 0;
		String netLockFlag = this.getPara("netLockFlag");
		String busiType = this.getPara("busiType");
		String lineLimit =  this.getPara("lineLimit");
		List<Map<String, Object>>  cpeList = new ArrayList<Map<String,Object>>();
		Map<String,Object> param = new HashMap<String,Object>();
		String cpeLockedNetLacCiCountStr = Configure.getInstance().
				getProperty("CPE_LOCKEDNET_LACCICOUNT");
		int cpeLockedNetLacCiCount = 
				Strings.isNullOrEmpty(cpeLockedNetLacCiCountStr) 
				? CPE_LOCKEDNET_DEFAULT_LACCICOUNT 
				: Integer.parseInt(cpeLockedNetLacCiCountStr);
		try{
			if(StringUtil.isNotEmpty(netLockFlag)) 
			{
				param.put("netLockFlag", netLockFlag);
			}
			if(StringUtil.isNotEmpty(busiType))
			{
				param.put("busiType", busiType);
			}
			if(StringUtil.isNotEmpty(lineLimit))
			{
				param.put("lineLimit",Integer.parseInt(lineLimit));
			}
		   ICpeManagerService cpeManagerService = 
						SpringContext.getBean(
								"cpeManagerService",
								ICpeManagerService.class);
		   cpeList =  cpeManagerService.queryAllResetByFlag(param);
		   
		   if(cpeList != null && cpeList.size() > 0){
			   allCount = cpeList.size();
			   String subsid =  "";
			   String productNo = "";
			   int tableNetLockFlag = -1;
			   for(Map<String, Object> tmp:cpeList)
			   {
				  subsid = (String)tmp.get("subsid");
				  productNo = (String)tmp.get("product_no");
				  tableNetLockFlag = (Integer)tmp.get("net_lock_flag");
				  String userLocations = null;
				  try {
					  cpeManagerService.cpeUserReset(subsid,productNo, userLocations, cpeLockedNetLacCiCount, tableNetLockFlag);
					  successfulCount++;
					  log.info(String.format("重置用户subsid:%s,productNo:%s 成功，" +
					  		"共:%s个CPE用户,已经参数重置到第:%s个CPE用户", subsid, productNo, allCount, successfulCount));
				  }catch(JedisException e){
					  throw new JedisException(e.getMessage());
				  }catch (Exception e) {
					 log.error(String.format("重置用户subsid:%s,productNo:%s 失败，" +
						  		"共:%s个CPE用户,参数重置到第:%s个CPE用户 ，原因:%s", subsid, productNo, allCount, successfulCount,e.toString()));
				  }
			   }
		   }else{
			   result.put("flag", 1);
			   result.put("message", "没有查询符合条件的cpe用户，重置失败！");
		   }
		   result.put("flag", 1);
		   result.put("message", String.format("重置:%s个CPE用户成功，:%s个失败！", successfulCount,allCount - successfulCount));
		}catch(JedisException e){
			 result.put("flag", 3);
			 log.error(e.getMessage());
			 result.put("message",e.getMessage());
		}catch (Exception e) {
			 e.printStackTrace();
			 result.put("flag", 3);
			 result.put("message", String.format("重置:%s个CPE用户成功，:%s个失败！", successfulCount,allCount - successfulCount));
		}
		//this.getRequest().setAttribute("netLockFlag", netLockFlag);
	    //this.getRequest().setAttribute("busiType", busiType);
	    //this.getRequest().setAttribute("lineLimit", lineLimit);
		//setAttr("dimCpeStationList", cpeList);
		//queryBatchResetCpe();
		renderJson(result);
	}
}