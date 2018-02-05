package com.ai.bdx.pop.service.impl;

import net.sf.json.JSONObject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ai.bdx.pop.buffer.IDataHandle;
import com.ai.bdx.pop.model.PopPolicyBaseinfo;
import com.ai.bdx.pop.model.PopPolicyRule;
import com.ai.bdx.pop.service.IPopSendOddService;
import com.ai.bdx.pop.util.ContactControlUtil;
import com.ai.bdx.pop.util.PopConstant;
import com.ai.bdx.pop.util.SpringContext;
import com.ai.bdx.pop.wsclient.ISendPccInfoService;
import com.ai.bdx.pop.wsclient.PolicyOptType;
import com.asiainfo.biframe.utils.string.StringUtil;
/***
 * 接收cep返回结果数据逻辑handler
 * @author liyz
 * */
public class PopReceiveCepDataHandlerImpl  implements IDataHandle{
	private static Logger log = LogManager.getLogger(PopReceiveCepDataHandlerImpl.class);
	
	private IPopSendOddService popSendOddService;

	public IPopSendOddService getPopSendOddService() {
		return popSendOddService;
	}

	public void setPopSendOddService(IPopSendOddService popSendOddService) {
		this.popSendOddService = popSendOddService;
	}


	@Override
	public void handle(JSONObject msg) {
		try{
		//入库处理 2张表 POP_RULE_SEND_ruleid(xxxx)   POP_SEND_taskid(xxxx)
		String ruleId = (String) msg.get("rule_id");
		String phone_no = (String) msg.get("phone_no");
		String taskId = ContactControlUtil.getCurrentTaskId(ruleId);
		
		String ruleSendTableName = ContactControlUtil.getRuleSendTableNameForRuleId(ruleId);
		
		String taskSendTableName = ContactControlUtil.getTaskSendTableNameForTaskId(taskId,ruleId);
		 
		if(StringUtil.isNotEmpty(ruleSendTableName) && StringUtil.isNotEmpty(taskSendTableName)  ){
			String ruleSendSql = PopConstant.INSERT_DATA_TO_TABLE.replace("{table}", ruleSendTableName).replace("{column}", "(product_no)").replace("{?}", "(?)");
			
			String taskSendSql = PopConstant.INSERT_DATA_TO_TABLE.replace("{table}", taskSendTableName).replace("{column}", "(product_no)").replace("{?}", "(?)");
			
			//POP_RULE_SEND_
	 		popSendOddService.saveDataToDbForSql(ruleSendSql, new Object[]{phone_no});
			
			//POP_SEND_
		 	popSendOddService.saveDataToDbForSql(taskSendSql, new Object[]{phone_no});
		 	//log.debug("执行sql:"+taskSendSql);
			//add by jinl 20150819 根据需求增加参数 策略级别
		 	PopPolicyRule rule = PopPolicyRule.dao().findById(ruleId);
		 	String policy_level_id="";
		 	if(rule!=null){
			 	String policyId=rule.getStr(PopPolicyRule.COL_POLICY_ID);
			 	PopPolicyBaseinfo baseInfo = PopPolicyBaseinfo.dao().findById(policyId);
			 	if(baseInfo!=null){
			 		policy_level_id = baseInfo.get(PopPolicyBaseinfo.COL_POLICY_LEVEL_ID).toString();
			 	}
		 	}
		 	//实时派单接口拍到PCC
			ISendPccInfoService sendPccInfoService = SpringContext.getBean("sendPccInfoService",ISendPccInfoService.class);
			sendPccInfoService.singlePhoneOpt( PolicyOptType.OPEN.getValue(), phone_no, ruleId,policy_level_id);
			log.info("phone_no={} ruleId={} policy_level_id={}",phone_no,ruleId,policy_level_id);

		}else{
			log.error("ruleSendTableName,taskSendTableName 是 null!当前ruleId:"+ruleId+"++++taskId:"+taskId);
		}
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}

}
