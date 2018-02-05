<%@ page language="java" import="java.util.*"
	contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ page import="com.ai.bdx.pop.model.PopPolicyBaseinfo"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="/WEB-INF/layouts/head.jsp"%>
<style type="text/css">
label {
	cursor: text;
}
</style>
</head>

<body onload="_p_query();">
	<br>
	<!-- BEGIN CONTAINER -->
	<div id="container" class="row-fluid">
		<!-- BEGIN PAGE -->
		<div class="container-fluid">
			<!-- BEGIN ADVANCED TABLE widget-->
			<div class="row-fluid">
				<div class="span12">
					<!-- BEGIN EXAMPLE TABLE widget-->
					<div class="widget">
						<div class="widget-title">
							<h4>
								<i class="icon-reorder"></i>策略查询
							</h4>
							<span class="tools"> <a href="javascript:;"
								class="icon-chevron-down"></a>
							</span>
						</div>
						<div class="widget-body">
							<form id="searchForm" name="searchForm" action="${ctx}/policyConfig/search" method="post"
								class="form-horizontal">
								<input type="hidden" name="popPolicyBaseinfoBean.policyTemplateFlag" value="0">
								<table class="queryTable table table-noborder">
									<tbody>
										<tr>
											<th width="70"><label
												class="">场景名称：</label></th>
											<td ><input name="popPolicyBaseinfoBean.policyName"
												class="ztreeCombox" type="text" value=""
												style="width: 172px;" /></td>
											<th width="70"><label
												class="">场景类型：</label></th>
											<td ><select
												class="form-control" style="width: 200px;"
												name="popPolicyBaseinfoBean.policyTypeId">
													<option value="">--请选择--</option>
													<c:forEach items="${dimPolicyTypes }" var="type">
														<option value="${type.id }">${type.name }</option>
													</c:forEach>
											</select></td>
											<th width="70"><label
												class="">策略等级：</label></th>
											<td ><select
												class="form-control" style="width: 186px;"
												name="popPolicyBaseinfoBean.policyLevelId">
													<option value="">--请选择--</option>
													<c:forEach items="${dimPolicyLevels }" var="level">
														<option value="${level.id }">${level.name }</option>
													</c:forEach>
											</select></td>
										</tr>
										<tr>
											<th width="70"><label class="">策略状态：</label></th>
											<td><select
												class="form-control" style="width: 186px;"
												name="popPolicyBaseinfoBean.policyStatusId">
													<option value="">--请选择--</option>
													<c:forEach items="${dimPolicyStatus }" var="status">
														<option value="${status.id }">${status.name }</option>
													</c:forEach>
											</select></td>
											<th width="100"><label
												class="">创建日期：</label></th>
											<td ><input id="start_time"
												name="popPolicyBaseinfoBean.createStartDate" value="" type="text" style="width: 80px;" />
												- <input id="end_time" name="popPolicyBaseinfoBean.createEndDate" value="" type="text"
												style="width: 80px;" /></td>
											<td></td><td></td>
										</tr>
										<tr>
											<td colspan="6" class="reset_btns" style="text-align: right;">
												<button type="reset" class="btn btn-small btn-primary">
													<i class="icon-search icon-white"></i> 清空
												</button>
												<button type="button" class="btn btn-small btn-primary" onclick="_p_query()">
													<i class="icon-search icon-white"></i> 搜索
												</button>
											</td>
										</tr>
									</tbody>
								</table>
							</form>
						</div>
					</div>
					<div>
						<iframe id="policy_list_frame" name="policy_list_frame" width="100%" frameborder="0"
							style="background: #ffffff; height: 740px;" src=""></iframe>
					</div>
				</div>
			</div>
		</div>
	</div>


	<div id="footer">
		2014 &copy; Asiainfo
		<div class="span pull-right">
			<span class="go-top"><i class="icon-arrow-up"></i></span>
		</div>
	</div>
	<!-- END FOOTER -->
	<div id="chartsModal_div" style="display: none;">
		<iframe id="chartsModal_frame" scrolling="no"
			name="chartsModal_update_frame" align="top" width="100%"
			height="620px" frameborder="0"></iframe>
	</div>
	<div id="myModal_div" style="display:none;">
			<iframe id="myModal_frame" scrolling="no" name="myModal_update_frame" align="top" width="100%" height="620px" frameborder="0"></iframe>
	</div>
	<%@ include file="/WEB-INF/layouts/bottom.jsp"%>
	<script type="text/javascript">
		$(function() {
			$("#start_time").datepicker({
				format : "yyyy-mm-dd"
			}).on("changeDate", function(e) {
				start_time = e.date.valueOf();
				//选中开始时间判断，结束时间必须大于开始时间
				var endDateStr = end_time.valueOf();
				if (endDateStr != "") {
					if (end_time < start_time) {
						alert("开始时间必须小于等于结束时间！");
						$("#start_time").val("");
					}
				}

			});
			$("#end_time").datepicker({
				format : "yyyy-mm-dd"
			}).on("changeDate", function(e) {
				end_time = e.date.valueOf();
				//选中结束时间判断，开始时间必须小于结束时间
				var startDateStr = $("#start_time").val();
				if (startDateStr != "") {
					if (start_time > end_time) {
						alert("结束时间必须大于等于开始时间！");
						$("#end_time").val("");
					}
				}
			});
		});
		function _p_query() {
			var frm = document.getElementById('searchForm');
			frm.action = '${ctx}/policyConfig/search?isPageSearch=false';
			frm.target = 'policy_list_frame';
			frm.submit();
		}
	</script>
</body>

</html>
