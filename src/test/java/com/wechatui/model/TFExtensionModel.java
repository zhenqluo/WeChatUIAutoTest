package com.wechatui.model;

import com.wechatui.base.UiMutual;
import io.qameta.allure.Allure;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.BeforeTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestWatcher;
import org.junit.platform.engine.TestExecutionResult;
import org.junit.platform.engine.TestTag;
import org.junit.platform.launcher.TestExecutionListener;
import org.junit.platform.launcher.TestIdentifier;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;

import static io.qameta.allure.Allure.addAttachment;

/**
 * @author law
 * @create 2022-05-2022/5/18 21:52
 */
public class TFExtensionModel implements TestWatcher, BeforeTestExecutionCallback, AfterTestExecutionCallback {
    Logger logger = LoggerFactory.getLogger(TFExtensionModel.class);
    /*
    因为在TestWatcher集中处理异常时打印出的日志行号不正确（因为打印的是调用日志输出函数所在行，所以打印出的行号都是一样的），不利于排错
    所以在不在TestWatcher中进行异常日志处理
     */
    @Override
    public void testFailed(ExtensionContext context, Throwable cause) {  //测试用例断言失败
        logger.error(cause.toString(),cause);
        logger.info("执行用例标题[{}],执行结果{}\n",context.getDisplayName(), "Test Failed");
        screenShot(context,cause,"TestFailedScreenshot");
    }
    @Override
    public void testSuccessful(ExtensionContext context) {//测试执行中止，如执行用例过程中抛出异常
        logger.info("执行用例标题[{}],执行结果{}\n",context.getDisplayName(),"Test Success");
    }
    @Override
    public void testAborted(ExtensionContext context, Throwable cause) { //测试执行中止，如执行用例过程中抛出异常
        logger.error(cause.toString(),cause);
        logger.info("执行用例标题[{}],执行结果{}\n",context.getDisplayName(),"Test Aborted");
        screenShot(context,cause,"TestAbortedScreenshot");
    }
    @Override
    public void beforeTestExecution(ExtensionContext context) throws Exception {
        StringBuffer sb = new StringBuffer();
        sb.append(getTimeString()+"\n");
        sb.append(context.getDisplayName()+"\n");
        sb.append(context.getRequiredTestClass().getSimpleName()+"\n");
        sb.append(context.getTestMethod().get().getName()+"\n");
        getStore(context).put("logs",sb.toString());
    }
    @Override
    public void afterTestExecution(ExtensionContext context) throws Exception {
        final String timeLogs = "开始时间："+getStore(context).remove("logs").toString()+"\n结束时间："+getTimeString();

        Allure.addAttachment("运行时间",timeLogs);
    }
    private ExtensionContext.Store getStore(ExtensionContext context) {
        return context.getStore(ExtensionContext.Namespace.create(context.getUniqueId(),context));
    }
    public String  getTimeString(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.S");
        String timeString = simpleDateFormat.format(new Date());
        simpleDateFormat = null;
        return timeString;
    }

    private void screenShot(ExtensionContext context, Throwable cause, String name){
        Object requiredTestInstance = context.getRequiredTestInstance();
        try {
            //下面获取driver属性报错java.lang.NoSuchFieldException: driver，因为driver是从父类继承来的
            //Field f = context.getRequiredTestClass().getDeclaredField("driver");
            Field f = context.getRequiredTestClass().getField("uiMutual");
            UiMutual obj = (UiMutual)f.get(requiredTestInstance);
            File screenshotFile = ((TakesScreenshot) obj.getDriver()).getScreenshotAs(OutputType.FILE);
            addAttachment(name, FileUtils.openInputStream(screenshotFile));
        }catch (Exception ex){
            logger.error(ex.toString(),ex);
        }
    }
}
/*
Junit5+extentreports生成测试报告：参考其中DefaultTestWatcher（定义扩展模型，继承了TestWatcher，同时重写里面的方法，用来捕获日志断言执行情况）
https://blog.csdn.net/sumlyl/article/details/109759516
如果测试失败，则用于调试输出的Log4j附加程序：https://www.it1352.com/2156814.html
 */
/*
java的反射机制提供了两种方法：
getDeclaredFields() ：该方法能获取到本类的所有属性，包括private，protected和public，但不能获取到继承的父类的属性。
getFields()：既能获取本类的属性也能得到父类的属性，但仅仅能获取public修饰的字段。
如果用getFields把所有的字段都改为public，那么修饰符则失去了其存在的意义。
因此可以用getSuperclass()得到父类，进行循环遍历，从而得到所有的属性。
*/
/*
JUnit 5 系列：扩展模型（Extension Model）：
https://blog.csdn.net/u012260238/article/details/89575617?spm=1001.2101.3001.6650.1&utm_medium=distribute.pc_relevant.none-task-blog-2%7Edefault%7ECTRLIST%7ERate-1-89575617-blog-108684339.pc_relevant_antiscanv2&depth_1-utm_source=distribute.pc_relevant.none-task-blog-2%7Edefault%7ECTRLIST%7ERate-1-89575617-blog-108684339.pc_relevant_antiscanv2&utm_relevant_index=2
JUnit 5 教程
https://dnocm.com/articles/cherry/junit-5-info/#%E5%88%AB%E5%90%8D
关于JUnit5 你必须知道的(三) 深入理解JUnit 5扩展机制
https://blog.csdn.net/qq_35448165/article/details/108684339
在JUnit5中，我如何从扩展调用测试类方法？
http://www.yiidian.com/questions/44118
如何将屏幕截图添加到Allure报告？
https://cloud.tencent.com/developer/ask/sof/1250659

 */
