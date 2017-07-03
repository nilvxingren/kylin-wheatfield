package com.rkylin.wheatfield.service.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rkylin.database.BaseDao;
import com.rkylin.wheatfield.constant.AccountConstants;
import com.rkylin.wheatfield.constant.BaseConstants;
import com.rkylin.wheatfield.constant.Constants;
import com.rkylin.wheatfield.constant.TransCodeConst;
import com.rkylin.wheatfield.manager.FinanaceAccountManager;
import com.rkylin.wheatfield.manager.FinanaceEntryHistoryManager;
import com.rkylin.wheatfield.manager.FinanaceEntryManager;
import com.rkylin.wheatfield.manager.ParameterInfoManager;
import com.rkylin.wheatfield.manager.TransOrderInfoManager;
import com.rkylin.wheatfield.pojo.Balance;
import com.rkylin.wheatfield.pojo.FinanaceEntry;
import com.rkylin.wheatfield.pojo.FinanaceEntryHistory;
import com.rkylin.wheatfield.pojo.FinanaceEntryHistoryQuery;
import com.rkylin.wheatfield.pojo.FinanaceEntryQuery;
import com.rkylin.wheatfield.pojo.OrderAuxiliary;
import com.rkylin.wheatfield.pojo.ParameterInfo;
import com.rkylin.wheatfield.pojo.ParameterInfoQuery;
import com.rkylin.wheatfield.pojo.TransOrderInfo;
import com.rkylin.wheatfield.pojo.TransOrderInfoQuery;
import com.rkylin.wheatfield.pojo.User;
import com.rkylin.wheatfield.service.CheckInfoService;
import com.rkylin.wheatfield.service.OperationServive;
import com.rkylin.wheatfield.service.OrderService;
import com.rkylin.wheatfield.utils.ArithUtil;
import com.rkylin.wheatfield.utils.DateUtil;
import com.rkylin.wheatfield.utils.SignUtils;
import com.rkylin.wheatfield.utils.StringUtils;

@Service
public class CheckInfoServiceImpl extends BaseDao implements CheckInfoService {

	private static Logger logger = LoggerFactory.getLogger(CheckInfoServiceImpl.class);
	@Autowired
	FinanaceAccountManager finanaceAccountManager;
	@Autowired
	FinanaceEntryManager finanaceEntryManager;
	@Autowired
	OperationServive operationServive;
	@Autowired
	TransOrderInfoManager transOrderInfoManager;
	@Autowired
	FinanaceEntryHistoryManager finanaceEntryHistoryManager;
	@Autowired
	OrderService orderService;
	@Autowired
	ParameterInfoManager parameterInfoManager;
	
	ArithUtil arithUtil=new ArithUtil();
	DateUtil dateUtil=new DateUtil();
	
