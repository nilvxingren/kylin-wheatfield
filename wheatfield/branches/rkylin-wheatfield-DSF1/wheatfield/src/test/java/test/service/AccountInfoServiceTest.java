package test.service;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import test.wheatfield.BaseJUnit4Test;
import com.rkylin.wheatfield.model.CommonResponse;
import com.rkylin.wheatfield.model.FinAccountResponse;
import com.rkylin.wheatfield.pojo.FinanaceEntry;
import com.rkylin.wheatfield.pojo.User;
import com.rkylin.wheatfield.service.AccountInfoService;

public class AccountInfoServiceTest   extends BaseJUnit4Test{

	@Autowired
	private AccountInfoService accountInfoService;
	
	@Test
	public void getFinAccountTest(){
		com.rkylin.wheatfield.bean.User user = new com.rkylin.wheatfield.bean.User();
		user.setInstCode("M000001");
		user.setUserId("18701514648");
		user.setType(new String[0]);;;
		FinAccountResponse res = accountInfoService.getFinAccount(user);
		System.out.println(res);
	}
	
	@Test
	public void openSubAccountTest(){
		User user = new User();
		user.userId="18701514648";
//		user.userId="18801438893";
//		user.constId ="M000001";
		user.constId ="M000002";
//		user.productId ="P000002";
		user.productId ="P000003";
//		user.referUserId="0";
		user.referUserId="M000002";
		user.creditType="101";
		FinanaceEntry fin = new FinanaceEntry();
		fin.setPaymentAmount(0L);
		fin.setReferId("8989800");
		CommonResponse res = accountInfoService.openSubAccount(user,fin);
		System.out.println(res);
	}
}
