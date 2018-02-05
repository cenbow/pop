package com.ai.bdx.pop.util;

import java.io.IOException;
import java.io.StringWriter;

import com.asiainfo.biframe.utils.string.StringUtil;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class JsonUtil {
	private static final ObjectMapper om = new ObjectMapper();
	static {
		//设置将json字符串转换为对象时忽略对象中不存在的属性
		om.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		//设置对象转换为json串时忽略对象为空的bean
		om.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
		//设置将对象转换为json串时仅转换不为空的属性
		om.setSerializationInclusion(Include.NON_NULL);
	}

	/**
	 * 对象转换为json字符串
	 * @param obj
	 * @return
	 */
	public static String obj2Json(Object obj) {
		String jsonStr = "{}";
		JsonGenerator jsonGenerator = null;
		try {
			if (obj != null) {
				StringWriter sw = new StringWriter();
				jsonGenerator = om.getJsonFactory().createJsonGenerator(sw);
				jsonGenerator.writeObject(obj);
				jsonStr = sw.toString();
			}
		} catch (Exception e) {

		} finally {
			if (jsonGenerator != null) {
				try {
					jsonGenerator.close();
				} catch (IOException e) {
				}
			}
		}
		return jsonStr;
	}

	/**
	 * json字符串转换为指定对象
	 * @param json
	 * @param type
	 * @return
	 */
	public static <T> T json2Obj(String json, Class<T> type) {
		JsonParser jp = null;
		try {
			if (StringUtil.isNotEmpty(json)) {
				jp = om.getJsonFactory().createJsonParser(json);
				return jp.readValueAs(type);
			}
		} catch (Exception e) {

		} finally {
			if (jp != null) {
				try {
					jp.close();
				} catch (IOException e) {
				}
			}
		}
		return null;
	}
}
