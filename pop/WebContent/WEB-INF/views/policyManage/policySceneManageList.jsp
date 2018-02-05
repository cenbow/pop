﻿<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ page import="java.util.*"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<head>
<title>策略场景管理查询结果列表</title>
<%@ include file="/WEB-INF/layouts/bottom.jsp"%>
<%@ include file="/WEB-INF/layouts/head.jsp"%>


<style type="text/css">
.table-noborder th,.table-noborder td {
	border-top: none;
}

.queryTable th,.queryTable td {
	padding: 4px;
}

.table_btns button {
	margin-right: 4px;
	margin-left: 10px;
}

.table_btns,table td.query_btns {
	text-align: right;
}

#tbl_policyBaseInfoAndRules span,.tbl_rules span {
	margin-left: 2px;
}

#tbl_policyBaseInfoAndRules button,.tbl_rules button {
	margin: 1px 2px;
}

.dataTables_filter {
	margin-right: 3px;
}

.autocut {
	white-space: nowrap;
	overflow: hidden;
	text-overflow: ellipsis;
}

.hideicon .prev_span,.hideicon .vsStyle_node.vsStyle_last_leaf {
	display: none;
}

span.arrow {
	display: inline-block;
	position: absolute;
	top: 5px;
	left: 0px;
	width: 16px;
	height: 16px;
	background: url('${ctx}/static/assets/treeTable/vsStyle/allbgs.png')
		no-repeat -16px 0px;
	margin: 0;
	padding: 0;
	border: medium none;
	cursor: pointer;
}

span.arrow_open {
	background-position: 0px 0px;
}

td.td_popover>.popover {
	max-width: 100%;
}

