package com.rkylin.wheatfield.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.rkylin.common.RedisIdGenerator;
import com.rkylin.utils.RkylinMailUtil;
import com.rkylin.wheatfield.api.PaymentInternalOutService;
import com.rkylin.wheatfield.common.ValHasNoParam;
import com.rkylin.wheatfield.constant.BaseConstants;
import com.rkylin.wheatfield.constant.Constants;
import com.rkylin.wheatfield.constant.TransCodeConst;
import com.rkylin.wheatfield.dao.FinanaceAccountDao;
import com.rkylin.wheatfield.exception.AccountException;
import com.rkylin.wheatfield.manager.FinanaceAccountManager;
import com.rkylin.wheatfield.manager.FinanaceEntryManager;
import com.rkylin.wheatfield.manager.TransOrderInfoManager;
import com.rkylin.wheatfield.model.CommonResponse;
import com.rkylin.wheatfield.model.WipeAccountResponse;
import com.rkylin.wheatfield.pojo.Balance;
import com.rkylin.wheatfield.pojo.FinanaceAccount;
import com.rkylin.wheatfield.pojo.FinanaceAccountQuery;
import com.rkylin.wheatfield.pojo.FinanaceEntry;
import com.rkylin.wheatfield.pojo.FinanaceEntryHistory;
import com.rkylin.wheatfield.pojo.TransOrderInfo;
import com.rkylin.wheatfield.pojo.TransOrderInfoQuery;
import com.rkylin.wheatfield.pojo.User;
import com.rkylin.wheatfield.response.ErrorResponse;
import com.rkylin.wheatfield.response.Response;
import com.rkylin.wheatfield.service.CheckInfoService;
import com.rkylin.wheatfield.service.IAPIService;
import com.rkylin.wheatfield.service.OperationServive;
import com.rkylin.wheatfield.service.PaymentInternalService;
import com.rkylin.wheatfield.utils.ArithUtil;
import com.rkylin.wheatfield.utils.CodeEnum;
import com.rkylin.wheatfield.utils.DateUtil;
@Transactional
@Service("paymentInternalService")
public class PaymentInternalServiceImpl implements PaymentInternalService,PaymentInternalOutService,IAPIService {
	private static Object lock=new Object();
	private static Logger logger = LoggerFactory.getLogger(PaymentAccountServieImpl.class);	
	@Autowired
	OperationServive operationService;
	@Autowired
	CheckInfoService checkInfoService;	
	@Autowired
	FinanaceEntryManager finanaceEntryManager;
	@Autowired
	RedisIdGenerator redisIdGenerator;
	@Autowired
	FinanaceAccountManager finanaceAccountManager;
	@Autowired
	TransOrderInfoManager transOrderInfoManager;
	@Autowired
	private FinanaceAccountDao finanaceAccountDao;
	
	ArithUtil arithUtil=new ArithUtil();
	DateUtil dateUtil=new DateUtil();
	@Override
	public ErrorResponse credit(FinanaceEntry finanaceEntry,String userId,String merchantId,String productId,String referAccountId) {
		// TODO 用户可透支额度增加，丰年预付信用账户余额增加
		logger.info("------为账户"+userId+"授信开始(用户可透支额度增加，丰年预付信用账户余额增加)------");
		logger.info("参数信息：finanaceEntry.paymentamount="+finanaceEntry.getPaymentAmount()+",finanaceEntry.referId="+finanaceEntry.getReferId()
				+",userId="+userId+",merchantId="+merchantId+",productId="+productId+",referAccountId="+referAccountId);
		synchronized (lock) {
			ErrorResponse response=new ErrorResponse();
			String reCode = "S0";
			String reMsg = "成功";
			//获取每个账户记账流水
			List<FinanaceEntry> finanaceEntries=new ArrayList<FinanaceEntry>();
			//获取所有账户记账流水
			List<FinanaceEntry> finanaceEntrieAlls=new ArrayList<FinanaceEntry>();
			User user=new User();
			user.userId=userId;
			user.constId=merchantId;
			user.productId=productId;
			user.referUserId=referAccountId;
			//判断账户状态是否正常
			boolean accountIsOK=operationService.checkAccount(user);
			if(!accountIsOK){
				logger.error("用户"+userId+"状态为非正常状态或账户不存在授信子账户");
				reCode = "S1";
				reMsg = "账户状态非正常状态";
			}else{
				try {
					String uId;
					//获取套录号
					String entryId=redisIdGenerator.createRequestNo();
					logger.info("用户"+userId+"授信套录号信息为："+entryId);
					for (int i = 0; i <= 1; i++) {
						uId=null;
						boolean flag=true;
						if(0==i){
							uId=userId;	
						}else {
							uId=TransCodeConst.THIRDPARTYID_FNYFXYZH;		
							flag=false;
						}
						user.userId=uId;
						Balance balance=null;
						//获取账户余额信息
						if(0==i){
							balance=checkInfoService.getBalance(user,"");
						}else{
							balance=checkInfoService.getBalance(user,uId);
						}
						if(null!=balance){
							balance.setPulseDegree(balance.getPulseDegree()+1);
							finanaceEntries=checkInfoService.getInternalFinanaceEntries(finanaceEntry, balance, flag, "credit", userId);
							if(finanaceEntries!=null){						
								if(null!=finanaceEntries&&finanaceEntries.size()>0){
									for(FinanaceEntry finanaceEntryInfo:finanaceEntries) {
										finanaceEntryInfo.setReferEntryId(entryId);
//										if(0==i){
//											finanaceEntryInfo.setAccrualType(BaseConstants.TYPE_BALANCE_CREDIT);
//										}else{
//											finanaceEntryInfo.setAccrualType(BaseConstants.TYPE_BALANCE_SETTLE);
//										}
										finanaceEntryInfo.setAccrualType(BaseConstants.TYPE_BALANCE_SETTLE);
										
										finanaceEntrieAlls.add(finanaceEntryInfo);
									}
								}else{
									logger.error("获取用户["+userId+"]账户流水信息失败");
									reCode = "S3";
									reMsg = "账户流水数据入库失败";
									break;
								}
							}else{
								logger.error("获取用户["+userId+"]账户流水信息失败");
								reCode = "S3";
								reMsg = "账户流水数据入库失败";
								break;
							}							
						}else{
							logger.error("获取用户["+userId+"]余额信息失败");
							reCode = "S4";
							reMsg = "用户余额信息查询失败";
						}
					}
					if("S0".equals(reCode)){
						//批量插入记录流水表
						if(!this.insertFinanaceEntry(finanaceEntrieAlls)){
							reCode = "S5";
							reMsg = "数据库操作异常";
						}
					}
					
				} catch (AccountException e) {
					// TODO: handle exception
					logger.error(e.getMessage());
					reCode = "S5";
					reMsg = "数据库操作异常";
				}
			}
			if("S0".equals(reCode)){
				response.setIs_success(true);
			}else{
				response.setCode(reCode);
				response.setMsg(reMsg);
			}		
			logger.info("------为账户"+userId+"授信结束------");
			return response;
		}
	}

