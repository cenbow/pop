<%@ page language="java" contentType="text/html; charset=utf-8"   pageEncoding="utf-8"%>
<%@page import="com.ai.bdx.pop.util.RequestUtil"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
String eplId = RequestUtil.getString(request,"eplId");
String eplShow =java.net.URLDecoder.decode(RequestUtil.getString(request,"eplShow",""), "utf-8");
String oper = request.getParameter("oper")==null ? "":request.getParameter("oper");
String replacePropList =  java.net.URLDecoder.decode(RequestUtil.getString(request,"replacePropList",""), "utf-8");
replacePropList = replacePropList.replaceAll("\"", "\\\\\"");
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title></title>
<script type="text/javascript">

	<% if(oper.equals("close")) { %>
		window.parent.parent.callback_close();
	<%} else {%>
		window.parent.parent.displayCepEventRule("<%=eplId%>","<%=eplShow%>");
	<%}%>
</script>
</head>
<body>
</body>
</html>