package com.ai.bdx.pop.util;

import it.unimi.dsi.fastutil.bytes.Byte2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.bytes.Byte2ShortOpenHashMap;
import it.unimi.dsi.fastutil.shorts.Short2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.shorts.Short2ShortOpenHashMap;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.BitSet;
import java.util.Calendar;
import java.util.Date;

import net.sf.json.JSONObject;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.helpers.Strings;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import com.ai.bdx.pop.exception.PopException;
import com.ai.bdx.pop.kafka.CepMessageCacheQueue;
import com.ai.bdx.pop.model.PopPolicyBaseinfo;
import com.ai.bdx.pop.model.PopPolicyRule;
import com.asiainfo.biframe.utils.date.DateUtil;
import com.asiainfo.biframe.utils.string.StringUtil;
 
/**
 * 接触控制工具类
 * @author zhangyb5
 *
 */
public class ContactControlUtil {
	private static Logger log = LogManager.getLogger();
      
	private static final String QUERY_CURRENT_TASK_ID_SQL = "SELECT CURRENT_TASK_ID FROM  POP_POLICY_RULE WHERE id = ?";
	
	private static final String  QUERY_CUST_LIST_TAB_NAME_SQL ="select pre_send_data_tab from POP_POLICY_RULE where id = ?";
	
	/**
	 * 计算两个日期相差的天数（不考虑时间）
	 * @param fromDate
	 * @param toDate
	 * @return
	 */
	public static short dateDiffNoTime(java.util.Date fromDate, java.util.Date toDate) {
		Date date1 = DateUtils.truncate(fromDate, Calendar.DATE);
		Date date2 = DateUtils.truncate(toDate, Calendar.DATE);
		return Short.valueOf(String.valueOf((date1.getTime() - date2.getTime()) / 86400000));
	}

	/**
	 * 在特定结构的MAP中查询用户号码
	 * @param custMap
	 * @param userAccount
	 * @return
	 */
	public static boolean matchUserAccount(Byte2ObjectOpenHashMap<Short2ObjectOpenHashMap<BitSet>> custMap,
			String userAccount) {
		boolean flag = false;
		if (custMap != null && NumberUtils.isDigits(userAccount) && userAccount.length() == 11) {
			byte v1 = ConvertUtils.parseByte(userAccount.substring(1, 3));
			short v2 = ConvertUtils.parseShort(userAccount.substring(3, 7));
			short v3 = ConvertUtils.parseShort(userAccount.substring(7));

			if (custMap.containsKey(v1)) {
				Short2ObjectOpenHashMap<BitSet> h2 = custMap.get(v1);
				if (h2.containsKey(v2)) {
					BitSet h3 = h2.get(v2);
					return h3.get(v3);
				}
			}
		}
		return flag;
	}

	/**
	 * 在特定结构的MAP中移除用户号码
	 * @param custMap
	 * @param userAccount
	 * @return 是否移除成功，true表示从缓存中移除该手机号；
	 */
	public static boolean removeUserAccountShortArray(Byte2ObjectOpenHashMap<Short2ObjectOpenHashMap<BitSet>> custMap,
			String userAccount) {
		boolean flag = false;
		if (custMap == null || !NumberUtils.isDigits(userAccount) || userAccount.length() != 11) {
			return flag;
		}
		byte v1 = ConvertUtils.parseByte(userAccount.substring(1, 3));
		short v2 = ConvertUtils.parseShort(userAccount.substring(3, 7));
		short v3 = ConvertUtils.parseShort(userAccount.substring(7));

		if (custMap.containsKey(v1)) {
			Short2ObjectOpenHashMap<BitSet> h2 = custMap.get(v1);

			if (h2.containsKey(v2)) {
				BitSet h3 = h2.get(v2);

				if (h3.get(v3)) {
					h3.clear(v3);
					flag = true;
				}
				if (h3.isEmpty()) {
					h2.remove(v2);
				} else {
					h2.put(v2, h3);
				}
			}
			if (h2.isEmpty()) {
				custMap.remove(v1);
			}
		}
		return flag;
	}

  
	/**
	 * 快速向给定的short数组中添加一个short元素
	 * @param datas
	 * @param data
	 * @return
	 */
	public static short[] wrapShortArray(short[] datas, short data) {
		short[] datasTmp = new short[datas.length + 1];
		System.arraycopy(datas, 0, datasTmp, 0, datas.length);
		System.arraycopy(new short[] { data }, 0, datasTmp, datas.length, 1);
		return datasTmp;
	}

