<%@page import="com.asiainfo.biframe.utils.date.DateUtil"%>
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@page import="java.util.Date"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<%request.setAttribute("today",DateUtil.date2String(new Date(), DateUtil.YYYY_MM_DD));%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="/WEB-INF/layouts/head.jsp"%>
<meta charset="utf-8" />
<title>策略场景</title>
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
</style>

</head>

<body>
	<div id="mainDiv" class="modal-body" style="width: auto; height: auto">
		<div class="controls input-append date form_date" data-date=""
			data-date-format="yyyy-mm-dd" data-link-field="startDate"
			data-link-format="yyyy-mm-dd" id="ctrl_start_time">
			<input size="16" type="text" value="" readonly>
			<span class="add-on"><i class="icon-th"></i></span>
		</div>
		<input class="commonWidth" id="startDate" name="start_time" value="${empty model.start_time?today:model.start_time.toString().substring(0,10)}" type="hidden" btvd-type="required" btvd-class='btvdclass' btvd-err="有效期为必填项"/>
		<div class="controls input-append date form_date" data-date=""
			data-date-format="yyyy-mm-dd" data-link-field="endDate"
			data-link-format="yyyy-mm-dd" id="ctrl_end_time">
			<input size="16" type="text" value="" readonly>
			<span class="add-on"><i class="icon-th"></i></span>
		</div>
		<input class="commonWidth" id="endDate" name="end_time" value="${empty model.end_time?today:model.end_time.toString().substring(0,10)}" type="hidden" btvd-type="required" btvd-class='btvdclass' btvd-err="有效期为必填项"/>
	
	            <table id="treeTable1" style="width:100%" class="table table-striped table-bordered table-mcdstyle table-epl">
                <thead>
                <tr>
                    <td style="width:200px;">标题</td>
                    <td>内容</td>
                </tr>
                </thead>
                <tbody>
                <tr id="1">
                    <td><span controller="true">1</span></td>
                    <td>内容</td></tr>
                <tr id="2" pId="1">
                    <td><span controller="true">2</span></td>
                    <td>内容</td></tr>
                <tr id="3" pId="2">
                    <td>3</td>
                    <td>内容</td>
                </tr>
                <tr id="4" pId="2">
                    <td>4</td>
                    <td>内容</td>
                </tr>
                <tr id="5" pId="4">
                    <td>4.1</td>
                    <td>内容</td>
                </tr>
                <tr id="6" pId="1" hasChild="true">
                    <td>5</td>
                    <td>注意这个节点是动态加载的</td>
                </tr>
                <tr id="7">
                    <td>8</td>
                    <td>内容</td>
                </tr>
                </tbody>
            </table>
			<form class="form-horizontal" role="form">
                    <fieldset>
                        <legend>配置数据源</legend>
                       <div class="form-group">
                          <label class="col-sm-2 control-label" for="ds_host">主机名</label>
                          <div class="col-sm-4" style="display: inline-block;">
                             <input class="form-control" id="ds_host" type="text" placeholder="192.168.1.161"/>
                          </div>
                          <label class="col-sm-2 control-label" for="ds_name">数据库名</label>
                          <div class="col-sm-4" style="display: inline-block;">
                             <input class="form-control" id="ds_name" type="text" placeholder="msh"/>
                          </div>
                       </div>
                       <div class="form-group">
                          <label class="col-sm-2 control-label" for="ds_username">用户名</label>
                          <div class="col-sm-4" style="display: inline-block;">
                             <input class="form-control" id="ds_username" type="text" placeholder="root"/>
                          </div>
                          <label class="col-sm-2 control-label" for="ds_password">密码</label>
                          <div class="col-sm-4" style="display: inline-block;">
                             <input class="form-control" id="ds_password" type="password" placeholder="123456"/>
                          </div>
                       </div>
                    </fieldset>     
                    <fieldset>
                         <legend>选择相关表</legend>
                        <div class="form-group">
                           <label for="disabledSelect"  class="col-sm-2 control-label">表名</label>
                           <div class="col-sm-10">
                              <select id="disabledSelect" class="form-control">
                                 <option>禁止选择</option>
                                 <option>禁止选择</option>
                              </select>
                           </div>
                        </div>
                    </fieldset>
                    
                       <fieldset>
                         <legend>字段名</legend>
                        <div class="form-group">
                           <label for="disabledSelect"  class="col-sm-2 control-label">表名</label>
                           <div class="col-sm-10">
                              <select id="disabledSelect" class="form-control">
                                 <option>禁止选择</option>
                                 <option>禁止选择</option>
                              </select>
                           </div>
                        </div>
                    </fieldset>
                </form>
	</div>
<%@ include file="/WEB-INF/layouts/bottom.jsp"%>
</body>
<script type="text/javascript">
$('.form_date').datetimepicker({language:'zh-CN',weekStart:1,todayBtn:1,autoclose: 1,
	todayHighlight: 1,startView: 2,minView: 2,forceParse: 0,initialDate: new Date(),startDate:new Date()
}).on('changeDate', function(ev){
     if('ctrl_start_time' == this.id){
    	 $('#ctrl_end_time').datetimepicker('setStartDate', ev.date.toString("yyyy-MM-dd"));
     }else{
    	 $('#ctrl_start_time').datetimepicker('setEndDate', ev.date.toString("yyyy-MM-dd"));
     }
});
var option = {
        theme:'vsStyle',
        expandLevel:2
    };
$('#treeTable1').treeTable({theme:'vsStyle',expandLevel:2});
</script>
</html> 