package com.wechatui.trytodo.tyeone.one;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.net.URL;
import java.util.concurrent.TimeUnit;

/**
 * @author law
 * @create 2022-05-2022/5/27 17:09
 */
public class CaseBase {
    public static WebDriver driver;
    public static WebDriverWait wait;
    @BeforeAll
    static void init() throws Exception{
        DesiredCapabilities cap = DesiredCapabilities.chrome();
        ChromeOptions options = new ChromeOptions();
//        options.addArguments("--disable-infobars");  // 禁止策略化
//        options.addArguments("--no-sandbox"); // 解决DevToolsActivePort文件不存在的报错
//        cap.setAcceptInsecureCerts(true);
//        cap.setJavascriptEnabled(true);
//        cap.setCapability(CapabilityType.ACCEPT_SSL_CERTS,true);
//        cap.setCapability(CapabilityType.ACCEPT_INSECURE_CERTS,true);
        cap.setCapability(ChromeOptions.CAPABILITY,options);
        driver = new RemoteWebDriver(new URL("http://192.168.162.130:4444"), cap);
        driver.get("https://www.baidu.com");
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        wait = new WebDriverWait(driver,5);
        System.out.println("ContactsPageTest...........");
        System.out.println(driver);
        System.out.println(driver.getTitle());
    }
    @AfterAll
    static void end(){
        driver.quit();
    }
}