	@Override
	public ErrorResponse downPaymentCredit(FinanaceEntry finanaceEntry,String userId, String merchantId, String productId,String referAccountId) {
		// TODO 预付金-----授信   丰年预付金账户金额增加   账户/商户可提现余额增加
		logger.info("------为账户"+userId+"做预付金--授信开始(丰年预付金账户金额增加   账户/商户可提现余额增加)------");
		logger.info("参数信息：finanaceEntry.paymentamount="+finanaceEntry.getPaymentAmount()+",finanaceEntry.referId="+finanaceEntry.getReferId()
				+",userId="+userId+",merchantId="+merchantId+",productId="+productId+",referAccountId="+referAccountId);
		synchronized (lock) {
			ErrorResponse response=new ErrorResponse();
			String reCode = "S0";
			String reMsg = "成功";
			//获取每个账户记账流水
			List<FinanaceEntry> finanaceEntries=new ArrayList<FinanaceEntry>();
			//获取所有账户记账流水
			List<FinanaceEntry> finanaceEntrieAlls=new ArrayList<FinanaceEntry>();
			User user=new User();
			user.userId=userId;
			user.constId=merchantId;
			user.productId=productId;
			user.referUserId=referAccountId;
			//判断账户状态是否正常
			boolean accountIsOK=operationService.checkAccount(user);
			if(!accountIsOK){
				logger.error("用户"+userId+"状态为非正常状态或账户没有预付金子账户");
				reCode = "S1";
				reMsg = "账户状态非正常状态";
			}else{
				try {
					String uId;
					//获取套录号
					String entryId=null;
					if(null!=finanaceEntry.getReferEntryId()&&!"".equals(finanaceEntry.getReferEntryId())){
						entryId=finanaceEntry.getReferEntryId();
					}else{
						entryId=redisIdGenerator.createRequestNo();
					}
					logger.info("账户预付金--授信的套录号为："+entryId);
					for (int i = 0; i <= 1; i++) {
						uId=null;
						boolean flag=true;
						if(0==i){
							uId=userId;	
						}else {
							uId=TransCodeConst.THIRDPARTYID_FNYFJZH;							
						}
						user.userId=uId;
						//获取账户余额
						Balance balance=null;
						if(0==i){
							balance=checkInfoService.getBalance(user,"");
						}else{
							balance=checkInfoService.getBalance(user,uId);
						}
						if(null!=balance){
							balance.setPulseDegree(balance.getPulseDegree()+1);
							finanaceEntries=checkInfoService.getInternalFinanaceEntries(finanaceEntry, balance, flag, "yufujin", userId);
							if(finanaceEntries!=null){						
								if(null!=finanaceEntries&&finanaceEntries.size()>0){
									for(FinanaceEntry finanaceEntryInfo:finanaceEntries) {
										finanaceEntryInfo.setReferEntryId(entryId);
										finanaceEntryInfo.setAccrualType(BaseConstants.TYPE_BALANCE_SETTLE);									
										finanaceEntrieAlls.add(finanaceEntryInfo);
									}
							}else{
								logger.error("获取用户["+userId+"]账户流水信息失败");
								reCode = "S3";
								reMsg = "账户流水数据入库失败";
								break;
							}
							}else{
								logger.error("获取用户["+userId+"]账户流水信息失败");
								reCode = "S3";
								reMsg = "账户流水数据入库失败";
								break;
							}							
						}else{
							logger.error("获取用户["+userId+"]余额信息失败");
							reCode = "S4";
							reMsg = "用户余额信息查询失败";
						}
					}
					if("S0".equals(reCode)){
						//批量插入记录流水表
						if(!this.insertFinanaceEntry(finanaceEntrieAlls)){
							logger.error("用户["+userId+"]分录流水入库失败");
							reCode = "S5";
							reMsg = "数据库操作异常";
						}
					}
				} catch (AccountException e) {
					logger.error(e.getMessage());
					reCode = "S5";
					reMsg = "数据库操作异常";
				}
			}
			if("S0".equals(reCode)){
				response.setIs_success(true);
			}else{
				response.setCode(reCode);
				response.setMsg(reMsg);
			}		
			logger.info("------为账户"+userId+"做预付金--授信结束(丰年预付金账户金额增加   账户/商户可提现余额增加)------");
			return response;
		}
	}
	
	@Override
	public ErrorResponse withdrawReturn(FinanaceEntry finanaceEntry,String merchantId) {
		// TODO 丰年主账户可提现余额减少，提现待清算账户可提现余额减少
		logger.info("------提现返回开始(丰年备付金账户可提现余额减少，提现待清算账户可提现余额减少)------");
		logger.info("参数信息：finanaceEntry.paymentamount="+finanaceEntry.getPaymentAmount()+",finanaceEntry.referId="+finanaceEntry.getReferId()
				+",merchantId="+merchantId);
		synchronized (lock) {
			ErrorResponse response=new ErrorResponse();
			String reCode = "S0";
			String reMsg = "成功";
			//获取每个账户记账流水
			List<FinanaceEntry> finanaceEntries=new ArrayList<FinanaceEntry>();
			//获取所有账户记账流水
			List<FinanaceEntry> finanaceEntrieAlls=new ArrayList<FinanaceEntry>();
			User user=new User();
			try {
				//获取套录号
				String entryId=redisIdGenerator.createRequestNo();
				logger.info("账户提现返回的套录号为："+entryId);
				String uId;
				for (int i = 0; i <= 1; i++) {
					uId=null;
					boolean flag=false;
					if(0==i){
						uId=TransCodeConst.THIRDPARTYID_FNZZH;	
					}else {
						uId=TransCodeConst.THIRDPARTYID_TXDQSZH;
					}
					//获取账户余额
					Balance balance=checkInfoService.getBalance(user,uId);
					if(null!=balance){
						balance.setPulseDegree(balance.getPulseDegree()+1);
						finanaceEntries=checkInfoService.getInternalFinanaceEntries(finanaceEntry, balance, flag, "withdrawReturn", uId);
						if(finanaceEntries!=null){						
							if(null!=finanaceEntries&&finanaceEntries.size()>0){
								for(FinanaceEntry finanaceEntryInfo:finanaceEntries) {
									finanaceEntryInfo.setReferEntryId(entryId);
									finanaceEntryInfo.setAccrualType(BaseConstants.TYPE_BALANCE_SETTLE);									
									finanaceEntrieAlls.add(finanaceEntryInfo);
								}
						}else{
							logger.error("获取用户["+uId+"]账户流水信息失败");
							reCode = "S3";
							reMsg = "账户流水数据入库失败";
							break;
						}
						}else{
							logger.error("获取用户["+uId+"]账户流水信息失败");
							reCode = "S3";
							reMsg = "账户流水数据入库失败";
							break;
						}							
					}else{
						logger.error("获取用户["+uId+"]余额信息失败");
						reCode = "S4";
						reMsg = "用户余额信息查询失败";
					}
				}
				if("S0".equals(reCode)){
					//批量插入记录流水表
					if(!this.insertFinanaceEntry(finanaceEntrieAlls)){
						logger.error("分录流水入库失败");
						reCode = "S5";
						reMsg = "数据库操作异常";
					}
				}
				
			} catch (AccountException e) {
				logger.error(e.getMessage());
				reCode = "S5";
				reMsg = "数据库操作异常";
			}
			if("S0".equals(reCode)){
				response.setIs_success(true);
			}else{
				response.setCode(reCode);
				response.setMsg(reMsg);
			}
			logger.info("------提现返回结束(丰年备付金账户可提现余额减少，提现待清算账户可提现余额减少)------");
			return response;
		}
	}

	@Override
	public ErrorResponse refundReturn(FinanaceEntry finanaceEntry,String userId,String merchantId) {
		// TODO 丰年贴息成本账户增加，丰年预付信用账户余额增加，P2P账户余额增加，用户可透余额增加
		//logger.info("------还款返回开始(贴息成本账户增加，丰年预付信用账户余额增加，P2P账户余额增加，用户可透余额增加)------");
		logger.info("------还款返回开始(贴息成本账户增加，其它应收款金额减，P2P账户余额增加，用户授信子账户增加)------");
		logger.info("参数信息：finanaceEntry.paymentamount="+finanaceEntry.getPaymentAmount()+",finanaceEntry.referId="+finanaceEntry.getReferId()
				+",userId="+userId+",merchantId="+merchantId);
		synchronized (lock) {
			ErrorResponse response=new ErrorResponse();
			String reCode = "S0";
			String reMsg = "成功";
			//获取每个账户记账流水
			List<FinanaceEntry> finanaceEntries=new ArrayList<FinanaceEntry>();
			//获取所有账户记账流水
			List<FinanaceEntry> finanaceEntrieAlls=new ArrayList<FinanaceEntry>();
			User user=new User();
			user.userId=userId;
			user.constId=merchantId;
			user.productId=Constants.USER_SUB_ACCOUNT;
			//判断账户状态是否正常
			boolean accountIsOK=operationService.checkAccount(user);
			if(!accountIsOK){
				logger.error("用户"+userId+"状态为非正常状态或账户没有授信子账户");
				reCode = "S1";
				reMsg = "账户状态非正常状态";
			}else{
				try {
					//获取套录号
					String entryId=redisIdGenerator.createRequestNo();
					logger.info("还款返回的套录号为："+entryId);
					String uId;
					for (int i = 0; i <= 1; i++) {
						uId=null;
						boolean flag=true;//记账方向   true= C  false= D 
						if(0==i){
							uId=userId;	
							flag=true;
						}else {
							//机构号临时写入备注
							finanaceEntry.setRemark(merchantId);
							uId=TransCodeConst.THIRDPARTYID_FNTXYWCBZH;
							flag=false;
						}
						user.userId=uId;
						//获取账户余额
						Balance balance=null;
						if(i == 0){
							balance=checkInfoService.getBalance(user,"");
						}else{
							balance=checkInfoService.getBalance(user,uId);
						}
						if(null!=balance){
							balance.setPulseDegree(balance.getPulseDegree()+1);
							finanaceEntries=checkInfoService.getInternalFinanaceEntries(finanaceEntry, balance, flag, "refundReturn", userId);
							if(finanaceEntries!=null){						
								if(null!=finanaceEntries&&finanaceEntries.size()>0){
									for(FinanaceEntry finanaceEntryInfo:finanaceEntries) {
										finanaceEntryInfo.setReferEntryId(entryId);
										if(0==i){
											finanaceEntryInfo.setAccrualType(BaseConstants.TYPE_BALANCE_CREDIT);	
										}else{
											finanaceEntryInfo.setAccrualType(BaseConstants.TYPE_BALANCE_SETTLE);
										}
										finanaceEntrieAlls.add(finanaceEntryInfo);
									}
							}else{
								logger.error("获取用户["+userId+"]账户流水信息失败");
								reCode = "S3";
								reMsg = "账户流水数据入库失败";
								break;
							}
							}else{
								logger.error("获取用户["+userId+"]账户流水信息失败");
								reCode = "S3";
								reMsg = "账户流水数据入库失败";
								break;
							}							
						}else{
							logger.error("获取用户["+userId+"]余额信息失败");
							reCode = "S4";
							reMsg = "用户余额信息查询失败";
						}
					}
					if("S0".equals(reCode)){
						//批量插入记录流水表
						if(!this.insertFinanaceEntry(finanaceEntrieAlls)){
							logger.error("分录流水入库失败");
							reCode = "S5";
							reMsg = "数据库操作异常";
						}
					}
					
				} catch (AccountException e) {
					logger.error(e.getMessage());
					reCode = "S5";
					reMsg = "数据库操作异常";
				}
			}
			if("S0".equals(reCode)){
				response.setIs_success(true);
			}else{
				response.setCode(reCode);
				response.setMsg(reMsg);
			}
			logger.info("------还款返回结束(丰年贴息成本账户增加，丰年预付信用账户余额增加，P2P账户余额增加，用户可透余额增加)------");
			return response;
		}
	}

