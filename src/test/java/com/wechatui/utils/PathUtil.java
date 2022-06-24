package com.wechatui.utils;

/**
 * @author law
 * @create 2022-05-2022/5/21 16:24
 */
import com.wechatui.base.TestCaseBase;
import com.wechatui.tests.member.ContactsPageTest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

/**
 * 获取项目的根路径
 * 在windows和linux系统下均可正常使用
 */
public class PathUtil {

    private static final Logger logger = LoggerFactory.getLogger(PathUtil.class);

    public final static String classPath;

    /**
     * 获取的是classpath路径，适用于读取resources下资源
     */
    static {
        classPath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
    }

    /**
     * 获取根目录
     */
    public static String getRootPath() {
        return RootPath("");
    }

    /**
     * 获取根路径，并自定义追加路径
     */
    public static String getRootPath(String plus) {
        return RootPath("/" + plus);
    }

    /**
     * 获取根路径，并去掉指定层数
     */
    public static String getRootPath(String str, int back) {
        for (int i = 0; i < back; i++) {
            str = str.substring(0, str.lastIndexOf(File.separator));
        }
        return str + File.separator;
    }

    private static String RootPath(String u_path) {
        String rootPath = "";
        //windows下
        if ("\\".equals(File.separator)) {
            //System.out.println(classPath);
            rootPath = classPath + u_path;
            rootPath = rootPath.replaceAll("/", "\\\\");
            if (rootPath.substring(0, 1).equals("\\")) {
                rootPath = rootPath.substring(1);
            }
        }
        //linux下
        if ("/".equals(File.separator)) {
            //System.out.println(classPath);
            rootPath = classPath + u_path;
            rootPath = rootPath.replaceAll("\\\\", "/");
        }
        return rootPath;
    }

    /*
     * 该函数用于查找参数化测试方法对应的测试数据文件
     * 项目结构上，要求tests文件夹下的测试文件和testdata/qa文件夹下的测试数据文件路径一致，测试数据文件名和测试方法文件名一致，
     * 如/tests/module/bbTest.java中有一个xx测试方法，则对应的测试文件为/testdata/qa/module/xx.yaml
     * 且由于使用Thread.currentThread().getStackTrace()[2].getMethodName()获取方法名，所以要求@ParameterizedTest()中指定的方法名要与测试方法名相同（此时可省略）
     * 下面程序取模块名时的方式决定了测试case必须放在tests文件夹下
     * 获取className时使用的是getStackTrace()[2]也就是说取得的classname是调用getTestDataFilePath()方法所在的类的类名
     * 当采用上面的文件结构约定时，pages文件夹中也应该存在一致的结构/pages/module/bb.java，但这个不是强制的。
     * 目前暂时采用上面的文件结构约定，若测试数据文件管理不采用这种规则，也可使用File.listFiles("/testdata/qa")查找测试方法aa对应的在/testdata/qa中的aa.yaml文件
     *
     * 如果我们想要获取当前类的类名，那么可以使用如下代码：
     * String className = this.getClass().getName();// 这是完整的类路径名
     * String simpleClassName = this.getClass().getSimpleName();// 仅仅是类名
     * 如果我们想要在某个方法内获取到该方法的方法名，可以使用如下代码：
     * String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
     */
    public static String getTestDataFilePath(String fileName){
        //StringBuffer 类型的字符串是线程安全的，StringBuilder 类型的字符串是线程不安全的。
        //生成规则如下：/ 加 test_data 加 命令行参数传入的测试环境字符串 加 测试类所在模块名 加 测试方法名 加 .yaml
        StringBuffer sb = new StringBuffer();
        sb.append("/");
        sb.append("testdata");
        sb.append("/");
        sb.append(getEvn());
        sb.append("/");
        String className = Thread.currentThread().getStackTrace()[2].getClassName();
        String  moduelName = "";
        String[] mArr = className.replace(".", "/").replaceFirst(".*tests/", "").split("/");  //测试用例必须放在tests目录下

        for (int i = 0; i < mArr.length-1; i++) {
            moduelName=moduelName+mArr[i]+"/";
        }
        //String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
        sb.append(moduelName);
        sb.append("/");
        //sb.append(methodName);
        //sb.append(".yaml");
        sb.append(fileName);

        String testDataFilePath = sb.toString();
        String msg = "对应的测试文件为"+testDataFilePath;
        logger.info(msg);
        /*  下面的逻辑放在调用方法中处理了
        //生成测试数据文件路径后还应该判断下该文件是否存在，不存在的话需给提示
        File file = new File(testDataFilePath);
        if (!file.exists()){
            logger.error(msg+"不存在！！");
            //throw new RuntimeException(msg+"不存在！！");  //经验证，此处抛出异常不会影响其他用例运行，但决定放在调用方法中处理
        }
         */
        return testDataFilePath;
    }
    public static String getEvn(){
        String env = System.getProperty("env");

        if (env != null && ("qa".equalsIgnoreCase(env) || "pro".equalsIgnoreCase(env) || "uat".equalsIgnoreCase(env)))
            return env.toLowerCase();
        else{
            return "qa";
        }
    }
}

/*
Java中getResourceAsStream的用法：https://www.cnblogs.com/macwhirr/p/8116583.html
 */