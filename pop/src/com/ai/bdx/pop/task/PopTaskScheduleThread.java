package com.ai.bdx.pop.task;

import it.unimi.dsi.fastutil.bytes.Byte2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.shorts.Short2ObjectOpenHashMap;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.BitSet;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ai.bdx.pop.bean.AvoidCustBean;
import com.ai.bdx.pop.bean.PopTaskBean;
import com.ai.bdx.pop.kafka.CepMessageReceiverThread;
import com.ai.bdx.pop.kafka.CepReceiveThreadCache;
import com.ai.bdx.pop.model.PopPolicyBaseinfo;
import com.ai.bdx.pop.model.PopPolicyRule;
import com.ai.bdx.pop.service.IPopSendOddService;
import com.ai.bdx.pop.util.CepUtil;
import com.ai.bdx.pop.util.ContactControlUtil;
import com.ai.bdx.pop.util.ErrorCodeMap;
import com.ai.bdx.pop.util.PopConstant;
import com.ai.bdx.pop.util.PopTaskCache;
import com.ai.bdx.pop.util.PopUtil;
import com.ai.bdx.pop.util.SimpleCache;
import com.ai.bdx.pop.util.SpringContext;
import com.ai.bdx.pop.wsclient.ISendPccInfoService;
import com.ai.bdx.pop.wsclient.PolicyOptType;
import com.asiainfo.biframe.utils.spring.SystemServiceLocator;
import com.google.common.base.Strings;
 
/***
 * @author liyz
 * 派单相关实现
 * 
 * */
public class PopTaskScheduleThread extends Thread {
	private static Logger log = LogManager.getLogger();
	private final PopTaskBean ptb;
	int retrycount = 3;//单位：次
	
	public PopTaskScheduleThread(PopTaskBean ptb) {
		this.setName("PopTaskScheduleThread[规则ID-任务ID:" + ptb.getRuleId() + "-" +ptb.getTaskId() + "]");
		this.ptb = ptb;
	}

