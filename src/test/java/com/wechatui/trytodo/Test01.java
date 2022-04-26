package com.wechatui.trytodo;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.wechatui.base.BasePage;
import com.wechatui.base.TestCaseBase;
import com.wechatui.model.AssertModel;
import com.wechatui.model.CaseObjectModel;
import com.wechatui.test_case.ContactsPageTest;
import com.wechatui.utils.FakerUtils;
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
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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
    @Test
    void test_08(){
        HashMap<String,ArrayList<String>> map = new HashMap<>();
        ArrayList<String>  strArr1 = new ArrayList<>();
        strArr1.add("uoi${a}dfsfdf${b}jj");
        strArr1.add("by.xpath");
        strArr1.add("//span[text()=${username}]");
        map.put("isElemExist",strArr1);
        HashMap<String,String> pm = new HashMap<>();
        pm.put("a","&&&");
        pm.put("b","***");
        pm.put("username","law");
        //现在需要替换上面的map中的${username}
        for (Map.Entry entry:map.entrySet()){
            System.out.println(entry.getKey());
            System.out.println(entry.getValue());
            ArrayList<String> arrayList = new ArrayList<>();
            ArrayList<String> params = (ArrayList<String>)entry.getValue();
            //正则表达式匹配${}
            String varRegEx = "\\$\\{.*?\\}";
            Pattern pattern = Pattern.compile(varRegEx);
            Matcher matcher=null;
            for (String param : params){
                //判断是否包含${}，若包含则取出${...}中的内容，最后整体替换掉${...}
                String str = param;
                if (matcher==null){
                    matcher=pattern.matcher(param);
                }else {
                    matcher.reset(param);
                }
                while (matcher.find()){
                    String temp=matcher.group();
                    String v = temp.substring(2,temp.length() -1);
                    str=str.replace(temp,pm.get(v));
                }
                arrayList.add(str);
            }
            entry.setValue(arrayList);
        }
        for (Map.Entry entry:map.entrySet()){
            System.out.println(entry.getKey());
            System.out.println(entry.getValue());
            for (String param : (ArrayList<String>)entry.getValue()){
                System.out.println(param);
            }
        }
    }
    @Test
    void test_09(){
        String varRegEx = "\\$\\{.*?\\}";
        String str = "uoi${a}dfsfdf${b}jj";
        Pattern pattern = Pattern.compile(varRegEx);
        Matcher matcher = pattern.matcher(str);
        while (matcher.find()){
            //System.out.println(matcher.group());
            String temp=matcher.group();
            String v = temp.substring(2,temp.length() -1);
            System.out.println(v);
            str=str.replace(temp,"哈哈");
            System.out.println(str);
        }
    }

    @Test
    void test_10() throws Exception{
        ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory());
        CaseObjectModel caseObjectModel=objectMapper.readValue(Test01.class.getResourceAsStream("/member/add_ramdom.yaml"), CaseObjectModel.class);
        caseObjectModel.getActualValue();
        System.out.println(caseObjectModel.getData().get(0).getParameters().get("username"));
        System.out.println(caseObjectModel.getData().get(1).getParameters().get("username"));
    }
}
