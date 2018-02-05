<%@ page language="java" contentType="text/html; charset=utf-8"  pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ page import="java.util.*"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<!doctype html>
<html>
<head>
<title>参数重置-批量用户</title>

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
.form-group {
	  margin-bottom: 0px;
	  margin-top: 0px;
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
<script type="text/javascript" src="${pageContext.request.contextPath }/static/assets/uniform/jquery.uniform.min.js"></script>
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
					<div class="widget-body no-padding " >
						<section style="padding-top: 35px;background-color: #EAE4EF;padding-bottom: 5px;">
						<form class="form-inline line"  role="form" action="${pageContext.request.contextPath }/cpeManager/queryBatchResetCpe" onsubmit="return submitMth()" style="margin-left: 25px;">
									<table>
									<tr>
										<td style="width: 20%;">
											<div class="">
												<label class="sr-only" for="netLockFlag" style="margin-left: 5px;">锁网状态</label>
												<select class="form-control input-sm"  name="netLockFlag" id="netLockFlagSelectTag">	
													<option value='-1' ${netLockFlag=='-1'?'selected':''} }></option>
													<option value='0' ${netLockFlag=='0'?'selected':''} }>未锁网</option>
													<option value='2' ${netLockFlag=='2'?'selected':''} }>锁网中</option>
													<option value='1' ${netLockFlag=='1'?'selected':''} }>锁网完成</option>
												</select>
											</div>  
										</td>    
										<td style="width: 20%;">
											<div class="">
												<label class="sr-only" for="busiType" style="margin-left: 5px;">业务状态 </label>
												<select class="form-control input-sm"  name="busiType" id="busiTypeSelectTag">	
													<option value='-1' ${busiType=='-1'?'selected':''} }></option>
													<option value='1' ${busiType=='1'?'selected':''} }>正常</option>
													<option value='2' ${busiType=='2'?'selected':''} }>城市限速</option>
													<option value='3' ${busiType=='3'?'selected':''} }>农村限速</option>
												</select>
											</div>
										</td>
										<td style="width: 20%;">
											<div class="" style="margin-left: 5px">
												<label class="sr-only" style="margin-left: 5px;"> </label>
												<input id="searchBystation" class="btn btn-primary btn-sm"  type="submit" value="查询">
											</div>
										</td>
									</tr>
									</table>																																																			 													
						</form>
						</section>
						<section style="background-color:#E9DFF1;">
							<div class="marigins" style="overflow: scroll;">
							<table class="table table-bordered " style="background-color: white; margin-bottom: 0px;" >
							<thead style="background-color: #DACAE8;" >
								<tr>
								<td style=" box-shadow: 0px 4px 0px #9E88BD;width: 2%;"></td>
								<td>锁网状态</td>
								<td>业务状态</td>
								<td>用户个数</td>
								</tr>
							</thead>
							<tbody id="willresertcell">
							<script type="text/javascript">
							  var ns = ["未锁网","锁网完成","锁网中"];
							  var bs = ["正常","城市限速","农村限速"];
							</script>
							<c:forEach items="${dimCpeStationList}" var="stationlist">
								<tr>
									<td style=" padding: 0px;background-color:#9E88BD;border-top: #9E88BD;width: 2%;"><div class="firstd">&nbsp; <br></div></td>
									<td><script>document.write(ns[${stationlist.NET_LOCK_FLAG}])</script></td>
									<td><script>document.write(bs[${stationlist.BUSI_TYPE-1}])</script></td>
									<td>${stationlist["count(1)"]}</td>
								</tr>
								</c:forEach>
							</tbody>
							<tfoot>
								<tr>
									<td style=" padding: 0px;background-color:#9E88BD;border-top: #9E88BD;width: 2%;"><div class="firstd" style="height: 24px;margin-bottom: 0px;"> &nbsp; <br></div></td>
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
					<form name="resetForm" method="post" action="${pageContext.request.contextPath }/cpeManager/resetAllCpeByFlag"  >
					<input type="hidden" name="netLockFlag" />
					<input type="hidden" name="busiType" />
					<section class="form-inline" style="padding-top: 30px;background-color: #E3DAEA;padding-bottom: 35px;">
							 <div class="form-group" style="display:inline;margin-left: 3%;">
								<label class="sr-only" for="cpeno" >&nbsp;&nbsp;&nbsp;&nbsp;重置数量:</label>
								<input id="lineLimit" name =lineLimit value=${lineLimit}>
								<input type="button" id="resetNumCPEBtn" value="参数重置"/>
							</div>
				   </section>	
				   </form>
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

function submitMth(){
	var stationName = $("#stationName").val();
	var cityName =$("#cityName").val();
	if(stationName.length==0&&cityName==0){
		alert('请填基站名称或城市名称');
		return false;
	}
}
function resetAll()
{
	resetForm.netLockFlag.value = $("#netLockFlag").val();
	resetForm.busiType.value = $("#busiType").val();
	resetForm.submit();
}

$("#resetNumCPEBtn").click(function(){
	var netLockFlag = $("#netLockFlagSelectTag").val();
	var busiType = $("#busiTypeSelectTag").val();
	var lineLimit = $("#lineLimit").val();
	if((netLockFlag == "-1" || netLockFlag == -1) && (busiType == -1 || busiType == "-1")){
		alert("请选择锁网状态和业务状态！");
	}
	var aj = $.ajax( {    
	    url:'${ctx}/cpeManager/resetAllCpeByFlag',// 跳转到 action    
	    data:{"netLockFlag":netLockFlag,"busiType":busiType,"lineLimit":lineLimit},    
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
});
</script>
</body>
</html>
