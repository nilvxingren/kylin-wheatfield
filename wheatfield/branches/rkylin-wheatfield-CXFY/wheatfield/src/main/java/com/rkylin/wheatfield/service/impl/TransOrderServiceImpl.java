package com.rkylin.wheatfield.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.rkylin.common.RedisIdGenerator;
import com.rkylin.util.bean.BeanMapper;
import com.rkylin.wheatfield.api.TransOrderDubboService;
import com.rkylin.wheatfield.bean.OrderQuery;
import com.rkylin.wheatfield.bean.TransOrderStatusUpdate;
import com.rkylin.wheatfield.constant.AccountConstants;
import com.rkylin.wheatfield.constant.BaseConstants;
import com.rkylin.wheatfield.constant.Constants;
import com.rkylin.wheatfield.constant.RedisConstants;
import com.rkylin.wheatfield.constant.TransCodeConst;
import com.rkylin.wheatfield.dao.TransOrderInfoDao;
import com.rkylin.wheatfield.manager.TransOrderInfoManager;
import com.rkylin.wheatfield.model.CommonResponse;
import com.rkylin.wheatfield.model.TransOrderInfosResponse;
import com.rkylin.wheatfield.model.TransOrderResponse;
import com.rkylin.wheatfield.pojo.Balance;
import com.rkylin.wheatfield.pojo.FinanaceEntry;
import com.rkylin.wheatfield.pojo.TransOrderInfo;
import com.rkylin.wheatfield.pojo.TransOrderInfoQuery;
import com.rkylin.wheatfield.pojo.User;
import com.rkylin.wheatfield.service.CheckInfoService;
import com.rkylin.wheatfield.service.OperationServive;
import com.rkylin.wheatfield.service.ParameterInfoService;
import com.rkylin.wheatfield.service.PaymentAccountService;
import com.rkylin.wheatfield.service.TransOrderService;
import com.rkylin.wheatfield.utils.BeanUtil;
import com.rkylin.wheatfield.utils.CodeEnum;
import com.rkylin.wheatfield.utils.CommUtil;

@Service("transOrderService")
public class TransOrderServiceImpl implements TransOrderService,TransOrderDubboService{
	private static Logger logger = LoggerFactory.getLogger(TransOrderServiceImpl.class);
	
	@Autowired
	private TransOrderInfoDao transOrderInfoDao;
	
    @Autowired
    private TransOrderInfoManager transOrderInfoManager;

    @Autowired
    private ParameterInfoService parameterInfoService;
    
    @Autowired
    private OperationServive operationService;
    
    @Autowired
    private RedisIdGenerator redisIdGenerator;
    
    @Autowired
    private CheckInfoService checkInfoService;
    
    @Autowired
    private PaymentAccountService paymentAccountService;
    
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
        transOrderInfoBack.setOrderNo(orderNo);
        transOrderInfoBack.setStatus(TransCodeConst.TRANS_STATUS_REFUND);
        transOrderInfoBack.setErrorMsg(errorMsg);
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
        if(!Arrays.asList(TransCodeConst.refundFunc).contains(transOrderInfo.getFuncCode())){
            logger.info("该交易不能走退票:orderNo="+orderNo+",instCode=="+instCode+",FuncCode="+transOrderInfo.getFuncCode());
            res.setMsg("该交易不能走退票:orderNo="+orderNo+",instCode=="+instCode+",FuncCode="+transOrderInfo.getFuncCode());
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
}