	@Override
	public ErrorResponse rightsPackageReturn(FinanaceEntry finanaceEntry,String merchantId) {
		// TODO 其他应付款款余额增加，丰年主账户余额增加
		logger.info("------债权包返回开始(其他应付款款余额增加，丰年主账户余额增加)------");
		logger.info("参数信息：finanaceEntry.paymentamount="+finanaceEntry.getPaymentAmount()+",finanaceEntry.referId="+finanaceEntry.getReferId()
				+",merchantId="+merchantId);
		synchronized (lock) {
			ErrorResponse response=new ErrorResponse();
			String reCode = "S0";
			String reMsg = "成功";
			//获取每个账户记账流水
			List<FinanaceEntry> finanaceEntries=new ArrayList<FinanaceEntry>();
			//获取所有账户记账流水
			List<FinanaceEntry> finanaceEntrieAlls=new ArrayList<FinanaceEntry>();
			User user=new User();
			user.constId=merchantId;
			try {
				//获取套录号
				String entryId=redisIdGenerator.createRequestNo();
				logger.info("债权包返回的套录号为："+entryId);
				String uId;
				for (int i = 0; i <= 1; i++) {
					uId=null;
					boolean flag=true;//记账方向   true= C  false= D 
					if(0==i){
						uId=TransCodeConst.THIRDPARTYID_FNZZH;	
					}else {
						uId=TransCodeConst.THIRDPARTYID_QTYFKZH;
					}
					user.userId=uId;
					//获取账户余额
					Balance balance=checkInfoService.getBalance(user,uId);
					if(null!=balance){
						balance.setPulseDegree(balance.getPulseDegree()+1);
						finanaceEntries=checkInfoService.getInternalFinanaceEntries(finanaceEntry, balance, flag, "rightsPackageReturn", uId);
						if(finanaceEntries!=null){						
							if(null!=finanaceEntries&&finanaceEntries.size()>0){
								for(FinanaceEntry finanaceEntryInfo:finanaceEntries) {
									finanaceEntryInfo.setReferEntryId(entryId);
									finanaceEntryInfo.setAccrualType(BaseConstants.TYPE_BALANCE_SETTLE);									
									finanaceEntrieAlls.add(finanaceEntryInfo);
								}
							}else{
								logger.error("获取用户["+uId+"]账户流水信息失败");
								reCode = "S3";
								reMsg = "账户流水数据入库失败";
								break;
							}
						}else{
							logger.error("获取用户["+uId+"]账户流水信息失败");
							reCode = "S3";
							reMsg = "账户流水数据入库失败";
							break;
						}							
					}else{
						logger.error("获取用户["+uId+"]余额信息失败");
						reCode = "S4";
						reMsg = "用户余额信息查询失败";
					}
				}
				if("S0".equals(reCode)){
					//批量插入记录流水表
					if(!this.insertFinanaceEntry(finanaceEntrieAlls)){
						logger.error("分录流水入库失败");
						reCode = "S5";
						reMsg = "数据库操作异常";
					}
				}
				
			} catch (AccountException e) {
				logger.error(e.getMessage());
				reCode = "S5";
				reMsg = "数据库操作异常";
			}
			if("S0".equals(reCode)){
				response.setIs_success(true);
			}else {
				response.setCode(reCode);
				response.setMsg(reMsg);
			}
			logger.info("------债权包返回结束(其他应付款款余额增加，丰年主账户余额增加)------");
			return response;
		}
	}

	/**
	 * 分润，供外部系统调用（dubbo）
	 * @param finanaceEntry
	 * @param userId
	 * @param merchantId
	 * @param productId
	 * @param referUserId
	 * @return
	 */
	public String shareBenefit(FinanaceEntry finanaceEntry,String userId,String merchantId,String productId,String referUserId) {
		ErrorResponse res = fenrun(finanaceEntry, userId, merchantId, productId, referUserId);
		if (res.isIs_success()) {
			return CodeEnum.SUCCESS.getCode();
		}
		return CodeEnum.FAILURE.getCode();
	}
	
