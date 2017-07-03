package com.rkylin.wheatfield.service;

import java.util.List;





import com.rkylin.wheatfield.pojo.Balance;
import com.rkylin.wheatfield.pojo.FinanaceEntry;
import com.rkylin.wheatfield.pojo.FinanaceEntryHistory;
import com.rkylin.wheatfield.pojo.SettleTransTab;
import com.rkylin.wheatfield.pojo.TransOrderInfo;
import com.rkylin.wheatfield.pojo.User;

public interface OperationServive {
	
	/** 
	 * 获取指定用户的账户ID
	 * */
	String getUserAccount(User user);
	/**
	 * 获取交易用户ID
	 * @param tradeAtom 原子交易
	 * @param flag 交易源与交易目标的标记
	 * @return 返回用户ID
	 * */
	String getUserId(TransOrderInfo transOrderInfo , boolean flag);
	/**
	 * 获取记账方向
	 * @param tradeType 交易类型
	 * @param flag 交易源与交易目标的标记
	 * @return 返回不同交易类型中的DC标志
	 * */
	int getDorC(String transType , boolean flag);
	/**
	 * 生成一笔提现交易
	 * @param tradeAtom 原子交易
	 * @param userBalance 用户余额
	 * @param entryId 套录号
	 * @param flag 交易源与交易目标的标志
	 * @return 返回提现交易账户流水
	 * */
	List<FinanaceEntry> getWithdrawAccount(TransOrderInfo transOrderInfo , Balance balance, String entryId ,boolean flag);
	/**
	 * 生成一笔充值交易
	 * @param tradeAtom 原子交易
	 * @param userBalance 用户余额
	 * @param entryId 套录号
	 * @param flag 交易源与交易目标的标志
	 * @return 返回提现交易充值流水
	 * */
	List<FinanaceEntry> getChargeAccount(TransOrderInfo transOrderInfo, Balance balance, String entryId ,boolean flag);
	/**
	 * 生成一笔扣款交易
	 * @param tradeAtom 原子交易
	 * @param userBalance 用户余额
	 * @param entryId 套录号
	 * @param flag 交易源与交易目标的标志
	 * @return 返回提现交易扣款流水
	 * */
	List<FinanaceEntry> getDeductAccount(TransOrderInfo transOrderInfo , Balance balance, String entryId ,boolean flag);
	/**
	 * 生成一笔转账交易
	 * @param tradeAtom 原子交易
	 * @param balance 用户A余额
	 * @param balanceB 用户B余额
	 * @param entryId 套录号
	 * @param flag 交易源与交易目标的标志
	 * @return 返回提现交易充值流水
	 * */
	List<FinanaceEntry> getADJUSTAccount(TransOrderInfo transOrderInfo, Balance balance,Balance balanceB, String entryId ,boolean flag);
	/**
	 * 获取指定一个套录号下多条流水中得账户ID数
	 * @param tradeList 多条流水
	 * @return 一个账户ID:0,一个以上账户ID:1,异常:-1
	 * */
	int getAccountcount(List<FinanaceEntry> tradeList);
	/**
	 * 获取指定一个套录号下多条流水中得账户ID数
	 * @param tradeList 多条流水
	 * @return 一个账户ID:0,一个以上账户ID:1,异常:-1
	 * */
	int getAccountcountByEntryHistory(List<FinanaceEntryHistory> tradeList);
	/**
	 * 判断账户状态
	 * @param user
	 * @return 正常状态返回true 非正常状态返回false
	 */
	boolean checkAccount(User user);
	/**
	 * 获取记账日期
	 * @return
	 */
	String getAccountDate();
	/**
	 * 整理冲正信息
	 * @param finanaceEn
	 * @param balance
	 * @param newEntryId
	 * @param reverseTime
	 * @return
	 */
	FinanaceEntry addReverseTrade(FinanaceEntry finanaceEn, Balance balance,String newEntryId, String reverseTime);
	/**
	 * 整理历史表中冲正信息
	 * @param finanaceEn
	 * @param balance
	 * @param newEntryId
	 * @param reverseTime
	 * @return
	 */
	FinanaceEntry addReverseTradeHistory(FinanaceEntryHistory finanaceEn, Balance balance,String newEntryId, String reverseTime);	
	
