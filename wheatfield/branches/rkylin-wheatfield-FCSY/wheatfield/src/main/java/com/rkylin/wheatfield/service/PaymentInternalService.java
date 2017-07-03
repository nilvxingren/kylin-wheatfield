package com.rkylin.wheatfield.service;

import java.util.List;

import com.rkylin.wheatfield.model.WipeAccountResponse;
import com.rkylin.wheatfield.pojo.FinanaceEntry;
import com.rkylin.wheatfield.pojo.TransOrderInfo;
import com.rkylin.wheatfield.response.ErrorResponse;
public interface PaymentInternalService {
	/**
	 * 抹账（运营平台）
	 * @param orderNo  订单号
	 * @param newOrderNo 新订单号
	 * @param merchantCode  机构号
	 * @return
	 */
	public WipeAccountResponse wipeAccountPlat(String newOrderNo,String orderNo, String merchantCode) ;
	/**
	 * 内部授信接口
	 * @param finanaceEntry 
	 * @return
	 */
	ErrorResponse credit(FinanaceEntry finanaceEntry,String userId,String merchantId,String productId,String referAccountId);
	/**
	 * 预付金----授信接口
	 * @param finanaceEntry  账户记账流水
	 * @param userId  用户Id
	 * @param merchantId 商户号/机构号
	 * @param productId 产品号/管理分组
	 * @param referAccountId 第三方管理号
	 * @return
	 */
	ErrorResponse downPaymentCredit(FinanaceEntry finanaceEntry,String userId,String merchantId,String productId,String referAccountId);
	/**
	 * 提现返回
	 * @param finanaceEntry
	 * @return
	 */
	ErrorResponse withdrawReturn(FinanaceEntry finanaceEntry,String merchantId);
	/**
	 * 还款返回
	 * @param finanaceEntry
	 * @return
	 */
	ErrorResponse refundReturn(FinanaceEntry finanaceEntry,String userId,String merchantId);
	/**
	 * 债权包返回
	 * @param finanaceEntry
	 * @return
	 */
	ErrorResponse rightsPackageReturn(FinanaceEntry finanaceEntry,String merchantId);
	/**
	 * 账户记账信息数据入库
	 * @param finanaceEntries
	 * @return
	 */
	public boolean insertFinanaceEntry(List<FinanaceEntry> finanaceEntries);
	/**
	 * 分润
	 * @param finanaceEntry 账户记账流水实体  其中支付金额、记账凭证号为必填
	 * @param userId 用户Id
	 * @param merchantId 商户号/机构号
	 * @param productId 产品号/管理分组
	 * @param referUserId 第三方用户userId
	 * @return
	 */
	ErrorResponse fenrun(FinanaceEntry finanaceEntry,String userId,String merchantId,String productId,String referUserId);
	/**
	 * 代收返回
	 * @param finanaceEntry
	 * @return
	 */
	ErrorResponse collectionReturn(FinanaceEntry finanaceEntry,String merchantId,String userId,String productId);
	/**
	 * 代付返回
	 * @param finanaceEntry
	 * @return
	 */
	ErrorResponse withholdReturn(FinanaceEntry finanaceEntry,String merchantId,String userId,String productId);
	/**
	 * 抹帐
	 * @param finanaceEntry
	 * @return
	 */
	ErrorResponse wipeAccount(TransOrderInfo transOrderInfo);
	/**
	 * 内部查询余额
	 * @param userId 用户Id
	 * @param rootInstCd 机构号
	 * @param productId 产品号
	 * @param referUserId 第三方Id
	 * @return 查询失败返回null
	 */
	Long getInternalBalance(String userId,String rootInstCd,String productId,String referUserId);
	
	/**
	 * 
	 * @Description : TODO(抹帐 dubbo提供接口)
	 * @Param : 
	 * @Return : 
	 * @Creator : liuhuan
	 * @CreateTime : 2015年9月15日 下午4:46:10
	 * @Updator : 
	 * @UpdateTime :
	 */
	public ErrorResponse wipeAccountForDubbo(String orderNo,String orderPackageNo,String merchantCode);
}