	@Override
	public ErrorResponse fenrun(FinanaceEntry finanaceEntry,String userId,String merchantId,String productId,String referUserId) {
		// TODO 分润
		logger.info("------分润开始------");
		logger.info("参数信息：finanaceEntry.paymentamount="+finanaceEntry.getPaymentAmount()+",finanaceEntry.referId="+finanaceEntry.getReferId()
				+",userId="+userId+",merchantId="+merchantId+",productId="+productId+",referUserId="+referUserId);
		synchronized (lock) {
			String reCode = "S0";
			String reMsg = "成功";
			ErrorResponse response=new ErrorResponse();
			//获取每个账户记账流水
			List<FinanaceEntry> finanaceEntries=new ArrayList<FinanaceEntry>();
			//获取所有账户记账流水
			List<FinanaceEntry> finanaceEntrieAlls=new ArrayList<FinanaceEntry>();
			try {
				//获取套录号
				String entryId=redisIdGenerator.createRequestNo();
				logger.info("分润的套录号为："+entryId);
				/**
				 * 
				 * 1.判断商户是否有台长的预付金
				 * 2.没有------其他应付款减   商户主账户加
				 * 3.有--------判断预付金金额是否大于分润金额
				 * 4.不大于----其它应付款减  台长主账户加  商户预付金子账户减  丰年预付金账户减
				 * 5.大于------ 预付金部分  其它应付款账户减  台长主账户加  商户预付金子账户减  丰年预付金账户减
				 * ------------ 正常分润   其他应付款账户减  商户主账户加
				 */
				User user=new User();
				user.constId=merchantId;
				user.productId=productId;
				user.referUserId=referUserId;
				if(null==referUserId||"".equals(referUserId)){
					//判断referUserId是否为空，为空直接走以下流程				
					user.referUserId=null;
					for (int i = 0; i <= 1; i++) {
						String uId=null;
						boolean flag=true;//记账方向   true= C  false= D 
						if(0==i){
							if(userId.equals(Constants.FN_ID)){//判断是否为丰年机构号
								uId=TransCodeConst.THIRDPARTYID_FNSYZH;//丰年收益账户
							}else{
								uId=userId;	//商户userId  如没有商户Id该处为台长Id  
							}							
						}else {
							flag=false;
							uId=TransCodeConst.THIRDPARTYID_QTYFKZH; //其它应付款账户
						}
						user.userId=uId;
						//user.productId=Constants.FN_PRODUCT;
						Balance balance=null;
						if(flag){
							if(userId.equals(Constants.FN_ID)){
								balance=checkInfoService.getBalance(user,uId);
							}else{
								balance=checkInfoService.getBalance(user,"");
							}							
						}else{
							balance=checkInfoService.getBalance(user,uId);
						}
						if(null==balance){
							logger.error("获取用户["+uId+"]余额信息失败");
							reCode = "S1";
							reMsg = "用户余额信息查询失败";
							break;
						}
						balance.setPulseDegree(balance.getPulseDegree()+1);
						finanaceEntries=checkInfoService.getInternalFinanaceEntries(finanaceEntry, balance, flag, "fenrun", uId);
						if(finanaceEntries==null || finanaceEntries.size()==0){	
							logger.error("获取用户["+uId+"]账户流水信息失败");
							reCode = "S1";
							reMsg = "账户流水数据入库失败";
							break;	
						}					
						for(FinanaceEntry finanaceEntryInfo:finanaceEntries) {
							finanaceEntryInfo.setReferEntryId(entryId);
							finanaceEntryInfo.setAccrualType(BaseConstants.TYPE_BALANCE_SETTLE);									
							finanaceEntrieAlls.add(finanaceEntryInfo);
						}
					}
				}else{
					//************商户中包含该ReferUserId的预付金子账户，走以下流程								
					//获取商户下的预付金子账户信息
					FinanaceAccountQuery query=new FinanaceAccountQuery();
					query.setAccountRelateId(userId);
					query.setRootInstCd(merchantId);
					query.setGroupManage(productId);
					query.setReferUserId(referUserId);
					List<FinanaceAccount> finanaceAccounts=finanaceAccountManager.queryList(query);
					if(null!=finanaceAccounts&&1==finanaceAccounts.size()){
						//设置其他应付款账户心跳次数
						int pulseDegreeQTYF=0;
						//获取预付金子账户金额
						Balance balanceYFJ=checkInfoService.getBalance(user,finanaceAccounts.get(0).getFinAccountId());
						long amount=balanceYFJ.getBalanceSettle();
						if(amount>0){
							//判断预付金额是否大于分润金额
							if(finanaceEntry.getPaymentAmount()>amount){//分润金额大于预付金额
								//获取大于预付金金额
								long money=finanaceEntry.getPaymentAmount()-amount;
								//######################################################预付金部分
								finanaceEntry.setPaymentAmount(amount);
								for (int i = 0; i <=3; i++) {
									String uId=null;
									boolean flag=true;//记账方向   true= C +   false= D -
									if(0==i){//1000
										uId=userId;	//商户预付金UserId
										flag=false;
									}else if(1==i) {//1000
										flag=true;
										uId=referUserId;//商户预付金子账户第三方ID（台长主账户）
									}else if(2==i){//1000
										flag=false;
										uId=TransCodeConst.THIRDPARTYID_QTYFKZH;//其他应付款账户
									}else if(3==i){
										flag=false;
										uId=TransCodeConst.THIRDPARTYID_FNYFJZH;//丰年预付金账户
									}
									if(i>0){
										//user.productId=Constants.FN_PRODUCT;//主账户产品号
										user.referUserId=null;
									}
									user.userId=uId;
									Balance balance=null;
									if(flag){
										balance=checkInfoService.getBalance(user,"");
									}else{
										if(i==0){
											balance=checkInfoService.getBalance(user,"");
										}else{
											balance=checkInfoService.getBalance(user,uId);
										}
									}
									if(null!=balance){
										balance.setPulseDegree(balance.getPulseDegree()+1);
										if(2==i){//其他应付款账户  需设置心跳计次
											pulseDegreeQTYF=balance.getPulseDegree();
										}
										finanaceEntries=checkInfoService.getInternalFinanaceEntries(finanaceEntry, balance, flag, "fenrun", uId);
										if(finanaceEntries!=null && 0<finanaceEntries.size()){						
											for(FinanaceEntry finanaceEntryInfo:finanaceEntries) {
												finanaceEntryInfo.setReferEntryId(entryId);
												finanaceEntryInfo.setAccrualType(BaseConstants.TYPE_BALANCE_SETTLE);									
												finanaceEntrieAlls.add(finanaceEntryInfo);
											}
										}else{
											logger.error("获取用户["+uId+"]账户流水信息失败");
											reCode = "S1";
											reMsg = "账户流水数据入库失败";
											break;
										}							
									}else{
										logger.error("获取用户["+uId+"]余额信息失败");
										reCode = "S1";
										reMsg = "用户余额信息查询失败";
										break;
									}
								}
								//#############################################################################分润部分
								finanaceEntry.setPaymentAmount(money);
								for (int i = 0; i <= 1; i++) {
									String uId=null;
									boolean flag=true;//记账方向   true= C  false= D 
									if(0==i){
										uId=userId;	//商户userId  如没有商户Id该处为台长Id  
									}else {
										flag=false;
										uId=TransCodeConst.THIRDPARTYID_QTYFKZH;
									}
									user.userId=uId;
									//user.productId=Constants.FN_PRODUCT;
									user.referUserId=null;
									Balance balance=null;
									if(flag){
										balance=checkInfoService.getBalance(user,"");
									}else{
										balance=checkInfoService.getBalance(user,uId);
									}
									if(null!=balance){
										if(1==i){
											balance.setPulseDegree(pulseDegreeQTYF+1);
										}else{
											balance.setPulseDegree(balance.getPulseDegree()+1);
										}
										finanaceEntries=checkInfoService.getInternalFinanaceEntries(finanaceEntry, balance, flag, "fenrun", uId);
										if(finanaceEntries!=null && 0<finanaceEntries.size()){						
											for(FinanaceEntry finanaceEntryInfo:finanaceEntries) {
												finanaceEntryInfo.setReferEntryId(entryId);
												finanaceEntryInfo.setAccrualType(BaseConstants.TYPE_BALANCE_SETTLE);									
												finanaceEntrieAlls.add(finanaceEntryInfo);
											}
										}else{
											logger.error("获取用户["+uId+"]账户流水信息失败");
											reCode = "S1";
											reMsg = "账户流水数据入库失败";
											break;
										}							
									}else{
										logger.error("获取用户["+uId+"]余额信息失败");
										reCode = "S1";
										reMsg = "用户余额信息查询失败";
										break;
									}
								}
							}else{
								//分润金额小于预付金额
								for (int i = 0; i <=3; i++) {
									String uId=null;
									boolean flag=true;//记账方向   true= C +   false= D -
									if(0==i){
										uId=userId;	//商户预付金UserId
										flag=false;
									}else if(1==i) {
										flag=true;
										uId=referUserId;//商户预付金子账户第三方ID（台长主账户）
									}else if(2==i){
										flag=false;
										uId=TransCodeConst.THIRDPARTYID_QTYFKZH;//其他应付款账户
									}else if(3==i){
										flag=false;
										uId=TransCodeConst.THIRDPARTYID_FNYFJZH;//丰年预付金账户
									}
									if(i>0){
										//user.productId=Constants.FN_PRODUCT;//主账户产品号
										user.referUserId=null;
									}
									user.userId=uId;
									Balance balance=null;
									if(2>i){
										balance=checkInfoService.getBalance(user,"");
									}else{
										balance=checkInfoService.getBalance(user,uId);
									}
									if(null!=balance){
										balance.setPulseDegree(balance.getPulseDegree()+1);
										finanaceEntries=checkInfoService.getInternalFinanaceEntries(finanaceEntry, balance, flag, "fenrun", uId);
										if(finanaceEntries!=null && 0<finanaceEntries.size()){						
											for(FinanaceEntry finanaceEntryInfo:finanaceEntries) {
												finanaceEntryInfo.setReferEntryId(entryId);
												finanaceEntryInfo.setAccrualType(BaseConstants.TYPE_BALANCE_SETTLE);									
												finanaceEntrieAlls.add(finanaceEntryInfo);
											}
										}else{
											logger.error("获取用户["+uId+"]账户流水信息失败");
											reCode = "S1";
											reMsg = "账户流水数据入库失败";
											break;
										}							
									}else{
										logger.error("获取用户["+uId+"]余额信息失败");
										reCode = "S1";
										reMsg = "用户余额信息查询失败";
										break;
									}
								}
							}
						}else{
							//######################################预付金为零
							for (int i = 0; i <= 1; i++) {
								String uId=null;
								boolean flag=true;//记账方向   true= C  false= D 
								if(0==i){
									uId=userId;	//商户userId  如没有商户Id该处为台长Id  
								}else {
									flag=false;
									uId=TransCodeConst.THIRDPARTYID_QTYFKZH;
								}
								user.userId=uId;
								//user.productId=Constants.FN_PRODUCT;
								user.referUserId=null;
								Balance balance=null;
								if(flag){
									balance=checkInfoService.getBalance(user,"");
								}else{
									balance=checkInfoService.getBalance(user,uId);
								}
								if(null!=balance){
									balance.setPulseDegree(balance.getPulseDegree()+1);
									finanaceEntries=checkInfoService.getInternalFinanaceEntries(finanaceEntry, balance, flag, "fenrun", uId);
									if(finanaceEntries!=null && 0<finanaceEntries.size()){						
										for(FinanaceEntry finanaceEntryInfo:finanaceEntries) {
											finanaceEntryInfo.setReferEntryId(entryId);
											finanaceEntryInfo.setAccrualType(BaseConstants.TYPE_BALANCE_SETTLE);									
											finanaceEntrieAlls.add(finanaceEntryInfo);
										}
									}else{
										logger.error("获取用户["+uId+"]账户流水信息失败");
										reCode = "S1";
										reMsg = "账户流水数据入库失败";
										break;
									}							
								}else{
									logger.error("获取用户["+uId+"]余额信息失败");
									reCode = "S1";
									reMsg = "用户余额信息查询失败";
									break;
								}
							}
						}
					}else{
						//如商户下边没有对应台长预付金子账户的账户信息，则将分润金额入商户账户
						logger.info("没有获取到商户"+userId+"与台长"+referUserId+"的预付金子账户对应关系，将分润金额全部入商户账户");
						user.referUserId=null;
						for (int i = 0; i <= 1; i++) {
							String uId=null;
							boolean flag=true;//记账方向   true= C  false= D 
							if(0==i){
								uId=userId;	//商户userId  					
							}else {
								flag=false;
								uId=TransCodeConst.THIRDPARTYID_QTYFKZH;
							}
							user.userId=uId;
							//user.productId=Constants.FN_PRODUCT;
							Balance balance=null;
							if(flag){
								balance=checkInfoService.getBalance(user,"");					
							}else{
								balance=checkInfoService.getBalance(user,uId);
							}
							if(null!=balance){
								balance.setPulseDegree(balance.getPulseDegree()+1);
								finanaceEntries=checkInfoService.getInternalFinanaceEntries(finanaceEntry, balance, flag, "fenrun", uId);
								if(finanaceEntries!=null && 0<finanaceEntries.size()){						
									for(FinanaceEntry finanaceEntryInfo:finanaceEntries) {
										finanaceEntryInfo.setReferEntryId(entryId);
										finanaceEntryInfo.setAccrualType(BaseConstants.TYPE_BALANCE_SETTLE);									
										finanaceEntrieAlls.add(finanaceEntryInfo);
									}
								}else{
									logger.error("获取用户["+uId+"]账户流水信息失败");
									reCode = "S1";
									reMsg = "账户流水数据入库失败";
									break;
								}							
							}else{
								logger.error("获取用户["+uId+"]余额信息失败");
								reCode = "S1";
								reMsg = "用户余额信息查询失败";
								break;
							}
						}
					}
				}	
				if("S0".equals(reCode)){
					//批量插入记录流水表
					if(!this.insertFinanaceEntry(finanaceEntrieAlls)){
						logger.error("分录流水入库失败");
						reCode = "S2";
						reMsg = "数据库操作异常";
					}
				}
				
			} catch (AccountException e) {
				logger.error("分润接口异常：" + e.getStackTrace().toString(), e);
				reCode = "S2";
				reMsg = "数据库操作异常";
			}
			if("S0".equals(reCode)){
				response.setIs_success(true);
			}else {
				response.setCode(reCode);
				response.setMsg(reMsg);
			}
			logger.info("------分润结束------");
			return response;
		}
	}
	
