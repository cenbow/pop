<%@page import="com.asiainfo.biframe.utils.date.DateUtil"%>
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@page import="java.util.Date"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<%String opt=String.valueOf(request.getAttribute("opt"));%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="/WEB-INF/layouts/head.jsp"%>
<meta charset="utf-8" />
<title>PCCID${"add"==opt?"创建":("view"==opt?"查看":"修改") }</title>
<meta content="width=device-width, initial-scale=1.0" name="viewport" />
<meta content="" name="description" />
<meta content="" name="author" />
<META HTTP-EQUIV="pragma" CONTENT="no-cache"> 
<META HTTP-EQUIV="Cache-Control" CONTENT="no-cache, must-revalidate">
<style type="text/css">
html,body{height:100%;}
.modal-body{max-height:1000px;}
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
	width:100px;
	font-weight:bold;
}
.btvdclass{
    border-color:#FF0000;
    color:red;
    font-weight:normal;
}
.commonWidth{
	width:185px; 
}
.template_div{
	float: left;line-height: 37px;padding-left:22px;
}
#tbl_rules span{margin-left: 2px;}
#tbl_rules button{ margin: 1px 2px; }
.autocut{white-space: nowrap;overflow: hidden;text-overflow:ellipsis;}
.widget-title div.button-group{float: right;padding-right: 23px;padding-top: 5px;}
.popover-content{ padding: 8px;font-size: 11px;line-height: 1.4;}
.popover.right .arrow{border-right-color: red;}
</style>


</head>

<body>
<div id="mainDiv" class="modal-body" style="width:auto;height:auto">
	<!-- 基本信息 start -->
	<div class="widget">
		<div class="widget-title">
			<h4>
				<i class="icon-reorder"></i>基本信息
			</h4>
		</div>
		<div class="widget-body">
			<form id="frm_baseinfo" method="post" class="form-horizontal" role="form" style="margin-bottom: 0;">
			<fieldset <c:if test="${opt == 'view'}">disabled</c:if>>
				<input type="hidden" id="model_id" name="model_id" value="${model.id }"/>
				<table class="table table-noborder">
					<tbody>
						<tr>
							<th style="text-align: right;"  width="80"><label style="text-align: right;" class="control-label"><font color="red">*</font>PCCID：</label></th>
							<td style="text-align: left;" >
								<input name="popDimPccId.id" id="pccid_id" type="text" class="commonWidth" value="${model.id}" onBlur="this.value=this.value.replace(/[^\d]/g,'') " 
									 maxlength="32" data-helptip="PCCID不允许重复，只能输入数字，32位长度请认真填写" title="${model.id }"/>
							</td>
						</tr>
						<tr>
							<th style="text-align: right;"  width="80"><label style="text-align: right;" class="control-label"><font color="red">*</font>策略名称：</label></th>
							<td style="text-align: left;" >
								<input name="popDimPccId.pccname" id="pccname" type="text" class="commonWidth" value="${model.pccname}" 
									 maxlength="32"  title="${model.pccname }"/>
							</td>
						</tr>
						<tr>
							<th style="text-align: right;"  width="80"><label style="text-align: right;" class="control-label">描述：</label></th>
							<td style="text-align: left;" >
								<input name="popDimPccId.remark" id="remark" type="text" class="commonWidth" value="${model.remark}" maxlength="300" data-helptip="针对ID定义的规则和可使用范围描述，300位长度请认真填写" title="${model.remark}"/>
							</td>
						</tr>
						<c:if test="${opt != 'add'}">
						<tr>
							<th style="text-align: right;"  width="80"><label style="text-align: right;" class="control-label">使用标志：</label></th>
							<td style="text-align: left;" >
								<select class="form-control"  style="width:199px;" name="popDimPccId.use_flag" id="use_flag">
									<c:forEach items="${popDimPccIdUseFlags}" var="popDimPccIdUseFlag">
									<c:if test="${model.use_flag == popDimPccIdUseFlag.id}">
									<option value="${popDimPccIdUseFlag.id }" selected="selected">${popDimPccIdUseFlag.name }</option>
									</c:if>
									<c:if test="${model.use_flag != popDimPccIdUseFlag.id}">
									<option value="${popDimPccIdUseFlag.id }">${popDimPccIdUseFlag.name }</option>
									</c:if>
									</c:forEach>
								</select>	 
							</td>
						</tr>
						</c:if>
						<tr>
							<td colspan="4" style="text-align: right;">
								<c:if test="${opt != 'view'}">
								<input id="btn_save" class="btn btn-small btn-primary" type="button" onClick="saveBaseInfo('${model.id }')" value="保存" />
								
								</c:if>
							</td>
						</tr>
					</tbody>
				</table>
			</fieldset>
			</form>
		</div>
	</div>
</div>

<%@ include file="/WEB-INF/layouts/bottom.jsp"%>
<script type="text/javascript">
jQuery(document).ready(function(){
	//"查看"功能表单只读属性设置
	viewInit();
});

function saveBaseInfo(pccid_id){
	if(validationBeforeSave()){//表单校验
		if(typeof pccid_id== 'undefined'){
			pccid_id = "";
		}
		$.ajax({
			url : "${ctx}/pccIdManage/saveBaseInfo?pccid_id="+pccid_id,
			type : "POST",
			data : $("#frm_baseinfo").serialize(),
			success : function(result) {
				if (result.success == 1) {
					if(pccid_id == ""){
						$('#model_id').val(result.id);//回写新生成的基本信息id
						alert("PCCID: "+$('#pccid_id').val()+' 保存成功!您可以点击“新建”按钮，继续新增pccid。');
						window.parent.callback_save_addpccid();
					}else{
						alert("PCCID: "+$('#pccid_id').val()+'保存成功!');
						window.parent.callback_save_addpccid();
					}
				} else {
					alert("PCCID: "+$('#pccid_id').val()+result.msg);
				}
			}
		});
	}
}

/**保存前验证*/
function validationBeforeSave(){
	var pccid_id_val = $('#pccid_id').val();
	var pccname = $('#pccname').val();
	if(pccid_id_val.length >32){
		alert('PCCID长度不能大于32');
		return false;
	}
	if(pccid_id_val.length == 0){
		alert('PCCID不能为空');
		return false;
	}
	if(pccname.length == 0){
		alert('策略名称不能为空');
		return false;
	}
	
	return true;
}
//"查看"功能表单只读属性设置
function viewInit(){
	if(${opt == 'view'}){
		$("#pccid_id").attr("disabled","disabled");
		$("#pccname").attr("disabled","disabled");
		$("#remark").attr("disabled","disabled");
		$("#use_flag").attr("disabled","disabled");
	}
}	
</script>

</body>
</html>