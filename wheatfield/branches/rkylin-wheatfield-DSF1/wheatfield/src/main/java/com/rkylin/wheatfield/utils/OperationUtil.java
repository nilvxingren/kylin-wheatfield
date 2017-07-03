package com.rkylin.wheatfield.utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.rkylin.wheatfield.constant.AccountConstants;
import com.rkylin.wheatfield.constant.BaseConstants;
import com.rkylin.wheatfield.constant.TransCodeConst;
import com.rkylin.wheatfield.manager.FinanaceAccountManager;
import com.rkylin.wheatfield.pojo.Balance;
import com.rkylin.wheatfield.pojo.FinanaceAccount;
import com.rkylin.wheatfield.pojo.FinanaceAccountQuery;
import com.rkylin.wheatfield.pojo.FinanaceEntry;
import com.rkylin.wheatfield.pojo.TransOrderInfo;
import com.rkylin.wheatfield.pojo.User;
import com.rkylin.wheatfield.service.AccountManageService;
import com.rkylin.wheatfield.service.impl.CheckInfoServiceImpl;
public class OperationUtil {
	private static Logger logger = LoggerFactory.getLogger(CheckInfoServiceImpl.class);	
	@Autowired
	@Qualifier("finanaceAccountManager")
	private FinanaceAccountManager finanaceAccountManager;
	@Autowired
	private AccountManageService accountManageService; 
	
	
	/** 
	 * 获取指定用户的账户ID
	 * */
	public String getUserAccount(User user){	
		String finAccountId=null;
		FinanaceAccountQuery financeAccountQuery=new FinanaceAccountQuery();
		financeAccountQuery.setFinAccountName(user.userId);
		if(user.userType!=null){
			financeAccountQuery.setFinAccountTypeId(user.userType.getCategory());
		}		
		financeAccountQuery.setRootInstCd(user.productId);
		financeAccountQuery.setGroupManage(user.constId);
		List<FinanaceAccount> financeAccounts=finanaceAccountManager.queryList(financeAccountQuery);
		if(null!=financeAccounts && financeAccounts.size()==1){
			finAccountId= financeAccounts.get(0).getFinAccountId();
		}
		return finAccountId;
	}
	/**
	 * 获取交易用户ID
	 * @param tradeAtom 原子交易
	 * @param flag 交易源与交易目标的标记
	 * @return 返回用户ID
	 * */
	public String getUserId(TransOrderInfo transOrderInfo , boolean flag){
		String rtn = null;
		String transType=transOrderInfo.getTransType().toString();
		if(transType.equals(TransCodeConst.ADJUST_ACCOUNT_AMOUNT)){//内部转账交易
			if(flag){
				rtn=transOrderInfo.getUserId();
			}else{
				rtn=transOrderInfo.getInterMerchantCode();
			}
		}else if (transType.equals(TransCodeConst.CHARGE)) {//充值
			if(flag){
				rtn=transOrderInfo.getUserId();
			}else{
				rtn=transOrderInfo.getInterMerchantCode();
			}
		}else if (transType.equals(TransCodeConst.CREDIT_CONSUME)) {//信用消费
			if(flag){
				rtn=transOrderInfo.getUserId();
			}else{
				rtn=transOrderInfo.getInterMerchantCode();
			}
		}else if(transType.equals(TransCodeConst.DEBIT_CONSUME)){//储蓄消费
			if(flag){
				rtn=transOrderInfo.getUserId();
			}else{
				rtn=transOrderInfo.getInterMerchantCode();
			}
		}else if (transType.equals(TransCodeConst.PAYMENT_COLLECTION)) {//代收付
			if(flag){
				rtn=transOrderInfo.getUserId();
			}else{
				rtn=transOrderInfo.getInterMerchantCode();
			}
		}else if (transType.equals(TransCodeConst.PAYMENT_WITHHOLD)) {//代扣
			if(flag){
				rtn=transOrderInfo.getUserId();
			}else{
				rtn=transOrderInfo.getInterMerchantCode();
			}
		}else if(transType.equals(TransCodeConst.WITHDROW)){//提现
			if(flag){
				rtn=transOrderInfo.getUserId();
			}else{
				rtn=transOrderInfo.getInterMerchantCode();
			}
		}
		return rtn;
	}	
	/**
	 * 获取记账方向
	 * @param tradeType 交易类型
	 * @param flag 交易源与交易目标的标记
	 * @return 返回不同交易类型中的DC标志
	 * */
	public int getDorC(String transType , boolean flag){
		int rtn=0;
		if(transType.equals(TransCodeConst.ADJUST_ACCOUNT_AMOUNT)){//内部转账交易
			if(flag){
				rtn = BaseConstants.CREDIT_TYPE;
			}
			else{
				rtn = BaseConstants.DEBIT_TYPE;
			}
		}else if (transType.equals(TransCodeConst.CHARGE)) {//充值
			rtn = BaseConstants.CREDIT_TYPE;
		}else if (transType.equals(TransCodeConst.FROZEN)) {//解冻
			if(flag){
				rtn = BaseConstants.CREDIT_TYPE;
			}
			else{
				rtn = BaseConstants.DEBIT_TYPE;
			}
		}else if (transType.equals(TransCodeConst.PAYMENT_WITHHOLD)) {//代扣
			if(flag){
				rtn = BaseConstants.DEBIT_TYPE;
			}
			else{
				rtn = BaseConstants.CREDIT_TYPE;
			}
		}else if(transType.equals(TransCodeConst.WITHDROW)){//提现
			rtn = BaseConstants.DEBIT_TYPE;
		}else if(transType.equals(TransCodeConst.FROZON)){//冻结
			if(flag){
				rtn = BaseConstants.DEBIT_TYPE;
			}
			else{
				rtn = BaseConstants.CREDIT_TYPE;
			}
		}
		return rtn;
	}
	/**
	 * 返回一条账户流水实例
	 * @param tradeAtom 原子交易
	 * @param userBalance 用户余额
	 * @param entryId 套录号
	 * @param flag 交易源与交易目标的标志
	 * @return 账户流水实例
	 * */
	@SuppressWarnings("static-access")
	private FinanaceEntry getAccountFlow(TransOrderInfo transOrderInfo, Balance balance, String entryId,boolean flag){
		FinanaceEntry rtnFlow=new FinanaceEntry();
		/*
		 * 获取账户流水主键,后续需要修改为有规则的流水号
		 * */
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");//设置日期格式
		
		String finEntryId = df.format(new Date());
		
		if(null != finEntryId){
			rtnFlow.setFinanaceEntryId(finEntryId);//设置流水主键
			//获取记账日期***********************************************后续添加
			
			rtnFlow.setAccountDate(new Date());//设置记账日期
			
			String userId = this.getUserId(transOrderInfo, flag);
			User user=new User();
			user.userId=userId;
			user.userType.toEnum(AccountConstants.ACCOUNT_TYPE_BASE);
			String accountId = this.getUserAccount(user);//获取账户ID
			if(null != accountId){
				
				rtnFlow.setFinAccountId(accountId);//设置账户ID
				rtnFlow.setReferEntryId(entryId);//设置套录号
				
				String tradeType = transOrderInfo.getTransType().toString();
				
				rtnFlow.setDirection(this.getDorC(tradeType, flag));//设置DC标志
				
				/* 账户余额 */
				rtnFlow.setAmount(balance.getAmount() );
				
				/* 可用余额 */
				rtnFlow.setBalanceUsable(balance.getBalanceUsable() );
				
				/* 清算余额 */
				rtnFlow.setBalanceSettle( balance.getBalanceSettle());
				
				/* 冻结余额 */
				rtnFlow.setBalanceFrozon( balance.getBalanceFrozon());
				
				/* 透支额度 */
				rtnFlow.setBalanceOverLimit(balance.getBalanceOverLimit() );

				/* 贷记余额 */
				rtnFlow.setBalanceCredit( balance.getBalanceCredit());
				
				/* 预留余额1 
				rtnFlow.setBalanceBonus( balance.getBalanceBonus());
				
				 预留余额2 
				rtnFlow.setBalancePoints(balance.getBalancePoints() );
				
				 预留余额3 
				rtnFlow.setBalanceReserve1( balance.getBalanceReserve1());*/
				
				if (transOrderInfo.getTransType()==BaseConstants.REVERSE_TYPE){
					//冲正交易
					rtnFlow.setReverseFlag(BaseConstants.REVERSE_TYPE);
				}
				else{
					//非冲正交易
					rtnFlow.setReverseFlag(BaseConstants.NOREVERSE_TYPE);
				}
				
				rtnFlow.setPartyIdFrom(transOrderInfo.getUserId());//设置交易发起方
				rtnFlow.setReferId(transOrderInfo.getOrderNo());//记账凭证号
				/*暂时定义为订单来源*/
				rtnFlow.setReferFrom(String.valueOf(BaseConstants.REFER_TRADE));//设置凭证来源
				rtnFlow.setPaymentAmount(transOrderInfo.getAmount());//获取交易发生额
				rtnFlow.setTransDate(transOrderInfo.getRequestTime());//设置交易日期
				rtnFlow.setThirdPartyId(transOrderInfo.getInterMerchantCode());//交易相关方
				if (TransCodeConst.FROZEN.equals(transOrderInfo.getTransType().toString())) {
					rtnFlow.setMyNotes(TransCodeConst.FROZEN);//我方摘要
				} else {
					
				}
				rtnFlow.setYourNotes("");//你方摘要
				rtnFlow.setHisNote("");//他方摘要
				if (transOrderInfo.getMerchantCode() == null) {
					rtnFlow.setRemark("");//其他备注
				} else {
					rtnFlow.setRemark(transOrderInfo.getRemark());//其他备注
				}
				rtnFlow.setRecordMap("");//设置安全码
				//rtnFlow.setPulseTime(dateUtil.getTime());//设置账户心跳时间
				rtnFlow.setPulseDegree(balance.getPulseDegree());//账户心跳计次
			}
			else{
				logger.error("获取用户["+userId+"的账户ID失败");
				rtnFlow = null;
			}
		}
		else{
			logger.error("获取账户流水失败");
			rtnFlow = null;
		}
		return rtnFlow;
	}
	/**
	 * 生成一笔提现交易
	 * @param tradeAtom 原子交易
	 * @param userBalance 用户余额
	 * @param entryId 套录号
	 * @param flag 交易源与交易目标的标志
	 * @return 返回提现交易账户流水
	 * */
	public List<FinanaceEntry> getWithdrawAccount(TransOrderInfo transOrderInfo , Balance balance, String entryId ,boolean flag) {
		FinanaceEntry finanaceEntry=null;
		long amount= transOrderInfo.getAmount();//交易金额
		List<FinanaceEntry> finanaceEntries=new ArrayList<FinanaceEntry>();
		//比较提现金额
		if(amount>balance.getBalanceSettle()){
			logger.error("提现交易中提现金额["+amount+"]大于用户["+transOrderInfo.getUserId()+"]的提现余额");
		}else{
			balance.setAmount(balance.getAmount()-amount);//账户余额
			balance.setBalanceUsable(balance.getBalanceUsable());//账户可用余额
			balance.setBalanceSettle(balance.getBalanceSettle());//账户清余额
			finanaceEntry=this.getAccountFlow(transOrderInfo, balance, entryId, flag);
			finanaceEntry.setAccrualType(BaseConstants.TYPE_BALANCE_SETTLE);//设置发生额类型为可提现余额
			finanaceEntries.add(finanaceEntry);
		}		
		return finanaceEntries;
	}
	/**
	 * 生成一笔充值交易
	 * @param tradeAtom 原子交易
	 * @param userBalance 用户余额
	 * @param entryId 套录号
	 * @param flag 交易源与交易目标的标志
	 * @return 返回提现交易充值流水
	 * */
	public List<FinanaceEntry> getChargeAccount(TransOrderInfo transOrderInfo, Balance balance, String entryId ,boolean flag) {
		List<FinanaceEntry> finanaceEntries = new ArrayList<FinanaceEntry>();
		FinanaceEntry finanaceEntry=null;
		
		long amount = transOrderInfo.getAmount();//获得交易金额
		balance.setAmount(balance.getAmount()+amount);//账户总金额
		balance.setBalanceUsable(balance.getBalanceUsable()+amount);//账户可用余额
		//判断充值类型是否为贷记卡充值
		if(transOrderInfo.getBusiTypeId().equals(String.valueOf(BaseConstants.CREDIT_TYPE))){
			/* 增加贷记余额 */
			balance.setBalanceCredit(balance.getBalanceCredit()+amount);
		}else{
			/* 非贷记发生额的时候增加可提现余额 */
			balance.setBalanceSettle(balance.getBalanceSettle()+amount);
		}
		finanaceEntry=this.getAccountFlow(transOrderInfo, balance, entryId, flag);
		//通过交易中的金额类型，设置发生额类型为贷记金额还是清算金额
		if(transOrderInfo.getBusiTypeId().equals(String.valueOf(BaseConstants.CREDIT_TYPE))){
			finanaceEntry.setAccrualType(BaseConstants.TYPE_BALANCE_CREDIT);//设置发生额类型为贷记余额
		}
		else{
			finanaceEntry.setAccrualType(BaseConstants.TYPE_BALANCE_SETTLE);//设置发生额类型为清算余额
		}
		finanaceEntries.add(finanaceEntry);
		return finanaceEntries;
	}
	/**
	 * 生成一笔扣款交易
	 * @param tradeAtom 原子交易
	 * @param userBalance 用户余额
	 * @param entryId 套录号
	 * @param flag 交易源与交易目标的标志
	 * @return 返回提现交易扣款流水
	 * */
	public List<FinanaceEntry> getDeductAccount(TransOrderInfo transOrderInfo , Balance balance, String entryId ,boolean flag) {
		FinanaceEntry finanaceEntry=null;
		List<FinanaceEntry> finanaceEntries = new ArrayList<FinanaceEntry>();
		long amount = transOrderInfo.getAmount();//获得交易金额
		
		if(flag){
			if(balance.getBalanceUsable()>=amount){
				/* 判断贷记余额和可提现余额，默认先扣除贷记余额然后再扣除可提现余额 */
				if(balance.getBalanceCredit()>=amount){
					//贷记余额>=扣款金额
					balance.setBalanceCredit(balance.getBalanceCredit()-amount);
					//扣款订单中的交易源
					balance.setAmount(balance.getAmount()-amount);//账户余额
					balance.setBalanceUsable(balance.getBalanceUsable()-amount);//账户可用余额
					finanaceEntry=this.getAccountFlow(transOrderInfo, balance, entryId, flag);
					finanaceEntry.setAccrualType(BaseConstants.TYPE_BALANCE_CREDIT);
					finanaceEntries.add(finanaceEntry);
				}
				else{
					//贷记余额<扣款金额
					if(balance.getBalanceCredit()==0){
						logger.info("贷记余额为0");
						balance.setBalanceSettle(balance.getBalanceSettle()-amount);
						//扣款订单中的交易源
						balance.setAmount(balance.getAmount()-amount);//账户余额
						balance.setBalanceUsable(balance.getBalanceUsable()-amount);//账户可用余额
						finanaceEntry=this.getAccountFlow(transOrderInfo, balance, entryId, flag);
						finanaceEntry.setAccrualType(BaseConstants.TYPE_BALANCE_SETTLE);//设置发生额类型为清算余额
						finanaceEntries.add(finanaceEntry);
					}
					else{
						long subAmountC = 0;
						for(int i = 0; i <=1 ; i ++){
							if(0 == i){
								subAmountC=balance.getBalanceCredit();
								balance.setBalanceCredit(Long.parseLong("0"));//贷记金额为0
								//扣款订单中的交易源
								balance.setAmount(balance.getAmount()-subAmountC);//账户余额
								balance.setBalanceUsable(balance.getBalanceUsable()-subAmountC);//账户可用余额
								finanaceEntry=this.getAccountFlow(transOrderInfo, balance, entryId, flag);
								finanaceEntry.setAccrualType(BaseConstants.TYPE_BALANCE_CREDIT);//置发生额类型为清算余额
								finanaceEntry.setPaymentAmount(subAmountC);
							}
							else{
								long subAmount=subAmountC-amount;
								logger.info("还需要从可提现余额中扣除金额["+subAmount+"]");
								balance.setBalanceSettle(balance.getBalanceSettle()-subAmount);
								//扣款订单中的交易源
								balance.setAmount(balance.getAmount()-subAmount);//账户余额
								balance.setBalanceUsable(balance.getBalanceUsable()-subAmount);//账户可用余额
								finanaceEntry=this.getAccountFlow(transOrderInfo, balance, entryId, flag);
								finanaceEntry.setAccrualType(BaseConstants.TYPE_BALANCE_SETTLE);//设置发生额类型为清算余额
								finanaceEntry.setPaymentAmount(subAmount);;
							}
							finanaceEntries.add(finanaceEntry);
						}
					}
				}
			}
			else{
				logger.error("扣款交易中扣款金额["+amount+"]大于用户["+transOrderInfo.getUserId()+"]的可用余额");
			}
		}
		else{
			//扣款订单中订单目标
			balance.setBalanceSettle(balance.getBalanceSettle()-amount);
			balance.setAmount(balance.getAmount()-amount);//账户余额
			balance.setBalanceUsable(balance.getBalanceUsable()-amount);//账户可用余额
			finanaceEntry=this.getAccountFlow(transOrderInfo, balance, entryId, flag);
			finanaceEntry.setAccrualType(BaseConstants.TYPE_BALANCE_SETTLE);//设置发生额类型为清算余额
			finanaceEntries.add(finanaceEntry);
		}
		return finanaceEntries;
	}
	/**
	 * 生成一笔转账交易
	 * @param tradeAtom 原子交易
	 * @param balance 用户A余额
	 * @param balanceB 用户B余额
	 * @param entryId 套录号
	 * @param flag 交易源与交易目标的标志
	 * @return 返回提现交易充值流水
	 * */
	public List<FinanaceEntry> getADJUSTAccount(TransOrderInfo transOrderInfo, Balance balance,Balance balanceB, String entryId ,boolean flag) {
		List<FinanaceEntry> finanaceEntries = new ArrayList<FinanaceEntry>();
		FinanaceEntry finanaceEntry=null;
		long amount = transOrderInfo.getAmount();//获得交易金额
		//设置账户A需要的相关数据
		//获取账户A余额并判断可提现余额是否满足转账条件
		if(balance.getBalanceSettle()<amount){
			logger.error("账户"+transOrderInfo.getUserId()+"的可提现余额小于"+amount);
		}else{
			balance.setBalanceSettle(balance.getBalanceSettle()-amount);//清算余额
			balance.setAmount(balance.getAmount()-amount);//账户余额
			balance.setBalanceUsable(balance.getBalanceUsable()-amount);//账户可用余额
			finanaceEntry=new FinanaceEntry();
			finanaceEntry=this.getAccountFlow(transOrderInfo, balance, entryId, flag);
			finanaceEntry.setAccrualType(BaseConstants.TYPE_BALANCE_SETTLE);
			finanaceEntries.add(finanaceEntry);
		}
		//设置账户B需要的相关数据
		balanceB.setAmount(balanceB.getAmount()+amount);//账户余额
		balanceB.setBalanceSettle(balanceB.getBalanceSettle()+amount);//账户清算余额/可提现余额
		balanceB.setBalanceUsable(balanceB.getBalanceUsable()+amount);//账户可用余额
		finanaceEntry=new FinanaceEntry();
		finanaceEntry=this.getAccountFlow(transOrderInfo, balanceB, entryId, flag);
		finanaceEntry.setAccrualType(BaseConstants.TYPE_BALANCE_SETTLE);
		finanaceEntries.add(finanaceEntry);
		return finanaceEntries;
	}
	
	
	/**
	 * 获取指定一个套录号下多条流水中得账户ID数
	 * @param tradeList 多条流水
	 * @return 一个账户ID:0,一个以上账户ID:1,异常:-1
	 * */
	public int getAccountcount(List<FinanaceEntry> tradeList){
		String accountID = "";
		boolean flag = true;
		try {
			if (tradeList==null || tradeList.size() <=0) {
				logger.error("流水记录为空!");
				return -1;
			} else {
				for (FinanaceEntry finanaceEntry : tradeList) {
					if ("".equals(accountID)) {
						accountID = finanaceEntry.getFinAccountId();
					} else {
						if (!accountID.equals(finanaceEntry.getFinAccountId())) {
							flag = false;
						}
					}
				}
				if (flag == false) {
					return 1;
				} else {
					return 0;
				}
			}
		} catch (Exception e) {
			logger.error("获取套录号下的账户流水ID失败");
			return -1;
		}
	}
}
