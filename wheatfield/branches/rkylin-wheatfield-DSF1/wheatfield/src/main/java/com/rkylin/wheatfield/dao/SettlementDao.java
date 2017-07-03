package com.rkylin.wheatfield.dao;

import java.util.List;
import java.util.Map;

import com.rkylin.wheatfield.pojo.SettleBatchManage;
import com.rkylin.wheatfield.pojo.SettleBatchManageQuery;
import com.rkylin.wheatfield.pojo.TransOrderInfo;
import com.rkylin.wheatfield.pojo.TransOrderInfoQuery;

public interface SettlementDao {

	List<TransOrderInfo> selectTransOrderInfoByDate(Map paraMap);
	
	List<Map<String,Object>> selectDedtInfoByDate(Map paraMap);
	
	List<Map<String,Object>> selectDedtDetailByDate(Map paraMap);
	
	List<Map<String,Object>> selectPaymentByDate(Map paraMap);
	
	List<Map<String,Object>> selectGenerationRes(Map paraMap);
	
	List<Map<String,Object>> selectGenerationCnt(Map paraMap);
	
	List<TransOrderInfo> selectTransOrderInfoCh(Map paraMap);
	
	List<TransOrderInfo> selectTransOrderInfoD(Map paraMap);

	int updatetransstatus(Map paraMap);
	
	List<Map<String,Object>> selectWithhold(Map paraMap);

	List<SettleBatchManage> selectbatchno(SettleBatchManage paraMap);

	List<Map<String, Object>> selectDeduct(Map paraMap);

}
