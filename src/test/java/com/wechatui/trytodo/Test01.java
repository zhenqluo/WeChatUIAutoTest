package com.wechatui.trytodo;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.wechatui.api.CommonOpr;
import com.wechatui.api.MemberManage;
import com.wechatui.api.PartyManage;
import com.wechatui.base.BasePage;
import com.wechatui.test_case.ContactsPageTest;
import com.wechatui.base.TestCaseBase;
import com.wechatui.model.AssertModel;
import com.wechatui.model.CaseObjectModel;
import com.wechatui.page_object.MainPage;
import com.wechatui.utils.FakerUtils;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author luo
 * @create 2022/4/17 上午9:09
 */
@Disabled
public class Test01 extends TestCaseBase{
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
        contactsPageTest.addMemberTest(null);
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
    @Test
    void test_11(){
        WebDriver driver = new ChromeDriver();
        BasePage bp = new BasePage(driver);
        driver.get("https://www.baidu.com");
        bp.sleep(1);
        bp.sendKeys(By.id("kw"),"hello");
        bp.click(By.id("su"));
        bp.sleep(3);
        bp.closeBrowser();
    }




    @Test
    void test_13(){
        MemberManage memberManage = new MemberManage();
        memberManage.addMember(null);
    }
    @Test
    void test_14(){
        do{
            System.out.println("aa");
        }while (!"0".equals("0"));
    }
    //java.lang.IllegalArgumentException: argument type mismatch  使用反射时报的错，下面验证下啥问题
    @Test
    void test_15(){
        Object[] params1 = {10};
        Object[] params2 = {new Integer(10)};
        String[] params3 = {"10"} ;
        try {

            Method method=Arrays.stream(Class.forName("com.wechatui.utils.FakerUtils").getMethods()).filter(m->m.getName().equals("getRandomString")).findFirst().get();//通过反射获取方法
            System.out.println(method);
            //Object actual1 = method.invoke(null,new Integer(10));//执行成功
            //Object actual2 = method.invoke(null,10);//执行成功
//            method.invoke(null,"10"); 执行报错
            //method.invoke(null,params1); //执行报错
            method.invoke(null,params2);//执行报错
            //method.invoke(null,params3);

            //System.out.println((String) actual1);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    @Test
    void test_16() throws Exception{
        for (int i = 0; i < 1; i++) {
            Method method=Arrays.stream(Class.forName("com.wechatui.utils.FakerUtils").getMethods()).filter(m->m.getName().equals("getRandomStringS")).findFirst().get();//通过反射获取方法
            System.out.println(method);
            String[] strings = {"10"};
            System.out.println(method.invoke(null,strings));
        }
        System.out.println("+++++++++++");
        Stream<Method> methodStream= Arrays.stream(Class.forName("com.wechatui.utils.FakerUtils").getMethods()).filter(m->m.getName().equals("getRandomStringS"));
        for (Object method : methodStream.toArray()){
            System.out.println((Method)method);
        }

    }
    @Test
    void test_17(){
        String addMemberURL = "https://qyapi.weixin.qq.com/cgi-bin/user/create?access_token="+ CommonOpr.getAccessToken();
        //接口创建临时成员
        MemberManage memberManage = new MemberManage();
        //接口创建已知信息的成员
        HashMap<String,Object> specificMember = createSpecificMemberInfo();
        given().
                contentType("application/json").
                body(specificMember).
        when().
                post(addMemberURL).
        then().log().all();
    }

    private HashMap<String,Object> createSpecificMemberInfo(){
        HashMap<String,Object> specificMember = new HashMap<>();
        specificMember.put("userid", "zhang003");
        specificMember.put("name","张三");
        specificMember.put("alias","youxiang01@123.com");
        specificMember.put("mobile","15602243341");
        //由于企业邮箱涉及到邮箱回收，邮箱回收接口不知道是什么，不可彻底删除，为避免添加成员失败，该成员的企业邮箱由随机数生成
        //specificMember.put("biz_mail","zhang003_mail@gqjk3.wecom.work");
        specificMember.put("biz_mail",FakerUtils.getRandomStringWithSuffix(8,"@gqjk3.wecom.work"));
        specificMember.put("to_invite",false);
        specificMember.put("department",1);
        return specificMember;
    }
    @Test
    void test_18(){
        WebDriver driver = new ChromeDriver();
        driver.get("https://www.baidu.com");
        WebDriverWait wait=new WebDriverWait(driver,5);
        Boolean bl = wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("ss")));
        System.out.println(bl);
    }
    @Test
    void test_19(){
        for (int i = 0; i < 1000; i++) {
            int index = (int) (Math.random()*(6));
            System.out.println(index+1);
        }

    }
    @Test
    void test_20(){
        HashMap<String,Object> partyInfo = new HashMap<>();
        partyInfo.put("name","销售部");
        partyInfo.put("parentid",1);
        partyInfo.put("id",788);
        System.out.println(new PartyManage().createParty(partyInfo));
        partyInfo.clear();
        partyInfo.put("name","团队B");
        partyInfo.put("parentid",788);
        partyInfo.put("id",789);
        System.out.println(new PartyManage().createParty(partyInfo));

    }
    @Test
    void test_21(){
        assertAll(()->assertTrue(true),
                ()->assertTrue(false),
                ()->assertTrue(true));
    }
    @RepeatedTest(10)
    void test_22(){
        new MainPage(driver).gotoPartyManage().deletePartySubmit("LPH61Uwc");
    }
    @Test
    void test_23(){
        driver.get("https://blog.csdn.net/weixin_42609477/article/details/108330294");
        long st = System.currentTimeMillis();
        new BasePage(driver).refresh();
        long end = System.currentTimeMillis();
        System.out.println((end-st)/1000);
    }








}
