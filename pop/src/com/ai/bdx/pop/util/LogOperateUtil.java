package com.ai.bdx.pop.util;

import javax.servlet.http.HttpServletRequest;

import com.ai.bdx.pop.exception.PopException;
import com.ai.bdx.pop.model.LogOperate;
import com.asiainfo.biframe.privilege.IUserSession;
import com.asiainfo.biframe.utils.date.DateUtil;
import com.asiainfo.biframe.utils.i18n.LocaleUtil;

public class LogOperateUtil {
	// operate key
	public static final String POLICY_MAN_ADD = "策略创建";
	public static final String POLICY_MAN_UPDATE = "策略修改";
	public static final String POLICY_MAN_DEL = "策略删除";
	public static final String POLICY_MAN_SUB = "策略提交";
	public static final String POLICY_MAN_TEM_ADD = "策略模板创建";
	public static final String POLICY_MAN_TEM_UPDATE = "策略模板修改";
	public static final String POLICY_MAN_TEM_DEL = "策略模板删除";
	public static final String POLICY_MAN_RULE_ADD = "策略规则创建";
	public static final String POLICY_MAN_RULE_UPDATE = "策略规则修改";
	public static final String POLICY_MAN_RULE_DEL = "策略规则删除";
	public static final String POLICY_MAN_APPROVAL_PASS = "策略审批通过";
	public static final String POLICY_MAN_APPROVAL_ADDJECT = "策略审批拒绝";
	public static final String POLICY_MAN_APPROVAL_TURN = "策略审批转发";
	public static final String POLICY_MAN_APPROVAL_CONFIRM_PASS = "策略审批通过";
	public static final String POLICY_MAN_APPROVAL_CONFIRM_NOPASS = "策略审批不通过";
	public static final String POLICY_MAN_APPROVAL_CONFIRM_DELIVERY = "策略审批派单";
	public static final String CPE_PARAM_RESET = "CPE参数重置";
	public static final String PCCID_ADD = "PCCID增加";
	public static final String PCCID_UPDATE = "PCCID修改";
	public static final String PCCID_DEL= "PCCID删除";
	public static final String POLICY_TASK_RESTART = "策略任务重启";
	public static final String POLICY_TASK_PAUSE = "策略任务暂停";
	public static final String POLICY_TASK_STOP = "策略任务停止";
	public static final String POLICY_RULE_RESTART = "策略规则重启";
	public static final String POLICY_RULE_PAUSE = "策略规则暂停";
	public static final String POLICY_RULE_STOP = "策略规则停止";
	
	private static LogOperate logMod = new LogOperate();

	public static void log(String operKey, String user, String desc) {
		logMod.dao()
				.set("id", PopUtil.generateId())
				.set("name", operKey)
				.set("desc", desc)
				.set("create_user", user)
				.set("create_time",
						DateUtil.getFormatCurrentTime(DateUtil.YYYY_MM_DD_HH_MM_SS))
				.save();
	}

	public static void log(String operKey,String desc,HttpServletRequest req)
   {
	   String user = "";
	   IUserSession userSession = (IUserSession) req.getSession().getAttribute("biplatform_user");
		if (userSession != null) {
			user = userSession.getUserID();
		} else {
			 user = (String)req.getSession().getAttribute("user");
		}
	  log(operKey,user,desc);
   }
}