	@Override
	public ErrorResponse collectionReturn(FinanaceEntry finanaceEntry,String merchantId, String userId, String productId) {
		// TODO 代收返回   账户提现金额增加   其它应收款减
		logger.info("------为账户"+userId+"代收返回开始(账户提现金额增加   其它应收款减)------");
		synchronized (lock) {
			ErrorResponse response=new ErrorResponse();
			String reCode = "S0";
			String reMsg = "成功";
			//获取每个账户记账流水
			List<FinanaceEntry> finanaceEntries=new ArrayList<FinanaceEntry>();
			//获取所有账户记账流水
			List<FinanaceEntry> finanaceEntrieAlls=new ArrayList<FinanaceEntry>();
			User user=new User();
			user.constId=merchantId;
			user.productId=productId;
			try {
				//获取套录号
				String entryId=redisIdGenerator.createRequestNo();
				logger.info("账户"+userId+"代收返回的套录号为："+entryId);
				String uId=null;;
				for (int i = 0; i <= 1; i++) {
					boolean flag=true;//记账方向   true= C  false= D 
					if(0==i){
						uId=userId;	
					}else {
						flag=false;
						uId=TransCodeConst.THIRDPARTYID_QTYSKZH;//其它应收款
					}
					user.userId=uId;
					//获取账户余额
					Balance balance=null;
					if(flag){
						balance=checkInfoService.getBalance(user,"");
					}else{
						balance=checkInfoService.getBalance(user,uId);
					}
					if(null!=balance){
						balance.setPulseDegree(balance.getPulseDegree()+1);
						finanaceEntries=checkInfoService.getInternalFinanaceEntries(finanaceEntry, balance, flag, "collectionReturn", uId);
						if(finanaceEntries!=null){						
							if(null!=finanaceEntries&&finanaceEntries.size()>0){
								for(FinanaceEntry finanaceEntryInfo:finanaceEntries) {
									finanaceEntryInfo.setReferEntryId(entryId);
									finanaceEntryInfo.setAccrualType(BaseConstants.TYPE_BALANCE_SETTLE);									
									finanaceEntrieAlls.add(finanaceEntryInfo);
								}
						}else{
							logger.error("获取用户["+uId+"]账户流水信息失败");
							reCode = "S3";
							reMsg = "账户流水数据入库失败";
							break;
						}
						}else{
							logger.error("获取用户["+uId+"]账户流水信息失败");
							reCode = "S3";
							reMsg = "账户流水数据入库失败";
							break;
						}							
					}else{
						logger.error("获取用户["+uId+"]余额信息失败");
						reCode = "S4";
						reMsg = "用户余额信息查询失败";
					}
				}
				if("S0".equals(reCode)){
					//批量插入记录流水表
					if(!this.insertFinanaceEntry(finanaceEntrieAlls)){
						logger.error("分录流水信息入库失败");
						reCode = "S5";
						reMsg = "数据库操作异常";
					}
				}
			} catch (AccountException e) {
				logger.error(e.getMessage());
				reCode = "S5";
				reMsg = "数据库操作异常";
			}
			
			if("S0".equals(reCode)){
				response.setIs_success(true);
			}else {
				response.setCode(reCode);
				response.setMsg(reMsg);
			}	
			logger.info("------为账户"+userId+"代收返回结束(账户提现金额增加   其它应收款减)------");
			return response;
		}
	}
	@Override
	public ErrorResponse withholdReturn(FinanaceEntry finanaceEntry,String merchantId, String userId, String productId) {
		// TODO 代付返回  账户可提现余额加   其它应付款减      账户可提现减   会唐备付金减
		logger.info("------代付返回开始(其它应付款减  备付金减)------");
		logger.info("参数信息：finanaceEntry.paymentamount="+finanaceEntry.getPaymentAmount()+",finanaceEntry.referId="+finanaceEntry.getReferId()
				+",userId="+userId+",merchantId="+merchantId+",productId="+productId);
		synchronized (lock) {
			ErrorResponse response=new ErrorResponse();
			String reCode = "S0";
			String reMsg = "成功";
			//获取每个账户记账流水
			List<FinanaceEntry> finanaceEntries=new ArrayList<FinanaceEntry>();
			//获取所有账户记账流水
			List<FinanaceEntry> finanaceEntrieAlls=new ArrayList<FinanaceEntry>();
			User user=new User();
			user.constId=merchantId;
			user.productId=productId;
			try {
				//获取套录号
				String entryId=redisIdGenerator.createRequestNo();
				logger.info("代付返回的套录号为："+entryId);
				String uId=null;
				for (int i = 0; i <= 1; i++) {
					boolean flag=false;//记账方向   true= C  false= D 
					if(0==i){
						uId=TransCodeConst.THIRDPARTYID_QTYFKZH;//其它应付款
					}else if(1==i){
						uId=TransCodeConst.THIRDPARTYID_FNZZH;//丰年备付金
					}
					user.userId=uId;
					//获取账户余额
					Balance balance=checkInfoService.getBalance(user,uId);
					if(null!=balance){
						balance.setPulseDegree(balance.getPulseDegree()+1);
						finanaceEntries=checkInfoService.getInternalFinanaceEntries(finanaceEntry, balance, flag, "withholdReturn", uId);
						if(finanaceEntries!=null){						
							if(null!=finanaceEntries&&finanaceEntries.size()>0){
								for(FinanaceEntry finanaceEntryInfo:finanaceEntries) {
									finanaceEntryInfo.setReferEntryId(entryId);
									finanaceEntryInfo.setAccrualType(BaseConstants.TYPE_BALANCE_SETTLE);									
									finanaceEntrieAlls.add(finanaceEntryInfo);
								}
						}else{
							logger.error("获取用户["+uId+"]账户流水信息失败");
							reCode = "S3";
							reMsg = "账户流水数据入库失败";
							break;
						}
						}else{
							logger.error("获取用户["+uId+"]账户流水信息失败");
							reCode = "S3";
							reMsg = "账户流水数据入库失败";
							break;
						}							
					}else{
						logger.error("获取用户["+uId+"]余额信息失败");
						reCode = "S4";
						reMsg = "用户余额信息查询失败";
					}
				}
				if("S0".equals(reCode)){
					//批量插入记录流水表
					if(!this.insertFinanaceEntry(finanaceEntrieAlls)){
						logger.error("分录流水入库失败");
						reCode = "S5";
						reMsg = "数据库操作异常";
					}
				}
				
			} catch (AccountException e) {
				logger.error(e.getMessage());
				reCode = "S5";
				reMsg = "数据库操作异常";
			}
			
			if("S0".equals(reCode)){
				response.setIs_success(true);
			}else {
				response.setCode(reCode);
				response.setMsg(reMsg);
			}		
			logger.info("------代付返回结束(账户可提现余额加   其它应付款减  备付金减)------");
			return response;
		}
	}
	
