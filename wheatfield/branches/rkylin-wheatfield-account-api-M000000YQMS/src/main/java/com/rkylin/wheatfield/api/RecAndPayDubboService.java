package com.rkylin.wheatfield.api;

import java.util.List;
import com.rkylin.wheatfield.bean.TransOrder;
import com.rkylin.wheatfield.model.CommonResponse;

/**
 * 代收付相关(新)
 * @author Achilles
 *
 */
public interface RecAndPayDubboService {
    
    
    public CommonResponse refundPingAn();
	
    
	/**
	 * 清结算通知提现结果
	 * @param list
	 * @return
	 */
//	public CommonResponse notifyWithdrawResult(List<TransOrder> list);
	/**
	 * 清结算通知代付结果
	 * @param list
	 * @return
	 */
//	public CommonResponse notifyPayResult(List<TransOrder> list);
	/**
	 * 清结算通知代收结果
	 * @param list
	 * @return
	 */
//	public CommonResponse notifyCollectionResult(List<TransOrder> list);
	/**
	 * 清结算通知贷款还款结果
	 * @param list
	 * @return
	 */
//	public CommonResponse notifyLoanRepaymentResult(List<TransOrder> list);
}
