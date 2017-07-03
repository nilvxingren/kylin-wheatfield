package test.wheatfield;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.rkylin.wheatfield.manager.AccountPasswordManager;
import com.rkylin.wheatfield.manager.ParameterInfoManager;
import com.rkylin.wheatfield.pojo.AccountPassword;
import com.rkylin.wheatfield.pojo.User;
import com.rkylin.wheatfield.service.AccountManageService;
import com.rkylin.wheatfield.service.IErrorResponseService;
import com.rkylin.wheatfield.service.NewAccountPasswordService;
import com.rkylin.wheatfield.service.impl.NewAccountPasswordServiceImpl;
import com.rkylin.wheatfield.utils.AccountValueCheckUtil;

public class NewAccountPasswordTest extends BaseJUnit4Test{
//	@Autowired
//	AccountPasswordManager accountPasswordManager;
//	@Autowired
//	IErrorResponseService ierrorResponseService;
//	@Autowired
//	AccountManageService accountManageService;
//	@Autowired
//	AccountValueCheckUtil accountValueCheckUtil;
//	@Autowired
//	ParameterInfoManager parameterInfoManager;
//	@Autowired
//	NewAccountPasswordService newAccountPasswordService;
	@Autowired
	NewAccountPasswordServiceImpl newAccountPasswordServiceImpl;
	
	@Autowired
	@Qualifier("newAccountPasswordService")
	private NewAccountPasswordService newAccountPasswordService;
	
	@Test
	public void MixErrorCountTest(){
		System.out.println("开是");
		User user = new User();
		user.constId = "M000001";
		user.productId = "P000002";
		user.userId= "sr_fn01";
		String result= newAccountPasswordServiceImpl.MixErrorCount(user);
		System.out.println("返回结果："+result);
	}
	

	public void operPassowordTest(){
		AccountPassword accountPassword = new AccountPassword();
		User user = new User();
		String opertype = "";
		user.constId = "M000001";
		user.productId = "P000002";
		user.userId= "sr_fn01";
		accountPassword.setPassword("aAaA!");
		accountPassword.setPasswordType("P");
		opertype = "insert";
//		String result= newAccountPasswordService.operPassword(accountPassword, opertype, user);
//		System.out.println("返回结果："+result);
	}

}
