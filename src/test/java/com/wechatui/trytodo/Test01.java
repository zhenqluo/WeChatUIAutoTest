package com.wechatui.trytodo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.wechatui.base.BasePage;
import com.wechatui.base.TestCaseBase;
import com.wechatui.test_case.ContactsPageTest;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.stream.Stream;

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
        TestCaseBase bp = new TestCaseBase();
        System.out.println(bp.getElemAtrributeVlue("by.id", "su", "value"));
        System.out.println(bp.getElemInnerHTML("by.xpath", "//span[@name='tj_settingicon']"));
    }
    @Test
    public void test_02(){
        WebDriver driver = new ChromeDriver();
        driver.get("http://www.baidu.com");
        TestCaseBase bp = new TestCaseBase();
        System.out.println(bp.isElemExist("by.id", "kw"));
    }
    @Test
    public void test_03() throws  Exception{
        ObjectMapper om = new ObjectMapper(new YAMLFactory());
        HashMap<String, ArrayList<String>> stringArrayListHashMap = new HashMap<>();
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("by.xpath");
        arrayList.add("//xpath[text()='张三']");
        stringArrayListHashMap.put("isElemExist",arrayList);
        om.writeValue(new File("testYAML.yaml"),stringArrayListHashMap);
    }
    @Test
    public void test04(){
        Class clz = String.class;
        //System.out.println(clz.getMethods());
        Arrays.stream(clz.getMethods()).forEach(m-> System.out.println(m.getName()));
        Arrays.stream(clz.getMethods()).forEach(m-> System.out.println(m.toString()));
    }
    @Test
    void test_05(){
        ContactsPageTest contactsPageTest = new ContactsPageTest();
        contactsPageTest.addMemberTest();
    }
}
