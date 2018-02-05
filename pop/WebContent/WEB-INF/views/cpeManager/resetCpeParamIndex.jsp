<%@ page language="java" contentType="text/html; charset=utf-8"  pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ page import="java.util.*"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!doctype html>
<html>
<head>
<title>参数重置-单用户</title>
<meta charset="utf-8" />
<meta http-equiv="Content-Type" content="text/html;charset=utf-8" />
<meta http-equiv="Cache-Control" content="no-store" />
<meta http-equiv="Pragma" content="no-cache" />
<meta http-equiv="Expires" content="0" />
<meta content="width=device-width, initial-scale=1.0" name="viewport" />
<link href=" ${pageContext.request.contextPath }/static/assets/jqueryUI/themes/base/jquery-ui.css" rel="stylesheet" type="text/css" />
<link href=" ${pageContext.request.contextPath }/static/assets/bootstrap/css/bootstrap.min.css" rel="stylesheet" type="text/css" />
<link href=" ${pageContext.request.contextPath }/static/assets/bootstrap/css/bootstrap-responsive.min.css" rel="stylesheet" type="text/css" />


<script type="text/javascript" src=" ${pageContext.request.contextPath }/static/js/jquery-1.8.3.min.js"></script>
<script type="text/javascript" src=" ${pageContext.request.contextPath }/static/assets/jqueryUI/jquery-ui.js"></script> 
<script type="text/javascript" src=" ${pageContext.request.contextPath }/static/assets/bootstrap/js/bootstrap.min.js"></script> 
<script type="text/javascript" src=" ${pageContext.request.contextPath }/static/assets/bootstrap/js/bootstrap-fileupload.js"></script>
 
