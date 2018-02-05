<%@ page contentType="text/html;charset=UTF-8"%>
<!doctype html>
<html>
<head>
<%@ include file="../layouts/head.jsp"%>
<title>审批关系列表</title>
</head>
<body>
	<!-- 浏览列表 start -->
	<table id="dg" class="easyui-datagrid" style="height:auto;" data-options="fitColumns:true,title:'审批关系列表',url:'${ctx}/aibi-approval/approvalRelation/list',singleSelect:false,pagination:true,toolbar:'#tb',resizeHandle:'both',striped:true,rownumbers:true,onDblClickRow:function(index,row){editInfo(row);}">
	    <thead>
	        <tr>
	        	<th data-options="field:'approveUserid',hidden:true"></th>
	        	<th data-options="field:'approveCreateUserid',hidden:true"></th>
	        	<th data-options="field:'deptId',checkbox:true"></th>
	        	<th data-options="field:'deptName'"  width="100">分公司/部门</th>
	        	<th data-options="field:'approveUsername'"  width="100">审批人员</th>
	        	<th data-options="field:'approveCreateUsername'"  width="100">定义人员</th>
	        	<th data-options="field:'createTime'"  width="100">定义时间</th>
	        	<th data-options="field:'approveUseridMsisdn'"  width="100">审批人员号码</th>
	        	<th data-options="field:'approveUseridEmail'" width="100">审批人员邮箱</th>
	        </tr>
	    </thead>
	</table>
	<div id="tb" class="toolbar_height">
	    <a href="#" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="newInfo()">新建</a>
	    <a href="#" class="easyui-linkbutton" iconCls="icon-edit" plain="true" onclick="editInfo()">修改</a>
	    <a href="#" class="easyui-linkbutton" iconCls="icon-remove" plain="true" onclick="delInfo()">删除</a>
	    <div style="float:right;margin-right:20px;">
		    <span>分公司/部门:</span>
		    <input id="deptId" class="ztreeCombox" type="text" readonly value="" style="width:128px;" data-options="'url':'${ctx}/aibi-approval/approvalRelation/getGroupTree',selectParentNode:true"/>
		    <span style="margin-left: 10px;">审批人员:</span>
		    <input id="approveUserid" class="ztreeCombox" type="text" readonly value="" style="width:128px;" data-options="'url':'${ctx}/aibi-approval/approvalRelation/getUserTree',selectParentNode:false,data:{simpleData:{rootPId:'dept_0'}}"/>
		    <a href="#" class="easyui-linkbutton" plain="true" onclick="doSearch()" iconCls="icon-search">查询</a>
	    </div>
	</div>
	<!-- 浏览列表 end -->
	<!-- 新建对话框 start -->
	<div id="dlg" class="easyui-dialog" style="width:600px;height:500px;padding:10px 20px" buttons="#dlg-buttons" data-options="iconCls:'icon-add',resizable:true,closed:true,modal:true,top:0">
	    <form id="fm" method="post" novalidate style="margin:0;padding:10px 40px;">
		    <div class="fitem">
		            <label>分公司/部门:</label>
		            <input id="fm_deptId" class="ztreeCombox" type="text" readonly value="" style="width:128px;" data-options="'url':'${ctx}/aibi-approval/approvalRelation/getGroupTree',selectParentNode:true,hiddenId:'deptId'"/>
		   	</div>
		   	<div class="fitem">
		           <label>审批人员:</label>
		           <input id="fm_approveUserid" class="ztreeCombox" type="text" readonly value="" style="width:128px;" data-options="'url':'${ctx}/aibi-approval/approvalRelation/getUserTree',selectParentNode:false,data:{simpleData:{rootPId:'dept_0'}},hiddenId:'approveUserid'"/>
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
		$('#'+id).combotree('tree').tree("search",$(this).val());
		if($(this).val().length == 0){
		     $('#'+id).combotree('tree').tree("collapseAll");
	         $('#'+id).combotree('setValue', '');
		}
    });
});
//查询
function doSearch(){
    $('#dg').datagrid('load',{
    	deptId:$('#deptId').ztreeComb('getValue')==''?'':$('#deptId').ztreeComb('getValue'),
    	approveUserid: $('#approveUserid').ztreeComb('getValue')==''?'':$('#approveUserid').ztreeComb('getValue')
    });
}

//打开新建渠道信息对话框
var url;
function newInfo(){
    $('#dlg').dialog('open').dialog('setTitle','新建');
    $('#fm').form('clear');
    $('#fm_deptId').ztreeComb('setEditable',true);
  //新建对话框中分类类型下拉框初始化
    url = '${ctx}/aibi-approval/approvalRelation/save';
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
         $('#fm').form('load',row);
         $('#fm_deptId').ztreeComb('setValue',row.deptId);
         $('#fm_deptId').ztreeComb('setText',row.deptName);
         $('#fm_deptId').ztreeComb('setEditable',false);
         $('#fm_approveUserid').ztreeComb('setValue',row.approveUserid);
         $('#fm_approveUserid').ztreeComb('setText',row.approveUsername);
         url = '${ctx}/aibi-approval/approvalRelation/update?beforeApproveUserid='+row.approveUserid+'&beforeCreateUserid='+row.approveCreateUserid;
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
    			var approveUserids = [];
    			var deptIds = [];
    			$(rows).each(function(i,row){
    				approveUserids.push(row.approveUserid);
    				deptIds.push(row.deptId);
    			});
    			$.post('${ctx}/aibi-approval/approvalRelation/delete', {deptIdsStr:deptIds.join(','), approveUseridsStr:approveUserids.join(',')}, function(result){
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