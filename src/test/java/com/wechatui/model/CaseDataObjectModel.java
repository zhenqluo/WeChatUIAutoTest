package com.wechatui.model;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author law
 * @create 2022-04-2022/4/19 11:39
 */
public class CaseDataObjectModel {
    private HashMap<String,Object> parameters;
    private ArrayList<AssertModel> asserts;
    private String caseTitle;

    public String getCaseTitle() {
        return caseTitle;
    }

    public void setCaseTitle(String caseTitle) {
        this.caseTitle = caseTitle;
    }

    public HashMap<String, Object> getParameters() {
        return parameters;
    }

    public void setParameters(HashMap<String, Object> parameters) {
        this.parameters = parameters;
    }

    public ArrayList<AssertModel> getAsserts() {
        return asserts;
    }

    public void setAsserts(ArrayList<AssertModel> asserts) {
        this.asserts = asserts;
    }
}
