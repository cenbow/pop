<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>策略列表</title>
<%@ include file="/WEB-INF/layouts/head.jsp"%>
<style>
.table-epl td {
	word-wrap: break-word;
}
.autocut{white-space: nowrap;display: block;overflow: hidden;text-overflow:ellipsis;}
</style>
</head>
<body style="overflow: hidden;">
	<!-- 策略列表 start -->
	<div class="widget">
		<div class="widget-title">
			<h4>
				<i class="icon-reorder"></i>策略列表
			</h4>
		</div>
		<div class="widget-body">
			<form role="form">
				<div class="form-group">
					<table class="table table-striped table-bordered table-mcdstyle table-epl" style="table-layout: fixed;" id="tbl_rules">
						<thead>
							<tr>
								<th width="140">场景编码</th>
								<th width="190">场景名称</th>
								<th width="200">规则名称</th>
								<th>场景类型</th>
								<th>策略等级</th>
								<th width="160">策略有效期</th>
								<th width="100">创建日期</th>
								<th width="140">操作</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach items="${pageList.list}" var="policy">
								<tr>
									<td>${policy.id}</td>
									<td class="autocut"><a href="${ctx}/policyConfig/edit?id=${policy.id}" target="_blank">${policy.policy_name}</a></td>
									<td style="white-space: nowrap;overflow: hidden;text-overflow:ellipsis;" title="${policy.policy_rule_name}">${policy.policy_rule_name}</td>
									<td style="text-align: center;" title="${policy.policy_type_name}">${policy.policy_type_name}</td>
									<td style="text-align: center;" title="${policy.policy_level_name}">${policy.policy_level_name}</td>
									<td style="text-align: center;" title="${policy.valid_start_date} ~ ${policy.valid_end_date}">${policy.valid_start_date} ~ ${policy.valid_end_date}</td>
									<td style="text-align: center;" title="${policy.create_date}">${policy.create_date}</td>
							        <td style="text-align: center;">
							         	<c:if test="${policy.can_edit }">
										<button type="button" class="btn btn-small btn-primary" onclick="open_dialog('修改规则','${ctx}/policyConfig/edit?id=${policy.id}');">
											<i class="icon-edit icon-white"></i>修改
										</button>
										<button type="button" class="btn btn-small btn-danger" onclick="del('${policy.id}')">
											<i class="icon-remove icon-white"></i>删除
										</button>
							         	</c:if>
							         	<c:if test="${!policy.can_edit }">
										<button type="button" class="btn btn-small btn-primary"  onclick="open_dialog('查看规则','${ctx}/policyConfig/edit?id=${policy.id}');" data-toggle="modal">
											<i class="icon-info-sign icon-white"></i>查看
										</button>
							         	</c:if>
							        </td>	
								</tr>
							</c:forEach>
						</tbody>
					</table>
				</div>
			</form>
			<c:if test="${!empty pageList.list}">
				<tags:pagination pageNum="${pageList.pageNumber}" paginationSize="${pageList.pageSize}"
					totalPage="${pageList.totalPage}" totalRow="${pageList.totalRow}" />
			 </c:if>
		</div>
	</div>
	<!-- 策略列表 end -->
	<%@ include file="/WEB-INF/layouts/bottom.jsp"%>
</body>
<script type="text/javascript">
/**打开模态对话框*/
function open_dialog(title,src){
	$("#myModal_frame", window.parent.document).attr("src",src);
	//去缓存
    var dialogParent = $("#myModal_div", window.parent.document).parent();
    var dialogOwn = $("#myModal_div", window.parent.document).clone();
    dialogOwn.hide();
    $("#myModal_div", window.parent.document).dialog({
        autoOpen: true,
        title: title,
        width: 1000,
		height:"auto",
		resizable:false,
		modal: true,
        close: function () {
        	dialogOwn.appendTo(dialogParent);
        	$(this).dialog("destroy").remove(); 
        }
    });
}
</script>
</html>