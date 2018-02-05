package com.ai.bdx.pop.controller;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ai.bdx.frame.privilegeServiceExt.service.IUserPrivilegeCommonService;
import com.ai.bdx.pop.base.PopController;
import com.ai.bdx.pop.bean.PopPolicyCustGroupBean;
import com.ai.bdx.pop.model.PopDimPccId;
import com.ai.bdx.pop.util.PopUtil;
import com.ai.bdx.pop.wsclient.impl.PopCmCustomersWsClientImpl;
import com.asiainfo.biframe.privilege.ICity;
import com.asiainfo.biframe.utils.spring.SystemServiceLocator;
import com.asiainfo.biframe.utils.string.StringUtil;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

public class SearchController extends PopController {
	private static final Logger log = LogManager.getLogger();

	public final static int INCREASE_NUM = 15;
	private static List<Map> onoffline = Lists.newArrayList();//上下线
	private static List<Map> custGroupList = Lists.newArrayList();//客户群列表
	private static List<Map> pccIdList = Lists.newArrayList();//pccid列表
	private static List<Map> cityPriorityList = new CopyOnWriteArrayList<Map>();//地市优先级列表
	
	static {
		Map<String, Object> mapUp = Maps.newHashMap();
		mapUp.put("id", "1");
		mapUp.put("name", "上线");
		Map<String, Object> mapD = Maps.newHashMap();
		mapD.put("id", "2");
		mapD.put("name", "下线");
		onoffline.add(mapUp);
		onoffline.add(mapD);
	}

