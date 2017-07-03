<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions"  prefix="fn"%> 

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>绑卡信息列表</title>
<meta http-equiv="pragma" content="no-cache"></meta>
<meta http-equiv="cache-control" content="no-cache"></meta>
<meta http-equiv="expires" content="0"></meta>
<script language="JavaScript" type="text/JavaScript">
	function beforeSubmit(butt) {
		if(document.getElementById("rootInstCd").value.trim() == "") {
			document.getElementById("validateMsg").innerHTML = "请输入机构号";
			return false;
		}
		
		if(document.getElementById("accountName").value.trim() == "") {
			document.getElementById("validateMsg").innerHTML = "请输入用户id";
			return false;
		}
		
		document.getElementById("validateMsg").innerHTML = "";
		
		return true;
	}
	
	function updateStatus(accountId){
		if(confirm("确定要把id为'" + accountId + "'的数据状态改为验证失败？")){
			updateURL = "${basePath}" + "/accountInfo/updateStatusById_" + accountId;
			window.location.href = updateURL;
		}
	}
</script>
</head>
<body>
<h3>绑卡状态查询修改</h3>

	<form action="${basePath}/accountInfo/queryList" method="post" id="accountInfoForm"  onSubmit="return beforeSubmit()">
		<table>
			<tr>
	          	<td>
	          		机构号:
	          		<input type="text" value="${rootInstCd}" id="rootInstCd" name="rootInstCd"/>
	          		
	          		用户id:
	          		<input type="text" value="${accountName}" id="accountName" name="accountName" />
	          		
	          		账户目的: 
	          		<input type="radio" value="1" checked="checked" id="accountPurpose" name="accountPurpose" />结算卡
	          		
	          		状态:
	          		<input type="radio" value="1" checked="checked" id="status" name="status"/>正常
	          		
	          		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	          		
	          		<input align="right" type="submit" value="查询" id="submitForm" onclick="beforeSubmit(this)" />
	          	</td>
			</tr>
		</table>
	</form>
	<div id="validateMsg" style="color:red;"></div>
	<table border="1px" cellpadding="0px" cellspacing="0px"  width="2000px" bordercolor="gray">
		<c:forEach var="accInfo" items="${accList}">
		 <tr>
			<th>账户信息ID</th>
			<th>账户ID</th>
			<th>账户名称</th>
			<th>账户类型ID</th>
			<th>账户目的</th>
			<th>账号</th>
			<th>账户属性</th>
			<th>开户日期</th>
			<th>账号用途</th>
			<th>币种</th>
			<th>状态</th>
			<th>开户行总行</th>
			<th>开户行总行名称</th>
			<th></th>
			<th>开户行支行名称</th>
			<th></th>
			<th></th>
			<th>开户证件类型</th>
			<th>证件号</th>
			<th>账户真实名称</th>
			<th>记录创建时间</th>
			<th>记录更新时间</th>
			<th>操作</th>
		 </tr>
		 <tr>
          	<td>${accInfo.accountId}</td>
          	<td>${accInfo.finAccountId}</td>
          	<td>${accInfo.accountName}</td>
          	<td>${accInfo.accountTypeId}</td>
          	<td>${accInfo.accountPurpose}</td>
          	<td>${accInfo.accountNumber}</td>
          	<td>${accInfo.accountProperty}</td>
          	<td>${accInfo.openAccountDate}</td>
          	<td>${accInfo.openAccountDescription}</td>
          	<td>${accInfo.currency}</td>
          	<td>${accInfo.status}</td>
          	<td>${accInfo.bankHead}</td>
          	<td>${accInfo.bankHeadName}</td>
          	<td>${accInfo.bankBranch}</td>
          	<td>${accInfo.bankBranchName}</td>
          	<td>${accInfo.bankProvince}</td>
          	<td>${accInfo.bankCity}</td>
          	<td>${accInfo.certificateType}</td>
          	<td>${accInfo.certificateNumber}</td>
          	<td>${accInfo.accountRealName}</td>
          	<td><fmt:formatDate value="${accInfo.createdTime}" pattern="yyyy-MM-dd"/></td>
          	<td><fmt:formatDate value="${accInfo.updatedTime}" pattern="yyyy-MM-dd"/></td>
<%-- 	          	<c:if test="${accInfo.status == 1 and accInfo.status != 4}"> --%>
			<c:if test="${accInfo.status == 1}">
          		<td><a href="javascript:void(0)" onclick="updateStatus('${accInfo.accountId}')">验证失败</a></td>
	          	</c:if>
<%-- 	          	<c:if test="${accInfo.status != 1}"> --%>
<!-- 	          		<td><span style="color:#873324;">验证失败</span></td> -->
<%-- 	          	</c:if> --%>
		 </tr>
		</c:forEach>
	</table>
	<div>
		<c:if test="${queryFlag==1 and fn:length(accList)==0}">
			数据库中没有符合条件的数据！
		</c:if>
	</div>
</body>
</html>

