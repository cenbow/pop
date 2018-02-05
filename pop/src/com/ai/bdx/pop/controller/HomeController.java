package com.ai.bdx.pop.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.cxf.common.util.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ai.bdx.frame.approval.service.IApprovalService;
import com.ai.bdx.frame.privilegeServiceExt.service.IUserPrivilegeCommonService;
import com.ai.bdx.pop.base.PopController;
import com.ai.bdx.pop.bean.ScheduleOnHome;
import com.ai.bdx.pop.bean.echarts.Option;
import com.ai.bdx.pop.bean.echarts.axis.Axis;
import com.ai.bdx.pop.bean.echarts.axis.Legend;
import com.ai.bdx.pop.bean.echarts.series.LineSeries;
import com.ai.bdx.pop.bean.echarts.series.util.EchartsConstants;
import com.ai.bdx.pop.enums.InfoType;
import com.ai.bdx.pop.model.PopDimPolicyLevel;
import com.ai.bdx.pop.model.PopDimPolicyStatus;
import com.ai.bdx.pop.model.PopDimPolicyType;
import com.ai.bdx.pop.model.PopPolicyBaseinfo;
import com.ai.bdx.pop.model.PopSystemInfo;
import com.ai.bdx.pop.util.ApprovalCONST;
import com.ai.bdx.pop.util.JsonUtil;
import com.ai.bdx.pop.util.PopConstant;
import com.ai.bdx.pop.util.SimpleCache;
import com.asiainfo.biframe.privilege.ICity;
import com.asiainfo.biframe.utils.spring.SystemServiceLocator;
import com.asiainfo.biframe.utils.string.StringUtil;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

/**
 * 首页Controller
 * 
 * @author zhangyb5
 * 
 */
public class HomeController extends PopController {
	private static final Logger log = LogManager.getLogger();

	private static final String SQL_4_COUNT_PREFIX = "select count(*) _count, ";

	private static final String SQL_4__COUNT_GROUP_IN_DATE = "str_to_date(t.create_time, '%Y-%m-%d') tt FROM pop_policy_baseinfo t GROUP BY tt ORDER BY tt";

	private static final String SQL_4__COUNT_GROUP_IN_MONTH = "str_to_date(t.create_time, '%Y-%m') tt FROM pop_policy_baseinfo t GROUP BY tt ORDER BY tt";

	private static final String SQL_4__COUNT_GROUP_IN_SNENE_TYPE = "t.policy_type_id tt FROM pop_policy_baseinfo t GROUP BY tt ORDER BY tt";

	private static final String SQL_4__COUNT_GROUP_IN_CITY = "t.city_id tt FROM pop_policy_baseinfo t GROUP BY tt ORDER BY tt";

	private static final Integer PAGE_SIZE = 5;
	
	private static final String CACHE_KEY_4_CITY_ID_NAME_MAP = "city_id_name_map_";
	
	private static final String PAGE_KEY = "page";

	public void index() {
		render("popMain.jsp");
	}
	
	public void toHome() {
		log.info("主页面调用...");
		
		initAttributes();
		
		initHomeInfo();

		render("home.jsp");
	}

	public void queryChart() {
		log.info("首页图表数据请求...");
		try {
			int typeId = Integer.parseInt(getPara("chart_2_type_id"));
			Option option = createOption(typeId);
			renderJson(JsonUtil.obj2Json(option));
		} catch (Exception e) {
			e.printStackTrace();
			renderJson(JsonUtil.obj2Json(new Option()));
		}
	}
	
