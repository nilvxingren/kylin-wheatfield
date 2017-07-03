package com.rkylin.wheatfield.service.impl;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.rkylin.util.bean.BeanMapper;
import com.rkylin.wheatfield.api.TransOrderDubboService;
import com.rkylin.wheatfield.bean.OrderQuery;
import com.rkylin.wheatfield.bean.TransOrderStatusUpdate;
import com.rkylin.wheatfield.dao.TransOrderInfoDao;
import com.rkylin.wheatfield.model.CommonResponse;
import com.rkylin.wheatfield.model.TransOrderInfosResponse;
import com.rkylin.wheatfield.pojo.TransOrderInfo;
import com.rkylin.wheatfield.pojo.TransOrderInfoQuery;
import com.rkylin.wheatfield.service.TransOrderService;
import com.rkylin.wheatfield.utils.BeanUtil;
import com.rkylin.wheatfield.utils.CodeEnum;
import com.rkylin.wheatfield.utils.CommUtil;

@Service("transOrderService")
public class TransOrderServiceImpl implements TransOrderService,TransOrderDubboService{
	private static Logger logger = LoggerFactory.getLogger(TransOrderServiceImpl.class);
	
	@Autowired
	private TransOrderInfoDao transOrderInfoDao;
	
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
        CommUtil commUtil = new CommUtil();
        if ((!commUtil.isEmpty(transOrder.getRequestNoSet())||!commUtil.isEmpty(transOrder.getOrderPackageNo())||
                !commUtil.isEmpty(transOrder.getOrderNoSet()))
            &&commUtil.isEmpty(transOrder.getMerchantCode())){
            logger.info("订单状态修改 requestId为空时,请求号/订单包号/订单号不为空时,机构号必输!");
            res.setMsg("requestId为空时,请求号/订单包号/订单号不为空时,机构号必输!"); 
            return res;
        }
        if (commUtil.isEmpty(transOrder.getMerchantCode())&&commUtil.isEmpty(transOrder.getRequestIdSet())){
            logger.info("订单状态修改 机构号和requestId不能同时为空!");
            res.setMsg("机构号和requestId不能同时为空!"); 
            return res;
        }
        if (commUtil.isEmpty(transOrder.getRequestIdSet())&&commUtil.isEmpty(transOrder.getRequestNoSet())
                &&commUtil.isEmpty(transOrder.getOrderPackageNo())&&commUtil.isEmpty(transOrder.getOrderNoSet())){
            logger.info("订单状态修改 requestId/请求号/订单包号/订单号不能同时为空!");
            res.setMsg("requestId/请求号/订单包号/订单号不能同时为空!"); 
            return res;
        }
        if (commUtil.isEmpty(transOrder.getStatus())){
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
}
