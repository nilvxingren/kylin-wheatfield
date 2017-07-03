package com.rkylin.wheatfield.service;

import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Transactional;

import com.rkylin.wheatfield.pojo.CreditRateTemplateRes;
import com.rkylin.wheatfield.response.SettlementSecResponse;

public interface SettlementServiceSec {
	/**
	 * 查询费率模版
	 * @return 状态代码 状态信息
	 * */
	@Transactional
	List<CreditRateTemplateRes> queryRateTemplate(CreditRateTemplateRes rateTemp) throws Exception;
	/**
	 * 更新费率模版
	 * 参数 type: 2:插入 3:更新 4：删除
	 * @return 状态代码 状态信息
	 * */
	@Transactional
	Map<String,String> operateRateTemplate(String type,CreditRateTemplateRes rateTemp);
	/**
	 * 更新计息开始日
	 * @return 状态代码 状态信息
	 * */
	@Transactional
	Map<String,String> updateInterestDate(String orderOnList,String interestDate,SettlementSecResponse response);
	/**
	 * 查询还款计划
	 * 参数 
	 *    type 查询类型:1：虚拟  2：真实
	 *    userId 商户用户ID
	 *    userOrderId 商户贷款订单号
	 *    rootInstId 商户号
	 *    orderId 账户贷款订单号
	 * @return 状态代码 状态信息
	 * */
	@Transactional
	Map<String,String> queryRepaymentPlan(String type,String userId,
			                              String userOrderId,String rootInstId,
			                              String orderId,SettlementSecResponse response);
	/**
	 * 日批上传代扣文件 ROP
	 * 参数 
	 *    rootInstId 商户号
	 *    providerId 提供商号  （M000002 P2P号）
	 * @return 状态代码 状态信息
	 * */
	@Transactional
	Map<String,String> uploadWithholdFile(String rootInstId,String providerId);
}