	public void show() {
		try {
			initAttributes();
			int page = getParaToInt(PAGE_KEY, 1);
			
			List<String> policyIds = this.getPolicyIds();
			Map<String, String> operate_names = this.getOperators();
			
			if (policyIds.isEmpty()) {
				setAttr("vegetables", null);
			} else {
				String select = "select t.* ";
				String sqlExceptSelect = " from pop_policy_baseinfo t where t.policy_status_id in (20,30,31,32,40,41,42) and t.id in (" + org.apache.commons.lang.StringUtils.join(policyIds.toArray(), ",") + ") order by t.create_time desc";
				Page<PopPolicyBaseinfo> list = PopPolicyBaseinfo.dao().paginate(page, 10, select, sqlExceptSelect);
				
				List<ScheduleOnHome> rst = new ArrayList<ScheduleOnHome>();
				for (PopPolicyBaseinfo ppbi : list.getList()) {
					ScheduleOnHome soh = new ScheduleOnHome();
					soh.setSid(ppbi.get(PopPolicyBaseinfo.COL_ID).toString());
					soh.setPolicyName(ppbi.get(PopPolicyBaseinfo.COL_POLICY_NAME).toString());
					soh.setCreateDate(ppbi.get(PopPolicyBaseinfo.COL_CREATE_TIME));
					soh.setCreator(ppbi.get(PopPolicyBaseinfo.COL_CREATE_USER_ID).toString());
					soh.setOperation(operate_names.get(ppbi.get(PopPolicyBaseinfo.COL_POLICY_STATUS_ID).toString()));
					rst.add(soh);
				}
				setAttr("bananacountries", rst);
				setAttr("vegetables", list);
			}
		} catch (Exception e) {
			e.printStackTrace();
			setAttr("vegetables", new ArrayList<ScheduleOnHome>());
		}
		render("supermarket.jsp");
	}
	
	public void findSystemInfoPage() {
		initAttributes();
		int page = getParaToInt(PAGE_KEY, 1);
		
		String select = "select t.* ";
		
		StringBuffer sqlExceptSelect = new StringBuffer("from pop_system_info t where t.user_id = ? and t.info_type in (?, ?) order by t.create_time desc");
//		StringBuffer sqlExceptSelect = new StringBuffer("from pop_system_info t where 1 = 1 ");
		
		Page<PopSystemInfo> list = PopSystemInfo.dao().paginate(page, 10, select, sqlExceptSelect.toString(), new Object[] {this.userId, InfoType.NOTICE.getValue(), InfoType.WARN.getValue()});
		setAttr("pageList", list);
		render("manage/sysInfoList.jsp");
	}

	private Option createOption(int typeId) {
		String _sql = SQL_4_COUNT_PREFIX;
		switch (typeId) {
		case 1:// 日期
			_sql += SQL_4__COUNT_GROUP_IN_DATE;
			break;
		case 2:// 月份
			_sql += SQL_4__COUNT_GROUP_IN_MONTH;
			break;
		case 3:// 场景类型
			_sql += SQL_4__COUNT_GROUP_IN_SNENE_TYPE;
			break;
		case 4:// 地市
			_sql += SQL_4__COUNT_GROUP_IN_CITY;
			break;
		}
		List<Record> result = Db.find(_sql);
		if (result.isEmpty()) {
			return new Option();
		}
		initDimInfo();
		return parse(typeId, result);
	}
	
	private void initHomeInfo() {
//		initDimInfo();
		List<ScheduleOnHome> scdList = new ArrayList<ScheduleOnHome>();
		try {
			scdList = createPage();
		} catch (Exception e) {
			e.printStackTrace();
			scdList = new ArrayList<ScheduleOnHome>();
		}
		List<PopSystemInfo> sysList;
		try {
			String _sql = "select t.* ";
			
			String sqlExceptSelect = "from pop_system_info t where t.user_id = ? and t.info_type in (?, ?) order by t.create_time desc";
			
			sysList = PopSystemInfo.dao().paginate(1, PAGE_SIZE, _sql, sqlExceptSelect, new Object[] { this.userId, InfoType.NOTICE.getValue(), InfoType.WARN.getValue() }).getList();
		} catch (Exception e) {
			e.printStackTrace();
			sysList = new ArrayList<PopSystemInfo>();
		}
		
		List<PopPolicyBaseinfo> policyList;
		try {
			String _sql = "select *";
			String sqlExceptSelect = " from pop_policy_baseinfo t where t.create_user_id = ? order by t.create_time desc";
			policyList = PopPolicyBaseinfo.dao().paginate(1, PAGE_SIZE, _sql, sqlExceptSelect, new Object[] { this.userId }).getList();
		} catch (Exception e) {
			e.printStackTrace();
			policyList = new ArrayList<PopPolicyBaseinfo>();
		}
		
		initDimInfo();
		escape(policyList);
		
		setAttr("scheduleList", scdList);
		setAttr("sysInfoList", sysList);
		setAttr("policyList", policyList);
	}
	
