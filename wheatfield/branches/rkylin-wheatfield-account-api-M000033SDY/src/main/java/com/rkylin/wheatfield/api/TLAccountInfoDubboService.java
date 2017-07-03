package com.rkylin.wheatfield.api;

import com.rkylin.wheatfield.response.TLAccounInfoResponse;

public interface TLAccountInfoDubboService {
	
	/**
	 * @param rootInstCd 机构号  必传
	 * @param acctNo 子账户号
	 *  
	 */
	public TLAccounInfoResponse queryAccountInfo(String rootInstCd,String acctNo);

}
