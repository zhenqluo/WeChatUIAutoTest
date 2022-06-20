package com.totry;

import com.wechatui.utils.LogService;
import io.restassured.response.Response;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.chrome.ChromeDriver;
import org.slf4j.Logger;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
/**
 * @author law
 * @create 2022-06-2022/6/1 9:52
 * 说明：本类是为验证UI融合API的可能性和实现，即API登录的cookie用在网页登录在网页保持登录态
 */
public class UIFuseAPI {
    static Logger logger = LogService.getInstance(UIFuseAPI.class).getLogger();
    public static void main(String[] args) {
        Response res = given().
                header("user-agent","user-agent: Mozilla/5.0(Windows NT10.0;WOW64)AppleWebKit/537.36(KHTML, like Gecko)Chrome/78.0.3904.108 Safari/537.36").
                contentType("application/json").
                body("{\"password\":\"lzq243532\",\"email\": \"luozq11@163.com\"}").
        when().
                post("https://ones.ai/project/api/project/auth/login");
        System.out.println(res.statusCode());
        System.out.println(res.headers());
        System.out.println(res.getCookies());
        Map<String,String> cookies = res.getCookies();
        for (String key : cookies.keySet()){
            logger.info("{} -> {}",key,cookies.get(key));
        }

        ChromeDriver driver = new ChromeDriver();
        driver.get("https://ones.ai/project/#/home/project");
        driver.manage().deleteAllCookies();
        for (String key : cookies.keySet()){
            driver.manage().addCookie(new Cookie(key,cookies.get(key)));
        }
        driver.get("https://ones.ai/project/#/home/project");
    }
}



















