package com.ai.bdx.pop.service.impl;


import org.springframework.jdbc.core.JdbcTemplate;

import com.ai.bdx.frame.privilegeServiceExt.service.IUserPrivilegeCommonService;
import com.ai.bdx.pop.bean.SmsMsgBean;
import com.ai.bdx.pop.model.PopSmsSend;
import com.ai.bdx.pop.model.PopSmsTemplet;
import com.ai.bdx.pop.service.SmsService;

public abstract class AbstractSmsServcieImpl implements SmsService{

	protected JdbcTemplate jdbcTemplate;
	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	protected IUserPrivilegeCommonService userPrivilegeService;
	
	
	
	public IUserPrivilegeCommonService getUserPrivilegeService() {
		return userPrivilegeService;
	}

	public void setUserPrivilegeService(IUserPrivilegeCommonService userPrivilegeService) {
		this.userPrivilegeService = userPrivilegeService;
	}

	/**
	 * 
	 * getSmsTempletByCode:根据CODE查询指定的短信模板
	 * @param code
	 * @return
	 * @throws Exception 
	 * @see com.ai.bdx.pop.service.SmsService#getSmsTempletByCode(java.lang.String)
	 */
	public PopSmsTemplet getSmsTempletByCode(String code)throws Exception{
		return PopSmsTemplet.dao().findById(code);
	}
	
   /**
    * 
    * SendMessage:发送短信方法
    * @param sendMsg
    * @return
    * @throws Exception 
    * @see com.ai.bdx.pop.service.SmsService#SendMessage
    */
	public String SendMessage(SmsMsgBean sendMsg)
			throws Exception {
		
        if(sendMsg!=null){
        	PopSmsSend sms=new PopSmsSend();
        	sms.set(PopSmsSend.COL_PHONENO, sendMsg.getPhoneNo());
        	sms.set(PopSmsSend.COL_SENDMSG, sendMsg.getSendMsg());
        	sms.set(PopSmsSend.COL_SENDFLAG, sendMsg.getSengFlag());
        	sms.set(PopSmsSend.COL_CHANNELNO, sendMsg.getChannelNo());
        	sms.set(PopSmsSend.COL_SENDTYPE, sendMsg.getSendtype());
        	sms.save();//先入库
        }
		  return null;
	}
	
	public String SendMessage(String smsCode, String PolicyID) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
}
