<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<!-- http://localhost:8080/wheatfield/pages/account/summary_submit.jsp -->
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<html>
<head>
<title>汇总</title>
<script language="javascript" type="text/javascript" src="/wheatfield/js/jquery-1.8.3.min.js"></script>
<script language="JavaScript" type="text/JavaScript">
	
	function summarizing(obj){
		if(!confirm("您确定汇总吗?")){
			return;
		}
		 $.ajax({  
	         type: "POST",      
	         url: '<%=basePath%>'+'summarizing',  
	         dataType:'json',
	         data:$("#summarizing").serialize(),
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
	
	<form method="post" id = "summarizing">
		<table>
			<tr>
	          	<td>
	          		功能码：<input type="text" id='funcCode' name="funcCode">
	          		机构号：<input type="text" id='merchantId' name="merchantId">
	          		订单类型：<input type="text" id='orderType' name="orderType">
	          		业务编码：<input type="text" id='bussinessCode' name="bussinessCode">
	          		日期类型：<input type="text" id='dateType' name="dateType">
	          		<input type="button" value="汇总" onclick="summarizing()" />
	          	</td>
			</tr>
		</table>
	</form>

</body>
</html>

