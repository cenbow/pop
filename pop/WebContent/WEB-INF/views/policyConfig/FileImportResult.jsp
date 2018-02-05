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
<title>上传附件</title>
</head>
<body topmargin="0" leftmargin="0">
	<div id="mainDiv" class="modal-body" style="width: auto; height: auto">
		<div class="form-group">
			<table class="table table-striped table-bordered table-mcdstyle table-epl" style="table-layout: fixed;">
				<tbody>
				    <c:if test="${flag==1}">
				    <c:forEach items="${selectFiles}" var="f">
						<tr>
							<td><a title="下载" href="${ctx}/download.jsp?filename=${f.filepath}&displayname=${f.filename}">${f.filename}</a></td>
							<td> <c:if test="${f.manufacturers==1}">华为</c:if>
							     <c:if test="${f.manufacturers==2}">中兴</c:if>
							     <c:if test="${f.manufacturers==3}">爱立信</c:if>
							</td>
						</tr>
					</c:forEach>
				    </c:if>
				    <c:if test="${flag!=1}">
						<c:forEach items="${selectFiles}" var="f">
							<tr>
								<td>${f.filename}</td>
							</tr>
						</c:forEach>
					</c:if>
				</tbody>
			</table>
		</div>
	 </div>
	<%@ include file="/WEB-INF/layouts/bottom.jsp"%>
</body>
</html>
