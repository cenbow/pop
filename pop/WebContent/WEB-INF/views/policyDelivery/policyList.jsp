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
</style>
</head>
<body style="overflow: hidden;">
	<!-- 策略列表 start -->
	<div class="widget">
		<div class="widget-title">
			<h4>
				<i class="icon-reorder"></i>策略列表
			</h4>
			<div style="float: right; margin: 5px 19px auto;"
				id="_p_btn_4_send_odd_">
				<button type="button" class="btn btn-small btn-primary"
					id="_p_send_odd_btn_">
					<i class="icon-envelope icon-white"></i> 派单
				</button>
			</div>

		</div>
		<div class="widget-body">
			<form role="form">
				<div class="form-group">
					<table
						class="table table-striped table-bordered table-mcdstyle table-epl"
						style="table-layout: fixed;" id="tbl_rules">
						<thead>
							<tr>
								<th width="20"><input type="checkbox"
									id="_p_list_checkbox_0_" /></th>
								<th>序号</th>
								<th>策略名称</th>
								<th>策略类型</th>
								<th>策略等级</th>
								<th>地市优先级</th>
								<th>策略有效期</th>
<!-- 																<th>策略状态</th> -->
							</tr>
						</thead>
						<tbody>
							<c:forEach items="${pageList.list}" var="policy">
								<tr>
									<td><input type="checkbox" id="${policy.id}"
										et="${policy.end_time}" /></td>
									<td>${policy.id}</td>
									<td><a href="javascript:void(0);"
										onclick="show('${policy.policy_name}', '/policyConfig/edit?id=${policy.id}');">${policy.policy_name}</a></td>
									<td>${policy.policy_type_id}</td>
									<td>${policy.policy_level_id}</td>
									<td>${policy.city_priority}</td>
									<td><fmt:formatDate value="${policy.start_time}"
											pattern="yyyy-MM-dd" /> ~ <fmt:formatDate
											value="${policy.end_time}" pattern="yyyy-MM-dd" /></td>
