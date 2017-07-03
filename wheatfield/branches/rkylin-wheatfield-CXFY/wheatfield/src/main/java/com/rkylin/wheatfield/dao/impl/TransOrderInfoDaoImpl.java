/*
 * Powered By chanjetpay-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2015
 */

package com.rkylin.wheatfield.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.rkylin.database.BaseDao;
import com.rkylin.wheatfield.bean.OrderQuery;
import com.rkylin.wheatfield.dao.TransOrderInfoDao;
import com.rkylin.wheatfield.pojo.GenerationPayment;
import com.rkylin.wheatfield.pojo.TransOrderInfo;
import com.rkylin.wheatfield.pojo.TransOrderInfoPage;
import com.rkylin.wheatfield.pojo.TransOrderInfoQuery;

@Repository("transOrderInfoDao")
public class TransOrderInfoDaoImpl extends BaseDao implements TransOrderInfoDao {
	
	@Override
	public int countByExample(TransOrderInfoQuery example) {
		return super.getSqlSession().selectOne("TransOrderInfoMapper.countByExample", example);
	}
	
	@Override
	public int deleteByExample(TransOrderInfoQuery example) {
		return super.getSqlSession().delete("TransOrderInfoMapper.deleteByExample", example);
	}
	
	@Override
	public int deleteByPrimaryKey(Integer id) {
		return super.getSqlSession().delete("TransOrderInfoMapper.deleteByPrimaryKey", id);
	}
	
	@Override
	public int insert(TransOrderInfo record) {
		return super.getSqlSession().insert("TransOrderInfoMapper.insert", record);
	}
	
	@Override
	public int insertSelective(TransOrderInfo record) {
		return super.getSqlSession().insert("TransOrderInfoMapper.insertSelective", record);
	}
	
	@Override
	public List<TransOrderInfo> selectByExample(TransOrderInfoQuery example) {
		return super.getSqlSession().selectList("TransOrderInfoMapper.selectByExample", example);
	}
	
    @Override
    public void insertSelectiveBatch(List<TransOrderInfo> list) {
        super.getSqlSession().insert("TransOrderInfoMapper.insertSelectiveBatch", list);
    }	
	
	@Override
	public TransOrderInfo selectByPrimaryKey(Integer id) {
		return super.getSqlSession().selectOne("TransOrderInfoMapper.selectByPrimaryKey", id);
	}
	
	@Override
	public int updateByPrimaryKeySelective(TransOrderInfo record) {
		if (record==null) {
			return 0;
		}
		record.setUpdatedTime(null);
		return super.getSqlSession().update("TransOrderInfoMapper.updateByPrimaryKeySelective", record);
	}
	
	@Override
	public int updateByPrimaryKey(TransOrderInfo record) {
		if (record==null) {
			return 0;
		}
		record.setUpdatedTime(null);
		return super.getSqlSession().update("TransOrderInfoMapper.updateByPrimaryKey", record);
	}

	@Override
	public List<TransOrderInfo> selectByExampleGroup(TransOrderInfoQuery example) {
		return super.getSqlSession().selectList("TransOrderInfoMapper.selectTransOrderGroup", example);
	}
	@Override
	public List<TransOrderInfo> selectByExampleGroupByInter(TransOrderInfoQuery example) {
		return super.getSqlSession().selectList("TransOrderInfoMapper.selectTransOrderGroupByInter", example);
	}
	@Override
	public List<TransOrderInfo> selectList(TransOrderInfoQuery example) {
		return super.getSqlSession().selectList("TransOrderInfoMapper.selectTransOrderList", example);
	}
	public List<TransOrderInfo> selectByGenId(List<GenerationPayment> list) {
		return super.getSqlSession().selectList("TransOrderInfoMapper.selectByGenId", list);
	}
	@Override
	public int batchUpdate( List<?> list){
		return super.batchUpdate("TransOrderInfoMapper.updateByOrderNo", list);
	}

