package com.ai.bdx.pop.util;

import java.io.StringWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import com.asiainfo.biframe.utils.date.DateUtil;

/**
 * 简单的Bean对象与XML字符串的转换
 * @author zhangyb5
 *
 */
public class XmlBeanConverter {
	public static final String XML_HEADER = "<?xml version=\"1.0\" encoding=\"GBK\" standalone=\"no\" ?>";
	public static final String XML_HEADER_UTF8 = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\" ?>";
	public static final String XML_HEADER_UTF8_PCC = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\" ?>";

	private static final Logger log = LogManager.getLogger();

	/**
	 * Bean转换为XML
	 * 仅支持简单的Bean对象
	 * 属性格式为:XXXX_YYYYY
	 * @param bean --普通bean对象
	 * @param formatOut --是否格式输出
	 * @return
	 * @throws Exception
	 */
	public static <T> String beanToXML(T bean, boolean formatOut) throws Exception {
		StringBuffer xml = new StringBuffer(XML_HEADER);
		xml.append("<ROOT>");
		if (bean != null) {
			Field[] properties = bean.getClass().getDeclaredFields();
			for (int i = 0; i < properties.length; i++) {
				Method meth = bean.getClass().getMethod("get" + properties[i].getName());
				xml.append("<").append(properties[i].getName()).append(">").append(getValue(meth.invoke(bean)))
						.append("</").append(properties[i].getName()).append(">");
			}
		}
		xml.append("</ROOT>");
		if (formatOut) {
			Document doc = DocumentHelper.parseText(xml.toString());
			StringWriter out = null;
			OutputFormat formate = OutputFormat.createPrettyPrint();
			out = new StringWriter();
			XMLWriter writer = new XMLWriter(out, formate);
			writer.write(doc);
			xml.delete(0, xml.length()).append(out.toString());
		}
		return xml.toString();
	}

	/**
	 * Bean转换为XML
	 * 仅支持简单的Bean对象
	 * 属性格式为:XXXX_YYYYY
	 * @param bean --普通bean对象
	 * @param formatOut --是否格式输出
	 * @return
	 * @throws Exception
	 */
	public static <T> String beanToXML(T bean, boolean formatOut, String type ) throws Exception {
		StringBuffer xml = new StringBuffer();
		if("utf-8".equalsIgnoreCase(type)) {
			xml.append(XML_HEADER_UTF8);
		} else {
			xml.append(XML_HEADER);
		}
		xml.append("<ROOT>");
		if (bean != null) {
			Field[] properties = bean.getClass().getDeclaredFields();
			for (int i = 0; i < properties.length; i++) {
				Method meth = bean.getClass().getMethod("get" + properties[i].getName());
				xml.append("<").append(properties[i].getName()).append(">").append(getValue(meth.invoke(bean)))
						.append("</").append(properties[i].getName()).append(">");
			}
		}
		xml.append("</ROOT>");
		if (formatOut) {
			Document doc = DocumentHelper.parseText(xml.toString());
			StringWriter out = null;
			OutputFormat formate = OutputFormat.createPrettyPrint();
			out = new StringWriter();
			XMLWriter writer = new XMLWriter(out, formate);
			writer.write(doc);
			xml.delete(0, xml.length()).append(out.toString());
		}
		return xml.toString();
	}


	private static String getValue(Object valObj) {
		String value = "";
		if (null != valObj) {
			if (valObj instanceof Date) {
				value = DateUtil.date2String((Date) valObj, DateUtil.YYYY_MM_DD);
			} else if(valObj instanceof HashMap) {
				HashMap map = (HashMap)valObj;
				StringBuffer buffer = new StringBuffer();
				if( map != null) {
					Object[] kes = map.keySet().toArray();
					for(int i=0; i<kes.length ; i++) {
						buffer.append("<").append((String)kes[i]).append(">");
						buffer.append(getValue(map.get(kes[i])));
						buffer.append("</").append((String)kes[i]).append(">");
					}
				}
				value = buffer.toString();
			} else {
				value = String.valueOf(valObj);
			}
		}
		return value;
	}

