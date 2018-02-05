<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %> 
<!doctype html>
<html>
<head>
<style type="text/css">   
.contentBox {position: relative;border: 1px solid black;min-height: 200px;line-height: 25px;}
</style>

<title>用户列表</title>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<%@ include file="/aibi_approval/layouts/head.jsp"%>
</head>
<body>
	<!-- 浏览列表 start -->
	<table id="dg" class="easyui-datagrid" style="width:100%;height:auto;" data-options="fitColumns:true,title:'用户列表',url:'${ctx}/userConfig/listUser',singleSelect:false,pagination:true,toolbar:'#tb',resizeHandle:'both',striped:true,rownumbers:true,onDblClickRow:function(index,row){editInfo(row);}">
	    <thead>
	        <tr>
	        	<th data-options="field:'xx',checkbox:true"></th>
	        	<th data-options="field:'userid'" width="100">账号</th>
	        	<th data-options="field:'username'" width="100">姓名</th>
	        	<th data-options="field:'mobilephone'" width="100">联系电话</th>
	        	<th data-options="field:'email'" width="100">邮箱</th>
	        	<th data-options="field:'createtime'" width="100">创建时间</th>
	        </tr>
	    </thead>
	</table>
	<div id="tb" class="toolbar_height">
	    <a href="#" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="newInfo()">新建</a>
	    <a href="#" class="easyui-linkbutton" iconCls="icon-edit" plain="true" onclick="editInfo()">修改</a>
	    <a href="#" class="easyui-linkbutton" iconCls="icon-remove" plain="true" onclick="delInfo()">删除</a>
	    
	    <div style="float:right;margin-right:20px;">
	        <span>用户帐号:</span>
		     <input id="quserId" name="quserId" class="easyui-textbox" style="width:200px;">
		    <span>用户姓名:</span>
		     <input id="quserName" name="quserName" class="easyui-textbox" style="width:200px;">
		    <a href="#" class="easyui-linkbutton" onclick="doSearch()" plain="true" iconCls="icon-search">查询</a>
	    </div>
	</div>
	<!-- 浏览列表 end -->
	<!-- 新建或修改对话框 start -->
	<div id="win" class="easyui-window" data-options="minimizable:false,closed:true,modal:true,top:0" title="新增用户" style="width:500px;height:500px;"> 
    	 <form id="fm" method="post" novalidate style="margin:0;padding:40px 40px;">
    	    <input type="hidden" name="uoperate" id="uoperate" />
    	    <div class="fitem">
	            <label>用户账号:</label>
	            <input name="userid" id="userid" class="easyui-textbox" data-options="required:true" style="width:250px;">
	        </div>
	        <div class="fitem">
	            <label>用户名称:</label>
	            <input name="username" id="username" class="easyui-textbox" data-options="required:true" style="width:250px;">
	        </div>
	        <div class="fitem">
	            <label>联系电话:</label>
	            <input name="mobile" id="mobile" class="easyui-textbox"  style="width:250px;">
	        </div>
	        <div class="fitem">
	            <label>邮箱:</label>
	            <input name="email" id="email" class="easyui-textbox" data-options="validType:'email'" style="width:250px;">
	        </div>
	        <div class="fitem">
	            <label>用户组:</label>
	            <input id="userGroup" class="ztreeCombox" type="text" readonly value="" style="width:220px;" data-options="'url':'${ctx}/userConfig/groupTree',required:true,selectParentNode:true,hiddenId:'userGroup_id'"/>
	        </div>
	        <div class="fitem">
	           <label>角色:</label>
	           <input id="userRoles" class="textbox" readonly value="" onclick="showRoleTree()" style="width:250px;height:20px"/>
	           <input id="userRoleIds" name="userRoleIds" type="hidden" />
	           <div id="menuContent" class="menuContent" style="display:none; position: absolute;background:#fff; border:1px solid #ccc;z-index:999;">
	                 <ul id="roles_tree" class="ztree" style="height:220px"></ul>
               </div>
	        </div>
	        <div id="dlg-buttons">
		       <a href="javascript:void(0)" class="easyui-linkbutton c6" iconCls="icon-ok" onclick="saveInfo()" style="width:90px">保存</a>
		       <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="closeWin()" style="width:90px">取消</a>
	       </div>
    </div>
    
	<!-- 新建对话框 end -->
</body>
<%@ include file="/aibi_approval/layouts/bottom.jsp"%>
<script type="text/javascript">
  var role_Tree;
  $(document).ready(function(){
	   // zTree 的参数配置，深入使用请参考 API 文档（setting 配置详解）
	   var setting = {
			        check:{
			        	enable: true
			        },
					data: {
						key: {
							checked: "checked",
							name: 'text',
							title: 'qtip'
						},
						simpleData: {
							enable: true,
							idKey: 'id',
							pIdKey: 'pid'
						}
					},
					 async: {
							enable: true,
							url: "${ctx}/userConfig/getAllRoleTree",
							autoParam: ["id"],
						},
					callback: {
						onCheck: onRoleCheck
					}

				};
	   // zTree 的数据属性，深入使用请参考 API 文档（zTreeNode 节点数据详解）
	   $(document).ready(function(){
		   role_Tree = $.fn.zTree.init($("#roles_tree"), setting);
	   });
  });

