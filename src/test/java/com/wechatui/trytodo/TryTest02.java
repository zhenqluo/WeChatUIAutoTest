package com.wechatui.trytodo;

import com.wechatui.api.MemberManage;
import com.wechatui.base.TestCaseBase;
import com.wechatui.page_object.ContactsPage;
import com.wechatui.utils.LogService;
import com.wechatui.utils.PathUtil;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.HashMap;

/**
 * @author law
 * @create 2022-05-2022/5/21 16:47
 */
@Disabled
public class TryTest02 {
    //Logger logger = LogService.getInstance(Test02.class).getLogger();
    Logger logger = LoggerFactory.getLogger(TryTest02.class);
    //private final static WebDriver driver = new ChromeDriver();
    private static WebDriver driver;

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
            LogService.getInstance(TryTest02.class).logException(ex);

        }
    }
    @Test
    void test_12() throws Exception{
        //WebDriver driver = new RemoteWebDriver(new URL("http://192.168.162.128:4444"), DesiredCapabilities.chrome());
        DesiredCapabilities cap = DesiredCapabilities.chrome();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-infobars");  // 禁止策略化
        options.addArguments("--no-sandbox"); // 解决DevToolsActivePort文件不存在的报错
        cap.setAcceptInsecureCerts(true);
        cap.setJavascriptEnabled(true);
        cap.setCapability(CapabilityType.ACCEPT_SSL_CERTS,true);
        cap.setCapability(CapabilityType.ACCEPT_INSECURE_CERTS,true);
        cap.setCapability(ChromeOptions.CAPABILITY,options);
        WebDriver driver = new RemoteWebDriver(new URL("http://192.168.162.130:4444"), cap);
        driver.get("https://www.baidu.com");
        driver.manage().window().maximize();
        try {
            driver.findElement(By.id("kw")).sendKeys("selenium");
            driver.findElement(By.id("su")).click();
            Thread.sleep(5000);
        }catch (Exception ex){
            LogService.getInstance().logException(ex);
        }
        Assertions.assertEquals("selenium_百度搜索",driver.getTitle());
        //driver.quit();
    }

    @Test
    void test_14() throws Exception{
        TestCaseBase.init();
        System.out.println(TestCaseBase.driver);
        MemberManage memberManage = new MemberManage();
        HashMap<String,Object> memberInfo = memberManage.addMember(null);
        System.out.println(TestCaseBase.driver);
        new ContactsPage(TestCaseBase.driver).deleteMember(memberInfo);
    }
    @BeforeAll
    static void init() throws Exception{
        DesiredCapabilities cap = DesiredCapabilities.chrome();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-infobars");  // 禁止策略化
        options.addArguments("--no-sandbox"); // 解决DevToolsActivePort文件不存在的报错
        cap.setAcceptInsecureCerts(true);
        cap.setJavascriptEnabled(true);
        cap.setCapability(CapabilityType.ACCEPT_SSL_CERTS,true);
        cap.setCapability(CapabilityType.ACCEPT_INSECURE_CERTS,true);
        cap.setCapability(ChromeOptions.CAPABILITY,options);
        driver = new RemoteWebDriver(new URL("http://192.168.162.130:4444"), cap);
        driver.get("https://www.baidu.com");
        driver.manage().window().maximize();
    }

    @AfterAll
    static void quit(){
        driver.quit();
    }
}

