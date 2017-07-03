package com.rkylin.wheatfield.api;

import java.util.List;

import com.rkylin.wheatfield.bean.AccountListQuery;
import com.rkylin.wheatfield.bean.FinAccAndBalanceQuery;
import com.rkylin.wheatfield.model.AmountResponse;
import com.rkylin.wheatfield.model.BalanceDeResponse;
import com.rkylin.wheatfield.model.CommonResponse;
import com.rkylin.wheatfield.model.FinAccAndBalanceResponse;
import com.rkylin.wheatfield.model.FinAccountAndBalanceResponse;
import com.rkylin.wheatfield.model.FinAccountBalanceResponse;
import com.rkylin.wheatfield.model.FinAccountInfoResponse;
import com.rkylin.wheatfield.model.FinAccountResponse;
import com.rkylin.wheatfield.model.InstAccountInfoResponse;
import com.rkylin.wheatfield.model.OpenSubAccountResponse;
import com.rkylin.wheatfield.model.PingAnAccountResponse;
import com.rkylin.wheatfield.model.ProductResponse;
import com.rkylin.wheatfield.model.UserResponse;
import com.rkylin.wheatfield.pojo.FinanaceEntry;

/**
 * 账户信息相关
 * 
 * @author Achilles
 *
 */
public interface AccountInfoDubboService {

    /**
     * 
     * Discription:转账通知
     * 
     * @param dto
     * @author Achilles
     * @since 2016年5月3日
     */
    public String noticeSingleTransfer(com.rkylin.gaterouter.dto.bankpayment.PaymentRespDto dto);

    /**
     * 民生转账-通知 Discription:
     * 
     * @param dto
     * @return String
     * @author Achilles
     * @since 2016年7月27日
     */
    public String noticeTransferGaterouter(com.rkylin.gaterouter.dto.ResponseDto dto);

    /**
     * 民生转账批量-通知 Discription:
     * 
     * @param dto
     * @return String
     * @author Achilles
     * @since 2016年7月27日
     */
    public String noticeBatchTransfer(com.rkylin.gaterouter.dto.bankpayment.BatchPaymentRespDto dto);

    /**
     * 平安退票 Discription:
     * 
     * @param requestId
     * @author Achilles
     * @since 2016年4月27日
     */
    public String refundPingAn(com.rkylin.gaterouter.dto.bankpayment.PaymentRespDto dto);

    /**
     * 运营平台调用账户,账户去多渠道开户,成功后在账户开户两个子账户 Discription:
     * 
     * @param user
     * @return CommonResponse
     * @author Achilles
     * @since 2016年4月26日
     */
    public OpenSubAccountResponse openSubAccountMice(com.rkylin.wheatfield.bean.User user);

    /**
     * 查询余额（dubbo）
     * 
     * @param user
     */
    public BalanceDeResponse getBalance(com.rkylin.wheatfield.bean.User user);

    /**
     * 获取用户账户信息
     */
    public FinAccountResponse getFinAccount(com.rkylin.wheatfield.bean.User user);
    /**
     * 银行卡信息查询
     * 
     * @param cardInfoQueryList
     * @return
     */
    // public AccountInfoResponse getCardInfor(List<CardInfoQuery> cardInfoQueryList);
    /**
     * 对公银行卡信息查询
     * 
     * @param cardInfoQueryList
     * @return
     */

    // public CorAccountInfoResponse getCorCardInfor(List<CardInfoQuery> cardInfoQueryList);
    /**
     * 新增或修改密码
     * 
     * @param user
     * @return
     */
    public CommonResponse addOrUpdatePassword(com.rkylin.wheatfield.bean.User user);

    /**
     * 校验密码是否存在
     * 
     * @param user
     * @return
     */
    public CommonResponse checkPasswordIfExist(com.rkylin.wheatfield.bean.User user);

    /**
     * 用户密码校验是否正常
     * 
     * @param user
     * @return
     */
    public CommonResponse passwordCheck(com.rkylin.wheatfield.bean.User user);

    /**
     * 密码锁定解锁 Discription:
     * 
     * @param user
     * @return CommonResponse
     * @author Achilles
     * @since 2016年8月30日
     */
    public CommonResponse passLockOrUnlock(com.rkylin.wheatfield.bean.User user);

    /**
     * 开子账户
     * 
     * @param user
     * @param finanaceEntry
     * @return
     */
    public CommonResponse openSubAccount(com.rkylin.wheatfield.pojo.User user, FinanaceEntry finanaceEntry);

