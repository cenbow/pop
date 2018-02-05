<%@page import="com.asiainfo.biframe.utils.date.DateUtil"%>
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@page import="java.util.Date"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="/WEB-INF/layouts/head.jsp"%>
<meta charset="utf-8" />
<title>上传附件</title>
<meta content="width=device-width, initial-scale=1.0" name="viewport" />
<meta content="" name="description" />
<meta content="" name="author" />
<META HTTP-EQUIV="pragma" CONTENT="no-cache">
<META HTTP-EQUIV="Cache-Control" CONTENT="no-cache, must-revalidate">
<style type="text/css">
html, body {
	height: 100%;
}

.modal-body {
	max-height: 1000px;
}

.ztreePanel {
	float: left;
	width: 200px;
	border: 1px solid #ccc;
}

.ztreeHidIcon {
	float: left;
	width: 8px;
}

.ztreeCTPanel {
	margin-left: 208px;
	border: 1px solid #ccc;
}

.form-horizontal .control-label {
	width: 100px;
	font-weight: bold;
}

.btvdclass {
	border-color: #FF0000;
	color: red;
	font-weight: normal;
}

.commonWidth {
	width: 185px;
}
</style>
</head>
<body>
	<div id="mainDiv" class="modal-body" style="width: auto; height: auto">
		<div class="widget-body">
			<form action="${ctx}/policyApproval/approvalConfirmNotPass"
				method="post">
				<input type="hidden" name="plocyid" id="plocyid" value="${plocyid}" />
				<input type="hidden" name="sucFlag" id="sucFlag" value="${sucFlag}" />
				<div class="form-group">
					<table
						class="table table-striped table-bordered table-mcdstyle table-epl">
						<tr>
							<td align="center"><select id="manufacturers"
								name="manufacturers" onchange="selectFiles();">
									<option value="">请选择</option>
									<option value="1">华为</option>
									<option value="2">中兴</option>
									<option value="3">爱立信</option>
							</select></td>
						</tr>
						<tr>
							<td><input id="file_upload" name="file_upload" type="file"
								value="导入文件" /> (文件类型：*.txt;文件大小不能超过50M)
								<div id="fileQueue"></div></td>
						</tr>
					</table>
				</div>
				<br>
				<div style="text-align: right;">
					<input id="btn_submit" class="btn btn-primary" type="button" value="开始上传" /> &nbsp;
					<input id="btn_cancel" class="btn btn-primary" type="button" value="取消所有上传" /> &nbsp;
					<input id="btn_return" class="btn btn-primary" type="button" value="关闭" onclick="closeWindow();" />
				</div>
			</form>
			<div style="ext-align:center">
			   <iframe id="upFilesList" name="upFilesList" src="" MARGINHEIGHT="1"  MARGINWIDTH="1" frameBorder="0" width="100%" scrolling="auto" height="270"></iframe>
			</div>
		</div>
	</div>
	<%@ include file="/WEB-INF/layouts/bottom.jsp"%>
	<script type="text/javascript">
jQuery(document).ready(function() {
	$("#btn_submit").click(function(){
		 var ids=$("#approvl_id").val();
		 var flag=true;
		 var plocyid=$('#plocyid').val();
		 if(plocyid.replace(/\s/g,'') == ''){     
             alert("请选择策略！");  
             flag=false;
             return false;     
        }   
		 var manufacturers=$('#manufacturers').val();
		 if(manufacturers.replace(/\s/g,'') == ''){     
             alert("请选择厂商！"); 
             flag=false;
             return false;     
        }  
		 if(flag){
			 $('#file_upload').uploadify('upload','*');
		 }
	});
	$("#btn_cancel").click(function(){
			 $('#file_upload').uploadify('cancel', '*');
	});
	
	

	$('#file_upload').uploadify({
		'height':23,
		'width':75,
		'swf' : '${ctx}/static/assets/uploadify/uploadify.swf',
		'buttonText': '文件导入',
		'fileTypeExts': '*.txt',
		'queueID': 'fileQueue',
		'auto': false,
		'multi': false,
		'fileSizeLimit' : '50MB',
		'uploader':'${ctx}/fileManager/upload',
		'formData':{'plocyid':'','manufacturers':''},
		'onUploadStart':function(file){
			 var plocyid=$('#plocyid').val();
			 if(plocyid.replace(/\s/g,'') == ''){     
                 alert("请选择策略！");     
                 return false;     
            }   
			 var manufacturers=$('#manufacturers').val();
			 if(manufacturers.replace(/\s/g,'') == ''){     
                 alert("请选择厂商！");     
                 return false;     
            }  
		   $("#file_upload").uploadify("settings", "formData", {'plocyid':plocyid,'manufacturers':manufacturers});  
		},
		'onUploadComplete':function(file){
			alert('文件 ' + file.name + ' 上传成功'); 
		},
		'onCheck':function(file, exists){
			if (exists) {
                alert('文件 ' + file.name + '已经存在');
            }
		},
		'onUploadError':function(file, errorCode, errorMsg, errorString){
		    alert('文件' + file.name + ' 上传失败: ');  
		},
		'onUploadSuccess' : function(file, data, response) {
			 var plocyid=$('#plocyid').val();
			 var manufacturers=$('#manufacturers').val();
			 if(plocyid.replace(/\s/g,'') != ''&&manufacturers.replace(/\s/g,'') != ''){ 
				 //$("#upFilesList").attr("height",150);
				 //$("#upFilesList").attr("width",300);
				 $("#upFilesList").attr("src",'${ctx}/fileManager/uploadSuccess?id='+plocyid+'&manufacturers='+manufacturers);
	           }   
		}
		
	});
	
	
});

function closeWindow(){
	parent.callback_close();
}
function selectFiles(){
	var flag=true;
	 var plocyid=$('#plocyid').val();
	 if(plocyid.replace(/\s/g,'') == ''){     
         alert("请返回策略审批列表选择一个策略！");  
         flag=false;
         $("#upFilesList").attr("src",'');
         return flag;     
    }   
	 var manufacturers=$('#manufacturers').val();
	 if(manufacturers.replace(/\s/g,'') == ''){     
         alert("请选择厂商！"); 
         flag=false;
         $("#upFilesList").attr("src",'');
         return flag;     
    }  
	 if(flag){
		// $("#upFilesList").attr("height",150);
		// $("#upFilesList").attr("width",300);
		 $("#upFilesList").attr("src",'${ctx}/fileManager/uploadSuccess?id='+plocyid+'&manufacturers='+manufacturers);
	 }
}
</script>
</body>
</html>