	/**
	 * 生成授信流水
	 * @param tradeAtom 原子交易
	 * @param balance 用户余额
	 * @param entryId 套录号
	 * @param flag 交易源与交易目标的标志
	 * @return 返回提现交易充值流水
	 * */
	List<FinanaceEntry> getCredit(FinanaceEntry finanaceEntry, Balance balance,boolean flag,String userId);
	/**
	 * 生成提现返回流水
	 * @param tradeAtom 原子交易
	 * @param balance 用户余额
	 * @param entryId 套录号
	 * @param flag 交易源与交易目标的标志
	 * @return 返回提现交易充值流水
	 * */
	List<FinanaceEntry> getWithdrawReturn(FinanaceEntry finanaceEntry, Balance balance,boolean flag,String userId);
	/**
	 * 生成还款返回流水
	 * @param tradeAtom 原子交易
	 * @param balance 用户余额
	 * @param entryId 套录号
	 * @param flag 交易源与交易目标的标志
	 * @return 返回提现交易充值流水
	 * */
	List<FinanaceEntry> getRefundReturn(FinanaceEntry finanaceEntry, Balance balance,boolean flag,String userId);
	/**
	 * 生成债权包流水
	 * @param tradeAtom 原子交易
	 * @param balance 用户余额
	 * @param entryId 套录号
	 * @param flag 交易源与交易目标的标志
	 * @return 返回提现交易充值流水
	 * */
	List<FinanaceEntry> getRightsPackageReturn(FinanaceEntry finanaceEntry, Balance balance,boolean flag,String userId);
	/**
	 * 生成分润流水数据
	 * @param finanaceEntry
	 * @param balance
	 * @param flag
	 * @param userId
	 * @return
	 */
	List<FinanaceEntry> getFenRun(FinanaceEntry finanaceEntry, Balance balance,boolean flag,String userId);
	/**
	 * 生成预付金记账流水数据
	 * @param finanaceEntry
	 * @param balance
	 * @param flag
	 * @param userId
	 * @return
	 */
	List<FinanaceEntry> getYuFuJin(FinanaceEntry finanaceEntry, Balance balance,boolean flag,String userId);
	/**
	 * 代收返回记账流水数据
	 * @param finanaceEntry
	 * @param balance
	 * @param flag
	 * @param userId
	 * @return
	 */
	List<FinanaceEntry> getCollectionReturn(FinanaceEntry finanaceEntry, Balance balance,boolean flag,String userId);
	/**
	 * 代付返回记账流水数据
	 * @param finanaceEntry
	 * @param balance
	 * @param flag
	 * @param userId
	 * @return
	 */
	List<FinanaceEntry> getWithholdReturn(FinanaceEntry finanaceEntry, Balance balance,boolean flag,String userId);

	/**
	 * 生成一笔冻结记账流水
	 * @param tradeAtom 原子交易
	 * @param balance 账户余额
	 * @param entryId 套录号
	 * @param flag 交易源与交易目标的标志
	 * @return 返回提现交易充值流水
	 * */
	List<FinanaceEntry> getFreezeAccount(TransOrderInfo transOrderInfo, Balance balance,String entryId ,boolean flag);
	/**
	 * 生成一笔解冻记账流水
	 * @param tradeAtom 原子交易
	 * @param balance 账户余额
	 * @param entryId 套录号
	 * @param flag 交易源与交易目标的标志
	 * @return 返回提现交易充值流水
	 * */
	List<FinanaceEntry> getFrozenAccount(TransOrderInfo transOrderInfo, Balance balance,String entryId ,boolean flag);
	/**
	 * 生成一笔代收记账流水
	 * @param tradeAtom 原子交易
	 * @param balance 账户余额
	 * @param entryId 套录号
	 * @param flag 交易源与交易目标的标志
	 * @return 返回提现交易充值流水
	 * */
	List<FinanaceEntry> getCollection(TransOrderInfo transOrderInfo, Balance balance,String entryId ,boolean flag);
	/**
	 * 生成一笔待付记账流水
	 * @param tradeAtom 原子交易
	 * @param balance 账户余额
	 * @param entryId 套录号
	 * @param flag 交易源与交易目标的标志
	 * @return 返回提现交易充值流水
	 * */
	List<FinanaceEntry> getWithhold(TransOrderInfo transOrderInfo, Balance balance,String entryId ,boolean flag);
	/**
	 * 获取退款订单的账户挂起信息
	 * @param transOrderInfo
	 * @return
	 */
	List<SettleTransTab> getSettleTransTabs(TransOrderInfo transOrderInfo);
	
	/**
	 * 生成一笔交易流水
	 * @param tradeAtom 原子交易
	 * @param balance 用户A余额
	 * @param entryId 套录号
	 * @param flag 交易源与交易目标的标志
	 * @return 返回提现交易充值流水
	 * */
	FinanaceEntry getFinanaceEntry(TransOrderInfo transOrderInfo, Balance balance , String entryId ,boolean flag);
}
