package com.rkylin.wheatfield.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.rkylin.wheatfield.manager.AccountInfoManager;
import com.rkylin.wheatfield.pojo.AccountInfo;
import com.rkylin.wheatfield.pojo.AccountInfoQuery;

@Controller
@Scope("prototype")
@RequestMapping("/accountInfo")
public class AccountInfoController{

	private static Logger logger = LoggerFactory.getLogger(AccountInfoController.class);
	
	@Autowired
	AccountInfoManager accountInfoManager;
	
	@RequestMapping("/indexPage")
	public String accountInfoIndex(HttpServletRequest req,Model model){
		String basePath = req.getScheme() + "://" + req.getServerName() + ":" + req.getServerPort() + req.getContextPath();
		model.addAttribute("basePath", basePath);
		
		return "account/account_info_list";
	}
	
	/** 
	* @Description:查询对私卡/对公户的详细信息
	*
	* @param rootInstId 机构号
	* @param userId 用户id
	* @param accountPurpose 账户目的（结算卡）
	* @param status 状态（正常）
	* @param response   
	*/
	@RequestMapping("/queryList")
	public String queryAccountInfo(AccountInfoQuery query,HttpServletRequest req,HttpServletResponse response, Model model) throws Exception{
		logger.info("查询对私卡/对公户的详细信息开始--------------rootInstId = " + query.getRootInstCd() + ",userId = " + query.getAccountName() + ", accountPurpose = " + query.getAccountPurpose()
				+ ", status = " + query.getStatus());
		response.setCharacterEncoding("UTF-8");  
		if(null == query.getRootInstCd() || "".equals(query.getRootInstCd().trim())){
			logger.info("机构号为空！");
			throw new Exception("机构号为空！");
		}
		if(null == query.getAccountName() || "".equals(query.getAccountName().trim())){
			logger.info("用户id为空！");
			throw new Exception("用户id为空！");
		}if(null == query.getAccountPurpose() || "".equals(query.getAccountPurpose().trim())){
			logger.info("账户目的（结算卡）为空！");
			throw new Exception("账户目的（结算卡）为空！");
		}if(null == query.getStatus()){
			logger.info("状态（正常）为空！");
			throw new Exception("状态（正常）为空！");
		}
		
		List<AccountInfo> accList = accountInfoManager.selectAccountListForJsp(query);
		
		model.addAttribute("accList",accList);
		model.addAttribute("rootInstCd", query.getRootInstCd());
		model.addAttribute("accountName", query.getAccountName());
		String basePath = req.getScheme() + "://" + req.getServerName() + ":" + req.getServerPort() + req.getContextPath();
		model.addAttribute("basePath", basePath);
		model.addAttribute("queryFlag", "1");
		
		return  "account/account_info_list";
	}
	
	/** 
	* @Description:根据账户表id把相应数据状态改为：4验证失败
	*
	* @param accountId 账户表id
	* @param status 状态（验证失败4）
	* @return   
	*/
	@RequestMapping("/updateStatusById_{accountId}")
	public String updateAccountInfoStatus(HttpServletRequest req,@PathVariable("accountId") String accountId,Model model){ 
		logger.info("更改账户状态信息开始--------------");
		
		AccountInfoQuery query = new AccountInfoQuery();
		query.setAccountId(Integer.parseInt(accountId));
		//把相应数据状态改为：4验证失败
		query.setStatus(4);
		
		accountInfoManager.updateAccountInfoStatus(query);

		String basePath = req.getScheme() + "://" + req.getServerName() + ":" + req.getServerPort() + req.getContextPath();
		model.addAttribute("basePath", basePath);
		
		return "account/account_info_success";
		
	} 
	
}
