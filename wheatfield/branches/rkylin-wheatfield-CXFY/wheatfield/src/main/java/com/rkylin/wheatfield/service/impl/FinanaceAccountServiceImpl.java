/**
*
*/
package com.rkylin.wheatfield.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.rkylin.wheatfield.api.FinanaceAccountService;
import com.rkylin.wheatfield.dao.FinanaceAccountDao;
import com.rkylin.wheatfield.model.CommonResponse;
import com.rkylin.wheatfield.pojo.FinanaceAccount;
import com.rkylin.wheatfield.pojo.FinanaceAccountQuery;
import com.rkylin.wheatfield.utils.CodeEnum;

/**
 * Description:	   
 * Date:          2015年12月8日 下午2:04:39 
 * @author        sun guoxing
 * @version       1.0 
 */
public class FinanaceAccountServiceImpl implements FinanaceAccountService{
	@Autowired
	FinanaceAccountDao finanaceAccountDao;
	
	private static Logger logger = LoggerFactory.getLogger(FinanaceEntryServiceImpl.class);
	
	/**
	 * 修改账户状态
	 * @param userId 用户id
	 * @param instCode 机构号
	 * @param productId 产品号
	 * @param status 状态值
	 * @return
	 */
	public CommonResponse updateFinAccountStatus(String userId,String instCode,String productId,String status){
		logger.info("修改账户状态：userId=" + userId + ",instCode=" + instCode+",productId="+productId+",status="+status);
		CommonResponse res = new CommonResponse();
		if (userId==null||"".equals(userId.trim())||instCode==null||"".equals(instCode.trim())||productId==null||"".equals(productId.trim())||status==null||"".equals(status.trim())) {
			res.setCode(CodeEnum.ERR_PARAM_NULL.getCode());
			res.setMsg(CodeEnum.ERR_PARAM_NULL.getMessage());
			return res;
		}
		FinanaceAccountQuery query = new FinanaceAccountQuery();
		query.setAccountRelateId(userId);
		query.setRootInstCd(instCode);
		query.setGroupManage(productId);
		List<FinanaceAccount> finAccList = finanaceAccountDao.selectByExample(query);
		logger.info("修改账户状态  查出的资金账户信息个数="+finAccList.size());
		if (finAccList.size()==0) {
			res.setCode(CodeEnum.FAILURE.getCode());
			res.setMsg("没有查出资金账户信息");
			return res;
		}
		FinanaceAccount finAccount = finAccList.get(0);
		finAccount.setStatusId(status);
		try {
			finanaceAccountDao.updateByPrimaryKey(finAccount);
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("修改账户状态  修改数据库异常");
			res.setCode(CodeEnum.FAILURE.getCode());
			res.setMsg("修改数据库异常，请检查传入的状态值");
			return res;
		}
		return res;
	}
	/**
	 * 
	* @Description:根据id修改账户状态
	*
	* @param finAccountId inanaceAccount表的id
	* @param status 要更改成的状态
	* @return
	 */
	@Override
	public int updateStatusById(String finAccountId, String status){
		logger.info("----------------------------------根据id修改账户状态开始，入参：finAccountId=" + finAccountId + ",status=" + status);
		
		if(null == finAccountId || "".equals(finAccountId)){
			logger.info("finAccountId不能为空");
			return -1;
		}
		if(null == status || "".equals(status)){
			logger.info("status不能为空");
			return -1;
		}
		FinanaceAccount fa = new FinanaceAccount();
		fa.setFinAccountId(finAccountId);
		fa.setStatusId(status);
		
		logger.info("----------------------------------根据id修改账户状态结束");
		return finanaceAccountDao.updateByPrimaryKeySelective(fa);
		
	}

}
