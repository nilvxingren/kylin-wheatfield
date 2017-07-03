package com.rkylin.wheatfield.service;

import java.util.List;
import java.util.Map;

import com.rkylin.wheatfield.model.CommonResponse;
import com.rkylin.wheatfield.pojo.AccountAgreement;
import com.rkylin.wheatfield.pojo.AccountInfo;
import com.rkylin.wheatfield.pojo.AccountInfoPlus;
import com.rkylin.wheatfield.pojo.CreditApprovalInfo;
import com.rkylin.wheatfield.pojo.CreditInfo;
import com.rkylin.wheatfield.pojo.CreditRepaymentHistory;
import com.rkylin.wheatfield.pojo.FinanaceAccount;
import com.rkylin.wheatfield.pojo.FinanaceEntry;
import com.rkylin.wheatfield.pojo.User;

/**
 * 账户管理相关接口
 * @author zhenpc
 *
 */
public interface AccountManageService {
	/**
	 * 检查账户有效性（dubbo）
	 * @param user
	 * @return 1:表示有效
	 */
	public CommonResponse verifyAccount(User user);
	/**
	 * 创建账户
	 * @author zhenpc
	 * @param user 开户用户信息
	 * @return 账户号
	 * */
	String openAccount(User user, FinanaceEntry finanaceEntry);
	
	/**
	 * 冻结账户
	 * @author zhenpc
	 * @param user 冻结用户信息
	 * @return 账户冻结是否成功
	 * */
	String freezeAccount(User user);
	
	/**
	 * 销户
	 * @author zhenpc
	 * @param user 关闭用户信息
	 * @return 关闭账户是否成功
	 * */
	String closeAccount(User user);
	
	/**
	 * 检查账户信息是否有效
	 * @author zhenpc
	 * @param user 用户信息
	 * @return 检查账户信息是否有效
	 * */
	String checkAccount(User user);
	
	/**
	 * @throws Exception 
	 * 账户授信
	 * @author zhenpc
	 * @param user 授信用户信息
	 * @param amount 授信金额
	 * @param creditDate 授信截止日期
	 * @throws  
	 * */
	String creditAccount(User user, CreditInfo creditInfo,
			AccountAgreement accountAgreement, String provideruserid)
			throws Exception;
	/**
	 * @throws Exception 
	 * 账户授信--新接口
	 * @author zhenpc
	 * @param user 授信用户信息
	 * @param amount 授信金额
	 * @param creditDate 授信截止日期
	 * @throws  
	 * */
	String creditAccountNew(User user, CreditApprovalInfo creditApprovalInfo,
			AccountAgreement accountAgreement, String provideruserid)
			throws Exception;
	/**
	 * 绑定银行账户(对公账户或对私账户)
	 * @param user 用户信息
	 * @param accountInfo 银行账户信息
	 * @return
	 */
	public String bindingBankAccount(User user , AccountInfo accountInfo);
	
	/**
	 * 查询账户授信信息
	 * @author zhenpc
	 * @param user 用户信息
	 * */
	
	List<CreditInfo> creditInfoQuery(User user, String providerId);
	/**
	 * 还款账单信息查询
	 * @author zhenpc
	 * @param user 用户信息
	 * */
	List<CreditRepaymentHistory> getRepaymentInfo(User user, String repayDate);
	/**
	 * 获取银行卡信息
	 * @param user
	 * @param objOrList
	 * @return
	 */
	List<AccountInfo> getAccountList(User user, String objOrList);
	/**
	 * 删除银行卡信息
	 * @param user
	 * @param accountNumber
	 * @return
	 */
	String delAccountInfo(User user, String accountNumber);
	/**
	 * 修改结算卡
	 * @param user
	 * @param accountNumber
	 * @return
	 */
	String updateAccountInfo(User user, String accountNumber);
	/**
	 * 解除冻结账户
	 * @param user
	 * @return
	 */
	String rmFreeze(User user);

	String accountCkeck(User user, String accountNumber);
	/**
	 * 根据卡号获取卡状态
	 * @param user
	 * @param accountNumber
	 * @return
	 */
	String getAccountByNo(User user, String accountNumber);
	/**
	 * 修改对公卡信息（系统审核-代付失败）
	 * @param user
	 * @param accountInfo
	 * @return
	 */

	 /**
	  * 定时任务 每天下午17点,对公账户发送一分钱
	  */
	 public void onecentService();
	String updateCreatesAccount(User user, Map<String, String> map);

	 
     /**
      * 定时任务每天下午20点,更新对公账户状态
      */
	 public void updateAccountoneCent();

/**
	 * 代付一分钱校验对公账户（定时调度）  2015-09-06
	 */
	 public void paymentJudgePublicAccount();

	/**
	 * 获取银行卡信息(增加总行logo的图片名称)
	 * @param user
	 * @param objOrList
	 * @return
	 */
	List<AccountInfoPlus> getAccountListPlus(User user, String objOrList);
	/**
		 * 查询对公账户一分钱代付结果，修改状态
		 */
	public void updatePubAccountByPayResult();

    /**
     * by liuhuan
     */
	 public List<FinanaceAccount> getAllAccount(User user,String oper);
}
