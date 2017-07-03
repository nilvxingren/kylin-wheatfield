<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<!-- http://localhost:8080/wheatfield/pages/account/queryOrder.jsp -->
<%@ include file="/pages/public.jsp"%>
<script type="text/javascript" src="<%=basePath %>pages/account/js/queryOrder.js"></script>
<html>
<head>
<title>订单查询</title>
</head>
<body>
	<form method="post" id="inputForm">
		<table>
			<tr>
	          	<td>
	          		订单号：<input type="text" id='orderNo' name="orderNo">
	          		<input type="button" value="选择字段" onclick="showCheckBox()" />
	          		<input type="button" value="查询" onclick="submitToQueryOrders()" />
	          	</td>
			</tr>
		</table>
	</form>
	<form id="fieldForm" hidden=true>
		<input type="checkbox" name="field" value="sex1" /> sex1
		<input type="checkbox" name="field" value="sex2" /> sex2
		<input type="checkbox" name="field" value="sex3" /> sex3
		<input type="checkbox" name="field" value="sex4" /> sex4
		<input type="checkbox" name="field" value="sex5" /> sex5
		<input type="checkbox" name="field" value="sex6" /> sex6
		<br>
		<input type="button" value="全选" onclick="allSelect()" />
		<input type="button" value="取消选中" onclick="cancleSelect()" />
		<input type="button" value="完成" onclick="selectFieldsOk()" />
	</form>
	
</body>
</html>
