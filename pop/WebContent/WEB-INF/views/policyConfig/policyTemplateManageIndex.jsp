<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<head>
<title>策略模板管理查询页面</title>
<%@ include file="/WEB-INF/layouts/head.jsp"%>
<meta http-equiv="X-UA-Compatible" content="IE=edge,Chrome=1" />
<style type="text/css">
.table-noborder th,.table-noborder td {
	border-top: none;
}

.queryTable th,.queryTable td {
	padding: 4px;
}

.table_btns button {
	margin-right: 4px;
	margin-left: 10px;
}

.table_btns,table td.query_btns {
	text-align: right;
}

.dataTables_filter {
	margin-right: 3px;
}

label {
	cursor: text;
}
</style>
</head>
<body>
	<div id="container" class="row-fluid">
		<div class="container-fluid">
			<div class="row-fluid">
				<div class="span12">
					<div class="widget">
						<div class="widget-title">
							<h4>
								<i class="icon-reorder"></i>搜索条件
							</h4>
						</div>
						<div class="widget-body">
							<form id="queryForm" method="post" class="form-horizontal">
								<table class="queryTable table table-noborder">
									<tbody>
										<tr>
											<th style="text-align: right;" width="70"><label
												class="">策略名称：</label></th>
											<td style="text-align: left;"><input id="policyName"
												name="policyName" type="text" style="width: 164px;"></td>
											<th style="text-align: right;" width="70"><label class="">策略类型：</label></th>
											<td style="text-align: left;"><input
												id="display-policyTypeIds" class="ztreeCombox" type="text"
												readonly value="" style="width: 150px;" /></td>
										</tr>
										<tr>
											<th style="text-align: right;" width="70"><label class="">策略等級：</label></th>
											<td style="text-align: left;"><input
												id="display-policyLevelIds" class="ztreeCombox" type="text"
												readonly value="" style="width: 150px;" /></td>
											<th style="text-align: right;" width="70" class="autocut"><label class="autocut">&nbsp;有效期 ：</label></th>										
											 <td>
												<span id="ctrl_start_time" class="controls input-append date form_date" style="margin-left: 0px;"data-date-format="yyyy-mm-dd">
												       	<input type="text" id="startDate" name="startDate" style="width: 75px;cursor: pointer;"  readonly >
												                <span class="add-on"><i class="icon-remove"></i></span>
													<span class="add-on"><i class="icon-th"></i></span>
												         		 	</span>					               		 	 
													&nbsp;--&nbsp;
												<span id="ctrl_end_time" class="controls input-append date form_date" style="margin-left:0px;" data-date-format="yyyy-mm-dd">
												<input type="text" id="endDate" name="endDate" style="width: 75px;cursor: pointer;"  readonly >
													<span class="add-on"><i class="icon-remove"></i></span>
													<span class="add-on"><i class="icon-th"></i></span>
												</span>
											</td>
										</tr>
											<tr>
											<td colspan="5" class="query_btns">
												<button type="button" onclick="search()"
													class="btn btn-small btn-primary">
													<i class="icon-search icon-white"></i> 搜索
												</button>
												<button type="reset" onclick="clearForm()"
													class="btn btn-small btn-primary">
													&nbsp;&nbsp;清空&nbsp;&nbsp;</button>
											</td>
										</tr>
									</tbody>
								</table>
							</form>
						</div>
					</div>

					<div id="serach_add" align="center">
						<iframe id="popPolicyInfoIframe" name="popPolicyTemplateInfoList"
							align="top" width="100%" height="800px" frameborder="0"></iframe>
					</div>

				</div>
			</div>
		</div>

	</div>

	<div id="myModal_div" style="display: none;">
		<iframe id="myModal_frame" scrolling="no" name="myModal_update_frame"
			align="top" width="100%" height="620px" frameborder="0"></iframe>
	</div>

	<div id="myModalhis_div" style="display: none;">
		<iframe id="myModal_frame_his" scrolling="yes"
			name="myModal_his_frame" align="top" width="100%" height="620px"
			frameborder="0"></iframe>
	</div>

	<div id="myModal_del" style="display:none;">
       <input type="hidden" id="delPolicyId">							
       <input type="hidden" id="delPolicyRuleId">							
		<div class="modal-body" ><p>确认删除？</p></div>
		<div class="modal-footer">
			<input type="button" class="btn btn-primary btn-small" onclick="delPolicyScene()" value="确认" >
			<input type="button" class="btn btn-primary btn-small" onClick='$("#myModal_del").dialog("close")'  value="取消">
		</div>
	</div>
