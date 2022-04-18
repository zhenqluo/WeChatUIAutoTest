package com.wechatui.base;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.HashMap;

/**
 * @author luo
 * @create 2022/4/16 下午4:33
 */
public class BasePage {
    public  WebDriver driver;
    //下面的语句引起空指针异常，改为在构造器中初始化
    //WebDriverWait wait=new WebDriverWait(driver,10);
    public WebDriverWait wait;
    public BasePage(WebDriver driver){
        this.driver=driver;
        wait=new WebDriverWait(this.driver,10);
    }

    public void sendKeys(By loc,String words){
        wait.until(ExpectedConditions.visibilityOfElementLocated(loc)).sendKeys(words);
    }
    public void click(By loc){
        wait.until(ExpectedConditions.elementToBeClickable(loc)).click();
    }
    public void judgeToSendKeys(By loc, HashMap<String,Object> map, String key){
        if (map.get(key) != null){//map.get(key) != null包含了没key和有key但没value的情况
            sendKeys(loc,map.get(key).toString());
        }
    }
    public void clear(By loc){
        wait.until(ExpectedConditions.presenceOfElementLocated(loc)).clear();
    }
    public WebElement getElement(String locMode,String locExpression){
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




}
