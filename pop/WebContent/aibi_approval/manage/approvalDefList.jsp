<%@ page contentType="text/html;charset=UTF-8"%>
<!doctype html>
<html>
<head>
<%@ include file="../layouts/head.jsp"%>
<title>流程定义列表</title>
</head>
<body>
	<!-- 浏览列表 start -->
	<table id="dg" class="easyui-datagrid" style="width:100%;height:auto;" data-options="fitColumns:true,title:'流程定义列表',url:'${ctx}/aibi-approval/approvalDef/list',singleSelect:false,pagination:true,toolbar:'#tb',resizeHandle:'both',striped:true,rownumbers:true,onDblClickRow:function(index,row){editInfo(row);}">
	    <thead>
	        <tr>
	        	<th data-options="field:'xx',checkbox:true"></th>
	        	<th data-options="field:'approveFlowId'" width="100">流程编号</th>
	        	<th data-options="field:'approveFlowName'" width="100">流程名称</th>
	        	<th data-options="field:'approveFlowAccessToken',formatter:function(val,row){return val==0?'私有':'公有';}" width="100">访问标识</th>
	        	<th data-options="field:'approveFlowLevelCnt'" width="100">审包含审批级别</th>
	        	<th data-options="field:'createUsername'" width="100">创建者</th>
	        	<th data-options="field:'createTime'" width="100">创建时间</th>
	        </tr>
	    </thead>
	</table>
	<div id="tb" class="toolbar_height">
	    <a href="#" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="newInfo()">新建</a>
	    <a href="#" class="easyui-linkbutton" iconCls="icon-edit" plain="true" onclick="editInfo()">修改</a>
	    <a href="#" class="easyui-linkbutton" iconCls="icon-remove" plain="true" onclick="delInfo()">删除</a>
	    <div style="float:right;margin-right:20px;">
		    <span>流程名称:</span>
		     <input id="approveFlowName" name="approveFlowName" class="easyui-textbox" style="width:200px;">
		    <a href="#" class="easyui-linkbutton" onclick="doSearch()" plain="true" iconCls="icon-search">查询</a>
	    </div>
	</div>
	<!-- 浏览列表 end -->
	<!-- 新建或修改对话框 start -->
	<div id="win" class="easyui-window" data-options="minimizable:false,closed:true,modal:true,top:0" title="流程定义" style="width:1024px;height:500px;"> 
    	<iframe scrolling="no" id='openIframe' frameborder="0"  src="" style="width:100%; height:460px"></iframe>
    </div>
	<!-- 新建对话框 end -->
</body>
<%@ include file="../layouts/bottom.jsp"%>
<script type="text/javascript">
$(function(){
	 //根据文本框中的值找到结点并显示出来
    $(".textbox-text").bind("input propertychange", function() {
		var id = $(this).next('.textbox-value').prop('name');
		if($('#'+id).is('.easyui-combotree')){
			$('#'+id).combotree('tree').tree("search",$(this).val());
			if($(this).val().length == 0){
			     $('#'+id).combotree('tree').tree("collapseAll");
		         $('#'+id).combotree('setValue', '');
			}
		}
    });
});
//查询
function doSearch(){
    $('#dg').datagrid('load',{
    	approveFlowName:$('#approveFlowName').textbox('getValue')==''?'':$('#approveFlowName').textbox('getValue')
    });
}
//选择人时禁止选择组织
function beforeSelect(node){
    if (node.disabled) {
    	 //var t = $('#approveUserid').combotree('tree');
    	 //$('#approveUserid').combotree('tree').tree('unselect');
    	 //$('#approveUserid').combo('showPanel');
        return false;
    }else{
    	return true;
    }
}

//打开新建对话框
var url;
function newInfo(){
	$('#openIframe')[0].src='${ctx}/aibi-approval/approvalDef/toAddOrUpdate'; 
	$('#win').window({onResize:function(){
		$('#openIframe').css('height',($('#win').height()-16)+'px');
	}});
	$('#win').window('setTitle','新建').window('open');
}
function closeWin(){
	$('#win').window('close');
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
    	 $('#openIframe')[0].src='${ctx}/aibi-approval/approvalDef/toAddOrUpdate?approveFlowId='+row.approveFlowId; 
    	 $('#win').window({onResize:function(){
    			$('#openIframe').css('height',($('#win').height()-16)+'px');
    		}});
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
    			var approveFlowIds = [];
    			$(rows).each(function(i,row){
    				approveFlowIds.push(row.approveFlowId);
    			});
    			$.post('${ctx}/aibi-approval/approvalDef/delete', {approveFlowIdsStr:approveFlowIds.join(',')}, function(result){
					 if (result.errorMsg){
			                $.messager.show({ title: '删除失败',msg: result.errorMsg,timeout:3000});
			            } else {
			                $('#dlg').dialog('close');
			                $('#dg').datagrid('reload');
			            }
				},'json');
    		}
    	});
    }
}
</script>
</html>