    /**
     * 查询所有mice子账户信息 Discription:
     * 
     * @return PingAnAccountResponse
     * @author Achilles
     * @since 2016年5月23日
     */
    public PingAnAccountResponse getSubAccountMice(List<com.rkylin.wheatfield.bean.User> userList);

    /**
     * 获取期初余额 Discription:
     * 
     * @param user
     * @return FinAccountBalanceResponse
     * @author Achilles
     * @since 2016年6月23日
     */
    public FinAccountBalanceResponse getFinAccountBalance(com.rkylin.wheatfield.bean.User user);

    /**
     * 批量开通子账户 Discription:
     * 
     * @param userList
     * @return CommonResponse
     * @author Achilles
     * @since 2016年7月6日
     */
    public CommonResponse openSubAccounts(List<com.rkylin.wheatfield.pojo.User> userList);

    /**
     * 根据传入机构号和用户id获取此人(同一个身份证下)所有账户信息 Discription:
     * 
     * @param user
     * @return CommonResponse
     * @author Achilles
     * @since 2016年8月25日
     */
    public UserResponse getAllUserInfo(com.rkylin.wheatfield.bean.User user);

    /**
     * 获取企业账户信息 Discription:
     * 
     * @return FinanaceCompanyResponse
     * @author Achilles
     * @since 2016年12月13日
     */
    // public FinanaceCompanyResponse getFinCompanyInfo(FinCompanyQuery query);

    /**
     * 获取个人账户信息 Discription:
     * 
     * @param query
     * @return FinanacePersonResponse
     * @author Achilles
     * @since 2016年12月13日
     */
    // public FinanacePersonResponse getFinPersonInfo(FinPersonQuery query);

    /**
     * 获取账户信息及余额等 Discription:
     * 
     * @param finAccAndBalanceQuery
     * @return FinAccountAndBalanceResponse
     * @author Achilles
     * @since 2017年2月15日
     */
    public FinAccountAndBalanceResponse getFinAccAndBalance(com.rkylin.wheatfield.bean.User user);

    /***
     * 获取账户信息及余额等 Discription:
     * 
     * @param finAccAndBalanceQuery
     * @return FinAccountAndBalanceResponse
     * @author Achilles
     * @since 2017年2月16日
     */
    public FinAccountAndBalanceResponse
            getFinAccountAndBalance(com.rkylin.wheatfield.bean.FinAccAndBalanceQuery finAccAndBalanceQuery);

    /**
     * 获取机构下产品号及对应名称 Discription:
     * 
     * @param instCode
     * @return ProductResponse
     * @author Achilles
     * @since 2017年2月16日
     */
    public ProductResponse getProduct(String instCode);

    /**
     * Discription: 获取机构下用户数量（个人用户及企业用户量）
     * 
     * @param instCode
     * @return InstAccountInfoResponse
     * @author sun
     * @since 2017年2月16日
     */
    public InstAccountInfoResponse getInstUserNo(String rootInstCd);

    /**
     * Discription: 获取机构下用户列表（个人用户及企业用户量）
     * 
     * @param query
     * @return InstAccountInfoResponse
     * @author sun
     * @since 2017年2月16日
     */
    public InstAccountInfoResponse getInstUserList(AccountListQuery query);
      
      /**
       * 查询用户账户信息
       * Discription:
       * @param user
       * @return FinAccountInfoResponse
       * @author Achilles
       * @since 2017年2月16日
       */
      public FinAccountInfoResponse getFinAccountInfo(com.rkylin.wheatfield.bean.User user);
      
      /**
       * 账户状态修改
       * Discription:
       * @param user
       * @return FinAccountInfoResponse
       * @author Achilles
       * @since 2017年2月17日
       */
      public CommonResponse updateFinAccountStatus(com.rkylin.wheatfield.bean.User user);
      
      
      /**
       * 获取商户账户余额及备付金
       * Discription:
       * @param user
       * @return FinAccAndBalanceResponse
       * @author Achilles
       * @since 2017年2月21日
       */
      public FinAccAndBalanceResponse getMerchantFinAccAndBalance(com.rkylin.wheatfield.bean.User user);
      
      /**
       * 获取余额等信心及上传FTP
       * Discription:
       * @param finAccAndBalanceQuery
       * @return CommonResponse
       * @author Achilles
       * @since 2017年2月24日
       */
      public CommonResponse getFinAccAndBalAndUpload(FinAccAndBalanceQuery finAccAndBalanceQuery);
      
      /**
       * 获取商户下全部用户数量
       * Discription:
       * @param instCode
       * @return NumberResponse
       * @author Achilles
       * @since 2017年3月9日
       */
      public AmountResponse getUserAmount(com.rkylin.wheatfield.bean.User user);
}
