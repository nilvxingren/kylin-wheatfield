<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!-- http://localhost:8080/wheatfield/pages/account/batchSubmitToRecAndPay.jsp -->
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
 <base href="<%=basePath%>">
<title>手动提交一批到代收付系统</title>
<script language="javascript" type="text/javascript" src="/wheatfield/js/jquery-1.8.3.min.js"></script>
<script language="JavaScript" type="text/JavaScript">
	
	function submitToRecAndPay(obj){
		var batch = $("#batch").val();
		if (batch==null || batch=='') {
			alert('批次号不能为空！');
			return;
		}
		if(!confirm("您确定执行吗?")){
			return;
		}
		 $.ajax({  
	         type: "POST",      
	         url: '<%=basePath%>'+'submitToRecAndPay',  
	         dataType:'json',
	         data:"batch="+batch,
	         success: function(jsonObj){ 
	        	 console.log('<%=basePath%>'+'submitToRecAndPay');
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
	          		批次号：<input type="text" id='batch' name="batch">
	          		<input type="button" value="确定" onclick="submitToRecAndPay(this)" />
	          	</td>
			</tr>
		</table>
	</form>
</body>
</html>

