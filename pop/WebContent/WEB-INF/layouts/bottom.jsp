<%@ page contentType="text/html;charset=UTF-8"%>

<!-- BEGIN JAVASCRIPTS -->
<!-- Load javascripts at bottom, this will reduce page load time -->
<script type="text/javascript" src="${ctx}/static/js/jquery-1.8.3.min.js"></script>
<script type="text/javascript" src="${ctx}/static/assets/jqueryUI/jquery-ui.js"></script> 
<script type="text/javascript" src="${ctx}/static/assets/bootstrap/js/bootstrap.min.js"></script> 
<script type="text/javascript" src="${ctx}/static/assets/bootstrap/js/bootstrap-fileupload.js"></script>
 
<!-- ie8 fixes -->
<!--[if lt IE 9]>
<script src="../js/excanvas.js"></script>
<script src="../js/respond.js"></script>
<![endif]--> 
<script type="text/javascript" src="${ctx}/static/assets/uniform/jquery.uniform.min.js"></script>
<script type="text/javascript" src="${ctx}/static/assets/bootstrap-datepicker/js/bootstrap-datepicker_zh_CN.js" charset="UTF-8"></script>   
<script type="text/javascript" src="${ctx}/static/assets/bootstrap-daterangepicker/date.js"></script>
<script type="text/javascript" src="${ctx}/static/assets/bootstrap-daterangepicker/daterangepicker.js"></script> 
<script type="text/javascript" src="${ctx}/static/assets/bootstrap-datetimepicker-master/js/bootstrap-datetimepicker.js" charset="UTF-8"></script>
<script type="text/javascript" src="${ctx}/static/assets/bootstrap-datetimepicker-master/js/locales/bootstrap-datetimepicker.zh-CN.js" charset="UTF-8"></script>
 <script type="text/javascript" src="${ctx}/static/assets/lazyscrollloading/jquery.lazyscrollloading-min.js"></script>
<script type="text/javascript" src="${ctx}/static/assets/ztree/jquery.ztree.all-3.5.js"></script>
<script type="text/javascript" src="${ctx}/static/assets/ztree/jquery.ztree.exhide-3.5.js"></script>
<script type="text/javascript" src="${ctx}/static/js/scripts.js"></script>
<script type="text/javascript" src="${ctx}/static/js/main.js" ></script>
<script type="text/javascript" src="${ctx}/static/js/json2.js" ></script>
<script src="${ctx}/static/assets/uploadify/jquery.uploadify.js" type="text/javascript"></script>
<script type="text/javascript" src="${ctx}/static/assets/treeTable/jquery.treeTable.js" ></script>
<script type="text/javascript" src="${ctx}/static/assets/jgrowl/jquery.jgrowl.min.js" ></script>
<script type="text/javascript">
$(document).ready(function() {
	// initiate layout and plugins
	App.init();
});
var map = {};
var mapTitle = {};

function setNameById(table, id, colId, colName, display) {
	$.post("${ctx}/search/getNameById", {
			"table": table,
			"id": id,
			"colId": colId,
			"colName": colName
		},
		function(name) {
			$("#" + display).val(name);
		});
}

/**
 *下拉列表搜索
 * obj   treeid
 * title 搜索关键字
 * table 搜索 表名
 * id	 对应的选项的value值，默认去表的 "id"字段
 * name  从name字段中匹配关键字，并展示该字段；默认取表的 "name"字段
 * cond  sql条件,将自动添加到where后
 **/
function listSearch(obj, title, table, id, name, cond, custom_sql) {
	$.ajax({
		type: 'POST',
		url: "${ctx}/search/listSearch",
		data: {
			"title": title,
			"table": table,
			"id": id,
			"name": name,
			"cond": cond,
			"custom_sql": custom_sql
		},
		dataType: "json",
		async: true,
		success: function(data) {
			$.fn.zTree.getZTreeObj(obj).addNodes(null, data, true);
		}
	});
}


function listSearchInit(treeId, obj, setting, title, table, id, name, cond) {
	$.ajax({
		type: 'POST',
		url: "${ctx}/search/listSearch",
		data: {
			"title": title,
			"table": table,
			"id": id,
			"name": name,
			"cond": cond
		},
		dataType: "json",
		async: true,
		success: function(data) {
			$.fn.zTree.init(obj, setting, data);
		}
	});
}


/**
 *动态加载下拉列表方法
 * @para ztreeId 加载节点的ztreeId
 * @para table  数据来源表
 * @para title 搜索关键字
 **/
function addNodes(ztreeId, table, id, name, cond) {
	//若有,从缓冲中取搜索关键字和当前页索引
	var pagenum = map[ztreeId+"_pagenum"];
	pagenum = pagenum==null?0:pagenum;
	map[ztreeId+"_pagenum"] = pagenum + 1;
	var searchkey = map[ztreeId+"_searchkey"];
	searchkey = searchkey==null?'':searchkey;
	var custom_sql = map[ztreeId+"_custom_sql"]
	custom_sql = custom_sql==null?"":custom_sql;
	var page_size = map[ztreeId+"_page_size"]
	page_size = page_size==null?"":page_size;
	$.ajax({
		type: 'POST',
		url: "${ctx}/search/addNodes",
		data: {
			"title": searchkey,
			"table": table,
			"id": id,
			"name": name,
			"cond": cond,
			"num": pagenum,
			"custom_sql":custom_sql,
			"page_size":page_size
		},
		dataType: "json",
		async: true,
		success: function(data) {
			$.fn.zTree.getZTreeObj(ztreeId).addNodes(null, data, true);
		}
	});
}

