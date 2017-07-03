package com.rkylin.wheatfield.manager;

import java.util.List;
import java.util.Map;

import com.rkylin.wheatfield.pojo.SettleBatchManage;
import com.rkylin.wheatfield.pojo.SettleBatchManageQuery;
import com.rkylin.wheatfield.pojo.TransOrderInfo;

public interface SettlementManager {

	List<TransOrderInfo> queryList(Map paraMap);
	
	List<Map<String,Object>> queryP2PList(Map paraMap);
	
	List<Map<String,Object>> queryP2PDetailList(Map paraMap);
	
	List<Map<String,Object>> queryPaymentList(Map paraMap);
	
	List<Map<String,Object>> selectGenerationRes(Map paraMap);

	List<Map<String,Object>> selectGenerationCnt(Map paraMap);
	
	List<TransOrderInfo> selectTransOrderInfoCh(Map paraMap);
	
	List<TransOrderInfo> selectTransOrderInfoD(Map paraMap);
	
	int updatetransstatus(Map paraMap);
	
	List<Map<String,Object>> selectWithhold(Map paraMap);

	List<SettleBatchManage> selectbatchno(SettleBatchManage paraMap);

	List<Map<String, Object>> selectDeduct(Map paraMap);
}
