package com.wechatui.base;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.wechatui.model.AssertModel;
import com.wechatui.model.CaseObjectModel;
import com.wechatui.model.ExtensionModel;
import com.wechatui.page_object.LoginPage;
import com.wechatui.utils.LogService;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.*;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.junit.jupiter.api.function.Executable;
import org.slf4j.Logger;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.*;
import java.util.concurrent.TimeUnit;


import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * @author law
 * @create 2022-04-2022/4/20 15:37
 * 说明：该类是test_case包中的类的基类，即测试用例类的基类
 */
@ExtendWith(ExtensionModel.class)

public class TestCaseBase {
    private static final Logger logger = LogService.getInstance(TestCaseBase.class).getLogger();
    public static WebDriver driver;
    public static WebDriverWait wait;
    //本想多创建一个专门用于等待元素消失的wait，设置等待时长为1s，但在实际测试时发现1s有时会出现元素仍然存在从而导致异常抛出的情况，所以没必要单独设置这个wait或者说这个wait的等待时长不能太短
    public static WebDriverWait disppearWait;
    private ArrayList<Executable> assertList = new ArrayList<>();
    public static ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory());


    @BeforeAll
    public static void init() throws Exception{
        DesiredCapabilities cap = DesiredCapabilities.chrome();
        ChromeOptions options = new ChromeOptions();
//        options.addArguments("--disable-infobars");  // 禁止策略化
//        options.addArguments("--no-sandbox"); // 解决DevToolsActivePort文件不存在的报错
//        cap.setAcceptInsecureCerts(true);
//        cap.setJavascriptEnabled(true);
//        cap.setCapability(CapabilityType.ACCEPT_SSL_CERTS,true);
//        cap.setCapability(CapabilityType.ACCEPT_INSECURE_CERTS,true);
//        cap.setCapability(ChromeOptions.CAPABILITY,options);
        WebDriver driver = new RemoteWebDriver(new URL("http://192.168.162.130:4444/wd/hub"), cap);
//        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        wait = new WebDriverWait(driver,5);
        disppearWait = new WebDriverWait(driver,3);
        File cookieFile = new File("cookie.yaml");

        driver.get("https://work.weixin.qq.com/wework_admin/frame");
        //使用分布式测试时selenium_node的浏览器是英文版的，企业微信默认为英文版，所以需先切换为中文版
        System.out.println("初始化："+driver);
        new LoginPage(driver).switchChinese();
        if (!cookieFile.exists()){
            try {
                Thread.sleep(25000);
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
        System.out.println("BasePageEnd");
        System.out.println(driver);
    }
    @AfterAll
    public static void end(){
        driver.quit();
    }

    public static List<CaseObjectModel> readYamlCaseData(String filePath){
        CaseObjectModel caseFileData=null;
        List<CaseObjectModel> testCaseList=null;
        try {
            //TypeReference typeReference = new TypeReference<List<CaseObjectModel>>() {};
            InputStream caseStream = TestCaseBase.class.getResourceAsStream(filePath);
            //System.out.println(caseStream);
            caseFileData = objectMapper.readValue(caseStream,CaseObjectModel.class);
            //变量替换
            caseFileData.getActualValue();
            //case裂变根据data列表数据个数生成相应用例数量
            testCaseList=caseFileData.testcaseGenerate();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return testCaseList;
    }


    //返回定位方式，yaml文件中通过字符串指定，这里把字符串转换为真正的By
    public By getLocType(String locMode, String locExpression){
        By locType = null;
        if (locMode.equalsIgnoreCase("by.id")){
            locType = By.id(locExpression);
        }
        if (locMode.equalsIgnoreCase("by.xpath")){
            locType = By.xpath(locExpression);
        }
        if (locMode.equalsIgnoreCase("by.name")){
            locType = By.name(locExpression);
        }
        if (locMode.equalsIgnoreCase("by.linkText")){
            locType = By.linkText(locExpression);
        }
        return locType;
    }
    public WebElement getElement(String locMode, String locExpression){
        WebElement ele=null;
        try {
            ele = wait.until(ExpectedConditions.presenceOfElementLocated(getLocType(locMode,locExpression)));
        }catch (NoSuchElementException | TimeoutException ex){  //如果元素没定位到会抛出异常，这里进行异常捕获
            //System.out.println("没定位到元素，请核实元素定位方式");
            //System.out.println(ex.getMessage());
            logger.error("没定位到元素{}:{}",locMode,locExpression);
            LogService.getInstance(TestCaseBase.class).logException(ex);
        }
        return ele;
    }
    //提供方法获取元素内部文本
    public String getElemInnerHTML(String locMode,String locExpression){
        //getText()不是获取InnerHTML
        //return getElement(locMode,locExpression).getText();
        logger.info("获取元素{}:{}内部文本innerHTML",locMode,locExpression);
        WebElement ele=getElement(locMode,locExpression);
        return ele == null? null:ele.getAttribute("innerHTML");
    }
    //提供方法获取元素属性值
    public String getElemAtrributeVlue(String locMode,String locExpression,String attr){
        logger.info("获取元素{}:{}内部属性{}属性值",locMode,locExpression,attr);
        WebElement ele=getElement(locMode,locExpression);
        return ele == null? null:ele.getAttribute(attr);
    }
    //提供方法先切换iframe再获取元素文本
    public String SwitchFrameAndGetElemInnerHTML(String frameId,String locMode,String locExpression){
        return null;
    }
    //提供方法判断元素是否存在
    public boolean isElemExist(String locMode,String locExpression){
        logger.info("判断元素{}:{}是否存在",locMode,locExpression);
        return getElement(locMode,locExpression) == null? false:true;
    }
    //提供方法判断元素是否不存在,比较适合页面元素消失的情况，如保存成员信息，点击保存后若成功则页面的保存/取消按钮会消失
    public boolean isElemDisappear(String locMode,String locExpression){
        boolean isExist = true;
        try {
            isExist=disppearWait.until(ExpectedConditions.invisibilityOfElementLocated(getLocType(locMode,locExpression)));
        }catch (TimeoutException ex){ //注意invisibilityOfElementLocated条件如果元素不存在返回true，但如果元素存在，不是返回false，而是抛出异常，所以需要对异常进行捕获并返回false
            logger.info("元素[{}:{}]仍在页面存在",locMode,locExpression);
            isExist = false;
        }
        return isExist;
    }
    //传入方法名和实参列表，反射执行方法
    public Object invokeMethod(String methodName, ArrayList<String> params){
        Object actual = null;
        try {
            Method method=Arrays.stream(this.getClass().getMethods()).filter(m->m.getName().equals(methodName)).findFirst().get();
            actual = method.invoke(this,params.toArray()); //invoke()是形参个数可变的方法，个数可变的形参可传入一个数组，但不能是ArrayList，所以把ArrayList转换成数组
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return actual;
    }
    /**
     * 根据case中的配置对返回结果进行软断言，但不会终结测试将断言结果存入断言结果列表中，在用例最后进行统一结果输出
     * 要实现断言，第一步是获取实际值actual，yaml文件中的actual是操作步骤并不是实际结果，所以需要执行这些操作步骤得到最终结果
     * 那如何取得该计算结果呢？如isElemExist: ["by.xpath","//xpath[text()='张三']"]，需要调用isElemExist()方法
     */
    public ArrayList<Executable> getAseertExec(ArrayList<AssertModel> asserts){
        if (asserts != null) {
            asserts.stream().forEach(assertModel -> {
                //取得actual的最终结果
                String methodName = assertModel.getActual().keySet().iterator().next(); //方法名
                ArrayList<String> params = assertModel.getActual().get(methodName);  //实参列表
                Object actual = invokeMethod(methodName,params);
                String macher = assertModel.getMatcher();
                Object expect = assertModel.getExpect();
                String reason = "当前断言:"+assertModel.getReason();

                if (macher.equalsIgnoreCase("assertTrue") && actual instanceof Boolean){
                    assertList.add(() -> {
                        //使用AssertThat("",actual,equalTo(true))来实现assertTrue的效果
                        assertThat(reason, (Boolean) actual,equalTo(true) );
                    });
                }else if (macher.equalsIgnoreCase("assertFalse") && actual instanceof Boolean){
                    assertList.add(() -> {
                        //使用AssertThat("",actual,equalTo(false))来实现assertFalse的效果
                        assertThat(reason, (Boolean) actual,equalTo(false));
                    });
                }else if (macher.equalsIgnoreCase("equalTo") ){
                    assertList.add(() -> {
                        //判断相等，并没有把actual和expect转换为字符串再比较，因为actual和expect返回的都是Object，而且equalTo比较器也是泛型参数，没必要转换
                        assertThat(reason, actual,equalTo(expect));
                    });
                }else if ((macher.equalsIgnoreCase("contains") || macher.equalsIgnoreCase("containsString")) && actual instanceof String && expect instanceof String){
                    assertList.add(() -> {
                        //containsString()判断是否包含子串，actual和expect必须转换为字符串
                        assertThat(reason, (String) actual,containsString((String) expect));
                    });
                }else{ //else模块一定要添加，很可能出现使用了超出规定范围的断言方式和页面元素获取方法，或者其他错误引起的不匹配上面几个if判断条件的情况，此时需要断言失败，并把原因显示出来
                    assertList.add(() -> {
                        //下面注释的这个断言是有问题的，因为存在调用方法取得actual是null的情况，而AssertModel的expect也可能为null（yaml文件不写该字段）此时null is null断言成功，但实际是有问题的
                        //assertThat(reason+"\n可能出现未知问题，请先核查断言中是否使用规定范围的断言方式和页面元素获取方法，当前取得的actual值为："+actual, actual,is(expect));
                        assertThat(reason+"\n可能出现未知问题，请先核查断言中是否使用规定范围的断言方式和页面元素获取方法，当前取得的actual值为："+actual, false);
                    });
                }
            });
        }
        return assertList;
    }


}
