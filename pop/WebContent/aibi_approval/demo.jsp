<%@page import="com.ai.bdx.frame.approval.model.MtlApproveFlowDef"%>
<%@page import="java.util.List"%>
<%@page import="com.ai.bdx.frame.approval.service.IMtlApproveFlowDefService"%>
<%@page import="com.ai.bdx.frame.approval.util.SpringContext"%>
<%@ page contentType="text/html;charset=UTF-8"%>
<!doctype html>
<html>
<head>
	<%@ include file="layouts/head.jsp"%>
	<title>demo</title>
</head>
<body>
	${message}
	
	<%
	IMtlApproveFlowDefService approveFlowDefService = (IMtlApproveFlowDefService)SpringContext.getBean("mpmApproveFlowDefService", IMtlApproveFlowDefService.class);
	List<MtlApproveFlowDef> list = (List<MtlApproveFlowDef>)approveFlowDefService.getAllApproveFlowDef();
	for(int i=0;i<list.size();i++){
		
	}
	%>
	
<%@ include file="layouts/bottom.jsp"%>
<script type="text/javascript">
	
</script>
</body>
</html> 