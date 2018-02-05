<%@ page contentType="text/html;charset=UTF-8"%>
<!doctype html>
<html>
<head>
<%@ include file="../layouts/head.jsp"%>
<title>流程管理列表</title>
</head>
<body>
	<!-- 浏览列表 start -->
	<table id="dg" class="easyui-datagrid" style="height:auto;" data-options="fitColumns:true,title:'流程管理列表',url:'${ctx}/aibi-approval/deptFlowRelation/list',singleSelect:true,pagination:true,toolbar:'#tb',resizeHandle:'both',striped:true,rownumbers:true">
	    <thead>
	        <tr>
	        	<th data-options="field:'drvTypeClassify'" width="100">驱动类型分类</th>
	        	<th data-options="field:'drvType'" width="100">驱动类型</th>
	        	<th data-options="field:'approveFlowName'" width="100">流程</th>
	        	<th data-options="field:'flowTypeName'" width="100">流程类型</th>
	        	<th data-options="field:'cityName'" width="100">地市</th>
	        	<th data-options="field:'deptName'" width="100">部门</th>
	        </tr>
	    </thead>
	</table>
	<div id="tb" class="toolbar_height">
	    <a href="#" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="newInfo()">新建</a>
	    <a href="#" class="easyui-linkbutton" iconCls="icon-remove" plain="true" onclick="delInfo()">删除</a>
	    <div style="float:right;margin-right:20px;">
		    <span>流程名称:</span>
		     <input id="approveFlowName" name="approveFlowName" class="easyui-textbox" style="width:200px;">
		    <a href="#" class="easyui-linkbutton" onclick="doSearch()" plain="true" iconCls="icon-search">查询</a>
	    </div>
	</div>
	<!-- 浏览列表 end -->
	<!-- 新建对话框 start -->
	<div id="dlg" class="easyui-dialog" style="width:600px;height:500px;padding:10px 20px" buttons="#dlg-buttons" data-options="iconCls:'icon-add',resizable:true,closed:true,modal:true,top:0">
	    <form id="fm" method="post" novalidate style="margin:0;padding:10px 40px;">
	        <div class="fitem">
	            <label>驱动类型分类:</label>
	            <input id="relationType" name="relationType" class="easyui-combobox" data-options="editable:false,valueField:'value',textField:'label',loadFilter: function(data){return filterDrvType(data);},onSelect: function(rec){drvTypeClassifySel(rec);},url:'${ctx}/aibi-approval/drvType/getDrvTypeClassifys',width:180" required="true">
	        </div>
	        <div class="fitem">
	            <label>驱动类型:</label>
	            <input id="campDrvId" name="campDrvId" class="easyui-combobox" data-options="editable:false,valueField:'code',textField:'val',loadFilter: function(data){return filterDrvType(data);},width:180" required="true">
	        </div>
	        <div class="fitem">
	            <label>流程:</label>
	            <input name="approveFlowId" class="easyui-combobox" data-options="editable:false,valueField:'value',textField:'label',url:'${ctx}/aibi-approval/deptFlowRelation/getAllFlows',width:180" required="true">
	        </div>
	        <div class="fitem">
	            <label>流程类型:</label>
	            <input name="flow_type" class="easyui-combobox" data-options="editable:false,valueField:'value',textField:'label',url:'${ctx}/aibi-approval/deptFlowRelation/getAllRelationTypes',width:180" required="true">
	        </div>
	        <div class="fitem">
	            <label>地市:</label>
	            <input id="cityId" name="cityId" class="easyui-combobox" data-options="editable:false,valueField:'value',textField:'label',url:'${ctx}/aibi-approval/deptFlowRelation/getCityList',width:180">
	        </div>
	        <div class="fitem">
		            <label>分公司/部门:</label>
		            <input id="deptId" class="ztreeCombox" type="text" readonly value="" style="width:151px;" data-options="'url':'${ctx}/aibi-approval/approvalRelation/getGroupTree',selectParentNode:true,hiddenName:'deptId'"/>
		   	</div>
	    </form>
	</div>
	<div id="dlg-buttons">
		<a href="javascript:void(0)" class="easyui-linkbutton c6" iconCls="icon-ok" onclick="saveInfo()" style="width:90px">保存</a>
		<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#dlg').dialog('close')" style="width:90px">取消</a>
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
//分类类型下拉框选择事件
function drvTypeClassifySel(rec){
	var url = '${ctx}/aibi-approval/drvType/getDrvTypes?drvTypeClassifyId='+rec.value;
    $('#campDrvId').combobox('setValue', '').combobox('reload', url);
}

