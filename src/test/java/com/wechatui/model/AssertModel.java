package com.wechatui.model;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author luo
 * @create 2022/4/18 下午11:46
 */
public class AssertModel {
    private HashMap<String,ArrayList<String>> actual;
    private String matcher;
    private String reason;
    private Object expect;

    public HashMap<String, ArrayList<String>> getActual() {
        return actual;
    }

    public void setActual(HashMap<String, ArrayList<String>> actual) {
        this.actual = actual;
    }


    public String getMatcher() {
        return matcher;
    }

    public void setMatcher(String matcher) {
        this.matcher = matcher;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Object getExpect() {
        return expect;
    }

    public void setExpect(Object expect) {
        this.expect = expect;
    }
}
