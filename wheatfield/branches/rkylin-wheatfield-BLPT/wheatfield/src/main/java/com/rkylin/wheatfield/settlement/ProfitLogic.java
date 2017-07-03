package com.rkylin.wheatfield.settlement;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Maps;
import com.ibm.icu.math.BigDecimal;
import com.rkylin.common.RedisIdGenerator;
import com.rkylin.order.mixservice.SettlementToOrderService;
import com.rkylin.order.pojo.OrderItem;
import com.rkylin.utils.RkylinMailUtil;
import com.rkylin.wheatfield.constant.Constants;
import com.rkylin.wheatfield.constant.SettleConstants;
import com.rkylin.wheatfield.constant.TransCodeConst;
import com.rkylin.wheatfield.exception.AccountException;
import com.rkylin.wheatfield.manager.ParameterInfoManager;
import com.rkylin.wheatfield.manager.SettleSplittingEntryManager;
import com.rkylin.wheatfield.manager.SettlementManager;
import com.rkylin.wheatfield.manager.TransDaysSummaryManager;
import com.rkylin.wheatfield.pojo.FinanaceEntry;
import com.rkylin.wheatfield.pojo.ParameterInfo;
import com.rkylin.wheatfield.pojo.ParameterInfoQuery;
import com.rkylin.wheatfield.pojo.SettleSplittingEntry;
import com.rkylin.wheatfield.pojo.TransDaysSummary;
import com.rkylin.wheatfield.pojo.TransDaysSummaryQuery;
import com.rkylin.wheatfield.pojo.TransOrderInfo;
import com.rkylin.wheatfield.response.ErrorResponse;
import com.rkylin.wheatfield.service.PaymentInternalService;
import com.rkylin.wheatfield.utils.SettlementUtils;

@Component("profitLogic")
@Transactional
public class ProfitLogic {
	private static Logger logger = LoggerFactory.getLogger(ProfitLogic.class);
	
	@Autowired
	ParameterInfoManager parameterInfoManager;
	
	@Autowired
	SettlementManager settlementManager;

	@Autowired
	RedisIdGenerator redisIdGenerator;
	
	@Autowired
	PaymentInternalService paymentInternalService;
	
	@Autowired
	SettleSplittingEntryManager settleSplittingEntryManager;

	@Autowired
	SettlementToOrderService settlementToOrderService;

