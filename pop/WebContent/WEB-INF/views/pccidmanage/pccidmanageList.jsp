<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>PCCID列表</title>
<%@ include file="/WEB-INF/layouts/head.jsp"%>
<style>
.table-epl td {
	word-wrap: break-word;
}
</style>
</head>
<body style="overflow: hidden;">
	<!-- PCCID列表 start -->
	<div class="widget">
		<div class="widget-title">
			<h4>
				<i class="icon-reorder"></i>PCCID列表
			</h4>
			<div style="float: right; margin: 5px 19px auto;"
				id="_d_p_new_pccid_btn_">
				<button type="button" class="btn btn-small btn-primary" onclick="dialog_add()" id="_p_new_pccid_btn_">
					<i class="icon-plus icon-white"></i> 新建
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
								<th width="12"><input type="checkbox"
									id="_p_list_checkbox_0_" /></th>
								<th>PCCID</th>
								<th>策略名称</th>
								<th>描述</th>
								<th width="53">使用标志</th>
								<th width="129">添加时间</th>
								<th>操作</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach items="${pageList.list}" var="pccid">
								<tr>
									<td><input type="checkbox" id="${pccid.id}"/></td>
									<td><c:out value="${pccid.id}"/></td>
									<td><c:out value="${pccid.pccname}"/></td>
									<td><c:out value="${pccid.remark}"/></td>
									<td>
									<c:if test="${pccid.use_flag==0}">
									未使用
									</c:if>
									<c:if test="${pccid.use_flag==1}">
									已使用
									</c:if>
									</td>
									<td>${pccid.addtime}</td>
								<td class="hidden-phone">
								<div id="operDiv_${pccid.id}">
									<button type="button" class="btn btn-small btn-primary"
										onclick="dialog_view('${pccid.id}')"
										data-toggle="modal">
										<i class="icon-white icon-info-sign"></i> 查看
									</button>
									<c:if test="${pccid.use_flag==0}">
										<button type="button" class="btn btn-small btn-primary"
											onclick="dialog_edit('${pccid.id}')"
											data-toggle="modal">
											<i class="icon-white icon-edit"></i> 修改
										</button>
										<button type="button" class="btn btn-small btn-danger"
											onclick="dialog_del('${pccid.id}')"
											data-toggle="modal">
											<i class="icon-remove icon-white"></i> 删除
										</button>
									</c:if>
									<c:if test="${pccid.use_flag==1}">
										<button type="button" class="btn btn-small btn-primary"
											onclick="dialog_edit('${pccid.id}')"
											data-toggle="modal" disabled="disabled">
											<i class="icon-white icon-edit"></i> 修改
										</button>
										<button type="button" class="btn btn-small btn-danger"
											onclick="dialog_del('${pccid.id}')"
											data-toggle="modal" disabled="disabled">
											<i class="icon-remove icon-white"></i> 删除
										</button>
									</c:if>
								</div>
								</td>
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

	<!-- PCCID列表 end -->
	<%@ include file="/WEB-INF/layouts/bottom.jsp"%>
	<script type="text/javascript">
		


		function dialog_view(pccid) {
			var dialogOwn = $("#myModal_div", window.parent.document);
			var dialogParent = dialogOwn.parent();
			var dialogBak = dialogOwn.clone();
			dialogOwn.find("#myModal_frame").attr("src",
					"${ctx}/pccIdManage/view?id=" + pccid);
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
		/*修改*/
		function dialog_edit(pccid) {
			var dialogOwn = $("#myModal_div", window.parent.document);
			var dialogParent = dialogOwn.parent();
			var dialogBak = dialogOwn.clone();
			dialogOwn.find("#myModal_frame").attr("src",
					"${ctx}/pccIdManage/edit?id=" + pccid);
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
		
		/*删除*/
		function dialog_del(pccid) {
			var myoption = {
				autoOpen : true,
				width : 430,
				height : "auto",
				title : "删除",
				modal : true,
				resizable : false
			};
			$(window.parent.document).find("#pccid_id").val(
					pccid);
			window.parent.mcdDialog("myModal_del", myoption);
		}
		
		/*新增*/
		function dialog_add() {
			var dialogOwn = $("#myModal_div", window.parent.document);
			var dialogParent = dialogOwn.parent();
			var dialogBak = dialogOwn.clone();
			dialogOwn.find("#myModal_frame").attr("src",
					"${ctx}/pccIdManage/addInit");
			var currentDialog = window.parent.mcdDialog('myModal_div', {
				autoOpen : true,
				width : $(window.parent).width() - 40,
				height : "auto",
				title : "新增",
				modal : true,
				resizable : false,
				close : function() {
					dialogParent.append(dialogBak);//备份内容
					currentDialog.dialog('destroy').remove();//销毁对话框
				}
			});
		}
		
	</script>
</body>
</html>