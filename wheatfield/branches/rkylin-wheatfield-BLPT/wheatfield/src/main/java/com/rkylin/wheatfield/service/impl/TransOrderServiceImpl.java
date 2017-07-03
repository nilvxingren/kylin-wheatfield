package com.rkylin.wheatfield.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.rkylin.common.RedisIdGenerator;
import com.rkylin.util.bean.BeanMapper;
import com.rkylin.wheatfield.api.PaymentAccountServiceApi;
import com.rkylin.wheatfield.api.SemiAutomatizationServiceApi;
import com.rkylin.wheatfield.api.TransOrderDubboService;
import com.rkylin.wheatfield.bean.BatchTransferOrderInfo;
import com.rkylin.wheatfield.bean.OrderQuery;
import com.rkylin.wheatfield.bean.TransOrderStatusUpdate;
import com.rkylin.wheatfield.constant.AccountConstants;
import com.rkylin.wheatfield.constant.BaseConstants;
import com.rkylin.wheatfield.constant.Constants;
import com.rkylin.wheatfield.constant.RedisConstants;
import com.rkylin.wheatfield.constant.TransCodeConst;
import com.rkylin.wheatfield.common.DateUtils;
import com.rkylin.wheatfield.common.RedisBase;
import com.rkylin.wheatfield.constant.AccountConstants;
import com.rkylin.wheatfield.constant.BaseConstants;
import com.rkylin.wheatfield.constant.Constants;
import com.rkylin.wheatfield.constant.RedisConstants;
import com.rkylin.wheatfield.constant.TransCodeConst;
import com.rkylin.wheatfield.dao.AccountInfoDao;
import com.rkylin.wheatfield.dao.CorporatAccountInfoDao;
import com.rkylin.wheatfield.dao.FinanaceAccountDao;
import com.rkylin.wheatfield.dao.TransOrderInfoDao;
import com.rkylin.wheatfield.exception.AccountException;
import com.rkylin.wheatfield.manager.AccountInfoManager;
import com.rkylin.wheatfield.manager.CorporatAccountInfoManager;
import com.rkylin.wheatfield.manager.TransOrderInfoManager;
import com.rkylin.wheatfield.model.BatchResponse;
import com.rkylin.wheatfield.model.CommonResponse;
import com.rkylin.wheatfield.model.TransOrderInfosResponse;
import com.rkylin.wheatfield.model.TransOrderResponse;
import com.rkylin.wheatfield.pojo.AccountInfo;
import com.rkylin.wheatfield.pojo.AccountInfoQuery;
import com.rkylin.wheatfield.pojo.Balance;
import com.rkylin.wheatfield.pojo.CorporatAccountInfo;
import com.rkylin.wheatfield.pojo.CorporatAccountInfoQuery;
import com.rkylin.wheatfield.pojo.FinanaceAccount;
import com.rkylin.wheatfield.pojo.FinanaceEntry;
import com.rkylin.wheatfield.pojo.TransDaysSummary;
import com.rkylin.wheatfield.pojo.TransDaysSummaryQuery;
import com.rkylin.wheatfield.pojo.TransOrderInfo;
import com.rkylin.wheatfield.pojo.TransOrderInfoQuery;
import com.rkylin.wheatfield.pojo.User;
import com.rkylin.wheatfield.response.ErrorResponse;
import com.rkylin.wheatfield.service.CheckInfoService;
import com.rkylin.wheatfield.service.GenerationPaymentService;
import com.rkylin.wheatfield.service.OperationServive;
import com.rkylin.wheatfield.service.OrderService;
import com.rkylin.wheatfield.service.ParameterInfoService;
import com.rkylin.wheatfield.service.PaymentAccountService;
import com.rkylin.wheatfield.service.TransOrderService;
import com.rkylin.wheatfield.utils.BeanUtil;
import com.rkylin.wheatfield.utils.CodeEnum;
import com.rkylin.wheatfield.utils.CommUtil;

@Service("transOrderService")
public class TransOrderServiceImpl implements TransOrderService,TransOrderDubboService{
	private static Logger logger = LoggerFactory.getLogger(TransOrderServiceImpl.class);
	
	private static Object lock=new Object();
	
	@Autowired
	private TransOrderInfoDao transOrderInfoDao;
	
    @Autowired
    private RedisBase redisBase;
    
    @Autowired
    private SemiAutomatizationServiceApi semiAutomatizationServiceApi;
    
    @Autowired
    private PaymentAccountService paymentAccountService;
    
    @Autowired
    @Qualifier("finanaceAccountDao")
    private FinanaceAccountDao finanaceAccountDao;
    
    @Autowired
    @Qualifier("paymentAccountService")
    private PaymentAccountServiceApi paymentAccountServiceApi;
    
    @Autowired
    private TransOrderInfoManager transOrderInfoManager;
    
    @Autowired
    CheckInfoService checkInfoService;
    
    @Autowired
    RedisIdGenerator redisIdGenerator;
    
    @Autowired
    OperationServive operationService;
    
    @Autowired
    @Qualifier("accountInfoDao")
    private AccountInfoDao accountInfoDao;
    
    @Autowired
    private AccountInfoManager accountInfoManager;
    
    @Autowired
    @Qualifier("corporatAccountInfoDao")
    private CorporatAccountInfoDao corporatAccountInfoDao;
    
    @Autowired
    private OrderService orderService;
	
    @Autowired
    private GenerationPaymentService generationPaymentService;
    
    @Autowired
    private ParameterInfoService parameterInfoService;
    
	/**
	 * 根据请求号查询订单
	 * @param query
	 * @return
	 */
	public TransOrderInfosResponse getOrdersByReqNo(OrderQuery query){
		logger.info("根据请求号查询订单     query="+query);
		TransOrderInfosResponse res = new TransOrderInfosResponse();
		if (query==null) {
			res.setCode(CodeEnum.ERR_PARAM_NULL.getCode());
			res.setMsg(CodeEnum.ERR_PARAM_NULL.getMessage());
			return res;
		}
		logger.info("根据请求号查询订单  参数  requestNoArray="+Arrays.toString(query.getRequestNoArray())+
				",merchantCode="+Arrays.toString(query.getStatusArray()));
		if (query.getRequestNoArray()==null||query.getRequestNoArray().length==0){
			res.setCode(CodeEnum.ERR_PARAM_NULL.getCode());
			res.setMsg(CodeEnum.ERR_PARAM_NULL.getMessage());
			return res;
		}
		List<TransOrderInfo> transOrderInfoList = transOrderInfoDao.queryByReqNo(query);
		logger.info("查出的订单个数="+transOrderInfoList.size());
		if (transOrderInfoList.size()==0) {
			res.setCode(CodeEnum.ERR_DATA_NO_RESULT.getCode());
			res.setMsg(CodeEnum.ERR_DATA_NO_RESULT.getMessage());
			return res;
		}
		res.setTransOrderInfoList(transOrderInfoList);
		return res;
	}
	
	/**
     * 订单查询-订单包号
     * @param query
     * @return
     */
	@Override
    public TransOrderInfosResponse getOrdersByPackageNo(com.rkylin.wheatfield.pojo.TransOrderInfoQuery query){
        logger.info("订单查询  输入参数 :"+BeanUtil.getBeanVal(query, null));
        TransOrderInfosResponse res = new TransOrderInfosResponse();
        res.setCode(CodeEnum.FAILURE.getCode());
        if (query==null) {
            res.setMsg(CodeEnum.ERR_PARAM_NULL.getMessage());
            return res;
        }
        if (query.getOrderPackageNo()==null||"".equals(query.getOrderPackageNo())) {
            res.setMsg("订单包号不能为空");
            return res;
        }
        int notFinalStateCount =  transOrderInfoDao.selectNotFinalStateCount(query);
        if (notFinalStateCount!=0) {
            res.setMsg("订单正在处理中!");
            return res; 
        }
        List<TransOrderInfo> transOrderInfoList = transOrderInfoDao.selectFinalState(query);
        logger.info("查出的订单个数="+transOrderInfoList.size());
        if (transOrderInfoList.size()==0) {
            res.setMsg(CodeEnum.ERR_DATA_NO_RESULT.getMessage());
            return res;
        }
        res.setTransOrderInfoList(transOrderInfoList);
        res.setCode(CodeEnum.SUCCESS.getCode());
        return res;
    }
	
