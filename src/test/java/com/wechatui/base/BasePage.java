package com.wechatui.base;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
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
    
    public BasePage(WebDriver driver){
        this.driver=driver;
        System.out.println("BasePage_driver:"+this.driver);
    }
    WebDriverWait wait=new WebDriverWait(driver,10);

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


}