.tbl_tasklist td {
	text-align: center;
}
</style>
<script type="text/javascript">
	function dialog_view(popPolicySceneManageBeanId) {
		var dialogOwn = $("#myModal_div", window.parent.document);
		var dialogParent = dialogOwn.parent();
		var dialogBak = dialogOwn.clone();
		dialogOwn.find("#myModal_frame").attr("src",
				"${ctx}/policyConfig/view?id=" + popPolicySceneManageBeanId);
		var currentDialog = window.parent.mcdDialog('myModal_div', {
			autoOpen : true,
			width : $(window.parent).width() - 40,
			height : "auto",
			title : "查看",
			modal : true,
			resizable : false,
			close : function() {
				dialogParent.append(dialogBak);//备份内容
				currentDialog.dialog('destroy').remove();//销毁对话框
			}
		});
	}

	function dialog_edit(popPolicySceneManageBeanId) {
		var dialogOwn = $("#myModal_div", window.parent.document);
		var dialogParent = dialogOwn.parent();
		var dialogBak = dialogOwn.clone();
		dialogOwn.find("#myModal_frame").attr("src",
				"${ctx}/policyConfig/edit?id=" + popPolicySceneManageBeanId);
		var currentDialog = window.parent.mcdDialog('myModal_div', {
			autoOpen : true,
			width : $(window.parent).width() - 40,
			height : "auto",
			title : "修改",
			modal : true,
			resizable : false,
			close : function() {
				dialogParent.append(dialogBak);//备份内容
				currentDialog.dialog('destroy').remove();//销毁对话框
			}
		});
	}
	/*暂停策略*/
	function dialog_pause(popPolicySceneManageBeanId,
			popPolicySceneManageBeanName, policyRuleId) {
		var myoption = {
			autoOpen : true,
			width : 430,
			height : "auto",
			title : "暂停",
			modal : true,
			resizable : false
		};
		$(window.parent.document).find("#pausePolicyId").val(
				popPolicySceneManageBeanId);
		$(window.parent.document).find("#pausePolicyName").val(
				popPolicySceneManageBeanName);
		$(window.parent.document).find("#pausePolicyRuleId").val(policyRuleId);
		$(window.parent.document).find("#pauseConfirmWords").append(
				"确认暂停策略【" + popPolicySceneManageBeanName + "】？");
		window.parent.mcdDialog("myModal_pause", myoption);
	}
	/*启动策略*/
	function dialog_reStart(popPolicySceneManageBeanId,
			popPolicySceneManageBeanName, policyRuleId) {
		var myoption = {
			autoOpen : true,
			width : 430,
			height : "auto",
			title : "启动",
			modal : true,
			resizable : false
		};
		$(window.parent.document).find("#reStartPolicyId").val(
				popPolicySceneManageBeanId);
		$(window.parent.document).find("#reStartPolicyName").val(
				popPolicySceneManageBeanName);
		$(window.parent.document).find("#reStartPolicyRuleId")
				.val(policyRuleId);
		$(window.parent.document).find("#reStartConfirmWords").append(
				"确认启动策略【" + popPolicySceneManageBeanName + "】？");
		window.parent.mcdDialog("myModal_reStart", myoption);
	}
	/*删除策略*/
	function dialog_del(popPolicySceneManageBeanId,
			popPolicySceneManageBeanName) {
		var myoption = {
			autoOpen : true,
			width : 430,
			height : "auto",
			title : "删除",
			modal : true,
			resizable : false
		};
		$(window.parent.document).find("#delPolicyId").val(
				popPolicySceneManageBeanId);
		$(window.parent.document).find("#delPolicyName").val(
				popPolicySceneManageBeanName);
		window.parent.mcdDialog("myModal_del", myoption);
	}
	/*策略派单*/
	function dialog_sendOrder(popPolicySceneManageBeanId,
			popPolicySceneManageBeanName, policyRuleId) {
		var myoption = {
			autoOpen : true,
			width : 430,
			height : "auto",
			title : "派单",
			modal : true,
			resizable : false
		};
		$(window.parent.document).find("#sendOrderPolicyId").val(
				popPolicySceneManageBeanId);
		$(window.parent.document).find("#sendOrderPolicyName").val(
				popPolicySceneManageBeanName);
		$(window.parent.document).find("#sendOrderPolicyRuleId").val(
				policyRuleId);
		//jinlong 20150504 修正连续操作 派单 功能 导致的弹出框提示信息不断累加长度的BUG BI_BUG_20150422_0035
		if ($(window.parent.document).find("#sendOrderConfirmWords").text() != '') {
			$(window.parent.document).find("#sendOrderConfirmWords").empty();
		}
		$(window.parent.document).find("#sendOrderConfirmWords").append(
				"确认派单策略【" + popPolicySceneManageBeanName + "】？");
		window.parent.mcdDialog("myModal_sendOrder", myoption);
	}

	/*
	暂停规则任务
	 */
	function dialog_pauseRuleTask(popPolicySceneManageBeanId,
			popPolicySceneManageBeanName, policyRuleId, policyRuleName) {
		var myoption = {
			autoOpen : true,
			width : 430,
			height : "auto",
			title : "暂停",
			modal : true,
			resizable : false
		};
		$(window.parent.document).find("#pauseTaskPolicyId").val(
				popPolicySceneManageBeanId);
		$(window.parent.document).find("#pauseTaskPolicyName").val(
				popPolicySceneManageBeanName);
		$(window.parent.document).find("#pauseTaskPolicyRuleId").val(
				policyRuleId);
		$(window.parent.document).find("#pauseTaskPolicyRuleName").val(
				policyRuleName);
		//jinlong 20150504 修正连续操作 暂停 功能 导致的弹出框提示信息不断累加长度的BUG
		if ($(window.parent.document).find("#pauseTaskConfirmWords").text() != '') {
			$(window.parent.document).find("#pauseTaskConfirmWords").empty();
		}
		$(window.parent.document).find("#pauseTaskConfirmWords").append(
				"确认暂停策略【" + popPolicySceneManageBeanName + "】规则【"
						+ policyRuleName + "】任务？");
		window.parent.mcdDialog("myModal_pauseTask", myoption);

	}

	/*
	重启规则任务
	 */
	function dialog_reStartRuleTask(popPolicySceneManageBeanId,
			popPolicySceneManageBeanName, policyRuleId, policyRuleName) {
		var myoption = {
			autoOpen : true,
			width : 430,
			height : "auto",
			title : "启动",
			modal : true,
			resizable : false
		};
		$(window.parent.document).find("#reStartTaskPolicyId").val(
				popPolicySceneManageBeanId);
		$(window.parent.document).find("#reStartTaskPolicyName").val(
				popPolicySceneManageBeanName);
		$(window.parent.document).find("#reStartTaskPolicyRuleId").val(
				policyRuleId);
		$(window.parent.document).find("#reStartTaskPolicyRuleName").val(
				policyRuleName);
		//jinlong 20150504 修正连续操作 启动 功能 导致的弹出框提示信息不断累加长度的BUG
		if ($(window.parent.document).find("#reStartTaskConfirmWords").text() != '') {
			$(window.parent.document).find("#reStartTaskConfirmWords").empty();
		}
		$(window.parent.document).find("#reStartTaskConfirmWords").append(
				"确认启动策略【" + popPolicySceneManageBeanName + "】规则【"
						+ policyRuleName + "】任务？");
		window.parent.mcdDialog("myModal_reStartTask", myoption);
	}

	/*
	终止规则任务
	 */
	function dialog_terminaterRuleTask(popPolicySceneManageBeanId,
			popPolicySceneManageBeanName, policyRuleId, policyRuleName) {
		var myoption = {
			autoOpen : true,
			width : 430,
			height : "auto",
			title : "终止",
			modal : true,
			resizable : false
		};
		$(window.parent.document).find("#terminaterTaskPolicyId").val(
				popPolicySceneManageBeanId);
		$(window.parent.document).find("#terminaterTaskPolicyName").val(
				popPolicySceneManageBeanName);
		$(window.parent.document).find("#terminaterTaskPolicyRuleId").val(
				policyRuleId);
		$(window.parent.document).find("#terminaterTaskPolicyRuleName").val(
				policyRuleName);
		//jinlong 20150504 修正连续操作 终止 功能 导致的弹出框提示信息不断累加长度的BUG
		if ($(window.parent.document).find("#terminaterTaskConfirmWords")
				.text() != '') {
			$(window.parent.document).find("#terminaterTaskConfirmWords")
					.empty();
		}
		$(window.parent.document).find("#terminaterTaskConfirmWords").append(
				"确认终止策略【" + popPolicySceneManageBeanName + "】规则【"
						+ policyRuleName + "】任务？");
		window.parent.mcdDialog("myModal_terminaterTask", myoption);
	}

	//策略进度查询 by lixq8 20150428 add
	function dialog_view_process(popPolicySceneManageBeanId) {
		//去缓存
		var dialogParent = $(window.parent.document).find("#myModal_div")
				.parent();
		var dialogOwn = $(window.parent.document).find("#myModal_div").clone();
		dialogOwn.hide();
		$(window.parent.document).find("#myModal_frame").attr(
				"src",
				"${ctx}/policyApproval/viewProcess?policy_id="
						+ popPolicySceneManageBeanId);
		var myoption = {
			autoOpen : true,
			width : $(window.parent).width() - 40,
			height : "auto",
			title : "进度查询",
			modal : true,
			resizable : false,
			close : function() {
				dialogOwn.appendTo(dialogParent);
				$(this).dialog("destroy").remove();
			}
		};
		window.parent.mcdDialog("myModal_div", myoption);
	}

	//重启规则任务
	function restart_task(thisobj, policyId, ruleId, taskId) {
		var $this = $(thisobj);
		$this.attr('disabled', 'disabled');
		$.ajax({
			url : "${ctx}/policySceneManage/reStartTask",
			type : "POST",
			data : {
				policyId : policyId,
				ruleId : ruleId,
				taskId : taskId
			},
			success : function(result) {
				if (result.success == 1) {
					alert('重启规则任务成功!');
				} else {
					alert('重启规则任务失败:' + result.msg);
					$this.removeAttr('disabled');
				}
			}
		});
	}

	$(document).ready(
			function() {
				$(window.parent.document).find(
						"#popPolicySceneManageBeanIframe").height(
						document.body.scrollHeight + 20);
			});