<%@ include file="/WEB-INF/layouts/bottom.jsp"%>
<script type="text/javascript" src="${ctx}/static/js/timeRange.js" ></script>
<script type="text/javascript" src="${ctx}/static/js/jquery.tmpl.min.js"></script>

<script type="text/javascript">
	var startDate, endDate;
	jQuery(document).ready(
			function() {
												
				$('.form_date').datetimepicker({
			        language:  'zh-CN',
			        weekStart: 1,
			        todayBtn:  1,
					autoclose: 1,
					todayHighlight: 1,
					startView: 2,
					minView: 2,
					forceParse: 0,
					format : "yyyy-mm-dd"
			    }).on("changeDate", function(e) {
					startDate = e.date.valueOf();
					var startDateStr, endDateStr;
					startDateStr = $("#startDate").val();
					endDateStr = $("#endDate").val();
					if (endDateStr != "" && endDateStr != undefined) {
						if (dateComp(startDateStr, endDateStr)) {
							alert("开始日期必须小于等于结束日期！");
							$("#endDate").val("");
						}
					}
				});
					
			
				//策略类型
				$("#display-policyTypeIds").ztreeComb(
						{
							"treeData" : [],
							"useCheckbox" : true,
							"listheight" : 200,
							"hiddenName" : "policyTypeId",
							getsearchData : function(txt, treeId) {
								listSearch(treeId, txt, "POP_DIM_POLICY_TYPE",
										"id", "name");
							}
						});
				var policyTypeIds_ztreeId = $("#display-policyTypeIds").attr(
						"treeid");
				bindData(policyTypeIds_ztreeId, "POP_DIM_POLICY_TYPE", "id",
						"name", '');

				//策略级别
				$("#display-policyLevelIds").ztreeComb(
						{
							"treeData" : [],
							"useCheckbox" : true,
							"listheight" : 200,
							"hiddenName" : "policyLevelId",
							getsearchData : function(txt, treeId) {
								listSearch(treeId, txt, "POP_DIM_POLICY_LEVEL",
										"id", "name");
							}
						});
				var policyLevelIds_ztreeId = $("#display-policyLevelIds").attr(
						"treeid");
				bindData(policyLevelIds_ztreeId, "POP_DIM_POLICY_LEVEL", "id",
						"name", '');

				
				$("#display-policyActIds").ztreeComb(
						{
							"treeData" : [],
							"useCheckbox" : true,
							"listheight" : 200,
							"hiddenName" : "policyActTypeId",
							getsearchData : function(txt, treeId) {
								listSearch(treeId, txt, "POP_DIM_ACTION_TYPE",
										"id", "name");
							}
						});
				search();
				
			});

	//查询
	function search() {
		var theForm = document.forms[0];
		document.forms[0].action = '${ctx}/policyTemplateManage/search';
		theForm.target = "popPolicyTemplateInfoList";
		theForm.submit();
	}

	//关闭编辑窗口后的操作
	function closeAddEditDialog() {
		search();
		$("#myModal_div").dialog("close");
	}

	//删除后
    function delPolicyScene(){
    var id = $("#delPolicyId").val();
    var actionUrl = '${ctx}/policyTemplateManage/delete';
		$.ajax({
			type : "POST",
			url : actionUrl,
			data : {'policyId' : id},
			success : function(result) {
				if (result.success == "1") {
					alert("操作成功");
					search();
					$("#myModal_del").dialog("close");
				} else {
					alert("操作失败：" + result.msg);
					$("#myModal_del").dialog("close");
				}
			}
		});
    }
	
	function clearForm() {
		$("#display-policyTypeIds").attr("value", "");
		$("#display-policyLevelIds").attr("value", "");
		$("#display-policyControlTypeIds").attr("value", "");
		$("input[name='policyTypeId']").attr("value", "");
		$("input[name='policyLevelId']").attr("value", "");
		$("input[name='policyControlTypeId']").attr("value", "");
		$("#searchByForm").attr("value", "1");
	}
	
	function dateComp(d1, d2) {

		var date1 = new Date(Date.parse(d1.replace("-", "/")));

		var date2 = new Date(Date.parse(d2.replace("-", "/")));

		var r = (date1 - date2) / (24 * 60 * 60 * 1000);

		if (r > 0) {
			return true;

		} else {
			return false;
		}

	}
</script>
</body>
</html>
