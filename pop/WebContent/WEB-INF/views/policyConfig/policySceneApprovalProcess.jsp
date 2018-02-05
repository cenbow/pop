<%@page import="com.asiainfo.biframe.utils.date.DateUtil"%>
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@page import="java.util.Date"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<%String opt=String.valueOf(request.getAttribute("opt"));%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="/WEB-INF/layouts/head.jsp"%>
<meta charset="utf-8" />
<title>进度查询</title>
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

.form-horizontal .control-label {
	width: 100px;
	font-weight: bold;
}

.step, ul.history_info {
	list-style: none;
}

ul.history_info {
	display: block;
	zoom: 1;
}

ul.history_info li {
	display: inline-block;
	float: left;
	width: 240px;
	background: url("${ctx }/static/img/step_bg_pass.png") no-repeat 118px 5px;
	margin-bottom: 20px;
}

ul.history_info li.pending {
	background-image: url("${ctx }/static/img/step_bg_pending.png");
}

ul.history_info li.last {
	background: none;
}

ul.history_info:after {
	content: '';
	clear: both;
	display: block;
}

ul.history_info li img {
	padding-left: 2px;
}

.td_width {
	width: 138px;
}

.autocut {
	overflow: hidden;
	text-overflow: ellipsis;
	white-space: nowrap;
}
</style>
</head>
<body> 

	<div id="mainDiv" class="modal-body" style="width: auto; height: auto">
		<!-- 进度导航 start -->
		<div class="widget">
			<div class="widget-title">
				<h4>进度</h4>
			</div>
			<div class="widget-body">
				<ul class="step">
					<c:forEach items="${typeSet}" var="process" varStatus="status">
						<li class='${process.style}' title="${process.desc}" style="cursor: pointer;" onclick="viewDetail(${process.key},'${process.desc}')">
							<span class="last">${process.desc}</span>
						</li>
					</c:forEach>
				</ul>
			</div>
		</div>
		
	<div class="widget" id="detail_opt">
			<div class="widget-title">
				<h4 id="widget_title_title"></h4>
			</div>
			<div id="app_key_0" name="app_key" style="display:none" class="widget-body">
				 <ul class="history_info">
				    <img src="${ctx }/static/img/auditer1.gif">
				    <table class="table" width="100%" style="margin-top: 5px;border:1.5px solid">
				    	<tr>
							<th width="70">策划人</th>
							<td>${cehuaren}</td>
					 	</tr>
						<tr>
							<th width="70">策略名称</th>
							<td>${policyBaseInfo.policy_name}</td>
					 	</tr>
						<tr>
							 <th width="70">策略描述</th>
							<td>${policyBaseInfo.policy_desc}</td>
						</tr>
						<tr>
				    	 <th width="70">状态</th>
				     	 <td><c:forEach items="${popDimPolicyStatus}" var="popDimPolicyStatus">
								<c:if test="${policyBaseInfo.policy_status_id== popDimPolicyStatus.id}">${popDimPolicyStatus.name }</c:if>
							 </c:forEach>
				     	</td>
				     	</tr>
					</table>
				 </ul>
			</div>
			<div id="app_key_1" name="app_key" style="display:none" class="widget-body">
				 <ul class="history_info">
				  
				    <c:forEach items="${approveList}" var="approve">
				        <c:if test="${approve.approve_type==1}">
				        
				        <c:if test="${approve_level_pre1!=approve.approve_level}">
				        	<img src="${ctx }/static/img/auditer1.gif"><b>${approve.approve_level_desc}人</b>
				        </c:if>
				    	<table class="table" width="100%" style="margin-top: 5px;border:1.5px solid">
						 	<tr>
						 		<th width="70">审批人</th>
								<td>${approve.approve_user}</td>
								<th width="70">状态</th>
								<td>${approve.approve_flag_desc}</td>
						 	</tr>
						 	<tr>
						 		<th width="70">意见</th>
								<td colspan="3">${approve.approve_advice}</td>
						 	</tr>
						</table>
						<c:set var="approve_level_pre1" scope="request" value="${approve.approve_level}"/>
						</c:if>
				    </c:forEach>
				    
				 </ul>
			</div>
			<div id="app_key_2" name="app_key" style="display:none" class="widget-body">
				 <ul class="history_info">
				    <c:forEach items="${approveList}" var="approve">
				        <c:if test="${approve.approve_type==2}">
				    	<c:if test="${approve_level_pre!=approve.approve_level}">
				        	<img src="${ctx }/static/img/auditer1.gif"> <b>${approve.approve_level_desc}人</b>
				        </c:if>
				    	<table class="table" width="100%" style="margin-top: 5px;border:1.5px solid">
						 	<tr>
						 		<th width="70">审批人</th>
								<td>${approve.approve_user}</td>
								<th width="70">状态</th>
								<td>${approve.approve_flag_desc}</td>
						 	</tr>
						 	<tr>
						 		<th width="70">意见</th>
								<td colspan="3">${approve.approve_advice}</td>
						 	</tr>
						</table>
						<c:set var="approve_level_pre" scope="request" value="${approve.approve_level}"/>
						</c:if>
				    </c:forEach>
				    
				 </ul>
			</div>
			<div id="app_key_999" name="app_key" style="display:none" class="widget-body">
				 <ul class="history_info">
				    <img src="${ctx }/static/img/auditer1.gif">
				    <table class="table" width="100%" style="margin-top: 5px;border:1.5px solid">
				     <tr>
				    	 <th width="70">状态</th>
				     	<td>
				     		 <c:forEach items="${popDimPolicyStatus}" var="popDimPolicyStatus">
								<c:if test="${policyBaseInfo.policy_status_id== popDimPolicyStatus.id}">${popDimPolicyStatus.name }</c:if>
							 </c:forEach>
				     	</td>
				     </tr>
				    </table>
				 </ul>
			</div>
	</div>
		
	</div>
	<%@ include file="/WEB-INF/layouts/bottom.jsp"%>
</body>
</html>
<script type="text/javascript">
jQuery(document).ready(function() {
	$("#widget_title_title").html("策划信息");
	hideDiv();						
	$("#app_key_0").css('display','block'); 
});
function viewDetail(key,info){
	hideDiv();
	$("#widget_title_title").html(info+"信息");
	var v_key="#app_key_"+key;
	$(v_key).css('display','block'); 
}
function hideDiv(){
	$("div[name='app_key']").each(function(){
	      //var a = $(this).attr("name");
	      $(this).css('display','none'); 
	});
}
</script>