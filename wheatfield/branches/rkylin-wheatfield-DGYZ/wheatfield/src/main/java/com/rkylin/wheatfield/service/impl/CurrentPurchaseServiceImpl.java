package com.rkylin.wheatfield.service.impl;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.rkylin.common.RedisIdGenerator;
import com.rkylin.wheatfield.common.AccountUtils;
import com.rkylin.wheatfield.common.DateUtils;
import com.rkylin.wheatfield.common.ValHasNoParam;
import com.rkylin.wheatfield.constant.AccountConstants;
import com.rkylin.wheatfield.constant.BaseConstants;
import com.rkylin.wheatfield.constant.Constants;
import com.rkylin.wheatfield.constant.TransCodeConst;
import com.rkylin.wheatfield.exception.AccountException;
import com.rkylin.wheatfield.manager.FinanaceAccountManager;
import com.rkylin.wheatfield.manager.FinanaceEntryManager;
import com.rkylin.wheatfield.manager.ParameterInfoManager;
import com.rkylin.wheatfield.manager.TransOrderInfoManager;
import com.rkylin.wheatfield.pojo.Balance;
import com.rkylin.wheatfield.pojo.FinanaceAccount;
import com.rkylin.wheatfield.pojo.FinanaceAccountQuery;
import com.rkylin.wheatfield.pojo.FinanaceEntry;
import com.rkylin.wheatfield.pojo.OrderAuxiliary;
import com.rkylin.wheatfield.pojo.TransOrderInfo;
import com.rkylin.wheatfield.pojo.TransOrderInfoQuery;
import com.rkylin.wheatfield.pojo.User;
import com.rkylin.wheatfield.response.ErrorResponse;
import com.rkylin.wheatfield.response.Response;
import com.rkylin.wheatfield.response.CurrentPurchaseResponse;
import com.rkylin.wheatfield.service.IAPIService;
import com.rkylin.wheatfield.service.OperationServive;
import com.rkylin.wheatfield.utils.DateUtil;
import com.rkylin.wheatfield.utils.StringUtils;
import com.rkylin.wheatfield.service.CheckInfoService;
import com.rkylin.wheatfield.service.CurrentPurchaseServie;
import com.rkylin.wheatfield.service.FinanaceEntryService;

/**
 * 活期申购 君融贷 创建子账户（若无）
 *              主账户（投资子账户） 转入活期本金子账户
 * 
 * @author mawanxia
 *
 */
@Transactional
@Service("currentPurchaseService")
public class CurrentPurchaseServiceImpl implements IAPIService, CurrentPurchaseServie {
	private static Object lock=new Object();
	private static Logger logger = LoggerFactory.getLogger(CurrentPurchaseServiceImpl.class);
	@Autowired
	FinanaceEntryManager finanaceEntryManager;
	@Autowired
	FinanaceAccountManager finanaceAccountManager;
	@Autowired
	RedisIdGenerator redisIdGenerator;
	@Autowired
	TransOrderInfoManager transOrderInfoManager;
	@Autowired
	OperationServive operationService;
	@Autowired
	CheckInfoService checkInfoService;
	@Autowired
	ParameterInfoManager parameterInfoManager;
	@Autowired
	FinanaceEntryService finanaceEntryServie;

	@Override
	public Response doJob(Map<String, String[]> paramMap, String methodName) {
		synchronized (lock) {
		String reCode = "P0";
		String reMsg = "成功";
		ErrorResponse response = new ErrorResponse();

		CurrentPurchaseResponse successResponse = new CurrentPurchaseResponse();
		if ("ruixue.wheatfield.demand.purchase".equals(methodName)) {
			Response checkResult = checkIsNUllValid(paramMap); //校验数据是否非空
			if (checkResult instanceof ErrorResponse) {
				return checkResult;
			}
			if (checkResult instanceof CurrentPurchaseResponse) {
				try {
					Map<String, Object> map = assembleData(paramMap); //组装数据
					User user = (User) map.get("user");
					TransOrderInfo transOrderInfo = (TransOrderInfo) map.get("transOrderInfo");
					OrderAuxiliary orderAuxiliary = (OrderAuxiliary) map.get("orderAuxiliary");
					checkTransferBaseData(transOrderInfo, orderAuxiliary);// 基础数据校验
					createSubAccount(user); //创建账户
					transfer(user, transOrderInfo, orderAuxiliary); // 申购交易
				} catch (AccountException e) {
					logger.error(e.getDefineMsg());
					response.setCode(e.getDefineCode());
					response.setMsg(e.getDefineMsg());
					return response;
				} catch (Exception e) {
					logger.error("系统错误");
					logger.error(e.getStackTrace().toString(),e);
					response.setCode("P");
					response.setMsg("系统错误");
					return response;
				}

			}
		
			successResponse.setIs_success(true);
	     }
		return successResponse;
		}
	}

