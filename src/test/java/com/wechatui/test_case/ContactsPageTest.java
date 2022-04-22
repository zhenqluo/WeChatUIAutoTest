package com.wechatui.test_case;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.wechatui.base.TestCaseBase;
import com.wechatui.model.AssertModel;
import com.wechatui.model.CaseObjectModel;
import com.wechatui.page_object.MainPage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertAll;

/**
 * @author luo
 * @create 2022/4/17 下午12:59
 */
public class ContactsPageTest extends TestCaseBase{
    //该WebDriver使用static修饰是因为在@BeforeAll中进行了引用，这种情况下能不能把该WebDriver对象放进TestCaseBase.java中用于继承？

    static WebDriver driver;
    static ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory());


    @BeforeAll
    static void init(){
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        wait = new WebDriverWait(driver,5);
        File cookieFile = new File("cookie.yaml");

        driver.get("https://work.weixin.qq.com/wework_admin/frame");
        if (!cookieFile.exists()){
            try {
                Thread.sleep(25000);
                Set<Cookie> cookies = driver.manage().getCookies();
                objectMapper.writeValue(cookieFile,cookies);
            } catch (InterruptedException| IOException e) {
                e.printStackTrace();
            }
        }else{
            try {
                TypeReference typeReference = new TypeReference<List<HashMap<String, Object>>>() {};
                List<HashMap<String, Object>> cookies= objectMapper.readValue(cookieFile, typeReference);
                cookies.forEach(cookie->{
                    //System.out.println(cookie.get("name").toString()+" = "+cookie.get("value").toString());
                    driver.manage().addCookie(new Cookie(cookie.get("name").toString(),cookie.get("value").toString()));
                });
                driver.navigate().refresh();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
    @ParameterizedTest
    @MethodSource
    public void addMemberTest(CaseObjectModel caseObject){
        //System.out.println("driver:"+driver);
        //测试用例的测试步骤
        new MainPage(driver).gotoAddMember().addMember(caseObject.getData().get(caseObject.getIndex()).getParameters());
        //下面开始写测试用例的断言，测试用例的断言支持多重断言、软断言，参考霍格沃兹接口测试框架StepModel类133-143行和ApiTestCaseModel类的113-116行代码
        //取yaml文件中的断言信息asserts，并断言
        ArrayList<AssertModel> asserts = caseObject.getData().get(caseObject.getIndex()).getAsserts();
        //统一断言
        assertAll("",getAseertExec(asserts).stream());//当ArrayList<Executable>中没有子项的时候该断言会执行通过，整个用例的也是pass的
    }

    public static List<CaseObjectModel> addMemberTest(){
        CaseObjectModel caseFileData=null;
        List<CaseObjectModel> testCaseList=null;
        try {
            //TypeReference typeReference = new TypeReference<List<CaseObjectModel>>() {};
            InputStream caseStream = ContactsPageTest.class.getResourceAsStream("/member/add.yaml");
            //System.out.println(caseStream);
            caseFileData = objectMapper.readValue(caseStream,CaseObjectModel.class);
            //case裂变根据data列表数据个数生成相应用例数量
            testCaseList=caseFileData.testcaseGenerate();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return testCaseList;
    }

}