	@Override
	public ErrorResponse wipeAccount(TransOrderInfo transOrderInfo) {
		// TODO 抹帐
		logger.info("------抹帐开始------");
		logger.info("参数信息：新订单号orderNo="+transOrderInfo.getOrderNo()+",旧订单号orderPackageNo="+transOrderInfo.getOrderPackageNo()+",机构码merchantCode="+TransCodeConst.SETTLEMENT_RT);
		synchronized (lock) {
			ErrorResponse response=new ErrorResponse();
			String reCode = "S0";
			String reMsg = "成功";
			//分录流水信息
			List<FinanaceEntry> finanaceEntrys=new ArrayList<FinanaceEntry>();
			//根据订单号获取订单信息
			TransOrderInfoQuery query=new TransOrderInfoQuery();
			query.setOrderPackageNo(transOrderInfo.getOrderPackageNo());
			query.setFuncCode(TransCodeConst.SETTLEMENT_RT);
			query.setMerchantCode(transOrderInfo.getMerchantCode());
			List<TransOrderInfo> transOrderInfos=transOrderInfoManager.queryList(query);
			if(transOrderInfos==null||transOrderInfos.size()==0){
				query.setOrderNo(transOrderInfo.getOrderPackageNo());
				query.setOrderPackageNo(null);
				query.setFuncCode(null);
				transOrderInfos=transOrderInfoManager.queryList(query);
			}else{
				logger.error("该订单已经抹帐");
				response.setCode("S1");
				response.setMsg("该订单已经抹帐,不能重复抹帐");
				return response;
			}
			if(transOrderInfos==null||transOrderInfos.size()!=1){
				logger.error("获取订单流水信息为空");
				reCode = "S1";
				reMsg = "获取订单流水信息为空";
			}else{
				
				//获取冲正交易号
				String funcCode=TransCodeConst.SETTLEMENT_RT;//清结算---冲正类型
				String orderNo=transOrderInfo.getOrderNo();
				String oldOrderNo=transOrderInfo.getOrderPackageNo();
				transOrderInfo=transOrderInfos.get(0);
				transOrderInfo.setFuncCode(funcCode);
				transOrderInfo.setOrderNo(orderNo);
				transOrderInfo.setOrderPackageNo(oldOrderNo);
				transOrderInfo.setMerchantCode(transOrderInfo.getMerchantCode());
				//根据订单号获取记账流水信息
				List<FinanaceEntryHistory> finanaceEntrieList=checkInfoService.getFinanaceEntrieHistoryByOrderId(String.valueOf(transOrderInfo.getRequestId()));
				if(finanaceEntrieList==null||finanaceEntrieList.size()<=0){
					logger.error("获取流水信息为空");
					reCode = "S1";
					reMsg = "获取流水信息为空";
				}else{
					try {
						//获取套录号
						String newEntryId=redisIdGenerator.createBatchRequestNo();
						logger.info("抹帐的新套录号为："+newEntryId);
						//获取冲正账期
						String reverseTime=operationService.getAccountDate();
						//int resultNum=operationService.getAccountcountByEntryHistory(finanaceEntrieList);
						int resultNum=1;
						if(newEntryId != null && reverseTime!=null){
							String accountID = "";
							FinanaceEntry finanaceEntry = null;
							Balance balance=new Balance();
							User user=new User();
							
							/* 为所有的账目流水全部生成一笔反向交易 */
							for(FinanaceEntryHistory finanaceEn : finanaceEntrieList){								
								if(!accountID.equals(finanaceEn.getFinAccountId())){
									accountID=finanaceEn.getFinAccountId();
									balance=checkInfoService.getBalance(user, finanaceEn.getFinAccountId());
									balance.setPulseDegree(balance.getPulseDegree()+1);
								}else{
									balance.setPulseDegree(balance.getPulseDegree()+1);
								}
								/* 发生额类型:清算余额.贷借方向:C贷 */
								if(BaseConstants.TYPE_BALANCE_SETTLE==finanaceEn.getAccrualType()&&BaseConstants.CREDIT_TYPE==finanaceEn.getDirection()){
									balance.setBalanceUsable(balance.getBalanceUsable()-finanaceEn.getPaymentAmount());//账户可用余额
									balance.setBalanceSettle(balance.getBalanceSettle()-finanaceEn.getPaymentAmount());//账户清算余额
									if(resultNum==1){
										balance.setAmount(balance.getAmount()-finanaceEn.getPaymentAmount());//账户余额
									}
								}
								/* 发生额类型:清算余额.贷借方向:D借 */
								if(BaseConstants.TYPE_BALANCE_SETTLE==finanaceEn.getAccrualType()&&BaseConstants.DEBIT_TYPE==finanaceEn.getDirection()){
									balance.setBalanceUsable(balance.getBalanceUsable()+finanaceEn.getPaymentAmount());//账户可用余额
									balance.setBalanceSettle(balance.getBalanceSettle()+finanaceEn.getPaymentAmount());//账户清算余额
									if(resultNum==1){
										balance.setAmount(balance.getAmount()+finanaceEn.getPaymentAmount());//账户余额
									}
								}
								/* 发生额类型:冻结余额.贷借方向:C贷 */
								if(BaseConstants.TYPE_BALANCE_FROZON==finanaceEn.getAccrualType()&&BaseConstants.CREDIT_TYPE==finanaceEn.getDirection()){
									balance.setBalanceFrozon(balance.getBalanceFrozon()-finanaceEn.getPaymentAmount());//账户冻结余额
									if(resultNum==1){
										balance.setAmount(balance.getAmount()-finanaceEn.getPaymentAmount());//账户余额
									}
								}
								/* 发生额类型:冻结余额.贷借方向:D借 */
								if(BaseConstants.TYPE_BALANCE_FROZON==finanaceEn.getAccrualType()&&BaseConstants.DEBIT_TYPE==finanaceEn.getDirection()){
									balance.setBalanceFrozon(balance.getBalanceFrozon()+finanaceEn.getPaymentAmount());//账户冻结余额
									if(resultNum==1){
										balance.setAmount(balance.getAmount()+finanaceEn.getPaymentAmount());//账户余额
									}
								}
								/* 发生额类型:贷记余额.贷借方向:C贷 */
								if(BaseConstants.TYPE_BALANCE_CREDIT==finanaceEn.getAccrualType()&&BaseConstants.CREDIT_TYPE==finanaceEn.getDirection()){
									balance.setBalanceUsable(balance.getBalanceUsable()-finanaceEn.getPaymentAmount());//账户可用余额
									balance.setBalanceCredit(balance.getBalanceCredit()-finanaceEn.getPaymentAmount());//账户贷记余额
									if(resultNum==1){
										balance.setAmount(balance.getAmount()-finanaceEn.getPaymentAmount());//账户余额
									}
								}
								/* 发生额类型:贷记余额.贷借方向:D借 */
								if(BaseConstants.TYPE_BALANCE_CREDIT==finanaceEn.getAccrualType()&&BaseConstants.DEBIT_TYPE==finanaceEn.getDirection()){
									balance.setBalanceUsable(balance.getBalanceUsable()+finanaceEn.getPaymentAmount());//账户可用余额
									balance.setBalanceCredit(balance.getBalanceCredit()+finanaceEn.getPaymentAmount());//账户贷记余额
									if(resultNum==1){
										balance.setAmount(balance.getAmount()+finanaceEn.getPaymentAmount());//账户余额
									}
								}
								/* 发生交易流水 */
								finanaceEntry = operationService.addReverseTradeHistory(finanaceEn, balance, newEntryId, reverseTime);
								if(finanaceEntry == null){
									logger.error("生成对应交易流水的反流水失败");
									reCode = "S3";
									reMsg = "生成对应交易流水的反流水失败";
									break;
								}
								finanaceEntry.setReferId(String.valueOf(transOrderInfo.getRequestId()));
								if(null==finanaceEntry.getRemark()||"".equals(finanaceEntry.getRemark())){
									finanaceEntry.setRemark("冲正-抹帐");
								}
								finanaceEntrys.add(finanaceEntry);
							}
							//将订单流水Id制空
							transOrderInfo.setRequestId(null);
							//设置新订单创建时间和更新时间为null;
							transOrderInfo.setCreatedTime(null);
							transOrderInfo.setUpdatedTime(null);
							if(reCode.equals("S0")){
								//批量插入记录流水表   写入数据失败尝试三次
								try {
									insertFinanaceEntry(finanaceEntrys, transOrderInfo);
								} catch (AccountException e) {
									logger.error("******************账户"+transOrderInfo.getUserId()+"记账流水数据入库失败一次");
									try {
										insertFinanaceEntry(finanaceEntrys, transOrderInfo);
									} catch (AccountException e1) {
										logger.error("******************账户"+transOrderInfo.getUserId()+"记账流水数据入库失败两次");
										try {
											insertFinanaceEntry(finanaceEntrys, transOrderInfo);
										} catch (AccountException e2) {
											//发送邮件或短信通知管理员
											logger.error("******************账户"+transOrderInfo.getUserId()+"记账流水数据入库失败三次");
											//启用线程 发送邮件
											RkylinMailUtil.sendMailThread("账户记账流水失败通知","******************账户"+transOrderInfo.getUserId()+"做商户扣款冲正记账流水数据入库连续失败三次", TransCodeConst.FINANACE_ENTRY_ERROR_TOEMAIL);
											logger.error(e2.getMessage());
											reCode = "S5";
											reMsg = "数据库操作异常";
										}
									}
								}
							}
						}
					} catch (AccountException e) {
						logger.error(e.getMessage());
						reCode = "S4";
						reMsg = "数据库操作异常";
					}
				}
			}		
			if(reCode.equals("S0")){
				response.setIs_success(true);
			}else{
				response.setCode(reCode);
				response.setMsg(reMsg);
			}
			logger.info("------抹帐结束------");
			return response;
		}
	}
	
