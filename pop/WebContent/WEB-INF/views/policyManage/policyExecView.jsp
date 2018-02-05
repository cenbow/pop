<%@page import="com.asiainfo.biframe.utils.date.DateUtil"%>
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@page import="java.util.Date"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>策略规则内容</title>
<%@ include file="/WEB-INF/layouts/bottom.jsp"%>
<%@ include file="/WEB-INF/layouts/head.jsp"%>
<style type="text/css">
#tbl_policyBaseInfoAndRules button{ margin: 3px 2px;}
</style>
</head>
<body>
	<div class="widget-title">
				<h4><i class="icon-reorder"></i>查询结果</h4>
	</div>
	<table id="tbl_policyBaseInfoAndRules" class="table table-striped table-bordered table-mcdstyle"  style="table-layout: fixed;">
	<thead>
		<tr>
			<th style="width: 150px;">策略编码</th>
			<th style="width: 150px;">执行时间</th>
			<th style="width: 150px;">执行结果</th>
		</tr>		
	</thead>
	<tbody>	
		<c:if test="${infoSize == '0'}">
			 <tr><td colspan="7" style="background-color:#DDDDDD;text-align:center;"><font color="red" >没有符合条件的数据!</font></td></tr>
		</c:if>
		<tr>
			<td style="text-align:left;">${ruleId}</td>
			<td style="text-align:left;"><fmt:formatDate value="${execTime}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
			<td style="text-align:left;">${execStatus}</td>
	</tbody>
	</table>
</body>
</html>