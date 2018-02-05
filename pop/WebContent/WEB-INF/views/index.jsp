<%@ page contentType="text/html; charset=UTF-8" %>
<html>
<head>
<title>策略运营平台</title>
</head>
<body>
<script>

var newWin = window.open("<%=request.getContextPath()%>/login",null,"height=620,width=930,top=0,left=0,status=no,toolbar=no,menubar=no,location=no,channelmode=0,directories=0,resizable=yes,titlebar=no",false);
window.opener=null;//必须添加;限制一台PC机只有一个浏览器窗口为'经营分析系统'
window.close();


//window.location = "login/login.jsp";
</script>

</body>