	/**
	 * xml转换为Bean
	 * 简单的Bean
	 * 属性格式为:XXXX_YYYYY
	 * @param xml
	 * @param t
	 * @return
	 * @throws Exception
	 */
	public static <T> T xmlToBean(String xml, T t) throws Exception {
		Document doc = DocumentHelper.parseText(xml);
		Element root = doc.getRootElement();
		Field[] properties = t.getClass().getDeclaredFields();
		Method setMeth;

		for (int j = 0; j < properties.length; j++) {
			try {
				setMeth = t.getClass().getMethod("set" + properties[j].getName(), properties[j].getType());
				setMeth.invoke(t, root.elementText(properties[j].getName()));
			} catch (Exception e) {
				log.warn("set" + properties[j].getName() + " value error:", e);
			}
		}
		return t;
	}
	
	
	
	/**
	 * Bean转换为XML
	 * 仅支持简单的Bean对象
	 * 属性格式为:XXXX_YYYYY
	 * @param bean --普通bean对象
	 * @param formatOut --是否格式输出
	 * @return
	 * @throws Exception
	 */
	public static <T> String beanToXMLForPCC(T bean, boolean formatOut,String instruction) throws Exception {
		Map<String,String> map=new HashMap<String,String>();
		map.put("addSubscriber||updateSubscriber||delSubscriber||SubscribeService||updateSubscribedService" +
				"||unSubscribeService||subscribeUsrSessionPolicy||updateUsrSessionPolicy||unsubscribeUsrSessionPolicy"
				+"addUsrLocation||updateUsrLocation||delUsrLocation", "subscriber");
		map.put("getSubscriber||getSubscriberAllService||getSubscriberAllUsrSessionPolicy||getUsrLocationgetSubscriberAllInf","subscriber");
		map.put("addBatSubscriber||updateBatSubscriber||delBatSubscriber||addBatService||updateBatService||delBatService" ,"BATInfo");
		
		StringBuffer xml = new StringBuffer(XML_HEADER);
		xml.append("<ROOT>");
		if (bean != null) {
			Field[] properties = bean.getClass().getDeclaredFields();
			String instructions="";
			xml.append("<pcrf:").append(instructions).append(">");
			if(true){
				xml.append("<inPara>").append("<subscriber>");	
			}else{xml.append("<inPara>").append("<BATInfo>");}
			
			
			for (int i = 0; i < properties.length; i++) {
				Method meth = bean.getClass().getMethod("get" + properties[i].getName());				
				xml.append("<attribute>");
				xml.append("<key>").append(properties[i].getName()).append("</key>");
				xml.append("<value>").append(getValue(meth.invoke(bean))).append("</value>");
				xml.append("</attribute>");						
			}
			if(true){
				xml.append("</inPara>").append("</subscriber>");
			}else{xml.append("</inPara>").append("</BATInfo>");}
			xml.append("</pcrf:").append(instructions).append(">");
		}
		xml.append("</ROOT>");
		if (formatOut) {
			Document doc = DocumentHelper.parseText(xml.toString());
			StringWriter out = null;
			OutputFormat formate = OutputFormat.createPrettyPrint();
			out = new StringWriter();
			XMLWriter writer = new XMLWriter(out, formate);
			writer.write(doc);
			xml.delete(0, xml.length()).append(out.toString());
		}
		return xml.toString();
	}
	
	
	/**
	 * xml转换为Map
	 * 简单的Bean
	 * 属性格式为:XXXX_YYYYY
	 * @param xml
	 * @param t
	 * @return
	 * @throws Exception
	 */
	public static Map<String,String> xmlToBeanForPCC(String xml) throws Exception {
		Document doc = DocumentHelper.parseText(xml);
		Element root = doc.getRootElement();
		Map<String,String>map=new HashMap<String,String>();
		String resultcode=root.element("resultCode").getText();
		//添加错误码
		map.put("resultcode", resultcode);
		String ErrorDescription=root.element("paras").element("key").getText();
		//添加错误描述
		map.put("ErrorDescription", ErrorDescription);		
		String OperationSucceeded=root.element("paras").element("value").getText();
		map.put("OperationSucceeded", OperationSucceeded);
		List<Element> attributes=root.element("ResultData").elements("attribute");
		 for (Element attribute : attributes) {
			 //添加具体的key和 value
	           String key=attribute.element("key").getText();
	           String value=attribute.element("value").getText();
	           map.put(key, value);
	        } 
		return map;
	}
	
	
}
