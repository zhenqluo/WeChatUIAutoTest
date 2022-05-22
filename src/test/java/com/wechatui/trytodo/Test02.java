package com.wechatui.trytodo;

import com.wechatui.utils.LogService;
import com.wechatui.utils.PathUtil;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author law
 * @create 2022-05-2022/5/21 16:47
 */
public class Test02 {
    //Logger logger = LogService.getInstance(Test02.class).getLogger();
    Logger logger = LoggerFactory.getLogger(Test02.class);
    @Test
    void test_01(){
        System.out.println(this.getClass().getResource("/").getPath());
        System.out.println(Thread.currentThread().getContextClassLoader().getResource("").getPath());
        System.out.println(PathUtil.classPath);
        System.out.println(PathUtil.getRootPath());
        System.out.println(PathUtil.getRootPath("/member/add.yaml").replaceAll("\\{1,}","\\"));
        System.out.println(PathUtil.getRootPath("member/add.yaml"));
        System.out.println(PathUtil.getRootPath("\\member\\add.yaml"));
        System.out.println(PathUtil.getRootPath("member\\add.yaml"));
    }
    @Test
    void test_02(){
        WebDriver driver= new ChromeDriver();
        driver.get("https://www.baidu.com");
        WebDriverWait wait = new WebDriverWait(driver,3);
        try {
            wait.until(ExpectedConditions.presenceOfElementLocated(By.id("ss"))).click();
        }catch (Exception ex){
            System.out.println(ex.getMessage());
            //Expected condition failed: waiting for presence of element located by: By.id: ss (tried for 3 second(s) with 500 milliseconds interval)
            System.out.println(ex.toString());
            //org.openqa.selenium.TimeoutException: Expected condition failed: waiting for presence of element located by: By.id: ss (tried for 3 second(s) with 500 milliseconds interval)

            //logger.error(ex.getMessage(),ex);
            LogService.getInstance(Test02.class).logException(ex);

        }
    }
}
