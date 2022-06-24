package com.wechatui.base;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.wechatui.common.SeleniumHelper;
import com.wechatui.model.AssertModel;
import com.wechatui.model.CaseObjectModel;
import com.wechatui.model.TFExtensionModel;
import com.wechatui.pages.LoginPage;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;



import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * @author law
 * @create 2022-04-2022/4/20 15:37
 * 说明：该类是test_case包中的类的基类，即测试用例类的基类
 */
@ExtendWith(TFExtensionModel.class)

public class TestCaseBase {
    private static final Logger logger = LoggerFactory.getLogger(TestCaseBase.class);

    public static UiMutual uiMutual = null;
    //本想多创建一个专门用于等待元素消失的wait，设置等待时长为1s，但在实际测试时发现1s有时会出现元素仍然存在从而导致异常抛出的情况，所以没必要单独设置这个wait或者说这个wait的等待时长不能太短
    //public static WebDriverWait disppearWait;
    private ArrayList<Executable> assertList = new ArrayList<>();
    public static ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory());


    @BeforeAll
    public static void init() {
        logger.info("指定的参数browser={}",System.getProperty("browser"));
        logger.info("指定的参数env={}",System.getProperty("env"));


        uiMutual = new SeleniumHelper();
        uiMutual.initDriver();


        File cookieFile = new File("cookie.yaml");

        uiMutual.open("https://work.weixin.qq.com/wework_admin/frame");
        //使用分布式测试时selenium_node的浏览器是英文版的，企业微信默认为英文版，所以需先切换为中文版
        new LoginPage(uiMutual).switchChinese();

        if (!cookieFile.exists()){
            try {
                Thread.sleep(25000);
                Set<Cookie> cookies = uiMutual.getCookies();
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
                    uiMutual.addCookie(new Cookie(cookie.get("name").toString(),cookie.get("value").toString()));
                });
                uiMutual.refresh();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
    @AfterAll
    public static void terminate(){
        uiMutual.closeBrowser();
    }

    public static List<CaseObjectModel> readYamlCaseData(String filePath){

        CaseObjectModel caseFileData=null;
        List<CaseObjectModel> testCaseList=null;

        //File file = new File(filePath);
        InputStream caseStream = TestCaseBase.class.getResourceAsStream(filePath);
        //if (file.exists()){  //不可使用此种方式判断文件是否存在，因为filePath是一个/../..相对文件路径，但使用class.getResourceAsStream()可以读取
        if (caseStream != null){
            try {
                caseFileData = objectMapper.readValue(caseStream,CaseObjectModel.class);
            }catch (Exception ex){
                logger.error(ex.toString(),ex);
            }
            //变量替换
            caseFileData.getActualValue();
            //case裂变根据data列表数据个数生成相应用例数量
            testCaseList=caseFileData.testcaseGenerate();
        }else{
            logger.error("测试数据文件{}不存在！！",filePath);
            throw new RuntimeException(filePath+"不存在！！");
        }

        return testCaseList;
    }



    //提供方法获取元素内部文本
    public String getElemInnerHTML(String locMode,String locExpression){
        //getText()不是获取InnerHTML
        //return getElement(locMode,locExpression).getText();
        logger.info("获取元素{}:{}内部文本innerHTML",locMode,locExpression);
        WebElement ele = uiMutual.getElement(locMode,locExpression);
        return ele == null? null:uiMutual.getElemAttributeValue(ele,"innerHTML");
    }
    //提供方法获取元素属性值
    public String getElemAtrributeVlue(String locMode,String locExpression,String attrName){
        logger.info("获取元素{}:{}内部属性{}属性值",locMode,locExpression,attrName);
        WebElement ele = uiMutual.getElement(locMode,locExpression);
        return ele == null? null:uiMutual.getElemAttributeValue(ele,attrName);
    }
    //提供方法先切换iframe再获取元素文本
    public String SwitchFrameAndGetElemInnerHTML(String frameId,String locMode,String locExpression){
        return null;
    }
    //提供方法判断元素是否存在
    public boolean isElemExist(String locMode,String locExpression){
        logger.info("判断元素{}:{}是否存在",locMode,locExpression);
        return uiMutual.getElement(locMode,locExpression) == null? false:true;
    }
    //提供方法判断元素是否不存在,比较适合页面元素消失的情况，如保存成员信息，点击保存后若成功则页面的保存/取消按钮会消失
    public boolean isElemDisappear(String locMode,String locExpression){
        return uiMutual.isElemDisappear(locMode,locExpression);
    }
    //传入方法名和实参列表，反射执行方法
    public Object invokeMethod(String methodName, ArrayList<String> params){
        Object actual = null;
        try {
            Method method=Arrays.stream(this.getClass().getMethods()).filter(m->m.getName().equals(methodName)).findFirst().get();
            actual = method.invoke(this,params.toArray()); //invoke()是形参个数可变的方法，个数可变的形参可传入一个数组，但不能是ArrayList，所以把ArrayList转换成数组
        } catch (IllegalAccessException e) {
            logger.error(e.toString(),e);
        } catch (InvocationTargetException e) {
            logger.error(e.toString(),e);
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

    protected static String getDefaultYamlFileName(){
        return Thread.currentThread().getStackTrace()[2].getMethodName()+".yaml";
    }


}
