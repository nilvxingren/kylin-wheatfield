package com.rkylin.wheatfield.service.impl;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rkylin.wheatfield.api.TransOrderDubboService;
import com.rkylin.wheatfield.bean.OrderQuery;
import com.rkylin.wheatfield.dao.TransOrderInfoDao;
import com.rkylin.wheatfield.model.TransOrderInfosResponse;
import com.rkylin.wheatfield.pojo.TransOrderInfo;
import com.rkylin.wheatfield.service.TransOrderService;
import com.rkylin.wheatfield.utils.CodeEnum;

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
}
