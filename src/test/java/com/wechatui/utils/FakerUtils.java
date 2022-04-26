package com.wechatui.utils;

import java.util.Random;

/**
 * @author law
 * @create 2022-04-2022/4/24 23:19
 * 说明：提供假数据
 * 生成随机字符串的三种方式：https://blog.csdn.net/qq_33443020/article/details/109646372
 */
public class FakerUtils {
    //生成指定长度范围的字符串
    public static String getRandomString(String lenStr){
        Integer length=new Integer(lenStr.trim());
        Random random=new Random();
        StringBuffer sb=new StringBuffer();
        for(int i=0;i<length;i++){
            int number=random.nextInt(3);
            long result=0;
            switch(number){
                case 0:
                    result=Math.round(Math.random()*25+65);
                    sb.append(String.valueOf((char)result));
                    break;
                case 1:
                    result=Math.round(Math.random()*25+97);
                    sb.append(String.valueOf((char)result));
                    break;
                case 2:
                    sb.append(String.valueOf(new Random().nextInt(10)));
                    break;
            }
        }
        return sb.toString();
    }
    //生成指定长度范围的字符串+前后缀
    public static String getRandomStringWithFix(String lenStr,String prefix,String suffix){
        String str = getRandomString(lenStr);
        return prefix+str+suffix;
    }
    //生成指定长度范围的字符串+后缀
    public static String getRandomStringWithSuffix(String lenStr,String suffix){
        String str = getRandomString(lenStr);
        return str+suffix;
    }
    //使用时间戳生成随机字符串

    //使用线程名+时间戳生成随机字符串（多线程下的随机数生成）
    //生成随机手机号
    public static String getPhoneNumber(){
        StringBuffer sb=new StringBuffer();
        for (int i = 0; i < 8; i++) {
            sb.append(String.valueOf(new Random().nextInt(10)));
        }
        return "156"+sb.toString();
    }
    //生成随机邮箱
    //生成随机身份证

}