	/**
	 * 君融贷做活期申购业务，若没子账户则创建子账户（1：活期本金子账户 2：活期利息子账户 3：昨日活期收益子账户）
	 * 
	 * @param user 用户对象
	 * @return 状态
	 * @throws AppException
	 */
	@Override
	public void createSubAccount(User user) throws AccountException {
		logger.info("------开通账户 createSubAccount start---------");
		logger.info("--用户ID--:" + user.userId + "--机构号--：" + user.constId + "--产品号--：" + user.productId + "--用户名--"
				+ user.userName + "第三方用户ID" + user.referUserId);
	
			List<FinanaceAccount> list = qryFinanaceAccountList(user, "1");// 查询用户主账户
			if (checkFinanaceAccountList(list, "1")) {
				List<FinanaceAccount> sublist = qryFinanaceAccountList(user, "2");// 查询用户主账户
				if (checkFinanaceAccountList(sublist, "2")) { // 判断是否需要并可以创建活期子账户
					String accountCode = list.get(0).getAccountCode();// 取主账户的户口编码
					saveSubAccount(user, accountCode); // 创建活期子账户
				}
			}

	

		logger.info("------开通账户 createSubAccount  end---------");

	}

	/**
	 * 申购 转入君融贷用户活期本金子账户
	 * 
	 * @param user
	 * @param transOrderInfo
	 * @return
	 */
	@Override
	@Transactional(rollbackFor = Exception.class, propagation = Propagation.NESTED)
	public void transfer(User user, TransOrderInfo transOrderInfo, OrderAuxiliary orderAuxiliary)
			throws AccountException {
		logger.info("-----------------------申购交易--账户UserId：" + transOrderInfo.getUserId() + "向账户"
				+ transOrderInfo.getInterMerchantCode() + "转入" + transOrderInfo.getAmount()
				+ "分，转账操作开始------------------------");
		logger.info("转账操作参数信息：amount=" + transOrderInfo.getAmount() + ",UserId=" + transOrderInfo.getUserId()
				+ ",funccode=" + transOrderInfo.getFuncCode() + ",intermerchantcode="
				+ transOrderInfo.getInterMerchantCode() + ",merchantcode=" + transOrderInfo.getMerchantCode()
				+ ",orderamount=" + transOrderInfo.getOrderAmount() + ",ordercount=" + transOrderInfo.getOrderCount()
				+ ",orderdate=" + transOrderInfo.getOrderDate() + ",orderno=" + transOrderInfo.getOrderNo()
				+ ",orderpackageno=" + transOrderInfo.getOrderPackageNo() + ",paychannelid="
				+ transOrderInfo.getPayChannelId() + ",remark=" + transOrderInfo.getRemark() + ",productid="
				+ orderAuxiliary.getProductQAA() + ",requestno=" + transOrderInfo.getRequestNo() + ",requesttime="
				+ transOrderInfo.getRequestTime() + ",status=" + transOrderInfo.getStatus() + ",tradeflowno="
				+ transOrderInfo.getTradeFlowNo() + ",userfee=" + transOrderInfo.getUserFee() + ",feeamount="
				+ transOrderInfo.getFeeAmount() + ",profit=" + transOrderInfo.getProfit() + ",busitypeid="
				+ transOrderInfo.getBusiTypeId() + ",bankcode=" + transOrderInfo.getBankCode() + ",errorcode="
				+ transOrderInfo.getErrorCode() + ",errormsg=" + transOrderInfo.getErrorMsg() + ",useripaddress="
				+ transOrderInfo.getUserIpAddress() + ",intoproductid=" + orderAuxiliary.getProductQAB());

		Long amount = transOrderInfo.getAmount();// 交易金额
		orderNoChk(transOrderInfo); // 校验订单号是否存在
		checkAccountIsOk(user, transOrderInfo);// 校验账户状态是否正常
		Balance intoBalance = checkIntoTran(transOrderInfo, orderAuxiliary);// 校验转入方余额信息是否有效
		List<FinanaceEntry> finanaceEntrieAlls = getFinanaceEntriesAndCheckBalance(user, transOrderInfo, intoBalance); // 获取交易集合并校验余额
		transOrderInfo = saveTranOrderInfo(transOrderInfo, finanaceEntrieAlls, orderAuxiliary,amount); // 保存订单信息表
		saveFinanaceEntry(finanaceEntrieAlls , transOrderInfo);// 保存用户记账流水表

		List<FinanaceEntry> jrdOrgFinanaceEntrieList = getJdrOrgFinanaceEntries(transOrderInfo , amount);// 获取君融贷机构交易流水
		saveFinanaceEntry(jrdOrgFinanaceEntrieList ,transOrderInfo);// 保存君融贷机构

	}

