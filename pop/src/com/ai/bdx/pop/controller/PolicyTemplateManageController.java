package com.ai.bdx.pop.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ai.bdx.pop.base.PopConfig;
import com.ai.bdx.pop.base.PopController;
import com.ai.bdx.pop.bean.PopPolicySceneManageBean;
import com.ai.bdx.pop.exception.PopException;
import com.ai.bdx.pop.model.PopPolicyBaseinfo;
import com.ai.bdx.pop.util.CityId2CityNameUtil;
import com.ai.bdx.pop.util.LogOperateUtil;
import com.asiainfo.biframe.utils.date.DateUtil;
import com.asiainfo.biframe.utils.string.StringUtil;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;

public class PolicyTemplateManageController extends PopController {
	private static final Logger log = LogManager.getLogger(PopConfig.class);
	private static final Integer PAGE_SIZE = 10;
private static final String PAGE_KEY = "page";	
	private static List<Object> condition = new ArrayList<Object>();
	
	public void index() {
		render("policyConfig/policyTemplateManageIndex.jsp");
	}
	
	

	public void search() {
		initAttributes();
		int page = getParaToInt("page", 1);		
		String policyName = this.getPara("policyName","").trim();		
		String policyType = this.getPara("policyTypeId","");
		String startTime = this.getPara("startDate","");
		String endTime = this.getPara("endDate","");
		String rulePriority = this.getPara("policyLevelId","");
		
		PopPolicySceneManageBean bean = null;
		List<PopPolicySceneManageBean> popPolicySceneManageBeanList = new ArrayList<PopPolicySceneManageBean>();
		//查询策略的基本信息
		StringBuffer select=new StringBuffer();
		StringBuffer policyBaseinfoSQL = new StringBuffer();
		
		select.append("SELECT info.id,info.policy_name,info.policy_desc, info.start_time,info.end_time, type.name as policy_type_id,")
		.append(" info.template_flag, info.policy_status_id,info.create_user_id, info.create_time,info.policy_task_tab,info.city_priority " ) ;						
		policyBaseinfoSQL.append("FROM  pop_policy_baseinfo info join pop_dim_policy_type type" )
		.append(" on info.policy_type_id=type.id ");
		
		policyBaseinfoSQL.append(" and (info.template_flag=1 or (info.template_flag=2 and info.create_user_id= ?)) ");		
		condition.clear();
		condition.add(userId);
		if(!StringUtil.isEmpty(policyName)){
			policyBaseinfoSQL.append(" and info.policy_name like ?");
			condition.add("%" + policyName + "%");
		}
		if(!StringUtil.isEmpty(policyType)){
			String type [] =policyType.split(",");
			policyBaseinfoSQL.append(" and(");
			for(int i=0;i<type.length;i++){
				policyBaseinfoSQL.append("  info.policy_type_id = ?");			
				condition.add(type[i]);
				if(i!=type.length-1){
					policyBaseinfoSQL.append(" or ");					
				}else{
					policyBaseinfoSQL.append(" ) ");
				}
			}	
		}
		if(!StringUtil.isEmpty(startTime)){
			policyBaseinfoSQL.append(" and info.create_time >= ?");
			condition.add(DateUtil.string2Date(startTime,"yyyy-MM-dd"));
		}
		if(!StringUtil.isEmpty(endTime)){
			policyBaseinfoSQL.append(" and info.create_time <= ?");
			condition.add(DateUtil.string2Date(endTime,"yyyy-MM-dd"));
		}
		if(!StringUtil.isEmpty(rulePriority)){
			String Priority [] =rulePriority.split(",");
			policyBaseinfoSQL.append(" and(");
			for(int i=0;i<Priority.length;i++){
				policyBaseinfoSQL.append("  info.policy_level_id = ?");			
				condition.add(Integer.parseInt(Priority[i]));
				if(i!=Priority.length-1){
					policyBaseinfoSQL.append(" or ");					
				}else{
					policyBaseinfoSQL.append(" ) ");
				}
			}			
		}
		policyBaseinfoSQL.append(" order by info.create_time desc");
		log.debug("PolicySceneManageController.search() policyBaseinfoSQL:" + policyBaseinfoSQL.toString());
		
		Page<PopPolicyBaseinfo> popPolicyBaseinfoList = PopPolicyBaseinfo.dao().paginate(page, PAGE_SIZE, select.toString(), policyBaseinfoSQL.toString(),condition.toArray());

		if (popPolicyBaseinfoList == null || popPolicyBaseinfoList.getList().size() == 0) {
			render("policyConfig/policyTemplateManageList.jsp");
			return;
		}
				
		//封装查询出的策略基本信息,整理policyId作为规则的查询条件
		PopPolicyBaseinfo popPolicyBaseinfoObj = null;
		for (int i = 0; i < popPolicyBaseinfoList.getList().size(); i++) {
			popPolicyBaseinfoObj = popPolicyBaseinfoList.getList().get(i);
			bean = popPolicyBaseinfoObj.toBean(PopPolicySceneManageBean.class);
			try {
				bean.setCityPriorityName(CityId2CityNameUtil.getCityNameByCache(popPolicyBaseinfoObj.getStr("city_priority")));
			} catch (Exception e) {
				log.error("根据地市ID查询城市名称异常：" + e);
			}
			bean.setPolicyId(String.valueOf(popPolicyBaseinfoObj.get("id")));
			bean.setPolicyTypeName(String.valueOf(popPolicyBaseinfoObj.get("policy_type_id")));
			bean.setPolicyRuleName(String.valueOf(popPolicyBaseinfoObj.get("create_time")));
			popPolicySceneManageBeanList.add(bean);

		}
		setAttr("popPolicySceneManageBeanList", popPolicySceneManageBeanList);
		setAttr("totalRowOfPolicySceneManage", popPolicyBaseinfoList.getTotalRow());
		setAttr("totalPageOfPolicySceneManage", popPolicyBaseinfoList.getTotalPage());
		setAttr("pageNumberOfPolicySceneManage", popPolicyBaseinfoList.getPageNumber());
		setAttr("pageSizeOfPolicySceneManage", popPolicyBaseinfoList.getPageSize());
		render("policyConfig/policyTemplateManageList.jsp");
	}
	
	
	public void delete(){
		Map<String, String> map = new HashMap<String, String>();
		String policyId = this.getPara("policyId","");
		try {
			if (StringUtil.isEmpty(policyId)) {
				throw new PopException("删除策略失败，策略Id为空。");
			}
			Db.update("DELETE FROM pop_policy_baseinfo WHERE id='"+policyId+"'");
			Db.update("DELETE FROM pop_policy_rule WHERE policy_id='"+policyId+"'");
			Db.update("DELETE FROM pop_policy_rule_act  WHERE  policy_rule_id in(SELECT id FROM  " +
					" pop_policy_rule WHERE  policy_id='"+policyId+"')");
			Db.update("DELETE from pop_policy_rule_custgroup where policy_rule_id in(SELECT id FROM  " +
					" pop_policy_rule WHERE  policy_id='"+policyId+"')");			
			map.put("success", "1");
			//记录日志
			String desc = String.format("删除策略模板,策略模板编号:%s", policyId);
			LogOperateUtil.log(LogOperateUtil.POLICY_MAN_TEM_DEL,desc,this.getRequest());
		} catch (PopException e) {
			map.put("success", "0");
			map.put("msg", e.getMessage());
			//记录日志
			String desc = String.format("删除策略模板失败:%s,策略模板编号:%s",e.getMessage(), policyId);
			LogOperateUtil.log(LogOperateUtil.POLICY_MAN_TEM_DEL,desc,this.getRequest());
		}
		//search();
		this.renderJson(map);
		}
		
		
	}


