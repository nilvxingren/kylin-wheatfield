package test.service;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.rkylin.wheatfield.api.FinanaceCompanyApi;
import com.rkylin.wheatfield.api.FinanacePersonApi;
import com.rkylin.wheatfield.bean.OpenAccountCompany;
import com.rkylin.wheatfield.bean.OpenAccountPerson;
import com.rkylin.wheatfield.common.DateUtils;
import com.rkylin.wheatfield.constant.Constants;
import com.rkylin.wheatfield.model.CommonResponse;

import test.wheatfield.BaseJUnit4Test;

public class FinanaceAccountServiceTest  extends BaseJUnit4Test{

    @Autowired
    private FinanacePersonApi finanacePersonApi;
    
    @Autowired
    private FinanaceCompanyApi finanaceCompanyApi;

    @Test
    public void accountoprComRealNameQueryBasicByDubboTest(){
        OpenAccountCompany company = new OpenAccountCompany();
//        company.setCompanyName("ARTHUR");
//        company.setCompanyShortName("KING ARTHUR");
//        company.setMcc("MCC");
//        company.setPost("721900");
//        company.setConnect("15051204040");
//        company.setAddress("建泰");
//        company.setBuslince("8098080");
//        company.setAcuntOpnLince("32423");
//        company.setCompanyCode("982734237894");
//        company.setCompanyType("2");
//        company.setTaxregCard1("44444444");
//        company.setTaxregCard2("333333333333");
//        company.setOrganCertificate("243242424");
//        company.setCorporateName("亚瑟");
//        company.setCorporateIdentity("340038503453");
//        company.setBusPlaceCtf("33232");
//        company.setLoanCard("23252352525");
//        company.setRemark("323542525");
//        company.setStartTime(DateUtils.getTheDayBefOrAfter(Constants.DATE_FORMAT_YYYYMMDD, -6));
//        company.setEndTime(DateUtils.getTheDayBefOrAfter(Constants.DATE_FORMAT_YYYYMMDD, 0));
        company.setUserId("150512040403");
        company.setRootInstCd("M000001");
        company.setProductId("P000002");
        company.setUserName("ARTHUR3");
//        company.setPageNum(2);
//        company.setPageSize(10);
        company.setAccountCode("RCB20170105161131001");//RPA2016123621412182001
//        company.setWhetherRealName("1");
        CommonResponse res = finanaceCompanyApi.accountoprComRealNameQueryBasicByDubbo(company);
        System.out.println(res);
    }
    
    @Test
    public void accountoprComRealNameUpByDubboTest(){
        OpenAccountCompany company = new OpenAccountCompany();
        company.setCompanyName("亚历山大大帝");
//        company.setCompanyShortName("KING ARTHUR");
//        company.setMcc("MCC");
//        company.setPost("721900");
//        company.setConnect("15051204040");
//        company.setAddress("建泰");
//        company.setBuslince("8098080");
//        company.setAcuntOpnLince("32423");
//        company.setCompanyCode("982734237894");
//        company.setCompanyType("2");
//        company.setTaxregCard1("44444444");
//        company.setTaxregCard2("333333333333");
//        company.setOrganCertificate("243242424");
//        company.setCorporateName("亚瑟");
//        company.setCorporateIdentity("340038503453");
//        company.setBusPlaceCtf("33232");
//        company.setLoanCard("23252352525");
//        company.setRemark("323542525");
        company.setUserId("150512040402");
        company.setRootInstCd("M000001");
        company.setProductId("P000002");
        company.setUserName("花豹");
//        company.setAccountCode("RCB20170105161131001");//RPA2016123621412182001
        company.setWhetherRealName("0");
        CommonResponse res = finanaceCompanyApi.accountoprComRealNameUpByDubbo(company);
        System.out.println(res);
    }
    
    @Test
    public void accountoprComRealNameOpenByDubboTest(){
        OpenAccountCompany company = new OpenAccountCompany();
        company.setCompanyName("ARTHUR");
        company.setCompanyShortName("KING ARTHUR");
        company.setMcc("MCC");
        company.setPost("721900");
        company.setConnect("150512040401");
        company.setAddress("建泰");
        company.setBuslince("80980806");
        company.setAcuntOpnLince("32423");
        company.setCompanyCode("982734237894");
        company.setCompanyType("2");
        company.setTaxregCard1("324");
        company.setTaxregCard2("342");
        company.setOrganCertificate("243242424");
        company.setCorporateName("亚瑟");
        company.setCorporateIdentity("340038503453");
        company.setBusPlaceCtf("33232");
        company.setLoanCard("23252352525");
        company.setRemark("323542525");
        company.setUserId("250512040400");
        company.setRootInstCd("M000002");
        company.setProductId("P000002");
        company.setUserName("ARTHUR3");
        company.setAccountCode("RCB"+DateUtils.getSysDateStr("yyyyMMddHHmmss")+"001");//RPA2016123621412182001
//        company.setWhetherRealName("1");
        company.setReferUserId("ReferUserId2");
        CommonResponse res = finanaceCompanyApi.accountoprComRealNameOpenByDubbo(company);
        System.out.println(res);
    }
    