	/**
	 * 校验有效性
	 * 
	 * @param paramMap  参数map
	 */
	private Response checkIsNUllValid(Map<String, String[]> paramMap) {
		String reCode = "P0";
		String reMsg = "成功";
		ErrorResponse response = new ErrorResponse();
		CurrentPurchaseResponse currentPurchaseResponse = new CurrentPurchaseResponse();
		if (!ValHasNoParam.hasParam(paramMap, "funccode")) {// 功能编码
			reCode = "P1";
			reMsg = "funccode不能为空";
		}
		if (!ValHasNoParam.hasParam(paramMap, "merchantcode")) {// 商户编码/机构号
			reCode = "P1";
			reMsg = "merchantcode不能为空";
		}
		if (!ValHasNoParam.hasParam(paramMap, "userid")) {// 用户号（转出A用户ID）
			reCode = "P1";
			reMsg = "userid不能为空";
		}
		if (!ValHasNoParam.hasParam(paramMap, "productid")) {// 产品号（转出A产品号）
			reCode = "P1";
			reMsg = "productid不能为空";
		}
		if (!ValHasNoParam.hasParam(paramMap, "intermerchantcode")) {// 中间商户编码(转入B用户ID)
			reCode = "P1";
			reMsg = "intermerchantcode不能为空";
		}
		if (!ValHasNoParam.hasParam(paramMap, "intoproductid")) {// 产品号（转入B产品号）
			reCode = "P1";
			reMsg = "intoproductid不能为空";
		}
		if (!ValHasNoParam.hasParam(paramMap, "amount")) {// 交易金额
			reCode = "P1";
			reMsg = "amount不能为空";
		}
		if (!ValHasNoParam.hasParam(paramMap, "orderno")) {// 订单号
			reCode = "P1";
			reMsg = "orderno不能为空";
		}
		if (!ValHasNoParam.hasParam(paramMap, "busitypeid")) {// 业务类型Id
			reCode = "P1";
			reMsg = "busitypeid不能为空";
		}
		if (!ValHasNoParam.hasParam(paramMap, "orderdate")) {// 订单日期 YYYY-MM-DD HH:MM:SS
			reCode = "P1";
			reMsg = "orderdate不能为空";
		}
		if (!ValHasNoParam.hasParam(paramMap, "requesttime")) {// 交易请求时间 YYYY-MM-DD HH:MM:SS
			reCode = "P1";
			reMsg = "requesttime不能为空";
		}
		if (!ValHasNoParam.hasParam(paramMap, "status")) {// 状态（ 1：正常）
			reCode = "P1";
			reMsg = "status不能为空";
		}
		if (!ValHasNoParam.hasParam(paramMap, "useripaddress")) {// 地址
			reCode = "P1";
			reMsg = "useripaddress不能为空";
		}

		if (reCode.equals("P0")) {
			currentPurchaseResponse.setIs_success(true);
			currentPurchaseResponse.setCallResult(true);
			return currentPurchaseResponse;

		} else {
			response.setCode(reCode);
			response.setMsg(reMsg);
			response.setIs_success(false);
			response.setCallResult(false);
			return response;
		}
	}

	/**
	 * 组装数据
	 * 
	 * @param paramMap 参数
	 * @return map
	 */
	private Map<String, Object> assembleData(Map<String, String[]> paramMap) throws AccountException {
		Map<String, Object> map = new HashMap<String, Object>();
		DateUtil dateUtil = new DateUtil();
		TransOrderInfo transOrderInfo = new TransOrderInfo();// 实例化交易订单信息对象
		OrderAuxiliary orderAuxiliary = new OrderAuxiliary();
		User user = new User();// 实例化用户对象

		for (Object keyObj : paramMap.keySet().toArray()) {
			System.out.println("key:" + keyObj);
			String[] strs = paramMap.get(keyObj);
			for (String value : strs) {
				logger.info("参数KEY=" + keyObj + " Value=" + value);
				// *****User Start*******
				if (keyObj.equals("userid")) {
					user.userId = value;
				}
				if (keyObj.equals("merchantcode")) {
					user.constId = value;
				}
				if (keyObj.equals("productid")) {
					user.productId = value;
				}
				// *******User end***********
				// *******TransOrderInfo Start********
				if (keyObj.equals("funccode")) {
					transOrderInfo.setFuncCode(value);
				}
				if (keyObj.equals("merchantcode")) {
					transOrderInfo.setMerchantCode(value);
				}
				if (keyObj.equals("userid")) {
					transOrderInfo.setUserId(value);
				}
				if (keyObj.equals("productid")) {
					transOrderInfo.setProductIdd(value);
					orderAuxiliary.setProductQAA(value);
				}
				if (keyObj.equals("intermerchantcode")) {
					transOrderInfo.setInterMerchantCode(value);
				}
				if (keyObj.equals("intoproductid")) {
					orderAuxiliary.setProductQAB(value);
				}
				if (keyObj.equals("amount")) {
					if (AccountUtils.isAmount(value)) {
						transOrderInfo.setAmount(Long.parseLong(value));
					} else {
						throw new AccountException("P2", "金额格式错误");
					}
				}
				if (keyObj.equals("orderno")) {
					transOrderInfo.setOrderNo(value);
				}
				if (keyObj.equals("orderdate")) {
					try {
						transOrderInfo.setOrderDate(dateUtil.parse(value, Constants.DATE_FORMAT_YYYYMMDDHHMMSS));
					} catch (ParseException e) {
						logger.error(e.getStackTrace().toString());
						throw new AccountException("P2", "orderdate日期格式错误");
					}
				}
				if (keyObj.equals("requesttime")) {
					try {
						transOrderInfo.setRequestTime(dateUtil.parse(value, Constants.DATE_FORMAT_YYYYMMDDHHMMSS));
					} catch (ParseException e) {
						logger.error(e.getStackTrace().toString());
						throw new AccountException("P2", "orderdate日期格式错误");
					}
				}
				if (keyObj.equals("userfee")) {
					transOrderInfo.setUserFee(Long.parseLong(value));
				}
				if (keyObj.equals("remark")) {
					transOrderInfo.setRemark(StringUtils.subStr(value, 120));
				}
				if (keyObj.equals("useripaddress")) {
					transOrderInfo.setUserIpAddress(value);
				}
				if (keyObj.equals("ordercount")) {
					transOrderInfo.setOrderCount(Integer.parseInt(value));
				}
				if (keyObj.equals("orderpackageno")) {
					transOrderInfo.setOrderPackageNo(value);
				}
				if (keyObj.equals("paychannelid")) {
					transOrderInfo.setPayChannelId(value);
				}
				if (keyObj.equals("requestno")) {
					transOrderInfo.setRequestNo(value);
				}
				if (keyObj.equals("tradeflowno")) {
					transOrderInfo.setTradeFlowNo(value);
				}
				if (keyObj.equals("status")) {
					if (AccountUtils.isNumeric(value)) {
					    transOrderInfo.setStatus(Integer.parseInt(value));
					} else {
						throw new AccountException("P2", "状态值无效");
					}  
				}
				if (keyObj.equals("bankcode")) {
					transOrderInfo.setBankCode(value);
				}
				if (keyObj.equals("busitypeid")) {
					transOrderInfo.setBusiTypeId(value);
				}
				if (keyObj.equals("errorcode")) {
					transOrderInfo.setErrorCode(value);
				}
				if (keyObj.equals("errormsg")) {
					transOrderInfo.setErrorMsg(value);
				}
				if (keyObj.equals("profit")) {
					transOrderInfo.setProfit(Long.parseLong(value));
				}

				// *******TransOrderInfo end********

			}
		}
		map.put("user", user);
		map.put("transOrderInfo", transOrderInfo);
		map.put("orderAuxiliary", orderAuxiliary);
		return map;
	}

