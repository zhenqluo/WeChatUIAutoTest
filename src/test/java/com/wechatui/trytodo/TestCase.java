package com.wechatui.trytodo;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * @author luo
 * @create 2022/4/16 下午4:44
 */
public class TestCase  {
    static WebDriver driver;

    WebDriverWait wait=new WebDriverWait(driver,10);

    public void sendKeys(By loc,String words){
        wait.until(ExpectedConditions.visibilityOfElementLocated(loc)).sendKeys(words);
    }
    public void click(By loc){
        wait.until(ExpectedConditions.elementToBeClickable(loc)).click();
    }
    public void clear(By loc){
        wait.until(ExpectedConditions.elementToBeClickable(loc)).clear();
    }


    @Test
    public void test_01() throws Exception{
        driver=new ChromeDriver();
        driver.get("https://www.baidu.com");
        driver.findElement(By.xpath("//input[@id='kw']")).sendKeys("企业微信");
        driver.findElement(By.xpath("//input[@id='su']")).click();
        Thread.sleep(3000);
        driver.quit();
    }
    @BeforeAll
    static void init() throws Exception{
        driver=new ChromeDriver();
        driver.get("https://work.weixin.qq.com/wework_admin/frame");
        File cookieFile = new File("cookie.yaml");
        ObjectMapper mp = new ObjectMapper(new YAMLFactory());
        if (!cookieFile.exists()) {
            Thread.sleep(15000);
            Set<Cookie> cookies = driver.manage().getCookies();
            mp.writeValue(new File("cookie.yaml"), cookies);
        }else {
            TypeReference typeReference = new TypeReference<List<HashMap<String,Object>>>(){};
            List<HashMap<String,Object>> cookieList = mp.readValue(cookieFile,typeReference);
            cookieList.forEach(cookie->{
                System.out.println(cookie.get("name").toString()+"="+cookie.get("value").toString());
                driver.manage().addCookie(new Cookie(cookie.get("name").toString(),cookie.get("value").toString()));
            });
            driver.navigate().refresh();
        }

    }
    @ParameterizedTest
    @MethodSource
    public void test_02(HashMap<String,Object> member) {
        /*
        click(By.xpath("//span[text()='添加成员']"));
        sendKeys(By.name("username"),member.get("username").toString());
        sendKeys(By.name("english_name"),member.get("english_name").toString());
        sendKeys(By.name("acctid"),member.get("acctid").toString());
        click(By.xpath("//input[@name='gender' and @value='2']"));
        clear(By.name("biz_mail"));
        sendKeys(By.name("biz_mail"),member.get("biz_mail").toString());
        clear(By.xpath("//div[@class='ww_telInput']/div/div/input"));
        sendKeys(By.xpath("//div[@class='ww_telInput']/div/div/input"),member.get("ww_tel").toString());
        sendKeys(By.name("mobile"),member.get("mobile").toString());
        sendKeys(By.name("ext_tel"),member.get("ext_tel").toString());
        sendKeys(By.name("xcx_corp_address"),member.get("xcx_corp_address").toString());
        sendKeys(By.name("alias"),member.get("alias").toString());
        sendKeys(By.name("position"),member.get("position").toString());
        click(By.name("sendInvite"));
        click(By.linkText("保存"));
         */
        /*如何优化上面的代码？member.get("position").toString()可能因为没对应的key-value而报空指针异常。
        可以考虑封装一个函数，该函数实现的功能是判断Hashmap中是否有key-value，有则执行对应的sendKeys操作，没有则不操作
        定义的该函数是judgeToSendKeys
         */
        click(By.xpath("//span[text()='添加成员']"));
//        sendKeys(By.name("username"),member.get("username").toString());
        judgeToSendKeys(By.name("username"),member,"username");
//        sendKeys(By.name("english_name"),member.get("english_name").toString());
        judgeToSendKeys(By.name("english_name"),member,"english_name");
//        sendKeys(By.name("acctid"),member.get("acctid").toString());
        judgeToSendKeys(By.name("acctid"),member,"acctid");
        click(By.xpath("//input[@name='gender' and @value='2']"));
        clear(By.name("biz_mail"));
//        sendKeys(By.name("biz_mail"),member.get("biz_mail").toString());
        judgeToSendKeys(By.name("biz_mail"),member,"biz_mail");
        clear(By.xpath("//div[@class='ww_telInput']/div/div/input"));
//        sendKeys(By.xpath("//div[@class='ww_telInput']/div/div/input"),member.get("ww_tel").toString());
        judgeToSendKeys(By.xpath("//div[@class='ww_telInput']/div/div/input"),member,"ww_tel");
//        sendKeys(By.name("mobile"),member.get("mobile").toString());
        judgeToSendKeys(By.name("mobile"),member,"mobile");
//        sendKeys(By.name("ext_tel"),member.get("ext_tel").toString());
        judgeToSendKeys(By.name("ext_tel"),member,"ext_tel");
//        sendKeys(By.name("xcx_corp_address"),member.get("xcx_corp_address").toString());
        judgeToSendKeys(By.name("xcx_corp_address"),member,"xcx_corp_address");
//        sendKeys(By.name("alias"),member.get("alias").toString());
        judgeToSendKeys(By.name("alias"),member,"alias");
//        sendKeys(By.name("position"),member.get("position").toString());
        judgeToSendKeys(By.name("position"),member,"position");
        click(By.name("sendInvite"));
        click(By.linkText("保存"));

    }
    private void judgeToSendKeys(By loc,HashMap<String,Object> map,String key){
        if (map.get(key) != null){//map.get(key) != null包含了没key和有key但没value的情况
            sendKeys(loc,map.get(key).toString());
        }
    }

    static List<HashMap<String,Object>> test_02() throws Exception{
        ObjectMapper mp = new ObjectMapper(new YAMLFactory());
        TypeReference typeReference=new TypeReference<List<HashMap<String,Object>>>(){};
        List<HashMap<String,Object>> addList=mp.readValue(TestCase.class.getResourceAsStream("/member/add.yaml"),typeReference);
        return addList;
    }
}
