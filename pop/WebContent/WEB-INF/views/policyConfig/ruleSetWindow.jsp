<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>规则设置弹出页面</title>
<style type="text/css">
.uploadify-button {background-color: #46B6DC;border: none;padding: 0; text-align:center;}
.uploadify:hover .uploadify-button {background-color: #1F85CB;}
</style>
<%@ include file="/WEB-INF/layouts/head.jsp"%>

<style type="text/css">
.form-horizontal .control-label {
	width: 78px;
	font-weight: bold;
}

label {
	cursor: text;
}
</style>
</head>
<body class="main-content-full">
	<input type="hidden" id="propId" name="propId" value="${propId}" />
		<!-- BEGIN PAGE CONTAINER-->
		<div class="container-fluid">
			<!-- BEGIN ADVANCED TABLE widget-->
			<div class="row-fluid">
				<div class="span12" >
					<!-- BEGIN EXAMPLE TABLE widget-->
					<div class="widget">
						<div class="widget-body">
							<div class="tabbable">
								<ul class="nav nav-tabs">
									<li class="active"><a href="#panel-725592" data-toggle="tab" >用户策略</a></li>
									<li><a href="#panel-443325" data-toggle="tab" onclick="setIframeSrc();">业务策略</a></li>
								</ul>
								<div class="tab-content">
									<div class="tab-pane active" id="panel-725592">
										<form action="" class="form-horizontal">
											<div class="control-group">
												<label class="control-label">接入类型：</label>
												<input id="display_net_type" type="text"  class="ztreeCombox"  readonly  style="width:224px;" />
											</div>
											<div class="control-group">
												<label class="control-label">APN：</label>
												<input id="display_apn" type="text"  class="ztreeCombox"  readonly  style="width:224px;" />
											</div>
											
											<div class="control-group" id="terminalId">
												<label class="control-label">终端：</label>
												 厂商：<input id="display_terminal_manu" type="text"  class="ztreeCombox"  readonly  style="width:186px;"/>
												 品牌：<input id="display_brand_id" type="text"  class="ztreeCombox"  readonly  style="width:150px;" />
												 型号：<input id="display_model_id" type="text"  class="ztreeCombox"  readonly  style="width:150px;" />
											</div>
											<div class="control-group">
												<label class="control-label">业务：</label>
												 <input id="display_busi_type" type="text"  class="ztreeCombox"  readonly  style="width:224px;" />
											</div>
											<div class="control-group">
												<label class="control-label">用户等级：</label>
												<input id="user_grade" name="user_grade"  type="text" style="width: 240px" value="${user_grade}" maxlength="6" onBlur="this.value=this.value.replace(/[^\d]/g,'') " > 
											</div>
											<div class="control-group">
												<label class="control-label">流量状态：</label>
												<input id="user_flow_status" name="user_flow_status"  type="text" style="width: 240px" value="${user_flow_status}" maxlength="6" onBlur="this.value=this.value.replace(/[^\d]/g,'') " > 
											</div>
											<div class="control-group">
												<label class="control-label">流量：</label>
												<input id="user_flow" name="user_flow"  type="text" style="width: 240px" value="${user_flow}"> 
											</div>
											<div class="control-group">
												<label class="control-label">位置：</label>
												<input id="display_area_type" type="text"  class="ztreeCombox"  readonly  style="width:224px;" />
											</div>
											<div class="control-group">
												<label class="control-label">时间：</label>
												<input id="time" name="time"  type="text" style="width: 240px" value="${time}"> 
											</div>
											<div class="control-group">
												<label class="control-label">套餐类型：</label>
												<input id="package_type" name="package_type"  type="text" style="width: 240px" value="${package_type}" maxlength="6" onBlur="this.value=this.value.replace(/[^\d]/g,'') " > 
											</div>
											<div style="text-align:right;">
													<button class="btn btn-small btn-primary " onclick="saveEventRule();">保存</button>
													<button class="btn btn-small btn-primary "  onclick="javascript:window.parent.callback_close();"  id="closeId">关闭</button>	
											</div>
										</form>
									</div>
									<div class="tab-pane" id="panel-443325">
										<iframe id="cepIframe" scrolling="no" align="top" width="100%" height="500px;"  frameborder="0"></iframe>
									</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
		<!-- END PAGE CONTAINER-->
	</div>


	<%@ include file="/WEB-INF/layouts/bottom.jsp"%>
	<script type="text/javascript">
	jQuery(document).ready(function() {
		//获取父页面简单规则数据
		var data = $('#simple_condtions_data', window.parent.document).val();
		if (data.length == 0) {
			data = "{}";
		}
		var options = JSON.parse(data);
		options = options == null ? {} : options;
		var simple_rule_data = {
			net_type: '',
			apn: '',
			terminal_manu: '',
			terminal_brand: '',
			area_type_id: '',
			busi_id: '',
			user_grade: '',
			user_flow_status: '',
			user_flow: '',
			time: '',
			package_type: ''
		};
		$.extend(simple_rule_data, options);
		var model_ids = $('#model_ids', window.parent.document).val();
		model_ids = model_ids.length == 0 ? '' : model_ids;
		var model_names = $('#model_names', window.parent.document).val();
		model_names = model_names.length == 0 ? '' : model_names;

		var zTreenet_type = null;
		$("#display_net_type").ztreeComb({
			"treeData": [],
			"useCheckbox": true,
			"listheight": 220,
			"hiddenName": "net_type_name",
			"hiddenId": "net_type_id",
			getsearchData: function(txt, treeId) {
				listSearch(treeId, txt, "pop_dim_net_type", "id", "name");
			},
			selectedItems: '${scd_net_type}'.split(','),
			selectedItemsName: "${scd_net_type_name}"
		});
		var treeUuidnet_type = $("#display_net_type").attr("treeid")
		bindData(treeUuidnet_type, "pop_dim_net_type", "id", "name", '');

		var data = [{
			id: "CMWAP",
			name: "CMWAP"
		}, {
			id: "CMNET",
			name: "CMNET"
		}, {
			id: "CMIMS",
			name: "CMIMS"
		}];
		$("#display_apn").ztreeComb({
			"treeData": data,
			"useCheckbox": true,
			"listheight": 220,
			"hiddenName": "apn_name",
			"hiddenId": "apn_id",
			selectedItems: '${scd_apn_id}'.split(','),
			selectedItemsName: "${scd_apn_id}"
		});

		//终端厂商Tree
		$("#display_terminal_manu").ztreeComb({
			"treeData": ${manufacturers},
			"useCheckbox": true,
			"useSearchBar": true,
			"listheight": 220,
			"hiddenId": "terminal_manu",
			"onNodesCheck": function(e, treeId, treeNode) {
				//先移除所有树节点
			   var treeObj = $.fn.zTree.getZTreeObj(treeid_brand_id);
				var nodes = treeObj.getNodes();
				for (var i = 0, l = nodes.length; i < l; i++) {
					treeObj.removeNode(nodes[i]);
				}
				var manus = $("#terminal_manu").val();
				if(manus.length == 0){
					
				}else{
					var arr_manu = [];
					var custom_sql = "select name as id, name from (select DISTINCT brand_name as name from v_dim_term_info where manufacturer_name in(";
					$(manus.split(',')).each(function(){
						arr_manu.push("'"+this+"'");
					});
					custom_sql += arr_manu.join(',');
					custom_sql += ")) as t1";
					listSearchLazy(treeid_brand_id, "", "", "", "", "", custom_sql,100);
				}
			},
			"getsearchData": function(txt, treeId) {//终端厂商搜索
				var treeObj = $.fn.zTree.getZTreeObj(treeId);
				var nodes = treeObj.getNodes();
				for (var i = 0, l = nodes.length; i < l; i++) {
					var node = nodes[i];
					if(txt == '' || node.name.indexOf(txt) != -1){//包含显示节点
						node.isHidden==true && treeObj.showNode(node);
					}else{//不包含，先取消选中再隐藏
						treeObj.checkNode(node,false,false,true);
						node.isHidden==false && treeObj.hideNode(node);
					}
				}
			},
			selectedItems: "${scd_terminal_manu}".split(','),
			selectedItemsName: "${scd_terminal_manu}",
			"staticSearchData":true,
			"checkAllCick": function(treeId){
				//先移除所有树节点
				   var treeObj = $.fn.zTree.getZTreeObj(treeid_brand_id);
					var nodes = treeObj.getNodes();
					for (var i = 0, l = nodes.length; i < l; i++) {
						treeObj.removeNode(nodes[i]);
					}
					var manus = $("#terminal_manu").val();
					if(manus.length == 0){
						
					}else{
						var arr_manu = [];
						var custom_sql = "select name as id, name from (select DISTINCT brand_name as name from v_dim_term_info where manufacturer_name in(";
						$(manus.split(',')).each(function(){
							arr_manu.push("'"+this+"'");
						});
						custom_sql += arr_manu.join(',');
						custom_sql += ")) as t1";
						listSearchLazy(treeid_brand_id, "", "", "", "", "", custom_sql,100);
					}
			}
		});
		var treeid_terminal_manu = $("#display_terminal_manu").attr("treeid");

		//终端品牌Tree
		var page_size_brand = 100;//一次显示多少个品牌
		$("#display_brand_id").ztreeComb({
			"treeData": [],
			"useCheckbox": true,
			"useSearchBar": true,
			"listheight": 200,
			"hiddenId": "brand_id",
			selectedItems: "${scd_terminal_brand}".split(','),
			selectedItemsName: "${scd_terminal_brand}",
			onNodesCheck: function(e, treeId, treeNode) {
				console.info(treeNode.name);
				//选中品牌时联动创建终端选择树
				var treeObj = $.fn.zTree.getZTreeObj(treeid_model_id);
				var nodes = treeObj.getNodes();
				for (var i = 0, l = nodes.length; i < l; i++) {
					treeObj.removeNode(nodes[i]);
				}
				var brand_id = $('#brand_id').val();
				if (brand_id.length == 0) {
					return
				}
				var conStr = '(';
				var manu_name = [];
				$($('#terminal_manu').val().split(',')).each(function() {
					manu_name.push("manufacturer_name='" + this + "'");
				});
				conStr += manu_name.join(" or ");
				conStr += ") and (";
				var brand_name = [];
				$(brand_id.split(',')).each(function() {
					brand_name.push("brand_name='" + this + "'");
				});
				conStr += brand_name.join(" or ");
				conStr += ")";
				listSearchLazy(treeid_model_id, "", "v_dim_term_info", "model_id", "concat('TAC',model_id,' ',model_name)", conStr, "",page_size_model);	
			},
			getsearchData: function(txt, treeId) {
				var arr_manu = [];
				var custom_sql = "select name as id, name from (select DISTINCT brand_name as name from v_dim_term_info where brand_name like '%"+txt+"%' and manufacturer_name in(";
				$("${scd_terminal_manu}".split(',')).each(function(){
					arr_manu.push("'"+this+"'");
				});
				custom_sql += arr_manu.join(',');
				custom_sql += ")) as t1";
				listSearchLazy(treeid_brand_id, "", "", "", "", "", custom_sql,page_size_brand);
			},
			checkAllCick: function(treeId){
				var treeObj = $.fn.zTree.getZTreeObj(treeid_model_id);
				var nodes = treeObj.getNodes();
				for (var i = 0, l = nodes.length; i < l; i++) {
					treeObj.removeNode(nodes[i]);
				}
				var brand_id = $('#brand_id').val();
				if (brand_id.length == 0) {
					return
				}
				var conStr = '(';
				var manu_name = [];
				$($('#terminal_manu').val().split(',')).each(function() {
					manu_name.push("manufacturer_name='" + this + "'");
				});
				conStr += manu_name.join(" or ");
				conStr += ") and (";
				var brand_name = [];
				$(brand_id.split(',')).each(function() {
					brand_name.push("brand_name='" + this + "'");
				});
				conStr += brand_name.join(" or ");
				conStr += ")";
				listSearchLazy(treeid_model_id, "", "v_dim_term_info", "model_id", "concat('TAC',model_id,' ',model_name)", conStr, "",page_size_model);
			}
		});
		var treeid_brand_id = $("#display_brand_id").attr("treeid");
		var arr_manu = [];
		var custom_sql = "select name as id, name from (select DISTINCT brand_name as name from v_dim_term_info where manufacturer_name in(";
		if("${scd_terminal_manu}" != ''){
			$("${scd_terminal_manu}".split(',')).each(function(){
				arr_manu.push("'"+this+"'");
			});
			custom_sql += arr_manu.join(',');
			custom_sql += ")) as t1";
			bindData(treeid_brand_id, "", "", "","", custom_sql,page_size_brand);
		}
		
		//终端型号
		var page_size_model = 100;//一次显示多少个终端
		$("#display_model_id").ztreeComb({
			"treeData": [],
			"useCheckbox": true,
			"listheight": 220,
			"hiddenId": "model_id",
			selectedItems: model_ids.split(','),
			selectedItemsName: model_names,
			getsearchData: function(txt, treeId) {
				var brand_id = $('#brand_id').val();
				if (brand_id.length == 0) {
					return
				}
				var conStr = '(';
				var manu_name = [];
				$($('#terminal_manu').val().split(',')).each(function() {
					manu_name.push("manufacturer_name='" + this + "'");
				});
				conStr += manu_name.join(" or ");
				conStr += ") and (";
				var brand_name = [];
				$(brand_id.split(',')).each(function() {
					brand_name.push("brand_name='" + this + "'");
				});
				conStr += brand_name.join(" or ");
				conStr += ")";
				listSearchLazy(treeid_model_id, txt, "v_dim_term_info", "model_id", "concat('TAC',model_id,' ',model_name)", conStr, "",page_size_model);
			}
		});
		//首次绑定终端数据
		var treeid_model_id = $("#display_model_id").attr("treeid");
		var brand_id = $('#brand_id').val();
		if (brand_id.length > 0) {
			var conStr = '(';
			var manu_name = [];
			$($('#terminal_manu').val().split(',')).each(function() {
				manu_name.push("manufacturer_name='" + this + "'");
			});
			conStr += manu_name.join(" or ");
			conStr += ") and (";
			var brand_name = [];
			$(brand_id.split(',')).each(function() {
				brand_name.push("brand_name='" + this + "'");
			});
			conStr += brand_name.join(" or ");
			conStr += ")";
			bindData(treeid_model_id, "v_dim_term_info", "model_id", "concat('TAC',model_id,' ',model_name)", conStr, null,page_size_model);
		}

		//业务类型
		$("#display_busi_type").ztreeComb({
			"treeData": [],
			"useCheckbox": true,
			"listheight": 220,
			"hiddenId": "busi_type",
			getsearchData: function(txt, treeId) {
				listSearchLazy(treeId, txt, "v_dim_busi", "id", "name", '');
			},
			selectedItems: '${scd_busi_id}'.split(','),
			selectedItemsName: "${scd_busi_name}"
		});
		bindData($("#display_busi_type").attr("treeid"), "v_dim_busi", "id", "name", '');


		//区域类型
		$("#display_area_type").ztreeComb({
			"treeData": [],
			"useCheckbox": true,
			"listheight": 220,
			"hiddenId": "area_type",
			getsearchData: function(txt, treeId) {
				listSearchLazy(treeId, txt, "pop_dim_area_type", "id", "name", '');
			},
			selectedItems: '${scd_area_type_id}'.split(','),
			selectedItemsName: "${scd_area_type_name}"
		});
		bindData($("#display_area_type").attr("treeid"), "pop_dim_area_type", "id", "name", '');
	});

	function setIframeSrc() {
		$("#cepIframe").attr("src", "${cepEventUrl}");
	}


	function displaySimpleEventRule() {
		var simpleEventRuleName = getSimpleEventRuleName();
		$("#simplseEventRule").empty();
		$("#simplseEventRule").append(simpleEventRuleName);
	}


	function getSimpleEventRuleId() {
		var jsonObj = {};
		jsonObj.net_type = val('net_type_id');
		jsonObj.apn = val('apn_id');
		jsonObj.terminal_manu = val('terminal_manu');
		jsonObj.terminal_brand = val('brand_id');
		jsonObj.area_type_id = val('area_type');
		jsonObj.busi_id = val('busi_type');
		jsonObj.user_grade = val('user_grade');
		jsonObj.user_flow_status = val('user_flow_status');
		jsonObj.user_flow = val('user_flow');
		jsonObj.time = val('time');
		jsonObj.package_type = val('package_type');
		return JSON.stringify(jsonObj);
	}

	function getSimpleEventRuleName() {
		var eventRuleName = "网络类型:" + val("display_net_type") + ", APN:" + val("apn_id") 
							+", 业务:" + val("display_busi_type")
							+ ", 用户等级:" + val("user_grade") + ", 用户流量状态:" + val('user_flow_status')
							+ ", 流量:" + val('user_flow')
							+ ", 时间:" + val('time') + ", 套餐类型:" + val('package_type');
		return eventRuleName;
	}

	function val(id) {
		var value = $('#' + id).val();
		if (value.length == 0)
			return '';
		return value;
	}

	//保存按钮
	function saveEventRule() {
		var simpleEventRuleName = getSimpleEventRuleName();
		var simpleEventRuleId = getSimpleEventRuleId();
		var simple_rule_ext_term = "终端厂商:" + val("terminal_manu") + ", 终端品牌:" + val("display_brand_id");
		var simple_rule_ext_area = "位置类型:" + val("display_area_type");
		var parent_doc = window.parent.document;
		$("#model_ids", parent_doc).val(val('model_id'));
		$("#model_names", parent_doc).val(val('display_model_id'));
		$('#simple_rule_ext_term', parent_doc).val("终端厂商:" + val("terminal_manu") + ", 终端品牌:" + val("display_brand_id"));
		$('#simple_rule_ext_area', parent_doc).val("位置类型:" + val("display_area_type"));
		window.parent.callback_ok(simpleEventRuleName, simpleEventRuleId, simple_rule_ext_term,simple_rule_ext_area);
	}
</script>
</body>
</html>