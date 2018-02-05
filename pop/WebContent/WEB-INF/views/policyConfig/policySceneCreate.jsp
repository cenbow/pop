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
<meta http-equiv="X-UA-Compatible" content="IE=edge,Chrome=1" />
<meta charset="utf-8" />
<title>策略场景${"add"==opt?"创建":("view"==opt?"查看":"修改") }</title>
<meta content="width=device-width, initial-scale=1.0" name="viewport" />
<meta content="" name="description" />
<meta content="" name="author" />
<META HTTP-EQUIV="pragma" CONTENT="no-cache"> 
<META HTTP-EQUIV="Cache-Control" CONTENT="no-cache, must-revalidate">
<style type="text/css">
html, body {
	height: 100%;
}

.modal-body {
	max-height: 1000px;
}

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
	width: 100px;
	font-weight: bold;
}

.btvdclass {
	border-color: #FF0000;
	color: red;
	font-weight: normal;
}

.commonWidth {
	width: 185px;
}

.template_div {
	float: left;
	line-height: 37px;
	padding-left: 22px;
}

#tbl_rules span {
	margin-left: 2px;
}

#tbl_rules button {
	margin: 1px 2px;
}

.autocut {
	white-space: nowrap;
	overflow: hidden;
	text-overflow: ellipsis;
}

.widget-title div.button-group {
	float: right;
	padding-right: 23px;
	padding-top: 5px;
}

.popover-content {
	padding: 8px;
	font-size: 11px;
	line-height: 1.4;
}

.popover.right .arrow {
	border-right-color: red;
}

<!--导航样式开始-->
li, ul {
	list-style: none;
	margin: 0;
	padding: 0;
}
.tab {
	background: none repeat scroll 0 0 #f5f5f5;
	border: 1px solid #ccc;
	float: left;
	font-size: 14px;
	line-height: 1;
	width: 100%;
}
.tab li {
	background: #f5f5f5;
	float: left;
	width: 180px;
	height: 36px;
	line-height: 36px;
	overflow: hidden;
	margin-right: 10px;
	cursor: pointer;
}
.tab li span {
	position: absolute;
	padding: 0px 30px 0 30px;
}
.tab li em {
	margin-left: 180px;
	background: #FF6633;
	position: absolute;
	border: 18px solid #f5f5f5;
	border-left: #f5f5f5;
	height: 0px;
	overflow: hidden;
}
.tab li .left {
	margin-left: 0;
	border: 18px solid #f5f5f5;
	border-left: #f5f5f5;
}
.tab .on {
	background: #00CCFF;
	color: #ffffff;
}
.tab .on em {
	background: #FF6633;
	border-left: 10px solid #00CCFF;
}

.tab .on .left {
	border-top: 18px solid #00CCFF;
	border-bottom: 18px solid #00CCFF;
	border-right: 18px solid #00CCFF;
	border-left: 18px solid #f5f5f5;
}
<!--导航样式结束-->
</style>

