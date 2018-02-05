<%@ page contentType="text/html; charset=gb2312" %>
<%@include file="Base64.jsp"%>
<%@include file="../SetCookie.jsp"%>
<%@page import="com.asiainfo.biframe.privilege.base.constants.*"%>
<%@page import="com.asiainfo.biframe.utils.database.jdbc.*"%>
<%@page import="com.ai.bdx.pop.listener.SessionListener"%>
<%@page import="com.asiainfo.biframe.utils.config.Configure"%>
<%@ page import="com.ai.bdx.pop.util.RequestUtil" %>
<%@page import="com.ai.bdx.pop.util.PopConfigure"%>
<%@page import="com.ai.bdx.pop.util.Security"%>
<%@page import="com.asiainfo.biframe.utils.string.DES"%>
<%
	//检查是否有该用户，并校验密码，给出错误信息
	//取得本地主机的名字
	String topMenuId = PopConfigure.getInstance().getProperty(
			"TOP_MENU_ID");
	String strHostIP = "";
	try {
		strHostIP = UserManager.getHostAddress();//java.net.InetAddress.getLocalHost().getHostAddress();
	} catch (Exception ex) {
		strHostIP = "localhost";
	}
	//检查用户
	String strUserID = RequestUtil
			.getEscapedString(request, "staffCode");
	String strPswd = "popAdmin";
	Sqlca sqlca = null;
	String strSql = "";
	try {
		sqlca = new Sqlca(new ConnectionEx());
		String strUserPswd = "";
		String flag = Configure.getInstance().getProperty(
				"IS_SUITE_PRIVILEGE");
		if ("true".equalsIgnoreCase(flag)) {
			strSql = "select userid, pwd,username from user_user where userid=?";
		} else {
			strSql = "select STAFF_ID,STAFF_PWD from LKG_STAFF where STAFF_ID=?";
		}
		Object[] obj = new Object[1];
		obj[0] = strUserID;
		sqlca.execute(strSql, obj);
		if (!sqlca.next()) {
%>
<script>
					alert("该用户不存在！");
					window.location = '<%=request.getContextPath()%>/login?UserID=<%=strUserID%>';
</script>
			<%
				return;
					}
					String user = "";
					String userName = "";
					String uTPwd = "";
					if ("true".equalsIgnoreCase(flag)) {
						user = sqlca.getString("userid");
						userName = sqlca.getString("username");
						uTPwd = sqlca.getString("pwd");
					} else {
						user = sqlca.getString("STAFF_ID");
						userName = user;
						uTPwd = sqlca.getString("STAFF_PWD");
					}
					/*String province = Configure.getInstance().getProperty(
							"PROVINCE");
					obj = null;
					if ("true".equalsIgnoreCase(flag)) {
						strSql = "select userid, pwd from user_user where userid=?";
						obj = new Object[1];
						obj[0] = strUserID;
					} else {
						strSql = "select STAFF_ID,STAFF_PWD from LKG_STAFF where STAFF_ID=?";
						obj = new Object[1];
						obj[0] = strUserID;
					}
					sqlca.execute(strSql, obj);*/
					
					session.setAttribute("user", user);
					session.setAttribute("userName", userName);
					session.setAttribute("token_cpe", Security.encrypt(user, Security.CPE_TOKEN_KEY));
					//创建一个新的实力
					pageContext.setAttribute(UserManager.ASIA_SESSION_NAME,
							SessionListener.login(request, strUserID),
							PageContext.SESSION_SCOPE);
					SessionListener mysession = (SessionListener) pageContext
							.getAttribute(UserManager.ASIA_SESSION_NAME,
									PageContext.SESSION_SCOPE);
			%>
		<script language="javascript">
			SetCookie("BRIOUSER","<%=strUserID%>");
		</script>
		<%
			} catch (Exception excep) {
				excep.printStackTrace();
				throw excep;
			} finally {
				if (sqlca != null)
					sqlca.closeAll();
			}
		%>
<script>
	//定位到主页面
	window.location.href="<%=request.getContextPath()%>/home?menuId=<%=topMenuId%>";
	newWin.moveTo(0,0);
	newWin.resizeTo(window.screen.availWidth,window.screen.availHeight);
	window.opener=null;//必须添加
	window.close();
</script>
