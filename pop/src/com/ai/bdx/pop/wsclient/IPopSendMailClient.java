package com.ai.bdx.pop.wsclient;

import javax.jws.WebService;

import com.ai.bdx.pop.bean.MailSendInfo;
@WebService
public interface IPopSendMailClient {
	public boolean sendMail(MailSendInfo mailInfo);
}
