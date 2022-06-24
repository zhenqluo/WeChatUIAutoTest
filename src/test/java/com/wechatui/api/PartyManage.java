package com.wechatui.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wechatui.model.ApiResponseModel;
import com.wechatui.utils.FakerUtils;
import com.wechatui.utils.LogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;

import static io.restassured.RestAssured.given;

/**
 * @author luo
 * @create 2022/5/4 下午7:34
 */
public class PartyManage {
    private static final Logger logger = LoggerFactory.getLogger(PartyManage.class);
    ObjectMapper objectMapper = new ObjectMapper();

    public ApiResponseModel getPartyList(){
        String listURL = "https://qyapi.weixin.qq.com/cgi-bin/department/list?access_token="+CommonOpr.getAccessToken();
        String res = given().get(listURL).then().log().all().extract().response().asString();//response().asString和body().asString是一样的
        logger.info("调用接口获取部门列表");
        return CommonOpr.toApiResModel(res);
    }
    public ApiResponseModel createParty(HashMap<String,Object> partyInfo){
        String createURL = "https://qyapi.weixin.qq.com/cgi-bin/department/create?access_token="+CommonOpr.getAccessToken();
        String res = given().
                contentType("application/json").
                body(partyInfo).
        when().
                post(createURL).
        then().
                extract().response().asString();
        ApiResponseModel apiResponseModel = CommonOpr.toApiResModel(res);
        apiResponseModel.setReqData(partyInfo);
        logger.info("调用接口创建部门{},结果为{}",partyInfo.get("name"),apiResponseModel.getErrmsg());
        return apiResponseModel;
    }
    //createParty重载方法，name为部门名称，parentid为父部门id
    public ApiResponseModel createParty(String name,int parentid){
        HashMap<String,Object> partyInfo = new HashMap<>();
        partyInfo.put("name",name);
        partyInfo.put("parentid",parentid);
        return createParty(partyInfo);
    }
    //createParty重载方法，parentid为父部门id，该方法用于在指定部门下生成一个名字为随机的子部门
    public ApiResponseModel createParty(int parentid){
        HashMap<String,Object> partyInfo = new HashMap<>();
        partyInfo.put("name", FakerUtils.getRandomString(6));
        partyInfo.put("parentid",parentid);
        return createParty(partyInfo);
    }
    public ApiResponseModel deleteParty(int id){
        String deleteURL = "https://qyapi.weixin.qq.com/cgi-bin/department/delete?access_token="+CommonOpr.getAccessToken();
        String res = given().
                param("id",id).
        when().
                get(deleteURL).
        then().extract().response().asString();
        logger.info("调用接口删除部门id:{}，结果:{}",id,res);
        return CommonOpr.toApiResModel(res);
    }
}
