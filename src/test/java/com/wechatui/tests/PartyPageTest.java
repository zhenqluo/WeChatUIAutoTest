package com.wechatui.tests;

import com.wechatui.api.MemberManage;
import com.wechatui.api.PartyManage;
import com.wechatui.base.TestCaseBase;
import com.wechatui.model.ApiResponseModel;
import com.wechatui.model.AssertModel;
import com.wechatui.model.CaseObjectModel;
import com.wechatui.pages.MainPage;
import com.wechatui.utils.FakerUtils;
import com.wechatui.utils.LogService;
import com.wechatui.utils.PathUtil;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author luo
 * @create 2022/5/3 下午11:57
 */
@Feature("部门管理")
public class PartyPageTest extends TestCaseBase {
    private final static Logger logger = LoggerFactory.getLogger(PartyPageTest.class);

    @ParameterizedTest
    @MethodSource
    @Story("添加部门")
    public void addPartyTest(CaseObjectModel caseObject){
        //数据准备，调用部门创建接口，创建销售部，及子部门团队B
        //todo:这个数据还是写死的，后期思考下怎么参数化
        PartyManage pm = new PartyManage();
        pm.deleteParty(789);
        pm.deleteParty(788);
        HashMap<String,Object> partyInfo = new HashMap<>();
        partyInfo.put("name","销售部");
        partyInfo.put("parentid",1);
        partyInfo.put("id",788);
        ApiResponseModel result1 = pm.createParty(partyInfo);
        partyInfo.clear();
        partyInfo.put("name","团队B");
        partyInfo.put("parentid",788);
        partyInfo.put("id",789);
        ApiResponseModel result2 = pm.createParty(partyInfo);
        if ((result1.getErrmsg().contains("department existed") || result1.getErrmsg().equals("created")) && (result2.getErrmsg().contains("department existed") || result2.getErrmsg().equals("created")) ){
            //测试步骤
            logger.info("正在测试创建部门，数据准备成功，满足测试条件，开始测试");
            new MainPage(uiMutual).gotoPartyManage().addParty(caseObject.getData().get(caseObject.getIndex()).getParameters());
            //取yaml文件中的断言信息asserts，并断言
            ArrayList<AssertModel> asserts = caseObject.getData().get(caseObject.getIndex()).getAsserts();
            //统一断言
            assertAll("",getAseertExec(asserts).stream());
            //todo:数据还原
        }else {
            logger.info("正在测试创建部门，数据准备失败，用例测试失败");
            //todo:需要添加失败断言
        }

    }
    static List<CaseObjectModel> addPartyTest(){
        return readYamlCaseData(PathUtil.getTestDataFilePath(getDefaultYamlFileName()));
    }

