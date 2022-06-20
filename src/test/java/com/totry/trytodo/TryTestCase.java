package com.totry.trytodo;

import com.wechatui.base.TestCaseBase;
import com.wechatui.model.CaseObjectModel;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;

/**
 * @author luo
 * @create 2022/4/16 下午4:44
 */
@Disabled
@Feature("成员管理")
public class TryTestCase extends TestCaseBase {
    @ParameterizedTest
    @MethodSource
    @Story("更新成员")
    @DisplayName("更新成员")
    void test_01(CaseObjectModel caseObjectModel){
        System.out.println(caseObjectModel);
    }
    static List<CaseObjectModel> test_01(){
        return readYamlCaseData("/testdata/qa/member/updateMemberTest.yaml");
    }
    @Test
    @Story("删除成员")
    @DisplayName("test")
    void test_02(){
        System.out.println("02");
    }
    @Test
    @Story("删除成员")
    void test_03(){
        System.out.println("03");
    }
    @Test
    @Story("添加成员")
    void test_04(){
        System.out.println("04");
    }
    @Test
    void test_05(){
        System.out.println("05");
    }

}