	/**
	 * 查询资金账户list
	 * 
	 * @param user 用户 ， type 类型 ：1 基本账户（主账户） ； 2子账户
	 * @return 资金账户list
	 * @throws AppException
	 */
	private List<FinanaceAccount> qryFinanaceAccountList(User user, String type) throws AccountException {
		logger.info("------qryFinanaceAccountList start---------");
		List<FinanaceAccount> list = new ArrayList<FinanaceAccount>();
		FinanaceAccountQuery query = new FinanaceAccountQuery();
		if (!AccountUtils.isEmpty(user.constId)) {
			query.setRootInstCd(user.constId); // 设置机构号
		}
		if (!AccountUtils.isEmpty(user.userId)) {
			query.setAccountRelateId(user.userId); // 设置账户关联ID
		}
		if ("1".equals(type)) {
			query.setFinAccountTypeId(AccountConstants.ACCOUNT_TYPE_BASE);// 设置"10001"基本账户类型
			list = finanaceAccountManager.queryList(query);// 根据条件查询资金账户表

		}
		if ("2".equals(type)) {
			query.setFinAccountTypeId(AccountConstants.ACCOUNT_TYPE_CHILD);// 设置"10002"子账户类型
			list = finanaceAccountManager.queryList(query);// 根据条件查询资金账户表
		}
		logger.info("------qryFinanaceAccountList end---------");
		return list;
	}

	/**
	 * 校验用户账户（主或子）
	 * 
	 * @param list 资金账户集合
	 * @param type 1:主账户 2子账户
	 * @return true：需要并可以创建活期子账户 false 不需要
	 * @throws AppException
	 */
	private boolean checkFinanaceAccountList(List<FinanaceAccount> list, String type) throws AccountException {
		logger.info("------checkFinanaceAccountList start---------");
		boolean b = false;
		if ("1".equals(type)) { // 主账户
			if (list == null || list.size() <= 0) {
				logger.info("主账户暂不存在，请先创建主账户！");
				throw new AccountException("P2", "主账户暂不存在，请先创建主账户！");
			} else if (!BaseConstants.ACCOUNT_STATUS_OK.equals(list.get(0).getStatusId())) {
				logger.info("主账户状态非正常，请先将主账户置为有效状态！");
				throw new AccountException("P2", "主账户状态非正常，请先将主账户置为有效状态！");
			} else {
				b = true;
			}
		}
		if ("2".equals(type)) { // 子账户
			List<String> subJrdAccountList =  Arrays.asList(Constants.subJrdAccount);
			int flag1 = 0; // 子账户的产品号是在活期账户的产品集合里标志位
			int flag2 = 0;// 子账户的产品号是在活期账户的产品集合里且状态正常标志位
			for (int i = 0; i < list.size(); i++) { // 遍历君融贷某用户下子账户集合
				FinanaceAccount finanaceAccount = list.get(i);
				if (subJrdAccountList.contains(finanaceAccount.getGroupManage())) {// 判断查询出子账户的产品号是否在活期账户的产品集合里
					flag1++;
					if (BaseConstants.ACCOUNT_STATUS_OK.equals(finanaceAccount.getStatusId())) {// 判断子账户状态正常
						flag2++;
					}

				}
			}
			if (flag1 != flag2) {// 判断子账户的产品号是在活期账户的产品集合里标志位 和
									// 子账户的产品号是在活期账户的产品集合里且状态正常标志位 是否不一致
				logger.info("子账户状态异常");
				throw new AccountException("P2", "子账户状态异常");
			} else if (flag1 == 0) { // 若flag为0 即 君融贷某用户下子账户没有活期产品的子账户
				b = true; // 等于0则需要创建活期子账户
			} else if (subJrdAccountList.size() == flag1) { // 判断子账户的产品号是在活期账户的产品集合里标志位与活期账户的产品集合大小是否相等
				b = false;// 相等则无需创建活期子账户
			} else {
				logger.info("子账户个数异常");
				throw new AccountException("P2", "子账户个数异常");
			}

		}
		logger.info("------checkFinanaceAccountList end---------");
		return b;
	}

