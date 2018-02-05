<%@ page  language="java" import="java.util.*" contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ page import="com.asiainfo.biframe.privilege.IMenuItem"%>
<%@ page import="com.asiainfo.biframe.utils.spring.SystemServiceLocator"%>
<%@ page import="org.apache.commons.collections.CollectionUtils"%>
<%@ page import="com.ai.bdx.frame.privilegeServiceExt.service.IUserPrivilegeCommonService"%>
<%@page import="com.asiainfo.biframe.privilege.base.constants.UserManager"%>
<%@page import="com.ai.bdx.pop.listener.SessionListener"%>
<%@page import="com.asiainfo.biframe.utils.string.StringUtil"%>
<%@page import="com.ai.bdx.pop.util.PopConfigure"%>
<%@page import="com.asiainfo.biframe.utils.string.DES"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<title>POP策略运营平台</title>
	<%@ include file="/WEB-INF/layouts/head.jsp"%>   
</head>
<%
int f = 0;
String uId = request.getParameter("userId");
if (uId != null && uId.length()>0 && !"null".equals(uId)) {
	uId = DES.decrypt(uId);
}
SessionListener mysession1 = null;
SessionListener mysession = (SessionListener) pageContext.getAttribute(UserManager.ASIA_SESSION_NAME,pageContext.SESSION_SCOPE );

if(mysession.getUser()==null){
	pageContext.setAttribute(UserManager.ASIA_SESSION_NAME,
			SessionListener.login(request, uId),
			PageContext.SESSION_SCOPE);
	mysession = (SessionListener) pageContext
			.getAttribute(UserManager.ASIA_SESSION_NAME,
					PageContext.SESSION_SCOPE);
}
if(mysession.getUser()==null){
	out.println("<script>alert('用户不存在');</script>");
}
String m_strLoginUserID = mysession.getUserID();
IMenuItem menuitem = null; 
String topMenuId = PopConfigure.getInstance().getProperty("TOP_MENU_ID");
IUserPrivilegeCommonService service = (IUserPrivilegeCommonService) SystemServiceLocator.getInstance().getService("userPrivilegeCommonService");
List<IMenuItem> menuList = service.getDirectlySubMenuItems(m_strLoginUserID, topMenuId);

%>
<body class="bodybg">
<div class=" home_sidebar">
	<a href="#" class="brand">&nbsp;</a>
    <div class="toogler_bg">
    	<div id="sidebar-toggler" class="sidebar-toggler hidden-phone"></div>
    </div>
<dl id="home_sidebar" class="home_sidebar">
	<dt class="active" onclick="show('/home/toHome')">
    	<span class="arrow"></span>
        <span class="icon-box"> <i class="icon-dashboard"></i></span>
       	 首页
    </dt>
	<%
		for(int i=0;i<menuList.size();i++){
			menuitem = menuList.get(i);
	%>
    <dt>
    	<span class="arrow"></span>
        <span class="icon-box"> <i class="icon-book"></i></span>
       	<%=menuitem.getMenuItemTitle() %>
    </dt>
    <dd>
    	<ul>
    	<% 
    	List<IMenuItem> subMenuList = service.getDirectlySubMenuItems(m_strLoginUserID, menuitem.getMenuItemId().toString());
    	IMenuItem subMenuitem = null; 
    	for(int j=0;j<subMenuList.size();j++){
    		subMenuitem = subMenuList.get(j);
    		String onclick = "show('"+subMenuitem.getUrl()+"')";
    		if(StringUtil.isNotEmpty(subMenuitem.getUrl())&&subMenuitem.getUrl().startsWith("http")){
    			 String userId = (String)session.getAttribute("user");
    			 String url = subMenuitem.getUrl();
    	   		if(url.contains("?")){
    	   			url += "&ailk_autoLogin_userId="+"popadmin";//固定用户
    	   			//url += "&ailk_autoLogin_userId="+userId;
    	   		}else{
    	   			//url += "?ailk_autoLogin_userId="+userId;
    	   			url += "?ailk_autoLogin_userId="+"popadmin";//固定用户
    	   		} 
    			onclick = "showUrl('"+url+"')";
    		}
    	%>
        	<li  onclick="<%=onclick%>"  url='<%=subMenuitem.getUrl() %>'><a href="javascript:void(0);"><%=subMenuitem.getMenuItemTitle() %></a></li>
        	<%} %>
        </ul>
    </dd>
    <%
		} %>
</dl><!--sidebar end -->
</div>
<div id="main-content">
<iframe id="showFrame" width="100%"  frameborder="0" style="background:#ffffff;" src="${ctx}/home/toHome"></iframe>
</div>

<%@ include file="/WEB-INF/layouts/bottom.jsp"%>
<script type="text/javascript">
jQuery(document).ready(function() {       
	
	$("#sidebar-toggler").click(function(){
		var parents=$(this).parents(".home_sidebar");
		if($(this).hasClass("closed")){
			parents.find("dl").show();
			parents.width(215).removeClass("nobg");
			$(this).removeClass("closed");
			$("#main-content").css("margin-left",215);
		}else{
			parents.width(23).addClass("nobg");
			$(this).addClass("closed");
			parents.find("dl").hide();
			$("#main-content").css("margin-left",23);
		}
	})
	
	$("#home_sidebar dt,#home_sidebar dd ul li").mouseenter(function(){
		$(this).addClass("onhover");
	}).mouseleave(function(){
		$(this).removeClass("onhover");
	});
	
	$("#home_sidebar dt").click(function(){
		if($(this).hasClass("active")){
			$(this).removeClass("active");
			$(this).siblings(" .active").removeClass("active");
			$(this).find(" .arrow").removeClass("open");
		}else{
			$(this).addClass("active").siblings(".active").removeClass("active").find(" .arrow").removeClass("open");
			$(this).next("dd").addClass("active");
			$(this).find(" .arrow").addClass("open");
		}
	});
	
	$("#home_sidebar dd ul li").click(function(){
		if($(this).hasClass("active")) {
			return;
		}
		$(this).addClass("active").siblings(".active").removeClass("active");
	});
	
	var height =document.documentElement.clientHeight ;
	$("#showFrame").attr("height",height);
});

function show(url){
	$("#showFrame").attr("src","${ctx}"+url);
}
function showUrl(url){
	if(url!=null&&url.indexOf("popWin")>-1){
		var newWin = window.open(url);
	}else{
		$("#showFrame").attr("src", url);
	}
	
}
function top_alert(msg,f){
	try{
		if (typeof(f) == "undefined")
		{
			$.jGrowl(msg,{life:4000});
		}else{
			$.jGrowl(msg,f);
		}
	}catch(err){
		_ciWindowAlert(msg);
	}
}
</script>
</body>
</html>
