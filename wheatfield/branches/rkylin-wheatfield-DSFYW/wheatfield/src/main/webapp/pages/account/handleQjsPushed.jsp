<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<!-- http://localhost:8080/wheatfield/pages/account/handleQjsPushed.jsp-->
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<html>
<head>
 <base href="<%=basePath%>">
<title>手动处理清结算推入的数据缓存</title>
<script language="javascript" type="text/javascript" src="/wheatfield/js/jquery-1.8.3.min.js"></script>
<script language="JavaScript" type="text/JavaScript">
	
	function handleQjsCachePushed(){
		var keys = $("#keys").val();
// 		if (keys==null || keys=='') {
// 			alert('key不能为空！');
// 			return;
// 		}
		if(!confirm("您确定执行吗?")){
			return;
		}
		 $.ajax({  
	         type: "POST",      
	         url: '<%=basePath%>'+'handleQjsCachePushed',  
	         dataType:'json',
	         data:"keys="+keys,
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
	
	<form method="post">
		<table>
			<tr>
	          	<td>
	          		key：<input type="text" id='keys' name="keys">
	          		<input type="button" value="确定" onclick="handleQjsCachePushed()" />
	          	</td>
			</tr>
		</table>
	</form>
</body>
</html>

