<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<html>
<head>
<title>选择显示字段</title>
<%@ include file="/pages/public.jsp"%>
</head>
<script language="JavaScript" type="text/JavaScript">

	//提交到后端
	function selectFieldsAndSubmit(obj){
		var orderNoo = '<%=orderNo%>';
		console.log(orderNoo);
		console.log(JSON.stringify($("#fieldForm").serialize()));
		var url ='<%=basePath%>'+"pages/account/queryOrder.jsp?orderNo=890809";  
		
// 		location.href=url;
		$.ajax({  
	         type: "POST",
	         url: url,  
	         data:"orderNo=890809",
	         success: function(jsonObj){ 
	        	close();
	 		}
	    }); 
		
		
// 		load(url, { orderNo: orderNoo},
// 				  function(data){
// 					close();
// 				  });
	}
	
	//全选
	function allSelect(obj){
		$("input[type='checkbox']").each(function(i,content){
			$(content).attr("checked",true);
		});
	}
	
	//反选
	function antiSelect(obj){
		$("input[type='checkbox']").each(function(i,content){
			$(content).attr("checked",!this.checked); 
		});
	}
</script>
<body>
	<form id="fieldForm">
		<input type="checkbox" name="field" value="sex1" /> sex1
		<input type="checkbox" name="field" value="sex2" /> sex2
		<input type="checkbox" name="field" value="sex3" /> sex3
		<input type="checkbox" name="field" value="sex4" /> sex4
		<input type="checkbox" name="field" value="sex5" /> sex5
		<input type="checkbox" name="field" value="sex6" /> sex6
		<br><br><br><br>
		<input type="button" value="全选" onclick="allSelect(this)" />
		<input type="button" value="反选" onclick="antiSelect(this)" />
		<input type="button" value="确定" onclick="selectFieldsAndSubmit(this)" />
	</form>
</body>
</html>