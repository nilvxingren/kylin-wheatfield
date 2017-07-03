<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<!-- http://localhost:8080/wheatfield/pages/account/realTimeColl.jsp -->
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<html>
<head>
 <base href="<%=basePath%>">
<title>手动查询实时代收结果并处理</title>
<script language="javascript" type="text/javascript" src="/wheatfield/js/jquery-1.8.3.min.js"></script>
<script language="JavaScript" type="text/JavaScript">
	
	function submitToRecAndPay(obj){
		var orderNo = $("#orderNo").val();
		if (orderNo==null || orderNo=='') {
			alert('订单号不能为空！');
			return;
		}
		if(!confirm("您确定执行吗?")){
			return;
		}
		 $.ajax({  
	         type: "POST",      
	         url: '<%=basePath%>'+'queryRealTimeCollTransInfo',  
	         dataType:'json',
	         data:"orderNo="+orderNo,
	         success: function(jsonObj){ 
	        	 console.log('<%=basePath%>'+'queryRealTimeCollTransInfo');
	        	 console.log(JSON.stringify(jsonObj));
	        	 if (jsonObj.success=="true") {
					alert("成功！");
				}else{
					alert(jsonObj.errMsg);
				}
	 		}
	   }); 
	}
	
</script>
</head>
<body>
	
	<form method="post">
		<table>
			<tr>
	          	<td>
	          		订单号：<input type="text" size="60" id='orderNo' name="orderNo">
	          		<input type="button" value="确定" onclick="submitToRecAndPay(this)" />(可以输入多个用英文逗号隔开)
	          	</td>
			</tr>
		</table>
	</form>
</body>
</html>

