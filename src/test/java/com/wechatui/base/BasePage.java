package com.wechatui.base;

import com.wechatui.utils.LogService;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;

/**
 * @author luo
 * @create 2022/4/16 下午4:33
 * 说明：该类是page_object包中的类的基类
 */
public class BasePage {
    private static final Logger logger = LogService.getInstance(BasePage.class).getLogger();
    public  WebDriver driver;
    //下面的语句引起空指针异常，改为在构造器中初始化
    //WebDriverWait wait=new WebDriverWait(driver,10);
    public WebDriverWait wait;
    public BasePage(WebDriver driver){
        this.driver=driver;
        wait=new WebDriverWait(this.driver,3);
    }
    public void open(String URL){
        logger.info("打开网站：{}",URL);
        driver.get(URL);
        sleep(1);
    }

    public void sendKeys(By loc,String words){
        logger.info("正在定位元素{}，输入文本{}",loc.toString(),words);
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(loc)).sendKeys(words);
        }catch (Exception ex){
            logger.error("定位元素{}，输入文本{}失败",loc.toString(),words);
            throw new RuntimeException(ex.getMessage());
        }
    }
    public void click(By loc){
        logger.info("正在定位元素{}进行点击操作",loc.toString());
        try {
            wait.until(ExpectedConditions.elementToBeClickable(loc)).click();
        }catch (Exception ex){
            logger.error("定位元素{}进行点击操作失败",loc.toString());
            throw new RuntimeException(ex.getMessage());
        }
    }
    public void judgeToSendKeys(By loc, HashMap<String,Object> map, String key){
        if (map.get(key) != null){//map.get(key) != null包含了没key和有key但没value的情况
            sendKeys(loc,map.get(key).toString());
        }
    }
    public void clear(By loc){
        logger.info("正在定位元素{}进行clear操作",loc.toString());
        try {
            wait.until(ExpectedConditions.presenceOfElementLocated(loc)).clear();
        }catch (Exception ex){
            logger.error("定位元素{}进行clear操作失败",loc.toString());
            throw new RuntimeException(ex.getMessage());
        }
    }
    public void sleep(double lt){
        try {
            Thread.sleep((long)lt*1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public void closeBrowser(){
        logger.info("关闭浏览器");
        driver.quit();
    }

}
