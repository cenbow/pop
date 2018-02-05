<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>规则设置弹出页面</title>
<style type="text/css">
.uploadify-button {background-color: #46B6DC;border: none;padding: 0; text-align:center;}
.uploadify:hover .uploadify-button {background-color: #1F85CB;}
</style>
<%@ include file="/WEB-INF/layouts/head.jsp"%>

<style type="text/css">
.form-horizontal .control-label {
	width: 78px;
	font-weight: bold;
}

label {
	cursor: text;
}
</style>
</head>
<body class="main-content-full">
	<input type="hidden" id="propId" name="propId" value="${propId}" />
	<!-- jinl 20160413 选择基站信息 -->
	<input type="hidden" id="lacciAndStationTypeJsons" name="lacciAndStationTypeJsons" value="${lacciAndStationTypeJsons}">
						
		<!-- BEGIN PAGE CONTAINER-->
		<div class="container-fluid">
			<!-- BEGIN ADVANCED TABLE widget-->
			<div class="row-fluid">
				<div class="span12" >
					<!-- BEGIN EXAMPLE TABLE widget-->
					<div class="widget">
						<div class="widget-body">
							<div class="tabbable">
								<ul class="nav nav-tabs">
									<li><a href="#panel-443325" data-toggle="tab" onclick="setIframeSrc();">基站信息</a></li>
								</ul>
									<div class="tab-pane" id="panel-443325">
										<iframe id="cepIframe" scrolling="yes" align="top" width="100%" height="500px;"  frameborder="0"></iframe>
									</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
		<!-- END PAGE CONTAINER-->
	</div>


	<%@ include file="/WEB-INF/layouts/bottom.jsp"%>
	<script type="text/javascript">
	jQuery(document).ready(function() {
		//add by jinl 20160412 设置cep iframe链接
		setIframeSrc();
		
		
	});

	function setIframeSrc() {
		$("#cepIframe").attr("src", "${cepEventLacciInfoUrl}");
	}


	
	//add by jinl 20160412 导出基站信息
	function exportLacciAndTacecgi(lacciAndStationTypeJsons){
		alert(lacciAndStationTypeJsons);
	}
</script>
</body>
</html>