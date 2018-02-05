package com.ai.bdx.pop.controller;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.cxf.common.util.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ai.bdx.pop.base.PopController;
import com.ai.bdx.pop.enums.PolicyStatus;
import com.ai.bdx.pop.model.PopDimPolicyLevel;
import com.ai.bdx.pop.model.PopDimPolicyStatus;
import com.ai.bdx.pop.model.PopDimPolicyType;
import com.ai.bdx.pop.model.PopPolicyBaseinfo;
import com.ai.bdx.pop.service.IPopSendOddService;
import com.ai.bdx.pop.util.CityId2CityNameUtil;
import com.ai.bdx.pop.util.LogOperateUtil;
import com.ai.bdx.pop.util.SpringContext;
import com.jfinal.plugin.activerecord.Page;

/**
 * 策略分发
 * 
 * @author zhuyq3
 */
public class PolicyDeliveryController extends PopController {
	private static final Logger log = LogManager.getLogger();

	private static final Integer PAGE_SIZE = 10;
	
	private static final String PAGE_KEY = "page";
	
	private static final String OPER_LIKE = " like ";
	
	private static Map<String, String> CONDITION = new HashMap<String, String>();
	
	private static Map<String, String> OPERATOR = new HashMap<String, String>();
	
//	private static String searchParams = "";

	public void sendOddSearch() {
		log.info("派单查询列表页面");

		initDimInfo();
		CONDITION.clear();
		render("policyDelivery/sendOddSearch.jsp");
	}
	
	public void searchList() {
		log.info("派单查询列表页面");

		search();
		initDimInfo();
		
		initOperationMap();

		int page = getParaToInt(PAGE_KEY, 1);
		String select = "select t.id, t.policy_name, t.policy_type_id, t.policy_level_id, t.policy_status_id, t.start_time, t.end_time, t.city_priority ";
		
		StringBuffer sqlExceptSelect = new StringBuffer("from pop_policy_baseinfo t where 1 = 1 ");
		for (Iterator<String> ite = CONDITION.keySet().iterator() ; ite.hasNext(); ) {
			String condition_key = ite.next();
			String operator = OPERATOR.get(condition_key);
			if (PopPolicyBaseinfo.COL_START_TIME.equals(condition_key) || PopPolicyBaseinfo.COL_END_TIME.equals(condition_key)) {
				sqlExceptSelect.append(" and t.").append(condition_key).append(operator).append("'").append(CONDITION.get(condition_key)).append("'");
//				CONDITION.remove(condition_key);
				ite.remove();
			} else {
				sqlExceptSelect.append(" and ").append(condition_key).append(operator);
				if (OPER_LIKE.equals(operator)) {
					sqlExceptSelect.append("'%" + CONDITION.get(condition_key) + "%' ");
//				CONDITION.remove(condition_key);
					ite.remove();
				} else {
					sqlExceptSelect.append("?");
				}
			}
			
		}
		sqlExceptSelect.append(" and  t.policy_status_id in (" + PolicyStatus.UNSENDODER.getValue() + "," + PolicyStatus.SENDODER_BASE_ERROR.getValue() + ")");
//		Page<PopPolicyBaseinfo> list = PopPolicyBaseinfo.dao().paginate(page, PAGE_SIZE, select, sqlExceptSelect.toString(), null == CONDITION || CONDITION.isEmpty() ? new HashMap() : CONDITION);
		
		Page<PopPolicyBaseinfo> list = PopPolicyBaseinfo.dao().paginate(page, PAGE_SIZE, select, sqlExceptSelect.toString(), CONDITION.values().toArray());
		
//		Page<PopPolicyBaseinfo> list = PopPolicyBaseinfo.dao().searchPaginate(page, PAGE_SIZE, null == CONDITION || CONDITION.isEmpty() ? new HashMap() : CONDITION);
		
		escape(list);

		setAttr("pageList", list);
		setAttr("totalRow", list.getTotalRow());
		render("policyDelivery/policyList.jsp");
	}
	
	public void createPolicyRuleTask() {
		Map<String, Object> json = new HashMap<String, Object>();
		json.put("count_1", 0);
		json.put("count_0", 0);
		String policyIds = getPara("ids");
		try {
			IPopSendOddService service = SpringContext.getBean("IPopSendOddService", IPopSendOddService.class);
			send(policyIds, service, json);
			//记录日志
			String desc = String.format("策略派单,策略编号:%s", policyIds);
			LogOperateUtil.log(LogOperateUtil.POLICY_MAN_APPROVAL_CONFIRM_DELIVERY,desc,this.getRequest());
			renderJson(json);
		} catch (Exception e) {
			e.printStackTrace();
			json.put("result", "failed");
			json.put("error", e.getMessage());
			json.put("count_0", (Integer) json.get("count_0") + 1);
			//记录日志
			String desc = String.format("策略派单失败:%s,策略编号:%s", e.getMessage(),policyIds);
			LogOperateUtil.log(LogOperateUtil.POLICY_MAN_APPROVAL_CONFIRM_DELIVERY,desc,this.getRequest());
			renderJson(json);
		}
	}
	
