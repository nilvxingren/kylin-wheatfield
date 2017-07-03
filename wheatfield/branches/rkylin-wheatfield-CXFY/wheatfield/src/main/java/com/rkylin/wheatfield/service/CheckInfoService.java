package com.rkylin.wheatfield.service;

import java.util.List;

import com.rkylin.wheatfield.pojo.Balance;
import com.rkylin.wheatfield.pojo.FinanaceEntry;
import com.rkylin.wheatfield.pojo.FinanaceEntryHistory;
import com.rkylin.wheatfield.pojo.OrderAuxiliary;
import com.rkylin.wheatfield.pojo.TransOrderInfo;
import com.rkylin.wheatfield.pojo.User;

/**
 * 检查数据有效性校验
 * @author zhilei
 *
 */
public interface CheckInfoService {
	/**
	 * 校验订单数据有效性
	 * @param transOrderInfo 订单信息
	 * @return
	 */
	String checkTradeInfo(TransOrderInfo transOrderInfo);
	/**
	 * 校验订单数据有效性
	 * @param transOrderInfo 订单信息
	 * @return
	 */
	String checkTradeInfo(TransOrderInfo transOrderInfo,OrderAuxiliary orderAuxiliary);
	/**
	 * 获取账户余额
	 * @param user 用户信息
	 * @return
	 */
	Balance getBalance(User user,String finAccountIdP);
	/**
	 * 获取账户流水实例
	 * @param transOrderInfo 订单信息
	 * @param balance 用户余额信息
	 * @param entryId 套录号
	 * @param flag true：用戶记录；false：商户记录
	 * @return
	 */
	List<FinanaceEntry> getFinanaceEntries(TransOrderInfo transOrderInfo,Balance balance,String entryId,boolean flag);
	/**
	 * 获取账户流水实例
	 * @param transOrderInfo 订单信息
	 * @param balance 用户余额信息
	 * @param entryId 套录号
	 * @param flag true：用戶记录；false：商户记录
	 * @return
	 */
	List<FinanaceEntry> getFinanaceEntries(TransOrderInfo transOrderInfo,Balance balance,Balance balanceB,String entryId,boolean flag);
	/**
	 * 根据单号Id获取当天账户记账流水记录信息
	 * @param orderId
	 * @return
	 */
	List<FinanaceEntry> getFinanaceEntrieByOrderId(String orderId);
	/**
	 * 根据单号Id获取历史账户记账流水记录信息
	 * @param orderId
	 * @return
	 */
	List<FinanaceEntryHistory> getFinanaceEntrieHistoryByOrderId(String orderId);
	/**
	 * 获取记录流水信息
	 * @param finanaceEntry 流水部分信息
	 * @param balance  余额信息
	 * @param entryId 套录号
	 * @param flag 状态
	 * @param method  方法名称
	 * @param userId  用户Id
	 * @return
	 */
	List<FinanaceEntry> getInternalFinanaceEntries(FinanaceEntry finanaceEntry,Balance balance,boolean flag,String method,String userId);
	
	/**
	 * 
	 * @Description : TODO(获取账户流水实例)
	 * @param transOrderInfo 订单信息
	 * @param balance 用户余额信息
	 * @param entryId 套录号
	 * @param flag true：用戶记录；false：商户记录
	 * @Return : 
	 * @CreateTime : 2015年10月27日 下午9:24:49
	 * @Creator : liuhuan 
	 */
	FinanaceEntry getFinanaceEntry(TransOrderInfo transOrderInfo,Balance balance,String entryId,boolean flag);
}