</script>
</head>
<body style="overflow: hidden;">
	<div class="widget">
		<div class="widget-title">
			<h4>
				<i class="icon-reorder"></i>查询结果
			</h4>
		</div>
		<div class="widget-body">
			<table id="tbl_policyBaseInfoAndRules"
				class="table  table-bordered table-mcdstyle table-epl"
				style="table-layout: fixed;">
				<thead>
					<tr>
						<th width="145" class="autocut" title="策略名称">策略名称</th>
						<th width="10%" class="autocut" title="策略类型">策略类型</th>
						<th width="14%" class="autocut" title="策略规则">策略规则</th>
						<th width="8%" class="autocut" title="客户规模">客户规模</th>
						<th width="10%" class="autocut" title="地市优先级">地市优先级</th>
						<th width="20%" class="autocut" title="有效期">有效期</th>
						<th width="13%" class="autocut" title="策略状态">策略状态</th>
						<th width="342" style="min-width: 50px;">操作</th>
					</tr>
				</thead>
				<tbody>
					<c:if test="${fn:length(popPolicySceneManageBeanList) == 0}">
						<tr>
							<td colspan="7"
								style="background-color: #DDDDDD; text-align: center;"><font
								color="red">没有符合条件的数据!</font></td>
						</tr>
					</c:if>
					<c:forEach items="${popPolicySceneManageBeanList}"
						var="popPolicySceneManageBean">
						<!-- 策略配置列表 start -->
						<c:set value="${popPolicySceneManageBean.policyId}"
							var="popPolicySceneManageBeanId"></c:set>
						<c:set value="${popPolicySceneManageBean.policyName}"
							var="popPolicySceneManageBeanName"></c:set>
						<tr class="policy_row" id="${popPolicySceneManageBean.policyId}">
							<td class="hidden-phone autocut"
								style="text-align:left;padding-left: 16px;<c:if test="${fn:length(popPolicySceneManageBean.ruleList) != 0}">position:relative;display:block;</c:if>"
								title="${popPolicySceneManageBean.policyName}"><c:if
									test="${fn:length(popPolicySceneManageBean.ruleList) != 0}">
									<span id="arrow_${popPolicySceneManageBean.policyId}"
										class="arrow"></span>
								</c:if>${popPolicySceneManageBean.policyName}</td>
							<td class="hidden-phone autocut" style="text-align: left;">${popPolicySceneManageBean.policyTypeName}</td>
							<td class="hidden-phone autocut"
								title="${popPolicySceneManageBean.policyControlName}">${popPolicySceneManageBean.policyControlName}</td>
							<td class="hidden-phone autocut" style="text-align: center;"
								title="${popPolicySceneManageBean.custgroupNumber}">${popPolicySceneManageBean.custgroupNumber}</td>
							<td class="hidden-phone autocut" style="text-align: left;"
								title="${popPolicySceneManageBean.cityPriorityName}">${popPolicySceneManageBean.cityPriorityName}</td>
							<td class="hidden-phone autocut" style="text-align: center;"
								title="<fmt:formatDate value="${popPolicySceneManageBean.startTime}" pattern="yyyy-MM-dd" /> ~ <fmt:formatDate value="${popPolicySceneManageBean.endTime}" pattern="yyyy-MM-dd" />">
								<fmt:formatDate value="${popPolicySceneManageBean.startTime}"
									pattern="yyyy-MM-dd" /> ~ <fmt:formatDate
									value="${popPolicySceneManageBean.endTime}"
									pattern="yyyy-MM-dd" />
							</td>
							<td class="hidden-phone autocut" style="text-align: center;"
								title="${popPolicySceneManageBean.policyStatusName}">${popPolicySceneManageBean.policyStatusName}</td>
							<td class="hidden-phone">
								<div id="operDiv_${popPolicySceneManageBean.policyId}">
									<button type="button" class="btn btn-small btn-primary"
										onclick="dialog_view('${popPolicySceneManageBean.policyId}')"
										data-toggle="modal">
										<i class="icon-white icon-info-sign"></i> 查看
									</button>
									<button type="button" class="btn btn-small btn-primary"
										onclick="dialog_edit('${popPolicySceneManageBean.policyId}')"
										data-toggle="modal"
										<c:if test="${popPolicySceneManageBean.policyStatusId != POP_POLICY_EDIT_STATUS}">disabled</c:if>>
										<i class="icon-white icon-edit"></i> 修改
									</button>
									<button type="button" class="btn btn-small btn-danger"
										onclick="dialog_del('${popPolicySceneManageBean.policyId}','${popPolicySceneManageBean.policyName}')"
										data-toggle="modal"
										<c:if test="${popPolicySceneManageBean.policyStatusId != POP_POLICY_EDIT_STATUS}">disabled</c:if>>
										<i class="icon-remove icon-white"></i> 删除
									</button>
									<button type="button" class="btn btn-small btn-primary"
										onclick="dialog_view_process('${popPolicySceneManageBean.policyId}')"
										data-toggle="modal">
										<i class="icon-white"></i> 进度查询
									</button>
									<c:if
										test="${popPolicySceneManageBean.policyStatusId == POP_POLICY_EXCUTE_STATUS}">
										<button type="button" class="btn btn-small btn-primary"
											onclick="dialog_pause('${popPolicySceneManageBean.policyId}', '${popPolicySceneManageBean.policyName}','${popPolicySceneManageBean.policyRuleId}')"
											data-toggle="modal">
											<i class="icon-white"></i> 暂停
										</button>
									</c:if>
									<c:if
										test="${popPolicySceneManageBean.policyStatusId == POP_POLICY_PAUSE_STATUS 
						|| popPolicySceneManageBean.policyStatusId == POP_POLICY_FAILURE_STATUS}">
										<button type="button" class="btn btn-small btn-primary"
											onclick="dialog_reStart('${popPolicySceneManageBean.policyId}', '${popPolicySceneManageBean.policyName}', '${popPolicySceneManageBean.policyRuleId}')"
											data-toggle="modal">
											<i class="icon-white"></i> 启动
										</button>
									</c:if>
								</div>
							</td>
						</tr>
						<c:if test="${fn:length(popPolicySceneManageBean.ruleList) != 0}">
							<tr id="rule_${popPolicySceneManageBean.policyId}"
								pId="${popPolicySceneManageBean.policyId}"
								style="display: none;">
								<!-- 规则列表 start -->
								<td colspan="7">
									<table
										class="table table-striped table-bordered table-mcdstyle tbl_rules"
										style="margin: 0 !important; border-collapse: collapse; border-spacing: 0; table-layout: fixed;"
										id="tbl_rules_${popPolicySceneManageBean.policyId}">
										<thead>
											<tr>
												<th width="145" class="autocut">规则ID</th>
												<th width="13%" class="autocut">规则名称</th>
												<th width="38%" class="autocut">动作</th>
												<th width="10%" class="autocut">状态</th>
												<th style="min-width: 100px;" width="200">操作</th>
											</tr>
										</thead>
										<tbody>
											<c:forEach items="${popPolicySceneManageBean.ruleList}"
												var="rule">
												<tr id="${rule.id }" pId="${rule.parentId }">
													<td class="hidden-phone autocut" title="${rule.id}">${rule.id
														}</td>
													<td class="hidden-phone autocut"
														title="${rule.policyRuleName}" style="text-align: left;">${rule.policyRuleName}</td>
													<td class="hidden-phone autocut"
														title="${rule.actionTypeName }">${rule.actionTypeName
														}</td>
													<td class="hidden-phone autocut" title="${rule.statusName}"
														style="text-align: center;">${rule.statusName}</td>
													<td class="td_popover" style="text-align: center;">
														<button type="button"
															class="btn btn-small btn-primary btn_tasklist"
															<c:if test="${!rule.canViewTask }">disabled</c:if>>
															<i class="icon-white"></i> 任务查看
														</button> <c:if test="${rule.canViewTask }">
															<div class="pannel_tasklist" style="display: none;">
																<table
																	class="tbl_tasklist table-bordered table-mcdstyle"
																	style="table-layout: fixed;">
																	<thead>
																		<tr>
																			<th width="80">执行时间</th>
																			<th width="80">预派发数</th>
																			<th width="80">实派发数</th>
																			<th width="80">剔除数</th>
																			<th width="70">状态</th>
																			<th width="70">操作</th>
																		</tr>
																	</thead>
																	<tbody>
																		<c:forEach items="${rule.taskList }" var="task">
																			<tr>
																				<td>${task.exec_date }</td>
																				<td>${task.preSendDataCount }</td>
																				<td>${task.sendDataCount }</td>
																				<td>${task.rejectDataCount }</td>
																				<td>${task.exec_status_name }</td>
																				<td><button type="button"
																						class="btn btn-small btn-primary"
																						<c:if test="${!task.can_restart}">disabled</c:if>
																						onclick="restart_task(this,'${rule.id }', '${rule.policyId }', '${task.id}')">
																						<i class="icon-white"></i> 重启
																					</button></td>
																			</tr>
																		</c:forEach>
																	</tbody>
																</table>
															</div>
														</c:if>
														<button type="button" class="btn btn-small btn-primary"
															<c:if test="${!rule.canPause }">disabled</c:if>
															onclick="dialog_pauseRuleTask('${popPolicySceneManageBean.policyId}', '${popPolicySceneManageBean.policyName}','${rule.id }','${rule.policyRuleName}')"
															data-toggle="modal">
															<i class="icon-white"></i> 暂停
														</button>

														<button type="button" class="btn btn-small btn-primary"
															<c:if test="${!rule.canStart }">disabled</c:if>
															onclick="dialog_reStartRuleTask('${popPolicySceneManageBean.policyId}', '${popPolicySceneManageBean.policyName}','${rule.id }','${rule.policyRuleName}')"
															data-toggle="modal">
															<i class="icon-white"></i> 启动
														</button>
														<button type="button" class="btn btn-small btn-primary"
															<c:if test="${!rule.canFinish }">disabled</c:if>
															onclick="dialog_terminaterRuleTask('${popPolicySceneManageBean.policyId}', '${popPolicySceneManageBean.policyName}','${rule.id }','${rule.policyRuleName}')"
															data-toggle="modal">
															<i class="icon-white"></i> 终止
														</button>
													</td>
												</tr>
											</c:forEach>

										</tbody>
									</table>
								</td>
							</tr>
							<!-- 规则列表 end -->
						</c:if>
					</c:forEach>
					<!-- 策略配置列表 end -->
				</tbody>
			</table>
			<c:if test="${not empty popPolicySceneManageBeanList}">
				<tags:pagination pageNum="${list.pageNumber}"
					paginationSize="${list.pageSize}" totalPage="${list.totalPage}"
					totalRow="${list.totalRow}" />
			</c:if>
			<div id="footer">2015 &copy; Asiainfo</div>
		</div>
	</div>
