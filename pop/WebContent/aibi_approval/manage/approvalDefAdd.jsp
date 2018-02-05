<%@ page contentType="text/html;charset=UTF-8"%>
<!doctype html>
<html>
<head>
<%@ include file="../layouts/head.jsp"%>
<title>流程定义新建</title>
<style type="text/css">
body{
margin: 2px;
}
.tbl_approval{
	margin-top: 5px;
}
.tbl_approval thead tr {
  background-color: #efefef;
  background: -webkit-linear-gradient(top,#F9F9F9 0,#efefef 100%);
  background: -moz-linear-gradient(top,#F9F9F9 0,#efefef 100%);
  background: -o-linear-gradient(top,#F9F9F9 0,#efefef 100%);
  background: linear-gradient(to bottom,#F9F9F9 0,#efefef 100%);
  background-repeat: repeat-x;
  filter: progid:DXImageTransform.Microsoft.gradient(startColorstr=#F9F9F9,endColorstr=#efefef,GradientType=0);
  height:25px;
}
.tbl_approval thead td{
	border-width:1px 1px 1px 0;border-style: dotted;border-color: #ccc; padding: 0px 5px;
	border-bottom: 1px solid #ccc;
	border-top: 1px solid #ccc;
}
.tbl_approval tbody tr{
	height:25px;overflow: hidden;
}
.tbl_approval tbody td{
	border-width:0 1px 1px 0;border-style: dotted;border-color: #ccc; padding: 0px 5px;
}
.hidden{
	display: none;
}
</style>
</head>
<body>
	<form id="fm" method="post" novalidate>
		<div id="tb" class="toolbar_height datagrid-toolbar">
			<span>流程名称:</span>
			<input id="approveFlowId" name="approveFlowId" type="hidden" value="${flowDef.approveFlowId }">
			<input id="approveFlowName" name="approveFlowName" class="easyui-textbox" style="width:200px;" required="true" value="${flowDef.approveFlowName }">
			<span style="margin-left: 10px;">访问标识:</span>
			<select class="easyui-combobox" name="approveFlowAccessToken" style="width:200px;" required="true">
			    <option value="0" <c:if test="${flowDef.approveFlowAccessToken == 0}">selected="selected"</c:if>>私有</option>
			    <option value="1" <c:if test="${flowDef.approveFlowAccessToken == 1}">selected="selected"</c:if>>公有</option>
			</select>
			<span style="margin-left: 10px;">流程类型:</span>
			<select class="easyui-combobox" id="flowType" name="flowType" style="width:200px;margin-right: 10px;" required="true" data-options="onSelect: function(rec){if(rec.value==1){$('.newApprovalSeq,.delApprovalSeq').hide();}else{$('.newApprovalSeq,.delApprovalSeq').show();}}">
			    <option value="1" <c:if test="${flowDef.flowType == 1}">selected="selected"</c:if>>普通</option>
			    <option value="2" <c:if test="${flowDef.flowType == 2}">selected="selected"</c:if>>会签</option>
			</select>
		    <a href="#" class="easyui-linkbutton fl_right" style="margin-right: 10px;" iconCls="icon-add" onclick="newApprovalLevel()">新增审批级别</a>
		</div>
	
		<table id="tbl" class="tbl_approval" border="0" cellspacing="0" cellpadding="0" style="width: 100%;table-layout: fixed;">
			<thead>
				<tr><td width="30%">审批级别</td><td width="40%">审批人</td><td width="30%">操作</td></tr>
			</thead>
			<tbody>
				<!-- 审批列表为空 -->
				<c:if test="${ empty level}">
					<tr class="level">
				    	<td>第【1】级审批</td>
				    	<td></td>
				    	<td>
				    		<a href="#" class="easyui-linkbutton" plain="true" data-options="iconCls:'icon-remove'"  onclick="delApprovalLevel(this)">删除该级审批</a>
				    		<a href="#" class="easyui-linkbutton newApprovalSeq" plain="true" data-options="iconCls:'icon-add'"  onclick="newApprovalSeq(this)">新增同级审批</a>
				    	</td>
			    	</tr>
			    	<tr class="seq">
				    	<td></td>
				    	<td>
				    	<span>审批参与者</span>
				    	<select class="easyui-combobox" style="width:100px;margin-right: 10px;" data-options="onSelect: selectType">
						    <option value="1">所属部门</option>
						    <option value="2">指定部门</option>
						    <option value="3">指定人</option>
						</select>
						<input class="ztreeCombox dept'" type="text" readonly value="" style="width:128px;" data-options="'url':'${ctx}/aibi-approval/approvalRelation/getGroupTree',selectParentNode:true"/>
						<input class="ztreeCombox user'" type="text" readonly value="" style="width:128px;" data-options="'url':'${ctx}/aibi-approval/approvalRelation/getUserTree',selectParentNode:false"/>
						</td>
				    	<td><a href="#" class="easyui-linkbutton delApprovalSeq" plain="true" data-options="iconCls:'icon-remove'" onclick="delApprovalSeq(this)">删除该审批</a></td>
			    	</tr>
				</c:if>
				<!-- 审批列表不为空 -->
				<c:if test="${not empty level}">
					<!-- 遍历级别 start  -->
					<c:forEach var="item" items="${level}">
						<tr class="level">
					    	<td>第【${item.key}】级审批</td>
					    	<td></td>
					    	<td>
					    		<a href="#" class="easyui-linkbutton" plain="true" data-options="iconCls:'icon-remove'"  onclick="delApprovalLevel(this)">删除该级审批</a>
					    		<a href="#" class="easyui-linkbutton newApprovalSeq" plain="true" data-options="iconCls:'icon-add'"  onclick="newApprovalSeq(this)">新增同级审批</a>
					    	</td>
				    	</tr>
						<c:forEach var="seq" items="${item.value}">
						<!-- 遍历级别下所有参与 start  -->
						<tr class="seq">
					    	<td></td>
					    	<td>
					    	<span>审批参与者</span>
					    	<select class="easyui-combobox" style="width:100px;margin-right: 10px;" data-options="onSelect: selectType">
							    <option value="1" <c:if test="${seq.id.approveObjType == 1}">selected="selected"</c:if>>所属部门</option>
							    <option value="2" <c:if test="${seq.id.approveObjType == 2}">selected="selected"</c:if>>指定部门</option>
							    <option value="3"<c:if test="${seq.id.approveObjType == 3}">selected="selected"</c:if>>指定人</option>
							</select>
							<input class="ztreeCombox dept <c:if test="${seq.id.approveObjType == 2}">show</c:if>" type="text" readonly value="" style="width:128px;" data-options="'url':'${ctx}/aibi-approval/approvalRelation/getGroupTree',selectParentNode:true <c:if test="${seq.id.approveObjType == 2}">,initValue:{text:'${seq.approveObjName }',value:'${seq.id.approveObjId }'}</c:if>"/>
							<input class="ztreeCombox user <c:if test="${seq.id.approveObjType == 3}">show</c:if>" type="text" readonly value="" style="width:128px;" data-options="'url':'${ctx}/aibi-approval/approvalRelation/getUserTree',selectParentNode:false <c:if test="${seq.id.approveObjType == 3}">,initValue:{text:'${seq.approveObjName }',value:'${seq.id.approveObjId }'}</c:if>"/>
							</td>
					    	<td><a href="#" class="easyui-linkbutton delApprovalSeq" plain="true" data-options="iconCls:'icon-remove'" onclick="delApprovalSeq(this)">删除该审批</a></td>
			    		</tr>
						</c:forEach>
						<!-- 遍历级别下所有参与 end  -->
					</c:forEach>
					<!-- 遍历级别 start  -->
				</c:if>
			</tbody>
		</table>
		<div id="dlg-buttons"  class="datagrid-toolbar" style="margin-top: 10px;padding: 5px;text-align: right;border:0px;">
			<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-ok" onclick="saveInfo()" style="width:90px">保存</a>
			<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:window.parent.closeWin();" style="width:90px">取消</a>
		</div>		
	</form>
</body>
<%@ include file="../layouts/bottom.jsp"%>
<!-- 审批级别模版 -->
<script id="tmpl_level" type="text/x-jquery-tmpl">
<tr class="level">
	<td>第【{{= level}}】级审批</td>
	<td></td>
	<td>
		<a href="#" class="easyui-linkbutton" plain="true" data-options="iconCls:'icon-remove'"  onclick="delApprovalLevel(this)">删除该级审批</a>
		<a href="#" class="easyui-linkbutton newApprovalSeq" plain="true" data-options="iconCls:'icon-add'"  onclick="newApprovalSeq(this)">新增同级审批</a>
	</td>
</tr>
</script>
<!-- 审批级别下同级审批模版 -->
<script id="tmpl_seq" type="text/x-jquery-tmpl">
<tr class="seq">
	<td></td>
	<td>
	<span>审批参与者</span>
	<select class="easyui-combobox" style="width:100px;margin-right: 10px;" data-options="onSelect: selectType">
	    <option value="1">所属部门</option>
	    <option value="2">指定部门</option>
	    <option value="3">指定人</option>
	</select>
	
	<input class="ztreeCombox dept'" type="text" readonly value="" style="width:128px;" data-options="'url':'${ctx}/aibi-approval/approvalRelation/getGroupTree',selectParentNode:true"/>
	<input class="ztreeCombox user'" type="text" readonly value="" style="width:128px;" data-options="'url':'${ctx}/aibi-approval/approvalRelation/getUserTree',selectParentNode:false"/>
	</td>
	<td><a href="#" class="easyui-linkbutton delApprovalSeq" plain="true" data-options="iconCls:'icon-remove'" onclick="delApprovalSeq(this)">删除该审批</a></td>
</tr>
</script>  
<script type="text/javascript">
$(function(){
	 //根据文本框中的值找到结点并显示出来
    $('#tbl .ztreeCombox:not(.show)').parent().hide();
    $('#flowType').combobox('getValue') == 1?$('.newApprovalSeq,.delApprovalSeq').hide():$('.newApprovalSeq,.delApprovalSeq').show();
    calc_iframe_height();
});
//选择参与类型
function selectType(rec){
    if(rec.value==1){
   		$(this).nextAll('.ztree_container').hide();
   }else if(rec.value==2){
   		$(this).nextAll('.ztree_container').eq(0).show();
   		$(this).nextAll('.ztree_container').eq(1).hide();
   }else if(rec.value==3){
	   $(this).nextAll('.ztree_container').eq(0).hide();
  		$(this).nextAll('.ztree_container').eq(1).show();
   }
}

//新建审批级别
function newApprovalLevel(){
	//查看所有已存在级别数
	var newLevel = $('#tbl').find('tr.level').length+1;
	var $level = $('#tmpl_level').tmpl({ "level" : newLevel});
	var $seq = $('#tmpl_seq').tmpl();
	$('#tbl > tbody').append($level).append($seq);
	$level.find('.easyui-linkbutton').linkbutton();
	$seq.find('.easyui-linkbutton').linkbutton();
	$seq.find('.easyui-combobox').combobox();
	$seq.find('.ztreeCombox').ztreeComb();
	$seq.find('.ztreeCombox').parent().hide();
	$('#flowType').combobox('getValue') == 1?$('.newApprovalSeq,.delApprovalSeq').hide():$('.newApprovalSeq,.delApprovalSeq').show();
	 calc_iframe_height();
}

//删除审批级别
function delApprovalLevel(thisobj){
	var currentLevelTR = $(thisobj).parent('td').parent('tr');
	currentLevelTR.nextUntil('.level','.seq').remove();
	currentLevelTR.remove();
	$('#tbl > tbody > tr.level').each(function(index){
		$(this).find('td:first').text('第【'+(index+1)+'】级审批');
	});
	 calc_iframe_height();
}
//新建同级审批级别
function newApprovalSeq(thisobj){
	var currentLevelTR = $(thisobj).parent('td').parent('tr');
	var lastSeqTR = currentLevelTR.nextUntil('tr.level','tr.seq:last');
	var $seq = $('#tmpl_seq').tmpl();
	if(lastSeqTR.length == 0){
		currentLevelTR.after($seq);
	}else{
		lastSeqTR.after($seq);
	}
	$seq.find('.easyui-linkbutton').linkbutton();
	$seq.find('.easyui-combobox').combobox();
	$seq.find('.ztreeCombox').ztreeComb();
	$seq.find('.ztreeCombox').parent().hide();
	 calc_iframe_height();
}
//删除同级审批级别
function delApprovalSeq(thisobj){
	var currentLevelTR = $(thisobj).parent('td').parent('tr');
	currentLevelTR.find('.ztreeCombox').ztreeComb('destory');
	currentLevelTR.remove();
	calc_iframe_height();
}

//保存
function saveInfo(){
    $('#fm').form('submit',{
        url: '${ctx}/aibi-approval/approvalDef/saveOrUpdate',
        onSubmit: function(param){
        	//审批级别数
        	param.approveFlowLevelCnt = $('#tbl').find('tr.level').length;
        	//第一个审批参与
        	var tr = $('#tbl').find('tr.level:first + tr.seq');
        	if(tr.length > 0){
        		if(tr.find('.easyui-combobox:first').combobox('getValue')==3){
            		param.firstApproveUser= tr.find('.ztreeCombox.user + input:hidden').val();
            	}else{
            		param.firstApproveUser='';
            	}
        	}
        	
        	//循环每一个审批级别
        	var retStr = [];
        	var error = false;
        	var errorCount = false;//一个审批级别至少有一个审批人
        	$('#tbl').find('tr.level').each(function(index,thisobj){
        		if(error){
        			return false;
        		}
        		var seqCount = 0;//同级审批数至少一个
        		$(this).nextUntil('.level','tr.seq').each(function(){//遍历紧跟其后的审批级别下同级审批
        			var levelApproveObjType = $(this).find('.easyui-combobox:first').combobox('getValue');
        			var levelApproveObjId = '';
        			var levelApproveObjName = '';
        			if(levelApproveObjType == 2 || levelApproveObjType == 3){
        				var _ztree = $(this).find('.ztreeCombox').eq(levelApproveObjType-2);
        				levelApproveObjName = _ztree.val();
        				levelApproveObjId = _ztree.next(':hidden').val();
        				if(levelApproveObjId == '' || levelApproveObjName == ''){
        					$.messager.alert('警告','请选择审批参与者！','warning');
        					error = true;
        					return false;
        				}
        			}
        			retStr.push("level=" + (index+1) + ",ObjType=" + levelApproveObjType + ",ObjId="+levelApproveObjId+",ObjName="+levelApproveObjName);
        			seqCount++;
        		});
        		if(!error && seqCount == 0){
        			$.messager.alert('警告','第【'+(index+1)+'】级审批应至少添加一个审批参与者！','warning');
        			error = true;
        			errorCount = true;
					return false;
        		}
        	});
	    	
	    	if (retStr.length == 0) {
	    		!errorCount && alert('审批流程至少需要定义1个审批级别！');
	    		return false;
	    	}
	    	
	    	param.approveFlowDefStr = retStr.join(';');
            return !error && $(this).form('validate');
        },
        success: function(result){
            var result = $.parseJSON(result);
            if (result.errorMsg){
                $.messager.show({ title: '保存失败',msg: result.errorMsg,timeout:3000});
            } else {
            	window.parent.$.messager.show({ title: '提示',msg: '保存成功',timeout:3000});
            	window.parent.closeWin();
            	window.parent.doSearch();
            }
        }
    });
}

function calc_iframe_height(){
	var otherHeight = 88;//上下工具条高度
	var tableHeiht = $('#tbl').height();
	//$('#openIframe', window.parent.document).css('height',otherHeight+tableHeiht+'px');
	
}
</script>
</html>