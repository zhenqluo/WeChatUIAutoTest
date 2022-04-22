package com.wechatui.trytodo;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.wechatui.base.BasePage;
import com.wechatui.base.TestCaseBase;
import com.wechatui.model.AssertModel;
import com.wechatui.test_case.ContactsPageTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertAll;

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
    @Test
    void test_06() throws Exception{
        TypeReference<ArrayList<AssertModel>> typeReference = new TypeReference<ArrayList<AssertModel>>() {};
        ObjectMapper om = new ObjectMapper(new YAMLFactory());
        ArrayList<AssertModel>  modelList = om.readValue(this.getClass().getResourceAsStream("/member/assertModel.yaml"),typeReference);
        TestCaseBase testCaseBase = new TestCaseBase();
        WebDriver driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get("https://www.baidu.com");
        Thread.sleep(1000);
        WebDriverWait wait = new WebDriverWait(driver,5);
        testCaseBase.wait=wait;
        ArrayList<Executable> excutList=testCaseBase.getAseertExec(modelList);
        assertAll(excutList.stream());
    }
    @Test
    void test_07(){
        ArrayList<Executable> execList = new ArrayList<>();
        assertAll(execList.stream());  //当execList中没有子项的时候该断言会执行通过，整个用例的也是pass的
    }
}