	@Autowired
	TransDaysSummaryManager transDaysSummaryManager;
	/**
	 * 结算操作
	 * 
	 * @param ropUrl ropUrl
	 * 
	 * @return
	 */
	@Transactional(rollbackFor = AccountException.class, propagation = Propagation.NESTED)
	public Map<String,String> profitLogic(String invoicedate,String rootInstCd) {	
		logger.info("分润更新用户余额 ————————————START————————————");
		Map<String,String> rtnMap = new HashMap<String,String>();
		rtnMap.put("errCode", "0000");
		rtnMap.put("errMsg", "成功");

    	SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
    	SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd");
    	SimpleDateFormat formatter2 = new SimpleDateFormat("HH:mm:ss");
    	SimpleDateFormat formatter3 = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
    	SimpleDateFormat formatter4 = new SimpleDateFormat("yyyyMMddHHmmss");
    	ParameterInfoQuery keyList =  new ParameterInfoQuery();
    	keyList.setParameterCode(SettleConstants.DAYEND);
    	List<ParameterInfo> parameterInfo = parameterInfoManager.queryList(keyList);
    	if (!"0".equals(parameterInfo.get(0).getParameterValue())) {
    		rtnMap.put("errCode", "0001");
    		rtnMap.put("errMsg", "日终没有正常结束！");
    		RkylinMailUtil.sendMailThread("清洁算开始异常","日终没有正常结束，不能开始清洁算操作", "21401233@qq.com");
    		return rtnMap;
    	}
    	
    	keyList.setParameterCode(SettleConstants.ACCOUNTDATE);
		parameterInfo = parameterInfoManager.queryList(keyList);
    	SettlementUtils settlementUtils = new SettlementUtils();
		String rtnurlKey = null;
    	String accountDate = "";;
    	try {
	    	if (invoicedate == null || "".equals(invoicedate)) {
				try {
					accountDate = settlementUtils.getAccountDate(parameterInfo.get(0).getParameterValue(), "yyyyMMdd",0);
				} catch (Exception e2) {
					logger.error("计算账期异常！" + e2.getMessage());
					rtnMap.put("errCode", "0001");
					rtnMap.put("errMsg", "计算账期异常");
					return rtnMap;
				}
	    	} else {
	    		accountDate =invoicedate.replace("-", "");
	    	}
			logger.info("取得的账期为"+ accountDate);
		
			// 判断债权包是否已经返回
//			List<TransDaysSummary> rtnSList = null;
//			TransDaysSummaryQuery query = new TransDaysSummaryQuery();
////			transDaysSummary.setRootInstCd(rootInstCd);//机构号
////			transDaysSummary.setOrderType(TransCodeConst.PAYMENT_COLLECTION);//订单类型--代收
////			transDaysSummary.setAccountDate(formatter.parse(accountDate));//账单日期
////			transDaysSummary.setSummaryAmount(Long.parseLong(amount));//汇总金额
////			transDaysSummary.setUserId(TransCodeConst.THIRDPARTYID_P2PZZH);
//			query.setRootInstCd(rootInstCd);//机构号
//			query.setOrderType(TransCodeConst.PAYMENT_COLLECTION);//订单类型--代收
//			query.setAccountDate(formatter3.parse(accountDate+ " 00:00:00"));//账单日期
//			query.setUserId(TransCodeConst.THIRDPARTYID_P2PZZH);
//			rtnSList = transDaysSummaryManager.queryList(query);
//			if (rtnSList== null || rtnSList.size() == 0) {
//	    		rtnMap.put("errCode", "0001");
//	    		rtnMap.put("errMsg", accountDate + "的债权包没有返回，所以不能进行分润");
//	    		return rtnMap;
//			} else {
//				if (rtnSList.get(0).getStatusId() == SettleConstants.SETTLE_STU_1) {
//				} else {//失败的场合
//		    		rtnMap.put("errCode", "0001");
//		    		rtnMap.put("errMsg", accountDate + "的债权包结果为失败，所以不能进行分润");
//		    		return rtnMap;
//				}
//			}
			
			// 取得需要交易
			Map<String,Object> paraMap = new HashMap<String,Object>();
			Map<String,String> configMap = new HashMap<String,String>();
			paraMap.put("ACCOUNT_DATE", accountDate + " 00:00:00");
			paraMap.put("FUNC_CODE", "'"+TransCodeConst.CHARGE+"'");
			paraMap.put("STATUS", SettleConstants.SETTLE_STU_0);
			paraMap.put("ROOT", rootInstCd);
			List<TransOrderInfo> rtnList = settlementManager.queryList(paraMap);
			//List<TransOrderInfo> rtnList = new ArrayList();
			logger.info("编辑取得的"+rtnList.size()+"条数据");
			
			// 编辑数据，取得需要结算的交易
			int counti = 0;
			ErrorResponse errorResponse = null;
			FinanaceEntry finanaceEntry = new FinanaceEntry();
			SettleSplittingEntry settleSplittingEntry = new SettleSplittingEntry();
        	List<OrderItem> orderPayments = new ArrayList<OrderItem>();
        	OrderItem orderItem = new OrderItem();
        	List<Map<String,String>> upList = new ArrayList();
        	BigDecimal profit_1 = null;
        	BigDecimal profit_2 = null;
        	boolean success_flg = false;
			if (rtnList.size() > 0) {
				for (TransOrderInfo transOrderInfo : rtnList) {
					success_flg = false;
					//没有分润过的数据
					if (Constants.MZ_ID.equals(transOrderInfo.getMerchantCode()) || Constants.SQSM_ID.equals(transOrderInfo.getMerchantCode())) {
						if ((TransCodeConst.DEBIT_CONSUME.equals(transOrderInfo.getFuncCode()) || TransCodeConst.CREDIT_CONSUME.equals(transOrderInfo.getFuncCode()))
								&& TransCodeConst.TRANS_STATUS_PROFIT!=transOrderInfo.getStatus()) {
							settleSplittingEntry = new SettleSplittingEntry();
							settleSplittingEntry.setSettleId(redisIdGenerator.settleEntryNo());
							settleSplittingEntry.setRootInstCd(transOrderInfo.getMerchantCode());
							settleSplittingEntry.setAccountDate(transOrderInfo.getAccountDate());
							settleSplittingEntry.setOrderNo(transOrderInfo.getOrderNo());
							settleSplittingEntry.setAccountRelateId("");
							settleSplittingEntry.setUserId(transOrderInfo.getInterMerchantCode());
							settleSplittingEntry.setSettleType(SettleConstants.SETTLE_TYPE_5);
							
							finanaceEntry = new FinanaceEntry();
							finanaceEntry.setReferId(settleSplittingEntry.getSettleId());

							if (Constants.MZ_ID.equals(transOrderInfo.getMerchantCode())) {// 信用加储值
								settleSplittingEntry.setAmount(transOrderInfo.getAmount());
								finanaceEntry.setPaymentAmount(settleSplittingEntry.getAmount());
								errorResponse = paymentInternalService.fenrun(finanaceEntry, settleSplittingEntry.getUserId(),
										settleSplittingEntry.getRootInstCd(),Constants.MZ_PRODUCT,"");
								success_flg = errorResponse.isIs_success();
							} else if (Constants.SQSM_ID.equals(transOrderInfo.getMerchantCode())) {
								if (TransCodeConst.CREDIT_CONSUME.equals(transOrderInfo.getFuncCode())) {// 储值消费
									settleSplittingEntry.setAmount(transOrderInfo.getAmount());
									finanaceEntry.setPaymentAmount(settleSplittingEntry.getAmount());
									errorResponse = paymentInternalService.fenrun(finanaceEntry, settleSplittingEntry.getUserId(),
											settleSplittingEntry.getRootInstCd(),Constants.SQSM_PRODUCT,"");
									success_flg = errorResponse.isIs_success();
								} else if (TransCodeConst.THIRDPARTYID_SQSMQYZZH.equals(transOrderInfo.getUserId())) {
									settleSplittingEntry.setAmount(transOrderInfo.getAmount());
									finanaceEntry.setPaymentAmount(settleSplittingEntry.getAmount());
									errorResponse = paymentInternalService.fenrun(finanaceEntry, settleSplittingEntry.getUserId(),
											settleSplittingEntry.getRootInstCd(),Constants.SQSM_PRODUCT,"");
									success_flg = errorResponse.isIs_success();
								} else {
									profit_1 = new BigDecimal(String.valueOf(settleSplittingEntry.getAmount()));
									profit_2 = new BigDecimal("2");
									profit_2 = profit_1.divide(profit_2, 0, BigDecimal.ROUND_HALF_UP);
									settleSplittingEntry.setAmount(profit_2.longValue());
									finanaceEntry.setAmount(profit_2.longValue());
									errorResponse = paymentInternalService.fenrun(finanaceEntry, TransCodeConst.THIRDPARTYID_SQSMQYZZH,
											settleSplittingEntry.getRootInstCd(),Constants.SQSM_PRODUCT,"");
									if (errorResponse.isIs_success()) {
										settleSplittingEntry.setStatusId(SettleConstants.SETTLE_STU_1);
										settleSplittingEntry.setRemark("");
										settleSplittingEntryManager.saveSettleSplittingEntry(settleSplittingEntry);
										profit_1 = profit_1.subtract(profit_2);
										settleSplittingEntry.setAmount(profit_1.longValue());
										finanaceEntry.setAmount(profit_1.longValue());
										errorResponse = paymentInternalService.fenrun(finanaceEntry, TransCodeConst.THIRDPARTYID_SQSMQYZZH,
												settleSplittingEntry.getRootInstCd(),Constants.SQSM_COLLECTION_PRODUCT,"");
										success_flg = errorResponse.isIs_success();
									} else {
										settleSplittingEntry.setStatusId(SettleConstants.SETTLE_STU_0);
										settleSplittingEntry.setRemark("账户操作失败！"+errorResponse.getMsg());
										settleSplittingEntryManager.saveSettleSplittingEntry(settleSplittingEntry);
									}
								}
							}
							
							if (success_flg) {
								settleSplittingEntry.setStatusId(SettleConstants.SETTLE_STU_1);
								settleSplittingEntry.setRemark("");
								orderItem = new OrderItem();
								orderItem.setOrderItemId(transOrderInfo.getOrderNo());
								orderItem.setOrderItemTypeId(transOrderInfo.getFuncCode());
								orderItem.setStatusId(String.valueOf(TransCodeConst.TRANS_STATUS_PROFIT));
	            				orderPayments.add(orderItem);  
	            				paraMap = new HashMap<String,Object>();
								paraMap.put("ORDER_NO", transOrderInfo.getOrderNo());
								paraMap.put("MERCHANT_CODE", transOrderInfo.getMerchantCode());
								paraMap.put("STATUS", TransCodeConst.TRANS_STATUS_PROFIT);
								settlementManager.updatetransstatus(paraMap);
							} else {
								settleSplittingEntry.setStatusId(SettleConstants.SETTLE_STU_0);
								settleSplittingEntry.setRemark("账户操作失败！"+errorResponse.getMsg());
							}
							settleSplittingEntryManager.saveSettleSplittingEntry(settleSplittingEntry);
						}
					}
				}
				//更新订单系统交易状态
				Map<String,Object> rtnMapUp = Maps.newHashMap();
	        	rtnMapUp.put("paymentList", orderPayments);
	        	try {
	        	rtnMapUp = settlementToOrderService.fenrunResultNofity(rtnMapUp);
	        	} catch (Exception z) {
	    			logger.error("订单系统更新异常！"+z.getMessage());
	    			rtnMap.put("errCode", "0001");
	    			rtnMap.put("errMsg", "订单系统更新异常！");
	    			return rtnMap;
	        	}
	        	//更新有失败的场合，把数据更新到分润表
	        	if(!"true".equals(rtnMapUp.get("issuccess"))){
					List<OrderItem> rtnOrderPayments = (List<OrderItem>) rtnMapUp.get("paymentList");
					logger.info("更新失败订单系统的件数：" + rtnOrderPayments.size());
					int i=0;
					//代收付订单号交易相同的数据进行汇总
					for (OrderItem temp : rtnOrderPayments) {
						Map<String, String> upMap = Maps.newHashMap();
						if("1".equals(temp.getStatusId())){
							upMap.put("remark", "找不到订单系统数据:" + temp.getOrderItemId());
						}else{
							upMap.put("remark", "订单系统数据状态已更新:" + temp.getOrderItemId());
						}
						upMap.put("orderNo", temp.getOrderItemId());
						upList.add(upMap);
					}
				}

				// 更新分润结果表
				if (upList.size() > 0) {
		        	logger.info("准备更新分润表数据件数:" + upList.size());
					int updateCount = settleSplittingEntryManager.batchUpdateByOrderNo(upList);
					rtnMap.put("errMsg", "更新订单系统结束，更新失败件数为：" + updateCount);
					rtnMap.put("errCode", "P0");	    
					return rtnMap;
				}
			} else {
				rtnMap.put("errCode", "0000");
				rtnMap.put("errMsg", "没有交易");
				return rtnMap;
			}
    	} catch (Exception z) {
			logger.error("结算失败，请联系相关负责人"+z.getMessage());
			rtnMap.put("errCode", "0001");
			rtnMap.put("errMsg", "结算失败，请联系相关负责人");
    	}
		logger.info("分润更新用户余额 ————————————END————————————");
		return rtnMap;
	}
}
