<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<!-- http://localhost:8080/wheatfield/pages/account/delCache.jsp -->
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<html>
<head>
<title>手动清除代收付推入缓存</title>
<script language="javascript" type="text/javascript" src="/wheatfield/js/jquery-1.8.3.min.js"></script>
<script language="JavaScript" type="text/JavaScript">
	
	function delCache(obj){
		var rootInstCd = $("#rootInstCd").val();
		var date = $("#date").val();
		if (rootInstCd==null || rootInstCd=='' || date==null || date=='') {
			alert('参数不能为空！');
			return;
		}
		if(!confirm("您确定执行吗?（您将删除此机构在该日所有代收付推入的缓存）")){
			return;
		}
		 $.ajax({  
	         type: "POST",      
	         url: '<%=basePath%>'+'deletePushedGenPay',  
	         dataType:'json',
	         data:"rootInstCd="+rootInstCd+"&date="+date,
	         success: function(jsonObj){ 
	        	 console.log('<%=basePath%>'+'deletePushedGenPay');
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
	          		机构号：<input type="text" id='rootInstCd' name="rootInstCd">
	          		日期：<input type="text" id='date' name="date">（格式：yyyyMMss）
	          		<input type="button" value="确定" onclick="delCache(this)" />
	          	</td>
			</tr>
		</table>
	</form>
</body>
</html>

