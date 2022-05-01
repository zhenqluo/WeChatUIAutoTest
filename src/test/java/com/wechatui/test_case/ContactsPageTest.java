package com.wechatui.test_case;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.wechatui.api.MemberManage;
import com.wechatui.base.BasePage;
import com.wechatui.base.TestCaseBase;
import com.wechatui.model.AssertModel;
import com.wechatui.model.CaseObjectModel;
import com.wechatui.page_object.ContactsPage;
import com.wechatui.page_object.MainPage;
import com.wechatui.utils.FakerUtils;
import com.wechatui.utils.LogService;
import org.junit.jupiter.api.*;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;

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
import static org.junit.jupiter.api.Assertions.assertFalse;

/**
 * @author luo
 * @create 2022/4/17 下午12:59
 */
public class ContactsPageTest extends TestCaseBase{
    private static final Logger logger = LogService.getInstance(ContactsPageTest.class).getLogger();
    //该WebDriver使用static修饰是因为在@BeforeAll中进行了引用，这种情况下能不能把该WebDriver对象放进TestCaseBase.java中用于继承？


    static ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory());



    @BeforeAll
    static void init(){
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        wait = new WebDriverWait(driver,5);
        disppearWait = new WebDriverWait(driver,3);
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
        //测试数据中的变量替换

        //测试用例的测试步骤
        new MainPage(driver).gotoAddMember().addMember(caseObject.getData().get(caseObject.getIndex()).getParameters());
        //下面开始写测试用例的断言，测试用例的断言支持多重断言、软断言，参考霍格沃兹接口测试框架StepModel类133-143行和ApiTestCaseModel类的113-116行代码
        //取yaml文件中的断言信息asserts，并断言
        ArrayList<AssertModel> asserts = caseObject.getData().get(caseObject.getIndex()).getAsserts();
        //统一断言
        assertAll("",getAseertExec(asserts).stream());//当ArrayList<Executable>中没有子项的时候该断言会执行通过，整个用例的也是pass的
    }

    static List<CaseObjectModel> addMemberTest(){
        return readYamlCaseData("/member/add_ramdom.yaml"); //使用该行代码会报错，暂时不知啥原因
    }
    static List<CaseObjectModel> readYamlCaseData(String filePath){
        CaseObjectModel caseFileData=null;
        List<CaseObjectModel> testCaseList=null;
        try {
            //TypeReference typeReference = new TypeReference<List<CaseObjectModel>>() {};
            InputStream caseStream = ContactsPageTest.class.getResourceAsStream(filePath);
            //System.out.println(caseStream);
            caseFileData = objectMapper.readValue(caseStream,CaseObjectModel.class);
            //变量替换
            caseFileData.getActualValue();
            //case裂变根据data列表数据个数生成相应用例数量
            testCaseList=caseFileData.testcaseGenerate();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return testCaseList;
    }
    @Test
    void deleteMemberTest(){
        logger.info("开始删除成员...");
        logger.info("先调用接口产生一个随机成员数据以供删除操作....");
        //接口创建临时成员
        MemberManage memberManage = new MemberManage();
        HashMap<String,Object> memberInfo = memberManage.addMember(null);
        //删除成员流程
        new MainPage(driver).gotoContacts().deleteMember(memberInfo);

        assertFalse(isElemExist("By.xpath","//span[text()='"+memberInfo.get("name")+"']"));
    }
    @ParameterizedTest
    @MethodSource
    void updateMemberTest(CaseObjectModel caseObject) throws Exception{
        logger.info("开始更新成员...");
        logger.info("先调用接口产生一个随机成员数据以供更新操作....");

        MemberManage memberManage = new MemberManage();
        //数据预先清理（避免前面的测试过程中断导致没删除清理数据导致下面创建张三失败）
        memberManage.deleteMember("zhang003");
        //接口创建临时成员
        HashMap<String,Object> ramdomMember = memberManage.addMember(null);
        //接口创建已知信息的成员
        HashMap<String,Object> specificMember=memberManage.addMember(createSpecificMemberInfo());
        //成员流程
        ContactsPage contactsPage = new MainPage(driver).gotoContacts();
        contactsPage.updateMember(ramdomMember.get("userid").toString(),caseObject.getData().get(caseObject.getIndex()).getParameters());
        //断言：使用页面是否还有保存或取消按钮
        ArrayList<AssertModel> asserts = caseObject.getData().get(caseObject.getIndex()).getAsserts();
        assertAll("",getAseertExec(asserts).stream());

        //判断页面是否还存在取消按钮（即更新信息不成功），若存在则点击取消按钮->点击离开此页按钮（此流程封装在ContactsPage.java中）
        if (!isElemDisappear("by.linkText","取消")){   //判断取消按钮是否已消失，若等待2s（wait设置的时间）还在，则执行取消操作
            contactsPage.cancelUpdate();
        }

        //调用接口删除上面创建的成员，进行数据还原
        memberManage.deleteMember(ramdomMember.get("userid").toString());
        memberManage.deleteMember(specificMember.get("userid").toString());
    }
    static List<CaseObjectModel> updateMemberTest(){
        return readYamlCaseData("/member/updateMember.yaml");
    }
    private HashMap<String,Object> createSpecificMemberInfo(){
        HashMap<String,Object> specificMember = new HashMap<>();
        specificMember.put("userid", "zhang003");
        specificMember.put("name","张三");
        specificMember.put("email","youxiang01@123.com");
        specificMember.put("mobile","15602243341");
        //由于企业邮箱涉及到邮箱回收，邮箱回收接口不知道是什么，不可彻底删除，为避免添加成员失败，该成员的企业邮箱由随机数生成
        //specificMember.put("biz_mail","zhang003_mail@gqjk3.wecom.work");
        specificMember.put("biz_mail",FakerUtils.getRandomStringWithSuffix(8,"@gqjk3.wecom.work"));
        specificMember.put("to_invite",false);
        specificMember.put("department",1);
        return specificMember;
    }

}