	/**
	 * 在特定结构的MAP中获取反馈状态
	 * @param feedMap
	 * @param userAccount
	 * @return
	 */
	public static Short getFeedStatus(Byte2ObjectOpenHashMap<Short2ObjectOpenHashMap<Short2ShortOpenHashMap>> feedMap,
			String userAccount) {
		Short feedStatus = null;
		if (MapUtils.isNotEmpty(feedMap) && NumberUtils.isDigits(userAccount) && userAccount.length() == 11) {
			byte v1 = ConvertUtils.parseByte(userAccount.substring(1, 3));
			short v2 = ConvertUtils.parseShort(userAccount.substring(3, 7));
			short v3 = ConvertUtils.parseShort(userAccount.substring(7));

			if (feedMap.containsKey(v1)) {
				Short2ObjectOpenHashMap<Short2ShortOpenHashMap> h2 = feedMap.get(v1);
				if (h2.containsKey(v2)) {
					Short2ShortOpenHashMap h3 = h2.get(v2);
					feedStatus = h3.get(v3);
				}
			}
		}
		return feedStatus;
	}
 
    
	/**
	 * 添加号码，如果已存在则返回false
	 * @param custMap
	 * @param userAccount
	 * @return
	 */
	public static boolean insertUserAccount(Byte2ObjectOpenHashMap<Short2ObjectOpenHashMap<BitSet>> custMap,
			String userAccount) {
		boolean add = false;
		if (custMap != null && NumberUtils.isDigits(userAccount) && userAccount.length() == 11) {
			byte v1 = ConvertUtils.parseByte(userAccount.substring(1, 3));
			short v2 = ConvertUtils.parseShort(userAccount.substring(3, 7));
			short v3 = ConvertUtils.parseShort(userAccount.substring(7));

			Short2ObjectOpenHashMap<BitSet> h2 = custMap.get(v1);
			if (h2 == null) {
				h2 = new Short2ObjectOpenHashMap<BitSet>(1);
				custMap.put(v1, h2);
				add = true;
			}
			BitSet h3 = h2.get(v2);
			if (h3 == null) {
				h3 = new BitSet(10000);
				add = true;
			}
			if (!h3.get(v3)) {
				h3.set(v3);
				add = true;
			}
			h2.put(v2, h3);
		}
		return add;
	}
	
	
	/**
	 * 将从数据库中取出的号码集合压缩转换为特定Map
	 * SQL格式：SELECT SUBSTR(product_no,2,2) v1,SUBSTR(product_no,4,4) v2,SUBSTR(product_no,8,4) v3 FROM {0} GROUP BY SUBSTR(product_no,2,2), SUBSTR(product_no,4,4),SUBSTR(product_no,8,4)
	 * @param sql
	 */
	public static Byte2ObjectOpenHashMap<Short2ObjectOpenHashMap<BitSet>> convertCustList2Map(String sql) {
		return convertCustList2Map(sql, null);
	}