</head>
<body>
<div id="mainDiv" class="modal-body" style="width:auto;height:auto">
	<div id="myModal_div" style="display:none;">
			<iframe id="myModal_frame" scrolling="no" name="myModal_update_frame" width="100%" height="1300px" frameborder="0"></iframe>
	</div>
	<ul class="tab widget-title">
		 <li id="navigation_j"><em class="left"></em><span>1、填写基本信息</span><em></em></li>
		 <li id="navigation_g"><em class="left"></em><span>2、新建规则</span><em></em></li>
		 <li id="navigation_t"><em class="left"></em><span>3、提交或保存成模板</span><em></em></li>
	</ul>
	<!-- 基本信息 start -->
	<div class="widget">
		<div class="widget-title">
			<h4>
				<i class="icon-reorder"></i>基本信息
			</h4>
			<!-- 新建显示 -->
			<c:if test="${(empty model.template_flag)}">
			<div class="template_div">
				<span>模板：</span>
				<select style="width:197px;margin-bottom: 0;" name="templateId" id="templateId" onchange="add_from_template()">
					<option value="999">--请选择--</option>
					<c:forEach items="${policyTemplates }" var="policyTemplate">
						<option value="${policyTemplate.id }" title="${policyTemplate.policy_name }">${policyTemplate.policy_name }</option>
					</c:forEach>
				</select>
				<input type="button" value="导入" class="btn btn-small btn-primary" style="display: none;"/>
			</div>
			</c:if>
			<span class="tools"> <a href="javascript:;"
				class="icon-chevron-down" id="span_close"></a>
			</span>
		</div>
		<div class="widget-body">
			<form id="frm_baseinfo" method="post" class="form-horizontal" role="form" style="margin-bottom: 0;">
			<fieldset <c:if test="${opt == 'view'}">disabled</c:if>>
				<input type="hidden" id="model_id" name="popPolicyBaseinfo.id" value="${model.id }"/>
				<input type="hidden" id="policy_status_id" name="popPolicyBaseinfo.policy_status_id" value="${model.policy_status_id  }"/>
				<table class="table table-noborder">
					<tbody>
						<tr>
							<th style="text-align: right;"  width="80"><label style="text-align: right;" class="control-label"><font color="red">*</font>策略名称：</label></th>
							<td style="text-align: left;" width="358" >
								<input name="popPolicyBaseinfo.policy_name" id="policy_name" type="text" class="commonWidth" value="${model.policy_name}" 
									 title="${model.policy_name }"  data-helptip="策略名称长度必须在1至75之间,且不含特殊字符`~!@#$^&*()+=|\[]{}:;',.<>/?" onBlur="this.value=this.value.replace(/[`~!@#$^&*()+=|\\\[\]\{\}:;'\,.<>/?]/g,'')"/>
							</td>
							<th style="text-align: right;" width="80"><label style="text-align: right;" class="control-label">策略类型：</label></th>
							<td style="text-align: left;" >
								<select class="form-control"  style="width:199px;" name="popPolicyBaseinfo.policy_type_id" id="policy_type_id">
									<c:forEach items="${dimPolicyTypes}" var="dimPolicyType">
									<c:if test="${model.policy_type_id == dimPolicyType.id}">
									<option value="${dimPolicyType.id }" selected="selected">${dimPolicyType.name }</option>
									</c:if>
									<c:if test="${model.policy_type_id != dimPolicyType.id}">
									<option value="${dimPolicyType.id }">${dimPolicyType.name }</option>
									</c:if>
									</c:forEach>
								</select>
							</td>
						</tr>
						<tr>
							<th style="text-align: right;" width="80"><label style="text-align: right;" class="control-label">策略等级：</label></th>
							<td style="text-align: left;" >
								<select class="form-control" style="width:199px;"  name="popPolicyBaseinfo.policy_level_id" id="policy_level_id">
									<c:forEach items="${dimPolicyLevels}" var="dimPolicyLevel">
									<c:if test="${model.policy_level_id == dimPolicyLevel.id }">
									<option value="${dimPolicyLevel.id }" selected="selected">${dimPolicyLevel.name }</option>
									</c:if>
									<c:if test="${model.policy_level_id != dimPolicyLevel.id }">
									<option value="${dimPolicyLevel.id }">${dimPolicyLevel.name }</option>
									</c:if>
									</c:forEach>
								</select>
							</td>
							<th style="text-align: right;"  width="80"><label style="text-align: right;" class="control-label"><font color="red">*</font>有效期：</label></th>
							<td style="text-align: left;" class="autocut">
								<div class="input-append date form_date"
									data-date-format="yyyy-mm-dd" data-link-field="startDate"
									data-link-format="yyyy-mm-dd" id="ctrl_start_time">
									<input style="width:auto;cursor: pointer;" size="16" id="display_start_time" type="text" value="${valid_start_time}" readonly>
									<span class="add-on"><i class="icon-remove"></i></span>
									<span class="add-on"><i class="icon-th"></i></span>
								</div>
								<input id="startDate" name="start_time" value="${valid_start_time}" type="hidden" onchange="hide_popover(this)"/>
								&nbsp;--&nbsp;
								<div class="input-append date form_date"
									data-date-format="yyyy-mm-dd" data-link-field="endDate"
									data-link-format="yyyy-mm-dd" id="ctrl_end_time">
									<input style="width:auto;cursor: pointer;" size="16" id="display_end_time" type="text" value="${valid_end_time}" readonly>
									<span class="add-on"><i class="icon-remove"></i></span>
									<span class="add-on"><i class="icon-th"></i></span>
								</div>
								<input id="endDate" name="end_time" value="${valid_end_time}" type="hidden"/></td>
						</tr>
						<tr>
							<th width="80" style="text-align: right;" ><label style="text-align: right;" class="control-label inline">客服口径：</label></th>
							<td style="text-align: left;"   class="autocut"  >
								<textarea class="form-control" rows="3" style="width: 300px;" name="popPolicyBaseinfo.custom_service_diameter" id="custom_service_diameter" data-helptip="客服口径长度不能超过400个汉字">${model.custom_service_diameter}</textarea>
							</td>
							<th width="80" style="text-align: right;" ><label style="text-align: right;" class="control-label inline"><font color="red">*</font>场景描述：</label></th>
							<td style="text-align: left;"   >
								<textarea class="form-control" rows="3" style="width: 300px;" name="popPolicyBaseinfo.policy_desc" id="policy_desc" data-helptip="场景描述长度必须在10至100之间">${model.policy_desc}</textarea>
							</td>
						</tr>
						<tr>
							<th style="text-align: right;"  width="80"><label style="text-align: right;" class="control-label">业务优先级：</label></th>
							<td style="text-align: left;" width="358">
								<input name="popPolicyBaseinfo.business_priority" id="business_priority" type="text" class="commonWidth" value="${model.business_priority}"  
									 title="${model.business_priority }"  maxlength="10" onBlur="this.value=this.value.replace(/[^\d]/g,'') "   data-helptip="【业务优先级】用于设置该业务在互斥组中的优先级。取值范围：0～2147483645，数字越大表示优先级越高。"/>
							</td>
							<div class="control-group">
								<th style="text-align: right;"  width="80">
								   <label style="text-align: right;" class="control-label">&nbsp;地市优先级：</label> 
								</th>
								<td style="text-align: left;">
									<input id="display_city_priority" name="city_priority_name" class="ztreeCombox" type="text" readonly style="width:224px;" value="${cityPriorityName }"/> 
								</td>
							</div>
							
						</tr>
						<tr>
							<td colspan="4" style="text-align: right;">
								<c:if test="${opt != 'view'}">
								<input id="btn_save" class="btn btn-small btn-primary" type="button" onclick="saveBaseInfo('${template_id}')" value="保存" />
								<c:if test="${opt == 'add'}">
								<input id="btn_reset" class="btn btn-small btn-primary" type="button" onclick="resetPage()" value="清空" />
								</c:if>
								</c:if>
							</td>
						</tr>
					</tbody>
				</table>
			</fieldset>
			</form>
		</div>
	</div>

	<!-- 基本信息 end -->
	
	<!-- 策略规则 start -->
	<div class="widget">
		<div class="widget-title">
			<h4>
				<i class="icon-reorder"></i>策略规则
			</h4>
			<div class="button-group">
			<c:if test="${opt != 'view'}">
				<button type="button" class="btn btn-small btn-primary" onclick="checkNewRule() && open_dialog('新建规则','${ctx}/policyConfigRule/forAddRule?policy_id='+$('#model_id').val());">
					<i class="icon-plus icon-white"></i>新建规则
				</button>
			</c:if>
			</div>
		</div>
		<div class="widget-body">
			<form role="form">
			<div class="form-group">
				<table class="table table-striped table-bordered table-mcdstyle table-epl" style="table-layout: fixed;" id="tbl_rules">
				   <thead>
				      <tr>
				         <th width="145" class="autocut" title="规则编码">规则编码</th>
				         <th width="15%" class="autocut" title="规则名称">规则名称</th>
				         <th width="15%" class="autocut" title="客户群">客户群</th>
				         <th width="15%" class="autocut" title="事件规则">事件规则</th>
				         <th width="10%" class="autocut" title="动作类型">动作类型</th>
				         <th >操作</th>
				      </tr>
				   </thead>
				   <tbody>
				   <c:forEach items="${rules }" var="rule">
				      <tr id="${rule.id }" pId="${rule.parentId }">
				         <td title="${rule.id }" class="autocut">${rule.id }</td>
				         <td title="${rule.policyRuleName}" class="autocut">${rule.policyRuleName}</td>
				         <td title="${rule.custgroup.custgroup_name }" class="autocut">${rule.custgroup.custgroup_name }</td>
				         <td title="${empty rule.eventCon.cep_rule_id?rule.eventCon.simple_condtions_desc:rule.eventCon.cep_rule_desc}" class="autocut">
				         	${empty rule.eventCon.cep_rule_id?rule.eventCon.simple_condtions_desc:rule.eventCon.cep_rule_desc}
				         </td>
				         <td title="${rule.actionTypeName }" class="autocut">${rule.actionTypeName }</td>
				         <td>
							<button type="button" class="btn btn-small btn-primary"  onclick="checkIsSave(this) && open_dialog('查看规则','${ctx}/policyConfigRule/view?rule_id=${rule.id}');" data-toggle="modal">
								<i class="icon-info-sign icon-white"></i>查看
							</button>
							<button type="button" class="btn btn-small btn-primary ${opt=='view'?'disabled':''}" onclick="checkIsSave(this) && open_dialog('修改规则','${ctx}/policyConfigRule/editInit?rule_id=${rule.id}');" ${opt=='view'?'disabled':''}>
								<i class="icon-edit icon-white"></i>修改
							</button>
							<button type="button" class="btn btn-small btn-danger ${opt=='view'?'disabled':''}" onclick="checkIsSave(this) && del('${rule.id}')" ${opt=='view'?'disabled':''}>
								<i class="icon-remove icon-white"></i>删除
							</button>
							<button type="button" class="btn btn-small btn-primary ${opt=='view'?'disabled':''}" onclick="checkIsSave(this) && open_dialog('添加子规则','${ctx}/policyConfigRule/forAddRule?policy_id=${model.id}&parent_id=${rule.id}');" ${opt=='view'?'disabled':''}>
								<i class="icon-plus icon-white"></i>添加子规则
							</button>
				         </td>				         
				      </tr>
				   </c:forEach>
				   </tbody>
				</table>
			</div>
			</form>
		</div>
	</div>
	<!-- 策略规则 end -->
	
	<div  style="text-align: right;padding-right: 25px;">
		<!-- 预创建、配置场景(非模板类型)编辑状态时方可保存模板 -->
		<c:if test="${opt != 'view' }">
			<c:if test="${empty model.policy_status_id || (model.template_flag==0 && model.policy_status_id==20)}">
		 	<input id="btn_save_template" class="btn  btn-small btn-primary" type="button" onclick="checkIsSave() && saveTemplate();" value="保存模板"/>
		 	&nbsp;
		 	<input id="btn_submit" class="btn  btn-small btn-primary" type="button" onclick="checkIsSave() && submitApprove();" value="提交" />
		 	</c:if>
		</c:if>
	</div>
	
	<!-- 模板类型选择框 start -->
	<div class="modal hide fade" id="dlg_template">
	  <div class="modal-header">
	    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
	    <h4>请选择模板类型</h4>
	  </div>
	  <div class="modal-body">
	  	<label class="radio">
	    	<input type="radio" name="rd_template_flag" value="1" checked>私有模板
	    </label>
	    <label class="radio">
	    	<input type="radio" name="rd_template_flag" value="2">公有模板
	    </label>
	  </div>
	  <div class="modal-footer">
	  		<input type="button" class="btn btn-primary btn-small" id="dlg_template_ok" value="确认" >
			<input type="button" class="btn btn-primary btn-small" id="dlg_template_cancle"  value="取消">
	  </div>
	</div>
	<!-- 模板类型选择框 end -->
	<!-- 删除确认框 start -->
	<div id="myModal_del" style="display:none;">
		<div class="modal-body" >确认删除？</div>
		<div class="modal-footer">
			<input type="button" class="btn btn-primary btn-small" id="dlg_delete_ok" value="确认" >
			<input type="button" class="btn btn-primary btn-small" id="dlg_delete_cancle"  value="取消">
		</div>
	</div>
	<!-- 删除确认框 end -->
	<!-- 提交确认框 start -->
	<div id="myModal_submit" style="display:none;">
		<div class="modal-body" >是否确认提交审批？</div>
		<div class="modal-footer">
			<input type="button" class="btn btn-primary btn-small" id="dlg_submit_ok" value="确认" >
			<input type="button" class="btn btn-primary btn-small" id="dlg_submit_cancle"  value="取消">
		</div>
	</div>
	<!-- 提交确认框 end -->