	/**
	 * 保存子账户
	 * 
	 * @param user
	 * @param accountCode
	 */
	@Transactional(rollbackFor = AccountException.class, propagation = Propagation.NESTED)
	private void saveSubAccount(User user, String accountCode) {
		FinanaceAccount finanaceAccount = null;
		List<String> subAccountList = Arrays.asList(Constants.subJrdAccount);
		for (int i = 0; i < subAccountList.size(); i++) {
			finanaceAccount = new FinanaceAccount();
			finanaceAccount.setFinAccountId(redisIdGenerator.accountEntryNo());// 自动生成id
			finanaceAccount.setRootInstCd(user.constId);// 设置机构码
			finanaceAccount.setGroupManage(subAccountList.get(i));// 设置产品号
			finanaceAccount.setFinAccountName(user.userId); // 设置资金账户名称
			finanaceAccount.setAccountRelateId(user.userId);// 设置 账户关联ID--userid
			finanaceAccount.setStatusId(BaseConstants.ACCOUNT_STATUS_OK);// 设置账户状态
			finanaceAccount.setAmount(Long.parseLong(BaseConstants.INITIAL_AMOUNT));// 设置账户余额
			finanaceAccount.setBalanceSettle(Long.parseLong(BaseConstants.INITIAL_AMOUNT));// 设置清算余额/可提现余额
			finanaceAccount.setBalanceFrozon(Long.parseLong(BaseConstants.INITIAL_AMOUNT));// 设置冻结余额
			finanaceAccount.setBalanceOverLimit(Long.parseLong(BaseConstants.INITIAL_AMOUNT));// 设置透支额度
			
			finanaceAccount.setFinAccountTypeId(AccountConstants.ACCOUNT_TYPE_CHILD);// 设置"10002"子账户类型
			finanaceAccount.setAccountCode(accountCode);//
			
			finanaceAccount.setGroupSettle(AccountConstants.GROUP_SETTLE_BASE);// 设置"30001"核算分组公共编号
			finanaceAccount.setCurrency(BaseConstants.CURRENCY_CNY);// 设置 币种 人民币
			finanaceAccount.setStartTime(DateUtils.getSysDate(Constants.DATE_FORMAT_YYYYMMDD));// 设置生效时间
			finanaceAccount.setBussControl(BaseConstants.BUSS_CONTRAL_TOTAL);// 设置账户业务控制
			finanaceAccount.setRemark("");// 设置 备注
			finanaceAccount.setRecordMap("");// 设置 记录安全码
			finanaceAccountManager.saveFinanaceAccount(finanaceAccount); // 保存
		}

	}

	/**
	 * 校验交易基础数据
	 * 
	 * @param transOrderInfo
	 * @param orderAuxiliary
	 * @return true ：成功 false ：失败
	 * @throws AccountException
	 */
	private boolean checkTransferBaseData(TransOrderInfo transOrderInfo, OrderAuxiliary orderAuxiliary) {
		boolean b = false;
		if (!Constants.JRD_BUSITYPE_PURCHASE.equals(transOrderInfo.getBusiTypeId())) {
			throw new AccountException("P2", "业务类型ID错误，只能为6[申购活期产品]");
		} else if (!Constants.JRD_PURCHASE_INTOPRODUCTID.equals(orderAuxiliary.getProductQAB())) {
			throw new AccountException("P2", "申购活期产品的转入子账户只能是P000024[活期本金]");
		} else if (!TransCodeConst.ADJUST_ACCOUNT_AMOUNT.equals(transOrderInfo.getFuncCode())) {
			throw new AccountException("P2", "功能编码必须是3001[内部转账交易]");
		} else if (!Constants.JRD_ID.equals(transOrderInfo.getMerchantCode())) {
			throw new AccountException("P2", "申购活期产品功能只能是君融贷机构申购");
		} else if (!Constants.JRD_PRODUCT.equals(transOrderInfo.getProductIdd())) {
			throw new AccountException("P2", "转出方产品号只能是主账户产品号");
		} else if(!transOrderInfo.getUserId().equals(transOrderInfo.getInterMerchantCode())){
			throw new AccountException("P2", "转出方产品用户和转入方的用户必须是同一个");
		}
		return b;

	}

