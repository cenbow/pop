/**   
 * @Title: PopBossAddProdServiceImpl.java
 * @Package com.ai.bdx.pop.service.impl
 * @Description: TODO(用一句话描述该文件做什么)
 * @author A18ccms A18ccms_gmail_com   
 * @date 2015-8-7 下午2:57:39
 * @version V1.0   
 */
package com.ai.bdx.pop.service.impl;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.ai.bdx.pop.bean.CpeUserInfo;
import com.ai.bdx.pop.bean.Prod;
import com.ai.bdx.pop.util.PopConstant;




/**
 * @ClassName: PopBossAddProdServiceImpl
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author jinlong
 * @date 2015-8-7 下午2:57:39
 * 
 */
public class PopBossAddProdServiceImpl {
	
	private static Logger log =  LogManager.getLogger();
	
	/**订购退订webservice 解析
	 * @Title: xmlToList
	 * @Description: TODO
	 * @param @param xml
	 * @param @return
	 * @param @throws DocumentException    
	 * @return List 
	 * @throws
	 */
	public static List<Prod> xmlToList(String xml) throws DocumentException{
		List<Prod> list =new ArrayList();
		Document dom = DocumentHelper.parseText(xml);
		 Element root=dom.getRootElement();
		 Element operation_request=root.element("operation_request");
		 Element MsgBody=root.element("msgbody");
		 if(MsgBody!=null){
				 List<Element> prodchinfo=MsgBody.elements("prodchinfo");
				 log.info("prodchinfo.size="+prodchinfo.size());
				 Prod prod = new Prod();
				 if(prodchinfo!=null&&prodchinfo.size()>0){
					 for(int i=0;i<prodchinfo.size();i++){
						 Element element = prodchinfo.get(i);
						 String prodid = element.element("prodid").getText();//产品编码
						 String mobileno = element.element("mobileno").getText();//手机号码
						 String eff_time = element.element("eff_time").getText();//生效时间
						 String exp_time = "";
						 //判断是否有失效时间这个节点
						 if(element.element("exp_time")!=null){
							 exp_time = element.element("exp_time").getText();//失效时间
						 }
						 String region = element.element("region").getText();//地区
						 String channel = element.element("channel").getText();//渠道
						 String optype = element.element("optype").getText();//操作类型
						 prod.setProdid(prodid);
						 prod.setMobileno(mobileno);
						 prod.setEff_time(eff_time);
						 if(exp_time!=null&&!"".equals(exp_time)){
							 prod.setExp_time(exp_time);
						 }
						 prod.setRegion(region);
						 prod.setChannel(channel);
						 prod.setOptype(optype);
						 list.add(prod);
					 }
				 }
		 }
		 
		return list;
	}
	
	
	public static List xmlToUserList(String xmlStr) throws DocumentException{
		 List list = new ArrayList();
		 Document dom = DocumentHelper.parseText(xmlStr);
		 Element root=dom.getRootElement();
		 Element operation_request=root.element("operation_request");
		 String operation = operation_request.getText();
		 Element MsgBody=root.element("userbody");
		 if(MsgBody!=null){
			 List<Element> userinfo=MsgBody.elements("userinfo");
			 if(operation.equals("cpeInstallAdapterTrigger")){
				 log.debug("userinfo size is "+userinfo.size());
				 if(userinfo!=null&&userinfo.size()>0){
					 for (int i = 0; i < userinfo.size(); i++) {
						 com.ai.bdx.pop.bean.CpeUserInfo cpeUserInfo = new CpeUserInfo();
						 Element element = userinfo.get(i);
						 String productno = element.element("productno").getText();
						 String subsid = element.element("subsid").getText();
						 String nettype = element.element("nettype").getText();
						 String countryname = element.element("countryname").getText();
						 String createtime = element.element("createtime").getText();
						 String plancode = element.element("plancode").getText();
						 cpeUserInfo.setProductNo(productno);
						 cpeUserInfo.setSubsid(subsid);
						 cpeUserInfo.setNetType(nettype);
						 cpeUserInfo.setCountryName(countryname);
						 SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
						 try {
							cpeUserInfo.setCreateTime(new Timestamp(sdf.parse(createtime).getTime()));
						 } catch (ParseException e) {
								log.error("BOSS提供的CPE数据行的开户时间字段不符合规范，请检查！");
						 }
						 cpeUserInfo.setPlanCode(plancode);
						 list.add(cpeUserInfo);
					}
				 }
			 }else if(operation.equals("scanFtpForDeleteTrigger")){
				 for (int i = 0; i < userinfo.size(); i++) {
					 com.asiainfo.biapp.pop.model.CpeUserInfo cpeUserInfo = new com.asiainfo.biapp.pop.model.CpeUserInfo();
					 Element element = userinfo.get(i);
					 String productno = element.element("productno").getText();
					 String subsid = element.element("subsid").getText();
					 String userstatu = element.element("userstatu").getText();
					 cpeUserInfo.setProductNo(productno);
					 cpeUserInfo.setSubsid(subsid);
					 if ("US24".equalsIgnoreCase(userstatu)) {// 欠费销户
							cpeUserInfo.setUserStatus(
									PopConstant.USER_STATUS_ARREARAGE);
						}
					if ("US20".equalsIgnoreCase(userstatu)) {// 销户
												cpeUserInfo.setUserStatus(
									PopConstant.USER_STATUS_CANCEL);
					}
					list.add(cpeUserInfo);
				 }
			}else if(operation.equals("cpeImsiChangeTrigger")){
				for (int i = 0; i < userinfo.size(); i++) {
					 Map<String,String> map = new HashMap<String, String>();
					 Element element = userinfo.get(i);
					 String productno = element.element("productno").getText();
					 String subsid = element.element("subsid").getText();
					 String oldmsi = element.element("oldmsi").getText();
					 String newmsi = element.element("newmsi").getText();
					 map.put("productno", productno);
					 map.put("subsid", subsid);
					 map.put("oldmsi", oldmsi);
					 map.put("newmsi", newmsi);
					 list.add(map);
				 }
			}
		 }
		return list;
	}

}