</div> <!-- mainDiv end -->
<%@ include file="/WEB-INF/layouts/bottom.jsp"%>
<script type="text/javascript">

$(function(){
	//查看表单只读设置
	viewInit();
    $("#navigation_j").attr("class","on");
	$("#navigation_j").click(function(){
		$("#navigation_j").attr("class","on");
		$("#navigation_g").attr("class","");
		$("#navigation_t").attr("class","");
	});
	$("#navigation_g").click(function(){
		if($('#model_id').val().length<=0){
			alert("请先填写基本信息，并保存！");
			$("#navigation_j").attr("class","on");
			$("#navigation_g").attr("class","");
			$("#navigation_t").attr("class","");
		}else{
			$("#navigation_j").attr("class","");
			$("#navigation_g").attr("class","on");
			$("#navigation_t").attr("class","");
			open_dialog('新建规则','/pop/policyConfigRule/forAddRule?policy_id='+$('#model_id').val());
		}
	});
	$("#navigation_t").click(function(){
		//先检查基本信息
		if($('#model_id').val().length<=0){
			alert("请先填写基本信息，并保存！");
			$("#navigation_j").attr("class","on");
			$("#navigation_g").attr("class","");
			$("#navigation_t").attr("class","");
		}else if(checkIsSave()){//在检查规则信息
			alert("请先新建规则之后再提交！");
			$("#navigation_j").attr("class","");
			$("#navigation_g").attr("class","on");
			$("#navigation_t").attr("class","");
			open_dialog('新建规则','/pop/policyConfigRule/forAddRule?policy_id='+$('#model_id').val());
		}else{
			$("#navigation_j").attr("class","");
			$("#navigation_g").attr("class","");
			$("#navigation_t").attr("class","on");
			alert("请点击页面下方“保存成模板”或者“提交”完成策略策划");
		}
		
	});
	
	//地市优先级
	$("#display_city_priority").ztreeComb({
		"treeData":[],
		"useCheckbox":false,
		"listheight":240,
		"hiddenName":"popPolicyBaseinfo.city_priority" ,
		getsearchData:function(txt,treeId){
			listCityPriorityFromCache(treeId , txt,"city_priority");
		},
		useCombBtn:${opt != 'view'}
	});
	var city_priority_ztreeId = $("#display_city_priority").attr("treeid");
	listCityPriorityFromCache(city_priority_ztreeId , '',"city_priority");
	$("#display_city_priority").val("${city_priority_name}");
	$("input[name='popPolicyBaseinfo.city_priority']").val("${city_priority}");
});



