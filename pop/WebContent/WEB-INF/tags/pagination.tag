<%@tag pageEncoding="UTF-8"%>
<%@ attribute name="paginationSize" type="java.lang.Integer" required="true"%>
<%@ attribute name="pageNum" type="java.lang.Integer" required="true"%>
<%@ attribute name="totalPage" type="java.lang.Integer" required="true"%>
<%-- <%@ attribute name="searchParams" type="java.lang.String" required="false"%> --%>
<%@ attribute name="totalRow" type="java.lang.Integer" required="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<%
int current =  pageNum;
int begin = Math.max(1, current - paginationSize/2);
int end = Math.min(begin + (paginationSize - 1), totalPage);
// String searchParamsTmp =searchParams;

// request.setAttribute("searchParams", searchParamsTmp);
request.setAttribute("current", current);
request.setAttribute("begin", begin);
request.setAttribute("end", end);
request.setAttribute("totalRow", totalRow);
%>

<div class="pagination" style="width: 100%;float: none;height: 30px;">
	<div style="display: inline-block;float: left;line-height: 30px;">共<span style="margin: 0 2px;">${totalRow}</span>条,当前第<strong  style="margin: 0 2px;color: #22878E;">${current}</strong>页,共<span  style="margin: 0 2px;">${totalPage }</span>页</div>
	<ul style="float: right;">
		 <% if (current > begin){%>
               	<li><a href="?page=1&sortType=${sortType}&${searchParams}">&lt;&lt;</a></li>
                <li><a href="?page=${current-1}&sortType=${sortType}&${searchParams}">&lt;</a></li>
         <%}else{%>
                <li class="disabled"><a href="#">&lt;&lt;</a></li>
                <li class="disabled"><a href="#">&lt;</a></li>
         <%} %>
 
		<c:forEach var="i" begin="${begin}" end="${end}">
            <c:choose>
                <c:when test="${i == current}">
                    <li class="active"><a href="?page=${i}&sortType=${sortType}&${searchParams}">${i}</a></li>
                </c:when>
                <c:otherwise>
                    <li><a href="?page=${i}&sortType=${sortType}&${searchParams}">${i}</a></li>
                </c:otherwise>
            </c:choose>
        </c:forEach>
	  
	  	 <% if (current < end){%>
               	<li><a href="?page=${current+1}&sortType=${sortType}&${searchParams}">&gt;</a></li>
                <li><a href="?page=${totalPage}&sortType=${sortType}&${searchParams}">&gt;&gt;</a></li>
         <%}else{%>
                <li class="disabled"><a href="#">&gt;</a></li>
                <li class="disabled"><a href="#">&gt;&gt;</a></li>
         <%} %>

	</ul>
</div>

