/*
 * Powered By rkylin-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2015
 */

package test.wheatfield;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.rkylin.wheatfield.manager.GenerationPaymentManager;
import com.rkylin.wheatfield.pojo.GenerationPayment;
import com.rkylin.wheatfield.pojo.GenerationPaymentQuery;

public class GenerationPaymentManagerTest extends BaseJUnit4Test {
	
	@Autowired
	@Qualifier("generationPaymentManager")
	private GenerationPaymentManager generationPaymentManager;


	public void testNewGenerationPayment() {
		GenerationPayment GenerationPayment = new GenerationPayment();
		generationPaymentManager.saveGenerationPayment(GenerationPayment);
	}


	public void testUpdateGenerationPayment(){
		GenerationPayment GenerationPayment = new GenerationPayment();
//		GenerationPayment.setGeneId((int) 2l);
		generationPaymentManager.saveGenerationPayment(GenerationPayment);
	}
	

	public void testDeleteGenerationPayment(){
		generationPaymentManager.deleteGenerationPaymentById(99L);
	}
	

	public void testDeleteGenerationPaymentByQuery(){
		GenerationPaymentQuery query = new GenerationPaymentQuery();
		generationPaymentManager.deleteGenerationPayment(query);
	}


	public void testFindGenerationPaymentById(){
		GenerationPaymentQuery query = new GenerationPaymentQuery();
		int size = generationPaymentManager.queryList(query).size();
		System.out.println(size);
	}
}
