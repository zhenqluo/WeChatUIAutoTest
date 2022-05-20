package com.wechatui.model;

import com.wechatui.utils.LogService;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestWatcher;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.slf4j.Logger;

import java.io.File;
import java.lang.reflect.Field;

import static io.qameta.allure.Allure.addAttachment;

/**
 * @author law
 * @create 2022-05-2022/5/18 21:52
 */
public class ExtensionModel implements TestWatcher {
    Logger logger = LogService.getInstance(ExtensionModel.class).getLogger();
    @Override
    public void testFailed(ExtensionContext context, Throwable cause) {  //测试用例断言失败
        screenShot(context,cause,"TestFailedScreenshot");
    }
    @Override
    public void testAborted(ExtensionContext context, Throwable cause) { //测试执行中止，如执行用例过程中抛出异常
        screenShot(context,cause,"TestAbortedScreenshot");
    }
    private void screenShot(ExtensionContext context, Throwable cause, String name){
        Object requiredTestInstance = context.getRequiredTestInstance();
        try {
            //下面获取driver属性报错java.lang.NoSuchFieldException: driver，因为driver是从父类继承来的
            //Field f = context.getRequiredTestClass().getDeclaredField("driver");
            Field f = context.getRequiredTestClass().getField("driver");
            Object obj = f.get(requiredTestInstance);
            File screenshotFile = ((TakesScreenshot) obj).getScreenshotAs(OutputType.FILE);
            addAttachment(name, FileUtils.openInputStream(screenshotFile));
        }catch (Exception ex){
            LogService.getInstance(ExtensionModel.class).logException(ex);
        }
    }
}
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
