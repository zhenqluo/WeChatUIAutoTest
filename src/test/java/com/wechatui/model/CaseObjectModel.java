package com.wechatui.model;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author luo
 * @create 2022/4/18 下午11:14
 * 用于把测试用例的YAML数据反序列化为该对象
 */
public class CaseObjectModel {

    private ArrayList<CaseDataObjectModel> data;

    public ArrayList<CaseDataObjectModel> getData() {
        return data;
    }

    public void setData(ArrayList<CaseDataObjectModel> data) {
        this.data = data;
    }
}
