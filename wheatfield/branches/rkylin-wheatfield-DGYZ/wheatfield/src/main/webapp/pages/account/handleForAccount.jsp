<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<!-- http://localhost:8080/wheatfield/pages/account/handleForAccount.jsp -->
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<html>
<head>
 <base href="<%=basePath%>">
<title>手动补账</title>
<script language="javascript" type="text/javascript" src="/wheatfield/js/jquery-1.8.3.min.js"></script>
<script language="JavaScript" type="text/JavaScript">
	
	function handlePushedResults(obj){
		var entryNum = $("#entryNum").val();
		var amount = $("#amount").val();
		var direction = $("#direction").val();
		var productid=$("#productid").val();
		var merchantcode=$("#merchantcode").val();
		if (entryNum==null || entryNum==''|| amount==null|| amount==''|| direction==null || direction==''||productid==null||productid==''||merchantcode==null||merchantcode=='') {
			alert('参数为空！');
			return;
		}
		if(!confirm("您确定执行吗?")){
			return;
		}
		 $.ajax({  
	         type: "POST",      
	         url: '<%=basePath%>'+'semiAuto',  
	         dataType:'json',
	         data:"entryNum="+entryNum+"&amount="+amount+"&direction="+direction+"&productid="+productid+"&merchantcode="+merchantcode,
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
	
	function genStatusUpdate(obj){
		var status = $("#status").val();
		if (status==null || status=='') {
			alert('参数为空！');
			return;
		}
		if(!confirm("您确定执行吗?")){
			return;
		}
		 $.ajax({  
	         type: "POST",      
	         url: '<%=basePath%>'+'queryRealTimeCollTransInfo',  
	         dataType:'json',
	         data:"status="+status,
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
	
	function moveDateSuccess(){
		if(!confirm("您确定执行吗?")){
			return;
		}
		 $.ajax({  
	         type: "POST",      
	         url: '<%=basePath%>'+'paymentAdjustOK',  
	         dataType:'json',
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
	
	function moveDateFailure(){
		if(!confirm("您确定执行吗?")){
			return;
		}
		 $.ajax({  
	         type: "POST",      
	         url: '<%=basePath%>'+'paymentAdjustNG',  
	         dataType:'json',
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
	function bfjSummary(){
		if(!confirm("您确定执行吗?")){
			return;
		}
		 $.ajax({  
	         type: "POST",      
	         url: '<%=basePath%>'+'bfjSummary',  
	         dataType:'json',
	         success: function(jsonObj){ 
	        	 console.log(JSON.stringify(jsonObj));
	        	 if (jsonObj.success=="true") {
					alert("执行完毕！");
				}else{
					alert(jsonObj.errMsg);
				}
	 		}
	   }); 
	}
	
	function updateOrder(){
		if(!confirm("您确定执行吗?")){
			return;
		}
		 $.ajax({  
	         type: "POST",      
	         url: '<%=basePath%>'+'updateOrder',  
	         dataType:'json',
	         success: function(jsonObj){ 
	        	 console.log(JSON.stringify(jsonObj));
	        	 if (jsonObj.success=="true") {
					alert("执行完毕！");
				}else{
					alert(jsonObj.errMsg);
				}
	 		}
	   }); 
	}
</script>
</head>
<body>
	<div>
		<table>
			<tr>
			<td>
				代收付结果读入汇总处理：
				<form method="post">
					<table>
						<tr>
				          	<td>
				          		用户流水号：<input type="text" id='entryNum'/>
				          		金额：<input type="text" id='amount'/>分
				          		记账方向：<input type="text" id='direction'/>（0为减,1为加）
				          		机构号：<input type="text" id='merchantcode'/>
								产品号：<input type="text" id='productid'/>					
				          		<input type="button" value="确定" onclick="handlePushedResults(this)" />
				          	</td>
						</tr>
					</table>
				</form>
				</td>
	</tr>
	<!-- ========================================================================= 
	<tr>
	<td>
	汇总状态修改：
	<form method="post">
		<table>
			<tr>
	          	<td>
	          		文件上传后的状态：<input type="text" id='status'>
	          		<input type="button" value="确定" onclick="genStatusUpdate()" />
	          	</td>
			</tr>
		</table>
	</form>
	</td>
	</tr>-->
	<!-- ========================================================================= -->
	<tr>
	<td>
	手动通知订单系统：
	<form method="post">
		<table>
			<tr>
	          	<td>
	          		<input type="button" value="通知订单系统" onclick="updateOrder()" />
	          	</td>
			</tr>
		</table>
	</form>
	</td>
	</tr>
	<tr>
		<td>
		手动汇总备付金金额：
		<form method="post">
			<table>
				<tr>
		          	<td>		          		
		          		<input type="button" value="汇总备付金金额" onclick="bfjSummary()" />
		          	</td>
				</tr>
			</table>
		</form>
		</td>
	</tr>
	<!-- ========================================================================= -->
	<tr>
	<td>
	代收付结果数据移库：
	<form method="post">
		<table>
			<tr>
	          	<td>
	          		<input type="button" value="成功结果移库" onclick="moveDateSuccess()" />
	          		<input type="button" value="失败结果移库" onclick="moveDateFailure()" />
	          	</td>
			</tr>
		</table>
	</form>			
			</td>
							
			</tr>
		</table>
	</div>
</body>
</html>

