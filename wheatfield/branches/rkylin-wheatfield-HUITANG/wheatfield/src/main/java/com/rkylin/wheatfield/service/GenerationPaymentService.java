package com.rkylin.wheatfield.service;

import java.util.Date;

import com.rkylin.wheatfield.model.CommonResponse;
import com.rkylin.wheatfield.pojo.GenerationPayment;
import com.rkylin.wheatfield.pojo.TransOrderInfo;

public interface GenerationPaymentService {
	
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
	 * 交易信息提交到代收付系统
	 * @param type 参考平台注释6:代收，7：代付
	 * @param batch 批次，可为空
	 * @param  accountDate  yyyy-MM-dd
	 */
	public void submitToCollection(int type,String batch,String rootInstCd,Date accountDate);
	/**
	 * 实时代收(提交到代收付系统)
	 * @param transOrderInfo
	 */
	public String submitToCollRealTime(TransOrderInfo transOrderInfo);
}
