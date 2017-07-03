<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %> 
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<%
	String a = request.getParameter("a");
	String d1 = "file";
	String dtTitle = "文件日期";
	String statusId = request.getParameter("statusId");
	if (a == null || a.equals("2")) {
		d1 = "result";
		dtTitle = "对账日期";
	} else if (a != null && a.equals("3")) {
		d1 = "day";
		dtTitle = "报表日期";
	} else if (a != null && a.equals("4")) {
		d1 = "month";
		dtTitle = "截止日期";
	} else if (a != null && a.equals("5")) {
		d1 = "zdy";
		dtTitle = "报表日期";
	} else {
		d1 = "file";
	}
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
 <base href="<%=basePath%>">
<title>系统提示</title>

<style type="text/css">
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
<script language="javascript" type="text/javascript"
	src="/wheatfield/js/DatePicker/WdatePicker.js"></script>


<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
<script language="JavaScript" type="text/JavaScript">
	function cfile() {
		if (document.queryFrm.startDate.value == "") {
			alert("日期不能为空!");
			return false;
		}
		if (confirm("确认要生成下游代理平台的对账文件吗")) {
			document.queryFrm.action = "cfile.jsp";
			document.queryFrm.target = "_self";
			document.queryFrm.submit();
		}
	}
	function downLoad(){
		var table = document.getElementById("valTable");//获取第一个表格  
        
        var tr = table.getElementsByTagName("tr");//获取行的第一个单元格 
        if(tr.length ==0){
        	alert("无符合条件的数据");
        	return;
        }else{
        	window.location.href="downColAndPay";
        }
	}
</script>
</head>
<body>
	<form name="queryFrm" action="getColAndPay" target="_self"
		method="post">
		<!-- /settlement/JR_router.jsp -->
		<tr>
			<td align="left">
				<table border="0" width="101%">
					<tr>
						<td width="1%"></td>
						<td>
							<table border="0" width="100%" class="boder ticky-wrap">
								<tr>
									<td width="5%" nowrap align="right"><%=dtTitle%>:</td>
									<td width="24%"><input type="text" id="startDate"
										name="startDate" value="${startDate}"
										onFocus="WdatePicker({dateFmt:'yyyyMMdd'})" class="Wdate"
										style="width:80%" readonly /></td>
									<td nowrap align="right" width="5%">状&nbsp;&nbsp;&nbsp;&nbsp;态：</td>
									<td width="24%"><select name="statusId"
										style="width:172px;" value="">
											<option value="" <%if("".equals(statusId)){%>SELECTED<%}%>>清算 - 全部</option>
											<option value="1" <%if("1".equals(statusId)){%>SELECTED<%}%>>清算 - 完成</option>
											<option value="0" <%if("0".equals(statusId)){%>SELECTED<%}%>>清算 - 失败</option>
									</select>
									</td>
									<td><input type="hidden" name="source" value="<%=d1%>">
										&nbsp;&nbsp; &nbsp;&nbsp; <input type="submit" value="确定">
									</td>
									<td>
										&nbsp;&nbsp; &nbsp;&nbsp; <a href="javascript:downLoad();"><span style="font-size: 13px;">导出报表</span></a>
									</td>
								</tr>
							</table>
						</td>
					</tr>
				</table>
				<div id="titlediv" align="center" style="overflow-x:hidden;overflow-y:hidden;margin-left:0.5%;width:97.5%" >
				<table border="0" align="center" height="20px;" width="200%"
					style="font-size: 11pt;margin-left:1.2%;">
					<tr bgcolor="#EEEEEE" height="30px" style="line-height: 30px;font-weight: bold;">
						<td valign="top" align="center" width="11%">资金清分号</td>
						<td valign="top" align="center" width="9%">管理机构代码号</td>
						<td valign="top" align="center" width="9%">账期</td>
						<td valign="top" align="center" width="7%">批次号</td>
						<td valign="top" align="center" width="9%">用户ID</td>
						<td valign="top" align="center" width="9%">金额</td>
						<td valign="top" align="center" width="9%">结算类型</td>
						<td valign="top" align="center" width="9%">交易状态</td>
						<td valign="top" align="center" width="9%">订单号</td>
						<td valign="top" align="center" width="9%">账户关联ID</td>
						<td valign="top" align="center" width="9%">备注</td>
					</tr>
				</table>
				</div>
				<div id="ddiv" style="overflow-x:scroll;overflow-y:scroll;height:480px;margin-left:0.5%;margin-top:0px;" onscroll="titlediv.scrollLeft=this.scrollLeft;">
				<table id="valTable" border="0" align="center" height="20px;" width="200%"
					style="font-size: 11pt;margin-left:1.2%;">
					<c:forEach items="${getColAndPayList}" var="items">
					<tr height="20px" bgColor="#F0FFF0">
						<td valign="top" align="center" width="11%" >${items.settleId}</td>
						<td valign="top" align="center" width="9%" >${items.rootInstCd}</td>
						<td valign="top" align="center" width="9%" ><fmt:formatDate value="${items.accountDate}" pattern="yyyy-MM-dd" /> </td>
						<td valign="top" align="center" width="7%" >${items.batchId}</td>
						<td valign="top" align="center" width="9%" >${items.userId}</td>
						<td valign="top" align="center" width="9%">${items.amount}</td>
						<td valign="top" align="center" width="9%" >${items.settleType}</td>
						<td valign="top" align="center" width="9%" >
						<c:if test="${items.statusId =='1'}">完成</c:if> 
						<c:if test="${items.statusId =='0'}">失败</c:if> 
						</td>
						<td valign="top" align="center" width="9%" >${items.orderNo}</td>
						<td valign="top" align="center" width="9%" >${items.accountRelateId}</td>
						<td valign="top" align="center" width="9%" >${items.remark}</td>
					</tr>
					</c:forEach>
				</table>
				</div>
			</td>
		</tr>
	</form>
</body>
</html>