//显示角色树 
function showRoleTree()
{
	var userRoles = $("#userRoles");
	var userRolesOffset = $("#userRoles").position();
	$("#menuContent").css({left:userRolesOffset.left + "px", top:userRolesOffset.top + userRoles.outerHeight() + "px"}).slideDown("fast");
	$("body").bind("mousedown", onBodyDown);
}

function hideMenu() {
	$("#menuContent").fadeOut("fast");
	$("body").unbind("mousedown", onBodyDown);
}

function onBodyDown(event) {
	if (!(event.target.id == "userRoles" || event.target.id == "menuContent" || $(event.target).parents("#menuContent").length>0)) {
		hideMenu();
	}
}

function onRoleCheck(e, treeId, treeNode) {
	var nodes = role_Tree.getCheckedNodes(true),
	v = "";
	for (var i=0, l=nodes.length; i<l; i++) {
		v += nodes[i].text + ",";
	}
	if (v.length > 0 ) v = v.substring(0, v.length-1);
	$("#userRoles").val(v);
}


//查询
function doSearch(){
    $('#dg').datagrid('load',{
    	quserId:$('#quserId').val()==''?'':$('#quserId').val(),
    	quserName:$('#quserName').val()==''?'':$('#quserName').val()	
    });
}

//打开新建对话框
var url;
function newInfo(){
	$("#userid").textbox("clear");
	$("#userid").textbox("readonly",false);
	$("#username").textbox("clear");
	$("#mobile").textbox("clear");
	$("#email").textbox("clear");
	$("#userRoleIds").val("");
	$("#userRoles").val("");
	$('#userGroup').ztreeComb("setValue","");
	$('#userGroup').ztreeComb("setText","");
	if(role_Tree)
		{
		    role_Tree.setting.async.url="${ctx}/userConfig/getAllRoleTree";
			refreshGTree();
		}
	$('#uoperate').val("add");
	$('#win').window('setTitle','新建').window('open');
}
function closeWin(){
	$('#win').window('close');
}

function refreshGTree()
{
	if(role_Tree!=null)
		{
		role_Tree.reAsyncChildNodes(null, "refresh");
	 }
}

function saveInfo()
{
	if(role_Tree!=null)
		{
		   var nodes = role_Tree.getCheckedNodes(true);
		   var iArr = [];
		   for(var i=0;i<nodes.length;i++)
			   {
			     iArr.push(nodes[i].id);
			   }
		   $("#userRoleIds").val(iArr.join(","));
		   $('#fm').form('submit',{
		        url: '${ctx}/userConfig/saveOrUpdateUser',
		        onSubmit: function(param){
		           if($("#userRoleIds").val()=="")
		        	   {
		        	      alert("用户角色不能为空!");
		        	      return false;
		        	   }
		           var pVal = $('#userGroup').ztreeComb("getValue");
		           if(pVal=="")
		        	   {
		        	      alert("用户组不能为空!");
		        	      return false;
		        	   }
	               return $(this).form('enableValidation').form('validate');
		        },
		        success: function(result){
		        	$.messager.alert('提示',result);
		            doSearch();
		            closeWin();
		        }
		    });
		}
}
//打开修改对话框
function editInfo(row){
	var rows = [];
	if(typeof row == 'undefined'){
		rows = $('#dg').datagrid('getSelections');
	}else{
		rows[0] = row;
	}
    
    if (rows.length!=1){
    	$.messager.alert('提示','请先选中一行,再点击修改按钮!','warning');
    }else {
    	 row = rows[0];
    	 $("#userid").textbox("setValue",row.userid);
    	 $("#username").textbox("setValue",row.username);
         $("#mobile").textbox("setValue",row.mobilephone);
    	 $("#email").textbox("setValue",row.email);
    	 $("#userid").textbox("readonly",true);
         //获取用户组和角色数据
         $.post('${ctx}/userConfig/getUserDetail', {uId:row.userid}, function(result){
        	 var ur = result.userRoles;
        	 var ug = result.userGroup;
        	 var un="",uid="";
        	 if(ur!=null)
        		 {
        		   for(var i=0;i<ur.length;i++)
        			   {
        			     un=un+ur[i].role_name+",";
        			     uid = uid+ur[i].rold_id+",";
        			   }
        		   un = un.substr(un,un.length-1);
        		   uid = uid.substr(uid,uid.length-1);
        		   $("#userRoleIds").val(uid);
              	   $("#userRoles").val(un);
        		 }
        	 if(ug!=null)
        		 {
        		   $('#userGroup').ztreeComb("setValue",ug.group_id);
            	   $('#userGroup').ztreeComb("setText",ug.group_name);  
        		 }
        	 role_Tree.setting.async.url="${ctx}/userConfig/getAllRoleTree?userId="+row.userid;
        	 refreshGTree();
        	 $('#uoperate').val("edit");
        	 $('#win').window('setTitle','修改').window('open');
		 },'json');
    }
}

//删除
function delInfo(){
	var rows = $('#dg').datagrid('getSelections');
    if (rows.length == 0){
    	$.messager.alert('提示','请至少选中一行,再点击删除按钮!','warning');
    }else {
    	$.messager.confirm('确认', '你是否确认要删除所选中行?', function(r){
    		if (r){
    			var rId = [];
    			$(rows).each(function(i,row){
    				rId.push(row.userid);
    			});
    			$.post('${ctx}/userConfig/deleteUser', {userid:rId.join(',')}, function(result){
			            	$.messager.alert('提示',result.msg);
			            	doSearch();
				},'json');
    		}
    	});
    }
}
</script>
</html>