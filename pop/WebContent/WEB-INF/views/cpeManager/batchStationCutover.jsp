<%@page import="com.asiainfo.biframe.utils.date.DateUtil"%>
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@page import="java.util.Date"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!doctype html>
<html>
<head>
<script type="text/javascript" src="${ctx}/static/js/jquery-1.8.3.min.js"></script>
<script type="text/javascript" src="${ctx}/static/assets/jqueryUI/jquery-ui.js"></script> 
<script type="text/javascript" src="${ctx}/static/assets/bootstrap/js/bootstrap.min.js"></script> 
<script type="text/javascript" src="${ctx}/static/assets/bootstrap/js/bootstrap-fileupload.js"></script>
<script type="text/javascript" src="${ctx}/static/assets/uniform/jquery.uniform.min.js"></script>
<script type="text/javascript" src="${ctx}/static/assets/bootstrap-datepicker/js/bootstrap-datepicker_zh_CN.js" charset="UTF-8"></script>   
<script type="text/javascript" src="${ctx}/static/assets/bootstrap-daterangepicker/date.js"></script>
<script type="text/javascript" src="${ctx}/static/assets/bootstrap-daterangepicker/daterangepicker.js"></script> 
<script type="text/javascript" src="${ctx}/static/assets/bootstrap-datetimepicker-master/js/bootstrap-datetimepicker.js" charset="UTF-8"></script>
<script type="text/javascript" src="${ctx}/static/assets/bootstrap-datetimepicker-master/js/locales/bootstrap-datetimepicker.zh-CN.js" charset="UTF-8"></script>
 <script type="text/javascript" src="${ctx}/static/assets/lazyscrollloading/jquery.lazyscrollloading-min.js"></script>
<script type="text/javascript" src="${ctx}/static/assets/ztree/jquery.ztree.all-3.5.js"></script>
<script type="text/javascript" src="${ctx}/static/assets/ztree/jquery.ztree.exhide-3.5.js"></script>
<script type="text/javascript" src="${ctx}/static/js/scripts.js"></script>
<script type="text/javascript" src="${ctx}/static/js/main.js" ></script>
<script type="text/javascript" src="${ctx}/static/js/json2.js" ></script>
<script src="${ctx}/static/assets/uploadify/jquery.uploadify.js" type="text/javascript"></script>
<script type="text/javascript" src="${ctx}/static/assets/treeTable/jquery.treeTable.js" ></script>
<script type="text/javascript" src="${ctx}/static/assets/jgrowl/jquery.jgrowl.min.js" ></script>
<%@ include file="/WEB-INF/layouts/head.jsp"%>

<title>参数重置-基站割接</title>

<meta charset="utf-8" />
<meta http-equiv="Content-Type" content="text/html;charset=utf-8" />
<meta http-equiv="Cache-Control" content="no-store" />
<meta http-equiv="Pragma" content="no-cache" />
<meta http-equiv="Expires" content="0" />
<meta content="width=device-width, initial-scale=1.0" name="viewport" />
<link href=" ${pageContext.request.contextPath }/static/assets/jqueryUI/themes/base/jquery-ui.css" rel="stylesheet" type="text/css" />
<link href=" ${pageContext.request.contextPath }/static/assets/bootstrap/css/bootstrap.min.css" rel="stylesheet" type="text/css" />
<link href=" ${pageContext.request.contextPath }/static/assets/bootstrap/css/bootstrap-responsive.min.css" rel="stylesheet" type="text/css" />


<style>
.line{display:-webkit-box}
.marigins{    
			margin-left: 15px;
			margin-right: 15px;
		}
thead td{
	    box-shadow: -2px 4px 0px #9E88BD;
		line-height: 15px;
}
.table th, .table td {
    padding: 8px;
    line-height: 16px;
    text-align: left;
    vertical-align: top;
    border-top: 1px solid #ddd;
}
.firstd{
    background-color: #C7BFCE;
    height: 30px;
    width: 85%;
    margin-top: 4px;
    box-shadow: -1px -1px 0px #f5f5f5;
    margin-left: 1px;
}
.firstdv{
	padding: 0px;background-color:#9E88BD;border-top: #9E88BD;
}
.reset-border1{
border: 4px solid;
    border-top-color: #D7BFEC;
    border-top-left-radius: 10px;
	border-top-right-radius: 10px;
    padding-left: 6px;
    padding-right: 6px;
    border-right-color: #7863EA;;
    border-left-color: #7863EA;;
    border-bottom-color: #7863EA;;
}
.txtlength{
	width: 55%;
}
</style>