	@Override
	public String checkTradeInfo(TransOrderInfo transOrderInfo) {
		// TODO 校验订单数据有效性
		String  msgStr="ok";
		if(null==transOrderInfo){
			logger.error("订单信息不能为空");
			msgStr="订单信息不能为空";
		}else {
			String userId = transOrderInfo.getUserId();//用户ID
			long amount = transOrderInfo.getAmount();//交易金额	
			String transType=transOrderInfo.getFuncCode();
			if(transType.equals(TransCodeConst.ADJUST_ACCOUNT_AMOUNT)){//内部转账交易
				//1.获取转账金额 2.判断转出账户金额 3.判断转入账户状态
				User user=new User();	
				user.userId=userId;
				user.constId=transOrderInfo.getMerchantCode();
				//errorCode暂存转出方产品号
				user.productId=transOrderInfo.getErrorCode();
				Balance balance=this.getBalance(user,"");
				if(amount>balance.getBalanceSettle()){//交易金额大于账户的转出金额
					msgStr="账户"+userId+"的可提现金额不足";
				}
			}else if (transType.equals(TransCodeConst.CHARGE)) {//充值
				if(transOrderInfo.getAmount()<=0){
					logger.error("充值金额不能小于等于0");
					return "充值金额不能小于等于0";
				}
				//--------------------------充值流程修改注释（充值成功）订单收到消息--->账户系统--->账户查上游渠道--->正常情况下入账-------------------------
				//判断订单是否已在通联充值成功
				//if(orderService.validataOrder(transOrderInfo)){
				//--------------------------------------------------------
				//+++++++++++++++++++++++++++充值新流程（充值成功）订单收到消息并利用私钥MD5加密写入remark--->账户系统--->加密信息验证--->验证OK入账++++++++++++++++++++++++++++++++++++++++++
				//MD5加密校验规则      机构号+产品号+userid+amount+orderno+account库中的PARAMETER_INFO表的
				//PARAMETER_CODE字段为MD5的PARAMETER_VALUE值
				logger.info("<<<<<<<<<<<<<<<<充值密钥校验开始");
				logger.info("订单系统传入的MD5加密串为："+transOrderInfo.getRemark());
				boolean isCharge=false;
				//获取加密私钥
				ParameterInfoQuery pQuery=new ParameterInfoQuery();
				pQuery.setParameterCode("MD5");
				pQuery.setStatus(1);
				List<ParameterInfo> parameterInfos=parameterInfoManager.queryList(pQuery);				
				if(null!=parameterInfos&&parameterInfos.size()==1){
					//获取账户系统端的MD5加密串
					String accountMD5="";
					SignUtils signUtils=new SignUtils();
					try {
						accountMD5=signUtils.getMD5Code(transOrderInfo.getMerchantCode()
								+transOrderInfo.getProductIdd()
								+transOrderInfo.getUserId()
								+transOrderInfo.getAmount()
								+transOrderInfo.getOrderNo()+parameterInfos.get(0).getParameterValue());
						logger.info("加密字符串为："+transOrderInfo.getMerchantCode()
								+transOrderInfo.getProductIdd()
								+transOrderInfo.getUserId()
								+transOrderInfo.getAmount()
								+transOrderInfo.getOrderNo()+parameterInfos.get(0).getParameterValue());
						logger.info("账户系统端加密串为："+accountMD5);						
						//比对密钥是否相等
						if(transOrderInfo.getRemark().equals(accountMD5)){
							isCharge=true;
						}
					} catch (IOException e) {
						logger.error(e.getMessage());
						return "充值订单:"+transOrderInfo.getRequestNo()+"密钥校验失败";
					}
				}		
				if(!isCharge){
					logger.error("充值订单:"+transOrderInfo.getRequestNo()+"密钥校验失败");
					return "充值订单:"+transOrderInfo.getRequestNo()+"密钥校验失败";
				}
				//把备注字段清空
				transOrderInfo.setRemark(null);
				logger.info(">>>>>>>>>>>>>>>>>>>>>>>>充值密钥校验结束");
			}else if (transType.equals(TransCodeConst.CREDIT_CONSUME)) {//信用消费
				User user=new User();
				user.userId=userId;
				user.constId=transOrderInfo.getMerchantCode();
				user.uEType=AccountConstants.ACCOUNT_TYPE_CHILD;
				if(transOrderInfo.getMerchantCode().equals(Constants.SQSM_ID)){
					user.productId=Constants.SQSM_CREDIT_PRODUCT;
				}else if(transOrderInfo.getMerchantCode().equals(Constants.MZ_ID)){
					user.productId=Constants.MZ_CREDIT_PRODUCT;
				}else{
					user.productId=Constants.USER_SUB_ACCOUNT;
				}
				Balance balance=this.getBalance(user,"");
				if(null!=balance){
					//if(amount>balance.getBalanceCredit()){
					if(amount>balance.getBalanceSettle()){
						logger.error("当前用户消费信用额度大于当前信用额度");
						msgStr="当前用户消费信用额度大于当前信用额度";
					}
				}else{
					logger.error("获取用户余额信息失败！"+userId);
					msgStr="获取用户余额信息失败！"+userId;
				}
			}else if(transType.equals(TransCodeConst.DEBIT_CONSUME)){//储蓄消费
				User user=new User();
				user.userId=userId;
				//user.uEType=AccountConstants.ACCOUNT_TYPE_BASE;
				user.constId=transOrderInfo.getMerchantCode();
				user.productId=transOrderInfo.getErrorCode();
				Balance balance=this.getBalance(user,"");
				if(null!=balance){
					if(user.productId.equals(Constants.FN_PRODUCT)){
						if(null!=transOrderInfo.getFeeAmount()&&transOrderInfo.getFeeAmount()>0){
							if((amount-transOrderInfo.getFeeAmount())>balance.getBalanceSettle()){
								logger.error("当前用户消费额度大于账户可用额度"+userId);
								msgStr="当前用户消费额度大于账户可用额度"+userId;
							}else{
								//判断账户红包余额是否满足红包消费条件
								user.productId=Constants.FN_RED_PACKET;
								Balance balanceHB=this.getBalance(user,"");
								if(null!=balanceHB){
									if(transOrderInfo.getFeeAmount()>balanceHB.getBalanceSettle()){
										logger.error("当前账户红包余额不足"+userId);
										msgStr="当前账户红包余额不足"+userId;
									}
								}else{
									logger.error("获取账户红包余额失败！"+userId);
									msgStr="获取账户红包余额失败！"+userId;
								}
							}
						}else{
							if(amount>balance.getBalanceSettle()){
								logger.error("当前用户消费额度大于账户可用额度"+userId);
								msgStr="当前用户消费额度大于账户可用额度"+userId;
							}
						}
					}else if(user.productId.equals(Constants.FN_RED_PACKET)){
						if(transOrderInfo.getFeeAmount()==null||transOrderInfo.getFeeAmount()<=0){
							logger.error("该订单为全红包消费，红包金额不能为空或小于等于零!用户号："+userId);
							msgStr="该订单为全红包消费，红包金额不能为空或小于等于零!用户号："+userId;
						}else{
							Balance balanceHB=this.getBalance(user,"");
							if(null!=balanceHB){
								if(transOrderInfo.getFeeAmount()>balanceHB.getBalanceSettle()){
									logger.error("当前账户红包余额不足"+userId);
									msgStr="当前账户红包余额不足"+userId;
								}
							}else{
								logger.error("获取账户红包余额失败！"+userId);
								msgStr="获取账户红包余额失败！"+userId;
							}
						}
					}else{
						if(amount>balance.getBalanceSettle()){
							logger.error("当前用户消费额度大于账户可用额度"+userId);
							msgStr="当前用户消费额度大于账户可用额度"+userId;
						}
					}
				}else{
					logger.error("获取用户余额信息失败！"+userId);
					msgStr="获取用户余额信息失败！"+userId;
				}
			}else if (transType.equals(TransCodeConst.PAYMENT_COLLECTION) || TransCodeConst.PAYMENT_REAL_TIME_COLLECTION.equals(transType)) {//(实时)代收
				if(amount<=0){
					logger.error("代收金额必须大于零！"+userId);
					msgStr="代收金额必须大于零！";
				}
			}else if (transType.equals(TransCodeConst.PAYMENT_WITHHOLD)) {//代付
				if(amount<=0){
					logger.error("代付金额必须大于零！"+userId);
					msgStr="代付金额必须大于零！";
				}else{
					if(!userId.equals(TransCodeConst.THIRDPARTYID_FNZZH)){
						User user=new User();
						user.userId=userId;
						if(TransCodeConst.THIRDPARTYID_DGZHJYZCZH.equals(userId)){
							user.constId = Constants.FN_ID;
						}else{
							user.constId=transOrderInfo.getMerchantCode();	
						}
						user.productId=transOrderInfo.getErrorCode();
						Balance balance=this.getBalance(user,"");
						if(null!=balance){
							if(transOrderInfo.getMerchantCode().equals(Constants.JRD_ID)){
								if(transOrderInfo.getOrderAmount()>balance.getBalanceSettle()){
									logger.error("当前用户余额不足"+userId);
									msgStr="当前用户余额不足"+userId;
								}
							}else{
								if(amount>balance.getBalanceSettle()){
									logger.error("当前用户余额不足"+userId);
									msgStr="当前用户余额不足"+userId;
								}
							}
						}else{
							logger.error("获取用户余额信息失败！"+userId);
							msgStr="获取用户余额信息失败！"+userId;
						}
					}
				}
				
			}else if(transType.equals(TransCodeConst.WITHDROW)){//提现
				//判断是否是特殊账户Id
				if(!StringUtils.stringIsHave(TransCodeConst.SPECIAL_ACCOUNT_NO, userId)){
					//包含返回false
					logger.error(userId+"为特殊账户，不能进行提现操作");
					msgStr=userId+"为特殊账户，不能进行提现操作";
				}else{
					if(amount>0){
						//判断当前用户账户余额是否大于提现余额				
						User user=new User();
						user.userId=userId;
						user.uEType=AccountConstants.ACCOUNT_TYPE_BASE;
						user.constId=transOrderInfo.getMerchantCode();
						Balance balance=this.getBalance(user,"");
						if(null!=balance){
							if(transOrderInfo.getMerchantCode().equals(Constants.JRD_ID)){
								//获取提现总余额=订单金额+用户手续费金额
								if(balance.getBalanceSettle()<transOrderInfo.getOrderAmount()){
									//判断充值子账户余额是否满足剩余金额条件
									Long subAccountAmount=transOrderInfo.getOrderAmount()-balance.getBalanceSettle();
									if(!isSubAccountAmount(userId, Constants.JRD_RECHARGE_ACCOUNT_PRODUCT, transOrderInfo.getMerchantCode(), subAccountAmount)){
										logger.error("当前用户所提现的额度大于账户可提现的额度");
										msgStr="当前用户所提现的额度大于账户可提现的额度";
									}
								}
							}else{
								//提现操作包含手续费，所以需要判断账户可提现余额是否大于订单金额
								if(transOrderInfo.getOrderAmount()>balance.getBalanceSettle()){
									logger.error("当前用户所提现的额度大于账户可提现的额度");
									msgStr="当前用户所提现的额度大于账户可提现的额度";
								}
							}
						}else{
							logger.error("获取用户余额信息失败！"+userId);
							msgStr="获取用户余额信息失败！";
						}
					}else{
						logger.error("提现金额必须大于零！"+userId);
						msgStr="提现金额必须大于零！";
					}
				}							
			}else if(transType.equals(TransCodeConst.AFTER_SPENDING_REFUND)){//消费后退款
				//获取旧订单中的订单金额
				TransOrderInfoQuery query=new TransOrderInfoQuery();
				query.setOrderNo(transOrderInfo.getOrderPackageNo());
				List<TransOrderInfo> transOrderInfos=transOrderInfoManager.queryList(query);
				if(null!=transOrderInfos&&1==transOrderInfos.size()){
					//获取原订单金额
					long oldOrdreMoney=transOrderInfos.get(0).getOrderAmount();
					//获取原订单交易类型
					String oldFuncode= transOrderInfos.get(0).getFuncCode();
					if(oldFuncode.equals(TransCodeConst.CREDIT_CONSUME)){
						msgStr="原订单为信用消费，不提供消费后退款";
					}else{
						if(transOrderInfo.getOrderAmount()>oldOrdreMoney){
							msgStr="订单退款金额不能大于原订单金额";
						}
					}
				}else{
					msgStr="原订单信息查询失败，请确认原订单号是否正确";
				}
			}else if(transType.equals(TransCodeConst.PRE_AUTHORIZATION)){//预授权
				
			}else if(transType.equals(TransCodeConst.ADVANCE)){//预付金
				
			}
		}		
		return msgStr;
	}
	@Override
	public String checkTradeInfo(TransOrderInfo transOrderInfo,	OrderAuxiliary orderAuxiliary) {
		// TODO 校验订单数据有效性
		String  msgStr="ok";
		if(null==transOrderInfo){
			logger.error("订单信息不能为空");
			msgStr="订单信息不能为空";
		}else {
			String userId = transOrderInfo.getUserId();//用户ID
			long amount = transOrderInfo.getAmount();//交易金额	
			String transType=transOrderInfo.getFuncCode();
			if(transType.equals(TransCodeConst.FROZON)){//冻结资金
				//判断账户提现余额是否满足冻结条件
				User user=new User();
				user.userId=userId;
				user.constId=transOrderInfo.getMerchantCode();
				user.productId=orderAuxiliary.getProductQAA();
				Balance balance=this.getBalance(user,"");
				if(null!=balance){
					//冻结金额是否满足条件
					if(amount>balance.getBalanceSettle()){
						logger.error("当前用户可提现余额小于冻结额度，需冻结金额为："+amount+",账户的可提现余额为："+balance.getBalanceSettle());
						msgStr="当前用户可提现余额小于冻结额度";
					}
				}else{
					logger.error("获取用户余额信息失败！"+userId);
					msgStr="获取用户余额信息失败！"+userId;
				}
			}else if(transType.equals(TransCodeConst.FROZEN)){//解冻
				if(TransCodeConst.THIRDPARTYID_FNHBZZH.equals(transOrderInfo.getUserId())){
					logger.error("该账户为丰年红包主账户，请输入授权码"+userId);
					msgStr="该账户为丰年红包主账户，请输入授权码!";
				}else{
					//判断账户提现余额是否满足解冻条件
					User user=new User();
					user.userId=userId;
					user.constId=transOrderInfo.getMerchantCode();
					user.productId=orderAuxiliary.getProductQAA();
					Balance balance=this.getBalance(user,"");
					if(null!=balance){
						//冻结金额是否满足条件
						if(balance.getBalanceFrozon()==0){
							logger.error("当前账户的冻结余额已为零");
							msgStr="当前账户的冻结余额已为零";
						}else if(transOrderInfo.getAmount()>balance.getBalanceFrozon()){
							logger.error("当前账户的冻结余额不满足解冻条件，账户已冻结金额"+balance.getBalanceFrozon()+";订单解冻金额"+transOrderInfo.getAmount());
							msgStr="当前账户的冻结余额小于交易金额，请确认";
						}
					}else{
						logger.error("获取用户余额信息失败！"+userId);
						msgStr="获取用户余额信息失败！"+userId;
					}
				}
			}else if(transType.equals(TransCodeConst.ORDER_REFUND)){//订单退款
				//判断账户提现余额是否满足冻结条件
				User user=new User();
				user.userId=userId;
				user.constId=transOrderInfo.getMerchantCode();
				user.productId=orderAuxiliary.getProductQAA();
				Balance balance=this.getBalance(user,"");
				if(null!=balance){
					//订单退款  需判断冻结资金是否满足
					if(amount>balance.getBalanceFrozon()){
						logger.error("请确认退款金额是否正确");
						msgStr="请确认退款金额是否正确";
					}
				}else{
					logger.error("获取用户余额信息失败！"+userId);
					msgStr="获取用户余额信息失败！"+userId;
				}
			}else if(transType.equals(TransCodeConst.FROZEN_AUTHCODE)){//授权码解冻
				//判断授权表中的余额是否满足解冻条件
				if(transOrderInfo.getAmount()>orderAuxiliary.getAuthAmount()){
					logger.error("当前 授权码的余额已不足，不能进行本次的支付，code:"+orderAuxiliary.getAuthCode());
					msgStr="当前 授权码的余额已不足，不能进行本次的支付";
				}else{
					if(null!=transOrderInfo.getInterMerchantCode()&&!"".equals(transOrderInfo.getInterMerchantCode())){
						User user=new User();
						user.userId=transOrderInfo.getInterMerchantCode();
						user.constId=transOrderInfo.getMerchantCode();
						user.productId=orderAuxiliary.getProductQAB();
						if(!operationServive.checkAccount(user)){
							logger.error("转入方的账户状态为非正常状态,判断条件userId:"+user.userId+",merchantCode:"+user.constId+",productId:"+user.productId);
							msgStr="转入方的账户状态为非正常状态";
						}
					}
				}
			}else if(transType.equals(TransCodeConst.ADJUST_ACCOUNT_AMOUNT)){//转账交易
				//记录最新金额
				Long nAmount=0l;
				User user=new User();
				user.userId=userId;
				user.constId=transOrderInfo.getMerchantCode();
				Balance balance=null;
				if(amount<=0){
					logger.info("转账金额不能小于等于0，订单金额："+amount);
					return "转账金额需大于0！";
				}
				//根据转账业务类型判断金额是否满足条件
				if(AccountConstants.JRD_BUSITYPE_INVESTMENT.equals(transOrderInfo.getBusiTypeId())){//君融贷--投资
					user.productId=orderAuxiliary.getProductQAA();
					balance=this.getBalance(user,"");
					if(amount>balance.getBalanceSettle()){//交易金额大于主账户的转出金额
						nAmount=amount-balance.getBalanceSettle();
						logger.info("投资--转出方主账户余额："+balance.getBalanceSettle());
						//获取充值子账户的余额
						user.productId=Constants.JRD_RECHARGE_ACCOUNT_PRODUCT;
						balance=this.getBalance(user, "");
						if(nAmount>balance.getBalanceSettle()){
							logger.info("投资--转出方充值子账户余额："+balance.getBalanceSettle());
							logger.error("投资--转出方账户余额不足，请确认！");
							msgStr="转出方账户余额不足，请确认！";
						}
					}
				}else if(AccountConstants.JRD_BUSITYPE_RECEIVABLE.equals(transOrderInfo.getBusiTypeId())){//君融贷--回款
					user.productId=Constants.JRD_RECHARGE_ACCOUNT_PRODUCT;
					balance=this.getBalance(user,"");
					if(amount>balance.getBalanceSettle()){//交易金额大于子账户的转出金额
						nAmount=amount-balance.getBalanceSettle();
						logger.info("回款--转出方充值子账户余额："+balance.getBalanceSettle());
						//获取主账户的余额
						user.productId=orderAuxiliary.getProductQAA();
						balance=this.getBalance(user, "");
						if(nAmount>balance.getBalanceSettle()){
							logger.info("回款--转出方主账户余额："+balance.getBalanceSettle());
							user.productId=Constants.JRD_FINANCED_ACCOUNT_PRODUCT;
							nAmount=nAmount-balance.getBalanceSettle();
							balance=this.getBalance(user, "");
							if(nAmount>balance.getBalanceSettle()){
								logger.info("回款--转出方融资子账户余额："+balance.getBalanceSettle());
								logger.error("回款--转出方账户余额不足，请确认！");
								msgStr="转出方账户余额不足，请确认！";
							}
						}
					}
				}else if(AccountConstants.JRD_BUSITYPE_RIGHTS_TRANS.equals(transOrderInfo.getBusiTypeId())){//君融贷--债权转让
					user.productId=orderAuxiliary.getProductQAA();
					balance=this.getBalance(user,"");
					if(amount>balance.getBalanceSettle()){//交易金额大于主账户的转出金额
						nAmount=amount-balance.getBalanceSettle();
						logger.info("债权转让--转出方主账户余额："+balance.getBalanceSettle());
						//获取充值子账户的余额
						user.productId=Constants.JRD_RECHARGE_ACCOUNT_PRODUCT;
						balance=this.getBalance(user, "");
						if(nAmount>balance.getBalanceSettle()){
							logger.info("债权转让--转出方充值子账户余额："+balance.getBalanceSettle());
							logger.error("债权转让--转出方账户余额不足，请确认！");
							msgStr="转出方账户余额不足，请确认！";
						}
					}
				}else{
					user.productId=orderAuxiliary.getProductQAA();
					balance=this.getBalance(user,"");
					if(amount>balance.getBalanceSettle()){//交易金额大于主账户的转出金额
						nAmount=amount-balance.getBalanceSettle();
						logger.info("转出方主账户余额："+balance.getBalanceSettle());
						msgStr="转出方账户余额不足，请确认！";
					}
				}
				
			}
		}
		return msgStr;
	}
	
