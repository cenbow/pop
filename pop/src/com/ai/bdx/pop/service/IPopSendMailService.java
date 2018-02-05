package com.ai.bdx.pop.service;
/**
   * @ClassName: IPopSendMailService 
   * @Description: pop发送邮件接口
   * @author zhilj
   * @date 创建时间：2015-5-11
 */
public interface IPopSendMailService {
	/**
	   * @Description: 发送邮件
	   * @param mailtitle
	   * @param mailContent
	   * @param filenames
	   * @param mailTos
	   * @Return: void
	 */
	public abstract void sendMail(String mailtitle,String mailContent,String filenames,String mailTos);
}
