<%@page import="com.ai.bdx.frame.approval.util.RequestUtil"%>
<%@ page contentType="text/html; charset=gb2312"%>
<%@include file="Base64.jsp"%>
<%@page import="com.asiainfo.biframe.privilege.base.constants.*"%>
<%@page import="com.asiainfo.biframe.utils.database.jdbc.*"%>
<%@page import="com.ai.bdx.frame.approval.listener.SessionListener"%>
<%@page import="com.asiainfo.biframe.utils.config.Configure"%>

<%
	//����Ƿ��и��û�����У�����룬����������Ϣ
	//ȡ�ñ�������������
	String strHostIP = "";
	try {
		strHostIP = UserManager.getHostAddress();//java.net.InetAddress.getLocalHost().getHostAddress();
	} catch (Exception ex) {
		strHostIP = "localhost";
	}
	//����û�
	String strUserID = RequestUtil
			.getEscapedString(request, "Username");
	String strPswd = RequestUtil.getEscapedString(request, "Password");
	Sqlca sqlca = null;
	String strSql = "";
	try {
		sqlca = new Sqlca(new ConnectionEx());
		String strUserPswd = "";
		String flag = Configure.getInstance().getProperty(
				"IS_SUITE_PRIVILEGE");
		if ("true".equalsIgnoreCase(flag)) {
			strSql = "select userid, pwd from user_user where userid=?";
		} else {
			strSql = "select STAFF_ID,STAFF_PWD from LKG_STAFF where STAFF_ID=?";
		}
		Object[] obj = new Object[1];
		obj[0] = strUserID;
		sqlca.execute(strSql, obj);
		if (!sqlca.next()) {
%>
<script>
					alert("���û������ڣ�");
					window.location = '<%=request.getContextPath()%>/aibi_approval/login/login.jsp?UserID=<%=strUserID%>';
</script>
<%
	return;
		}
		String user = "";
		if ("true".equalsIgnoreCase(flag)) {
			user = sqlca.getString("userid");
		} else {
			user = sqlca.getString("STAFF_ID");
		}
		session.setAttribute("user", user);
		String province = Configure.getInstance().getProperty(
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
		sqlca.execute(strSql, obj);
		if (!sqlca.next()) {
%>
<script>
					alert("�������");
					window.location = '<%=request.getContextPath()%>/login?UserID=<%=strUserID%>';
				</script>
<%
	return;
		}
		//����һ���µ�ʵ��
		pageContext.setAttribute(UserManager.ASIA_SESSION_NAME,
				SessionListener.login(request, strUserID, "ADMIN"),
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
	//��λ����ҳ��
	var newWin = window.open("<%=request.getContextPath()%>/aibi_approval/index.jsp","indexPage","status=no,toolbar=no,menubar=no,scrollbars=yes,location=no,channelmode=0,directories=0,resizable=no,titlebar=no",false);
	newWin.moveTo(0, 0);
	newWin.resizeTo(window.screen.availWidth, window.screen.availHeight);
	window.opener = null;//�������
</script>