	@Override
	public Balance getBalance(User user,String finAccountIdP) {
		Balance balance=null;
		if(null!=user){
			balance=new Balance();
			String finAccountId=null;
			try {
				if(null!=finAccountIdP&&!"".equals(finAccountIdP)){
					finAccountId=finAccountIdP;
				}else{
					finAccountId= operationServive.getUserAccount(user);
				}				
				if(null==finAccountId || finAccountId.equals("")){
					logger.error("用户["+user.userId+"]没有对应的账户ID");
					return null;
				}				
				  try {  
		              Map<String, String> param = new HashMap<String, String>();  
		              param.put("iv_fin_account_id", finAccountId); 
		              param.put("iv_accrual_type", user.payType);
		              super.getSqlSession().selectList("MyBatisMap.getBalance", param);
		              logger.info(">>>>>>执行存储过程查询余额，返回结果：param信息："+param);		              if(null!=param&&String.valueOf(param.get("on_err_code")).equals("0")){
		            	  logger.info("[getUserBalance]执行存储过程成功");
		            	  	balance.setAmount(Long.parseLong(String.valueOf(param.get("on_amount"))));
							balance.setBalanceCredit(Long.parseLong(String.valueOf(param.get("on_balance_credit"))));
							balance.setBalanceFrozon(Long.parseLong(String.valueOf(param.get("on_balance_frozon"))));
							balance.setBalanceOverLimit(Long.parseLong(String.valueOf(param.get("on_balance_over_limit"))));
							balance.setBalanceSettle(Long.parseLong(String.valueOf(param.get("on_balance_settle"))));
							balance.setBalanceUsable(Long.parseLong(String.valueOf(param.get("on_balance_usable"))));
							balance.setPulseTime(String.valueOf(param.get("on_pulse_time")));
							balance.setPulseDegree(Integer.valueOf(String.valueOf(param.get("on_pulse_degree"))));
							balance.setFinAccountId(finAccountId);
		              }else{
		            	  logger.error("[getUserBalance]获取用户余额失败");
		            	  return null;
		              }
		              
		        } catch (Exception e) {  
		        	logger.error("[getUserBalance]执行存储过程异常，获取用户余额失败"+e.getMessage());
		        	 return null;
		        } 				
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("[getUserBalance]执行存储过程异常，获取用户余额失败"+e.getMessage());
				 return null;
			}
		}
		return balance;
	}
	@Override
	public List<FinanaceEntry> getFinanaceEntries(TransOrderInfo transOrderInfo, Balance balance, Balance balanceB,String entryId, boolean flag) {
		List<FinanaceEntry> finanaceEntries=null;
		String transType = transOrderInfo.getFuncCode();
		if(transType.equals(TransCodeConst.ADJUST_ACCOUNT_AMOUNT)){//内部转账交易
			finanaceEntries=operationServive.getADJUSTAccount(transOrderInfo, balance,balanceB, entryId, flag);
		}
		return finanaceEntries;
	}
	@Override
	public List<FinanaceEntry> getFinanaceEntries(TransOrderInfo transOrderInfo,Balance balance, String entryId,boolean flag) {
		List<FinanaceEntry> finanaceEntries=null;
		String transType = transOrderInfo.getFuncCode();
		if(transType.equals(TransCodeConst.ADJUST_ACCOUNT_AMOUNT)||transType.equals(TransCodeConst.AFTER_SPENDING_REFUND)
				||transType.equals(TransCodeConst.FROZEN_AUTHCODE)&&TransCodeConst.ADJUST_ACCOUNT_AMOUNT.equals(transOrderInfo.getErrorCode())){//内部转账交易
			User user=new User();
			user.userId=transOrderInfo.getInterMerchantCode();
			//user.uEType=AccountConstants.ACCOUNT_TYPE_BASE;
			//if(transOrderInfo.getMerchantCode().equals(Constants.HT_ID)){//这个只针对会堂给会场云做转账
			if(transOrderInfo.getMerchantCode().equals(Constants.HT_ID)&&!transOrderInfo.getErrorCode().equals(Constants.HT_TRANSFER_PRODUCT_STRING)){//这个只针对会堂给会场云做转账
				user.constId=Constants.HT_CLOUD_ID;
				user.productId=Constants.HT_CHANGDIFANG_ACCOUNT;
			//}else{
			}else if(transOrderInfo.getMerchantCode().equals(Constants.HT_ID)&&transOrderInfo.getErrorCode().equals(Constants.HT_TRANSFER_PRODUCT_STRING)){//这个只针对会唐与会唐之间的转账
				user.constId=transOrderInfo.getMerchantCode();
				user.productId=Constants.HT_PRODUCT;
			}
			else{
				user.constId=transOrderInfo.getMerchantCode();
				if(transOrderInfo.getTradeFlowNo()!=null && transOrderInfo.getTradeFlowNo()!=""){
					user.productId=transOrderInfo.getTradeFlowNo();
				}else{
					user.productId=transOrderInfo.getErrorCode();
				}
				user.referUserId=transOrderInfo.getErrorMsg();
			}
			
			
			Balance balanceB=this.getBalance(user, "");
			//finanaceEntries=operationServive.getADJUSTAccount(transOrderInfo, balance,balanceB, entryId, flag);
			if(balanceB==null){
				finanaceEntries=null;
			}else{
				finanaceEntries=operationServive.getADJUSTAccount(transOrderInfo, balance,balanceB, entryId, flag);
			}			
		}else if (transType.equals(TransCodeConst.CHARGE)) {//充值
			finanaceEntries=operationServive.getChargeAccount(transOrderInfo, balance, entryId, flag);
		}else if (transType.equals(TransCodeConst.CREDIT_CONSUME)) {//信用消费
			finanaceEntries=operationServive.getDeductAccount(transOrderInfo, balance, entryId, flag);
		}else if(transType.equals(TransCodeConst.DEBIT_CONSUME)){//储蓄消费
			finanaceEntries=operationServive.getDeductAccount(transOrderInfo, balance, entryId, flag);
		}else if(transType.equals(TransCodeConst.WITHDROW)){//提现
			finanaceEntries=operationServive.getWithdrawAccount(transOrderInfo, balance, entryId, flag);
		}else if(transType.equals(TransCodeConst.ADVANCE)){//预付金
			User user=new User();
			user.uEType=AccountConstants.ACCOUNT_TYPE_BASE;
			user.constId=transOrderInfo.getMerchantCode();
			user.productId=Constants.FN_PRODUCT;
			user.userId=transOrderInfo.getInterMerchantCode();
			Balance balanceB=this.getBalance(user, "");
			finanaceEntries=operationServive.getADJUSTAccount(transOrderInfo, balance,balanceB, entryId, flag);
		}else if(transType.equals(TransCodeConst.PRE_AUTHORIZATION)){//预授权
			User user=new User();
			user.userId=transOrderInfo.getInterMerchantCode();
			user.uEType=AccountConstants.ACCOUNT_TYPE_BASE;
			user.constId=transOrderInfo.getMerchantCode();
			Balance balanceB=this.getBalance(user, "");
			//获取转账记账流水
			finanaceEntries=operationServive.getADJUSTAccount(transOrderInfo, balance,balanceB, entryId, flag);
			//获取B账户冻结记账流水
			List<FinanaceEntry> finanaceEntriesList=operationServive.getFreezeAccount(transOrderInfo, balanceB, entryId, flag);
			if(null!=finanaceEntriesList && 0<finanaceEntriesList.size()){
				for (FinanaceEntry finanaceEntry : finanaceEntriesList) {
					finanaceEntries.add(finanaceEntry);
				}
			}else{
				finanaceEntries=null;
			}
		}else if(TransCodeConst.PAYMENT_COLLECTION.equals(transType) || TransCodeConst.PAYMENT_REAL_TIME_COLLECTION.equals(transType)){//代收
			finanaceEntries=operationServive.getCollection(transOrderInfo, balance, entryId, flag);
		}else if(transType.equals(TransCodeConst.PAYMENT_WITHHOLD)){//代付
			finanaceEntries=operationServive.getWithhold(transOrderInfo, balance, entryId, flag);
		}else if(transType.equals(TransCodeConst.FROZON)||transType.equals(TransCodeConst.FROZON_AUTHCODE)){//冻结
			finanaceEntries=operationServive.getFreezeAccount(transOrderInfo, balance, entryId, flag);
		}else if(transType.equals(TransCodeConst.FROZEN)||transType.equals(TransCodeConst.FROZEN_AUTHCODE)){//解冻
			finanaceEntries=operationServive.getFrozenAccount(transOrderInfo, balance, entryId, flag);
		}
		return finanaceEntries;
	}
	@Override
	public List<FinanaceEntry> getFinanaceEntrieByOrderId(String orderId) {
		// TODO 根据单号Id获取当天账户记账流水记录信息
		List<FinanaceEntry> finanaceEntries=null;
		FinanaceEntryQuery query=new FinanaceEntryQuery();
		query.setReferId(orderId);
		query.setReferFrom(String.valueOf(BaseConstants.REFER_TRADE));//记账来源-----订单类型
		finanaceEntries=finanaceEntryManager.queryList(query);
		return finanaceEntries;
	}
	@Override
	public List<FinanaceEntryHistory> getFinanaceEntrieHistoryByOrderId(String orderId) {
		// TODO 根据单号Id获取历史账户记账流水记录信息
		List<FinanaceEntryHistory> finanaceEntryHistories=null;
		FinanaceEntryHistoryQuery query=new FinanaceEntryHistoryQuery();
		query.setReferId(orderId);
		query.setReferFrom(String.valueOf(BaseConstants.REFER_TRADE));//记账来源-----订单类型
		finanaceEntryHistories=finanaceEntryHistoryManager.queryList(query);
		return finanaceEntryHistories;
	}
	@Override
	public List<FinanaceEntry> getInternalFinanaceEntries(FinanaceEntry finanaceEntry,Balance balance, boolean flag,String method,String userId) {
		List<FinanaceEntry> finanaceEntries=null;
		if(method.equals("credit")){//授信
			finanaceEntries=operationServive.getCredit(finanaceEntry, balance, flag, userId);
		}else if(method.equals("withdrawReturn")){//提现返回
			finanaceEntries=operationServive.getWithdrawReturn(finanaceEntry, balance, flag, userId);
		}else if(method.equals("refundReturn")){//还款返回
			finanaceEntries=operationServive.getRefundReturn(finanaceEntry, balance,flag, userId);
		}else if(method.equals("rightsPackageReturn")){//债权包返回
			finanaceEntries=operationServive.getRightsPackageReturn(finanaceEntry, balance,flag, userId);
		}else if(method.equals("fenrun")){//分润
			finanaceEntries=operationServive.getFenRun(finanaceEntry, balance,flag, userId);
		}else if(method.equals("yufujin")){//预付金
			finanaceEntries=operationServive.getYuFuJin(finanaceEntry, balance,flag, userId);
		}else if(method.equals("collectionReturn")){//代收返回
			finanaceEntries=operationServive.getCollectionReturn(finanaceEntry, balance,flag, userId);
		}else if(method.equals("withholdReturn")){//代付返回
			finanaceEntries=operationServive.getWithholdReturn(finanaceEntry, balance,flag, userId);
		}
		return finanaceEntries;
	}
	/**
	 * 对比子账户的金额是否满足条件
	 * @param userId
	 * @param productId
	 * @param constId
	 * @param amount
	 * @return
	 */
	private boolean isSubAccountAmount(String userId,String productId,String constId,Long amount){
		boolean isOk=false;
		try {
			User user=new User();
			user.userId=userId;
			user.constId=constId;
			user.productId=productId;
			Balance balance=this.getBalance(user, "");
			if(balance!=null){
				if(balance.getBalanceSettle()>=amount){
					isOk=true;
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return isOk;
	}
	
	@Override
	public FinanaceEntry getFinanaceEntry(TransOrderInfo transOrderInfo,Balance balance, String entryId, boolean flag) {
		FinanaceEntry finanaceEntry = null;
		String transType = transOrderInfo.getFuncCode();
		if(transType.equals(TransCodeConst.ADJUST_ACCOUNT_AMOUNT)){//内部转账交易
			finanaceEntry = operationServive.getFinanaceEntry(transOrderInfo, balance, entryId, flag);
		}
		return finanaceEntry;
	}
}
