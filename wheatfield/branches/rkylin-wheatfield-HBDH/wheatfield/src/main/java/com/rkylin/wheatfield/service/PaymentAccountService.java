package com.rkylin.wheatfield.service;

import java.util.List;
import java.util.Map;

import com.rkylin.wheatfield.model.CommonResponse;import com.rkylin.wheatfield.model.BalanceResponse;
import com.rkylin.wheatfield.model.ReversalResponse;
import com.rkylin.wheatfield.pojo.AdvanceBalance;
import com.rkylin.wheatfield.pojo.Balance;
import com.rkylin.wheatfield.pojo.FinanaceEntry;
import com.rkylin.wheatfield.pojo.OrderAuxiliary;
import com.rkylin.wheatfield.pojo.TransOrderInfo;
import com.rkylin.wheatfield.pojo.User;
import com.rkylin.wheatfield.response.ErrorResponse;
import com.rkylin.wheatfield.response.Response;

/**
 * 账户操作接口
 * @author zhenpc
 *
 */
public interface PaymentAccountService {
	
	/**
	 * 代收(dubbo)
	 * @param transOrderInfo
	 * @return
	 */
	public CommonResponse collection(TransOrderInfo transOrderInfo);

	/**
	 * 查询余额（dubbo）
	 * @param user
	 * @param finAccountId
	 * @param type  1:精确查找（有平衡检查）    2：查询用户所有账户（没有平衡检查） 
	 * @return
	 */
	public BalanceResponse getUserBalance(User user,String finAccountId,String type);
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
	 * 生成退票记录
	 * @param transOrderInfo
	 * @return
	 */
	public String makeRefundRecords(TransOrderInfo transOrderInfo);
	
	/**
	 * 获取账户余额
	 * @param user 指定的用户信息
	 * @return 账户余额信息
	 * */
	Balance getBalance(User user,String finAccountIdP);
	
	/**
	 * 充值
	 * @param transOrderInfo 交易订单信息
	 * @param productId 产品号   （provisionAccountNo 充值备付金汇缴帐号   不用  备注无视）
	 * @param userId 用户号
	 */
	ErrorResponse recharge(TransOrderInfo transOrderInfo, String productId, String userId);
	
		
	/**
	 * 预付金待付--订单
	 * @param transOrderInfo 交易订单信息
	 * @param productId 产品号  
	 * @param userId 用户号
	 */
	ErrorResponse advanceorders(TransOrderInfo transOrderInfo, String productId,String referUserId);
	
	/**
	 * 扣款
	 * @param transOrderInfo 交易订单信息
	 * @param productId 产品号   （provisionAccountNo 扣款备付金付款帐号   不用  备注无视） 
	 * @param userId 用户号
	 */
	ErrorResponse deduct(TransOrderInfo transOrderInfo, String productId, String userId);
	
	/**
	 * 转账
	 * @param transOrderInfo 交易订单信息
	 * @param userId 用户号
	 * */
	ErrorResponse transfer(TransOrderInfo transOrderInfo ,String productId, String userId);
	
	/**
	 * 提现
	 * @param transOrderInfo 交易订单信息
	 * @param userId 用户号
	 * */
	ErrorResponse withdrow(TransOrderInfo transOrderInfo ,String productId, String userId,String referUserId);

	/**
	 * 商户扣款冲正
	 * @param transOrderInfo 原始订单信息
	 * @param userId 用户号
	 */
	ErrorResponse antiDeduct(TransOrderInfo transOrderInfo);
	
	/**
	 * 消费前退款
	 * @param transOrderInfo 交易订单信息
	 * @param userId 用户号
	 * */
	ErrorResponse consumptionbeforerefund(TransOrderInfo transOrderInfo);
	
	/**
	 * 消费后退款
	 * @param transOrderInfo 交易订单信息
	 * @param userId 用户号
	 * */
	ErrorResponse afterspendingrefund(TransOrderInfo transOrderInfo ,String productId, String userId, String referUserId);
	
