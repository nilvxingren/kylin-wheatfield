/*
 * Powered By chanjetpay-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2015
 */

package com.rkylin.wheatfield.dao;

import java.util.List;
import java.util.Map;

import com.rkylin.wheatfield.bean.OrderQuery;
import com.rkylin.wheatfield.pojo.GenerationPayment;
import com.rkylin.wheatfield.pojo.TransOrderInfo;
import com.rkylin.wheatfield.pojo.TransOrderInfoQuery;

public interface TransOrderInfoDao {
	public List<TransOrderInfo> selectByGenId(List<GenerationPayment> list);
	
	int countByExample(TransOrderInfoQuery example);
	
	int deleteByExample(TransOrderInfoQuery example);
	
	int deleteByPrimaryKey(Integer id);
	
	int insert(TransOrderInfo record);
	
	int insertSelective(TransOrderInfo record);
	
	public void insertSelectiveBatch(List<TransOrderInfo> list);
	
	List<TransOrderInfo> selectByExample(TransOrderInfoQuery example);
	
	List<TransOrderInfo> selectByExampleGroup(TransOrderInfoQuery example);
	
	List<TransOrderInfo> selectByExampleGroupByInter(TransOrderInfoQuery example);
	
	TransOrderInfo selectByPrimaryKey(Integer id);
	
	int updateByPrimaryKeySelective(TransOrderInfo record);
	
	int updateByPrimaryKey(TransOrderInfo record);

	List<TransOrderInfo> selectList(TransOrderInfoQuery example);
	
	int batchUpdate( List<?> list);
	
	public void batchUpdateOrderInfor(List<TransOrderInfo> list);
	
	List<TransOrderInfo> selectTransOrderInfos(TransOrderInfoQuery query);
	
	List<TransOrderInfo> selectTransOrdersAndSumId(TransOrderInfoQuery query);
	
	/**
	 * 退票查询订单
	 * @param query
	 * @return
	 */
	public List<TransOrderInfo> selectTransOrdersRefund(TransOrderInfoQuery query);
	
	public void updateStatusByOrderNoAndMerCode(TransOrderInfo order);
	
	public void batchUpdateStatusByOrderNoAndMerCode(List<TransOrderInfo> list);
	
	List<Map<String, Object>> selectSummaryInfo(Map summaryMap);
	public List<TransOrderInfo> queryByReqNo(OrderQuery query);
	public List<TransOrderInfo> selectByOrderNoAndInstCode(List<Map<String,String>> mapList);
	
	public List<TransOrderInfo> selectByOrderNosAndInstCode(TransOrderInfoQuery query);
	
	/**
	 * 查询终态数据
	 * Discription:
	 * @param query
	 * @return List<TransOrderInfo>
	 * @author Achilles
	 * @since 2016年8月2日
	 */
	public List<TransOrderInfo> selectFinalState(TransOrderInfoQuery query);
	
	/*
	 * 查询非终态个数
	 */
	public int selectNotFinalStateCount(TransOrderInfoQuery query);
	
	public List<TransOrderInfo> selectByCon(TransOrderInfoQuery query);
	
	public int updateByPrimaryKeysSelective(TransOrderInfoQuery record);
}
