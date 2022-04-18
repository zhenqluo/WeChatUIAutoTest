package com.wechatui.model;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author luo
 * @create 2022/4/18 下午11:14
 * 用于把测试用例的YAML数据反序列化为该对象
 */
public class CaseObjectModel {
    private HashMap<String,Object> parameters;
    private ArrayList<AssertModel> asserts;
}
