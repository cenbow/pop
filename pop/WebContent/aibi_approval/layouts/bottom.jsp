<%@ page contentType="text/html;charset=UTF-8"%>
<script type="text/javascript" src="${ctx}/aibi_approval/assets/js/jquery-1.7.1.min.js"></script>
<script type="text/javascript" src="${ctx}/aibi_approval/assets/js/jquery-easyui/jquery.easyui.min.js"></script>
<!-- 带td提示的datagrid插件 -->
<script type="text/javascript" src="${ctx}/aibi_approval/assets/js/jquery-easyui/jquery.easyui.datagridtip.js"></script>
<!-- 带有搜索框的combotree插件 -->
<script type="text/javascript" src="${ctx}/aibi_approval/assets/js/jquery-easyui/jquery.easyui.combotreesearch.js"></script>
<script type="text/javascript" src="${ctx}/aibi_approval/assets/js/jquery-easyui/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript" src="${ctx}/aibi_approval/assets/js/jquery.tmpl.js"></script>
<script type="text/javascript" src="${ctx}/aibi_approval/assets/js/ztree/jquery.ztree.all-3.5.js"></script>
<script type="text/javascript" src="${ctx}/aibi_approval/assets/js/ztree/jquery.ztree.excombo.js"></script>
<script type="text/javascript" src="${ctx}/aibi_approval/assets/js/iframeResizer/iframeResizer.js"></script>
<!-- 所有页面公共js部分 -->
<script type="text/javascript">
(function($){
	//ztreecombox组件初始化
	$(".ztreeCombox").each(function(){
		$(this).ztreeComb();
	});
	$(window).resize(function(){
 		$('#dg').datagrid('resize',{width:$(this).innerWidth()});
	});
})(jQuery);
</script>