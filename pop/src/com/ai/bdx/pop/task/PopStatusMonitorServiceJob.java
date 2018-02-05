package com.ai.bdx.pop.task;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.tools.ant.util.DateUtils;
import org.springframework.util.CollectionUtils;

import com.ai.bdx.pop.bean.PopPolicyBaseinfoBean;
import com.ai.bdx.pop.bean.PopPolicyRuleSendBean;
import com.ai.bdx.pop.bean.PopTaskBean;
import com.ai.bdx.pop.enums.PolicyStatus;
import com.ai.bdx.pop.kafka.CepReceiveThreadCache;
import com.ai.bdx.pop.service.IPopSendOddService;
import com.ai.bdx.pop.util.CepUtil;
import com.ai.bdx.pop.util.PopConstant;
import com.ai.bdx.pop.util.PopTaskCache;
import com.ai.bdx.pop.util.SimpleCache;
import com.ai.bdx.pop.util.ThreadPool;
import com.asiainfo.biframe.utils.config.Configure;
import com.asiainfo.biframe.utils.spring.SystemServiceLocator;
import com.asiainfo.biframe.utils.string.StringUtil;

/**
 * 任务过期终止定时任务
 * 
 * @author liyz
 * */
public class PopStatusMonitorServiceJob implements Serializable {
	private static Logger log = LogManager.getLogger();
	
	private IPopSendOddService popSendOddService;

	public IPopSendOddService getPopSendOddService() {
		return popSendOddService;
	}
	public void setPopSendOddService(IPopSendOddService popSendOddService) {
		this.popSendOddService = popSendOddService;
	}

	public void runTask() {
		try {
			log.info("策略过期定时任务-开始!");
			List<PopPolicyBaseinfoBean>  popPolicyBaseinfoBeanList = popSendOddService.getOutOfDatePopPolicyList();
			for(PopPolicyBaseinfoBean popPolicyBaseinfoBean:popPolicyBaseinfoBeanList){
				String policyId = popPolicyBaseinfoBean.getPolicyId();
				String popTaskTable = popPolicyBaseinfoBean.getPolicyTaskTab();
				log.debug("+++++++执行策略id:"+policyId+",状态过期查询遍历..++++++++++++");
				Map<String,PopPolicyRuleSendBean> popPolicyRuleMap  = popPolicyBaseinfoBean.getPopPolicyRuleMap();
				Set<String> policyKeySet = popPolicyRuleMap.keySet();
				for (Iterator<String> itr = policyKeySet.iterator(); itr.hasNext();) {
					String key = itr.next();
					PopPolicyRuleSendBean  popPolicyRuleSendBean= popPolicyRuleMap.get(key);
					String ruleId = popPolicyRuleSendBean.getRuleId();
					log.debug("+++++++执行规则id:"+ruleId+",状态过期查询遍历..++++++++++++");
					Map<String, PopTaskBean> popTaskBeanMap  = popPolicyRuleSendBean.getPopTaskMap();
					Set<String> taskKeySet = popTaskBeanMap.keySet();
					for (Iterator<String> itrt = taskKeySet.iterator(); itrt.hasNext();) {
						String taskId = itrt.next();
						PopTaskBean popTaskBean= popTaskBeanMap.get(taskId);
						short execStatus = popTaskBean.getExecStatus();
						if(execStatus == PopConstant.TASK_STATUS_DDPD || execStatus == PopConstant.TASK_STATUS_PDZ || execStatus == PopConstant.TASK_STATUS_PDZT ){
							//将状态设置为失效
							String sql = "update "+popTaskTable +" set exec_status = ?,exec_info=? where id = ?  " ;
							popSendOddService.saveDataToDbForSql(sql,new Object[]{PopConstant.TASK_STATUS_PDSX,"任务逾期未执行已失效!",taskId});
							log.debug("++++++任务id:"+ruleId+"逾期未执行,设置状态为过期..++++++++++++");
						} 
						PopTaskCache.getInstance().removeTask(popTaskBean);
					}
					
					//判断是否包含cep
					if(!StringUtil.isEmpty(popPolicyRuleSendBean.getCepRuleId())){
						CepUtil.finishCepEvent(popPolicyRuleSendBean.getCepRuleId());
						//关闭监听
						if (CepReceiveThreadCache.getInstance().exist(ruleId)) {
							try {
								CepReceiveThreadCache.getInstance().remove(ruleId);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}
					
					//更改rule 状态
					String sql = "update  POP_POLICY_RULE set rule_status = ?  where id = ?  " ;
					popSendOddService.saveDataToDbForSql(sql,new Object[]{PolicyStatus.SENDODER_DONE.getValue(),ruleId});
				}
				//更改 policy 状态
				String sql = "update  POP_POLICY_BASEINFO set policy_status_id = ?  where id = ?  " ;
				popSendOddService.saveDataToDbForSql(sql,new Object[]{PolicyStatus.SENDODER_DONE.getValue(),policyId});
			}
			
			log.info("策略过期定时任务-成功!");
		} catch (Exception e) {
			log.error("策略过期定时任务-异常", e);
		}

	}
}