<%-- 																		<td>${policy.policy_status_id}</td> --%>
								</tr>
							</c:forEach>
						</tbody>
					</table>
					<c:if test="${!empty pageList.list}">
						<tags:pagination pageNum="${pageList.pageNumber}"
							paginationSize="${pageList.pageSize}"
							totalPage="${pageList.totalPage}" totalRow="${pageList.totalRow}" />
					</c:if>
				</div>
			</form>
		</div>
	</div>

	<div id="dialog_4_detail_" style="display: none;">
		<iframe src="" frameborder="0" scrolling="auto" id="_p_dialog_frame"
			style="width: 100%; height: 99%;"></iframe>
	</div>

	<!-- 策略列表 end -->
	<%@ include file="/WEB-INF/layouts/bottom.jsp"%>
	<script type="text/javascript">
		$(function() {
			$('#_p_list_checkbox_0_').on(
					'click',
					function() {
						var isChecked = $(this).attr('checked');
						if (isChecked == 'checked') {
							$('table tbody td input[type="checkbox"]').each(
									function(i, obj) {
										$(obj).attr('checked', true);
										$(obj).parent().addClass('checked');
									});
						} else {
							$('table tbody td input[type="checkbox"]').each(
									function(i, obj) {
										$(obj).attr('checked', false);
										$(obj).parent().removeClass('checked');
									});
						}
					});

			$('#_p_send_odd_btn_').on(
					'click',
					function() {
						var isPass = true;
						var arrs = new Array();
						$('table tbody td input[type="checkbox"]:checked')
								.each(function(i, obj) {
									arrs.push($(obj).attr('id'));
									if (comptime($(obj).attr('et')) < 0) {
										isPass = false;
									}
								});
						if (!isPass) {
							alert('过期策略不能参与派单!');
							return;
						}
						if (arrs.length) {
							alert('准备派单，有' + arrs.length + '个单要派!');
							send_odd(arrs);
							$('#_p_btn_4_send_odd_').hide(600);
						} else {
							alert('请至少选择一条进行派发!');
						}
					});
		});

		function show(name, url) {
			// 			var _p_iframe = top.document.getElementById('showFrame');
			// 			$(_p_iframe).attr('src', '${ctx}' + url);

			//去缓存
			// 			var dialogParent = $(window.parent.document).find("#myModal_div").parent();
			// 			var dialogOwn = $(window.parent.document).find("#myModal_div").clone();
			// 			dialogOwn.hide();
			// 			$(window.parent.document).find("#myModal_frame").attr("src", "${ctx}" + url);
			// 			var myoption = {
			// 				autoOpen : true,
			// 				width : $(window.parent).width() - 40,
			// 				height : "auto",
			// 				title : "查看",
			// 				modal : true,
			// 				resizable : false,
			// 				close : function() {
			// 					dialogOwn.appendTo(dialogParent);
			// 					$(this).dialog("destroy").remove();
			// 				}
			// 			};
			// 			window.parent.mcdDialog("myModal_div", myoption);

			$("#dialog_4_detail_").dialog({
				autoOpen : true,
				width : 1078,
				height : 657,
				title : '[' + name + ']',
				resizable : false,
				modal : true
			});
			$('#dialog_4_detail_ iframe').attr('src', '${ctx}' + url);
		}

		function send_odd(arrs) {
			var _p_time_out_ = 0;
			var tick = setInterval(function() {
				alert('派单中，请稍后...');
				if (_p_time_out_++ > 5) {
					clearInterval(tick);
					alert('派单超时，请稍后重试!');
					$('#_p_btn_4_send_odd_').show(600);
					return;
				}
			}, 1800);
			$.ajax({
				type : 'POST',
				url : '${ctx}/policyDelivery/createPolicyRuleTask',
				data : {
					ids : arrs.join(',')
				},
				dataType : 'json',
				success : function(data) {
					clearInterval(tick);
					if (data.result == 'success') {
						var _p_info_ = (data.message == undefined ? '派单失败'
								: data.message)
								+ '! 其中:派单成功'
								+ filt(data.count_1)
								+ '条，派单失败'
								+ filt(data.count_0) + '条!';
						alert(_p_info_);
					}
					if (data.result == 'failed') {
						alert(data.error);
					}
					window.location.reload();
				},
				error : function(data) {
					clearInterval(tick);
					console.log('error code : ' + data.error);
				}
			});
			$('#_p_btn_4_send_odd_').show(600);
		}

		function comptime(endTime) {
			var currentTime = getCurrentTime();
			var currentTimes = currentTime.substring(0, 10).split('-');
			var endTimes = endTime.substring(0, 10).split('-');

			currentTime = currentTimes[1] + '-' + currentTimes[2] + '-'
					+ currentTimes[0];
			endTime = endTimes[1] + '-' + endTimes[2] + '-' + endTimes[0];

			var a = (Date.parse(endTime) - Date.parse(currentTime)) / 3600 / 1000;
			if (a < 0) {
				return -1;
			} else if (a > 0) {
				return 1;
			} else if (a == 0) {
				return 0;
			} else {
				return 'exception';
			}
		}

		function getCurrentTime() {
			var d = new Date();
			return d.getFullYear() + "-" + complete_with_0_(d.getMonth() + 1)
					+ "-" + d.getDate() + " " + complete_with_0_(d.getHours())
					+ ":" + complete_with_0_(d.getMinutes()) + ":"
					+ complete_with_0_(d.getSeconds()) + ":"
					+ d.getMilliseconds();
		}

		function complete_with_0_(sou) {
			sou = new String(sou);
			return sou.length > 1 ? sou : '0' + sou;
		}

		function filt(msg) {
			return msg != undefined ? msg : 0;
		}
	</script>
</body>
</html>