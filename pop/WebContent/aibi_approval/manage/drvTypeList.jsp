<%@ page contentType="text/html;charset=UTF-8"%>
<!doctype html>
<html>
<head>
<%@ include file="../layouts/head.jsp"%>
<title>驱动类型列表</title>
</head>
<body>
	<!-- 浏览列表 start -->
	<table id="dg" class="easyui-datagrid" style="height:auto;" data-options="fitColumns:true,title:'驱动类型列表',url:'${ctx}/aibi-approval/drvType/list',singleSelect:true,rownumbers:true,pagination:true,toolbar:'#tb',resizeHandle:'both',striped:true,onDblClickRow:function(index,row){editInfo();}">
	    <thead>
	        <tr>
	        	<th data-options="field:'campDrvId',hidden:true"></th>
	        	<th data-options="field:'tableID',hidden:true"></th>
	        	<th data-options="field:'tableColVal',hidden:true"></th>
	        	<th data-options="field:'drvDesabled',hidden:true"></th>
	        	<th data-options="field:'flowId',hidden:true"></th>
	        	<th data-options="field:'drvTypeClassify'" width="80">驱动类型分类</th>
	        	<th data-options="field:'drvType'" width="100">驱动类型</th>
	        	<th data-options="field:'flowModel'"  width="100">流程模式</th>
	        	<th data-options="field:'drvDisabled'"  width="50">是否禁用</th>
	        	<th data-options="field:'campDrvDesc',resizable:true" width="150">描述</th>
	        </tr>
	    </thead>
	</table>
	<div id="tb" class="toolbar_height">
	    <a href="#" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="newInfo()">新建</a>
	    <a href="#" class="easyui-linkbutton" iconCls="icon-edit" plain="true" onclick="editInfo()">修改</a>
	    <div style="float:right;margin-right:20px;">
		    <span>驱动类型分类:</span>
		    <input id="drvTypeClassify" name="drvTypeClassify">
		    <span style="margin-left: 10px;">驱动类型:</span>
		    <input id="drvType" name="drvType" class="easyui-combobox" data-options="valueField:'code',textField:'val',data:[{code:'-1',val:'全部'}],editable:false">
		    <a href="#" class="easyui-linkbutton" plain="true" onclick="doSearch()" iconCls="icon-search">查询</a>
	    </div>
	</div>
	<!-- 浏览列表 end -->
	<!-- 新建对话框 start -->
	<div id="dlg" class="easyui-dialog" style="width:600px;height:500px;padding:10px 20px" buttons="#dlg-buttons" data-options="iconCls:'icon-add',resizable:true,closed:true,modal:true,top:0">
	    <form id="fm" method="post" novalidate style="margin:0;padding:10px 40px;">
	        <div class="fitem">
	            <label>驱动类型分类:</label>
	            <input id="tableID" name="tableID" class="easyui-combobox" data-options="editable:false,valueField:'value',textField:'label',loadFilter: function(data){return filterDrvType(data);},onSelect: function(rec){drvTypeClassifySel(rec);},url:'${ctx}/aibi-approval/drvType/getDrvTypeClassifys',width:180" required="true">
	        </div>
	        <div class="fitem">
	            <label>驱动类型:</label>
	             <input id="tableColVal" name="tableColVal" class="easyui-combobox" data-options="editable:false,valueField:'code',textField:'val',loadFilter: function(data){return filterDrvType(data);},width:180" required="true">
	        </div>
	        <div class="fitem">
	            <label>流程模式:</label>
	             <input id="flowId" name="flowId" class="easyui-combobox" data-options="editable:false,valueField:'value',textField:'label',url:'${ctx}/aibi-approval/drvType/getFLowModels',width:180" required="true">
	        </div>
	        <div class="fitem">
	            <label>是否禁用:</label>
	            <input name="drvDesabled" class="easyui-combobox" data-options="editable:false,valueField:'id',textField:'val',data:[{id:'0',val:'否'},{id:'1',val:'是'}],width:180" required="true">
	        </div>
	        <div class="fitem">
	            <label>类型描述:</label>
	            <input name="campDrvDesc" class="easyui-textbox" data-options="multiline:true,width:180" style="height:50px">
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
//查询
function doSearch(){
    $('#dg').datagrid('load',{
    	drvTypeClassify: $('#drvTypeClassify').combobox('getValue'),
    	drvType: $('#drvType').combobox('getValue')
    });
}

$(function(){
	//分类类型下拉框初始化
	$('#drvTypeClassify').combobox({
	    url:'${ctx}/aibi-approval/drvType/getDrvTypeClassifys',
	    valueField:'value',
	    textField:'label',
	    editable:false,
	    onSelect: function(rec){
	    	if(rec.value == '-1'){
	    		$('#drvTypeClassify,#drvType').combobox('setValue','-1');
	    		$('#drvTypeClassify,#drvType').combobox('setText','全部');
	    	}else{
	    		var url = '${ctx}/aibi-approval/drvType/getDrvTypes?drvTypeClassifyId='+rec.value;
	    	    $('#drvType').combobox('reload', url);
	    	    $('#drvType').combobox('setValue','-1');
	    	}
	    }
	});
	$('#drvTypeClassify,#drvType').combobox('setValue','-1').combobox('setText','全部');
	$('#dg').datagrid('doCellTip',{'max-width':'100px'});
});

//分类类型下拉框选择事件
function drvTypeClassifySel(rec){
	var url = '${ctx}/aibi-approval/drvType/getDrvTypes?drvTypeClassifyId='+rec.value;
    $('#tableColVal').combobox('reload', url);
}

//过滤"全部"option
function filterDrvType(data){
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

//打开新建渠道信息对话框
var url;
function newInfo(){
    $('#dlg').dialog('open').dialog('setTitle','新建');
    $('#fm').form('clear');
  //新建对话框中分类类型下拉框初始化
    url = '${ctx}/aibi-approval/drvType/saveDrvType';
}

//打开修改渠道信息对话框
function editInfo(){
    var row = $('#dg').datagrid('getSelected');
    if (row){
        $('#dlg').dialog('open').dialog('setTitle','编辑');
        $('#fm').form('clear');
        $('#tableColVal').combobox('reload', '${ctx}/aibi-approval/drvType/getDrvTypes?drvTypeClassifyId='+row.tableID);
        $('#fm').form('load',row);
        url = '${ctx}/aibi-approval/drvType/saveDrvType?campDrvId='+row.campDrvId;
    }else{
    	$.messager.alert('提示','请先选中行,再点击修改按钮!','warning');
    }
}

//保存渠道信息
function saveInfo(){
	//是否该驱动是否已经配置流程模式
	var result = $.getJSON('${ctx}/aibi-approval/drvType/checkDrvExist?'+$("#fm").serialize());
	if(result.flag == false){
		 $.messager.show({ title: '保存失败', msg: '已经存在或者每个驱动只能配置一个流程模式,请重新配置后再保存！'});
		return false;
	}
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
</script>
</html>