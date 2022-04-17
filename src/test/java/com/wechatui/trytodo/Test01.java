package com.wechatui.trytodo;

import java.lang.reflect.Type;
import java.util.HashMap;

/**
 * @author luo
 * @create 2022/4/17 上午9:09
 */
public class Test01 {
    public static void main(String[] args) {
        HashMap<String ,Object> map = new HashMap<>();
        map.put("a",1);
        map.put("b","b");
        map.put("c",null);
        if (map.get("d") == null){
            System.out.println("is null");
        }
    }
}
