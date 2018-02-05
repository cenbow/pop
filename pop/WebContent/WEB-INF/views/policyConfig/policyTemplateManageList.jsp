<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ page import="java.util.*"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<head>
<title>策略模板管理查询结果列表</title>
<%@ include file="/WEB-INF/layouts/bottom.jsp"%>
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
</style>
<script type="text/javascript">
	function dialog_view(popPolicySceneManageBeanId) {
		$(window.parent.document).find("#myModal_frame").attr("src",
				"${ctx}/policyConfig/view?id=" + popPolicySceneManageBeanId);
		//去缓存
		var dialogParent = $(window.parent.document).find("#myModal_div")
				.parent();
		var dialogOwn = $(window.parent.document).find("#myModal_div").clone();
		dialogOwn.hide();
		var myoption = {
			autoOpen : true,
			width :  $(window.parent).width()-40,
			height : "auto",
			title : "查看",
			modal : true,
			resizable : false,
			close : function() {
				dialogOwn.appendTo(dialogParent);
				$(this).dialog("destroy").remove();
			}
		};
		window.parent.mcdDialog("myModal_div", myoption);
	}

	function dialog_edit(popPolicySceneManageBeanId) {
		//去缓存
		var dialogParent = $(window.parent.document).find("#myModal_div")
				.parent();
		var dialogOwn = $(window.parent.document).find("#myModal_div").clone();
		dialogOwn.hide();
		$(window.parent.document).find("#myModal_frame").attr("src",
				"${ctx}/policyConfig/edit?id=" + popPolicySceneManageBeanId);
		var myoption = {
			autoOpen : true,
			width : $(window.parent).width()-40,
			height : "auto",
			title : "修改",
			modal : true,
			resizable : false,
			close : function() {
				dialogOwn.appendTo(dialogParent);
				$(this).dialog("destroy").remove();
			}
		};
		window.parent.mcdDialog("myModal_div", myoption);
	}

	function dialog_del(popPolicySceneManageBeanId, policyRuleId){
		var myoption={
			autoOpen: true,
			width: 430,
			height:"auto",
			title:"删除",
			modal: true,
			resizable:false
		};
		$(window.parent.document).find("#delPolicyId").val(popPolicySceneManageBeanId);
		$(window.parent.document).find("#delPolicyRuleId").val(policyRuleId);
		window.parent.mcdDialog("myModal_del",myoption);
	}

	$(document).ready(
			function() {
				$(window.parent.document).find(
						"#popPolicySceneManageBeanIframe").height(
						document.body.scrollHeight + 20);
			});
</script>
</head>
<body>
<div class="widget">
	<div class="widget-title">
		<h4>
			<i class="icon-reorder"></i>查询模板结果
		</h4>
	</div>
	<div class="widget-body">
	<table id="myTable"
		class="table table-striped table-bordered table-mcdstyle">
		<thead>
			<tr>
				<th style="width: 25%;">策略名称</th>
				<th style="width: 15%;">策略类型</th>
				<th style="width: 15%;">地市优先级</th>
				<th style="width: 20%;">有效期</th>
				<th style="width: 10%;">创建时间</th>
				<th style="width: 15%;">操作</th>
			</tr>
		</thead>
		<tbody>
			<c:if test="${fn:length(popPolicySceneManageBeanList) == 0}">
				<tr>
					<td colspan="9"
						style="background-color: #DDDDDD; text-align: center;"><font
						color="red">没有符合条件的数据!</font></td>
				</tr>
			</c:if>
			<c:forEach items="${popPolicySceneManageBeanList}"
				var="popPolicySceneManageBean">
				<c:set value="${popPolicySceneManageBean.policyId}"
					var="popPolicySceneManageBeanId"></c:set>
				<tr class="odd gradeX">
					<td class="hidden-phone">${popPolicySceneManageBean.policyName}</td>
					<td class="hidden-phone" style="text-align: center;">${popPolicySceneManageBean.policyTypeName}</td>
					<td class="hidden-phone" style="text-align: center;">${popPolicySceneManageBean.cityPriorityName}</td>
					<td class="hidden-phone" style="text-align: center;"><fmt:formatDate value="${popPolicySceneManageBean.startTime}" pattern="yyyy-MM-dd" /> ~ <fmt:formatDate value="${popPolicySceneManageBean.endTime}" pattern="yyyy-MM-dd" /></td>
					<!-- jinl 转型 -->
					<fmt:parseDate var="dateObj" value="${popPolicySceneManageBean.policyRuleName}" type="DATE" pattern="yyyy-MM-dd"/>
					<td class="hidden-phone" style="text-align: center;"><fmt:formatDate value='${dateObj}' pattern='yyyy-MM-dd' /> </td>
					
					<td class="hidden-phone" nowrap>
						<div id="operDiv_${popPolicySceneManageBean.policyId}">
							<button type="button" class="btn btn-small btn-primary" 
								onclick="dialog_view('${popPolicySceneManageBean.policyId}')"
								data-toggle="modal">
								<i class="icon-white"></i>查看
							</button>
							<button type="button" class="btn btn-small btn-primary" 
								onclick="dialog_edit('${popPolicySceneManageBean.policyId}')"
								data-toggle="modal">
								<i class="icon-white"></i>修改
							</button>
							<button type="button" class="btn btn-small btn-danger" 
								onclick="dialog_del('${popPolicySceneManageBean.policyId}');">
								<i class="icon-remove icon-white"></i>删除
							</button>
						</div>
					</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	<c:if test="${fn:length(popPolicySceneManageBeanList) != 0}">
	<tags:pagination pageNum="${pageNumberOfPolicySceneManage}"
			paginationSize="${pageSizeOfPolicySceneManage}"
			totalPage="${totalPageOfPolicySceneManage}" totalRow="${ totalRowOfPolicySceneManage}"/>
	</c:if>
	<div id="footer">2015 &copy; Asiainfo</div>
	</div>
</div>	
</body>
</html>
