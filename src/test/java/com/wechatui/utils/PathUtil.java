package com.wechatui.utils;

/**
 * @author law
 * @create 2022-05-2022/5/21 16:24
 */
import java.io.File;

/**
 * 获取项目的根路径
 * 在windows和linux系统下均可正常使用
 */
public class PathUtil {

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

}

/*
Java中getResourceAsStream的用法：https://www.cnblogs.com/macwhirr/p/8116583.html
 */