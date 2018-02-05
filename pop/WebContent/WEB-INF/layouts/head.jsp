<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<c:set var="ctx" value="${pageContext.request.contextPath}" />
<meta charset="utf-8" />
<meta http-equiv="Content-Type" content="text/html;charset=utf-8" />
<meta http-equiv="Cache-Control" content="no-store" />
<meta http-equiv="Pragma" content="no-cache" />
<meta http-equiv="Expires" content="0" />
<meta content="width=device-width, initial-scale=1.0" name="viewport" />
<link href="${ctx}/static/assets/jqueryUI/themes/base/jquery-ui.css" rel="stylesheet" type="text/css" />
<link href="${ctx}/static/assets/bootstrap/css/bootstrap.min.css" rel="stylesheet" type="text/css" />
<link href="${ctx}/static/assets/bootstrap/css/bootstrap-responsive.min.css" rel="stylesheet" type="text/css" />
<link href="${ctx}/static/assets/bootstrap/css/bootstrap-fileupload.css" rel="stylesheet" type="text/css" />
<link href="${ctx}/static/assets/uniform/css/uniform.default.css" rel="stylesheet" type="text/css" />
<link href="${ctx}/static/assets/font-awesome/css/font-awesome.css" rel="stylesheet" type="text/css" />
<link href="${ctx}/static/assets/bootstrap-datepicker/css/datepicker.css" rel="stylesheet" type="text/css" />
<link href="${ctx}/static/assets/bootstrap-daterangepicker/daterangepicker.css" rel="stylesheet" type="text/css" />
<link href="${ctx}/static/assets/bootstrap-datetimepicker-master/css/bootstrap-datetimepicker.min.css" rel="stylesheet" media="screen"></link>

<link href="${ctx}/static/assets/ztree/zTreeStyle/zTreeStyle.css" rel="stylesheet" type="text/css" />

<link href="${ctx}/static/css/style.css" rel="stylesheet" type="text/css" />
<link href="${ctx}/static/css/style_responsive.css" rel="stylesheet" type="text/css" />
<link href="${ctx}/static/css/style_mcd.css" rel="stylesheet" type="text/css" id="style_color" />

<link rel="icon" href="${ctx}/static/img/favicon/pop_favicon.ico" type="image/x-icon" />
<link rel="shortcut icon" href="${ctx}/static/img/favicon/pop_favicon.ico" type="image/x-icon" />

<link rel="stylesheet" type="text/css" href="${ctx}/static/assets/uploadify/uploadify.css">
<link rel="stylesheet" type="text/css" href="${ctx}/static/assets/jgrowl/jquery.jgrowl.css">
<link rel="stylesheet" type="text/css" href="${ctx}/static/assets/treeTable/vsStyle/jquery.treeTable.css">
<style type="text/css">
	.table-noborder th,.table-noborder td{border-top:none;}
	.queryTable th,.queryTable td{padding:4px;}
	.table_btns button{margin-right:4px; margin-left:10px;}
	.table_btns,table td.query_btns{text-align:right;}
	.dataTables_filter{margin-right:3px;}
	
	.ztreePanel{float:left; width:200px; border:1px solid #ccc;}
	.ztreeHidIcon{float:left; width:8px;}
	.ztreeCTPanel{margin-left:208px; border:1px solid #ccc;}
	/* jquery jGrowl */
	.jGrowl .jGrowl-notification{filter: alpha(opacity=80); -moz-opacity:0.8; opacity: 0.8;}
	.jGrowl .ui-state-highlight{background:none; border:none;}
	.jGrowl .ui-state-highlight .jGrowl-message{color:#fff;}
	.jGrowl .ui-state-highlight .jGrowl-close{color:#fff;}
</style>

