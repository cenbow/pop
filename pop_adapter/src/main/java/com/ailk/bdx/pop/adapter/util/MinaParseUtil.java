package com.ailk.bdx.pop.adapter.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ailk.bdx.pop.adapter.bean.BaseConfig;
import com.ailk.bdx.pop.adapter.model.McCdrDetail;
import com.ailk.bdx.pop.adapter.schedule.NioTcpClient;
import com.google.common.collect.HashMultimap;

 
/***
 * 解析sca传来的字符串,将其转换成key-value的MAP
 *
 * */
 
public class MinaParseUtil {
	private static final Logger log = LogManager.getLogger();
	
	private static List<String> columnlist = new ArrayList();
	static{
		HashMultimap<String, BaseConfig> multiConfig = XmlConfigureUtil.getInstance().getMultConfigItems();
		Set<String> keys = multiConfig.keySet();
		for(String key : keys){
			if(XmlConfigureUtil.SOCKET.equals(key)){
				Set<BaseConfig> values = multiConfig.get(key);
				for(BaseConfig config : values){
					String sourceFileField = config.getContent().getSourceFileField();//sourceFileField
					String[] messages = StringUtils.splitByWholeSeparatorPreserveAllTokens(sourceFileField, ",");
					for(int i=0;i<messages.length;i++){
						columnlist.add(messages[i]);
					}
				}
			}
		}
		
	}
	
	/**
	 * 将message:key:value,key,value,key:value
	 * 转成map返回
	 * */
	public static Map getMessageDetailForMap(String message) {

		if (StringUtils.isNotEmpty(message)) {
			Class<McCdrDetail> clazz = null;
			McCdrDetail detail = null;
			Map m = new HashMap();
			try {
//				clazz = (Class<McCdrDetail>) Class
//						.forName("com.ailk.bdx.cep.adapter.model.McCdrDetail"); // MessageDetail
//
//				detail = (McCdrDetail) clazz.newInstance();
//				detail.setMessageMap(m);
				String[] messages = StringUtils
						.splitByWholeSeparatorPreserveAllTokens(message, ",");
				for (String msg : messages) {
					if (StringUtils.isNotBlank(msg)
							&& StringUtils.indexOf(msg, ':') != -1) {
						String k = StringUtils
								.splitByWholeSeparatorPreserveAllTokens(msg,
										":")[0];
						String v = StringUtils
								.splitByWholeSeparatorPreserveAllTokens(msg,
										":")[1];
						m.put(k, v);
					}
				}
			} catch (Exception e) {
				log.error("解析字符串消息成Map错误  ", e);
			}
			return m;
		} else {
			return null;
		}
	}
	
	/**
	 * 将message:key:value,key,value,key:value
	 * 转成ValueString返回
	 * */
	public static String getMessageDetailForStr(String message) {

		if (StringUtils.isNotEmpty(message)) {
			Class<McCdrDetail> clazz = null;
			McCdrDetail detail = null;
			StringBuffer m = new StringBuffer();
			Map m1 = new HashMap();
			try {
				String[] messages = StringUtils
						.splitByWholeSeparatorPreserveAllTokens(message, ",");
				for (int i=0;i<messages.length;i++) {
					String msg = messages[i];
					if (StringUtils.isNotBlank(msg)
							&& StringUtils.indexOf(msg, ':') != -1) {
						String k = StringUtils
						.splitByWholeSeparatorPreserveAllTokens(msg,
								":")[0];
						String v = StringUtils
								.splitByWholeSeparatorPreserveAllTokens(msg,
										":")[1];
						m1.put(k, v);
					}
				}
			for(int j=0;j<columnlist.size();j++){
				String coumlValue = (String)m1.get(columnlist.get(j));
				if(j<columnlist.size()-1){
					m.append(coumlValue).append(",");
				}else{
					m.append(coumlValue);
				} 
			 
			}
			
			 
			} catch (Exception e) {
				log.error("解析字符串消息成String错误  ", e);
			}
			return m.toString();
		} else {
			return null;
		}
	}
	
	
	
}