	/**
	 * 订单状态修改
	 * Discription:
	 * @param transOrder
	 * @return CommonResponse
	 * @author Achilles
	 * @since 2016年8月17日
	 */
	@Override
    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
    public CommonResponse updateTransOrderStatus(TransOrderStatusUpdate transOrder) {
        logger.info("订单状态修改  入参 :"+BeanUtil.getBeanVal(transOrder, null)); 
        CommonResponse res = new CommonResponse();
        res.setCode(CodeEnum.FAILURE.getCode());
        if ((!CommUtil.isEmp(transOrder.getRequestNoSet())||!CommUtil.isEmp(transOrder.getOrderPackageNo())||
                !CommUtil.isEmp(transOrder.getOrderNoSet()))
            &&CommUtil.isEmp(transOrder.getMerchantCode())){
            logger.info("订单状态修改 requestId为空时,请求号/订单包号/订单号不为空时,机构号必输!");
            res.setMsg("requestId为空时,请求号/订单包号/订单号不为空时,机构号必输!"); 
            return res;
        }
        if (CommUtil.isEmp(transOrder.getMerchantCode())&&CommUtil.isEmp(transOrder.getRequestIdSet())){
            logger.info("订单状态修改 机构号和requestId不能同时为空!");
            res.setMsg("机构号和requestId不能同时为空!"); 
            return res;
        }
        if (CommUtil.isEmp(transOrder.getRequestIdSet())&&CommUtil.isEmp(transOrder.getRequestNoSet())
                &&CommUtil.isEmp(transOrder.getOrderPackageNo())&&CommUtil.isEmp(transOrder.getOrderNoSet())){
            logger.info("订单状态修改 requestId/请求号/订单包号/订单号不能同时为空!");
            res.setMsg("requestId/请求号/订单包号/订单号不能同时为空!"); 
            return res;
        }
        if (CommUtil.isEmp(transOrder.getStatus())){
            logger.info("订单状态修改  状态值为空!");
            res.setMsg("状态值必输!"); 
            return res; 
        }
        TransOrderInfoQuery query = new TransOrderInfoQuery();
        BeanMapper.copy(transOrder, query);
//        query.setRequestIdSet(transOrder.getRequestIdSet());
//        query.setRequestNoSet(transOrder.getRequestNoSet());
//        query.setOrderPackageNo(transOrder.getOrderPackageNo());
//        query.setOrderNoSet(transOrder.getOrderNoSet());
//        query.setMerchantCode(transOrder.getMerchantCode());
//        query.setStatus(transOrder.getStatus());
        List<TransOrderInfo> transOrderList =  transOrderInfoDao.selectByCon(query);
        if (transOrderList.size()==0) {
            logger.info("订单状态修改  没有查出相关的订单!");
            res.setMsg("没有查出相关的订单!"); 
            return res;  
        }
        query = new TransOrderInfoQuery();
        Set<Integer> requestIdSet = new HashSet<Integer>();
        for (TransOrderInfo transOrderInfoo : transOrderList) {
            requestIdSet.add(transOrderInfoo.getRequestId());
        }
        query.setRequestIdSet(requestIdSet);
        query.setStatus(transOrder.getStatus());
        transOrderInfoDao.updateByPrimaryKeysSelective(query);
        return new CommonResponse();
    }
	
//    @Override
//    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)  
//    public TransOrderResponse refund(String instCode,String orderNo,String errorMsg) {
//        logger.info("退票传入参数  orderNo="+orderNo+",instCode="+instCode+",errorMsg="+errorMsg);
//        TransOrderResponse res = new TransOrderResponse();
//        res.setCode(CodeEnum.FAILURE.getCode());
//        if (orderNo==null||"".equals(orderNo)||instCode==null||"".equals(instCode)) {
//            logger.info("参数为空!");
//            res.setMsg(CodeEnum.ERR_PARAM_NULL.getMessage());
//            return res;
//        }
//        TransOrderInfoQuery query = new TransOrderInfoQuery();
//        query.setOrderNo(orderNo);
//        query.setMerchantCode(instCode);
//        List<TransOrderInfo> transOrderInfoList = transOrderInfoManager.selectTransOrderInfosRefund(query);
//        logger.info("==orderNo="+orderNo+" instCode=="+instCode+"  transOrderInfoList.size()="+transOrderInfoList.size());
//        if (transOrderInfoList.size()==0) {
//            logger.info("==orderNo="+orderNo+" instCode=="+instCode+" 没有查出相应的订单信息");
//            res.setMsg("orderNo="+orderNo+",instCode="+instCode+" 没有查出相应的订单信息");
//            return res;
//        } 
//        TransOrderInfo transOrderInfo = transOrderInfoList.get(0);
//        if (TransCodeConst.TRANS_STATUS_PAY_SUCCEED!=transOrderInfo.getStatus()) {
//            logger.info("订单非正常状态orderNo="+orderNo+",instCode="+instCode+",status="+transOrderInfo.getStatus());
//            res.setMsg("订单非正常状态orderNo="+orderNo+",instCode="+instCode+",status="+transOrderInfo.getStatus());
//            return res;
//        }
//        List<TransOrderInfo> transOrderInfoPennyList = new ArrayList<TransOrderInfo>();
//        TransOrderInfo transOrderInfoBack = new TransOrderInfo();
//        transOrderInfoBack.setOrderNo(orderNo);
//        transOrderInfoBack.setStatus(TransCodeConst.TRANS_STATUS_REFUND);
//        transOrderInfoBack.setErrorMsg(errorMsg);
//        transOrderInfoBack.setBusiTypeId(transOrderInfo.getBusiTypeId());
//        transOrderInfoBack.setTradeFlowNo(transOrderInfo.getTradeFlowNo());
//        transOrderInfoPennyList.add(transOrderInfoBack);
//        res.setTransOrderInfoList(transOrderInfoPennyList);
//        Map<String, String> instcodeToProductMap = parameterInfoService.getParaValAndProductIdByParamCode(RedisConstants.INSTCODE_TO_PRODUCT_KEY);
//        if (instcodeToProductMap==null) {
//            logger.info("没有查到机构和主账户产品对应关系,请检查!");
//            res.setMsg("系统异常!");
//            return res;   
//        }
//        if (instcodeToProductMap.get(transOrderInfo.getMerchantCode())==null) {
//            logger.info("机构"+transOrderInfo.getMerchantCode()+"没有查到机构和主账户产品对应关系");
//            res.setMsg("系统异常!");
//            return res;   
//        }
//        transOrderInfo.setErrorCode(instcodeToProductMap.get(transOrderInfo.getMerchantCode()));
//        //只有代付，提现可以走退票
//        if(!Arrays.asList(TransCodeConst.refundFunc).contains(transOrderInfo.getFuncCode())){
//            logger.info("该交易不能走退票:orderNo="+orderNo+",instCode=="+instCode+",FuncCode="+transOrderInfo.getFuncCode());
//            res.setMsg("该交易不能走退票:orderNo="+orderNo+",instCode=="+instCode+",FuncCode="+transOrderInfo.getFuncCode());
//            return res;
//        }        
//        TransOrderInfo transOrderInfoUpdate = new TransOrderInfo();
//        transOrderInfoUpdate.setRequestId(transOrderInfo.getRequestId());
//        transOrderInfoUpdate.setErrorMsg(errorMsg);
//        transOrderInfoUpdate.setStatus(TransCodeConst.TRANS_STATUS_REFUND);
//        transOrderInfoDao.updateByPrimaryKeySelective(transOrderInfoUpdate);
//        // 生成新的转账，充值等记录流水
//        User user=new User();
//        String userId = transOrderInfo.getUserId();
//        user.userId=userId;
//        user.constId = transOrderInfo.getMerchantCode();
//        if(Constants.HT_CLOUD_ID.equals(transOrderInfo.getMerchantCode()) && Constants.HT_PRODUCT.equals(transOrderInfo.getErrorCode())){
//            user.constId=Constants.HT_ID;
//        }
//        if(TransCodeConst.THIRDPARTYID_DGZHJYZCZH.equals(userId)){
//            user.constId = Constants.FN_ID;
//        }
//        user.productId=transOrderInfo.getErrorCode();
//        logger.info("userId="+userId+",constId="+transOrderInfo.getMerchantCode()+",productId="+transOrderInfo.getErrorCode());
//        if (transOrderInfo.getErrorCode()==null) {
//            user.uEType=AccountConstants.ACCOUNT_TYPE_BASE;
//        }
//        //判断账户状态是否正常
//        boolean accountIsOK=operationService.checkAccount(user);
//        if(!accountIsOK){
//            logger.info("账户状态非正常  用户id="+user.userId+",机构号="+user.constId);
//            res.setMsg("账户状态非正常  用户id="+user.userId+",机构号="+user.constId);
//            return res;
//        }
//        //获取套录号
//        String entryId=redisIdGenerator.createRequestNo();
//        //获取每个账户记账流水
//        List<FinanaceEntry> finanaceEntries = null;
//        List<FinanaceEntry> finanaceEntriesAll=new ArrayList<FinanaceEntry>();
//        //记录该订单交易流水原始amount金额
//        long amount=transOrderInfo.getAmount();
//        Balance balanceA = new Balance();
//        for (int i = 0; i <= 1; i++) {
//            boolean flag=true;
//            if(0==i){
//                userId=transOrderInfo.getUserId();  
//            }else {
//                userId=TransCodeConst.THIRDPARTYID_FNZZH;                               
//                flag=false;
//            }
//            user.userId=userId;
//            Balance balance=null;
//            if(flag){
//                balance=checkInfoService.getBalance(user,"");
//            }else{
//                balance=checkInfoService.getBalance(user,userId);
//            }
//            if(balance==null){ //无法获取用户余额信息！
//                logger.info("无法获取用户余额信息  用户id="+user.userId+",机构号="+user.constId);
//                res.setMsg("无法获取用户余额信息   用户id="+user.userId+",机构号="+user.constId);
//                return res;
//            }
//            
//            balance.setPulseDegree(balance.getPulseDegree()+1);
//            transOrderInfo.setFuncCode(TransCodeConst.CHARGE);
//            //判断机构号是丰年
//            if (i == 0 && Constants.FN_ID.equals(transOrderInfo.getMerchantCode()) && transOrderInfo.getUserFee() > 0){
//                logger.info("--------退票是丰年用户并且有手续费,用户的退票金额为订单金额+手续费");
//                transOrderInfo.setAmount(amount + transOrderInfo.getUserFee());
//            } else {
//                transOrderInfo.setAmount(amount);
//            }
//            finanaceEntries=checkInfoService.getFinanaceEntries(transOrderInfo, balance, entryId, flag);
//            if(finanaceEntries.size()==0){
//                logger.info("生成流水失败  用户id="+user.userId+",机构号="+user.constId);
//                res.setMsg("生成流水失败   用户id="+user.userId+",机构号="+user.constId);
//                return res;
//            }
//            for(FinanaceEntry finanaceEntry:finanaceEntries) {
//                finanaceEntry.setAccrualType(BaseConstants.TYPE_BALANCE_SETTLE);
//                finanaceEntry.setReferId(String.valueOf(transOrderInfo.getRequestId()));
//                if(null==finanaceEntry.getRemark()||"".equals(finanaceEntry.getRemark())){
//                    finanaceEntry.setRemark("退票重划");
//                }
//            }
//            finanaceEntriesAll.addAll(finanaceEntries);
//            if (i==0){
//                balanceA = balance;
//            }
//        }
//        transOrderInfo.setAmount(amount);
//        //判断如果有手续费，做一笔转账
//        if (TransCodeConst.instToEarnMap.containsKey(transOrderInfo.getMerchantCode())&& transOrderInfo.getUserFee() > 0){
//            String earningsEntryId=redisIdGenerator.createRequestNo();
//            logger.info("--------退票用户是有手续费,做收益户向用户的转账，金额为手续费");
//            transOrderInfo.setAmount(transOrderInfo.getUserFee());
//            transOrderInfo.setFuncCode(TransCodeConst.ADJUST_ACCOUNT_AMOUNT);
//            userId=TransCodeConst.instToEarnMap.get(transOrderInfo.getMerchantCode());
//            Balance earningsBalance=null;
//            earningsBalance=checkInfoService.getBalance(user,userId);
//            if (earningsBalance==null){ //无法获取用户余额信息！
//                logger.error("获取收益账户余额信息失败;userId="+userId);
//                res.setMsg("获取收益账户余额信息失败;userId="+userId);
//                return res;
//            }
//            if (earningsBalance.getBalanceSettle() < transOrderInfo.getUserFee()){
//                logger.error("收益账户余额不足;userId="+userId);
//                res.setMsg("收益账户余额不足;userId="+userId);
//                return res;
//            }
//            earningsBalance.setPulseDegree(earningsBalance.getPulseDegree()+1);
//            finanaceEntries=checkInfoService.getFinanaceEntries(transOrderInfo, earningsBalance,balanceA, earningsEntryId, false);
//            if(null!=finanaceEntries&&finanaceEntries.size()>0){
//                for(FinanaceEntry finanaceEntry:finanaceEntries) {
//                    finanaceEntry.setAccrualType(BaseConstants.TYPE_BALANCE_SETTLE);    
//                    finanaceEntry.setReferId(String.valueOf(transOrderInfo.getRequestId()));
//                    if(null==finanaceEntry.getRemark()||"".equals(finanaceEntry.getRemark())){
//                        finanaceEntry.setRemark("转账");
//                    }
//                    if(finanaceEntry.getPaymentAmount()>0){
//                        finanaceEntriesAll.add(finanaceEntry);
//                    }
//                }
//            }
//        }
//        //恢复原始amount金额
//        transOrderInfo.setOrderNo(CommUtil.getGenerateNum(8, 32));
//        transOrderInfo.setOrderPackageNo(orderNo);
//        transOrderInfo.setAmount(amount);
//        transOrderInfo.setRequestId(null);
//        transOrderInfo.setStatus(TransCodeConst.TRANS_STATUS_NORMAL);
//        transOrderInfo.setFuncCode(TransCodeConst.REFUND);
//        transOrderInfo.setErrorMsg("");
//        transOrderInfo.setCreatedTime(null);
//        transOrderInfo.setRemark("退票重划");
//        paymentAccountService.insertFinanaceEntry(finanaceEntriesAll, transOrderInfo, null);
//        res.setCode(CodeEnum.SUCCESS.getCode());
//        return res;
//    }   