/**
 * 分页懒加载,页大小为15
 * ztreeId treeid
 * title 搜索关键字
 * table 搜索 表名
 * id	 对应的选项的value值，默认去表的 "id"字段
 * name  从name字段中匹配关键字，并展示该字段；默认取表的 "name"字段
 * cond  sql条件,将自动添加到where后
 * custom_sql  自定义sql(table，id，name，cond都将无效) 例如：select name as id, name from (select DISTINCT brand_name as name from v_dim_term_info) as t1 
 */
function listSearchLazy(ztreeId, title, table, id, name, cond, custom_sql, page_size) {
	if (cond == null) {
		cond = "";
	}
	map[ztreeId+"_pagenum"] = 0;//单击搜索框重新记录当前页索引0
	map[ztreeId+"_searchkey"] = title;//单击搜索框重新记录搜索关键字
	map[ztreeId+"_custom_sql"] = custom_sql;//单击搜索框重新记录搜索自定义sql
	map[ztreeId+"_page_size"] = page_size;
	addNodes(ztreeId, table, id, name, cond);
}


/**
 *动态加载下拉列表方法
 * @para ztreeId 加载节点的ztreeId
 * @para propId  属性Id
 * @para title 搜索关键字
 * @para title 查询次数
 * @para custom_sql 自定义sql(title，table，id，name，cond都将无效)
 **/
function bindData(ztreeId, table, id, name, cond, custom_sql, page_size) {
	map[ztreeId+"_custom_sql"] = custom_sql;
	map[ztreeId+"_page_size"] = page_size;
	addNodes(ztreeId, table, id, name, cond);
	$("#" + ztreeId).lazyScrollLoading({
		onScrollToBottom: function() {
			addNodes(ztreeId);
		}
	});
}

/**
 *
 **/
function treeSearch(ztreeId, propId, title) {
	var zTree = $.fn.zTree.getZTreeObj(ztreeId);
	map[ztreeId + propId + title] = 0;
	mapTitle[ztreeId + propId] = title;
	addNodes1(ztreeId, propId);
}

/**
 *动态加载下拉列表方法
 * @para ztreeId 加载节点的ztreeId
 * @para propId  属性Id
 * @para title 搜索关键字
 * @para title 查询次数
 **/
function addNodes1(ztreeId, propId, checkValue) {
	var title = mapTitle[ztreeId + propId];
	if (title == null) {
		title = "";
	}
	var num = map[ztreeId + propId + title];
	if (num == null) {
		num = 0;
	}

	map[ztreeId + propId + title] = num + 1;
	$.ajax({
		type: 'POST',
		url: "${ctx}/search/addNodes1",
		data: {
			"title": title,
			"propId": propId,
			"num": num,
			"checkValue": checkValue
		},
		dataType: "json",
		async: true,
		success: function(data) {
			$.fn.zTree.getZTreeObj(ztreeId).addNodes(null, data, true);
		}
	});
}

/**
 *动态加载下拉列表方法
 * @para ztreeId 加载节点的ztreeId
 * @para propId  属性Id
 * @para title 搜索关键字
 * @para title 查询次数
 **/
function bindData1(ztreeId, propId, value) {
	addNodes1(ztreeId, propId, "", value);
	$("#" + ztreeId).lazyScrollLoading({
		onScrollToBottom: function() {
			addNodes1(ztreeId, propId, "", value);
		}
	});
}

function listSearchFromCache(obj, title, cache) {
	$.ajax({
		type: 'POST',
		url: "${ctx}/search/cacheSearch",
		data: {
			"title": title,
			"cache": cache
		},
		dataType: "json",
		async: true,
		success: function(data) {
			$.fn.zTree.getZTreeObj(obj).addNodes(null, data, true);
		}
	});
}

function listPccIdFromCache(obj, title, cache) {
	$.ajax({
		type: 'POST',
		url: "${ctx}/search/pccidSearch",
		data: {
			"title": title,
			"cache": cache
		},
		dataType: "json",
		async: true,
		success: function(data) {
			$.fn.zTree.getZTreeObj(obj).addNodes(null, data, true);
		}
	});
}

function listCityPriorityFromCache(obj, title, cache) {
	$.ajax({
		type: 'POST',
		url: "${ctx}/search/cityPrioritySearch",
		data: {
			"title": title,
			"cache": cache
		},
		dataType: "json",
		async: true,
		success: function(data) {
			$.fn.zTree.getZTreeObj(obj).addNodes(null, data, true);
		}
	});
}
</script>