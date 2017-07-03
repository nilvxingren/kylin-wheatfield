<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="utf-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
// 	String contextPath = request.getContextPath();
%>
<script type="text/javascript" src="<%=basePath %>js/jquery-1.8.3.min.js"></script>
<script type="text/javascript">
	var basePath = '<%=basePath%>';
// 		contextPath = "http://localhost:8080"+contextPath;
	//console.debug("contextPath============="+contextPath);
</script>