    @Override
    public CommonResponse notifyTransOrderResults(List<com.rkylin.wheatfield.bean.TransOrderInfo> transOrderInfoOutList) {
        logger.info("代收付结果通知  入参 :"+BeanUtil.getBeanListVal(transOrderInfoOutList, null));
        //记录代收付清结算推送次数
        Long pushCount = redisBase.getIncreLong(RedisConstants.RECEIVE_AND_PAYMENT_PUSH_COUNT_KEY, 1L,  60L, TimeUnit.DAYS);
        logger.info("代收付结果通知推送次数="+pushCount);
        CommonResponse res = new CommonResponse();
        res.setCode(CodeEnum.FAILURE.getCode());
        if (transOrderInfoOutList==null||transOrderInfoOutList.size()==0) {
            res.setMsg("参数为空!"); 
            return res;
        }
        List<com.rkylin.wheatfield.pojo.TransOrderInfo> transOrderInfoSuccessList = new ArrayList<com.rkylin.wheatfield.pojo.TransOrderInfo>();
        List<com.rkylin.wheatfield.pojo.TransOrderInfo> transOrderInfoFailList = new ArrayList<com.rkylin.wheatfield.pojo.TransOrderInfo>();
        List<com.rkylin.wheatfield.pojo.TransOrderInfo> transOrderInfoRefundList = new ArrayList<com.rkylin.wheatfield.pojo.TransOrderInfo>();
        com.rkylin.wheatfield.pojo.TransOrderInfo transOrderInfo = null;
        Set<String> newOrderNoSet = new HashSet<String>();
        for (com.rkylin.wheatfield.bean.TransOrderInfo transOrderInfoOut : transOrderInfoOutList) {
            String field = BeanUtil.validateBeanProEmpty(transOrderInfoOut, new String[]{"merchantCode","orderNo","amount","status"});
            if (field!=null) {
                logger.info(field+"不能为空,MerchantCode="+transOrderInfoOut.getMerchantCode()+",OrderNo="+transOrderInfoOut.getOrderNo());
                res.setMsg(field+"不能为空,MerchantCode="+transOrderInfoOut.getMerchantCode()+",OrderNo="+transOrderInfoOut.getOrderNo());
                return res;
            }
            transOrderInfo = new com.rkylin.wheatfield.pojo.TransOrderInfo();
            transOrderInfo.setMerchantCode(transOrderInfoOut.getMerchantCode());
            transOrderInfo.setOrderNo(transOrderInfoOut.getOrderNo());
            transOrderInfo.setAmount(transOrderInfoOut.getAmount());
            transOrderInfo.setStatus(transOrderInfoOut.getStatus());
            transOrderInfo.setErrorCode(transOrderInfoOut.getErrorCode());
            transOrderInfo.setErrorMsg(transOrderInfoOut.getErrorMsg());
            if (transOrderInfoOut.getStatus()==TransCodeConst.TRANS_STATUS_PAY_SUCCEED) {
                transOrderInfoSuccessList.add(transOrderInfo);
            }else if (transOrderInfoOut.getStatus()==TransCodeConst.TRANS_STATUS_PAY_FAILED) {
                transOrderInfoFailList.add(transOrderInfo);
            }else if (transOrderInfoOut.getStatus()==TransCodeConst.TRANS_STATUS_REFUND) {
                transOrderInfoRefundList.add(transOrderInfo);
            }else{
                logger.info("Status="+transOrderInfoOut.getStatus()+"非法,MerchantCode="+transOrderInfoOut.getMerchantCode()+",OrderNo="+transOrderInfoOut.getOrderNo());
                res.setMsg("Status="+transOrderInfoOut.getStatus()+"非法,MerchantCode="+transOrderInfoOut.getMerchantCode()+",OrderNo="+transOrderInfoOut.getOrderNo());
                return res;
            }
            newOrderNoSet.add(transOrderInfoOut.getMerchantCode()+"_"+transOrderInfoOut.getOrderNo());
        }
        Set<String> preOrderNoSet = redisBase.getSet(RedisConstants.RECEIVE_AND_PAYMENT_PUSH_ORDERNO_UNIQUE_KEY);
        logger.info("清结算本次推送前,原保存的   机构号_订单号="+preOrderNoSet);
        if (preOrderNoSet!=null && preOrderNoSet.size()!=0) {
            for(String newOrderNo:newOrderNoSet){
                for(String preOrderNo:preOrderNoSet){
                    if (preOrderNo.equals(newOrderNo)) {
                        logger.info("校验清结算是否已经推送过代收付数据   机构号_订单号="+preOrderNo+"  存在于缓存中");
                        res.setMsg("重复推送,机构号_订单号="+preOrderNo);
                        return res;
                    }
                }
            }
        }
        redisBase.saveOrUpdateSet(RedisConstants.RECEIVE_AND_PAYMENT_PUSH_ORDERNO_UNIQUE_KEY, newOrderNoSet, 20, TimeUnit.MINUTES);
        if (transOrderInfoSuccessList.size()!=0) {
            logger.info("推送的数据状态为成功的个数="+transOrderInfoSuccessList.size());
            logger.info("处理数据状态为成功的,存储之前   所有缓存的key=" +redisBase.getSet(RedisConstants.RECEIVE_AND_PAYMENT_PUSH_ALL_KEYS));
            String oneSuccessKey = RedisConstants.RECEIVE_AND_PAYMENT_PUSH_SUCCESS_KEY_PREFIX+pushCount;
            redisBase.saveOrUpdateStrSet(RedisConstants.RECEIVE_AND_PAYMENT_PUSH_ALL_KEYS, oneSuccessKey, 7, TimeUnit.HOURS, false);
            logger.info("处理数据状态为成功的,存储之后   所有缓存的key=" + redisBase.getSet(RedisConstants.RECEIVE_AND_PAYMENT_PUSH_ALL_KEYS));
            redisBase.saveOrUpdateList(oneSuccessKey, transOrderInfoSuccessList, 6, TimeUnit.HOURS);
        }
        if (transOrderInfoFailList.size()!=0) {
            logger.info("推送的数据状态为失败的个数="+transOrderInfoFailList.size());
            logger.info("处理数据状态为失败的,存储之前   所有缓存的key=" +redisBase.getSet(RedisConstants.RECEIVE_AND_PAYMENT_PUSH_ALL_KEYS));
            String oneFailKey = RedisConstants.RECEIVE_AND_PAYMENT_PUSH_FAIL_KEY_PREFIX+pushCount;
            redisBase.saveOrUpdateStrSet(RedisConstants.RECEIVE_AND_PAYMENT_PUSH_ALL_KEYS, oneFailKey, 7, TimeUnit.HOURS, false);
            logger.info("处理数据状态为失败的,存储之后   所有缓存的key=" + redisBase.getSet(RedisConstants.RECEIVE_AND_PAYMENT_PUSH_ALL_KEYS));
            redisBase.saveOrUpdateList(oneFailKey, transOrderInfoFailList, 6, TimeUnit.HOURS);
        }
        if (transOrderInfoRefundList.size()!=0) {
            logger.info("推送的数据状态为退票的个数="+transOrderInfoRefundList.size());
            logger.info("处理数据状态为退票的,存储之前   所有缓存的key=" +redisBase.getSet(RedisConstants.RECEIVE_AND_PAYMENT_PUSH_ALL_KEYS));
            String oneRefundKey = RedisConstants.RECEIVE_AND_PAYMENT_PUSH_REFUND_KEY_PREFIX+pushCount;
            redisBase.saveOrUpdateStrSet(RedisConstants.RECEIVE_AND_PAYMENT_PUSH_ALL_KEYS, oneRefundKey, 7, TimeUnit.HOURS, false);
            logger.info("处理数据状态为退票的,存储之后   所有缓存的key=" + redisBase.getSet(RedisConstants.RECEIVE_AND_PAYMENT_PUSH_ALL_KEYS));
            redisBase.saveOrUpdateList(oneRefundKey, transOrderInfoRefundList, 6, TimeUnit.HOURS);           
        }
        res.setCode(CodeEnum.SUCCESS.getCode());
        return res;
    }
    
