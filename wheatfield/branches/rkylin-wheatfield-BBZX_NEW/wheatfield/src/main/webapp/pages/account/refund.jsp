<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %> 
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
 <base href="<%=basePath%>">
<title>退票</title>

<style type="text/css">
body {
	height: 388px;
}

.boder {
	border: #35bded 1px solid;
}

.boder1 {
	border: #fbd44b 1px solid;
}

.left1 {
	font-size: 15px;
}

.shadow {
	FILTER: dropshadow(color =   #d0cece, offx =   1, offy =   1, positive =   1);
}

.font1 {
	font-size: 18px;
}

.font2 {
	font-size: 14px;
	color: #FFFFFF;
}

.font3 {
	font-size: 18px;
	color: #923e01;
	font-weight: bold;
}

.font31 {
	font-size: 14px;
	color: #923e01;
	font-weight: bold;
}

.font4 {
	color: #923e01;
	font-size: 12px;
}

.font5 {
	color: #0a6092;
	font-size: 14px;
	font-weight: bold;
	background-color: #e9edf3;
}

.red {
	color: red;
}

.biao td {
	border: 1px solid #d3d3e6;
	border-width: 0 1px 1px 0;
	margin: 2px 0 2px 0;
	text-align: center;
	height: 30px;
}

.boxhead {
	font-family: Verdana, Arial, Helvetica, sans-serif;
	font-size: 10pt;
	font-weight: bold;
	text-decoration: none;
	color: #888888;
	background: #EEFFEE;
	padding-top: 3px;
	padding-bottom: 3px;
	padding-right: 0px;
	padding-left: 0px;
}
.sticky-wrap {
    overflow-x: auto; /* Allows wide tables to overflow its containing parent */
    position: relative;
    margin: 0 auto 1.5em;
}
a{
	text-decoration:none;
}
#valTable tr{ background: #F0FFF0;}
#valTable tr:nth-child(2n){ background: #FFFFFF;}
#valTable tr{ background-color: expression((this.sectionRowIndex % 2 == 0) ?   "#F0FFF0" : "#FFFFFF" );} 

</style>
<script language="javascript" type="text/javascript" src="/wheatfield/js/jquery-1.8.3.min.js"></script>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
<script language="JavaScript" type="text/JavaScript">
	
	function submitRefund(obj){
		var orderNo = $("#orderNo").val();
		var insCode = $("#insCode").val();
		if (orderNo==null || orderNo=='' ||insCode==null||insCode=='' ) {
			alert('订单号或机构号不能为空！');
			return;
		}
		if(!confirm("您确定退票吗?")){
			return;
		}
		 $.ajax({  
	         type: "POST",      
	         url: '<%=basePath%>'+'handleRefund',  
	         dataType:'json',
	         data:"orderNo="+orderNo+"&insCode="+insCode,
	         success: function(jsonObj){ 
	        	 console.log('<%=basePath%>'+'handleRefund');
	        	 console.log(JSON.stringify(jsonObj));
	        	 if (jsonObj.success=="true") {
					alert("成功！");
				}else{
					alert("失败！");
				}
	 		}
	   }); 
// 		document.getElementById("handleRefundForm").submit();
	}
	
</script>
</head>
<body>
	
	<form method="post">
		<table>
			<tr>
	          	<td>
	          		订单号：<input type="text" id='orderNo' name="orderNo">
	          		机构号：<input type="text" id='insCode' name="insCode">
	          		<input type="button" value="确定" onclick="submitRefund(this)" />
	          	</td>
			</tr>
		</table>
	</form>
</body>
</html>

