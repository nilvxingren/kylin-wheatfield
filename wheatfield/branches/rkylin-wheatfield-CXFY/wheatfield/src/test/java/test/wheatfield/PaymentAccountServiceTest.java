package test.wheatfield;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.rkylin.wheatfield.enumtype.UserTypeEnum;
import com.rkylin.wheatfield.model.BalanceResponse;
import com.rkylin.wheatfield.pojo.Balance;
import com.rkylin.wheatfield.pojo.User;
import com.rkylin.wheatfield.service.PaymentAccountService;

public class PaymentAccountServiceTest extends BaseJUnit4Test{

	
	@Autowired
	private PaymentAccountService paymentAccountService;
	
	@Test
	public void getUserBalanceTest(){
		User user=new User();
//		user.userType=UserTypeEnum.toEnum("10001");
		user.userId="18701514648";
		user.productId="P000002";
		user.constId="M000001";
		BalanceResponse balance= paymentAccountService.getUserBalance(user, "","2");
		System.out.println(balance);
	}
	
	@Test
	public void getBalanceTest(){
		User user=new User();
		user.userType=UserTypeEnum.toEnum("10001");
		user.userId="dsf";
		user.productId="TA001";
		Balance balance= paymentAccountService.getBalance(user, "");
		
		
		//UserTypeEnum.USER.getCategory();
		
		System.out.println(UserTypeEnum.USER.getCategory()+balance.getAmount());
	}
}
