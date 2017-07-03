package test.wheatfield;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class JRDCurrentPurchaseTest {
	public static void main(String[] args) {
		test();
	}
	
	public static void test(){
		
		
		URL url=null;
		try {
			//地址
			String str = "http://127.0.0.1:8081/wheatfield/ropapi?"
			           +"funccode=3001&"
					   +"merchantcode=M000005&"
			           +"userid=jrd-base-mawanxia&"
					   +"productid=P000011&"
			           +"intermerchantcode=jrd-base-mawanxia&"
			           +"intoproductid=P000024&" 
			           +"amount=12&"
			           +"orderno=201510291236300014&"
			           +"orderdate=2015-10-28%2013%3A09%3A30&"
					   +"requesttime=2015-10-28%2013%3A09%3A30&"
					   +"status=1&"
					   +"busitypeid=6&"
					   +"useripaddress=127.0.0.1&"
					   +"method=ruixue.wheatfield.demand.purchase&"
					   +"app_key=857F5C39-884C-470F-9FA7-DCABFD558ABE&format=xml&"
			           +"timestamp=2015-10-28%2013%3A11%3A09&"
					   +"session=800e236c-2fdf-4326-be7b-884a68c5ff85&sign=375D6C7165DED8A07F7563B3F4F966AF";
			
			//url = new URL("http://127.0.0.1:8080/wheatfield/ropapi?amount=5&userid=jrd-base-mawanxia&funccode=3001&intermerchantcode=jrd-tran-mawanxia&merchantcode=M000005&orderamount=2&ordercount=1&orderdate=2015-10-20%2013%3A39%3A34&orderno=201511029339340004&requesttime=2015-10-20%2013%3A39%3A34&status=1&userfee=0&productid=P000011&intoproductid=P000022&useripaddress=127.0.0.1&method=ruixue.wheatfield.user.transfer.new&app_key=857F5C39-884C-470F-9FA7-DCABFD558ABE&format=xml&timestamp=2015-10-20%2014%3A25%3A56&session=79b17793-c296-4e4a-8469-fe47dbad5ff2&sign=9269D93E25E793E7434CC9F6BFD53709");
		    url = new URL(str);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		BufferedReader r=null;
		try {
			URLConnection con=url.openConnection();
			r = new BufferedReader(new InputStreamReader(con.getInputStream(),"UTF-8"));
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		String str="";
		do{
			try {
				str=r.readLine();
			} catch (IOException e) {
				e.printStackTrace();
			}
			System.out.println(str);
			
		}while(str!=null);

		try {
			r.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