	@Override
	public void run() {
		log.debug("PopTaskScheduleThread[规则ID-任务ID:" + ptb.getRuleId() + "-" +ptb.getTaskId() + "] ----  start,pccid:"+ptb.getPccid());
		String taskId = ptb.getTaskId();
		String ruleId = ptb.getRuleId();
		String policyId = ptb.getPolicyId();
		String msgCode = "";
		String message = "";
		long t1 = System.currentTimeMillis();
		IPopSendOddService  popSendOddService = null;
		short sendoddStatus = 0;
		try {
			ptb.setRetry(ptb.getRetry()+1);
			popSendOddService = (IPopSendOddService) SystemServiceLocator.getInstance().getService("IPopSendOddService");
			//#######################################清表#########################################
			msgCode = "POP3001";
			popSendOddService.deleteTaskInfo(ptb);
			//#######################################获取静态客户群###################################
			AvoidCustBean avoidCustBean = null;
			//判断是否有静态客户群
			if(!Strings.isNullOrEmpty(ptb.getCustGroupId())){
				msgCode = "POP3002";
				avoidCustBean = popSendOddService.getAvoidAfterCustAndDelCust(ptb.getCustGroupId(),ptb.getAvoidCustGroupIds(),ptb.getCustGroupType(),ptb.getExecDate());
			}
			//#######################################复杂事件#######################################
			if(!Strings.isNullOrEmpty(ptb.getCepRuleId())) {
				 msgCode = "POP3003";
				//启动队列监听
				if (!CepReceiveThreadCache.getInstance().exist(ruleId)) {
					//给CEP发消息注册启动事件信息
					CepUtil.registCepEvent(ptb.getCepRuleId());
					CepMessageReceiverThread thread = new CepMessageReceiverThread(ptb.getCepRuleId(),ruleId);
					thread.start();
				}
				//数据缓存
				if(!Strings.isNullOrEmpty(ptb.getCustGroupId())){
					msgCode = "POP3004";
					//如果包含复杂事件并且有客户群,缓存静态客户群
					//拼接缓存KEY
					String ruleCustKey = PopConstant.POP_RULE_CUST_CACHE_PREFIX.replace("{RULEID}", ptb.getRuleId());
					//加载缓存数据 规则静态汇总表
					String rulePreSendDataTab =  ptb.getRulePreSendDataTab();
					Byte2ObjectOpenHashMap<Short2ObjectOpenHashMap<BitSet>> phoneData =  ContactControlUtil.convertCustList2Map(PopConstant.QUERY_USER_DATA_TO_MAP_TMP_SQL.replace("{0}", rulePreSendDataTab));
					//先清除
					SimpleCache.getInstance().remove(ruleCustKey);
					SimpleCache.getInstance().put(ruleCustKey, phoneData, getRemainTime());
				}
				//缓存当前taskId
				SimpleCache.getInstance().put(PopConstant.CACHE_KEY_ACTIVITY_TASKID + "_" + ruleId, taskId,PopUtil.getRemainTime());
			} 
			
			//#######################################pcc接口 待实现##################################
			msgCode = "POP3005";
			//popSendOddService.sendCustInfo2ChannelNotRealTime(avoidCustBean,ptb);
			//sendoddStatus = PopConstant.TASK_STATUS_PDCG;
			//ptb.setExecStatus(sendoddStatus);
			
			//#######################################数据入库#######################################
			msgCode = "POP3006";
			popSendOddService.saveDataToDb(avoidCustBean,ptb);
			sendoddStatus = PopConstant.TASK_STATUS_PDCG;
			ptb.setExecStatus(sendoddStatus);
	        log.debug("存储静态客户群结果成功!"+ptb.getSendDataTab());
			//#######################################更新状态#######################################
			message = "任务执行成功!";
			//更新 策略规则执行历史模板表 状态
			ptb.setEndTime(new Timestamp(System.currentTimeMillis()));
			popSendOddService.updatePopTaskExeRelation(ptb,message,true);
			//派发批量用户签约信息接口
			
			ISendPccInfoService sendPccInfoService = SpringContext.getBean("sendPccInfoService",ISendPccInfoService.class);
			
			log.info("SendDataTab={} PolicyId={}",ptb.getSendDataTab(),ptb.getPolicyId());
			//获取策略类型 add by jinl
			//add by jinl 20150819 根据需求增加参数 策略级别
		 	PopPolicyRule rule = PopPolicyRule.dao().findById(ruleId);
		 	String policy_level_id="";
		 	if(rule!=null){
			 	String policy_Id=rule.getStr(PopPolicyRule.COL_POLICY_ID);
			 	PopPolicyBaseinfo baseInfo = PopPolicyBaseinfo.dao().findById(policy_Id);
			 	if(baseInfo!=null){
			 		policy_level_id = baseInfo.get(PopPolicyBaseinfo.COL_POLICY_LEVEL_ID).toString();
			 	}
		 	}
		 	log.debug("派发批量用户签约信息接口开始!ptb.getPccid():"+ptb.getPccid());
		 	sendPccInfoService.batchPhonesOpt(PolicyOptType.OPEN.getValue(), ptb.getSendDataTab(),  ptb.getPccid(),policy_level_id);
		 	//sendPccInfoService.batchPhonesOpt(PolicyOptType.OPEN.getValue(), ptb.getSendDataTab(),  ptb.getPolicyId(),policy_level_id);
			log.debug("派发批量用户签约信息接口结束!");
			log.debug("PopTaskScheduleThread[规则ID-任务ID:" + ptb.getRuleId() + "-" +ptb.getTaskId() + "] ---- success");
			log.debug("ruleId{},本次任务[{}]执行共计耗时：{}",ruleId,taskId, (System.currentTimeMillis() - t1) / 1000.0 + " s.");
		} catch (Throwable e) {
			log.error(e);
			sendoddStatus = PopConstant.TASK_STATUS_PDSB;
			ptb.setExecStatus(sendoddStatus);
			this.doError(msgCode, e);
		}  
		
	}
	private long getRemainTime() {
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(new Date());
		calendar.set(Calendar.HOUR, -12);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		calendar.add(calendar.DATE, 1);
		long tomorrow = calendar.getTimeInMillis();

		return tomorrow - System.currentTimeMillis();
	}
	
	private void doError(String msgCode, Throwable e) {
		try {
			//回滚
			String step = (String) msgCode.substring(3,msgCode.length());
			if(!Strings.isNullOrEmpty(ptb.getCepRuleId()) && Integer.parseInt(step) >= 2003 ){
				removeCep();
			}
			if (ptb.getRetry() >= retrycount) {
				PopTaskCache.getInstance().removeTask(ptb);
				String message = ErrorCodeMap.getMsg(msgCode);
				ptb.setEndTime(new Timestamp(System.currentTimeMillis()));
				IPopSendOddService popSendOddService = (IPopSendOddService) SystemServiceLocator.getInstance().getService("IPopSendOddService");
				popSendOddService.updatePopTaskExeRelation(ptb,message,false);
			}else{
				Calendar calendar = new GregorianCalendar();
				calendar.setTime(new Date());
				calendar.add(Calendar.MINUTE,10);
				Long delayLongTime = calendar.getTimeInMillis();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String delayTime =  sdf.format(new Date(delayLongTime));
				ptb.setExecDate(delayTime);
				log.warn("++++++++++任务执行失败!规则id:"+ptb.getRuleId()+",任务id:"+ptb.getTaskId()+"+++系统将延迟10分钟后:"+delayTime+"重新执行!++++++++");
				PopTaskCache.getInstance().putTask(ptb);
			}
			
			
		}catch (Throwable ee) {
			log.error("", ee);
		}
	}
	
	
	private void removeCep(){
		if (CepReceiveThreadCache.getInstance().exist(ptb.getRuleId())) {
			try {
				CepUtil.stopCepEvent(ptb.getCepRuleId());
				CepReceiveThreadCache.getInstance().remove(ptb.getRuleId());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	

}
