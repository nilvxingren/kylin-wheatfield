package com.rkylin.wheatfield.api;

import com.rkylin.wheatfield.model.WipeAccountResponse;
import com.rkylin.wheatfield.pojo.FinanaceEntry;
import com.rkylin.wheatfield.response.ErrorResponse;

public interface PaymentInternalOutService {
	
	/**
	 * 分润
	 * @param finanaceEntry
	 * @param userId
	 * @param merchantId
	 * @param productId
	 * @param referUserId
	 * @return
	 */
	public String shareBenefit(FinanaceEntry finanaceEntry,String userId,String merchantId,String productId,String referUserId);
	
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
	
	/**
	 * 抹账（运营平台）
	 * @param orderNo  订单号
	 * @param newOrderNo 新订单号
	 * @param merchantCode  机构号
	 * @return
	 */
	public WipeAccountResponse wipeAccountPlat(String newOrderNo,String orderNo, String merchantCode);
}
