package com.ai.bdx.pop.phonefilter;

import it.unimi.dsi.fastutil.bytes.Byte2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.shorts.Short2ObjectOpenHashMap;
import java.util.BitSet;
import net.sf.json.JSONObject;
 

/**
 * 接触总体控制
 * @author zhangyb5
 * 控制用户是否可接触：时段控制，免打扰控制，动静用户群匹配，接触次数控制
 */
public interface IUnifiedContactControlFilter {
	 
	
	/**
	 * 实时营销接触控制(复杂事件CEP)
	 * @param activityCode 活动编码
	 * @param message  格式json字符串
	 * @throws Exception 
	 */
	public void realTimeContactControl(String activityCode, JSONObject message) throws Exception;
}