	private void send(String policyIds, IPopSendOddService service,
			Map<String, Object> json) throws Exception {
		int count_1 = 0;
		int count_0 = 0;
		if (policyIds.indexOf(",") != -1) {
			for (String policyId : policyIds.split(",")) {
				boolean isSuccess = service.createPolicyRuleTask(policyId);
				if (isSuccess) {
					count_1++;
				} else {
					count_0++;
				}
			}
		} else {
			boolean isSuccess = service.createPolicyRuleTask(policyIds);
			if (isSuccess) {
				count_1++;
			} else {
				count_0++;
			}
		}
		String message = "";
		if (count_1 > 0) {
			json.put("result", "success");
			message = "派单成功";
		} else {
			message = "派单失败";
		}
		json.put("count_1", count_1);
		json.put("count_0", count_0);
		json.put("message", message);
	}

	private void search() {
		CONDITION.clear();
		Map<String, String[]> map = getParaMap();
		for (Iterator<Entry<String, String[]>> ite = map.entrySet().iterator(); ite.hasNext();) {
			Map.Entry<String, String[]> entry = (Entry<String, String[]>) ite.next();
			String _value = entry.getValue()[0].trim().replaceAll("'", "");
			String _key = entry.getKey();
			if (StringUtils.isEmpty(_value) || PAGE_KEY.equals(_key)) {
				continue;
			}
			CONDITION.put(_key, _value);
		}
		setAttr("searchParams", paralize(CONDITION));
	}

	private String paralize(Map<String, String> conditions) {
		StringBuffer sb = new StringBuffer();
		for (Iterator<Map.Entry<String, String>> ite = conditions.entrySet().iterator(); ite.hasNext() ; ) {
			Entry<String, String> entry = ite.next();
			sb.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
		}
		if (!conditions.isEmpty()) {
			sb.deleteCharAt(sb.length() - 1);
		}
		return sb.toString();
	}
	
	private void initOperationMap() {
		OPERATOR.put(PopPolicyBaseinfo.COL_POLICY_LEVEL_ID, " = ");
		OPERATOR.put(PopPolicyBaseinfo.COL_END_TIME, " <= ");
		OPERATOR.put(PopPolicyBaseinfo.COL_START_TIME, " >= ");
		OPERATOR.put(PopPolicyBaseinfo.COL_POLICY_TYPE_ID, " = ");
		OPERATOR.put(PopPolicyBaseinfo.COL_POLICY_NAME, " like ");
		OPERATOR.put(PopPolicyBaseinfo.COL_POLICY_STATUS_ID, " = ");
	}

	private void escape(Page<PopPolicyBaseinfo> list) {
		if (list.getList().isEmpty()) {
			return;
		}
		for (PopPolicyBaseinfo ppbinfo : list.getList()) {
			ppbinfo.set(PopPolicyBaseinfo.COL_POLICY_TYPE_ID, PopDimPolicyType.getTypeName(ppbinfo.get(PopPolicyBaseinfo.COL_POLICY_TYPE_ID).toString()));
			ppbinfo.set(PopPolicyBaseinfo.COL_POLICY_LEVEL_ID, PopDimPolicyLevel.getTypeName(ppbinfo.get(PopPolicyBaseinfo.COL_POLICY_LEVEL_ID).toString()));
			ppbinfo.set(PopPolicyBaseinfo.COL_POLICY_STATUS_ID, PopDimPolicyStatus.getTypeName(ppbinfo.get(PopPolicyBaseinfo.COL_POLICY_STATUS_ID).toString()));
			try {
				ppbinfo.put(PopPolicyBaseinfo.COL_CITY_PRIORITY, CityId2CityNameUtil.getCityNameByCache(ppbinfo.getStr(PopPolicyBaseinfo.COL_CITY_PRIORITY)));
			} catch (Exception e) {
				log.error("根据地市ID查询城市名称异常：" + e);
			}
		}
	}

	private void initDimInfo() {
		log.info("初始化维表数据");
		List<PopDimPolicyType> dimPolicyTypes = PopDimPolicyType.dao().findAll();
		List<PopDimPolicyLevel> dimPolicyLevels = PopDimPolicyLevel.dao().findAll();
		List<PopDimPolicyStatus> dimPolicyStatus = PopDimPolicyStatus.dao().findAll();
		PopDimPolicyType.fillTypes(dimPolicyTypes);
		PopDimPolicyLevel.fillTypes(dimPolicyLevels);
		PopDimPolicyStatus.fillTypes(dimPolicyStatus);
		setAttr("dimPolicyTypes", dimPolicyTypes);
		setAttr("dimPolicyLevels", dimPolicyLevels);
		setAttr("dimPolicyStatus", dimPolicyStatus);
	}
	
}
