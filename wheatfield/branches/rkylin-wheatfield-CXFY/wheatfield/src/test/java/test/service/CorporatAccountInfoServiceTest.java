/**
 * @File name : AccountManageServiceImplTest.java
 * @Package : test.service
 * @Description : TODO(用一句话描述该文件做什么)
 * @Creator : Administrator
 * @CreateTime : 2015年9月7日 下午6:11:50
 * @Version : 1.0
 * @Update records:
 *      1.1 2015年9月7日 by Administrator: 
 *      1.0 2015年9月7日 by Administrator: Created 
 * All rights served : FENGNIAN Corporation
 */
package test.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.rkylin.wheatfield.pojo.CorporatAccountInfo;
import com.rkylin.wheatfield.response.ErrorResponse;
import com.rkylin.wheatfield.service.CorporatAccountInfoService;

import test.wheatfield.BaseJUnit4Test;

public class CorporatAccountInfoServiceTest  extends BaseJUnit4Test{
	@Autowired
	private CorporatAccountInfoService corporatAccountInfoService;
	
	@Test
	public void testInsertCorporatAccountInfo() {
		Map<String, String[]> paramMap = new HashMap<String,String[]>();
		String[] constidSum = new String[1];
		constidSum[0] = "M000003";
		paramMap.put("constid", constidSum);

		String arrayList = "<signinfodetails>"+
				"<signinfodetail>"+
				"<rootInstCd>M00000X6217710702946088</rootInstCd>"+
				"<accountNumber>3333388</accountNumber>"+
				"<accountRealName>6217710702946088</accountRealName>"+
				"<currency>CNY</currency>"+
				"<bankHead>302</bankHead>"+
				"<bankHeadName>北京</bankHeadName>"+
				"<bankBranch>北京</bankBranch>"+
				"<bankBranchName>中信银行</bankBranchName>"+
				"<bankProvince>30219</bankProvince>"+
				"<bankCity>3333</bankCity>"+
				"<certificateType>3333</certificateType>"+
				"<certificateNumber>444444</certificateNumber>"+
				"</signinfodetail>"+
				"<signinfodetail>"+
				"<rootInstCd>M00000X60722342</rootInstCd>"+
				"<accountNumber>123101213212</accountNumber>"+
				"<accountRealName>62172342946088</accountRealName>"+
				"<currency>CNY</currency>"+
				"<bankHead>302</bankHead>"+
				"<bankHeadName>北asd京</bankHeadName>"+
				"<bankBranch>北asd</bankBranch>"+
				"<bankBranchName>中asdf信银行</bankBranchName>"+
				"<bankProvince>302</bankProvince>"+
				"<bankCity>3333</bankCity>"+
				"<certificateType>3333</certificateType>"+
				"<certificateNumber>23233</certificateNumber>"+
				"</signinfodetail>"+
				"</signinfodetails>";
		
		String[] listSum = new String[1];
		listSum[0] = arrayList;
		paramMap.put("corporateaccountarray", listSum);
		
		ErrorResponse errorResponse = corporatAccountInfoService.insertCorporatAccountInfo(paramMap);
		if(errorResponse != null){
			System.out.println(errorResponse.getMsg());
		}
	}

	
	@Test
	public void testSelCorporatAccountInfo(){
		Map<String, String[]> paramMap = new HashMap<String,String[]>();
		String[] corporateaccountidSum = new String[1];
		corporateaccountidSum[0] = "16";
		paramMap.put("corporateaccountid", corporateaccountidSum);
		
		String[] createTimeFromSum = new String[1];
		createTimeFromSum[0] = "2015-09-06 15:33:47";
		paramMap.put("createdtimefrom", createTimeFromSum);
		
		List<CorporatAccountInfo> corporatAccountInfo = corporatAccountInfoService.selCorporatAccountInfo(paramMap);
		if(corporatAccountInfo != null && !corporatAccountInfo.isEmpty()){
			System.out.println(corporatAccountInfo.size() + "   ----------------------");
		}else{
			System.out.println("0    ----------------------------");
		}
	}
	
	@Test
	public void testUpdateCorporatAccountInfo(){
		Map<String, String[]> paramMap = new HashMap<String,String[]>();
		String[] corporateaccountidSum = new String[1];
		corporateaccountidSum[0] = "16";
		paramMap.put("corporateaccountid", corporateaccountidSum);
		
		String[] accountnumberSum = new String[1];
		accountnumberSum[0] = "88888888888888888";
		paramMap.put("accountnumber", accountnumberSum);
		
		String[] accountRealName = new String[1];
		accountRealName[0] = " 真实Realname";
		paramMap.put("accountrealname", accountRealName);
		
		String[] currency = new String[1];
		currency[0] = "CNY";
		paramMap.put("currency", currency);
		
		String[] bankhead = new String[1];
		bankhead[0] = "ICBC";
		paramMap.put("bankhead", bankhead);
		
		ErrorResponse errorResponse =  corporatAccountInfoService.updateCorporatAccountInfo(paramMap);
		System.out.println(errorResponse);
		System.out.println(errorResponse.isIs_success());
		System.out.println(errorResponse.getMsg());
	}
}
