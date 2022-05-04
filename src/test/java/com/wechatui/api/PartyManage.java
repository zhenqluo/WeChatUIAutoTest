package com.wechatui.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wechatui.model.ApiResponseModel;
import com.wechatui.utils.LogService;
import org.slf4j.Logger;

import java.util.HashMap;

import static io.restassured.RestAssured.given;

/**
 * @author luo
 * @create 2022/5/4 下午7:34
 */
public class PartyManage {
    private static final Logger logger = LogService.getInstance(PartyManage.class).getLogger();
    ObjectMapper objectMapper = new ObjectMapper();

    public ApiResponseModel getPartyList(){
        String listURL = "https://qyapi.weixin.qq.com/cgi-bin/department/list?access_token="+CommonOpr.getAccessToken();
        String res = given().get(listURL).then().log().all().extract().response().asString();//response().asString和body().asString是一样的

        ApiResponseModel resModel = null;
        try {
            resModel = objectMapper.readValue(res, ApiResponseModel.class);
        }catch (Exception ex){
            LogService.getInstance(PartyManage.class).logException(ex);
        }

        return resModel;
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

        ApiResponseModel resModel = null;
        try {
            resModel = objectMapper.readValue(res, ApiResponseModel.class);
        }catch (Exception ex){
            LogService.getInstance(PartyManage.class).logException(ex);
        }

        return resModel;
    }
}
