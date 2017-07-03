<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<!-- http://localhost:8080/wheatfield/pages/account/handleRecAndPayCachePushed.jsp-->
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<html>
<head>
 <base href="<%=basePath%>">
<title>手动处理代收付推入的数据缓存</title>
<script language="javascript" type="text/javascript" src="/wheatfield/js/jquery-1.8.3.min.js"></script>
<script language="JavaScript" type="text/JavaScript">
	
	function handleRecAndPayCachePushed(obj){
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
	         url: '<%=basePath%>'+'handleRecAndPayCachePushed',  
	         dataType:'json',
	         data:"key="+batch,
	         success: function(jsonObj){ 
	        	 console.log('<%=basePath%>'+'handleRecAndPayCachePushed');
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
	          		<input type="button" value="确定" onclick="handleRecAndPayCachePushed(this)" />
	          	</td>
			</tr>
		</table>
	</form>
</body>
</html>

