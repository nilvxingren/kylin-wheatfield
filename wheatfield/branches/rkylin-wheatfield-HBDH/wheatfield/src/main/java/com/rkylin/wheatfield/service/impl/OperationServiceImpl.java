package com.rkylin.wheatfield.service.impl;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rkylin.common.RedisIdGenerator;
import com.rkylin.wheatfield.constant.AccountConstants;
import com.rkylin.wheatfield.constant.BaseConstants;
import com.rkylin.wheatfield.constant.Constants;
import com.rkylin.wheatfield.constant.TransCodeConst;
import com.rkylin.wheatfield.exception.AccountException;
import com.rkylin.wheatfield.manager.FinanaceAccountManager;
import com.rkylin.wheatfield.manager.ParameterInfoManager;
import com.rkylin.wheatfield.pojo.Balance;
import com.rkylin.wheatfield.pojo.FinanaceAccount;
import com.rkylin.wheatfield.pojo.FinanaceAccountQuery;
import com.rkylin.wheatfield.pojo.FinanaceEntry;
import com.rkylin.wheatfield.pojo.FinanaceEntryHistory;
import com.rkylin.wheatfield.pojo.ParameterInfo;
import com.rkylin.wheatfield.pojo.ParameterInfoQuery;
import com.rkylin.wheatfield.pojo.SettleTransTab;
import com.rkylin.wheatfield.pojo.TransOrderInfo;
import com.rkylin.wheatfield.pojo.User;
import com.rkylin.wheatfield.service.CheckInfoService;
import com.rkylin.wheatfield.service.OperationServive;
import com.rkylin.wheatfield.utils.DateUtil;

@Service
public class OperationServiceImpl implements OperationServive {
	private static Logger logger = LoggerFactory.getLogger(OperationServiceImpl.class);	
	
