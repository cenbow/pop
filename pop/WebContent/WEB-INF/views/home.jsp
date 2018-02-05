<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>首页</title>
<%@ include file="/WEB-INF/layouts/head.jsp"%>
<meta http-equiv="X-UA-Compatible" content="IE=edge,Chrome=1" />
<style type="text/css">
body {
	height: 100%;
}

._p_chart {
	float: left;
	width: 100%;
	height: 320px;
	text-align: center;
}

._p_fieldset {
	border: 1px solid #ccc;
}

._p_legend {
	border: none;
	width: 100px;
	text-align: center;
	margin-left: 10px;
}
</style>
<script type="text/javascript" src="${ctx}/static/js/p_chart.js"></script>
<script type="text/javascript" src="${ctx}/static/js/esl.js"></script>
</head>
<body style="overflow: auto;">

	<div class="alert alert-info">
		<button data-dismiss="alert" class="close">×</button>
		欢迎${sessionScope.userName}使用 <strong>POP运营平台</strong> . 以下是指引图!
	</div>

	<div class="row-fluid circle-state-overview" style="margin-top: 20px;">
		<div class="span2 responsive clearfix" data-tablet="span3"
			data-desktop="span2">
			<div class="circle-wrap" onclick="toPage('addPolicy')">
				<div class="stats-circle turquoise-color" title="点击跳转到策划基本信息页面。">
					<i class="icon-th-list"></i>
				</div>
				<p>策划基本信息</p>
			</div>
		</div>
		<div class="span2 responsive" data-tablet="span3" data-desktop="span2">
			<div style="float: left; margin-top: 43px;">
				<i class="icon-arrow-right"></i>
			</div>
			<div class="circle-wrap" onclick="alert('请先策划基本信息！');">
				<div class="stats-circle red-color" title="先策划基本信息，再策划规则。">
					<i class="icon-cog"></i>
				</div>
				<p>策划规则</p>
			</div>
		</div>
		<div class="span2 responsive" data-tablet="span3" data-desktop="span2">
			<div style="float: left; margin-top: 43px;">
				<i class="icon-arrow-right"></i>
			</div>
			<div class="circle-wrap" onclick="alert('请先策划规则！');">
				<div class="stats-circle green-color" title="策划规则时选择用户策略。">
					<i class="icon-inbox"></i>
				</div>
				<p>选择用户策略</p>
			</div>
		</div>
		<div class="span2 responsive" data-tablet="span3" data-desktop="span2">
			<div style="float: left; margin-top: 43px;">
				<i class="icon-arrow-right"></i>
			</div>
			<div class="circle-wrap" onclick="alert('请先策划规则！');">
				<div class="stats-circle gray-color" title="策划规则时选择客户群。">
					<i class="icon-comments-alt"></i>
				</div>
				<p>选择客户群</p>
			</div>
		</div>
		<div class="span2 responsive" data-tablet="span3" data-desktop="span2">
			<div style="float: left; margin-top: 43px;">
				<i class="icon-arrow-right"></i>
			</div>
			<div class="circle-wrap" onclick="toPage('approval');">
				<div class="stats-circle green-color" title="审批">
					<i class="icon-check"></i>
				</div>
				<p>审批</p>
			</div>
		</div>
		<div class="span2 responsive" data-tablet="span3" data-desktop="span2">
			<div style="float: left; margin-top: 43px;">
				<i class="icon-arrow-right"></i>
			</div>
			<div class="circle-wrap" onclick="toPage('delivery');">
				<div class="stats-circle blue-color" title="派单">
					<i class="icon-envelope"></i>
				</div>
				<p>派单</p>
			</div>
		</div>
	</div>


	<table class="commonTable" width="98%" height="100%" align="center"
		style="padding-left: 20px; padding-right: 20px;">
		<tr>
			<td width="60%" height="99%">
				<div class="row-fluid">
					<div class="span12">
						<!-- BEGIN RECENT ORDERS PORTLET-->
						<div class="widget">
							<div class="widget-title">
								<h4>
									<i class="icon-tags"></i>待办列表
								</h4>
								<span class="tools"> <a href="javascript:;"
									class="icon-chevron-down"></a>
								</span>
							</div>
							<div class="widget-body">
								<table width="100%" height="260"
									class="table table-striped table-bordered table-advance table-hover">
									<thead>
										<tr>
											<th width="180"><span class="hidden-phone">策略名称</span></th>
											<th width="90"><span class="hidden-phone ">创建日期</span></th>
											<th width="180"><span class="hidden-phone">创建人/部门</span></th>
											<th width="90"><span>操作</span></th>
										</tr>
									</thead>
									<tbody>
										<c:if test="${empty scheduleList}">
											<tr>
												<td colspan="4" height="99%">没有待办事项</td>
											</tr>
										</c:if>
										<c:forEach items="${scheduleList}" var="schedule">
											<tr>
												<td width="180">${schedule.policyName}</td>
												<td width="90"><fmt:formatDate
														value="${schedule.createDate}" pattern="yyyy-MM-dd" /></td>
												<td width="180">${schedule.creator}</td>
												<td width="90">
													<button type="button" class="btn btn-small btn-primary"
														onclick="invokepages(this);" mat="${schedule.sid}">
														<i class="icon-glass icon-white"></i>
														${schedule.operation}
													</button>
												</td>
											</tr>
										</c:forEach>
									</tbody>
								</table>
								<div class="space7"></div>
								<table width="100%">
									<tr>
										<td colspan="4" align="right"><a
											style="margin: 0px 8px 10px 0px" href="javascript:void(0);"
											onclick="show('待办列表', '${ctx}/home/show')">&#62;&#62;&#62;更多</a></td>
									</tr>
								</table>
							</div>
						</div>
						<!-- END RECENT ORDERS PORTLET-->
					</div>
				</div>

			</td>
			<td width="40%" height="99%">
				<div class="row-fluid">
					<div class="span12">
						<!-- BEGIN RECENT ORDERS PORTLET-->
						<div class="widget">
							<div class="widget-title">
								<h4>
									<i class="icon-bell"></i>系统信息
								</h4>
								<span class="tools"> <a href="javascript:;"
									class="icon-chevron-down"></a> <!-- 									<a href="javascript:;" class="icon-remove"></a> -->
								</span>
							</div>
							<div class="widget-body">
								<div class="slimScrollDiv"
									style="position: relative; overflow: hidden; width: auto; height: 264px;">
									<ul class="item-list scroller padding" data-height="260"
										data-always-visible="1"
										style="overflow: hidden; width: auto; height: 260px;">
										<c:if test="${empty sysInfoList}">
											<li><span class="label label-success"><i
													class="icon-bell"></i></span> <span>没有更多数据</span></li>
										</c:if>
										<c:forEach items="${sysInfoList}" var="sysInfo"
											varStatus="index">
											<c:if test="${sysInfo.info_type eq '0'}">
												<li><span class="label label-success"><i
														class="icon-bell"></i></span> <span>${sysInfo.content}.</span> <span
													class="small italic"><fmt:formatDate
															value="${sysInfo.create_time}"
															pattern="yyyy-MM-dd HH:mm:ss" /></span></li>
											</c:if>
											<c:if test="${sysInfo.info_type eq '2'}">
												<li><span class="label label-warning"><i
														class="icon-bullhorn"></i></span> <span>${sysInfo.content}.</span>
													<span class="small italic"><fmt:formatDate
															value="${sysInfo.create_time}"
															pattern="yyyy-MM-dd HH:mm:ss" /></span></li>
											</c:if>
										</c:forEach>
									</ul>
									<!-- 									<div class="slimScrollBar ui-draggable" -->
									<!-- 										style="width: 7px; position: absolute; top: 0px; opacity: 0.4; display: block; border-top-left-radius: 7px; border-top-right-radius: 7px; border-bottom-right-radius: 7px; border-bottom-left-radius: 7px; z-index: 99; right: 1px; height: 232.504363001745px; background: rgb(0, 0, 0);"></div> -->
									<!-- 									<div class="slimScrollRail" -->
									<!-- 										style="width: 7px; height: 100%; position: absolute; top: 0px; display: none; border-top-left-radius: 7px; border-top-right-radius: 7px; border-bottom-right-radius: 7px; border-bottom-left-radius: 7px; opacity: 0.2; z-index: 90; right: 1px; background: rgb(51, 51, 51);"></div> -->
								</div>
								<div class="space5"></div>
								<div style="text-align: right;">
									<a style="margin: 0px 8px 10px 0px" href="javascript:void(0);"
										onclick="show('系统信息', '${ctx}/home/findSystemInfoPage');">&#62;&#62;&#62;更多</a>
								</div>
								<div class="clearfix no-top-space no-bottom-space"></div>
							</div>
						</div>
						<!-- END RECENT ORDERS PORTLET-->
					</div>
				</div>
			</td>
		</tr>

		<tr>
			<td width="100%" height="280" colspan="2">
				<div class="row-fluid">
					<div class="span12">
						<!-- BEGIN RECENT ORDERS PORTLET-->
						<div class="widget">
							<div class="widget-title">
								<h4>
									<i class="icon-tags"></i>我的策略
								</h4>
								<span class="tools"> <a href="javascript:;"
									class="icon-chevron-down"></a>
								</span>
							</div>
							<div class="widget-body">
								<table width="100%" height="260"
									class="table table-striped table-bordered table-advance table-hover">
									<thead>
										<tr>
											<th><span class="hidden-phone">序号</span></th>
											<th><span class="hidden-phone ">策略名称</span></th>
											<th><span class="hidden-phone">策略类型</span></th>
											<th><span class="hidden-phone">策略等级</span></th>
											<th><span class="hidden-phone ">策略有效期</span></th>
											<th><span class="hidden-phone">策略状态</span></th>
										</tr>
									</thead>
									<tbody>
										<c:if test="${empty policyList}">
											<tr>
												<td colspan="4" height="99%">没有更多数据</td>
											</tr>
										</c:if>
										<c:forEach items="${policyList}" var="policy"
											varStatus="index">
											<tr>
												<td>${index.count}</td>
												<td><a href="javascript:void(0);"
													onclick="show('${policy.policy_name}', '${ctx}/policyConfig/edit?id=${policy.id}');">${policy.policy_name}</a></td>
												<td>${policy.policy_type_id}</td>
												<td>${policy.policy_level_id}</td>
												<td><fmt:formatDate value="${policy.start_time}"
														pattern="yyyy-MM-dd" /> ~ <fmt:formatDate
														value="${policy.end_time}" pattern="yyyy-MM-dd" /></td>
												<td>${policy.policy_status_id}</td>
											</tr>
										</c:forEach>
									</tbody>
								</table>
								<div class="space7"></div>
								<table width="100%">
									<tr>
										<td colspan="4" align="right"><a
											style="margin: 0px 8px 10px 0px"
											href="${ctx}/policySceneManage">&#62;&#62;&#62;更多</a></td>
									</tr>
								</table>
							</div>
						</div>
						<!-- END RECENT ORDERS PORTLET-->
					</div>
				</div>
			</td>
		</tr>

		<tr>
			<td colspan="2">

				<div class="row-fluid">
					<div class="span6">
						<!-- BEGIN SITE VISITS PORTLET-->
						<div class="widget">
							<div class="widget-title">
								<h4>
									<i class="icon-bar-chart"></i>趋势图(按日)
								</h4>
								<span class="tools"> <a href="javascript:;"
									class="icon-chevron-down"></a>
								</span>
							</div>
							<div class="widget-body" style="height: 300px;">
								<div style="width: 100%; height: 95%;"
									id="_p_home_page_chart_1_">请稍候...</div>
							</div>
						</div>
						<!-- END SITE VISITS PORTLET-->
					</div>
					<div class="span6">
						<!-- BEGIN SITE VISITS PORTLET-->
						<div class="widget">
							<div class="widget-title">
								<h4>
									<i class="icon-bar-chart"></i>策略场景数量分布图
								</h4>
								<select style="float: right; width: 70px; height: 37px;"
									id="_p_select_">
									<option value="1">日期</option>
									<option value="2">月份</option>
									<option value="3">场景类型</option>
									<option value="4">地市</option>
								</select> <span class="tools"> <a href="javascript:;"
									class="icon-chevron-down"></a> <!-- 									<a href="javascript:;" -->
									<!-- 									class="icon-remove"></a> -->
								</span>
							</div>
							<div class="widget-body" style="height: 300px;">
								<div class="_p_echart_" mat="org_chart_2_"
									style="width: 100%; height: 95%;" id="_p_home_page_chart_2_">请稍候...</div>
							</div>
						</div>

						<!-- END SITE VISITS PORTLET-->
					</div>
				</div>
			</td>
		</tr>
	</table>

	<div id="dialog_4_system_info_" style="display: none;">
		<iframe src="" frameborder="0" scrolling="auto"
			id="_system_info_dialog_frame" style="width: 100%; height: 99%;"></iframe>
	</div>

	<%@ include file="/WEB-INF/layouts/bottom.jsp"%>
	<%-- 	<script type="text/javascript" src="${ctx}/static/js/echarts-plain.js"></script> --%>
	<script type="text/javascript">
		$(function() {

			var location = '${ctx}/static/js/echarts';
			require.config({
				paths : {
					echarts : location,
					'echarts/chart/line' : location,
					'echarts/chart/bar' : location
				}
			});

			loadChart_F();
			// 			loadChart_S();
			var params = {
				chart_2_type_id : $('#_p_select_').val()
			};
			_p_INIT_ECHART_ENGINE('${ctx}/home/queryChart', params);

			$('#_p_select_').on(
					'change',
					function() {
						params = {
							chart_2_type_id : $('#_p_select_').val()
						};
						loadChart_S('${ctx}/home/queryChart', params,
								'_p_home_page_chart_2_', 'org_chart_2_');
					});

		});

		function show(title, url) {
			$("#dialog_4_system_info_").dialog({
				autoOpen : true,
				width : 1100,
				height : 656,
				title : title,
				resizable : false,
				modal : true
			});
			// 			$('#_system_info_dialog_frame').attr('src', 'www.baidu.com');
			var _ifr = window.top.document.getElementById('showFrame').contentWindow.document
					.getElementById('_system_info_dialog_frame');
			_ifr.src = url;
			// 			console.log(_ifr);
			// 			$this = $(_ifr);
			// 			$(this).find('iframe').attr('src', 'www.baidu.com');
		}

		function invokepages(obj) {
			var _oper = $(obj).text().trim();
			var _url = '${ctx}/';
			switch (_oper) {
			case '调整':
				_url += 'policyConfig/edit?id=';
				break;
			case '审批':
				_url += 'policyApproval/searchApproveInit?popPolicyBaseinfo.id=';
				break;
			case '确认':
				_url += 'policyApproval/searchConfirmInit?popPolicyBaseinfo.id=';
				break;
			}
			_url += $(obj).attr('mat');
			// 			window.location.href = _url;
			var _title = '策略' + _oper;
			show(_title, _url);
			//var frm = window.top.document.getElementById('showFrame');
			//frm.src = _url;
		}
		
		//根据传入的page参数定位到相应页面
		function toPage(page){
			var topObj = window.top.document;
			var matchUrlPart="";
			if('addPolicy' == page){
				matchUrlPart = 'policyConfig/addInit';
			}else if('delivery' == page){
				matchUrlPart = 'sendOddSearch';
			}else if('approval' == page){
				matchUrlPart = 'searchApproveInit';
			}else{
				alert('无法定位到目标页面:'+page);
			}
			var liObj = null;
			$(topObj).find("li").each(function(i){
				if($(this).attr('url').indexOf(matchUrlPart) >= 0){
					liObj = $(this);
					return false;
				}
			});
			if(liObj!=null){
				liObj.parent().parent().prev().click();
				liObj.click();
			}
		}
	</script>
</body>
</html>