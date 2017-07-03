/**
 * @File name : SemiAutomatizationServiceImpl.java
 * @Package : com.rkylin.wheatfield.service.impl
 * @Description : TODO(人工补账接口)
 * @Creator : liuhuan
 * @CreateTime : 2015年11月11日 下午1:10:34
 * @Version : 1.0
 */
package com.rkylin.wheatfield.service.impl;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rkylin.common.RedisIdGenerator;
import com.rkylin.wheatfield.constant.BaseConstants;
import com.rkylin.wheatfield.constant.TransCodeConst;
import com.rkylin.wheatfield.exception.AccountException;
import com.rkylin.wheatfield.manager.FinanaceEntryManager;
import com.rkylin.wheatfield.pojo.Balance;
import com.rkylin.wheatfield.pojo.FinanaceEntry;
import com.rkylin.wheatfield.pojo.TransOrderInfo;
import com.rkylin.wheatfield.pojo.User;
import com.rkylin.wheatfield.service.CheckInfoService;
import com.rkylin.wheatfield.service.OperationServive;
import com.rkylin.wheatfield.service.SemiAutomatizationService;
import com.rkylin.wheatfield.utils.DateUtil;

@Service("semiAutomatizationService")
public class SemiAutomatizationServiceImpl implements SemiAutomatizationService {
	private static Logger logger = LoggerFactory.getLogger(SemiAutomatizationServiceImpl.class);	
	
	@Autowired
	private FinanaceEntryManager finanaceEntryManager;
	@Autowired
	private CheckInfoService checkInfoService;	
	@Autowired
	private RedisIdGenerator redisIdGenerator;
	@Autowired
	private OperationServive operationService;
	
	//人工补账
	@Override
	public void ForAccount(List<Map<String,String>> paramsValueList){
//		if(paramsValueList == null){
//			
//		}
		
		List<String> resultList = new ArrayList<String>();
		for(Map<String,String> paramMap : paramsValueList){
			String type = paramMap.get("type");
			if("1001".equals(type)){
				dbOperate(paramMap);
			}
			
			
//			String resultMsg = "";
//			if(!hasParam(paramMap,"amount")){
//				resultMsg += "amount不能为空;";
//			}
			
			
			
			
		}
		
	}
	//单笔Type=0001
	public void dbOperate(Map<String,String> paramMap){
		User user = new User();
		String userId = paramMap.get("userid");
		user.userId = userId;
		user.constId = paramMap.get("consid");
		user.productId = paramMap.get("productid");
		String type = paramMap.get("type");
		long amount = Long.parseLong(paramMap.get("amount"));
		
		//判断账户状态是否正常
		boolean accountIsOK = operationService.checkAccount(user);
		if(!accountIsOK){
			logger.error("用户"+userId+"状态为非正常状态");
		}
		
		Balance balance=checkInfoService.getBalance(user,"");
		if(balance != null){
			//获取套录号
			String entryId = redisIdGenerator.createRequestNo();
			boolean flag = true;
			if("1".equals(type)){
				flag = true;
			}else if("0".equals(type)){
				flag = false;
			}
			balance.setPulseDegree(balance.getPulseDegree() + 1);
			FinanaceEntry finanaceEntry = getFinanaceEntry(balance,userId,entryId,flag,type,amount);
			if(finanaceEntry != null){
				List<FinanaceEntry> finanaceEntries = new ArrayList<FinanaceEntry>();
				finanaceEntries.add(finanaceEntry);
				insertFinanaceEntry(finanaceEntries);
			}
		}else{
			logger.error("账户 " + userId + " 获取余额信息失败!");
		}
	}
	
	public void insertFinanaceEntry(List<FinanaceEntry> finanaceEntries){
		try {
			if(null != finanaceEntries && !finanaceEntries.isEmpty()){
				finanaceEntryManager.saveFinanaceEntryList(finanaceEntries);
			}
		} catch (AccountException e) {
			throw new AccountException("ERROR");
		}
	}	
	
