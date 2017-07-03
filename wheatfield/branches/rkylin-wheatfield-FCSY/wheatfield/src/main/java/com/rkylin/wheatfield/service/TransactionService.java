package com.rkylin.wheatfield.service;

import org.springframework.transaction.annotation.Transactional;

/**
 * 
 * @author  zhenpc@chanjet.com
 * @version 2015年3月31日 上午10:29:07
 */

public interface TransactionService {
	
	@Transactional
	public void insertTest();

}
