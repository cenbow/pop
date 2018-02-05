package com.ai.bdx.pop.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.cxf.common.util.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ai.bdx.pop.base.PopController;
import com.ai.bdx.pop.model.PopDimPccId;
import com.ai.bdx.pop.model.PopDimPccIdUseFlag;
import com.ai.bdx.pop.model.PopPolicyBaseinfo;
import com.ai.bdx.pop.util.LogOperateUtil;
import com.ai.bdx.pop.util.PopUtil;
import com.asiainfo.biframe.utils.string.StringUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
/**
 * pccid 管理
 * 
 * @author jinlong
 * @date 2015-06-05
 */
public class PccIdManageController extends PopController {
	private static final Logger log = LogManager.getLogger(PccIdManageController.class);

	public static Map<String, Object> searchConditionMap = new HashMap<String, Object>();

	private static final Integer PAGE_SIZE = 10;
	
	private static final String PAGE_KEY = "page";
	
	private static final String OPER_LIKE = " like ";
	
	private static Map<String, String> CONDITION = new HashMap<String, String>();
	
	private static Map<String, String> OPERATOR = new HashMap<String, String>();
	

	public void pccIdManageSearch() {
		log.info("派单查询列表页面");

		//initPccIdManageInfo();
		CONDITION.clear();
		render("pccidmanage/pccidmanage.jsp");
	}
	
