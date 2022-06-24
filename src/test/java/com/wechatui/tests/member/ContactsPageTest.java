package com.wechatui.tests.member;


import com.wechatui.api.MemberManage;
import com.wechatui.base.TestCaseBase;
import com.wechatui.model.AssertModel;
import com.wechatui.model.CaseObjectModel;
import com.wechatui.pages.member.ContactsPage;
import com.wechatui.pages.MainPage;
import com.wechatui.utils.FakerUtils;
import com.wechatui.utils.LogService;
import com.wechatui.utils.PathUtil;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.jupiter.api.*;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


/**
 * @author luo
 * @create 2022/4/17 下午12:59
 */
@Feature("成员管理")
public class ContactsPageTest extends TestCaseBase {
    private static final Logger logger = LoggerFactory.getLogger(ContactsPageTest.class);
    //该WebDriver使用static修饰是因为在@BeforeAll中进行了引用，这种情况下能不能把该WebDriver对象放进TestCaseBase.java中用于继承？


    @ParameterizedTest
    @MethodSource
    @Story("添加成员")
    public void addMemberTest(CaseObjectModel caseObject){
        //System.out.println("driver:"+driver);
        //测试数据中的变量替换

        //测试用例的测试步骤
        new MainPage(uiMutual).gotoAddMember().addMember(caseObject.getData().get(caseObject.getIndex()).getParameters());
        //下面开始写测试用例的断言，测试用例的断言支持多重断言、软断言，参考霍格沃兹接口测试框架StepModel类133-143行和ApiTestCaseModel类的113-116行代码
        //取yaml文件中的断言信息asserts，并断言
        ArrayList<AssertModel> asserts = caseObject.getData().get(caseObject.getIndex()).getAsserts();
        //统一断言
        assertAll("",getAseertExec(asserts).stream());//当ArrayList<Executable>中没有子项的时候该断言会执行通过，整个用例的也是pass的
    }

    static List<CaseObjectModel> addMemberTest(){
        //return readYamlCaseData("/testdata/qa/member/addMemberTest.yaml");
        return readYamlCaseData(PathUtil.getTestDataFilePath(getDefaultYamlFileName()));
    }

    @Test
    @Story("删除成员")
    @DisplayName("删除成员")
    void deleteMemberTest() throws Exception{
        logger.info("开始删除成员...");
        logger.info("先调用接口产生一个随机成员数据以供删除操作....");
        //接口创建临时成员
        MemberManage memberManage = new MemberManage();
        HashMap<String,Object> memberInfo = memberManage.addMember(null);
        //删除成员流程
        new MainPage(uiMutual).gotoContacts().deleteMember(memberInfo);

        assertTrue(isElemExist("By.Xpath","//div[@id='js_tips' and text()='删除成功']"));
    }
    @ParameterizedTest
    @MethodSource
    @Story("更新成员信息")
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
        ContactsPage contactsPage = new MainPage(uiMutual).gotoContacts();
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
        return readYamlCaseData(PathUtil.getTestDataFilePath(getDefaultYamlFileName()));
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
    @ParameterizedTest
    @MethodSource
    @Story("通过文件批量导入成员")
    void importTemplateTest(CaseObjectModel caseObject){
        //todo:导入前数据清理,读入文件数据，根据账号调用接口删除数据。或通过生成随机数的方式生成批量数据文件，最后恢复数据
        new MemberManage().deleteMember("lisi");
        String fileName = caseObject.getData().get(0).getParameters().get("filePath").toString();
        String relativePath = PathUtil.getTestDataFilePath(fileName);
        String absolutePath = PathUtil.getRootPath(relativePath);
        logger.info("批量导入成员测试，导入文件地址：{}",absolutePath);
        new MainPage(uiMutual).gotoContacts().importTemplate(absolutePath);
        //取yaml文件中的断言信息asserts，并断言
        ArrayList<AssertModel> asserts = caseObject.getData().get(caseObject.getIndex()).getAsserts();
        //统一断言
        assertAll("",getAseertExec(asserts).stream());
    }
    static List<CaseObjectModel> importTemplateTest(){
        return readYamlCaseData(PathUtil.getTestDataFilePath(getDefaultYamlFileName()));
    }

}
