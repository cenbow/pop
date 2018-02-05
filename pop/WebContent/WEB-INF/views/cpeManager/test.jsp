<%-- <%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ page import="java.util.*,com.ai.bdx.pop.bean.*"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib uri="http://bi.asiainfo.com/taglibs/dic" prefix="dic"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" /> --%>
<%@ page language="java" contentType="text/html; charset=utf-8"  pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ page import="java.util.*"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!doctype html>
<html>
<head>
<title>CPE场景-按单个号码查询</title>
<meta charset="utf-8" />
<meta http-equiv="Content-Type" content="text/html;charset=utf-8" />
<meta http-equiv="Cache-Control" content="no-store" />
<meta http-equiv="Pragma" content="no-cache" />
<meta http-equiv="Expires" content="0" />
<meta content="width=device-width, initial-scale=1.0" name="viewport" />
<link href=" ${pageContext.request.contextPath }/static/assets/jqueryUI/themes/base/jquery-ui.css" rel="stylesheet" type="text/css" />
<link href=" ${pageContext.request.contextPath }/static/assets/bootstrap/css/bootstrap.min.css" rel="stylesheet" type="text/css" />
<link href=" ${pageContext.request.contextPath }/static/assets/bootstrap/css/bootstrap-responsive.min.css" rel="stylesheet" type="text/css" />


<script type="text/javascript" src=" ${pageContext.request.contextPath }/static/js/jquery-1.8.3.min.js"></script>
<script type="text/javascript" src=" ${pageContext.request.contextPath }/static/assets/jqueryUI/jquery-ui.js"></script> 
<script type="text/javascript" src=" ${pageContext.request.contextPath }/static/assets/bootstrap/js/bootstrap.min.js"></script> 
<script type="text/javascript" src=" ${pageContext.request.contextPath }/static/assets/bootstrap/js/bootstrap-fileupload.js"></script>
 
<!-- ie8 fixes -->
<!--[if lt IE 9]>
<script src="../js/excanvas.js"></script>
<script src="../js/respond.js"></script>
<![endif]--> 
<style>
.line{display:-webkit-box}
.marigins{    
			margin-left: 15px;
			margin-right: 15px;
		}
thead td{
	    box-shadow: -2px 4px 0px #9E88BD;
		line-height: 15px;
}
.table th, .table td {
    padding: 8px;
    line-height: 16px;
    text-align: left;
    vertical-align: top;
    border-top: 1px solid #ddd;
}
.firstd{
    background-color: #C7BFCE;
    height: 30px;
    width: 85%;
    margin-top: 4px;
    box-shadow: -1px -1px 0px #f5f5f5;
    margin-left: 1px;
}
.firstdv{
	padding: 0px;background-color:#9E88BD;border-top: #9E88BD;
}
.reset-border1{
border: 4px solid;
    border-top-color: #D7BFEC;
    border-top-left-radius: 10px;
	border-top-right-radius: 10px;
    padding-left: 6px;
    padding-right: 6px;
    border-right-color: #7863EA;;
    border-left-color: #7863EA;;
    border-bottom-color: #7863EA;;
}
.txtlength{
	width: 55%;
}

</style>
<script type="text/javascript" src=" ${pageContext.request.contextPath }/static/assets/uniform/jquery.uniform.min.js"></script>
<script type="text/javascript" src=" ${pageContext.request.contextPath }/static/assets/bootstrap-datepicker/js/bootstrap-datepicker_zh_CN.js" charset="UTF-8"></script>   
<script type="text/javascript" src=" ${pageContext.request.contextPath }/static/assets/bootstrap-daterangepicker/date.js"></script>
<script type="text/javascript" src=" ${pageContext.request.contextPath }/static/assets/bootstrap-daterangepicker/daterangepicker.js"></script> 
<script type="text/javascript" src=" ${pageContext.request.contextPath }/static/assets/bootstrap-datetimepicker-master/js/bootstrap-datetimepicker.js" charset="UTF-8"></script>
<script type="text/javascript" src=" ${pageContext.request.contextPath }/static/assets/bootstrap-datetimepicker-master/js/locales/bootstrap-datetimepicker.zh-CN.js" charset="UTF-8"></script>
<script type="text/javascript" src=" ${pageContext.request.contextPath }/static/assets/lazyscrollloading/jquery.lazyscrollloading-min.js"></script>
<script type="text/javascript" src=" ${pageContext.request.contextPath }/static/assets/ztree/jquery.ztree.all-3.5.js"></script>
<script type="text/javascript" src=" ${pageContext.request.contextPath }/static/assets/ztree/jquery.ztree.exhide-3.5.js"></script>
<script type="text/javascript" src=" ${pageContext.request.contextPath }/static/js/scripts.js"></script>
<script type="text/javascript" src=" ${pageContext.request.contextPath }/static/js/main.js" ></script>
<script type="text/javascript" src=" ${pageContext.request.contextPath }/static/js/json2.js" ></script>
<script src=" ${pageContext.request.contextPath }/static/assets/uploadify/jquery.uploadify.js" type="text/javascript"></script>
<script type="text/javascript" src=" ${pageContext.request.contextPath }/static/assets/treeTable/jquery.treeTable.js" ></script>
<script type="text/javascript" src=" ${pageContext.request.contextPath }/static/assets/jgrowl/jquery.jgrowl.min.js" ></script>
<script>

</script>
</head>
<body style="width: 100%;">
Hello:${request.contextPath}
</body>
</html>