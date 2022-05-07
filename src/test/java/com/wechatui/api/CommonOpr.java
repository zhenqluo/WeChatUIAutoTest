package com.wechatui.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.wechatui.model.ApiResponseModel;
import com.wechatui.utils.LogService;
import org.slf4j.Logger;

import static io.restassured.RestAssured.given;

/**
 * @author law
 * @create 2022-04-2022/4/28 22:26
 */
public class CommonOpr {
    private static final Logger logger = LogService.getInstance(CommonOpr.class).getLogger();
    private static String accessToken = null;
    private static ObjectMapper objectMapper = new ObjectMapper();

    private static void gettoken(){
        String corpid = "wwaac24df3e4ed9d6c";
        String corpsecret = "npvQpdfmnETDu7NIq2nFROJnrvNMe06FwLl-AenAXaY";
        String response=given().
                when().get("https://qyapi.weixin.qq.com/cgi-bin/gettoken?corpid="+corpid+"&corpsecret="+corpsecret).
                then().
                log().body().extract().response().asString();//注意asString不要写成toString，一个对象调用toString()方法意味着什么你应该很清楚

        accessToken = JsonPath.read(response,"$.access_token");
        logger.info("获取access_token：{}",accessToken);
    }

    public static String getAccessToken() {
        if (accessToken == null){
            gettoken();
        }
        return accessToken;
    }
    public static ApiResponseModel toApiResModel(String res){
        ApiResponseModel resModel = null;
        try {
            resModel = objectMapper.readValue(res, ApiResponseModel.class);
        }catch (Exception ex){
            LogService.getInstance(PartyManage.class).logException(ex);
        }

        return resModel;
    }

}
