<%@ page contentType="text/html;charset=UTF-8"%>
<!doctype html>
<html>
<head>
	<%@ include file="layouts/head.jsp"%>
	<title>首页</title>
</head>
<body class="easyui-layout">
	<!-- 左边导航树 begin -->
    <div data-options="region:'west',title:'审批管理',split:true" style="width:150px;">
	    <ul id="tree_nav" class="easyui-tree"></ul>
    </div>
    <!-- 左边导航树 end -->
    
    <!-- main 区域 begin -->
    <div data-options="region:'center'">
	    <div id="tt" class="easyui-tabs" fit="true" border="false" plain="true" data-options="border:false" style="height:700px;">
		</div>
    </div>
    <!-- main 区域 end -->
</body>
<%@ include file="layouts/bottom.jsp"%>
<script type="text/javascript">
$(function(){
	addTab('驱动类型', '${ctx }/aibi_approval/manage/drvTypeList.jsp',false);
	$(window).resize(function(){
		setTimeout(function(){
			$('#tt').tabs('resize');
	        $('#tt iframe').width($('#tt').parent().attr('width'));
		},100);
    });
});
$('#tree_nav').tree({
	data: [{text: '驱动类型',"attributes":{"url":"${ctx }/aibi_approval/manage/drvTypeList.jsp"}},
	       {text: '审批关系',"attributes":{"url":"${ctx }/aibi_approval/manage/approvalRelationList.jsp"}},
	       {text: '流程定义',"attributes":{"url":"${ctx }/aibi_approval/manage/approvalDefList.jsp"}},
		   {text: '流程管理',"attributes":{"url":"${ctx }/aibi_approval/manage/deptFlowRelationList.jsp"}}
	       ],
	onClick: function(node){
		addTab(node.text,node.attributes.url,true);
	}
});
function addTab(title, url,closable){
    if ($('#tt').tabs('exists', title)){
        $('#tt').tabs('select', title);
    } else {
        var content = '<iframe scrolling="no" frameborder="0"  src="'+url+'" style="width:100%;height:500px;"></iframe>';
        $('#tt').tabs('add',{
            title:title,
            content:content,
            closable:closable
        });
        $('iframe:last').iFrameResize({log:true,sizeWidth:false,sizeHeight:true});
    }
}
</script>
</html> 