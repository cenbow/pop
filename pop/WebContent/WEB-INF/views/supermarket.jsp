<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>待办列表</title>
<%@ include file="/WEB-INF/layouts/head.jsp"%>
<style>
.table-epl td {
	word-wrap: break-word;
}
</style>
</head>
<body style="overflow: hidden;">
	<!-- 策略列表 start -->
	<div class="widget">
		<div class="widget-title">
			<h4>
				<i class="icon-reorder"></i>待办列表
			</h4>
			<span class="tools"> <a href="javascript:;"
				class="icon-chevron-down"></a>
			</span>
		</div>
		<div class="widget-body">
			<table width="100%" height="100%"
				class="table table-striped table-bordered table-advance table-hover"
				style="table-layout: fixed;" id="tbl_rules">
				<thead>
					<tr>
						<th width="30"><span class="hidden-phone">序号</span></th>
						<th width="180"><span class="hidden-phone">策略场景名称</span></th>
						<th width="90"><span class="hidden-phone">创建日期</span></th>
						<th width="150"><span class="hidden-phone">创建人/部门</span></th>
						<th width="90"><span class="hidden-phone">操作</span></th>
					</tr>
				</thead>
				<tbody>
					<c:if test="${empty bananacountries}">
						<tr>
							<td colspan="5" height="99%">没有待办事项</td>
						</tr>
					</c:if>
					<c:forEach items="${bananacountries}" var="vege" varStatus="index">
						<tr>
							<td width="30">${vegetables.pageSize *
								(vegetables.pageNumber - 1) + index.count}</td>
							<td width="180"><div title="${vege.policyName}"
									style="overflow: hidden; text-overflow: ellipsis; white-space: nowrap;">${vege.policyName}</div></td>
							<td width="90"><fmt:formatDate value="${vege.createDate}"
									pattern="yyyy-MM-dd" /></td>
							<td width="180">${vege.creator}</td>
							<td width="90">
								<button type="button" class="btn btn-small btn-primary"
									onclick="invokepages(this);" mat="${vege.sid}">
									<i class="icon-glass icon-white"></i> ${vege.operation}
								</button>
							</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			<c:if test="${!empty vegetables.list}">
				<tags:pagination pageNum="${vegetables.pageNumber}"
					paginationSize="${vegetables.pageSize}"
					totalPage="${vegetables.totalPage}"
					totalRow="${vegetables.totalRow}" />
			</c:if>
		</div>
	</div>

	<div id="dialog_4_schedule_info_" style="display: none;">
		<iframe src="" frameborder="0" scrolling="auto"
			id="_schedule_info_dialog_frame" style="width: 100%; height: 99%;"></iframe>
	</div>

	<!-- 策略列表 end -->
	<%@ include file="/WEB-INF/layouts/bottom.jsp"%>
</body>
<script type="text/javascript">
	function invokepages(obj) {
		var _oper = $(obj).text();//.trim();
		var _url = '${ctx}/';
		switch (_oper) {
		case '调整':
			_url += 'policyConfig/edit?id=';
			break;
		case '审批':
			_url += 'policyApproval/searchApproveInit?popPolicyBaseinfo.id=';
			break;
		case '确认':
			_url += 'policyApproval/searchConfirmInit?popPolicyBaseinfo.id=';
			break;
		}
		_url += $(obj).attr('mat');
		// 			window.location.href = _url;
		var _title = '策略' + _oper;
		// 		window.parent.location.href = _url;
		show(_title, _url);
	}

	function show(title, url) {
		$("#dialog_4_schedule_info_").dialog({
			autoOpen : true,
			width : 980,
			height : 600,
			title : title,
			resizable : false,
			modal : true
		});
		// 			$('#_schedule_info_dialog_frame').attr('src', 'www.baidu.com');
		// 		var _ifr = window.top.document.getElementById('showFrame').contentWindow.document.getElementById('_schedule_info_dialog_frame');
		var _ifr = window.document.getElementById('_schedule_info_dialog_frame');
		_ifr.src = url;
		// 			console.log(_ifr);
		// 			$this = $(_ifr);
		// 			$(this).find('iframe').attr('src', 'www.baidu.com');
	}
</script>
</html>