//过滤"全部"option
function filterDrvType(data){
	debugger
	if(typeof data == 'undefined' || data == null){
		return [];
	}
	var arr = [];
	$(data).each(function(){
		if((typeof this.value != 'undefined' && this.value != '-1') || (typeof this.code != 'undefined' && this.code != '-1')){
			arr.push(this);
		}
	});
	return arr;
}
//查询
function doSearch(){
    $('#dg').datagrid('load',{
    	approveFlowName:$('#approveFlowName').textbox('getValue')==''?'':$('#approveFlowName').textbox('getValue')
    });
}

//打开新建渠道信息对话框
var url;
function newInfo(){
    $('#dlg').dialog('open').dialog('setTitle','新建');
    $('#fm').form('clear');
    $('#deptId').ztreeComb('setValue','');
    $('#deptId').ztreeComb('setText','');
  //新建对话框中分类类型下拉框初始化
    url = '${ctx}/aibi-approval/deptFlowRelation/save';
}

//打开修改渠道信息对话框
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
    	 $('#dlg').dialog('open').dialog('setTitle','编辑');
         $('#fm').form('clear');
         $('#campDrvId').combobox('reload', '${ctx}/aibi-approval/drvType/getDrvTypes?drvTypeClassifyId='+row.relationType);
         $('#fm').form('load',row);
         $('#deptId').ztreeComb('setValue',row.deptId);
         $('#deptId').ztreeComb('setText',row.deptName);
         url = '${ctx}/aibi-approval/approvalDef/update?beforeApproveUserid='+row.approveUserid+'&beforeCreateUserid='+row.approveCreateUserid;
    }
}

//保存渠道信息
function saveInfo(){
    $('#fm').form('submit',{
        url: url,
        onSubmit: function(){
            return $(this).form('validate');
        },
        success: function(result){
            var result = $.parseJSON(result);
            if (result.errorMsg){
                $.messager.show({ title: '保存失败',msg: result.errorMsg,timeout:3000});
            } else {
                $('#dlg').dialog('close');
                $('#dg').datagrid('reload');
            }
        }
    });
}

//删除渠道信息
function delInfo(){
	var rows = $('#dg').datagrid('getSelections');
    if (rows.length == 0){
    	$.messager.alert('提示','请至少选中一行,再点击删除按钮!','warning');
    }else {
    	$.messager.confirm('确认', '你是否确认要删除所选中行?', function(r){
    		if (r){
    			//String campDrvIds, String relationTypes, String flow_types, String approveFlowIds, String cityIds, String deptIds,
    			var campDrvIds = [];
    			var relationTypes = [];
    			var approveFlowIds = [];
    			var flow_types = [];
    			var cityIds = [];
    			var deptIds = [];
    			$(rows).each(function(i,row){
    				campDrvIds.push(row.campDrvId);
    				relationTypes.push(row.relationType);
    				approveFlowIds.push(row.approveFlowId);
    				flow_types.push(row.flow_type);
    				cityIds.push(typeof row.cityId == 'undefined'?'':row.cityId);
    				deptIds.push(typeof row.deptId == 'undefined'?'':row.deptId);
    			});
    			$.post('${ctx}/aibi-approval/deptFlowRelation/delete', {campDrvIds:campDrvIds.join(','),relationTypes:relationTypes.join(',')
    				,approveFlowIds:approveFlowIds.join(','),flow_types:flow_types.join(',')
    				,cityIds:cityIds.join(','),deptIds:deptIds.join(',')
    			}, function(result){
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