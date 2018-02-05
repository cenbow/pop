package com.ai.bdx.pop.phonefilter.impl.local;

import it.unimi.dsi.fastutil.bytes.Byte2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.shorts.Short2ObjectOpenHashMap;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.BitSet;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import com.ai.bdx.pop.bean.AvoidCustBean;
import com.ai.bdx.pop.exception.PopException;
import com.ai.bdx.pop.phonefilter.IPopAvoidBotherFilter;
import com.ai.bdx.pop.util.ConvertUtils;
import com.ai.bdx.pop.util.PopConstant;
import com.ai.bdx.pop.util.PopUtil;
import com.ai.bdx.pop.util.SimpleCache;
import com.ai.bdx.pop.util.SpringContext;
import com.asiainfo.biframe.utils.spring.SystemServiceLocator;
import com.asiainfo.biframe.utils.string.StringUtil;
import com.google.common.base.Strings;
 

/**
 * @author zhangyb5
 * 免打扰控制
 */
public class PopAvoidBotherFilterImpl implements IPopAvoidBotherFilter {

	private static Logger log = LogManager.getLogger();
	private static final String QUERY_AVOID_CUST_DATA_SQL = "SELECT avoid_type_id,SUBSTR(PRODUCT_NO,2,2) v1,SUBSTR(PRODUCT_NO,4,4) v2,SUBSTR(PRODUCT_NO,8,4) v3  FROM POP_AVOID_CUSTINFO where avoid_type_id in (?)";

	private static final String QUERY_AVOID_CUST_ALL_DATA_SQL = "SELECT avoid_type_id,SUBSTR(PRODUCT_NO,2,2) v1,SUBSTR(PRODUCT_NO,4,4) v2,SUBSTR(PRODUCT_NO,8,4) v3  FROM POP_AVOID_CUSTINFO";
	
	public Map<String, Byte2ObjectOpenHashMap<Short2ObjectOpenHashMap<BitSet>>> loadBotherData(final String avoidTypes) {
		log.debug("加载免打扰类型id:"+avoidTypes+"-数据开始...");
		if(Strings.isNullOrEmpty(avoidTypes)){
			throw new PopException("免打扰类型不能为空!");
		}
	 
		String sql = QUERY_AVOID_CUST_DATA_SQL.replace("?", avoidTypes);
		//结构：渠道免打扰类型ID+客户类型ID ， 号码段一，号码段二，号码段三
		final Map<String, Byte2ObjectOpenHashMap<Short2ObjectOpenHashMap<BitSet>>> h1 = new HashMap<String, Byte2ObjectOpenHashMap<Short2ObjectOpenHashMap<BitSet>>>();
		JdbcTemplate jt = SpringContext.getBean("jdbcTemplate", JdbcTemplate.class);
	 
		jt.setFetchSize(500);
		
		jt.query(sql, new RowMapper() {
			public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
				try {
					String avoidType = rs.getString("avoid_type_id");
					byte v1 = rs.getByte("V1");
					short v2 = rs.getShort("V2");
					short v3 = rs.getShort("V3");

					Byte2ObjectOpenHashMap<Short2ObjectOpenHashMap<BitSet>> h2 = h1.get(avoidType);
					if (h2 == null) {
						h2 = new Byte2ObjectOpenHashMap<Short2ObjectOpenHashMap<BitSet>>();
						h1.put(avoidType, h2);
					}

					Short2ObjectOpenHashMap<BitSet> h3 = h2.get(v1);
					if (h3 == null) {
						h3 = new Short2ObjectOpenHashMap<BitSet>();
						h2.put(v1, h3);
					}

					BitSet h4 = h3.get(v2);
					if (h4 == null) {
						h4 = new BitSet(10000);
						h3.put(v2, h4);
					}
					h4.set(v3);
				} catch (Exception e) {
					log.warn("非法号码：1" + (rs.getString("V1") + rs.getString("V2") + rs.getString("V3")), e);
				}
				return null;
			}
		});
		 