/**保存基本信息*/
function saveBaseInfo(template_id){
	if(validationBeforeSave()){//表单校验
		if(typeof template_id== 'undefined'){
			template_id = "";
		}
		$.ajax({
			url : "${ctx}/policyConfig/saveBaseInfo?template_id="+template_id,
			type : "POST",
			data : $("#frm_baseinfo").serialize(),
			success : function(result) {
				if (result.success == 1) {
					if(template_id == ""){
						$('#model_id').val(result.id);//回写新生成的基本信息id
						alert($('#policy_name').val()+'策略保存成功!您可以点击“新建规则”按钮，新建策略规则。');
					}else{
						alert($('#policy_name').val()+'策略保存成功!');
						window.location.href="${ctx}/policyConfig/edit?id="+result.id;
					}
				} else {
					alert($('#policy_name').val()+'策略保存失败!');
				}
			}
		});
	}
}

/**
 * 改变错误样式
 */
function changeErrorStyle(id){
	id = '#' + id;
	var oldCss = $(id).css('border-color');
	$(id).css('border-color','red').focus();
	setTimeout(function(){$(id).css('border-color',oldCss)}, 4000);
}

/**保存前验证*/
function validationBeforeSave(){
	var policy_name_val = $('#policy_name').val();
	var policy_desc_val = $('#policy_desc').val();
	var custom_service_diameter_val = $('#custom_service_diameter').val();
	var business_priority_val = $('#business_priority').val();
	if(policy_name_val.length >75){
		alert('策略名称长度不能大于75');
		changeErrorStyle('policy_name');
		return false;
	}
	if(policy_name_val.length == 0){
		alert('策略名称不能为空');
		changeErrorStyle('policy_name');
		return false;
	}
	if(policy_desc_val.length >100 || policy_desc_val.length < 10){
		alert('场景描述长度必须在10至100之间');
		changeErrorStyle('policy_desc');
		return false;
	}
	if(custom_service_diameter_val.length >500 || policy_desc_val.length < 0){
		alert('客服口径长度不能超过500');
		changeErrorStyle('custom_service_diameter');
		return false;
	}
	if(business_priority_val >2147483645 || business_priority_val < 0){
		alert('【业务优先级】取值范围：0～2147483645');
		changeErrorStyle('rule_priority');
		return false;
	}	
	if($('#display_start_time').val().length == 0){
		alert('有效期开始日期不能为空');
		changeErrorStyle('display_start_time');
		return false;
	}
	if($('#display_end_time').val().length == 0){
		alert('有效期结束日期不能为空');
		changeErrorStyle('display_end_time');
		return false;
	}
	var isRepeat = false;
	//策略名称重复检查
	$.ajax({
		url : "${ctx}/policyConfig/checkPolicyNameRepeat",
		type : "POST",
		async :false,
		data : {policy_name:policy_name_val,policy_id:$('#model_id').val()},
		success : function(result) {
			if (result.success == 1 && result.flag == true) {
				alert('名称为:'+policy_name_val+'策略已经存在,请重新填写策略名称!');
				changeErrorStyle('policy_name');
				$('#policy_name').val('');
				isRepeat = true;
			}
		}
	});
	if(isRepeat){
		return false;
	}
	return true;
}

