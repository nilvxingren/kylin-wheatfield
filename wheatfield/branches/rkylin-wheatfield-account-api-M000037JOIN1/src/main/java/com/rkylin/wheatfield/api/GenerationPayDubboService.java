package com.rkylin.wheatfield.api;

import com.rkylin.wheatfield.model.CommonResponse;
import com.rkylin.wheatfield.model.TransOrderInfosResponse;
import com.rkylin.wheatfield.pojo.TransOrderInfoQuery;

/**
 *   代收付相关
 * @author Achilles
 *
 */
public interface GenerationPayDubboService {

	
	/**
	 * 计息系统查询订单状态（代收付系统是否返回结果）
	 * @param query
	 * @return
	 */
	public TransOrderInfosResponse getTransOrderInfos(TransOrderInfoQuery query);
	/**
	 * 获取系统账期
	 * @return
	 */
	public String getAccountDate();
	/**
	 * 退票（运营平台）
	 * @param orderNo
	 * @param instCode
	 * @return
	 */
	public CommonResponse getTransInforAndRefundRecords(String orderNo, String instCode) ;
	/**
	 * 手动提交发送失败(处于临时状态，sendType为3的)的代收交易到代收付系统
	 * @param batch 批次号
	 * @return
	 */
	public CommonResponse submitToRecAndPaySysManual(String batch);
	
	/**
	 * 手动处理代收付推入的数据缓存
	 * @param key
	 */
	public  CommonResponse handleRecAndPayCachePushed(String batch);
	
	/**
	 * 根据代收付系统查出的实时代收结果修改账户系统
	 * @param orderNoArray
	 */
	public CommonResponse updateRealTimeTransInfoByRecAndPaySys(String[] orderNoArray);
	
	/**
	 * 去代收付系统查询批量发送的代收付结果处理代收付结果
	 * @param orderNoArray 订单号数组
	 * @param batch	批次号
	 * @return
	 */
	public CommonResponse updateGenResultsByQuyRecAndPaySys(String batch,String[] orderNoArray);
	
	   /**
     * 40143交易发送到多渠道
     * Discription:
     * @return CommonResponse
     * @author Achilles
     * @since 2016年7月27日
     */
	public  CommonResponse submitToGateRouterTransferBatch(String accountDate,Integer sendType,String protocol);
//	public  CommonResponse submitToGateRouter40143(String accountDate,Integer sendType,String protocol);
}