	/**
	 * 校验订单号是否存在
	 * 
	 * @param transOrderInfo  交易订单信息
	 * @return true ： 存在 false :不存在
	 */
	private boolean orderNoChk(TransOrderInfo transOrderInfo) {
		TransOrderInfoQuery tquery = new TransOrderInfoQuery();
		String orderNo = transOrderInfo.getOrderNo(); // 订单号
		String merchantCode = transOrderInfo.getMerchantCode(); // 机构号
		tquery.setOrderNo(orderNo);
		tquery.setMerchantCode(merchantCode);
		List<TransOrderInfo> orderList = transOrderInfoManager.queryList(tquery); // 根据订单号，机构号查询交易订单信息
		if (orderList != null && orderList.size() > 0) {
			logger.info("订单号：" + orderNo + "在机构号：" + merchantCode + "中已存在");
			throw new AccountException("P2", "该交易订单号已存在，请确认！");

		} else {
			return true;
		}

	}

	/**
	 * 校验用户状态是否正常
	 * 
	 * @param user  用户
	 * @param transOrderInfo 交易订单信息
	 * @return true 状态正常
	 * @throws AccountException
	 */
	private boolean checkAccountIsOk(User user, TransOrderInfo transOrderInfo) {
		// 判断账户状态是否正常
		boolean accountIsOK = operationService.checkAccount(user);
		if (!accountIsOK) {
			logger.error("用户" + transOrderInfo.getUserId() + "状态为非正常状态");
			throw new AccountException("P2", "用户状态为非正常状态");

		} else {
			return true;
		}

	}

	/**
	 * 校验订单信息有效性
	 * 
	 * @param transOrderInfo
	 * @param orderAuxiliary
	 * @return true 订单信息 有效
	 */
	/*private boolean checkTranOrderValid(TransOrderInfo transOrderInfo, OrderAuxiliary orderAuxiliary) {
		// 判断订单信息是否有误
		String msg = checkInfoService.checkTradeInfo(transOrderInfo, orderAuxiliary);
		if (!"ok".equals(msg)) {
			logger.error(msg);
			throw new AccountException("P2", msg);

		} else {
			return true;
		}
	}*/

	/**
	 * 校验转入方余额
	 * 
	 * @param transOrderInfo
	 * @param orderAuxiliary
	 * @return true 转入方余额 有效
	 */
	private Balance checkIntoTran(TransOrderInfo transOrderInfo, OrderAuxiliary orderAuxiliary) {
		// 获取转入方的余额信息
		User user = new User();
		user.userId = transOrderInfo.getInterMerchantCode();
		user.constId = transOrderInfo.getMerchantCode();
		user.productId = orderAuxiliary.getProductQAB();
		Balance balance = checkInfoService.getBalance(user, "");
		if (null == balance) {
			logger.error("获取转入方余额信息失败！");
			throw new AccountException("P2", "获取转入方余额信息失败！");
		} else {
			return balance;
		}
	}

	/**
	 * 获取交易集合并校验余额
	 * 
	 * @param user 用户
	 * @param transOrderInfo 交易订单信息
	 * @param intoBalance 转入方余额
	 * @return true：正确
	 */
	private List<FinanaceEntry> getFinanaceEntriesAndCheckBalance(User user, TransOrderInfo transOrderInfo,
			Balance intoBalance) {
		// 获取每个账户记账流水
		List<FinanaceEntry> finanaceEntries = new ArrayList<FinanaceEntry>();
		// 获取所有账户记账流水
		List<FinanaceEntry> finanaceEntrieAlls = new ArrayList<FinanaceEntry>();
		// 获取套录号
		String entryId = redisIdGenerator.createRequestNo();
		// 主账户金额不满足时，充值子账户中的金额需要部分转账
		Balance balance = checkInfoService.getBalance(user, "");
		if (null != balance) {

			Long amount = transOrderInfo.getAmount(); // 交易金额
			Long nAmount = 0L; // 剩余发生额
			if (balance.getBalanceSettle() < amount) { // 判断 清算余额/可提现余额 是否小于发生金额
				nAmount = amount - balance.getBalanceSettle();// 剩余发生额 ＝ 交易金额 － 清算余额/可提现余额
				transOrderInfo.setAmount(balance.getBalanceSettle());
			}
			balance.setPulseDegree(balance.getPulseDegree() + 1); // 设置账户的心跳
			boolean flag = false;
			finanaceEntries = checkInfoService.getFinanaceEntries(transOrderInfo, balance, intoBalance, entryId, flag);// 获取账户流水实例
			if (null != finanaceEntries && finanaceEntries.size() > 0) {
				for (FinanaceEntry finanaceEntry : finanaceEntries) {
					finanaceEntry.setAccrualType(BaseConstants.TYPE_BALANCE_SETTLE);
					if (null == finanaceEntry.getRemark() || "".equals(finanaceEntry.getRemark())) {
						finanaceEntry.setRemark("君融贷用户申购活期产[主账户转账]");
					}
					if (finanaceEntry.getPaymentAmount() > 0) {
						finanaceEntrieAlls.add(finanaceEntry);
					}
				}
			}
			if (nAmount > 0) {// 金额不满足，从投资子账户（充值子账户）中扣除
				flag = false;
				user.productId = Constants.JRD_RECHARGE_ACCOUNT_PRODUCT;
				balance = checkInfoService.getBalance(user, "");
				transOrderInfo.setAmount(nAmount);
				if (null != balance) {
					balance.setPulseDegree(balance.getPulseDegree() + 1);
					finanaceEntries = checkInfoService.getFinanaceEntries(transOrderInfo, balance, intoBalance, entryId,
							flag);
					if (null != finanaceEntries && finanaceEntries.size() > 0) {
						for (FinanaceEntry finanaceEntry : finanaceEntries) {
							finanaceEntry.setAccrualType(BaseConstants.TYPE_BALANCE_SETTLE);
							if (null == finanaceEntry.getRemark() || "".equals(finanaceEntry.getRemark())) {
								finanaceEntry.setRemark("君融贷用户申购活期产[子账户转账]");
							}
							if (finanaceEntry.getPaymentAmount() > 0) {
								finanaceEntrieAlls.add(finanaceEntry);
							}
						}
					} else {
						logger.error("账户"+transOrderInfo.getUserId()+"的可转账余额小于"+amount);
						throw new AccountException("P2", "账户的可转账余额小于交易金额");
					}
				} else {
					logger.error("获取账户余额信息失败！userId:" + user.userId + ";产品号：" + user.productId);
					throw new AccountException("P2", "获取账户余额信息失败！");
				}
			}

		} else {
			logger.error("获取账户余额信息失败！userId:" + user.userId + ";产品号：" + user.productId);
			throw new AccountException("P2", "获取账户余额信息失败！");
		}
		return finanaceEntrieAlls;
	}

