package com.wechatui.test_case;

import com.wechatui.api.PartyManage;
import com.wechatui.base.BasePage;
import com.wechatui.base.TestCaseBase;
import com.wechatui.model.ApiResponseModel;
import com.wechatui.model.AssertModel;
import com.wechatui.model.CaseObjectModel;
import com.wechatui.page_object.MainPage;
import com.wechatui.utils.LogService;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;

/**
 * @author luo
 * @create 2022/5/3 下午11:57
 */
public class PartyPageTest extends TestCaseBase {
    private final static Logger logger = LogService.getInstance(PartyPageTest.class).getLogger();

    @ParameterizedTest
    @MethodSource
    public void addPartyTest(CaseObjectModel caseObject){
        //数据准备，调用部门创建接口，创建销售部，及子部门团队B
        //todo:这个数据还是写死的，后期思考下怎么参数化
        PartyManage pm = new PartyManage();
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
        if ((result1.getErrmsg().contains("department existed") || result1.getErrmsg()=="created") && (result2.getErrmsg().contains("department existed") || result2.getErrmsg()=="created") ){
            //测试步骤
            logger.info("正在测试创建部门，数据准备成功，满足测试条件，开始测试");
            new MainPage(driver).gotoPartyManage().addParty(caseObject.getData().get(caseObject.getIndex()).getParameters());
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
        return readYamlCaseData("/party/addParty.yaml");
    }
}
