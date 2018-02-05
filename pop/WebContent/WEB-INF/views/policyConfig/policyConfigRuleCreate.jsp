<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="java.util.List"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="/WEB-INF/layouts/head.jsp"%>
<title>策略规则</title>
<style type="text/css">
html, body {
	height: 100%;
}

.modal-body {
	max-height: 100%;
}

.line {
	border: 1px solid #ccc;
	border-radius: 15px;
}

.form-horizontal .control-label {
	width: 100px;
	font-weight: bold;
}

label {
	cursor: text;
}
.btvdclass{
    border-color:#FF0000;
    color:red;
    font-weight:normal;
}
.popover-content{ padding: 8px;font-size: 11px;line-height: 1.4;}
li {list-style-type:none;}
ul {margin-left:100px;}
ul.exe_time_box{clear: both;}
ul.exe_time_box title{display:inline-block;}
.hasSaved li{overflow: hidden;float:left; height:24px; line-height:24px; padding:0 4px 0 6px; margin-bottom:8px; font-size:12px; font-weight:bold; color:#1070c1; border:1px solid #aecde4; background:#e8f2f6;padding-right: 20px;}
.hasSaved li .time{margin:0 8px 0 12px;}
.hasSaved li .close{position: absolute;right:0;top:2px;float:right; width:15px; height:15px; display:inline; margin:2px 5px 0 0; background:url(${ctx}/static/img/btn_close.png) no-repeat center center; text-decoration:none;}
.hasSaved li .close:hover{background:url(${ctx}/static/img/btn_close_hover.png) no-repeat;}
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
	<c:if test="${flag != 'view'}"> 
     <ul id="tab-widget-title" class="tab widget-title">
		 <li id="navigation_1" class="on" onclick="funChangeTab(this.id);"><em class="left"></em><span>1、填写规则名称</span><em></em></li>
		 <li id="navigation_2" onclick="funChangeTab(this.id);"><em class="left"></em><span>2、选择客户群</span><em></em></li>
		 <li id="navigation_3" onclick="funChangeTab(this.id);"><em class="left"></em><span>3、 设置事件规则</span><em></em></li>
		 <li id="navigation_4" onclick="funChangeTab(this.id);"><em class="left"></em><span>4、 设置执行时间</span><em></em></li>
		 <li id="navigation_5" onclick="funChangeTab(this.id);"><em class="left"></em><span>5、 设置动作类型</span><em></em></li>
	</ul>
	</c:if> 
	<div id="mainDiv" class="modal-body" style="width: auto; height: auto">
	<div class="container-fluid">
		<form id="ruleForm" method="post" class="form-horizontal">
		<input type="hidden" name="popPolicyRule.policy_id" value="${param.policy_id }">
		<input type="hidden" name="popPolicyRule.parent_id" value="${param.parent_id }" >
		<input type="hidden" name="rule_id" id="rule_id" value="${popPolicyRule.id }" >
		<input type="hidden" name="startDate" id='startDate' value="${startDate }"/>
		<input type="hidden" name="endDate" id='endDate' value="${endDate }"/>
			<div style="height: auto;">
				<fieldset  <c:if test="${flag == 'view'}"> disabled </c:if> >
					<div class="control-group">
					   <label style="text-align: right;" class="control-label">&nbsp;<font color="red">*</font>规则编码：</label> 
						<input type="text" name="rule_id" id="rule_id" value="${rule_id}" readonly> 
					</div>
					<div class="control-group">
					   <label style="text-align: right;" class="control-label">&nbsp;<font color="red">*</font>PCCID：</label> 
						<input id="display_pcc_id"  name="display_pcc_id" class="ztreeCombox"   type="text"  readonly      style="width:224px;"/> 
					</div>
					<div class="control-group">
						<label style="text-align: right;" class="control-label">&nbsp;<font color="red">*</font>规则名称：</label> 
						<input id="policy_rule_name" type="text" name="popPolicyRule.policy_rule_name"  style="width: 240px" value="${popPolicyRule.policy_rule_name}"  data-helptip="规则名称长度必须在1至75之间"  onBlur="this.value=this.value.replace(/[`~!@#$^&*()+=|\\\[\]\{\}:;'\,.<>/?]/g,'')">
					</div>
					<div class="control-group">
					   <label style="text-align: right;" class="control-label">&nbsp;客户群：</label> 
						<input id="display_custgroup_id"  name="display_custgroup_id" class="ztreeCombox"   type="text"  readonly      style="width:224px;" data-helptip="客户群、简单规则和复杂事件规则三者至少选择一个"/> 
					</div>
					<div class="control-group">
						<label style="text-align: right;" class="control-label">&nbsp;事件规则：</label> 
						<input id="btn_show_rule_set" type="button" class="btn btn-primary btn-small" onclick="showRuleSetWindow()" value="事件设置" <c:if test="${flag == 'view'}"> disabled </c:if>/> 
						<div id="showCEPEventRule" style="display:none;margin-left: 100px;">
						<ul class='hasSaved' id="cepEvent_ul" style="display:inline-block;margin:5px 0px 0px 0px;" >
							<li style='white-space: nowrap;'>复杂事件规则:</li>
							<li>${popPolicyRuleEventCon.cep_rule_desc}<a class="close" href="javascript:void(0)" onclick="deleteCepEventRule($(this).parent())">&nbsp;</a> </li>
							<input id="btn_get_LACCIInfo" type="button" class="btn btn-primary btn-small" onclick="getLACCIInfo()" value="显示基站信息"/> 
						</ul>
						</div>
						
						<div id="showSimplseEventRule" style="display:none;margin-left: 100px;">
						<ul class='hasSaved' id="simpleEvent_ul" style="display:inline-block;margin:5px 0px 0px 0px;" >
								<li style='white-space: nowrap;'>简单规则:</li>
								<li title='${popPolicyRuleEventCon.simple_condtions_desc}' style="position: relative;">${popPolicyRuleEventCon.simple_condtions_desc}<a class="close" href="javascript:void(0)" onclick="deleteSimpleEventRule($(this).parent())">&nbsp;</a> </li>
								<li id="simple_rule_ext_term" style="display: block;clear: left;">
									<span>终端厂商:${terminal_manu }, 终端品牌:${brand_name }</span>
									<a id="a_view_term" href="javascript:void(0)" style="color: #2FADE7;" onclick="view_term_detail(this)">终端详细</a>
								</li>
								<li id="simple_rule_ext_area" style="display: block;clear: left;">
									<span>位置类型:${area_type_name }</span>
									<a id="a_view_area" href="javascript:void(0)" style="color: #2FADE7;" onclick="view_area_detail(this)">位置详细</a>
								</li>
						</ul>
						</div>
						
						<input type="hidden" id="cep_rule_id" name="cep_rule_id" value="${popPolicyRuleEventCon.cep_rule_id}">
						<input type="hidden" id="cep_rule_desc" name="cep_rule_desc" value="${popPolicyRuleEventCon.cep_rule_desc}">
						<!-- jinl 20160413 选择基站信息 start-->
						<input type="hidden" id="lacciAndStationTypeJsons" name="lacciAndStationTypeJsons" value='${popPolicyRuleEventCon.cep_lacciAndStationTypeJsons}'>
						<input type="hidden" id="choosedLacciAndCount" name="choosedLacciAndCount" value="${popPolicyRuleEventCon.cep_choosedlacci_desc}">
						<input type="hidden" id="cepQueryLacciInfoUrl" name="cepQueryLacciInfoUrl" value="${cepQueryLacciInfoUrl}">
						<!-- jinl 20160413 选择基站信息 end-->
						<input type="hidden" id="simple_condtions_data" name="simple_condtions_data" value='${popPolicyRuleEventCon.simple_condtions_data}'>
						<input type="hidden" id="simple_condtions_desc" name="simple_condtions_desc" value='${popPolicyRuleEventCon.simple_condtions_desc}'>
						<input type="hidden" id="model_ids" name="model_ids" value='${model_ids}'>
						<input type="hidden" id="model_names" name="model_names" value='${model_names}'>
						</div>
						
						
					<div class="control-group">
						<label style="text-align: right;" class="control-label">&nbsp;<font color="red">*</font>执行时间 ：</label> 
						<input  id="btn_set_action_date" class="btn btn-small btn-primary" type="button" onclick="setActionDate()" value="日期设置" <c:if test="${flag == 'view'}"> disabled </c:if>/>
						
						<div id="showDataRanges" style="display:none;margin-left: 100px;">
							<ul class='hasSaved' id="showDataRanges_ul" style="display:inline-block;margin:5px 0px 0px 0px;" >
								<li style='white-space: nowrap;'>执行日期:</li>
								<li>${popPolicyRuleExecTime.date_ranges}<c:if test="${flag != 'view'}"><a class="close" href="javascript:void(0)" onclick="deleteDate($(this).parent());">&nbsp;</a></c:if></li>
							</ul>
						</div>
						<div id="showTimeRanges"  style="display:none;margin-left: 100px;">
							<ul class='hasSaved' id="showTimeRanges_ul" style="display:inline-block;margin:5px 0px 0px 0px;">
								<li style='white-space: nowrap;'>接触时段:</li>
								<li>${popPolicyRuleExecTime.time_ranges}<c:if test="${flag != 'view'}"><a class="close" href="javascript:void(0)" onclick="deleteTimeRange($(this).parent())">&nbsp;</a></c:if></li>
							</ul>
						</div>
						<div id="showAvoidRanges" style="display:none;margin-left: 100px;">
							<ul class='hasSaved' id="showAvoidRanges_ul" style="display:inline-block;margin:5px 0px 0px 0px;">
									<li style='white-space: nowrap;'>屏蔽时段:</li>
									<li>${popPolicyRuleExecTime.avoid_ranges}<c:if test="${flag != 'view'}"><a class="close" href="javascript:void(0)" onclick="deleteAvoidRange($(this).parent())">&nbsp;</a></c:if></li>
							</ul>
						</div>
						
					</div>
				<!-- add by jinl 20160411 基站信息选择 display:none; -->
				<div class="control-group">
						<label style="text-align: right;" class="control-label">基站信息选择 ：</label> 
						<input  id="btn_set_LacciInfo" class="btn btn-small btn-primary" type="button" onclick="chooseLacciInfoWindow()" value="基站信息选择 " <c:if test="${flag == 'view'}"> disabled </c:if>/>
						<div id="showCEPLacciInfo" style="margin-left: 100px;">
						<ul class='hasSaved' id="LacciInfo_ul" style="display:inline-block;margin:5px 0px 0px 0px;" >
							<li style='white-space: nowrap;'>所选择的基站信息:</li>
							<li>${popPolicyRuleEventCon.cep_choosedlacci_desc}<a class="close" href="javascript:void(0)" onclick="deleteCepEventRule($(this).parent())">&nbsp;</a> </li>
							<input id="btn_LACCIInfo_export" type="button" class="btn btn-primary btn-small" onclick="getLACCIInfoExport()" value="导出"/> 
						</ul>
						</div>
				
				</div>	
					
				<!-- 执行时间选择框 start -->
				<div class="modal hide fade" id="actionDateId" style="height: 500px;width: 600px;">
				 
				  <div class="modal-header">
				    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
				    <h4>执行时间</h4>
				  </div>
				  
				  <div class="modal-body" style="height: 400px;">
				  <div class="control-group">
				  <label class="control-label">&nbsp;<font color="red">*</font>执行日期：</label> 
				  		<span id="ctrl_start_time" class="controls input-append date form_date"  style="margin-left: 0px;"data-date-format="yyyy-mm-dd">
		                    	<input type="text" id="absoluteDateStart" name="start_date" style="width: 80px;background-color:#fff;cursor: pointer;" readonly >
			                    <span class="add-on"><i class="icon-remove"></i></span>
								<span class="add-on"><i class="icon-th"></i></span>
	               		 </span>-
	               		 <span id="ctrl_end_time" class="controls input-append date form_date"  style="margin-left: 0px;" data-date-format="yyyy-mm-dd">
		                    	<input type="text" id="absoluteDateEnd"  name="start_date" style="width: 80px;background-color:#fff;cursor: pointer;"  readonly onchange="addDateBatch();">
			                    <span class="add-on"><i class="icon-remove"></i></span>
								<span class="add-on"><i class="icon-th"></i></span>
	             		 </span>
		               <div>
		              		 <span>
			                	<input type="hidden" name="date_ranges" id="date_ranges"  value="${popPolicyRuleExecTime.date_ranges }">
			                	<ul class="hasSaved" id="showAddDate">
									<c:if test="${not empty popPolicyRuleExecTime.date_ranges}">
				                		<li style="white-space: nowrap;">${popPolicyRuleExecTime.date_ranges }
											<a class="close" onclick="deleteDate($(this).parent());" href="javascript:void(0)">&nbsp;</a>
										</li>
									</c:if>
								</ul>
			                </span>
			            </div>
			            
			            <!-- 规则级模版 -->
						<script type="text/x-jquery-tmpl" id="templateAddDate">
							<li style="white-space: nowrap;">{{= tplDate }}<a class="close"   href="javascript:void(0)" onclick="deleteDate($(this).parent());">&nbsp;</a></li>
						</script>
	               </div>
				  	<div class="control-group">
						<label class="control-label">&nbsp;<font color="red">*</font>接触时段：</label>
						<div style="position:relative;">
							<input type="hidden" name="time_ranges" id="time_ranges" value="${popPolicyRuleExecTime.time_ranges }">
							<input  name="timeRangeTmp" id="timeRangeTmp"  class="timeRange" type="text" style="width: 276px;cursor: pointer;" readonly>
						</div>
						<div>
							<span>
		                	<ul class="hasSaved" id="showTimeRange">
		                		<c:if test="${not empty popPolicyRuleExecTime.time_ranges}">
				                		<li style="white-space: nowrap;">${popPolicyRuleExecTime.time_ranges }
											<a class="close" onclick="deleteDate($(this).parent());" href="javascript:void(0)">&nbsp;</a>
										</li>
								</c:if>
		                	</ul>
		                	</span>
						</div>
					</div>
					
					<div class="control-group">
						<label class="control-label">&nbsp;屏蔽时段：</label> 
						<div style="position:relative;">
							<input type="hidden" name="avoid_ranges" value="${popPolicyRuleExecTime.avoid_ranges }">
							<input  name="avoidTimeRangeTmp" id="avoidTimeRangeTmp" class="timeRange" type="text" style="width: 276px;cursor: pointer;" readonly>
						</div>
						<div>
							<span>
		                	<ul class="hasSaved" id="showAvoidRange">
		                		<c:if test="${not empty popPolicyRuleExecTime.avoid_ranges}">
				                		<li style="white-space: nowrap;">${popPolicyRuleExecTime.avoid_ranges }
											<a class="close" onclick="deleteDate($(this).parent());" href="javascript:void(0)">&nbsp;</a>
										</li>
								</c:if>
		                	</ul>
		                	</span>
						</div>
					</div>
				  </div>
				  
				  <div class="modal-footer">
				  	<input type="button" class="btn btn-primary btn-small" id="setActionDate_ok" value="确定"/>
				  	<input type="button" class="btn btn-primary btn-small" id="setActionDate_cancle" value="关闭"/>
				  </div>
				</div>
				<!-- 执行时间选择框 end -->
					
					
					
					<div class="control-group">
						<label style="text-align: right;" class="control-label">&nbsp;<font color="red">*</font>动作类型：</label> 
						<label class="radio inline">
							<input type="radio" name="policy_act_type_id" value="1" <c:if test="${popPolicyRuleAct.policy_act_type_id == 1 || flag == 'add'}"> checked </c:if>>管控类
						</label>
						<label class="radio inline">
							<input type="radio" name="policy_act_type_id"  value="2" <c:if test="${popPolicyRuleAct.policy_act_type_id == 2 }"> checked </c:if>>营销类
						</label>
					</div>
					<div id="controlType" style="<c:if test="${popPolicyRuleAct.policy_act_type_id == 2}">display: none;</c:if>">
						<div class="line"  style="margin-bottom: 25px;">
							<div class="control-group" style="margin-top: 10px;margin-left: 15px;">
								<label style="text-align: right;" class="control-label">&nbsp;管控动作：</label> 
								<select style="width: 255px" name="control_type_id">
									<c:forEach items="${dimControlTypes }" var="dimControlTypes">
										<option value="${dimControlTypes.id}" <c:if test="${dimControlTypes.id == popPolicyRuleAct.control_type_id  }">selected</c:if> >${dimControlTypes.name}</option>.
									</c:forEach>
								</select>
							</div>
							<div class="control-group" style="margin-top: 10px;margin-left: 15px;">
								<label style="text-align: right;" class="control-label">&nbsp;<font color="red">*</font>动作参数：</label>
								 <input  type="text" id="control_param" name="control_param" style="width: 240px" value="${popPolicyRuleAct.control_param }" maxlength="6" onBlur="this.value=this.value.replace(/[^\d]/g,'') " data-helptip="动作参数必须输入整数">
							</div>
						</div>
					</div>
					
					<div id="marketType" style="<c:if test="${popPolicyRuleAct.policy_act_type_id == 1 || flag == 'add'}">display: none;</c:if>">
						<div style="margin-left: 15px;font-weight: bold;">执行通知</div>
						<div class="line"  style="margin-bottom: 25px;">
							<div class="control-group" style="margin-top: 10px;margin-left: 15px;">
								<label class="control-label">&nbsp;通知方式：</label>
								 <select name="exec_channel_id" style="width: 255px">
									<c:forEach items="${popDimCampChannel }" var="popDimCampChannel">
									<option value="${popDimCampChannel.id}"  <c:if test="${popDimCampChannel.id == popPolicyRuleAct.exec_channel_id  }"> selected </c:if> >${popDimCampChannel.name}</option>.
								   </c:forEach>
								</select> 
								<label style="font-weight: bold;display: inline-block;">&nbsp;&nbsp;通知频次: </label>
								<select name="exec_camp_frequency" style="width: 255px" >
									<c:forEach items="${popDimContactFreq }" var="popDimContactFreq">
									 <option value="${popDimContactFreq.id}"  <c:if test="${popDimContactFreq.id == popPolicyRuleAct.exec_camp_frequency  }"> selected </c:if> >${popDimContactFreq.name}</option>.
								   </c:forEach>
								</select>
							</div>

							<div class="control-group" style="margin-left: 15px;">
								<label   class="control-label" >&nbsp;通知内容：</label>
								<textarea id="exec_camp_content" name="exec_camp_content" rows="2"  style="height: 40px; resize: none; width: 780px" maxlength="1000" data-helptip="执行通知内容和失效通知内容至少填写一个；通知内容不填写，其通知方式和通知频次不生效">${popPolicyRuleAct.exec_camp_content }</textarea>
							</div>
						</div>
						
						<div style="margin-left: 15px;font-weight: bold;">失效通知</div>
						<div class="line"  style="margin-bottom: 25px;"  >
							<div class="control-group" style="margin-top: 10px;margin-left: 15px;">
								<label class="control-label">通知方式：</label>
								 <select name="invalid_channel_id" style="width: 255px">
									<c:forEach items="${popDimCampChannel }" var="popDimCampChannel">
									 <option value="${popDimCampChannel.id}"  <c:if test="${popDimCampChannel.id == popPolicyRuleAct.invalid_channel_id  }"> selected </c:if> >${popDimCampChannel.name}</option>.
								   </c:forEach>
								</select> 
								<label style="font-weight: bold;display: inline-block;">&nbsp;&nbsp;通知频次: </label>
								<select name="invalid_camp_frequency" style="width: 255px">
									<c:forEach items="${popDimContactFreq }" var="popDimContactFreq">
									 <option value="${popDimContactFreq.id}"  <c:if test="${popDimContactFreq.id == popPolicyRuleAct.invalid_camp_frequency  }"> selected </c:if> >${popDimContactFreq.name}</option>.
								   </c:forEach>
								</select>
							</div>
							<div class="control-group" style="margin-left: 15px;">
								<label class="control-label" for="note">&nbsp;通知内容：</label>
								<textarea id="invalid_camp_content" name="invalid_camp_content" rows="2" 
									style="height: 40px; resize: none; width: 780px" maxlength="1000" data-helptip="执行通知内容和失效通知内容至少填写一个；通知内容不填写，其通知方式和通知频次不生效">${popPolicyRuleAct.invalid_camp_content}</textarea>
							</div>
						</div>
						
						<div class="control-group">
							<label style="text-align: right;" class="control-label">&nbsp;免打扰客户：</label>
							<input id="display-avoid_custgroup_ids"  name="display-avoid_custgroup_ids"  type="text"  class="ztreeCombox"   readonly  style="width:224px;" /> 
						</div>
					</div>

					<div class="control-group" >
						<label style="text-align: right;" class="control-label" for="note">&nbsp;规则优先级：</label> 
						<input id="rule_priority" name="rule_priority"  type="text" style="width: 240px" value="${popPolicyRule.rule_priority }" maxlength="10" onBlur="this.value=this.value.replace(/[^\d]/g,'') "  data-helptip="【规则优先级】用于标识规则执行时的优先级。取值范围：0～2147483645，数字越小表示优先级越高。"> 
					</div>
				</fieldset>

					<div style="background: #F5F5F5; height: 20px; padding: 10px;">
						<div class="btns" style="float: right;">
						<c:if test="${flag != 'view'}">
						 	<input type="button" class="btn btn-primary btn-small" onclick="saveRule()" value="保存" /> 
						 </c:if>
						<input id="btn_close" class="btn btn-primary btn-small" type="button"  onclick="window.parent.callback_close_addrule()" value="关闭" />
						</div>
					</div>
			</div>
		</form>
</div>
	</div>
	<%@ include file="/WEB-INF/layouts/bottom.jsp"%>
	<script type="text/javascript" src="${ctx}/static/js/timeRange.js" ></script>
	<script type="text/javascript" src="${ctx}/static/js/jquery.tmpl.min.js"></script>
	
	<!-- 接触时间段模版 -->
	<script type="text/x-jquery-tmpl" id="templateTimeRange">
	<li style="white-space: nowrap;">{{= tplDate }}<a class="close"  href="javascript:void(0)" onclick="deleteTimeRange($(this).parent())">&nbsp;</a></li>
	</script>
	<!-- 屏蔽时间段扰模版 -->
	<script type="text/x-jquery-tmpl" id="templateAvoidRange">
	<li style="white-space: nowrap;">{{= tplDate }}<a class="close"  href="javascript:void(0)" onclick="deleteAvoidRange($(this).parent())">&nbsp;</a></li>
	</script>
	<script id="terms_tmpl" type="text/x-jquery-tmpl"><tr><td>{{= id}}</td><td>{{= name}}</td></tr></script>
	<script id="areas_tmpl" type="text/x-jquery-tmpl"><tr><td>{{= id}}</td><td>{{= name}}</td></tr></script>
	<script type="text/javascript">
		jQuery(document).ready(function() {
			//查看表单只读设置
			viewInit();
			$("#navigation_1").click(function(){
				$("#policy_rule_name").focus();
			});
			$("#navigation_2").click(function(){
				$("#display_custgroup_id").focus();
				$("#display_custgroup_id").click();
			});
			$("#navigation_3").click(function(){
				$("#btn_show_rule_set").focus();
				$("#btn_show_rule_set").click();
			});
			$("#navigation_4").click(function(){
				$("#btn_set_action_date").focus();
				$("#btn_set_action_date").click();
			});
				
			//动作类型radio改变事件
			$(':radio[name="policy_act_type_id"]').change(function(){
				if (this.value == '2') {
					$("#controlType").hide();
					$("#marketType").show();
				}else if(this.value == '1') {
					$("#marketType").hide();
					$("#controlType").show();
				}
			});
			//客户群
			$("#display_custgroup_id").ztreeComb({
				"treeData":[],
				"useCheckbox":false,
				"listheight":240,
				"hiddenName":"custgroup_id" ,
				getsearchData:function(txt,treeId){
					listSearchFromCache(treeId , txt,"custGroup");
				},
				useCombBtn:${flag != 'view'}
			});
			
			var statusIds_ztreeId = $("#display_custgroup_id").attr("treeid");
			listSearchFromCache(statusIds_ztreeId , '',"custGroup");
			
			<c:if test="${flag != 'add'}">
				$("#display_custgroup_id").val("${popPolicyRuleCustgroup.custgroup_name}");
				$("input[name='custgroup_id']").val("${popPolicyRuleCustgroup.custgroup_id}");
				
				<c:if test="${not empty popPolicyRuleExecTime.date_ranges}">  
					$("#showDataRanges").show();
				</c:if>
				
				<c:if test="${not empty popPolicyRuleExecTime.time_ranges}">
					$("#showTimeRanges").show();
				</c:if>
				
				<c:if test="${not empty popPolicyRuleExecTime.avoid_ranges}">
					$("#showAvoidRanges").show();
				</c:if>
				
				<c:if test="${not empty popPolicyRuleEventCon.cep_rule_desc}">  
					$("#showCEPEventRule").show();
				</c:if>
				<c:if test="${not empty popPolicyRuleEventCon.simple_condtions_desc}">  
					$("#showSimplseEventRule").show();
				</c:if>
				
			</c:if>
			
			//策略ID
			$("#display_pcc_id").ztreeComb({
				"treeData":[],
				"useCheckbox":false,
				"listheight":240,
				"hiddenName":"display_pcc_id_name" ,
				getsearchData:function(txt,treeId){
					listPccIdFromCache(treeId , txt,"pccId");
				},
				useCombBtn:${flag != 'view'}
			});
			var pcc_statusIds_ztreeId = $("#display_pcc_id").attr("treeid");
			listPccIdFromCache(pcc_statusIds_ztreeId , '',"pccId");
			
			<c:if test="${flag != 'add'}">
			$("#display_pcc_id").val("${popPolicyRule.pccId}");
			$("input[name='display_pcc_id_name']").val("${popPolicyRule.pccId}");
			</c:if>
			
			//免打扰客户下拉列表
		 	$("#display-avoid_custgroup_ids").ztreeComb({
				"treeData":[],
				"useCheckbox":true,
				"listheight":240,
				"hiddenName":"avoid_custgroup_ids",
				"hiddenId":"avoid_custgroup_ids",
				getsearchData:function(txt,treeId){
					listSearch(treeId , txt,"pop_dim_aviod_type","id", "name");
				},
				useCombBtn:${flag != 'view'}
			});
			
		 	var avoidCustgroupIds_ztreeId = $("#display-avoid_custgroup_ids").attr("treeid");
			bindData(avoidCustgroupIds_ztreeId,"pop_dim_aviod_type","id","name",'');
		
			<c:if test="${flag != 'add'}">
				$("#display-avoid_custgroup_ids").val("${avoid_custgroup_names}");
				$("input[name='avoid_custgroup_ids']").val("${popPolicyRuleAct.avoid_custgroup_ids}");
			</c:if>
			
			$('.form_date').datetimepicker({language:'zh-CN',weekStart:1,todayBtn:1,autoclose: 1,
				todayHighlight: 1,startView: 2,minView: 2,forceParse: 0,
				initialDate: new Date()}).on('changeDate', function(ev){
				     if('ctrl_start_time' == this.id){
				    	 $('#ctrl_end_time').datetimepicker('setStartDate', ev.date.toString("yyyy-MM-dd"));
				     }else if('ctrl_end_time' == this.id){
				    	 $('#ctrl_start_time').datetimepicker('setEndDate', ev.date.toString("yyyy-MM-dd"));
				     }
				});;
			$('.form_date').datetimepicker('setStartDate', '${startDate}');
			$('.form_date').datetimepicker('setEndDate', '${endDate}');
			
			$("#setActionDate_cancle").click(function(){
				$("#actionDateId").modal("hide");
			});
			
			$("#setActionDate_ok").click(function(){
				if($('#date_ranges').val().length == 0){
					alert('执行日期不能为空');
					changeErrorStyle('absoluteDateStart',false);
					changeErrorStyle('absoluteDateEnd',false);
					return false;
				}
				if($('#time_ranges').val().length == 0){
					alert('接触时段不能为空');
					changeErrorStyle('timeRangeTmp',false);
					return false;
				}
				
				$("#showDataRanges").show();
				$("#showDataRanges_ul").empty();
				$("#showDataRanges_ul").append("<li>执行日期:</li>");
				$("#showDataRanges_ul").append($("#showAddDate").html());
				
				$("#showTimeRanges").show();
				$("#showTimeRanges_ul").empty();
				$("#showTimeRanges_ul").append("<li>接触时段:</li>");
				$("#showTimeRanges_ul").append($("#showTimeRange").html());
				
				$("#showAvoidRanges").show();
				$("#showAvoidRanges_ul").empty();
				$("#showAvoidRanges_ul").append("<li>屏蔽时段:</li>");
				$("#showAvoidRanges_ul").append($("#showAvoidRange").html());
				
				$("#actionDateId").modal("hide");
			});
		});
		
		function setActionDate(){
			$("#actionDateId").modal("show");
		}
		//规则设置页面
		function showRuleSetWindow() {
		 	var src = "${ctx}/policyConfigRule/ruleSet?rule_id=${rule_id }&simple_condtions_data=" + window.encodeURIComponent(window.encodeURIComponent($('#simple_condtions_data').val()));
		 	var dlg_html = '<div id="myModal_div"><iframe id="myModal_frame" scrolling="auto" align="top" width="100%"  height="760" frameborder="0" src="about:blank"></iframe></div>';
		 	 $(dlg_html).dialog({
		 		autoOpen: true,
		 		title: '规则设置',
		 		width: "900px",
		 		resizable: false,
		 		modal: true,
		 		position: ["center", "top"],
		 		open: function() {
		 			$(this).find('iframe').attr("src",src);
		 		},
		 		close: function() {
		 			//消除两次加载iframe页面问题
		 			$(this).find('iframe').attr("src","about:blank");
		 			$(this).dialog("destroy").remove();
		 		}
		 	});
		 }
		
		//add by jinl 20160411 选择基站信息
		function chooseLacciInfoWindow() {
		 	var src = "${ctx}/policyConfigRule/chooseLacciInfoWindow?rule_id=${rule_id }&simple_condtions_data=" + window.encodeURIComponent(window.encodeURIComponent($('#simple_condtions_data').val()));
		 	var dlg_html = '<div id="myModal_div"><iframe id="myModal_frame" scrolling="auto" align="top" width="100%"  height="760" frameborder="0" src="about:blank"></iframe></div>';
		 	 $(dlg_html).dialog({
		 		autoOpen: true,
		 		title: '基站信息选择页面',
		 		width: "900px",
		 		resizable: false,
		 		modal: true,
		 		position: ["center", "top"],
		 		open: function() {
		 			$(this).find('iframe').attr("src",src);
		 		},
		 		close: function() {
		 			//消除两次加载iframe页面问题
		 			$(this).find('iframe').attr("src","about:blank");
		 			$(this).dialog("destroy").remove();
		 		}
		 	});
		 }
		
		//add by jinl 20160411 导出基站信息文件
		function getLACCIInfoExport(){
			var lacciJson=$("#lacciAndStationTypeJsons").val();
			//策略名称重复检查
		  	$.ajax({
		  		url : "${ctx}/policyConfigRule/getLACCIInfoExportFile",
		  		type : "POST",
		  		async :false,
		  		data : {policy_rule_id:"${popPolicyRule.id }",policy_id:"${param.policy_id}",lacciJson:lacciJson },
		  		success : function(result) {
		  			if (result.success == 1) {
						alert("导出成功,请选择保存");
						window.open("./downloadExportFile?path="+result.path+"&filename="+result.filename+"&laccifilepath="+result.resultFilePath);
					}
				},error : function() {  
				      alert('导出信息失败'); 
				}  
		  	});
			
		}
		//add by jinl 20160414
		function displayCepLacciInfoRule(cepId,cepDesc,city,areaClass,area,lacciName,choosedLacciAndCount){
			$("#showCEPLacciInfo").show();
			$("#LacciInfo_ul").empty();
			$("#LacciInfo_ul").html("<li>已选择的基站信息:</li><li>"+choosedLacciAndCount+"<a class='close' href='javascript:void(0)' onclick='deleteCepEventRule($(this).parent())'>&nbsp;</a> </li><input id='btn_LACCIInfo_export' type='button' class='btn btn-primary btn-small' onclick='getLACCIInfoExport()' value='导出'/> ");
			$("input[name='cep_rule_desc']").val("");
			$("input[name='cep_rule_desc']").val(cepDesc);
			$("input[name='cep_rule_id']").val("");
			$("input[name='cep_rule_id']").val(cepId);
			$("input[name='cep_city']").val("");
			$("input[name='cep_city']").val(city);
			$("input[name='cep_areaClass']").val("");
			$("input[name='cep_areaClass']").val(areaClass);
			$("input[name='cep_area']").val("");
			$("input[name='cep_area']").val(area);
			$("input[name='cep_lacciName']").val("");
			$("input[name='cep_lacciName']").val(lacciName);
			$("input[name='choosedLacciAndCount']").val("");
			$("input[name='choosedLacciAndCount']").val(choosedLacciAndCount);
			$("#myModal_div").dialog("close");
			//根据选择的查询条件获取lacci信息
			queryLacciAndTacecgi(city,areaClass,area,lacciName);
		}
		//add by jinl 20160414 根据选择的查询条件获取lacci信息
		function queryLacciAndTacecgi(city,areaClass,area,lacciName){
			var portraitLink = "${cepQueryLacciInfoUrl}";
				$.ajax({
							url : portraitLink,
							type : "get",
							dataType: "jsonp",
							data : {"city" : encodeURIComponent(encodeURIComponent(city)),"areaClass" : encodeURIComponent(encodeURIComponent(areaClass)), "area" : encodeURIComponent(encodeURIComponent(area)),"lacciName" : encodeURIComponent(encodeURIComponent(lacciName))},
							jsonp:'callback',
							jsonpCallback: "receive",
							headers:{
								 "Access-Control-Allow-Origin":":*",
						         "Access-Control-Allow-Headers":"x-requested-with,content-type"

							},
							contentType:"application/x-www-form-urlencoded; charset=utf-8", //编码格式
							success : function(data) {
								alert("获取lacci信息成功");
								var json = eval(data);
								var lacciAndStationTypeJsonsStr ="{\"lacciJions\":[";
								  $.each(json, function (index, value) {  
						                 //循环获取数据    
						                 var lacci = json[index].lacci;  
						                 var station_type = json[index].station_type;  
						                 var temp ="{\"lacci\":"+lacci+",\"station_type\":"+station_type+"}";
						                 if(index!=0){
						                	 lacciAndStationTypeJsonsStr = lacciAndStationTypeJsonsStr+",";
						                 }
						                 lacciAndStationTypeJsonsStr = lacciAndStationTypeJsonsStr+temp;
						             }); 
								lacciAndStationTypeJsonsStr = lacciAndStationTypeJsonsStr+"]}";
								$("#lacciAndStationTypeJsons").val(lacciAndStationTypeJsonsStr);
							},  
							error : function() {  
							      alert('获取lacci信息失败'); 
							}  

						});
		}
		
		function receive(data) { 
			 
	    }  

		
		
		
		  /**
		   * 改变错误样式 by lixq8 add 20140421
		   */
		  function changeErrorStyle(id,flag){
		  	id = '#' + id;
		  	var oldCss = $(id).css('border-color');
		  	$(id).css('border-color','red');
		  	if(typeof flag == 'undefined' || flag == true){
		  		$(id).focus();
		  	}
		  	setTimeout(function(){$(id).css('border-color',oldCss)}, 4000);
		  }

		  /**保存前验证 by lixq8 add 20140421*/
		  function validationBeforeSave(){
			  var rule_id = $('#display_pcc_id').val();
				if(rule_id.length==0){
					alert('【策略编码】不能为空');
					changeErrorStyle('display_pcc_id');
					return false;
				}
			var rule_priority_val = $('#rule_priority').val();
			if(rule_priority_val >2147483645 || rule_priority_val < 0){
				alert('【规则优先级】取值范围：0～2147483645');
				changeErrorStyle('rule_priority');
				return false;
			}
		  	var policy_rule_name = $('#policy_rule_name').val();
		  	if(policy_rule_name.length == 0){
		  		alert('规则名称不能为空');
		  		changeErrorStyle('policy_rule_name');
		  		return false;
		  	}
		  	if(policy_rule_name.length >75){
		  		alert('规则名称长度不能大于75');
		  		changeErrorStyle('policy_rule_name');
		  		return false;
		  	}
		  	var isRepeat = false;
		  	//策略名称重复检查
		  	$.ajax({
		  		url : "${ctx}/policyConfigRule/checkRulePolicyNameRepeat",
		  		type : "POST",
		  		async :false,
		  		data : {policy_rule_name:policy_rule_name,policy_rule_id:"${popPolicyRule.id }",policy_id:"${param.policy_id}" },
		  		success : function(result) {
					if (result.success == 1 && result.flag == true) {
						alert("名称为:"+policy_rule_name+"的规则已在该策略场景中存在,请重新填写规则名称!");
						$('#policy_rule_name').val('').focus();
					}
				}
		  	});
		  	if(isRepeat){
		  		return false;
		  	}
		  	
		  	//客户群和事件规则必选其一检查
		  	if($('#display_custgroup_id').val().length == 0 && $('#cep_rule_id').val().length == 0 && $('#simple_condtions_data').val().length == 0){
		  		alert('客户群和事件规则至少填写一个');
		  		changeErrorStyle('display_custgroup_id',false);
		  		changeErrorStyle('btn_show_rule_set',false);
		  		return false;
		  	}
		  	
			if($('#date_ranges').val().length == 0){
				alert('执行日期不能为空');
				if($('#actionDateId').is(':visible')){
					changeErrorStyle('absoluteDateStart');
					changeErrorStyle('absoluteDateEnd');
				}else{
					changeErrorStyle('btn_set_action_date',false);
				}
				return false;
			}
			if($('#time_ranges').val().length == 0){
				alert('接触时段不能为空');
				if($('#actionDateId').is(':visible')){
					changeErrorStyle('timeRangeTmp');
				}else{
					if($('#btn_set_action_date').css('border-color') != 'red'){
						changeErrorStyle('btn_set_action_date',false);
					}
				}
				return false;
			}
		  	//动作类型校验
		  	if($(':checked[name="policy_act_type_id"]').val() == '1'){//管控类
		  		var control_param_val = $('#control_param').val();
		  		if(control_param_val.length == 0){
		  			alert('动作参数不能为空');
			  		changeErrorStyle('control_param');
			  		return false;
		  		}
		  		control_param_val = parseInt(control_param_val);
		  		if(isNaN(control_param_val)){
		  			alert('动作参数必须为数字');
			  		changeErrorStyle('control_param');
			  		return false;
		  		}
		  		if(control_param_val<0 || control_param_val>32767){
		  			alert('动作参数必须在0～32767范围内');
			  		changeErrorStyle('control_param');
			  		return false;
		  		}
		  	}else{//营销类
		  		if($('#exec_camp_content').val().length == 0 && $('#invalid_camp_content').val().length == 0){
		  			alert('执行通知或失效通知内容至少填写一个');
		  			changeErrorStyle('exec_camp_content');
					changeErrorStyle('invalid_camp_content');
					return false;
		  		}
		  	}
		  	return true;
		  }
		  
		function saveRule() {
			if(validationBeforeSave()){//表单校验
			   <% if("edit".equals(request.getAttribute("flag"))){ %>
			     //edit
				 var actionUrl = "${ctx}/policyConfigRule/edit";
					$.ajax({
						url : actionUrl,
						type : "POST",
						data : $("#ruleForm").serialize(),
						success : function(result) {
							if (result.success == "1") {
								alert("修改规则:"+result.ruleName+"成功，您可以点击“新建规则”或“修改”按钮，继续新建或修改策略规则。");
								window.parent.callback_save_addrule();						
							} else {
								alert("修改规则"+result.ruleName+"失败：" + result.msg);
							}
						}
					});
			  <%
		    	} else if ("add".equals(request.getAttribute("flag") ) ) {
			 %>
				    //add
				    var actionUrl = "${ctx}/policyConfigRule/createRule";
					$.ajax({
						url : actionUrl,
						type : "POST",
						data : $("#ruleForm").serialize(),
						success : function(result) {//alert(result);
							if (result.success == "1") {
								alert("保存规则:"+result.ruleName+"成功，您可以点击“新建规则”按钮，继续新建策略规则。");
								window.parent.callback_save_addrule();
							} else {
								alert("保存规则"+result.ruleName+"失败：" + result.msg);
							}
						}
					});
			  <%
		    	}
			 %>
			}
		}
		
		function callback_close(){
			$("#myModal_div").dialog("close");
		}
		
		function callback_ok(simpleEventRuleName,simpleEventRuleId, simple_rule_ext_term,simple_rule_ext_area){
			$("#showSimplseEventRule").show();
			try {$('#a_view_term,#a_view_area').popover('destory');} catch (e) {}
			$("#simpleEvent_ul").empty();
			var tmpHtml = [];
			tmpHtml.push("<li>简单规则:</li><li>"+simpleEventRuleName+"<a class='close' href='javascript:void(0)' onclick='deleteSimpleEventRule($(this).parent())'>&nbsp;</a> </li>");
			tmpHtml.push('<li id="simple_rule_ext_term" style="display: block;clear: left;">')
			tmpHtml.push('<span>'+simple_rule_ext_term+'</span><a id="a_view_term" href="javascript:void(0)" style="color: #2FADE7;" onclick="view_term_detail()">终端详细</a>')
			tmpHtml.push('</li>')
			tmpHtml.push('<li id="simple_rule_ext_area" style="display: block;clear: left;">')
			tmpHtml.push('<span>'+simple_rule_ext_area+'</span><a id="a_view_area" href="javascript:void(0)" style="color: #2FADE7;" onclick="view_area_detail()">位置详细</a>')
			tmpHtml.push('</li>')
			$("#simpleEvent_ul").html(tmpHtml.join(''));
			
			$("#showCEPEventRule").empty();
			$("input[name='simple_condtions_desc']").val(simpleEventRuleName);
			$("input[name='simple_condtions_data']").val(simpleEventRuleId);
			$("input[name='cep_rule_desc']").val('');
			$("input[name='cep_rule_id']").val('');
			
			$("#myModal_div").dialog("close");
		}
		//查看终端详细
		function view_term_detail(){
			var thisobj = $('#a_view_term');
			if(!thisobj.is('[data-original-title]')){
				$.ajax({
					type: 'POST',
					url: "${ctx}/policyConfigRule/searchTerm",
					data: {"rule_id": $("#rule_id").val()},
					dataType: " json",
					async: true,
					success: function(data) {
						var tmpHtml = "<table class='table table-striped table-bordered table-mcdstyle'><thead><tr><th>TAC</th><th>终端型号</th></tr></thead><tbody>";
						$( "#terms_tmpl").tmpl(data).each(function(){
							tmpHtml += this.outerHTML;
						});
						tmpHtml += "</tbody></table>";
						thisobj.popover({html:true,placement:'right',title:'终端详细',content:tmpHtml,template : '<div class="popover"><div class="arrow"></div><div class="popover-content"></div></div>'}).popover('show');
					}
				});
			}
		}
		//查询区域详细
		function view_area_detail(){
			var thisobj = $('#a_view_area');
			if(!thisobj.is('[data-original-title]')){
				var simple_condtions_data = JSON.parse($('#simple_condtions_data').val());
				var area_type_id = simple_condtions_data.area_type_id;
				if(typeof area_type_id == 'undefined' || area_type_id == null || area_type_id == ''){
					alert('没有选择区域!');
					return;
				}
				$.ajax({
					type: 'POST',
					url: "${ctx}/policyConfigRule/searchArea",
					data: {"area_type_id":area_type_id},
					dataType: " json",
					async: true,
					success: function(data) {
						var tmpHtml = "<table class='table table-striped table-bordered table-mcdstyle'><thead><tr><th>LACII</th><th>位置</th></tr></thead><tbody>";
						$( "#areas_tmpl").tmpl(data).each(function(){
							tmpHtml += this.outerHTML;
						});
						tmpHtml += "</tbody></table>";
						thisobj.popover({html:true,placement:'right',title:'位置详细',content:tmpHtml,template : '<div class="popover"><div class="arrow"></div><div class="popover-content"></div></div>'}).popover('show');
					}
				});
			}
		}
		
		var addedDateArray = new Array();
		
		//添加时间
		function addDate(strDate,_type){
			
			var startDate=$('#startDate').val();
			var endDate=$('#endDate').val();
			var dateArray = new Array();
			var arrayLength = addedDateArray.length;
			for (var i = 0 ; i< arrayLength ; i++){
				if(addedDateArray[i] == strDate){
					return;
				}
			}
			addedDateArray.push(strDate);
			addedDateArray.sort();
			//$(":hidden[name='date_ranges']").val(addedDateArray.join(','));
			$("#showAddDate").html("");
			var tmpDateStart = addedDateArray[0];
			var tmpDateEnd = addedDateArray[0];
			for(var i=1; i<addedDateArray.length; i++) {
				if(tmpDateStart == '') {
					tmpDateStart = addedDateArray[i];
					tmpDateEnd = addedDateArray[i];
					continue;
				}
				var t1 = DateNewInstance(tmpDateEnd);
				var t2 = DateNewInstance(addedDateArray[i]);
				t1.setTime(t1.getTime()+24*60*60*1000); //加一天
				if(t1.getTime() == t2.getTime()) {
					tmpDateEnd = addedDateArray[i];
				} else {
					var str = tmpDateStart;
					if(tmpDateStart != tmpDateEnd){
						str += " 至 "+tmpDateEnd;
					}
					var tplData = {tplDate:str};					
					$('#templateAddDate').tmpl(tplData).appendTo('#showAddDate');
					dateArray.push(str.replace(" 至 ","~"));
					tmpDateStart = addedDateArray[i];
					tmpDateEnd = addedDateArray[i];
				}
				
			}
			var str = tmpDateStart;
			if(tmpDateStart != tmpDateEnd){
				str += " 至 "+tmpDateEnd;
			}
			var tplData = {tplDate:str};		
			$('#templateAddDate').tmpl(tplData).appendTo('#showAddDate');
			dateArray.push(str.replace(" 至 ","~"));
			$(":hidden[name='date_ranges']").val(dateArray.join(","));
			
		}
		
		//按时间段添加时间
		function addDateBatch(){
			
			var startDateStr = $("#absoluteDateStart").val();
			var endDateStr = $("#absoluteDateEnd").val();
			
			if(startDateStr == null || endDateStr == null	||startDateStr == '' || endDateStr == '') {
				return false;
			}
			var startDate = DateNewInstance(startDateStr);
			var endDate = DateNewInstance(endDateStr);
			while(startDate.getTime() <= endDate.getTime()){
				var dateStr = DateToString(startDate);
				addDate(dateStr);
				startDate.setTime(startDate.getTime()+24*60*60*1000);
			}
			$("#absoluteDateStart").val("");
			$("#absoluteDateEnd").val("");
			return true;
		}
		
		//new Date(yyyy-MM-dd)
		function DateNewInstance(str){
			var arr = str.split('-');
			var year = arr[0];
			var month = Number(arr[1])-1;
			var day = Number(arr[2]);
			var dat = new Date(year,month,day,0,0,0,0);
			return dat;
		}
		

		//格式化日期：yyyy-MM-dd
		function DateToString(date)
		{
		    var year = date.getFullYear();
		    var month = date.getMonth();
		    var day = date.getDate();
		    var datestr;
			month = month + 1;
		    if (month <= 9){
		        month = '0' + month;
		    }
		    if (day < 10){
		        day = '0' + day;
		    }
		    datestr = year + '-' + month + '-' + day ;
		    return datestr;
		}
		
		var addedAvoidArray = new Array();
		
		//删除接触时段
		function deleteTimeRange(jqObj){
			var text=$.trim(jqObj.text());
			for(var i=0; i<addedAvoidArray.length; i++){
				var n = addedAvoidArray[i];
				if(text == n){
					addedAvoidArray.splice(i,1);
					i--;
				}
			}
			jqObj.remove();
			$(":input[name='time_ranges']").val(addedAvoidArray.join(','));
		}
		//删除免打扰时段
		function deleteAvoidRange(jqObj){
			var text=$.trim(jqObj.text());
			for(var i=0; i<addedAvoidArray.length; i++){
				var n = addedAvoidArray[i];
				if(text == n){
					addedAvoidArray.splice(i,1);
					i--;
				}
			}
			jqObj.remove();
			$(":input[name='avoid_ranges']").val(addedAvoidArray.join(','));
		}
		
		//删除简单事件规则
		function deleteSimpleEventRule(jqObj){
			jqObj.remove();
			
			$("input[name='simple_condtions_desc']").val("");
			$("input[name='simple_condtions_data']").val("");
			$('#model_ids,#model_names').val('');
			$('#simple_rule_ext_term,#simple_rule_ext_area').remove();
		}
		
		//删除复杂事件规则
		function deleteCepEventRule(jqObj){
			jqObj.remove();
			$("input[name='cep_rule_desc']").val("");
			$("input[name='cep_rule_id']").val("");
		}
		
		//删除时间
		function deleteDate(jqObj){
			var text=$.trim(jqObj.text());
			var startDate = '';
			var endDate = '';
			if(text.indexOf('至') < 0){
				startDate = text;
				endDate = text;
			} else {
				var arr = text.split('至');
				startDate = $.trim(arr[0]);
				endDate = $.trim(arr[1]);
			}
			var start =getTempDate(startDate);
			var end = getTempDate(endDate);
			for(var i=0; i<addedDateArray.length; i++){
				var n = addedDateArray[i];
				var thisDate = getTempDate(n);
				if(thisDate >= start && thisDate <= end){
					addedDateArray.splice(i,1);
					i--;
				}
			}
			jqObj.remove();
			$(":hidden[name='date_ranges']").val(addedDateArray.join(','));
		}
		
		//日期字符串转化为日期格式
		function getTempDate(dependedVal){
		    //根据日期字符串转换成日期
		    var regEx = new RegExp("\\-","gi");
		    dependedVal=dependedVal.replace(regEx,"/");
		    var milliseconds=Date.parse(dependedVal);
		    var dependedDate=new Date();
		    dependedDate.setTime(milliseconds);
		    return dependedDate;

		}
		
		function displayCepEventRule(cepId,cepDesc){
			try {$('#a_view_term,#a_view_area').popover('destory');} catch (e) {}
			$("#showSimplseEventRule").empty();
			$("input[name='simple_condtions_desc']").val("");
			$("input[name='simple_condtions_data']").val("");
			$('#model_ids,#model_names').val('');
			
			$("#showCEPEventRule").show();
			$("#cepEvent_ul").empty();
			$("#cepEvent_ul").html("<li>复杂事件规则:</li><li>"+cepDesc+"<a class='close' href='javascript:void(0)' onclick='deleteCepEventRule($(this).parent())'>&nbsp;</a> </li>");
			$("input[name='cep_rule_desc']").val("");
			$("input[name='cep_rule_desc']").val(cepDesc);
			$("input[name='cep_rule_id']").val("");
			$("input[name='cep_rule_id']").val(cepId);
			$("#myModal_div").dialog("close");
		}
		
		/** 检查是客户群和事件规则 不能同时为空*/
		function checkCustGroupAndEvent() {
			var custgroup_id = $("#custgroup_id").val();
			var cep_rule_id = $("#cep_rule_id").val();
			var simple_condtions_data = $("#simple_condtions_data").val();
			if(custgroup_id =="" && cep_rule_id=="" && simple_condtions_data ==""){
				alert("客户群、事件规则 不能同时为空");
				return false;
			}
		}
		
		function funChangeTab(id){
			//先移除其他元素样式
			$("#tab-widget-title li").each(function(){
				$(this).attr("class","");
			});
			//针对某个特定元素加样式
			var v_id="#"+id;
			$(v_id).attr("class","on");
		}
		//查看表单只读设置
		function viewInit(){
			if(${flag == 'view'}){
				$("#display_pcc_id").attr("disabled","disabled");
				$("#policy_rule_name").attr("disabled","disabled");
				$("#display_custgroup_id").attr("disabled","disabled");
				$("#display-avoid_custgroup_ids").attr("disabled","disabled");
				$("#rule_priority").attr("disabled","disabled");
				$("#control_param").attr("disabled","disabled");
				$("#exec_camp_content").attr("disabled","disabled");
				$("#invalid_camp_content").attr("disabled","disabled");
			}
		}


		//查询基站详细信息
		function getLACCIInfo(){
			var src = "${ctx}/policyConfigRule/showLACCIInfo?eplId=${popPolicyRuleEventCon.cep_rule_id }&pageSize=10&page=1";
		 	var dlg_html = '<div id="myModal_div"><iframe id="myModal_frame" scrolling="auto" align="top" width="100%"  height="600" frameborder="0" src="about:blank"></iframe></div>';
		 	 $(dlg_html).dialog({
		 		autoOpen: true,
		 		title: '显示基站信息',
		 		width: "900px",
		 		resizable: false,
		 		modal: true,
		 		position: ["center", "top"],
		 		open: function() {
		 			$(this).find('iframe').attr("src",src);
		 		},
		 		close: function() {
		 			//消除两次加载iframe页面问题
		 			$(this).find('iframe').attr("src","about:blank");
		 			$(this).dialog("destroy").remove();
		 		}
		 	});
		
		}
		
	</script>
</body>
</html>
