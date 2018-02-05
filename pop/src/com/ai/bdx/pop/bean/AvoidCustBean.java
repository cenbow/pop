package com.ai.bdx.pop.bean;

import it.unimi.dsi.fastutil.bytes.Byte2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.shorts.Short2ObjectOpenHashMap;

import java.util.BitSet;

/**
 * @author liyz
 * 免打扰bean
 * **/
public class AvoidCustBean {

	//免打扰删除的客户群
	public Byte2ObjectOpenHashMap<Short2ObjectOpenHashMap<BitSet>> avoidDeleteCustMap;
	//免打扰之后的客户群
	public Byte2ObjectOpenHashMap<Short2ObjectOpenHashMap<BitSet>> avoidAfterCustMap;

	public Byte2ObjectOpenHashMap<Short2ObjectOpenHashMap<BitSet>> getAvoidDeleteCustMap() {
		return avoidDeleteCustMap;
	}

	public void setAvoidDeleteCustMap(
			Byte2ObjectOpenHashMap<Short2ObjectOpenHashMap<BitSet>> avoidDeleteCustMap) {
		this.avoidDeleteCustMap = avoidDeleteCustMap;
	}

	public Byte2ObjectOpenHashMap<Short2ObjectOpenHashMap<BitSet>> getAvoidAfterCustMap() {
		return avoidAfterCustMap;
	}

	public void setAvoidAfterCustMap(
			Byte2ObjectOpenHashMap<Short2ObjectOpenHashMap<BitSet>> avoidAfterCustMap) {
		this.avoidAfterCustMap = avoidAfterCustMap;
	}
	
	
}