    @Override
    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
    public CommonResponse manageRecAndPayCacheResults(Set<String> keySet) {
        synchronized (lock) {
            if (keySet==null) {
                keySet = redisBase.getSet(RedisConstants.RECEIVE_AND_PAYMENT_PUSH_ALL_KEYS);
            }
            logger.info("开始处理缓存中的清结算推入的)代收付数据   所有缓存数据的  key是:" + keySet);
            TransOrderResponse transOrderRes = new TransOrderResponse();
            transOrderRes.setCode(CodeEnum.FAILURE.getCode());
            if (keySet==null || keySet.size()==0) {
                logger.info("缓存中没有要处理的代收付数据!");
                transOrderRes.setMsg("缓存中没有要处理的代收付数据!");
                return transOrderRes;
            }
            List<com.rkylin.wheatfield.pojo.TransOrderInfo> transOrderInfoAllList = new ArrayList<com.rkylin.wheatfield.pojo.TransOrderInfo>();
            List<com.rkylin.wheatfield.pojo.TransOrderInfo> transOrderInfoPennyList = new ArrayList<com.rkylin.wheatfield.pojo.TransOrderInfo>();
            for (String key : keySet) {
                List<com.rkylin.wheatfield.pojo.TransOrderInfo> transOrderInfoList = redisBase.getList(key);
                if (transOrderInfoList == null || transOrderInfoList.size()==0) {
                    logger.info("处理缓存中的代收付数据   根据  key=" + key + " 查出数据(continue) 个数==0");
                    continue;
                }
                logger.info("处理缓存中的代收付数据   根据  key=" + key + " 数据的个数="+transOrderInfoList.size());
                if (key.contains(RedisConstants.RECEIVE_AND_PAYMENT_PUSH_SUCCESS_KEY_PREFIX)) {//处理返回成功的订单
                    transOrderRes = manageSuccessRecAndPayCacheResults(transOrderInfoList,key,transOrderInfoPennyList);
                }else if (key.contains(RedisConstants.RECEIVE_AND_PAYMENT_PUSH_FAIL_KEY_PREFIX)) {//处理返回失败的订单
                    transOrderRes = manageFailRecAndPayCacheResults(transOrderInfoList,key,transOrderInfoPennyList);
                }else if (key.contains(RedisConstants.RECEIVE_AND_PAYMENT_PUSH_REFUND_KEY_PREFIX)) {//处理退票的订单
                    int m = 0;
                    List<TransOrderInfo> transOrderInfoDoneList = new ArrayList<TransOrderInfo>();//正常处理后的数据
                    for (TransOrderInfo transOrderInfo : transOrderInfoList) {
                        transOrderRes = refund(transOrderInfo.getMerchantCode(),transOrderInfo.getOrderNo(),transOrderInfo.getErrorMsg());
                        if (!CodeEnum.SUCCESS.getCode().equals(transOrderRes.getCode())) {
                            logger.info("退票失败  订单号="+transOrderInfo.getOrderNo()+",msg="+transOrderRes.getMsg());
                        }else{
                            transOrderInfoDoneList.add(transOrderInfo);
                            if (Constants.BIZ_TYPE_PENNY.equals(transOrderRes.getTransOrderInfoList().get(0).getBusiTypeId())) {
                                transOrderInfoPennyList.addAll(transOrderRes.getTransOrderInfoList()); 
                            }
                            m++;
                        }
                    }
                    transOrderRes.setCode(CodeEnum.SUCCESS.getCode());
                    if (m!=transOrderInfoList.size()) {
                        transOrderRes.setCode(CodeEnum.FAILURE.getCode());
                    }
                }
                if (CodeEnum.SUCCESS.getCode().equals(transOrderRes.getCode())) {
                    transOrderInfoAllList.addAll(transOrderRes.getTransOrderInfoList());
                }else{
                    logger.info("处理缓存中的代收付数据   key=" + key + " 处理异常,msg="+transOrderRes.getMsg());  
                    continue;
                }
                redisBase.delSet(key);
                redisBase.saveOrUpdateStrSet(RedisConstants.RECEIVE_AND_PAYMENT_PUSH_ALL_KEYS, key, 7L, TimeUnit.DAYS, true);
                logger.info("处理缓存中一个key对应的代收付数据  结束   key=" + key + " 已经删除此key相关的数据!"); 
            }
            //一分钱修改卡状态
            if (transOrderInfoPennyList.size()!=0) {
                updateAccountInfoByPayResult(transOrderInfoPennyList);
            }
          //通知订单系统
            if (transOrderInfoAllList.size()!=0) {
                CommonResponse res = orderService.updateOrderSysOrderStatus(transOrderInfoAllList);
                if (!CodeEnum.SUCCESS.getCode().equals(res.getCode())) {
                     logger.info(" 更新订单系统异常异常!"); 
                     transOrderRes.setMsg(" 更新订单系统异常异常!");
                }else{
                    transOrderRes.setCode(CodeEnum.SUCCESS.getCode());
                }           
            }else {
                logger.info("处理异常或没有查到缓存中的数据!"); 
                transOrderRes.setMsg(" 处理异常或没有查到缓存中的数据!");
            }
            return transOrderRes;            
        }
    }
    
