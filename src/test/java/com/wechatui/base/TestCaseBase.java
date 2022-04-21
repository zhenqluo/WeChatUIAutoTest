package com.wechatui.base;

import com.wechatui.model.AssertModel;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.junit.jupiter.api.function.Executable;

import java.util.ArrayList;

import static org.hamcrest.MatcherAssert.assertThat;

import static org.hamcrest.MatcherAssert.assertThat;

/**
 * @author law
 * @create 2022-04-2022/4/20 15:37
 * 说明：该类是test_case包中的类的基类，即测试用例类的基类
 */
public class TestCaseBase {
    public WebDriverWait wait;
    private ArrayList<Executable> assertList = new ArrayList<>();

    public WebElement getElement(String locMode, String locExpression){
        WebElement ele=null;
        if (locMode.equals("by.id")){
            ele = wait.until(ExpectedConditions.presenceOfElementLocated(By.id(locExpression)));
        }
        if (locMode.equals("by.xpath")){
            ele = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(locExpression)));
        }
        if (locMode.equals("by.name")){
            ele = wait.until(ExpectedConditions.presenceOfElementLocated(By.name(locExpression)));
        }
        return ele;
    }
    //提供方法获取元素内部文本
    public String getElemInnerHTML(String locMode,String locExpression){
        //getText()不是获取InnerHTML
        //return getElement(locMode,locExpression).getText();
        return getElement(locMode,locExpression).getAttribute("innerHTML");
    }
    //提供方法获取元素属性值
    public String getElemAtrributeVlue(String locMode,String locExpression,String attr){
        return getElement(locMode,locExpression).getAttribute(attr);
    }
    //提供方法先切换iframe再获取元素文本
    public String SwitchFrameAndGetElemInnerHTML(String frameId,String locMode,String locExpression){
        return null;
    }
    //提供方法判断元素是否存在
    public boolean isElemExist(String locMode,String locExpression){
        return getElement(locMode,locExpression) == null? false:true;
    }
    /**
     * 根据case中的配置对返回结果进行软断言，但不会终结测试将断言结果存入断言结果列表中，在用例最后进行统一结果输出
     */
    public ArrayList<Executable> getAseertExec(ArrayList<AssertModel> asserts){
        if (asserts != null) {
            asserts.stream().forEach(assertModel -> {
                assertList.add(() -> {
                    assertThat(assertModel.getReason(), true);
                });
            });
        }
        return assertList;
    }


}
