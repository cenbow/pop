package com.ai.bdx.pop.task;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;

import com.ai.bdx.pop.bean.PopUserPolicyManageBean;
import com.ai.bdx.pop.util.PopConstant;
import com.ai.bdx.pop.util.SpringContext;
import com.asiainfo.biframe.utils.string.StringUtil;
/**
 * 	用户策略规则汇总数据表调度
   * @ClassName: PopUserPolicyRuleServiceJob
   * @Description: TODO
   * @author zhilj
   * @date 创建时间：2015-4-24
 */
public class PopUserPolicyRuleServiceJob implements Serializable{
	private static final long serialVersionUID = 1L;
	private static Logger log = LogManager.getLogger();
	
	public void runTask() {
		try{			
			List<PopUserPolicyManageBean> beanList = new ArrayList<PopUserPolicyManageBean>();
			JdbcTemplate jdbcTemplate = SpringContext.getBean("jdbcTemplate", JdbcTemplate.class);
			log.debug("用户策略规则汇总数据表..开始!");
			StringBuffer querySql = new StringBuffer();
			querySql.append("select pr.id, pr.policy_rule_name, pb.policy_type_id, pb.policy_level_id, pa.policy_act_type_id,pa.exec_name, ");
			querySql.append("pr.create_time, pr.send_data_tab, pr.rule_status  ");
			querySql.append("from pop_policy_rule pr,  pop_policy_baseinfo pb, ");
			querySql.append("(select  pa.policy_rule_id, pa.policy_act_type_id,concat(ifnull(concat('执行内容：',concat ( ");
			querySql.append("(select pc.name from pop_dim_camp_channel pc where pa.exec_channel_id = pc.id), '---', pa.exec_camp_content, '---', ");
			querySql.append("(select pf.name from pop_dim_contact_freq pf where pf.id=pa.exec_camp_frequency) ),'   '),'') ,ifnull(concat('失效内容：',concat( ");
			querySql.append("(select  pc.name from pop_dim_camp_channel pc where pc.id=pa.invalid_channel_id ),'---',pa.invalid_camp_content,'---', ");
			querySql.append("(select pf.name from  pop_dim_contact_freq pf where pf.id=pa.invalid_camp_frequency)),'   '),'')) exec_name from pop_policy_rule_act pa  ");
			querySql.append("where pa.policy_act_type_id = 2 union  ");
			querySql.append("select pa.policy_rule_id,pa.policy_act_type_id, concat(pt.name,'---',control_param) exec_name ");
			querySql.append("from pop_policy_rule_act pa, pop_dim_control_type pt ");
			querySql.append("where policy_act_type_id = 1 and pa.control_type_id =pt.id) pa  ");
			querySql.append("where length(pr.send_data_tab)>0 and pr.policy_id = pb.id and pr.id = pa.policy_rule_id and pr.rule_status in(?,?,?,?,?) ");
			List<Map> list = jdbcTemplate.queryForList(querySql.toString(), new Object[] {PopConstant.RULE_STATUS_PAIDANCHENGGONG,PopConstant.RULE_STATUS_WANCHENG,
						PopConstant.RULE_STATUS_ZANTING,PopConstant.RULE_STATUS_ZHIXINGZHONG,PopConstant.RULE_STATUS_ZHONGZHI});
			if(CollectionUtils.isNotEmpty(list)){
				for(int i=0;i<list.size();i++){
					String tableName = (String) list.get(i).get("send_data_tab");
					if(StringUtil.isNotEmpty(tableName)){
						StringBuffer tableSql = new StringBuffer();
						tableSql.append("select product_no, feedback_status, feedback_time from "+tableName.toLowerCase());
						List<Map> reallist = jdbcTemplate.queryForList(tableSql.toString());
						if(CollectionUtils.isNotEmpty(reallist)){
							for(int j=0;j<reallist.size();j++){
								PopUserPolicyManageBean userBean = new PopUserPolicyManageBean();
								userBean.setRuleId((String)list.get(i).get("id"));								
								userBean.setPolicyRuleName((String)list.get(i).get("policy_rule_name"));
								userBean.setPolicyTypeId((String) list.get(i).get("policy_type_id"));
								userBean.setPolicyLevelId(String.valueOf(list.get(i).get("policy_level_id")));
								userBean.setActTypeId(String.valueOf(list.get(i).get("policy_act_type_id")));
								userBean.setCreateTime((Date)list.get(i).get("create_time"));
								userBean.setProductNo((String)reallist.get(j).get("product_no"));
								userBean.setPolicySignTime((Date)reallist.get(j).get("feedback_time"));
								userBean.setPolicySignStatus(String.valueOf(reallist.get(j).get("feedback_status")));
								userBean.setRuleStatusId(String.valueOf(list.get(i).get("rule_status")));
								userBean.setPolicyRuleContent((String)list.get(i).get("exec_name"));
								beanList.add(userBean);
							}
						}
					}
				}
			}
			if(CollectionUtils.isNotEmpty(beanList)){
				String deleteSql = new String();
				deleteSql = "truncate table pop_user_policy_rule_data";
				jdbcTemplate.execute(deleteSql);
				insertIntoTable(beanList,jdbcTemplate);
			}
			log.debug("用户策略规则汇总数据表..成功!");
		}catch(Exception e){
			log.error("用户策略规则汇总数据表失败!"+e);
		}
		
	}

	private void insertIntoTable(final List<PopUserPolicyManageBean> beanList,JdbcTemplate jdbcTemplate) {
		StringBuffer sql = new StringBuffer();
		sql.append("insert into pop_user_policy_rule_data ");
		sql.append("(product_no, rule_id, policy_rule_name, policy_type_id, policy_level_id, act_type_id, policy_rule_content, create_time, policy_sign_time, policy_sign_status,rule_status_id) ");
		sql.append("values (?,?,?,?,?,?,?,?,?,?,?) ");
		jdbcTemplate.batchUpdate(sql.toString(), new BatchPreparedStatementSetter(){
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				 ps.setString(1, beanList.get(i).getProductNo());
				 ps.setString(2, beanList.get(i).getRuleId());
				 ps.setString(3, beanList.get(i).getPolicyRuleName());
				 ps.setShort(4, Short.valueOf(beanList.get(i).getPolicyTypeId()));
				 ps.setShort(5, Short.valueOf(beanList.get(i).getPolicyLevelId()));
				 ps.setShort(6, Short.valueOf(beanList.get(i).getActTypeId()));
				 ps.setString(7, beanList.get(i).getPolicyRuleContent());
				 ps.setTimestamp(8,(java.sql.Timestamp)beanList.get(i).getCreateTime());
				 ps.setTimestamp(9, (java.sql.Timestamp) beanList.get(i).getPolicySignTime());
				 if(StringUtil.isEmpty(beanList.get(i).getPolicySignStatus())||"null".equals(beanList.get(i).getPolicySignStatus())){
					 ps.setNull(10, Types.INTEGER);
				 }else{
					 ps.setShort(10, Short.valueOf(beanList.get(i).getPolicySignStatus()));
				 }
				 ps.setShort(11, Short.valueOf(beanList.get(i).getRuleStatusId()));
			}
			public int getBatchSize() {
				return beanList.size();
			}
			
		  });
	}
}
