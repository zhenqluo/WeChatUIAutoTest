package com.wechatui.model;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author luo
 * @create 2022/5/4 下午8:32
 * 说明：该类用于反序列化微信接口请求返回的json数据、存放请求数据
 */
public class ApiResponseModel {
    public int errcode;
    public String errmsg;
    public int id;
    public HashMap<String,Object> reqData; //用于存放请求数据，如PartyManage类的createParty(HashMap<String,Object> partyInfo)方法

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public ArrayList<HashMap<String,Object>> department;

    public int getErrcode() {
        return errcode;
    }

    public void setErrcode(int errcode) {
        this.errcode = errcode;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }

    public ArrayList<HashMap<String, Object>> getDepartment() {
        return department;
    }

    public void setDepartment(ArrayList<HashMap<String, Object>> department) {
        this.department = department;
    }

    public HashMap<String, Object> getReqData() {
        return reqData;
    }

    public void setReqData(HashMap<String, Object> reqData) {
        this.reqData = reqData;
    }

    @Override
    public String toString() {
        return "ApiResponseModel{" +
                "errcode=" + errcode +
                ", errmsg='" + errmsg + '\'' +
                ", id=" + id +
                '}';
    }
}
