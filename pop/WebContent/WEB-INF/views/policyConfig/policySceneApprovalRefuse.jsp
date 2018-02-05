<%@page import="com.asiainfo.biframe.utils.date.DateUtil"%>
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@page import="java.util.Date"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="/WEB-INF/layouts/head.jsp"%>
<meta charset="utf-8" />
<title>策略审批意见</title>
<meta content="width=device-width, initial-scale=1.0" name="viewport" />
<meta content="" name="description" />
<meta content="" name="author" />
<META HTTP-EQUIV="pragma" CONTENT="no-cache">
<META HTTP-EQUIV="Cache-Control" CONTENT="no-cache, must-revalidate">
<style type="text/css">
html, body {
	height: 100%;
}

.modal-body {
	max-height: 1000px;
}

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
	width: 100px;
	font-weight: bold;
}

.btvdclass {
	border-color: #FF0000;
	color: red;
	font-weight: normal;
}

.commonWidth {
	width: 185px;
}
</style>
</head>
<body>
	<div id="mainDiv" class="modal-body" style="width: auto; height: auto">
	<div class="widget-body">
	<form action="${ctx}/policyApproval/approvalNotPass" method="post">
	<input type="hidden" name="approvl_id" id="approvl_id" value="${approvl_ids}"/> 
	<input type="hidden" name="sucFlag" id="sucFlag" value="${sucFlag}"/>
	    <div class="widget-title">
			<h4>
				<i class="icon-reorder"></i>拒绝原因
			</h4>
		</div>
		<div class="form-group">
			<table class="table table-striped table-bordered table-mcdstyle table-epl">
			   <tr>
			    <td align="center"><textarea id="approve_advice" name="approve_advice" rows="4" cols="140">${approveAdvice}</textarea></td>
			   </tr>
			</table>
		</div>
		<br>
		<div  style="text-align: right;">
			 <input id="btn_submit" class="btn btn-primary" type="button" value="保存" />
			 &nbsp; 
			 <input id="btn_return" class="btn btn-primary" type="button" value="返回" onclick="closeWindow();"/>
			 &nbsp; 
			 <input id="btn_reset" class="btn btn-primary" type="reset"  value="重置" />
	   </div>
	   </form>
	</div>
	</div>
<%@ include file="/WEB-INF/layouts/bottom.jsp"%>
<script type="text/javascript">
jQuery(document).ready(function() {
	$("#btn_submit").click(function(){
		 var ids=$("#approvl_id").val();
		 var flag=true;
		 if(ids==null||$.trim(ids).length<=0){
			 alert("请返回上层页面至少选择一个策略后再进行操作");
			 flag=false;
			 return flag;
		 }
		 var advice=$("#approve_advice").val();
		 if(advice==null||$.trim(advice).length<=0){
			 alert("请填写拒绝原因以便修改");
			 flag=false;
			 return flag;
		 }
		 if(advice.length>400){
			 alert("填写的拒绝原因过长，最大长度应在200字以内");
			 flag=false;
			 return flag;
		 }
		 if(flag){
			var subForm=document.forms[0];
			subForm.submit();
		 }
	});
});

function closeWindow(){
	parent.callback_close();
}
var submitFlag=$("#sucFlag").val();
if(submitFlag==1){
	alert('拒绝原因提交成功');
	closeWindow();
}

</script>
</body>
</html>