	@Override
	public Long getInternalBalance(String userId,String rootInstCd,String productId,String referUserId) {
		// TODO 获取账户余额
		logger.info("------------------内部账户余额查询开始，UserId="+userId+" rootInstCd="+rootInstCd+" productId="+productId+" referUserId="+referUserId);
		long amount=0;
		User user=new User();
		user.userId=userId;
		user.productId=productId;
		user.constId=rootInstCd;
		if(null!=referUserId && !"".equals(referUserId)){
			user.referUserId=referUserId;
		}
		Balance balance=checkInfoService.getBalance(user,"");
		if(null!=balance){
			if(Constants.FN_PRODUCT.equals(productId) || Constants.USER_SUB_ACCOUNT.equals(productId)){//提现余额  //信用余额
				amount=balance.getBalanceSettle();
			}
		}else {
			logger.error("内部账户余额查询失败，UserId="+userId);
			return null;
		}
		logger.info("------内部账户余额查询结束------");
		return amount;
	}
	
	@Override
	public Response doJob(Map<String, String[]> paramMap, String methodName) {
		// TODO Auto-generated method stub
		String reCode = "P0";
		String reMsg = "成功";
		ErrorResponse response=new ErrorResponse();
		if(!ValHasNoParam.hasParam(paramMap, "userid")){
			reCode="P1";
			reMsg="userid不能为空";
		}else if(!ValHasNoParam.hasParam(paramMap, "paymentamount")){
			reCode="P1";
			reMsg="paymentamount不能为空";
		}else if(!ValHasNoParam.hasParam(paramMap, "merchantcode")){
			reCode="P1";
			reMsg="merchantcode不能为空";
		}else if(!ValHasNoParam.hasParam(paramMap, "referid")){
			reCode="P1";
			reMsg="referid不能为空";
		}
		
		if(!reCode.equals("P0")){
			response.setCode(reCode);
			response.setMsg(reMsg);	
			return response;
		}		
			
		String userId=null;
		String merchantId=null;
		String productId=null;
		String referUserId=null;
		FinanaceEntry finanaceEntry=new FinanaceEntry();
		for(Object keyObj : paramMap.keySet().toArray()){
			System.out.println("key:" + keyObj);
			String[] strs = paramMap.get(keyObj);
			for(String value : strs){
				if(keyObj.equals("userid")){
					userId=value;
				}else if(keyObj.equals("paymentamount")){
					finanaceEntry.setPaymentAmount(Long.parseLong(value));
				}else if(keyObj.equals("yournotes")){
					finanaceEntry.setYourNotes(value);
				}else if(keyObj.equals("remark")){
					finanaceEntry.setRemark(value);
				}else if(keyObj.equals("recordmap")){
					finanaceEntry.setRecordMap(value);
				}else if(keyObj.equals("merchantcode")){
					merchantId=value;
				}else if(keyObj.equals("referid")){
					finanaceEntry.setReferId(value);
				}else if(keyObj.equals("merchantid")){
					merchantId=value;
				}else if(keyObj.equals("productid")){
					productId=value;
				}else if(keyObj.equals("referuserid")){
					referUserId=value;
				}
			}
		}
		if("ruixue.wheatfield.paymentinternal.credit".equals(methodName)){//内部授信接口
			response=this.credit(finanaceEntry, userId, merchantId, productId, referUserId);
		}else if("ruixue.wheatfield.paymentinternal.withdrawerturn".equals(methodName)){//提现返回
			response=this.withdrawReturn(finanaceEntry,merchantId);
		}else if("ruixue.wheatfield.paymentinternal.refundreturn".equals(methodName)){//还款返回
			response=this.refundReturn(finanaceEntry, userId,merchantId);
		}else if("ruixue.wheatfield.paymentinternal.rightsackagereturn".equals(methodName)){//债权包返回
			response=this.rightsPackageReturn(finanaceEntry, merchantId);
		}else if("ruixue.wheatfield.paymentinternal.fenrun".equals(methodName)){//分润
			response=this.fenrun(finanaceEntry, userId, merchantId, productId, referUserId);
		}else if("ruixue.wheatfield.paymentinternal.withholdreturn".equals(methodName)){//代付返回
			response=this.withholdReturn(finanaceEntry, merchantId, referUserId, productId);
		}
		return response;
	}
	