    @Override
    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
    public CommonResponse updateAccInfoStatusRefund(String merchantCode,String orderNo,String errorMsg){
        CommonResponse res = new CommonResponse();
        res.setCode(CodeEnum.FAILURE.getCode());
        if (merchantCode==null||"".equals(merchantCode)||orderNo==null||"".equals(orderNo)) {
            res.setMsg("参数为空!");
            return res;
        }
        TransOrderResponse transOrderResponse = refund(merchantCode,orderNo,errorMsg);
        if (!CodeEnum.SUCCESS.getCode().equals(transOrderResponse.getCode())) {
            logger.info("退票失败  订单号="+orderNo+",msg="+res.getMsg());
            res.setMsg(transOrderResponse.getMsg());
        }else if (transOrderResponse.getTransOrderInfoList().size()!=0){
            res = updateAccountInfoStatusRefund(transOrderResponse.getTransOrderInfoList().get(0));
            if (!CodeEnum.SUCCESS.getCode().equals(res.getCode())) {
                logger.info("退票后修改卡状态失败(有可能是提现退票,不需要修改)     msg="+res.getMsg());
            }
        }       
        return res;
    }
  
    private CommonResponse updateAccountInfoStatusRefund(TransOrderInfo transOrderInfo){
        AccountInfoQuery accountInfoQuery = new AccountInfoQuery();
        accountInfoQuery.setRootInstCd(transOrderInfo.getMerchantCode());
        accountInfoQuery.setAccountNumber(transOrderInfo.getTradeFlowNo());
        accountInfoQuery.setStatus(Constants.ACCOUNT_NUM_STATRS_1);
        List<AccountInfo> accountInfoList = accountInfoManager.queryList(accountInfoQuery);
        CommonResponse res = new CommonResponse();
        if (accountInfoList.size()==0) {
            res.setCode(CodeEnum.FAILURE.getCode());
            res.setMsg("没有查到卡信息,机构号:"+transOrderInfo.getMerchantCode()+",订单号:"+transOrderInfo.getOrderNo());
            return res;
        }
        AccountInfo accountInfo = new AccountInfo();
        accountInfo.setAccountId(accountInfoList.get(0).getAccountId());
        accountInfo.setStatus(Constants.ACCOUNT_NUM_STATRS_4);
        accountInfoManager.updateByPrimaryKeySelective(accountInfo);
        return res;
    }    
    
