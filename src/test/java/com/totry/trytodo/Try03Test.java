package com.totry.trytodo;

import com.google.common.collect.ImmutableMap;
import com.wechatui.base.TestCaseBase;
import io.qameta.allure.Allure;
import jdk.nashorn.internal.runtime.Timing;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.BeforeTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.platform.commons.util.Preconditions;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author law
 * @create 2022-06-2022/6/22 16:47
 */
@ExtendWith(TestExecution.class)
public class Try03Test {
    @Test
    public void test301(){
        System.out.println("test301");
        assertTrue(1+1==2);
    }
}

class TestExecution implements BeforeTestExecutionCallback, AfterTestExecutionCallback {
    @Override
    public void beforeTestExecution(ExtensionContext context) throws Exception {
        StringBuffer sb = new StringBuffer();
        sb.append(context.getDisplayName()+"\n");
        sb.append(context.getRequiredTestClass().getSimpleName()+"\n");
        sb.append(context.getTestMethod().get().getName()+"\n");
        sb.append(getTimeString()+"\n");
        getStore(context).put("log",sb.toString());
    }
    @Override
    public void afterTestExecution(ExtensionContext context) throws Exception {
        final String logs = getStore(context).remove("log").toString();
        Allure.addAttachment("日志",logs);
        Allure.attachment("日志",logs);
        Allure.descriptionHtml("<h1>这是一个标题</h1>");
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
}