	public List<Map> getCustGroupList() {
		PopCmCustomersWsClientImpl popCmCustomersWsClientImpl = new PopCmCustomersWsClientImpl();
		String strJson = "";
		try {
			strJson = popCmCustomersWsClientImpl.getCustomersList("popadmin");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Gson gson = new Gson();
		//创建一个JsonParser
		JsonParser parser = new JsonParser();
		//通过JsonParser对象可以把json格式的字符串解析成一个JsonElement对象
		JsonElement jsonElement = parser.parse(strJson);
		JsonArray jsonArray = jsonElement.getAsJsonArray();
		Iterator it = jsonArray.iterator();
		while (it.hasNext()) {
			PopPolicyCustGroupBean PopPolicyBean = gson.fromJson(it.next().toString(), PopPolicyCustGroupBean.class);
			Map<String, Object> map = Maps.newHashMap();
			map.put("id", PopPolicyBean.getTarget_customers_id());
			map.put("name", PopPolicyBean.getTarget_customers_name());
			custGroupList.add(map);
		}

		return custGroupList;

	}

	public void index() {

	}

	/**
	 * 列表值搜索
	 */
	public void listSearch() {
		String title = getPara("title"); //搜索内容
		String table = getPara("table");//搜索表名
		String id = Strings.isNullOrEmpty(getPara("id")) ? "id" : getPara("id"); // 对应表的id值
		String name = Strings.isNullOrEmpty(getPara("name")) ? "name" : getPara("name");//展示，以及匹配的字段listSearch
		String cond = getPara("cond");//过滤条件

		StringBuffer sql = new StringBuffer();
		sql.append("select ").append(id).append(" as id  ,").append(name).append(" as name ");
		sql.append(" from ").append(table);
		sql.append(" where ").append(name).append(" like '%").append(title).append("%' ");
		if (!Strings.isNullOrEmpty(cond)) {
			sql.append(" and ").append(cond);
		}
		sql.append(" order by ").append(id).append(" desc");
		List<Record> result = Db.find(sql.toString());
		renderJson(PopUtil.convertRecordList2JSONString(result));
	}

	/**
	 * 常量值搜索
	 */
	public void cacheSearch() {
		String title = getPara("title"); //搜索内容
		String cache = getPara("cache");//搜索表名
		List<Map> list = Lists.newArrayList();
		if ("onoffline".equalsIgnoreCase(cache)) {
			if (!Strings.isNullOrEmpty(title)) {
				for (Map m : onoffline) {
					if (String.valueOf(m.get("name")).contains(title)) {
						list.add(m);
					}
				}
			} else {
				list = onoffline;
			}

		} else if ("custGroup".equalsIgnoreCase(cache)) {
			log.debug(custGroupList);
			if (custGroupList != null && custGroupList.size() > 0) {
			} else {
				getCustGroupList();
			}

			if (!Strings.isNullOrEmpty(title)) {
				for (Map m : custGroupList) {
					if (m!=null&&String.valueOf(m.get("name")).contains(title)) {
						list.add(m);
					}
				}
			} else {
				list = custGroupList;
			}

		}
		//add by jinl 20150624去重
		List<Map> pccIdListTemp = Lists.newArrayList();
		if (!list.isEmpty()) {
			for(Map m:list){
				if(!pccIdListTemp.contains(m)&&m!=null){
					pccIdListTemp.add(m);
				}
			}
		}
		renderJson(new Gson().toJson(pccIdListTemp));
	}

	public void addNodes() {
		String title = getPara("title"); //搜索内容
		String table = getPara("table");//搜索表名
		String id = Strings.isNullOrEmpty(getPara("id")) ? "id" : getPara("id"); // 对应表的id值
		String name = Strings.isNullOrEmpty(getPara("name")) ? "name" : getPara("name");//展示，以及匹配的字段listSearch
		String cond = getPara("cond");//过滤条件
		String custom_sql = getPara("custom_sql");//自定义sql
		int page_size = Strings.isNullOrEmpty(getPara("page_size")) ? INCREASE_NUM : Integer
				.valueOf(getPara("page_size")); //页大小

		int num = Strings.isNullOrEmpty(getPara("num")) ? 0 : Integer.valueOf(getPara("num")); //查询次数
		StringBuffer sql = new StringBuffer();
		if (StringUtil.isEmpty(custom_sql)) {
			sql.append("select ").append(id).append(" as id  ,").append(name).append(" as name ");
			sql.append(" from ").append(table);
			sql.append(" where ").append(name).append(" like '%").append(title).append("%' ");
			if (!Strings.isNullOrEmpty(cond)) {
				sql.append(" and ").append(cond);
			}
			sql.append(" order by ").append(id).append(" desc");
		} else {
			sql.append(custom_sql);
		}
		int split = sql.indexOf("from");
		String select = sql.substring(0, split);
		String sqlExceptSelect = sql.substring(split);
		Page<Record> returnList = Db.paginate(num + 1, page_size, select, sqlExceptSelect);
		renderJson(PopUtil.convertRecordList2JSONString(returnList.getList()));
	}

	/**
	 * 查询 PccId 
	 *  add by jinl 20150609
	 * @return
	 */
	public List<Map> getpccIdList() {
		pccIdList = Lists.newArrayList();
		StringBuffer sql = new StringBuffer();
		sql.append("select id, use_flag from POP_DIM_PCC_ID order by id ");
		List<Record> dimPccIdList= Db.find(sql.toString());
		for (Integer i = 0; i < dimPccIdList.size(); i++) {
			Integer flag = dimPccIdList.get(i).get(PopDimPccId.use_flag);//按是否启用标志过滤，需求变更，不过滤
				Map<String, Object> map = Maps.newTreeMap();
				map.put("id", dimPccIdList.get(i).get(PopDimPccId.id));
				map.put("name", dimPccIdList.get(i).get(PopDimPccId.id));
				pccIdList.add(map);
		}
		return pccIdList;
	}

	/**
	 * add by jinl 20150609
	 * pccid常量值搜索
	 */
	public void pccidSearch() {
		String title = getPara("title"); //搜索内容
		String cache = getPara("cache");//搜索表名
		List<Map> list = Lists.newArrayList();
		List<Map> pccIdListTemp = Lists.newArrayList();
		List<Map> pccIdListOver = Lists.newArrayList();
		if ("pccId".equalsIgnoreCase(cache)) {
			pccIdListTemp = getpccIdList();
			if (!pccIdListTemp.isEmpty()) {
				//set过滤
//				HashSet hs = new HashSet<Map>(pccIdListTemp);
//				pccIdListOver.clear();
//				pccIdListOver.addAll(hs);
				//过滤
				for(Map m:pccIdListTemp){
					if(!pccIdListOver.contains(m)){
						pccIdListOver.add(m);
					}
				}
				
			}
			log.debug(pccIdListOver);
			if (!Strings.isNullOrEmpty(title)) {
				for (Map m : pccIdListOver) {
					if (String.valueOf(m.get("name")).contains(title)) {
						list.add(m);
					}
				}
			} else {
				list = pccIdListOver;
			}

		}
		renderJson(new Gson().toJson(list));
	}
	
	
	/**
	 * 查询 地市优先级  
	 *  add by jinl 20150609
	 * @return
	 * @throws Exception 
	 */
	public List<Map> getcityPriorityList() throws Exception {
		cityPriorityList = new CopyOnWriteArrayList<Map>();//修正防止抛 java.util.ConcurrentModificationException
		IUserPrivilegeCommonService service = (IUserPrivilegeCommonService) SystemServiceLocator.getInstance().getService(PRIVILEGE_SERVICE_NAME);
		List<ICity> list = service.getAllCity();
		for (Integer i = 0; i < list.size(); i++) {
			Map<String, Object> map = Maps.newHashMap();
			map.put("id", list.get(i).getCityId());
			map.put("name", list.get(i).getCityName());
			cityPriorityList.add(map);
		}
		return cityPriorityList;
	}

	/**
	 * add by jinl 20150702
	 * 地市优先级 常量值搜索
	 * @throws Exception 
	 */
	public void cityPrioritySearch() throws Exception {
		String title = getPara("title"); //搜索内容
		String cityPriorityCache = getPara("cache");//搜索表名
		List<Map> list = Lists.newArrayList();
		List<Map> cityPriorityListTemp = new CopyOnWriteArrayList<Map>();
		List<Map> cityPriorityListTemp2 = Lists.newArrayList();
		List<Map> cityPriorityListOver = Lists.newArrayList();
		if ("city_priority".equalsIgnoreCase(cityPriorityCache)) {
			//查询 地市优先级
			cityPriorityListTemp = getcityPriorityList();
			//cityPriorityListTemp = cityPriorityListTemp2;
			if (!cityPriorityListTemp.isEmpty()) {
				//过滤
				for(Map m:cityPriorityListTemp){
					if(!cityPriorityListOver.contains(m)){
						cityPriorityListOver.add(m);
					}
				}
				
			}
			log.debug(cityPriorityListOver);
			if (!Strings.isNullOrEmpty(title)) {
				for (Map m : cityPriorityListOver) {
					if (String.valueOf(m.get("name")).contains(title)) {
						list.add(m);
					}
				}
			} else {
				list = cityPriorityListOver;
			}

		}
		renderJson(new Gson().toJson(list));
	}


}
