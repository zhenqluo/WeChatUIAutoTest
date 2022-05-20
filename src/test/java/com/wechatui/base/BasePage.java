package com.wechatui.base;

import com.wechatui.utils.LogService;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
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
        sleep(0.5);
    }

    public void sendKeys(By loc,String words){
        clear(loc); //进行文本输入前无论啥清空先清空
        logger.info("正在定位元素[{}]，输入文本{}",loc.toString(),words);
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(loc)).sendKeys(words);
        }catch (Exception ex){
            logger.error("定位元素[{}]，输入文本{}失败",loc.toString(),words);
            throw new RuntimeException(ex.getMessage());
        }
        sleep(0.5);  //为方便调试临时添加
    }
    public void click(By loc){
        logger.info("正在定位元素[{}]进行点击操作",loc.toString());
        try {
            wait.until(ExpectedConditions.elementToBeClickable(loc)).click();
        }catch (Exception ex){
            logger.error("定位元素[{}]进行点击操作失败",loc.toString());
            //从业务流程上讲有一个元素定位失败，则整个流程就可以终止了，所以下面抛出另一个异常终止流程执行，
            // 如果不抛出，因为所有操作都捕抓了异常，导致下一个流程步骤可继续执行，假如每一个流程都是元素定位，导致不断的等待超时抛出异常，最后需超长等待才能发现流程问题
            throw new RuntimeException(ex.getMessage());
        }
        sleep(0.5);//为方便观察调试临时添加
    }
    public void judgeToSendKeys(By loc, HashMap<String,Object> map, String key){
        if (map.get(key) != null){//map.get(key) != null包含了没key和有key但没value的情况
            sendKeys(loc,map.get(key).toString());
        }
    }
    public void clear(By loc){
        logger.info("正在定位元素[{}]进行clear操作",loc.toString());
        try {
            wait.until(ExpectedConditions.presenceOfElementLocated(loc)).clear();
        }catch (Exception ex){
            logger.error("定位元素[{}]进行clear操作失败",loc.toString());
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
    public void refresh(){
        driver.navigate().refresh();
        //sleep(0.1);//解决StaleElementReferenceException的第一种方法：引入强制等待，这种方式不好把控时间，不考虑
        /*  --解决StaleElementReferenceException的第二中方法：原本想着是刷新页面引起的元素过时异常，那刷新后等待页面加载完成再进行下一步操作是否可性？经验证，结论是不可行
        JavascriptExecutor jsExec = (JavascriptExecutor) driver;
        while (true){
            boolean loaded = (Boolean) jsExec.executeScript("return document.readyState == \"complete\"");
            if (loaded) break;
        }
         */
    }


}
