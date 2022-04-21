package com.wechatui.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author luo
 * @create 2022/4/18 下午11:14
 * 用于把测试用例的YAML数据反序列化为该对象
 */
public class CaseObjectModel {

    private ArrayList<CaseDataObjectModel> data;
    private ArrayList<CaseObjectModel> testCaseList = new ArrayList<>();
    private int index = 0;

    public ArrayList<CaseDataObjectModel> getData() {
        return data;
    }

    public void setData(ArrayList<CaseDataObjectModel> data) {
        this.data = data;
    }

    public ArrayList<CaseObjectModel> getTestCaseList() {
        return testCaseList;
    }

    public void setTestCaseList(ArrayList<CaseObjectModel> testCaseList) {
        this.testCaseList = testCaseList;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    /**
     * 测试用例裂变，基于数据自动生成多份测试用例
     * @return
     */
    public List<CaseObjectModel> testcaseGenerate(){
        for (int i = 0; i < data.size(); i++) {
            CaseObjectModel newTestCaseObj = new CaseObjectModel();
            newTestCaseObj.data=this.data;
            newTestCaseObj.index=i;
            testCaseList.add(newTestCaseObj);
        }
        return testCaseList;
    }
}
