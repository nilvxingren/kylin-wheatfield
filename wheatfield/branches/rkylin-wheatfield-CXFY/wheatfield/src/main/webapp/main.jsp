<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<html>
<head>
<base href="<%=basePath%>">
<title>清结算系统</title>
<style type="text/css">
.title{
	font-size:15px;
	font-family:"黑体";
	color:#FFFFFF;
	text-align:right;
	border:#000000 1px solid;}
.subTitle{
	font-size:14px;
	text-align:left;
	color: #FFFFFF;
	cursor:pointer;}
.dtree {
	font-family: Verdana, Geneva, Arial, Helvetica, sans-serif;
	font-size: 12px;
	color: #666;
	white-space: nowrap;
	background-color:#edfcff;
	border:#009acb 1px solid;
	width:217px;
	height:460px;
	line-height:30px;
	float:left;
}
.dtree img {
	border: 0px;
	vertical-align: middle;
}
.dtree a {
	color: #333;
	text-decoration: none;
}
.dtree a.node, .dtree a.nodeSel {
	white-space: nowrap;
	padding: 1px 2px 1px 2px;
}
.dtree a.node:hover, .dtree a.nodeSel:hover {
	color:red;
	text-decoration: underline;
}
.dtree a.nodeSel {
	font-weight:bold;
}
.dtree .clip {
	overflow: hidden;
}
</style>
<script type="text/javascript" src="js/dtree.js"></script>
<script language="javascript"> 
	function SetCwinHeight(){
	  var bobo=document.getElementById("rightFrm"); //iframe id
	  if (document.getElementById){
	   if (bobo && !window.opera){
		if (bobo.contentDocument && bobo.contentDocument.body.offsetHeight){
		 bobo.height = bobo.contentDocument.body.offsetHeight * 1.1;
		}else if(bobo.Document && bobo.Document.body.scrollHeight){
		 bobo.height = bobo.Document.body.scrollHeight * 1.1;
		}
	   }
	  }
	 }
	function shineme(obj){
		obj.style.fontWeight="bold";
		obj.style.backgroundColor="beige";
		obj.style.color="red";
	}


	function noshineme(obj){
		obj.style.fontWeight="";
		obj.style.backgroundColor="#EDFCFF";
		obj.style.color="black";
	}
	</script>
</head>

<body>
	<table border=0 width="80%" align="center" cellspacing="0" cellpadding="0">
		<tr>
			<td colspan="3" height="34" bgcolor="#0b6092" class="title">
			清结算系统&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			</td>
		</tr>

		<tr>
			<td width="219" valign="top" bgcolor="#76CAE6">
				<table width="100%" border="0" cellspacing="0" cellpadding="0">
				  <tr>
						<td width="100%" height="36" bgcolor="#009acb" class="boder">&nbsp;
							<img src="images/body_06.gif" width="14" height="20" align="absmiddle" />
							&nbsp;当前用户：<span class ="subTitle">管理员</sapn>
						</td>
				  </tr>
				  <tr>
						<td height="30" >&nbsp;</td>
				  </tr>
				  <tr>
					  <td>	  
					  <table width="218" border="0" cellspacing="0" cellpadding="0" bgcolor="#EDFCFF">
					  <tr>
						<td height="378" valign="top"><div class="dtree">
							<script type="text/javascript">
							<%
								int nNode = 0;
							%>

							d = new dTree('d');

							d.add(<%=nNode++%>,-1,'  ');
							d.add(<%=nNode++%>,0,'风控分析','','','','','img/folderopen.gif',false);
							    d.add(<%=nNode++%>,<%=nNode-2%>,'差错处理','building.jsp','','rightFrm');
							    d.add(<%=nNode++%>,<%=nNode-3%>,'代扣更新用户','pages/account/update_account.jsp','','rightFrm');
							    d.add(<%=nNode++%>,<%=nNode-4%>,'会唐对账','pages/account/collate_tlht_c.jsp','','rightFrm');
							    d.add(<%=nNode++%>,<%=nNode-5%>,'分润更新用户','pages/account/update_settle.jsp','','rightFrm');
							    d.add(<%=nNode++%>,<%=nNode-6%>,'冲正&抹账','pages/account/correct_settle.jsp','','rightFrm');
							d.add(<%=nNode++%>,0,'对账','','','','','img/folderopen.gif',false);
							d.add(<%=nNode++%>,<%=nNode-2%>,'对账结果下载','getSettle','','rightFrm');
							d.add(<%=nNode++%>,0,'代收付','','','','','img/folderopen.gif',false);
							d.add(<%=nNode++%>,<%=nNode-2%>,'代收付结果下载','getColAndPay','','rightFrm');
							
							
//							d.add(<%=nNode++%>,0,'业务管理','','','','','img/folderopen.gif',false);
//							d.add(<%=nNode++%>,<%=nNode-2%>,'商户信息','','','rightFrm');
//							d.add(<%=nNode++%>,<%=nNode-2%>,'渠道商户入网','merchant/importMerchant.jsp','','rightFrm');
//							d.add(<%=nNode++%>,<%=nNode-3%>,'招行联名卡信息导入','merchant/importAccountCMB.jsp','','rightFrm');
//							d.add(<%=nNode++%>,<%=nNode-4%>,'大区伙伴关系导入','merchant/importAgent.jsp','','rightFrm');
//							d.add(<%=nNode++%>,<%=nNode-5%>,'渠道信息','','','rightFrm');
//							d.add(<%=nNode++%>,<%=nNode-2%>,'基本信息','building.jsp','','rightFrm');
//							d.add(<%=nNode++%>,<%=nNode-3%>,'协议信息','building.jsp','','rightFrm');
//							d.add(<%=nNode++%>,<%=nNode-8%>,'伙伴信息','','','rightFrm');
//							d.add(<%=nNode++%>,<%=nNode-2%>,'基本信息','building.jsp','','rightFrm');
//							d.add(<%=nNode++%>,<%=nNode-3%>,'协议信息','building.jsp','','rightFrm');
//							d.add(<%=nNode++%>,<%=nNode-11%>,'产品信息','','','rightFrm');
//							d.add(<%=nNode++%>,<%=nNode-2%>,'基本信息','building.jsp','','rightFrm');
//							d.add(<%=nNode++%>,<%=nNode-3%>,'产品政策','building.jsp','','rightFrm');
//
//							d.add(<%=nNode++%>,0,'交易数据','','','','','img/folderopen.gif',false);
//							d.add(<%=nNode++%>,<%=nNode-2%>,'交易数据同步','building.jsp','','rightFrm');
//							d.add(<%=nNode++%>,<%=nNode-3%>,'渠道对账数据导入','building.jsp','','rightFrm');
//							d.add(<%=nNode++%>,<%=nNode-4%>,'导入历史查看','building.jsp','','rightFrm');
//
//							d.add(<%=nNode++%>,0,'畅捷宝','','','','','img/folderopen.gif',false);
//							d.add(<%=nNode++%>,<%=nNode-2%>,'代付渠道对账','settlement/settle_kft/gettransdata.jsp','','rightFrm');
//							d.add(<%=nNode++%>,<%=nNode-3%>,'申购','settlement/settle_kft/shengou.jsp','','rightFrm');
//							d.add(<%=nNode++%>,<%=nNode-4%>,'赎回','settlement/settle_kft/shuhui.jsp','','rightFrm');
//
//							d.add(<%=nNode++%>,0,'T+0','','','','','img/folderopen.gif',false);
//							d.add(<%=nNode++%>,<%=nNode-2%>,'T+0','settlement/T0/t0.jsp','','rightFrm');
//
//							d.add(<%=nNode++%>,0,'清结算','','','','','img/folderopen.gif',false);
//							d.add(<%=nNode++%>,<%=nNode-2%>,'清分','building.jsp','','rightFrm');
//							d.add(<%=nNode++%>,<%=nNode-3%>,'对账','','','rightFrm');
//								d.add(<%=nNode++%>,<%=nNode-2%>,'对账文件下载','settlement/collate/download.jsp?a=1','','rightFrm');
//								d.add(<%=nNode++%>,<%=nNode-3%>,'对账结果下载','settlement/collate/download.jsp?a=2','','rightFrm');
//								d.add(<%=nNode++%>,<%=nNode-4%>,'重新对账','settlement/collate/reCollate.jsp','','rightFrm');
//								d.add(<%=nNode++%>,<%=nNode-5%>,'北京银行通道结算差异','report/download.jsp?a=6003','','rightFrm');
//							d.add(<%=nNode++%>,<%=nNode-8%>,'清算','settlement/settle/invoice.jsp','','rightFrm');
//							d.add(<%=nNode++%>,<%=nNode-9%>,'结算','','','rightFrm');
//								d.add(<%=nNode++%>,<%=nNode-2%>,'结算','settlement/settle/disbsure.jsp','','rightFrm');
//								d.add(<%=nNode++%>,<%=nNode-3%>,'特殊日设置','settlement/settle/dayControl.jsp','','rightFrm');
//							d.add(<%=nNode++%>,<%=nNode-12%>,'入账','','','rightFrm');
//								d.add(<%=nNode++%>,<%=nNode-2%>,'商户入账单','settlement/bill/sendMail.jsp','','rightFrm');
//								d.add(<%=nNode++%>,<%=nNode-3%>,'商户入账单(新)','settlement/bill/sendMailNew.jsp','','rightFrm');
//
//							d.add(<%=nNode++%>,0,'差错处理','','','','','img/folderopen.gif',false);
//							d.add(<%=nNode++%>,<%=nNode-2%>,'退款查询','building.jsp','','rightFrm');
//							d.add(<%=nNode++%>,<%=nNode-3%>,'管理员处理','settlement/adex.jsp','','rightFrm');
//
//							d.add(<%=nNode++%>,0,'风控分析','','','','','img/folderopen.gif',false);
//							d.add(<%=nNode++%>,<%=nNode-2%>,'冻结/挂账','settlement/settle/freeze.jsp','','rightFrm');
//							d.add(<%=nNode++%>,<%=nNode-3%>,'费率错账处理','settlement/settle/wrongaccount.jsp','','rightFrm');
//							d.add(<%=nNode++%>,<%=nNode-4%>,'差错处理','settlement/settle/wrongtrans.jsp','','rightFrm');
//
//							d.add(<%=nNode++%>,0,'伙伴分润','','','','','img/folderopen.gif',false);
//							d.add(<%=nNode++%>,<%=nNode-2%>,'分润计算','building.jsp','','rightFrm');
//							d.add(<%=nNode++%>,<%=nNode-3%>,'分润明细下载','building.jsp','','rightFrm');
//
//							d.add(<%=nNode++%>,0,'备付金报表','','','','','img/folderopen.gif',false);
//							d.add(<%=nNode++%>,<%=nNode-2%>,'数据录入','deposit/data_cmb.jsp','','rightFrm');
//							d.add(<%=nNode++%>,<%=nNode-3%>,'报表下载','deposit/bfjbaobiao.jsp','','rightFrm');
//							d.add(<%=nNode++%>,<%=nNode-4%>,'报表管理','deposit/bfjtaizhang.jsp','','rightFrm');
//							
//							d.add(<%=nNode++%>,0,'报表查询','','','','','img/folderopen.gif',false);
//							d.add(<%=nNode++%>,<%=nNode-2%>,'日报表','settlement/collate/download.jsp?a=3','','rightFrm');
//							d.add(<%=nNode++%>,<%=nNode-3%>,'月报表','settlement/collate/download.jsp?a=4','','rightFrm');
////							d.add(<%=nNode++%>,<%=nNode-4%>,'周报表','building.jsp','','rightFrm');
//							d.add(<%=nNode++%>,<%=nNode-5%>,'业务收益统计','report/download.jsp?a=6001','','rightFrm');
//							d.add(<%=nNode++%>,<%=nNode-6%>,'自定义报表','settlement/collate/download.jsp?a=5','','rightFrm');
//							d.add(<%=nNode++%>,<%=nNode-7%>,'招行联名卡报表','settlement/collate/zhlmk.jsp','','rightFrm');
//							d.add(<%=nNode++%>,<%=nNode-8%>,'爱信诺补贴报表','settlement/collate/axn00100.jsp','','rightFrm');
//							d.add(<%=nNode++%>,<%=nNode-9%>,'posp分润报表','settlement/collate/posp_fenrun.jsp','','rightFrm');
//							d.add(<%=nNode++%>,<%=nNode-10%>,'费率查询','settlement/collate/flcx_fee.jsp','','rightFrm');
//							d.add(<%=nNode++%>,<%=nNode-11%>,'各种报表','settlement/settle/baobiaozh.jsp','','rightFrm');
//
//							d.add(<%=nNode++%>,0,'字典配置','','','','','img/folderopen.gif',false);
//							d.add(<%=nNode++%>,<%=nNode-2%>,'MCC','building.jsp','','','','',false);
//							d.add(<%=nNode++%>,<%=nNode-3%>,'银行','building.jsp','','','','',false);
//							d.add(<%=nNode++%>,<%=nNode-4%>,'卡Bin','building.jsp','','','','',false);
//							d.add(<%=nNode++%>,<%=nNode-5%>,'地区','building.jsp','','','','',true);
//							d.add(<%=nNode++%>,<%=nNode-6%>,'内部岗位','building.jsp','','','','',false);
//							
//							d.add(<%=nNode++%>,0,'系统管理','','','','','img/folderopen.gif',false);
//							d.add(<%=nNode++%>,<%=nNode-2%>,'定时任务','','','','','img/folderopen.gif',false);
//							d.add(<%=nNode++%>,<%=nNode-2%>,'添加任务','system/task/task_setting.jsp','','rightFrm');
//							d.add(<%=nNode++%>,<%=nNode-3%>,'任务列表','system/task/task_list.jsp','','rightFrm');
//							d.add(<%=nNode++%>,<%=nNode-4%>,'处理历史','system/task/task_trace.jsp','','rightFrm');
//							
//							d.add(<%=nNode++%>,<%=nNode-6%>,'用户权限','','','','','img/folderopen.gif',false);
//							d.add(<%=nNode++%>,<%=nNode-2%>,'角色权限管理','system/user/readme.jsp','','rightFrm','');
//							d.add(<%=nNode++%>,<%=nNode-3%>,'用户查询','system/user/readme.jsp','','rightFrm','');
//							d.add(<%=nNode++%>,<%=nNode-4%>,'添加用户','system/user/readme.jsp','','rightFrm','');
//							d.add(<%=nNode++%>,<%=nNode-5%>,'修改密码','system/user/readme.jsp','','rightFrm','');
//
//							d.add(<%=nNode++%>,<%=nNode-11%>,'系统日志','building.jsp','','rightFrm');							

							
							d.add(<%=nNode++%>,0,'退出','index.jsp','','','','',false);


							document.write(d);
						</script>
						</div></td>
					  </tr>
					  </table>
					  </td>
				  </tr>
				</table>
			</td>
			<td width="5">
				<iframe id="hiddenFrm" name="hiddenFrm" width="3px" height="0px" frameborder="0" src="building.jsp" frameborder="0"></iframe>			
			</td>

			<td valign="top">
				<iframe id="rightFrm" name="rightFrm" onload="Javascript:SetCwinHeight()" width="100%" height="0px" frameborder="0" src="right.jsp" frameborder="0"></iframe>
			</td>
		</tr>
	<table>
</body>
</html>