		log.debug("客户免打扰数据初始化结束");
		return h1;
	}


	public Map<String, Byte2ObjectOpenHashMap<Short2ObjectOpenHashMap<BitSet>>> loadBotherData() {
	 
		String sql = QUERY_AVOID_CUST_ALL_DATA_SQL;
		//结构：渠道免打扰类型ID+客户类型ID ， 号码段一，号码段二，号码段三
		final Map<String, Byte2ObjectOpenHashMap<Short2ObjectOpenHashMap<BitSet>>> h1 = new HashMap<String, Byte2ObjectOpenHashMap<Short2ObjectOpenHashMap<BitSet>>>();
		JdbcTemplate jt = SpringContext.getBean("jdbcTemplate", JdbcTemplate.class);
	 
		jt.setFetchSize(500);
		
		jt.query(sql, new RowMapper() {
			public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
				try {
					String avoidType = rs.getString("avoid_type_id");
					byte v1 = rs.getByte("V1");
					short v2 = rs.getShort("V2");
					short v3 = rs.getShort("V3");

					Byte2ObjectOpenHashMap<Short2ObjectOpenHashMap<BitSet>> h2 = h1.get(avoidType);
					if (h2 == null) {
						h2 = new Byte2ObjectOpenHashMap<Short2ObjectOpenHashMap<BitSet>>();
						h1.put(avoidType, h2);
					}

					Short2ObjectOpenHashMap<BitSet> h3 = h2.get(v1);
					if (h3 == null) {
						h3 = new Short2ObjectOpenHashMap<BitSet>();
						h2.put(v1, h3);
					}

					BitSet h4 = h3.get(v2);
					if (h4 == null) {
						h4 = new BitSet(10000);
						h3.put(v2, h4);
					}
					h4.set(v3);
				} catch (Exception e) {
					log.warn("非法号码：1" + (rs.getString("V1") + rs.getString("V2") + rs.getString("V3")), e);
				}
				return null;
			}
		});
		 
		log.debug("客户免打扰数据初始化结束");
		return h1;
	}

	/***
	 * 初始免打扰到SimpleCache
	 * */
	public void initBotherData() {
		String key = PopConstant.CACHE_KEY_AVOID_BOTHER_TYPE_DATA;
		Map<String, Byte2ObjectOpenHashMap<Short2ObjectOpenHashMap<BitSet>>> avoidAllCustMap = (Map<String, Byte2ObjectOpenHashMap<Short2ObjectOpenHashMap<BitSet>>>) loadBotherData();
		SimpleCache.getInstance().put(key,avoidAllCustMap, PopConstant.CACHE_TIME);
	}
	
	
	
	/**
	 * 获取免打扰之后的客户群 和 剔除的客户群
	 * @param avoidTypeId 免打扰类型id
	 * @param data 原始客户群
	 * @return 	avoidAfterCust --免打扰后客户群
				delCust--删除客户群
	 * **/
	public synchronized AvoidCustBean allowPassFilterBatch(String avoidBotherTypeIds,Byte2ObjectOpenHashMap<Short2ObjectOpenHashMap<BitSet>> data) {
		AvoidCustBean avoidCustBean = new AvoidCustBean();
		String key = PopConstant.CACHE_KEY_AVOID_BOTHER_TYPE_DATA;
		Map<String, Byte2ObjectOpenHashMap<Short2ObjectOpenHashMap<BitSet>>> avoidAllCustMap = (Map<String, Byte2ObjectOpenHashMap<Short2ObjectOpenHashMap<BitSet>>>) SimpleCache.getInstance().get(key);
		if(avoidAllCustMap == null){
			avoidAllCustMap = (Map<String, Byte2ObjectOpenHashMap<Short2ObjectOpenHashMap<BitSet>>>) loadBotherData();
			SimpleCache.getInstance().put(key,avoidAllCustMap, PopConstant.CACHE_TIME);
		}
		Byte2ObjectOpenHashMap<Short2ObjectOpenHashMap<BitSet>> avoidDeleteCustMap  =  new Byte2ObjectOpenHashMap<Short2ObjectOpenHashMap<BitSet>>();
		if(!Strings.isNullOrEmpty(avoidBotherTypeIds)){
		for (String avoidBotherTypeId : avoidBotherTypeIds.split(",")) {
		 Byte2ObjectOpenHashMap<Short2ObjectOpenHashMap<BitSet>> phones = avoidAllCustMap.get(avoidBotherTypeId);
			if (phones != null) {
				for (Map.Entry<Byte, Short2ObjectOpenHashMap<BitSet>> e1 : data.entrySet()) {
						if (phones.containsKey(e1.getKey())) {
							Short2ObjectOpenHashMap<BitSet> te2 = phones.get(e1.getKey());
							for (Map.Entry<Short, BitSet> e2 : e1.getValue().entrySet()) {
								if (te2.containsKey(e2.getKey())) {
									BitSet te3 = te2.get(e2.getKey());
									BitSet e3 = e2.getValue();
									BitSet tmpE3 = new BitSet();
									tmpE3.or(e3);
									Short2ObjectOpenHashMap<BitSet> h3 = avoidDeleteCustMap.get(e1.getKey());
									if (h3 == null) {
										h3 = new Short2ObjectOpenHashMap<BitSet>();
										avoidDeleteCustMap.put(e1.getKey(), h3);
									}
		
									BitSet h4 = h3.get(e2.getKey());
									tmpE3.and(te3);
									if (h4 == null) {
										h4 = new BitSet(10000);
										h3.put(e2.getKey(), h4);
									}
									h4.or(tmpE3);
									e3.andNot(te3);
								}
							}
						}
			 	}
		  }
		}
		}
		avoidCustBean.setAvoidAfterCustMap(data);
		avoidCustBean.setAvoidDeleteCustMap(avoidDeleteCustMap);
		 
		return avoidCustBean;
	}

	@Override
	public boolean allowPassFilter(String activityCode, String productNo) {
		boolean flag = false;//默认不允许通过
		if (StringUtil.isEmpty(productNo)) {
			log.error("手机号为空！");
			return flag;
		}

		String avoidBotherTypeIds = PopUtil.getAvoidBotherTypeIdsByActId(activityCode);
		if (StringUtil.isEmpty(avoidBotherTypeIds)) {
			log.debug("未设置免打扰客户类型，不需要免打扰!");
			return !flag;
		}
		
		String[] avoidBotherTypeIdArr = avoidBotherTypeIds.split(",");
		
		for(int i=0;i<avoidBotherTypeIdArr.length;i++){
			String avoidBotherTypeId = avoidBotherTypeIdArr[i];
			String key = PopConstant.CACHE_KEY_AVOID_BOTHER_TYPE_DATA;
			Map<String, Byte2ObjectOpenHashMap<Short2ObjectOpenHashMap<BitSet>>> custMap = (Map<String, Byte2ObjectOpenHashMap<Short2ObjectOpenHashMap<BitSet>>>) SimpleCache.getInstance().get(key);
			if(custMap == null){
				custMap = (Map<String, Byte2ObjectOpenHashMap<Short2ObjectOpenHashMap<BitSet>>>) loadBotherData();
				SimpleCache.getInstance().put(key,custMap, PopConstant.CACHE_TIME);
			}
			//判断免打扰客户是否需要剔除
			Byte2ObjectOpenHashMap<Short2ObjectOpenHashMap<BitSet>> data = custMap.get(avoidBotherTypeId);
			byte v1 = ConvertUtils.parseByte(productNo.substring(1, 3));
			short v2 = ConvertUtils.parseShort(productNo.substring(3, 7));
			short v3 = ConvertUtils.parseShort(productNo.substring(7));
			if (data != null &&data.containsKey(v1)) {
				Short2ObjectOpenHashMap<BitSet> h1 = data.get(v1);
				if (h1.containsKey(v2)) {
					BitSet h2 = h1.get(v2);
					if (h2.get(v3)) {
						flag = true;
					}
				}
			}
		
		}
		return !flag;
	}

}
