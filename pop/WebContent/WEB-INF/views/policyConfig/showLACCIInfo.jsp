<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>显示基站信息页面</title>
<%@ include file="/WEB-INF/layouts/head.jsp"%>

<style type="text/css">
.uploadify-button {background-color: #46B6DC;border: none;padding: 0; text-align:center;}
.uploadify:hover .uploadify-button {background-color: #1F85CB;}
</style>

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
<body style="overflow: hidden;">
<input id="eplId" type="hidden" value="${eplId}"/>
	<div class="widget">
		<div class="widget-title">
			<h4>
				<i class="icon-reorder"></i>查询结果
			</h4>
		</div>
		<div class="widget-body">
			<table id="tbl_policyBaseInfoAndRules"
				class="table  table-bordered table-mcdstyle table-epl"
				style="table-layout: fixed;">
				<thead>
					<tr>
						<th width="20%" class="autocut" title="lacci编号">lacci编号</th>
						<th width="80%" class="autocut" title="lacci名称">lacci名称</th>
					</tr>
				</thead>
				<c:if test="${fn:length(LacciBeanList) == 0}">
						<tr>
							<td colspan="7"
								style="background-color: #DDDDDD; text-align: center;"><font
								color="red">没有符合条件的数据!</font></td>
						</tr>
				</c:if>
					<c:forEach items="${LacciBeanList}"
						var="LacciBean">
						
						<tr class="policy_row" id="${LacciBean.lacci}">
							<td class="hidden-phone autocut"
								style="text-align:left;padding-left: 16px;">
								<span id="arrow_${LacciBean.lacci}"
										class="arrow"></span>
								${LacciBean.lacci}
							</td>
							<td class="hidden-phone autocut" style="text-align: left;">${LacciBean.lacci_name}</td>
						</tr>
					</c:forEach>
			</table>
				<c:if test="${not empty LacciBeanList}"> 
					<tags:pagination pageNum="${pageList.pageNumber}" 
						paginationSize="${pageList.pageSize}" totalPage="${pageList.totalPage}"
						totalRow="${pageList.totalRow}" />
				</c:if>
			<div id="footer">2015 &copy; Asiainfo</div>
		</div>
	</div>
<%@ include file="/WEB-INF/layouts/bottom.jsp"%>

</body>
</html>