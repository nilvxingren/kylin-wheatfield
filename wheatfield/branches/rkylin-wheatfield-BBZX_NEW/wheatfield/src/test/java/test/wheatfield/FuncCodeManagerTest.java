/*
 * Powered By chanjetpay-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2015
 */

package test.wheatfield;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.rkylin.wheatfield.enumtype.UserTypeEnum;
import com.rkylin.wheatfield.manager.FuncCodeManager;
import com.rkylin.wheatfield.pojo.FuncCode;
import com.rkylin.wheatfield.pojo.FuncCodeQuery;
import com.rkylin.wheatfield.pojo.User;

public class FuncCodeManagerTest extends BaseJUnit4Test {
	
	@Autowired
	private FuncCodeManager funcCodeManager;
	
	
	@Test
	@Transactional	
	public void testTrans(){
		for (int i = 0; i < 2; i++) {
			FuncCode funcCode=newFuncCode("testCode"+i, "testName"+i);
			if(i!=0){
				throw new RuntimeException("ERROR");
			}
			funcCodeManager.saveFuncCode(funcCode);
		}
	}
	
	private FuncCode newFuncCode(String funcCodeStr,String funcName){
		FuncCode funcCode=new FuncCode();
		funcCode.setFuncCode(funcCodeStr);
		funcCode.setFuncName(funcName);		
		return funcCode;
	}
	
	
	
	public void getBalanceTest(){
		User user=new User();
		user.userType=UserTypeEnum.toEnum("10001");
		user.userId="dsf";
		user.productId="TA001";
//		Balance balance= paymentAccountService.getBalance(user, "");
		
		
		//UserTypeEnum.USER.getCategory();
		
//		System.out.println(UserTypeEnum.USER.getCategory()+balance.getAmount());
	}

	public void testNewFuncCode() {
		FuncCode FuncCode = new FuncCode();
//		FuncCode.setAccountingNo("1111111");
		funcCodeManager.saveFuncCode(FuncCode);
	}


	public void testUpdateFuncCode(){
		FuncCode FuncCode = new FuncCode();
//		FuncCode.setId(2l);
//		FuncCode.setAccountingNo("222z");
		funcCodeManager.saveFuncCode(FuncCode);
	}
	

	public void testDeleteFuncCode(){
		funcCodeManager.deleteFuncCodeById(99L);
	}
	

	public void testDeleteFuncCodeByQuery(){
		FuncCodeQuery query = new FuncCodeQuery();
//		query.setAccountingNo("1111111");
		funcCodeManager.deleteFuncCode(query);
	}


	public void testFindFuncCodeById(){
		FuncCodeQuery query = new FuncCodeQuery();
//		query.setAccountingNo("1111111");
		int size = funcCodeManager.queryList(query).size();
		System.out.println(size);
	}
}
