<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %> 
<!doctype html>
<html>
<head>
<style type="text/css">   
.contentBox {position: relative;border: 1px solid black;min-height: 200px;line-height: 25px;}
</style>

<title>角色列表</title>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<%@ include file="/aibi_approval/layouts/head.jsp"%>
</head>
<body>
	<!-- 浏览列表 start -->
	<table id="dg" class="easyui-datagrid" style="width:100%;height:auto;" data-options="fitColumns:true,title:'角色列表',url:'${ctx}/userConfig/listUserRole',singleSelect:false,pagination:true,toolbar:'#tb',resizeHandle:'both',striped:true,rownumbers:true,onDblClickRow:function(index,row){editInfo(row);}">
	    <thead>
	        <tr>
	        	<th data-options="field:'xx',checkbox:true"></th>
	        	<th data-options="field:'role_id'" hidden=true>编号</th>
	        	<th data-options="field:'role_name'" width="100">角色名称</th>
	        	<th data-options="field:'create_time'" width="100">创建时间</th>
	        </tr>
	    </thead>
	</table>
	<div id="tb" class="toolbar_height">
	    <a href="#" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="newInfo()">新建</a>
	    <a href="#" class="easyui-linkbutton" iconCls="icon-edit" plain="true" onclick="editInfo()">修改</a>
	    <a href="#" class="easyui-linkbutton" iconCls="icon-remove" plain="true" onclick="delInfo()">删除</a>
	    <div style="float:right;margin-right:20px;">
		    <span>角色名称:</span>
		     <input id="roleName" name="roleName" class="easyui-textbox" style="width:200px;">
		    <a href="#" class="easyui-linkbutton" onclick="doSearch()" plain="true" iconCls="icon-search">查询</a>
	    </div>
	</div>
	<!-- 浏览列表 end -->
	<!-- 新建或修改对话框 start -->
	<div id="win" class="easyui-window" data-options="minimizable:false,closed:true,modal:true,top:0" title="新增角色" style="width:500px;height:500px;"> 
    	 <form id="fm" method="post" novalidate style="margin:0;padding:40px 40px;">
    	    <input id="roleId" name="roleId" type="hidden" value=""/>
    	    <input id="priIds" name="priIds" type="hidden" value=""/>
	        <div class="fitem">
	            <label>角色名称:</label>
	            <input name="rName" id="rName" class="easyui-textbox" data-options="required:true" style="width:250px;">
	        </div>
	        <div>
	           <label>角色权限:</label>
	           <ul id="role_pri" class="ztree"></ul>
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
  var role_priTree;
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
							url: "${ctx}/userConfig/getPriTree",
							autoParam: ["id"],
						}
				};
	   // zTree 的数据属性，深入使用请参考 API 文档（zTreeNode 节点数据详解）
	   $(document).ready(function(){
		   role_priTree = $.fn.zTree.init($("#role_pri"), setting);
	   });
  });
//查询
function doSearch(){
    $('#dg').datagrid('load',{
    	roleName:$('#roleName').val()==''?'':$('#roleName').val()
    });
}

//打开新建对话框
var url;
function newInfo(){
	$("#roleId").val("");
	$("#rName").textbox("setValue","");
	if(role_priTree)
		{
		    role_priTree.setting.async.url="${ctx}/userConfig/getPriTree";
			refreshGTree();
		}
	
	$('#win').window('setTitle','新建').window('open');
}
function closeWin(){
	$('#win').window('close');
}

function refreshGTree()
{
	if(role_priTree!=null)
		{
		role_priTree.reAsyncChildNodes(null, "refresh");
	 }
}

function saveInfo()
{
	if(role_priTree!=null)
		{
		   var nodes = role_priTree.getCheckedNodes(true);
		   var iArr = [];
		   for(var i=0;i<nodes.length;i++)
			   {
			     iArr.push(nodes[i].id);
			   }
		   $("#priIds").val(iArr.join(","));
		   $('#fm').form('submit',{
		        url: '${ctx}/userConfig/saveOrUpdateRole',
		        onSubmit: function(param){
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
    	 $("#roleId").val(row.role_id);
    	 $("#rName").textbox("setValue",row.role_name);
    	 role_priTree.setting.async.url="${ctx}/userConfig/getPriTree?roleId="+row.role_id;
    	 refreshGTree();
    	 $('#win').window('setTitle','修改').window('open');
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
    				rId.push(row.role_id);
    			});
    			$.post('${ctx}/userConfig/deleteRole', {rId:rId.join(',')}, function(result){
			            	$.messager.alert('提示',result.msg);
			            	doSearch();
				},'json');
    		}
    	});
    }
}
</script>
</html>