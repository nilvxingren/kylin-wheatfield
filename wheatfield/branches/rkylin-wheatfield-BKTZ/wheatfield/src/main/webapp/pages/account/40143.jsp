<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<!-- http://localhost:8080/wheatfield/pages/account/40143.jsp -->
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<html>
<head>
<title>处理40143交易</title>
<script language="javascript" type="text/javascript" src="/wheatfield/js/jquery-1.8.3.min.js"></script>
<script language="JavaScript" type="text/JavaScript">
	function send40143(obj){
		if(!confirm("您确定发送吗?")){
			return;
		}
		 $.ajax({  
	         type: "POST",      
	         url: '<%=basePath%>'+'send40143',  
	         dataType:'json',
	         data:$("#send40143").serialize(),
	         success: function(jsonObj){ 
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
	<form method="post" id="send40143">
		<table>
			<tr>
	          	<td>
	          		账期：<input type="text" id='accountDate' name="accountDate">
	          		发送类型：<input type="text" id='sendType' name="sendType">
	          		<input type="button" value="发送" onclick="send40143()" />
	          	</td>
			</tr>			
		</table>
	</form>
</body>
</html>

