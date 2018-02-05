package com.ai.bdx.pop.kafka;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

 








import org.springframework.util.CollectionUtils;

import com.ai.bdx.pop.util.CepCONST;
import com.ai.bdx.pop.util.CepUtil;
import com.ai.bdx.pop.util.PopConstant;
import com.ai.bdx.pop.util.PopUtil;
import com.ai.bdx.pop.util.SimpleCache;
import com.ai.bdx.pop.util.SpringContext;
import com.asiainfo.biframe.utils.config.Configure;
import com.google.common.base.Strings;
 

public class CepReceiveThreadCache {

	private static final Logger log = LogManager.getLogger();

	private Map<String, CepMessageReceiverThread> threadMap = null;

	/** 保证单例 */
	static class CepReceiveThreadCacheHolder {
		static CepReceiveThreadCache holder = new CepReceiveThreadCache();
	}

	private CepReceiveThreadCache() {
			threadMap = new ConcurrentHashMap<String, CepMessageReceiverThread>();
	}

	public static CepReceiveThreadCache getInstance() {
		return CepReceiveThreadCacheHolder.holder;
	}

	public void put(String key, CepMessageReceiverThread cepMessageReceiverThread) {
			threadMap.put(key, cepMessageReceiverThread);
	}

	public CepMessageReceiverThread get(String key) {
		CepMessageReceiverThread t =  threadMap.get(key);
		return t;
	}

	public void remove(String key) throws Exception {
			CepMessageReceiverThread t =  threadMap.get(key);
			t.stopThread();
			threadMap.remove(key);
	}
	
	 

	public boolean exist(String key) {
		return threadMap.containsKey(key);
	}

	public Map getThreadMap() {
		return threadMap;
	}

	public void stopYesterdayTask() throws Exception {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		long today = sdf.parse(sdf.format(Calendar.getInstance().getTime())).getTime();

		for (Entry<String, CepMessageReceiverThread> entry : threadMap.entrySet()) {
			CepMessageReceiverThread thread = entry.getValue();
			if (thread.getExecuteDaste().getTime() < today) {
				CepUtil.stopCepEvent(thread.getEventId());
				thread.stopThread();
				 threadMap.remove(entry.getKey());
				 
			}

		}
	}

	public void init() throws ParseException {
		final JdbcTemplate jt = SpringContext.getBean("jdbcTemplate", JdbcTemplate.class);

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	 
		final String date = sdf.format(Calendar.getInstance().getTime());
 
		String taskSql = CepCONST.QUERY_POP_RULE_TASK_SQL;
		if ("oracle".equalsIgnoreCase(PopUtil.getDBType())) {
			taskSql = CepCONST.QUERY_POP_RULE_TASK_SQL_ORACLE.replace("{today}", date);
		}    
		final String sql = taskSql;
		log.info("执行SQL：{}", taskSql);

		jt.query(CepCONST.QUERY_CEP_EVENT_INIT_SQL, new RowMapper() {

			@Override
			public Object mapRow(ResultSet rs, int arg1) throws SQLException {
				String ruleId = rs.getString("id");
				String rule_currentTaskId = rs.getString("current_task_id");
				String policyTaskTab = rs.getString("policy_task_tab");
				String cepRuleId = rs.getString("cep_rule_id");
				List<Map> policyTaskList;
				//通过表名,查询当前规则的任务
				if(!Strings.isNullOrEmpty(policyTaskTab)){
					String sqltmp = sql.replace("{table}", policyTaskTab);
//					if ("oracle".equalsIgnoreCase(PopUtil.getDBType())) {
//					    policyTaskList  = jt.queryForList(sql, new Object[] { ruleId,PopConstant.TASK_STATUS_PDCG });
//					}else{
						policyTaskList  = jt.queryForList(sqltmp, new Object[] { date,ruleId,PopConstant.TASK_STATUS_PDCG });
			//		}
					if(!CollectionUtils.isEmpty(policyTaskList)){
						Map m = policyTaskList.get(0);
						String taskId = (String)m.get("id");
						//判断当前规则是否正在执行
						if (!CepReceiveThreadCache.getInstance().exist(ruleId)) {
							try {
							CepUtil.registCepEvent(cepRuleId);
							//判断当前缓存taskId 是否 是查询出来的 taskId
							CepMessageReceiverThread thread = new CepMessageReceiverThread(cepRuleId, ruleId);
							thread.start();
							log.debug("++++++规则id:"+ruleId +",taskId:"+taskId+"在表:"+policyTaskTab+"中查询到正在执行的任务..启动监听..当前日期:"+date+"++++++++");
							} catch (Exception e) {
								e.printStackTrace();
						 }
						}
						//判断rule_currentTaskId 是否 等于 taskId
						if(!rule_currentTaskId.equals(taskId)){
							//则更新POP_POLICY_RULE
							String sql = "update POP_POLICY_RULE set current_task_id = '"+taskId +"' where id = '"+ruleId+"'";
							jt.execute(sql);
						}
						SimpleCache.getInstance().put(PopConstant.CACHE_KEY_ACTIVITY_TASKID + "_" + ruleId, taskId,PopUtil.getRemainTime());
					}else{
						log.debug("++++++规则id:"+ruleId +",在表:"+policyTaskTab+"中没有查询到正在执行的任务..无需启动监听..当前日期:"+date+"++++++++");
					}
					
				}
				return null;
			}

		});
	}

}