<!-- ie8 fixes -->
<!--[if lt IE 9]>
<script src="../js/excanvas.js"></script>
<script src="../js/respond.js"></script>
<![endif]--> 
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
tbody td{
	font-size: 10px;
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

</style>
<script type="text/javascript" src=" ${pageContext.request.contextPath }/static/assets/uniform/jquery.uniform.min.js"></script>
<script type="text/javascript" src=" ${pageContext.request.contextPath }/static/assets/bootstrap-datepicker/js/bootstrap-datepicker_zh_CN.js" charset="UTF-8"></script>   
<script type="text/javascript" src=" ${pageContext.request.contextPath }/static/assets/bootstrap-daterangepicker/date.js"></script>
<script type="text/javascript" src=" ${pageContext.request.contextPath }/static/assets/bootstrap-daterangepicker/daterangepicker.js"></script> 
<script type="text/javascript" src=" ${pageContext.request.contextPath }/static/assets/bootstrap-datetimepicker-master/js/bootstrap-datetimepicker.js" charset="UTF-8"></script>
<script type="text/javascript" src=" ${pageContext.request.contextPath }/static/assets/bootstrap-datetimepicker-master/js/locales/bootstrap-datetimepicker.zh-CN.js" charset="UTF-8"></script>
<script type="text/javascript" src=" ${pageContext.request.contextPath }/static/assets/lazyscrollloading/jquery.lazyscrollloading-min.js"></script>
<script type="text/javascript" src=" ${pageContext.request.contextPath }/static/assets/ztree/jquery.ztree.all-3.5.js"></script>
<script type="text/javascript" src=" ${pageContext.request.contextPath }/static/assets/ztree/jquery.ztree.exhide-3.5.js"></script>
<script type="text/javascript" src=" ${pageContext.request.contextPath }/static/js/scripts.js"></script>
<script type="text/javascript" src=" ${pageContext.request.contextPath }/static/js/main.js" ></script>
<script type="text/javascript" src=" ${pageContext.request.contextPath }/static/js/json2.js" ></script>
<script src=" ${pageContext.request.contextPath }/static/assets/uploadify/jquery.uploadify.js" type="text/javascript"></script>
<script type="text/javascript" src=" ${pageContext.request.contextPath }/static/assets/treeTable/jquery.treeTable.js" ></script>
<script type="text/javascript" src=" ${pageContext.request.contextPath }/static/assets/jgrowl/jquery.jgrowl.min.js" ></script>
<script>

</script>
</head>
<body style="width: 100%;margin: 10px;">
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
					<div class="widget-body no-padding " >
						<section style="padding-top: 35px;background-color: #EAE4EF;padding-bottom: 5px;">
						<form class="form-inline line"  role="form" action="${ctx}/cpeManager/queryLockedLacCi" style="margin-left: 25px;" onsubmit="return submitMth()">
									<table>
									<tr>
									<td>
										<div class="form-group">
											<label class="sr-only" for="subsid" style="margin-right: 20px;margin-left: 15px">CPE编号</label>
											<%-- <input class="input-sm" type="text" name="subsid" id="subsid" placeholder="CPE编号" value="${subsid}"> --%>
											<input class="input-sm" type="text" name="subsid" id="subsid" placeholder="CPE编号" value="">
										</div>
									</td>
										
									<td>
										<div class="form-group">
											<label class="sr-only"  for="productNo" style="margin-right: 20px;margin-left: 5px">手机号码 </label>
											<input class="input-sm" type="text" name="productNo" id="productNo" value="${productNo}" placeholder="手机号码">										
										</div>
									</td>
									
									<td>
										<div class="form-group" style="margin-left: 30px">
											<input id="searchuser" class="btn btn-primary btn-sm"  type="submit" value="查询">
										</div>
									</td>
									</tr>
									</table>
						</form>
						</section>
						<section style="background-color:#E9DFF1;">
							<div class="marigins" style="overflow-x: scroll;">
							<table class="table table-bordered " style="background-color: white; margin-bottom: 0px;" >
							<thead style="background-color: #DACAE8;" >
								<tr>
								<td style=" box-shadow: 0px 4px 0px #9E88BD;"></td>
								<td>所属地市</td>
								<td>所属县市</td>
								<td>所属乡镇</td>
								<td>所属村庄</td>
								<td>小区</td>
								<td>CPE号码</td>
								<td>手机号码</td>
								<td>锁网状态</td>
								<td>业务状态</td>
								<td>用户状态</td>
								<td>开户日期</td>
								<td>产品类型</td>
								</tr>
							</thead>
							<tbody id="willresertcell" style="font-size: 10px;">
								<c:forEach items="${cpeLockLacCiBeanList}" var="cpeLockLacCi">
									<tr>
									<td style=" padding: 0px;background-color:#9E88BD;border-top: #9E88BD;"><div class="firstd">&nbsp; <br></div></td>
									<td>${cpeLockLacCi.cityName}</td>
									<td>${cpeLockLacCi.countyName}</td>
									<td>${cpeLockLacCi.townName}</td>
									<td>${cpeLockLacCi.countryName}</td>
									<td>${cpeLockLacCi.cellName}</td>
									<td>${cpeLockLacCi.subsid}</td>
									<td>${cpeLockLacCi.productNo}</td>
									<td>${cpeLockLacCi.netLockFlag == 0 ?'未锁网':'' }${cpeLockLacCi.netLockFlag == 2 ? '锁网中（1-8个）':'' }${cpeLockLacCi.netLockFlag == 1 ?'锁网完成（9个）':'' }</td>
									<td>${cpeLockLacCi.busiType == 1 ?'正常':'' }${cpeLockLacCi.busiType == 2 ? '已锁定（在城市基站）':'' }${cpeLockLacCi.busiType == 3 ?'已锁定（在9个农村锁网小区外）':'' }</td>
									<td>${cpeLockLacCi.userStatus==1?'正常':''}${cpeLockLacCi.userStatus==2?'欠费':''}${cpeLockLacCi.userStatus==3?'销号':''}</td>
									<td>${cpeLockLacCi.createTime}</td>
									<td>${cpeLockLacCi.netType}</td>
									</tr>
								</c:forEach>
							</tbody>
							<tfoot>
								<tr>
									<td style=" padding: 0px;background-color:#9E88BD;border-top: #9E88BD;"><div class="firstd" style="height: 24px;margin-bottom: 3px;"> &nbsp; <br></div></td>
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
						<section class="form-inline" style="padding-top: 30px;background-color: #E3DAEA;padding-bottom: 35px;">
							 <div class="form-group" style="display:inline;margin-left: 3%;">
								<label class="sr-only" for="cpeno" >&nbsp;&nbsp;&nbsp;&nbsp;可接入小区列表:</label>
								<input type="hidden" id="cpexqno">
								&nbsp;&nbsp;&nbsp;&nbsp;<input class="form-control input-sm" type="text" name="cpenoname" id="cpexqnoname" placeholder="可接入小区列表" style="width:30%;">
								<input type="button" value="选择小区" style="margin-left: 25px; margin-right: 25px;" id="selectxq">
								<input type="button" value="参数重置" id="resetCellbtn">
							</div>
						</section>
					</div>
					<!-- end widget content -->
					<div class="" style="margin-top:10px;width:80%;display:none;margin-left:5%" id="xqtable">
					
						<div class="container-fluid reset-border1" >
							<div>
							<header style="background-image:url(${pageContext.request.contextPath }/static/img/my/1.png);margin-top: 0px;margin-left: -6px;margin-right: -6px;height: 30px;">参数重置-小区选择<span style="float: right;margin-top: 4px;margin-right: 1px;"><input type="image" src="${pageContext.request.contextPath }/static/img/my/2.png" id="closexqdiv"/></span></header>
							</div>
							<div class="" style="display:-webkit-box">
								<table style="width:100%;">
								<tr>
									<td><input type="text" style="margin-top: 16px;width: 170px;" id="searchcontent"><a id='searchCellbtn' href="javascript:void(0);">搜索</a>&nbsp;&nbsp;&nbsp;&nbsp;<a href="javascript:void(0);" id='clearCellbtn'>清空</a></td>
								</tr>
								<tr>
								<td style="width:60%;">
								<div class="">
									<div class="">
										<div class="" style="height: 300px; overflow: auto;border: 1px solid #C49FE6;">
											<table class="table table-hover table-bordered" id="allxq">
												<thead>
													
												</thead>
												<tbody id="allxqTableBody" style="font-size: 12px;">
													
												</tbody>
											</table>
										</div>
									</div>
								</div>
								</td>
								<td style="width:10%;">
								<div style="padding-left: 5%;">
									<input type="button" value="  添加  " id="addxq"><br/><br/>
									<input type="button" value="  去除  " id="remoexq">
								</div>
								</td>
								<td style="width:30%;">
								<div class="">									
									<div class="row">
										<div class="" style="height: 300px; overflow: auto;border:2px solid;">
											<table class="table table-hover table-bordered">
												<thead>
												<tr>
													
												</tr>
												</thead>
												<tbody id="resetxq" style="font-size: 12px;">
													
												</tbody>
											</table>
										</div>
									</div>
								</div>
								</td>
								</tr>
								<tr>
									<td colspan="3">
									<div style="display: inline-flex; width:100%;margin-top: 10px;margin-bottom: 8px;">
									<div style=' width:50%;display: inline; '><input type="button" style="float:right;margin-right:25px;" value="  确定  " id="addxqok"></div>
									<div style=' width:50%;display: inline;'><input type="button" style="float:left;margin-left:5px;" value="  取消  " id="remoexqtable"></div>
									</div>
									</td>
								</tr>
							</table>
							</div>
						
				
						</div>
					
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

		

		$("#addxq").click(function() {
			var oriH = $("#resetxq").html();
			$("#resetxq").empty();
			var i=0;
			var eid='';
			$("#allxqTableBody tr :checked").each(function(){
				if(i<9){
					var cellno = $(this).val();
					var isExists = false;
					$(oriH).each(function(){
					   if($(this).attr("id")=="reset"+cellno)
						   {
						      isExists = true;
						      return;
						   }
					});
					if(!isExists)
						{
						var cellname = $(this).attr("b_value");
						var city = $(this).attr("b_city");
						var county = $(this).attr("b_county");
						var town = $(this).attr("b_town");
						var country = $(this).attr("b_country")
						eid+="<tr id='reset"+cellno+"'><td><input type='checkbox' value='"+cellno+"'b_city='"+city+"' b_county='"+county+"' b_town='"+town+"' b_country='"+country+"' b_value='"+cellname+"' checked>"+cellname+"</td></tr>";
						}
					}
				i++;
			});
			$("#resetxq").html(oriH+eid);
		
	});
	$("#remoexq").click(function() {
			
			$("#resetxq tr :checked").each(function(){
				
				
					$(this).parent().parent().remove();
					
			});
			
		
	});
	$("#addxqok").click(function() {
			var eid='';
			var eidname=''
			var html = '';
			$("#resetxq tr :checked").each(function(){
				if(eid!=''){
					eid=eid+','+$(this).val();
					eidname=eidname+','+$(this).attr("b_value");
				}else{
					eid=$(this).val();
					eidname=$(this).attr("b_value");
				}
				var cellno = $(this).val();
				var cellname = $(this).attr("b_value");
				var city = $(this).attr("b_city");
				var county = $(this).attr("b_county");
				var town = $(this).attr("b_town");
				var country = $(this).attr("b_country")
				html+="<tr><td style=' padding: 0px;background-color:#9E88BD;border-top: #9E88BD;'><div class='firstd'>&nbsp; <br></div></td><td>"+city+"</td><td>"+county+"</td><td>"+town+"</td><td>"+country+"</td><td>"+cellname+"</td><td>${subsid}</td><td>${productNo}</td><td>限速</td><td>正常</td><td>${createTime}</td><td>${netType}</td></tr>";
			});
			$("#cpexqno").val(eid);
			$("#cpexqnoname").val(eidname);
			if(eid!=''){
				$("#willresertcell").empty();
				$("#willresertcell").html(html);
			}
			var light=document.getElementById("xqtable");
			
			light.style.display='none';
		
		});
	$("#selectxq").click(function() {
			var light=document.getElementById("xqtable");
			searchCellAjax();
			light.style.display='block';
		
	});
	
	$("#closexqdiv").click(function(){
		var light=document.getElementById("xqtable");
			light.style.display='none';
	});
	
	$("#remoexqtable").click(function() {
			var light=document.getElementById("xqtable");
			light.style.display='none';
	});
	
	//查询小区
	
	$("#searchCellbtn").click(function(){
		searchCellAjax();
	});
	
	$("#clearCellbtn").click(function(){
		$("#searchcontent").val("");
		$("#allxqTableBody").empty();
	});
	
	function searchCellAjax()
	{	
		var subsid = '${subsid}';
		var productNo = '${productNo}';
		var searchcontent = $("#searchcontent").val();
		$("#allxqTableBody").empty();
		$(document).ready(function(){
		$.ajax({
	   	  url: "${ctx}/cpeManager/queryAccessibleLacCi"
	     ,data: {subsid:subsid,productNo:productNo,keyword:searchcontent}
	     ,dataType : "json"
	     ,success: function(data){
	    	 var html ='';
	    	 for(var i = 0;i<data.length;i++){
	    		 var cell = data[i];
	    		 if(cell.userLocation == 'null'||cell.userLocation==null||cell.userLocation.length==0)
	    			 continue;
	    		 html = html
	    		 	  + "<tr>"
	    		 	  + "<td><input type='checkbox' value='"+cell.userLocation+"' b_city='"+cell.cityName+"' b_county='"+cell.countyName+"' b_town='"+cell.townName+"' b_country='"+cell.countryName+"' b_value='"+cell.cellName+"'></td>"													
	    		 	  + "<td style='width:50px'>"+cell.cityName+"</td>"
	    		 	  + "<td style='width:60px'>"+cell.countyName+"</td>"
	    		 	  + "<td style='width:90px'>"+cell.townName+"</td>"
	    		 	  + "<td>"+cell.countryName+"</td>"
	    		 	  + "<td>"+cell.cellName+"</td>"
	    		 	  +	"</tr>"
	    	 }
	    	 $("#allxqTableBody").html(html);
	       },
	       error: function(XMLHttpRequest, textStatus, errorThrown) {
	            alert("查询角色错误,错误code:"+XMLHttpRequest.status);
	         }
	      });
	});
}
	$("#resetCellbtn").click(function(){
		var newUsrloctions = $("#cpexqno").val();
		ajaxResetCell(newUsrloctions);
	});
	/* $("#searchuser").click(function(){
		var subsid = $("#subsid").val();
		var productNo =$("#productNo").val();
		if(subsid.length==0&&productNo==0){
			alert('11111');
		}
		
		
	}); */
	
	function submitMth(){
		var subsid = $("#subsid").val();
		var productNo =$("#productNo").val();
		if(subsid.length==0&&productNo==0){
			alert('请填入CPE设备号或手机号');
			return false;
		}
	}
	
	var ajaxResetCell = function (datas){
		var productNo = '${productNo}';
		var subsid = '${subsid}';
		if(productNo == "" && subsid == ""){
			alert("请输入CPE设备号和手机号码！");
		}
		var aj = $.ajax( {    
		    url:'${ctx}/cpeManager/cpeUserReset',// 跳转到 action    
		    data:{userLocations:datas,productNo:productNo,subsid:subsid},    
		    type:'post',    
		    cache:false,    
		    dataType:'json',    
		    success:function(data) {    
		       	alert(data.message);
		     },    
		     error : function(data) {    
		          alert(data.message);    
		     }    
		});
	};
	
	
</script>
</body>
</html>