</head>
<body style="width: 100%;">
<section id="widget-grid" >
<div class="row-fluid">

		<!-- NEW COL START -->
		<div class="col-sm-12 col-md-12 col-lg-12">

			<!-- Widget ID (each widget will need unique ID)-->
			<div class="jarviswidget" id="wid-id-0" data-widget-colorbutton="false" data-widget-editbutton="false" data-widget-custombutton="false">
				
				

				<!-- widget div-->
				<div>

					<!-- widget edit box -->
					<div class="jarviswidget-editbox">
						<!-- This area used as dropdown edit box -->

					</div>
					<!-- end widget edit box -->

					<!-- widget content -->
					<div class="widget-body no-padding " onselectstart="return false" >
						<section style="padding-top: 35px;background-color: #EAE4EF;padding-bottom: 5px;">
						<form class="form-inline line"  role="form" style="margin-left: 25px;" action="${pageContext.request.contextPath }/batchStationCutover/getBatchStationInfos" method="post">
									<table>
									<tr>
									<td>
										<div class="form-group">
											<label class="sr-only" for="inputTime" style="margin-right: 5px;margin-left: 15px">开始时间</label>
											<span class="controls input-append date form_date" style="margin-left: 0px;"data-date-format="yyyy-mm-dd">
											<input type="text" id="starttime" name="startTime" value="${requestScope.startTime }" style="width: 100px;cursor: pointer;"  readonly >
											<span class="add-on"><i class="icon-th"></i></span>
											</span>
										</div>
									</td>
									
									<td>
										<div class="form-group">
											<label class="sr-only" for="inputTime" style="margin-right: 5px;margin-left: 15px">结束时间</label>
											<span class="controls input-append date form_date" style="margin-left: 0px;"data-date-format="yyyy-mm-dd">
											<input type="text" id="endtime" name="endTime" value="${requestScope.endTime }" style="width: 100px;cursor: pointer;"  readonly >
											<span class="add-on"><i class="icon-th"></i></span>
											</span>
										</div>
									</td>
										
										<td>
										<div class="form-group">
											<label class="sr-only" for="cpeno" for="user_status" style="margin-left: 15px;">是否参数重置 </label>
											<select class="form-control input-sm txtlength" style="width: 100px;"  name="resetFlag" id="user_status" placeholder="业务状态">	
												<c:if test="${requestScope.resetFlag!=0||requestScope.resetFlag!=1 }"><option value='2' selected>全部</option></c:if>
												
												<c:if test="${requestScope.resetFlag==0 }"><option value='0' selected>未重置</option></c:if>
												<c:if test="${requestScope.resetFlag!=0 }"><option value='0' >未重置</option></c:if>
												<c:if test="${requestScope.resetFlag==1 }"><option value='1' selected>已重置</option></c:if>
												<c:if test="${requestScope.resetFlag!=1 }"><option value='1' >已重置</option></c:if>
											</select>										
										</div>
									</td>
										
									<td>
										<div class="form-group" >
											<input id="searchInstBox2" class="btn btn-primary btn-sm mybtn" style="margin-left:10px;" type="submit" value="查询" >
										</div>
									</td>
									
									<td>
										<div class="form-group" >
											<input id="resetUsrlvalue" class="btn btn-primary btn-sm mybtn" style="margin-left:10px;" type="button" value="参数重置" >
										</div>
									</td>
									
									<td>
										<div class="form-group">
											<input id="openfileImport" class="btn btn-primary btn-sm mybtn" style="margin-left:10px;" type="button" value="文件导入" >
										</div>
									</td>
									</tr>
									</table>																																																			 													
						</form>
						</section>
						<section  style="background-color:#E9DFF1;">
							<div class="marigins" style="overflow-x: scroll;">
							<table class="table table-bordered" style="background-color: white; margin-bottom: 0px;">
							<thead style="background-color: #DACAE8;">
								<tr>
							<!-- 	<td style=" box-shadow: 0px 4px 0px #9E88BD;"></td> -->
								<td style=" padding: 0px;background-color:#9E88BD;border-top: #9E88BD;"><div class="firstd" > <input type="checkbox"  style="float: right;margin-right: 20%"  id="selectAll"> </div></td>
							<!-- 	<td></td> -->
								<td>CPE号码</td>
								<td>手机号码</td>
								<td>原小区usrloction</td>
								<td>新小区usrloction</td>
								<td>失效时间</td>
								<td>导入时间</td>
								<td>是否参数重置</td>
								</tr>
							</thead>
							<tbody>
							   
					<c:if test="${!empty requestScope.batchStationlist }">
							<c:forEach items="${requestScope.batchStationlist }" var="batchStationBean" varStatus="s">
								<tr>
								<td style=" padding: 0px;background-color:#9E88BD;border-top: #9E88BD;"><div class="firstd" > <input type="checkbox"  style="float: right;margin-right: 20%" value='${batchStationBean.subsid }+${batchStationBean.productNo }+${batchStationBean.userlocationOld }+${batchStationBean.userlocationNew }+${batchStationBean.lac_ci_hex_code_old }+${batchStationBean.lac_ci_hex_code_new }'> </div></td>
								<%-- <td>${s.count }</td> --%>
								<td>${batchStationBean.subsid }</td>
								<td>${batchStationBean.productNo }</td>
								<td>${batchStationBean.userlocationOld }</td>
								<td>${batchStationBean.userlocationNew }</td>
								<td>${batchStationBean.cutoverTime }</td>
								<td>${batchStationBean.importTime }</td>
								<c:if test="${batchStationBean.resetFlag==0 }"><td>未重置</td></c:if>
								<c:if test="${batchStationBean.resetFlag==1 }"><td>已重置</td></c:if>
								</tr>
							</c:forEach>
					</c:if>	
					
							</tbody>
							<tfoot>
								<tr>
								<td style=" padding: 0px;background-color:#9E88BD;border-top: #9E88BD;"><div class="firstd" > &nbsp;</div></td>
								<!-- <td></td> -->
								<td></td>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
								
								</tr>
								<tr>
								<td style=" padding: 0px;background-color:#9E88BD;border-top: #9E88BD;"><div class="firstd">&nbsp; </div></td>
								<!-- <td></td> -->
								<td></td>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
								
								</tr>
				
							</tfoot>
							</table>
							</div>
						</section>
						
					</div>
					<!-- end widget content -->
					<!--begin import file-->
					
					<div class="widget-body no-padding " style="width:95%; left:110px;display: none; position: absolute; top:50px;" id="importfilediv">
						<section id="importfileheader">
							<header  style="background-image:url(${pageContext.request.contextPath }/static/img/my/1.png);border-radius: 4px;height: 30px;">基站割接信息导入<span style="float: right;margin-top: 4px;margin-right: 1px;"><input type="image" src="${pageContext.request.contextPath }/static/img/my/2.png"/ id="closeImport"></span></header>
						</section>
						<section style="padding-top: 35px;background-color: #EAE4EF;padding-bottom: 5px;">
						<form class="form-inline line"  role="form" style="margin-left: 25px;" >
									<table>
									<tr>
									<td>
										<div class="form-group">
											<label class="sr-only" for="inputTime" style="margin-right: 10px;margin-left: 15px">导入基站割接信息</label>
											<!-- <input id="fileToUpload" type="file" class="file"  size="24" name="file" onchange="document.getElementById('fileToUploadtxt').value=getPath(this)">-->
											<!-- <input type="text" id="fileToUploadtxt" name="fileToUploadtxt" style="width: 160px;cursor: pointer;"  > -->
											<!-- <a id="searchInstBox" class="btn btn-primary btn-sm" href="javascript:void(0);">文件预览</a>  -->
									
										</div>
									</td>
									<td>	<div id="fileQueue"></div></td>
										<td><input id="file_upload" name="file_upload" type="file"
											value="文件预览" />
											<!-- 	<div id="fileQueue"></div> -->
										</td>
									<td>
										<div class="form-group" style="margin-left: 20px">
											
										</div>
									</td>
									
									<td>
										<input id="btn_submit" class="btn btn-primary mybtn" type="button" value="开始上传" /> &nbsp;
										<input id="down_submit" class="btn btn-primary mybtn" type="button" value="模板下载" /> &nbsp;
									<!-- 	<input id="btn_cancel" class="btn btn-primary" type="button" value="取消所有上传" /> &nbsp; -->
									</td>
									</tr>
									</table>																																																			 													
						</form>
						</section>
						<section  style="background-color:#E9DFF1;">
							<div class="marigins" style="overflow-x: scroll;">
							<table class="table table-bordered" style="background-color: white; margin-bottom: 0px;" id="importtable">
							<thead style="background-color: #DACAE8;" >
								<tr>																
								<td style=" box-shadow: 0px 4px 0px #9E88BD;">所属地市</td>
								<td>所属县市</td>
								<td>原enodebid</td>
								<td>新enodebid</td>
								<td>原基站名称</td>
								<td>新基站名称</td>
								<td>原小区名称</td>
								<td>新小区名称</td>
								<td>原LAC(十进制)</td>
								<td>原LAC(十六进制)</td>
								<td>原CI(十进制)</td>
								<td>原CI(十六进制)</td>
								<td>新LAC(十进制)</td>
								<td>新LAC(十六进制)</td>
								<td>新CI(十进制)</td>
								<td>新CI(十六进制)</td>
								<td>割接时间</td>
								</tr>
							</thead>

						

							<tbody id="inputStation">

							</tbody>
							<tfoot>
								<tr>
								<td class="mytd"></td>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
								</tr>
								<tr>
								<td class="mytd"></td>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
								</tr>
								<tr>
								<td class="mytd"></td>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
								</tr>
							</tfoot>
							</table>
							</div>
							
						</section>
						
					</div>
						
					<!--end import file-->
					</div>
				<div>
				</div>
				<!-- end widget div -->

			</div>
			<!-- end widget -->

		</div>
		<!-- END COL -->

	</div>

	<!-- row -->
	