$('#dlg_template_cancle').click(function(){
	$('#dlg_template').modal('hide');
});
$('#dlg_template_ok').click(function(){
	$('#dlg_template').modal('hide');
	var template_flag = $(':checked[name="rd_template_flag"]').val();
	//提交保存模板
	if(validationBeforeSave()){//表单校验
		$.ajax({
			url : "${ctx}/policyConfig/saveTemplate?template_flag="+template_flag,
			type : "POST",
			data : $("#frm_baseinfo").serialize(),
			success : function(result) {
				if (result.success == 1) {
					alert($('#policy_name').val()+'策略模板保存成功!');
					window.location.href="${ctx}/policyConfig/edit?id="+result.id;
				} else {
					alert($('#policy_name').val()+'策略模板保存失败!'+result.msg);
				}
			}
		});
	} 
});
/**保存成模板*/
function saveTemplate(){
	$('#dlg_template').modal('show');
}

/**检查是否可以新建规则*/
function checkNewRule(){
	if($('#model_id').val().length>0){
		return true;
	}else{
		alert('请先保存基本信息!');
		return false;
	}
}

/**打开模态对话框*/
function open_dialog(title,src){
    var dialogParent = $("#myModal_div").parent();
    var dialogOwn = $("#myModal_div");
    var dialogBak = $("#myModal_div").clone();
    dialogOwn.find("#myModal_frame").attr("src",src);
    $("#myModal_div").dialog({
        autoOpen: true,
        title: title,
        width: $(window).width()-30,
		resizable:true,
		modal: true,
        close: function() {
        		dialogBak.appendTo(dialogParent);
	        	$(this).dialog("destroy").remove(); 
        }
    });
}
/**新增规则页面保存后回调*/
function callback_save_addrule(){
	$("#myModal_div").dialog("close");
	window.location.href="${ctx}/policyConfig/edit?id="+$('#model_id').val();
}
/**新增规则页面关闭后回调*/
function callback_close_addrule(){
	$("#myModal_div").dialog("close");
}