	@SuppressWarnings("unchecked")
	private String getCityName(String cityId) throws Exception {
		if (StringUtils.isEmpty(cityId)) {
			return "";
		}
		Map<String, String> map = (Map<String, String>) SimpleCache.getInstance().get(CACHE_KEY_4_CITY_ID_NAME_MAP);
		if (map == null || map.isEmpty()) {
			map = getMap();
		}
		return map.get(cityId);
	}

	private Map<String, String> getMap() throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		IUserPrivilegeCommonService service = (IUserPrivilegeCommonService) SystemServiceLocator.getInstance().getService(PRIVILEGE_SERVICE_NAME);
		List<ICity> list = service.getAllCity();
		
		for (Iterator<ICity> ite = list.iterator(); ite.hasNext(); ) {
			ICity city = ite.next();
			if ((StringUtil.isEmpty(city.getDmCountyId()) || "-1".equals(city.getDmCountyId()))
						&& (StringUtil.isEmpty(city.getDmDeptId()) || "-1".equals(city.getDmDeptId()))) {
				map.put(city.getCityId(), city.getCityName());
			}
		}
		SimpleCache.getInstance().put(CACHE_KEY_4_CITY_ID_NAME_MAP, map, PopConstant.CACHE_TIME * 2);
		return map;
	}

	/**
	 * 
	 * @return
	 * @throws Exception 
	 */
	private List<ScheduleOnHome> createPage() throws Exception {
		Map<String, String> operate_names = this.getOperators();
		List<ScheduleOnHome> list = new ArrayList<ScheduleOnHome>();
		List<String> policyIds = this.getPolicyIds();
		
		if (policyIds.isEmpty()) {
			return new ArrayList<ScheduleOnHome>();
		}
		
		String _sql = "select t.*";
		
//		String sqlExceptSelect = " from pop_policy_baseinfo t where t.policy_status_id in (20,30,31,32,40,41,42,50) and t.id in (" + org.apache.commons.lang.StringUtils.join(policyIds.toArray(), ",") + ") order by t.create_time desc";
		String sqlExceptSelect = " from pop_policy_baseinfo t where t.policy_status_id in (20,30,31,32,40,41,42) and t.id in (" + org.apache.commons.lang.StringUtils.join(policyIds.toArray(), ",") + ") order by t.create_time desc";
		
		List<PopPolicyBaseinfo> ppbiList = PopPolicyBaseinfo.dao().paginate(1, PAGE_SIZE, _sql, sqlExceptSelect).getList();
		if (ppbiList.isEmpty()) {
			return new ArrayList<ScheduleOnHome>();
		}
		for (PopPolicyBaseinfo ppbi : ppbiList) {
			ScheduleOnHome soh = new ScheduleOnHome();
			soh.setSid(ppbi.get(PopPolicyBaseinfo.COL_ID).toString());
			soh.setPolicyName(ppbi.get(PopPolicyBaseinfo.COL_POLICY_NAME).toString());
			soh.setCreateDate(ppbi.get(PopPolicyBaseinfo.COL_CREATE_TIME));
			soh.setCreator(ppbi.get(PopPolicyBaseinfo.COL_CREATE_USER_ID).toString());
			soh.setOperation(operate_names.get(ppbi.get(PopPolicyBaseinfo.COL_POLICY_STATUS_ID).toString()));
			list.add(soh);
		}
		return list;
	}
	
	public Map<String, String> getOperators() {
		Map<String, String> operate_names = new HashMap<String, String>();
		operate_names.put("20", "调整");
		operate_names.put("30", "审批");
		operate_names.put("31", "审批");
		operate_names.put("32", "审批");
		operate_names.put("40", "确认");
		operate_names.put("41", "确认");
		operate_names.put("42", "确认");
		return operate_names;
	}
	
	public List<String> getPolicyIds() throws Exception {
		List<String> policyIds = new ArrayList<String>();
		IApprovalService service = (IApprovalService) SystemServiceLocator.getInstance().getService(ApprovalCONST.APPROVE_SERVICE );
		@SuppressWarnings("unchecked")
		List<Object> records = service.getApprovalIdByUser(this.userId, "");
		for (Iterator<Object> ite = records.iterator(); ite.hasNext(); ) {
			policyIds.add(String.valueOf(ite.next()));
		}
		return policyIds;
	}

	@SuppressWarnings("unchecked")
	private Option parse(int typeId, List<Record> result) {
		Option option = new Option();

		Legend legend = new Legend();
		List<Object> data = new ArrayList<Object>();
		try {
			data.add("场景数");
			legend.setData(data);
			option.setLegend(legend);

			List<Object> xData = new ArrayList<Object>();
			Axis xAxis = new Axis();
			for (Record record : result) {
				Object obj = record.get("tt");
				String x = obj == null ? "" : obj.toString();
				switch (typeId) {
				case 2:
					String sep = "-";
					if (x.indexOf(sep) != -1 && x.lastIndexOf(sep) > 6 && x.split(sep).length == 3) {
						String[] xes = x.split(sep);
						xData.add(xes[0] + sep + xes[1]);
					} else {
						xData.add(x);
					}
					break;
				case 3:
					String sql4Type = "select * from pop_dim_policy_type t where t.id = " + x;
					xData.add(PopDimPolicyType.dao().find(sql4Type).get(0).getStr(PopDimPolicyType.COL_NAME));
					break;
				case 4:
					xData.add(getCityName(x));
					break;
				default:
					xData.add(x);
				}
			}
			xAxis.setData(xData);
			Axis[] xAxises = { xAxis };
			option.setxAxis(xAxises);

			Axis yAxis = new Axis();
			yAxis.setName("个(单位)");
			Axis[] yAxises = { yAxis };
			option.setyAxis(yAxises);

			List<LineSeries> serieses = new ArrayList<LineSeries>();
			LineSeries series = new LineSeries();
			series.setName("场景数");
			series.setType(EchartsConstants.ECHARTS_BASE_CHARTTYPE.BAR);
			List<Object> sData = new ArrayList<Object>();
			for (Record record : result) {
				sData.add(record.get("_count"));
			}
			series.setData(sData);
			serieses.add(series);
			option.setSeries(serieses);
			return option;
		} catch (Exception e) {
			e.printStackTrace();
			return new Option();
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

	private void escape(List<PopPolicyBaseinfo> list) {
		if (list.isEmpty()) {
			return;
		}
		for (PopPolicyBaseinfo ppbinfo : list) {
			ppbinfo.set(PopPolicyBaseinfo.COL_POLICY_TYPE_ID, PopDimPolicyType.getTypeName(ppbinfo.get(PopPolicyBaseinfo.COL_POLICY_TYPE_ID).toString()));
			ppbinfo.set(PopPolicyBaseinfo.COL_POLICY_LEVEL_ID, PopDimPolicyLevel.getTypeName(ppbinfo.get(PopPolicyBaseinfo.COL_POLICY_LEVEL_ID).toString()));
			ppbinfo.set(PopPolicyBaseinfo.COL_POLICY_STATUS_ID, PopDimPolicyStatus.getTypeName(ppbinfo.get(PopPolicyBaseinfo.COL_POLICY_STATUS_ID).toString()));
		}
	}
	
}
