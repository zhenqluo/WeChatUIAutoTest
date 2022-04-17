package com.wechatui.test_case;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.wechatui.page_object.MainPage;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * @author luo
 * @create 2022/4/17 下午12:59
 */
public class ContactsPageTest {
    static WebDriver driver;
    static ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory());

    @BeforeAll
    static void init(){
        driver = new ChromeDriver();
        File cookieFile = new File("cookie.yaml");

        driver.get("https://work.weixin.qq.com/wework_admin/frame");
        if (!cookieFile.exists()){
            try {
                Thread.sleep(15000);
                Set<Cookie> cookies = driver.manage().getCookies();
                objectMapper.writeValue(cookieFile,cookies);
            } catch (InterruptedException| IOException e) {
                e.printStackTrace();
            }
        }else{
            try {
                TypeReference typeReference = new TypeReference<List<HashMap<String, Object>>>() {};
                List<HashMap<String, Object>> cookies= objectMapper.readValue(cookieFile, typeReference);
                cookies.forEach(cookie->{
                    //System.out.println(cookie.get("name").toString()+" = "+cookie.get("value").toString());
                    driver.manage().addCookie(new Cookie(cookie.get("name").toString(),cookie.get("value").toString()));
                });
                driver.navigate().refresh();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
    @ParameterizedTest
    @MethodSource
    public void addMemberTest(HashMap<String,Object> member){
        //System.out.println("driver:"+driver);
        new MainPage(driver).gotoAddMember().addMember(member);
        
    }

    static List<HashMap<String,Object>> addMemberTest(){
        List<HashMap<String,Object>> members=null;
        try {
            TypeReference typeReference = new TypeReference<List<HashMap<String, Object>>>() {};
            InputStream caseStream = ContactsPageTest.class.getResourceAsStream("/member/add.yaml");
            //System.out.println(caseStream);
            members = objectMapper.readValue(caseStream,typeReference);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return members;
    }


}
