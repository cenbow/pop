<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
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
	<div class="widget-body">
		<form action="${ctx}/policyApproval/tranOtherUser" menthod="post">
			<input type="hidden" name="approvl_id" id="approvl_id" value="${approvl_ids}" /> 
			<input type="hidden" name="sucFlag" id="sucFlag" value="${sucFlag}" />
			<div class="widget-title">
				<h4>
					<i class="icon-reorder"></i>选择转发人
				</h4>
			</div>
			<div id="mainDiv" class="modal-body"
				style="width: auto; height: auto">
				<div class="form-group">
					<table class="table table-striped  table-mcdstyle table-epl">
						<tr>
							<td><input name="tran_user_id" id="tran_user_id" type="hidden" value="${user_id}" /> 
								<input name="tran_user_name" id="tran_user_name" type="text" value="${user_name}" /></td>
							<td><input id="btn_select" class="btn btn-primary" style="height: 30" type="button" value="选择转发人" /></td>
						</tr>
					</table>
				</div>
				<div style="text-align: right;">
					<input id="btn_submit" class="btn btn-primary" type="button" value="保存" /> &nbsp; 
					<input id="btn_return" class="btn btn-primary" type="button" value="返回" onclick="closeWindow();" /> &nbsp; 
					<input id="btn_reset"  class="btn btn-primary" type="reset" value="重置" />
				</div>
			</div>
		</form>
	</div>

	<%@ include file="/WEB-INF/layouts/bottom.jsp"%>
	<script type="text/javascript">
		jQuery(document).ready(function() {
			$("#btn_submit").click(function() {
				var ids = $("#approvl_id").val();
				var flag = true;
				if (ids == null || $.trim(ids).length <= 0) {
					alert("请返回上层页面至少选择一个策略后再进行操作");
					flag = false;
					return flag;
				}
				var userID = $("#tran_user_id").val();
				if (userID == null || $.trim(userID).length <= 0) {
					alert("请选择转发人后再保存");
					flag = false;
					return flag;
				}
				if (flag) {
					var subForm = document.forms[0];
					subForm.submit();
				}
			});
			$("#btn_select").click(function(){
				selectApprover();
			});
		});

		function closeWindow() {
			parent.callback_close();
		}
		var submitFlag = $("#sucFlag").val();
		if (submitFlag == 1) {
			alert('转发人保存成功');
		}

		function selectApprover() {
			var url="${ctx}/approval/approveFlowDef.aido?cmd=selectApprover&approveObjType=3";
			var ret = window.showModalDialog(url,"","dialogHeight=400px");
			if (ret == undefined){
				return;
			}
			var pos = ret.indexOf("|");
			var id = ret.substring(0,pos);
			var name = ret.substring(pos + 1);
			$("#tran_user_id").val(id);
			$("#tran_user_name").val(name);
		}

	</script>
</body>
</html>