<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<%@ include file="/WEB-INF/layouts/head.jsp"%>
		<meta http-equiv="X-UA-Compatible" content="IE=edge,Chrome=1" />
		<meta charset="utf-8" />
		 <meta content="width=device-width, initial-scale=1.0" name="viewport" />
		  <meta content="" name="description" />
  		<meta content="" name="author" />
		<style>
		</style>
	</head>
<c:set var="ctx" value="${pageContext.request.contextPath}" />	
<body id="login-body"  >
  <div id="login">
    <form id="loginform" class="form-vertical no-padding no-margin" action = "${ctx}/userConfig/updatePWD" method = "post" name ="logon">
           <div class="control-group">
              <div class="controls">
                  <div class="input-prepend">
                      <span class="add-on"><i class="icon-key"></i></span><input id="oldPwd"  name="oldPwd" type="text" placeholder="旧密码" />
                  </div>
              </div>
          </div>
          <div class="control-group">
              <div class="controls">
                  <div class="input-prepend">
                      <span class="add-on"><i class="icon-key"></i></span><input id="newPwd"   name="newPwd" type="password" placeholder="新密码" />
                  </div>
              </div>
          </div>
          <div class="control-group">
              <div class="controls">
                  <div class="input-prepend">
                      <span class="add-on"><i class="icon-key"></i></span><input id="rePwd"  name="rePwd" type="password" placeholder="重复密码" />
                  </div>
              </div>
          </div>
          <c:if test="${not empty msg}">
            <font color="red">${msg }</font>
          </c:if>
      <input type="button" id="login-btn" class="btn btn-block login-btn" onclick="setFormAction();" value="修改密码" />
    </form>
  </div>
	<%@ include file="/WEB-INF/layouts/bottom.jsp"%>
</body>
</html>
<script language = "JavaScript">
$(function(){
	$("#oldPwd").focus();
});
function isProper(string) {
    if (string.search(/^\w+( \w+)?$/) != -1)
        return true;
    else
        return false;
}
function checkValidat(input,msg)
{
     if(input.value==""){
    	 confirm(msg);
    	 input.focus();
    	 return false;
     }
     if(!isProper(input.value))
     {
    	 confirm("请输入合法的用户名（字母、数字、下划线）！");
    	 input.focus();
    	 return false;
     }
     return true;
     
}
function setFormAction(){
	if(checkValidat(loginform.oldPwd,"请输入旧密码！")&&checkValidat(loginform.newPwd,"请输入新密码！"))
		{
		   if(loginform.newPwd.value!=loginform.rePwd.value)
		   {
			   confirm("两次输入密码不一致，请确认！"); 
			   loginform.newPwd.focus();
		   }
		   else
			   {
			     loginform.submit();
			   }
		   
		}
}
</script>