	public static Byte2ObjectOpenHashMap<Short2ObjectOpenHashMap<BitSet>> convertCustList2Map(String sql,
			JdbcTemplate jt) {
		final Byte2ObjectOpenHashMap<Short2ObjectOpenHashMap<BitSet>> h1 = new Byte2ObjectOpenHashMap<Short2ObjectOpenHashMap<BitSet>>(
				1);
		if (jt == null) {
			jt = SpringContext.getBean("jdbcTemplate", JdbcTemplate.class);
		}
		jt.setFetchSize(500);
		jt.query(sql, new RowMapper() {
			public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
				try {
					byte v1 = Byte.valueOf(rs.getString(1));
					short v2 = Short.valueOf(rs.getString(2));
					short v3 = Short.valueOf(rs.getString(3));
					Short2ObjectOpenHashMap<BitSet> h2 = h1.get(v1);
					if (h2 == null) {
						h2 = new Short2ObjectOpenHashMap<BitSet>(1);
						h1.put(v1, h2);
					}
					BitSet h3 = h2.get(v2);
					if (h3 == null) {
						h3 = new BitSet(10000);
					}
					h3.set(v3);
					/*	ShortArrayList h = ShortArrayList.wrap(h3);
						h.add(v3);*/
					h2.put(v2, h3);
				} catch (Exception e) {
					log.warn("非法号码：1" + (rs.getString(1) + rs.getString(2) + rs.getString(3)), e);
				}
				return null;
			}
		});
		return h1;
	}

	
	/**
	 * 检测活动是否有效【判断活动状态是否有效，是否合法活动】
	 * @param activityCode
	 * @return
	 */
	public static boolean checkActiveStatus(String activityCode) {
		Integer status = (Integer) SimpleCache.getInstance().get(PopConstant.CACHE_KEY_ACTIVITY_STATUS + "_" + activityCode);
		if (status == null) {
			status = updateActiveStatus(activityCode, -1);
		}
		if (status != 1) {//无效活动
			return false;
		}

		Date[] beginEnd = CepUtil.getRuleBenEndDate(activityCode);
		if (Calendar.getInstance().getTimeInMillis() < beginEnd[0].getTime()
				|| Calendar.getInstance().getTimeInMillis() > beginEnd[1].getTime()) {
			return false;
		}
		return CepUtil.isRuleTaskExecute(activityCode);
	}


	/**
	 * 更新缓存中的活动的状态：0-无效（暂停，完成，终止等），1-有效（派单成功）
	 * @param activityCode
	 * @param status：0：无效；1：有效；-1：需要重新加载
	 * @return
	 */
	public static int updateActiveStatus(String activityCode, int status) {
		int flag = 0;
		try {
			if (status == -1) {//重新从数据库中加载
				flag = PopUtil.loadFlagByActiveCode(activityCode, "rule_status");
			} else {//直接更新缓存（用于第一次状态变更接收接口）
				flag = status;
				SimpleCache.getInstance().put(PopConstant.CACHE_KEY_ACTIVITY_STATUS + "_" + activityCode, status,
						PopUtil.getRemainTime());
			}
		} catch (Exception e) {
			log.error("loadActiveStatusByActiveCode[" + activityCode + "] error:", e);
		}
		return flag;
	}

	
	public static void saveEventData(JSONObject data) throws Exception {
		//插入O表队列，并调用实时派发接口
		CepMessageCacheQueue.getCepMessageQueue().offer(data);
	}
	
	/**
	 * 加载活动的用户清单
	 * @param ruleId
	 * @return
	 */
	public static Byte2ObjectOpenHashMap<Short2ObjectOpenHashMap<BitSet>> loadCustListByActiveCode(String ruleId) {
		//将清单数据读取到Map中
		Byte2ObjectOpenHashMap<Short2ObjectOpenHashMap<BitSet>> custMap = null;
		String taskId = getCurrentTaskId(ruleId);
		String duserTableName = getUserTabByRuleId(ruleId);
		if (StringUtil.isNotEmpty(duserTableName)) {
			custMap =  ContactControlUtil.convertCustList2Map(PopConstant.QUERY_USER_DATA_TO_MAP_TMP_SQL.replace("{0}", duserTableName));
			String ruleCustKey = PopConstant.POP_RULE_CUST_CACHE_PREFIX.replace("{RULEID}",ruleId);
			//将数据放到缓存
			SimpleCache.getInstance().put(ruleCustKey, custMap,PopConstant.CACHE_TIME);
		}

		return custMap;
	}

	
	/**
	 * 获取活动的当前任务编号，如果缓存中没有则重新加载
	 * @param ruleId
	 * @return
	 */
	public static String getCurrentTaskId(String ruleId) {
		String taskId = "";
		try {
			taskId = (String) SimpleCache.getInstance().get(PopConstant.CACHE_KEY_ACTIVITY_TASKID + "_" + ruleId);
			if (StringUtil.isEmpty(taskId)) {
				JdbcTemplate jt = SpringContext.getBean("jdbcTemplate", JdbcTemplate.class);
				taskId = (String) jt.queryForObject(QUERY_CURRENT_TASK_ID_SQL, new String[] { ruleId },
						String.class);
				SimpleCache.getInstance().put(PopConstant.CACHE_KEY_ACTIVITY_TASKID + "_" + ruleId, taskId,
						PopUtil.getRemainTime());
			}
		} catch (Exception e) {
			log.error("", e);
		}
		return taskId;
	}
	
	
	/**
	 * 根据规则编号获取规则清单表名称
	 * @param taskId 
	 * @return
	 */
	public static String getRuleSendTableNameForRuleId(String ruleId) {
		String tableName = null;
		try {
			String ruleTableKey = PopConstant.CACHE_KEY_POP_RULE_SEND_TABLENAME+ruleId;
			tableName = (String) SimpleCache.getInstance().get(ruleTableKey);
			if(StringUtil.isEmpty(tableName)){
				PopPolicyRule popPolicyRule= PopPolicyRule.dao().findById(ruleId);
				tableName = popPolicyRule.get(popPolicyRule.COL_SEND_DATA_TAB);
				if(StringUtil.isEmpty(tableName)){
					throw new PopException("规则汇总发送表为空!! 请检查ruleId:"+ruleId);
				}else{
					SimpleCache.getInstance().put(ruleTableKey, tableName,PopUtil.getRemainTime());
				}
			}
		}catch (Exception e) {
			log.error("根据规则编号获取规则清单表名称失败!", e);
			throw new PopException("规则汇总发送表为空!! 请检查ruleId:"+ruleId);
		}
		return tableName;
	}
	
	
	/**
	 * 根据任务编号获取任务清单表名称
	 * @param taskId 
	 * @return
	 */
	public static String getTaskSendTableNameForTaskId(String TaskId,String ruleId) {
		String tableName = null;
		try {
			String ruleTaskKey = PopConstant.CACHE_KEY_POP_TASK_SEND_TABLENAME+TaskId;
			tableName = (String) SimpleCache.getInstance().get(ruleTaskKey);
			if(StringUtil.isEmpty(tableName)){
				PopPolicyRule popPolicyRule= PopPolicyRule.dao().findById(ruleId);
				String policyId = popPolicyRule.get(popPolicyRule.COL_POLICY_ID);
				PopPolicyBaseinfo  popPolicyBaseinfo= PopPolicyBaseinfo.dao().findById(policyId);
				String policyTab = popPolicyBaseinfo.get(PopPolicyBaseinfo.COL_POLICY_TASK_TAB);
				JdbcTemplate jt = SpringContext.getBean("jdbcTemplate", JdbcTemplate.class);
				String sql = "select send_data_tab from " +policyTab + " where id = ?";
				tableName = (String) jt.queryForObject(sql,new String[] { TaskId }, String.class);
				if(StringUtil.isEmpty(tableName)){
					throw new PopException("任务发送表为空!! 请检查TaskId:"+TaskId);
				}else{
					SimpleCache.getInstance().put(ruleTaskKey, tableName,PopUtil.getRemainTime());
				}
			}
		}catch (Exception e) {
			log.error("根据任务编号获取任务清单表名称失败!", e);
			throw new PopException("任务发送表为空!! 请检查TaskId:"+TaskId);
		}
		return tableName;
	}
	
	
	
	
	
	
	/**
	 * 根据任务编号获取清单表名称
	 * @param ruleId
	 * @param taskId 
	 * @return
	 */
	public static String getUserTabByRuleId(String ruleId) {
		String tabName = null;
		try {
			JdbcTemplate jt = SpringContext.getBean("jdbcTemplate", JdbcTemplate.class);
			tabName = (String) jt.queryForObject(QUERY_CUST_LIST_TAB_NAME_SQL,new String[] { ruleId }, String.class);
		} catch (Exception e) {
			log.error("getUserTabByTaskId(" + ruleId + ")", e);
		}
		return tabName;
	}

	
	
	public static void main(String[] args) {
	}
}
