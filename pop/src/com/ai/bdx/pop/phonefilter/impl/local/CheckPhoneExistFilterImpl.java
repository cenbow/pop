package com.ai.bdx.pop.phonefilter.impl.local;

import it.unimi.dsi.fastutil.bytes.Byte2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.shorts.Short2ObjectOpenHashMap;

import java.util.BitSet;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ai.bdx.pop.phonefilter.ICheckPhoneExistFilter;
import com.ai.bdx.pop.util.ContactControlUtil;
import com.ai.bdx.pop.util.PopConstant;
 

/**
 * 号码检查是否存在表缓存过滤Impl
 * @author guoyj
 *
 */
public class CheckPhoneExistFilterImpl implements ICheckPhoneExistFilter {

	private static Logger log = LogManager.getLogger();
	//table对应号码记录缓存
	private final Object2ObjectOpenHashMap<String, Byte2ObjectOpenHashMap<Short2ObjectOpenHashMap<BitSet>>> tabPhoneDataMap = new Object2ObjectOpenHashMap<String, Byte2ObjectOpenHashMap<Short2ObjectOpenHashMap<BitSet>>>();
	public static final byte[] LOCK = new byte[0];

	public boolean checkExist(String tabNameKey, String targetPhone) {
		boolean exists = false;
		synchronized (LOCK) {
			Byte2ObjectOpenHashMap<Short2ObjectOpenHashMap<BitSet>> phoneData = tabPhoneDataMap.get(tabNameKey);
			if (phoneData == null) {
				log.info(tabNameKey + "表数据准备添加进缓存");
				phoneData = ContactControlUtil.convertCustList2Map(PopConstant.QUERY_USER_DATA_TO_MAP_TMP_SQL.replace(
						"{0}", tabNameKey));
				tabPhoneDataMap.put(tabNameKey, phoneData);
			}
			exists = !ContactControlUtil.insertUserAccount(phoneData, targetPhone);
		}
		return exists;
	}

	@Override
	public Object removeDataByKey(String tabNameKey) {
		synchronized (LOCK) {
			log.info("移除表" + tabNameKey + "对应号码检查缓存数据");
			return tabPhoneDataMap.remove(tabNameKey);
		}
	}

}