	public void searchList() {
		log.info("派单查询列表页面");
		List<Object> values = new ArrayList<Object>();
		search();
		//initPccIdManageInfo();
		
		int searchByForm = getParaToInt("searchByForm", 0);
		String pccid_id = this.getPara("pccid_id", "").trim();
		String pccid_name = this.getPara("pccid_name", "").trim();
		String pccid_use_flag = this.getPara("pccid_use_flag", "").trim();
		int page = getParaToInt(PAGE_KEY, 1);
		String select = "select t.id ,t.pccname, t.use_flag, t.remark,t.addtime ";
		
		StringBuffer sqlExceptSelect = new StringBuffer(" from pop_dim_pcc_id  t where 1 = 1 ");
		if (StringUtil.isNotEmpty(pccid_id)) {
			//sqlExceptSelect.append(" and t.id like '%" + pccid_id + "%' ");
			sqlExceptSelect.append(" and t.id like ? ");
			values.add("%"+pccid_id+"%");
		}
		if (StringUtil.isNotEmpty(pccid_name)) {
			//sqlExceptSelect.append(" and t.pccname like '%" + pccid_name + "%' ");
			sqlExceptSelect.append(" and t.pccname like ? ");
			values.add("%"+pccid_name+"%");
		}
		if (StringUtil.isNotEmpty(pccid_use_flag)) {
			sqlExceptSelect.append(" and t.use_flag in(" + pccid_use_flag+") ");
		}
			sqlExceptSelect.append(" order by t.use_flag asc ");
		String sqlstr=select +sqlExceptSelect.toString();
		System.out.println(sqlstr);
		Page<PopDimPccId> list =null;
		try {
			list = PopDimPccId.dao().paginate(page, 10, "select * ",
					"from (" + sqlstr + ") a ",values.toArray());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		setAttr("pageList", list);
		setAttr("totalRow", list.getTotalRow());
		setAttr("totalPage", list.getTotalPage());
		setAttr("pageNumber", list.getPageNumber());
		setAttr("pageSize", list.getPageSize());
		render("pccidmanage/pccidmanageList.jsp");
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
	/**
	 * 
	 * @Title: initPccIdManageInfo
	 * @Description: TODO
	 * @param     
	 * @return void 
	 * @throws
	 */
	public void initPccIdManageInfo(){
		
	}
	
	/**
	 * PCCID创建初始化
	 */
	public void addInit() {
		initAttributes();
		String from = getPara("from");
		String id = getPara("id");
		log.debug("PCCID创建初始化...");
		List<PopDimPccIdUseFlag> popDimPccIdUseFlags = PopDimPccIdUseFlag.dao().findAll();//所有PCCID使用标志		
		setAttr("popDimPccIdUseFlags", popDimPccIdUseFlags);
		setAttr("opt", "add");
		render("pccidmanage/pccidmanageCreate.jsp");
	}
	
	/**
	 * 查看
	 */
	public void view() {
		this.initAttributes();
		String id = this.getPara("id");
		String opt = "view";
		PopDimPccId model = PopDimPccId.dao().findById(id);
		if(StringUtil.isNotEmpty(model)){
			//格式化显示有效期：生效时间，失效时间 
			setAttr("model", model);
			List<PopDimPccIdUseFlag> popDimPccIdUseFlags = PopDimPccIdUseFlag.dao().findAll();//所有PCCID使用标志		
			setAttr("popDimPccIdUseFlags", popDimPccIdUseFlags);
			setAttr("opt", opt);
		}
		render("pccidmanage/pccidmanageCreate.jsp");
	}
	
	/**
	 * 	删除pccid
	 */
	public void delete() {
		this.initAttributes();
		String pccid_id = getPara("pccid_id");
		Map<String, Object> result = Maps.newHashMap();
		try {
			List<String> sqlList = Lists.newArrayList();
			sqlList.add("delete from POP_DIM_PCC_ID where id='" + pccid_id + "'");
			Db.batch(sqlList, sqlList.size());
			PopDimPccId.dao().deleteById(pccid_id);//删除自身
			result.put("success", "1");
			//记录日志　
			String desc = String.format("删除PCCID:%s",pccid_id);
			LogOperateUtil.log(LogOperateUtil.PCCID_DEL,desc,this.getRequest());
		} catch (Exception e) {
			result.put("success", "2");
			result.put("msg", e.getMessage());
			log.error("删除pccid发生异常", e);
			//记录日志　
			String desc = String.format("删除PCCID失败:%s",pccid_id);
			LogOperateUtil.log(LogOperateUtil.PCCID_DEL,desc,this.getRequest());
		}
		renderJson(result);
	}
	
	/**
	 * 预编辑基本信息或查看
	 */
	public void edit() {
		this.initAttributes();
		String id = this.getPara("id");
		String opt = "edit";
		PopDimPccId model = PopDimPccId.dao().findById(id);
		List<PopDimPccIdUseFlag> popDimPccIdUseFlags = PopDimPccIdUseFlag.dao().findAll();//所有PCCID使用标志		
		setAttr("popDimPccIdUseFlags", popDimPccIdUseFlags);
		setAttr("model", model);
		setAttr("opt", opt);
		render("pccidmanage/pccidmanageCreate.jsp");
	}
	
	/**
	 * 新建或修改PCCID基本信息
	 */
	public void saveBaseInfo() {
		this.initAttributes();
		String id = this.getPara("pccid_id");
		String pccid_id = this.getPara("popDimPccId.id");
		String pccname =this.getPara("popDimPccId.pccname");
		String pccid_remark = this.getPara("popDimPccId.remark");
		HashMap<Object, Object> result = Maps.newHashMap();
		try {
			if (StringUtil.isEmpty(id)) {//新建或从模板新建
				PopDimPccId PopDimPccIdInfo = PopDimPccId.dao().findById(pccid_id);
				if(PopDimPccIdInfo!=null){
					result.put("success", 2);
					result.put("id", pccid_id);
					result.put("msg", "已经存在，不允许新增重复的PCCID");
				}else{
				String newId = PopUtil.generateId();
				PopDimPccId popDimPccId = this.getModel(PopDimPccId.class);
				popDimPccId.set(PopDimPccId.id, pccid_id)
						.set(PopDimPccId.pccname, pccname)
						.set(PopDimPccId.use_flag,0)
						.set(PopDimPccId.remark, pccid_remark);
				popDimPccId.save();
				log.debug("pccid创建:{}", popDimPccId.toString());

				result.put("success", 1);
				result.put("id", newId);
				//记录日志
				String desc = popDimPccId.toString();
				LogOperateUtil.log(LogOperateUtil.PCCID_ADD,desc,this.getRequest());
				}
			} else {//修改
				PopDimPccId popDimPccId = PopDimPccId.dao().findById(id);
				PopDimPccId _popDimPccId = this.getModel(PopDimPccId.class);//表单提交要修改的信息
				String oldDesc = popDimPccId.toString();
				//ID相等，不修改ID
				if(popDimPccId.get(PopDimPccId.id).equals(_popDimPccId.get(PopDimPccId.id))){
					popDimPccId.set(PopDimPccId.id,
							_popDimPccId.get(PopDimPccId.id))
					.set(PopDimPccId.pccname, 
							_popDimPccId.get(PopDimPccId.pccname))
					.set(PopDimPccId.use_flag,
							_popDimPccId.get(PopDimPccId.use_flag))
					.set(PopDimPccId.remark,
							_popDimPccId.get(PopDimPccId.remark)).update();
					log.debug("修改策略配置基本信息:{}", popDimPccId.toString());
					result.put("success", 1);
					result.put("id", popDimPccId.get(PopDimPccId.id));
					//记录日志
					String desc = popDimPccId.toString();
					desc = String.format("修改前的数据:%s\n,修改后的数据:%s",oldDesc,desc);
					LogOperateUtil.log(LogOperateUtil.PCCID_UPDATE,desc,this.getRequest());
				}else{
					//查询要修改的ID（新的ID）是否存在
					PopDimPccId popDimPccIdTest = PopDimPccId.dao().findById(_popDimPccId.get(PopDimPccId.id));
					if(popDimPccIdTest!=null&&!"".equals(popDimPccIdTest.get(PopDimPccId.id))){
						result.put("success", 2);
						result.put("id", pccid_id);
						result.put("msg", "已经存在，不允许新增重复的PCCID");
					}else{
					//ID不等，需要修改ID,删除库中旧pccid记录，新增新的记录
					PopDimPccId.dao().deleteById(id);
					popDimPccId.set(PopDimPccId.id,
							_popDimPccId.get(PopDimPccId.id))
					.set(PopDimPccId.pccname,
							_popDimPccId.get(PopDimPccId.pccname))
					.set(PopDimPccId.use_flag,
							_popDimPccId.get(PopDimPccId.use_flag))
					.set(PopDimPccId.remark,
							_popDimPccId.get(PopDimPccId.remark)).save();
					
					log.debug("修改策略配置基本信息:{}", popDimPccId.toString());
					result.put("success", 1);
					result.put("id", popDimPccId.get(PopDimPccId.id));
					//记录日志
					String desc = popDimPccId.toString();
					desc = String.format("修改前的数据:%s\n,修改后的数据:%s",oldDesc,desc);
					LogOperateUtil.log(LogOperateUtil.PCCID_UPDATE,desc,this.getRequest());
					}
				}
			
				}
			
		} catch (Exception e) {
			log.error("保存策略配置基本信息异常:" + e.getMessage(), e);
			result.put("success", 2);
			result.put("msg", e.getMessage());
			//记录日志
			String desc = String.format("修改PCCID失败");
			LogOperateUtil.log(LogOperateUtil.PCCID_UPDATE,desc,this.getRequest());
		}

		renderJson(result);
	}
	
	public String htmlEncode0(Object str){
		if (str==null) return null;
		String s = str.toString();
		if (s.length() == 0) return "";
		s = s.replaceAll("&", "&amp;");
        s = s.replaceAll("<", "&lt;");
        s = s.replaceAll(">", "&gt;");
        s = s.replaceAll(" ", "&nbsp;");
        s = s.replaceAll("'", "&#39;");
        s = s.replaceAll("\"", "&quot;");
        s = s.replaceAll("\n", "<br>");
		return s;
	}
	
}