	public FinanaceEntry getFinanaceEntry(Balance balance,String userId,String entryId,boolean flag,String type,long amount) {
		FinanaceEntry finanaceEntry = null;
		if(balance.getBalanceSettle() < amount){
			logger.error("账户" + userId + "的可转账余额小于" + amount);
		}else{
			if(flag){
				balance.setAmount(balance.getAmount() + amount);//账户余额
				balance.setBalanceUsable(balance.getBalanceUsable() + amount);//账户可用余额
				balance.setBalanceSettle(balance.getBalanceSettle() + amount);//账户清余额
			}else{
				balance.setAmount(balance.getAmount() - amount);//账户余额
				balance.setBalanceUsable(balance.getBalanceUsable() - amount);//账户可用余额
				balance.setBalanceSettle(balance.getBalanceSettle() - amount);//账户清余额
			}
			finanaceEntry = new FinanaceEntry();
			finanaceEntry = this.getAccountFlow(balance, entryId, flag,type,amount);
			finanaceEntry.setAccrualType(BaseConstants.TYPE_BALANCE_SETTLE);
		}
		return finanaceEntry;
	}
	
	public FinanaceEntry getAccountFlow(Balance balance,String entryId,boolean flag,String type,long amount){
		FinanaceEntry rtnFlow = new FinanaceEntry();
		String finEntryId = redisIdGenerator.createFINEntryrRequestNo();
		if(null != finEntryId){
			rtnFlow.setFinanaceEntryId(finEntryId);//设置流水主键
			//获取记账日期
			String accountDateString = operationService.getAccountDate();
			if(accountDateString == null || accountDateString.equals("")){
				return null;
			}				
			try {
				rtnFlow.setAccountDate(new DateUtil().parse(accountDateString, "yyyy-MM-dd"));
			} catch (ParseException e) {
				e.printStackTrace();
			}//设置记账日期
			String accountId=balance.getFinAccountId();//获取账户ID
			
			if(null != accountId){
				
				rtnFlow.setFinAccountId(accountId);//设置账户ID
				rtnFlow.setReferEntryId(entryId);//设置套录号
				
				String tradeType = type;
				
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
				if (Integer.valueOf(type)==BaseConstants.REVERSE_TYPE){
					//冲正交易
					rtnFlow.setReverseFlag(BaseConstants.REVERSE_TYPE);
				}
				else{
					//非冲正交易
					rtnFlow.setReverseFlag(BaseConstants.NOREVERSE_TYPE);
				}
				rtnFlow.setPartyIdFrom(balance.getFinAccountId());//设置交易发起方
				rtnFlow.setReferId("");//记账凭证号
				/*暂时定义为订单来源*/
				rtnFlow.setReferFrom(String.valueOf(BaseConstants.REFER_TRADE));//设置凭证来源
				rtnFlow.setPaymentAmount(amount);//获取交易发生额
				rtnFlow.setTransDate(new DateUtil().getNow());//设置交易日期
				rtnFlow.setThirdPartyId("");//交易相关方
//				rtnFlow.setThirdPartyId(this.getThirdPartyId(transOrderInfo,flag));//交易相关方
				rtnFlow.setYourNotes("");//你方摘要
				rtnFlow.setHisNote("");//他方摘要
				rtnFlow.setRemark("手动补账");
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
	 * 获取记账方向
	 * @param tradeType 交易类型
	 * @param flag 交易源与交易目标的标记          另一种情况：true=CREDIT_TYPE  flase=DEBIT_TYPE
	 * @return 返回不同交易类型中的DC标志
	 * */
	public int getDorC(String transType , boolean flag){
		int rtn = 0;
		if(flag){
			rtn = BaseConstants.CREDIT_TYPE;
		}else{
			rtn = BaseConstants.DEBIT_TYPE;
		}
		return rtn;
	}
	
	public String getThirdPartyId(TransOrderInfo transOrderInfo , boolean flag){
		String rtn = null;
		String transType=transOrderInfo.getFuncCode();
		if(transType.equals(TransCodeConst.ADJUST_ACCOUNT_AMOUNT)){//内部转账交易
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

//	public boolean hasParam(Map<String,String> requestParam,String key){
//		boolean checkFlag = false;
//		if(requestParam.containsKey(key) && requestParam.get(key) != null && !"".equals(requestParam.get(key))){
//			checkFlag = true;
//		}
//		return checkFlag;
//	}
}