	/**
	 * 预授权
	 * ##分两步 1.a账户向b账户转账一笔金额；2.该笔金额入账到b账户的冻结余额
	 * @param transOrderInfo 交易订单信息
	 * @param productId 产品号
	 * @param userId 用户号
	 * @referUserId	子账户关联账户，如P2P授信子账户中的P2P机构ID
	 * */
	ErrorResponse preauthorization(TransOrderInfo transOrderInfo ,String productId, String referUserId);
	/**
	 * 预授权完成---预授权部分完成
	 * ##预授权完成  1.根据订单中的信息反查到预授权订单的相关信息  2. 根据订单中的金额解冻账户b的冻结余额
	 * @param transOrderInfo 交易订单信息
	 * @param productId 产品号
	 * @param userId 用户号
	 * @referUserId	子账户关联账户，如P2P授信子账户中的P2P机构ID
	 * */
	ErrorResponse preauthorizationcomplete(TransOrderInfo transOrderInfo ,String productId, String referUserId);
	/**
	 * 代收
	 * @param transOrderInfo
	 * @param productId
	 * @return
	 */
	ErrorResponse collection(TransOrderInfo transOrderInfo ,String productId);
	/**
	 * 代付
	 * @param transOrderInfo
	 * @param productId
	 * @return
	 */
	ErrorResponse withhold(TransOrderInfo transOrderInfo ,String productId);
	/**
	 * 获取商户与台长之间的关系 及相关余额
	 * @param user 账户查询相关对象  如果User中的referUserId有值，查询条件将会按照UserId(商户号)/productId(产品号)/constId(机构号)/referUserId(台长Id)
	 * 			   进行单条预付金信息查询，会自动忽略QueryType中的有效值
	 * @param queryType 查询类型  1=以商户Id查询关联台长相关的预付金余额信息，2=以台长Id查询所关联商户的预付金余额
	 * @return 台长给商户的预付金余额，及商户的当天发生额
	 */
	 List<AdvanceBalance> getAdvanceBalance(User user,String queryType);
	/**
	 * 冻结账户金额
	 * @param transOrderInfo
	 * @param productId
	 * @return
	 */
	ErrorResponse freezefund(TransOrderInfo transOrderInfo,OrderAuxiliary orderAuxiliary);
	/**
	 * 解冻账户金额
	 * @param transOrderInfo
	 * @param productId
	 * @return
	 */
	ErrorResponse thawfund(TransOrderInfo transOrderInfo,OrderAuxiliary orderAuxiliary);
	/**
	 * 订单退款
	 * @param transOrderInfo
	 * @param productId
	 * @return
	 */
	ErrorResponse orderrefund(TransOrderInfo transOrderInfo,OrderAuxiliary orderAuxiliary);
	/**
	 * 冻结账户金额返回授权码
	 * @param transOrderInfo
	 * @param productId
	 * @return
	 */
	ErrorResponse freezefundauthcode(TransOrderInfo transOrderInfo,OrderAuxiliary orderAuxiliary);
	/**
	 * 解冻账户金额
	 * @param transOrderInfo
	 * @param productId
	 * @return
	 */
	ErrorResponse thawfundauthcode(TransOrderInfo transOrderInfo,OrderAuxiliary orderAuxiliary);
	/**
	 * 数据写入订单交易数据表、账户记账表
	 * @param finanaceEntries  整理好的记账数据
	 * @param transOrderInfo 整理好的订单交易数据
	 * @return
	 */
	public void insertFinanaceEntry (List<FinanaceEntry> finanaceEntries,TransOrderInfo transOrderInfo,String[] productIdStrings);
	
	//####################################君融贷###############################################
	/**
	 * 提现--君融贷
	 * @param transOrderInfo
	 * @param orderAuxiliary
	 * @return
	 */
	String withdrowJRD(TransOrderInfo transOrderInfo,OrderAuxiliary orderAuxiliary);
	/**
	 * 代付--君融贷
	 * @param transOrderInfo
	 * @param orderAuxiliary
	 * @return
	 */
	String withholdJRD(TransOrderInfo transOrderInfo,OrderAuxiliary orderAuxiliary);
	/**
	 * 转账新接口
	 * @param transOrderInfo
	 * @param orderAuxiliary
	 * @return
	 */
	String transferNew(TransOrderInfo transOrderInfo,OrderAuxiliary orderAuxiliary);
	//########################################################################################
	
	//#####################################通信运维############################################
	/**
	 * 充值--通讯运维
	 * @param transOrderInfo
	 * @param productId
	 * @param userId
	 * @return
	 */
	String rechargeTXYW(TransOrderInfo transOrderInfo, String productId, String userId);
	
	/**
	 * 提现--通讯运维
	 * @param transOrderInfo
	 * @param orderAuxiliary
	 * @return
	 */
	String withdrowTXYW(TransOrderInfo transOrderInfo,OrderAuxiliary orderAuxiliary);
	
	//#########################################################################################
	
    /**
     * @Description : TODO(商户扣款冲正dubbo提供接口)
     * @Params : funcCode :交易编码 商户冲正 10011
     *           orderNo : 新订单号
     *           userIpAddress : 用户IP地址
     *           orderPackageNo : 订单包号 ----原定单号
     *           rootInstCd : 机构码
     * @Return :
     * @Creator : liuhuan 
     * @CreateTime : 2015年9月15日 下午1:51:34
     * @Updator : 
     * @UpdateTime :
     */
    public ErrorResponse antideductForDubbo(String funcCode,String orderNo,String userIpAddress,String orderPackageNo,String rootInstCd);
    
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
	 * 代收|实时代收
	 * @param paramMap
	 * @return
	 */
	public Response collectionBiz(Map<String, String[]> paramMap);
	
	/**
	 * 实时代收：记录订单信息，生成记账流水
	 * @param transOrderInfo
	 * @return
	 */
	public ErrorResponse collectionRealTime(TransOrderInfo transOrderInfo);
	
	/**
	 * @Description : TODO(转账通用)
	 * @CreateTime : 2015年10月27日 上午9:30:20
	 * @Creator : liuhuan
	 */
	//@Override
	public String transferInCommonUse(TransOrderInfo transOrderInfo,OrderAuxiliary orderAuxiliary);
	
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
}
