<%@ page contentType="text/html;charset=UTF-8"%>
<%@page import=" java.util.Date"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
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
	<%
	
		 Object date = session.getAttribute("date"); 
		
		 long time =0;
		 long nowtime = new Date().getTime();
		 if(date!=null){
			 time = Long.parseLong(date.toString()) + 60*1000;
		 }
		 Object count = session.getAttribute("count");
			int errorcount=0;
			if(count!=null){
				errorcount = Integer.parseInt(count.toString());
			}

		System.out.println(time);
		if(date!=null&&new Date().getTime()-time-6000>=0){
			session.setAttribute("date",null);
			session.setAttribute("count",null);
		}
	%>
<body id="login-body"  >
 <div class="login-header">
      <div id="logo" class="center">
      </div>
  </div>

  <div id="login">
    <form id="loginform" class="form-vertical no-padding no-margin" action = "" method = "post" name ="logon">
      <div class="lock">
          <i class="icon-lock"></i>
      </div>
      <div class="control-wrap">
          <h4>POP运营平台</h4>
          <div class="control-group">
              <div class="controls">
                  <div class="input-prepend">
                      <span class="add-on"><i class="icon-user"></i></span><input id="pop_username"  name="Username" type="text" placeholder="用户名" />
                  </div>
              </div>
          </div>
          <div class="control-group">
              <div class="controls">
                  <div class="input-prepend" >
                      <span class="add-on"><i class="icon-key"></i></span><input id="pop_password"   name="Password" type="password" placeholder="密码" />
                  </div>
                  <div class="clearfix space5"></div>
              </div>
          </div>
          <div class="control-group">
              <div class="controls">
                  <div class="input-prepend">
                      <span class="add-on"><i class="icon-key"></i></span><input id="code_vcation"  style="width: 77px;" name="Vcation" type="text" placeholder="验证码" /> <span class='add-on' style='margin-left: 5px;width:101px' title='点击刷新' id="v_container" ></span> 
                  </div>
				 
              </div>
          </div>
      </div>
      <input type="submit" id="login-btn" class="btn btn-block login-btn" onclick="setFormAction(document.forms[0],'Logon');" value="登录" />
    </form>
  </div>
  <div id="login-copyright">
      2015 &copy; 亚信智慧数据.
  </div>
	<%@ include file="/WEB-INF/layouts/bottom.jsp"%>
</body>
</html>
<script language = "JavaScript">
window.ctx = '${ctx}';
</script>
<script type="text/javascript" src="${ctx}/static/js/gVerify.js" ></script>
<script type="text/javascript" src="${ctx}/static/js/des_new.js" ></script>
<script language = "JavaScript">
var ltime = <%=time%>;
var errorcount = <%=count%>;


var verifyCode = new GVerify("v_container");
function verification(){
	var verification=$.trim(document.forms[0].verification.value);
	if(verification.length!=0){
		if(verifyCode.validate(verification)){return true;}else{$("#msg").text("验证码输入不正确");return false;}
	}else{
		$("#msg").text("验证码不能为空");
		return false;
	}
}
$(function(){
	$("#pop_username").focus();
});
function isProper(string) {
    if (string.search(/^\w+( \w+)?$/) != -1)
        return true;
    else
        return false;
}
function setFormAction(objForm,intID){
	var verification=objForm.Vcation.value;
	if(errorcount>=5&&0>(new Date().getTime()-60000-ltime)){
		confirm("您错误次数太多,请休息片刻再试!!");
		return false;
	}
	switch(intID){
		case "Logon":
			if(objForm.Username.value==''){
				confirm("请输入用户名！");
				return false;
			}else if(verification==''){
				confirm("请输入验证码！");
				return false;
			}else if(!verifyCode.validate(verification)){
				confirm("验证码错误！");
				return false;
			}else if(isProper(objForm.Username.value)){
				objForm.Username.value =strEnc(objForm.Username.value,'D','E','S');
				objForm.Password.value =strEnc(objForm.Password.value,'D','E','S');
				objForm.action = "${ctx}/login/verifyUser";
				//objForm.submit();
			}else{
				confirm("请输入合法的用户名（字母、数字、下划线）！");
				return false;
			}
			break;
		default :
			break;
	}
}
</script>
