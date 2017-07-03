/**
 * @File name : PaymentAccountService.java
 * @Package : com.rkylin.wheatfield.api
 * @Description : TODO(用一句话描述该文件做什么)
 * @Creator : Administrator
 * @CreateTime : 2015年8月25日 下午5:27:18
 * @Version : 1.0
 * @Update records:
 *      1.1 2015年8月25日 by Administrator: 
 *      1.0 2015年8月25日 by Administrator: Created 
 * All rights served : FENGNIAN Corporation
 */
package com.rkylin.wheatfield.api;

import java.util.List;
import java.util.Map;

import com.rkylin.wheatfield.bean.BatchTransfer;
import com.rkylin.wheatfield.model.BalanceResponse;
import com.rkylin.wheatfield.model.BatchResponse;
import com.rkylin.wheatfield.model.CommonResponse;
import com.rkylin.wheatfield.model.ReversalResponse;
import com.rkylin.wheatfield.pojo.Balance;
import com.rkylin.wheatfield.pojo.TransOrderInfo;
import com.rkylin.wheatfield.pojo.User;
import com.rkylin.wheatfield.response.ErrorResponse;

/**
 * @ProjectName：rkylin-wheatfield-account-api
 * @FileName ：PaymentAccountService
 * @Description ：账户对外提供接口操作类
 * @Creator ：liuhuan
 * @CreateTime ：2015年8月25日 下午5:27:44
 * @version : 1.0
 */
public interface PaymentAccountServiceApi {
	
	/**
	 * 查询余额（dubbo）
	 * @param user
	 * @param finAccountId
	 * @param type  1:精确查找（有平衡检查）    2：查询用户所有账户（没有平衡检查） 
	 * @return
	 */
	public BalanceResponse getUserBalance(User user,String finAccountId,String type);
    /**
     * @Description : TODO(查询余额)
     * @Param : 
     * @Return : 
     * @CreateTime : 2015年8月28日 下午2:45:42
     * @Updator : 
     * @UpdateTime :
     */
    public Balance getBalance(User user,String finAccountId);
    /**
     * @Description : TODO(消费扣款)
     * @Param : 
     * @Return : 
     * @CreateTime : 2015年8月28日 下午2:45:59
     * @Updator : 
     * @UpdateTime :
     */
    public ErrorResponse deductForDubbo(TransOrderInfo transOrderInfo, String productId);
    /**
     * @Description : TODO(转账接口)
     * @Param : 
     * @Return : 
     * @CreateTime : 2015年8月28日 下午4:07:34
     * @Updator : 
     * @UpdateTime :
     */
    public ErrorResponse transferForDubbo(TransOrderInfo transOrderInfo,String productId);
    
    /**
     * @Description : TODO(商户扣款冲正dubbo提供接口)
     * @Params : funcCode :交易编码 商户冲正 10011
     *           orderNo : 新订单号
     *           userIpAddress : 用户IP地址
     *           orderPackageNo : 订单包号 ----原定单号
     *           rootInstCd : 机构码
     * @Return : 
     * @CreateTime : 2015年9月15日 下午1:51:34
     * @Updator : 
     * @UpdateTime :
     */
    public ErrorResponse antideductForDubbo(String funcCode,String orderNo,String userIpAddress,String orderPackageNo,String rootInstCd);
    
	/**
	 * 冲正（运营平台）
	 * @param funcCode 机构号
	 * @param newOrderNo 新订单号
	 * @param userIpAddress 用户ip
	 * @param oldOrderNo 旧订单号
	 * @param rootInstCd 机构号
	 * @return
	 */
    public ReversalResponse reversalPlat(String funcCode,String newOrderNo,String userIpAddress,String oldOrderNo,String rootInstCd);
    
    /**
     * 
     * @Description : TODO(消费后退款)
     * @Param : 
     * @Return : 
     * @CreateTime : 2015年9月15日 下午3:36:13
     * @Updator : 
     * @UpdateTime :
     */
    public ErrorResponse afterSpendingRefundForDubbo(TransOrderInfo transOrderInfo,String productId,String referUserId);
    
    /**
     * 代收
     * @param transOrderInfo
     * @return
     */
    public CommonResponse collection(TransOrderInfo transOrderInfo);
    
    /**
     * Discription:红包兑换dubbo提供接口
     * @param transOrderInfo
     * @param productId   产品号（转出A产品号）
     * @param intoProductId  产品号（转入B产品号)
     * @return ErrorResponse
     * @author liuhuan
     * @since 2016年5月9日
     */
    public ErrorResponse redPackageExchangeForDubbo(TransOrderInfo transOrderInfo,String productId,String intoProductId);
    
    /**
     * 转账
     * Discription:
     * @param paramMap
     * @return CommonResponse
     * @author Achilles
     * @since 2016年6月29日
     */
    public CommonResponse transferInCommon(Map<String, String[]> paramMap);
    
    /***
     * 批量转账
     * Discription:
     * @param batchTransferOrderInfo
     * @return BatchResponse
     * @author Achilles
     * @since 2016年8月3日
     */
    public BatchResponse transferBatch(com.rkylin.wheatfield.bean.BatchTransferOrderInfo batchTransferOrderInfo);
    
    /**
     * 批量转账
     * Discription:
     * @param batchTransfer
     * @return CommonResponse
     * @author Achilles
     * @since 2016年9月22日
     */  
    public CommonResponse transferBatch(BatchTransfer batchTransfer);
    
    /**
     * 代收批量
     * @param transOrderList
     * @return
     */
    public BatchResponse collectionBatch(List<com.rkylin.wheatfield.bean.TransOrderInfo> transOrderList);
    
    /**
     * 40151  jiao  yi
     * Discription:
     * @param transOrderInfo
     * @return CommonResponse
     * @author Achilles
     * @since 2016年10月24日
     */
    public CommonResponse recharge(com.rkylin.wheatfield.bean.TransOrderInfo transOrderInfo);    
    /**
     * Discription:充值
     * @param transOrderInfo
     * @author liuhuan
     * @since 2017年2月6日
     */
    public CommonResponse rechargeByDubbo(TransOrderInfo transOrderInfo);
}

