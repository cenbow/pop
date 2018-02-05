<%@ page language="java" import="java.util.*"
	contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>PCCID查询</title>
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
								<i class="icon-reorder"></i>PCCID查询
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
											<th style="text-align: right;" width="70px"><label
												class="">PCCID：</label></th>
											<td style="text-align: left;" width="128px"><input name="pccid_id"
												type="text" value=""  maxlength="32" onBlur="this.value=this.value.replace(/[^\d]/g,'') "  
												style="width:128px;"/>
												</td>
											<th style="text-align: right;" width="70px"><label
												class="">策略名称：</label></th>
											<td style="text-align: left;" width="128px"><input name="pccid_name"
												type="text" value=""  maxlength="32" 
												style="width:128px;"/>
												</td>
											<th style="text-align: right;" width="80"><label
												class="">使用标志：</label></th>
											<td style="text-align: left;">
											<input id="display-pccid_use_flag" class="ztreeCombox"   type="text"  readonly   value=""   style="width:113px;" />
											</td>
										</tr>
										<tr>
											<td colspan="6" class="query_btns">
												<button type="button" id="search_button" onclick="_p_query()" 
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
						<iframe id="pccid_list_frame"  name="pccidInfoList" width="100%" frameborder="0"
							style="background: #ffffff; height: 740px;" src=""></iframe>
					</div>
				</div>
			</div>
		</div>
	</div>
	<div id="myModal_div" style="display:none;">
			<iframe id="myModal_frame" scrolling="no" name="myModal_update_frame" align="top" width="100%" height="1100px" frameborder="0"></iframe>
	</div>
	
	<div id="myModal_del" style="display:none;">
       <input type="hidden" id="pccid_id">							
		<div class="modal-body" >确认删除？</div>
		<div class="modal-footer">
			<input type="button" class="btn btn-primary btn-small" onclick="delPccid()" value="确认" >
			<input type="button" class="btn btn-primary btn-small" onClick='$("#myModal_del").dialog("close")'  value="取消">
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

		function _p_query() {
			var url = '${ctx}/pccIdManage/searchList?';
			$('#pccid_list_frame').attr('src',
					url + $('#searchForm').serialize());
		}
		
		//删除后
	    function delPccid(){
	    var id = $("#pccid_id").val();
	    var actionUrl = "${ctx}/pccIdManage/delete";
			$.ajax({
				type : "POST",
				url : actionUrl,
				data : {'pccid_id' : id},
				success : function(result) {
					if (result.success == "1") {
						alert("删除pccid【"+id+"】操作成功！");
						search();
						$("#myModal_del").dialog("close");
					} else {
						alert("删除pccid【"+id+"】操作失败：" + result.msg);
						$("#myModal_del").dialog("close");
					}
				}
			});
	    }
		
		//查询
		function search() {
			var theForm = document.forms[0];
			document.forms[0].action = '${ctx}/pccIdManage/searchList';
			theForm.target = "pccidInfoList";
			theForm.submit();
		}
		
		//策略级别
		$("#display-pccid_use_flag").ztreeComb({
			"treeData":[],
			"useCheckbox":true,
			"listheight":200,
			"hiddenName":"pccid_use_flag" ,
			getsearchData:function(txt,treeId){
				listSearch(treeId , txt,"pop_dim_pcc_id_use_flag","id", "name");
			}
		});
		var pccid_use_flag_ztreeId = $("#display-pccid_use_flag").attr("treeid");
		bindData(pccid_use_flag_ztreeId,"pop_dim_pcc_id_use_flag","id","name",'');
		

		function clearForm() {
			$("#display-pccid_use_flag").attr("value", "");
			$("input[name='pccid_use_flag']").attr("value", "");
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
		/**新增pccid页面保存后回调*/
		function callback_save_addpccid(){
			$("#myModal_div").dialog("close");
			search();
		}
	</script>

</body>
</html>