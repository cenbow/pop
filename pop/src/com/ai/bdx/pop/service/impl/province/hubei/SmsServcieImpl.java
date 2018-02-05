package com.ai.bdx.pop.service.impl.province.hubei;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ai.bdx.frame.approval.service.IApprovalService;
import com.ai.bdx.pop.bean.SmsMsgBean;
import com.ai.bdx.pop.model.PopPolicyBaseinfo;
import com.ai.bdx.pop.model.PopSmsSend;
import com.ai.bdx.pop.model.PopSmsTemplet;
import com.ai.bdx.pop.service.impl.AbstractSmsServcieImpl;
import com.ai.bdx.pop.util.ApprovalCONST;
import com.asiainfo.biframe.privilege.IUser;
import com.asiainfo.biframe.utils.spring.SystemServiceLocator;
import com.asiainfo.biframe.utils.string.StringUtil;

public class SmsServcieImpl extends AbstractSmsServcieImpl {
	private static Logger log = LogManager.getLogger();


	/**
	 * 
	 * SendMessage:发送短信
	 * @param smsCode
	 * @param PolicyID
	 * @return
	 * @throws Exception 
	 * @see com.ai.bdx.pop.service.SmsService#SendMessage(java.lang.String, java.lang.String)
	 */
	public String SendMessage(String smsCode, String policy_id) throws Exception {
		PopPolicyBaseinfo policyBaseInfo = PopPolicyBaseinfo.dao().findById(policy_id);
		PopSmsTemplet smsTmp=getSmsTempletByCode(smsCode);
		IApprovalService service=(IApprovalService) SystemServiceLocator.getInstance().getService("approvalService");
		
		List<String> list=service.getCurrentApproversByApprovalID(policy_id);
		//userPrivilegeService.get
		List<IUser> users=userPrivilegeService.getUserList(list);
		if(users!=null&&users.size()>0){
			IUser cuser=userPrivilegeService.getUser(policyBaseInfo.getStr(PopPolicyBaseinfo.COL_CREATE_USER_ID));
			String sendMsg=smsTmp.getStr(PopSmsTemplet.COL_SMSCONTENT);
			//由[#person#] 策划的策略 [#policyname#]，需要您的 审批.您尽快登陆PCC运营平台进行审批
			sendMsg=sendMsg.replace("#person#", cuser.getUsername()).replace("#policyname#", policyBaseInfo.getStr(PopPolicyBaseinfo.COL_POLICY_NAME));
			for(int i=0;i<users.size();i++){
				IUser u=users.get(i);
				SmsMsgBean sms=new SmsMsgBean(u.getMobilePhone(),sendMsg);
				SendMessage(sms);
			}
			
		}
		return null;
	}
	
	public String SendMessage(SmsMsgBean sendMsg) throws Exception {
		if (sendMsg != null) {

			PopSmsSend sms = new PopSmsSend();
			sms.set(PopSmsSend.COL_PHONENO, sendMsg.getPhoneNo());
			sms.set(PopSmsSend.COL_SENDMSG, sendMsg.getSendMsg());
			sms.set(PopSmsSend.COL_SENDFLAG, sendMsg.getSengFlag());
			sms.set(PopSmsSend.COL_CHANNELNO, sendMsg.getChannelNo());
			sms.set(PopSmsSend.COL_SENDTYPE, sendMsg.getSendtype());
			sms.save();// 先入库

			// 调用shell发送短信
			log.debug("调用短信接口发送短信：phoneNo={},sendMsg={},channelNo={}",sendMsg.getPhoneNo(), sendMsg.getSendMsg(),sendMsg.getChannelNo());
			execJar(sendMsg.getPhoneNo(), sendMsg.getSendMsg(),sendMsg.getChannelNo());
		}
		return null;
	}
	

	/**
	 * 
	 * execJar:调用短信脚本程序
	 * @param PhoneNo
	 * @param Msg
	 * @param channel
	 * @return
	 * @throws Exception
	 * @return String
	 */
	public String execJar(String PhoneNo, String Msg, String channel)
			throws Exception {
		if(StringUtil.isEmpty(channel)){
			channel="1065845101";
		}
		String result = "";
		//java -Dfile.encoding=GBK -jar /home/pop/sms/sms.jar  18872489608 测试222  1065845101
		//String shell="java -Dfile.encoding=GBK -jar /home/pop/sms/sms.jar "+PhoneNo+"  "+Msg+" "+channel;
		String shell="java -Dfile.encoding=GBK -jar /home/pop/sms/sms.jar "+PhoneNo+" "+Msg.replaceAll("\\s*","")+" "+channel;
		log.debug("shell="+shell);
		try {
			Process process = Runtime.getRuntime().exec(shell);
			process.waitFor();
			InputStream in=process.getInputStream();
			BufferedReader read = new BufferedReader(new InputStreamReader(in));
			result = read.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		log.error("短信发送结果：PhoneNo={},Msg={},channel={},result={}", PhoneNo, Msg,channel, result);
		return result;
	}

	

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String PhoneNo="18872489608";
		String Msg="您好";
		String channel="1065845101";
		//execJar(PhoneNo,Msg,channel);
	}
}
