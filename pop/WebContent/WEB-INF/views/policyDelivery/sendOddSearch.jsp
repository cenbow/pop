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
							<form id="searchForm" name="searchForm" action="#" method="post"
								class="form-horizontal">
								<table class="queryTable table table-noborder">
									<tbody>
										<tr>
											<th style="text-align: right;" width="70"><label
												class="">策略名称：</label></th>
											<td style="text-align: left;"><input name="policy_name"
												class="ztreeCombox" type="text" value=""
												style="width: 278px;" /></td>
											<th style="text-align: right;" width="70"><label
												class="">策略类型：</label></th>
											<td style="text-align: left;"><select
												class="form-control" style="width: 172px;"
												name="policy_type_id">
													<option value=""></option>
													<c:forEach items="${dimPolicyTypes }" var="type">
														<option value="${type.id }">${type.name }</option>
													</c:forEach>
											</select></td>
											<th style="text-align: right;" width="70"><label
												class="">策略等级：</label></th>
											<td style="text-align: left;"><select
												class="form-control" style="width: 186px;"
												name="policy_level_id">
													<option value=""></option>
													<c:forEach items="${dimPolicyLevels }" var="level">
														<option value="${level.id }">${level.name }</option>
													</c:forEach>
											</select></td>
										</tr>
										<tr>
											<!-- 											<th style="text-align: right;" width="70"><label -->
											<!-- 												class="">策略状态：</label></th> -->
											<!-- 											<td style="text-align: right;"><select -->
											<!-- 												class="form-control" style="width: 186px;" -->
											<!-- 												name="policy_status_id"> -->
											<!-- 													<option value="">--请选择--</option> -->
											<%-- 													<c:forEach items="${dimPolicyStatus }" var="status"> --%>
											<%-- 														<option value="${status.id }">${status.name }</option> --%>
											<%-- 													</c:forEach> --%>
											<!-- 											</select></td> -->
											<th style="text-align: right;" width="100"><label
												class="">策略有效期：</label></th>
											<!-- 											<td style="text-align: right;"><input id="start_time" -->
											<!-- 												name="start_time" value="" type="text" style="width: 80px;" /><span -->
											<!-- 												class="add-on"><i class="icon-remove"></i></span> <span -->
											<!-- 												class="add-on"><i class="icon-th"></i></span> - <input -->
											<!-- 												id="end_time" name="end_time" value="" type="text" -->
											<!-- 												style="width: 80px;" /><span class="add-on"><i -->
											<!-- 													class="icon-remove"></i></span> <span class="add-on"><i -->
											<!-- 													class="icon-th"></i></span></td> -->
											<td style="text-align: left;"><span
												class="controls input-append date form_date"
												style="margin-left: 0px;" data-date-format="yyyy-mm-dd">
													<input type="text" id="start_time" name="start_time"
													style="width: 70px; cursor: pointer;" readonly="readonly">
													<span class="add-on" id="start_time_clear"><i
														class="icon-remove"></i></span> <span class="add-on"><i
														class="icon-th"></i></span>
											</span>&nbsp;--&nbsp;<span
												class="controls input-append date form_date"
												style="margin-left: 0px;" data-date-format="yyyy-mm-dd">
													<input type="text" id="end_time" name="end_time"
													style="width: 70px; cursor: pointer;" readonly="readonly">
													<span class="add-on" id="end_time_clear"><i
														class="icon-remove"></i></span> <span class="add-on"><i
														class="icon-th"></i></span>
											</span></td>
										</tr>
										<tr>
											<td colspan="6" class="query_btns">
												<button type="button" id="search_button"
													class="btn btn-small btn-primary">
													<i class="icon-search icon-white"></i> 搜索
												</button>
												<button class="btn btn-small btn-primary"
													onclick="clearForm()" type="reset">
													&nbsp;&nbsp;清空&nbsp;&nbsp;</button>
											</td>
										</tr>
									</tbody>
								</table>
							</form>
						</div>
					</div>
					<div>
						<iframe id="policy_list_frame" width="100%" frameborder="0"
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
	<%@ include file="/WEB-INF/layouts/bottom.jsp"%>
	<script type="text/javascript">
		var startDate, endDate;
		$(function() {
			//开始日期
			$(".form_date").datetimepicker({
				language : 'zh-CN',
				weekStart : 1,
				todayBtn : 1,
				autoclose : 1,
				todayHighlight : 1,
				startView : 2,
				minView : 2,
				forceParse : 0,
				format : "yyyy-mm-dd"
			}).on("changeDate", function(e) {
				startDate = e.date.valueOf();
				var startDateStr, endDateStr;
				startDateStr = $("#start_time").val();
				endDateStr = $("#end_time").val();
				if (endDateStr != "" && endDateStr != undefined) {
					if (dateComp(startDateStr, endDateStr)) {
						alert("开始日期必须小于等于结束日期！");
						$("#end_time").val("");
					}
				}
			});

			//结束日期
// 			$("#end_time").datetimepicker({
// 				language : 'zh-CN',
// 				weekStart : 1,
// 				todayBtn : 1,
// 				autoclose : 1,
// 				todayHighlight : 1,
// 				startView : 2,
// 				minView : 2,
// 				forceParse : 0,
// 				format : "yyyy-mm-dd"
// 			}).on("changeDate", function(e) {
// 				endDate = e.date.valueOf();
// 				var startDateStr, endDateStr;
// 				startDateStr = $("#start_time").val();
// 				endDateStr = $("#end_time").val();
// 				if (startDateStr != "" && startDateStr != undefined) {
// 					if (dateComp(startDateStr, endDateStr)) {
// 						alert("结束日期必须大于等于开始日期！");
// 						$("#end_time").val("");
// 					}
// 				}
// 			});

			// 			$("#start_time_span").datetimepicker({
			// 							language : 'zh-CN',
			// 							weekStart : 1,
			// 							todayBtn : 1,
			// 							autoclose : 1,
			// 							todayHighlight : 1,
			// 							startView : 2,
			// 							minView : 2,
			// 							forceParse : 0,
			// 				initialDate : new Date(),
			// 				startDate : new Date(),
			// 				format : "yyyy-mm-dd"
			// 			}).on("changeDate", function(e) {
			// 				start_time = e.date.valueOf();
			// 				//选中开始时间判断，结束时间必须大于开始时间
			// 				var endDateStr = $("#end_time").val();
			// 				if (endDateStr != "") {
			// 					if (end_time < start_time) {
			// 						alert("开始时间必须小于等于结束时间！");
			// 						$("#start_time").val("");
			// 					}
			// 				} else {
			// 					$("#start_time").val(start_time);
			// 				}
			// 			});
			// 			$("#end_time_span").datetimepicker({
			// 				language : 'zh-CN',
			// 				weekStart : 1,
			// 				todayBtn : 1,
			// 				autoclose : 1,
			// 				todayHighlight : 1,
			// 				startView : 2,
			// 				minView : 2,
			// 				forceParse : 0,
			// 				initialDate : new Date(),
			// 				startDate : new Date(),
			// 				format : "yyyy-mm-dd"
			// 			}).on("changeDate", function(e) {
			// 				end_time = e.date.valueOf();
			// 				//选中结束时间判断，开始时间必须小于结束时间
			// 				var startDateStr = $("#start_time").val();
			// 				if (startDateStr != "") {
			// 					if (start_time > end_time) {
			// 						alert("结束时间必须大于等于开始时间！");
			// 						$("#end_time").val("");
			// 					}
			// 				}
			// 			});

			$('#start_time_clear').on('click', function() {
				$('#start_time').val('');
			});
			$('#end_time_clear').on('click', function() {
				$('#end_time').val('');
			});

			$('#search_button').on('click', function() {
				_p_query();
			});

		});

		function _p_query() {
			var url = '${ctx}/policyDelivery/searchList?';
			$('#policy_list_frame').attr('src',
					url + $('#searchForm').serialize());
		}

		function clearForm() {
			$("#display-policyTypeIds").val("value", "");
			$("#display-policyLevelIds").attr("value", "");
			$("#display-policyControlTypeIds").attr("value", "");
			$("input[name='policyTypeId']").attr("value", "");
			$("input[name='policyLevelId']").attr("value", "");
			$("input[name='policyControlTypeId']").attr("value", "");
			$("#searchForm").attr("value", "1");

		}

		function dateComp(d1, d2) {

			var date1 = new Date(Date.parse(d1.replace("-", "/")));

			var date2 = new Date(Date.parse(d2.replace("-", "/")));

			var r = (date1 - date2) / (24 * 60 * 60 * 1000);

			if (r > 0) {
				return true;

			} else {
				return false;
			}

		}
	</script>
</body>

</html>
