package com.totry.trytodo.extendTest;

/**
 * @author law
 * @create 2022-05-2022/5/18 21:07
 */


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

@Disabled
@ExtendWith(MyWatcher.class)
public class BaiduTestxx extends BaseT{
    Logger logger = LoggerFactory.getLogger(BaiduTestxx.class);
    @BeforeAll
    static void setup(){
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);

    }
    @Test
    @Disabled
    void baiduTest(){
        driver.get("https://www.baidu.com");
        driver.findElement(By.id("kw")).sendKeys("哈哈");
        driver.findElement(By.id("su")).click();
        int a = 5/0; //模拟程序异常抛出
        Assertions.assertEquals(3,4);
    }
    @Test
    void ExceptionTest(){
        try{
            driver.get("https://www.baidu.com");
            logger.info("打开百度");
            driver.findElement(By.id("kw")).sendKeys("hha");
            logger.info("定位输入框");
            driver.findElement(By.id("ss")).click();
        }catch (Exception ex){
            //LogService.getInstance(BaiduTest.class).logException(ex);
            //logger.error("哦哦哦哦哦哦哦",ex);
            logger.error(ex.toString(),ex);
            throw ex;
        }
    }
    @Test
    void successTest(){
        Assertions.assertEquals(3,3);
    }



    public static WebDriver getDriver() {
        return driver;
    }
}

