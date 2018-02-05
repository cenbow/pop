<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %> 
<%@ taglib uri="http://bi.asiainfo.com/biframe/privilege" prefix="pri"%>
<%@ page import="java.util.*"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<head>
<title>用户策略管理查询结果列表</title>
<%@ include file="/WEB-INF/layouts/bottom.jsp"%>
<%@ include file="/WEB-INF/layouts/head.jsp"%>
<style type="text/css">
#tbl_policyBaseInfoAndRules button{ margin: 3px 2px;}
.widget-title div.button-group{float: right;padding-right: 23px;padding-top: 5px;}
.table-epl td {
	word-wrap: break-word;
}
.autocut{white-space: nowrap;overflow: hidden;text-overflow:ellipsis;}
</style>
<script type="text/javascript">
jQuery(document).ready(function() {
	 $("#selectAll").click(function(){
		 $('input[name="selectedPolicyID"]').each(function(){
				this.click(); 
		});
	 });
});
function queryPolicyExecInfo(ruleId,productNo){
	//去缓存
    var dialogParent = $(window.parent.document).find("#policyExex_div").parent();
    var dialogOwn = $(window.parent.document).find("#policyExex_div").clone();
    dialogOwn.hide();
	$(window.parent.document).find("#policyExexl_frame").attr("src","${ctx}/policyUserManage/queryPolicyExecInfo?ruleId="+ruleId+"&productNo="+productNo);
	var myoption={
		autoOpen: true,
		width: $(window.parent).width()-40,
		width: "500px",
		height:"300",
		title:"策略执行信息",
		modal: true,
		resizable:false,
        close: function () { 
        	dialogOwn.appendTo(dialogParent);
        	$(this).dialog("destroy").remove(); 
        }
	};
	window.parent.mcdDialog("policyExex_div",myoption);
}
//终止用户策略签约信息
function stopPolicy(ruleData,policyRuleName){
	var myoption={
		autoOpen: true,
		width: 430,
		height:"auto",
		title:"终止",
		modal: true,
		resizable:false
	};
	$(window.parent.document).find("#pausePolicyruleData").val(ruleData);
	$(window.parent.document).find("#pausePolicyRuleName").val(policyRuleName);
	//$(window.parent.document).find("#terminaterTaskConfirmWords").append("您确认终止策略【"+policyRuleName+"】规则？");
	$(window.parent.document).find("#terminaterTaskConfirmWords").text("您确认终止这用户签约信息吗？");
	window.parent.mcdDialog("myModal_terminaterTask",myoption);
}
function getSelected(){
	var ids='';
	$('input[name="selectedPolicyID"]:checked').each(function(){
          var v=$(this).val();
           ids=ids+','+v;
      });
   return ids.substring(1,ids.length);
}
function batchStopPolicy(){
	var ruleData=getSelected();
	if(ruleData!=null&&$.trim(ruleData).length>0){
		stopPolicy(ruleData,"");
	}else{
		alert("请至少选择一个策略之后再操作！");
	}
}
</script>
</head>
<body style="overflow: hidden;">
	<div class="widget">
		<div class="widget-title">
			<h4><i class="icon-reorder"></i>查询结果</h4>
			<div class="button-group">
			<pri:rightPresent roleType ="2" resourceType="1001" actionId="BI-POP-STOP" resourceId="BI-POP" operationType="10">
				<button type="button" class="btn btn-small btn-primary" onclick="batchStopPolicy()">
					<i class="icon-white"></i>批量终止用户签约
				</button>
			</pri:rightPresent>
			</div>
		</div>
	<div class="widget-body">
			<form role="form">
				<div class="form-group">
	<!-- <table id="tbl_policyBaseInfoAndRules" class="table table-striped table-bordered table-mcdstyle"  style="table-layout: fixed;"> -->
	<table class="table table-striped table-bordered table-mcdstyle table-epl" style="table-layout: fixed;" id="tbl_rules">
	<thead>
		<tr>
			<th style="width: 15px;"><input name="selectAll" type="checkbox" id="selectAll"/></th>
			<th style="width: 120px;">策略编码</th>
			<th style="width: 85px;">规则名称</th>
			<th style="width: 105px;">策略规则内容</th>
			<th style="width: 75px;">策略等级</th>
			<th style="width: 75px;">策略类型</th>
			<th style="width: 70px">地市优先级</th>
			<th style="width: 120px;">策略签约时间</th>
			<th style="width: 80px;">策略签约状态</th>
			<th >操作</th>
		</tr>
	</thead>
	<tbody>
		<c:if test="${fn:length(policyUserInfoList) == 0}">
			<c:if test="${visitsTimes == '1'}">
			 <tr><td colspan="9" style="background-color:#DDDDDD;text-align:center;"><font color="red" >没有符合条件的数据!</font></td></tr>
			</c:if>
		</c:if>
		<c:forEach items="${policyUserInfoList}" var="policyItem">
			<tr class="" id="row_${policyItem.ruleId}">
				<td><input name="selectedPolicyID" type="checkbox" id="selectedPolicyID" value="${policyItem.ruleId}_${policyItem.productNo}"/></td>
				<td style="text-align:left;">${policyItem.ruleId}</td>				
				<c:if test="${not empty policyItem.policyRuleName && fn:length(policyItem.policyRuleName) < 10}">
					<td style="text-align:center;">${policyItem.policyRuleName}</td>
				</c:if>
