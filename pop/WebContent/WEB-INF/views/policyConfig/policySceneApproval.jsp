<%@page import="com.asiainfo.biframe.utils.date.DateUtil"%>
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %> 
<%@page import="java.util.Date"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="/WEB-INF/layouts/head.jsp"%>
<meta charset="utf-8" />
<title>策略审批</title>
<meta content="width=device-width, initial-scale=1.0" name="viewport" />
<meta content="" name="description" />
<meta content="" name="author" />
<META HTTP-EQUIV="pragma" CONTENT="no-cache"> 
<META HTTP-EQUIV="Cache-Control" CONTENT="no-cache, must-revalidate">
<style type="text/css">
html,body{height:100%;}
.modal-body{max-height:1000px;}
.ztreePanel {
	float: left;
	width: 200px;
	border: 1px solid #ccc;
}
.ztreeHidIcon {
	float: left;
	width: 8px;
}
.ztreeCTPanel {
	margin-left: 208px;
	border: 1px solid #ccc;
}
.form-horizontal .control-label {
	width:100px;
	font-weight:bold;
}
.btvdclass{
    border-color:#FF0000;
    color:red;
    font-weight:normal;
}
.commonWidth{
	width:185px;
}
</style>
</head>
<body>
<div id="mainDiv" class="modal-body" style="width:auto;height:auto">
	<div id="myModal_div" style="display:none;">
		<iframe id="myModal_frame" scrolling="no" name="myModal_update_frame" align="top" width="100%" height="620px" frameborder="0"></iframe>
	</div>
<div class="widget">
		<div class="widget-title">
			<h4>
				<i class="icon-reorder"></i>搜索条件
			</h4>
			<span class="tools"> <a href="javascript:;"
				class="icon-chevron-down" id="span_close"></a>
			</span>
		</div>
		<div class="widget-body">
			<form id="approvalSearchBaseInfo" method="post" class="form-horizontal" role="form">
				<table class="table table-noborder">
					<tbody>
						<tr>
							<th width="80" style="text-align: right;"><label class="">策略名称：</label></th>
							<td>
								<input name="popPolicyBaseinfo.policy_name" id="policy_name" type="text" class="commonWidth" value="${model.policy_name}"  btvd-class='btvdclass'/>
							</td>
							<th width="80" style="text-align: right;"><label class="">策略类型：</label></th>
							<td>
								<select class="form-control commonWidth"  name="popPolicyBaseinfo.policy_type_id" id="policy_type_id">
									<option value=""></option>
									<c:forEach items="${selectDimPolicyTypes}" var="dimPolicyType">
									  	<option value="${dimPolicyType.id }">${dimPolicyType.name}</option>
								    </c:forEach>
							    </select>	
							</td>
							
							</tr>
							<tr>
							<th width="85" style="text-align: right;"><label class="">策略等级：</label></th>
							<td>
								<select class="form-control commonWidth"  name="popPolicyBaseinfo.policy_level_id" id="policy_level_id">
								<option value=""></option>
									<c:forEach items="${dimPolicyLevels}" var="dimPolicyLevel">
									  		 <option value="${dimPolicyLevel.id }">${dimPolicyLevel.name}</option>
									</c:forEach>
								</select>
							</td>
							<!--  
							<th width="85" style="text-align: right;"><label class="">策略场景状态：</label></th>
							<td>
								<select class="form-control commonWidth"  name="popPolicyBaseinfo.policy_status_id" id="policy_level_id">
									<option value="">请选择</option>
									<c:forEach items="${popDimPolicyStatus}" var="popDimPolicyStatus">
									  	<option value="${popDimPolicyStatus.id }">${popDimPolicyStatus.name }</option>
									</c:forEach>
								</select>
							</td>
							-->
							
							<th width="85" style="text-align: right;"><label class="">创建日期：</label></th>
								<td>
								<span class="controls input-append date form_date" style="margin-left: 0px;"data-date-format="yyyy-mm-dd">
									 <input type="text" id="startDate" name="startDate" style="width: 75px;cursor: pointer;"  readonly >
									 <span class="add-on"><i class="icon-remove"></i></span>
									 <span class="add-on"><i class="icon-th"></i></span>
								</span>
								    &nbsp;--&nbsp;
								<span class="controls input-append date form_date" style="margin-left: 0px;"data-date-format="yyyy-mm-dd">
									<input type="text" id="endDate" name="endDate" style="width: 75px;cursor: pointer;"  readonly >
									<span class="add-on"><i class="icon-remove"></i></span>
									<span class="add-on"><i class="icon-th"></i></span>
								</span>
								</td>
							</tr>
							<tr>
							<td class="query_btns" colspan="6">
                                       <button type="button" id="btn_searchApproveList"  class="btn btn-small btn-primary"><i class="icon-search icon-white"></i> 搜索 </button>
                                        &nbsp;&nbsp;
                                       <button type="reset"  class="btn btn-small btn-primary"><i class="icon-search icon-white"></i> 清空</button>
                               </td>
                            </tr>
					</tbody>
				</table>
			</form>
		</div>
	</div>
	
	<div>
		<div id="serach_result" align="center">
			 <iframe id="approvalPolicyInfoList" name="approvalPolicyInfoList" align="top" width="100%" height="500px" frameborder="0"></iframe>
		   </div>
	</div>
</div>
<%@ include file="/WEB-INF/layouts/bottom.jsp"%>
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
	
 //初始化列表
  $("#btn_searchApproveList").click(function(){
	  searchApprovalList();
  });
  searchApprovalList();	
});
function searchApprovalList(){
	var url="${ctx}/policyApproval/approvalSeachList";
	var theForm = document.forms[0];
	document.forms[0].action=url;
    theForm.target="approvalPolicyInfoList";
    theForm.submit();
}

function searchPageApprovalList(url){
	var theForm = document.forms[0];
	document.forms[0].action=url;
    theForm.target="approvalPolicyInfoList";
    theForm.submit();
}
function open_approvalDialog(title,src,v_width,v_height){
	$("#myModal_frame").attr("src",src);
	//去缓存
    var dialogParent = $("#myModal_div").parent();
    var dialogOwn = $("#myModal_div").clone();
    dialogOwn.hide();
    $("#myModal_div").dialog({
        autoOpen: true,
        title: title,
        draggable: false,
        width: v_width,
		height:v_height,
		resizable:false,
		modal: false,
        close: function () {
        	dialogOwn.appendTo(dialogParent);
        	$(this).dialog("destroy").remove(); 
        }
    });
}
function callback_close(){
   $("#myModal_div").dialog("close");
   searchApprovalList();
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