</section>
	
<script type="text/javascript">

	function cpeUpdateLoction(){
		var cks=$("input[type='checkbox']:gt(0):checked");
		if(chs.length>0){
			return true;
		}else{
			alert("请勾选");
			return false;
		}
	}
	
	$("#resetUsrlvalue").click(function(){
		var datas;
		var array=new Array();
		var cks=$("input[type='checkbox']:gt(0):checked");
		for(var i=0;i<cks.length;i++){
			var cur=$(cks.get(i));
			console.log("ck's value:"+cur.val());
			array.push(cur.val());
			var ids=array.join(":");
		}
		if(cks.length>0){
					aj = $.ajax( {    
						url:'${pageContext.request.contextPath }/batchStationCutover/batchStationCutover',
					    data:{ids:ids},
					    type:'post',    
					    cache:false,    
					    dataType:'json',    
					    success:function(jsonData) {    
					    	var result=eval(jsonData);
					    	alert(result.msg);
					    	var startTime=$("#starttime").val();
					    	var endTime=$("#endtime").val();
					    	var resetFlag=$("#user_status").val();
					    	location.href="${pageContext.request.contextPath }/batchStationCutover/getBatchStationInfos?startTime="+startTime+"&endTime="+endTime+"&resetFlag="+resetFlag;
					        if(data.msg =="true" ){
					        	$(".smart-form #d_employeeid").val("");
					        	alert('该员工编号已存在请重新输入！');
					        }else{    
					        	alert(data.msg);    
					        }    
					     },    
					     error : function() {    
					          alert("操作异常！");    
					     }    
					});
		}
	});
   	//查询按钮
	function getBatchStationList(){
		var startTime=$("#starttime").val();
		var endTime=$("#endtime").val();
		var resetFlag=$("#user_status").find("option:selected").val();
		
		$.ajax({
   		   type:"post",
   		   url:'${pageContext.request.contextPath }/batchStationCutover/getBatchStationInfos',
   		   data:{
   			startTime:startTime,
   			endTime:endTime,
   			resetFlag:resetFlag
   		   },
   		 async:false,
			dataType:"json",
			success:function(jsonData){
			//	alert("1111");
				var	js=eval(jsonData);
				 var batchStationlist=js.batchStationlist;
					for(var i=0;i<batchStationlist.length;i++){
						alert(batchStationlist[i].subsid);
					} 
			}
   	   });
		
	}
	$().ready(function(){
	
	$('.form_date').datetimepicker({
        language:  'zh-CN',
        weekStart: 1,
        todayBtn:  1,
		autoclose: 1,
		todayHighlight: 1,
		startView: 2,
		minView: 2,
		forceParse: 0,
		format : "yyyy-mm-dd"
    });
	$("#openfileImport").click(function() {
			$("#inputStation").empty();
			var light=document.getElementById("importfilediv");
			light.style.display='';
			light.style.top='35px';
			light.style.left='30px';
	});
	$("#importfilebtn").click(function(){
		ajaxFileUpload();
	});
	$("#closeImport").click(function(){
		var light=document.getElementById("importfilediv");
			light.style.display='none';
	});

		var clicked = "Nope.";
        var mausx = "0";
        var mausy = "0";
        var winx = "0";
        var winy = "0";
        var difx = mausx - winx;
        var dify = mausy - winy;

        $("html").mousemove(function (event) {
            mausx = event.pageX;
            mausy = event.pageY;
            winx = $("#importfilediv").offset().left;
            winy = $("#importfilediv").offset().top;
            if (clicked == "Nope.") {
                difx = mausx - winx;
                dify = mausy - winy;
            }

            var newx = event.pageX - difx - $("#importfilediv").css("marginLeft").replace('px', '');
            var newy = event.pageY - dify - $("#importfilediv").css("marginTop").replace('px', '');
            $("#importfilediv").css({ top: newy, left: newx });

        });

        $("#importfileheader").mousedown(function (event) {
            clicked = "Yeah.";
        });

        $("html").mouseup(function (event) {

            clicked = "Nope.";
        });
	
 
    	
    	//全选或全不选
    	var selectAll = $("#selectAll");
    	selectAll.click(function(){
    		 var bn=selectAll.prop("checked");
    		// alert(bn);
    		 $("input[type='checkbox']:gt(0)").prop("checked",bn);
    	});

    	
    		$("#btn_submit").click(function(){
    			var flag=true;
    			 if(flag){
    				 $('#file_upload').uploadify('upload','*');
    			 }
    		});
    		/* $("#btn_cancel").click(function(){
    				 $('#file_upload').uploadify('cancel', '*');
    		}); */
    		//文件上传
    		$('#file_upload').uploadify({
    			'height':23,
    			'width':75,
    			'swf' : '${ctx}/static/assets/uploadify/uploadify.swf',
    			'buttonText': '文件浏览',
    			'fileTypeExts': '*.xlsx',
    			'queueID': 'fileQueue',
    			'auto': false,
    			'multi': false,
    			'fileSizeLimit' : '50MB',
    			'uploader':'${ctx}/batchStationCutover/uploadBatchStationExcle',
    			'formData':{'plocyid':'','manufacturers':''},
    			'onUploadStart':function(file){
    			   $("#file_upload").uploadify("settings", "formData", {'plocyid':'','manufacturers':''});  
    			},
    			'onUploadComplete':function(file){
    				
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

    				var result=eval('(' + data + ')');

    				if(result.flag!='1'){
    					alert(result.message);
    				}
    				if(result.flag=='1'){
    					$("#inputStation").empty();
    					alert(result.message);
    					var station = result.stationExcel;
    					 var html ='';
    			    	 for(var i = 0;i<station.length;i++){
    			    		 var cell = station[i];
    			    		 html = html+"<tr>";
    			    		 if(cell.city_name=='null'){
    			    			 html+="<td class='mytd'></td>";
    			    		 }else{
    			    			 html+="<td class='mytd'>"+cell.city_name+"</td>";
    			    		 }
    			    		 if(cell.county_name=='null'){
    			    			 html+="<td></td>";
    			    		 }else{
    			    		 	  html+="<td>"+cell.county_name+"</td>";
    			    		 }
    			    		 	html  +="<td>"+cell.enodeid_old+"</td>"
    			    		 	  +"<td>"+cell.enodeid_new+"</td>";
    			    		 if(cell.enodeName_old=='null'){
    			    			 html +="<td></td>";
    			    		 }else{
    			    		 	 html +="<td>"+cell.enodeName_old+"</td>";
    			    		 }
    			    		 if(cell.enodeName_new=='null'){
    			    			 html +="<td></td>";
    			    		 }else{
    			    		 	 html +="<td>"+cell.enodeName_new+"</td>";
    			    		 }
    			    		 if(cell.cell_name_old=='null'){
    			    			 html +="<td></td>";
    			    		 }else{
    			    		 	 html +="<td>"+cell.cell_name_old+"</td>";
    			    		 }
    			    		 if(cell.cell_name_new=='null'){
    			    			 html +="<td></td>";
    			    		 }else{
   			    		 	     html +="<td>"+cell.cell_name_new+"</td>";
    			    		 }
   			    		 	 html +="<td>"+cell.lac_dec_id_old+"</td>"
			    		 		  +"<td>"+cell.lac_hex_code_old+"</td>"
			    		 		  +"<td>"+cell.ci_dec_id_old+"</td>"
			    		 		  +"<td>"+cell.ci_hex_code_old+"</td>"
			    		 		  +"<td>"+cell.lac_dec_id_new+"</td>"
			    		 		  +"<td>"+cell.lac_hex_code_new+"</td>"
			    		 		  +"<td>"+cell.ci_dec_id_new+"</td>"
			    		 		  +"<td>"+cell.ci_hex_code_new+"</td>"
			    		 		  +"<td>"+cell.chg_date+"</td>"
    			    		 	  +"</tr>";
    			    	 }
    			    	 $("#inputStation").html(html);
    					
    				}else{
    					alert(result.message);
    				} 
    				

    			}
    			
    		});
    		$("#down_submit").click(function(){
    			location.href="${pageContext.request.contextPath }/batchStationCutover/downLoadExcel";
    		});
    		
    	});
	</script>

</body>
</html>