package com.rkylin.wheatfield.api;

import java.util.List;

import com.rkylin.wheatfield.model.BalanceDeResponse;
import com.rkylin.wheatfield.model.CommonResponse;
import com.rkylin.wheatfield.model.FinAccountBalanceResponse;
import com.rkylin.wheatfield.model.FinAccountResponse;
import com.rkylin.wheatfield.model.OpenSubAccountResponse;
import com.rkylin.wheatfield.model.PingAnAccountResponse;
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

}
