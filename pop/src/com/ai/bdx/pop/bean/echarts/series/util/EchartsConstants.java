package com.ai.bdx.pop.bean.echarts.series.util;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class EchartsConstants {
	
	public static final class ECHARTS_CHARTTYPE {
		public static final String LINE = "Line";//折线图
		public static final String AREA = "Area2D";//折线面积图
		public static final String BAR = "Column2D";//柱图
		public static final String HORIZONTAL_BAR = "Bar2D";//水平柱图
		public static final String SCATTER = "scatter";//
		public static final String K = "k";//
		public static final String PIE_FULL = "Pie2D";//饼图
		public static final String PIE_EMPTY = "Doughnut2D";//圆环图
		public static final String RADAR = "radar";//雷达图
		public static final String MAP_CHINA = "MapChina";//中国地图
		public static final String MAP_BJ = "MapBeiJing";//北京地图
		public static final String CHORD = "chord";//
		public static final String FORCE = "force";//
		public static final String GAUGE = "Gauges2D";//仪表盘
		public static final String FUNNEL = "funnel";//
		public static final String TORNADO = "tornado";
		public static final String BRAIN = "brain";
		public static final String SUBTEXT = "subtext";
		
		public static final List<String> STACK_TYPES = new ArrayList<String>();
		
		static {
			STACK_TYPES.add("StackedColumn2DLineDY");//双y轴堆积
			STACK_TYPES.add("MSStackedColumn2DLineDY");//双y轴多系列堆积
			STACK_TYPES.add("StackedColumn2D");//单y轴堆积
			STACK_TYPES.add("MSCombiDY2D");//双y轴多系列
		}
		
	}
	
	public static final class ECHARTS_BASE_CHARTTYPE {
		public static final String LINE = "line";
		public static final String BAR = "bar";
		public static final String SCATTER = "scatter";
		public static final String K = "k";
		public static final String PIE = "pie";
		public static final String RADAR = "radar";
		public static final String MAP = "map";
		public static final String CHORD = "chord";
		public static final String FORCE = "force";
		public static final String GAUGE = "gauge";
		public static final String FUNNEL = "funnel";
	}
	
	public static final class ECHARTS_INDEX_Y {
		public static final String INDEX_0 = "P";
		public static final String INDEX_1 = "S";
	}
	
	public static final class ECHARTS_COLORS {
		public static final String FONT_COLOR = "white";
		public static final String LINE_DASHED = "dashed";
	}
	
	public static final class ECHARTS_LEGEND {
		public static final String TIME_LINE = "时间进度";
		public static final String GUARD_LINE = "警戒线";
	}
	
	public static final int DEFAULT_MARKPOINT_SYMBOLSIZE = 30;
	public static final int DEFAULT_LINE_SYMBOLSIZE = 5;
	public static final String DEFAULT_LINE_SYMBOL = "circle";
	public static final int DEFAULT_FONT_SIZE = 30;
	public static final String DEFAULT_NULL_4_LINE = "-";
	
	public static final String OUT_OPTION = "option";
	public static final String OUT_GRID = "datagrid";
	public static final String TABLE_GRID_PREFIX = "tab__";
	public static final String DEFAULT_CONCAT = "|";
	public static final String CHART_TYPE_4_GRID = "datagrid";
	public static final String DEFAULT_ADDRESS = "http://www.asiainfo.com.cn/index.html";
	
	public static final Map<String, String> DIV_IDS = new HashMap<String, String>();
	public static final List<String> FOWARD_DATA_IDS = new ArrayList<String>();
	public static final List<String> FOWARD_CHART_IDS = new ArrayList<String>();
	
	static {
		DIV_IDS.put("tab__index_2_pc", "tab__index_2");
		FOWARD_DATA_IDS.add("PARSE_4_PC_PURCHASE_TOP30__");
		FOWARD_DATA_IDS.add("PARSE_4_PC_PURCHASE_TOP10__");
		FOWARD_CHART_IDS.add("purchase_top_30");
		FOWARD_CHART_IDS.add("purchase_top_10");
	}
	
	public static final class BRAIN_CONST {
		public static final String TOTAL = "node_1";
		public static final String HEALTH_FINANCE = "node_1-1";
		public static final String HEALTH_BUSINESS = "node_1-2";
		public static final String MARKET_POSITION = "node_1-3";
		public static final String GROSS_MARGIN = "node_1-1-1";
		public static final String TURNOVER_RATE = "node_1-1-2";
		public static final String RECEIVABLE_PERCENT = "node_1-1-3";
		public static final String DISBURSEMENT_PERCENT = "node_1-1-4";
		public static final String SALE_PRICE_RATE = "node_1-2-1";//销售价格与市场水平比
		public static final String HEADQUARTERS_SALE_RATE = "node_1-2-2";
		public static final String SOCIAL_CHANNEL_SALE_RATE = "node_1-2-3";
		public static final String RETAIL_RATE = "node_1-2-4";
		public static final String TD_TERMINAL_SALES_MARKET_SHARE = "node_1-3-1";
		public static final String DEFAULT_CONCAT = "-";
		
		public static final String RISE = "rise";
		public static final String DECLINE = "decline";
		public static final String REDUCE = "reduce";
		public static final String KEEP = "keep";
	}
	
	public static final class LIGHT_COLORS {
		public static final String GREEN = "green";//正常
		public static final String YELLOW = "yellow";//低告警
		public static final String RED = "red";//高告警
		public static final String GREEN_YELLOW = "green,yellow";
		public static final String GREEN_RED = "green,red";
		public static final String YELLOW_GREEN = "yellow,green";
		public static final String YELLOW_RED = "yellow,red";
		public static final String RED_GREEN = "red,green";
		public static final String RED_YELLOW = "red,yellow";
		
		public static final String[] colors = { "c774b0", "8f7cc1", "e1ad6c",
				"5aa5d8", "84ae6f" };
	}
	
	public static final class LINK_MORE {
		public static final String PATH = "./";
		public static final String COMPANY_MANAGEMENT = PATH + "company-management.html";
		public static final String SALES = PATH + "sales.html";
		public static final String STOCK = PATH + "stock.html";
		public static final String INCOME = PATH + "revenues.html";
		public static final String CHANNEL = PATH + "channel.html";
		public static final String HEALTH_DEGREE = PATH + "health-degree.html";
		public static final String PRODUCT = PATH + "product.html";
	}
	
	public static final class GridSelSite {
		
		public static Map<Integer, String> GRID_TYPES_MAP = new LinkedHashMap<Integer, String>();
		
		static {
			GRID_TYPES_MAP.put(0, "整体");
			GRID_TYPES_MAP.put(1, "终端");
			GRID_TYPES_MAP.put(2, "号卡");
			GRID_TYPES_MAP.put(3, "套餐");
			GRID_TYPES_MAP.put(4, "资源");
		}
		
		public static final Integer PAGE_SIZE = 15;
		
		public static final String GRID_COLOR_RED = "red";
		public static final String GRID_COLOR_GREEN = "green";
		public static final String GRID_COLOR_BLUE = "blue";
		
		public static final Integer GRID_TYPE_0 = 0;//整体
		public static final Integer GRID_TYPE_1 = 1;//终端
		public static final Integer GRID_TYPE_2 = 2;//号卡
		public static final Integer GRID_TYPE_3 = 3;//套餐
		public static final Integer GRID_TYPE_4 = 4;//资源
		
		public static final Integer KPI_VALUE_TYPE_0 = 0;//潜力
		public static final Integer KPI_VALUE_TYPE_1 = 1;//能力
		
		public static final String SLIDER_BAR_SELECTED_START = "2011-11";
		public static final String SLIDER_BAR_SELECTED_END = "2012-06";
	}
	
}