<%-- 				<c:if test="${policyItem.policyRuleName!=null&&policyItem.policyRuleName.length() >= 10}"> --%>
				<c:if test="${fn:length(policyItem.policyRuleName) >= 10}">
				
					<td class="hidden-phone autocut" title="${policyItem.policyRuleName}">${policyItem.policyRuleName}</td>
				</c:if>
				
				<c:if test="${not empty policyItem.policyRuleContent && fn:length(policyItem.policyRuleContent) < 10}">
					<td style="text-align:center;">${policyItem.policyRuleContent}</td>
				</c:if>
				<c:if test="${fn:length(policyItem.policyRuleContent) >= 10}">
					<td class="hidden-phone autocut" title="${policyItem.policyRuleContent}">${policyItem.policyRuleContent}</td>
				</c:if>
				<td style="text-align:center;">${policyItem.policyLevelName}</td>
				<td style="text-align:left;">${policyItem.policyTypeName}</td>
				<td style="text-align:center;">${policyItem.cityPriorityName}</td>
				<td style="text-align:left;"><fmt:formatDate value="${policyItem.policySignTime}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
				<td style="text-align:center;">${policyItem.policySignStatusName}</td>
				<td class="hidden-phone">
					<div id="operDiv_${policyItem.ruleId}">
						<button type="button" class="btn btn-small btn-primary"  onclick="queryPolicyExecInfo('${policyItem.ruleId}','${policyItem.productNo}')" data-toggle="modal"><i class="icon-white"></i> 执行信息  </button>
						 <pri:rightPresent roleType ="2" resourceType="1001" actionId="BI-POP-STOP" resourceId="BI-POP" operationType="10">
							<button type="button" class="btn btn-small btn-primary"  onclick="stopPolicy('${policyItem.ruleId}_${policyItem.productNo}','${policyItem.policyRuleName}')" data-toggle="modal"  <c:if test="${policyItem.policyLevelId != POP_POLICY_LEVEL_USER}">disabled</c:if>><i class="icon-white"></i> 终止用户签约  </button>
						</pri:rightPresent>
					</div>		
				</td>
			</tr>
		</c:forEach>
	</tbody>
	</table>
	<c:if test="${fn:length(policyUserInfoList) != 0}">
		<tags:pagination pageNum="${pageNumberOfPolicySceneManage}"
		 paginationSize="${pageSizeOfPolicySceneManage}" 
		 totalPage="${totalPageOfPolicySceneManage}" totalRow="${totalRowOfPolicySceneManage}" />
	</c:if>
	</div id="footer">
	</form>
	</div>
	</div>
</body>
</html>