</body>
<script type="text/javascript">
	$(function() {
		//规则表格树插件初始化
		$('table.tbl_rules').treeTable({
			theme : 'vsStyle',
			expandLevel : 999
		});

		//策略配置 规则显示和隐藏
		$('.arrow').toggle(function() {//显示
			//$(this).css('background-position','0px 0px');
			$(this).addClass('arrow_open');
			//显示规则列表id=arrow_policyId
			var policyId = $(this).attr('id').substring(6);
			$('#rule_' + policyId).show();
		}, function() {//隐藏
			//$(this).css('background-position','-16px 0px');
			$(this).removeClass('arrow_open');
			//隐藏规则列表id=arrow_policyId
			var policyId = $(this).attr('id').substring(6);
			$('#rule_' + policyId).hide();
		});
		//展开时重新计算框架高度
		$('span.arrow,span[arrow="true"]').click(
				function() {
					if ($(this).is(
							'.vsStyle_active_node vsStyle_open,.arrow_open')) {
						var otherHeight = 114;
						var tableHeight = $('#tbl_policyBaseInfoAndRules')
								.height();
						var newHeight = tableHeight + otherHeight;
						if (newHeight > $('#popPolicyInfoIframe',
								window.parent.document).height()) {
							$('#popPolicyInfoIframe', window.parent.document)
									.height(newHeight);
						}
					}
				});
		//表格行背景交换变换
		$('#tbl_policyBaseInfoAndRules tr.policy_row:odd').css('background',
				'#f9f8f8');
		$('#tbl_policyBaseInfoAndRules tr.policy_row:even').css('background',
				'#fff');

		$('.btn_tasklist')
				.each(
						function() {
							var $this = $(this);
							if ($this.next('.pannel_tasklist').length == 0) {
								return true;
							}
							$this.click(function(event) {
								event.stopPropagation();
							});//阻止按钮click冒泡,消除错误关闭popover影响
							//复制隐藏任务列表并添加到popover中
							var content = $this.next('.pannel_tasklist').html()
									.replace("display:none;", "");
							$this.next('.pannel_tasklist').remove();
							$this
									.popover({
										html : true,
										placement : 'bottom',
										trigger : 'click',
										content : content,
										template : '<div class="popover"><div class="arrow"></div><div class="popover-content"></div></div>'
									});
							$this.one('click', function() {//单击按钮生成popover层后，注册popover层单击事件添加阻止冒泡语句
								$(this).next('.popover').click(function(e) {
									e.stopPropagation();
								});
							});
						});
		//单击popover外侧隐藏popover弹出层
		$(document).click(
				function(event) {
					$('.td_popover > .popover:visible').prev('.btn_tasklist')
							.popover('hide');
				});
	});
</script>
</html>