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
 * 说明：该类是page_object包中的类的基类
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
    public void open(String URL){
        driver.get(URL);
        sleep(1);
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
    public void sleep(double lt){
        try {
            Thread.sleep((long)lt*1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