	@Transactional(rollbackFor = AccountException.class, propagation = Propagation.NESTED)
	public boolean insertFinanaceEntry(List<FinanaceEntry> finanaceEntries){
		boolean result=false;
		if(null!=finanaceEntries&&0<finanaceEntries.size()){
			List<FinanaceEntry> finaceEntryList=new ArrayList<FinanaceEntry>();
			for (FinanaceEntry finanaceEntry : finanaceEntries) {
				finaceEntryList.add(finanaceEntry);
			}
			finanaceEntryManager.saveFinanaceEntryList(finaceEntryList);
			result=true;
		}
		return result;
	}
	@Transactional(rollbackFor = AccountException.class, propagation = Propagation.NESTED)
	public void insertFinanaceEntry(List<FinanaceEntry> finanaceEntries,TransOrderInfo transOrderInfo){
		try {
			//String[] productIdStrings={""};
			if(null!=finanaceEntries&&0<finanaceEntries.size()){
				transOrderInfo.setAccountDate(finanaceEntries.get(0).getAccountDate());
				transOrderInfoManager.saveTransOrderInfo(transOrderInfo);
				if(TransCodeConst.MERCHANT_RT.equals(transOrderInfo.getFuncCode())||TransCodeConst.SETTLEMENT_RT.equals(transOrderInfo.getFuncCode())||TransCodeConst.MANUAL_RT.equals(transOrderInfo.getFuncCode())||TransCodeConst.CONSUMPTION_BEFER_REFUND.equals(transOrderInfo.getFuncCode())){
					//如果是冲正交易，需要将原有交易订单状态置为冲正
					TransOrderInfoQuery query=new TransOrderInfoQuery();
					query.setOrderNo(transOrderInfo.getOrderPackageNo());
					query.setMerchantCode(transOrderInfo.getMerchantCode());
					List<TransOrderInfo> transOrderInfos=transOrderInfoManager.queryList(query);
					for (TransOrderInfo orderInfo : transOrderInfos) {
						orderInfo.setStatus(TransCodeConst.TRANS_STATUS_REVERSAL);
						transOrderInfoManager.saveTransOrderInfo(orderInfo);
						//transSettlementService.transToSettlement(orderInfo,productIdStrings);
					}
				}
				List<FinanaceEntry> finaceEntryList=new ArrayList<FinanaceEntry>();
				for (FinanaceEntry finanaceEntry : finanaceEntries) {
					finanaceEntry.setReferId(String.valueOf(transOrderInfo.getRequestId()));
					finaceEntryList.add(finanaceEntry);
				}
				finanaceEntryManager.saveFinanaceEntryList(finaceEntryList);
			}
		} catch (AccountException e) {
			throw new AccountException("ERROR");
		}
	}

	/**
	 * 抹账（运营平台）
	 * @param orderNo  订单号
	 * @param newOrderNo 新订单号
	 * @param merchantCode  机构号
	 * @return
	 */
	@Transactional(rollbackFor = AccountException.class, propagation = Propagation.NESTED)
	public WipeAccountResponse wipeAccountPlat(String newOrderNo,String orderNo, String merchantCode) {
		//查出原订单号
		logger.info("抹账（运营平台）  传入参数orderNo="+orderNo+",newOrderNo="+newOrderNo+",merchantCode="+merchantCode);
		WipeAccountResponse res = new WipeAccountResponse();
		if (orderNo==null||"".equals(orderNo.trim())||newOrderNo==null||"".equals(newOrderNo.trim())||merchantCode==null||"".equals(merchantCode.trim())) {
			res.setCode(CodeEnum.ERR_PARAM_NULL.getCode());
			res.setMsg(CodeEnum.ERR_PARAM_NULL.getMessage());
			return res;
		}
		TransOrderInfoQuery query=new TransOrderInfoQuery();
		query.setOrderNo(orderNo);
		query.setMerchantCode(merchantCode);
		List<TransOrderInfo> transOrderInfos=transOrderInfoManager.queryList(query);
		logger.info("抹账（运营平台）  查出的订单个数="+transOrderInfos.size());
		if (transOrderInfos.size()==0) {
			res.setCode(CodeEnum.ERR_DATA_NO_RESULT.getCode());
			res.setMsg(CodeEnum.ERR_DATA_NO_RESULT.getMessage());
			return res;
		}
		TransOrderInfo transOrderInfo = transOrderInfos.get(0);
		Integer requestId = transOrderInfo.getRequestId();	
		List<FinanaceEntryHistory> finanaceEntrieList=checkInfoService.getFinanaceEntrieHistoryByOrderId(String.valueOf(requestId));
		logger.info("抹账（运营平台）  查出的流水个数="+finanaceEntrieList.size());
		if (finanaceEntrieList.size()==0) {
			res.setCode(CodeEnum.FAILURE.getCode());
			res.setMsg("没有查出流水信息");
			return res;
		}
		String[] finAccIdArray = new String[finanaceEntrieList.size()];
		for (int i = 0; i < finanaceEntrieList.size(); i++) {
			finAccIdArray[i]=finanaceEntrieList.get(i).getFinAccountId();
		}
		List<FinanaceAccount> finAccList = finanaceAccountDao.selectByFinAccountId(finAccIdArray);
		logger.info("抹账（运营平台） 抹账前查出的资金账户信息个数="+finAccList.size());
		if (finAccList.size()==0) {
			res.setCode(CodeEnum.FAILURE.getCode());
			res.setMsg("抹账前没有查出资金账户信息");
			return res;
		}
		List<Balance> balanceBeforeList = new ArrayList<Balance>();
		for (int i = 0; i < finAccList.size(); i++) {
			FinanaceAccount finAccount = finAccList.get(i);
			User user = new User();
			user.userId=finAccount.getAccountRelateId();
			user.constId=merchantCode;
			user.statusID=BaseConstants.ACCOUNT_STATUS_OK;
			Balance balance = checkInfoService.getBalance(user, finAccount.getFinAccountId());
			if (balance==null) {
				res.setCode(CodeEnum.FAILURE.getCode());
				res.setMsg("抹账前无法获取用户账户信息");
				return res;
			}
			balance.setUserId(finAccount.getAccountRelateId());
			balanceBeforeList.add(balance);
		}
		res.setBalanceBeforeList(balanceBeforeList);
		ErrorResponse errorResponse = wipeAccountForDubbo(newOrderNo,orderNo,merchantCode);
		if (!errorResponse.isIs_success()) {
			res.setCode(CodeEnum.FAILURE.getCode());
			res.setMsg(errorResponse.getMsg());
			return res;
		}
		List<Balance> balanceAfterList = new ArrayList<Balance>();
		for (int i = 0; i < finAccList.size(); i++) {
			FinanaceAccount finAccount = finAccList.get(i);
			User user = new User();
			user.userId=finAccount.getAccountRelateId();
			user.constId=merchantCode;
			user.statusID=BaseConstants.ACCOUNT_STATUS_OK;
			Balance balance = (Balance) checkInfoService.getBalance(user, finAccount.getFinAccountId());
			if (balance==null) {
				res.setCode(CodeEnum.FAILURE.getCode());
				res.setMsg("抹账后无法获取用户账户信息");
				return res;
			}
			balance.setUserId(finAccount.getAccountRelateId());
			balanceAfterList.add(balance);
		}
		res.setBalanceAfterList(balanceAfterList);;
		return res;
	}
	
	@Override
	public ErrorResponse wipeAccountForDubbo(String orderNo,String orderPackageNo, String merchantCode) {
		logger.info("-------------通过dubbo调用抹账接口start------------------");
		
		logger.info("----------入参：orderNo : " + orderNo + ";orderPackageNo : " + orderPackageNo + ";merchantCode : " + merchantCode + "--------");
		
		ErrorResponse response=new ErrorResponse();
		if(orderNo == null || "".equals(orderNo)){
			response.setCode("P1");
			response.setMsg("orderNo不能为空");
			logger.info("orderNo不能为空");
			return response;
		}
		if(orderPackageNo == null || "".equals(orderPackageNo)){
			response.setCode("P1");
			response.setMsg("orderPackageNo不能为空");
			logger.info("orderPackageNo不能为空");
			return response;
		}
		if(merchantCode == null || "".equals(merchantCode)){
			response.setCode("P1");
			response.setMsg("merchantCode不能为空");
			logger.info("merchantCode不能为空");
			return response;
		}
		TransOrderInfo transOrderInfo = new TransOrderInfo();
		transOrderInfo.setOrderNo(orderNo);
		transOrderInfo.setOrderPackageNo(orderPackageNo);
		transOrderInfo.setMerchantCode(merchantCode);
		
		response = wipeAccount(transOrderInfo);

		logger.info("------------通过dubbo调用抹账接口end---------------");
		return response;
	}
}
