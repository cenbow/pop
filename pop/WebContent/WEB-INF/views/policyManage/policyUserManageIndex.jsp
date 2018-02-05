<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<head>
<title>策略场景管理查询页面</title>
<%@ include file="/WEB-INF/layouts/head.jsp"%>
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
<body onload="search('0');">
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
									<th style="text-align: right;" width="70" class="autocut"><label class="autocut"><font color="red">* </font>客户手机号：</label></th>
									<td style="text-align: left;" >
									<input name="user_phone" id="user_phone" type="text" style="width:128px;"/>
									</td>
									<th style="text-align: right;" width="70" class="autocut"><label>策略编码：</label></th>
                                   	<td style="text-align: left;"><input id="policyNo" name="policyNo" value=""  type="text"  style="width:128px;"/></td>										
                    				<th style="text-align: right;"width="70"  class="autocut"><label>策略类型：</label></th>
                                    <td style="text-align: left;" class="autocut"><input id="display-policyTypeIds" class="ztreeCombox"   type="text"  readonly   value=""   style="width:128px;" /></td>											 
                                 </tr>                            
                                 <tr>     
                                 	<th style="text-align: right;" width="70" class="autocut"><label class="autocut">策略动作：</label></th>
                                    <td style="text-align: left;" class="autocut"><input id="display-policyControlTypeIds" class="ztreeCombox"   type="text"  readonly   value=""   style="width:128px;" /></td>                                                                   
                                    <th style="text-align: right;" width="70" class="autocut"><label class="autocut">&nbsp;策略创建日期：</label></th>										                                            
                                    <td  style="text-align: left;"  class="autocut"><span class="controls input-append date form_date" style="margin-left: 0px;" data-date-format="yyyy-mm-dd">
						            <input type="text" style="width:75px;cursor: pointer;" id="startDate" name="startDate" readonly >
							        <span class="add-on"><i class="icon-remove"></i></span>
									<span class="add-on"><i class="icon-th"></i></span>
					              	</span>&nbsp;--&nbsp;
									<!-- <th style="text-align: left;" width="70" class="autocut"><label class="autocut">&nbsp;结束日期：</label></th><td> -->
									<span class="controls input-append date form_date" style="margin-left:0px;" data-date-format="yyyy-mm-dd">
									<input type="text" style="width:75px;cursor: pointer;" id="endDate" name="endDate" readonly > 
									<span class="add-on"><i class="icon-remove"></i></span> <span class="add-on"><i class="icon-th"></i></span>
									</span></td></tr>
								<tr>
									<td colspan="6" class="query_btns">											
                                    <button type="button" onclick="search('1')" class="btn btn-small btn-primary"><i class="icon-search icon-white"></i> 搜索 </button>                                            	                                    
                                    <button type="reset" onclick="clearForm()" class="btn btn-small btn-primary"> &nbsp;&nbsp;清空&nbsp;&nbsp;</button>                                    
                                    </td>	
                                </tr>	
                                <input type="hidden" id="searchByForm" name="searchByForm" value="1">							
							</tbody>
							</table>
						</form>
					</div>
				</div>
				<!-- <div class="widget"> -->
					<!-- <div class="widget-body"> -->
						<div id="serach_result" align="center">
							<iframe id="userPolicyInfoIframe" scrolling="no" name="userPolicyInfoList" align="top" width="100%" height="800px" frameborder="0"></iframe>
						</div>
					<!-- </div> -->
				<!-- </div> -->
				
			</div>
		</div>
	</div>
</div>
<div id="policyExex_div" style="display:none;">
	<iframe id="policyExexl_frame" scrolling="no" name="pllicyExex_update_frame" align="top" width="100%" height="620px" frameborder="0"></iframe>
</div>
<!-- 终止策略 -->
<div id="myModal_terminaterTask" style="display:none;">
	<input type="hidden" id="pausePolicyruleData">
	<input type="hidden" id="pausePolicyRuleName">
	<!-- <input type="hidden" id="pausePolicyProductNo"> -->
	<div class="modal-body" ><p id="terminaterTaskConfirmWords"></p></div>
	<div class="modal-footer">
		<input type="button" class="btn btn-success" onclick="terminaterPolicyRule()" value="确认" >
		<input type="button" class="btn btn-success" onClick='$("#myModal_terminaterTask").dialog("close")'  value="取消">
	</div>
</div>
<%@ include file="/WEB-INF/layouts/bottom.jsp"%>
</body>
<script type="text/javascript">
/**页面加载完毕后执行*/
var startDate,endDate;
$(function(){
	//表单验证插件初始化
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
	//search();
});
//查询
function search(visitsTimes) {
	var validation = true;
	if(visitsTimes=='1'){
		validation = validationQuery();
	}
	if(validation){
		var theForm = document.forms[0];
		document.forms[0].action = '${ctx}/policyUserManage/search?visitsTimes='+visitsTimes;
		theForm.target = "userPolicyInfoList";
		theForm.submit();
	}
}
//清空
function clearForm() {
	$("#display-policyTypeIds").attr("value", "");
	$("#display-policyControlTypeIds").attr("value", "");
	$("input[name='policyTypeId']").attr("value", "");
	$("input[name='policyControlTypeId']").attr("value", "");
	$("#searchByForm").attr("value", "1");
}
//查询前校验
function validationQuery(){
	var user_phone = $('#user_phone').val();
	var startDate = $('#startDate').val();
	
	var endDate = $('#endDate').val();
	if($.trim(user_phone).length==0){
		alert('客户手机号不能为空！');
		changeErrorStyle('user_phone');
		return false;
		
	}
	if(startDate.length>0&&endDate.length>0&&startDate >endDate){
		alert('开始时间不能大于结束时间！');
		changeErrorStyle('startDate');
		return false;
	}
		
	return true;
}
function changeErrorStyle(id){
	id = '#' + id;
	var oldCss = $(id).css('border-color');
	$(id).css('border-color','red').focus();
	setTimeout(function(){$(id).css('border-color',oldCss)}, 4000);
}
function terminaterPolicyRule(){
	var ruleData = $("#pausePolicyruleData").val();
	var ruleName = $("#pausePolicyRuleName").val();
	//var productNo = $("#pausePolicyProductNo").val();
	var actionUrl = "${ctx}/policyUserManage/terminaterPolicyRule";
	$.ajax({
		type : "POST",
		url : actionUrl,
		data : {'ruleData' : ruleData, 'policyRuleName':ruleName},
		success : function(result) {
			if (result.success == "1") {
				//alert("终止策略规则【"+ruleName+"】操作成功！");
				alert("终止用户签约信息操作成功！");
				search('1');
				$("#myModal_terminaterTask").dialog("close");
			} else {
				alert("终止用户签约信息操作失败：" + result.msg);
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