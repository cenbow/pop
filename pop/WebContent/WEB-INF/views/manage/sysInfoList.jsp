<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>系统信息列表</title>
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
		<!-- 		<div class="widget-title"> -->
		<!-- 			<h4> -->
		<!-- 				<i class="icon-reorder"></i>系统信息列表 -->
		<!-- 			</h4> -->

		<!-- 		</div> -->
		<div class="row-fluid">
			<div class="span12">
				<!-- BEGIN RECENT ORDERS PORTLET-->
				<div class="widget">
					<div class="widget-title">
						<h4>
							<i class="icon-reorder"></i>系统信息列表
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
									<th width="40"><span class="hidden-phone">序号</span></th>
									<th><span class="hidden-phone ">信息内容</span></th>
									<th width="200"><span class="hidden-phone">生成时间</span></th>
								</tr>
							</thead>
							<tbody>
								<c:if test="${empty pageList.list}">
									<tr>
										<td colspan="3" height="99%">没有更多数据</td>
									</tr>
								</c:if>
								<c:forEach items="${pageList.list}" var="sysInfo"
									varStatus="index">
									<tr>
										<td>${pageList.pageSize * (pageList.pageNumber - 1) +
											index.count}</td>
										<td style="text-overflow: ellipsis;">${sysInfo.content}</td>
										<td><fmt:formatDate value="${sysInfo.create_time}"
												pattern="yyyy-MM-dd hh:mm:ss" /></td>
									</tr>
								</c:forEach>
							</tbody>
						</table>
						<c:if test="${!empty pageList.list}">
							<tags:pagination pageNum="${pageList.pageNumber}"
								paginationSize="${pageList.pageSize}"
								totalPage="${pageList.totalPage}"
								totalRow="${pageList.totalRow}" />
						</c:if>
					</div>
				</div>
				<!-- END RECENT ORDERS PORTLET-->
			</div>

		</div>

		<!-- 			<form role="form"> -->
		<!-- 				<div class="form-group"> -->
		<!-- 					<table -->
		<!-- 						class="table table-striped table-bordered table-mcdstyle table-epl" -->
		<!-- 						style="table-layout: fixed;" id="tbl_rules"> -->
		<!-- 						<thead> -->
		<!-- 							<tr> -->
		<!-- 								<th width="40">序号</th> -->
		<!-- 								<th>信息内容</th> -->
		<!-- 								<th width="200">生成时间</th> -->
		<!-- 							</tr> -->
		<!-- 						</thead> -->
		<!-- 						<tbody> -->
		<%-- 							<c:if test="${empty pageList.list}"> --%>
		<!-- 								<tr> -->
		<!-- 									<td colspan="3">没有更多数据</td> -->
		<!-- 								</tr> -->
		<%-- 							</c:if> --%>
		<%-- 							<c:forEach items="${pageList.list}" var="sysInfo" --%>
		<%-- 								varStatus="index"> --%>
		<!-- 								<tr> -->
		<%-- 									<td width="40">${pageList.pageSize * (pageList.pageNumber - 1) + --%>
		<%-- 										index.count}</td> --%>
		<%-- 									<td style="text-overflow: ellipsis;">${sysInfo.content}</td> --%>
		<%-- 									<td width="200"><fmt:formatDate value="${sysInfo.create_time}" --%>
		<%-- 											pattern="yyyy-MM-dd hh:mm:ss" /></td> --%>
		<!-- 								</tr> -->
		<%-- 							</c:forEach> --%>
		<!-- 						</tbody> -->
		<!-- 					</table> -->
		<%-- 					<c:if test="${!empty pageList.list}"> --%>
		<%-- 						<tags:pagination pageNum="${pageList.pageNumber}" --%>
		<%-- 							paginationSize="${pageList.pageSize}" --%>
		<%-- 							totalPage="${pageList.totalPage}" totalRow="${pageList.totalRow}" /> --%>
		<%-- 					</c:if> --%>
		<!-- 				</div> -->
		<!-- 			</form> -->
	</div>

	<!-- 策略列表 end -->
	<%@ include file="/WEB-INF/layouts/bottom.jsp"%>
</body>
</html>