	/**
	 * 获取君融贷 机构流水
	 * @param transOrderInfo 交易订单信息
	 * @param Long amount 交易发生金额
	 * @return 流水集合
	 */
	public List<FinanaceEntry> getJdrOrgFinanaceEntries(TransOrderInfo transOrderInfo , Long amount) {
		String quotaFlag = "1"; //交易金额标志
		List<FinanaceEntry> list = new ArrayList<FinanaceEntry>();
		// 获取活期存款子账户余额(x)
		User jrdOrgCurrentAccountUser = new User();
		jrdOrgCurrentAccountUser.constId = Constants.JRD_ID;
		jrdOrgCurrentAccountUser.productId = Constants.JRD_CURRENT_ACCOUNT;
		Balance jrdOrgCurrentAccountUserBalance = checkInfoService.getBalance(jrdOrgCurrentAccountUser, "");
		Long jrdOrgCurrentAccountBalance = jrdOrgCurrentAccountUserBalance.getBalanceSettle();

		// 获取可用活期存款子账户余额(Z)
		User jrdOrgQuotaAccountUser = new User();
		jrdOrgQuotaAccountUser.constId = Constants.JRD_ID;
		jrdOrgQuotaAccountUser.productId = Constants.JRD_QUOTA_ACCOUNT;
		Balance jrdOrgQuotaAccountUserBalance = checkInfoService.getBalance(jrdOrgQuotaAccountUser, "");
		Long jrdOrgQuotaAccountBalance = jrdOrgQuotaAccountUserBalance.getBalanceSettle();

		// 获取已用活期存款子账户余额(y)
		User jrdOrgUsedAccountUser = new User();
		jrdOrgUsedAccountUser.constId = Constants.JRD_ID;
		jrdOrgUsedAccountUser.productId = Constants.JRD_USED_ACCOUNT;
		Balance jrdOrgUsedAccountUserBalance = checkInfoService.getBalance(jrdOrgUsedAccountUser, "");
		Long jrdOrgUsedAccountBalance = jrdOrgUsedAccountUserBalance.getBalanceSettle();
      
		// 记账规则 需满足 可用活期存款子账户余额（z） ＝ 活期存款子账户（x） － 已用活期存款子账户余额（y）&&
		// 可用活期存款子账户余额（z）>= 0
		if (jrdOrgQuotaAccountBalance > 0) {// 判断可用活期存款子账户余额是否大于0
			// 可用活期存款子账户余额 大于 0 ,则 活期存款子账户 和 可用活期存款子账户余额 都加 交易发生额
			jrdOrgCurrentAccountBalance = jrdOrgCurrentAccountBalance + amount; // 活期存款子账户 = 活期存款子账户 +  交易发生金额
			jrdOrgQuotaAccountBalance = jrdOrgQuotaAccountBalance + amount; // 可用活期存款子账户余额 ＝ 可用活期存款子账户余额 ＋ 交易发生金额

		} else if (jrdOrgQuotaAccountBalance == 0) { // 否则 判断可用活期存款子账户余额是否 等于0
			if(jrdOrgCurrentAccountBalance - jrdOrgUsedAccountBalance >= 0){ // x－y >= 0 
				jrdOrgCurrentAccountBalance = jrdOrgCurrentAccountBalance + amount; // 活期存款子账户（x） = 活期存款子账户 +  交易发生金额
				jrdOrgQuotaAccountBalance = amount; // z ＝ 交易金额
			} else {
				if( amount > (jrdOrgUsedAccountBalance -jrdOrgCurrentAccountBalance )){ //交易金额 > y - x
					quotaFlag = "0";// 额度标志
					jrdOrgQuotaAccountBalance = amount - (jrdOrgUsedAccountBalance -jrdOrgCurrentAccountBalance); // z = 交易金额 － （已用活期存款子账户余额（y）－活期存款子账户（x））
					
				} 
				jrdOrgCurrentAccountBalance = jrdOrgCurrentAccountBalance + amount; // 活期存款子账户（x） = 活期存款子账户 +  交易发生金额
			}
		}
        
		String entryId = redisIdGenerator.createRequestNo(); //获取套录号
		boolean flag = true;
		// 活期存款子账户流水
		jrdOrgCurrentAccountUserBalance.setPulseDegree(jrdOrgCurrentAccountUserBalance.getPulseDegree() + 1);// 心跳计次加一
		jrdOrgCurrentAccountUserBalance.setBalanceSettle(jrdOrgCurrentAccountBalance);// 清算余额
		jrdOrgCurrentAccountUserBalance.setAmount(jrdOrgCurrentAccountBalance);// 账户余额
		jrdOrgCurrentAccountUserBalance.setBalanceUsable(jrdOrgCurrentAccountBalance);// 账户可用余额
		FinanaceEntry jrdOrgCurrentAccountUseFinanaceEntry = new FinanaceEntry();

		jrdOrgCurrentAccountUseFinanaceEntry = finanaceEntryServie.getAccountFlow(transOrderInfo,
				jrdOrgCurrentAccountUserBalance, entryId, flag,"1");// 1 :交易金额
		jrdOrgCurrentAccountUseFinanaceEntry.setAccrualType(BaseConstants.TYPE_BALANCE_SETTLE);// 发生额类型
		jrdOrgCurrentAccountUseFinanaceEntry.setRemark("君融贷机构申购活期产[活期存款子账户入账]");
		list.add(jrdOrgCurrentAccountUseFinanaceEntry);

		// 可用活期存款子账户余额
		if (jrdOrgQuotaAccountBalance > 0) {
			jrdOrgQuotaAccountUserBalance.setPulseDegree(jrdOrgQuotaAccountUserBalance.getPulseDegree() + 1);// 心跳计次加一
			jrdOrgQuotaAccountUserBalance.setBalanceSettle(jrdOrgQuotaAccountBalance);// 清算余额
			jrdOrgQuotaAccountUserBalance.setAmount(jrdOrgQuotaAccountBalance);// 账户余额
			jrdOrgQuotaAccountUserBalance.setBalanceUsable(jrdOrgQuotaAccountBalance);// 账户可用余额
			FinanaceEntry jrdOrgQuotaAccountUserFinanaceEntry = new FinanaceEntry();

			jrdOrgQuotaAccountUserFinanaceEntry = finanaceEntryServie.getAccountFlow(transOrderInfo,
					jrdOrgQuotaAccountUserBalance, entryId, flag,quotaFlag);
			jrdOrgQuotaAccountUserFinanaceEntry.setAccrualType(BaseConstants.TYPE_BALANCE_SETTLE);// 发生额类型
			jrdOrgQuotaAccountUserFinanaceEntry.setRemark("君融贷机构申购活期产[可用活期存款子账户入账]");
			list.add(jrdOrgQuotaAccountUserFinanaceEntry);
		}

		return list;
	}

