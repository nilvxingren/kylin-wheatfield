package com.rkylin.wheatfield.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rkylin.wheatfield.exception.AccountException;
import com.rkylin.wheatfield.manager.FuncCodeManager;
import com.rkylin.wheatfield.manager.OrgInfoManager;
import com.rkylin.wheatfield.pojo.FuncCode;
import com.rkylin.wheatfield.pojo.OrgInfo;
import com.rkylin.wheatfield.service.TransactionService;

/**
 * 
 * @author  zhenpc@chanjet.com
 * @version 2015年3月31日 上午10:30:47
 */

@Service("transactionService")
public class TransactionServiceImpl implements TransactionService{

	@Autowired
	private FuncCodeManager funcCodeManager;
	@Autowired
	private OrgInfoManager orgInfoManager;
	
	@Override
	public void insertTest() {
		
		try {
			FuncCode funcCode=new FuncCode();
			funcCode.setFuncCode("testCode1234");
			funcCode.setFuncName("testName");
			
			OrgInfo orgInfo=new OrgInfo();
			orgInfo.setRootInstCd("tttt");
			orgInfo.setRootInstName("aaaaa");		
			
			funcCodeManager.saveFuncCode(funcCode);
			System.out.println("---------》2");
			orgInfoManager.saveOrgInfo(orgInfo);
			System.out.println("---------》3");
			throw new  AccountException("ERROR");
		} catch (AccountException e) {
			// TODO Auto-generated catch block
			throw new  AccountException("ERROR");
		}
	}

}
