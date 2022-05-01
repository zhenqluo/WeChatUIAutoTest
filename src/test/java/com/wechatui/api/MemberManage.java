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

    //调用接口创建成员
    public HashMap<String,Object> addMember(HashMap<String,Object> memberInfo){
        String addMemberURL = "https://qyapi.weixin.qq.com/cgi-bin/user/create?access_token="+CommonOpr.getAccessToken();
        if (memberInfo == null){
            int errcode = 9999;
            do{
                if (memberInfo != null) memberInfo.clear();
                memberInfo=getRamdomMemberInfo();

                String response=given().
                        contentType("application/json").
                        body(memberInfo).
                when().
                        post(addMemberURL).
                then().extract().response().asString();
                errcode = JsonPath.read(response,"$.errcode");  //注意这里返回的是int类型而不是String类型
            }while (errcode != 0);
        }else {
            given().
                    contentType("application/json").
                    body(memberInfo).
            when().
                    post(addMemberURL);
        }

        logger.info("调用接口创建成员：{}",memberInfo.toString());
        return memberInfo;
    }
    public HashMap<String,Object> getRamdomMemberInfo(){
        HashMap<String,Object> memberInfo = new HashMap<>();
        memberInfo.put("userid", FakerUtils.getRandomString(6));
        memberInfo.put("name",FakerUtils.getRandomString(6));
        memberInfo.put("alias",FakerUtils.getRandomString(6));
        memberInfo.put("mobile",FakerUtils.getPhoneNumber());
        memberInfo.put("biz_mail",FakerUtils.getRandomStringWithSuffix(6,"_mail@gqjk3.wecom.work"));
        memberInfo.put("to_invite",false);
        memberInfo.put("department",1);
        return memberInfo;
    }
    //调用接口删除成员
    public void deleteMember(String userid){
        logger.info("调用接口删除成员userid:{}",userid);
        given().get("https://qyapi.weixin.qq.com/cgi-bin/user/delete?access_token="+CommonOpr.getAccessToken()+"&userid="+userid);
    }
}
