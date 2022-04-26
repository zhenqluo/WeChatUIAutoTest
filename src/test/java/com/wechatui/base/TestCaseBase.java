package com.wechatui.base;

import com.wechatui.model.AssertModel;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.junit.jupiter.api.function.Executable;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;


import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * @author law
 * @create 2022-04-2022/4/20 15:37
 * 说明：该类是test_case包中的类的基类，即测试用例类的基类
 */
public class TestCaseBase {
    public static WebDriverWait wait;
    private ArrayList<Executable> assertList = new ArrayList<>();

    public WebElement getElement(String locMode, String locExpression){
        WebElement ele=null;
        try {
            if (locMode.equals("by.id")){
                ele = wait.until(ExpectedConditions.presenceOfElementLocated(By.id(locExpression)));
            }
            if (locMode.equals("by.xpath")){
                ele = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(locExpression)));
            }
            if (locMode.equals("by.name")){
                ele = wait.until(ExpectedConditions.presenceOfElementLocated(By.name(locExpression)));
            }
        }catch (NoSuchElementException | TimeoutException ex){  //如果元素没定位到会抛出异常，这里进行异常捕获
            System.out.println("没定位到元素，请核实元素定位方式");
            System.out.println(ex.getMessage());
        }

        return ele;
    }
    //提供方法获取元素内部文本
    public String getElemInnerHTML(String locMode,String locExpression){
        //getText()不是获取InnerHTML
        //return getElement(locMode,locExpression).getText();
        WebElement ele=getElement(locMode,locExpression);
        return ele == null? null:ele.getAttribute("innerHTML");
    }
    //提供方法获取元素属性值
    public String getElemAtrributeVlue(String locMode,String locExpression,String attr){
        WebElement ele=getElement(locMode,locExpression);
        return ele == null? null:ele.getAttribute(attr);
    }
    //提供方法先切换iframe再获取元素文本
    public String SwitchFrameAndGetElemInnerHTML(String frameId,String locMode,String locExpression){
        return null;
    }
    //提供方法判断元素是否存在
    public boolean isElemExist(String locMode,String locExpression){
        return getElement(locMode,locExpression) == null? false:true;
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
