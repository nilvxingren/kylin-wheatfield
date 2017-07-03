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
	
	//手动补账
	@Override
	public String ForAccount(List<Map<String,String>> paramsValueList){
		logger.info("-----------手动补账开始-----------");
		String resultMsg = "OK";
		if(paramsValueList == null || paramsValueList.isEmpty()){
			return "请正确传入集合信息";
		}
		List<FinanaceEntry> finanaceEntryList = new ArrayList<FinanaceEntry>();
		
		List<String> resultList = new ArrayList<String>();
		for(Map<String,String> paramMap : paramsValueList){
			String errorMsg = "";
			String type =  paramMap.get("type");
			if(type == null || "".equals(type)){
				errorMsg += "type不能为空;";
			}
			String userid = "";
			String consid = "";
			String productid = "";
			if(type != null && ("1001".equals(type) || "4013".equals(type))){
				userid = paramMap.get("userid");
				if(userid == null || "".equals(userid)){
					errorMsg += "userid不能为空;";
				}
				consid = paramMap.get("consid");
				if(consid == null || "".equals(consid)){
					errorMsg += "consid不能为空;";
				}
				productid = paramMap.get("productid");
				if(productid == null || "".equals(productid)){
					errorMsg += "productid不能为空;";
				}				
			}

			String amount = paramMap.get("amount");
			if(amount == null || "".equals(amount)){
				errorMsg += "amount不能为空;";
			}

			//传值0表示失败，1表示成功
			String status = paramMap.get("status");
			if(status == null || "".equals(status)){
				errorMsg += "status不能为空;";
			}
			if(errorMsg != null && !"".equals(errorMsg)){
				errorMsg = "用户：" + userid + "---" + errorMsg;
				resultList.add(errorMsg);
				continue;
			}
			
			if(!"1001".equals(type) && "0".equals(status) ){
				errorMsg = "用户：" + userid + "---该订单状态0失败";
				resultList.add(errorMsg);
				continue;
			}
			
			if("1001".equals(type)){
				//单笔
				FinanaceEntry finanaceEntry = dbOperate(paramMap,errorMsg);
				if(finanaceEntry == null){
					logger.error("用户" + userid + "获取流水为空!");
					errorMsg += "用户" + userid + "获取流水为空!";
				}else{
					finanaceEntryList.add(finanaceEntry);
				}
			}else if("4016".equals(type) || "4013".equals(type) || "4014".equals(type)){
				//提现,代收，代付
				List<FinanaceEntry> finanaceEntrys = txdsdfOperate(paramMap,errorMsg);
				if(finanaceEntrys == null || finanaceEntrys.isEmpty()){
					logger.error("用户" + userid + ";产品号" + productid + ";机构号" + consid + "金额" + amount + "类型" + type + "获取流水为空!");
					errorMsg += "用户 " + userid + " 获取流水为空;";	
				}else{
					for(FinanaceEntry finanaceEntry : finanaceEntrys){
						finanaceEntryList.add(finanaceEntry);
					}					
				}
			}
			if(errorMsg != null && !"".equals(errorMsg)){
				resultList.add(errorMsg);
			}
		}
		if(finanaceEntryList != null && !finanaceEntryList.isEmpty()){
			insertFinanaceEntry(finanaceEntryList);
			logger.info("-----------传入记录 ：" + paramsValueList.size() + " 条--------");
			logger.info("-----------手动补账记录入库 : " + finanaceEntryList.size() + " 条--------");
		}
		if(resultList != null && !resultList.isEmpty()){
			logger.info("------手动补账存在错误记录--------");
			for(String errorMsg : resultList){
				logger.info(errorMsg);
			}
			resultMsg = "存在错误信息,请查看日志信息!";
		}
		logger.info("-----------手动补账结束-----------");
		return resultMsg;
	}
	//单笔Type=1001
	public FinanaceEntry dbOperate(Map<String,String> paramMap,String errorMsg){
		User user = new User();
		String userId = paramMap.get("userid");
		user.userId = userId;
		user.constId = paramMap.get("consid");
		user.productId = paramMap.get("productid");
		String type = paramMap.get("type");
		String status = paramMap.get("status");
		long amount = Long.parseLong(paramMap.get("amount"));
		
		//判断账户状态是否正常
		boolean accountIsOK = operationService.checkAccount(user);
		if(!accountIsOK){
			logger.error("用户"+userId+"状态为非正常状态");
			errorMsg += "用户"+userId+"状态为非正常状态";
		}
		
		Balance balance = checkInfoService.getBalance(user,"");
		FinanaceEntry finanaceEntry = null;
		if(balance != null){
			//获取套录号
			String entryId = redisIdGenerator.createRequestNo();
			boolean flag = true;
			if("1".equals(status)){
				flag = true;
			}else if("0".equals(status)){
				flag = false;
			}
			balance.setPulseDegree(balance.getPulseDegree() + 1);
			finanaceEntry = getFinanaceEntry(balance,userId,entryId,flag,type,amount);
			if(finanaceEntry != null){
				return finanaceEntry;
			}
		}else{
			logger.error("账户 " + userId + " 获取余额信息失败!");
			errorMsg += "用户"+userId+" 获取余额信息失败;";
		}
		return finanaceEntry;
	}
	
	//提现Type=4016,代收Type=4013,代付Type=4014
	public List<FinanaceEntry> txdsdfOperate(Map<String,String> paramMap,String errorMsg){
		List<FinanaceEntry> finanaceEntryList = new ArrayList<FinanaceEntry>();
		String type = paramMap.get("type");
		long amount = Long.parseLong(paramMap.get("amount"));
		String userId = "";
		Balance balance = null;
		boolean flag = true;
		for(int i = 0 ; i < 2 ; i++){
			if("4016".equals(type)){
				if(i == 0){
					//提现待清算
					userId = TransCodeConst.THIRDPARTYID_TXDQSZH;	
					balance = checkInfoService.getBalance(new User(),userId);
				}else if(i == 1){
					//瑞金麟备付金
					userId = TransCodeConst.THIRDPARTYID_FNZZH;
					balance = checkInfoService.getBalance(new User(),userId);
				}	
				flag = false;
			}else if("4013".equals(type)){
				if(i == 0){
					//其他应收款
					userId = TransCodeConst.THIRDPARTYID_QTYSKZH;
					balance = checkInfoService.getBalance(new User(),userId);	
					flag = false;
				}else if(i == 1){
					User user = new User();
					userId = paramMap.get("userid");
					user.userId = userId;
					user.constId = paramMap.get("consid");
					user.productId = paramMap.get("productid");
					
					//判断账户状态是否正常
					boolean accountIsOK = operationService.checkAccount(user);
					if(!accountIsOK){
						logger.error("用户"+userId+"状态为非正常状态");
						errorMsg += "用户"+userId+"状态为非正常状态";
						break;
					}
					balance = checkInfoService.getBalance(user,"");
					flag = true;
				}
			}else if("4014".equals(type)){
				if(i == 0){
					//其他应付款
					userId = TransCodeConst.THIRDPARTYID_QTYFKZH;
					balance = checkInfoService.getBalance(new User(),userId);
				}else if(i == 1){
					//瑞金麟备付金
					userId = TransCodeConst.THIRDPARTYID_FNZZH;
					balance = checkInfoService.getBalance(new User(),userId);	
				}
				flag = false;
			}
			
			FinanaceEntry finanaceEntry =  null;
			if(balance != null){
				//获取套录号
				String entryId = redisIdGenerator.createRequestNo();
				balance.setPulseDegree(balance.getPulseDegree() + 1);
				finanaceEntry = getFinanaceEntry(balance,userId,entryId,flag,type,amount);
				if(finanaceEntry == null){
					logger.error("账户 " + userId + " 获取流水信息失败!");
					errorMsg += "用户"+userId+" 获取流水信息失败;";
					break;
				}else{
					finanaceEntryList.add(finanaceEntry);
				}
			}else{
				logger.error("账户 " + userId + " 获取余额信息失败!");
				errorMsg += "用户"+userId+" 获取余额信息失败;";
				break;
			}
		}
		return finanaceEntryList;
	}
	
	public FinanaceEntry getFinanaceEntry(Balance balance,String userId,String entryId,boolean flag,String type,long amount) {
		FinanaceEntry finanaceEntry = null;
		if(balance.getBalanceSettle() < amount && !flag){
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
		rtnFlow.setFinanaceEntryId(redisIdGenerator.createFINEntryrRequestNo());//设置流水主键
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
		
		rtnFlow.setFinAccountId(balance.getFinAccountId());//设置账户ID
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
		rtnFlow.setThirdPartyId(this.getThirdPartyId(type,flag));//交易相关方
		rtnFlow.setYourNotes("");//你方摘要
		rtnFlow.setHisNote("");//他方摘要
		String remark = "手动补账";
		if("4016".equals(type)){
			remark = "提现手动补账";
		}else if("4013".equals(type)){
		    remark = "代收手动补账";	
		}else if("4014".equals(type)){
			remark = "代付手动补账";
		}else if("1001".equals(type)){
			remark = "单笔手动补账";
		}
		rtnFlow.setRemark(remark);
		rtnFlow.setRecordMap("");//设置安全码
		rtnFlow.setPulseTime(String.valueOf(new Date().getTime()));//设置账户心跳时间
		rtnFlow.setPulseDegree(balance.getPulseDegree());//账户心跳计次
		return rtnFlow;
	}
	
	/**
	 * 获取记账方向
	 * @param tradeType 交易类型
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
	
	public String getThirdPartyId(String type , boolean flag){
		String rtn = null;
		String transType = type;
		 if (transType.equals(TransCodeConst.PAYMENT_COLLECTION)) {//代收付
			rtn = TransCodeConst.THIRDPARTYID_FNZZH;
		}else if (transType.equals(TransCodeConst.PAYMENT_WITHHOLD)) {//代扣
			rtn = TransCodeConst.THIRDPARTYID_FNZZH;
		}else if(transType.equals(TransCodeConst.WITHDROW)){//提现
			rtn = TransCodeConst.THIRDPARTYID_TXDQSZH;
		}
		return rtn;
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
}
