<%-- <%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ page import="java.util.*,com.ai.bdx.pop.bean.*"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib uri="http://bi.asiainfo.com/taglibs/dic" prefix="dic"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" /> --%>
<%@ page language="java" contentType="text/html; charset=utf-8"  pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ page import="java.util.*"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!doctype html>
<html>
<head>
<title>CPE场景-按单个号码查询</title>
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
						<form class="form-inline line"  role="form" action="${pageContext.request.contextPath }/cpeSelectSubsid/selectSubsid" onsubmit="return checkQuery()" style="margin-left: 25px;">
									<input type =hidden name ='userId' value='${param.userId}' >
									<input type =hidden name ='token' value='${param.token}' >
									<table>
									<tr>
									<td style="width: 20%;">
										<div class="form-group">
											<label class="sr-only" for="subsid" style="margin-left: 15px ;">CPE编号</label>
											<input class="form-control input-sm txtlength" type="text" name="subsid" id="subsid" value="${subsid }" placeholder="CPE编号">
										</div>
									</td>
										
									<td style="width: 20%;">
										<div class="form-group">
											<label class="sr-only"  for="productNo" style="margin-left: 15px;">手机号码 </label>
											<input class="form-control input-sm txtlength" type="text" name="productNo" id="productNo" value="${productNo }" placeholder="手机号码">										
										</div>
									</td>
									
									<td style="width: 20%;">
										<div class="form-group">
											<label class="sr-only"  for="cityName" style="margin-left: 15px;">所属地市 </label>
											<select class="form-control input-sm txtlength"  name="cityName" id="cityName" placeholder="所属地市">	
												<c:if test="${cityName=='0' }"><option value='0' selected></option></c:if>
												<c:if test="${cityName!='0' }"><option value='0' ></option></c:if>
												<c:if test="${cityName == '武汉' }"><option value='武汉' selected>武汉</option></c:if>
												<c:if test="${cityName != '武汉' }"><option value='武汉'>武汉</option></c:if>
												<c:if test="${cityName == '黄石' }"><option value='黄石' selected>黄石</option></c:if>
												<c:if test="${cityName != '黄石' }"><option value='黄石'>黄石</option></c:if>
												<c:if test="${cityName == '鄂州' }"><option value='鄂州' selected>鄂州</option></c:if>
												<c:if test="${cityName != '鄂州' }"><option value='鄂州'>鄂州</option></c:if>
												<c:if test="${cityName == '宜昌' }"><option value='宜昌' selected>宜昌</option></c:if>
												<c:if test="${cityName != '宜昌' }"><option value='宜昌'>宜昌</option></c:if>
												<c:if test="${cityName == '恩施' }"><option value='恩施' selected>恩施</option></c:if>
												<c:if test="${cityName != '恩施' }"><option value='恩施'>恩施</option></c:if>
												<c:if test="${cityName == '十堰' }"><option value='十堰' selected>十堰</option></c:if>
												<c:if test="${cityName != '十堰' }"><option value='十堰'>十堰</option></c:if>
												<c:if test="${cityName == '咸宁' }"><option value='咸宁' selected>咸宁</option></c:if>
												<c:if test="${cityName != '咸宁' }"><option value='咸宁'>咸宁</option></c:if>
												<c:if test="${cityName == '荆州' }"><option value='荆州' selected>荆州</option></c:if>
												<c:if test="${cityName != '荆州' }"><option value='荆州'>荆州</option></c:if>
												<c:if test="${cityName == '荆门' }"><option value='荆门' selected>荆门</option></c:if>
												<c:if test="${cityName != '荆门' }"><option value='荆门'>荆门</option></c:if>
												<c:if test="${cityName == '随州' }"><option value='随州' selected>随州</option></c:if>
												<c:if test="${cityName != '随州' }"><option value='随州'>随州</option></c:if>
												<c:if test="${cityName == '黄冈' }"><option value='黄冈' selected>黄冈</option></c:if>
												<c:if test="${cityName != '黄冈' }"><option value='黄冈'>黄冈</option></c:if>
												<c:if test="${cityName == '孝感' }"><option value='孝感' selected>孝感</option></c:if>
												<c:if test="${cityName != '孝感' }"><option value='孝感'>孝感</option></c:if>
												<c:if test="${cityName == '天门' }"><option value='天门' selected>天门</option></c:if>
												<c:if test="${cityName != '天门' }"><option value='天门'>天门</option></c:if>
												<c:if test="${cityName == '潜江' }"><option value='潜江' selected>潜江</option></c:if>
												<c:if test="${cityName != '潜江' }"><option value='潜江'>潜江</option></c:if>
												<c:if test="${cityName == '襄阳' }"><option value='襄阳' selected>襄阳</option></c:if>
												<c:if test="${cityName != '襄阳' }"><option value='襄阳'>襄阳</option></c:if>
											</select>
										</div>
									</td>
									
									<td style="width: 20%;">
										<div class="form-group">
											<label class="sr-only" for="busiType" style="margin-left: 15px;">业务状态 </label>
											<select class="form-control input-sm txtlength"  name="busiType" id="busiType" placeholder="业务状态">	
<!-- 												<option value='0'>正常</option>
												<option value='1'>限速</option> -->
												<c:if test="${busiType==1 }"><option value='1' selected>正常</option></c:if>
												<c:if test="${busiType!=1 }"><option value='1' >正常</option></c:if>
												<c:if test="${busiType==2 }"><option value='2' selected>限速</option></c:if>
												<c:if test="${busiType!=2 }"><option value='2' >限速</option></c:if>
											</select>										
										</div>
									</td>
									
									<td style="width: 20%;">
										<div class="form-group" style="margin-left: 30px">
											<input id="searchInstBox" class="btn btn-primary btn-sm"  type="submit" value="查询">
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
								<td style=" box-shadow: 0px 4px 0px #9E88BD;"></td>
								<td>所属地市</td>
								<td>所属县市</td>
								<td>所属乡镇</td>
								<td>所属村庄</td>
								
								
								<td>CPE号码</td>
								<td>手机号码</td>
								<td>业务状态</td>
								<td>用户状态</td>
								<td>开户日期</td>
								<td>产品类型</td>
								<td>可接入小区列表</td>
								</tr>
							</thead>
							<tbody id="willresertcell">
								<c:forEach items="${cpeUserInfoList}" var="userlist">
								<tr>
									<td style=" padding: 0px;background-color:#9E88BD;border-top: #9E88BD;"><div class="firstd">&nbsp; <br></div></td>
									<td>${userlist.cityName}</td>
									<td>${userlist.countyName}</td>
									<td>${userlist.townName}</td>
									<td>${userlist.countryName}</td>
									<td>${userlist.subsid}</td>
									<td>${userlist.productNo}</td>
									<td>${userlist.busiType=='1'?'正常':'限速' }</td>
									<td>${userlist.userStatus=='1'?'正常':''}${userlist.userStatus=='2'?'欠费':''}${userlist.userStatus=='3'?'销号':''}</td>
									<td>${userlist.createTime}</td>
									<td>${userlist.netType}</td>
									<td>${userlist.count}</td>
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
						
					</div>
					<!-- end widget content -->
					
					
						
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
function checkQuery(){
	var subsid = $("#subsid").val();
	var productNo =$("#productNo").val();
	if(subsid.length==0&&productNo.length==0){
		alert('请填CPE编号或手机号码');
		return false;
	}
}

</script>
</body>
</html>