package com.ai.bdx.pop.phonefilter;

import it.unimi.dsi.fastutil.bytes.Byte2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.shorts.Short2ObjectOpenHashMap;

import java.util.BitSet;
import java.util.Map;

import com.ai.bdx.pop.bean.AvoidCustBean;
 

/**
 * @author dengshu
 *
 */
public interface IPopAvoidBotherFilter {

	/**
	 *加载免打扰数据
	 */
	public Map<String, Byte2ObjectOpenHashMap<Short2ObjectOpenHashMap<BitSet>>> loadBotherData(final String avoidType);

	
	/**
	 * 是否允许通过，免打扰数据
	 * @param avoidBotherTypeId 免打扰类型
	 * @param productNo  活动号码
	 * @return true 允许通过, false被过滤
	 */
	public boolean allowPassFilter(String avoidBotherTypeId, String productNo);

	/**
	 * 批量过滤（仅静态号码集）
	 * @param avoidBotherTypeId
	 * @param data
	 * @return 过滤后剩余的号码。
	 */
	public AvoidCustBean allowPassFilterBatch(String avoidBotherTypeId,Byte2ObjectOpenHashMap<Short2ObjectOpenHashMap<BitSet>> data);

	
	/***
	 * 初始免打扰到SimpleCache
	 * */
	public void initBotherData();
	
}
