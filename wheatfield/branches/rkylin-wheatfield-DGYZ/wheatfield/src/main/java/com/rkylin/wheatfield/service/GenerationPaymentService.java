package com.rkylin.wheatfield.service;

import java.util.Date;
import java.util.List;
import java.util.Set;

import com.rkylin.crps.pojo.BaseResponse;
import com.rkylin.crps.pojo.OrderDetail;
import com.rkylin.wheatfield.model.CommonResponse;
import com.rkylin.wheatfield.model.TransOrderInfosResponse;
import com.rkylin.wheatfield.pojo.GenerationPayment;
import com.rkylin.wheatfield.pojo.TransOrderInfo;
import com.rkylin.wheatfield.pojo.TransOrderInfoQuery;

public interface GenerationPaymentService {
	
//	public CommonResponse getGenPaymentList(List<OrderDetail> orderDetailList,int judge);

	/**
	 * 计息系统查询订单状态（代收付系统是否返回结果）
	 * @param query
	 * @return
	 */
	public TransOrderInfosResponse getTransOrderInfos(TransOrderInfoQuery query);

	/**
	 * 去代收付系统查询批量发送的代收付结果处理代收付结果
	 * @param orderNoArray 订单号数组
	 * @param batch	批次号
	 * @return
	 */
	public CommonResponse updateGenResultsByQuyRecAndPaySys(String batch,String[] orderNoArray);
	
	/**
	 * 查询交易记录并生成退票记录
	 * @param orderNo
	 * @param instCode
	 * @return
	 */
	public CommonResponse getTransInforAndRefundRecords(String orderNo, String instCode) ;
	
	/**
	 * 将返回文件转换为代收付文件
	 * @param generationPayment
	 * @return
	 */
	String payMentResultTransform(GenerationPayment generationPayment);

//	String uploadPaymentFile(int type, String batch);

	String getAccountDate();

	String paymentFile(String invoicedate,String batch,String opertype,String priOrPubKey);

	String accountNotify(String urlKey, String suffix, int type,
			String priOrPubKey);

	void uploadPaymentFile(int type, String batch, String rootInstCd,
			Date accountDate);
	
	/**
	 * 手动提交发送失败(处于临时状态，sendType为3的)的代收交易到代收付系统
	 * @param batch 批次号
	 * @return
	 */
	public CommonResponse submitToRecAndPaySysManual(String batch);
	
	/**
	 * 代收付交易提交到代收付系统
	 * @param type 6:代收，7：代付
	 * @param rootInstCd 机构号
	 * @param  accDate  系统账期
	 * @param  accountDate  不同机构处理后的账期
	 */
	public void submitToRecAndPaySys(int type,String rootInstCd,String accDate,Date accountDate);
	/**
	 * 实时代收(提交到代收付系统)
	 * @param transOrderInfo
	 */
	public String submitToCollRealTime(TransOrderInfo transOrderInfo);
	
	/**
	 * 根据代收付系统查出的实时代收结果修改账户系统
	 * @param orderNoArray
	 */
	public CommonResponse updateRealTimeTransInfoByRecAndPaySys(String[] orderNoArray);
	
	/**
	 * 代收付推入的数据订单号进行缓存，分机构缓存，防止数据过大，循环影响性能；如果在今天的缓存找不到就去昨天的缓存查找；如果需要清除缓存可以使用页面手动功能
	 * @param rootInstCd 机构号
	 * @param newOrderNoSet 新的订单号
	 * @return
	 */
	public boolean isPushedGenPay(String rootInstCd,Set<String> newOrderNoSet);
	
	/**
	 * 删除缓存的某机构某天缓存的所有代收付推入的数据
	 * @param rootInstCd
	 * @param date
	 */
	public void delPushedGenPaySet(String rootInstCd,String date);
	
	/**
	 * 定时处理缓存中代收付系统推送的数据
	 */
	public void handleRecAndPayCacheData();
	
	/**
	 * 手动处理代收付推入的数据缓存
	 * @param key
	 */
	public  CommonResponse handleRecAndPayCachePushed(String key);
	
	
//	public void refund99(BaseResponse baseResponse);
}