    @Override
    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)  
    public TransOrderResponse refund(String instCode,String orderNo,String errorMsg) {
        logger.info("退票传入参数  orderNo="+orderNo+",instCode="+instCode+",errorMsg="+errorMsg);
        TransOrderResponse res = new TransOrderResponse();
        res.setCode(CodeEnum.FAILURE.getCode());
        if (orderNo==null||"".equals(orderNo)||instCode==null||"".equals(instCode)) {
            logger.info("参数为空!");
            res.setMsg(CodeEnum.ERR_PARAM_NULL.getMessage());
            return res;
        }
        TransOrderInfoQuery query = new TransOrderInfoQuery();
        query.setOrderNo(orderNo);
        query.setMerchantCode(instCode);
        List<TransOrderInfo> transOrderInfoList = transOrderInfoManager.selectTransOrderInfosRefund(query);
        logger.info("==orderNo="+orderNo+" instCode=="+instCode+"  transOrderInfoList.size()="+transOrderInfoList.size());
        if (transOrderInfoList.size()==0) {
            logger.info("==orderNo="+orderNo+" instCode=="+instCode+" 没有查出相应的订单信息");
            res.setMsg("orderNo="+orderNo+",instCode="+instCode+" 没有查出相应的订单信息");
            return res;
        } 
        TransOrderInfo transOrderInfo = transOrderInfoList.get(0);
        if (TransCodeConst.TRANS_STATUS_PAY_SUCCEED!=transOrderInfo.getStatus()) {
            logger.info("订单非正常状态orderNo="+orderNo+",instCode="+instCode+",status="+transOrderInfo.getStatus());
            res.setMsg("订单非正常状态orderNo="+orderNo+",instCode="+instCode+",status="+transOrderInfo.getStatus());
            return res;
        }
        List<TransOrderInfo> transOrderInfoPennyList = new ArrayList<TransOrderInfo>();
        TransOrderInfo transOrderInfoBack = new TransOrderInfo();
//        transOrderInfoBack.setMerchantCode(transOrderInfo.getMerchantCode());
        transOrderInfoBack.setOrderNo(orderNo);
        transOrderInfoBack.setStatus(TransCodeConst.TRANS_STATUS_REFUND);
        transOrderInfoBack.setErrorMsg(errorMsg);
//        transOrderInfoBack.setFuncCode(transOrderInfo.getFuncCode());
        transOrderInfoBack.setBusiTypeId(transOrderInfo.getBusiTypeId());
        transOrderInfoBack.setTradeFlowNo(transOrderInfo.getTradeFlowNo());
        transOrderInfoPennyList.add(transOrderInfoBack);
        res.setTransOrderInfoList(transOrderInfoPennyList);
        Map<String, String> instcodeToProductMap = parameterInfoService.getParaValAndProductIdByParamCode(RedisConstants.INSTCODE_TO_PRODUCT_KEY);
        if (instcodeToProductMap==null) {
            logger.info("没有查到机构和主账户产品对应关系,请检查!");
            res.setMsg("系统异常!");
            return res;   
        }
        if (instcodeToProductMap.get(transOrderInfo.getMerchantCode())==null) {
            logger.info("机构"+transOrderInfo.getMerchantCode()+"没有查到机构和主账户产品对应关系");
            res.setMsg("系统异常!");
            return res;   
        }
        transOrderInfo.setErrorCode(instcodeToProductMap.get(transOrderInfo.getMerchantCode()));
        //只有代付，提现可以走退票
        if(!TransCodeConst.PAYMENT_WITHHOLD.equals(transOrderInfo.getFuncCode()) && !TransCodeConst.WITHDROW.equals(transOrderInfo.getFuncCode())){
            logger.info("只有代付，提现可以走退票==orderNo="+orderNo+",instCode=="+instCode+",FuncCode="+transOrderInfo.getFuncCode());
            res.setMsg("只有代付，提现可以走退票==orderNo="+orderNo+",instCode=="+instCode+",FuncCode="+transOrderInfo.getFuncCode());
            return res;
        }        
        TransOrderInfo transOrderInfoUpdate = new TransOrderInfo();
        transOrderInfoUpdate.setRequestId(transOrderInfo.getRequestId());
        transOrderInfoUpdate.setErrorMsg(errorMsg);
        transOrderInfoUpdate.setStatus(TransCodeConst.TRANS_STATUS_REFUND);
        transOrderInfoDao.updateByPrimaryKeySelective(transOrderInfoUpdate);
        // 生成新的转账，充值等记录流水
        User user=new User();
        String userId = transOrderInfo.getUserId();
        user.userId=userId;
        user.constId = transOrderInfo.getMerchantCode();
        if(Constants.HT_CLOUD_ID.equals(transOrderInfo.getMerchantCode()) && Constants.HT_PRODUCT.equals(transOrderInfo.getErrorCode())){
            user.constId=Constants.HT_ID;
        }
        if(TransCodeConst.THIRDPARTYID_DGZHJYZCZH.equals(userId)){
            user.constId = Constants.FN_ID;
        }
        user.productId=transOrderInfo.getErrorCode();
        logger.info("userId="+userId+",constId="+transOrderInfo.getMerchantCode()+",productId="+transOrderInfo.getErrorCode());
        if (transOrderInfo.getErrorCode()==null) {
            user.uEType=AccountConstants.ACCOUNT_TYPE_BASE;
        }
        //判断账户状态是否正常
        boolean accountIsOK=operationService.checkAccount(user);
        if(!accountIsOK){
            logger.info("账户状态非正常  用户id="+user.userId+",机构号="+user.constId);
            res.setMsg("账户状态非正常  用户id="+user.userId+",机构号="+user.constId);
            return res;
        }
        //获取套录号
        String entryId=redisIdGenerator.createRequestNo();
        //获取每个账户记账流水
        List<FinanaceEntry> finanaceEntries = null;
        List<FinanaceEntry> finanaceEntriesAll=new ArrayList<FinanaceEntry>();
        //记录该订单交易流水原始amount金额
        long amount=transOrderInfo.getAmount();
        Balance balanceA = new Balance();
        for (int i = 0; i <= 1; i++) {
            boolean flag=true;
            if(0==i){
                userId=transOrderInfo.getUserId();  
            }else {
                userId=TransCodeConst.THIRDPARTYID_FNZZH;                               
                flag=false;
            }
            user.userId=userId;
            Balance balance=null;
            if(flag){
                balance=checkInfoService.getBalance(user,"");
            }else{
                balance=checkInfoService.getBalance(user,userId);
            }
            if(balance==null){ //无法获取用户余额信息！
                logger.info("无法获取用户余额信息  用户id="+user.userId+",机构号="+user.constId);
                res.setMsg("无法获取用户余额信息   用户id="+user.userId+",机构号="+user.constId);
                return res;
            }
            
            balance.setPulseDegree(balance.getPulseDegree()+1);
            transOrderInfo.setFuncCode(TransCodeConst.CHARGE);
            //判断机构号是丰年
            if (i == 0 && Constants.FN_ID.equals(transOrderInfo.getMerchantCode()) && transOrderInfo.getUserFee() > 0){
                logger.info("--------退票是丰年用户并且有手续费,用户的退票金额为订单金额+手续费");
                transOrderInfo.setAmount(amount + transOrderInfo.getUserFee());
            } else {
                transOrderInfo.setAmount(amount);
            }
            finanaceEntries=checkInfoService.getFinanaceEntries(transOrderInfo, balance, entryId, flag);
            if(finanaceEntries.size()==0){
                logger.info("生成流水失败  用户id="+user.userId+",机构号="+user.constId);
                res.setMsg("生成流水失败   用户id="+user.userId+",机构号="+user.constId);
                return res;
            }
            for(FinanaceEntry finanaceEntry:finanaceEntries) {
                finanaceEntry.setAccrualType(BaseConstants.TYPE_BALANCE_SETTLE);
                finanaceEntry.setReferId(String.valueOf(transOrderInfo.getRequestId()));
                if(null==finanaceEntry.getRemark()||"".equals(finanaceEntry.getRemark())){
                    finanaceEntry.setRemark("退票重划");
                }
            }
            finanaceEntriesAll.addAll(finanaceEntries);
            if (i==0){
                balanceA = balance;
            }
        }
        //恢复原始amount金额
        transOrderInfo.setAmount(amount);
        //判断如果有手续费，做一笔转账
        if (TransCodeConst.instToEarnMap.containsKey(transOrderInfo.getMerchantCode())&& transOrderInfo.getUserFee() > 0){
            String earningsEntryId=redisIdGenerator.createRequestNo();
            logger.info("--------退票用户是有手续费,做收益户向用户的转账，金额为手续费");
            transOrderInfo.setAmount(transOrderInfo.getUserFee());
            transOrderInfo.setFuncCode(TransCodeConst.ADJUST_ACCOUNT_AMOUNT);
            userId=TransCodeConst.instToEarnMap.get(transOrderInfo.getMerchantCode());
            Balance earningsBalance=null;
            earningsBalance=checkInfoService.getBalance(user,userId);
            if (earningsBalance==null){ //无法获取用户余额信息！
                logger.error("获取收益账户余额信息失败;userId="+userId);
                res.setMsg("获取收益账户余额信息失败;userId="+userId);
                return res;
            }
            if (earningsBalance.getBalanceSettle() < transOrderInfo.getUserFee()){
                logger.error("收益账户余额不足;userId="+userId);
                res.setMsg("收益账户余额不足;userId="+userId);
                return res;
            }
            earningsBalance.setPulseDegree(earningsBalance.getPulseDegree()+1);
            finanaceEntries=checkInfoService.getFinanaceEntries(transOrderInfo, earningsBalance,balanceA, earningsEntryId, false);
            if(null!=finanaceEntries&&finanaceEntries.size()>0){
                for(FinanaceEntry finanaceEntry:finanaceEntries) {
                    finanaceEntry.setAccrualType(BaseConstants.TYPE_BALANCE_SETTLE);    
                    finanaceEntry.setReferId(String.valueOf(transOrderInfo.getRequestId()));
                    if(null==finanaceEntry.getRemark()||"".equals(finanaceEntry.getRemark())){
                        finanaceEntry.setRemark("转账");
                    }
                    if(finanaceEntry.getPaymentAmount()>0){
                        finanaceEntriesAll.add(finanaceEntry);
                    }
                }
            }
        }
        //恢复原始amount金额
        transOrderInfo.setOrderNo(CommUtil.getGenerateNum(8, 32));
        transOrderInfo.setOrderPackageNo(orderNo);
        transOrderInfo.setAmount(amount);
        transOrderInfo.setRequestId(null);
        transOrderInfo.setStatus(TransCodeConst.TRANS_STATUS_NORMAL);
        transOrderInfo.setFuncCode(TransCodeConst.REFUND);
        transOrderInfo.setErrorMsg("");
        transOrderInfo.setCreatedTime(null);
        transOrderInfo.setRemark("退票重划");
        paymentAccountService.insertFinanaceEntry(finanaceEntriesAll, transOrderInfo, null);
        res.setCode(CodeEnum.SUCCESS.getCode());
        return res;
    }    
    
    /**
     * 其他应收款减,备付金减,修改订单状态为冲正
     * Discription:
     * @param transOrderInfoFailList
     * @param oneFailKey
     * @return CommonResponse
     * @author Achilles
     * @since 2016年10月13日
     */
    private TransOrderResponse manageFailRecAndPayCacheResults(List<com.rkylin.wheatfield.pojo.TransOrderInfo> transOrderInfoFailList,
            String oneFailKey,List<com.rkylin.wheatfield.pojo.TransOrderInfo> transOrderInfoPennyList){
        TransOrderResponse transOrderRes = new TransOrderResponse();
        transOrderRes.setCode(CodeEnum.FAILURE.getCode());
        List<TransOrderInfo> transOrderInfoList = getTransOrderInfoListByInstcodeAndOrderNo(transOrderInfoFailList);
        logger.info("处理失败的推送数据  查出账户系统的订单的个数="+transOrderInfoList.size());
        if(transOrderInfoList.size()==0){
            transOrderRes.setMsg("没有查出相应订单信息!");
            return transOrderRes;
        }
        List<com.rkylin.wheatfield.pojo.TransOrderInfo> transOrderInfoUpdateList = new ArrayList<com.rkylin.wheatfield.pojo.TransOrderInfo>();
        Long collFailSumAmount = 0l;
        List<TransOrderInfo> transOrderInfoDoneList = new ArrayList<TransOrderInfo>();//正常处理后的数据
        for (TransOrderInfo transOrderInfo : transOrderInfoList) {
            if (transOrderInfo.getStatus()!=TransCodeConst.TRANS_STATUS_NORMAL) {
                logger.info("代收付结果返回,处理失败订单,Status="+transOrderInfo.getStatus()+"非正常,MerchantCode="+transOrderInfo.getMerchantCode()+",OrderNo="+transOrderInfo.getOrderNo());            
                continue;
            }
            if (TransCodeConst.PAYMENT_WITHHOLD.equals(transOrderInfo.getFuncCode())&&
                    Constants.BIZ_TYPE_PENNY.equals(transOrderInfo.getBusiTypeId())) {
                transOrderInfo.setStatus(TransCodeConst.TRANS_STATUS_PAY_FAILED);
                transOrderInfoPennyList.add(transOrderInfo);
            }            
            TransOrderInfo transOrderInfoUpdate = new TransOrderInfo();
            for (TransOrderInfo transOrderInfoFail : transOrderInfoFailList) {
                if (transOrderInfoFail.getMerchantCode().equals(transOrderInfo.getMerchantCode())&&
                        transOrderInfoFail.getOrderNo().equals(transOrderInfo.getOrderNo())) {
                    transOrderInfoFail.setFuncCode(transOrderInfo.getFuncCode());
                    transOrderInfoUpdate.setMerchantCode(transOrderInfoFail.getMerchantCode());
                    transOrderInfoUpdate.setOrderNo(transOrderInfoFail.getOrderNo());
                    transOrderInfoUpdate.setErrorMsg(transOrderInfoFail.getErrorMsg());
                    transOrderInfoUpdate.setStatus(TransCodeConst.TRANS_STATUS_PAY_FAILED);
                    transOrderInfoUpdateList.add(transOrderInfoUpdate);
                    if (TransCodeConst.PAYMENT_COLLECTION.equals(transOrderInfo.getFuncCode())) {
                        collFailSumAmount+=transOrderInfoFail.getAmount();
                    }
                    break;
                }
            }
            transOrderInfoDoneList.add(transOrderInfo);
        }
        transOrderRes.setTransOrderInfoList(transOrderInfoDoneList);
        if (transOrderInfoUpdateList.size()==0) {
            transOrderRes.setMsg("处理失败!");
            return transOrderRes; 
        }
        if (collFailSumAmount!=0l) {
            List<com.rkylin.wheatfield.bean.User> userList = new ArrayList<com.rkylin.wheatfield.bean.User>();
            com.rkylin.wheatfield.bean.User user = new com.rkylin.wheatfield.bean.User();
            user.setInstCode(Constants.FN_ID);
            user.setUserId(TransCodeConst.THIRDPARTYID_FNZZH);
            user.setProductId(Constants.FN_PRODUCT);
            user.setAmount(collFailSumAmount);
            user.setStatus(TransCodeConst.MINUS);
            user.setFinAccountId(oneFailKey.substring(oneFailKey.length()-20, oneFailKey.length()));
            userList.add(user);
            com.rkylin.wheatfield.bean.User user2 = new com.rkylin.wheatfield.bean.User();
            BeanMapper.copy(user, user2);
            user2.setUserId(TransCodeConst.THIRDPARTYID_QTYSKZH);
            userList.add(user2);
            CommonResponse res = semiAutomatizationServiceApi.operateFinAccounts(userList);
            if (!CodeEnum.SUCCESS.getCode().equals(res.getCode())) {
                logger.info("将代收失败的生成流水对冲备付金和其它应收款时发生异常   msg="+res.getMsg()); 
                transOrderRes.setMsg("将代收失败的生成流水对冲备付金和其它应收款时发生异常   msg="+res.getMsg());
                return transOrderRes;
            }  
        }
        transOrderInfoDao.batchUpdateByOrderNoAndMerCode(transOrderInfoUpdateList);
        transOrderRes.setCode(CodeEnum.SUCCESS.getCode());
        return transOrderRes;
    }  
    
    /**
     * 代收/贷款还款记账:用户加,其他应收款减;如果是贷款还款,发起内部转账并代付还款
     * Discription:
     * @param transOrderInfoSuccessList
     * @param oneSuccessKey
     * @return CommonResponse
     * @author Achilles
     * @since 2016年10月17日
     */
    private TransOrderResponse manageSuccessRecAndPayCacheResults(List<com.rkylin.wheatfield.pojo.TransOrderInfo> transOrderInfoSuccessList,
            String oneSuccessKey,List<com.rkylin.wheatfield.pojo.TransOrderInfo> transOrderInfoPennyList){
        TransOrderResponse transOrderRes = new TransOrderResponse();
        transOrderRes.setCode(CodeEnum.FAILURE.getCode());
        List<TransOrderInfo> transOrderInfoList = getTransOrderInfoListByInstcodeAndOrderNo(transOrderInfoSuccessList);
        logger.info("处理成功的推送数据  查出账户系统的订单的个数="+transOrderInfoList.size());
        if(transOrderInfoList.size()==0){
            logger.info("没有查出相应订单信息!");
            transOrderRes.setMsg("没有查出相应订单信息!");
            return transOrderRes;
        }
        com.rkylin.wheatfield.bean.User user = null;
        Long amount = null;
        Long qtyskSumAmount = 0l;
        List<com.rkylin.wheatfield.pojo.TransOrderInfo> transOrderInfoLoanList = new ArrayList<com.rkylin.wheatfield.pojo.TransOrderInfo>();
        Map<String, String> instcodeToProductMap = parameterInfoService.getParaValAndProductIdByParamCode(RedisConstants.INSTCODE_TO_PRODUCT_KEY);
        if (instcodeToProductMap==null) {
            logger.info("没有查到机构和主账户产品对应关系,请检查!");
            transOrderRes.setMsg("系统异常!");
            return transOrderRes;   
        }
        String productId = null;
        List<TransOrderInfo> transOrderInfoDoneList = new ArrayList<TransOrderInfo>();//正常处理后的数据
        for (TransOrderInfo transOrderInfo : transOrderInfoList) {
            if (transOrderInfo.getStatus()!=TransCodeConst.TRANS_STATUS_NORMAL) {
                logger.info("代收付结果返回,处理成功订单,Status="+transOrderInfo.getStatus()+"非正常,MerchantCode="+transOrderInfo.getMerchantCode()+",OrderNo="+transOrderInfo.getOrderNo());            
                continue;
            }
            productId = instcodeToProductMap.get(transOrderInfo.getMerchantCode());
            if (productId==null) {
                logger.info("代收付结果返回,处理成功订单,机构"+transOrderInfo.getMerchantCode()+"没有查到机构和主账户产品对应关系");            
                continue;               
            }
            if (TransCodeConst.PAYMENT_WITHHOLD.equals(transOrderInfo.getFuncCode())&&
                    Constants.BIZ_TYPE_PENNY.equals(transOrderInfo.getBusiTypeId())) {
                transOrderInfo.setStatus(TransCodeConst.TRANS_STATUS_PAY_SUCCEED);
                transOrderInfoPennyList.add(transOrderInfo);
            }
            for (TransOrderInfo transOrderInfoSuccess : transOrderInfoSuccessList) {
                if (transOrderInfoSuccess.getMerchantCode().equals(transOrderInfo.getMerchantCode())&&
                        transOrderInfoSuccess.getOrderNo().equals(transOrderInfo.getOrderNo())) {
                    transOrderInfoSuccess.setFuncCode(transOrderInfo.getFuncCode());
                    if (TransCodeConst.PAYMENT_COLLECTION.equals(transOrderInfo.getFuncCode())) {
                        amount = transOrderInfoSuccess.getAmount();
                        break;
                    }                    
                }
            }
            CommonResponse res = null;
            if (TransCodeConst.PAYMENT_COLLECTION.equals(transOrderInfo.getFuncCode())) {//代收
                if (Constants.BIZ_TYPE_1.equals(transOrderInfo.getBusiTypeId())) {//贷款还款
                    transOrderInfo.setProductIdd(productId);
                    transOrderInfo.setAmount(amount);
                    transOrderInfoLoanList.add(transOrderInfo); 
                }
                user = new com.rkylin.wheatfield.bean.User();
                user.setInstCode(transOrderInfo.getMerchantCode());
                user.setUserId(transOrderInfo.getUserId());
                user.setProductId(productId);
                user.setAmount(amount);
                user.setStatus(TransCodeConst.PLUS); 
                user.setFinAccountId(transOrderInfo.getRequestId()+"");
                res = semiAutomatizationServiceApi.operateFinAccount(user);
                if (CodeEnum.SUCCESS.getCode().equals(res.getCode())) {
                    qtyskSumAmount+=amount;
                }                
            }
            if (res==null||CodeEnum.SUCCESS.getCode().equals(res.getCode())) {
                TransOrderInfo transOrderUpdate = new TransOrderInfo();
                transOrderUpdate.setRequestId(transOrderInfo.getRequestId());
                transOrderUpdate.setStatus(TransCodeConst.TRANS_STATUS_PAY_SUCCEED);
                transOrderInfoDao.updateByPrimaryKeySelective(transOrderUpdate);  
                transOrderInfo.setStatus(TransCodeConst.TRANS_STATUS_PAY_SUCCEED);
                transOrderInfoDoneList.add(transOrderInfo);
            }
        }
        if (transOrderInfoDoneList.size()==0) {
            transOrderRes.setMsg("处理失败,请检查数据!");
            return transOrderRes;
        }
        if (qtyskSumAmount!=0l) {
            user = new com.rkylin.wheatfield.bean.User();
            user.setInstCode(Constants.FN_ID);
            user.setUserId(TransCodeConst.THIRDPARTYID_QTYSKZH);
            user.setProductId(Constants.FN_PRODUCT);
            user.setAmount(qtyskSumAmount);
            user.setStatus(TransCodeConst.MINUS); 
            user.setFinAccountId(oneSuccessKey.substring(oneSuccessKey.length()-20, oneSuccessKey.length()));
            CommonResponse res = semiAutomatizationServiceApi.operateFinAccount(user);  
            if (!CodeEnum.SUCCESS.getCode().equals(res.getCode())) {
                logger.info("其他应收款账户减钱失败"); 
                transOrderRes.setMsg("处理失败!");
                return transOrderRes;
            }
        }        
        if (transOrderInfoLoanList.size()!=0) {//贷款还款发起内部转账,然后代付
            CommonResponse res = transferAndWithholdAfterLoanBack(transOrderInfoLoanList);
            if (!CodeEnum.SUCCESS.getCode().equals(res.getCode())) {
                logger.info("代收付结果返回,内部(贷款还款)转账异常:"+res.getMsg()); 
                transOrderRes.setMsg(res.getMsg());
                return transOrderRes;
            }
            transOrderRes.setCode(CodeEnum.SUCCESS.getCode());
        }
        transOrderRes.setTransOrderInfoList(transOrderInfoDoneList);
        transOrderRes.setCode(CodeEnum.SUCCESS.getCode());
        return transOrderRes;
    }

    private List<TransOrderInfo> getTransOrderInfoListByInstcodeAndOrderNo(
            List<com.rkylin.wheatfield.pojo.TransOrderInfo> transOrderInfoSuccessList) {
        List<Map<String, String>> orderNoToInstCodeMaplist = new ArrayList<Map<String, String>>();
        for (com.rkylin.wheatfield.pojo.TransOrderInfo transOrderInfo : transOrderInfoSuccessList) {
            Map<String, String> map = new HashMap<String, String>();
            map.put("orderNo", transOrderInfo.getOrderNo());
            map.put("instCode", transOrderInfo.getMerchantCode());
            orderNoToInstCodeMaplist.add(map);
        }
        List<TransOrderInfo> transOrderInfoList = transOrderInfoDao.selectByOrderNoAndInstCode(orderNoToInstCodeMaplist);
        return transOrderInfoList;
    }
    
    /**
     * 转账给放款方,并发起代付（代收付返回结果后）
     * @param transOrderInfoList
     * @return
     */
    private CommonResponse transferAndWithholdAfterLoanBack(List<TransOrderInfo> transOrderInfoList){
        CommonResponse res = new CommonResponse();
        res.setCode(CodeEnum.FAILURE.getCode());
        Set<String> finAccIdSet = new HashSet<String>(); 
        for (TransOrderInfo transOrderInfo : transOrderInfoList) {
            finAccIdSet.add(transOrderInfo.getInterMerchantCode());
        }
        String[] finAccIdArray =  finAccIdSet.toArray(new String[0]);
        //查出转账时收款方的账户信息
        List<FinanaceAccount> finAccList = finanaceAccountDao.selectByFinAccountId(finAccIdArray);
        if (finAccList.size()==0) {
            res.setMsg("没有查到转入方账户信息");
            return res;
        }
        List<TransOrderInfo> transOrderInfooList = null;
        Map<String,List<TransOrderInfo>> finAccIdToOrdersMap = new HashMap<String,List<TransOrderInfo>>();
        for (TransOrderInfo transOrderInfo : transOrderInfoList) {
            if (finAccIdToOrdersMap.containsKey(transOrderInfo.getInterMerchantCode())) {
                transOrderInfooList = finAccIdToOrdersMap.get(transOrderInfo.getInterMerchantCode());
                transOrderInfooList.add(transOrderInfo);
                finAccIdToOrdersMap.put(transOrderInfo.getInterMerchantCode(), transOrderInfooList);
            }else {
                transOrderInfooList = new ArrayList<TransOrderInfo>();
                transOrderInfooList.add(transOrderInfo);
                finAccIdToOrdersMap.put(transOrderInfo.getInterMerchantCode(), transOrderInfooList);
            }
        }
        BatchTransferOrderInfo batchTransferOrderInfo = null;
        Set<String> orderPackageNoSet = new HashSet<String>(); //生成随机数临时放置，防止重复
        List<com.rkylin.wheatfield.bean.OrderInfo> orderInfoList = null;
        BatchResponse batchResponse = null;
        int failCount = 0;
        //转账到收款方,发起代付
        for (String finAccountId : finAccIdToOrdersMap.keySet()) {
            transOrderInfooList = finAccIdToOrdersMap.get(finAccountId);
            batchTransferOrderInfo = new BatchTransferOrderInfo();
            batchTransferOrderInfo.setMerchantCode(transOrderInfooList.get(0).getMerchantCode());
            FinanaceAccount finanaceAccount = null;
            for (FinanaceAccount finAccount:finAccList) {
                if (finAccount.getFinAccountId().equals(finAccountId)) {
                    batchTransferOrderInfo.setIntoProductId(finAccount.getGroupManage());
                    batchTransferOrderInfo.setIntoUserId(finAccount.getAccountRelateId());
                    finanaceAccount = finAccount;
                    break;
                }
            }
            batchTransferOrderInfo.setFuncCode(TransCodeConst.ADJUST_ACCOUNT_AMOUNT);
            batchTransferOrderInfo.setOrderPackageNo(CommUtil.getGenerateNum(orderPackageNoSet, 1, 14)+"_LOANT");
            Set<String> orderNoSet = new HashSet<String>(); //生成随机数临时放置，防止重复
            Long sumAmount = 0l;
            com.rkylin.wheatfield.bean.OrderInfo orderInfo = null;  
            for (TransOrderInfo transOrderInfo : transOrderInfooList) {
                orderInfo = new com.rkylin.wheatfield.bean.OrderInfo();
                orderInfo.setAmount(transOrderInfo.getAmount());
                orderInfo.setOutUserId(transOrderInfo.getUserId());
                orderInfo.setOutProductId(transOrderInfo.getProductIdd());
                orderInfo.setRequestNo(transOrderInfo.getRequestId()+"");
                orderInfo.setOrderNo(CommUtil.getGenerateNum(5, 25)+"_LOANT");
                orderInfo.setRemark("贷款还款,结果返回,内部转账");
                orderInfoList = new ArrayList<com.rkylin.wheatfield.bean.OrderInfo>();
                orderInfoList.add(orderInfo);
                batchTransferOrderInfo.setOrderInfoList(orderInfoList);
                batchResponse = paymentAccountServiceApi.transferBatch(batchTransferOrderInfo);
                if (CodeEnum.SUCCESS.getCode().equals(batchResponse.getCode())) {
                    sumAmount +=transOrderInfo.getAmount(); 
                }else {
                    logger.info("代收付结果返回,内部转账异常;转入方账户id="+finAccountId+",msg="+batchResponse.getMsg());
                } 
            }
            if (sumAmount==0l) {
                failCount++;
                continue;
            }
            finanaceAccount.setAmount(sumAmount);
            ErrorResponse errorResponse = withholdAfterLoanBack(finanaceAccount,orderNoSet);//收款方发起代付
            if (!errorResponse.isIs_success()) {
                logger.info("代收付结果返回,发起代付异常,账户id="+finAccountId+",msg="+errorResponse.getMsg());
            } 
         }
        if (failCount==0) {
            res.setCode(CodeEnum.SUCCESS.getCode()); 
        }
        return res;
    }
    
    private ErrorResponse withholdAfterLoanBack(FinanaceAccount finanaceAccount,Set<String> orderNoSet){
        TransOrderInfo transOrderInfoInto = new TransOrderInfo();
        transOrderInfoInto.setFuncCode(TransCodeConst.PAYMENT_WITHHOLD);
        Date date = DateUtils.getSysDate(Constants.DATE_FORMAT_YYYYMMDDHHMMSS);
        transOrderInfoInto.setOrderDate(date);
        transOrderInfoInto.setOrderNo(CommUtil.getGenerateNum(orderNoSet, 6, 26)+"_LOANW");
        transOrderInfoInto.setAmount(finanaceAccount.getAmount());
        transOrderInfoInto.setOrderAmount(finanaceAccount.getAmount());
        transOrderInfoInto.setRequestTime(date);
        transOrderInfoInto.setStatus(TransCodeConst.TRANS_STATUS_NORMAL);
        transOrderInfoInto.setUserFee(0L);
        transOrderInfoInto.setMerchantCode(finanaceAccount.getRootInstCd());
        transOrderInfoInto.setUserId(finanaceAccount.getAccountRelateId());
        transOrderInfoInto.setProductIdd(finanaceAccount.getGroupManage());
        transOrderInfoInto.setInterMerchantCode(finanaceAccount.getAccountRelateId());
        transOrderInfoInto.setOrderCount(1);
        transOrderInfoInto.setUserIpAddress("127.0.0.1");
        transOrderInfoInto.setRemark("贷款还款,代收付返回结果,内部转账后发起代付");
        ErrorResponse errorResponse =paymentAccountService.withhold(transOrderInfoInto, transOrderInfoInto.getProductIdd());
        return errorResponse;
    }

    @Override
    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
    public void updateAccountInfoByPayResult(List<com.rkylin.wheatfield.pojo.TransOrderInfo> transOrderInfoPennyList) {
        List<AccountInfo> accountInfoUpdateList = new ArrayList<AccountInfo>();
        for (TransOrderInfo transOrderInfo : transOrderInfoPennyList) {
            AccountInfoQuery accountInfoQuery = new AccountInfoQuery();
            accountInfoQuery.setRootInstCd(transOrderInfo.getMerchantCode());
            accountInfoQuery.setAccountNumber(transOrderInfo.getTradeFlowNo());
            int status = Constants.ACCOUNT_NUM_STATRS_3;
            accountInfoQuery.setStatus(status);
            if (transOrderInfo.getStatus()==TransCodeConst.TRANS_STATUS_REFUND) {
                status = Constants.ACCOUNT_NUM_STATRS_1;
                accountInfoQuery.setStatus(status);
            }
            List<AccountInfo> accountInfoList = accountInfoDao.selectByNumAndConstId(accountInfoQuery);
            if (accountInfoList.size()==0) {
                logger.info("根据状态"+status+",机构号="+accountInfoQuery.getRootInstCd()+",卡号="+accountInfoQuery.getAccountNumber()+",没有查到相应的卡信息!");
                continue;
            }
            AccountInfo accountInfo= new AccountInfo();
            accountInfo.setAccountId(accountInfoList.get(0).getAccountId());
            if (transOrderInfo.getStatus()==TransCodeConst.TRANS_STATUS_PAY_SUCCEED) {
                accountInfo.setStatus(Constants.ACCOUNT_NUM_STATRS_1);   
            }
            if (transOrderInfo.getStatus()==TransCodeConst.TRANS_STATUS_PAY_FAILED||transOrderInfo.getStatus()==TransCodeConst.TRANS_STATUS_REFUND) {
                accountInfo.setStatus(Constants.ACCOUNT_NUM_STATRS_4);
            }
            accountInfoUpdateList.add(accountInfo);
        }
        if (accountInfoUpdateList.size()!=0) {
            accountInfoDao.batchUpdate(accountInfoUpdateList); 
        }
    }
    
    
    
}
