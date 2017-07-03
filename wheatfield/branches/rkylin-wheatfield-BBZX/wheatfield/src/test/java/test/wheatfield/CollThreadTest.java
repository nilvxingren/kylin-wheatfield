package test.wheatfield;

import java.io.IOException;
import java.util.Random;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;

public class CollThreadTest{

	public static void creatPerson() {
		for (int i = 0; i < 101; i++) {
			Thread t=new Thread(new Runnable() {				
				@Override
				public void run() {
					t();
				}				
			});
			t.start();
		}
	}

	public static void main(String[] args) {
		creatPerson();
	}
	
	public static void t() {
		String url ="http://localhost:8080/wheatfield/ropapi?"
				+ "amount=1&"
				+ "userid=18701514648&"
				+ "funccode=40131&"
				+ "merchantcode=M000001&"
				+ "orderamount=1&"
				+ "ordercount=1&"
				+ "orderdate=2015-11-20%2015%3A52%3A34&"
				+ "orderno="+getGenerateId()+"AA&"
				+ "productid=P000002&"
				+ "status=1&"
				+ "userfee=0&"
				+ "useripaddress=127.0.0.1&"
				+ "method=ruixue.wheatfield.user.collection&"
				+ "format=json&"
				+ "timestamp=2015-11-20%2015%3A52%3A53&"
				+ "requesttime=2015-11-20%2015%3A52%3A53&"
				+ "remark=%E5%AE%9E%E6%97%B6%E4%BB%A3%E6%94%B6%E5%B9%B6%E5%8F%91101&";
		
		HttpClient client = new HttpClient();
		HttpMethod method = new GetMethod(url);
		try {
			client.executeMethod(method);
		} catch (HttpException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static String getGenerateId(){
		Random random = new Random();
		int randomNumber =  random.nextInt(99999)%(99999-1) + 1;
		String id = ""+ System.currentTimeMillis()+Thread.currentThread().getId()+randomNumber;
		return id;
	}
	
}
