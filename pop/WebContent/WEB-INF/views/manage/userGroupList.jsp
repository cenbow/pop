<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %> 
<!doctype html>
<html>
<head>

<title>用户组列表</title>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<%@ include file="/aibi_approval/layouts/head.jsp"%>
</head>
<body>
	<!-- 浏览列表 start -->
	<table id="dg" class="easyui-datagrid" style="width:100%;height:auto;" data-options="fitColumns:true,title:'用户组列表',url:'${ctx}/userConfig/listGroup',singleSelect:false,pagination:true,toolbar:'#tb',resizeHandle:'both',striped:true,rownumbers:true,onDblClickRow:function(index,row){editInfo(row);}">
	    <thead>
	        <tr>
	        	<th data-options="field:'xx',checkbox:true"></th>
	        	<th data-options="field:'group_id'" hidden=true>编号</th>
	        	<th data-options="field:'parent_id'" hidden=true>父编号</th>
	        	<th data-options="field:'group_name'" width="100">组名</th>
	        	<th data-options="field:'create_time'" width="100">创建时间</th>
	        </tr>
	    </thead>
	</table>
	<div id="tb" class="toolbar_height">
	    <a href="#" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="newInfo()">新建</a>
	    <a href="#" class="easyui-linkbutton" iconCls="icon-edit" plain="true" onclick="editInfo()">修改</a>
	    <a href="#" class="easyui-linkbutton" iconCls="icon-remove" plain="true" onclick="delInfo()">删除</a>
	    <div style="float:right;margin-right:20px;">
		    <span>用户组名称:</span>
		     <input id="groupName" name="groupName" class="easyui-textbox" style="width:200px;">
		    <a href="#" class="easyui-linkbutton" onclick="doSearch()" plain="true" iconCls="icon-search">查询</a>
	    </div>
	</div>
	<!-- 浏览列表 end -->
	<!-- 新建或修改对话框 start -->
	<div id="win" class="easyui-window" data-options="minimizable:false,closed:true,modal:true,top:0" title="新增用户组" style="width:500px;height:500px;"> 
    	 <form id="fm" method="post" novalidate style="margin:0;padding:40px 40px;">
    	    <input id="gId" name="gId" type="hidden" value=""/>
	        <div class="fitem">
	            <label>用户组名称:</label>
	            <input name="gName" id="gName" class="easyui-textbox" data-options="required:true" style="width:250px;">
	        </div>
	        <div class="fitem">
	            <label>上级用户组:</label>
	            <!-- <input id="pGroup" class="ztreeCombox dept" type="text" readonly value="" style="width:220px;" data-options="'url':'${ctx}/userConfig/groupTree',selectParentNode:true"/>
	             							<input class="ztreeCombox user <c:if test="${seq.id.approveObjType == 3}">show</c:if>" type="text" readonly value="" style="width:128px;" data-options="'url':'${ctx}/aibi-approval/approvalRelation/getUserTree',selectParentNode:false <c:if test="${seq.id.approveObjType == 3}">,initValue:{text:'${seq.approveObjName }',value:'${seq.id.approveObjId }'}</c:if>"/>
	             
	              -->
	            <input id="userGroup" class="ztreeCombox" type="text" readonly value="" style="width:220px;" data-options="'url':'${ctx}/userConfig/groupTree',selectParentNode:true,hiddenId:'userGroup_id'"/>
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
  $(document).ready(function(){

  });
//查询
function doSearch(){
    $('#dg').datagrid('load',{
    	groupName:$('#groupName').val()==''?'':$('#groupName').val()
    });
}

//打开新建对话框
var url;
function newInfo(){
	$("#gId").val("");
	$("#gName").textbox("setValue","");
	$('#userGroup').ztreeComb("setValue","");
	$('#userGroup').ztreeComb("setText","");
	refreshGTree();
	$('#win').window('setTitle','新建').window('open');
}
function closeWin(){
	$('#win').window('close');
}

function refreshGTree()
{
	var zTree = $('#userGroup').ztreeComb("getZTree");
	if(zTree!=null)
		{
		zTree.reAsyncChildNodes(null, "refresh");
	 }
}

function saveInfo()
{
     $('#fm').form('submit',{
	        url: '${ctx}/userConfig/saveOrUpdateGroup',
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
    	 	$("#gId").val(row.group_id);
    		$("#gName").textbox("setValue",row.group_name);
    		var pId = row.parent_id;
    		var zTree = $('#userGroup').ztreeComb("getZTree");
    		var pNode = zTree.getNodeByParam("id",pId);
    		if(pNode){
    			$('#userGroup').ztreeComb("setValue",pId);
        		$('#userGroup').ztreeComb("setText",pNode.text);
    		}
    		else
   			{
   			$('#userGroup').ztreeComb("setValue","0");
       		$('#userGroup').ztreeComb("setText","");
   			}
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
    			var gId = [];
    			$(rows).each(function(i,row){
    				gId.push(row.group_id);
    			});
    			$.post('${ctx}/userConfig/deleteGroup', {gId:gId.join(',')}, function(result){
			            	$.messager.alert('提示',result.msg);
			            	doSearch();
				},'json');
    		}
    	});
    }
}
</script>
</html>