/**提交审批*/
function submitApprove(){
	if($('#tbl_rules tr[depth]').length==0){
		alert('尚未添加规则,禁止提交审批!');
		return;
	}
	window.mcdDialog("myModal_submit",{
		autoOpen: true,
		width: 430,
		height:"auto",
		title:"提示",
		modal: true,
		resizable:false
	});
}
$('#dlg_submit_cancle').click(function(){
	$("#myModal_submit").dialog("close");
});
$('#dlg_submit_ok').click(function(){
	$("#myModal_submit").dialog("close");
	$.ajax({
		url : "${ctx}/policyConfig/submitPolicy",
		type : "POST",
		data : {policy_id:$('#model_id').val()},
		success : function(result) {
			if (result.success == 1) {
				alert(result.msg);
				if(window.parent && window.parent && window.parent.search){
					window.parent.search();
				}
				window.location.href="${ctx}/policyConfig/edit?id="+$('#model_id').val();
			} else {
				alert(result.msg);
			}
		}
	});
});
/**删除规则*/
var ruleId;
function del(_ruleId){
	ruleId = _ruleId;
	window.mcdDialog("myModal_del",{
		autoOpen: true,
		width: 430,
		height:"auto",
		title:"删除",
		modal: true,
		resizable:false
	});
}
$('#dlg_delete_cancle').click(function(){
	$("#myModal_del").dialog("close");
});
$('#dlg_delete_ok').click(function(){
	$("#myModal_del").dialog("close");
	$.ajax({
		url : "${ctx}/policyConfigRule/delete",
		type : "POST",
		data : {rule_id:ruleId},
		success : function(result) {
			if (result.success == 1) {
				alert("删除策略规则【"+$('#'+ruleId+' > td:nth-child(2)').text()+"】操作成功！");
				window.location.href="${ctx}/policyConfig/edit?id="+$('#model_id').val();
			} else {
				alert("删除规则策略【"+$('#'+ruleId+' > td:nth-child(2)').text()+"】操作失败：" + result.msg);
			}
		}
	});
});

