package com.wechatui.trytodo.extendTest;


import com.wechatui.utils.LogService;
import io.qameta.allure.model.Attachment;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestInstances;
import org.junit.jupiter.api.extension.TestWatcher;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import static io.qameta.allure.Allure.addAttachment;


import java.io.File;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Optional;

/**
 * @author luo
 * @create 2022/5/13 下午9:25
 */
public class MyWatcher implements TestWatcher {
    public void testDisabled(ExtensionContext context, Optional<String> reason) {
    }

    public void testSuccessful(ExtensionContext context) {
        System.out.println("用例执行成功");

        try {
            //使用调试的方式来理解ExtensionContext
            Optional<AnnotatedElement> element = context.getElement();
            String displayName = context.getDisplayName();
            Class<?> requiredTestClass = context.getRequiredTestClass();
            //requiredTestClass.getDeclaredField()
            Field[] fields=context.getRequiredTestClass().getDeclaredFields();
            int size = fields.length;
            Field f = context.getRequiredTestClass().getDeclaredField("driver");
            TestInstances requiredTestInstances = context.getRequiredTestInstances();
            Method requiredTestMethod = context.getRequiredTestMethod();
            Object requiredTestInstance = context.getRequiredTestInstance();
            context.getConfigurationParameter("str");
        }catch (Exception exception){

        }

    }

    public void testAborted(ExtensionContext context, Throwable cause) {
        System.out.println("testAborted");
        Object requiredTestInstance = context.getRequiredTestInstance();
        BaiduTest bt = (BaiduTest)requiredTestInstance;
        //context.getRequiredTestClass();
        try {
            Field f = context.getRequiredTestClass().getDeclaredField("driver");
            Object obj = f.get(bt);
            File screenshotFile = ((TakesScreenshot) obj).getScreenshotAs(OutputType.FILE);
            //FileUtils.copyFile(screenshotFile, new File("/Users/wuyuanyan/idea-workspace/Junit5TestPro/src/test/java/com/fortest/screenshot"+System.currentTimeMillis()+".png"));
            addAttachment("Screenshot",FileUtils.openInputStream(screenshotFile));
        }catch (Exception ex){

        }
    }

    public void testFailed(ExtensionContext context, Throwable cause) {
        System.out.println("用例执行失败");
        Class clzz = context.getRequiredTestClass();
        if (cause instanceof Exception ){
            LogService.getInstance(clzz).logException((Exception) cause);
        }

        Object requiredTestInstance = context.getRequiredTestInstance();
        BaiduTest bt = (BaiduTest)requiredTestInstance;
        //context.getRequiredTestClass();
        try {
            Field f = clzz.getField("driver");
            Object obj = f.get(bt);
            File screenshotFile = ((TakesScreenshot) obj).getScreenshotAs(OutputType.FILE);
            //FileUtils.copyFile(screenshotFile, new File("/Users/wuyuanyan/idea-workspace/Junit5TestPro/src/test/java/com/fortest/screenshot"+System.currentTimeMillis()+".png"));
            addAttachment("Screenshot",FileUtils.openInputStream(screenshotFile));
        }catch (Exception ex){

        }

    }
}


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