	public List<TransOrderInfo> selectTransOrdersRefund(TransOrderInfoQuery query) {
		return super.getSqlSession().selectList("TransOrderInfoMapper.selectTransOrdersRefund", query);
	}
	
	@Override
	public List<TransOrderInfo> selectTransOrderInfos(TransOrderInfoQuery query) {
		return super.getSqlSession().selectList("TransOrderInfoMapper.selectTransOrders", query);
	}
	
	@Override
	public List<TransOrderInfo> selectTransOrdersAndSumId(TransOrderInfoQuery query) {
		return super.getSqlSession().selectList("TransOrderInfoMapper.selectTransOrdersAndSumId", query);
	}

	@Override
	public void batchUpdateOrderInfor(List<TransOrderInfo> list) {
		super.getSqlSession().update("TransOrderInfoMapper.updateByOrderNo", list);
	}
	
	public void updateStatusByOrderNoAndMerCode(TransOrderInfo order) {
		super.getSqlSession().update("TransOrderInfoMapper.updateStatusByOrderNoAndMerCode", order);
	}
	
	public void batchUpdateStatusByOrderNoAndMerCode(List<TransOrderInfo> list) {
	    if (list==null||list.size()==0) {
           return; 
        }
		super.getSqlSession().update("TransOrderInfoMapper.batchUpdateStatusByOrderNoAndMerCode", list);
	}

	@Override
	public List<Map<String, Object>> selectSummaryInfo(Map summaryMap) {
		return super.getSqlSession().selectList("TransOrderInfoMapper.getSummary", summaryMap);
	}
	@Override
	public List<TransOrderInfo> queryByReqNo(OrderQuery query) {
		return super.getSqlSession().selectList("TransOrderInfoMapper.selectByReqNo", query);
	}
	public List<TransOrderInfo> selectByOrderNoAndInstCode(List<Map<String,String>> mapList) {
		if (mapList==null ||mapList.size()==0) {
			return new ArrayList<TransOrderInfo>();
		}
		return super.getSqlSession().selectList("TransOrderInfoMapper.selectByOrderNoAndInstCode", mapList);
	}
    public List<TransOrderInfo> selectByOrderNosAndInstCode(TransOrderInfoQuery query) {
        if (query.getMerchantCode()==null||"".equals(query.getMerchantCode())||query.getOrderNoSet()==null||query.getOrderNoSet().size()==0) {
            return new ArrayList<TransOrderInfo>();
        }
        return super.getSqlSession().selectList("TransOrderInfoMapper.selectByOrderNosAndInstCode", query);
    }	

    @Override
    public List<TransOrderInfo> selectFinalState(TransOrderInfoQuery query) {
        return super.getSqlSession().selectList("TransOrderInfoMapper.selectFinalState", query);
    }

    @Override
    public int selectNotFinalStateCount(TransOrderInfoQuery query) {
        return super.getSqlSession().selectOne("TransOrderInfoMapper.selectNotFinalStateCount", query);
    }
	
    @Override
    public List<TransOrderInfo> selectByCon(TransOrderInfoQuery query) {
        return super.getSqlSession().selectList("TransOrderInfoMapper.selectByCon", query);
    }
    
    @Override
    public int updateByPrimaryKeysSelective(TransOrderInfoQuery record) {
        if (record==null||record.getRequestIdSet()==null||record.getRequestIdSet().size()==0) {
            return 0;
        }
        record.setUpdatedTime(null);
        return super.getSqlSession().update("TransOrderInfoMapper.updateByPrimaryKeysSelective", record);
    }
    
    @Override
    public List<TransOrderInfo> selTransOrderInfo(TransOrderInfoPage pageQuery) {
        return super.getSqlSession().selectList("TransOrderInfoMapper.selTransOrderInfo", pageQuery);
    }
    
    @Override
    public int selTransOrderInfoCount(TransOrderInfoPage pageQuery) {
        return super.getSqlSession().selectOne("TransOrderInfoMapper.selTransOrderInfoCount", pageQuery);
    }
}