/**从模板创建*/
function add_from_template(){
	var value = $("#templateId option:selected").val();
	if(value == 999){
		return;
	}
	$('#frm_baseinfo').attr('action','${ctx}/policyConfig/addInit?from=template&template_id='+value).submit();
}

/**检查是否保存信息*/
function checkIsSave(thisobj){
	if(thisobj && $(thisobj).is(".disabled")){
		return false;
	}
	if($('#model_id').val().length==0){
		alert('请先保存,再操作');
		return false;
	}
	return true;
}

/** 检查是否重复*/
function checkRepeat(){
	var $policy_name = $('#policy_name');
	var value = $policy_name.val();
	if(value.length == 0){
		return;
	}
	$.ajax({
		url : "${ctx}/policyConfig/checkPolicyNameRepeat",
		type : "POST",
		async :false,
		data : {policy_name:value,policy_id:$('#model_id').val()},
		success : function(result) {
			if (result.success == 1 && result.flag == true) {
				alert('名称为:'+value+'策略已经存在,请重新填写策略名称!');
				$policy_name.val('').focus();
			}
		}
	});
}

/**清空页面，初始化本页面*/
function resetPage(){
	window.location.href="${ctx}/policyConfig/addInit";
}

/**页面加载完毕后执行*/
$(function(){
	//日期控件初始化
	$('.form_date').datetimepicker({language:'zh-CN',weekStart:1,todayBtn:1,autoclose: 1,
	todayHighlight: 1,startView: 2,minView: 2,forceParse: 0,initialDate: new Date(),startDate:new Date()
	}).on('changeDate', function(ev){
	     if('ctrl_start_time' == this.id){
	    	 $('#ctrl_end_time').datetimepicker('setStartDate', ev.date.toString("yyyy-MM-dd"));
	     }else if('ctrl_end_time' == this.id){
	    	 $('#ctrl_start_time').datetimepicker('setEndDate', ev.date.toString("yyyy-MM-dd"));
	     }
	});
	
	<%if("view".equals(opt)){%>
		$('.add-on').hide();
	<%}%>
	//规则表格树插件初始化
	$('#tbl_rules').treeTable({theme:'vsStyle',expandLevel:999});
	//根据表格深度树展开行宽度自适应
	var maxDepth = 1;
	$('#tbl_rules tr:not(:first)').each(function(){
		$(this).attr('depth')>maxDepth && (maxDepth=$(this).attr('depth'));
	});
	$('#tbl_rules th:first').width($('#tbl_rules th:first').width()+(maxDepth-1)*18);
	
	//同步父窗体iframe高度
	$("#myModal_frame", window.parent.document).load(function(){
		var docHeight = $(document).height() + 30;
		if($(this).height()<docHeight){
			$(this).height(docHeight);
		}
	});
});

//查看表单只读设置
function viewInit(){
	if(${opt == 'view'}){
		$("#display_city_priority").attr("disabled","disabled");
	}
}
</script>
</body>
</html> 