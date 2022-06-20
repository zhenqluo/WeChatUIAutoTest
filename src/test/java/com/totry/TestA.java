package com.totry;

/**
 * @author law
 * @create 2022-06-2022/6/16 13:50
 */
/*
如果我们想要获取当前类的类名，那么可以使用如下代码：
String className = this.getClass().getName();// 这是完整的类路径名
String simpleClassName = this.getClass().getSimpleName();// 仅仅是类名
如果我们想要在某个方法内获取到该方法的方法名，可以使用如下代码：
String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
 */

class TestB{
    public static void readyaml(){
        System.out.println("readyaml");
        String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
        String className = Thread.currentThread().getStackTrace()[2].getClassName();
        String str = "com/echatui/tests/ContactsPageTest";
        String  moduelName = "";
        String[] mArr = str.replace(".", "/").replaceFirst(".*tests/", "").split("/");

        for (int i = 0; i < mArr.length-1; i++) {
            moduelName=moduelName+mArr[i]+"/";
        }


        System.out.println(moduelName);
        System.out.println(methodName);
        System.out.println(className);
    }
}
public class TestA {

    public void hello() {
        System.out.println("hello world");
        String className = this.getClass().getName();
        String simpleClassName = this.getClass().getSimpleName();
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        // hello world
        //className: com.totry.TestA, simpleClassName: TestA, methodName: hello
        System.out.println("className: " + className + ", simpleClassName: " + simpleClassName + ", methodName: " + methodName);
        TestB.readyaml();
    }

    public static void main(String[] args) {
        new TestA().hello();
    }
}

