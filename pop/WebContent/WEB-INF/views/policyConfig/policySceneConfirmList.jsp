<%@page import="com.asiainfo.biframe.utils.date.DateUtil"%>
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib uri="http://bi.asiainfo.com/taglibs/dic" prefix="dic"%>
<%@page import="java.util.Date"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="/WEB-INF/layouts/head.jsp"%>
<meta charset="utf-8" />
<title>策略确认</title>
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
<body  style="overflow: hidden;">
<div class="widget">
	    <div class="widget-title">
			<h4>
				<i class="icon-reorder"></i>查询结果
				<input type="hidden" name="sucFlag" id="sucFlag" value="${sucFlag}"/>
			</h4>
		</div>
	<div id="mainDiv" class="widget-body">
		<div class="form-group">
			<table
				class="table table-striped table-bordered table-mcdstyle table-epl"
				style="table-layout: fixed;">
				<thead>
					<tr>
					    <th width="3%"><input name="selectAll" type="checkbox" id="selectAll"/></th>
						<th width="16%">策略名称</th>
						<th width="10%">策略类型</th>
						<th width="6%">策略等级</th>
						<th width="8%">策略状态</th>
						<th width="14%">策略有效期</th>
						<th width="12%">创建人/部门</th>
						<th width="8%">地市优先级</th>
						<th width="12%">创建时间</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${popPolicyBaseinfo}" var="policyinfo">
						<tr id="row_${policyinfo.id }">
							<td><input name="selectedPolicyID" type="checkbox" id="selectedPolicyID" value="${policyinfo.id}"/></td>
							<td><a title="查看详情" href="javascript:dialog_view('${policyinfo.id}');">${policyinfo.policy_name }</a></td>
							<td>
								<c:forEach items="${selectDimPolicyTypes}" var="dimPolicyType1">
									<c:if test="${policyinfo.policy_type_id== dimPolicyType1.id }">${dimPolicyType1.name}</c:if>
								</c:forEach></td>
							 <td>
							     <c:forEach items="${dimPolicyLevels}" 	var="dimPolicyLevel">
									<c:if test="${policyinfo.policy_level_id== dimPolicyLevel.id}">${dimPolicyLevel.name }</c:if>
								 </c:forEach>
							 </td>
							 <td>
							  	<c:forEach items="${popDimPolicyStatus}" var="popDimPolicyStatus">
									<c:if test="${policyinfo.policy_status_id== popDimPolicyStatus.id}">${popDimPolicyStatus.name }</c:if>
								</c:forEach>
							 </td>
							<td><fmt:formatDate value="${policyinfo.start_time}" pattern="yyyy-MM-dd" /> ~ <fmt:formatDate value="${policyinfo.end_time}" pattern="yyyy-MM-dd" /></td>
							<td>${policyinfo.create_user_id}</td>
							<td>${policyinfo.city_priority}</td>
							<td><fmt:formatDate value="${policyinfo.create_time}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
		<dic:page name="splitPage" functionName="gopage" linkPath="${ctx}/policyApproval/confirmSeachList" key="0"/>
		<br>
		<div  style="text-align: right;">
			 <input id="btn_save_template" class="btn btn-small btn-primary" type="button" onclick="confirm('1')" value="通过" />&nbsp; 
			 <input id="btn_submit" class="btn btn-small btn-primary" type="button" onclick="confirm('2')" value="不通过" />
	  </div>
	</div>
</div>	
<%@ include file="/WEB-INF/layouts/bottom.jsp"%>
<script type="text/javascript">
jQuery(document).ready(function() {
	 $("#selectAll").click(function(){
		 $('input[name="selectedPolicyID"]').each(function(){
				this.click(); 
		});
	 });
});
function getSelected(){
	var ids='';
	$('input[name="selectedPolicyID"]:checked').each(function(){
          var v=$(this).val();
           ids=ids+','+v;
      });
   return ids.substring(1,ids.length);
}
function confirm(type){
	var id=getSelected();
		if(id!=null&&typeof(id)!= "undefined"&&$.trim(id).length>0){
			if(type=='1'){
				var url="${ctx}/policyApproval/approvalConfirmPass?id="+id;
		    	location.href=url;
			}else if(type=='2'){
				var url="${ctx}/policyApproval/approvalConfirmNotPassInit?id="+id;
				parent.open_dialog('确认意见',url,500,300);
			}
		}else{
			alert("请至少选择一个策略之后再操作！");
		}
	}
function dialog_view(id){
	url="${ctx}/policyConfig/view?id="+id;
	parent.open_dialog('查看',url,$(window.parent).width()-40,"auto");
}
function gopage(url){
  parent.searchPageConfirmList(url);
}
var submitFlag=$("#sucFlag").val();
if(submitFlag==1){
	alert('确认通过成功');
}
</script>
</body>
</html>