<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!-- http://localhost:8080/wheatfield/pages/account/handle40142.jsp -->
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
 <base href="<%=basePath%>">
<title>处理40142交易</title>
<script language="javascript" type="text/javascript" src="/wheatfield/js/jquery-1.8.3.min.js"></script>
<script language="JavaScript" type="text/JavaScript">
function manageBackFail40142(obj){
	var orderNo = $("#orderNo").val();
	if(!confirm("您确定执行吗?")){
		return;
	}
	 $.ajax({  
         type: "POST",      
         url: '<%=basePath%>'+'manageBackFail40142',  
         dataType:'json',
         data:"orderNo="+orderNo,
         success: function(jsonObj){ 
        	 console.log('<%=basePath%>'+'manageBackFail40142');
        	 console.log(JSON.stringify(jsonObj));
        	 if (jsonObj.success=="true") {
				alert("成功！");
			}else{
				alert(jsonObj.errMsg);
			}
 		}
   }); 
}
	
	function handle40142(obj){
		var accountDate = $("#accountDate").val();
		var verify = $("#verify").val();
		if(!confirm("您确定执行吗?")){
			return;
		}
		 $.ajax({  
	         type: "POST",      
	         url: '<%=basePath%>'+'handle40142',  
	         dataType:'json',
	         data:"accountDate="+accountDate+"&verify="+verify,
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
	汇总的4014代付返回后,调账
	<form method="post">
		<table>
			<tr>
	          	<td>
	          		代付(4014)账期：<input type="text" id='accountDate' name="accountDate">
	          			是否校验资金一致(0不校验,其他都校验)<input type="text" id='verify' name="verify">
	          		<input type="button" value="确定" onclick='handle40142(this)' />
	          	</td>
			</tr>
		</table>
	</form>
	处理转账失败的40142交易(状态为11或5)
	<form method="post">
		<table>
			<tr>
	          	<td>
	          		订单号：<input type="text" id='orderNo' name="orderNo">
	          		<input type="button" value="确定" onclick='manageBackFail40142(this)' />
	          	</td>
			</tr>
		</table>
	</form>	
</body>
</html>

