<%@page import="com.ai.bdx.frame.approval.model.MtlApproveFlowDef"%>
<%@page import="java.util.List"%>
<%@page
	import="com.ai.bdx.frame.approval.service.IMtlApproveFlowDefService"%>
<%@page import="com.ai.bdx.frame.approval.util.SpringContext"%>
<%@ page contentType="text/html;charset=UTF-8"%>
<!doctype html>
<html>
<head>
<%@ include file="../layouts/head.jsp"%>
<title>登录</title>
<style type="text/css">
body{
text-align: center;
}
</style>
</head>
<body>
	<div style="width:400px;margin: 50px auto;" class="container">
	 <div class="easyui-panel" title="用户登录" style="width:400px;">
        <div style="padding:10px 60px 20px 60px;">
	        <form id="ff" method="post" action="${ctx}/aibi_approval/login/verifyUser.jsp" target="_blank">
	            <table cellpadding="5">
	                <tr>
	                    <td>用户名:</td>
	                    <td><input class="easyui-textbox" type="text" name="Username" id="Username" data-options="required:true,validType:'isProper',missingMessage:'不能为空'"></input></td>
	                </tr>
	                <tr>
	                    <td>密码:</td>
	                    <td><input class="easyui-textbox" type="password" name="Password" data-options="required:true,missingMessage:'不能为空'"></input></td>
	                </tr>
	            </table>
	        </form>
	        <div style="text-align:center;padding:5px">
	            <a href="javascript:void(0)" class="easyui-linkbutton" onclick="submitForm()">登录</a>
	            <a href="javascript:void(0)" class="easyui-linkbutton" onclick="clearForm()">重置</a>
	        </div>
        </div>
	</div>
	</div>
</body>
<%@ include file="../layouts/bottom.jsp"%>
<script type="text/javascript">
$.extend($.fn.validatebox.defaults.rules, {
	isProper: {
        validator: function(value, param){
            return (value.search(/^\w+( \w+)?$/) != -1);
        },
        message: '请输入合法的用户名（字母、数字、下划线）'
    }
});
//提交登录
function submitForm(){
    $('#ff').form('submit');
}
//重置表单
function clearForm(){
    $('#ff').form('clear');
}
</script>
</html>
