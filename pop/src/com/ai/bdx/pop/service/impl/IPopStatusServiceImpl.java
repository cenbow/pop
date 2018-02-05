package com.ai.bdx.pop.service.impl;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;

import com.ai.bdx.pop.model.PopPolicyBaseinfo;
import com.ai.bdx.pop.model.PopPolicyRule;
import com.ai.bdx.pop.service.IPopStatusService;
import com.asiainfo.biframe.utils.string.StringUtil;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;


public class IPopStatusServiceImpl implements IPopStatusService {
	private Logger log = LogManager.getLogger();
	private JdbcTemplate jdbcTemplate;
	
	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	private DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 

/**
 * 
 * updateSendOddStatus:更新派单号码状态和时间回填
 * @param ruleid
 * @param taskid
 * @param phoneStatusList String[]={手机号，状态，时间}
 * @see com.ai.bdx.pop.service.IPopStatusService#updateSendOddStatus(java.lang.String, java.lang.String, java.util.List)
 */
	public void updateSendOddStatus(String ruleid,String taskid,List<String[]> phoneStatusList) {

		if (StringUtil.isNotEmpty(ruleid)
				&& StringUtil.isNotEmpty(taskid)&& phoneStatusList != null
				&& phoneStatusList.size() > 0) {
			//根据规则找规则派单表
			PopPolicyRule rule=PopPolicyRule.dao().findById(ruleid);
			//String rule_currentTaskId=rule.getStr(PopPolicyRule.COL_CURRENT_TASK_ID);
			String rule_sendDataTab=rule.getStr(PopPolicyRule.COL_SEND_DATA_TAB);//得到规则派单表
			//根据规则和任务id找策略派单表
			String polocyId=rule.getStr(PopPolicyRule.COL_POLICY_ID);
			PopPolicyBaseinfo baseInfo=PopPolicyBaseinfo.dao().findById(polocyId);
			String taskTable=baseInfo.getStr(PopPolicyBaseinfo.COL_POLICY_TASK_TAB);
			StringBuffer b=new StringBuffer("select send_data_tab from ");
			b.append(taskTable).append(" where id=?");
			Record r=Db.findFirst(b.toString(), taskid);
			String sendDataTab=r.getStr("send_data_tab");//得到策略派单表
			//更新规则派单表
			 updateFeedBakStatus(rule_sendDataTab,phoneStatusList);
			//更新策略派单表
			 updateFeedBakStatus(sendDataTab,phoneStatusList);
		}

	}


	/**
	 * 
	 * updateFeedBakStatus:更新状态
	 * @param tabname
	 * @param list 
	 * @return void
	 */
	private  void updateFeedBakStatus(String tabname,final List<String[]> list){
		if(StringUtil.isNotEmpty(tabname)&&list!=null&&list.size()>0){
			StringBuffer b=new StringBuffer("update  ").append(tabname);
			b.append(" set feedback_status=?,feedback_time=? where product_no=?");
			log.debug("开始更新表{}的状态：list size={}",tabname,list.size());
			getJdbcTemplate().execute(b.toString(), new PreparedStatementCallback() {
					public Object doInPreparedStatement(PreparedStatement preparedstatement)throws SQLException, DataAccessException {
						int count=0;
						for (int i=0;i<list.size();i++) {
							String[] phoneInfo=list.get(i);
							count++;
							batchUpdate(preparedstatement,phoneInfo,count,5000);//每5000条提交一次
						}
						// 防止遗漏数据，再执行一遍
						preparedstatement.executeBatch();
						return null;
					}
				});
			
		}
	}
	/**
	 * 
	 * updateFeedBakStatus:单个更新状态
	 * @param tabname
	 * @param arrs 
	 * @return void
	 */
	private  void updateFeedBakStatus(String tabname,final String[] arrs){
		if(StringUtil.isNotEmpty(tabname)&&arrs!=null){
			StringBuffer b=new StringBuffer("update  ").append(tabname);
			b.append(" set feedback_status=?,feedback_time=? where product_no=?");
			log.debug("开始更新表{}的状态：list size={}",tabname,arrs.length);
			Object[] obj=new Object[]{Integer.parseInt(arrs[1]),Timestamp.valueOf(arrs[2]),arrs[0]};
			getJdbcTemplate().update(b.toString(),obj);
		}
	}
	/**
	 * 
	 * batchUpdate:批量更新操作
	 * @param preparedstatement
	 * @param phoneInfo String[]={手机号，状态，时间} 时间格式 yyyy-MM-dd HH:mm:ss
	 * @param total
	 * @param batchSize 
	 * @return void
	 */
	private void batchUpdate(PreparedStatement preparedstatement,String[] phoneInfo, int total, int batchSize) {
		//
		log.debug("更新表中手机号状态：手机号={},状态={},时间={} ",phoneInfo[0],phoneInfo[1],phoneInfo[2]);
		try {
			preparedstatement.setInt(1,Integer.parseInt(phoneInfo[1]));
			preparedstatement.setTimestamp(2,Timestamp.valueOf(phoneInfo[2]));
			preparedstatement.setString(3, phoneInfo[0]);
			preparedstatement.addBatch();
			if (total % batchSize == 0) {// 每多少条提交一下
				preparedstatement.executeBatch();
			}
		} catch (SQLException e1) {
			log.error("更新手机号状态参数预处理错误SQLException:" + e1.getStackTrace());
		} catch (Exception e2) {
			log.error("更新手机号状态参数预处理错误Exception:" + e2.getStackTrace());
		}

	}
	@Override
	public void updateSendOddStatus(String ruleid, String taskid,
			String[] phoneStatus) {

		if (StringUtil.isNotEmpty(ruleid) && StringUtil.isNotEmpty(taskid)
				&& phoneStatus != null) {
			// 根据规则找规则派单表
			PopPolicyRule rule = PopPolicyRule.dao().findById(ruleid);
			// String
			// rule_currentTaskId=rule.getStr(PopPolicyRule.COL_CURRENT_TASK_ID);
			String rule_sendDataTab = rule
					.getStr(PopPolicyRule.COL_SEND_DATA_TAB);// 得到规则派单表
			// 根据规则和任务id找策略派单表
			String polocyId = rule.getStr(PopPolicyRule.COL_POLICY_ID);
			PopPolicyBaseinfo baseInfo = PopPolicyBaseinfo.dao().findById(
					polocyId);
			String taskTable = baseInfo
					.getStr(PopPolicyBaseinfo.COL_POLICY_TASK_TAB);
			StringBuffer b = new StringBuffer("select send_data_tab from ");
			b.append(taskTable).append(" where id=?");
			Record r = Db.findFirst(b.toString(), taskid);
			String sendDataTab = r.getStr("send_data_tab");// 得到策略派单表
			// 更新规则派单表
			updateFeedBakStatus(rule_sendDataTab,phoneStatus);
			// 更新策略派单表
			updateFeedBakStatus(sendDataTab, phoneStatus);
		}

	}
	  
	
	
}
