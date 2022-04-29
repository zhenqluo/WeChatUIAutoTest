package com.wechatui.api;

import com.jayway.jsonpath.JsonPath;
import com.wechatui.utils.FakerUtils;
import com.wechatui.utils.LogService;
import io.restassured.response.Response;
import org.slf4j.Logger;

import java.util.HashMap;

import static io.restassured.RestAssured.given;

/**
 * @author law
 * @create 2022-04-2022/4/27 21:23
 */
public class MemberManage {

    private static final Logger logger = LogService.getInstance(MemberManage.class).getLogger();
    public MemberManage(){ }

    public HashMap<String,Object> addMember(){
        HashMap<String,Object> memberInfo = new HashMap<>();
        int errcode = 9999;
        do{
            memberInfo.clear();
            memberInfo.put("userid", FakerUtils.getRandomString(6));
            memberInfo.put("name",FakerUtils.getRandomString(6));
            memberInfo.put("alias",FakerUtils.getRandomString(6));
            memberInfo.put("mobile",FakerUtils.getPhoneNumber());
            memberInfo.put("biz_mail",FakerUtils.getRandomStringWithSuffix(6,"_mail@gqjk3.wecom.work"));
            memberInfo.put("to_invite",false);
            memberInfo.put("department",1);

            String response=given().
                    contentType("application/json").
                    body(memberInfo).
            when().
                    post("https://qyapi.weixin.qq.com/cgi-bin/user/create?access_token="+CommonOpr.getAccessToken()).
            then().extract().response().asString();
            errcode = JsonPath.read(response,"$.errcode");  //注意这里返回的是int类型而不是String类型
        }while (errcode != 0);
        logger.info(memberInfo.toString());
        return memberInfo;
    }
}