	/**
	 * 保存交易订单信息数据
	 * 
	 * @param transOrderInfo 交易订单信息
	 * @param finanaceEntrieAlls 交易流水表
	 * @param orderAuxiliary 订单辅助
	 */
	private TransOrderInfo  saveTranOrderInfo(TransOrderInfo transOrderInfo, List<FinanaceEntry> finanaceEntrieAlls,
			OrderAuxiliary orderAuxiliary , Long amount) {
		transOrderInfo.setAccountDate(finanaceEntrieAlls.get(0).getAccountDate());
		transOrderInfo.setRemark("申购活期产品");
		transOrderInfo.setErrorCode(orderAuxiliary.getProductQAA());
		transOrderInfo.setErrorMsg(orderAuxiliary.getProductQAB());
		transOrderInfo.setAmount(amount);
		transOrderInfoManager.saveTransOrderInfo(transOrderInfo);
		return transOrderInfo;

	}

	/**
	 * 保存交易流水
	 * 
	 * @param transOrderInfo  交易订单信息
	 * @param finanaceEntrieAlls 交易流水表
	 */
	private void saveFinanaceEntry(List<FinanaceEntry> finanaceEntrieAlls , TransOrderInfo transOrderInfo) {
		Integer requestId = transOrderInfo.getRequestId();
		for (FinanaceEntry finanaceEntry : finanaceEntrieAlls) {
			finanaceEntry.setReferId(String.valueOf(requestId)) ;
			
		}
		finanaceEntryManager.saveFinanaceEntryList(finanaceEntrieAlls);
	}

}
