<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %> 
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
String genType = request.getAttribute("genType")==null?"":String.valueOf(request.getAttribute("genType"));
String interfaceName = request.getAttribute("interfaceName")==null?"":String.valueOf(request.getAttribute("interfaceName"));
String dateType = request.getAttribute("dateType")==null?"":String.valueOf(request.getAttribute("dateType"));
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
	function reviewDo() {
		if("<%=interfaceName %>"!="") {
			document.getElementById("<%=interfaceName %>").style.background = "#A8A8A8";
		}
	}

	function submithtGenForm(butt) {
		if(!window.confirm("您正在 触发 '" + butt.value + "' 方法, 确定执行吗? ")) return;
		
		var id = butt.id;
		
		if("uploadFileButt" == id) {
			document.getElementById("interfaceName").value = "uploadFileButt";
		} else if ("downloadFileButt" == id) {
			document.getElementById("interfaceName").value = "downloadFileButt";
		} else {
			window.alert("您的操作有误!");
		}
		
		butt.style.background = "#A8A8A8";
		
		document.getElementById("htGenForm").submit();
	}
</script>
</head>
<body onload="reviewDo();">
	<form name="accountFrm" action="updateCreditAccount" target="_self" method="post">
		<table>
			<!-- /settlement/JR_router.jsp -->
			<tr>
				<td align="left" height="20px">
					<input type="submit" value="代收付更新" onclick="return confirm('是否重新通过代收付信息更新账户信息？')">
				</td>
			</tr>
			<tr>
	           <td class="red">${errCode} ${errMsg}</td>
			</tr>
		</table>
	</form>
	
	<hr />
	
	<form action="<%=path %>/uploadAndDownloadFileHT" method="post" id="htGenForm">
		<table>
			<tr>
	          	<td>
	          		<h3>会唐 <i>代收付结果更新</i></h3>
	          		<select name="genType">
	          			<option value="r" <%if("r".equals(genType)){%>selected<%} %>>代收</option>
	          			<option value="p" <%if("p".equals(genType)){%>selected<%} %>>代付</option>
	          		</select>
	          		
	          		&nbsp;&nbsp;
	          		<b><small>当日汇总</small></b>
	          		<input type="checkbox" value="0" name="dateType" <%if("0".equals(dateType)){%>checked<%} %> />
	          		
	          		<hr>
	          		
	          		<input type="button" value="交易信息汇总 + 代收付文件上传" id="uploadFileButt" onclick="submithtGenForm(this)" />
	          		<input type="button" value="文件读取 + 代收付结果读入" id="downloadFileButt" onclick="submithtGenForm(this)" />
	          		<input type="hidden" value="" name="interfaceName" id="interfaceName" />
	          	</td>
			</tr>
		</table>
	</form>
</body>
</html>