	@Autowired
	FinanaceAccountManager finanaceAccountManager;
	@Autowired
	CheckInfoService checkInfoService;	
	@Resource
	private RedisIdGenerator redisIdGenerator;
	@Autowired
	ParameterInfoManager parameterInfoManager;
	DateUtil dateUtil=new DateUtil();
	/** 
	 * 获取指定用户的账户ID
	 * */
	public String getUserAccount(User user){	
		String finAccountId=null;
		FinanaceAccountQuery financeAccountQuery=new FinanaceAccountQuery();
		financeAccountQuery.setAccountRelateId(user.userId);
		if(user.userType!=null){
			financeAccountQuery.setFinAccountTypeId(user.userType.getCategory());
		}else{
			financeAccountQuery.setFinAccountTypeId(user.uEType);
		}
		financeAccountQuery.setRootInstCd(user.constId);
		financeAccountQuery.setGroupManage(user.productId);
		financeAccountQuery.setReferUserId(user.referUserId);
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
		String transType=transOrderInfo.getFuncCode();
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
				rtn=TransCodeConst.THIRDPARTYID_FNZZH;
			}
		}else if (transType.equals(TransCodeConst.CREDIT_CONSUME)) {//信用消费
			if(flag){
				rtn=transOrderInfo.getUserId();
			}else{
				rtn=TransCodeConst.THIRDPARTYID_FNYFXYZH;
			}
		}else if(transType.equals(TransCodeConst.DEBIT_CONSUME)){//储蓄消费
			if(flag){
				rtn=transOrderInfo.getUserId();
			}else{
				rtn=TransCodeConst.THIRDPARTYID_QTYFKZH;
			}
		}else if (transType.equals(TransCodeConst.PAYMENT_WITHHOLD)) {//代付
			if(flag){
				rtn=transOrderInfo.getUserId();
			}else{
				rtn=TransCodeConst.THIRDPARTYID_FNZZH;
			}
		}else if(transType.equals(TransCodeConst.WITHDROW)){//提现
			if(flag){
				rtn=transOrderInfo.getUserId();
			}else{
				rtn=TransCodeConst.THIRDPARTYID_TXDQSZH;
			}
		}else if(transType.equals(TransCodeConst.ADVANCE)){//预付金
			if(flag){
				rtn=transOrderInfo.getInterMerchantCode();
			}else{
				rtn=TransCodeConst.THIRDPARTYID_FNZZHQY;
			}
		}else if(transType.equals(TransCodeConst.PRE_AUTHORIZATION)){//预授权
			if(flag){
				rtn=transOrderInfo.getUserId();
			}else{
				rtn=transOrderInfo.getInterMerchantCode();
			}
		}
		return rtn;
	}	
	/**
	 * 获取交易相关方Id
	 * @param tradeAtom 原子交易
	 * @param flag 交易源与交易目标的标记
	 * @return 返回交易相关方Id
	 * */
	public String getThirdPartyId(TransOrderInfo transOrderInfo , boolean flag){
		String rtn = null;
		String transType=transOrderInfo.getFuncCode();
		if(transType.equals(TransCodeConst.ADJUST_ACCOUNT_AMOUNT)){//内部转账交易
			//获取转入方的fin_account_Id
//			User user=new User();
//			user.constId=transOrderInfo.getMerchantCode();
//			user.productId=transOrderInfo.getErrorCode();
//			if(transOrderInfo.getTradeFlowNo()!=null&&!"".equals(transOrderInfo.getTradeFlowNo())){
//				user.productId=transOrderInfo.getTradeFlowNo();
//			}
			//rtn=this.getUserAccount(user);
			rtn="";
		}else if (transType.equals(TransCodeConst.CHARGE)) {//充值
			rtn=TransCodeConst.THIRDPARTYID_FNZZH;
		}else if (transType.equals(TransCodeConst.CREDIT_CONSUME)) {//信用消费
			rtn=TransCodeConst.THIRDPARTYID_FNYFXYZH;
		}else if(transType.equals(TransCodeConst.DEBIT_CONSUME)){//储蓄消费
			rtn=TransCodeConst.THIRDPARTYID_QTYFKZH;
		}else if (transType.equals(TransCodeConst.PAYMENT_COLLECTION)) {//代收付
			rtn=TransCodeConst.THIRDPARTYID_FNZZH;
		}else if (transType.equals(TransCodeConst.PAYMENT_WITHHOLD)) {//代扣
			rtn=TransCodeConst.THIRDPARTYID_FNZZH;
		}else if(transType.equals(TransCodeConst.WITHDROW)){//提现
			rtn=TransCodeConst.THIRDPARTYID_TXDQSZH;
		}
		return rtn;
	}	
	/**
	 * 获取记账方向
	 * @param tradeType 交易类型
	 * @param flag 交易源与交易目标的标记          另一种情况：true=CREDIT_TYPE  flase=DEBIT_TYPE
	 * @return 返回不同交易类型中的DC标志
	 * */
	public int getDorC(String transType , boolean flag){
		int rtn=0;
		if(transType.equals(TransCodeConst.ADJUST_ACCOUNT_AMOUNT)){//内部转账交易
			if(flag){
				rtn = BaseConstants.CREDIT_TYPE;
			}else{
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
		}else if(transType.equals(TransCodeConst.WITHDROW)){//提现
			if(flag){
				rtn = BaseConstants.DEBIT_TYPE;
			}
			else{
				rtn = BaseConstants.CREDIT_TYPE;
			}
		}else if(transType.equals(TransCodeConst.FROZON)){//冻结
			if(flag){
				rtn = BaseConstants.CREDIT_TYPE;
			}
			else{
				rtn = BaseConstants.DEBIT_TYPE;
			}
		}else if(transType.equals(TransCodeConst.CREDIT_CONSUME)){//信用消费
			if(flag){
				rtn=BaseConstants.DEBIT_TYPE;
			}else {
				rtn=BaseConstants.CREDIT_TYPE;
			}
		}else if(transType.equals(TransCodeConst.DEBIT_CONSUME)){//储蓄消费
			if(flag){
				rtn=BaseConstants.DEBIT_TYPE;
			}else {
				rtn=BaseConstants.CREDIT_TYPE;
			}
		}else{
			if(flag){
				rtn = BaseConstants.CREDIT_TYPE;
			}
			else{
				rtn = BaseConstants.DEBIT_TYPE;
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
	
	public FinanaceEntry getAccountFlow(TransOrderInfo transOrderInfo, Balance balance, String entryId,boolean flag){
		FinanaceEntry rtnFlow=new FinanaceEntry();
		String finEntryId =redisIdGenerator.createFINEntryrRequestNo();
		if(null != finEntryId){
			rtnFlow.setFinanaceEntryId(finEntryId);//设置流水主键
			//获取记账日期
			String accountDateString=this.getAccountDate();
			if(accountDateString==null||accountDateString.equals("")){
				return null;
			}				
			try {
				rtnFlow.setAccountDate(dateUtil.parse(accountDateString, "yyyy-MM-dd"));
			} catch (ParseException e) {
				e.printStackTrace();
			}//设置记账日期
			String accountId=balance.getFinAccountId();//获取账户ID
			
			if(null != accountId){
				
				rtnFlow.setFinAccountId(accountId);//设置账户ID
				rtnFlow.setReferEntryId(entryId);//设置套录号
				
				String tradeType = transOrderInfo.getFuncCode();
				
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
				
				//************************************************************
				if (Integer.valueOf(transOrderInfo.getFuncCode())==BaseConstants.REVERSE_TYPE){
					//冲正交易
					rtnFlow.setReverseFlag(BaseConstants.REVERSE_TYPE);
				}
				else{
					//非冲正交易
					rtnFlow.setReverseFlag(BaseConstants.NOREVERSE_TYPE);
				}
				
				//rtnFlow.setPartyIdFrom(transOrderInfo.getUserId());//设置交易发起方
				rtnFlow.setPartyIdFrom(balance.getFinAccountId());//设置交易发起方
				rtnFlow.setReferId(transOrderInfo.getOrderNo());//记账凭证号
				/*暂时定义为订单来源*/
				rtnFlow.setReferFrom(String.valueOf(BaseConstants.REFER_TRADE));//设置凭证来源
				rtnFlow.setPaymentAmount(transOrderInfo.getAmount());//获取交易发生额
				rtnFlow.setTransDate(transOrderInfo.getRequestTime());//设置交易日期
				rtnFlow.setThirdPartyId(this.getThirdPartyId(transOrderInfo,flag));//交易相关方
				if (TransCodeConst.FROZEN.equals(transOrderInfo.getFuncCode())) {
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
				rtnFlow.setPulseTime(String.valueOf(new Date().getTime()));//设置账户心跳时间
				rtnFlow.setPulseDegree(balance.getPulseDegree());//账户心跳计次
			}
			else{
				logger.error("获取用户["+accountId+"的账户ID失败");
				rtnFlow = null;
			}
		}
		
		return rtnFlow;
	}
	/**
	 * 返回一条账户流水实例 -内部使用
	 * @param tradeAtom 原子交易
	 * @param userBalance 用户余额
	 * @param entryId 套录号
	 * @param flag 交易源与交易目标的标志
	 * @return 账户流水实例
	 * */
	private FinanaceEntry getAccountInternalFlow(FinanaceEntry finanaceEntry, Balance balance, boolean flag,String userId){
		FinanaceEntry rtnFlow=new FinanaceEntry();
		String finEntryId =redisIdGenerator.createFINEntryrRequestNo();
		if(null != finEntryId){
			rtnFlow.setFinanaceEntryId(finEntryId);//设置流水主键
			//获取记账日期
			String accountDateString=this.getAccountDate();
			if(accountDateString==null||accountDateString.equals("")){
				return null;
			}				
			try {
				rtnFlow.setAccountDate(dateUtil.parse(accountDateString, "yyyy-MM-dd"));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}//设置记账日期
			String accountId=balance.getFinAccountId();
			if(null != accountId){
				rtnFlow.setFinAccountId(accountId);	
				if(finanaceEntry.getDirection()!=null){
					rtnFlow.setDirection(finanaceEntry.getDirection());//设置DC标志
				}else{
					rtnFlow.setDirection(this.getDorC("", flag));//设置DC标志
				}				
				
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
				//交易金额
				rtnFlow.setPaymentAmount(finanaceEntry.getPaymentAmount());
				//************************************************************
				
				//rtnFlow.setPartyIdFrom(userId);//设置交易发起方
				rtnFlow.setPartyIdFrom(balance.getFinAccountId());//设置交易发起方
				rtnFlow.setReferFrom(String.valueOf(BaseConstants.REFER_FLOW));//设置凭证来源
				rtnFlow.setReferId(finanaceEntry.getReferId());//记账凭证号
				try {
					rtnFlow.setTransDate(dateUtil.parse(dateUtil.getDate(), "yyyy-MM-dd"));
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}//设置交易日期
				rtnFlow.setReverseFlag(BaseConstants.NOREVERSE_TYPE);
				rtnFlow.setYourNotes(finanaceEntry.getYourNotes());
				rtnFlow.setHisNote("");//他方摘要
				rtnFlow.setRemark(finanaceEntry.getRemark());//备注
				rtnFlow.setRecordMap(finanaceEntry.getRecordMap());//安全码
				
				rtnFlow.setPulseTime(String.valueOf(new Date().getTime()));//设置账户心跳时间
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
	 * 获取账期
	 * @return
	 */
	public String getAccountDate(){
		String accountDate=null;
		ParameterInfoQuery query=new ParameterInfoQuery();
		query.setParameterCode(TransCodeConst.ACCOUNTDATE_CODE);
		List<ParameterInfo> parameterInfos=parameterInfoManager.queryList(query);
		if(parameterInfos!=null&&parameterInfos.size()>0){
			accountDate=parameterInfos.get(0).getParameterValue();
		}
		return accountDate;
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
		if(flag){
			if(transOrderInfo.getErrorCode()!=null){
				if(!transOrderInfo.getErrorCode().equals(TransCodeConst.ORDER_REFUND)){
					amount=transOrderInfo.getOrderAmount();
				}
			}else{
				if(transOrderInfo.getMerchantCode().equals(Constants.JRD_ID)|| transOrderInfo.getMerchantCode().equals(Constants.TXYW_ID)){
					amount=transOrderInfo.getAmount();
				}else{
					amount=transOrderInfo.getOrderAmount();
				}
			}
			balance.setAmount(balance.getAmount()-amount);//账户余额
			balance.setBalanceUsable(balance.getBalanceUsable()-amount);//账户可用余额
			balance.setBalanceSettle(balance.getBalanceSettle()-amount);//账户清余额
			finanaceEntry=this.getAccountFlow(transOrderInfo, balance, entryId, flag);
			finanaceEntry.setAccrualType(BaseConstants.TYPE_BALANCE_SETTLE);//设置发生额类型为可提现余额
			finanaceEntry.setPaymentAmount(amount);
			finanaceEntries.add(finanaceEntry);
			return finanaceEntries;
		}else{
			balance.setAmount(balance.getAmount()+amount);//账户余额
			balance.setBalanceUsable(balance.getBalanceUsable()+amount);//账户可用余额
			balance.setBalanceSettle(balance.getBalanceSettle()+amount);//账户清余额
			finanaceEntry=this.getAccountFlow(transOrderInfo, balance, entryId, flag);
			finanaceEntry.setAccrualType(BaseConstants.TYPE_BALANCE_SETTLE);//设置发生额类型为可提现余额
			finanaceEntries.add(finanaceEntry);
			return finanaceEntries;
		}
		
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
		/*if(transOrderInfo.getBusiTypeId().equals(String.valueOf(BaseConstants.CREDIT_TYPE))){
			 增加贷记余额 
			balance.setBalanceCredit(balance.getBalanceCredit()+amount);
		}else{
			 非贷记发生额的时候增加可提现余额 
			balance.setBalanceSettle(balance.getBalanceSettle()+amount);
		}*/
		 //非贷记发生额的时候增加可提现余额 
		balance.setBalanceSettle(balance.getBalanceSettle()+amount);
		finanaceEntry=this.getAccountFlow(transOrderInfo, balance, entryId, flag);
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
		if(transOrderInfo.getFuncCode().equals(String.valueOf(TransCodeConst.CREDIT_CONSUME))){//信用消费
			if(flag){
				if(amount > balance.getBalanceSettle()){
					logger.info("用户[" + transOrderInfo.getUserId() + "]订单金额大于用户贷记余额");
					return null;
				}
				//贷记余额
				balance.setBalanceSettle(balance.getBalanceSettle()-amount);
				//扣款订单中的交易源
				balance.setAmount(balance.getAmount()-amount);//账户余额
				balance.setBalanceUsable(balance.getBalanceUsable()-amount);//账户可用余额
			}else{
				//提现余额
				balance.setBalanceSettle(balance.getBalanceSettle()+amount);
				balance.setAmount(balance.getAmount()+amount);//账户余额
				balance.setBalanceUsable(balance.getBalanceUsable()+amount);//账户可用余额
			}
			finanaceEntry=this.getAccountFlow(transOrderInfo, balance, entryId, flag);
			if(flag){
				finanaceEntry.setAccrualType(BaseConstants.TYPE_BALANCE_CREDIT);
			}else{
				finanaceEntry.setAccrualType(BaseConstants.TYPE_BALANCE_SETTLE);
			}
			
		}else if(transOrderInfo.getFuncCode().equals(String.valueOf(TransCodeConst.DEBIT_CONSUME))){//储蓄消费
			
			if(flag){
				if(amount > balance.getBalanceUsable()){
					logger.info("用户[" + transOrderInfo.getUserId() + "]订单金额大于用户可用余额");
					return null;
				}
				balance.setBalanceSettle(balance.getBalanceSettle()-amount);
				balance.setAmount(balance.getAmount()-amount);//账户余额
				balance.setBalanceUsable(balance.getBalanceUsable()-amount);//账户可用余额
			}else{
				balance.setBalanceSettle(balance.getBalanceSettle()+amount);
				balance.setAmount(balance.getAmount()+amount);//账户余额
				balance.setBalanceUsable(balance.getBalanceUsable()+amount);//账户可用余额
			}
			
			finanaceEntry=this.getAccountFlow(transOrderInfo, balance, entryId, flag);
			finanaceEntry.setAccrualType(BaseConstants.TYPE_BALANCE_SETTLE);//设置发生额类型为清算余额
		}
		//中间商户号
		//获取finAccountId
		User user=new User();
		user.userId=transOrderInfo.getInterMerchantCode();
		user.uEType=AccountConstants.ACCOUNT_TYPE_BASE;
		String finAccountId=this.getUserAccount(user);
		finanaceEntry.setThirdPartyId(finAccountId);
		finanaceEntries.add(finanaceEntry);
		
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
			logger.error("账户"+transOrderInfo.getUserId()+"的可转账余额小于"+amount);
			
		}else{
			balance.setBalanceSettle(balance.getBalanceSettle()-amount);//清算余额
			balance.setAmount(balance.getAmount()-amount);//账户余额
			balance.setBalanceUsable(balance.getBalanceUsable()-amount);//账户可用余额
			finanaceEntry=new FinanaceEntry();
			finanaceEntry=this.getAccountFlow(transOrderInfo, balance, entryId, flag);
			finanaceEntry.setAccrualType(BaseConstants.TYPE_BALANCE_SETTLE);
			finanaceEntries.add(finanaceEntry);
			
			//设置账户B需要的相关数据
			balanceB.setPulseDegree(balanceB.getPulseDegree()+1);//心跳计次加一
			balanceB.setAmount(balanceB.getAmount()+amount);//账户余额
			balanceB.setBalanceSettle(balanceB.getBalanceSettle()+amount);//账户清算余额/可提现余额
			balanceB.setBalanceUsable(balanceB.getBalanceUsable()+amount);//账户可用余额
			finanaceEntry=new FinanaceEntry();
			flag=true;
			finanaceEntry=this.getAccountFlow(transOrderInfo, balanceB, entryId, flag);
			finanaceEntry.setAccrualType(BaseConstants.TYPE_BALANCE_SETTLE);
			finanaceEntries.add(finanaceEntry);
		}
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
	@Override
	public int getAccountcountByEntryHistory(List<FinanaceEntryHistory> tradeList) {
		String accountID = "";
		boolean flag = true;
		try {
			if (tradeList==null || tradeList.size() <=0) {
				logger.error("流水记录为空!");
				return -1;
			} else {
				for (FinanaceEntryHistory finanaceEntry : tradeList) {
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
	
	@Override
	public boolean checkAccount(User user) {
		FinanaceAccountQuery query=new FinanaceAccountQuery();
		query.setRootInstCd(user.constId);
		query.setAccountRelateId(user.userId);
		query.setGroupManage(user.productId);
		query.setFinAccountTypeId(user.uEType);
		query.setReferUserId(user.referUserId);
		query.setStatusId(BaseConstants.ACCOUNT_STATUS_OK);
		List<FinanaceAccount> faList=finanaceAccountManager.queryList(query);
		boolean is_success=false;
		if(faList!=null && faList.size()==1){
			FinanaceAccount fa=faList.get(0);
			String statusId=fa.getStatusId();
			if(BaseConstants.ACCOUNT_STATUS_OK.equals(statusId)){
				is_success=true;
			}
		}
		return is_success;
	}
	
	@Override
	public List<FinanaceEntry> getCredit(FinanaceEntry finanaceEntry,Balance balance, boolean flag,String userId) {
		// TODO 授信
		long amount=finanaceEntry.getPaymentAmount();
		List<FinanaceEntry> finanaceEntries = new ArrayList<FinanaceEntry>();
//		if(flag){
//			//贷记额度
//			balance.setBalanceCredit(balance.getBalanceCredit()+amount);
//		}else{
			//提现额度
			balance.setBalanceSettle(balance.getBalanceSettle()+amount);
			flag=true;
//		}
		//账户余额
		balance.setAmount(balance.getAmount()+amount);
		//可用余额
		balance.setBalanceUsable(balance.getBalanceUsable()+amount);
		finanaceEntry.setReverseFlag(BaseConstants.NOREVERSE_TYPE);
		finanaceEntry=this.getAccountInternalFlow(finanaceEntry, balance, flag, userId);
		finanaceEntries.add(finanaceEntry);
		return finanaceEntries;
	}
	@Override
	public List<FinanaceEntry> getYuFuJin(FinanaceEntry finanaceEntry,Balance balance, boolean flag, String userId) {
		// TODO 预付金
		long amount=finanaceEntry.getPaymentAmount();
		List<FinanaceEntry> finanaceEntries = new ArrayList<FinanaceEntry>();
		//可提现余额
		balance.setBalanceSettle(balance.getBalanceSettle()+amount);
		//账户余额
		balance.setAmount(balance.getAmount()+amount);
		//可用余额
		balance.setBalanceUsable(balance.getBalanceUsable()+amount);
		finanaceEntry.setReverseFlag(BaseConstants.NOREVERSE_TYPE);
		finanaceEntry=this.getAccountInternalFlow(finanaceEntry, balance, flag, userId);
		finanaceEntries.add(finanaceEntry);
		return finanaceEntries;
	}
	
	@Override
	public List<FinanaceEntry> getWithdrawReturn(FinanaceEntry finanaceEntry,Balance balance,  boolean flag,String userId) {
		// TODO 提现返回
		long amount=finanaceEntry.getPaymentAmount();
		if(amount>balance.getBalanceSettle()){
			logger.error("账户可提现余额不足");
			return null;
		}
		List<FinanaceEntry> finanaceEntries = new ArrayList<FinanaceEntry>();
		balance.setBalanceSettle(balance.getBalanceSettle()-amount);
		balance.setAmount(balance.getAmount()-amount);
		balance.setBalanceUsable(balance.getBalanceUsable()-amount);
		finanaceEntry.setReverseFlag(BaseConstants.NOREVERSE_TYPE);
		finanaceEntry=this.getAccountInternalFlow(finanaceEntry, balance, flag, userId);
		finanaceEntries.add(finanaceEntry);
		return finanaceEntries;
	}
	@Override
	public List<FinanaceEntry> getRefundReturn(FinanaceEntry finanaceEntry,
			Balance balance, boolean flag,String userId) {
		// TODO 还款返回
		long amount=finanaceEntry.getPaymentAmount();
		List<FinanaceEntry> finanaceEntries = new ArrayList<FinanaceEntry>();
		
		finanaceEntry.setReverseFlag(BaseConstants.NOREVERSE_TYPE);
		if(flag){
			balance.setBalanceSettle(balance.getBalanceSettle() + amount);
			//贷记余额
			//balance.setBalanceCredit(balance.getBalanceCredit()+amount);
			//可用余额
			balance.setBalanceUsable(balance.getBalanceUsable()+amount);
			//账户余额
			balance.setAmount(balance.getAmount()+amount);
			finanaceEntry=this.getAccountInternalFlow(finanaceEntry, balance, flag, userId);
			finanaceEntries.add(finanaceEntry);
		}else {
			flag=true;
			User user=null;
			for (int i = 0; i <3; i++) {
				if(i==0){//丰年贴息成本账户  默认
					//提现余额
					balance.setBalanceSettle(balance.getBalanceSettle()+amount);
					//可用余额
					balance.setBalanceUsable(balance.getBalanceUsable()+amount);
					//账户余额
					balance.setAmount(balance.getAmount()+amount);
					finanaceEntry=this.getAccountInternalFlow(finanaceEntry, balance, flag, userId);
				}else if(i==1){//丰年预付信用账户
					user=new User();
					balance=new Balance();
					//user.payType=String.valueOf(BaseConstants.TYPE_BALANCE_OVERLIMIT);
					balance=checkInfoService.getBalance(user,TransCodeConst.THIRDPARTYID_FNYFXYZH);
					//提现余额
					balance.setBalanceSettle(balance.getBalanceSettle()+amount);
					//可用余额
					balance.setBalanceUsable(balance.getBalanceUsable()+amount);
					//账户余额
					balance.setAmount(balance.getAmount()+amount);
					//心跳计次
					balance.setPulseDegree(balance.getPulseDegree()+1);
					finanaceEntry=this.getAccountInternalFlow(finanaceEntry, balance, flag, userId);
				}else if(i==2){//P2P账户余额
					user=new User();
					balance=new Balance();
					//user.payType=String.valueOf(BaseConstants.TYPE_BALANCE_OVERLIMIT);
					balance=checkInfoService.getBalance(user,TransCodeConst.THIRDPARTYID_P2PZZH);
					//提现余额
					balance.setBalanceSettle(balance.getBalanceSettle()+amount);
					//可用余额
					balance.setBalanceUsable(balance.getBalanceUsable()+amount);
					//账户余额
					balance.setAmount(balance.getAmount()+amount);
					//心跳计次
					balance.setPulseDegree(balance.getPulseDegree()+1);
					finanaceEntry=this.getAccountInternalFlow(finanaceEntry, balance, flag, userId);
				}
				finanaceEntries.add(finanaceEntry);
			}
		}
		
		return finanaceEntries;
	}
	@Override
	public List<FinanaceEntry> getFenRun(FinanaceEntry finanaceEntry,Balance balance, boolean flag, String userId) {
		// TODO 分润
		long amount=finanaceEntry.getPaymentAmount();//获取交易金额
		List<FinanaceEntry> finanaceEntries = new ArrayList<FinanaceEntry>();
		if(flag){
			//可提现余额
			balance.setBalanceSettle(balance.getBalanceSettle()+amount);
			//账户余额
			balance.setAmount(balance.getAmount()+amount);
			//可用余额
			balance.setBalanceUsable(balance.getBalanceUsable()+amount);
		}else{
			if(balance.getBalanceSettle() < amount){
				logger.info("账户可提现金额:" + balance.getBalanceSettle() + " 小于订单金额");
				return null;
			}
			//可提现余额
			balance.setBalanceSettle(balance.getBalanceSettle()-amount);
			//账户余额
			balance.setAmount(balance.getAmount()-amount);
			//可用余额
			balance.setBalanceUsable(balance.getBalanceUsable()-amount);
		}
		finanaceEntry.setReverseFlag(BaseConstants.NOREVERSE_TYPE);
		finanaceEntry=this.getAccountInternalFlow(finanaceEntry, balance, flag, userId);
		finanaceEntries.add(finanaceEntry);
		return finanaceEntries;
	}
	
	@Override
	public List<FinanaceEntry> getRightsPackageReturn(FinanaceEntry finanaceEntry, Balance balance, boolean flag,String userId) {
		// TODO 债权包返回
		long amount=finanaceEntry.getPaymentAmount();//获取交易金额
		List<FinanaceEntry> finanaceEntries = new ArrayList<FinanaceEntry>();
		//提现余额
		balance.setBalanceSettle(balance.getBalanceSettle()+amount);
		//账户余额
		balance.setAmount(balance.getAmount()+amount);
		//可用余额
		balance.setBalanceUsable(balance.getBalanceUsable()+amount);
		finanaceEntry.setReverseFlag(BaseConstants.NOREVERSE_TYPE);
		finanaceEntry=this.getAccountInternalFlow(finanaceEntry, balance, flag, userId);
		finanaceEntries.add(finanaceEntry);
		return finanaceEntries;
	}
	@Override
	public List<FinanaceEntry> getCollectionReturn(FinanaceEntry finanaceEntry,Balance balance, boolean flag, String userId) {
		// TODO 代收返回
		long amount=finanaceEntry.getPaymentAmount();//获取交易金额
		List<FinanaceEntry> finanaceEntries = new ArrayList<FinanaceEntry>();
		if(flag){
			//提现余额
			balance.setBalanceSettle(balance.getBalanceSettle()+amount);
			//账户余额
			balance.setAmount(balance.getAmount()+amount);
			//可用余额
			balance.setBalanceUsable(balance.getBalanceUsable()+amount);
		}else{
			//提现余额
			balance.setBalanceSettle(balance.getBalanceSettle()-amount);
			//账户余额
			balance.setAmount(balance.getAmount()-amount);
			//可用余额
			balance.setBalanceUsable(balance.getBalanceUsable()-amount);
		}		
		finanaceEntry.setReverseFlag(BaseConstants.NOREVERSE_TYPE);
		finanaceEntry=this.getAccountInternalFlow(finanaceEntry, balance, flag, userId);
		finanaceEntries.add(finanaceEntry);
		return finanaceEntries;
	}
	@Override
	public List<FinanaceEntry> getWithholdReturn(FinanaceEntry finanaceEntry,Balance balance, boolean flag, String userId) {
		// TODO 待付返回
		long amount=finanaceEntry.getPaymentAmount();//获取交易金额
		List<FinanaceEntry> finanaceEntries = new ArrayList<FinanaceEntry>();
		if(flag){
			//提现余额
			balance.setBalanceSettle(balance.getBalanceSettle()+amount);
			//账户余额
			balance.setAmount(balance.getAmount()+amount);
			//可用余额
			balance.setBalanceUsable(balance.getBalanceUsable()+amount);
		}else{
			//提现余额
			balance.setBalanceSettle(balance.getBalanceSettle()-amount);
			//账户余额
			balance.setAmount(balance.getAmount()-amount);
			//可用余额
			balance.setBalanceUsable(balance.getBalanceUsable()-amount);
		}		
		finanaceEntry.setReverseFlag(BaseConstants.NOREVERSE_TYPE);
		finanaceEntry=this.getAccountInternalFlow(finanaceEntry, balance, flag, userId);
		finanaceEntries.add(finanaceEntry);
		return finanaceEntries;
	}
	
	
	@Override
	public FinanaceEntry addReverseTrade(FinanaceEntry finanaceEn,Balance balance, String newEntryId, String reverseTime) {
		FinanaceEntry rtnFlow = new FinanaceEntry();
		/* 从原始map中移出最后更新时间和记录创建时间 */
		rtnFlow.setCreatedTime(new Date());
		rtnFlow.setUpdatedTime(new Date());
		
		//更新冲正操作的套录号
		rtnFlow.setReferEntryId(newEntryId);
		//更新冲正记账日期
		try {
			rtnFlow.setAccountDate(dateUtil.parse(reverseTime, "yyyy-MM-dd") );
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		//更新冲正状态标识
		rtnFlow.setReverseFlag(BaseConstants.REVERSE_TYPE);
		//借贷标识互换
		rtnFlow.setDirection(this.D2C(finanaceEn.getDirection()));//设置DC标志
		try {
			String taFinEntryKey = redisIdGenerator.createRequestNo();
			if(null == taFinEntryKey){
				logger.error("冲正交易中获取FinanaceEntry的主键失败");
			}
			else{
				//修改凭证源为元账目的流水号
				rtnFlow.setFinanaceEntryId(taFinEntryKey);//设置流水主键
				rtnFlow.setFinAccountId(finanaceEn.getFinAccountId());//设置账户ID
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
				
				
				//更新冲正状态标识				
				rtnFlow.setReferId(finanaceEn.getFinanaceEntryId());//记账凭证号
				rtnFlow.setReferFrom(String.valueOf(BaseConstants.REFER_REVERSAL));//设置凭证来源
				rtnFlow.setPaymentAmount(finanaceEn.getPaymentAmount());//获取交易发生额
				rtnFlow.setTransDate(new Date());//设置交易日期
				rtnFlow.setAccrualType(finanaceEn.getAccrualType());
				//rtnFlow.setPartyIdFrom(finanaceEn.getPartyIdFrom());//设置交易发起方
				rtnFlow.setPartyIdFrom(balance.getFinAccountId());//设置交易发起方
				rtnFlow.setThirdPartyId(finanaceEn.getThirdPartyId());//交易相关方
				rtnFlow.setMyNotes(finanaceEn.getMyNotes());//我方摘要
				rtnFlow.setYourNotes(finanaceEn.getYourNotes());//你方摘要
				rtnFlow.setHisNote(finanaceEn.getHisNote());//他方摘要
				rtnFlow.setRemark(finanaceEn.getRemark());//其他备注
				rtnFlow.setRecordMap(finanaceEn.getRecordMap());//设置安全码
				rtnFlow.setPulseTime(dateUtil.getTime());//设置账户心跳时间
				rtnFlow.setPulseDegree(balance.getPulseDegree());//账户心跳计次
			}
		} catch (Exception e) {
			logger.error("[addReverseTrade]插入交易的冲正账目失败");
		}
		return rtnFlow;
	}
	@Override
	public FinanaceEntry addReverseTradeHistory(FinanaceEntryHistory finanaceEn, Balance balance,String newEntryId, String reverseTime) {
		FinanaceEntry rtnFlow = new FinanaceEntry();
		/* 从原始map中移出最后更新时间和记录创建时间 */
		rtnFlow.setCreatedTime(new Date());
		rtnFlow.setUpdatedTime(new Date());
		
		//更新冲正操作的套录号
		rtnFlow.setReferEntryId(newEntryId);
		//更新冲正记账日期
		try {
			rtnFlow.setAccountDate(dateUtil.parse(reverseTime, "yyyy-MM-dd") );
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		//更新冲正状态标识
		rtnFlow.setReverseFlag(BaseConstants.REVERSE_TYPE);
		//借贷标识互换
		rtnFlow.setDirection(this.D2C(finanaceEn.getDirection()));//设置DC标志
		try {
			String taFinEntryKey = redisIdGenerator.createRequestNo();
			if(null == taFinEntryKey){
				logger.error("冲正交易中获取FinanaceEntry的主键失败");
			}
			else{
				rtnFlow.setFinanaceEntryId(taFinEntryKey);//设置流水主键
				rtnFlow.setFinAccountId(finanaceEn.getFinAccountId());//设置账户ID
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
				
				
				//更新冲正状态标识				
				rtnFlow.setReferId(finanaceEn.getFinanaceEntryId());//记账凭证号
				rtnFlow.setReferFrom(String.valueOf(BaseConstants.REFER_REVERSAL));//设置凭证来源
				rtnFlow.setPaymentAmount(finanaceEn.getPaymentAmount());//获取交易发生额
				rtnFlow.setTransDate(new Date());//设置交易日期
				rtnFlow.setAccrualType(finanaceEn.getAccrualType());
				rtnFlow.setPartyIdFrom(finanaceEn.getPartyIdFrom());//设置交易发起方
				rtnFlow.setThirdPartyId(finanaceEn.getThirdPartyId());//交易相关方
				rtnFlow.setMyNotes(finanaceEn.getMyNotes());//我方摘要
				rtnFlow.setYourNotes(finanaceEn.getYourNotes());//你方摘要
				rtnFlow.setHisNote(finanaceEn.getHisNote());//他方摘要
				rtnFlow.setRemark(finanaceEn.getRemark());//其他备注
				rtnFlow.setRecordMap(finanaceEn.getRecordMap());//设置安全码
				rtnFlow.setPulseTime(dateUtil.getTime());//设置账户心跳时间
				rtnFlow.setPulseDegree(balance.getPulseDegree());//账户心跳计次
			}
		} catch (Exception e) {
			logger.error("插入交易的冲正账目失败");
		}
		return rtnFlow;
	}
	
	@Override
	public List<FinanaceEntry> getFreezeAccount(TransOrderInfo transOrderInfo,Balance balance, String entryId, boolean flag) {
		// TODO 冻结交易流水
		/**步骤：
		 * 1.获取订单中的冻结金额
		 * 2.获取账户中的可提现金额
		 * 3.提现余额大于等于订单冻结金额   将提现余额转入该账户中的冻结余额
		 * 	  提现余额小于订单冻结金额   提示  订单冻结余额不能大于账户可提现余额
		 */
		long freezeMoney=transOrderInfo.getAmount();//订单冻结余额
		long balanceSettle=balance.getBalanceSettle();//获取账户可提现余额
		List<FinanaceEntry> finanaceEntries = new ArrayList<FinanaceEntry>();
		if(freezeMoney>balanceSettle){
			return null;
		}else{
			flag=false;
			FinanaceEntry finanaceEntry=new FinanaceEntry();
			balance.setPulseDegree(balance.getPulseDegree()+1);
			balance.setAmount(balance.getAmount()-freezeMoney);//账户总余额
			balance.setBalanceUsable(balance.getBalanceUsable()-freezeMoney);//账户可用余额
			balance.setBalanceSettle(balance.getBalanceSettle()-freezeMoney);//账户可提现余额
			//设置账户提现余额流水			
			finanaceEntry.setReverseFlag(BaseConstants.NOREVERSE_TYPE);
			finanaceEntry=this.getAccountFlow(transOrderInfo, balance, entryId, flag);
			finanaceEntries.add(finanaceEntry);
			//设置账户冻结余额流水
			flag=true;
			balance.setPulseDegree(balance.getPulseDegree()+1);
			balance.setAmount(balance.getAmount()+freezeMoney);//账户总余额
			balance.setBalanceFrozon(balance.getBalanceFrozon()+freezeMoney);//账户冻结余额
			finanaceEntry=this.getAccountFlow(transOrderInfo, balance, entryId, flag);
			finanaceEntries.add(finanaceEntry);			
		}
		return finanaceEntries;
	}
	@Override
	public List<FinanaceEntry> getFrozenAccount(TransOrderInfo transOrderInfo,Balance balance, String entryId, boolean flag) {
		// TODO 解冻交易流水
		long freezeMoney=transOrderInfo.getAmount();//订单解冻余额
		if(freezeMoney>balance.getBalanceFrozon()){
			freezeMoney=balance.getBalanceFrozon();
		}
		List<FinanaceEntry> finanaceEntries = new ArrayList<FinanaceEntry>();
		flag=false;
		FinanaceEntry finanaceEntry=new FinanaceEntry();
		balance.setPulseDegree(balance.getPulseDegree()+1);
		balance.setAmount(balance.getAmount()-freezeMoney);//账户总余额
		
		balance.setBalanceFrozon(balance.getBalanceFrozon()-freezeMoney);//账户冻结余额
		//设置账户提现余额流水			
		finanaceEntry.setReverseFlag(BaseConstants.NOREVERSE_TYPE);
		finanaceEntry=this.getAccountFlow(transOrderInfo, balance, entryId, flag);
		finanaceEntries.add(finanaceEntry);
		//设置账户冻结余额流水
		flag=true;
		balance.setPulseDegree(balance.getPulseDegree()+1);
		balance.setAmount(balance.getAmount()+freezeMoney);//账户总余额
		balance.setBalanceUsable(balance.getBalanceUsable()+freezeMoney);//账户可用余额
		balance.setBalanceSettle(balance.getBalanceSettle()+freezeMoney);//账户可提现余额
		finanaceEntry=this.getAccountFlow(transOrderInfo, balance, entryId, flag);
		finanaceEntries.add(finanaceEntry);			
		return finanaceEntries;
	}
	@Override
	public List<FinanaceEntry> getCollection(TransOrderInfo transOrderInfo,Balance balance, String entryId, boolean flag) {
		// TODO 代收分录流水
		long orderAmount=transOrderInfo.getAmount();//获取订单金额
		
		List<FinanaceEntry> finanaceEntries = new ArrayList<FinanaceEntry>();
		FinanaceEntry finanaceEntry=new FinanaceEntry();
		balance.setAmount(balance.getAmount()+orderAmount);//账户总余额
		balance.setBalanceUsable(balance.getBalanceUsable()+orderAmount);//账户可用余额
		balance.setBalanceSettle(balance.getBalanceSettle()+orderAmount);//账户可提现余额
		finanaceEntry.setReverseFlag(BaseConstants.NOREVERSE_TYPE);
		finanaceEntry=this.getAccountFlow(transOrderInfo, balance, entryId, flag);
		finanaceEntries.add(finanaceEntry);
		return finanaceEntries;
	}
	@Override
	public List<FinanaceEntry> getWithhold(TransOrderInfo transOrderInfo,Balance balance, String entryId, boolean flag) {
		// TODO 代付分录流水
		long orderAmount=transOrderInfo.getAmount();//获取订单金额
		
		List<FinanaceEntry> finanaceEntries = new ArrayList<FinanaceEntry>();
		FinanaceEntry finanaceEntry=new FinanaceEntry();
		if(flag){
			balance.setAmount(balance.getAmount()+orderAmount);//账户总余额
			balance.setBalanceUsable(balance.getBalanceUsable()+orderAmount);//账户可用余额
			balance.setBalanceSettle(balance.getBalanceSettle()+orderAmount);//账户可提现余额
		}else{
			balance.setAmount(balance.getAmount()-orderAmount);//账户总余额
			balance.setBalanceUsable(balance.getBalanceUsable()-orderAmount);//账户可用余额
			balance.setBalanceSettle(balance.getBalanceSettle()-orderAmount);//账户可提现余额
		}		
		finanaceEntry.setReverseFlag(BaseConstants.NOREVERSE_TYPE);
		finanaceEntry=this.getAccountFlow(transOrderInfo, balance, entryId, flag);
		finanaceEntries.add(finanaceEntry);
		return finanaceEntries;
	}
	/**
	 * 互换借贷标记
	 * @param str 记账标记值
	 * @return 借贷相反值
	 * */
	public int D2C(int dic){
		int rtn=-1;
		if(BaseConstants.DEBIT_TYPE==dic){
			rtn = BaseConstants.CREDIT_TYPE;
		}
		else if(BaseConstants.CREDIT_TYPE==dic){
			rtn = BaseConstants.DEBIT_TYPE;
		}
		else{
			logger.error("指定的借贷标示["+rtn+"]非法");
		}
		return rtn;
	}
	@Override
	public List<SettleTransTab> getSettleTransTabs(TransOrderInfo transOrderInfo) {
		List<SettleTransTab> settleTransTabs=new ArrayList<SettleTransTab>();
		try {
			for (int i = 0; i <=1 ; i++) {//i==0时为商户挂起的金额   i==1时为台长挂起的金额，前提是referUserId有值
				SettleTransTab settleTransTab=new SettleTransTab();
				if(0==i){
					settleTransTab.setOrderNo(transOrderInfo.getOrderNo());
					settleTransTab.setTabNo(transOrderInfo.getOrderNo()+"000"+(i+1));
					settleTransTab.setRootInstCd(transOrderInfo.getMerchantCode());
					settleTransTab.setGroupManage(transOrderInfo.getErrorCode());//用交易订单表中的错误编码存储管理分组
					settleTransTab.setBatchNo("");
					settleTransTab.setUserId(transOrderInfo.getUserId());
					settleTransTab.setReferUserId("");
					settleTransTab.setAmount(transOrderInfo.getFeeAmount());//feeAmount存储的为商户退款挂起的金额
					settleTransTab.setObligate1("");
					settleTransTab.setObligate2("");
					settleTransTabs.add(settleTransTab);
				}else{
					if(null!=transOrderInfo.getErrorMsg()&&!"".equals(transOrderInfo.getErrorMsg())){//用交易订单表中的错误信息存储referUserId
						settleTransTab.setOrderNo(transOrderInfo.getOrderNo());
						settleTransTab.setTabNo(transOrderInfo.getOrderNo()+"000"+(i+1));
						settleTransTab.setRootInstCd(transOrderInfo.getMerchantCode());
						settleTransTab.setGroupManage(transOrderInfo.getErrorCode());//用交易订单表中的错误编码存储管理分组
						settleTransTab.setBatchNo("");
						settleTransTab.setUserId(transOrderInfo.getErrorMsg());//用交易订单表中的错误信息存储referUserId
						settleTransTab.setReferUserId("");
						settleTransTab.setAmount(transOrderInfo.getUserFee());//userFee存储的为台长退款挂起的金额
						settleTransTab.setObligate1("");
						settleTransTab.setObligate2("");
						settleTransTabs.add(settleTransTab);
					}
				}
			}
		} catch (AccountException e) {
			
		}
		return settleTransTabs;
	}
	
	@Override
	public FinanaceEntry getFinanaceEntry(TransOrderInfo transOrderInfo,Balance balance, String entryId, boolean flag) {
		FinanaceEntry finanaceEntry=null;
		long amount = transOrderInfo.getAmount();//获得交易金额
		//设置账户A需要的相关数据
		//获取账户A余额并判断可提现余额是否满足转账条件
		if(!flag && balance.getBalanceSettle() < amount){
			logger.error("账户" + transOrderInfo.getUserId() + "的可转账余额小于" + amount);
		}else{
			if(flag){
				balance.setBalanceSettle(balance.getBalanceSettle() + amount);//清算余额
				balance.setAmount(balance.getAmount() + amount);//账户余额
				balance.setBalanceUsable(balance.getBalanceUsable() + amount);//账户可用余额
			}else{
				balance.setBalanceSettle(balance.getBalanceSettle() - amount);//清算余额
				balance.setAmount(balance.getAmount() - amount);//账户余额
				balance.setBalanceUsable(balance.getBalanceUsable() - amount);//账户可用余额	
			}
			finanaceEntry = new FinanaceEntry();
			finanaceEntry = this.getAccountFlow(transOrderInfo, balance, entryId, flag);
			finanaceEntry.setAccrualType(BaseConstants.TYPE_BALANCE_SETTLE);
		}
		return finanaceEntry;
	}
}
	
