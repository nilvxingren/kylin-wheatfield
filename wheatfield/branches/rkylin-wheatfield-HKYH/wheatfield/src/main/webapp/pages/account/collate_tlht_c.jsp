<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %> 
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
String result = String.valueOf(request.getAttribute("result"));
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
 <base href="<%=basePath%>">
<title>系统提示</title>

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
<script language="javascript" type="text/javascript" src="/wheatfield/js/DatePicker/WdatePicker.js"></script>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
<script language="JavaScript" type="text/JavaScript">
	function submitHtCollForm(butt) {
		if(document.getElementById("invoicedate").value == "") {
			document.getElementById("valiSpan").innerHTML = "请输入账期";
			return;
		}
		
		if(!window.confirm("您正在触发 '"+ butt.value +"' 方法, 确定要执行吗?")) {
			return;
		}
		
		var form = document.getElementById("htCollForm");
		butt.disabled = true;
		
		if(butt.id == "readCollateFileHT") {
			butt.value="上游对账中 ... ...";
			form.action = "<%=path %>/readCollateFileHT";
		} else if(butt.id == "createDebtAccountFileHT") {
			butt.value="文件发送中 ... ...";
			form.action = "<%=path %>/createDebtAccountFileHT";
		} else {
			window.alert("您的操作有误!");
			return;
		}
		
		form.submit();
	}
</script>
</head>
<body>
	<form action="javascript:void(0)" method="post" id="htCollForm">
		<table>
			<tr>
	          	<td>
	          		<h3>会唐 <i>上游对账 & 发送下游对账文件</i></h3>
	          		
	          		账期:
	          		<input type="text" id="invoicedate"
						name="invoicedate" value="${invoicedate}"
						onFocus="WdatePicker({dateFmt:'yyyy-MM-dd'})" class="Wdate"
						readonly title="前次操作账期:${invoicedate}" />
						
					<small>
						<span class="red" id="valiSpan">
							
						</span>
					</small>
	          		<hr>
	          		
	          		<input type="button" value="上游对账" id="readCollateFileHT" onclick="submitHtCollForm(this)" />
	          		<input type="button" value="发送下游对账文件" id="createDebtAccountFileHT" onclick="submitHtCollForm(this)" />
	          		<%-- if(!"success".equals(result)){%>disabled<%} --%>
	          	</td>
			</tr>
			
			<tr>
	           <td class="red">${errCode} ${errMsg}</td>
			</tr>
		</table>
	</form>
</body>
</html>