    @Test
    public void accountoprPerRealNameQueryBasicByDubboTest(){
        OpenAccountPerson openAccountPerson = new OpenAccountPerson(); 
//        openAccountPerson.setPersonChnName("汪达尔人");
//        openAccountPerson.setPersonEngName("SA LA SEN");
//        openAccountPerson.setPersonType(1);
//        openAccountPerson.setPersonSex("1");
//        openAccountPerson.setBirthday("20161212");
//        openAccountPerson.setCertificateType("1");
//        openAccountPerson.setCertificateNumber("610321198704042116");
//        openAccountPerson.setMobileTel("8989898989");
//        openAccountPerson.setFixTel("010-09877777");
//        openAccountPerson.setEmail("AchillesWild@hotmail.com");
//        openAccountPerson.setPost("721304");
//        openAccountPerson.setAddress("陈仓区");
//        openAccountPerson.setRemark("反恐");
        openAccountPerson.setUserId("AchillesWild5");
        openAccountPerson.setRootInstCd("M000001");
        openAccountPerson.setStartTime(DateUtils.getTheDayBefOrAfter(Constants.DATE_FORMAT_YYYYMMDD, -2));
        openAccountPerson.setEndTime(DateUtils.getTheDayBefOrAfter(Constants.DATE_FORMAT_YYYYMMDD, 1));
//        openAccountPerson.setProductId("P000002");
        openAccountPerson.setUserName("FAITH");
//        openAccountPerson.setAccountCode("RPA20170105143140001");
//        openAccountPerson.setWhetherRealName("1");
//        openAccountPerson.setReferUserId("ReferUserId1");
        openAccountPerson.setPageNum(1);
        openAccountPerson.setPageSize(1);
        CommonResponse res = finanacePersonApi.accountoprPerRealNameQueryBasicByDubbo(openAccountPerson);
        System.out.println(res);
    }
    
    @Test
    public void accountoprPerRealNameUpByDubboTest(){
        OpenAccountPerson openAccountPerson = new OpenAccountPerson(); 
        openAccountPerson.setPersonChnName("斯堪的纳维亚");
        openAccountPerson.setPersonEngName("si kan wei na wei ya");
//        openAccountPerson.setPersonType(1);
//        openAccountPerson.setPersonSex("1");
//        openAccountPerson.setBirthday("20161227");
//        openAccountPerson.setCertificateType("1");
//        openAccountPerson.setCertificateNumber("610321198004042116");
//        openAccountPerson.setMobileTel("3333333");
//        openAccountPerson.setFixTel("010-4444444444");
//        openAccountPerson.setEmail("AchillesWild@hotmail.com");
//        openAccountPerson.setPost("721304");
//        openAccountPerson.setAddress("陈仓区");
//        openAccountPerson.setRemark("terrorist");
        openAccountPerson.setUserId("AchillesWild3");
        openAccountPerson.setRootInstCd("M000001");
        openAccountPerson.setProductId("P000002");
        openAccountPerson.setUserName("少校");
//        openAccountPerson.setAccountCode("RPA20170105143140001");
        openAccountPerson.setWhetherRealName("1");
        openAccountPerson.setReferUserId("AchillesWild33");
        CommonResponse res = finanacePersonApi.accountoprPerRealNameUpByDubbo(openAccountPerson);
        System.out.println(res);
    }
    
    @Test
    public void accountoprPerRealNameOpenByDubboTest(){
        OpenAccountPerson openAccountPerson = new OpenAccountPerson(); 
        openAccountPerson.setPersonChnName("萨拉森A");
        openAccountPerson.setPersonEngName("SA LA SEN");
        openAccountPerson.setPersonType(1);
        openAccountPerson.setPersonSex("1");
        openAccountPerson.setBirthday("20161212");
        openAccountPerson.setCertificateType("1");
        openAccountPerson.setCertificateNumber("6103211985040421161");
        openAccountPerson.setMobileTel("8989898989");
        openAccountPerson.setFixTel("010-09877777");
        openAccountPerson.setEmail("AchillesWild@hotmail.com");
        openAccountPerson.setPost("721304");
        openAccountPerson.setAddress("陈仓区");
        openAccountPerson.setRemark("反恐");
        openAccountPerson.setUserId("AchillesWild8");
        openAccountPerson.setRootInstCd("M000002");
        openAccountPerson.setProductId("P000002");
        openAccountPerson.setUserName("FAITH");
        openAccountPerson.setAccountCode("RPA"+DateUtils.getSysDateStr("yyyyMMddHHmmss")+"001");//RPA2016123621412182001
        openAccountPerson.setWhetherRealName("1");
        openAccountPerson.setReferUserId("ReferUserId3");
        CommonResponse res = finanacePersonApi.accountoprPerRealNameOpenByDubbo(openAccountPerson);
        System.out.println(res);
    }
}
