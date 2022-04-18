package com.wechatui.trytodo;

import com.wechatui.base.BasePage;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.lang.reflect.Type;
import java.util.HashMap;

/**
 * @author luo
 * @create 2022/4/17 上午9:09
 */
public class Test01 {
    public static void main(String[] args) {
        HashMap<String ,Object> map = new HashMap<>();
        map.put("a",1);
        map.put("b","b");
        map.put("c",null);
        if (map.get("d") == null){
            System.out.println("is null");
        }
    }
    @Test
    public void test_01(){
        WebDriver driver = new ChromeDriver();
        driver.get("https://www.baidu.com");
        BasePage bp = new BasePage(driver);
        System.out.println(bp.getElemAtrributeVlue("by.id", "su", "value"));
        System.out.println(bp.getElemInnerHTML("by.xpath", "//span[@name='tj_settingicon']"));
    }
    @Test
    public void test_02(){
        WebDriver driver = new ChromeDriver();
        driver.get("http://www.baidu.com");
        BasePage bp = new BasePage(driver);
        System.out.println(bp.isElemExit("by.id", "kw"));
    }
}
