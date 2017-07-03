<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<!-- http://localhost:8080/wheatfield/pages/account/sendToGateRouter.jsp -->
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<html>
<head>
<title>发送多渠道</title>
<script language="javascript" type="text/javascript" src="/wheatfield/js/jquery-1.8.3.min.js"></script>
<script language="JavaScript" type="text/JavaScript">
	function sendToGateRouter(obj){
		if(!confirm("您确定发送吗?")){
			return;
		}
		 $.ajax({  
	         type: "POST",      
	         url: '<%=basePath%>'+'sendToGateRouter',  
	         dataType:'json',
	         data:$("#sendToGateRouter").serialize(),
	         success: function(jsonObj){ 
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
	<form method="post" id="sendToGateRouter">
		<table>
			<tr>
	          	<td>
	          		账期：<input type="text" id='accountDate' name="accountDate">
	          		发送类型：<input type="text" id='sendType' name="sendType">
	          		机构：<input type="text" id='protocol' name="protocol">
	          		<input type="button" value="发送" onclick="sendToGateRouter(this)" />
	          	</td>
			</tr>			
		</table>
	</form>
</body>
</html>

