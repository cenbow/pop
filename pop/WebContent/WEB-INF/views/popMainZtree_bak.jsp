<%@ page language="java" import="java.util.*"
	contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ page import="com.asiainfo.biframe.privilege.IMenuItem"%>
<%@ page import="com.asiainfo.biframe.utils.spring.SystemServiceLocator"%>
<%@ page import="org.apache.commons.collections.CollectionUtils"%>
<%@ page import="com.ai.bdx.pop.service.IMpmUserPolicyService"%>
<%@page
	import="com.asiainfo.biframe.privilege.base.constants.UserManager"%>
<%@page import="com.ai.bdx.pop.listener.SessionListener"%>
<%@page import="com.asiainfo.biframe.utils.string.StringUtil"%>
<%@page import="com.ai.bdx.pop.util.PopConfigure"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>POP策略运营平台</title>
<%@ include file="/WEB-INF/layouts/head.jsp"%>
<meta http-equiv="X-UA-Compatible" content="IE=edge,Chrome=1" />
<style type="text/css">
.wrap-menu {
	width: 400px;
/* 	width: 200px; */
	margin: 0 auto;
	overflow: auto;
	font: 14px/1.5 Tahoma, Arial, sans-serif;
}
</style>
</head>
<%

SessionListener mysession = (SessionListener) pageContext.getAttribute(UserManager.ASIA_SESSION_NAME,pageContext.SESSION_SCOPE );
String m_strLoginUserID = mysession.getUserID();
IMenuItem menuitem = null; 
String topMenuId = PopConfigure.getInstance().getProperty("TOP_MENU_ID");
IMpmUserPolicyService service = (IMpmUserPolicyService) SystemServiceLocator.getInstance().getService("mpmUserPolicyService");
List<IMenuItem> menuList = service.getDirectlySubMenuItems(m_strLoginUserID, topMenuId);

%>
<body style="overflow: auto;">
	<div class="home_sidebar">
		<a href="#" class="brand">&nbsp;</a>
		<div>
			<div id="sidebar-toggler" class="sidebar-toggler hidden-phone"></div>
		</div>
		<div class="wrap-menu"></div>
		<div class="zTreeDemoBackground left">
			<ul id="treeDemo" class="ztree"></ul>
		</div>

	</div>
	<div id="main-content">
		<iframe id="showFrame" width="100%" height="100%" frameborder="0"
			style="background: #ffffff;" src="${ctx}/home/toHome"></iframe>
	</div>
	<%@ include file="/WEB-INF/layouts/bottom.jsp"%>

	<script type="text/javascript">
jQuery(document).ready(function() {       
	
	var setting = {    
            check:{enable:false},
            data:{simpleData:{enable:true}}
            //callback:{onCheck:onCheck}
        };
	
	var zNodes =[
	{id:0, pId:0,click:"show('/home/toHome')",name:"首页", open:false},
	  <%
	    if(menuList!=null&&menuList.size()>0){
	    	for(int m=0;m<menuList.size();m++){
	    		menuitem = menuList.get(m);
	    		String p1="";
	    		if(menuitem.getPic1()!=null){
	    			p1=menuitem.getPic1();
	    		}
	    		String p2="";
	    		if(menuitem.getPic2()!=null){
	    			p2=menuitem.getPic2();
	    		}
	    		if(p1!=""&&p2!=""){
	    			%>
	    			{ id:<%=menuitem.getMenuItemId()%>, pId:<%=menuitem.getParentId()%>, name:"<%=menuitem.getMenuItemTitle()%>", open:false,iconOpen:"${ctx}/static/assets/ztree/zTreeStyle/img/<%=p1%>", iconClose:"${ctx}/static/assets/ztree/zTreeStyle/img/<%=p2%>"},
	    			<%
	    		}else{
	    			%>
	    			{ id:<%=menuitem.getMenuItemId()%>, pId:<%=menuitem.getParentId()%>, name:"<%=menuitem.getMenuItemTitle()%>", open:false},
	    			<%
	    		}
	    	 //取二级菜单
	    	 List<IMenuItem> subMenuList = service.getDirectlySubMenuItems(m_strLoginUserID, menuitem.getMenuItemId().toString());
    	     IMenuItem subMenuitem = null; 
    	     for(int j=0;j<subMenuList.size();j++){
    	    	 subMenuitem=subMenuList.get(j);
    	    	 String p21="";
 	    		if(subMenuitem.getPic1()!=null){
 	    			p21=subMenuitem.getPic1();
 	    		}
 	    		String p22="";
 	    		if(subMenuitem.getPic2()!=null){
 	    			p22=subMenuitem.getPic2();
 	    		}
 	    		if(subMenuitem.getMenuItemId()==9190201){
 	    			
 	    		}
    	    	
    	    	String onclick = "show('"+subMenuitem.getUrl()+"')";
    	    		if(StringUtil.isNotEmpty(subMenuitem.getUrl())&&subMenuitem.getUrl().startsWith("http")){
    	    			 String userId = (String)session.getAttribute("user");
    	    			 String url = subMenuitem.getUrl();
    	    	   		if(url.contains("?")){
    	    	   			url += "&ailk_autoLogin_userId="+userId;
    	    	   		}else{
    	    	   			url += "?ailk_autoLogin_userId="+userId;
    	    	   		} 
    	    			onclick = "showUrl('"+url+"')";
    	    		}
    	    	if(p21!=null&&p22!=null){%>
    	    	{id:<%=subMenuitem.getMenuItemId()%>, pId:<%=subMenuitem.getParentId()%>,click:"<%=onclick%>",name:"<%=subMenuitem.getMenuItemTitle()%>", open:false,iconOpen:"${ctx}/static/assets/ztree/zTreeStyle/img/diy/6.png", iconClose:"${ctx}/static/assets/ztree/zTreeStyle/img/diy/6.png"},
			 <%}else{%>
    	    	 {id:<%=subMenuitem.getMenuItemId()%>, pId:<%=subMenuitem.getParentId()%>,click:"<%=onclick%>",name:"<%=subMenuitem.getMenuItemTitle()%>", open:false},
    	   	 <%
    	      }
    	  	 //取三级菜单
    	     }
	       }
	    }
	  %>           
	];
	
	$.fn.zTree.init($("#treeDemo"), setting, zNodes);
	/*  function onCheck(e,treeId,treeNode){
         var treeObj=$.fn.zTree.getZTreeObj("treeDemo"),
         nodes=treeObj.getCheckedNodes(true),
         v="";
         for(var i=0;i<nodes.length;i++){
             v+=nodes[i].name + ",";
             alert(nodes[i].id); //获取选中节点的值
         }
      }
	 */

	var height =document.documentElement.clientHeight+700 ;
	$("#showFrame").attr("height",height);
});

function show(url){
	$("#showFrame").attr("src","${ctx}"+url);
}
function showUrl(url){
	$("#showFrame").attr("src", url);
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
