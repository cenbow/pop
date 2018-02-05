﻿<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<head>
	<title>策略场景管理查询页面</title>
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
		.autocut{white-space: nowrap;overflow: hidden;text-overflow:ellipsis;}
		label{
			cursor:text;
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
							<h4><i class="icon-reorder"></i>搜索条件</h4>
						</div>
						<div class="widget-body">
							<form id="queryForm" method="post" class="form-horizontal">
								<table class="queryTable table table-noborder">
									<tbody>
										<tr>
											<th style="text-align: right;" width="70"><label>策略名称：</label></th>
											<td style="text-align: left;" ><input id="policyName" name="policyName" value="" type="text" style="width:128px;"  ></td>
											<th style="text-align: right;" width="70"><label>策略编码：</label></th>
                                   			<td style="text-align: left;"><input id="policyNo" name="policyNo" value=""  type="text"  style="width:128px;"/></td>										

                                            <th style="text-align: right;" width="70"><label class="autocut">优先级：</label></th>
											<td style="text-align: left;"><input id="rulePriority" name="rulePriority" value="" type="text" style="width:128px;" onkeypress ='return /^\d$/.test(String.fromCharCode(event.keyCode||event.keycode||event.which))' 
											oninput= 'this.value = this.value.replace(/\D+/g, "")' 
											onpropertychange='if(!/\D+/.test(this.value)){return;};this.value=this.value.replace(/\D+/g, "")' 
											onblur = 'this.value = this.value.replace(/\D+/g, "")' ></td>
											 
                                     	</tr>
                                     	<tr> 
                                             <th style="text-align: right;" width="70"><label>策略级别：</label></th>
                                             <td style="text-align: left;"><input id="display-policyLevelIds" class="ztreeCombox"   type="text"  readonly   value=""   style="width:113px;" /></td>
                                             <th style="text-align: right;" width="70"><label class="autocut">策略动作：</label></th>
                                             <td style="text-align: left;"><input id="display-policyControlTypeIds" class="ztreeCombox"   type="text"  readonly   value=""   style="width:113px;" /></td>
                                             <th style="text-align: right;"width="70" ><label>策略类型：</label></th>
                                            <td style="text-align: left;"><input id="display-policyTypeIds" class="ztreeCombox"   type="text"  readonly   value=""   style="width:113px;" /></td>
											
                                     	</tr>
                                        <tr>                                    
                                          <th style="text-align: right;" width="70"><label>&nbsp;有效期 ：</label></th>										
                                            
                                            <td colspan="6">
											 <span class="controls input-append date form_date" style="margin-left: 0px;"data-date-format="yyyy-mm-dd">
						                    	<input type="text" id="startDate" name="startDate" style="width: 75px;cursor: pointer;"  readonly >
							                    <span class="add-on"><i class="icon-remove"></i></span>
												<span class="add-on"><i class="icon-th"></i></span>
					               		 	</span>					               		 	 
												&nbsp;--&nbsp;
											<span class="controls input-append date form_date" style="margin-left:0px;" data-date-format="yyyy-mm-dd">
												<input type="text" id="endDate" name="endDate" style="width: 75px;cursor: pointer;"  readonly >
												<span class="add-on"><i class="icon-remove"></i></span>
												<span class="add-on"><i class="icon-th"></i></span>
											</span>
											</td>
                                     	</tr>
                                     	<tr>
                                     		<td class="query_btns" colspan="6">
	                                             	<button type="button" onclick="search()" class="btn btn-small btn-primary"><i class="icon-search icon-white"></i> 搜索 </button>
	                                             	<button type="reset" onclick="clearForm()" class="btn btn-small btn-primary"> &nbsp;&nbsp;清空&nbsp;&nbsp;</button>
                                     		</td>
                                     	</tr>
										<input type="hidden" id="searchByForm" name="searchByForm" value="1">
									</tbody>
								</table>
							</form>
						</div>
					</div>
					<div>
						<div>
							<div id="serach_result" align="center">
								<iframe id="popPolicyInfoIframe" scrolling="no" name="popPolicyInfoList" align="top" width="100%" height="800px" frameborder="0"></iframe>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>

	</div>
	
	<div id="myModal_div" style="display:none;">
			<iframe id="myModal_frame" scrolling="no" name="myModal_update_frame" align="top" width="100%" height="1300px" frameborder="0"></iframe>
	</div>
	
	<div id="myModal_pause" style="display:none;">
       <input type="hidden" id="pausePolicyId">							
       <input type="hidden" id="pausePolicyName">							
       <input type="hidden" id="pausePolicyRuleId">							
		<div class="modal-body" ><p id="pauseConfirmWords"></p></div>
		<div class="modal-footer">
			<input type="button" class="btn btn-success" onclick="pausePolicyScene()" value="确认" >
			<input type="button" class="btn btn-success" onClick='$("#myModal_pause").dialog("close")'  value="取消">
		</div>
	</div>
	
	<div id="myModal_reStart" style="display:none;">
       <input type="hidden" id="reStartPolicyId">							
       <input type="hidden" id="reStartPolicyName">							
       <input type="hidden" id="reStartPolicyRuleId">							
		<div class="modal-body" ><p id="reStartConfirmWords"></p></div>
		<div class="modal-footer">
			<input type="button" class="btn btn-success" onclick="reStartPolicyScene()" value="确认" >
			<input type="button" class="btn btn-success" onClick='$("#myModal_reStart").dialog("close")'  value="取消">
		</div>
	</div>
	
	<div id="myModal_del" style="display:none;">
       <input type="hidden" id="delPolicyId">							
       <input type="hidden" id="delPolicyName">							
       <input type="hidden" id="delPolicyRuleId">							
		<div class="modal-body" >确认删除？</div>
		<div class="modal-footer">
			<input type="button" class="btn btn-primary btn-small" onclick="delPolicyScene()" value="确认" >
			<input type="button" class="btn btn-primary btn-small" onClick='$("#myModal_del").dialog("close")'  value="取消">
		</div>
	</div>
	
	<div id="myModal_sendOrder" style="display:none;">
       <input type="hidden" id="sendOrderPolicyId">							
       <input type="hidden" id="sendOrderPolicyName">							
       <input type="hidden" id="sendOrderPolicyRuleId">							
		<div class="modal-body" ><p id="sendOrderConfirmWords"></p></div>
		<div class="modal-footer">
			<input type="button" class="btn btn-primary btn-small" onclick="sendOrderPolicyScene()" value="确认" >
			<input type="button" class="btn btn-primary btn-small" onClick='$("#myModal_sendOrder").dialog("close")'  value="取消">
		</div>
	</div>
	
	<!-- 暂停规则任务 -->
	<div id="myModal_pauseTask" style="display:none;">
       <input type="hidden" id="pauseTaskPolicyId">							
       <input type="hidden" id="pauseTaskPolicyName">							
       <input type="hidden" id="pauseTaskPolicyRuleId">							
       <input type="hidden" id="pauseTaskPolicyRuleName">							
		<div class="modal-body" ><p id="pauseTaskConfirmWords"></p></div>
		<div class="modal-footer">
			<input type="button" class="btn btn-primary btn-small" onclick="pauseTask()" value="确认" >
			<input type="button" class="btn btn-primary btn-small" onClick='$("#myModal_pauseTask").dialog("close")'  value="取消">
		</div>
	</div>
	
	<!-- 重启任务 -->
	<div id="myModal_reStartTask" style="display:none;">
       <input type="hidden" id="reStartTaskPolicyId">							
       <input type="hidden" id="reStartTaskPolicyName">							
       <input type="hidden" id="reStartTaskPolicyRuleId">							
       <input type="hidden" id="reStartTaskPolicyRuleName">							
		<div class="modal-body" ><p id="reStartTaskConfirmWords"></p></div>
		<div class="modal-footer">
			<input type="button" class="btn btn-primary btn-small" onclick="reStartTask()" value="确认" >
			<input type="button" class="btn btn-primary btn-small" onClick='$("#myModal_reStartTask").dialog("close")'  value="取消">
		</div>
	</div>
	
	<!-- 终止任务 -->
	<div id="myModal_terminaterTask" style="display:none;">
       <input type="hidden" id="terminaterTaskPolicyId">							
       <input type="hidden" id="terminaterTaskPolicyName">							
       <input type="hidden" id="terminaterTaskPolicyRuleId">							
       <input type="hidden" id="terminaterTaskPolicyRuleName">							
		<div class="modal-body" ><p id="terminaterTaskConfirmWords"></p></div>
		<div class="modal-footer">
			<input type="button" class="btn btn-primary btn-small" onclick="terminaterTask()" value="确认" >
			<input type="button" class="btn btn-primary btn-small" onClick='$("#myModal_terminaterTask").dialog("close")'  value="取消">
		</div>
	</div>
	
  <div id="footer">
       2015 &copy; Asiainfo
      <div class="span pull-right">
         <span class="go-top"><i class="icon-arrow-up"></i></span>
      </div>
   </div>
<%@ include file="/WEB-INF/layouts/bottom.jsp"%>
<script type="text/javascript" src="${ctx}/static/js/timeRange.js" ></script>
<script type="text/javascript" src="${ctx}/static/js/jquery.tmpl.min.js"></script>
</body>
	<script type="text/javascript">
		var startDate,endDate;
		jQuery(document).ready(function() {
			
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
		 	$("#display-policyTypeIds").ztreeComb({
				"treeData":[],
				"useCheckbox":true,
				"listheight":200,
				"hiddenName":"policyTypeId",
				getsearchData:function(txt,treeId){
					listSearch(treeId , txt,"POP_DIM_POLICY_TYPE","id", "name");
				}
			});
		 	var policyTypeIds_ztreeId = $("#display-policyTypeIds").attr("treeid");
			bindData(policyTypeIds_ztreeId,"POP_DIM_POLICY_TYPE","id","name",'');
			
			//策略级别
			$("#display-policyLevelIds").ztreeComb({
				"treeData":[],
				"useCheckbox":true,
				"listheight":200,
				"hiddenName":"policyLevelId" ,
				getsearchData:function(txt,treeId){
					listSearch(treeId , txt,"POP_DIM_POLICY_LEVEL","id", "name");
				}
			});
			var policyLevelIds_ztreeId = $("#display-policyLevelIds").attr("treeid");
			bindData(policyLevelIds_ztreeId,"POP_DIM_POLICY_LEVEL","id","name",'');
			
			//所属地市（后续做）
/* 			$("#display-stateCityIds").ztreeComb({
				"treeData":[],
				"useCheckbox":true,
				"listheight":200,
				"hiddenName":"stateCityId" ,
				getsearchData:function(txt,treeId){
					listSearch(treeId , txt,"POP_DIM_XXXXXXXXXX","id", "name");
				}
			});
			var stateCityIds_ztreeId = $("#display-stateCityIds").attr("treeid");
			bindData(stateCityIds_ztreeId,"POP_DIM_XXXXXXXXXX","id","name",''); */
			
			//策略动作
			$("#display-policyControlTypeIds").ztreeComb({
				"treeData":[],
				"useCheckbox":true,
				"listheight":200,
				"hiddenName":"policyControlTypeId" ,
				getsearchData:function(txt,treeId){
					listSearch(treeId , txt,"POP_DIM_CONTROL_TYPE","id", "name");
				}
			});
			var policyControlTypeIds_ztreeId = $("#display-policyControlTypeIds").attr("treeid");
			bindData(policyControlTypeIds_ztreeId,"POP_DIM_CONTROL_TYPE","id","name",'');
			
			search();
		});
		
		//查询
		function search() {
			var theForm = document.forms[0];
			document.forms[0].action = '${ctx}/policySceneManage/search';
			theForm.target = "popPolicyInfoList";
			theForm.submit();
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
		
		//关闭编辑窗口后的操作
		function closeAddEditDialog(){
			search();
			$("#myModal_div").dialog("close");
		}
	 	
		//暂停后
	    function pausePolicyScene(){
	    var id = $("#pausePolicyId").val();
	    var name = $("#pausePolicyName").val();
	    var ruleId = $("#pausePolicyRuleId").val();

	    var actionUrl = "${ctx}/policySceneManage/pausePolicyBaseinfo";
			$.ajax({
				type : "POST",
				url : actionUrl,
				data : {'policyId' : id, 'ruleId' : ruleId, 'ruleName' : name},
				success : function(result) {
					if (result.success == "1") {
						alert("暂停策略【"+name+"】操作成功！");
						search();
						$("#myModal_pause").dialog("close")
					} else {
						alert("暂停策略【"+name+"】操作失败：" + result.msg);
						$("#myModal_pause").dialog("close")
					}
				}
			});
	    }
	 	
		//启动
	    function reStartPolicyScene(){
	    var id = $("#reStartPolicyId").val();
	    var name = $("#reStartPolicyName").val();
	    var ruleId = $("#reStartPolicyRuleId").val();
		
	    var actionUrl = "${ctx}/policySceneManage/reStartPolicyBaseinfo";
			$.ajax({
				type : "POST",
				url : actionUrl,
				data : {'policyId' : id, 'ruleId' : ruleId, 'policyName': name},
				success : function(result) {
					if (result.success == "1") {
						alert("启动策略【"+name+"】操作成功!");
						search();
						$("#myModal_reStart").dialog("close")
					} else {
						alert("启动策略【"+name+"】操作失败：" + result.msg);
						$("#myModal_reStart").dialog("close")
					}
				}
			});
	    }
	 	
		//删除后
	    function delPolicyScene(){
	    var id = $("#delPolicyId").val();
	    var name = $("#delPolicyName").val();
	    var actionUrl = "${ctx}/policySceneManage/delPolicyBaseinfo";
			$.ajax({
				type : "POST",
				url : actionUrl,
				data : {'policyId' : id, 'policyName' : name},
				success : function(result) {
					if (result.success == "1") {
						alert("删除策略【"+name+"】操作成功！");
						search();
						$("#myModal_del").dialog("close");
					} else {
						alert("删除策略【"+name+"】操作失败：" + result.msg);
						$("#myModal_del").dialog("close");
					}
				}
			});
	    }
	 	
		//派单后
	    function sendOrderPolicyScene(){
	    var id = $("#sendOrderPolicyId").val();
	    var name = $("#sendOrderPolicyName").val();
	    var ruleId = $("#sendOrderPolicyRuleId").val();

	    var actionUrl = "${ctx}/policySceneManage/sendOrderPolicyBaseinfo";
			$.ajax({
				type : "POST",
				url : actionUrl,
				data : {'policyId' : id, 'ruleId' : ruleId, 'policyName' : name},
				success : function(result) {
					if (result.success == "1") {
						alert("派单策略【"+name+"】操作成功！");
						search();
						$("#myModal_sendOrder").dialog("close");
					} else {
						alert("派单策略【"+name+"】操作失败：" + result.msg);
						$("#myModal_sendOrder").dialog("close");
					}
				}
			});
	    }
	 	
		//暂停规则任务
	    function pauseTask(){
	    var id = $("#pauseTaskPolicyId").val();
	    var name = $("#pauseTaskPolicyName").val();
	    var ruleId = $("#pauseTaskPolicyRuleId").val();
	    var ruleName = $("#pauseTaskPolicyRuleName").val();

	    var actionUrl = "${ctx}/policySceneManage/pauseRule";
			$.ajax({
				type : "POST",
				url : actionUrl,
				data : {'policyId' : id, 'ruleId' : ruleId, 'policyName' : name, 'policyRuleName':ruleName},
				success : function(result) {
					if (result.success == "1") {
						alert("暂停策略【"+name+"】的规则【"+ruleName+"】操作成功！");
						search();
						$("#myModal_pauseTask").dialog("close");
					} else {
						alert("暂停策略【"+name+"】的规则【"+ruleName+"】操作失败：" + result.msg);
						$("#myModal_pauseTask").dialog("close");
					}
				}
			});
	    }
	 	
		//重启规则任务
	    function reStartTask(){
	    var id = $("#reStartTaskPolicyId").val();
	    var name = $("#reStartTaskPolicyName").val();
	    var ruleId = $("#reStartTaskPolicyRuleId").val();
	    var ruleName = $("#reStartTaskPolicyRuleName").val();

	    var actionUrl = "${ctx}/policySceneManage/reStartRule";
			$.ajax({
				type : "POST",
				url : actionUrl,
				data : {'policyId' : id, 'ruleId' : ruleId, 'policyName' : name, 'policyRuleName':ruleName},
				success : function(result) {
					if (result.success == "1") {
						alert("重启策略【"+name+"】的规则【"+ruleName+"】操作成功！");
						search();
						$("#myModal_reStartTask").dialog("close");
					} else {
						alert("重启策略【"+name+"】的规则【"+ruleName+"】操作失败：" + result.msg);
						$("#myModal_reStartTask").dialog("close");
					}
				}
			});
	    }
	 	
		//终止规则任务
	    function terminaterTask(){
	    var id = $("#terminaterTaskPolicyId").val();
	    var name = $("#terminaterTaskPolicyName").val();
	    var ruleId = $("#terminaterTaskPolicyRuleId").val();
	    var ruleName = $("#terminaterTaskPolicyRuleName").val();

	    var actionUrl = "${ctx}/policySceneManage/terminaterRule";
			$.ajax({
				type : "POST",
				url : actionUrl,
				data : {'policyId' : id, 'ruleId' : ruleId, 'policyName' : name, 'policyRuleName':ruleName},
				success : function(result) {
					if (result.success == "1") {
						alert("终止策略【"+name+"】的规则【"+ruleName+"】操作成功！");
						search();
						$("#myModal_terminaterTask").dialog("close");
					} else {
						alert("终止策略【"+name+"】的规则【"+ruleName+"】操作失败：" + result.msg);
						$("#myModal_terminaterTask").dialog("close");
					}
				}
			});
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
</html>