    /*
    ** 删除部门测试用例
     */
    //根部门->部门A->子部门A1（A1包含成员m1），删除部门A1，预期删除失败
    @Story("删除部门")
    @DisplayName("删除包含成员的部门，删除失败")
    @Test
    //@RepeatedTest(5)
    public void deletePartyATest(){
        //数据准备：调用接口创建两个部门，两个部门有父子关系，再在子部门添加一个成员
        String pPartyName = FakerUtils.getRandomString(8);
        String cPartyName = FakerUtils.getRandomString(8);
        PartyManage pm = new PartyManage();
        MemberManage mm = new MemberManage();
        ApiResponseModel res1 = pm.createParty(pPartyName,1);
        ApiResponseModel res2 = pm.createParty(cPartyName,res1.getId());
        HashMap<String,Object> memberInfo = mm.addMember(res2.getId());
        //删除部门测试流程
        new MainPage(uiMutual).gotoPartyManage().deletePartySubmit(res2.getReqData().get("name").toString());
        //断言
        assertTrue(isElemExist("by.xpath","//div[@id='js_tips' and text()='该部门存在子部门或成员，无法删除']"));
        //数据还原
        mm.deleteMember(memberInfo.get("userid").toString());
        pm.deleteParty(res2.getId());
        pm.deleteParty(res1.getId());
    }
    //根部门->部门A->子部门A1，删除部门A，预期删除失败
    @Test
    @Story("删除部门")
    @DisplayName("删除有子部门的部门，删除失败")
    public void deletePartyBTest(){
        //数据准备：调用接口创建两个部门，两个部门有父子关系
        String pPartyName = FakerUtils.getRandomString(8);
        String cPartyName = FakerUtils.getRandomString(8);
        PartyManage pm = new PartyManage();
        ApiResponseModel res1 = pm.createParty(pPartyName,1);
        ApiResponseModel res2 = pm.createParty(cPartyName,res1.getId());
        //删除部门测试流程
        new MainPage(uiMutual).gotoPartyManage().deleteParty(res1.getReqData().get("name").toString());
        //断言
        assertTrue(isElemExist("by.xpath","//div[text()='请删除此部门下的成员或子部门后，再删除此部门']"));
        //TODO：进行上面的测试流程后页面会出现一个弹框，按理这个弹框必须关闭才能进行下一个页面操作，但这里因为出现该弹窗已是该测试用例的最后一步且下一个测试用例都是从MainPage开始的，MainPage会重新请求主页，重新请求主页弹窗不影响，所以不会影响下一个用例的执行。后续留意下不处理这个弹窗有没有其它影响
        //数据还原
        pm.deleteParty(res2.getId());
        pm.deleteParty(res1.getId());
    }
    //根部门->部门A，删除部门A，预期删除成功
    @Test
    @Story("删除部门")
    @DisplayName("删除的部门既没有子部门也没有成员，删除成功")
    public void deletePartyCTest(){
        //数据准备：调用接口在根部门下创建一个子部门
        String cPartyName = FakerUtils.getRandomString(8);
        PartyManage pm = new PartyManage();
        ApiResponseModel res = pm.createParty(cPartyName,1);
        //删除部门测试流程
        new MainPage(uiMutual).gotoPartyManage().deletePartySubmit(res.getReqData().get("name").toString());
        //断言
        assertTrue(isElemExist("by.xpath","//div[@id='js_tips' and text()='删除部门成功']"));
        //TODO：进行上面的测试流程后页面会出现一个弹框，按理这个弹框必须关闭才能进行下一个页面操作，但这里因为出现该弹窗已是该测试用例的最后一步且下一个测试用例都是从MainPage开始的，MainPage会重新请求主页，重新请求主页弹窗不影响，所以不会影响下一个用例的执行。后续留意下不处理这个弹窗有没有其它影响
        //数据还原，因为可以删除成功，可以不做数据还原
    }
    /*
    ** 修改部门名称测试用例：
    * 1）修改部门名称与已存在的所有部门名称不一样，修改成功；
    * 2）修改部门名称与已存在的同级部门名称一样，修改失败；
    * 3）修改部门名称与下级部门名称一样，修改成功；
    * 4）修改部门名称与其他部门的下级部门名称一样，修改成功；第3种情况是第4种的特殊，所以实现第3种即可。
    **数据准备结构如下：
    * --部门A
    * ----部门a
    * --部门B
     */
    @Story("更新部门")
    @DisplayName("修改部门名称与已存在的所有部门名称不一样，修改成功")
    @Test
    public void updateParty1Test(){
        //测试数据准备
        PartyManage pm = new PartyManage();
        ApiResponseModel res = pm.createParty(1);
        String newName = FakerUtils.getRandomString(8);
        //更新部门名称流程
        new MainPage(uiMutual).gotoPartyManage().updateParty(newName,res.getReqData().get("name").toString());
        //断言
        assertTrue(isElemExist("by.xpath"," //div[@id='js_tips' and text()='修改名称成功']"));
        //数据恢复
        pm.deleteParty(res.getId());
    }
    @Story("更新部门")
    @DisplayName("修改部门名称与已存在的同级部门名称一样，修改失败")
    @Test
    public void updateParty2Test(){
        //测试数据准备--创建两个同级部门
        PartyManage pm = new PartyManage();
        ApiResponseModel res1 = pm.createParty(1);
        ApiResponseModel res2 = pm.createParty(1);
        //更新部门名称流程
        new MainPage(uiMutual).gotoPartyManage().updateParty(res1.getReqData().get("name").toString(),res2.getReqData().get("name").toString());
        //断言
        assertTrue(isElemExist("by.xpath"," //div[@id='js_tips' and text()='该部门已存在']"));
        //数据还原
        pm.deleteParty(res1.getId()); //todo:使用try catch finally来保证数据还原？
        pm.deleteParty(res2.getId());
    }
    @Story("更新部门")
    @DisplayName("修改部门名称与下级部门名称一样，修改成功")
    @Test
    //@RepeatedTest(10) //为调试StaleElementReferenceException添加的重复测试
    public void updateParty3Test(){
        //测试数据准备
        PartyManage pm = new PartyManage();
        ApiResponseModel res1 = pm.createParty(1);
        ApiResponseModel res2 = pm.createParty(res1.getId());
        //更新部门名称流程
        new MainPage(uiMutual).gotoPartyManage().updateParty(res2.getReqData().get("name").toString(),res1.getReqData().get("name").toString());
        //断言
        assertTrue(isElemExist("by.xpath"," //div[@id='js_tips' and text()='修改名称成功']"));
        //数据还原
        pm.deleteParty(res2.getId());
        pm.deleteParty(res1.getId());
    }


}
